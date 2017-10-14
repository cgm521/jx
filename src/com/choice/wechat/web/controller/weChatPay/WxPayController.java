package com.choice.wechat.web.controller.weChatPay;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import sun.misc.BASE64Encoder;

import com.choice.test.domain.Card;
import com.choice.test.domain.Net_OrderDtl;
import com.choice.test.domain.Net_Orders;
import com.choice.test.domain.Voucher;
import com.choice.test.service.CardSearch;
import com.choice.test.service.PubitemSearch;
import com.choice.test.utils.CodeHelper;
import com.choice.test.utils.Commons;
import com.choice.test.utils.DateFormat;
import com.choice.webService.CRMWebservice;
import com.choice.wechat.common.AccessToken;
import com.choice.wechat.common.WeChatUser;
import com.choice.wechat.constants.weChatPay.WxPayConstants;
import com.choice.wechat.domain.bookDesk.Company;
import com.choice.wechat.domain.bookDesk.Firm;
import com.choice.wechat.domain.bookMeal.GroupActm;
import com.choice.wechat.domain.bookMeal.NetDishAddItem;
import com.choice.wechat.domain.bookMeal.NetDishProdAdd;
import com.choice.wechat.domain.campaign.ActmItem;
import com.choice.wechat.domain.myCard.Coupon;
import com.choice.wechat.domain.myCard.NetGoodsOrderDtl;
import com.choice.wechat.domain.myCard.NetGoodsOrders;
import com.choice.wechat.domain.weChatPay.WxOrderActm;
import com.choice.wechat.persistence.WeChatPay.WxPayMapper;
import com.choice.wechat.persistence.bookDesk.BookDeskMapper;
import com.choice.wechat.persistence.bookMeal.BookMealMapper;
import com.choice.wechat.persistence.campaign.CampaignMapper;
import com.choice.wechat.persistence.myCard.MyCardMapper;
import com.choice.wechat.service.WeChatPay.IWxPayService;
import com.choice.wechat.util.CallWebService;
import com.choice.wechat.util.LogUtil;
import com.choice.wechat.util.Sign;
import com.choice.wechat.util.TemplateMsgUtil;
import com.choice.wechat.util.ValueCheck;
import com.choice.wechat.util.WeChatUtil;
import com.choice.wechat.util.mq.MqSender;
import com.choice.wechat.util.tenpay.MdlTemplate;
import com.choice.wechat.util.tenpay.WXOrderQuery;
import com.wxap.util.MD5SignUtil;
import com.wxap.util.Sha1Util;
import com.wxap.util.XMLUtil;

/**
 * 微信支付
 * @author ZGL
 * @Date 2015-04-02 14:46:51
 */
@Controller
@RequestMapping(value="wxPay")
public class WxPayController {

	@Autowired
	private WxPayMapper wxPayMapper;
	@Autowired
	private IWxPayService wxPayService;
	@Autowired
	private BookMealMapper bookMealMapper;
	@Autowired
	private BookDeskMapper bookDeskMapper;
	@Autowired
	private MyCardMapper myCardMapper;
	@Autowired
	private CampaignMapper campaignMapper;
	
	//微信支付返回的流水号暂存对象，用后即删
	private static Map<String,Object> VtransactionidMap = new HashMap<String,Object>();
	public static Map<String,Object> orderPayFlag = new HashMap<String,Object>();
	//会员充值时，存入推荐人的员工号，用后即删
	private static Map<String, Object> empNoRecommendMap=new HashMap<String, Object>();
	/**
	 * 跳转到买单
	 * @param modelMap
	 * @param request
	 * @author ZGL
	 * @Date 2015年4月1日 14:51:39
	 * @return
	 */
	@RequestMapping(value = "toBuyOrderForPay")
	public ModelAndView toBuyOrderForPay(ModelMap modelMap,HttpServletRequest request) {
		try {
			String firmid = request.getParameter("firmid");
			String pk_group = request.getParameter("pk_group");
			String code = request.getParameter("code");

//			Map<String, String> map = new HashMap<String, String>();
//			map.put("openid", request.getParameter("openid"));
//			map.put("firmid", firmid);
//			map.put("orderType", request.getParameter("orderType"));


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
			WeChatUser user = (WeChatUser) request.getSession().getAttribute("WeChatUser");
			if(user == null || user.getOpenid()==null || "".equals(user.getOpenid())){//返回按钮，已取得用户授权的情况
				//通过网页授权方式获取openID
				String oauth2[] = WeChatUtil.getOAuth2(appId, secret, code);
				//获取用户基本信息
				user = WeChatUtil.getWeChatUser(oauth2[0],token.getToken());
				if(StringUtils.hasText(user.getOpenid())){
					request.getSession().setAttribute("WeChatUser", user);
				}
			}
			
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
			modelMap.put("company", company);
			modelMap.put("code", code);
			modelMap.put("pk_group", pk_group);
			modelMap.put("pageFrom", request.getHeader("REFERER"));
			modelMap.put("firmid", firmid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(WxPayConstants.MAIDAN, modelMap);
	}
	/**
	 * 跳转到支付页面
	 * @param modelMap
	 * @param request
	 * @author ZGL
	 * @Date 2015年4月2日 14:51:39
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "toOrderPay")
	public ModelAndView toOrderPay(ModelMap modelMap,HttpServletRequest request,Net_Orders net_Orders,String code, String state) throws Exception {
			String pk_firm = request.getParameter("firmid");
			String pk_group = request.getParameter("pk_group");
			Map<String,Object> firmMap = wxPayMapper.getFirmInfo(pk_firm);
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
			
			String openid = request.getParameter("openid");
			AccessToken token = WeChatUtil.getAccessToken(appId, secret);
			WeChatUser user = (WeChatUser) request.getSession().getAttribute("WeChatUser");
			if(user == null || user.getOpenid()==null || "".equals(user.getOpenid())){//返回按钮，已取得用户授权的情况
				//通过网页授权方式获取openID
				String oauth2[] = WeChatUtil.getOAuth2(appId, secret, code);
				//获取用户基本信息
				user = WeChatUtil.getWeChatUser(oauth2[0],token.getToken());
				if(StringUtils.hasText(user.getOpenid())){
					request.getSession().setAttribute("WeChatUser", user);
					openid = user.getOpenid();
				}
			} else {
				openid = user.getOpenid();
			}
			
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
			
			//-----------------------------------------------查询该门店可用的电子券--------------------------------------------------
			Map<String,Object> mapFirmCoupon = new HashMap<String,Object>();//根据活动设置查询门店可用电子券中间存储器
			List<Map<String,Object>> listFirmCoupon = wxPayMapper.queryCouponByFirm(pk_group, pk_firm, DateFormat.getStringByDate(new Date(), "yyyy-MM-dd"));
			if(ValueCheck.IsNotEmptyForList(listFirmCoupon)){
				for(Map<String,Object> map : listFirmCoupon){
					if(ValueCheck.IsNotEmpty(map.get("vvouchercode"))){
						mapFirmCoupon.put(map.get("vvouchercode").toString(), map.get("vvouchercode"));
					}
				}
			}
			//modelMap.put("listCoupon", mapFirmCoupon);
			//------------------------------------------------查询该用户可用的券-----------------------------------------------------
			//查询该企业定义的电子券基本信息
			/*List<Map<String,Object>> listCouponBase = wxPayMapper.listCouponBase(pk_group);
			Map<String,Object> mapCouponBase = new HashMap<String,Object>();//根据活动设置查询门店可用电子券中间存储器
			if(ValueCheck.IsNotEmptyForList(listCouponBase)){
				for(Map<String,Object> map : listCouponBase){
					if(ValueCheck.IsNotEmpty(map.get("actmcode"))){
						mapCouponBase.put(map.get("vcode").toString(),map);
					}
				}
			}*/
		
		try {
			//---------如果该企业没有搜到电子券 下面的方法就不走了------------------------------------
//			if(ValueCheck.IsNotEmptyForMap(mapCouponBase)){
				//查询微信绑定的会员实体卡
			
			/***20150626 如果Unionid不为空，则用他取值***/
			List<Card> listCardInfo = null;
			if(StringUtils.hasText(user.getUnionid())){
				listCardInfo = CardSearch.listCard(user.getUnionid());
			}else{
				listCardInfo = CardSearch.listCard(openid);
			}
			
			if(listCardInfo!=null && listCardInfo.size()>0){
				Card card = listCardInfo.get(0);
				modelMap.put("cardNo", card.getCardNo());
				modelMap.put("passwd", card.getPasswd());
				modelMap.put("isMember", "Y");
				// 判断此会员卡是否可用于本订单门店
				boolean canUse = CardSearch.isCardTypRestExist(card.getTyp(), firmMap.get("VCODE").toString());
				if(!canUse) {
					modelMap.put("canUseCardPay", "N");
				} else {
					modelMap.put("canUseCardPay", "Y");
				}
			}else{
				modelMap.put("isMember", "N");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String,Object> mapParam = new HashMap<String,Object>();
		System.out.println("==========================="+openid);
		mapParam.put("openid",openid);
		List<Map<String,Object>> listMaps = wxPayMapper.queryVinvoicetitle(mapParam);
		if(ValueCheck.IsNotEmptyForList(listMaps)){
			modelMap.put("vinvoicetitle", listMaps.get(0).get("vinvoicetitle"));
		}
		
		// 重新查询订单数据，以保证数据是最新的
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", net_Orders.getId());
		List<Net_Orders> listOrders = bookMealMapper.getOrderMenus(map);
		Net_Orders order;
		if(null != listOrders && !listOrders.isEmpty()) {
			order = listOrders.get(0);
			// 修改问题：先支付时总金额是从页面传过来的，没有加台位费。修改为非先支付模式时设置总金额
			//order.setSumprice(net_Orders.getSumprice());
			if("Y".equals(Commons.getConfig().getProperty("mustPayBeforeOrder"))) {
				order.setSumprice(order.getMoney());
				//璁㈠崟鑿滃搧鍒楄〃
				List<Net_OrderDtl> orderDtl = PubitemSearch.getOrderDtlMenus(order.getId());
				try {
					//查询可用电子券
					List<Voucher> listCoupon = new ArrayList<Voucher>();
					List<Voucher> tempListCoupon = bookMealMapper.getListCouponInfo(openid, pk_firm);
					for(Voucher item : tempListCoupon) {
						DecimalFormat df = new DecimalFormat("####0.##");
						item.setAmt(df.parse(item.getAmt()).toString());
						if (Double.parseDouble(item.getAmt()) <= 5) {
							item.setPic("5_coupon.png");// 电子券背景图
							item.setFontColor("#AA5C00");// 左侧立减**元字体颜色
						} else if (Double.parseDouble(item.getAmt()) <= 10) {
							item.setPic("10_coupon.png");// 电子券背景图
							item.setFontColor("#FF4E4E");// 左侧立减**元字体颜色
						} else {
							item.setPic("50_coupon.png");// 电子券背景图
							item.setFontColor("#4EAAFF");// 左侧立减**元字体颜色
						}
						
						//活动菜品过滤
						List<ActmItem> listActmItem = campaignMapper.listActmItem(null, item.getActmCode());
						//
						boolean hasItem = false;
						//
						if(null != listActmItem && !listActmItem.isEmpty()) {
							for(ActmItem actmItem : listActmItem) {
								for(Net_OrderDtl dtl : orderDtl) {
									if(dtl.getFoodsid().equals(actmItem.getPk_pubitem())) {
										//订单中包含该菜品时添加
										listCoupon.add(item);
										break;
									}
								}
							}
						} else {
							//如果没有活动
							listCoupon.add(item);
						}
					}
					modelMap.put("listCoupon", listCoupon);
				} catch(Exception ex) {
					// 获取失败，不使用电子券
				}
			} else {
				order.setSumprice(net_Orders.getSumprice());
			}
			
			// 如果是外卖，使用页面传过来的支付金额
			String isfeast = request.getParameter("isfeast");
			if(StringUtils.hasText(isfeast) && "2".equals(isfeast)) {
				modelMap.put("isMember", "Y");
				order.setSumprice(net_Orders.getSumprice());
			}
			
			LogUtil.writeToTxt(LogUtil.BUSINESS, "重新获取订单数据成功。订单号【" + order.getId() + "】，商户订单号【" + order.getOutTradeNo() + "】。订单金额【" 
					+ order.getSumprice() + "】" + System.getProperty("line.separator"));
		} else {
			order = net_Orders;
			LogUtil.writeToTxt(LogUtil.BUSINESS, "重新获取订单数据失败。订单号【" + order.getId() + "】" + System.getProperty("line.separator"));
		}
		
		modelMap.put("pk_group", pk_group);
		modelMap.put("signMap", signMap);
		// 是否服务商模式
		String tenPaySeriveFlag = Commons.getConfig().getProperty("tenPaySeriveFlag");
		if ("0".equals(tenPaySeriveFlag)) {
			// 非服务商模式
			modelMap.put("appId", appId);
		} else {
			// 服务商模式
			modelMap.put("appId", "wx35f67221fe52ad3c");
		}
		modelMap.put("sub_appId", appId);
		modelMap.put("firmId", firmMap.get("VCODE"));
		// 微信支付传递门店名称，以区分哪个门店支付的
		modelMap.put("firmName", firmMap.get("VNAME"));
		modelMap.put("company", company);
		order.setSumprice(WeChatUtil.formatDoubleLength(order.getSumprice(), 2));//格式两位小数
		modelMap.put("net_Orders", order);
		modelMap.put("pk_firm", pk_firm);
		modelMap.put("openid", openid);
		modelMap.put("code", code);
		modelMap.put("pageFrom", request.getHeader("REFERER"));
		modelMap.put("openScan", request.getParameter("openScan"));
		modelMap.put("billstate", request.getParameter("billstate"));//要更新的订单状态
		modelMap.put("mustPayBeforeOrder", Commons.getConfig().getProperty("mustPayBeforeOrder"));
		return new ModelAndView(WxPayConstants.ORDERPAY, modelMap);
	}
	/**
	 * 确认支付页面--微信支付获取预支付id
	 * @param modelMap
	 * @param request
	 * @author ZGL
	 * @Date 2015年4月2日 14:51:39
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "enterPay")
	@ResponseBody
	public String enterPay(ModelMap modelMap,HttpServletRequest request, HttpServletResponse response) throws IOException { 
		try {
			String sign = "";
			String nonce = Sha1Util.getNonceStr();
			String pk_group = request.getParameter("pk_group");
			String code = request.getParameter("code");
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
			
			String openid = request.getParameter("openid");
			
			AccessToken token = WeChatUtil.getAccessToken(appId, secret);
			WeChatUser user = (WeChatUser) request.getSession().getAttribute("WeChatUser");
			if(user == null || user.getOpenid()==null || "".equals(user.getOpenid())){//返回按钮，已取得用户授权的情况
				//通过网页授权方式获取openID
				String oauth2[] = WeChatUtil.getOAuth2(appId, secret, code);
				//获取用户基本信息
				user = WeChatUtil.getWeChatUser(oauth2[0],token.getToken());
				if(StringUtils.hasText(user.getOpenid())){
					request.getSession().setAttribute("WeChatUser", user);
					openid = user.getOpenid();
				}
			} else {
				openid = user.getOpenid();
			}
			
			// 是否服务商模式
			String tenPaySeriveFlag = Commons.getConfig().getProperty("tenPaySeriveFlag");
			
			// 从门店获取商户号信息
			String firmid = request.getParameter("pk_firm");
			// 商户号 
		    String partner = Commons.partner;
			String partner_key = Commons.partner_key;
			LogUtil.writeToTxt(LogUtil.BUSINESS, "获取预支付ID，partner【" + partner + "】，key【" + partner_key + "】");
			List<Firm> listFirm = bookMealMapper.getFirmList(null, null, firmid);
			if(null != listFirm && !listFirm.isEmpty()) {
				Firm firm = listFirm.get(0);
				if(StringUtils.hasText(firm.getVtpaccount())) {
					partner = firm.getVtpaccount();
				}
				if(StringUtils.hasText(firm.getVtpkey())) {
					partner_key = firm.getVtpkey();
				}
			}
			LogUtil.writeToTxt(LogUtil.BUSINESS, "获取预支付ID，partner【" + partner + "】，key【" + partner_key + "】");
			
			// -------------------微信支付 参数获取 预支付号----------------------
			SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
			if("0".equals(tenPaySeriveFlag)) {
				// 非服务商模式
				parameters.put("appid", Commons.appId);
				parameters.put("mch_id", partner);
				parameters.put("openid", openid);
			} else {
				// 服务商模式
				parameters.put("appid", "wx35f67221fe52ad3c");
				parameters.put("mch_id", "1271747601");
				parameters.put("sub_appid", Commons.appId);
				parameters.put("sub_mch_id", partner);
				parameters.put("sub_openid", openid);
			}
			
			String firmName = request.getParameter("firmName");
			String outTradeNo = request.getParameter("outTradeNo");
			String fee = WeChatUtil.formatDoubleLength(request.getParameter("amt"),0);
			String empNoRecommend=request.getParameter("empNoRecommend");
			String notify_url = Commons.notify_url;
			//如果是购买电子券的微信支付
			if("购买电子券".equals(firmName)){
				notify_url = Commons.coupon_notify_url;
				//outTradeNo   cardNo会员卡号     state  date  couponids所有电子券的id以“,”隔开   couponnums每种电子券的数量以“,”隔开      sumpoint积分  sumprice支付金额   vnum积分卡数量
				String cardNo = request.getParameter("cardNo");
				String sumprice = fee;
				String sumpoint = request.getParameter("sumPoint");
				String couponids = request.getParameter("couponids");
				String couponnums = request.getParameter("couponnums");
				String vnum = request.getParameter("vnum");
				wxPayMapper.addCouponItem(outTradeNo,cardNo,openid,pk_group,couponids,couponnums,sumpoint,sumprice,vnum);
			}
			parameters.put("nonce_str",  nonce);
			parameters.put("body", firmName);
			//parameters.put("out_trade_no",  request.getParameter("orderid"));//微信点餐订单号前台传进来
			parameters.put("out_trade_no", outTradeNo);//商户订单号前台传进来
			parameters.put("total_fee", fee);
			parameters.put("spbill_create_ip",WeChatUtil.getIpAddr(request));
			parameters.put("notify_url", notify_url);
			parameters.put("trade_type", "JSAPI");
			if("会员卡充值".equals(firmName)){
				empNoRecommendMap.put(outTradeNo, empNoRecommend);		
			}
			//获取签名
			String orderPar = WeChatUtil.orderParams(parameters);
			LogUtil.writeToTxt(LogUtil.TENPAY, "==微信支付获取预支付ID传入的参数加密===WxPayServiceImpl.class"+new Throwable().getStackTrace()[0].getLineNumber()+"行=="+orderPar+System.getProperty("line.separator"));
			
			if("0".equals(tenPaySeriveFlag)) {
				// 非服务商模式
				sign = MD5SignUtil.Sign(orderPar, partner_key);
			} else {
				// 服务商模式
				sign = MD5SignUtil.Sign(orderPar, "2b954a077b3d40e398b93a76597cc9e7");
			}
		      
			parameters.put("sign", sign);
			String requestXML = WeChatUtil.getRequestXml(parameters);
			LogUtil.writeToTxt(LogUtil.TENPAY, "==微信支付获取预支付ID传入的参数xml格式加sign后===WxPayServiceImpl.class"+new Throwable().getStackTrace()[0].getLineNumber()+"行=="+requestXML+System.getProperty("line.separator"));
			
			String res = WeChatUtil.httpRequestNew(Commons.unified_order_url, "POST", requestXML);
			LogUtil.writeToTxt(LogUtil.TENPAY, "==微信支付获取预支付ID传回的数据===WxPayServiceImpl.class"+new Throwable().getStackTrace()[0].getLineNumber()+"行=="+res+System.getProperty("line.separator"));
			
			Map<String, String> map = XMLUtil.doXMLParseForUTF8(res);//解析微信返回的信息，以Map形式存储便于取值
			LogUtil.writeToTxt(LogUtil.TENPAY, "==微信支付获取预支付ID传回格式化后的数据===WxPayServiceImpl.class"+new Throwable().getStackTrace()[0].getLineNumber()+"行=="+WeChatUtil.orderParams(new TreeMap<Object, Object>(map))+System.getProperty("line.separator"));
			SortedMap<Object,Object> params = new TreeMap<Object,Object>();
			if(ValueCheck.IsNotEmpty(map.get("prepay_id"))){
				System.out.println("================"+map.get("prepay_id"));
				//params.put("appId", Commons.appId);
				if("0".equals(tenPaySeriveFlag)) {
					// 非服务商模式
					params.put("appId", Commons.appId);
				} else {
					// 服务商模式
					params.put("appId", "wx35f67221fe52ad3c");
				}
				params.put("timeStamp", Sha1Util.getTimeStamp());
				params.put("nonceStr",  Sha1Util.getNonceStr());
				params.put("package", "prepay_id="+map.get("prepay_id"));
				params.put("signType", "MD5");//MD5
				if("0".equals(tenPaySeriveFlag)) {
					// 非服务商模式
					params.put("paySign", MD5SignUtil.Sign(WeChatUtil.orderParams(params), partner_key));
				} else {
					// 服务商模式
					params.put("paySign", MD5SignUtil.Sign(WeChatUtil.orderParams(params), "2b954a077b3d40e398b93a76597cc9e7"));
				}
				params.put("sendUrl", notify_url); //付款成功后跳转的页面
		        String userAgent = request.getHeader("user-agent");
		        char agent = userAgent.charAt(userAgent.indexOf("MicroMessenger")+15);
		        params.put("agent", new String(new char[]{agent}));//微信版本号，用于前面提到的判断用户手机微信的版本是否是5.0以上版本。
		        String json = JSONObject.fromObject(params).toString();
		        LogUtil.writeToTxt(LogUtil.TENPAY, "==微信支付返回到前台的数据===WxPayServiceImpl.class"+new Throwable().getStackTrace()[0].getLineNumber()+"行=="+json+System.getProperty("line.separator"));
		        System.out.println("===+++==========="+json);
		        //如果勾选了开发票，更新订单发票头字段
		        if("Y".equals(request.getParameter("visopeninvoice"))){
			        Net_Orders orders = new Net_Orders();
					orders.setId(request.getParameter("orderid"));
					orders.setVinvoicetitle(request.getParameter("vinvoicetitle"));
			        bookDeskMapper.updateOrdr(orders);
			      //查询该微信下的发票抬头记录
			        Map<String,Object> mapParam = new HashMap<String,Object>();
			        mapParam.put("openid", openid);
			        mapParam.put("vinvoicetitle", request.getParameter("vinvoicetitle"));
					List<Map<String,Object>>listVinvoicetitle = wxPayMapper.queryVinvoicetitle(mapParam);
					//如果没有查询到本次输入的发票抬头记录，插入到表中
					if(!ValueCheck.IsNotEmptyForList(listVinvoicetitle)){
						wxPayMapper.addVinvoicetitle(mapParam);
					}
		        }
		        
		        // 保存订单使用的活动信息
		        saveCouponInfo(request,"1");
		        
		        return json;
			}else{
				params.put("errMsg", map.get("return_msg"));
				String json = JSONObject.fromObject(params).toString();
				return  json;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 微信支付成功后点击提示成功页面的按钮进入后台方法插入支付信息及优惠信息
	 * @author ZGL
	 * @date 2015-04-15 11:15:33
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="insertPayInfoForTenpay")
	@ResponseBody
	public String insertPayInfoFroTenpay(ModelMap modelMap,HttpServletRequest request, HttpServletResponse response) {
		try {
			if(ValueCheck.IsEmpty(request.getParameter("poserial"))){
				return "-2";
			}
			LogUtil.writeToTxt(LogUtil.TENPAY, "==账单支付成功标志"+orderPayFlag.get(request.getParameter("poserial"))+"==WxPayController.class"+new Throwable().getStackTrace()[0].getLineNumber()+"行=="+System.getProperty("line.separator"));
			if("Y".equals(orderPayFlag.get(request.getParameter("poserial")))){
				LogUtil.writeToTxt(LogUtil.TENPAY, "==删除账单"+request.getParameter("poserial")+"支付成功标志"+orderPayFlag.get(request.getParameter("poserial"))+"==WxPayController.class"+new Throwable().getStackTrace()[0].getLineNumber()+"行=="+System.getProperty("line.separator"));
				//orderPayFlag.remove(request.getParameter("poserial"));
				return "1";
			}
			String memInfo = "";
			//查询订单是否支付成功
			Map<String,String> map = new HashMap<String,String>();
			map.put("appid", request.getParameter("appid"));
			map.put("mch_id", Commons.partner);
			map.put("transaction_id", "");
			//map.put("out_trade_no", request.getParameter("poserial"));
			map.put("out_trade_no", request.getParameter("outTradeNo"));
			map.put("nonce_str", Sha1Util.getNonceStr());
			map.put("total_fee",WeChatUtil.multipliedNum(request.getParameter("cardAmt"),100,0));//支付金额
			map.put("pk_store", request.getParameter("pk_firm"));
			
			// 如果门店中已设置商户号信息，使用门店设置。否则使用配置文件
			String partner = Commons.partner;
			String partnerKey = Commons.partner_key;
			String firmid = request.getParameter("pk_firm");
			// 是否服务商模式
			String tenPaySeriveFlag = Commons.getConfig().getProperty("tenPaySeriveFlag");
			if(StringUtils.hasText(firmid)) {
				List<Firm> listFirm = bookMealMapper.getFirmList(null, null, firmid);
				if(null != listFirm && !listFirm.isEmpty()) {
					Firm firm = listFirm.get(0);
					if(StringUtils.hasText(firm.getVtpaccount())) {
						partner = firm.getVtpaccount();
					}
					map.put("partner", partner);
					if ("0".equals(tenPaySeriveFlag)) {
						partnerKey = firm.getVtpkey();
						map.put("partner_key", partnerKey);
					}
				}
			}

			// 此处获取accessToken
			String accessToken = WeChatUtil.getAccessToken(Commons.appId, Commons.secret).getToken();
		   LogUtil.writeToTxt(LogUtil.TENPAY, "==微信支付调用回调插入结算方式开始调用查询财付通订单方法===WxPayController.class"+new Throwable().getStackTrace()[0].getLineNumber()+"行=="+JSONObject.fromObject(map)+System.getProperty("line.separator"));
		       
		   	//根据outtradeno查询订单
			String outTradeNo = request.getParameter("outTradeNo");
			List<Net_Orders> orders = bookMealMapper.getListNetOrdersByOutTradeNo(outTradeNo);
			String orderid = "";
			if(null != orders && !orders.isEmpty()) {
				orderid = orders.get(0).getId();
			}
			
			// 此处调用订单查询接口验证是否交易成功
			boolean isSucc = reqOrderquery(map, accessToken, orderid);
			LogUtil.writeToTxt(LogUtil.TENPAY, "==微信支付调用回调插入结算方式开始调用查询财付通订单方法完成===WxPayController.class"+new Throwable().getStackTrace()[0].getLineNumber()+"行=isSucc="+isSucc+System.getProperty("line.separator"));
//			isSucc=true;
			 //如果成功，执行账单结算保存操作
			if(isSucc){
				Map<String,Object> mapParam =  WeChatUtil.TransformMap(request.getParameterMap());
				LogUtil.writeToTxt(LogUtil.TENPAY, "==微信支付调用回调插入结算方式开始调用查询财付通订单方法前台传入流水号"+mapParam.get("vtransactionid")+"==WxPayController.class"+new Throwable().getStackTrace()[0].getLineNumber()+"行=isSucc="+isSucc+System.getProperty("line.separator"));
				//如果流水号不为空
				if(ValueCheck.IsNotEmpty(VtransactionidMap.get(request.getParameter("poserial")))){
					mapParam.put("vtransactionid",  VtransactionidMap.get(request.getParameter("poserial")));
				}
				LogUtil.writeToTxt(LogUtil.TENPAY, "==微信支付调用回调插入结算方式开始调用查询财付通订单方法处理后的传入流水号"+mapParam.get("vtransactionid")+"==WxPayController.class"+new Throwable().getStackTrace()[0].getLineNumber()+"行=isSucc="+isSucc+System.getProperty("line.separator"));
				LogUtil.writeToTxt(LogUtil.TENPAY, "==微信支付调用回调插入结算方式获取vtransactionid===WxPayController.class"+new Throwable().getStackTrace()[0].getLineNumber()+"行=vtransactionid="+VtransactionidMap.get(request.getParameter("poserial"))+System.getProperty("line.separator"));
				if(ValueCheck.IsNotEmpty(mapParam.get("voucherCode"))){
					//memInfo = payWithCoupon(mapParam);
					memInfo = "1";
				}else{
					memInfo = "1";
				}
				//如果支付成功，更新订单状态为完成
				if("1".equals(memInfo)){
					LogUtil.writeToTxt(LogUtil.TENPAY, "==微信支付调用回调查询结算方式开始===WxPayController.class"+new Throwable().getStackTrace()[0].getLineNumber()+"行=="+System.getProperty("line.separator"));
					mapParam.put("paycode",  Commons.vwxpaycode);
					mapParam.put("payname",  Commons.vwxpayname);
					//保存账单支付信息、修改订单状态
					//memInfo = wxPayService.savePayment(mapParam);

//					orderPayFlag.put(request.getParameter("poserial"), "Y");//将账单支付状态改为支付
					
					LogUtil.writeToTxt(LogUtil.TENPAY, "==微信支付调用回调查询结算方式完成===WxPayController.class"+new Throwable().getStackTrace()[0].getLineNumber()+"行=="+System.getProperty("line.separator"));
					if(!"1".equals(memInfo)){//更新订单状态失败，撤销会员卡支付，电子券消费
//						memInfo = wxPayService.cancelPayWithCard(mapParam);
						memInfo = "1";
					}
					
					VtransactionidMap.remove(request.getParameter("poserial"));//删除该订单对应的流水号
				}
			}else{
				return "-1";
			}
			return memInfo;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 会员卡支付
	 * @param modelMap
	 * @param request
	 * @param net_Orders
	 * @param code
	 * @param state
	 * @author ZGL
	 * @Date 2015-04-08 16:47:44
	 * @return
	 */
	@RequestMapping(value = "payWithCard")
	@ResponseBody
	public String payWithCard(ModelMap modelMap,HttpServletRequest request, Net_Orders net_Orders,String code, String state) {
		try {
			
			String firmid = request.getParameter("firmid");
			String pk_firm = request.getParameter("pk_firm");
			String pk_group = request.getParameter("pk_group");
			String cardNo = request.getParameter("cardNo");
			String billno = request.getParameter("billno");
			String pax = request.getParameter("pax");
			String cardAmt = request.getParameter("cardAmt");
			String discAmt = request.getParameter("discAmt");
			String billAmt = request.getParameter("billAmt");
			String poserial = request.getParameter("poserial");
			String voucherCode = request.getParameter("voucherCode");
			String passwd = request.getParameter("passwd");
			Map<String,Object> mapParam =  WeChatUtil.TransformMap(request.getParameterMap());
			//查询会员支付方式
//			List<Map<String, Object>> paytypList = wxPayMapper.queryCardPaytyp(pk_group);
//			Map<String, Object> paytypMap = new HashMap<String, Object>();
			if(ValueCheck.IsEmpty(Commons.vcardpaycode)){
				return "-101";
			}
			//会员卡支付
			String params = "cardNo="+cardNo+"&firmId="+firmid+"&fenAmt=0.0&cardAmt="+cardAmt+"&dateString="+
					DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss")+"&invoiceAmt=0.0&empNo=999999&empName=微会员支付&billAmt="+billAmt+
					"&conacct="+billno+"&discAmt="+discAmt+"&billno="+billno+"&pax=1&paytypeCode="+Commons.vcardpaycode+"&paytyp="+
					Commons.vcardpayname+"&jifenAmt=" + cardAmt + "&posid=0" +
					"&sft=1&serial="+poserial+"&password="+passwd+"&dworkdate="+DateFormat.getStringByDate(new Date(), "yyyy-MM-dd");
			LogUtil.writeToTxt(LogUtil.CALLWEBSERVICE, "调用会员支付，参数：" + params);
			String memInfo = CallWebService.httpCallWebServiceGet("CRMWebService/cardOutAmt", params);
			LogUtil.writeToTxt(LogUtil.CALLWEBSERVICE, "调用会员支付，返回结果：" + memInfo);
			//会员卡支付返回数据格式：消费金额@赠送金额@赠送积分@卡余额@卡内剩余积分
			//会员卡支付成功并且电子券编码不为空，调用电子券消费接口
			if(memInfo.split("@").length==5){
				mapParam.put("cardReturn", memInfo);
				memInfo = "1";
			}else{
				return memInfo;
			}
	        
	        // 保存订单使用的活动信息
	        saveCouponInfo(request,"2");
	        
			//如果会员卡支付成功并且选择了电子券，调用电子消费方法
			if("1".equals(memInfo) && ValueCheck.IsNotEmpty(voucherCode)){
				memInfo = payWithCoupon(mapParam);
			}
			//如果支付成功，更新订单状态为完成
			if("1".equals(memInfo)){
				mapParam.put("paycode",  Commons.vcardpaycode);
				mapParam.put("payname",  Commons.vcardpayname);
				//保存账单支付信息、修改订单状态
				memInfo = wxPayService.savePayment(mapParam);
				if(!"1".equals(memInfo)){//更新订单状态失败，撤销会员卡支付，电子券消费
					memInfo = wxPayService.cancelPayWithCard(mapParam);
					memInfo = "-100";
				}
				/**START**/
				if("1".equals(memInfo)){
					/**发送会员消费成功模版消息 song 20150507**/
					String cardReturn = (String) mapParam.get("cardReturn");
					String cardRe[] = cardReturn.split("@");
					
					String openid = request.getParameter("openid");
					
					String isfeast = request.getParameter("isfeast");
					
					String url = "";
					if(isfeast!=null && "2".equals(isfeast)){
						url = buildTempMsgUrl(request,poserial,openid,isfeast);//外卖消费消息
					}else{
						url = buildTempMsgUrl(request,poserial,openid);
					}
					
					String firmName = bookDeskMapper.getFirmName(pk_firm);
					
					Map<String,String> msgMap = new HashMap<String,String>();
					msgMap.put("templateCode", "OPENTM201054648");
					msgMap.put("openid", openid);
					msgMap.put("url", url);
					msgMap.put("first", "您好，您的会员卡消费成功！");
					msgMap.put("keyword1", cardAmt==null||"".equals(cardAmt)?"0元":WeChatUtil.formatDoubleLength(cardAmt, 2));
					msgMap.put("keyword2", cardRe[2]);
					msgMap.put("keyword3", firmName==null?"":firmName);
					msgMap.put("keyword4", cardRe[3]);
					msgMap.put("keyword5", cardRe[4]);
					msgMap.put("remark", "感谢您的使用，更多信息请点击详情查看！");
					
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
					
					TemplateMsgUtil.sendTemplateMsg(token.getToken(), msgMap);
				}
				/**END**/
			}
			return memInfo;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return  "";
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
		memInfo = CallWebService.httpCallWebServiceGet("CRMWebService/cardOutCoupon", params);
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
	 * 取消会员卡支付
	 * @param modelMap
	 * @param request
	 * @param net_Orders
	 * @param code
	 * @param state
	 * @author ZGL
	 * @Date 2015-04-08 16:47:44
	 * @return
	 */
	@RequestMapping(value = "cancelPayWithCard")
	@ResponseBody
	public String cancelPayWithCard(ModelMap modelMap,HttpServletRequest request) {
		try {
			Map<String,Object> mapParam = WeChatUtil.TransformMap(request.getParameterMap());
			String memInfo = wxPayService.cancelPayWithCard(mapParam);
			return memInfo;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return  "";
	}
	/**
	 * 微信支付退款
	 * @param modelMap
	 * @param request
	 * @author ZGL
	 * @Date 2015-04-08 16:47:44
	 * @return
	 */
	@RequestMapping(value = "cancelPayWithTenpay")
	@ResponseBody
	public String cancelPayWithTenpay(ModelMap modelMap,HttpServletRequest request) {
		try {
			Map<String,Object> mapParam = WeChatUtil.TransformMap(request.getParameterMap());
			String memInfo = wxPayService.cancelPayWithTenpay(mapParam);
			return memInfo;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return  "";
	}
	/**
	 * 微信支付接口回调方法，根据返回的数据处理后续操作
	 * @param modelMap
	 * @param request
	 * @param response
	 * @param scene_id
	 * @param openid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="notifyPay")
	public void wechatPaySuccess(ModelMap modelMap,HttpServletRequest request, HttpServletResponse response,
			String scene_id,String openid){
		try {
	        System.out.println("~~~~~~~~~~~~~~~微信支付回调方法开始~~~~~~~~~");
	        
	        String channelOrderNum=request.getParameter("channelOrderNum");//渠道订单号（如未用到不需关注） 
	        String errorDetail=request.getParameter("errorDetail");//错误描述 
	        String goodsInfo=request.getParameter("goodsInfo");
	        String attach=request.getParameter("attach");
	        String inscd=request.getParameter("inscd");
	        String mchntid=request.getParameter("mchntid");
	        String orderNum=request.getParameter("orderNum");
	        String respcd=request.getParameter("respcd");
	        String txamt=request.getParameter("txamt");
	        String consumerAccount=request.getParameter("consumerAccount");
	        String sign=request.getParameter("sign");
	        String attach_openid=attach;
			PrintWriter out = response.getWriter();
	        InputStream inStream = request.getInputStream();
	       // ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
            LogUtil.writeToTxt(LogUtil.TENPAY, "==微信支付调用回调方法notifyPay传入的参数===WxPayController.clas"
            		+ "------channelOrderNum="+channelOrderNum+"--errorDetail="+errorDetail+"-"
            		+ "goodsInfo="+goodsInfo+"-attach="+attach+"-"
            		+ "inscd="+inscd+"-mchntid="+mchntid+"-orderNum="+orderNum+"-"
            		+ "respcd="+respcd+"-txamt="+txamt+"-consumerAccount="+consumerAccount+"-"
            		+ "respcd="+respcd+"-sign="+sign+"-------WxPayController.class="+
            		new Throwable().getStackTrace()[0].getLineNumber()+"行=="+System.getProperty("line.separator"));
            String result = request.getQueryString();
	      /*  byte[] buffer = new byte[1024];
	        int len = 0;
	        while ((len = inStream.read(buffer)) != -1) {
	            outSteam.write(buffer, 0, len);
	        }
	        outSteam.close();
	        inStream.close();*/
	       // String result  = new String(outSteam.toByteArray(),"utf-8");
	        LogUtil.writeToTxt(
					LogUtil.TENPAY,
					"==微信支付调用回调方法notifyPay传入的参数===" + result+"-------WxPayController.class="+
					new Throwable().getStackTrace()[0].getLineNumber()+"行=="+ System.getProperty("line.separator"));

	        // 此处获取accessToken
			String accessToken = WeChatUtil.getAccessToken(Commons.appId, Commons.secret).getToken();
			
			/*//根据outtradeno查询订单
			String outTradeNo = map.get("out_trade_no");*/
			String outTradeNo = orderNum;//商户订单号
			List<Net_Orders> orders = bookMealMapper.getListNetOrdersByOutTradeNo(outTradeNo);
			String orderid = "";
			String pk_store = "";
			if(null != orders && !orders.isEmpty()) {
				orderid = orders.get(0).getId();
				pk_store = orders.get(0).getFirmid();
			}else {
				// 根据商户订单号查询不到账单时，根据openid查询账单
				Map<String, String> param = new HashMap<String, String>();
				param.put("openid", attach_openid);
				param.put("state1", "6");
				param.put("state2", "7");
				param.put("dat", DateFormat.getStringByDate(new Date(),	"yyyy-MM-dd"));
				List<Net_Orders> listNet_Orders = bookMealMapper.getOrderMenus(param);
				if(null != listNet_Orders && !listNet_Orders.isEmpty()) {
					orderid = listNet_Orders.get(0).getId();
					pk_store = listNet_Orders.get(0).getFirmid();
					LogUtil.writeToTxt(LogUtil.TENPAY, "==微信支付调用回调方法notifyPay根据商户订单号查询不到数据===WxPayController.class="+new Throwable().getStackTrace()[0].getLineNumber()+"行==查到的订单orderid="+orderid+"--pk_store="+pk_store+System.getProperty("line.separator"));
				}
			}
			
			if(null != orderid && !"".equals(orderid)) {
				// 如果门店中已设置商户号信息，使用门店设置。否则使用配置文件
				/*String partner = Commons.partner;
				String partnerKey = Commons.partner_key;
				// 是否服务商模式
				String tenPaySeriveFlag = Commons.getConfig().getProperty("tenPaySeriveFlag");
				if(StringUtils.hasText(pk_store)) {
					List<Firm> listFirm = bookMealMapper.getFirmList(null, null, pk_store);
					if(null != listFirm && !listFirm.isEmpty()) {
						Firm firm = listFirm.get(0);
						if(StringUtils.hasText(firm.getVtpaccount())) {
							partner = firm.getVtpaccount();
						}
						map.put("partner", partner);
						if ("0".equals(tenPaySeriveFlag)) {
							partnerKey = firm.getVtpkey();
							map.put("partner_key", partnerKey);
						}
					}
				}
				
				// 此处调用订单查询接口验证是否交易成功
				boolean isSucc = reqOrderquery(map, accessToken, orderid);
				*/
				// 此处调用订单查询接口验证是否交易成功
				if (!"00".equals(respcd)) {
					SortedMap<Object, Object> signParams = new TreeMap<Object, Object>();
					signParams.put("version", "2.0");
					signParams.put("signType", "SHA256");
					signParams.put("charset", "utf-8");
					signParams.put("origOrderNum", orderNum);
					signParams.put("txndir", "Q");
					signParams.put("busicd", "INQY");
					signParams.put("inscd", Commons.getConfig().getProperty("inscd"));
					signParams.put("mchntid", mchntid);
					signParams.put("terminalid", Commons.terminalid);
					signParams.put("outOrderNum", orderNum);
					
					String orderStr = orderParams(signParams) + Commons.getConfig().getProperty("xulian_sign_key");//xulian_sign_key
					String querysign = Sha1Util.getSha256(orderStr);

					JSONObject obj = new JSONObject();
					obj.put("version", "2.0");
					obj.put("signType", "SHA256");
					obj.put("charset", "utf-8");
					obj.put("origOrderNum", orderNum);
					obj.put("txndir", "Q");
					obj.put("busicd", "INQY");
					obj.put("inscd", Commons.getConfig().getProperty("inscd"));
					obj.put("mchntid", mchntid);
					obj.put("terminalid", Commons.terminalid);
					obj.put("outOrderNum", orderNum);
					obj.put("sign", querysign);
					
					String uri = "http://test.quick.ipay.so/scanpay/unified";
					
					try{
						String queryres = WeChatUtil.executeHttpMethod(uri, obj);
						JSONObject resobj = JSONObject.fromObject(queryres);
						respcd = resobj.getString("respcd");
					} catch(Exception ex) {
						ex.printStackTrace();
					}
				}
				
				// 支付成功，商户处理后同步返回给微信参数
				if (!"00".equals(respcd)) {
					// 支付失败
					System.out.println("支付失败");
				} else {
					System.out.println("===============付款成功==============");
					// ------------------------------
					// 处理业务开始
					// ------------------------------
					// ------------------------------
					// 处理业务完毕

					// ------------------------------
					// 给财付通返回成功信息，防止财付通后台定时发送成功指令
					/*String noticeStr = setXML("SUCCESS", "");
					// out.print(new
					// ByteArrayInputStream(noticeStr.getBytes(Charset.forName("UTF-8"))));
					out.write(noticeStr);
					LogUtil.writeToTxt(
							LogUtil.TENPAYREFUND,
							"notifyPay通知财付通已收到通知输入参数：" + noticeStr
									+ System.getProperty("line.separator"));*/

					// 返回页面支付成功提示信息
					modelMap.put("amt",	WeChatUtil.dividedNum(txamt, 100, 2));// 支付金额
					modelMap.put("vtransactionid", channelOrderNum);// 财付通支付流水号

					/** START **/
					/** 发送支付成功模版消息 song 20150506 **/
					Map<String, String> msgMap = new HashMap<String, String>();
					msgMap.put("templateCode", "OPENTM201642055");
					msgMap.put("openid", attach_openid);
					// msgMap.put("url",
					// buildTempMsgUrl(request,map.get("out_trade_no"),map.get("openid")));
					msgMap.put("url", buildTempMsgUrl(request, orderid, attach_openid));

					/** 查询订单详情 **/
					Map<String, String> queryMap = new HashMap<String, String>();
					// queryMap.put("id", map.get("out_trade_no"));
					queryMap.put("id", orderid);
					queryMap.put("openid",attach_openid );//request.getParameter("openid")
					List<Net_Orders> listNet_Orders = bookMealMapper.getOrderMenus(queryMap);

					String storeName = "";// 商店名称
					String resv = "";// 订单编号
					if (listNet_Orders != null && listNet_Orders.size() > 0) {
						storeName = listNet_Orders.get(0).getFirmdes();
						resv = listNet_Orders.get(0).getResv();

						// 是否外卖订单
						if ("2".equals(listNet_Orders.get(0).getIsfeast())) {
							msgMap.put("url", buildTempMsgUrl(request, orderid, attach_openid, "2"));
						}

						if (!"Y".equals(orderPayFlag.get(listNet_Orders.get(0).getId()))) {
							// 改为渠道订单号 将流水号暂存，保存订单结算信息时用
							VtransactionidMap.put(orderid, channelOrderNum);//map.get("transaction_id")

							LogUtil.writeToTxt(
									LogUtil.TENPAY,
									"==微信支付调用回调方法notifyPay更新订单支付开始===WxPayController.class"
											+ new Throwable().getStackTrace()[0]
													.getLineNumber()
											+ "行=="
											+ System.getProperty("line.separator"));
							// 将支付方式插入到数据中
							Map<String, Object> paramMap = new HashMap<String, Object>();
							paramMap.put("appid", Commons.appId);
							paramMap.put("billno", listNet_Orders.get(0).getResv());// 订单号
							paramMap.put("resv", listNet_Orders.get(0).getResv());// 账单号
							paramMap.put("poserial", listNet_Orders.get(0).getId());
							//如果是先支付，最终支付金额应该取微信返回的支付金额
							if("Y".equals(Commons.getConfig().getProperty("mustPayBeforeOrder"))) {
								paramMap.put("cardAmt", WeChatUtil.dividedNum(txamt,100,2));//最终支付金额
							} else {
								paramMap.put("cardAmt", listNet_Orders.get(0).getPaymoney());//最终支付金额
							}
							paramMap.put("discAmt", "0.00");// 优惠金额
							paramMap.put("billAmt", listNet_Orders.get(0).getSumprice());// 账单金额
							paramMap.put("cardNo", "");// 会员卡号
							paramMap.put("voucherCode", "");// 电子券号
							paramMap.put("discpaycode", "");// 优惠对应的支付方式编码
							paramMap.put("discpayname", "");// 优惠对应的支付方式名称
							paramMap.put("billstate", "6");// 订单的状态
							paramMap.put("openid", attach_openid);// 20150507
																		// song
																		// add
							if (ValueCheck.IsNotEmpty(listNet_Orders.get(0).getVinvoicetitle())) {
								paramMap.put("visopeninvoice", "Y");// 开发票
							} else {
								paramMap.put("visopeninvoice", "N");// 不开开发票
							}
							paramMap.put("vinvoicetitle", listNet_Orders.get(0).getVinvoicetitle());// 发票抬头
							paramMap.put("firmid", listNet_Orders.get(0).getVcode());// 门店编码
							paramMap.put("pk_firm", listNet_Orders.get(0).getFirmid());// 门店主键
							paramMap.put("vtransactionid",channelOrderNum );//改为订单渠道号  财付通流水号暂存账单主键，后台会自动处理 map.get("transaction_id")
							paramMap.put("paycode", Commons.vwxpaycode);
							paramMap.put("payname", Commons.vwxpayname);
							paramMap.put("isfeast", listNet_Orders.get(0).getIsfeast());// 订单类型 20151022 song
							//POS推送增加商户订单号
							paramMap.put("out_trade_no", orderNum);//map.get("out_trade_no")
							// 保存账单支付信息、修改订单状态
							wxPayService.savePayment(paramMap);//TODO
							LogUtil.writeToTxt(
									LogUtil.TENPAY,
									"==微信支付调用回调方法notifyPay更新订单支付状态容器开始===WxPayController.class"
											+ new Throwable().getStackTrace()[0]
													.getLineNumber()
											+ "行=="
											+ System.getProperty("line.separator"));
							// orderPayFlag.put(
							// listNet_Orders.get(0).getId(),"Y");//将账单支付状态改为支付
							LogUtil.writeToTxt(
									LogUtil.TENPAY,
									"==微信支付调用回调方法notifyPay更新订单支付完成===WxPayController.class"
											+ new Throwable().getStackTrace()[0]
													.getLineNumber()
											+ "行=="
											+ System.getProperty("line.separator"));

							// 返回页面提示信息
							msgMap.put("first", "您好，微信支付已成功！");
							msgMap.put("keyword1", resv);
							msgMap.put("keyword2", WeChatUtil.dividedNum(txamt, 100, 2));
							msgMap.put("keyword3", storeName);
							msgMap.put("keyword4", DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
							msgMap.put("remark", "感谢您的光临，更多信息请点击详情查看！");

							TemplateMsgUtil.sendTemplateMsg(accessToken, msgMap);
							
							// 插入会员消费记录
							String usedOpenid =attach_openid; //map.get("openid");
							// 是否服务商模式
							/*if ("1".equals(tenPaySeriveFlag)) {
								// 服务商模式
								usedOpenid = map.get("sub_openid");
							}*/
							List<Card> listCard = CardSearch.listCard(usedOpenid);
							
							if (listCard != null && listCard.size() > 0) {
								String firmid = listNet_Orders.get(0).getVcode();
								String cardNo = listCard.get(0).getCardNo();
								String billno = listNet_Orders.get(0).getId();
								String cardAmt = "0";
								String discAmt = "0";
								String billAmt = (String)paramMap.get("cardAmt");
								String poserial = listNet_Orders.get(0).getId();
								String passwd = listCard.get(0).getPasswd();
								String dataStr = DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss");
								
								//会员卡支付
								String params = "cardNo="+cardNo+"&firmId="+firmid+"&fenAmt=0.0&cardAmt="+cardAmt+"&dateString="+
										dataStr+"&invoiceAmt=0.0&empNo=999999&empName=微会员支付&billAmt="+billAmt+
										"&conacct="+billno+"&discAmt="+discAmt+"&billno="+billno+"&pax=1&paytypeCode="+
										Commons.vcardpaycode+"&paytyp="+Commons.vcardpayname+"&jifenAmt=0&posid=0" +
										"&sft=1&serial="+poserial+"&password="+passwd;
								try{
									String memInfo = CallWebService.httpCallWebServiceGet("CRMWebService/cardOutAmt", params);
									LogUtil.writeToTxt(LogUtil.CALLWEBSERVICE, "插入会员记录参数：【" + params + "】结果：" + memInfo);
								} catch(Exception ex) {
									LogUtil.writeToTxt(LogUtil.CALLWEBSERVICE, "插入会员记录结果失败");
								}
								
								// 计算赠送积分
								params = "cardNo=" + cardNo + "&firmId=" + firmid + "&dateString=" + dataStr
										+ "&billAmt=" + listNet_Orders.get(0).getSumprice() + "&realAmt=" + billAmt + "&type=0";
								try {
									String res = CallWebService.httpCallWebServiceGet("CRMWebService/findGiftFen", params);
									LogUtil.writeToTxt(LogUtil.CALLWEBSERVICE, "查询赠送积分参数：【" + params + "】结果：" + res);
									
									if(StringUtils.hasText(res) && Double.parseDouble(res) > 0) {
										// 赠送积分
										params = "cardNo=" + cardNo + "&firmId=" + firmid + "&dateTime=" + dataStr
												+ "&empNo=999&empName=微信支付赠积分&fen=" + res + "&serial=" + poserial;
										res = CallWebService.httpCallWebServiceGet("CRMWebService/cardInPoint", params);
										LogUtil.writeToTxt(LogUtil.CALLWEBSERVICE, "赠送积分参数：【" + params + "】结果：" + res);
									}
								} catch(Exception ex) {
									LogUtil.writeToTxt(LogUtil.CALLWEBSERVICE, "赠送积分失败");
								}
								
								try {
									// 调用满赠功能
									int cardId = Integer.parseInt(listCard.get(0).getCardId());
									LogUtil.writeToTxt(LogUtil.CALLWEBSERVICE, "调用满赠功能参数：【cardId:" + cardId
											+ "】；【金额：" + billAmt + "】；【门店编码：" + firmid + "】；【流水号：" + poserial + "】");
									myCardMapper.callGiftVoucher(cardId, Double.parseDouble(billAmt), firmid, poserial);
								} catch(Exception ex) {
									LogUtil.writeToTxt(LogUtil.CALLWEBSERVICE, "调用满赠功能失败");
								}
							}
						} else {
							// orderPayFlag.remove(request.getParameter("poserial"));
						}
					} else {
						// 查不到订单，充值记录
						Map<String, Object> paramMap = new HashMap<String, Object>();
						paramMap.put("vtransactionid",
								channelOrderNum);//改为渠道订单号  map.get("transaction_id") 财付通流水号暂存账单主键，后台会自动处理
						paramMap.put("poserial", orderNum); // 订单号map.get("out_trade_no")
						paramMap.put("cardAmt", WeChatUtil.dividedNum(
								txamt, 100, 2));// 支付金额
						paramMap.put(
								"firmid",
								Commons.getConfig().getProperty(
										"virtualStoreCode")); // 门店编号
						paramMap.put("orderType", "4"); // 订单类型 3微信支付；4在线充值
						try {
							wxPayMapper.addTransactionRec(paramMap);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
					/** END **/
				}
			} else {
				String transactionId =channelOrderNum;// map.get("transaction_id");
				
				// 如果此请求已处理过，不再处理
				String flag = (String)orderPayFlag.get(transactionId);
				if(!"Y".equals(flag)) {
					// 设置为已处理
					orderPayFlag.put(transactionId, "Y");
					// 改为渠道订单号  商户订单号，做为充值流水号
					String poserial = channelOrderNum;//map.get("out_trade_no");
					
					//out.write("Can not found order");
					LogUtil.writeToTxt(LogUtil.TENPAY, "==微信支付调用回调方法notifyPay未查询到订单===WxPayController.class"+new Throwable().getStackTrace()[0].getLineNumber()+"行=="+result+System.getProperty("line.separator"));
					String noticeStr = setXML("SUCCESS", "");
					out.write(noticeStr);
					
					//调用CRM充值存储过程
					String chargeOpenid =attach_openid ;//map.get("openid");
					// 是否服务商模式
					/*String tenPaySeriveFlag = Commons.getConfig().getProperty("tenPaySeriveFlag");
					if ("1".equals(tenPaySeriveFlag)) {
						// 服务商模式
						chargeOpenid = map.get("sub_openid");
					}		*/		
					//获得集合中存入的推荐人员工号
					String empNoRecommend=(String) empNoRecommendMap.get(outTradeNo);	
					empNoRecommendMap.remove(empNoRecommend);
					String empName=empNoRecommend;
					if("".equals(empNoRecommend)||empNoRecommend.isEmpty()){
						empNoRecommend="1";//会员充值时，默认的推荐员工号位1
						empName="微信充值";
					}
					String res = charge(chargeOpenid, WeChatUtil.dividedNum(txamt, 100, 2), poserial,empNoRecommend,empName);
					if("1".equals(res)) {
						// 充值成功，增加充值记录
						Map<String, Object> paramMap = new HashMap<String, Object>();
						paramMap.put("vtransactionid", transactionId);// 财付通流水号暂存账单主键，后台会自动处理
						paramMap.put("poserial", poserial); // 订单号
						paramMap.put("cardAmt", WeChatUtil.dividedNum(txamt, 100, 2));// 支付金额
						paramMap.put("firmid", Commons.getConfig().getProperty("firmId")); // 门店编号
						paramMap.put("orderType", "4"); // 订单类型 3微信支付；4在线充值
						try {
							wxPayMapper.addTransactionRec(paramMap);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String setXML(String return_code, String return_msg) {
		return "<xml><return_code><![CDATA[" + return_code + "]]></return_code><return_msg><![CDATA[" + return_msg + "]]></return_msg></xml>";
	}
	
	/**
	 * 调用CRM充值存储过程
	 * @param openid
	 * @param chargeMoney
	 * @return
	 */
	public String charge(String openid, String chargeMoney, String poserial,String empNoRecommend,String empName) {
		DecimalFormat df = new DecimalFormat("####0.00");
		List<Card> listCard = CardSearch.listCard(openid);
		if(listCard != null && !listCard.isEmpty()) {
			Card c=listCard.get(0);
			double giftAmt = calcGiftAmt(c.getCardId(), chargeMoney);
			String result = CardSearch.charge(Integer.parseInt(c.getCardId()), Double.parseDouble(chargeMoney) +  giftAmt, Double.parseDouble(c.getzAmt()), 
					empNoRecommend, Commons.firmId, "100", 0, Double.parseDouble(chargeMoney), Double.parseDouble(chargeMoney), giftAmt, 
					Commons.payMentCode, Commons.payMent, DateFormat.getStringByDate(new Date(), "yyyy-MM-dd"), empName, empName, 
					DateFormat.getStringByDate(new Date(), "yyyyMMddHHmmss"), "0", "0", 0, poserial);
			
			if("1".equals(result)) {
				// 充值成功，推送微信消息
				String accessToken = WeChatUtil.getAccessToken(Commons.appId, Commons.secret).getToken();
				Map<String,String> param = new HashMap<String,String>();
				param.put("templateCode", "OPENTM201061496");
				param.put("first", "充值成功！");
				param.put("keyword1", chargeMoney);
				param.put("keyword2", "微信充值");
				param.put("keyword3", df.format(Double.parseDouble(c.getzAmt()) + Double.parseDouble(chargeMoney)));
				param.put("remark", "会员名称：" + c.getName() + "\n充值时间：" + DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
				
				param.put("openid", openid);
				TemplateMsgUtil.sendTemplateMsg(accessToken, param);
				
				return "1";
			}
		}
		return "-1";
	}
	
	public Double calcGiftAmt(String cardId, String chargeMoney) {
		String chargeTime = DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss");
		double giftAmt = myCardMapper.calcGiftAmt(cardId, chargeTime, Double.parseDouble(chargeMoney));
		
		return giftAmt;
	}
	
	/**
	 * 请求订单查询接口
	 * @param map
	 * @param accessToken
	 * @param orderid
	 * @return
	 * @throws Exception 
	 */
	public static boolean reqOrderquery(Map<String, String> map, String accessToken, String orderid) throws Exception {
		WXOrderQuery orderQuery = new WXOrderQuery();
		orderQuery.setAppid(map.get("appid"));
		orderQuery.setMch_id(map.get("mch_id"));
		orderQuery.setTransaction_id(map.get("transaction_id"));
		orderQuery.setOut_trade_no(map.get("out_trade_no"));
		orderQuery.setNonce_str(map.get("nonce_str"));
		orderQuery.setPartner(map.get("partner"));
		
		//此处需要密钥PartnerKey，此处直接写死，自己的业务需要从持久化中获取此密钥，否则会报签名错误
		// 是否服务商模式
		String tenPaySeriveFlag = Commons.getConfig().getProperty("tenPaySeriveFlag");
		if ("0".equals(tenPaySeriveFlag)) {
			// 非服务商模式
			orderQuery.setPartnerKey(map.get("partner_key"));
		} else {
			// 服务商模式
			orderQuery.setPartnerKey("2b954a077b3d40e398b93a76597cc9e7");
		}
		
		Map<String, String> orderMap = orderQuery.reqOrderquery();
		
		VtransactionidMap.put(orderid,orderMap.get("transaction_id"));//删除该订单对应的流水号
		
		LogUtil.writeToTxt(LogUtil.TENPAY, "==查询微信支付后中间变量值：订单号："+orderid+"，流水号："+map.get("transaction_id")+"===WxPayController.class"+new Throwable().getStackTrace()[0].getLineNumber()+"行=="+System.getProperty("line.separator"));
		LogUtil.writeToTxt(LogUtil.TENPAYREFUND, "查询财付通账单支付记录输入参数："+JSONObject.fromObject(orderQuery)+"；返回数据："+JSONObject.fromObject(orderMap)+System.getProperty("line.separator"));
		
		//此处添加支付成功后，支付金额和实际订单金额是否等价，防止钓鱼

		if (orderMap.get("return_code") != null && orderMap.get("return_code").equalsIgnoreCase("SUCCESS")) {
			if (orderMap.get("trade_state") != null && orderMap.get("trade_state").equalsIgnoreCase("SUCCESS")) {
				String total_fee = map.get("total_fee");
				String order_total_fee = orderMap.get("total_fee");
				if (Integer.parseInt(order_total_fee) >= Integer.parseInt(total_fee)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 支付成功后发送支付成功的模版消息
	 * @param accessToken
	 * @param openid
	 * @param orderid
	 * @return
	 */
	public boolean sendTemplateMsg(String accessToken, String openid, String orderid) {
		MdlTemplate tempMsg = new MdlTemplate();
		tempMsg.addItem("remark", "欢迎再次购买！", "#4169E1");
		tempMsg.addItem("expDate", "2015年12月24日", "#4169E1");
		tempMsg.addItem("number", "1份", "#4169E1");
		tempMsg.addItem("name", "赞助费", "#4169E1");
		tempMsg.addItem("productType", "商品名", "#4169E1");
		tempMsg.setTemplate_id("5ODqfQRJl-dpMf8s-S9TNDeQo4DATDJZDhUvr1gOAcU");
		tempMsg.setTopcolor("#7CFC00");
		tempMsg.setTouser(openid);
		//此处构造支付订单详情页面地址，此处demo不做详细处理，实际项目中需要处理

		tempMsg.setUrl("/order?orderid=" + orderid);
		JSONObject obj = JSONObject.fromObject(tempMsg);
		System.out.println("obj=" + obj);
		// 此处调用发送模版消息的API

		/*
		 * JSONObject jo = WeixinUtil.sendTemplate(accessToken, tempMsg);
		 * System.out.println("jo===" + jo); 
		 * if (jo != null && jo.getInt("errcode") == 0) {
		 *  	return true; 
		 *  }
		 * System.out.println("========发送模版消息失败=========");
		 */
		return false;
	}
	//跳转到微信商城
	@RequestMapping(value="myWxxd")
	public ModelAndView myWxxd(ModelMap modelMap){
		return new ModelAndView(WxPayConstants.MYWXXD, modelMap);
	}
	
	/**
	 * 
	 * @param request
	 * @param orderid 订单号
	 * @param openid 微信号
	 * @return
	 */
	private String buildTempMsgUrl(HttpServletRequest request, String orderid, String openid){
		StringBuffer sb = request.getRequestURL();
		String baseUrl = sb.substring(0, sb.lastIndexOf("/"));
		baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf("/"));
		sb.delete(0, sb.length());
		sb.append(baseUrl)
			.append("/bookMeal/orderDetail.do?orderid=").append(orderid)
			.append("&openid=").append(openid)
			.append("&orderTyp=0");
		
		return sb.toString();
	}
	
	/**
	 * 外卖
	 * @param request
	 * @param orderid 订单号
	 * @param openid 微信号
	 * @return
	 */
	private String buildTempMsgUrl(HttpServletRequest request, String orderid, String openid, String isfeast){
		StringBuffer sb = request.getRequestURL();
		String baseUrl = sb.substring(0, sb.lastIndexOf("/"));
		baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf("/"));
		sb.delete(0, sb.length());
		sb.append(baseUrl)
			.append("/takeout/orderDetail.do?orderid=").append(orderid)
			.append("&openid=").append(openid)
			.append("&orderTyp=2");
		
		return sb.toString();
	}
	/**
	 * 获取订单需支付状态
	 * @param request
	 * @param orders
	 * @return
	 */
	@RequestMapping(value="getOrderState")
	@ResponseBody
	private Object getOrderState(HttpServletRequest request, Net_Orders orders){
		
		//如果订单主键、订单号不为空，查询门店是否推送结算信息
		if(ValueCheck.IsNotEmpty(orders.getId()) && ValueCheck.IsNotEmpty(orders.getResv())){
			Map<String, Object> folioPaymoney = wxPayMapper.getFolioPaymoney(orders);
			if(ValueCheck.IsNotEmptyForMap(folioPaymoney)){
				if(ValueCheck.IsNotEmpty(folioPaymoney.get("paymoney"))) {
					DecimalFormat df = new DecimalFormat("####0.00");
					String paymoney = df.format(folioPaymoney.get("paymoney"));
					folioPaymoney.put("PAYMONEY", paymoney);
					folioPaymoney.put("SUMPRICE", df.format(folioPaymoney.get("sumprice")));
					folioPaymoney.put("zengsong", WeChatUtil.formatNumForListResult(wxPayMapper.listPreferentialZs(orders), "nzmoney", 2));//赠送
					folioPaymoney.put("tuicai", WeChatUtil.formatNumForListResult(wxPayMapper.listPreferentialTc(orders), "nzmoney", 2));//退菜
					folioPaymoney.put("zhekou", WeChatUtil.formatNumForListResult(wxPayMapper.listPreferentialZk(orders), "nrefundamt", 2));//折扣
					folioPaymoney.put("OUTTRADENO", folioPaymoney.get("outTradeNo"));
					LogUtil.writeToTxt(LogUtil.BUSINESS, "folioPaymoney:"+folioPaymoney+System.getProperty("line.separator"));
					return folioPaymoney;
				}
			}
		}
		return "";
	}
	/**
	 * 订单确认界面
	 * @param modelMap
	 * @param orders
	 * @return
	 */
	@RequestMapping(value="toConfirmPage")
	private ModelAndView toConfirmPage(ModelMap modelMap, Net_Orders orders,String code){
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", orders.getId());
		map.put("firmid", orders.getFirmid());
		List<Net_Orders> listNet_Orders = bookMealMapper.getOrderMenus(map);

		List<Firm> listFirm = bookMealMapper.getFirmList(null, null, orders.getFirmid());
		List<Net_OrderDtl> orderDtl = PubitemSearch.getOrderDtlMenus(orders.getId());

		if (listNet_Orders != null && !listNet_Orders.isEmpty()) {
			modelMap.put("orderHead", listNet_Orders.get(0));
		}
		if (listFirm != null && !listFirm.isEmpty()) {
			modelMap.put("firm", listFirm.get(0));
		}
		modelMap.put("orderDtl", orderDtl);
		modelMap.put("orderDtlSize", orderDtl.size());
		
		// 获取菜品附加项列表
		List<NetDishAddItem> listNetDishAddItem = bookMealMapper.getDishAddItemList("", orders.getId());
		// 获取附加产品列表
		List<NetDishProdAdd> listNetDishProdAdd = bookMealMapper.getDishProdAddList("", orders.getId());

		double totalPrice = 0.0;
		for (Net_OrderDtl dtl : orderDtl) {
			if (null != dtl.getPrice() && !"".equals(dtl.getPrice())) {
				totalPrice += Double.parseDouble(dtl.getPrice()) * Integer.parseInt(dtl.getFoodnum());
			}
			
			// 将附加项添加到菜品下
			List<NetDishAddItem> tempList = new ArrayList<NetDishAddItem>();
			for (NetDishAddItem item : listNetDishAddItem) {
				if (dtl.getId().equals(item.getPk_orderDtlId())) {
					tempList.add(item);
					if(null != item.getNprice() && !"".equals(item.getNprice())) {
						totalPrice += Double.parseDouble(item.getNprice()) * Integer.parseInt(dtl.getFoodnum());
					}
				}
			}
			if (!tempList.isEmpty()) {
				dtl.setListDishAddItem(tempList);
			}
			
			// 将附加产品添加到菜品下
			List<NetDishProdAdd> addList = new ArrayList<NetDishProdAdd>();
			for(NetDishProdAdd item : listNetDishProdAdd) {
				if (dtl.getId().equals(item.getPk_orderDtlId()) && dtl.getFoodsid().equals(item.getPk_pubitem())) {
					addList.add(item);
					if(null != item.getNprice() && !"".equals(item.getNprice())) {
						totalPrice += Double.parseDouble(item.getNprice()) * Integer.parseInt(dtl.getFoodnum());
					}
				}
			}
			if (!addList.isEmpty()) {
				dtl.setListDishProdAdd(addList);
			}
		}
		if (null != listNet_Orders && !listNet_Orders.isEmpty()) {
			DecimalFormat df = new DecimalFormat("####0.00");
			modelMap.put("totalPrice", df.format(totalPrice));
			modelMap.put("orderDetail", listNet_Orders.get(0));
			modelMap.put("paymoney", df.format(Double.parseDouble(listNet_Orders.get(0).getPaymoney())));
		}
		modelMap.put("zengsongList", WeChatUtil.formatNumForListResult(wxPayMapper.listPreferentialZs(orders), "nzmoney", 2));//赠送
		modelMap.put("tuicaiList", WeChatUtil.formatNumForListResult(wxPayMapper.listPreferentialTc(orders), "nzmoney,price", 2));//退菜
		modelMap.put("zhekouList", WeChatUtil.formatNumForListResult(wxPayMapper.listPreferentialZk(orders), "nrefundamt", 2));//折扣
		modelMap.put("otherInfo", WeChatUtil.formatNumForListResult(wxPayMapper.listPreferentialOtherInfo(orders), "nrefundamt", 2));//其他异常信息
		modelMap.put("groupInfo", WeChatUtil.formatNumForListResult(wxPayMapper.getGroupRecord(orders.getId()), "nderatenum", 2));//其他异常信息
		modelMap.put("code", code);
		return new ModelAndView(WxPayConstants.CONFIRMPAGE, modelMap);
	}
	/**
	 * 跳转到我要结账等待界面
	 * @param modelMap
	 * @param orders
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="toCheckout")
	public ModelAndView toCheckout(HttpServletRequest request, ModelMap modelMap,Net_Orders orders, String resv,String code, String state) throws Exception{
		// 获取参数
		String couponStrArray = request.getParameter("couponStr");
		String[] infoArray = couponStrArray.split(":");
		String orderId = infoArray[0];
		// 设定我要结帐的状态
		Map<String, String> userInfoMap = WeChatUtil.getUserInfoMap();
		userInfoMap.put(orderId + "_checkOut", "Y");
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", orderId);
		List<Net_Orders> listNet_Orders = bookMealMapper.getOrderMenus(map);
		orders = listNet_Orders.get(0);
		List<Firm> listFirm = bookMealMapper.getFirmList(null, null, orders.getFirmid());
		String pk_group = request.getParameter("pk_group");
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
		WeChatUser user = (WeChatUser) request.getSession().getAttribute("WeChatUser");
		if(user == null || user.getOpenid()==null || "".equals(user.getOpenid())){//返回按钮，已取得用户授权的情况
			//通过网页授权方式获取openID
			String oauth2[] = WeChatUtil.getOAuth2(appId, secret, code);
			//获取用户基本信息
			user = WeChatUtil.getWeChatUser(oauth2[0],token.getToken());
			if(StringUtils.hasText(user.getOpenid())){
				request.getSession().setAttribute("WeChatUser", user);
			}
		}
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
		
		/***20150626 如果Unionid不为空，则用他取值***/
		List<Card> listCard = null;
		if(StringUtils.hasText(user.getUnionid())){
			listCard = CardSearch.listCard(user.getUnionid());
		}else{
			listCard = CardSearch.listCard(user.getOpenid());
		}
		
		String couponJson = "";
		if(listCard!=null && listCard.size()>0){
			// 处理电子券信息
			String couponStr = "";
			JSONArray couponArray = new JSONArray();
			if(infoArray.length > 1) {
				couponStr = infoArray[1];
				if(null != couponStr && !couponStr.isEmpty()) {
					String[] subInfo = couponStr.split(";");
					Map<String,String> couponMap = new HashMap<String,String>();
					List<Map<String,String>> listCoupon = new ArrayList<Map<String,String>>();
//					String ifCountinue="1";//1 继续往下走，0-直接进入下一次循环
					String couponcodeonly ="";//电子券唯一编码集合
					//循环处理选择的电子券
					for(String info : subInfo) {
						if(null != info && !info.isEmpty()) {
							String[] value = info.split("_");
							couponcodeonly += ","+value[3];
//							if(listCoupon!=null && listCoupon.size()>0){
//								for(Map<String,String> mapCoupon : listCoupon){
//									if(value[2].equals(mapCoupon.get("cardCouponid"))){
//										mapCoupon.put("cardCouponNum", WeChatUtil.formatDoubleLength(WeChatUtil.stringPlusDouble(mapCoupon.get("cardCouponNum"),1).toString(),0));
//										ifCountinue = "0";
//									}
//								}
//							}
//							if("0".equals(ifCountinue)){//找到记录了，继续下次循环
//								continue;
//							}
							couponMap = new HashMap<String,String>();
							couponMap.put("cardCouponid", value[2]);
							couponMap.put("cardCouponNum", "1");
							couponMap.put("cardCouponMoney", value[1]);
							listCoupon.add(couponMap);
							
//							JSONArray subArray = new JSONArray();
//							JSONObject cardCouponObj = new JSONObject();
//							cardCouponObj.put("cardCouponid", value[0]);
//							cardCouponObj.put("cardCouponNum", "1");
//							cardCouponObj.put("cardCouponMoney", value[1]);
//							subArray.add(cardCouponObj);
						}
					}
					JSONObject obj = new JSONObject();
					obj.put("couponid", "");
					obj.put("couponNum", "");
					obj.put("couponMoney", "");
					obj.put("type", "3");
					if(listCoupon!=null && listCoupon.size()>0){
						obj.put("cardCoupon", listCoupon.toString());
					}else{
						obj.put("cardCoupon", "");
					}
					obj.put("cardIntegralId", "");
					obj.put("cardIntegralNum", "");
					obj.put("cardIntegralMoney", "");
					obj.put("pcardamt", "");
					obj.put("pcardid", listCard.get(0).getCardId());
					obj.put("cardpassword", listCard.get(0).getPasswd()+"");
					obj.put("pcardno", listCard.get(0).getCardNo());
					obj.put("pamt", "");
					obj.put("pserial", "");
					obj.put("moneyye", "");
					obj.put("integralye", "");
					obj.put("pcardintegral", "");
					obj.put("pcardmoney", "");
					obj.put("integralzs", "");
					obj.put("couponcodeonly", ValueCheck.IsEmpty(couponcodeonly)==true?"":couponcodeonly.substring(1));
					obj.put("couponname", "");
					obj.put("cardtypeid", listCard.get(0).getTyp());
					obj.put("cardtypenam", listCard.get(0).getTypDes());
					couponArray.add(obj);
				}
				couponJson = ",\"xiaofeiList\":" + couponArray.toString();
			}
		}
		
		//处理团购券信息
		//20151027 song add
		String codeData = "";
		String resultGroup = "";//处理后的结果
		if(infoArray.length > 2) {
			codeData = infoArray[2];
			//v_FD4AE2EE8A7C4D4B9B83_2001_3_100_00=852369,v_AEC6E59A20DD4F65AA6F_30012_1_80_00=123456,v_AEC6E59A20DD4F65AA6F_30012_1_80_00=58693
			if(codeData!=null && !"".equals(codeData)){
				codeData = codeData.replaceAll("v_", "");
				String[] codes = codeData.split(",");
				List<GroupActm> groupList = new ArrayList<GroupActm>();
				for(String group:codes){
					if(group!=null && !"".equals(group)){
						try{
							GroupActm actm = new GroupActm();
							actm.setGroupcode(group.substring(group.indexOf("=")+1));
							group = group.substring(0, group.indexOf("="));
							String temp[] = group.split("_");
							actm.setPk_actm(temp[0]);
							actm.setVcode(temp[1]);
							actm.setCouponcode(temp[2]);
							actm.setnDerateNum(temp[3]+"."+temp[4]);
							groupList.add(actm);
						}catch(Exception e){
							System.out.println("团购券格式错误");
							e.printStackTrace();
						}
					}
				}
				JSONArray groupArray = new JSONArray();
				groupArray.addAll(groupList);
				resultGroup = ",\"groupList\":" + groupArray.toString();
			}
		}
		//end
		
		String states = "",json="";
		//发送我要结账请求
		if(ValueCheck.IsNotEmpty(orders.getId())){
			json = "{\"serialid\":\""+orders.getId()+"\",\"resv\":\""+orders.getResv()+"\"" + couponJson + resultGroup + ",\"type\":\"4\",\"vcode\":\""+listFirm.get(0).getFirmCode()
					+"\",\"dat\":\""+orders.getDat()+"\"}";
			try {
				MqSender.sendOrders(json,listFirm.get(0).getFirmCode()+"_orderList",0);
				states =  "ok";
			} catch (Exception e) {
				e.printStackTrace();
				states = "发送消息失败，错误信息："+e.getCause().getMessage();
			}
		}
		LogUtil.writeToTxt(LogUtil.MQSENDORDER, "我要结帐信息到MQ服务器中，队列名：" + listFirm.get(0).getFirmCode() + "_orderList，订单对象：" + json.toString()+",发送状态："+state);
//		return state;
		modelMap.put("orders",orders);
		modelMap.put("checkoutmsg", Commons.checkoutmsg);
		modelMap.put("code", code);
		modelMap.put("signMap", signMap);
		modelMap.put("appId", appId);
		return new ModelAndView(WxPayConstants.CHECKOUTTOPAY, modelMap);
	}
	/**
	 * 更新订单状态
	 * @param request
	 * @param orders
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="updateOrdr")
	@ResponseBody
	public Object updateOrdr(HttpServletRequest request, Net_Orders orders) throws Exception{
		if(ValueCheck.IsNotEmpty(orders.getId()) && ValueCheck.IsNotEmpty(orders.getState())){
			String cnt = bookDeskMapper.updateOrdr(orders);
			if("1".equals(cnt)){
				Map<String,String> messageMap = new HashMap<String,String>();
				String token = WeChatUtil.getAccessToken(Commons.appId,Commons.secret).getToken();
				String openid = request.getParameter("openid");
				messageMap.put("templateCode", "OPENTM213722270");//微信的模版编号
				messageMap.put("openid",  openid);
				messageMap.put("first","");
				messageMap.put("keyword1","订单"+orders.getResv()+"支付完毕，点击查看详情。");
				messageMap.put("keyword2",Commons.vtitle);
				messageMap.put("keyword3",DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
				messageMap.put("remark","感谢您的使用，更多信息请点击详情查看！");
				messageMap.put("url",buildTempMsgUrl(request,orders.getId(),openid));
				//发送消息到微信
				TemplateMsgUtil.sendTemplateMsg(token,messageMap);
				
				return "ok";
			}
		}
		return "false";
	}
	
	/**
	 * 发起我要结帐时更新paymoney为0，否则多次发起时只能取到上一次的数据
	 * @param request
	 */
	@RequestMapping(value="resetPayMoney")
	@ResponseBody
	public String resetPayMoney(HttpServletRequest request) {
		String orderId = request.getParameter("orderid");
		// 发起我要结帐时更新paymoney为空，否则多次发起时只能取到上一次的数据
		Net_Orders temp = new Net_Orders();
		temp.setPaymoney("");
		temp.setId(orderId);
		LogUtil.writeToTxt(LogUtil.BUSINESS, "重置paymoney，订单id：" + orderId);
		bookDeskMapper.resetPayMoeny(temp);
		LogUtil.writeToTxt(LogUtil.BUSINESS, "重置paymoney成功，");
		return "1";
	}

	/**
	 *财付通对账接口-订单查询
	 * @param modelMap
	 * @param request
	 * @author ZGL
	 * @Date 2015-09-21 11:00:00
	 * @return
	 */
	@RequestMapping(value = "normalorderquery")
	@ResponseBody
	public String normalorderquery(ModelMap modelMap,HttpServletRequest request) {
		try {
			Map<String,Object> mapParam = WeChatUtil.TransformMap(request.getParameterMap());
			String memInfo = wxPayService.normalorderquery(mapParam);
			return memInfo;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return  "";
	}
	
	/**
	 * 发送我要结帐请求到POS
	 * @param request
	 * @param orders
	 * @return
	 */
	@RequestMapping(value = "sendPayRequestToPOS")
	@ResponseBody
	public String sendPayRequestToPOS(HttpServletRequest request, Net_Orders orders) {
		String states = "",json="";
		//发送我要结账请求
		if(ValueCheck.IsNotEmpty(orders.getId())){
			json = "{\"serialid\":\""+orders.getId()+"\",\"resv\":\""+orders.getResv()+"\",\"type\":\"4\",\"vcode\":\"" + orders.getVcode()
					+"\",\"dat\":\""+orders.getDat()+"\"}";
			try {
				MqSender.sendOrders(json, orders.getVcode() + "_orderList",0);
				states =  "ok";
			} catch (Exception e) {
				e.printStackTrace();
				states = "发送消息失败，错误信息："+e.getCause().getMessage();
			}
		}
		LogUtil.writeToTxt(LogUtil.MQSENDORDER, "我要结帐信息到MQ服务器中，队列名：" + orders.getVcode() + "_orderList，订单对象：" + json.toString()+",发送状态："+states);
		
		return states;
	}
	
	/**
	 * 保存订单使用的活动
	 * @param orderActm
	 */
	public void saveCouponInfo(HttpServletRequest request,String type) {
		WxOrderActm orderActm = new WxOrderActm();
		
        String actmCode = request.getParameter("actmCode");
        String voucherCode = request.getParameter("voucherCode");
        String ordersid = request.getParameter("orderid");
        if(StringUtils.hasText(actmCode) && StringUtils.hasText(voucherCode)) {
    		try {
    			 double discamt = 0.0D;
    		      if ("1".equals(type))
    		        discamt = Double.parseDouble(WeChatUtil.dividedNum(request.getParameter("discAmt"), Integer.valueOf(100), 2));
    		      else {
    		        discamt = Double.parseDouble(request.getParameter("discAmt"));
    		      }
	    		orderActm.setPk_orderactm(CodeHelper.createUUID());
	    		orderActm.setVordersid(ordersid);
	    		orderActm.setVactmcode(actmCode);
	    		orderActm.setVvouchercode(voucherCode);
	    		orderActm.setNdiscamt(discamt);
	    		orderActm.setNcnt(1);
	    		orderActm.setVtype("");
	    		
        		// 保存之前先删除，防止支付失败和取消支付时数据重复
        		wxPayMapper.deleteOrderActm(orderActm);
        		
        		// 保存订单使用的活动
        		wxPayMapper.saveOrderActm(orderActm);
    		} catch(Exception ex) {
    			LogUtil.writeToTxt(LogUtil.BUSINESS, "保存订单活动信息失败：" + ex.getMessage());
    		}
        }
	}
	
	/**
	 * 购买电子券微信支付的回调方法
	 * */
	@RequestMapping(value="notifycouponpay")
	public void wechatPaySuccess(ModelMap modelMap,HttpServletRequest request, HttpServletResponse response){
		try {
	        System.out.println("~~~~~~~~~~~~~~~微信支付回调方法开始~~~~~~~~~");
			PrintWriter out = response.getWriter();
	        InputStream inStream = request.getInputStream();
	        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
	        byte[] buffer = new byte[1024];
	        int len = 0;
	        while ((len = inStream.read(buffer)) != -1) {
	            outSteam.write(buffer, 0, len);
	        }
	        outSteam.close();
	        inStream.close();
	        String result  = new String(outSteam.toByteArray(),"utf-8");
            LogUtil.writeToTxt(LogUtil.TENPAY, "==微信支付调用回调方法notifyPay传入的参数===WxPayController.class"+new Throwable().getStackTrace()[0].getLineNumber()+"行=="+result+System.getProperty("line.separator"));
	        //将返回的参数格式话
	        Map<String, String> map = XMLUtil.doXMLParse(result);
	        for(Object keyValue : map.keySet()){
	            System.out.println("==========="+keyValue+"="+map.get(keyValue));
	        }
			String outTradeNo = map.get("out_trade_no");
			
			String noticeStr = setXML("SUCCESS", "");
			// out.print(new
			// ByteArrayInputStream(noticeStr.getBytes(Charset.forName("UTF-8"))));
			out.write(noticeStr);
			LogUtil.writeToTxt(
					LogUtil.TENPAYREFUND,
					"notifyPay通知财付通已收到通知输入参数：" + noticeStr
							+ System.getProperty("line.separator"));
			//修改记录状态
			wxPayMapper.updateCouponItem(outTradeNo);
			//查询记录
			List<Map<String,Object>> list = wxPayMapper.queryWxCouponItem(outTradeNo);
			Map<String,Object> couponsmap = list.get(0);
			String[] couponids = ((String)couponsmap.get("couponids")).split(",");
			String[] couponnums = ((String)couponsmap.get("couponnums")).split(",");
			String cardNo = (String)couponsmap.get("cardno");
			String sumpoint = (String)couponsmap.get("sumpoint");
			String sumprice = (String)couponsmap.get("sumprice");
			String vnum = (String)couponsmap.get("vnum");
			String openid = (String)couponsmap.get("openid");
			String pk_group = (String)couponsmap.get("pk_group");
			//查询所有电子券
			List<Coupon> couponList = myCardMapper.getCouponList(null);
			NetGoodsOrders order = new NetGoodsOrders();
			order.setState("2");
			order.setPaymentType("0");
			order.setOpenid(openid);
			order.setSumPrice(Double.parseDouble(sumprice));
			order.setSumPoint(Integer.parseInt(sumpoint));
			order.setPk_group(pk_group);
			order.setCustId(cardNo);
			List<NetGoodsOrderDtl> ln = new ArrayList<NetGoodsOrderDtl>();
			for(Coupon c : couponList){
				for(int i =0;i<couponids.length;i++){
					if(couponids[i].equals(c.getPk_id())){
						NetGoodsOrderDtl n = new NetGoodsOrderDtl();
						n.setGoodsid(couponids[i]);
						n.setGoodsname(c.getVname());
						n.setGoodsnum(couponnums[i]);
						n.setPrice(c.getNprice());
						n.setTotalprice(Double.parseDouble(c.getNprice())*Integer.parseInt(couponnums[i])+"");
						String nfen = c.getNfen();
						if("".equals(nfen) || nfen == null){
							nfen = "0";
						}
						n.setPayType(Integer.parseInt(nfen)>0?"2":"1");
						n.setCouponCode(c.getVcode());
						ln.add(n);
					}
				}
			}
			order.setListNetGoodsOrderDtl(ln);
			//saveGoodsOrder
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
				
				// 计算积分购买张数
				int cnt = Integer.parseInt(vnum);
//				for(NetGoodsOrderDtl dtl : order.getListNetGoodsOrderDtl()) {
//					if("2".equals(dtl.getPayType())) {
//						cnt += Integer.parseInt(dtl.getGoodsnum());
//					}
//				}
				
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
			
		}catch(Exception e){
			LogUtil.writeToTxt(LogUtil.BUSINESS,"电子券赠券失败！");
		}
	}
	
	
	@RequestMapping(value = "xunlian_enterPay")
	@ResponseBody
	public String xunlian_enterPay(ModelMap modelMap,HttpServletRequest request, HttpServletResponse response) throws IOException { 
		try {
			String outTradeNo = request.getParameter("outTradeNo");
			String fee = formatNumberLength(request.getParameter("amt"));
			String openid=request.getParameter("openid");
			String notify_url = Commons.notify_url;
			String mchntid = Commons.mchntid;
			String terminalid = Commons.terminalid;
			String xulian_sign_key = Commons.getConfig().getProperty("xulian_sign_key");
			// 从门店获取商户id
			String firmid = request.getParameter("pk_firm");
			LogUtil.writeToTxt(LogUtil.BUSINESS, "获取预支付ID，mchntid【" + mchntid + "】");
			List<Firm> listFirm = bookMealMapper.getFirmList(null, null, firmid);
			if(null != listFirm && !listFirm.isEmpty()) {
				Firm firm = listFirm.get(0);
				if(StringUtils.hasText(firm.getVtpaccount())) {
					mchntid = firm.getVtpaccount();//迅联的商户id等于门店里商户id
				}
				if(StringUtils.hasText(firm.getVtpkey())) {
					xulian_sign_key = firm.getVtpkey();
				}
			}
			LogUtil.writeToTxt(LogUtil.BUSINESS, "获取预支付ID，mchntid【" + mchntid + "】");
			SortedMap<Object, Object> signParams = new TreeMap<Object, Object>();
			signParams.put("version", "2.0");
			signParams.put("signType", "SHA256");
			signParams.put("charset", "utf-8");
			signParams.put("attach", openid);
			signParams.put("busicd","WPAY");
			signParams.put("backUrl", notify_url);
			signParams.put("chcd", "WXP");
			signParams.put("frontUrl", "status.html");
			signParams.put("mchntid", mchntid);//100000000000203
			signParams.put("orderNum", outTradeNo);
			signParams.put("txamt",fee);
			signParams.put("terminalid", terminalid);//cssj0001
			
			String orderStr = orderParams(signParams) + xulian_sign_key;//xulian_sign_key
			String sign = Sha1Util.getSha256(orderStr);
			System.out.println("@@@@@@@orderStr:" + orderStr);
			System.out.println("@@@@@@@sign:" + sign);
			
			JSONObject obj = new JSONObject();
			obj.put("version", "2.0");
			obj.put("signType", "SHA256");
			obj.put("charset", "utf-8");
			obj.put("attach", openid);
			obj.put("busicd","WPAY");
			obj.put("backUrl", notify_url);
			obj.put("chcd", "WXP");
			obj.put("frontUrl", "status.html");
			obj.put("mchntid", mchntid);//100000000000203
			obj.put("orderNum", outTradeNo);
			obj.put("txamt",fee);
			obj.put("terminalid", terminalid);//cssj0001
			
			obj.put("sign", sign);
			System.out.println("@@@@@@@baseStr:" + obj.toString());
			String baseStr = baseCode(obj.toString());
			System.out.println("@@@@@@@baseStr:" + baseStr);
			
			String redirect_uri = "data=" + baseStr;  
			return	redirect_uri;	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 将map中的键值对ASCII 码 从小到大排序（字典序）
	 * 
	 * @param signParams
	 * @return
	 * @throws Exception
	 */
	private static String orderParams(SortedMap<Object, Object> signParams)
			throws Exception {
		StringBuffer sb = new StringBuffer();
		Set es = signParams.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (ValueCheck.IsNotEmpty(v)) {
				sb.append(k + "=" + v + "&");
			}
			// 要采用URLENCODER的原始值！
		}
		String params = sb.substring(0, sb.lastIndexOf("&"));
		System.out.println("==========" + params);
		return params;
	}
	/**
	 * 返回 12位字符串  		1元表示为 000000000100，0.01元表示为000000000001
	 * @param num
	 * @return
	 */
	private static String formatNumberLength(String num){
		StringBuffer  result=new StringBuffer();
		int len=num.length();
		int zero_total=12-len;
		for(int i=0;i<zero_total;i++){
			result.append("0");
		}
		result.append(num);
		return result.toString();
	}
	private static String baseCode(String str) {
		BASE64Encoder encoder = new BASE64Encoder();
		String encoded = encoder.encode(str.getBytes());
		
		return encoded;
	}
	
}
