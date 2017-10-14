package com.choice.test.service;

import java.util.List;
import java.util.Map;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.log4j.Logger;

import com.choice.test.domain.Card;
import com.choice.test.domain.CardRules;
import com.choice.test.domain.CardTyp;
import com.choice.test.domain.ChargeRecord;
import com.choice.test.domain.ConsumeRecord;
import com.choice.test.domain.Voucher;
import com.choice.test.persistence.WeChatCrmMapper;
import com.choice.test.service.impl.WeChatCrmServiceImpl;
import com.choice.test.utils.Commons;
import com.choice.test.utils.DateFormat;
import com.choice.webService.CRMWebservice;
import com.choice.wechat.domain.myCard.LabelFavorite;
/**
 * 会员卡
 * @author 孙胜彬
 */
public class CardSearch {
	static Logger logger = Logger.getLogger(CardSearch.class.getName());
	static WeChatCrmMapper crmMapper=new WeChatCrmServiceImpl();
	/**
	 * 添加会员信息
	 * @param card
	 * @return
	 */
	public static String addCardRed(Card card){
		try {
			String cont = crmMapper.addCardWebService(card.getName(), card.getTele(), card.getOpenid(), 
					card.getTyp(),card.getTypDes(),card.getChlb(), card.getFirmId(), card.getPasswd(),card.getOthersShareCode());
			return cont;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 添加会员卡类型
	 * @param card
	 * @return
	 */
	public static List<CardTyp> getCardTyp(){
		try {
			List<CardTyp> listCardTyp = crmMapper.getCardTyp(null);
			return listCardTyp;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 查询微信会员
	 * @param openid
	 * @return
	 */
	public static List<Card> listCard(String openid){
		try {
			Card card = new Card();
			card.setOpenid(openid);
			List<Card> list = crmMapper.queryCardWebService(card);
			return list;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 根据卡号查询微信会员
	 * @param cardNo
	 * @return
	 */
	public static List<Card> listCardByCardno(String cardNo){
		try {
			Card card = new Card();
			card.setCardNo(cardNo);
			List<Card> listCard = crmMapper.queryCardWebService(card);
			return listCard;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 根据推荐码查询微信会员
	 * @param shareCode 注册码
	 */
	public static List<Card> listCardByShareCode(Card card){
		try {
			
			List<Card> listCard = crmMapper.queryCardWebService(card);
			return listCard;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 查询充值记录
	 * @param openid
	 * @return
	 */
	public static List<ChargeRecord> listChargeRecord(String cardNo,String openid){
		try {
			List<ChargeRecord> listChargeRecord = crmMapper.chargeRecordWebService(cardNo, openid);
			return listChargeRecord;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 查询消费记录
	 * @param openid
	 * @return
	 */
	public static List<ConsumeRecord> listConsumeRecord(String cardNo,String openid){
		try {
			List<ConsumeRecord> listConsumeRecord = crmMapper.consumeRecordWebService(cardNo, openid);
			return listConsumeRecord;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 发送短信前查询会员是否绑定微信
	 * @param openid
	 * @return
	 */
	public static List<Card> listWXCard(String tele, String openid){
		try {
			Card card = new Card();
			card.setTele(tele);
			card.setOpenid(openid);
			List<Card> listCard = crmMapper.queryCardWebService(card);
			return listCard;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 微信绑定老会员
	 * @param card
	 * @return
	 */
	public static String addWXCard(Card card){
		try {
			String cont = crmMapper.addWXCardWebService(card.getCardNo(), card.getOpenid(), card.getPasswd());
			return cont;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获取短信验证码
	 * @param card
	 * @return
	 */
	public static String sendSms(Card card){
		try {
			String cont = crmMapper.generateRandom(card.getTele());
			return cont;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 查询电子卷
	 * @param card
	 * @return
	 */
	public static List<Voucher> getVoucher(Card card){
		try {
			List<Voucher> listVoucher = crmMapper.getVoucher(card.getOpenid());
			return listVoucher;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 查询会员卡规则
	 * @param card
	 * @return
	 */
	public static List<CardRules> getCardRules(Card card){
		try {
			List<CardRules> listCardRules = crmMapper.getCardRules(null);
			return listCardRules;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 修改手机号
	 * @param card
	 * @return
	 */
	public static String updateTele(String openid,String tele){
		try {
			String cont = crmMapper.updateTele(openid, tele);
			return cont;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 验证登陆
	 * @param openid
	 * @return
	 */
	public static Card loginCard(String cardNo,String pass, String telephone){
		try {
			Card card = new Card();
			card.setCardNo(cardNo);
			card.setPasswd(pass);
			card.setTele(telephone);
			List<Card> listCard = crmMapper.queryCardWebService(card);
			return listCard.get(0);
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 绑定会员卡赠送金额
	 * @param openid
	 * @return
	 */
	public static String changeAmt(int cardId,double totalAmt,double zAmt,String empNo,String firmId,String isCredit,String invoiceNo,String isGrpCard,
			double invoiceAmt,double inAmt,double giftAmt,String payMentCode,String payMent,String dateTime,String endDate,String empName,String oEmpName){
		try {
			int i = crmMapper.changeAmt(cardId, totalAmt, zAmt, empNo, firmId, isCredit, invoiceNo, isGrpCard, invoiceAmt, inAmt, giftAmt, payMentCode, payMent, dateTime, endDate, empName, oEmpName);
			return i+"";
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 会员卡充值
	 * @param openid
	 * @return
	 */
	public static String charge(int cardId, double totalAmt, double zAmt, String empNo, String firmId, String invoiceNo, int rsn, 
			double invoiceAmt, double rmbAmt, double giftAmt, String payMentCode, String payMent, String dateTime, String empName,String oEmpName,
			String tim, String sft, String posId, int flag, String poserial){
		try {
			int i = crmMapper.charge(cardId, totalAmt, zAmt, empNo, firmId, invoiceNo, rsn, invoiceAmt, rmbAmt, giftAmt, 
					payMentCode, payMent, dateTime, empName, oEmpName, tim, sft, posId, flag, poserial);
			return i+"";
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 会员扣费
	 * @param openid
	 * @return
	 */
	public static String cardOutAmt(String cardNo,String outAmt,String firmId,String date,String invoiceAmt,String empName,String firmName,String emp,
			double billAmt,String conacct,double discAmt,String billno,String grpsta,double credit,int pax,String paytyp,double fenAmt){
		JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();       
	    factoryBean.setServiceClass(CRMWebservice.class);       
	    factoryBean.setAddress(Commons.CRMwebService); 
		CRMWebservice crmService = (CRMWebservice)factoryBean.create();
		try {
			String cont = crmService.cardOutAmtWebService(cardNo, Double.parseDouble(outAmt), firmId,firmName, date, Double.parseDouble(invoiceAmt), emp,empName,billAmt,conacct,discAmt,billno,grpsta,credit,pax,paytyp,fenAmt);
			return cont;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return "0";
	}
	public static void main(String[] args){
			JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();       
		    factoryBean.setServiceClass(CRMWebservice.class);       
		    factoryBean.setAddress(Commons.CRMwebService); 
			CRMWebservice crmService = (CRMWebservice)factoryBean.create();
			try {
				String cont = crmService.addCardWebService("臧国良", "18734290127", "98", "26");
				System.out.println(cont);
			} catch (Exception e) {
				logger.error(e);
				e.printStackTrace();
			}
	}
	 /**
	  * 验证该微信号、手机号是否已经存在
	  * @param card
	  * @return
	  */
	public static List<Card> verifyMemberExist(Card card) {
		try {
			List<Card> listCard = crmMapper.queryCardWebService(card);
			return listCard;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 查询剩余、获得、支出积分
	 * @param card
	 * @return
	 */
	public static Map<String, Object> getIntegrationRecodeSum(Card card) {
		try {
			Map<String, Object> integrationRecodeSum = crmMapper.getIntegrationRecodeSum(card.getCardNo(),card.getCardId(),card.getOpenid(),card.getPk_group());
			return integrationRecodeSum;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 查询积分明细
	 * @param card
	 * @param type
	 * @return
	 */
	public static List<Map<String, Object>> getIntegrationRecodes(Card card,String type) {
		try {
			List<Map<String, Object>> listIntegrationRecodes = crmMapper.getIntegrationRecodes(card.getCardNo(),card.getCardId(),card.getOpenid(),card.getPk_group(),type);
			return listIntegrationRecodes;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据会员卡类型及门店编码查询数据是否存在
	 * @param type
	 * @param firmCode
	 * @return
	 */
	public static boolean isCardTypRestExist(String type, String firmCode) {
		try {
			boolean exist = crmMapper.isCardTypRestExist(type, firmCode);
			return exist;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return false;
	}
	public static List<LabelFavorite> getAllLabeFavorite(){
		try {
			List<LabelFavorite> listLabelFavorite=crmMapper.getAllLabelFavorite();
			return listLabelFavorite;		
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
}
