package com.choice.wechat.web.controller.takeout;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.choice.test.domain.Card;
import com.choice.test.domain.Net_OrderDtl;
import com.choice.test.domain.Net_OrderPackageDetail;
import com.choice.test.domain.Net_Orders;
import com.choice.test.domain.Voucher;
import com.choice.test.persistence.WeChatCrmMapper;
import com.choice.test.service.PubitemSearch;
import com.choice.test.service.CardSearch;
import com.choice.test.service.impl.WeChatCrmServiceImpl;
import com.choice.test.utils.CodeHelper;
import com.choice.test.utils.Commons;
import com.choice.test.utils.DateFormat;
import com.choice.webService.CRMWebservice;
import com.choice.wechat.common.AccessToken;
import com.choice.wechat.common.WeChatUser;
import com.choice.wechat.constants.bookDesk.FindDiningRoomConstants;
import com.choice.wechat.domain.bookDesk.Company;
import com.choice.wechat.domain.bookDesk.Firm;
import com.choice.wechat.domain.bookMeal.NetDishAddItem;
import com.choice.wechat.domain.bookMeal.NetDishProdAdd;
import com.choice.wechat.domain.game.Actm;
import com.choice.wechat.domain.takeout.Address;
import com.choice.wechat.domain.takeout.RangeCoordi;
import com.choice.wechat.domain.takeout.StoreRange;
import com.choice.wechat.domain.templateMsg.BaseTemplateMsg;
import com.choice.wechat.domain.templateMsg.MsgData;
import com.choice.wechat.persistence.WeChatPay.WxPayMapper;
import com.choice.wechat.persistence.bookDesk.BookDeskMapper;
import com.choice.wechat.persistence.bookMeal.BookMealMapper;
import com.choice.wechat.persistence.takeout.TakeOutMapper;
import com.choice.wechat.service.campaign.CampaignTools;
import com.choice.wechat.util.BaiduMapApi;
import com.choice.wechat.util.BuilderOrderMessage;
import com.choice.wechat.util.LogUtil;
import com.choice.wechat.util.MessageUtil;
import com.choice.wechat.util.Sign;
import com.choice.wechat.util.TemplateMsgUtil;
import com.choice.wechat.util.ValueCheck;
import com.choice.wechat.util.WeChatUtil;
import com.choice.wechat.util.mq.MqSender;

/**
 * 
 * @author song
 * 外卖
 *
 */
@Controller
@RequestMapping(value="takeout")
public class TakeOutController {
	@Autowired
	private TakeOutMapper takeOutMapper;
	@Autowired
	private BookMealMapper bookMealMapper;
	@Autowired
	private BookDeskMapper bookDeskMapper;
	@Autowired
	private WxPayMapper wxPayMapper;
	private WeChatCrmMapper crmMapper = new WeChatCrmServiceImpl();
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
	@RequestMapping(value="takeoutSelect")
	public ModelAndView takeOutSelect(HttpSession session, HttpServletRequest request,ModelMap modelMap,String code, String state, String pk_group){
		try {
			String openid = request.getParameter("openID");
			if(!StringUtils.hasText(code) && !StringUtils.hasText(openid)){
				return null;
			}
			String appId = Commons.appId;
			String secret = Commons.secret;
			
			modelMap.put("appId", appId);
			
			String nextType = request.getParameter("nextType");
			session.setAttribute("nextType", nextType);//点击门店后，跳转类型
			//log.debug("获取微信ACCESSTOKEN开始");
			AccessToken token = WeChatUtil.getAccessToken(appId, secret);
			//log.debug("获取微信ACCESSTOKEN结束");
		WeChatUser user = (WeChatUser) session.getAttribute("WeChatUser");
			if(user == null){//返回按钮，已取得用户授权的情况
				//通过网页授权方式获取openID
				//log.debug("获取微信OPENID开始");
				String oauth2[] = WeChatUtil.getOAuth2(appId, secret, code);
				//log.debug("获取微信OPENID结束");
				//获取用户基本信息
			//	log.debug("获取微信用户信息开始");
				user = WeChatUtil.getWeChatUser(oauth2[0],token.getToken());
				//log.debug("获取微信用户信息结束");
				if(StringUtils.hasText(user.getOpenid())){
					session.setAttribute("WeChatUser", user);
					openid = user.getOpenid();
				}
			} else {
				if(StringUtils.hasText(user.getOpenid())){
					openid = user.getOpenid();
				}
			}
			
			modelMap.put("openid", openid);
			modelMap.put("pk_group", pk_group);
						
			/*****************没有坐标的时候去getLocation取坐标*****************/
			StringBuffer sb = request.getRequestURL();
			//modelMap.put("targetUrl", sb.toString().replace(request.getRequestURI(), ""));
			
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
			//log.debug("生成微信JSSDK签名开始");
			Map<String, String> signMap = Sign.sign(WeChatUtil.getJsapiTicket(appId, token.getToken()).getTicket(),sb.toString());
			modelMap.put("signMap", signMap);
			//log.debug("生成微信JSSDK签名结束");
			
			// 绿数对接，保存公众号base id
			if(null == user) {
				user = new WeChatUser();
			}
			user.setPubid(request.getParameter("pubID"));
			user.setUnionid(request.getParameter("uuid"));
			MessageUtil.setUser(openid, user);
			
			//return new ModelAndView("takeout/takeoutSelect",modelMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("takeout/takeoutSelect",modelMap);
		
	}
	@RequestMapping(value="orderDetail")
	public ModelAndView orderDetail(ModelMap modelMap,HttpServletRequest request){
		try {
			String firmid = request.getParameter("firmid");
			String orderid = request.getParameter("orderid");
			String pk_group = request.getParameter("pk_group");
			String openid = request.getParameter("openid");

			if(!StringUtils.hasText("openid")){
				return null;
			}

			modelMap.put("code", request.getParameter("code"));//兼容以前的程序

			List<Address> address_list = takeOutMapper.getAddress(openid, "");
			if(address_list==null || address_list.isEmpty()){
				modelMap.put("mustAddAddress", "Y");
			}else{
				modelMap.put("address_list", address_list);
			}

			//需要定位的场景：选店铺-》点餐-》明细，此时有坐标；其他进入的默认没有
			String lat =  request.getParameter("lat");
			String lng =  request.getParameter("lng");
			HttpSession session = request.getSession();
			if(!StringUtils.hasText(lat)){
				lat = (String) session.getAttribute("lat");
			}
			if(!StringUtils.hasText(lng)){
				lng = (String) session.getAttribute("lng");
			}

			Map<String,String> baidumap = BaiduMapApi.getLocation(lat, lng);
			String address = baidumap.get("address");
			modelMap.put("baidu_address", address);

			// 查询我要结帐的状态
			Map<String, String> userInfoMap = WeChatUtil.getUserInfoMap();
			if(userInfoMap.containsKey(orderid + "_checkOut") && "Y".equals(userInfoMap.get(orderid + "_checkOut"))) {
				modelMap.put("hasAskCheckOut", "Y");
			}

			Map<String, String> map = new HashMap<String, String>();
			map.put("id", orderid);
			map.put("openid", openid);
			map.put("firmid", firmid);
			map.put("orderType", request.getParameter("orderType"));
			List<Net_Orders> listNet_Orders = takeOutMapper.getOrderMenus(map);

			List<Firm> listFirm = bookMealMapper.getFirmList(null, null, firmid);
			List<Net_OrderDtl> orderDtl = PubitemSearch
					.getOrderDtlMenus(orderid);

			if (listNet_Orders != null && !listNet_Orders.isEmpty()) {
				modelMap.put("orderHead", listNet_Orders.get(0));
			}
			Firm firm = new Firm();
			if (listFirm != null && !listFirm.isEmpty()) {
				firm = listFirm.get(0);
				modelMap.put("firm", firm);
			}
			modelMap.put("orderDtl", orderDtl);

			// 获取菜品附加项列表
			List<NetDishAddItem> listNetDishAddItem = bookMealMapper.getDishAddItemList(pk_group, orderid);
			// 获取附加产品列表
			List<NetDishProdAdd> listNetDishProdAdd = bookMealMapper.getDishProdAddList(pk_group, orderid);

			double totalPrice = 0.0;
			for (Net_OrderDtl dtl : orderDtl) {
				if (null != dtl.getPrice() && !"".equals(dtl.getPrice())) {
					totalPrice += Double.parseDouble(dtl.getPrice())
							* Integer.parseInt(dtl.getFoodnum());
				}

				// 将附加项添加到菜品下
				List<NetDishAddItem> tempList = new ArrayList<NetDishAddItem>();
				for (NetDishAddItem item : listNetDishAddItem) {
					if (dtl.getId().equals(item.getPk_orderDtlId()) && dtl.getFoodsid().equals(item.getPk_pubItem())) {
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
				//如果是套餐查询套餐明细及其附加项、附加产品
				if("1".equals(dtl.getIspackage())){
					List<Net_OrderPackageDetail> listPackageDtl = bookMealMapper.getOrderPackageDtl(dtl);
					if(null != listPackageDtl && listPackageDtl.size()>0){
						tempList = new ArrayList<NetDishAddItem>();
						addList = new ArrayList<NetDishProdAdd>();
						for(Net_OrderPackageDetail orderPackageDtl : listPackageDtl){
							//附加项
							for (NetDishAddItem item : listNetDishAddItem) {
								if (orderPackageDtl.getPk_orderpackagedetail().equals(item.getPk_orderDtlId()) && orderPackageDtl.getPk_pubitem().equals(item.getPk_pubItem())) {
									tempList.add(item);
									if(null != item.getNprice() && !"".equals(item.getNprice())) {
										totalPrice += Double.parseDouble(item.getNprice()) * Integer.parseInt(dtl.getFoodnum());
									}
								}
							}
							if (!tempList.isEmpty()) {
								orderPackageDtl.setListDishAddItem(tempList);
							}
							//附加产品
							for(NetDishProdAdd item : listNetDishProdAdd) {
								if (orderPackageDtl.getPk_orderpackagedetail().equals(item.getPk_orderDtlId()) && orderPackageDtl.getPk_pubitem().equals(item.getPk_pubitem())) {
									addList.add(item);
									if(null != item.getNprice() && !"".equals(item.getNprice())) {
										totalPrice += Double.parseDouble(item.getNprice()) * Integer.parseInt(dtl.getFoodnum());
									}
								}
							}
							if (!addList.isEmpty()) {
								orderPackageDtl.setListDishProdAdd(addList);
							}
						}
						dtl.setOrderPackageDetailList(listPackageDtl);
					}
				}
			}
			DecimalFormat df = new DecimalFormat("####0.00");
			if (null != listNet_Orders && !listNet_Orders.isEmpty()) {
				modelMap.put("totalPrice", df.format(totalPrice));
				modelMap.put("orderDetail", listNet_Orders.get(0));
			}
			modelMap.put("pk_group", pk_group);

			// 是否扫码点餐
			modelMap.put("scanType", request.getParameter("scanType"));

			request.setAttribute("openid", openid);
			modelMap.put("openid", openid);

			// 获取电子券
			//processCoupon(modelMap, openid, firmid);

			// 页面来源
			modelMap.put("pageFrom", request.getParameter("pageFrom"));

			//点餐时间
			List<String> time_list = new ArrayList<String>();
			Calendar now  =  Calendar.getInstance();
			now.setTime(new Date());
			now.set(1970, 0, 1);
			
			int hour = 21;
			int min = 0;
			if(firm != null && StringUtils.hasText(firm.getDinnerendtime())) {
				String dinnerEndTime = firm.getDinnerendtime();
				hour = Integer.parseInt(dinnerEndTime.substring(0, 2));
				min = Integer.parseInt(dinnerEndTime.substring(3, 5));
			}
			
			Calendar end  =  Calendar.getInstance();
			end.setTime(new Date());
			end.set(1970, 0, 1, hour, min, 0);

			SimpleDateFormat sfTime = new SimpleDateFormat("HH:mm");
			while(now.compareTo(end)<=0){
				time_list.add(sfTime.format(now.getTime()));
				if(now.get(Calendar.MINUTE)%30 != 0){
					now.add(Calendar.HOUR_OF_DAY, 1);
					if(now.get(Calendar.MINUTE)>=30){
						now.set(Calendar.MINUTE, 30);
					}else{
						now.set(Calendar.MINUTE, 0);
					}
				}else{
					now.add(Calendar.MINUTE, 30);
				}
			}
//			modelMap.put("default_time", time_list.isEmpty()?"":time_list.get(0));
			modelMap.put("default_time", "1");
			modelMap.put("time_list", time_list);

			Map<String,Object> mapParam = new HashMap<String,Object>();
			mapParam.put("openid",openid);
			List<Map<String,Object>> listMaps = wxPayMapper.queryVinvoicetitle(mapParam);
			if(ValueCheck.IsNotEmptyForList(listMaps)){
				modelMap.put("vinvoicetitle", listMaps.get(0).get("vinvoicetitle"));//发票信息
			}
			
			// 获取配送范围
			List<StoreRange> listStoreRange = takeOutMapper.getListStoreRange(pk_group, firmid);
			// 获取配送范围地图坐标点
			List<RangeCoordi> listRangeCoordi = takeOutMapper.getListRangeCoordi(pk_group, firmid);
			
			// 整理坐标点列表，将坐标点对应到各自的配送区域内
			for(RangeCoordi rc : listRangeCoordi) {
				for(StoreRange sr : listStoreRange) {
					if(rc.getIareaseq() == sr.getIareaseq()) {
						sr.getListRangeCoordi().add(rc);
						break;
					}
				}
			}
			modelMap.put("listStoreRange", listStoreRange);
			
			// 读取优惠活动
			List<Actm> listActm = CampaignTools.getListTakeOutActm(firmid);
			
			// 获取类型为账单减免中，折扣金额最大的活动
			String pkActm = CampaignTools.getMaxDiscountActm(totalPrice, listActm);
			
			// 过滤账单减免活动，仅保留折扣最大的活动
			List<Actm> list = CampaignTools.filterRemitCampaign(pkActm, totalPrice, listActm, orderDtl);
			modelMap.put("listActm", list);
			
			// 计算应付金额
			double payMoney = CampaignTools.calcPayMoney(pkActm, totalPrice, listActm, orderDtl);
			DecimalFormat d = new DecimalFormat("####0.0");
			d.setRoundingMode(RoundingMode.DOWN);
			modelMap.put("payMoney", df.format(Double.parseDouble(d.format(payMoney))));
			
			return new ModelAndView("takeout/orderDetail", modelMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取可用电子券
	 * @param modelMap
	 * @param openid
	 * @param firmid
	 */
	private void processCoupon(ModelMap modelMap, String openid, String firmid) {
		try {
			// 查询电子券列表
			List<Voucher> listCoupon = this.bookMealMapper.getListCouponInfo(openid, firmid);
			for(Voucher item : listCoupon) {
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
			}
			modelMap.put("listCoupon", listCoupon);
			modelMap.put("couponSize", listCoupon == null || listCoupon.isEmpty() ? "0" : listCoupon.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value="saveAddress")
	@ResponseBody
	public String saveAddress(HttpServletRequest request){
		try{
			String openid = request.getParameter("openid");
			String name = request.getParameter("name");
			String address = request.getParameter("address");
			String phoneNo = request.getParameter("phoneNo");
			String pk_id = CodeHelper.createUUID();

			Address addr = new Address();
			addr.setAddress(address);
			addr.setContentName(name);
			addr.setOpenid(openid);
			addr.setPk_id(pk_id);
			addr.setTele(phoneNo);
			takeOutMapper.addAddress(addr);
			return pk_id;
		}catch(Exception e){
			e.printStackTrace();
			return "FALSE";
		}
	}

	@RequestMapping(value="saveAndSendOrders")
	@ResponseBody
	public String saveAndSendOrders(HttpServletRequest request){
		Map<String,String> map = new HashMap<String,String>();
		try{
			String openid = request.getParameter("openid");
			map.put("orderid", request.getParameter("orderid"));
			map.put("name", request.getParameter("name"));
			map.put("addr", request.getParameter("addr"));
			map.put("tele", request.getParameter("tele"));
			map.put("datmin", request.getParameter("datmin"));
			map.put("deliverytime", request.getParameter("datmin"));//外卖送达时间统一字段
			map.put("vinvoicetitle", "");
			
			//如果勾选了开发票，更新订单发票头字段
	        if("Y".equals(request.getParameter("visopeninvoice"))){
	        	map.put("vinvoicetitle", request.getParameter("vinvoicetitle"));
	        	
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
			
			String payType = request.getParameter("payType");
			if(payType != null){
				if("0".equals(payType)){//在线支付
					map.put("payment", "2");//网上银行
					map.put("state", "2");//生效已支付
				}else{
					map.put("payment", "0");//到店支付 （货到付款）
					map.put("state", "1");//生效未支付
				}
			}

			int count = takeOutMapper.updateTakeOutOrders(map);//更新外卖订单信息
			if(count<=0){
				return "FALSE";
			}
			// 推送点菜单
			String orderid = request.getParameter("orderid");

			Firm firm = new Firm();
			Net_Orders orders = BuilderOrderMessage.builderOrderMessage(takeOutMapper, bookMealMapper, orderid, "8");
			if(orders != null){
				orders.setAddr(map.get("addr"));
				orders.setDat(DateFormat.getStringByDate(new Date(), "yyyy-MM-dd"));
				//查询门店
				List<Firm> listFirm = bookMealMapper.getFirmList(null, null, orders.getFirmid());

				if (listFirm != null && !listFirm.isEmpty()) {
					firm =  listFirm.get(0);
					orders.setVcode(firm.getFirmCode());
				}else{
					return "不存在此门店";
				}
				//设置送达时间
				orders.setDeliverytime(map.get("deliverytime"));
				//记录本次记录
				Map<String,Object> mqMap = new HashMap<String,Object>();
				mqMap.put("pk_mqlogs", orders.getSerialid());
				mqMap.put("orderid", orders.getId());
				mqMap.put("vtype", "1");
				mqMap.put("errmsg", "");
				mqMap.put("state", "");
				bookDeskMapper.addMqLogs(mqMap);
				//发送mq消息
				orders.setSerialid(mqMap.get("pk_mqlogs")+"");//重置mq记录的主键
				JSONObject json = JSONObject.fromObject(orders);
				LogUtil.writeToTxt(LogUtil.MQSENDORDER, "下单到MQ服务器中，队列名："+firm.getFirmCode()+"_orderList，订单对象："+json.toString());
				MqSender.sendOrders(json.toString(),firm.getFirmCode()+"_orderList",Commons.mqEffectiveTime);//设置消息有效期为半小时
				sendTempMsg(orders, request);
			}

			return "TRUE";
		}catch(Exception e){
			e.printStackTrace();
			
			map.put("state", "0");//保存未提交
			takeOutMapper.updateTakeOutOrders(map);
			return "FALSE";
		}
	}

	/**
	 * 
	 * @param request
	 * @return
	 * 只更新，用于外卖未支付前
	 */
	@RequestMapping(value="saveTakeOutOrders")
	@ResponseBody
	public String saveTakeOutOrders(HttpServletRequest request){
		Map<String,String> map = new HashMap<String,String>();
		map.put("orderid", request.getParameter("orderid"));
		map.put("name", request.getParameter("name"));
		map.put("addr", request.getParameter("addr"));
		map.put("tele", request.getParameter("tele"));
		map.put("datmin", request.getParameter("datmin"));
		map.put("deliverytime", request.getParameter("datmin"));//外卖送达时间统一字段
		map.put("firmid", request.getParameter("firmid"));//只作为 调用非会员订外卖，自动注册为会员接口时 用
		map.put("sumprice", request.getParameter("sumprice"));//只作为 调用非会员订外卖，自动注册为会员接口时 用
		String openid=request.getParameter("openid");//只作为 调用非会员订外卖，自动注册为会员接口时 用
		String payType = request.getParameter("payType");
		String msg ="";
				
		try {//调用非会员订外卖，自动注册为会员接口
			msg = crmService.takeOutMemberRegesiter(map.get("name"),map.get("tele"),map.get("addr"),map.get("firmid"),map.get("sumprice"),openid);
			LogUtil.writeToTxt(LogUtil.CALLWEBSERVICE, "调用非会员叫外卖时自动注册会员接口成功。接口名称【takeOutMemberRegesiter】，参数值【name,tele,address,firmid,billamt分别为" +"【" +map.get("name")+"【" +map.get("tele")+"】【" +map.get("addr")+"】【" + map.get("firmid")+"】【" +map.get("sumprice")+"】，返回值【" + msg + "】 正确并注册微信会员返回 cardId 正确但未注册微信会员返回 1 错误返回-3");
		} catch (Exception e) {
			LogUtil.writeToTxt(LogUtil.CALLWEBSERVICE, "调用非会员叫外卖时自动注册会员接口失败。接口名称【takeOutMemberRegesiter】，参数值【name,tele,address,firmid,billamt分别为" + "【" +map.get("name")+"【" +map.get("tele")+"】【" +map.get("addr")+"】【" + map.get("firmid")+"】【" +map.get("sumprice")+"】，返回值【" + msg + "】正确并注册微信会员返回 cardId 正确但未注册微信会员返回 1 错误返回-3");
			e.printStackTrace();
		}
		if(!"1".equals(msg)&&!"-3".equals(msg)&&!"".equals(msg)){//微信外卖注册会员后，根据返回的cardId生成二维码和条形码
			crmMapper.generateQRCodeAndBarCode(map.get("tele"), msg);//吉祥要求把手机号做为cardId
		}			
	
		/*if ( listCard.size() == 0 || listCard == null ) {			
			// 插入会员记录
			Card card = new Card();
			card.setName(map.get("name"));
			card.setTele(map.get("tele"));
			card.setOpenid(openid);
			card.setChlb("会员");
			card.setFirmId(map.get("firmid"));
			card.setTyp(Commons.getConfig().getProperty("registerCardTyp"));
			String cont= CardSearch.addCardRed(card);
			LogUtil.writeToTxt(LogUtil.CALLWEBSERVICE, "未调用接口，外卖自动注册会员openid="+openid+";cont="+cont+"name="+card.getName()+";listCard.size()="+listCard.size());			
		}*/
		if(payType != null){
			if("0".equals(payType)){//在线支付
				map.put("payment", "2");//网上银行
				//map.put("state", "2");//生效已支付
			}else{
				map.put("payment", "0");//到店支付 （货到付款）
				//map.put("state", "1");//生效未支付
			}
			map.put("state", "0");
		}
		try{
			int count = takeOutMapper.updateTakeOutOrders(map);//更新外卖订单信息
			if(count<=0){
				return "FALSE";
			}
			return "TRUE";
		}catch(Exception e){
			e.printStackTrace();
			return "FALSE";
		}

	}

	/**
	 * 
	 * @param request
	 * @return
	 * 支付成功后，只更新状态
	 */
	@RequestMapping(value="sendTakeOutOrders")
	@ResponseBody
	public String sendTakeOutOrders(HttpServletRequest request){
		String id = request.getParameter("orderid");
		try{
			bookMealMapper.updateOrderState(id, "2");
			
			return "TRUE";
		}catch(Exception e){
			e.printStackTrace();
			//支付成功后调用此方法，此时不应该恢复
			//bookMealMapper.updateOrderState(id, "0");//恢复到初始状态
			return "FALSE";
		}

	}
	
	private void sendTempMsg(Net_Orders orders, HttpServletRequest request){
		//根据pk_group，获取配置信息
		Company company = WeChatUtil.getCompanyInfo(orders.getPk_group());
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

		String tempid = Commons.getConfig().getProperty("TM00530");
		BaseTemplateMsg msg = new BaseTemplateMsg();
		msg.setTouser(orders.getOpenid());
		msg.setTemplate_id(tempid);
		//msg.setTopcolor("#000000");

		StringBuffer sb = request.getRequestURL();
		String baseUrl = sb.substring(0, sb.lastIndexOf("/"));
		baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf("/"));
		sb.delete(0, sb.length());
		sb.append(baseUrl)
			.append("/takeout/orderDetail.do?orderid=").append(orders.getId())
			.append("&openid=").append(orders.getOpenid())
			.append("&firmid=").append(orders.getFirmid())
			.append("&pk_group=").append(orders.getPk_group()==null?"":orders.getPk_group())
			.append("&orderTyp=2");
		msg.setUrl(sb.toString());
		Map<String,MsgData> map = new HashMap<String,MsgData>();
		MsgData data = new MsgData();
		data.setValue("您的订单已受理成功");
		map.put("first", data);

		data = new MsgData();
		data.setValue(orders.getFirmdes());
		map.put("storeName", data);

		data = new MsgData();
		data.setValue(orders.getResv());
		map.put("orderId", data);

		data = new MsgData();
		data.setValue("外卖");
		map.put("orderType", data);

		data = new MsgData();
		sb.delete(0, sb.length());
		sb.append("外送电话：").append(orders.getContact()==null?"":orders.getContact());
		sb.append("\n外送地址：").append(orders.getAddr()==null?"":orders.getAddr());
		sb.append("\n感谢您的使用，请耐心等待");
		data.setValue(sb.toString());
		map.put("remark", data);
		msg.setData(map);
		TemplateMsgUtil.sendTmplateMsg(token.getToken(), msg);
	}
}
