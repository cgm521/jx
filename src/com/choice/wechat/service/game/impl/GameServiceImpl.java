package com.choice.wechat.service.game.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.choice.test.domain.Card;
import com.choice.test.service.CardSearch;
import com.choice.test.utils.CodeHelper;
import com.choice.test.utils.Commons;
import com.choice.test.utils.DateFormat;
import com.choice.webService.CRMWebservice;
import com.choice.wechat.domain.bookDesk.Company;
import com.choice.wechat.domain.game.Actm;
import com.choice.wechat.domain.game.DrawCount;
import com.choice.wechat.domain.game.Gift;
import com.choice.wechat.domain.game.SN;
import com.choice.wechat.persistence.game.GameMapper;
import com.choice.wechat.service.game.IGameService;
import com.choice.wechat.util.LogUtil;
import com.choice.wechat.util.TemplateMsgUtil;
import com.choice.wechat.util.WeChatUtil;

@Service
public class GameServiceImpl implements IGameService{
	@Autowired
	private GameMapper gameMapper;
	
	private CRMWebservice crmService;
	
	{
		//初始化接口调用
		JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();       
	    factoryBean.setServiceClass(CRMWebservice.class); 
	    //获取配置文件中的CTF访问路径
	    String CTFUrl = Commons.CRMwebService;      
	    factoryBean.setAddress(CTFUrl); 
		crmService = (CRMWebservice)factoryBean.create();
	}
	
	public String giftAction(String openid, String clientID, String gameType, String snCode, String url, String pk_group) {
		//LogUtil.writeToTxt("share_gift", "走过giftAction+openid:"+openid+"卡号:"+clientID+"URL:"+url+"pk_group:"+pk_group);
		List<Gift> list =null;
		String gameType_chinese="";
		if("lucky_draw".equals(gameType)){
			if(snCode!=null && !"".equals(snCode)){
				List<SN> snList = gameMapper.getSN(snCode, "", "");
				SN sn = snList.get(0);
				if(sn.getDraw()<1){//未领奖
					gameMapper.updateSN(snCode, 1);//更新为已领奖
				}else{
					return "已领过奖";
				}
				int lucky = Integer.parseInt(sn.getLucky());
			    list = gameMapper.getGift(gameType, lucky);
			    gameType_chinese="大转盘游戏";
			}else{
				return "-0000";
			}
		}else if("share_gift".equals(gameType)){
			list = gameMapper.getGift(gameType, 1);	
			gameType_chinese="推荐码分享";
			//获取活动信息
			List<Gift> listGift = gameMapper.getGift(gameType,1);
			if(null != listGift && !listGift.isEmpty()) {
				//获取领券活动的剩余次数
				int usable=listGift.get(0).getUsable();
				if(usable<1){
					return "-1111";
				}
				gameMapper.updateGiftQuantity(gameType, 1);
			}
		}
		String pk_actm = list.get(0).getPk_actm();		
		boolean isVoucher = true;//送券
		List<Actm> actm_List = gameMapper.getActmDetailVoucher(pk_actm);
		if(actm_List==null || actm_List.size()<=0){
			isVoucher = false;//送积分
			actm_List = gameMapper.getActmDetailFen(pk_actm);
		}	
		String res = "";
		try {
			if(isVoucher){
				for(Actm actm : actm_List){
					String uuid = CodeHelper.createUUID();
					LogUtil.writeToTxt(LogUtil.CALLWEBSERVICE, "【"+gameType_chinese+"】调用送券接口，参数：卡号【" + clientID + "】，活动编码【" 
							+ actm.getVcode() + "】，电子券编码【" + actm.getVouchercode() + "】，数量【" 
							+ actm.getVouchernum() + "】，流水号【" + uuid + "】");
					
					res += crmService.cardInCoupon(clientID, "1000", "99999", "送券", actm.getVcode(), 
							DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss"),actm.getVouchercode(),
							actm.getVouchernum(), uuid);
					LogUtil.writeToTxt(LogUtil.CALLWEBSERVICE, "【"+gameType_chinese+"】调用送券接口，返回值【" + res + "】");
					if(res==null || res.contains("-")){
						throw new RuntimeException("CRM调用失败");
					}
				}
				Map<String,String> msgMap = new HashMap<String,String>();
				msgMap.put("templateCode", "OPENTM201048309");
				msgMap.put("openid", openid);

				StringBuffer target_url = new StringBuffer(url);
				target_url = target_url.delete(target_url.lastIndexOf("/"), target_url.length());
				target_url = target_url.delete(target_url.lastIndexOf("/"), target_url.length());
				target_url.append("/myCard/cardInfo.do?openid=").append(openid)
				.append("&pk_group=").append(pk_group);

				msgMap.put("url", target_url.toString());
				msgMap.put("first", "系统赠送您一张电子券");
				msgMap.put("keyword1", res);
				msgMap.put("keyword2", gameType_chinese+"奖品赠送成功");
				msgMap.put("keyword3", "请查看详细信息");
				msgMap.put("keyword4", "请查看详细信息");
				msgMap.put("remark", "感谢您的使用");

				// 根据pk_group，获取配置信息
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
				TemplateMsgUtil.sendTemplateMsg(WeChatUtil.getAccessToken(appId, secret).getToken(), msgMap);
			}else{
				int fenshu = 0;
				for(Actm actm : actm_List){
					fenshu += actm.getIfennum();
					String uuid = CodeHelper.createUUID();
					
					LogUtil.writeToTxt(LogUtil.CALLWEBSERVICE, "【"+gameType_chinese+"】调用送积分接口，参数：卡号【" + clientID + "】，赠送积分【" 
							+ actm.getIfennum() + "】，流水号【" + uuid + "】");
					
					res += crmService.cardInPoint(clientID, "1000", "99999", "送积分", 
							DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss"), actm.getIfennum(), uuid);
					
					LogUtil.writeToTxt(LogUtil.CALLWEBSERVICE, "【"+gameType_chinese+"】调用送积分接口，返回值【" + res + "】");
					
					if(res==null || !"1".equals(res)){
						throw new RuntimeException("CRM调用失败");
					}
				}
				List<Card> listCard = CardSearch.listCardByCardno(clientID);
				if (listCard != null && listCard.size() > 0) {
					Card card = listCard.get(0);
					Map<String,String> msgMap = new HashMap<String,String>();
					msgMap.put("templateCode", "OPENTM200898560");
					msgMap.put("openid", openid);

					StringBuffer target_url = new StringBuffer(url);
					target_url = target_url.delete(target_url.lastIndexOf("/"), target_url.length());
					target_url = target_url.delete(target_url.lastIndexOf("/"), target_url.length());
					target_url.append("/myCard/cardInfo.do?openid=").append(openid)
					.append("&pk_group=").append(pk_group);

					msgMap.put("url", target_url.toString());
					msgMap.put("first", gameType_chinese+"赠送积分已到账");
					msgMap.put("keyword1", card.getName());
					msgMap.put("keyword2", card.getCardNo());
					msgMap.put("keyword3",  fenshu+"");
					msgMap.put("keyword4", card.getTtlFen());
					msgMap.put("remark", "感谢您的使用");

					// 根据pk_group，获取配置信息
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
					TemplateMsgUtil.sendTemplateMsg(WeChatUtil.getAccessToken(appId, secret).getToken(), msgMap);
				}
			}
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("CRM调用失败");
		}
	}

	public String keywordAction(String vcode, String openid, String clientID, String url, String pk_group) {
		List<Actm> actm_List = gameMapper.getActmDetailVoucherFromVcode(vcode);
		if(actm_List==null || actm_List.size()<=0){
			return "未找到活动";
		}
		
		Actm actm =  actm_List.get(0);
		
		int actmCount = gameMapper.getWordActmCount(actm.getPk_actm());
		if(actmCount<=0){
			try{
				gameMapper.insertWordActm(actm.getPk_actm(), actm.getVcode());
			}catch(Exception e){
				//防止多线程时，同时插入失败，导致退出
			}
		}
		
		int usednum = gameMapper.updateWordActm(actm.getPk_actm(),actm.getIticketnum());//已送数量
		if(usednum<=0){//未更新成功
			return "券已送完";
		}
		
		String res = "";
		try {
			String uuid = CodeHelper.createUUID();
			
			LogUtil.writeToTxt(LogUtil.CALLWEBSERVICE, "大转盘游戏调用送券接口，参数：卡号【" + clientID + "】，活动编码【" 
					+ actm.getVcode() + "】，电子券编码【" + actm.getVouchercode() + "】，数量【" 
					+ actm.getVouchernum() + "】，流水号【" + uuid + "】");
			
			res += crmService.cardInCoupon(clientID, "1000", "99999", "送券", actm.getVcode(), 
					DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss"),actm.getVouchercode(),
					actm.getVouchernum(), uuid);
			
			LogUtil.writeToTxt(LogUtil.CALLWEBSERVICE, "大转盘游戏调用送券接口，返回值【" + res + "】");
			
			if(res==null || "".equals(res) || res.contains("-")){
				return "-0";
			}

			StringBuilder sb = new StringBuilder("keywordAction_");
			sb.append("用户卡号：").append(clientID).append("送券成功，券编号为：").append(res);
			LogUtil.writeToTxt(LogUtil.BUSINESS, sb.toString());
			Map<String,String> msgMap = new HashMap<String,String>();
			msgMap.put("templateCode", "OPENTM201048309");
			msgMap.put("openid", openid);

			StringBuffer target_url = new StringBuffer(url);
			target_url = target_url.delete(target_url.lastIndexOf("/"), target_url.length());
			target_url = target_url.delete(target_url.lastIndexOf("/"), target_url.length());
			target_url.append("/myCard/cardInfo.do?openid=").append(openid)
			.append("&pk_group=").append(pk_group);

			msgMap.put("url", target_url.toString());
			msgMap.put("first", "系统赠送您一张电子券");
			msgMap.put("keyword1", res);
			msgMap.put("keyword2", "发消息送券赠送成功");
			msgMap.put("keyword3", "请查看详细信息");
			msgMap.put("keyword4", "请查看详细信息");
			msgMap.put("remark", "感谢您的参与");

			// 根据pk_group，获取配置信息
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
			TemplateMsgUtil.sendTemplateMsg(WeChatUtil.getAccessToken(appId, secret).getToken(), msgMap);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("CRM调用失败");
		}
	}
	/*
	 * xqd
	 */
	public String getCouponAction(Card card, String openid,String url,String pk_group) {
		String res = "";
		LogUtil.writeToTxt("getCoupon", "领券。card【" + JSONObject.fromObject(card) + "】，openid【" + openid+ "】。url【" 
				+url + "】pk_group【"+pk_group+"】" + System.getProperty("line.separator"));
		if(card==null||card.getCardNo()==null){
			//还不是会员，提示去注册会员		
			res="请先注册会员";
		}else{			
			//已经是会员了
			//获取用户的领券次数
			int count=0;
			List<DrawCount> list_DrawCount = gameMapper.getDrawCount(openid, "get_coupon");
			LogUtil.writeToTxt("getCoupon", "list_DrawCount【" + list_DrawCount.size() + "】，openid【" + openid+ "】" 
					+"】count【"+count+"】" + System.getProperty("line.separator"));
			if(list_DrawCount.size()>0 ){
				count=list_DrawCount.get(0).getNowCount();
				LogUtil.writeToTxt("getCoupon", "list_DrawCount【" + list_DrawCount.get(0) + "】，openid【" + openid+ "】" 
						+"】count【"+count+"】" + System.getProperty("line.separator"));
			 }
			//获取活动信息
			List<Gift> listGift = gameMapper.getGift("get_coupon",1);
			if(null != listGift && !listGift.isEmpty()) {
				//获取领券活动的剩余次数
				int usable=listGift.get(0).getUsable();
				//获得券的名称
				String luckyName=listGift.get(0).getLuckyName();
				//每个微信号最多可以领券的次数IPERCNT
				int ipercnt=listGift.get(0).getIpercnt();
				if(usable<1){
					// 领券次数用完了，活动结束了
					res="活动结束了，敬请期待下次活动...";
			     }else{
			    	 //如果一个微信号领券次数大于规定的每个微信号最多能领券的次数			
				    if(count>=ipercnt){
						// 已经领过券了，提示不能再领了，可以去会员信息里查看领券信息
						Map<String,String> msgMap = new HashMap<String,String>();
						msgMap.put("templateCode", "OPENTM201048309");
						msgMap.put("openid", openid);
		
						StringBuffer target_url = new StringBuffer(url);
						target_url = target_url.delete(target_url.lastIndexOf("/"), target_url.length());
						target_url = target_url.delete(target_url.lastIndexOf("/"), target_url.length());
						target_url.append("/myCard/cardInfo.do?openid=").append(openid)
						.append("&pk_group=").append(pk_group);
		
						msgMap.put("url", target_url.toString());
						msgMap.put("first", "您已经领过券了！");
						msgMap.put("keyword1", "本活动只能参加"+ipercnt+"次哟");
						msgMap.put("keyword2", "《我要领券》");
						msgMap.put("keyword3", "请查看详细信息");
						msgMap.put("keyword4", "请查看详细信息");
						msgMap.put("remark", "感谢您的使用");
		
						// 根据pk_group，获取配置信息
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
						TemplateMsgUtil.sendTemplateMsg(WeChatUtil.getAccessToken(appId, secret).getToken(), msgMap);					
						res+="您已经领过券了";
						
					}else{
						// 微信号的领券次数还没到上限
						// 从奖品库（表WX_GAME_GIFT）减少可以领券的次数
	        			gameMapper.updateGiftQuantity("get_coupon", 1);
	        			//增加用户的领券次数
	        			if(count==0){
	        				gameMapper.insertDrawCount(openid, "get_coupon");
	        			}else{
	        				gameMapper.updateDrawCount(openid, "get_coupon");
	        			}
	        			//在WX_GAME_GIFT表中，查询获得活动编码
	        			List<Gift> list = gameMapper.getGift("get_coupon", 1);
	        			String pk_actm = list.get(0).getPk_actm();
	        			List<Actm> actm_List = gameMapper.getActmDetailVoucher(pk_actm);
	        			String cardNo= CardSearch.listCard(openid).get(0).getCardNo();
        			 	try{	
		        			for(Actm actm : actm_List){
								String uuid = CodeHelper.createUUID();
								LogUtil.writeToTxt(LogUtil.CALLWEBSERVICE, "《我要领券！》游戏调用送券接口，参数：卡号【" + cardNo + "】，活动编码【" 
										+ actm.getVcode() + "】，电子券编码【" + actm.getVouchercode() + "】，数量【" 
										+ actm.getVouchernum() + "】，流水号【" + uuid + "】");
								
								res += crmService.cardInCoupon(cardNo, "1000", "99999", "送券", actm.getVcode(), 
										DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss"),actm.getVouchercode(),
										actm.getVouchernum(), uuid);
								LogUtil.writeToTxt(LogUtil.CALLWEBSERVICE, "《我要领券！》游戏调用送券接口，返回值【" + res + "】");
								if(res==null || res.contains("-")){
									throw new RuntimeException("CRM调用失败");
								}
							}
		        			Map<String,String> msgMap = new HashMap<String,String>();
							msgMap.put("templateCode", "OPENTM201048309");
							msgMap.put("openid", openid);
	
							StringBuffer target_url = new StringBuffer(url);
							target_url = target_url.delete(target_url.lastIndexOf("/"), target_url.length());
							target_url = target_url.delete(target_url.lastIndexOf("/"), target_url.length());
							target_url.append("/myCard/cardInfo.do?openid=").append(openid)
							.append("&pk_group=").append(pk_group);
	
							msgMap.put("url", target_url.toString());
							msgMap.put("first", "领券成功！");
							msgMap.put("keyword1", luckyName);
							msgMap.put("keyword2", "《我要领券》");
							msgMap.put("keyword3", "请查看详细信息");
							msgMap.put("keyword4", "请查看详细信息");
							msgMap.put("remark", "感谢您的使用");
	
							// 根据pk_group，获取配置信息
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
							TemplateMsgUtil.sendTemplateMsg(WeChatUtil.getAccessToken(appId, secret).getToken(), msgMap);
		        			res="领券成功";
        			  } catch (Exception e) {
        				  e.printStackTrace();
        				  throw new RuntimeException("CRM调用失败");
        			  }	        			
				  }
			   }
			}
		}		
		return res;		

	}	
}
