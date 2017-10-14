package com.choice.wechat.web.controller.bookDesk;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.choice.test.domain.Card;
import com.choice.test.domain.DeskTimes;
import com.choice.test.domain.Net_OrderDtl;
import com.choice.test.domain.Net_Orders;
import com.choice.test.persistence.WeChatOrderMapper;
import com.choice.test.service.CardSearch;
import com.choice.test.service.PubitemSearch;
import com.choice.test.service.impl.WechatOrderService;
import com.choice.test.utils.CodeHelper;
import com.choice.test.utils.Commons;
import com.choice.test.utils.DateFormat;
import com.choice.test.utils.GenerateRandom;
import com.choice.wechat.common.AccessToken;
import com.choice.wechat.common.WeChatUser;
import com.choice.wechat.constants.bookDesk.BookDeskConstants;
import com.choice.wechat.domain.bookDesk.City;
import com.choice.wechat.domain.bookDesk.Company;
import com.choice.wechat.domain.bookDesk.Firm;
import com.choice.wechat.domain.bookDesk.Sft;
import com.choice.wechat.domain.bookDesk.StoreTable;
import com.choice.wechat.domain.bookMeal.FdItemSale;
import com.choice.wechat.domain.bookMeal.NetDishAddItem;
import com.choice.wechat.domain.bookMeal.NetDishProdAdd;
import com.choice.wechat.domain.templateMsg.BaseTemplateMsg;
import com.choice.wechat.domain.templateMsg.MsgData;
import com.choice.wechat.persistence.bookDesk.BookDeskMapper;
import com.choice.wechat.persistence.bookMeal.BookMealMapper;
import com.choice.wechat.service.bookDesk.IBookDeskService;
import com.choice.wechat.util.BaiduMapApi;
import com.choice.wechat.util.LogUtil;
import com.choice.wechat.util.Sign;
import com.choice.wechat.util.TemplateMsgUtil;
import com.choice.wechat.util.WeChatUtil;
import com.choice.wechat.util.mq.MqSender;

/**
 * 订位
 */
@Controller
@RequestMapping(value="bookDesk")
public class BookDeskController {

	@Autowired
	private BookDeskMapper bookDeskMapper;
	@Autowired
	private IBookDeskService bookDeskService;
	@Autowired
	private BookMealMapper bookMealMapper;
	static WeChatOrderMapper orderMapper = new WechatOrderService();
	
	/**
	 * 
	 */
	@RequestMapping(value="listFirm")
	public ModelAndView listFirm(HttpSession session, HttpServletRequest request, ModelMap modelMap, String code, String state, String pk_group){
		try {
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
			
			modelMap.put("appId", appId);
			
			AccessToken token = WeChatUtil.getAccessToken(appId, secret);
			
			WeChatUser user = (WeChatUser) session.getAttribute("WeChatUser");
			if(user == null){//返回按钮，已取得用户授权的情况
				//通过网页授权方式获取openID
				String oauth2[] = WeChatUtil.getOAuth2(appId, secret, code);
				//获取用户基本信息
				user = WeChatUtil.getWeChatUser(oauth2[0],token.getToken());
				session.setAttribute("WeChatUser", user);
			}
			
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
			
			Map<String, String> signMap = Sign.sign(WeChatUtil.getJsapiTicket(appId, token.getToken()).getTicket(),sb.toString());
			modelMap.put("signMap", signMap);
			
			modelMap.put("openid", user.getOpenid());
			modelMap.put("pk_group", pk_group);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(BookDeskConstants.GETLOCATION,modelMap);
	}
	/**
	 * 订位基本信息
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value="listFirmFromCity")
	public ModelAndView listFirmFromCity(HttpServletRequest request, ModelMap modelMap){
		try {
			String pk_group = request.getParameter("pk_group");
			String openid = request.getParameter("openid");
			String lat =  request.getParameter("lat");
			String lng =  request.getParameter("lng");
			
			HttpSession session = request.getSession();
			if(!StringUtils.hasText(lat)){
				lat = (String) session.getAttribute("lat");
			}
			if(!StringUtils.hasText(lng)){
				lng = (String) session.getAttribute("lng");
			}
			session.setAttribute("lat", lat);
			session.setAttribute("lng", lng);
			
			String LatLng = lat+","+lng;
			String coord_type = request.getParameter("coord_type");//坐标类型
			if(coord_type==null || "".equals(coord_type)){//如果为空，默认不是百度坐标
				try{
					LatLng = BaiduMapApi.geoconv(lat, lng);//转换坐标
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
			modelMap.put("LatLng", LatLng);
			
			//城市
			List<City> listCity = bookDeskMapper.getCityList(pk_group, null);
			City userCity = (City)listCity.get(0);//默认为第一个
			
			Map<String,String> map = BaiduMapApi.getLocation(lat, lng);
			String cityName = map.get("city");
			String address = map.get("address");
			
			modelMap.put("address", address);
			
			for(City city:listCity){//找到用户基本信息匹配的城市
				if(cityName.contains(city.getVname())){
					userCity = city;
				}
			}

			//餐次
			List<Sft> listSft = new ArrayList<Sft>();
			Sft sft1 = new Sft();
			sft1.setCode("1");
			sft1.setId("001");
			sft1.setName("午市");

			Sft sft2 = new Sft();
			sft2.setCode("2");
			sft2.setId("002");
			sft2.setName("晚市");

			listSft.add(sft1);
			listSft.add(sft2);

			//根据时间确定餐次
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			String dat = "";//所订餐日期
			Sft sft = listSft.get(0);//餐次

			Calendar c = Calendar.getInstance();  
			c.setTime(new Date()); 

			int currHour = c.get(Calendar.HOUR_OF_DAY);
			int currMinute = c.get(Calendar.MINUTE);

			if(currHour > 16 || (currHour == 16 && currMinute > 30)){
				c.add(Calendar.DATE, 1);//16.30以后默认订第二天
				sft = listSft.get(0);;//默认午市
			}else if(currHour>10 || (currHour==10 && currMinute > 30)){
				sft = listSft.get(1);;//默认晚市
			}

			dat = sf.format(c.getTime());

			List<String> listDate = new ArrayList<String>();//可订位日期
			SimpleDateFormat sfw = new SimpleDateFormat("yyyy-MM-dd    EEEE");
			for(int i=1;i<30;i++){
				c.add(Calendar.DATE, 1);
				listDate.add(sfw.format(c.getTime()));
			}
			//根据城市查询店铺
			List<Firm> listFirm = choiceFirm(pk_group,userCity.getPk_city(),sft.getCode(),dat);
			
			Map<String,String> distanceMap = BaiduMapApi.getDistance(listFirm, lat, lng);
			modelMap.put("distanceMap", distanceMap);//距离
			
			modelMap.put("listCity", listCity);//城市列表
			modelMap.put("listSft", listSft);//餐次列表
			modelMap.put("listDate", listDate);//日期列表
			modelMap.put("userCity", userCity);//用户城市
			modelMap.put("listFirm", listFirm);//门店列表
			modelMap.put("sft", sft);//默认餐次
			modelMap.put("dat", dat);//默认订餐日期
			modelMap.put("openid", openid);
			modelMap.put("pk_group", pk_group);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(BookDeskConstants.LISTFIRM,modelMap);
	}

	@RequestMapping(value="choiceFirm")
	@ResponseBody
	public List<Firm> choiceFirm(ModelMap modelMap, String pk_group, String pk_city, String sft, String dat ){
		List<Firm> listFirm = null;
		try{
			listFirm = choiceFirm(pk_group,pk_city,sft,dat);
		}catch(Exception e){
			e.printStackTrace();
		}
		return listFirm;
	}

	/**
	 * 选择门店
	 * @return
	 */
	public List<Firm> choiceFirm(String pk_group, String pk_city, String sft, String dat){
		try {
			//门店列表
			List<Firm> listFirm = bookDeskMapper.getFirmList(pk_group, pk_city, null);

			for(int i=0;i<listFirm.size();i++){
				Firm firm = (Firm) listFirm.get(i);
				//可改为门店和桌台表关联查询，以减少查询次数
				List<StoreTable> listStoreTable = bookDeskMapper.getDeskFormFirm(pk_group, firm.getFirmid(), sft, dat);
				firm.setListStoreTable(listStoreTable);
			}
			return listFirm;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param modelMap
	 * @param request
	 * @return
	 * 订位页面
	 */
	@RequestMapping(value="gotoOrder")
	public ModelAndView gotoOrder(ModelMap modelMap,HttpServletRequest request){
		try{

			String pk_group = request.getParameter("pk_group");
			String pk_city = request.getParameter("pk_city");
			String firmid = request.getParameter("firmid");
			String dat = request.getParameter("dat");//门店列表选择的订餐日期
			String sft = request.getParameter("sft");//门店列表选择的订餐餐次
			String openid = request.getParameter("openid");

			if(dat==null){
				modelMap.put("msg", "请返回首页");
				return new ModelAndView(BookDeskConstants.FAILEDPAGE,modelMap);
			}

			String tele = "";//会员电话
			String clientID = "";//会员号
			List<Card> listCard = CardSearch.listCard(openid);
			if (listCard != null && listCard.size() > 0) {
				tele = listCard.get(0).getTele();
				clientID = listCard.get(0).getCardId();
			}

			modelMap.put("tele", tele);
			modelMap.put("clientID", clientID);

			List<Firm> listFirm = bookDeskMapper.getFirmList(pk_group, pk_city, firmid);
			Firm firm = listFirm.get(0);

			//根据时间确定餐次，判断前面选择的是否在具体门店的截止时间前
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

			//餐次
			List<Sft> listSft = new ArrayList<Sft>();
			Sft sft1 = new Sft();
			sft1.setCode("1");
			sft1.setId("001");
			sft1.setName("午市");

			Sft sft2 = new Sft();
			sft2.setCode("2");
			sft2.setId("002");
			sft2.setName("晚市");

			listSft.add(sft1);
			listSft.add(sft2);

			Date nowDate = new Date();
			Calendar c = Calendar.getInstance(); 
			c.setTime(nowDate);
			if(dat.equals(sf.format(nowDate))){//如果预定日期等于当前日期

				int currHour = c.get(Calendar.HOUR_OF_DAY);
				int currMinute = c.get(Calendar.MINUTE);

				String dinners[] =  firm.getDinnerendtime().split(":");
				String lunchs[] = firm.getLunchendtime().split(":");

				if(currHour > Integer.parseInt(dinners[0]) || (currHour == Integer.parseInt(dinners[0]) && currMinute > Integer.parseInt(dinners[1]))){
					c.add(Calendar.DATE, 1);//16.30以后默认订第二天
					sft = listSft.get(0).getCode();//默认午市
				}else if(currHour > Integer.parseInt(lunchs[0]) || (currHour == Integer.parseInt(lunchs[0]) && currMinute > Integer.parseInt(lunchs[1]))){
					sft = listSft.get(1).getCode();//默认晚市
				}
				dat = sf.format(c.getTime());
			}


			List<String> listDate = new ArrayList<String>();//可订位日期
			SimpleDateFormat sfw = new SimpleDateFormat("yyyy-MM-dd    EEEE");
			for(int i=1;i<30;i++){
				c.add(Calendar.DATE, 1);
				listDate.add(sfw.format(c.getTime()));
			}

			List<StoreTable> listStoreTable = bookDeskMapper.getDeskFormFirm(pk_group, firm.getFirmid(), sft, dat);

			String defaultPax = "";
			String defaultRoomTyp = "";
			if(listStoreTable != null && listStoreTable.size() > 0){
				for(int i=0;i<listStoreTable.size();i++){
					if(Integer.parseInt(listStoreTable.get(i).getNum())>0){
						defaultPax = listStoreTable.get(i).getPax();
						defaultRoomTyp = listStoreTable.get(i).getRoomtyp();
						break;
					}
				}
			}

			modelMap.put("listSft", listSft);//餐次列表
			modelMap.put("listDate", listDate);//日期列表
			modelMap.put("sft", sft);//默认餐次
			modelMap.put("dat", dat);//默认订餐日期
			modelMap.put("defaultRoomTyp", defaultRoomTyp);//默认桌台
			modelMap.put("defaultPax", defaultPax);//默认桌台人数
			modelMap.put("openid", openid);
			modelMap.put("pk_group", pk_group);
			modelMap.put("listStoreTable", listStoreTable);
			modelMap.put("firm", firm);
			return new ModelAndView(BookDeskConstants.ORDER,modelMap);
		}catch(Exception e){
			e.printStackTrace();
		}
		return new ModelAndView(BookDeskConstants.FAILEDPAGE,modelMap);
	}

	/**
	 * 
	 * @param modelMap
	 * @param orders
	 * @param roomtyp
	 * @param realPax 桌台预先定义的人数
	 * @param deskTime
	 * @return
	 * 提交订单
	 */
	@RequestMapping(value="confirmOrder")
	public ModelAndView confirmOrder(ModelMap modelMap,HttpServletRequest request,Net_Orders orders,DeskTimes deskTime){
		try{
			String roomtyp = request.getParameter("roomtyp");
			String realPax = request.getParameter("realPax");
			String clientID = request.getParameter("clientID");

			String openid = orders.getOpenid();
			if(openid==null || "".equals(openid)){
				modelMap.put("msg", "抱歉，获取微信号失败");
				return new ModelAndView(BookDeskConstants.FAILEDPAGE,modelMap);
			}
			//插入会员
			if(clientID==null || "".equals(clientID)){
				List<Card> phoneCard = CardSearch.listWXCard(orders.getContact(), null);//通过手机号查询会员
				if(phoneCard!=null && phoneCard.size()>0){
					clientID = phoneCard.get(0).getCardId();
				}else{
					Card card = new Card();
					card.setOpenid(orders.getOpenid());
					card.setTele(orders.getContact());
					card.setName(orders.getName());
					card.setChlb("会员");

					String clientNO = CardSearch.addCardRed(card);//新增微信会员
					List<Card> listCard = CardSearch.listCardByCardno(clientNO);
					clientID = listCard.get(0).getCardId();
				}
			}

			List<StoreTable> listDesks = bookDeskMapper.findResvTbl(roomtyp, orders.getSft(), orders.getDat(), orders.getFirmid(), realPax);
			if(listDesks.size()==0){
				modelMap.put("msg", "抱歉，您预定的桌台已被抢先预定，请选择其他桌台！");
				modelMap.put("sft", orders.getSft());
				modelMap.put("openid", orders.getOpenid());
				modelMap.put("firmid", orders.getFirmid());
				modelMap.put("dat",orders.getDat());
				return new ModelAndView(BookDeskConstants.FAILEDPAGE,modelMap);
			}
			//随机抽取一个桌台
			Integer custIndex = new Random().nextInt(listDesks.size());
			StoreTable table = listDesks.get(custIndex);
			orders.setTables(table.getTbl());
			deskTime.setResvtblid(table.getId());

			//订单id
			String id=CodeHelper.createUUID();
			//保存时间桌台信息
			deskTime.setId(CodeHelper.createUUID());
			deskTime.setOrdersid(id);
			deskTime.setState("1");//有效

			//生成订单
			String resv=DateFormat.getStringByDate(new Date(), "yyMMddHHmmss")+(new java.util.Random().nextInt(90)+10);
			orders.setId(id);
			orders.setResv(resv);
			orders.setIsfeast("1");//订桌订单
			orders.setRannum(resv+GenerateRandom.getRanNum(6));

			try{
				bookDeskService.saveOrder(orders,deskTime,clientID);
			}catch(RuntimeException e){
				e.printStackTrace();
				modelMap.put("msg", "抱歉，您预定的桌台已被抢先预定，请选择其他桌台！");
				modelMap.put("sft", orders.getSft());
				modelMap.put("openid", orders.getOpenid());
				modelMap.put("firmid", orders.getFirmid());
				modelMap.put("dat",orders.getDat());
				return new ModelAndView(BookDeskConstants.FAILEDPAGE,modelMap);
			}

			modelMap.put("orderid", id);
			modelMap.put("openid", orders.getOpenid());
			modelMap.put("firmid", orders.getFirmid());
			modelMap.put("pk_group", orders.getPk_group());
			return new ModelAndView(BookDeskConstants.SUCCESSPAGE,modelMap);
		}catch(Exception e){
			e.printStackTrace();
		}
		return new ModelAndView(BookDeskConstants.FAILEDPAGE,modelMap);
	}

	/**
	 * 
	 * @param modelMap
	 * @param orderid
	 * @param openid
	 * @param firmid
	 * @param pk_group
	 * @return
	 * 订位单订单明细
	 */
	@RequestMapping(value="orderDetail")
	public ModelAndView orderDetail(ModelMap modelMap,HttpServletRequest request){
		try{
			Map<String,String> map = new HashMap<String,String>();
			map.put("id", request.getParameter("orderid"));
			map.put("openid", request.getParameter("openid"));
			map.put("firmid", request.getParameter("firmid"));
			map.put("orderType", request.getParameter("orderType"));
			List<Net_Orders> listNet_Orders = bookDeskMapper.getOrderMenus(map);
			Net_Orders order = listNet_Orders.get(0);
			order.setMoney(WeChatUtil.formatDoubleLength(order.getMoney(), 2));
			modelMap.put("orderDetail", order);
			modelMap.put("pk_group",request.getParameter("pk_group"));
			
			String bookMealOrderID = request.getParameter("bookMealOrderID");
			modelMap.put("bookMealOrderID", bookMealOrderID);//点菜单ID
			
			String isCnSft = Commons.getConfig().getProperty("isCnSft");
			Map<String,String> sftNameMap = null;
			if(isCnSft==null || "".equals(isCnSft)){//是否启用中餐餐次
				sftNameMap = bookDeskMapper.getShiftSft();
			}else{
				sftNameMap = bookDeskMapper.getFirmSft(order.getFirmid());
			}
			modelMap.put("sfts", sftNameMap);
			
			return new ModelAndView(BookDeskConstants.ORDERDETAIL,modelMap);
		}catch(Exception e){
			e.printStackTrace();
		}
		return new ModelAndView(BookDeskConstants.FAILEDPAGE,modelMap);
	}

	/**
	 * 
	 * @param modelMap
	 * @param orderid
	 * @param pk_group
	 * @return
	 * 取消订单
	 */
	@RequestMapping(value="cancelOrder")
	public ModelAndView cancelOrder(HttpServletRequest request,HttpSession session, ModelMap modelMap, String orderid, String pk_group, String orderType,String firmid,String state,String vtransactionid,String paymoney){
		try{
			StringBuilder sb = new StringBuilder("?orderid=");
			sb.append(orderid).append("&pk_group=").append(pk_group);
			
			/**订单取消消息推送**/
			Map<String,String> params = new HashMap<String,String>(); 
			params.put("id", orderid);
			List<Net_Orders> list = bookDeskMapper.getOrderMenus(params);
			if(list!=null && !list.isEmpty()){
				Net_Orders orders = list.get(0);
				
				//取消支付时需要确定是微信支付还是会员卡支付
				//WeChatUser user = (WeChatUser) session.getAttribute("WeChatUser");
				sb.append("&openid=").append(orders.getOpenid());
				bookDeskService.cancelOrders(orderid,orders.getOpenid(),pk_group,firmid,state,vtransactionid,paymoney,orderType);
				
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

				String tempid = Commons.getConfig().getProperty("TM00534");
				BaseTemplateMsg msg = new BaseTemplateMsg();
				msg.setTouser(orders.getOpenid());
				msg.setTemplate_id(tempid);
				//msg.setTopcolor("#000000");

				StringBuffer url = request.getRequestURL();
				String baseUrl = url.substring(0, url.lastIndexOf("/"));
				baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf("/"));
				url.delete(0, url.length());
				
				Map<String,MsgData> map = new HashMap<String,MsgData>();
				MsgData data = new MsgData();
				data.setValue("您的订单已取消成功");
				map.put("first", data);

				data = new MsgData();
				data.setValue(orders.getFirmdes());
				map.put("storeName", data);

				data = new MsgData();
				data.setValue(orders.getResv());
				map.put("orderId", data);

				data = new MsgData();
				if(null != orderType && "0".equals(orderType)) {
					data.setValue("堂食");
					
					url.append(baseUrl)
					.append("/bookMeal/orderDetail.do?orderid=").append(orders.getId())
					.append("&openid=").append(orders.getOpenid())
					.append("&firmid=").append(orders.getFirmid())
					.append("&pk_group=").append(orders.getPk_group())
					.append("&orderTyp=0");
					msg.setUrl(url.toString());
				}else if(null != orderType && "2".equals(orderType)){
					data.setValue("外卖");
					
					url.append(baseUrl)
					.append("/bookMeal/orderDetail.do?orderid=").append(orders.getId())
					.append("&openid=").append(orders.getOpenid())
					.append("&firmid=").append(orders.getFirmid())
					.append("&pk_group=").append(orders.getPk_group())
					.append("&orderTyp=0");
					msg.setUrl(url.toString());	
				}else{
					url.append(baseUrl)
					.append("/bookDesk/orderDetail.do?orderid=").append(orders.getId())
					.append("&openid=").append(orders.getOpenid())
					.append("&firmid=").append(orders.getFirmid())
					.append("&pk_group=").append(orders.getPk_group())
					.append("&orderTyp=1");
					msg.setUrl(url.toString());
				
					String isCnSft = Commons.getConfig().getProperty("isCnSft");
					Map<String,String> sftNameMap =  bookDeskMapper.getShiftSft();
					if(isCnSft==null || "".equals(isCnSft)){//是否启用中餐餐次
						sftNameMap = bookDeskMapper.getShiftSft();
					}else{
						sftNameMap = bookDeskMapper.getFirmSft(orders.getFirmid());
					}
					
					url.delete(0, url.length());
					url.append("预订/")
					.append(orders.getPax())
					.append("人/")
					.append((orders.getArrtime()==null||"".equals(orders.getArrtime()))?orders.getDat():orders.getArrtime())
					.append("/")
					.append(sftNameMap.get(orders.getSft()));
					data.setValue(url.toString());
				}
				map.put("orderType", data);

				data = new MsgData();
				data.setValue("感谢您的光临，希望您下次再来");
				map.put("remark", data);

				msg.setData(map);
				TemplateMsgUtil.sendTmplateMsg(token.getToken(), msg);
			}
			
			if(null != orderType && "0".equals(orderType)) {
				return new ModelAndView(sb.insert(0,"redirect:/bookMeal/orderDetail.do").toString());
			}else if(null != orderType && "2".equals(orderType)){
				return new ModelAndView(sb.insert(0,"redirect:/takeout/orderDetail.do").toString());
			}else{
				//取消订位单相关联的点菜单
				if(list!=null && list.size()>0){
					Net_Orders order = list.get(0);
					if(StringUtils.hasText(order.getBookDeskOrderID())){//如果是订位单
						bookDeskService.cancelOrders(order.getBookDeskOrderID(),order.getOpenid(),pk_group,firmid,"","",order.getSumprice(),"-1");//用orderType=-1区分订位单
					}
				}
			}
			
			return new ModelAndView(sb.insert(0,"redirect:/bookDesk/orderDetail.do").toString());
		}catch(Exception e){
			e.printStackTrace();
		}
		return new ModelAndView(BookDeskConstants.FAILEDPAGE,modelMap);
	}

	/**
	 * 
	 * @param session
	 * @param modelMap
	 * @param code
	 * @param state
	 * @param pk_group
	 * @return
	 * 我的订位单
	 */
	@RequestMapping(value="findOrders")
	public ModelAndView findOrders(HttpServletRequest request, ModelMap modelMap, String code, String state){
		try {
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

			String openid = request.getParameter("openid");
			if(openid==null || "".equals(openid)){
				WeChatUser user = (WeChatUser) request.getSession().getAttribute("WeChatUser");
				if(user == null){//返回按钮，已取得用户授权的情况
					//通过网页授权方式获取openID
					String oauth2[] = WeChatUtil.getOAuth2(appId, secret, code);
					//获取用户基本信息
					AccessToken token = WeChatUtil.getAccessToken(appId, secret);
					user = WeChatUtil.getWeChatUser(oauth2[0],token.getToken());
					if(StringUtils.hasText(user.getOpenid())){
						request.getSession().setAttribute("WeChatUser", user);
					}
				}
				openid = user.getOpenid();
			}

			String orderState = "0";

			List<Net_Orders> listNet_Orders = null;
			if(openid!=null && !"".equals(openid)){
				listNet_Orders = getOrders(pk_group,openid,"1",orderState);
			}
			
			/*
			String isCnSft = Commons.getConfig().getProperty("isCnSft");
			Map<String,String> sftNameMap = null;
			if(isCnSft==null || "".equals(isCnSft)){//是否启用中餐餐次
				sftNameMap = bookDeskMapper.getShiftSft();
			}else{
				sftNameMap = bookDeskMapper.getFirmSft(order.getFirmid());
			}
			*/
			
			modelMap.put("isCnSft",Commons.getConfig().getProperty("isCnSft"));
			modelMap.put("sfts",bookDeskMapper.getShiftSft());
			modelMap.put("listNet_Orders",listNet_Orders);
			modelMap.put("pk_group",pk_group);
			modelMap.put("openid",openid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(BookDeskConstants.ORDERS,modelMap);
	}

	/**
	 * 
	 * @param request
	 * @param modelMap
	 * @param code
	 * @param state
	 * @return
	 * 我的点菜单
	 */
	@RequestMapping(value="findMenuOrders")
	public ModelAndView findMenuOrders(HttpServletRequest request, ModelMap modelMap, String code, String state){
		try {
			String pk_group = request.getParameter("pk_group");
			String openid = request.getParameter("openid");
			if(!StringUtils.hasText(openid)) {
				openid = request.getParameter("openID");
			}
			if(null == openid || openid.isEmpty()) {
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

				WeChatUser user = (WeChatUser) request.getSession().getAttribute("WeChatUser");
				if(user == null || user.getOpenid()==null || "".equals(user.getOpenid())){//返回按钮，已取得用户授权的情况
					//通过网页授权方式获取openID
					String oauth2[] = WeChatUtil.getOAuth2(appId, secret, code);
					//获取用户基本信息
					AccessToken token = WeChatUtil.getAccessToken(appId, secret);
					user = WeChatUtil.getWeChatUser(oauth2[0],token.getToken());
//					if(ValueCheck.IsEmpty(user.getOpenid())){
//						user.setOpenid("oQcZ9t7jysCVrMiGJ0q7OJodXysI");
//					}
					if(StringUtils.hasText(user.getOpenid())){
						request.getSession().setAttribute("WeChatUser", user);
					}
				}
				openid = user.getOpenid();
			}

			String orderState = "0";

			List<Net_Orders> listNet_Orders = null;
			if(openid!=null && !"".equals(openid)){
				listNet_Orders = getOrders(pk_group,openid,"0",orderState);
			}

			modelMap.put("listNet_Orders",listNet_Orders);
			modelMap.put("pk_group",pk_group);
			modelMap.put("openid",openid);
			modelMap.put("code",code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(BookDeskConstants.MENUORDERS,modelMap);
	}
	
	@RequestMapping(value="findTakeOutOrders")
	public ModelAndView findTakeOutOrders(ModelMap modelMap, HttpServletRequest request, String code, String state){
		try {
			String pk_group = request.getParameter("pk_group");
			String openid = request.getParameter("openid");
			if(!StringUtils.hasText(openid)) {
				openid = request.getParameter("openID");
			}
			if(null == openid || openid.isEmpty()) {
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

				WeChatUser user = (WeChatUser) request.getSession().getAttribute("WeChatUser");
				if(user == null || user.getOpenid()==null || "".equals(user.getOpenid())){//返回按钮，已取得用户授权的情况
					//通过网页授权方式获取openID
					String oauth2[] = WeChatUtil.getOAuth2(appId, secret, code);
					//获取用户基本信息
					AccessToken token = WeChatUtil.getAccessToken(appId, secret);
					user = WeChatUtil.getWeChatUser(oauth2[0],token.getToken());
					if(StringUtils.hasText(user.getOpenid())){
						request.getSession().setAttribute("WeChatUser", user);
					}
				}
				openid = user.getOpenid();
			}

			String orderState = "0";

			List<Net_Orders> listNet_Orders = null;
			if(openid!=null && !"".equals(openid)){
				listNet_Orders = getOrders(pk_group,openid,"2",orderState);
			}

			modelMap.put("listNet_Orders",listNet_Orders);
			modelMap.put("pk_group",pk_group);
			modelMap.put("openid",openid);
			modelMap.put("code",code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("takeout/takeoutOrders",modelMap);
	}

	@RequestMapping(value="getOrders")
	@ResponseBody
	public List<Net_Orders> getOrders(ModelMap modelMap, String pk_group, String openid, String orderType, String orderState){
		if(openid==null || "".equals(openid)){
			return null;
		}
		return getOrders(pk_group,openid,orderType,orderState);
	}

	/**
	 * 
	 * @param pk_group
	 * @param orderType(NETORDERS.ISFEAST) 0 订餐 1 订桌
	 * @param orderState 0 进行中 1 已结单
	 * @return
	 */
	private List<Net_Orders>  getOrders(String pk_group, String openid, String orderType, String orderState){
		Map<String,String> map = new HashMap<String,String>();
		map.put("openid", openid);
		map.put("orderType", orderType);
		if(orderState==null || "".equals(orderState)){
			map.put("state2", "2");
		}else{
			if("0".equals(orderState)){
				map.put("state2", "2");
			}else if("1".equals(orderState)){
				map.put("state1", "2");
			}
		}
		List<Net_Orders> listNet_Orders = bookDeskMapper.getOrderMenus(map);
		if(listNet_Orders!=null && !listNet_Orders.isEmpty()){
			for(Net_Orders net_Orders : listNet_Orders){
				net_Orders.setMoney(WeChatUtil.formatDoubleLength(net_Orders.getMoney(), 2));
			}
		}
		return listNet_Orders;
	}
	
	@RequestMapping(value="gotoMap")
	public ModelAndView gotoMap(ModelMap modelMap, HttpServletRequest request){
		String LatLng = request.getParameter("LatLng");
		String address = request.getParameter("address");
		String openid = request.getParameter("openid");
		String pk_group = request.getParameter("pk_group");
		String firmName = request.getParameter("firmName");
		String city = request.getParameter("city");
		String addr = request.getParameter("addr");
		String position = request.getParameter("position");
		
		modelMap.put("city", city);
		modelMap.put("firmName", firmName);
		modelMap.put("LatLng", LatLng);
		modelMap.put("openid", openid);
		modelMap.put("pk_group", pk_group);
		modelMap.put("addr", addr);
		modelMap.put("address", address);
		modelMap.put("position", position);
		String ak = Commons.baidumap;
		if(ak==null || "".equals(ak)){
			ak = "OK0b22YVQNCccuyzf3hGhrVx";
		}
		modelMap.put("ak", ak);
		return new ModelAndView(BookDeskConstants.BAIDUMAP,modelMap);
	}
	/**
	 * 扫码下单更新订单桌号
	 * @param modelMap
	 * @param request
	 * @param orders
	 * @return
	 */
	@RequestMapping(value="commitOrdr")
	@ResponseBody
	public Object updateOrdr(ModelMap modelMap,HttpServletRequest request,Net_Orders orders){
		// 返回结果
		JSONObject result = new JSONObject();
		try{
			Firm firm = new Firm();
			/*orders.setState("7");
			String res = bookDeskMapper.updateOrdr(orders);*/

			Map<String, String> map = new HashMap<String, String>();
			map.put("id", orders.getId());
			map.put("firmid", orders.getFirmid());
			if(null != request) {
				map.put("openid", request.getParameter("openid"));
				map.put("orderType", request.getParameter("orderType"));
			} else {
				map.put("openid", orders.getOpenid());
				map.put("orderType", orders.getType());
			}
			//查询账单主表
			List<Net_Orders> listNet_Orders = bookMealMapper.getOrderMenus(map);
			if (listNet_Orders != null && !listNet_Orders.isEmpty()) {
				orders = listNet_Orders.get(0);
				//查询门店
				List<Firm> listFirm = bookMealMapper.getFirmList(null, null, orders.getFirmid());

				if (listFirm != null && !listFirm.isEmpty()) {
					firm =  listFirm.get(0);
					orders.setVcode(firm.getFirmCode());
				}else{
					return "门店不存在";
				}
				//查询账单明细
				List<Net_OrderDtl> orderDtl = PubitemSearch.getOrderDtlMenus(orders.getId());
				
				// 查询菜品门店编码
				List<FdItemSale> listPubItemCode = PubitemSearch.getListPubItemCode(orders.getFirmid());
				//获取套餐门店编码
				List<FdItemSale> listPackageCode = orderMapper.getListPackageCode(orders.getFirmid());
				listPubItemCode.addAll(listPackageCode);
				Map<String,String> mapPubItemCode = new HashMap<String,String>();
				Map<String,String> mapPubItemName = new HashMap<String,String>();
				// 将菜品门店编码容器改为map 提高查找效率
				for(FdItemSale item : listPubItemCode) {
					mapPubItemCode.put(item.getId(), item.getItcode());
					mapPubItemName.put(item.getId(), item.getDes());
				}

				// 获取菜品附加项列表
				List<NetDishAddItem> listNetDishAddItem = bookMealMapper.getDishAddItemList(orders.getPk_group(), orders.getId());
				// 获取附加产品列表
				List<NetDishProdAdd> listNetDishProdAdd = bookMealMapper.getDishProdAddList(orders.getPk_group(), orders.getId());
				// 将附加产品作为附加项传给POS
				if(null != listNetDishProdAdd && !listNetDishProdAdd.isEmpty()) {
					for(NetDishProdAdd item : listNetDishProdAdd) {
						NetDishAddItem temp = new NetDishAddItem();
						temp.setFcode(item.getFcode());
						temp.setNcount(item.getNcount());
						temp.setNprice(item.getNprice());
						temp.setPk_dishAddItem(item.getPk_dishProdAdd());
						temp.setPk_group(item.getPk_group());
						temp.setPk_orderDtlId(item.getPk_orderDtlId());
						temp.setPk_ordersId(item.getPk_ordersId());
						temp.setPk_prodcutReqAttAc(item.getPk_prodReqAdd());
						temp.setPk_redefine(item.getPk_prodAdd());
						temp.setPk_pubItem(item.getPk_pubitem());
						temp.setRedefineName(item.getProdAddName());
						temp.setSelfPkPubitem(item.getSelfPkPubitem());
						
						// 设置菜品门店编码
						for(FdItemSale pubitem : listPubItemCode) {
							if(temp.getRedefineName().equals(pubitem.getDes())) {
								temp.setFcode(pubitem.getItcode());
								break;
							}
						}
						
						listNetDishAddItem.add(temp);
					}
				}
				double totalPrice = 0.0;
				// 是否删除自动添加的菜品，如果所有菜品的类别和配置文件中的相同，则删除
				boolean delAutoItem = true;
				String delAutoPubitemType = Commons.getConfig().getProperty("delAutoPubitemType");
				//循环账单明细，将附加项与菜品关联起来
				for (Net_OrderDtl dtl : orderDtl) {
					if (null != dtl.getPrice() && !"".equals(dtl.getPrice())) {
						totalPrice += Double.parseDouble(dtl.getPrice()) * Integer.parseInt(dtl.getFoodnum());
					}
					
					// 设置菜品的门店编码
					for(FdItemSale item : listPubItemCode) {
						if(dtl.getFoodsid().equals(item.getId())) {
							dtl.setPcode(item.getItcode());
							break;
						}
					}
					
					// 将附加项添加到菜品下
					List<NetDishAddItem> tempList = new ArrayList<NetDishAddItem>();
					for (NetDishAddItem item : listNetDishAddItem) {
						if (dtl.getFoodsid().equals(item.getPk_pubItem()) && dtl.getId().equals(item.getPk_orderDtlId())) {
							// 设置菜品的门店编码
							if(StringUtils.hasText(item.getSelfPkPubitem())) {
								item.setFcode(mapPubItemCode.get(item.getSelfPkPubitem()));
							}
							tempList.add(item);
							if(null != item.getNprice() && !"".equals(item.getNprice())) {
								totalPrice += Double.parseDouble(item.getNprice()) * Integer.parseInt(dtl.getFoodnum());
							}
						}
					}
					if (!tempList.isEmpty()) {
						dtl.setListDishAddItem(tempList);
					}
					
					/*// 将附加产品添加到菜品下
					if(null != listNetDishProdAdd && !listNetDishProdAdd.isEmpty()) {
						List<NetDishProdAdd> addList = new ArrayList<NetDishProdAdd>();
						for(NetDishProdAdd item : listNetDishProdAdd) {
							if (dtl.getId().equals(item.getPk_orderDtlId()) && dtl.getFoodsid().equals(item.getPk_pubitem())) {
								addList.add(item);
							}
						}
						if (!addList.isEmpty()) {
							dtl.setListDishProdAdd(addList);
						}
					}*/
					
					if(null != dtl.getGrptyp() && !dtl.getGrptyp().equals(delAutoPubitemType)) {
						// 有不同的类别，不删除
						delAutoItem = false;
					}
					//如果菜品是套餐
					if("1".equals(dtl.getIspackage())){
						List<Map<String,Object>> listPacageDtl = PubitemSearch.listFolioPacageDtl(dtl);
						if(listPacageDtl != null && listPacageDtl.size()>0){
							for (Map<String,Object> mapDtl : listPacageDtl){
								mapDtl.put("tcpcode", mapPubItemCode.get(mapDtl.get("pk_pubitem")));//获取菜品编码
								mapDtl.put("tcpname", mapPubItemName.get(mapDtl.get("pk_pubitem")));//获取菜品名称
								mapDtl.put("tctotalprice", mapDtl.get("tcprice"));//获取菜品总金额
								//处理套餐菜品的附加项及附加产品
								tempList = new ArrayList<NetDishAddItem>();
								for (NetDishAddItem item : listNetDishAddItem) {
									if (mapDtl.get("pk_orderpackagedetail").equals(item.getPk_orderDtlId()) && mapDtl.get("pk_pubitem").equals(item.getPk_pubItem())) {
										tempList.add(item);
										if(null != item.getNprice() && !"".equals(item.getNprice())) {
											totalPrice += Double.parseDouble(item.getNprice()) * Integer.parseInt(dtl.getFoodnum());
										}
									}
								}
								if (!tempList.isEmpty()) {
									mapDtl.put("tclistDishAddItem", tempList);
								}
							}
							dtl.setListDishTcItem(listPacageDtl);
						}
					}
				}
				orders.setListNetOrderDtl(orderDtl);
				
				// 自动添加一个菜品
				if(!delAutoItem && null != orders.getTables() && !orders.getTables().equals("")) {
					Net_Orders addOrder = new Net_Orders();
					addOrder.setId(orders.getId());
					addOrder.setFirmid(orders.getFirmid());
					addOrder.setTables(orders.getTables());
					addOrder.setPax(orders.getPax());
					List<Net_OrderDtl> addDtl = new ArrayList<Net_OrderDtl>();
					addOrder.setListNetOrderDtl(addDtl);
					bookMealMapper.addOpenItem(addOrder);
					
					// 判断订单中是否已包含开台菜品
					boolean itemExist = false;
					for(Net_OrderDtl dtl : orderDtl) {
						for(Net_OrderDtl subDtl : addOrder.getListNetOrderDtl()) {
							// 设置菜品的门店编码
							for(FdItemSale item : listPubItemCode) {
								if(subDtl.getFoodsid().equals(item.getId())) {
									subDtl.setPcode(item.getItcode());
									break;
								}
							}
							
							if(dtl.getFoodsid().equals(subDtl.getFoodsid())) {
								itemExist = true;
								break;
							}
						}
						if(itemExist) {
							break;
						}
					}
					// 订单中未包含自动添加的菜品，保存菜品明细
					if(!itemExist) {
						bookMealMapper.saveOrderDtl(addOrder);
						orders.getListNetOrderDtl().addAll(addOrder.getListNetOrderDtl());
					}
				}
				
				orders.setSumprice(String.valueOf(totalPrice));
				orders.setSerialid(CodeHelper.createUUID());
				String needSite = Commons.getConfig().getProperty("needSite");
				if("0".equals(needSite)){//如果不需要验证台位，type=37
					orders.setType("37");
				}else{//如果需要验证台位，type=1
					orders.setType("1");
				}
				
				//记录本次记录
				Map<String,Object> mqMap = new HashMap<String,Object>();
				mqMap.put("pk_mqlogs", orders.getSerialid());
				mqMap.put("orderid", orders.getId());
				mqMap.put("vtype", "1");
				mqMap.put("errmsg", "");
				mqMap.put("state", "");
				
				result.put("serialid", orders.getSerialid());
				// 如果mqtype不为空，此次下单为检测是否可下单，设置type
				if(null != request) {
					String mqtype = request.getParameter("mqtype");
					if(StringUtils.hasText(mqtype)) {
						orders.setType(mqtype);
						mqMap.put("state", "1");
					}
				}
				
				bookDeskMapper.addMqLogs(mqMap);
				//发送mq消息
				orders.setSerialid(mqMap.get("pk_mqlogs")+"");//重置mq记录的主键
				JSONObject json = JSONObject.fromObject(orders);
				LogUtil.writeToTxt(LogUtil.MQSENDORDER, "下单到MQ服务器中，队列名："+firm.getFirmCode()+"_orderList，订单对象："+json.toString());
				try{
					MqSender.sendOrders(json.toString(),firm.getFirmCode()+"_orderList",Commons.mqEffectiveTime);//设置消息有效期为半小时
				}catch(Exception e){
					LogUtil.writeToTxt(LogUtil.MQSENDORDER, "下单到MQ服务器中发生错误，错误原因："+e.getCause().getMessage()+"。队列名："+firm.getFirmCode()+"_orderList，订单对象："+json.toString());

				}
			}else{
				result.put("code", "账单不存在");
				return result;
			}
			
			result.put("code", "1");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		result.put("code", "12");
		return result;
	}
	@RequestMapping(value="/deleteDetails")
	@ResponseBody
	public Object deleteDetails(HttpServletRequest request,Net_Orders orders){
		try{
			bookDeskMapper.deleteDetails(orders);
		}catch(Exception e){
			e.printStackTrace();
			return e.getCause().getMessage();
		}
		return "1";
		
	}
	
	/**
	 * 获取预下单pos返回消息
	 * @param serialid
	 * @return
	 */
	@RequestMapping(value="getPreOrderInfo")
	@ResponseBody
	public Object getPreOrderInfo(HttpServletRequest request) {
		String serialid = request.getParameter("serialid");
		Map<String, JSONObject> infoMap = WeChatUtil.getPreOrderInfoMap();
		if(null != infoMap && infoMap.containsKey(serialid)) {
			return infoMap.get(serialid);
		}
		return "";
	}
	
	public BookMealMapper getBookMealMapper() {
		return bookMealMapper;
	}
	
	public void setBookMealMapper(BookMealMapper bookMealMapper) {
		this.bookMealMapper = bookMealMapper;
	}
	
	public BookDeskMapper getBookDeskMapper() {
		return bookDeskMapper;
	}
	
	public void setBookDeskMapper(BookDeskMapper bookDeskMapper) {
		this.bookDeskMapper = bookDeskMapper;
	}
}