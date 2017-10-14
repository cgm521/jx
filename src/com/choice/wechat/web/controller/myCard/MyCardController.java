package com.choice.wechat.web.controller.myCard;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.choice.test.domain.Card;
import com.choice.test.domain.CardRules;
import com.choice.test.domain.CardTyp;
import com.choice.test.domain.ChargeRecord;
import com.choice.test.domain.ConsumeRecord;
import com.choice.test.domain.Voucher;
import com.choice.test.service.CardSearch;
import com.choice.test.utils.CodeHelper;
import com.choice.test.utils.Commons;
import com.choice.test.utils.DateFormat;
import com.choice.test.utils.GenerateRandom;
import com.choice.test.utils.SendSMSByWxt;
import com.choice.test.utils.SendSMSByXieHe;
import com.choice.test.utils.TestSDKClient;
import com.choice.webService.CRMWebservice;
import com.choice.wechat.common.AccessToken;
import com.choice.wechat.common.WeChatUser;
import com.choice.wechat.constants.myCard.MyCardConstants;
import com.choice.wechat.domain.bookDesk.City;
import com.choice.wechat.domain.bookDesk.Company;
import com.choice.wechat.domain.bookDesk.Firm;
import com.choice.wechat.domain.bookDesk.Street;
import com.choice.wechat.domain.myCard.LabelFavorite;
import com.choice.wechat.domain.myCard.Coupon;
import com.choice.wechat.domain.myCard.MemberRight;
import com.choice.wechat.domain.myCard.NetGoodsOrders;
import com.choice.wechat.domain.myCard.NetGoodsOrderDtl;
import com.choice.wechat.persistence.bookMeal.BookMealMapper;
import com.choice.wechat.persistence.bookDesk.BookDeskMapper;
import com.choice.wechat.persistence.myCard.MyCardMapper;
import com.choice.wechat.service.game.IGameService;
import com.choice.wechat.util.LogUtil;
import com.choice.wechat.util.Sign;
import com.choice.wechat.util.WeChatUtil;
import com.choice.wechat.util.TemplateMsgUtil;
import com.jianzhou.sdk.BusinessService;

/**
 * 会员卡
 */
@Controller
@RequestMapping(value = "myCard")
public class MyCardController {

	@Autowired
	private MyCardMapper myCardMapper;
	
	@Autowired
	private BookDeskMapper bookDeskMapper;
	
	@Autowired
	private BookMealMapper bookMealMapper;
	@Autowired
	private IGameService gameService;

	private static Logger log = Logger.getLogger(MyCardController.class);

	/**
	 * 会员卡信息
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "cardInfo")
	public ModelAndView cardInfo(ModelMap modelMap, HttpServletRequest request, String code, String state, String pk_group, String openid) {
		String pagePath = "";		//存储要跳转的页面路径
		try {
			/*
			 * if (pk_group == null || "".equals(pk_group)) { return null; }
			 */
			
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
			
			//openid是否为空
			boolean hasOpenid = true;
			//判断openid是否为空，为空调用方法获取当前openid（微信id）
			String unionid = "";
			if(openid == null || "".equals(openid)){
				log.debug("获取用户OPENID开始");
				if(StringUtils.hasText(code)) {
					// 通过网页授权方式获取openID
					String oauth2[] = WeChatUtil.getOAuth2(appId, secret, code);
		
					// 获取用户基本信息
					AccessToken token = WeChatUtil.getAccessToken(appId, secret);
					WeChatUser user = WeChatUtil.getWeChatUser(oauth2[0],
							token.getToken());
					if(StringUtils.hasText(user.getOpenid())){
						request.getSession().setAttribute("WeChatUser", user);
						openid = user.getOpenid();
						unionid = user.getUnionid();
					} else {
						hasOpenid = false;
					}
				}
			}else{//openid不为空时，也获取unionid
				WeChatUser user = (WeChatUser) request.getSession().getAttribute("WeChatUser");
				if(user!=null){
					unionid = user.getUnionid();
				}else{
					// 获取用户基本信息
					AccessToken token = WeChatUtil.getAccessToken(appId, secret);
					user = WeChatUtil.getWeChatUser(openid, token.getToken());
					if(StringUtils.hasText(user.getOpenid())){
						request.getSession().setAttribute("WeChatUser", user);
					}
					unionid = user.getUnionid();
				}
			}
			
			// 如果获取不到openid，从session中取
			if(!hasOpenid) {
				WeChatUser user = (WeChatUser) request.getSession().getAttribute("WeChatUser");
				if(user!=null){
					openid = user.getOpenid();
					unionid = user.getUnionid();
				}
			}
			log.debug("获取用户OPENID结束");
			
			log.debug("获取用户会员卡信息开始");
			/***20150625 如果Unionid不为空，则用他取值***/
			List<Card> listCard = null;
			if(StringUtils.hasText(unionid)){
				listCard = CardSearch.listCard(unionid);
			}else{
				listCard = CardSearch.listCard(openid);
			}
			
			if (listCard != null && listCard.size() != 0) {
				List<CardRules> listCardRules = CardSearch.getCardRules(null);
				modelMap.put("exclusprivle", listCardRules.get(0).getExclusprivle());
				modelMap.put("cardtele", listCardRules.get(0).getCardtele());
				modelMap.put("store", listCardRules.get(0).getSTORE());
				modelMap.put("listCard", listCard);
				modelMap.put("card", listCard.get(0));
				pagePath = MyCardConstants.CARD_INFO;
			}else{
				Card card = new Card();
				
				// 如果是通过扫描门店二维码进入，查询门店信息
				String firmid = request.getParameter("firmid");
				if(null != firmid && !"".equals(firmid)) {
					// 查询门店
					List<Firm> listFirm = bookMealMapper.getFirmList(pk_group, null, firmid);
					if(null != listFirm && !listFirm.isEmpty()) {
						Firm firm = listFirm.get(0);
						//card.setFirmId(firm.getFirmCode());
						modelMap.put("firmCode", firm.getFirmCode());
					}
				}
				
//				card.setCardNo("12312312");
//				card.setTele("111111");
				modelMap.put("card", card);
				pagePath = MyCardConstants.MEMBER_REGISTER;
			}
			log.debug("获取用户会员卡信息结束");
			modelMap.put("openid", openid);
			modelMap.put("unionid", unionid==null?"":unionid);//增加unionid
			modelMap.put("pk_group", pk_group);
			
			// 获取会员卡类型列表
			List<CardTyp> cardTypList = myCardMapper.getCardTypList();
			modelMap.put("cardTypList", cardTypList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(pagePath, modelMap);
	}
	@RequestMapping(value = "cardInfo_afterRegister")
	public ModelAndView cardInfo_afterRegister(ModelMap modelMap, HttpServletRequest request, String code, String state, String pk_group, String openid){
		String pagePath = "";		//存储要跳转的页面路径
		try {
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
			
			//openid是否为空
			boolean hasOpenid = true;
			//判断openid是否为空，为空调用方法获取当前openid（微信id）
			String unionid = "";
			if(openid == null || "".equals(openid)){
				log.debug("获取用户OPENID开始");
				if(StringUtils.hasText(code)) {
					// 通过网页授权方式获取openID
					String oauth2[] = WeChatUtil.getOAuth2(appId, secret, code);
		
					// 获取用户基本信息
					AccessToken token = WeChatUtil.getAccessToken(appId, secret);
					WeChatUser user = WeChatUtil.getWeChatUser(oauth2[0],
							token.getToken());
					if(StringUtils.hasText(user.getOpenid())){
						request.getSession().setAttribute("WeChatUser", user);
						openid = user.getOpenid();
						unionid = user.getUnionid();
					} else {
						hasOpenid = false;
					}
				}
			}else{//openid不为空时，也获取unionid
				WeChatUser user = (WeChatUser) request.getSession().getAttribute("WeChatUser");
				if(user!=null){
					unionid = user.getUnionid();
				}else{
					// 获取用户基本信息
					AccessToken token = WeChatUtil.getAccessToken(appId, secret);
					user = WeChatUtil.getWeChatUser(openid, token.getToken());
					if(StringUtils.hasText(user.getOpenid())){
						request.getSession().setAttribute("WeChatUser", user);
					}
					unionid = user.getUnionid();
				}
			}
			
			// 如果获取不到openid，从session中取
			if(!hasOpenid) {
				WeChatUser user = (WeChatUser) request.getSession().getAttribute("WeChatUser");
				if(user!=null){
					openid = user.getOpenid();
					unionid = user.getUnionid();
				}
			}
			log.debug("获取用户OPENID结束");
			
			log.debug("获取用户会员卡信息开始");
			/***20150625 如果Unionid不为空，则用他取值***/
			List<Card> listCard = null;
			if(StringUtils.hasText(unionid)){
				listCard = CardSearch.listCard(unionid);
			}else{
				listCard = CardSearch.listCard(openid);
			}
			
			if (listCard != null && listCard.size() != 0) {
				List<CardRules> listCardRules = CardSearch.getCardRules(null);
				modelMap.put("exclusprivle", listCardRules.get(0).getExclusprivle());
				modelMap.put("cardtele", listCardRules.get(0).getCardtele());
				modelMap.put("store", listCardRules.get(0).getSTORE());
				modelMap.put("listCard", listCard);
				modelMap.put("card", listCard.get(0));
				pagePath = "myCard/cardInfo_afterRegister";
			}
			log.debug("获取用户会员卡信息结束");
			modelMap.put("openid", openid);
			modelMap.put("unionid", unionid==null?"":unionid);//增加unionid
			modelMap.put("pk_group", pk_group);
			
			// 获取会员卡类型列表
			List<CardTyp> cardTypList = myCardMapper.getCardTypList();
			modelMap.put("cardTypList", cardTypList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(pagePath, modelMap);
	}
	/**
	 * 绑定会员卡信息
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "addCard")
	public ModelAndView addCard(ModelMap modelMap, String pk_group,
			String openid) {
		modelMap.put("pk_group", pk_group);
		modelMap.put("openid", openid);
		return new ModelAndView(MyCardConstants.ADD_CARD, modelMap);
	}

	/**
	 * 验证卡号是否存在
	 * 
	 * @param modelMap
	 * @param card
	 * @return 0:不存在 1:存在
	 */
	@RequestMapping(value = "verifyCardExist")
	@ResponseBody
	public int verifyCardExist(ModelMap modelMap, Card card) {
		List<Card> cardList = CardSearch.listCardByCardno(card.getCardNo());

		if (null != cardList && !cardList.isEmpty()) {
			return 1;
		}
		return 0;
	}
	
	/**
	 * 老会员绑定微信号
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "saveWXCard")
	@ResponseBody
	public void saveWXCard(ModelMap modelMap, Card card) {
		//添加微信会员卡
		String cont = CardSearch.addWXCard(card);
		/*if (cont != null && !cont.equals("")) {		
			List<Card> listCard = CardSearch.listCard(card.getOpenid());
			List<CardRules> listCardRules = CardSearch.getCardRules(null);
			modelMap.put("exclusprivle", listCardRules.get(0).getExclusprivle());
			modelMap.put("cardtele", listCardRules.get(0).getCardtele());
			modelMap.put("store", listCardRules.get(0).getSTORE());
			modelMap.put("listCard", listCard);
			modelMap.put("card", listCard.get(0));
		}
		modelMap.put("openid", card.getOpenid());
		modelMap.put("pk_group", card.getPk_group());
		return new ModelAndView(MyCardConstants.CARD_INFO, modelMap);*/
	}

	/**
	 * 验证该微信号、手机号是否已经存在
	 * 
	 * @param modelMap
	 * @param card
	 * @return 0:不存在 1:存在
	 */
	@RequestMapping(value = "verifyMemberExist")
	@ResponseBody
	public List<Card> verifyMemberExist(ModelMap modelMap, Card card) {
		List<Card> listCard = CardSearch.verifyMemberExist(card);		
		return listCard;
	}
	/**
	 * 验证推荐码是否正确，是否存在
	 * @param modeMap
	 * @param card
	 * @return 
	 */
	@RequestMapping(value="verifyShareCode")
	@ResponseBody
	public List<Card> verifyShareCode(ModelMap modeMap,Card card){
		List<Card> listCard=CardSearch.listCardByShareCode(card);
		return listCard;	
	}
	/**
	 * 注册微信会员
	 * 
	 * @param modelMap
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "saveWXMember")
	public ModelAndView saveWXMember(HttpServletRequest req,ModelMap modelMap, Card card) throws UnsupportedEncodingException {
		String cont = "";
		//typ、typdes在后台查询卡类别标志WECHATSTATE=‘Y'是微信卡的 取第一条数据
		
		String vname = card.getName();
		if(vname !=null && !"".equals(vname)){//处理汉字乱码问题
			card.setName(URLDecoder.decode(vname,"UTF-8"));
//			card.setName( new String(vname.getBytes("ISO-8859-1"),"UTF-8"));
		}
		if(null != card.getTypDes() && !"".equals(card.getTypDes())) {
			card.setTypDes(URLDecoder.decode(card.getTypDes(), "UTF-8"));
		}
		 
		card.setChlb("会员");
		
/*		// 会员数据是否多品牌分开
		String cardForMultiBrand = Commons.getConfig().getProperty("cardForMultiBrand");
		List<Card> listCardFromTele;
		LogUtil.writeToTxt(LogUtil.BUSINESS, "cardForMultiBrand:" + cardForMultiBrand);
		if("Y".equals(cardForMultiBrand)) {
			//根据手机号、会员卡类型查询会员
			Card condition = new Card();
			condition.setTele(card.getTele());
			condition.setTyp(Commons.getConfig().getProperty("registerCardTyp"));
			listCardFromTele = CardSearch.verifyMemberExist(condition);
		} else {
			//根据手机号查询会员
			listCardFromTele = CardSearch.listWXCard(card.getTele(), null);
		}
		
		//如果查询到就更新以前的会员信息,执行绑定操作
		if(listCardFromTele != null && listCardFromTele.size() > 0){
			card.setCardNo(listCardFromTele.get(0).getCardNo());
			cont = CardSearch.addWXCard(card);
		}else{
			cont = CardSearch.addCardRed(card);
		}*/
		cont = CardSearch.addCardRed(card);
		//如果注册时，填入了他人的推荐码,则给推荐人送积分或送券
		if(card.getOthersShareCode()!=null&&card.getOthersShareCode()!=""){
			Card othersCard=new Card();
			othersCard.setMyselfShareCode(card.getOthersShareCode());
			//根据推荐码，查询推荐人的会员信息
			List<Card> listOthersCard=CardSearch.listCardByShareCode(othersCard);
		    //因为在注册界面，验证过推荐码一定是存在的
		    othersCard=listOthersCard.get(0);
		    String url = req.getRequestURL().toString();//给推荐人的微信客户端推送模板消息时，用到
			//调用给推荐人送积分或券的方法
			gameService.giftAction(othersCard.getOpenid(),othersCard.getCardNo(),"share_gift","",url,"");
		}		
		if (cont != null && !cont.equals("")) {
			List<Card> listCard = CardSearch.listCard(card.getOpenid());
			List<CardRules> listCardRules = CardSearch.getCardRules(null);
			modelMap.put("exclusprivle", listCardRules.get(0).getExclusprivle());
			modelMap.put("cardtele", listCardRules.get(0).getCardtele());
			modelMap.put("store", listCardRules.get(0).getSTORE());
			modelMap.put("listCard", listCard);
			if(null != listCard && listCard.size()>0){
				modelMap.put("card", listCard.get(0));
			}
		}
		modelMap.put("openid", card.getOpenid());
		modelMap.put("pk_group", card.getPk_group());
		return new ModelAndView(MyCardConstants.CARD_INFO, modelMap);
	}
	/**
	 * 查询会员积分记录
	 * @param modelMap
	 * @param card
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "showIntegration")
	public ModelAndView showIntegration(ModelMap modelMap, Card card,String type) {
		//查询剩余、获得、支出积分
		Map<String,Object> integrationRecodeSum=CardSearch.getIntegrationRecodeSum(card);
		//查询积分明细
		List<Map<String,Object>> listIntegrationRecodes=CardSearch.getIntegrationRecodes(card,type);
		modelMap.put("openid", card.getOpenid());
		modelMap.put("pk_group", card.getPk_group());
		modelMap.put("type", type);
		modelMap.put("integrationRecodeSum", integrationRecodeSum);
		modelMap.put("listFen", listIntegrationRecodes);
		return new ModelAndView(MyCardConstants.INTEGRATION_RECORDS, modelMap);
	}
	
	/**
	 * 获取电子券
	 * @param modelMap
	 * @param openid
	 * @return
	 */
	@RequestMapping(value = "myCardVoucher")
	public ModelAndView myCardVoucher(ModelMap modelMap, HttpServletRequest request, String openid, String pk_group) {
		Card card=new Card();
		card.setOpenid(openid);
		List<Voucher> listVoucher=CardSearch.getVoucher(card);
		modelMap.put("listVoucher", listVoucher);
		
		// 限制门店列表
		List<Firm> firmList = myCardMapper.getLimitFirmList(null, null);
		
		// 未使用电子券列表
		List<Voucher> noUseVoucher = new ArrayList<Voucher>();
		// 已使用电子券列表
		List<Voucher> haveUsedVoucher = new ArrayList<Voucher>();
		// 未使用电子券列表
		List<Voucher> haveExpiredVoucher = new ArrayList<Voucher>();
		
		for(Voucher voucher : listVoucher) {
			DecimalFormat df = new DecimalFormat("####0.##");
			voucher.setAmt(df.format(Double.parseDouble(voucher.getAmt())));
			
			// 限制门店
			String limitFirm = "";
			// 根据活动编码获取限制门店
			for(Firm firm : firmList) {
				String couponActmCode = voucher.getActmCode();
				String actmCode = firm.getFirmCode();
				if(couponActmCode!=null && actmCode!=null){
					if(!couponActmCode.isEmpty() && !actmCode.isEmpty() && couponActmCode.equals(actmCode)) {
						limitFirm += firm.getFirmdes() + " ";
					}
				}
			}
			voucher.setRest(limitFirm);
			
			if (Double.parseDouble(voucher.getAmt()) <= 5) {
				voucher.setPic("coupon_limit5.png");// 电子券背景图
				voucher.setFontColor("#AA5C00");// 左侧立减**元字体颜色
			} else if (Double.parseDouble(voucher.getAmt()) <= 10) {
				voucher.setPic("coupon_limit10.png");// 电子券背景图
				voucher.setFontColor("#FF4E4E");// 左侧立减**元字体颜色
			} else if (Double.parseDouble(voucher.getAmt()) <= 20) {
				voucher.setPic("coupon_limit20.png");// 电子券背景图
				voucher.setFontColor("#FF4E4E");// 左侧立减**元字体颜色
			} else {
				voucher.setPic("coupon_limit50.png");// 电子券背景图
				voucher.setFontColor("#4EAAFF");// 左侧立减**元字体颜色
			}
			
			if("2".equals(voucher.getSta())) {
				noUseVoucher.add(voucher);
			}
			if("3".equals(voucher.getSta())) {
				haveUsedVoucher.add(voucher);
			}
			if("4".equals(voucher.getSta())) {
				haveExpiredVoucher.add(voucher);
			}
		}
		
		modelMap.put("noUseVoucher", noUseVoucher);
		modelMap.put("haveUsedVoucher", haveUsedVoucher);
		modelMap.put("haveExpiredVoucher", haveExpiredVoucher);
		modelMap.put("pk_group", pk_group);
		modelMap.put("openid", openid);
		
		//重新保存微信用户信息
		WeChatUser user = (WeChatUser)request.getSession().getAttribute("WeChatUser");
		if(user != null) {
			request.getSession().setAttribute("WeChatUser", user);
		}
		
		return new ModelAndView(MyCardConstants.MY_CARD_VOUCHER, modelMap);
	}
	
	/**
	 * 查询充值记录
	 * @param modelMap
	 * @param cardNo
	 * @param openid
	 * @return
	 */
	@RequestMapping(value = "listChargeRecord")
	public ModelAndView listChargeRecord(ModelMap modelMap, String cardNo, String openid, String chargeDate, String pk_group) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		if(chargeDate == null || "".equals(chargeDate)) {
			Calendar now = Calendar.getInstance();
			chargeDate = sdf.format(now.getTime());
		}
		modelMap.put("pk_group", pk_group);
		modelMap.put("openid", openid);
		modelMap.put("cardNo", cardNo);
		modelMap.put("chargeDate", chargeDate);
		modelMap.put("headDate", chargeDate.substring(2, 4) + "年" + chargeDate.substring(5, 7) + "月");
		
		ChargeRecord condition = new ChargeRecord();
		condition.setCardno(cardNo);
		condition.setOpenid(openid);
		condition.setTim(chargeDate);
		List<ChargeRecord> listChargeRecord = myCardMapper.listChargeRecord(condition);
		
		double totalAmt = 0.0;
		for(ChargeRecord record : listChargeRecord) {
			totalAmt += Double.parseDouble(record.getRmbamt()) + Double.parseDouble(record.getGiftamt());
		}
		modelMap.put("totalAmt", WeChatUtil.formatDoubleLength(String.valueOf(totalAmt),2));
		
		List<ChargeRecord> resultList = processChargeRecord(listChargeRecord);
		modelMap.put("listChargeRecord", resultList);
		
		// 可选日期，半年内
		Calendar cal = Calendar.getInstance();
		// 先减半年，然后逐月递加。构造有序数据
		cal.add(Calendar.MONTH, -6);
		List<String> listDate = new ArrayList<String>();
		for(int i = 0; i < 6; i++) {
			cal.add(Calendar.MONTH, 1);
			listDate.add(sdf.format(cal.getTime()));
		}
		modelMap.put("listDate", listDate);
		
		return new ModelAndView(MyCardConstants.MY_CHARGE_RECORD, modelMap);
	}
	
	/**
	 * 处理充值记录，赠送金额单独作为一条记录显示出来
	 * @param listChargeRecord
	 * @return
	 */
	private List<ChargeRecord> processChargeRecord(List<ChargeRecord> listChargeRecord) {
		List<ChargeRecord> result = new ArrayList<ChargeRecord>();
		for(ChargeRecord data : listChargeRecord) {
			result.add(data);
			// 如果有赠送金额，增加一条记录
			if(null != data.getGiftamt() && !"".equals(data.getGiftamt()) && Double.parseDouble(data.getGiftamt()) > 0) {
				ChargeRecord temp = new ChargeRecord();
				temp.setCardno(data.getCardno());
				temp.setFirmdes(data.getFirmdes());
				temp.setOpenid(data.getOpenid());
				temp.setOperater(data.getOperater());
				temp.setPayment("赠送");
				temp.setRmbamt(data.getGiftamt());
				temp.setTim(data.getTim());
				result.add(temp);
			}
		}
		return result;
	}
	
	/**
	 * 查询消费记录
	 * @param modelMap
	 * @param cardNo
	 * @param openid
	 * @return
	 */
	@RequestMapping(value = "listConsumeRecord")
	public ModelAndView listConsumeRecord(ModelMap modelMap, String cardNo, String openid, String chargeDate, String pk_group) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		if(chargeDate == null || "".equals(chargeDate)) {
			Calendar now = Calendar.getInstance();
			chargeDate = sdf.format(now.getTime());
		}
		modelMap.put("pk_group", pk_group);
		modelMap.put("openid", openid);
		modelMap.put("cardNo", cardNo);
		modelMap.put("chargeDate", chargeDate);
		modelMap.put("headDate", chargeDate.substring(2, 4) + "年" + chargeDate.substring(5, 7) + "月");
		
		ConsumeRecord condition = new ConsumeRecord();
		condition.setCardno(cardNo);
		condition.setOpenid(openid);
		condition.setTim(chargeDate);
		List<ConsumeRecord> listConsumeRecord = myCardMapper.listConsumeRecord(condition);
		modelMap.put("listConsumeRecord", listConsumeRecord);
		
		double totalAmt = 0.0;
		for(ConsumeRecord record : listConsumeRecord) {
			totalAmt += Double.parseDouble(record.getAmt());
		}
		modelMap.put("totalAmt", WeChatUtil.formatDoubleLength(String.valueOf(totalAmt),2));
		
		// 可选日期，半年内
		Calendar cal = Calendar.getInstance();
		// 先减半年，然后逐月递加。构造有序数据
		cal.add(Calendar.MONTH, -6);
		List<String> listDate = new ArrayList<String>();
		for(int i = 0; i < 6; i++) {
			cal.add(Calendar.MONTH, 1);
			listDate.add(sdf.format(cal.getTime()));
		}
		modelMap.put("listDate", listDate);
		
		return new ModelAndView(MyCardConstants.MY_CONSUME_RECORD, modelMap);
	}
	
	/**
	 * 查询余额详细
	 * @param modelMap
	 * @param cardNo
	 * @param openid
	 * @return
	 */
	@RequestMapping(value = "listBalanceRecord")
	public ModelAndView listBalanceRecord(ModelMap modelMap, String cardNo, String openid, String chargeDate, String pk_group) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		if(chargeDate == null || "".equals(chargeDate)) {
			Calendar now = Calendar.getInstance();
			chargeDate = sdf.format(now.getTime());
		}
		modelMap.put("pk_group", pk_group);
		modelMap.put("openid", openid);
		modelMap.put("cardNo", cardNo);
		modelMap.put("chargeDate", chargeDate);
		modelMap.put("headDate", chargeDate.substring(2, 4) + "年" + chargeDate.substring(5, 7) + "月");
		
		// 充值记录
		ChargeRecord condition = new ChargeRecord();
		condition.setCardno(cardNo);
		condition.setOpenid(openid);
		condition.setTim(chargeDate);
		List<ChargeRecord> midChargeRecord = myCardMapper.listChargeRecord(condition);
		
		double totalChargeAmt = 0.0;
		for(ChargeRecord record : midChargeRecord) {
			totalChargeAmt += Double.parseDouble(record.getRmbamt()) + Double.parseDouble(record.getGiftamt());
			if(record.getRmbamt().indexOf("-") < 0) {
				record.setRmbamt("+" + record.getRmbamt());
			}
		}
		modelMap.put("totalChargeAmt", WeChatUtil.formatDoubleLength(String.valueOf(totalChargeAmt),2));
		
		List<ChargeRecord> listChargeRecord = this.processChargeRecord(midChargeRecord);
		
		// 消费记录
		ConsumeRecord consumeCondition = new ConsumeRecord();
		consumeCondition.setCardno(cardNo);
		consumeCondition.setOpenid(openid);
		consumeCondition.setTim(chargeDate);
		List<ConsumeRecord> listConsumeRecord = myCardMapper.listConsumeRecord(consumeCondition);
		modelMap.put("listConsumeRecord", listConsumeRecord);
		
		double totalConsumeAmt = 0.0;
		for(ConsumeRecord record : listConsumeRecord) {
			totalConsumeAmt += Double.parseDouble(record.getAmt());
			// 
			if(record.getAmt().indexOf("-") >= 0) {
				record.setAmt(record.getAmt().replace("-", "+"));
			} else {
				record.setAmt("-" + record.getAmt());
			}
			// 将消费记录加入充值记录列表
			ChargeRecord info = new ChargeRecord();
			info.setCardno(record.getCardno());
			info.setFirmdes(record.getFirmdes());
			info.setTim(record.getTim());
			info.setRmbamt(record.getAmt());
			info.setPayment("");
			listChargeRecord.add(info);
		}
		modelMap.put("totalConsumeAmt", WeChatUtil.formatDoubleLength(String.valueOf(totalConsumeAmt),2));
		
		Collections.sort(listChargeRecord, new Comparator<ChargeRecord>() {
			public int compare(ChargeRecord r1, ChargeRecord r2) {
				return r2.getTim().compareTo(r1.getTim());
			}
		});
		modelMap.put("listChargeRecord", listChargeRecord);
		
		// 可选日期，半年内
		Calendar cal = Calendar.getInstance();
		// 先减半年，然后逐月递加。构造有序数据
		cal.add(Calendar.MONTH, -6);
		List<String> listDate = new ArrayList<String>();
		for(int i = 0; i < 6; i++) {
			cal.add(Calendar.MONTH, 1);
			listDate.add(sdf.format(cal.getTime()));
		}
		modelMap.put("listDate", listDate);
		
		return new ModelAndView(MyCardConstants.MY_BALANCE_RECORD, modelMap);
	}

	/**
	 * 在线充值
	 * @param modelMap
	 * @param card
	 * @return
	 */
	@RequestMapping(value = "myCardCharge")
	public ModelAndView myCardCharge(ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response, Card card) {
		try{
			// 获取appId
			// 根据pk_group，获取配置信息
			Company company = WeChatUtil.getCompanyInfo(card.getPk_group());

			String appId = Commons.appId;
			if(null != company) {
				if (null != company.getAppId() && !"".equals(company.getAppId())) {
					appId = company.getAppId();
				}
			}
			
			// 获取赠送金额
			String chargeTime = DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss");
			double giftAmt = this.myCardMapper.calcGiftAmt(card.getCardId(), chargeTime, 100.0);
			modelMap.put("giftAmt", giftAmt);
			modelMap.put("canGet", giftAmt + 100);
			
			// 微信支付
			//modelMap.put("appId", appId);
			// 是否服务商模式
			String tenPaySeriveFlag = Commons.getConfig().getProperty("tenPaySeriveFlag");
			if ("0".equals(tenPaySeriveFlag)) {
				// 非服务商模式
				modelMap.put("appId", appId);
			} else {
				// 服务商模式
				modelMap.put("appId", "wx35f67221fe52ad3c");
			}
			
			modelMap.put("orderId", CodeHelper.createUUID());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		modelMap.put("card", card);
		
		return new ModelAndView(MyCardConstants.MY_CARD_CHARGE, modelMap);
	}
	
	/**
	 * 支付失败后重新获取订单号
	 * @return 订单号
	 */
	@RequestMapping(value = "resetOrderId")
	@ResponseBody
	public String resetOrderId() {
		return CodeHelper.createUUID();
	}
	
	/**
	 * 调用CRM充值
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "charge")
	@ResponseBody
	public String charge(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
		String openid = request.getParameter("openid");
		String chargeMoney = request.getParameter("chargeMoney");
		String giftAmt = request.getParameter("giftAmt");
		String orderId = request.getParameter("orderId");
		modelMap.put("openid", openid);
		DecimalFormat df = new DecimalFormat("####0.00");
		List<Card> listCard = CardSearch.listCard(openid);
		if(listCard != null && !listCard.isEmpty()) {
			Card c=listCard.get(0);
			/*CardSearch.changeAmt(Integer.parseInt(c.getCardId()), Double.parseDouble(chargeMoney), Double.parseDouble(c.getzAmt()), 
					"1", Commons.firmId, "N", "100", "N", 0, Double.parseDouble(chargeMoney), 0, Commons.payMentCode, 
					Commons.payMent, DateFormat.getStringByDate(new Date(), "yyyy-MM-dd"), 
					c.getEnddate(), "微信充值", "微信充值");*/
			CardSearch.charge(Integer.parseInt(c.getCardId()), Double.parseDouble(chargeMoney) +  Double.parseDouble(giftAmt), Double.parseDouble(c.getzAmt()), 
					"1", Commons.firmId, "100", 0, Double.parseDouble(chargeMoney), Double.parseDouble(chargeMoney), Double.parseDouble(giftAmt), 
					Commons.payMentCode, Commons.payMent, DateFormat.getStringByDate(new Date(), "yyyy-MM-dd"), "微信充值", "微信充值", 
					DateFormat.getStringByDate(new Date(), "yyyyMMddHHmmss"), "0", "0", 0, CodeHelper.createUUID());
			
			// 充值成功，推送微信消息
			String accessToken = WeChatUtil.getAccessToken(Commons.appId, Commons.secret).getToken();
			Map<String,String> param = new HashMap<String,String>();
			param.put("templateCode", "OPENTM201061496");
			param.put("first", "充值成功！");
			param.put("keyword1", chargeMoney);
			param.put("keyword2", "微信充值");
			param.put("keyword3", df.format(Double.parseDouble(c.getzAmt()) + Double.parseDouble(chargeMoney)));
			param.put("remark", "会员名称：" + c.getName() + " 充值时间：" + DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
			// 冰城暂用
			/*param.put("first", "微信充值成功！");
			param.put("keynote1", DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
			param.put("keynote2", df.format(Double.parseDouble(chargeMoney) + Double.parseDouble(giftAmt)));
			param.put("remark", "会员卡余额：" + df.format(Double.parseDouble(c.getzAmt()) + Double.parseDouble(chargeMoney) + Double.parseDouble(giftAmt)));*/
			
			param.put("openid", openid);
			TemplateMsgUtil.sendTemplateMsg(accessToken, param);
			
			return "success";
		}
		return "fail";
	}
	
	/**
	 * 验证该微信号、手机号是否已经存在
	 * 
	 * @param modelMap
	 * @param card
	 * @return 0:不存在 1:存在
	 */
	@RequestMapping(value = "calcGiftAmt")
	@ResponseBody
	public Double calcGiftAmt(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
		String chargeTime = DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss");
		String chargeMoney = request.getParameter("chargeMoney");
		String cardId = request.getParameter("cardId");
		double giftAmt = this.myCardMapper.calcGiftAmt(cardId, chargeTime, Double.parseDouble(chargeMoney));
		
		return giftAmt;
	}
	
	/**
	 * 
	 * @param modelMap
	 * @param request
	 * @param openid
	 * @param pk_group
	 * @return
	 * 完善会员资料
	 * song
	 */
	@RequestMapping(value = "compInformation")
	public ModelAndView compInformation(ModelMap modelMap, HttpServletRequest request, String openid, String pk_group){
		try{
			if(openid==null || "".equals(openid)){
				return null;
			}
			WeChatUser user = (WeChatUser) request.getSession().getAttribute("WeChatUser");
			if(user == null){//返回按钮，已取得用户授权的情况
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
				
				AccessToken token = WeChatUtil.getAccessToken(appId, secret);
				//获取用户基本信息
				user = WeChatUtil.getWeChatUser(openid,token.getToken());
				if(user != null && StringUtils.hasText(user.getOpenid())){
					request.getSession().setAttribute("WeChatUser", user);
				}
			}
			
			String unionid = request.getParameter("unionid");
			/***20150625 如果Unionid不为空，则用他取值***/
			List<Card> listCard = null;
			if(StringUtils.hasText(unionid)){
				listCard = CardSearch.listCard(unionid);
			}else{
				listCard = CardSearch.listCard(openid);
			}
			
			Card card = null;
			if(listCard!=null && listCard.size()>0){
				card = listCard.get(0);
			}
			//查询会员可以选择的所有标签和兴趣爱好
			List<LabelFavorite> listLabelFavorite=CardSearch.getAllLabeFavorite();		
			modelMap.put("pk_group", pk_group);
			modelMap.put("openid", openid);
			modelMap.put("unionid", unionid);
			modelMap.put("card", card);
			modelMap.put("vcompanyname", Commons.vcompanyname);
			modelMap.put("vcompanytel", Commons.vcompanytel);
			modelMap.put("listLabelFavorite", listLabelFavorite);
			//城市列表
			List<City> listCity = bookDeskMapper.getCityList(pk_group, null);
			modelMap.put("listCity", listCity);
			
			//门店列表
			List<Firm> listFirm = bookMealMapper.getFirmList(pk_group, null, null);
			modelMap.put("listFirm", listFirm);
			
			return new ModelAndView(MyCardConstants.COMP_INFORMATION,modelMap);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "saveInformation")
	public ModelAndView saveInformation(ModelMap modelMap, HttpServletRequest request){
		String openid = request.getParameter("openid");
		String pk_group = request.getParameter("pk_group");
		String name = request.getParameter("name");
		String sex = request.getParameter("sex");
		String bridate = request.getParameter("bridate");
		String mail = request.getParameter("mail");
		String addr = request.getParameter("addr");
		String cardNo = request.getParameter("cardNo");
		String unionid = request.getParameter("unionid");
		String label=request.getParameter("label");
		String favorite=request.getParameter("favorite");	
		try{
			if(unionid==null || "".equals(unionid)){
				unionid = openid;
			}
			
			if(StringUtils.hasText(unionid) && StringUtils.hasText(cardNo)){//微信号或卡号为空
				
				JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();       
			    factoryBean.setServiceClass(CRMWebservice.class);       
			    factoryBean.setAddress(Commons.CRMwebService); 
				CRMWebservice crmService = (CRMWebservice)factoryBean.create();
				
				String result = "";
				try {
					result = crmService.updateCardInfoWechat(unionid,name,sex,bridate,mail,addr,label,favorite);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				//如果城市不为空，保存城市信息
				String city = request.getParameter("city");
				String firmCode = request.getParameter("firmCode");
				if(StringUtils.hasText(city) || StringUtils.hasText(firmCode)) {
					Card card = new Card();
					card.setCity(city);
					card.setCardNo(cardNo);
					card.setRest(firmCode);
					myCardMapper.updateCardInfo(card);
				}
				
				if("1".equals(result)){//更新CRM成功
					//判断是否完善过
					int count = myCardMapper.getCountOfRows(cardNo, unionid);
					if(count > 0){
						return cardInfo(modelMap, request, "", "", pk_group, openid);
					}
					
					//暂时送电子券
					StringBuilder boh_url = new StringBuilder(Commons.BOHUrl);
					if(!Commons.BOHUrl.endsWith("/")){
						boh_url.append("/");
					}
					boh_url.append("actmExec/completedataSCoupon.do?cardNo=")
					.append(cardNo)
					.append("&openId=")
					.append(unionid);

					String coupon = WeChatUtil.httpRequestReturnString(boh_url.toString(), "GET", null);
					if(StringUtils.hasText(coupon) && !coupon.startsWith("-")){
						//赠券成功，增加记录
						String pk_id = CodeHelper.createUUID();
						myCardMapper.addCardUpdate(pk_id, cardNo, unionid);//新增完善成功记录
						
						Map<String,String> msgMap = new HashMap<String,String>();
						msgMap.put("templateCode", "OPENTM201048309");
						msgMap.put("openid", openid);
						
						int couponCnt = 1;
						if(coupon.indexOf(",") >= 0) {
							couponCnt = coupon.split(",").length;
						}

						StringBuffer target_url = request.getRequestURL();
						target_url = target_url.delete(target_url.lastIndexOf("/"), target_url.length());
						target_url.append("/cardInfo.do?openid=").append(openid)
						.append("&pk_group=").append(pk_group);

						msgMap.put("url", target_url.toString());
						msgMap.put("first", "系统赠送您" + couponCnt + "张电子券");
						msgMap.put("keyword1", coupon);
						msgMap.put("keyword2", "完善客户资料成功");
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
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return cardInfo(modelMap, request, "", "", pk_group, openid);
	}
	
	@RequestMapping(value = "reback")
	public ModelAndView reback(HttpServletRequest request,String openid,String pk_group){
		//根据pk_group，获取配置信息
		Company company = WeChatUtil.getCompanyInfo(pk_group);
		String appId = Commons.appId;
		if(null != company) {
			if (null != company.getAppId() && !"".equals(company.getAppId())) {
				appId = company.getAppId();
			}
		}
		
		StringBuffer sb = request.getRequestURL();
		String baseUrl = sb.substring(0, sb.lastIndexOf("/"));
		sb.delete(0, sb.length());
		sb.append("redirect:")
			.append("https://open.weixin.qq.com/connect/oauth2/authorize?appid=").append(appId)
			.append("&redirect_uri=").append(baseUrl)
			.append("/cardInfo.do?openid=").append(openid)
			.append("&response_type=code&scope=snsapi_base&state=123#wechat_redirect");
		return new ModelAndView(sb.toString());
	}
	
	/**
	 * 购买优惠券
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "buyCoupon")
	public ModelAndView buyCoupon(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
		String openid = request.getParameter("openid");
		String appId = Commons.appId;
		
		// 优惠券列表
		List<Coupon> couponList = myCardMapper.getCouponList(null);
		
		// 限制门店列表
		List<Firm> firmList = myCardMapper.getLimitFirmList(null, null);
		
		// 电子券(现金购买)
		List<Coupon> cashCouponList = new ArrayList<Coupon>();
		// 电子券(积分兑换)
		List<Coupon> pointCouponList = new ArrayList<Coupon>();
		
		// 循环电子券列表，根据关联活动获取限制门店，并区分积分和现金
		for(Coupon coupon : couponList) {
			String limitFirm = "";
			// 根据活动编码获取限制门店
			for(Firm firm : firmList) {
				String couponActmCode = coupon.getVactmCode();
				String actmCode = firm.getFirmCode();
				if(StringUtils.hasText(couponActmCode) && StringUtils.hasText(actmCode) && couponActmCode.equals(actmCode)) {
					limitFirm += firm.getFirmdes() + " ";
				}
			}
			
			if(!limitFirm.isEmpty()) {
				coupon.setLimitFirm(limitFirm);
			}
			
			// 根据价格设定背景图片
			if(coupon.getNmoney() <= 5){
				coupon.setVpic("coupon_limit5.png");
			}else if(coupon.getNmoney() <= 10){
				coupon.setVpic("coupon_limit10.png");
			}else if(coupon.getNmoney() <= 20){
				coupon.setVpic("coupon_limit20.png");
			}else{
				coupon.setVpic("coupon_limit50.png");
			}
			
			if(coupon.getNeedfen() > 0) {
				coupon.setVtype("2");
			} else {
				coupon.setVtype("1");
			}
			
			// 现金购买
			String nfen = coupon.getNfen();
			if("".equals(nfen) || nfen == null){
				nfen = "0";
			}
			if(Integer.parseInt(nfen)==0) {
				// 使用CRM中的定义计算价格，防止CRM中价格修改，电商平台未更新
				if(coupon.getDiscrate() > 0) {
					coupon.setNprice(String.valueOf(coupon.getNmoney() * (1 - coupon.getDiscrate() / 100)));
				} else {
					coupon.setNprice(String.valueOf(coupon.getNmoney() - coupon.getDiscamt()));
				}
				
				cashCouponList.add(coupon);
			} else {
				coupon.setNprice(String.valueOf(coupon.getNeedfen()));
				pointCouponList.add(coupon);
			}
			coupon.setNprice(WeChatUtil.formatDoubleLength(coupon.getNprice(), 2));
		}
		
		modelMap.put("cashCouponList", cashCouponList);
		modelMap.put("pointCouponList", pointCouponList);
		modelMap.put("openid", openid);
		modelMap.put("cardNo", request.getParameter("cardNo"));
		modelMap.put("cardId", request.getParameter("cardId"));
		modelMap.put("cardPoint", request.getParameter("cardPoint"));
		
		// 微信支付
		//modelMap.put("appId", appId);
		// 是否服务商模式
		String tenPaySeriveFlag = Commons.getConfig().getProperty("tenPaySeriveFlag");
		if ("0".equals(tenPaySeriveFlag)) {
			// 非服务商模式
			modelMap.put("appId", appId);
		} else {
			// 服务商模式
			modelMap.put("appId", "wx35f67221fe52ad3c");
		}
		modelMap.put("orderId", CodeHelper.createUUID());
		
		return new ModelAndView(MyCardConstants.BUY_COUPON, modelMap);
	}
	
	/**
	 * 保存商品订单
	 * @param modelMap
	 * @param order
	 * @return
	 */
	@RequestMapping(value = "saveGoodsOrder")
	@ResponseBody
	public String saveGoodsOrder(ModelMap modelMap, NetGoodsOrders order, String cardNo) {
		String res = myCardMapper.saveGoodsOrder(order);

		JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();       
	    factoryBean.setServiceClass(CRMWebservice.class);       
	    factoryBean.setAddress(Commons.CRMwebService); 
		CRMWebservice crmService = (CRMWebservice)factoryBean.create();
		
		// 赠送券
		StringBuilder sb = new StringBuilder();
		sb.append("cardNo=")
		.append(cardNo)
		.append("&firmId=1000&empNo=99999&empName=购买电子券&actCode=&dateTime=")
		.append(DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss"))
		.append("&couponCode=");
		String wsRet = "";
		for(NetGoodsOrderDtl dtl : order.getListNetGoodsOrderDtl()) {
			String arrayStr = sb.toString() + dtl.getCouponCode() + "&num=" + dtl.getGoodsnum() + "&serial=" + CodeHelper.createUUID();
			
			try {
				wsRet = crmService.cardInCoupon(cardNo, "1000", "99999", "购买电子券", "", 
						DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss"), dtl.getCouponCode(), 
						Integer.parseInt(dtl.getGoodsnum()), CodeHelper.createUUID());
				LogUtil.writeToTxt(LogUtil.CALLWEBSERVICE, "调用赠送电子券接口成功。接口名称【cardInCoupon】，参数值【" + arrayStr + "】，返回值【" + wsRet + "】");
			} catch (Exception e) {
				LogUtil.writeToTxt(LogUtil.CALLWEBSERVICE, "调用赠送电子券接口失败。接口名称【cardInCoupon】，参数值【" + arrayStr + "】，错误信息【" + e.getMessage() + "】");
				e.printStackTrace();
			}
		}
		
		if(order.getSumPoint() > 0 ) {
			// 扣减积分
			String param = "cardNo=" + cardNo + "&firmId=1000&empNo=99999&empName=购买电子券&dateTime=" 
			+ DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss") + "&fen=" + order.getSumPoint() + "&serial=" + CodeHelper.createUUID()
			+ "&password=123456&type=0";
			
			try {
				wsRet = crmService.cardOutPoint(cardNo, "1000", "99999", "购买电子券", 
						DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss"), order.getSumPoint(), 
						CodeHelper.createUUID(), "123456");
				LogUtil.writeToTxt(LogUtil.CALLWEBSERVICE, "调用扣减积分接口成功。接口名称【cardOutPoint】，参数值【" + param + "】，返回值【" + wsRet + "】");
			} catch (Exception e) {
				LogUtil.writeToTxt(LogUtil.CALLWEBSERVICE, "调用扣减积分接口失败。接口名称【cardOutPoint】，参数值【" + param + "】，错误信息【" + e.getMessage() + "】");
				e.printStackTrace();
			}
			
			// 计算购买张数
			int cnt = 0;
			for(NetGoodsOrderDtl dtl : order.getListNetGoodsOrderDtl()) {
				if("2".equals(dtl.getPayType())) {
					cnt += Integer.parseInt(dtl.getGoodsnum());
				}
			}
			
			// 保存积分支出记录
			try {
				int reslut = myCardMapper.addCostFen(Integer.parseInt(order.getCustId()), order.getSumPoint(), cnt);
				if(reslut <= 0 ) {
					LogUtil.writeToTxt(LogUtil.CALLWEBSERVICE, "插入积分兑换记录失败，会员卡ID【" + order.getCustId() + "】， 积分【" + order.getSumPoint() + "】，数量【" + cnt + "】");
				} else {
					LogUtil.writeToTxt(LogUtil.CALLWEBSERVICE, "插入积分兑换记录成功，会员卡ID【" + order.getCustId() + "】， 积分【" + order.getSumPoint() + "】，数量【" + cnt + "】");
				}
			} catch (Exception e) {
				LogUtil.writeToTxt(LogUtil.CALLWEBSERVICE, "插入积分兑换记录失败，会员卡ID【" + order.getCustId() + "】， 积分【" + order.getSumPoint()
						+ "】，数量【" + cnt + "】,失败原因【" + e.getMessage() + "】");
				e.printStackTrace();
			}
		}
		
		return res;
	}
	
	/**
	 * 
	 * @param modelMap
	 * @param request
	 * @param code
	 * @param state
	 * @return
	 */
	@RequestMapping(value = "voucherDetail")
	public ModelAndView voucherDetail(ModelMap modelMap, HttpServletRequest request, String code, String state) {
		String param = request.getParameter("id");
		String[] params = param.split("_");
		String id = "";
		String oriCardid = ""; //转赠者cardid
		if(params.length > 0) {
			id = params[0];
		}
		if(params.length > 1) {
			oriCardid = params[1];
		}
		Voucher v = new Voucher();
		v.setId(id);
		List<Voucher> voucherList = this.myCardMapper.getVoucherByCondition(v);
		
		Voucher voucher = new Voucher();
		if(null != voucherList && !voucherList.isEmpty()) {
			voucher = voucherList.get(0);
			modelMap.put("voucher", voucher);
			
			// 限制门店列表
			List<Firm> firmList = myCardMapper.getLimitFirmList(null, null);
			String limitFirm = "";
			// 根据活动编码获取限制门店
			for(Firm firm : firmList) {
				String couponActmCode = voucher.getActmCode();
				String actmCode = firm.getFirmCode();
				if(couponActmCode!=null && actmCode!=null){
					if(!couponActmCode.isEmpty() && !actmCode.isEmpty() && couponActmCode.equals(actmCode)) {
						limitFirm += firm.getFirmdes() + " ";
					}
				}
			}
			voucher.setRest(limitFirm);
			
			String openid = request.getParameter("openid");
			
			//根据pk_group，获取配置信息
			String pk_group = request.getParameter("pk_group");
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
			AccessToken token = WeChatUtil.getAccessToken(appId, secret);
			
			if(null != code && !"".equals(code)) {
				// 通过网页授权方式获取openID
				String oauth2[];
				try {
					oauth2 = WeChatUtil.getOAuth2(appId, secret, code);
					WeChatUser user = WeChatUtil.getWeChatUser(oauth2[0], token.getToken());
					openid = user.getOpenid();
					
					// 根据openid查询是否已经是会员，不是会员提醒注册
					List<Card> listCard = CardSearch.listCard(openid);
					if(null != listCard && !listCard.isEmpty()) {
						// 判断与赠送者是否是同一个人，不是的时候页面显示接受电子券按钮
						String acceptCardId = listCard.get(0).getCardId();
						if(!voucher.getCardId().equals(acceptCardId)) {
							modelMap.put("acceptCardId", acceptCardId);
							modelMap.put("showAcceptBtn", "Y");
						} else if(voucher.getCardId().equals(acceptCardId)) {
							//电子券绑定的卡ID和登录用户卡ID一致时，说明电子券为登录用户所有，显示转赠按钮
							modelMap.put("showTransBtn", "Y");
						}
						
						if(StringUtils.hasText(oriCardid) && !oriCardid.equals(voucher.getCardId())) {
							//传过来的卡ID和电子券绑定的卡ID不一致时，说明已经有人领取，隐藏领取按钮
							modelMap.put("showAcceptBtn", "");
						}
					} else {
						modelMap.put("isMember", "N");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			modelMap.put("openid", openid);
			
			StringBuffer sb = request.getRequestURL();
			String queryString = request.getQueryString();
			if(queryString != null && !"".equals(queryString)){
				sb.append("?");
				int index = queryString.indexOf("#");
				if(index < 0){
					sb.append(queryString);
				}else{
					sb.append(queryString.substring(0, index));
				}
			}
			//获取jsJDK需要的参数
			Map<String, String> signMap = Sign.sign(WeChatUtil.getJsapiTicket(appId, token.getToken()).getTicket(),sb.toString());
			modelMap.put("signMap", signMap);
			modelMap.put("appId", appId);
		}
		
		return new ModelAndView(MyCardConstants.VOUCHER_DETAIL, modelMap);
	}
	
	/**
	 * 接受赠送的电子券
	 * @param modelMap
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "acceptVoucher")
	@ResponseBody
	public String acceptVoucher(ModelMap modelMap, HttpServletRequest request) {
		String id = request.getParameter("id");
		String cardId = request.getParameter("cardId");
		
		Voucher voucher = new Voucher();
		voucher.setId(id);
		voucher.setCardId(cardId);
		
		int result = myCardMapper.updateCardIdOfVoucher(voucher);
		
		if(result > 0) {
			return "ok";
		}
		
		return "";
	}
	
	/**
	 * 根据会员卡类型查询会员特权
	 * @param modelMap
	 * @param cardType
	 */
	@RequestMapping(value = "memberRight")
	public ModelAndView memberRight(ModelMap modelMap, String cardType, String cardNo, String openid) {
		List<MemberRight> rightList = myCardMapper.getMemberRightList(cardType);
		if(null != rightList && !rightList.isEmpty()) {
			modelMap.put("memberRight", rightList.get(0));
		}
		
		modelMap.put("openid", openid);
		modelMap.put("cardNo", cardNo);
		
		return new ModelAndView(MyCardConstants.MEMBER_RIGHT, modelMap);
	}
	
	/**
	 * 根据城市获取门店列表
	 * @param modelMap
	 * @param pk_city
	 * @return
	 */
	@RequestMapping(value="getFirmByCity")
	@ResponseBody
	public List<Firm> getFirmByCity(ModelMap modelMap, String pk_city){
		List<Firm> listFirm = null;
		try{
			listFirm = bookMealMapper.getFirmList(null, pk_city, null);
		}catch(Exception e){
			e.printStackTrace();
		}
		return listFirm;
	}
	
	/**
	 * 验证用户密码是否正确
	 * @param cardId
	 * @param passwd
	 * @return 0:失败； 1：成功
	 */
	@RequestMapping(value="verifyPass")
	@ResponseBody
	public String verifyPassword(String cardId, String passwd) {
		// 验证用户密码是否正确
		boolean result = myCardMapper.verifyPassword(cardId, passwd);
		
		return result ? "1" : "0";
	}
	
	/**
	 * 设置密码
	 * @param cardId
	 * @param passwd
	 * @return
	 */
	@RequestMapping(value="setPassword")
	@ResponseBody
	public String setPassword(String cardId, String passwd) {
		int result = myCardMapper.setPassword(cardId, passwd);
		
		return result + "";
	}
	
	/**
	 * 
	 * @param cardId
	 * @param tele
	 * @return
	 */
	@RequestMapping(value="resetPassword")
	@ResponseBody
	public String resetPassword(String cardId, String tele) {
		// 生成随机六位数
		String rannum = GenerateRandom.getRanNum(6);
		// 发送短信
		String content = "【" + Commons.vcompanyname + "】" + "尊敬的用户,您的密码被重置为:" + rannum + ",请及时修改！";
		
		String smsType = Commons.getConfig().getProperty("smsChannelType");
		// 返回值
		int result = 0;
		try {
			if("2".equals(smsType)) {
				// 使用谐和平台发送
				SendSMSByXieHe.sendSMS(tele, content);
			} else if("3".equals(smsType)) {
				// 使用网信通平台发送
				SendSMSByWxt.sendSMS(tele, content);
			} else if("9".equals(smsType)) {
				// 使用辰森短信平台发送
				result = WeChatUtil.sendSMSbyPlatform(tele, content);
			} else if("11".equals(smsType)){
				String jzname = Commons.getConfig().getProperty("jzusername");;//登录建周的用户名
				String jzpwd = Commons.getConfig().getProperty("jzpwd");;//登录建周的密码
				String jznote = Commons.getConfig().getProperty("jznote");;//建周的签名
				BusinessService bs = new BusinessService();
				bs.setWebService("http://www.jianzhou.sh.cn/JianzhouSMSWSServer/services/BusinessService");
				String ss = "尊敬的用户,您的密码被重置为:" + rannum + ",请及时修改！"+jznote;
				int i = bs.sendBatchMessage(jzname,jzpwd ,tele, ss);
				System.out.println("建周短信平台返回码："+i+"电话："+tele+",ss:"+ss+"---"+jzname+","+jzpwd+","+jznote);
			} else {
				// 使用亿美平台发送
				result = TestSDKClient.sendSMS(tele, content);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			result = -1;
		}
		
		// 发送短信成功，更新密码
		if (result == 0) {
			LogUtil.writeToTxt("resetPassword", "密码重置成功。卡ID【" + cardId + "】，新密码【" + rannum + "】");
			// 设置密码为随机数
			result = myCardMapper.setPassword(cardId, rannum);
		}
		
		return result > 0 ? "OK" : "NG";
	}
	/**
	 * 扫码点餐，输入手机号自动注册会员
	 * @param openid
	 * @param tele
	 * @return
	 */
	@RequestMapping(value="registerMember")
	@ResponseBody
	public String registerMermber(Card card){
		// 插入会员记录
		card.setChlb("会员");
		card.setTyp(Commons.getConfig().getProperty("registerCardTyp"));
		String cardNo=CardSearch.addCardRed(card);
		return cardNo;
	}
}