package com.choice.test.web.controller;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.choice.test.constants.CardConstants;
import com.choice.test.domain.Card;
import com.choice.test.domain.CardRules;
import com.choice.test.domain.ChargeRecord;
import com.choice.test.domain.ConsumeRecord;
import com.choice.test.domain.Voucher;
import com.choice.test.service.CardSearch;
import com.choice.test.utils.CodeHelper;
import com.choice.test.utils.Commons;
import com.choice.test.utils.DateFormat;
import com.choice.wechat.util.LogUtil;

/**
 * 会员卡
 * @author 孙胜彬
 */
@Controller
@RequestMapping(value = "card")
public class CardController {
	/**
	 * 添加会员信息
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "addCard")
	public ModelAndView addCard(ModelMap modelMap, String code,String state) {
		String openid="";
		try {
			openid = CodeHelper.getOpenID(Commons.appId, Commons.secret, code);
			System.out.println(openid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<Card> listCard=CardSearch.listCard(openid);
		if(listCard !=null && listCard.size()!=0){
			List<CardRules> listCardRules=CardSearch.getCardRules(null);
			modelMap.put("exclusprivle", listCardRules.get(0).getExclusprivle());
			modelMap.put("cardtele", listCardRules.get(0).getCardtele());
			modelMap.put("store", listCardRules.get(0).getSTORE());
			modelMap.put("listCard", listCard);
			modelMap.put("openid", openid);
			return new ModelAndView(CardConstants.LIST_CARD,modelMap);
		}
		modelMap.put("listCardTyp", CardSearch.getCardTyp());
		modelMap.put("openid", openid);
		return new ModelAndView(CardConstants.ADD_CARD,modelMap);
	}
	
	/**
	 * 跳转到添加会员信息
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "toAddCard")
	public ModelAndView toAddCard(ModelMap modelMap, String openid) {
		modelMap.put("openid", openid);
		return new ModelAndView(CardConstants.ADD_CARD,modelMap);
	}
	/**
	 * 添加会员信息
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "saveCard")
	public ModelAndView addCardRed(ModelMap modelMap, Card card) {
		String cardNo=CardSearch.addCardRed(card);
		if(null==cardNo || cardNo.equals("")){
			return new ModelAndView(CardConstants.ADD_CARD,modelMap);
		}else{
			List<Card> listCard=CardSearch.listCard(card.getOpenid());
			List<CardRules> listCardRules=CardSearch.getCardRules(null);
			modelMap.put("exclusprivle", listCardRules.get(0).getExclusprivle());
			modelMap.put("cardtele", listCardRules.get(0).getCardtele());
			modelMap.put("store", listCardRules.get(0).getSTORE());
			modelMap.put("openid", card.getOpenid());
			modelMap.put("listCard", listCard);
			return new ModelAndView(CardConstants.LIST_CARD,modelMap);
		}
	}
	/**
	 * 微信绑定老会员
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "saveWXCard")
	public ModelAndView saveWXCard(ModelMap modelMap, Card card) {
		String cont=CardSearch.addWXCard(card);
		if(cont !=null && !cont.equals("")){
			List<Card> listCard=CardSearch.listCard(card.getOpenid());
//			if(null !=listCard && listCard.size() !=0){
//				Card c=listCard.get(0);
//				CardSearch.changeAmt(Integer.parseInt(c.getCardId()), Double.parseDouble(Commons.inAmt), Double.parseDouble(c.getzAmt()), 
//						"1", Commons.firmId, "N", "100", "N", 0, Double.parseDouble(c.getChgAmt()), 0, Commons.payMentCode, 
//						Commons.payMent, DateFormat.getStringByDate(new Date(), "yyyy-MM-dd"), 
//						c.getEnddate(), "微信充值", "微信充值");
//			}
			List<CardRules> listCardRules=CardSearch.getCardRules(null);
			modelMap.put("exclusprivle", listCardRules.get(0).getExclusprivle());
			modelMap.put("cardtele", listCardRules.get(0).getCardtele());
			modelMap.put("store", listCardRules.get(0).getSTORE());
			modelMap.put("openid", card.getOpenid());
			modelMap.put("listCard", listCard);
			return new ModelAndView(CardConstants.LIST_CARD,modelMap);
		}
		return new ModelAndView(CardConstants.ADD_CARD,modelMap);
	}
	/**
	 * 查询会员信息
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "listCard")
	public ModelAndView listCard(ModelMap modelMap, String openid)
			throws Exception {
		List<Card> listCard=CardSearch.listCard(openid);
		List<CardRules> listCardRules=CardSearch.getCardRules(null);
		modelMap.put("exclusprivle", listCardRules.get(0).getExclusprivle());
		modelMap.put("cardtele", listCardRules.get(0).getCardtele());
		modelMap.put("store", listCardRules.get(0).getSTORE());
		modelMap.put("openid", openid);
		modelMap.put("listCard", listCard);
		return new ModelAndView(CardConstants.LIST_CARD,modelMap);
	}
	/**
	 * 验证微信会员是否存在
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "listCardValidate")
	@ResponseBody
	public String listCardValidate(ModelMap modelMap, Card card)
			throws Exception {
		List<Card> listCard=CardSearch.listCard(card.getOpenid());
		if(listCard.size() !=0){
			return "1";
		}
		return "0";
	}
	/**
	 * 预存余额
	 * @param modelMap
	 * @param zamt
	 * @return
	 */
	@RequestMapping(value="cardZAmt")
	public ModelAndView cardZAmt(ModelMap modelMap, String zamt){
		modelMap.put("zamt", zamt);
		List<CardRules> listCardRules=CardSearch.getCardRules(null);
		modelMap.put("chgrules", listCardRules.get(0).getChgrules());
		return new ModelAndView(CardConstants.CARDZAMT,modelMap);
	}
	/**
	 * 积分余额
	 * @param modelMap
	 * @param zamt
	 * @return
	 */
	@RequestMapping(value="cardTtlFen")
	public ModelAndView cardTtlFen(ModelMap modelMap, String ttlFen){
		modelMap.put("ttlFen", ttlFen);
		List<CardRules> listCardRules=CardSearch.getCardRules(null);
		modelMap.put("jifenrules", listCardRules.get(0).getJifenrules());
		return new ModelAndView(CardConstants.CARDTTLFEN,modelMap);
	}
	/**
	 * 电子卷
	 * @param modelMap
	 * @param zamt
	 * @return
	 */
	@RequestMapping(value="cardVoucher")
	public ModelAndView cardVoucher(ModelMap modelMap, String openid){
		Card card=new Card();
		card.setOpenid(openid);
		List<Voucher> listVoucher=CardSearch.getVoucher(card);
		modelMap.put("listVoucher", listVoucher);
		return new ModelAndView(CardConstants.CARDVOUCHER,modelMap);
	}
	/**
	 * 会员卡说明
	 * @param modelMap
	 * @param zamt
	 * @return
	 */
	@RequestMapping(value="cardExplain")
	public ModelAndView cardExplain(ModelMap modelMap){
		List<CardRules> listCardRules=CardSearch.getCardRules(null);
		modelMap.put("cardexplan", listCardRules.get(0).getCardexplan());
		return new ModelAndView(CardConstants.CARDEXPLAIN,modelMap);
	}
	/**
	 * 判断该手机是否注册过会员
	 * @param modelMap
	 * @param tele
	 * @return
	 */
	@RequestMapping(value="getTele")
	@ResponseBody
	public String getTele(ModelMap modelMap,Card card){
		List<Card> listCard=CardSearch.listWXCard(card.getTele(), null);
		if(listCard.size()!=0){
			return "OK"+listCard.get(0).getCardNo();
		}
		return null;
	}
	/**
	 * 获取手机验证码
	 * @param modelMap
	 * @param tele
	 * @return
	 */
	@RequestMapping(value="sendSms")
	@ResponseBody
	public String sendSms(ModelMap modelMap,Card card){
		String ranNum = CardSearch.sendSms(card);
		return ranNum;
	}
	/**
	 * 修改手机号码页面
	 * @param modelMap
	 * @param tele
	 * @return
	 */
	@RequestMapping(value="updateTeleHtml")
	public ModelAndView updateTele(ModelMap modelMap,String openid){
		modelMap.put("openid", openid);
		return new ModelAndView(CardConstants.UPDATETELE,modelMap);
	}
	/**
	 * 修改手机号码
	 * @param modelMap
	 * @param tele
	 * @return
	 */
	@RequestMapping(value="updateTele")
	@ResponseBody
	public String updateTele(ModelMap modelMap,Card card){
		String cont = CardSearch.updateTele(card.getOpenid(), card.getTele());
		return cont;
	}
	
	/**
	 * 查询充值记录
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "listChargeRecord")
	public ModelAndView listChargeRecord(ModelMap modelMap, String cardNo,String openid)
			throws Exception {
		List<ChargeRecord> listChargeRecord=CardSearch.listChargeRecord(cardNo, openid);
		modelMap.put("listChargeRecord", listChargeRecord);
		return new ModelAndView(CardConstants.CHARGERECORD,modelMap);
	}
	/**
	 * 查询消费记录
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "listConsumeRecord")
	public ModelAndView listConsumeRecord(ModelMap modelMap, String cardNo,String openid)
			throws Exception {
		List<ConsumeRecord> listConsumeRecord=CardSearch.listConsumeRecord(cardNo, openid);
		modelMap.put("listConsumeRecord", listConsumeRecord);
		return new ModelAndView(CardConstants.CONSUMERECORD,modelMap);
	}
	
	/**
	 * 验证登陆
	 * @param modelMap
	 * @param tele
	 * @return
	 */
	@RequestMapping(value="loginCard")
	@ResponseBody
	public Card loginCard(ModelMap modelMap,String cardNo,String pass,String telephone){
		return CardSearch.loginCard(cardNo, pass, telephone);
	}
	/**
	 * 会员扣费
	 * @param modelMap
	 * @param tele
	 * @return
	 */
	@RequestMapping(value="cardOutAmt")
	@ResponseBody
	public Card cardOutAmt(ModelMap modelMap,Card card){
//		CardSearch.cardOutAmt(card.getCardNo(), card.getOutAmt(), card.getFirmId(), card.getDate(), card.getInvoiceAmt(), card.getEmpName());
//		String cardNo,String outAmt,String firmId,String date,String invoiceAmt,String empName,String firmName,String emp,
//		double billAmt,String conacct,double discAmt,String billno,String grpsta,double credit,int pax,String paytyp,double fenAmt)
		Card c = CardSearch.listCardByCardno(card.getCardNo()).get(0);
		c.setOutAmt(card.getOutAmt());
		return c;
	}
}
