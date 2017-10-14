package com.choice.wechat.web.controller.game;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.choice.test.domain.Card;
import com.choice.test.service.CardSearch;
import com.choice.test.utils.Commons;
import com.choice.wechat.common.AccessToken;
import com.choice.wechat.common.WeChatUser;
import com.choice.wechat.constants.activity.ActivityConstants;
import com.choice.wechat.domain.bookDesk.Company;
import com.choice.wechat.domain.game.DrawCount;
import com.choice.wechat.domain.game.Gift;
import com.choice.wechat.domain.game.SN;
import com.choice.wechat.persistence.game.GameMapper;
import com.choice.wechat.service.game.IGameService;
import com.choice.wechat.util.WeChatUtil;
import com.choice.wxc.util.LogUtil;
import com.wxap.util.MD5Util;

@Controller
@RequestMapping(value="activity")
public class ActivityController{
	@Autowired
	public GameMapper gameMapper;
	
	@Autowired
	public IGameService gameService;
	
	@RequestMapping(value="gotoActivity")
	public ModelAndView gotoActivity(HttpSession session,String code,String pk_group,ModelMap modelMap){
		// 获取用户信息
		getUserInfo(session, code, pk_group, modelMap,"lucky_draw");
		
		List<Gift> list = gameMapper.getGift("lucky_draw",-1);
		modelMap.put("gifts", list);//奖品
		
		modelMap.put("pk_group", pk_group);
		ModelAndView mav = new ModelAndView("../activity/index",modelMap);
		return mav;
	}
	
	/**
	 * 获取用户信息
	 * @param session
	 * @param code
	 * @param pk_group
	 * @param modelMap
	 * @return
	 */
	public Card getUserInfo(HttpSession session, String code, String pk_group, ModelMap modelMap,String gameType) {
		Card card = null;
		String openid = "";
		//根据pk_group，获取配置信息
		Company company = WeChatUtil.getCompanyInfo(pk_group);
		String appId = Commons.appId;
		String secret = Commons.secret;
		if(null != company) {
			if (null != company.getAppId() && !"".equals(company.getAppId())) {
				appId = company.getAppId();
			}
			if (null != company.getSecret() && !"".equals(company.getSecret())) {
				secret = company.getSecret();
			}
		}
		WeChatUser user = (WeChatUser) session.getAttribute("WeChatUser");
		try{
			if(user == null){//返回按钮，已取得用户授权的情况
				AccessToken token = WeChatUtil.getAccessToken(appId, secret);
				//通过网页授权方式获取openID
				String oauth2[] = WeChatUtil.getOAuth2(appId, secret, code);
				//获取用户基本信息
				user = WeChatUtil.getWeChatUser(oauth2[0],token.getToken());
				if(StringUtils.hasText(user.getOpenid())){
					session.setAttribute("WeChatUser", user);
				}
			}
			modelMap.put("openid", user.getOpenid());
			
			String clientID = "";//会员号
			/***20150625 如果Unionid不为空，则用他取值***/
			List<Card> listCard = null;
			if(StringUtils.hasText(user.getUnionid())){
				listCard = CardSearch.listCard(user.getUnionid());
				openid = user.getUnionid();
			}else{
				listCard = CardSearch.listCard(user.getOpenid());
				openid = user.getOpenid();
			}
			
			if (listCard != null && listCard.size() > 0) {
				clientID = listCard.get(0).getCardNo();
				card = listCard.get(0);
			}
			modelMap.put("clientID", clientID);
			
			/*********查询已抽奖次数**********/
			List<DrawCount> list = gameMapper.getDrawCount(openid, gameType);
			if(list==null || list.size()<=0){
				modelMap.put("drawCount", "0");
			}else{
				DrawCount draw = list.get(0);
				modelMap.put("drawCount", draw.getNowCount());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return card;
	}
	
	@RequestMapping(value="running")
	@ResponseBody
	public Map<String,String> running(HttpServletRequest req, HttpServletResponse resp){
		Map<String,String> map = new HashMap<String,String>();
		try{
			String openid = req.getParameter("openid");
			int choose = 0;
			if(StringUtils.hasText(openid)){
				/*********查询已抽奖次数**********/
				List<DrawCount> list = gameMapper.getDrawCount(openid, "lucky_draw");
				if(list==null || list.size()<=0){
					gameMapper.insertDrawCount(openid, "lucky_draw");
				}else{
					DrawCount draw = list.get(0);
					if(draw.getNowCount()>=3){//已抽了3次
						map.put("error","invalid");
						return map;
					}
					gameMapper.updateDrawCount(openid, "lucky_draw");
				}
				/*********查询是否已中奖**********/
				List<SN> list_sn = gameMapper.getSN("",openid,"lucky_draw");
				if(list_sn==null || list_sn.size()<=0){//没有中过奖
					choose = new Activity().activity();
					if(choose > 0){
						map.put("success","1");
						map.put("prizetype",choose+"");
						String sn = MD5Util.MD5(new StringBuilder(openid).append("lucky_draw").append(choose).toString());
						map.put("sn",sn);
						/*********记录中奖信息**********/
						SN object_sn = new SN();
						object_sn.setGameType("lucky_draw");
						object_sn.setSn(sn);
						object_sn.setOpenid(openid);
						object_sn.setLucky(choose+"");
						gameMapper.insertSN(object_sn);
					}
				}else{
					map.put("error","getsn");
					map.put("sn",list_sn.get(0).getSn());
				}
				return map;
			}else{
				map.put("sn","逗你玩");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return map;
	}
	
	@RequestMapping(value="setTel")
	@ResponseBody
	public Map<String,Boolean> setTel(HttpServletRequest req, HttpServletResponse resp){
		Map<String,Boolean> map = new HashMap<String,Boolean>();
		try{
			/***********赠送奖品*************/
			String snCode = req.getParameter("code");
			String clientID = req.getParameter("clientID");
			String openid = req.getParameter("openid");
			String pk_group = req.getParameter("pk_group");
			String tel = req.getParameter("tel");
			try{
				
				String url = req.getRequestURL().toString();
				if(clientID==null || "".equals(clientID)){
					Card card = new Card();
					WeChatUser user = (WeChatUser) req.getSession().getAttribute("WeChatUser");
					if(user==null){
						card.setName("抽奖");
						//card.setOpenid(openid);
					}else{
						card.setName(user.getNickname()==null?"抽奖":user.getNickname());
						//card.setOpenid(StringUtils.hasText(user.getUnionid())?user.getUnionid():openid);
					}
					card.setChlb("会员");
					card.setTele(tel);
					
					clientID = CardSearch.addCardRed(card);//直接新增
					/*
					//根据手机号查询会员
					List<Card> listCardFromTele = CardSearch.listWXCard(tel);
					//如果查询到就更新以前的会员信息,执行绑定操作
					if(listCardFromTele != null && listCardFromTele.size()>0){
						card.setCardNo(listCardFromTele.get(0).getCardNo());
						clientID = CardSearch.addWXCard(card);
					}else{
						clientID = CardSearch.addCardRed(card);
					}
					*/
				}
				
				gameService.giftAction(openid,clientID,"lucky_draw",snCode,url,pk_group);
			}catch(Exception e){
				e.printStackTrace();
				map.put("success",false);
			}
			map.put("success",true);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return map;
	}
	
	@RequestMapping(value="egg")
	@ResponseBody
	public Map<String,String> egg(HttpServletRequest req, HttpServletResponse resp){
		Map<String,String> map = new HashMap<String,String>();
		try{
			int choose = new Activity().activity();
			if(choose > 0){
				map.put("msg","1");
				map.put("prize","一等奖");
			}else{
				map.put("sn","逗你玩");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return map;
	}
	
	private class Activity{
		private int activity(){
			List<Gift> list = gameMapper.getGift("lucky_draw",-1);
			int total = total(list);
			
			int randomNumber = (int) (Math.random()*total);
			LogUtil.writeToTxt(LogUtil.BUSINESS, "@@@@@@@@随机数： " + randomNumber);
	        int priority = 0;
	        for (Gift g : list) {
	        	for(int i=0;i<g.getUsable();i++){
	        		priority += g.getPriority();
	        		if (priority >= randomNumber) {
	        			// 从奖品库移出奖品
	        			int count = gameMapper.updateGiftQuantity("lucky_draw", g.getLucky());
	        			if(count<=0){//更新剩余可用数量更新了0行
	        				return 0;
	        			}
	        			return g.getLucky();
	        		}
	        	}
	        }
	        // 抽奖次数多于奖品时，没有奖品
	        return 0;
		}
		
		//计算总概率
		private int total(List<Gift> list){
			int result = 150;//默认不中奖权重为150
			if(list!=null && list.size()>0){
				for (Gift g : list) {
					result += g.getUsable()*g.getPriority();
					LogUtil.writeToTxt(LogUtil.BUSINESS, "@@@@@@@@usable:" + g.getUsable() + "; priority:" + g.getPriority() + "; result:" + result);
				}
			}
			return result;
		}
		
		/* 
		public static void main(String args[]){
			System.out.println(activity());
		}
		*/
	}
	
	/**
	 * 优惠券分享   gameType:shareCoupon
	 * @param session
	 * @param code
	 * @param pk_group
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value="couponActivity")
	public ModelAndView gotoCouponActivity(HttpSession session, String code, String pk_group, ModelMap modelMap){
		// 获取用户信息
		Card card = getUserInfo(session, code, pk_group, modelMap,"lucky_draw");
		if(null != card) {
			modelMap.put("haveRegister", "Y");
			modelMap.put("card", card);
		}
		
		// 获取用户领取次数
		String openid = (String)modelMap.get("openid");
		List<DrawCount> listDrawCount = gameMapper.getDrawCount(openid, ActivityConstants.GAME_TYPE_SHARECOUPON);
		if(null != listDrawCount && !listDrawCount.isEmpty()) {
			modelMap.put("haveGot", "Y");
		}
		
		// 获取活动信息
		List<Gift> listGift = gameMapper.getGift(ActivityConstants.GAME_TYPE_SHARECOUPON, 0);
		if(null != listGift && !listGift.isEmpty()) {
			modelMap.put("activityInfo", listGift.get(0));
		}
		
		return new ModelAndView(ActivityConstants.COUPON_ACTIVITY, modelMap);
	}

	@RequestMapping(value="gotoGetCoupon")
	public ModelAndView gotoGetCoupon(HttpServletRequest req,HttpSession session,String code,String pk_group,ModelMap modelMap){
		// 获取用户信息
		Card card = getUserInfo(session, code, pk_group, modelMap,"get_coupon");
		//获取用户的openid
		String openid = (String)modelMap.get("openid");
		
		String url = req.getRequestURL().toString();
		//调用领券活动的接口
		String res= gameService.getCouponAction(card,openid,url,pk_group);
		modelMap.put("res", res);
		return  new ModelAndView("activity/getCoupon",modelMap);
	}
	
	
}
