package com.choice.wechat.service.WeChatPay.impl;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.net.ssl.SSLContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.choice.test.domain.Card;
import com.choice.test.domain.Net_Orders;
import com.choice.test.exception.CRUDException;
import com.choice.test.service.CardSearch;
import com.choice.test.utils.Commons;
import com.choice.test.utils.DateFormat;
import com.choice.wechat.common.AccessToken;
import com.choice.wechat.domain.bookDesk.Company;
import com.choice.wechat.domain.bookDesk.Firm;
import com.choice.wechat.domain.game.Actm;
import com.choice.wechat.domain.weChatPay.WxOrderActm;
import com.choice.wechat.persistence.WeChatPay.WxPayMapper;
import com.choice.wechat.persistence.bookDesk.BookDeskMapper;
import com.choice.wechat.persistence.bookMeal.BookMealMapper;
import com.choice.wechat.persistence.takeout.TakeOutMapper;
import com.choice.wechat.service.WeChatPay.IWxPayService;
import com.choice.wechat.service.campaign.CampaignTools;
import com.choice.wechat.util.BuilderOrderMessage;
import com.choice.wechat.util.CallWebService;
import com.choice.wechat.util.LogUtil;
import com.choice.wechat.util.TemplateMsgUtil;
import com.choice.wechat.util.ValueCheck;
import com.choice.wechat.util.WeChatUtil;
import com.choice.wechat.util.mq.MqSender;
import com.choice.wechat.util.tenpay.RefundRequestHandler;
import com.choice.wechat.util.tenpay.client.ClientResponseHandler;
import com.choice.wechat.web.controller.bookDesk.BookDeskController;
import com.choice.wechat.web.controller.weChatPay.WxPayController;
import com.wxap.client.TenpayHttpClient;
import com.wxap.util.MD5SignUtil;
import com.wxap.util.MD5Util;
import com.wxap.util.Sha1Util;
import com.wxap.util.XMLUtil;
/**
 * 微信支付后台方法
 * @author ZGL
 * @Date 2015-04-03 17:33:30
 *
 */
@Service
public class WxPayServiceImpl implements IWxPayService{
	private static Logger log = Logger.getLogger(WxPayServiceImpl.class);
	@Autowired
	private WxPayMapper wxPayMapper;
	@Autowired
	private BookDeskMapper bookDeskMapper;
	@Autowired
	private BookMealMapper bookMealMapper;
	@Autowired
	private TakeOutMapper takeOutMapper;

	@Override
	public List<Map<String, Object>> saveOrder(String firmid, String dat) throws CRUDException {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 取消会员支付
	 * @author ZGL
	 * @throws Exception 
	 * @Date 2015-04-09 13:25:28
	 */
	@Override
	public String cancelPayWithCard(Map<String, Object> mapParam) throws Exception {
		String firmid = (String) mapParam.get("firmid");
		String pk_group =  (String) mapParam.get("pk_group");
		String cardNo =  (String) mapParam.get("cardNo");
		String poserial =  (String) mapParam.get("poserial");
		String voucherCode =  (String) mapParam.get("voucherCode");
		
		String params = "";
		String memInfo = "";
		//撤销会员卡支付
		params = "cardNo="+cardNo+"&firmId="+firmid+"&dateTime="+
				DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss")+"&empNo=999999&empName=微会员支付"+
				"&serial="+poserial+"&password=123456";
		memInfo = CallWebService.httpCallWebServiceGet("CRMWebService/cardOutAmtReverse", params);
		if("1".equals(memInfo)){
			memInfo = cancalCoupon(mapParam);
		}
		return memInfo;
	}
	/**
	 * 取消电子券消费
	 * @param mapParam
	 * @return 1：成功； -102：失败
	 * @throws Exception
	 */
	@Override
	public String cancalCoupon(Map<String, Object> mapParam) throws Exception {
		try{
			String firmid = (String) mapParam.get("firmid");
			String pk_group =  (String) mapParam.get("pk_group");
			String cardNo =  (String) mapParam.get("cardNo");
			String poserial =  (String) mapParam.get("poserial");
			
			String params = "";
			String memInfo = "";
			//撤销电子券消费--失败需要回滚撤销会员卡支付的操作
			params = "cardNo="+cardNo+"&firmId="+firmid+"&couponCode=1&serial="+poserial+
					"&dateTime="+DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss")+"&empNo=999999&empName=微会员支付";
			memInfo = CallWebService.httpCallWebServiceGet("CRMWebService/cardOutCouponReverse", params);
			if(!"1".equals(memInfo)){
				//回滚撤销会员卡支付操作
				params = "cardNo="+cardNo+"&firmId="+firmid+"&dateTime="+DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss")+
						"&posId=0&serial="+poserial+"";
				CallWebService.httpCallWebServiceGet("CRMWebService/cancelLastOperate", params);
				memInfo = "-102";
			}
			return memInfo;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 更新订单状态
	 * 保存订单支付方式
	 * @author ZGL
	 * @date 2015-04-09 14:20:11
	 */
	@Override
	public String savePayment(Map<String, Object> mapParam) throws Exception {
		try{
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();//结算方式list对象
			JSONArray cardJson = new JSONArray();//会员结算方式Json对象
			
			mapParam.put("billAmt", WeChatUtil.formatDoubleLength(mapParam.get("billAmt") + "", 2));
			mapParam.put("cardAmt", WeChatUtil.formatDoubleLength(mapParam.get("cardAmt") + "", 2));
			String poserial = mapParam.get("poserial")+"";
			Net_Orders orders = new Net_Orders();
			orders.setId(poserial);
			orders.setState(mapParam.get("billstate")+"");
			orders.setRealmoney(mapParam.get("billAmt")+"");
			orders.setPaymoney(mapParam.get("cardAmt")+"");
			orders.setVtransactionid(mapParam.get("vtransactionid")+"");
			LogUtil.writeToTxt(LogUtil.BUSINESS, "处理结算方式时参数："+JSONObject.fromObject(mapParam).toString());
			String res = bookDeskMapper.updateOrdr(orders);
			
			// 订单信息
			Map<String, String> conmap = new HashMap<String, String>();
			conmap.put("id", poserial);
			List<Net_Orders> listNet_Orders = bookMealMapper.getOrderMenus(conmap);//账单信息
			
			// 查询会员信息
			String cardNo = "";
			LogUtil.writeToTxt(LogUtil.BUSINESS, "查询会员信息，openid：" + listNet_Orders.get(0).getOpenid());
			List<Card> listCard = CardSearch.listCard(listNet_Orders.get(0).getOpenid());
			if (listCard != null && listCard.size() > 0) {
				cardNo = listCard.get(0).getCardNo();
				LogUtil.writeToTxt(LogUtil.BUSINESS, "查询会员信息，会员卡号：" + cardNo);
			}
			
			// 查询订单对应的活动信息
			List<WxOrderActm> listOrderActm = wxPayMapper.getListOrderActm(poserial);
			double discAmt = 0.0;
			String voucherCode = "";
			for(WxOrderActm data : listOrderActm) {
				discAmt += data.getNdiscamt();
				voucherCode = data.getVvouchercode();
				
				Map<String, Object> infoMap = new HashMap<String, Object>();
				infoMap.put("couponid", data.getVactmcode());
				infoMap.put("couponNum", "1");
				infoMap.put("couponMoney", Double.toString(data.getNdiscamt()));
				infoMap.put("type", "1");
				
				// 会员卡支付
				if(ValueCheck.IsEmpty(mapParam.get("vtransactionid"))) {
					JSONObject obj = new JSONObject();
					obj.put("cardCouponid", data.getVactmcode());
					obj.put("cardCouponNum", "1");
					obj.put("cardCouponMoney", Double.toString(data.getNdiscamt()));
					cardJson.add(obj);
				} else {
					list.add(infoMap);
				}
				
				// 会员卡支付时已经调用过。非会员卡支付时，调用电子券核销。
				if(StringUtils.hasText(cardNo) && ValueCheck.IsNotEmpty(mapParam.get("vtransactionid"))) {
					mapParam.put("cardNo", cardNo);
					mapParam.put("voucherCode", data.getVvouchercode());
					try {
						payWithCoupon(mapParam);
					} catch(Exception ex) {
						LogUtil.writeToTxt(LogUtil.CALLWEBSERVICE, "调用电子券消费接口失败，参数【" + mapParam.toString() + "】");
					}
				}
			}
			if(discAmt > 0) {
				mapParam.put("discAmt", discAmt);// 优惠金额
				mapParam.put("cardNo", "");// 会员卡号
				mapParam.put("voucherCode", voucherCode);// 电子券号
				mapParam.put("discpaycode", "");// 优惠对应的支付方式编码
				mapParam.put("discpayname", "");// 优惠对应的支付方式名称
			}
			
			res = wxPayMapper.addPayment(mapParam)+"";
			//将结算数据发送给门店
			Map<String,Object> jsonMap = new HashMap<String,Object>();//mq发送信息map对象
			jsonMap.put("serialid", mapParam.get("poserial"));
			jsonMap.put("resv", listNet_Orders.get(0).getResv());
			jsonMap.put("tables", listNet_Orders.get(0).getTables());
			jsonMap.put("vcode", mapParam.get("firmid"));
			jsonMap.put("dat", listNet_Orders.get(0).getDat());
			jsonMap.put("type", "2");
			
			//20150710 song add
			String isfeast = (String)mapParam.get("isfeast");
			if(isfeast!=null && "2".equals(isfeast)){
				jsonMap.put("type", "9"); //9 外卖下单结算
			}
			//end

			jsonMap.put("visopeninvoice", mapParam.get("visopeninvoice"));//是否开发票
			jsonMap.put("vinvoicetitle", mapParam.get("vinvoicetitle"));//发票抬头
			jsonMap.put("invoicemoney", mapParam.get("cardAmt"));//发票金额
			//如果是开发票并且发票金额不为空
			if("Y".equals(mapParam.get("visopeninvoice")) && ValueCheck.IsNotEmpty(mapParam.get("vinvoicetitle"))){
				//查询该微信下的发票抬头记录
				List<Map<String,Object>>listVinvoicetitle = wxPayMapper.queryVinvoicetitle(mapParam);
				//如果没有查询到本次输入的发票抬头记录，插入到表中
				if(!ValueCheck.IsNotEmptyForList(listVinvoicetitle)){
					wxPayMapper.addVinvoicetitle(mapParam);
				}
			}
			Map<String, Object> map = new HashMap<String, Object>();
			//微信支付
			if(ValueCheck.IsNotEmpty(mapParam.get("vtransactionid"))){
				map = new HashMap<String, Object>();
				map.put("couponid", mapParam.get("paycode")+"");
				map.put("couponNum", "1");
				map.put("couponMoney", mapParam.get("cardAmt")+"");
				map.put("type", "2");
				// 增加交易流水号，用于pos退款  20151221
				map.put("weChatPaySerialid", mapParam.get("vtransactionid") + "");
				map.put("out_trade_no", mapParam.get("out_trade_no") + "");
				list.add(map);
				
				try {
					// 插入支付记录，供对账使用
					mapParam.put("orderType", "3"); //订单类型 3微信支付；4在线充值
					wxPayMapper.addTransactionRec(mapParam);
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}else{//会员卡支付
				//List<Card> listC = CardSearch.listCardByCardno(mapParam.get("cardNo")+"");
				List<Card> listC = CardSearch.listCard(listNet_Orders.get(0).getOpenid());
				String cardReturn = mapParam.get("cardReturn")+"";
				if(ValueCheck.IsEmpty(cardReturn)){
					LogUtil.writeToTxt(LogUtil.TENPAY, "会员结账返回数据为空,卡号："+mapParam.get("cardNo"));
					return "-111";//会员卡消费返回数据为空
				}
				String[] cardRe = cardReturn.split("@");
				map = new HashMap<String, Object>();
				map.put("couponid", mapParam.get("paycode")+"");
				map.put("couponNum", "1");
				map.put("couponMoney", mapParam.get("cardAmt")+"");
				map.put("type", "3");
				map.put("cardIntegralId", "");
				map.put("cardIntegralNum", "");
				map.put("cardIntegralMoney", "");
				map.put("pcardamt", mapParam.get("cardAmt")+"");//储值消费金额
				map.put("pcardid", listC.get(0).getCardId());//卡id--需要查询或者前台传进来
				map.put("pcardno", mapParam.get("cardNo")+"");//卡号
				map.put("pamt", mapParam.get("billAmt")+"");//消费金额 (账单金额)
				map.put("pserial", mapParam.get("poserial")+"");//流水号
				map.put("moneyye", cardRe[3]);//本次储值余额
				map.put("integralye", cardRe[4]);//本次积分余额
				map.put("pcardintegral", cardRe[1]);//积分消费金额
				map.put("pcardmoney", cardRe[0]);//现金消费金额
				map.put("integralzs", cardRe[2]);//本次赠送积分
				map.put("couponcodeonly", mapParam.get("voucherCode")+"");//券编号 多张用，隔开
				map.put("couponmoney", mapParam.get("discAmt")+"");//使用券优惠金额  总和
				map.put("couponname", "");//使用券名称 暂不用
				map.put("cardtypeid", listC.get(0).getTyp());//卡类型
				map.put("cardtypenam", listC.get(0).getTypDes());//卡类型名称
				
				// 添加会员卡优惠券相关信息
				if(null != cardJson && !cardJson.isEmpty()) {
					map.put("cardCoupon", cardJson);
				}
				
				list.add(map);
			}
			
			// 如果是先支付后下单，推送订单信息到POS
			String mustPayBeforeOrder = Commons.getConfig().getProperty("mustPayBeforeOrder");
			if(null != mustPayBeforeOrder && "Y".equals(mustPayBeforeOrder) && !"2".equals(isfeast)){
				// 推送点菜单
				BookDeskController controller = new BookDeskController();
				controller.setBookMealMapper(bookMealMapper);
				controller.setBookDeskMapper(bookDeskMapper);
				controller.updateOrdr(null, null, orders);
			}
			
			jsonMap.put("xiaofeiList", list);
			
			if(isfeast!=null && "2".equals(isfeast)){
				// 推送点菜单
				String orderid = poserial;

				Firm firm = new Firm();
				Net_Orders orders_tt = BuilderOrderMessage.builderOrderMessage(takeOutMapper, bookMealMapper, orderid, "8");
				if(orders_tt != null){
					// 外送执行活动
					double billMoney = Double.parseDouble(orders_tt.getSumprice());
					// 读取优惠活动
					List<Actm> tempList = CampaignTools.getListTakeOutActm(orders_tt.getFirmid());
					// 获取类型为账单减免中，折扣金额最大的活动
					String pkActm = CampaignTools.getMaxDiscountActm(billMoney, tempList);
					// 过滤账单减免活动，仅保留折扣最大的活动
					List<Actm> listActm = CampaignTools.filterRemitCampaign(pkActm, billMoney, tempList, orders_tt.getListNetOrderDtl());
					// 营销活动总减免金额
					double totalDiscount = 0;
					//组装营销活动到pos
					for(Actm actm : listActm) {
						double discountMoney = CampaignTools.getDiscountMoney(billMoney, actm);
						totalDiscount += discountMoney;
						
						map = new HashMap<String, Object>();
						map.put("couponName", actm.getVname());
						map.put("couponMoney", Double.toString(discountMoney));
						map.put("couponid", actm.getVcode());
						map.put("type", "2");
						map.put("couponNum", "1");
						
						((List<Map<String,Object>>)jsonMap.get("xiaofeiList")).add(map);
					}
					
					orders_tt.setSumprice(Double.toString(billMoney - totalDiscount));
					
					orders_tt.setDat(DateFormat.getStringByDate(new Date(), "yyyy-MM-dd"));
					//查询门店
					List<Firm> listFirm = bookMealMapper.getFirmList(null, null, orders_tt.getFirmid());

					if (listFirm != null && !listFirm.isEmpty()) {
						firm =  listFirm.get(0);
						orders_tt.setVcode(firm.getFirmCode());
					}else{
						return "不存在此门店";
					}
					//记录本次记录
					Map<String,Object> mqMap = new HashMap<String,Object>();
					mqMap.put("pk_mqlogs", orders_tt.getSerialid());
					mqMap.put("orderid", orders_tt.getId());
					mqMap.put("vtype", "1");
					mqMap.put("errmsg", "");
					mqMap.put("state", "");
					bookDeskMapper.addMqLogs(mqMap);
					//发送mq消息
					orders_tt.setSerialid(mqMap.get("pk_mqlogs")+"");//重置mq记录的主键
					JSONObject json = JSONObject.fromObject(orders_tt);
					LogUtil.writeToTxt(LogUtil.MQSENDORDER, "下单到MQ服务器中，队列名："+firm.getFirmCode()+"_orderList，订单对象："+json.toString());
					MqSender.sendOrders(json.toString(),firm.getFirmCode()+"_orderList",Commons.mqEffectiveTime);//设置消息有效期为半小时
				}
			}
			MqSender.sendOrders(JSONObject.fromObject(jsonMap).toString(), mapParam.get("firmid")+"_orderList",0);
			LogUtil.writeToTxt(LogUtil.MQSENDORDER, "发送结算信息："+JSONObject.fromObject(jsonMap).toString());
			WxPayController.orderPayFlag.put( listNet_Orders.get(0).getId(),"Y");//将账单支付状态改为支付
			return res;
		}catch(Exception e){
			e.printStackTrace();
			log.error(e);
			LogUtil.writeToTxt(LogUtil.BUSINESS, "处理结算方式时发送错误：" + e.getMessage());
			return "";
		}
	}

	/**
	 * 电子券消费方法
	 * @author ZGL
	 * @date 2015-04-15 11:28:51
	 * @param mapParam
	 * @return 1:成功;-99：消费失败
	 * @throws Exception
	 */
	public String payWithCoupon(Map<String,Object> mapParam) throws Exception{
		String params = "",memInfo="",
					cardNo=mapParam.get("cardNo")+"",
					firmid=mapParam.get("firmid")+"",
					billno=mapParam.get("billno")+"",
					voucherCode=mapParam.get("voucherCode")+"",
					poserial=mapParam.get("poserial")+"";
		
		params = "cardNo="+cardNo+"&firmId="+firmid+"&dateTime="+
				DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss")+"&empNo=999999&empName=微会员支付"+
				"&billno="+billno+"&couponCode="+voucherCode+"&serial="+poserial+"&password=123456";
		LogUtil.writeToTxt(LogUtil.CALLWEBSERVICE, "调用电子券消费接口：参数【" + params + "】");
		memInfo = CallWebService.httpCallWebServiceGet("CRMWebService/cardOutCoupon", params);
		LogUtil.writeToTxt(LogUtil.CALLWEBSERVICE, "调用电子券消费接口：结果【" + memInfo + "】");
		//如果消费电子券接口返回标志为不成功，撤销会员卡支付的金额
		if(!"1".equals(memInfo)){
			params = "cardNo="+cardNo+"&firmId="+firmid+"&dateTime="+
					DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss")+"&empNo=999999&empName=微会员支付"+
					"&serial="+poserial+"&password=123456";
			CallWebService.httpCallWebServiceGet("CRMWebService/cardOutAmtReverse", params);
			memInfo = "-99";
		}else{
			/**START**/
			/**发送优惠券使用成功模版消息 song 20150507**/
			Map<String,String> msgMap = new HashMap<String,String>();
			msgMap.put("templateCode", "OPENTM200972357");
			msgMap.put("openid", (String)mapParam.get("openid"));
			msgMap.put("first", "您有一张优惠券使用成功！");
			msgMap.put("keyword1", "微信优惠券");
			msgMap.put("keyword2", (String)mapParam.get("voucherCode"));
			msgMap.put("keyword3", DateFormat.getStringByDate(new Date(), "yyyy-MM-dd"));
			msgMap.put("remark", "感谢您的使用，请继续关注我们新的优惠活动！");
			
			//根据pk_group，获取配置信息
			Company company = WeChatUtil.getCompanyInfo((String)mapParam.get("pk_group"));
			
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
			
			TemplateMsgUtil.sendTemplateMsg(token.getToken(), msgMap);
			/**END**/
		}
		return memInfo;
	}
	
	/**
	 * 微信支付退款--不用了
	 * 1、调用微信退款接口 
	 * 2、取消电子券消费接口
	 * @author ZGL
	 * @date 2015-04-18 11:26:01
	 */
	@Override
	public String cancelPayWithTenpay(Map<String, Object> mapParam) {
		try{
			//商户号 
		    String partner = Commons.partner;
	
		    //密钥 
		    String key = Commons.partner_key;
		    
		    //创建查询请求对象
		    RefundRequestHandler reqHandler = new RefundRequestHandler(null, null);
		    //通信对象
		    TenpayHttpClient httpClient = new TenpayHttpClient();
		    //应答对象
		    ClientResponseHandler resHandler = new ClientResponseHandler();
		    
		    //-----------------------------
		    //设置请求参数
		    //-----------------------------
		    reqHandler.init();
		    reqHandler.setKey(key);
		    reqHandler.setGateUrl("https://mch.tenpay.com/refundapi/gateway/refund.xml");
		    System.out.println("=-=-=-==-=============1="+DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		    //-----------------------------
		    //设置接口参数
		    //-----------------------------
		    //*****sign***签名将会在 下面的getRequestURL方法 中初始化并放到Parameter里 以下set方法是构建sign的参数的过程
		     reqHandler.setParameter("appid", Commons.appId);
		    reqHandler.setParameter("partner", partner);	//商户号
		    reqHandler.setParameter("out_trade_no", mapParam.get("id")+"");//商户订单号
		    reqHandler.setParameter("transaction_id", mapParam.get("vtransactionid")+"");//财付通订单号
		    reqHandler.setParameter("out_refund_no", mapParam.get("id")+"");	//商户退款单号
		    reqHandler.setParameter("total_fee", mapParam.get("paymoney")+"");	//总金额
		    reqHandler.setParameter("refund_fee", mapParam.get("paymoney")+"");//退款金额
		    reqHandler.setParameter("op_user_id", partner);//	操作员,操作员帐号,默认为商户号
		    //操作员密码,MD5处理 操作员密码,默认为商户后台登录密码
		    reqHandler.setParameter("op_user_passwd", MD5Util.MD5Encode("993801","GBK"));	
		    	
		    reqHandler.setParameter("recv_user_id", "");	//接收人帐号
		    reqHandler.setParameter("reccv_user_name", "");//接收人姓名  
		    System.out.println("=-=-=-==-=============1="+DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		    LogUtil.writeToTxt(LogUtil.TENPAYREFUND, "==微信支付调用回调方法传入的参数===WxPayServiceImpl.class"+new Throwable().getStackTrace()[0].getLineNumber()+"行=="+System.getProperty("line.separator"));
	      //-----------------------------
		    //设置通信参数
		    //-----------------------------
		    //设置请求返回的等待时间
		    httpClient.setTimeOut(5);	
		    //设置ca证书
		    httpClient.setCaInfo(new File("C:/WeChat/cert/apiclient_cert.pem"));
				
		    //设置个人(商户)证书
		    httpClient.setCertInfo(new File("C:/WeChat/cert/apiclient_cert.p12"), partner);
		    
		    //设置发送类型POST
		    httpClient.setMethod("POST");     
		    System.out.println("=-=-=-==-=============1="+DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		    
		    //设置请求内容
		    String requestUrl = reqHandler.getRequestURL();
		    httpClient.setReqContent(requestUrl);
		    String rescontent = "null";
	
		    //后台调用
		    if(httpClient.call()) {
			    System.out.println("=-=-=-==-=============1="+DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		    	//设置结果参数
		    	rescontent = httpClient.getResContent();
		    	resHandler.setContent(rescontent);
		    	resHandler.setKey(key);
		    	   	
		    	//获取返回参数
		    	String retcode = resHandler.getParameter("retcode");
		    	
		    	//判断签名及结果
		    	if(resHandler.isTenpaySign()&& "0".equals(retcode)) {
			    	/*退款状态	refund_status	
						4，10：退款成功。
						3，5，6：退款失败。
						8，9，11:退款处理中。
						1，2: 未确定，需要商户原退款单号重新发起。
						7：转入代发，退款到银行发现用户的卡作废或者冻结了，导致原路退款银行卡失败，资金回流到商户的现金帐号，需要商户人工干预，通过线下或者财付通转账的方式进行退款。
					*/
			    	String refund_status=resHandler.getParameter("refund_status");
			    	String out_refund_no=resHandler.getParameter("out_refund_no");
			    	
			    	System.out.println("商户退款单号"+out_refund_no+"的退款状态是："+refund_status);
			    	 LogUtil.writeToTxt(LogUtil.TENPAYREFUND, "==微信支付退款参数===WxPayServiceImpl.class"+new Throwable().getStackTrace()[0].getLineNumber()+"行=="+"商户退款单号"+out_refund_no+"的退款状态是："+refund_status+System.getProperty("line.separator"));
				      //判断返回的状态 返回提示信息
			    	if("4".equals(refund_status) || "10".equals(refund_status)){
			    		//调用撤销电子券的方法
//			    		String	memInfo = cancalCoupon(mapParam);
			    		return "1";
			    	}else{
			    		return "-1";
			    	}
		    		
		    	} else {
		    		//错误时，返回结果未签名，记录retcode、retmsg看失败详情。
		    		System.out.println("验证签名失败或业务错误");
		    		System.out.println("retcode:" + resHandler.getParameter("retcode")+  " retmsg:" + resHandler.getParameter("retmsg"));
		    		 LogUtil.writeToTxt(LogUtil.TENPAYREFUND, "==微信支付退款参数==验证签名失败或业务错误=WxPayServiceImpl.class"+new Throwable().getStackTrace()[0].getLineNumber()+"行==retcode:" + resHandler.getParameter("retcode")+  " retmsg:" + resHandler.getParameter("retmsg")+System.getProperty("line.separator"));
			     }	
		    } else {
		    	System.out.println("后台调用通信失败");   	
		    	System.out.println(httpClient.getResponseCode());
		    	System.out.println(httpClient.getErrInfo());
		    	//有可能因为网络原因，请求已经处理，但未收到应答。
		    	LogUtil.writeToTxt(LogUtil.TENPAYREFUND, "==微信支付退款参数==后台调用通信失败=WxPayServiceImpl.class"+new Throwable().getStackTrace()[0].getLineNumber()+"行==ResponseCode" + httpClient.getResponseCode()+  "。。。。ErrInfo:" + httpClient.getErrInfo()+System.getProperty("line.separator"));
		    }
		    
		    //获取debug信息,建议把请求、应答内容、debug信息，通信返回码写入日志，方便定位问题
		    System.out.println("http res:" + httpClient.getResponseCode() + "," + httpClient.getErrInfo());
		    System.out.println("req url:" + requestUrl);
		    System.out.println("req debug:" + reqHandler.getDebugInfo());
		    System.out.println("res content:" + rescontent);
		    System.out.println("res debug:" + resHandler.getDebugInfo());
		    LogUtil.writeToTxt(LogUtil.TENPAYREFUND, "==微信支付退款参数==方法最后输入日志=WxPayServiceImpl.class"+new Throwable().getStackTrace()[0].getLineNumber()+"行==http res:" + httpClient.getResponseCode() + "," + httpClient.getErrInfo()+System.getProperty("line.separator"));
		    LogUtil.writeToTxt(LogUtil.TENPAYREFUND, "==微信支付退款参数==方法最后输入日志=WxPayServiceImpl.class"+new Throwable().getStackTrace()[0].getLineNumber()+"行==req url:" + requestUrl+System.getProperty("line.separator"));
		    LogUtil.writeToTxt(LogUtil.TENPAYREFUND, "==微信支付退款参数==方法最后输入日志=WxPayServiceImpl.class"+new Throwable().getStackTrace()[0].getLineNumber()+"行==req debug:" + reqHandler.getDebugInfo()+System.getProperty("line.separator"));
		    LogUtil.writeToTxt(LogUtil.TENPAYREFUND, "==微信支付退款参数==方法最后输入日志=WxPayServiceImpl.class"+new Throwable().getStackTrace()[0].getLineNumber()+"行==res content:" + rescontent+System.getProperty("line.separator"));
		    LogUtil.writeToTxt(LogUtil.TENPAYREFUND, "==微信支付退款参数==方法最后输入日志=WxPayServiceImpl.class"+new Throwable().getStackTrace()[0].getLineNumber()+"行==res debug:" + resHandler.getDebugInfo()+System.getProperty("line.separator"));
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 微信支付退款V3
	 * @author ZGL
	 * @date 2015-04-22 09:36:05
	 * @param mapParam
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String cancelPayWithTenpayV3(Map<String, Object> mapParam) throws Exception {
		//获取商户号及财付通密钥
		String partner = Commons.partner;
		String partner_key = Commons.partner_key;
		//如果门店已设置商户号及密钥，使用门店设置
		String pk_store = (String)mapParam.get("pk_store");
		List<Firm> listFirm = bookMealMapper.getFirmList(null, null, pk_store);
		if(null != listFirm && !listFirm.isEmpty()) {
			Firm firm = listFirm.get(0);
			if(StringUtils.hasText(firm.getVtpaccount())) {
				partner = firm.getVtpaccount();
			}
			if(StringUtils.hasText(firm.getVtpkey())) {
				partner_key = firm.getVtpkey();
			}
		}
		
        SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
        //将金额转换成分
        String money = WeChatUtil.multipliedNum(mapParam.get("paymoney"),100,0);
       parameters.put("appid", Commons.appId);
       parameters.put("mch_id", partner);
       parameters.put("nonce_str", Sha1Util.getNonceStr());
      //在notify_url中解析微信返回的信息获取到 transaction_id，此项不是必填，详细请看上图文档
       parameters.put("out_trade_no", mapParam.get("id")+"");//商户订单号
       parameters.put("transaction_id", mapParam.get("vtransactionid")+"");//财付通订单号
       parameters.put("out_refund_no", mapParam.get("id")+"");	//商户退款单号
       parameters.put("total_fee", money);	//总金额
       parameters.put("refund_fee", money);//退款金额
       parameters.put("op_user_id",partner);
       String orderPar = WeChatUtil.orderParams(parameters);
       String sign = MD5SignUtil.Sign(orderPar, partner_key);
       parameters.put("sign", sign);
       
       String reuqestXml =  WeChatUtil.getRequestXml(parameters);
       LogUtil.writeToTxt(LogUtil.TENPAYREFUND, "微信支付退款调用参数信息："+reuqestXml);
      KeyStore keyStore  = KeyStore.getInstance("PKCS12");
      FileInputStream instream = new FileInputStream(new File(Commons.certPath));//放退款证书的路径
      try {
          keyStore.load(instream, partner.toCharArray());
      } finally {
          instream.close();
      }

      SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, partner.toCharArray()).build();
      SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
              sslcontext,
              new String[] { "TLSv1" },
              null,
              SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
      CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
      try {

          HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/secapi/pay/refund");//退款接口
          
          System.out.println("executing request" + httpPost.getRequestLine());
          StringEntity  reqEntity  = new StringEntity(reuqestXml);
          // 设置类型 
          reqEntity.setContentType("application/x-www-form-urlencoded"); 
          httpPost.setEntity(reqEntity);
          LogUtil.writeToTxt(LogUtil.TENPAYREFUND, "微信支付退款开始调用接口：https://api.mch.weixin.qq.com/secapi/pay/refund");
          CloseableHttpResponse response = httpclient.execute(httpPost);
          try {
              LogUtil.writeToTxt(LogUtil.TENPAYREFUND, "微信支付退款获取接口返回数据开始");
              HttpEntity entity = response.getEntity();

              System.out.println("----------------------------------------");
              System.out.println(response.getStatusLine());
              LogUtil.writeToTxt(LogUtil.TENPAYREFUND, "微信支付退款获取接口调用状态："+response.getStatusLine());
              if (entity != null) {
                  System.out.println("Response content length: " + entity.getContentLength());
                  BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent(),"UTF-8"));
                  String text,resultXmlStr="";
                  while ((text = bufferedReader.readLine()) != null) {
                      System.out.println(text);
                      resultXmlStr += text;
                  }
				LogUtil.writeToTxt(LogUtil.TENPAYREFUND, "微信支付退款返回信息："+resultXmlStr);
      	        Map<String, String> map = XMLUtil.doXMLParse(resultXmlStr);
      	        if("SUCCESS".equals(map.get("result_code"))){
      	        	return "1";
      	        }else{
      	        	return map.get("err_code_des");
      	        }
                 
              }
              EntityUtils.consume(entity);
          } finally {
              response.close();
          }
      } finally {
          httpclient.close();
      }
	LogUtil.writeToTxt(LogUtil.TENPAYREFUND, "微信支付退款方法完毕");
	return "";
	}

	/**
	 * 财付通对账接口-订单查询
	 * @author ZGL
	 * @date 2015-09-21 11:26:01
	 */
	@Override
	public String normalorderquery(Map<String, Object> mapParam) {
		String urll = "http://mch.tenpay.com/cgi-bin/mchdown_real_new.cgi";
		try{
	       String orderPar = "spid="+Commons.partner+"&trans_time="+mapParam.get("dat")+"&stamp="+Sha1Util.getTimeStamp();
	       String sign = MD5SignUtil.Sign(orderPar, Commons.partner_key).toLowerCase();
	       orderPar += "&sign="+sign;
	       String  sss = CallWebService.httpUrlConnection(urll, orderPar);
			return  sss;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public static void main(String[] args) {
		String s= "交易时间,财付通订单号,商家订单号,支付类型,银行订单号,交易状态,订单交易金额,退款单号,退款金额,退款状态,手续费金额,费率,交易说明,\n`2015-09-17 11:35:51, `1233762202351509178345000571, `c8edab60wxp073802560000675391380,财付通余额, `,用户已支付,2.00, `0,0.00,`,0.04,2.00%, 测试外卖  \n`2015-09-17 11:31:40, `1233762202341509178708946881, `c221ee92wxp010402560000675365905,财付通余额, `,用户已支付,36.00, `0,0.00,`,0.72,2.00%, 测试外卖  \n`2015-09-17 11:32:58, `1233762202321509178106956190, `a2f6161bwxp028802560000675378304,光大借记卡快捷支付, `201509178100574014,用户已支付,2.00, `0,0.00,`,0.04,2.00%, 测试外卖  \n\n总交易单数, 总交易金额, 总退款金额, 总手续费金额\n3,40.00,0.00,0.80,\n";
		String[] listS = s.split("\n");
		for(String rs : listS){
			if(ValueCheck.IsNotEmpty(rs)){
				System.out.println(rs);
				String[] ls = rs.split(",");
				for(String lss : ls){
					if("`".equals(lss.trim().substring(0,1))){
						lss = lss.trim().substring(1);
					}
					System.out.println(lss);
				}
			}
		}
	}
}
