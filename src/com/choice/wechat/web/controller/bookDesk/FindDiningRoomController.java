package com.choice.wechat.web.controller.bookDesk;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.choice.test.domain.Card;
import com.choice.test.domain.DeskTimes;
import com.choice.test.domain.Net_Orders;
import com.choice.test.service.CardSearch;
import com.choice.test.utils.CodeHelper;
import com.choice.test.utils.Commons;
import com.choice.test.utils.DateFormat;
import com.choice.test.utils.GenerateRandom;
import com.choice.wechat.common.AccessToken;
import com.choice.wechat.common.WeChatUser;
import com.choice.wechat.constants.bookDesk.FindDiningRoomConstants;
import com.choice.wechat.domain.bookDesk.Brands;
import com.choice.wechat.domain.bookDesk.City;
import com.choice.wechat.domain.bookDesk.Company;
import com.choice.wechat.domain.bookDesk.Firm;
import com.choice.wechat.domain.bookDesk.MealTime;
import com.choice.wechat.domain.bookDesk.StoreTable;
import com.choice.wechat.domain.bookDesk.Street;
import com.choice.wechat.domain.game.Actm;
import com.choice.wechat.domain.templateMsg.BaseTemplateMsg;
import com.choice.wechat.domain.templateMsg.MsgData;
import com.choice.wechat.persistence.bookDesk.BookDeskMapper;
import com.choice.wechat.persistence.bookMeal.BookMealMapper;
import com.choice.wechat.persistence.common.location.LocationMapper;
import com.choice.wechat.persistence.takeout.TakeOutMapper;
import com.choice.wechat.service.bookDesk.IBookDeskService;
import com.choice.wechat.service.campaign.CampaignTools;
import com.choice.wechat.util.BaiduMapApi;
import com.choice.wechat.util.BuilderOrderMessage;
import com.choice.wechat.util.LogUtil;
import com.choice.wechat.util.MessageUtil;
import com.choice.wechat.util.Sign;
//import com.choice.wechat.util.MemCachedUtil;
import com.choice.wechat.util.TemplateMsgUtil;
import com.choice.wechat.util.WeChatUtil;
import com.choice.wechat.util.mq.MqSender;
/**
 * 
 * @author song
 * 找餐厅
 *
 */
@Controller
@RequestMapping(value="dining")
public class FindDiningRoomController {
	@Autowired
	private BookDeskMapper bookDeskMapper;
	@Autowired
	private IBookDeskService bookDeskService;
	@Autowired
	private LocationMapper locationMapper;
	@Autowired
	private BookMealMapper bookMealMapper;
	@Autowired
	private TakeOutMapper takeOutMapper;

	private static Logger log = Logger.getLogger(FindDiningRoomController.class);
	
	@RequestMapping(value="homePage")
	public ModelAndView homePage(ModelMap modelMap, HttpServletRequest request, String code){
		StringBuffer sb = request.getRequestURL();
		String baseUrl = sb.substring(0, sb.lastIndexOf("/"));
		modelMap.put("baseUrl", baseUrl);
		
		String pk_group = request.getParameter("pk_group");
		//根据pk_group，获取配置信息
		Company company = WeChatUtil.getCompanyInfo(pk_group);
		String appId = Commons.appId;
		if(null != company) {
			if (null != company.getAppId() && !"".equals(company.getAppId())) {
				appId = company.getAppId();
			}
		}

		modelMap.put("appId", appId);
		
		return new ModelAndView(FindDiningRoomConstants.HOMEPAGE,modelMap);
	}
	/**
	 * 微信jssdk获取位置
	 */
	@RequestMapping(value="listFirm")
	public ModelAndView listFirm(HttpSession session, HttpServletRequest request, ModelMap modelMap, String code, String state, String pk_group){
		try {
			String openid = request.getParameter("openID");
			if(!StringUtils.hasText(code) && !StringUtils.hasText(openid)){
				return null;
			}
			log.debug("查询门店列表方法开始");
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
			
			String nextType = request.getParameter("nextType");
			session.setAttribute("nextType", nextType);//点击门店后，跳转类型
			log.debug("获取微信ACCESSTOKEN开始");
			AccessToken token = WeChatUtil.getAccessToken(appId, secret);
			log.debug("获取微信ACCESSTOKEN结束");
			WeChatUser user = (WeChatUser) session.getAttribute("WeChatUser");
			if(user == null){//返回按钮，已取得用户授权的情况
				//通过网页授权方式获取openID
				log.debug("获取微信OPENID开始");
				String oauth2[] = WeChatUtil.getOAuth2(appId, secret, code);
				log.debug("获取微信OPENID结束");
				//获取用户基本信息
				log.debug("获取微信用户信息开始");
				user = WeChatUtil.getWeChatUser(oauth2[0],token.getToken());
				log.debug("获取微信用户信息结束");
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
			/*String lat = (String) session.getAttribute("lat");
			String lng = (String) session.getAttribute("lng");
			
			if(StringUtils.hasText(lat) && StringUtils.hasText(lng)){
				return listFirmFromCity(request,modelMap,openid,pk_group);
			}
			
			log.debug("查询系统中是否保存有用户的地理坐标开始");
			Map<String,Object> location = locationMapper.getLocation(StringUtils.hasText(openid)?openid:"-99999");
			log.debug("查询系统中是否保存有用户的地理坐标结束");
			if(location != null){
				lat = (String) location.get("LAT");
				lng = (String) location.get("LNG");
				if(StringUtils.hasText(lat) && StringUtils.hasText(lng)){
					session.setAttribute("lat", lat);
					session.setAttribute("lng", lng);
					return listFirmFromCity(request,modelMap,openid,pk_group);
				}
			}*/
			
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
			log.debug("生成微信JSSDK签名开始");
			Map<String, String> signMap = Sign.sign(WeChatUtil.getJsapiTicket(appId, token.getToken()).getTicket(),sb.toString());
			modelMap.put("signMap", signMap);
			log.debug("生成微信JSSDK签名结束");
			
			// 绿数对接，保存公众号base id
			if(null == user) {
				user = new WeChatUser();
			}
			user.setPubid(request.getParameter("pubID"));
			user.setUnionid(request.getParameter("uuid"));
			MessageUtil.setUser(openid, user);
			
			return new ModelAndView(FindDiningRoomConstants.GETLOCATION,modelMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(FindDiningRoomConstants.LISTFIRM,modelMap);
	}
	/**
	 * 订位基本信息
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value="listFirmFromCity")
	public ModelAndView listFirmFromCity(HttpServletRequest request, ModelMap modelMap, String openid, String pk_group){
		try {
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

			String nextType = request.getParameter("nextType");
			if(nextType!=null && !"".equals(nextType)){
				session.setAttribute("nextType", nextType);//点击门店后，跳转类型
			}else{
				nextType = (String)session.getAttribute("nextType");
			}
			
			String LatLng = lat+","+lng;
			String coord_type = request.getParameter("coord_type");//坐标类型
			if(coord_type==null || "".equals(coord_type)){//如果为空，默认不是百度坐标
				try{
					log.debug("调用百度API转换坐标开始");
					LatLng = BaiduMapApi.geoconv(lat, lng);//转换坐标
					log.debug("调用百度API转换坐标结束");
				}catch(Exception e){
					e.printStackTrace();
				}
			}

			modelMap.put("LatLng", LatLng);

			//城市
			log.debug("获取城市列表开始");
			List<City> listCity = bookDeskMapper.getCityList(pk_group, null);
			log.debug("获取城市列表结束");
			City userCity = null;//默认为全部
			log.debug("调用百度API解析坐标开始");
			Map<String,String> map = BaiduMapApi.getLocation(lat, lng);
			log.debug("调用百度API解析坐标结束");
			String cityName = map.get("city");
			String address = map.get("address");
			modelMap.put("address", address);

			for(City city:listCity){//找到用户基本信息匹配的城市
				if(cityName.contains(city.getVname())){
					userCity = city;
				}
			}
			
			modelMap.put("userCity", userCity==null?new City():userCity);//用户城市
			
			Map<String,String> datAndSft = getDefaultDatAndSft();
			//根据城市查询店铺
			Map<String,String> param = new HashMap<String,String>();
			param.put("pk_group", pk_group);
			param.put("pk_city", userCity==null?"":userCity.getPk_city());
			param.put("openid", openid);
			//增加判断是 外卖 、订位、等位、点餐。
			param.put("nextType", nextType);
			//根据配置文件过滤品牌
			String brandFilter = Commons.getConfig().getProperty("brandFilter");
			if(brandFilter!=null && !"".equals(brandFilter)){
				StringBuilder brand_sb = new StringBuilder();
				if(brandFilter.indexOf(",")>-1){
					String brand_keys[] = brandFilter.split(",");
					for(int i=0;i<brand_keys.length;i++){
						brand_sb.append("'").append(brand_keys[i]).append("',");
					}
					brand_sb.deleteCharAt(brand_sb.length()-1);
				}else{
					brand_sb.append("'").append(brandFilter).append("'");
				}
				brandFilter = brand_sb.toString();
			}
			param.put("brandFilter", brandFilter);
			
			log.debug("获取门店列表开始");
			List<Firm> listFirm = choiceFirm(param,datAndSft.get("dat"),datAndSft.get("sft"));
			//如果当前城市没有店铺，查询所有城市的
			String pk_city = param.get("pk_city");
			if(StringUtils.hasText(pk_city)){
				if(listFirm==null || listFirm.size()<=0){
					param.put("pk_city","");
					listFirm = bookDeskMapper.getFirmList(param);//查询全部城市的店铺
					
					modelMap.put("userCity", new City());//用户城市 全部
				}
			}
			
			log.debug("获取门店列表结束");
			//距离
			log.debug("调用百度API获取距离");
			BaiduMapApi.getDistance(listFirm, lat, lng);
			
			// 根据距离过滤门店列表
			filterFirmByDistance(listFirm);
			
			//按距离排序
			try{
				Collections.sort(listFirm, new Comparator<Firm>() {   
					public int compare(Firm f1, Firm f2) {
						if(f1.getDistance()==null){
							return 1;
						}else if(f2.getDistance()==null){
							return -1;
						}else{
							return f1.getDistance().compareTo(f2.getDistance());
						}
					}
				});
			}catch(Exception e){}
			modelMap.put("sort", "2");//排序规则
			
			//品牌
			log.debug("获取品牌列表");
			List<Brands> brands = bookDeskMapper.getBrands(pk_group,brandFilter);
			//区域
			log.debug("获取街道列表");
			List<Street> streets = null;
			if(userCity!=null && listFirm!=null && listFirm.size()>0){//取全部城市店铺的时候
				streets = bookDeskMapper.getStreet(userCity.getPk_city());
			}
			
			// 获取门店优惠活动
			/*for(Firm firm : listFirm) {
				// 读取优惠活动
				List<Actm> listActm = CampaignTools.getListTakeOutActm(firm.getFirmid());
				firm.setListActm(listActm);
			}*/

			modelMap.put("listCity", listCity);//城市列表
			modelMap.put("listFirm", listFirm);//门店列表
			modelMap.put("brandsList", brands);//品牌列表
			modelMap.put("streetsList", streets);//街道列表
			modelMap.put("openid", openid);
			modelMap.put("pk_group", pk_group);
			log.debug("查询门店列表方法结束");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(FindDiningRoomConstants.LISTFIRM,modelMap);
	}

	@RequestMapping(value="getStreet")
	@ResponseBody
	public List<Street> getStreet(ModelMap modelMap, String pk_city){
		List<Street> listStreet = null;
		try{
			listStreet = bookDeskMapper.getStreet(pk_city);
		}catch(Exception e){
			e.printStackTrace();
		}
		return listStreet;
	}

	@RequestMapping(value="choiceFirm")
	@ResponseBody
	public List<Firm> choiceFirm(ModelMap modelMap, HttpServletRequest request){
		List<Firm> listFirm = null;
		try{
			String pk_group = request.getParameter("pk_group");
			String pk_city = request.getParameter("pk_city"); 
			String brands = request.getParameter("brands"); 
			String sort = request.getParameter("sort"); 
			String street = request.getParameter("street");
			String storeName = request.getParameter("storeName");
			String openid = request.getParameter("openid");
			
			Map<String,String> datAndSft = getDefaultDatAndSft();
			String dat = datAndSft.get("dat");
			String sft = datAndSft.get("sft");

			HttpSession session = request.getSession();
			
			String lat =  request.getParameter("lat");
			String lng =  request.getParameter("lng");
			
			// 根据地址，计算经纬度
			String address = request.getParameter("address");
			String locInfo = BaiduMapApi.getLatLng(openid, address);
			if(StringUtils.hasText(locInfo)) {
				String[] res = locInfo.split(",");
				if(null != res && res.length > 1) {
					lat = res[0];
					lng = res[1];
				}
			}
			
			if(!StringUtils.hasText(lat)){
				lat = (String) session.getAttribute("lat");
			}
			if(!StringUtils.hasText(lng)){
				lng = (String) session.getAttribute("lng");
			}
			/*session.setAttribute("lat", lat);
			session.setAttribute("lng", lng);*/

			//根据城市查询店铺
			Map<String,String> param = new HashMap<String,String>();
			param.put("pk_group", pk_group);
			param.put("pk_city", pk_city);
			param.put("brands", brands);
			//param.put("sort", sort);
			param.put("street", street);
			param.put("vname", storeName);
			param.put("openid", openid);
			//过滤外卖
			
			String nextType = request.getParameter("nextType");
			if(nextType!=null && !"".equals(nextType)){
				session.setAttribute("nextType", nextType);//点击门店后，跳转类型
			}else{
				nextType = (String)session.getAttribute("nextType");
			}
			param.put("nextType", nextType);
			
			//根据配置文件过滤品牌
			String brandFilter = Commons.getConfig().getProperty("brandFilter");
			if(brandFilter!=null && !"".equals(brandFilter)){
				StringBuilder brand_sb = new StringBuilder();
				if(brandFilter.indexOf(",")>-1){
					String brand_keys[] = brandFilter.split(",");
					for(int i=0;i<brand_keys.length;i++){
						brand_sb.append("'").append(brand_keys[i]).append("',");
					}
					brand_sb.deleteCharAt(brand_sb.length()-1);
				}else{
					brand_sb.append("'").append(brandFilter).append("'");
				}
				brandFilter = brand_sb.toString();
			}
			param.put("brandFilter", brandFilter);
			
			listFirm = choiceFirm(param,dat,sft);
			//距离
			BaiduMapApi.getDistance(listFirm, lat, lng);
			
			// 根据距离过滤门店列表
			filterFirmByDistance(listFirm);
			
			if(StringUtils.hasText(sort) && "2".equals(sort)){
				try {
					Collections.sort(listFirm, new Comparator<Firm>() {   
						public int compare(Firm f1, Firm f2) {
							if(f1.getDistance()==null){
								return 1;
							}else if(f2.getDistance()==null){
								return -1;
							}else{
								return f1.getDistance().compareTo(f2.getDistance());
							}
						}
					});
				} catch(Exception ex) {
					log.error("获取门店距离错误：" + ex.getMessage());
				}
			}
			
			// 获取门店优惠活动
			for(Firm firm : listFirm) {
				// 读取优惠活动
				//List<Actm> listActm = CampaignTools.getListTakeOutActm(firm.getFirmid());
				//firm.setListActm(listActm);
				List<Actm> listActm = new ArrayList<Actm>();
				firm.setListActm(listActm);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return listFirm;
	}
	
	/**
	 * 根据距离过滤门店列表
	 * @param listFirm
	 */
	public void filterFirmByDistance(List<Firm> listFirm) {
		//门店列表展示距离范围，单位为公里，不填则为不限制距离
		String kiloRange = Commons.getConfig().getProperty("kiloRange");
		if(StringUtils.hasText(kiloRange)) {
			//若限制展示范围，剔除范围外的门店
			for(int i = listFirm.size() - 1; i >= 0; i--) {
				Firm firm = listFirm.get(i);
				//门店距离
				if(firm.getDistance()!=null){//如果不加判断，下行有时会报空指针
					long distance = firm.getDistance();
					if(distance > Long.parseLong(kiloRange) * 1000) {
						listFirm.remove(i);
				}
				
				}
			}
		}
	}

	/**
	 * 选择门店
	 * @return
	 */
	public List<Firm> choiceFirm(Map<String,String> param, String dat, String sft){
		try {
			//String pk_city = param.get("pk_city");
			//String pk_group = param.get("pk_group");

			//String key = pk_group==null?"":pk_group + pk_city;

			List<Firm> listFirm = null;
			/*
			if(MemCachedUtil.getObject(key) != null) {
				listFirm = (List<Firm>)MemCachedUtil.getObject(key);
			} else {
				//门店列表
				listFirm = bookDeskMapper.getFirmList(param);
				if(listFirm!=null){
					for(Firm f:listFirm){
						Map<String,Object> map = bookDeskMapper.getDeskState(f.getFirmid(), dat, sft);
						double total = ((BigDecimal) map.get("TOTAL")).doubleValue();
						double book = ((BigDecimal) map.get("BOOK")).doubleValue();
						if(total>0){
							double baifenbi = book/total;
							if(baifenbi<=0.3){
								f.setDeskState(2);//紧张
							}else if(baifenbi>0.7){
								f.setDeskState(0);//空闲
							}else{
								f.setDeskState(1);//中等
							}
						}

						f.setActmList(bookDeskMapper.getFirmActm(f.getFirmid()));

					}
				}

				MemCachedUtil.setObject(key, listFirm);
			 */
			//门店列表
			listFirm = bookDeskMapper.getFirmList(param);
			if(listFirm!=null){
				for(Firm f:listFirm){
					Map<String,Object> map = bookDeskMapper.getDeskState(f.getFirmid(), dat, sft);
					double total = ((BigDecimal) map.get("TOTAL")).doubleValue();
					BigDecimal b = (BigDecimal) map.get("BOOK");
					double book = 0;
					if(b!=null){
						book = b.doubleValue();
					}
					if(total>0){
						double baifenbi = book/total;
						if(baifenbi<=0.3){
							f.setDeskState(2);//紧张
						}else if(baifenbi>0.7){
							f.setDeskState(0);//空闲
						}else{
							f.setDeskState(1);//中等
						}
					}
					f.setActmList(bookDeskMapper.getFirmActm(f.getFirmid()));
				}
			}
			return listFirm;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 取默认的日期、餐次
	 */
	private Map<String,String> getDefaultDatAndSft(){
		Map<String,String> map = new HashMap<String,String>();

		//根据时间确定餐次
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String dat = "";//所订餐日期
		String sft = "1";//餐次

		Calendar c = Calendar.getInstance();  
		c.setTime(new Date()); 

		int currHour = c.get(Calendar.HOUR_OF_DAY);
		int currMinute = c.get(Calendar.MINUTE);

		if(currHour > 16 || (currHour == 16 && currMinute > 30)){
			c.add(Calendar.DATE, 1);//16.30以后默认订第二天
			sft = "1";//默认午市
		}else if(currHour>10 || (currHour==10 && currMinute > 30)){
			sft = "2";//默认晚市
		}

		dat = sf.format(c.getTime());

		map.put("dat", dat);
		map.put("sft", sft);
		return map;
	}

	/**
	 * 
	 * @param modelMap
	 * @param request
	 * @return
	 */
	@RequestMapping(value="gotoFirm")
	public ModelAndView gotoFirm(ModelMap modelMap,HttpServletRequest request){
		String pk_group = request.getParameter("pk_group");
		String firmid = request.getParameter("firmid");
		String openid = request.getParameter("openid");
		String LatLng = request.getParameter("LatLng");//百度地图坐标
		String deskState = request.getParameter("deskState");//桌位状态
		String waitTime = request.getParameter("waitTime");//预计等待时间
		String city = request.getParameter("cityName");//预计等待时间
		try{
			if(firmid==null || "".equals(firmid)){//
				return new ModelAndView("forward:/dining/homePage.do",modelMap);
			}
			Map<String,String> param = new HashMap<String,String>();
			param.put("pk_group", pk_group);
			param.put("firmid", firmid);
			param.put("openid", openid);
			List<Firm> listFirm = bookDeskMapper.getFirmList(param);

			Firm firm = new Firm();
			if(listFirm!=null && listFirm.size()>0){
				firm = listFirm.get(0);
				firm.setActmList(bookDeskMapper.getFirmActm(firm.getFirmid()));
			}
			try{
				firm.setDeskState(Integer.parseInt(deskState));
			}catch(Exception e){
				firm.setDeskState(0);
			}
			firm.setWaitTime(waitTime);

			modelMap.put("firm", firm);
			modelMap.put("pk_group", pk_group);
			modelMap.put("openid", openid);
			modelMap.put("LatLng", LatLng);
			modelMap.put("city", city);
		}catch(Exception e){
			e.printStackTrace();
		}
		return new ModelAndView(FindDiningRoomConstants.FIRMDETAIL,modelMap);
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
		return new ModelAndView(FindDiningRoomConstants.BAIDUMAP,modelMap);
	}

	@RequestMapping(value="storeUp")
	@ResponseBody
	public String storeUp(ModelMap modelMap, HttpServletRequest request){
		String openid = request.getParameter("openid");
		String pk_group = request.getParameter("pk_group");
		String firmid = request.getParameter("firmid");
		String storeupvinit = request.getParameter("storeupvinit");
		try{
			int value = Integer.parseInt(storeupvinit);
			if(value == -1){//-1时未收藏，调收藏方法
				bookDeskMapper.storeUp(firmid, openid, pk_group);
			}else if(value == 0){
				bookDeskMapper.cancelStoreUp(firmid, openid, pk_group);
			}
			return "Y";
		}catch(Exception e){
			e.printStackTrace();
			return "N";
		}
	}

	@RequestMapping(value="bookDesk")
	public ModelAndView bookDesk(ModelMap modelMap, HttpServletRequest request){
		String openid = request.getParameter("openid");
		String pk_group = request.getParameter("pk_group");
		String firmid = request.getParameter("firmid");
		try{
			modelMap.put("openid", openid);
			modelMap.put("pk_group", pk_group);
			modelMap.put("firmid", firmid);
			String vname = bookDeskMapper.getFirmName(firmid);
			modelMap.put("firmdes", vname);	
			Map<String,String> param = new HashMap<String,String>();
			param.put("pk_group", pk_group);
			param.put("firmid", firmid);
			param.put("openid", openid);
			List<Firm> listFirm = bookDeskMapper.getFirmList(param);
			if(listFirm!=null && listFirm.size()>0){
				modelMap.put("firm",  listFirm.get(0));
			}
			if(firmid==null){
				modelMap.put("msg", "请返回首页");
				return new ModelAndView(FindDiningRoomConstants.FAILEDPAGE,modelMap);
			}
			
			String tele = "";//会员电话
			String clientID = "";//会员号
			WeChatUser user = (WeChatUser) request.getSession().getAttribute("WeChatUser");
			
			/***20150625 如果Unionid不为空，则用他取值***/
			List<Card> listCard = null;
			if(StringUtils.hasText(user==null?"":user.getUnionid())){
				listCard = CardSearch.listCard(user.getUnionid());
			}else{
				listCard = CardSearch.listCard(openid);
			}
			
			if (listCard != null && listCard.size() > 0) {
				tele = listCard.get(0).getTele();
				clientID = listCard.get(0).getCardId();
			}

			modelMap.put("tele", tele);
			modelMap.put("clientID", clientID);

			//Map<String,Object> map = bookDeskMapper.getMealTime(firmid);
			List<MealTime> mealList = null;
			String isCnSft = Commons.getConfig().getProperty("isCnSft");
			if(isCnSft==null || "".equals(isCnSft)){//是否启用中餐餐次
				mealList = bookDeskMapper.getMealTimeFromView(firmid);
			}else{
				mealList = bookDeskMapper.getMealTimeFromViewCn(firmid);
			}
			
			if(mealList!=null && mealList.size()>0){
				String dat = "";//默认日期
				String sft = String.valueOf(mealList.get(0).getVcode());//默认餐次
				String datmins = "";//默认到店时间

				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				SimpleDateFormat sfTime = new SimpleDateFormat("HH:mm");
				Calendar startC = Calendar.getInstance();  
				Calendar endC = Calendar.getInstance();  

				String startTime = "";
				String endTime = "";
				
				Date previous = null;

				Map<Integer,String> allTime = new LinkedHashMap<Integer,String>();//所有时间

				int nowIndex = 1;//当前时刻在总的时刻列表中的索引
				Calendar now  =  Calendar.getInstance();
				now.setTime(new Date());
				now.set(1970, 0, 1);
				int i = 1;

				Map<String,String> seMap = new HashMap<String,String>();//餐次开始结束索引
				Map<String,String> sftMap = new LinkedHashMap<String,String>();//可选餐次
				Map<String,String> sftNameMap = new HashMap<String,String>();//餐次代码名称对应表
				StringBuilder sb = new StringBuilder();
				
				for(int m=0;m<mealList.size();m++){
					MealTime mt = mealList.get(m);
					sftNameMap.put(mt.getVcode(), mt.getVname());
					
					sb.delete(0, sb.length());
					sb.append(mt.getVname())
						.append("(")
						.append(mt.getBeginTime())
						.append(" - ")
						.append(mt.getEndTime())
						.append(")");
					
					sftMap.put(mt.getVcode(), sb.toString());
					seMap.put("Start_"+mt.getVcode(), i+"");
					
					startTime = "1970-01-01 " + mt.getBeginTime();
					endTime = "1970-01-01 " + mt.getEndTime();

					startC.setTime(sf.parse(startTime));
					endC.setTime(sf.parse(endTime));
					
					/**
					 * 预订默认:所有餐次时间不会重叠。并且根据餐次顺序，时间递增。
					 * 预订只能预订当日开始的餐次。不能预订昨日，但营业时间在今日的餐次。
					 * 例如，夜宵时间22:00-04:00，当前时间12号1:00，不能再预订昨日的夜宵。只能从今天的第一个餐次预订。
					 */
					
					//20151113 add 判断是否跨天：开始时间大于结束时间
					boolean isNextDay = false;
					if(startC.compareTo(endC)>0){
						endC.add(Calendar.DAY_OF_MONTH, 1);//日期加 一天
						isNextDay = true;
					}
					
					/** 晚餐跨天 夜宵1:00-4:00的情况 **/
					if(m > 0){
						if(startC.getTime().before(previous)){//如果本餐次开始时间小于上一餐次结束时间
							startC.add(Calendar.DAY_OF_MONTH, 1);//日期加 一天
							endC.add(Calendar.DAY_OF_MONTH, 1);//日期加 一天
						};
					}
					previous = endC.getTime();
					
					while(startC.compareTo(endC)<0){
						allTime.put(i, sfTime.format(startC.getTime()));
						
						if(isNextDay){
							if(startC.get(Calendar.DAY_OF_MONTH) == 2){
								modelMap.put("nextDayIndex", i);//第二天时间开始索引
								isNextDay = false;
							}
						}
						
						if(startC.compareTo(now)<=0){
							nowIndex = i+1;
							sft = mt.getVcode();
						}
						startC.add(Calendar.MINUTE, 30);
						i++;
					}
					seMap.put("End_"+mt.getVcode(), (i-1)+"");
					
					startC.add(Calendar.MINUTE, -30);
					if(startC.compareTo(now)<0){
						if(m==mealList.size()-1){
							sft = "";
						}else{
							try{
								sft = String.valueOf((Integer.parseInt(mt.getVcode())+1));
							}catch(Exception e){
								sft="";
								e.printStackTrace();
							}
						}
					}
					
					if(endC.compareTo(now)<=0){
						if(m==mealList.size()-1){
							sft = "";
						}else{
							try{
								sft = String.valueOf((Integer.parseInt(mt.getVcode())+1));
							}catch(Exception e){
								sft="";
								e.printStackTrace();
							}
						}
					}
				}

				datmins = allTime.get(nowIndex);

				modelMap.put("datmins", datmins);
				modelMap.put("seMap", seMap);//可选餐次开始结束索引
				modelMap.put("sftMap", sftMap);//可选餐次
				modelMap.put("sftNameMap", sftNameMap);//可选餐次代码名称对应
				modelMap.put("nowIndex", nowIndex);//当前时间所在的索引
				modelMap.put("allTime", allTime);//开始至结束所有时间点

				Map<String,String> datMap = new HashMap<String,String>();
				Map<String,String> weekMap = new HashMap<String,String>();
				sf = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat weeksf =  new SimpleDateFormat("MM-dd EEEE");
				now.setTime(new Date());
				datMap.put("today",sf.format(now.getTime()));
				weekMap.put("today", weeksf.format(now.getTime()));

				dat = datMap.get("today");

				now.add(Calendar.DAY_OF_MONTH, 1);
				datMap.put("tomorrow",sf.format(now.getTime()));
				weekMap.put("tomorrow", weeksf.format(now.getTime()));

				now.add(Calendar.DAY_OF_MONTH, 1);
				datMap.put("houtian",sf.format(now.getTime()));
				weekMap.put("houtian", weeksf.format(now.getTime()));

				modelMap.put("dat", dat);
				modelMap.put("sft", sft);
				modelMap.put("datMap", datMap);//日期列表
				modelMap.put("weekMap", weekMap);//日期星期列表

				List<StoreTable> listStoreTable = bookDeskMapper.getDeskFormFirm(pk_group, firmid, sft, dat);
				String defaultPax = "";
				String defaultRoomTyp = "";
				String defaultRoomTypID = "";
				String defaultMax_Min = "";

				int j=0;
				if(listStoreTable != null && listStoreTable.size() > 0){
					for(;j<listStoreTable.size();j++){
						StoreTable table = listStoreTable.get(j);
						if(Integer.parseInt(table.getNum())>0){
							defaultPax = table.getMinpax();
							defaultRoomTyp = table.getRoomtyp();
							defaultRoomTypID = table.getId();
							break;
						}
					}
				}
				if(j<listStoreTable.size()){
					defaultMax_Min = listStoreTable.get(j).getMinpax() + "-" + listStoreTable.get(j).getMaxpax();
				}
				modelMap.put("defaultMax_Min", defaultMax_Min);//默认最大人数最小人数显示信息
				modelMap.put("defaultRoomTyp", defaultRoomTyp);//默认桌台
				modelMap.put("defaultRoomTypID", defaultRoomTypID);//默认桌台
				modelMap.put("defaultPax", defaultPax);//默认桌台人数
				modelMap.put("listStoreTable", listStoreTable);
			}

			return new ModelAndView(FindDiningRoomConstants.BOOKDESK,modelMap);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value="getDeskFormFirm")
	@ResponseBody
	public List<StoreTable> getDeskFormFirm(ModelMap modelMap, HttpServletRequest request){
		String pk_group = request.getParameter("pk_group");
		String firmid = request.getParameter("firmid");
		String dat = request.getParameter("dat");
		String sft = request.getParameter("sft");
		try{
			List<StoreTable> listStoreTable = bookDeskMapper.getDeskFormFirm(pk_group, firmid, sft, dat);
			return listStoreTable;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
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
				modelMap.put("msg", "请返回首页进行操作");
				return new ModelAndView(FindDiningRoomConstants.FAILEDPAGE,modelMap);
			}

			/*
			Calendar now = Calendar.getInstance(); 
			now.setTime(new Date());
			 */

			/* 
			//2015-06-25 去除不是会员就自动注册为会员功能
			//插入会员
			if(clientID==null || "".equals(clientID)){
				List<Card> phoneCard = CardSearch.listWXCard(orders.getContact());//通过手机号查询会员
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
			*/
			
			/***预订限制 20151022***/
			String limitType = Commons.getConfig().getProperty("limitType");//限制类型
			if(StringUtils.hasText(limitType)){
				String limitNum = Commons.getConfig().getProperty("limitNum");//限制单数
				
				int limitOrdersNum = 0;
				try{
					limitOrdersNum = Integer.parseInt(StringUtils.hasText(limitNum)?limitNum:"0");
				}catch(Exception e){
					log.error("限制单数格式错误："+limitNum);
				}
				
				if(limitOrdersNum > 0){
					Map<String,String> paramMap = new HashMap<String,String>();
					paramMap.put("openid", openid);
					paramMap.put("dat", orders.getDat());
					paramMap.put("state", "2");
					paramMap.put("isfeast","1");//预订单
					
					int bookNum = 0;//已订数量
					String errorMsg = "";
					if("0".equals(limitType)){//限制一天内可预订几单
						bookNum = bookDeskMapper.getCountOrdersWithOpenid(paramMap);
						errorMsg = "在预订日期您还有未完成的订单，请先完成或取消订单后再进行预订";
					}else if("1".equals(limitType)){//限制一个班次可预订几单
						paramMap.put("sft", orders.getSft());
						bookNum = bookDeskMapper.getCountOrdersWithOpenid(paramMap);
						errorMsg = "在预订日期餐次您还有未完成的订单，请先完成或取消订单后再进行预订";
					}
					
					if(bookNum>=limitOrdersNum){//已订数量大于限制数量
						modelMap.put("msg", errorMsg);
						modelMap.put("sft", orders.getSft());
						modelMap.put("openid", orders.getOpenid());
						modelMap.put("firmid", orders.getFirmid());
						modelMap.put("dat",orders.getDat());
						return new ModelAndView(FindDiningRoomConstants.FAILEDPAGE,modelMap);
					}
				}
			}
			/***end***/
			
			List<StoreTable> listDesks = bookDeskMapper.findResvTbl(roomtyp, orders.getSft(), orders.getDat(), orders.getFirmid(), realPax);
			if(listDesks.size()==0){
				modelMap.put("msg", "抱歉，您预定的桌台已被抢先预定，请选择其他桌台！");
				modelMap.put("sft", orders.getSft());
				modelMap.put("openid", orders.getOpenid());
				modelMap.put("firmid", orders.getFirmid());
				modelMap.put("dat",orders.getDat());
				return new ModelAndView(FindDiningRoomConstants.FAILEDPAGE,modelMap);
			}
			//随机抽取一个桌台
			//Integer custIndex = new Random().nextInt(listDesks.size());
			//StoreTable table = listDesks.get(custIndex);
			/** 改为优先取最大人数最小的台位 **/
			StoreTable table = listDesks.get(0);
			orders.setTables(table.getTbl());
			deskTime.setResvtblid(table.getId());
			deskTime.setVcode(table.getTbl());//总部修改桌台，主键会改变。修改为保存台位号 20150910

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
				bookDeskService.saveOrder(orders,deskTime,clientID==null?"":clientID);
				
				modelMap.put("orderid", id);
				modelMap.put("openid", orders.getOpenid());
				modelMap.put("firmid", orders.getFirmid());
				modelMap.put("pk_group", orders.getPk_group());

				String bookMeal = request.getParameter("bookMeal");
				if(bookMeal==null || !"1".equals(bookMeal)){
					/*
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
						.append("/bookDesk/orderDetail.do?orderid=").append(orders.getId())
						.append("&openid=").append(orders.getOpenid())
						.append("&firmid=").append(orders.getFirmid())
						.append("&pk_group=").append(orders.getPk_group())
						.append("&orderTyp=1");
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
					sb.delete(0, sb.length());
					sb.append("预订/")
					.append(orders.getPax())
					.append("人/")
					.append(orders.getDat())
					.append("/");
					String sft = orders.getSft();
					if("1".equals(sft)){
						sb.append("午市");
					}else if("2".equals(sft)){
						sb.append("晚市");
					}else if("3".equals(sft)){
						sb.append("夜宵");
					}
					data.setValue(sb.toString());
					map.put("orderType", data);

					data = new MsgData();
					
					Map<String,String> param = new HashMap<String,String>();
					param.put("firmid", orders.getFirmid());
					List<Firm> listFirm = bookDeskMapper.getFirmList(param);

					Firm firm = new Firm();
					if(listFirm!=null && listFirm.size()>0){
						firm = listFirm.get(0);
					}
					
					sb.delete(0, sb.length());
					sb.append("商店电话：").append(firm.getTele()==null?"":firm.getTele());
					sb.append("\n商店地址：").append(firm.getAddr()==null?"":firm.getAddr());
					sb.append("\n感谢您的光临，请在预订时间前到达本店");
					data.setValue(sb.toString());
					map.put("remark", data);

					msg.setData(map);
					TemplateMsgUtil.sendTmplateMsg(token.getToken(), msg);
					*/
				}else{
					return new ModelAndView("forward:/bookMeal/gotoMenu.do?type=bookDesk&bookDeskOrderID="+id,modelMap);
				}
			}catch(RuntimeException e){
				e.printStackTrace();
				modelMap.put("msg", "抱歉，您预定的桌台已被抢先预定，请选择其他桌台！");
				modelMap.put("sft", orders.getSft());
				modelMap.put("openid", orders.getOpenid());
				modelMap.put("firmid", orders.getFirmid());
				modelMap.put("dat",orders.getDat());
				return new ModelAndView(FindDiningRoomConstants.FAILEDPAGE,modelMap);
			}
			
			StringBuilder sb = new StringBuilder("redirect:/bookDesk/orderDetail.do?openid=");
			sb.append(openid)
				.append("&orderid=").append(orders.getId())
				.append("&firmid=").append(orders.getFirmid())
				.append("&pk_group=").append(orders.getPk_group())
				.append("&orderType=1");
			return new ModelAndView(sb.toString());
			//return new ModelAndView(FindDiningRoomConstants.SUCCESSPAGE,modelMap);
		}catch(Exception e){
			e.printStackTrace();
		}
		return new ModelAndView(FindDiningRoomConstants.FAILEDPAGE,modelMap);
	}
	
	/**
	 * 
	 * @param modelMap
	 * @param request
	 * @return
	 */
	@RequestMapping(value="updateLocation")
	@ResponseBody
	public JSONObject updateLocation(ModelMap modelMap, HttpServletRequest request){
		JSONObject result = null;
		try{
			String pk_group = request.getParameter("pk_group");
			String lat = request.getParameter("lat"); 
			String lng = request.getParameter("lng"); 
			String coord_type = request.getParameter("coord_type"); //坐标类型
			
			request.getSession().setAttribute("lat", lat);
			request.getSession().setAttribute("lng", lng);

			String LatLng = lat+","+lng;
			if(coord_type==null || "".equals(coord_type)){//如果为空，默认不是百度坐标
				try{
					LatLng = BaiduMapApi.geoconv(lat, lng);//转换坐标
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			result = new JSONObject();
			result.put("LatLng", LatLng);

			//城市
			List<City> listCity = bookDeskMapper.getCityList(pk_group, null);
			City userCity = (City)listCity.get(0);//默认为第一个
			Map<String,String> map = BaiduMapApi.getLocation(lat, lng);
			String cityName = map.get("city");
			String address = map.get("address");
			result.put("address", address);
			result.put("cityName", cityName);

			for(City city:listCity){//找到用户基本信息匹配的城市
				if(cityName.contains(city.getVname())){
					userCity = city;
				}
			}
			result.put("pk_city", userCity.getPk_city());
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping(value="pushToPOS")
	@ResponseBody
	public String pushToPOS(ModelMap modelMap, HttpServletRequest request) {
		try{
			String orderid = request.getParameter("orderid");
			
			Firm firm = null;
			Net_Orders orders = null;
			
			// 推送订位单
			Net_Orders deskOrder = new Net_Orders();
			String deskOrderid = request.getParameter("deskOrderid");
			if(deskOrderid==null || "".equals(deskOrderid)){
				return "";
			}
			
			Map<String, String> queryDeskMap = new HashMap<String, String>();
			queryDeskMap.put("id", deskOrderid);
			List<Net_Orders> deskOrders = bookMealMapper.getOrderMenus(queryDeskMap);
			if (deskOrders != null && !deskOrders.isEmpty()) {
				deskOrder = deskOrders.get(0);
			}
			
			if(deskOrder.getState()==null || !"0".equals(deskOrder.getState())){
				return "";
			}
			
			//更新状态为1
			orders = new Net_Orders();
			orders.setId(deskOrderid);
			orders.setState("1");
			bookDeskMapper.updateOrdr(orders);
			
			if(firm == null){
				//查询门店
				List<Firm> listFirm = bookMealMapper.getFirmList(null, null, deskOrder.getFirmid());

				if (listFirm != null && !listFirm.isEmpty()) {
					firm =  listFirm.get(0);
					deskOrder.setVcode(firm.getFirmCode());
				}else{
					return "不存在此门店";
				}
			}
			
			deskOrder.setSerialid(CodeHelper.createUUID());
			deskOrder.setType("10");
			/*
			//pos和预制数据，和net_orders定义不同，加一符合
			String sft = deskOrder.getSft();
			try{
				deskOrder.setSft(String.valueOf(Integer.parseInt(sft)+1));
			}catch(Exception e){}
			*/
			
			Map<String,String> sftNameMap = null;
			String isCnSft = Commons.getConfig().getProperty("isCnSft");
			if(isCnSft==null || "".equals(isCnSft)){//是否启用中餐餐次
				sftNameMap = bookDeskMapper.getShiftSft();
			}else{
				sftNameMap = bookDeskMapper.getFirmSft(deskOrder.getFirmid());
			}
			
			// 推送点菜单
			orders = BuilderOrderMessage.builderOrderMessage(takeOutMapper, bookMealMapper, orderid, "11");
			if(orders != null){
				//查询门店
				List<Firm> listFirm = bookMealMapper.getFirmList(null, null, orders.getFirmid());

				if (listFirm != null && !listFirm.isEmpty()) {
					firm =  listFirm.get(0);
					orders.setVcode(firm.getFirmCode());
				}else{
					return "不存在此门店";
				}

				deskOrder.setListNetOrderDtl(orders.getListNetOrderDtl());
				/*
				json = JSONObject.fromObject(orders);
				LogUtil.writeToTxt(LogUtil.MQSENDORDER, "下单到MQ服务器中，队列名："+firm.getFirmCode()+"_orderList，订单对象："+json.toString());
				MqSender.sendOrders(json.toString(),firm.getFirmCode()+"_orderList",Commons.mqEffectiveTime);//设置消息有效期为半小时
				mqMap = new HashMap<String,Object>();
				mqMap.put("pk_mqlogs", orders.getSerialid());
				mqMap.put("orderid", orders.getId());
				mqMap.put("vtype", "1");
				mqMap.put("errmsg", "");
				mqMap.put("state", "");
				bookDeskMapper.addMqLogs(mqMap);
				*/
			}

			//记录本次记录
			Map<String,Object> mqMap = new HashMap<String,Object>();
			mqMap.put("pk_mqlogs", deskOrder.getSerialid());
			mqMap.put("orderid", deskOrder.getId());
			mqMap.put("vtype", "1");
			mqMap.put("errmsg", "");
			mqMap.put("state", "");
			bookDeskMapper.addMqLogs(mqMap);
			//发送mq消息
			deskOrder.setSerialid(mqMap.get("pk_mqlogs")+"");//重置mq记录的主键
			JSONObject json = JSONObject.fromObject(deskOrder);
			LogUtil.writeToTxt(LogUtil.MQSENDORDER, "推送订位单到MQ服务器中，队列名：" + firm.getFirmCode() + "_orderList，订单对象：" + json.toString());
			MqSender.sendOrders(json.toString(), firm.getFirmCode() + "_orderList",0);
			
			//根据pk_group，获取配置信息
			Company company = WeChatUtil.getCompanyInfo("");
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
			msg.setTouser(deskOrder.getOpenid());
			msg.setTemplate_id(tempid);
			//msg.setTopcolor("#000000");

			StringBuffer sb = request.getRequestURL();
			String baseUrl = sb.substring(0, sb.lastIndexOf("/"));
			baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf("/"));
			sb.delete(0, sb.length());
			sb.append(baseUrl)
				.append("/bookDesk/orderDetail.do?orderid=").append(deskOrder.getId())
				.append("&openid=").append(deskOrder.getOpenid())
				.append("&firmid=").append(deskOrder.getFirmid())
				.append("&pk_group=").append(deskOrder.getPk_group())
				.append("&orderTyp=1");
			msg.setUrl(sb.toString());
			Map<String,MsgData> map = new HashMap<String,MsgData>();
			MsgData data = new MsgData();
			data.setValue("您的订单已受理成功");
			map.put("first", data);

			data = new MsgData();
			data.setValue(deskOrder.getFirmdes());
			map.put("storeName", data);

			data = new MsgData();
			data.setValue(deskOrder.getResv());
			map.put("orderId", data);

			data = new MsgData();
			sb.delete(0, sb.length());
			sb.append("预订/")
			.append(deskOrder.getPax())
			.append("人/")
			.append((deskOrder.getArrtime()==null||"".equals(deskOrder.getArrtime()))?deskOrder.getDat():deskOrder.getArrtime())
			.append("/")
			.append(sftNameMap.get(deskOrder.getSft()));
			data.setValue(sb.toString());
			map.put("orderType", data);

			data = new MsgData();
			sb.delete(0, sb.length());
			sb.append("商店电话：").append(deskOrder.getTele()==null?"":deskOrder.getTele());
			sb.append("\n商店地址：").append(deskOrder.getAddr()==null?"":deskOrder.getAddr());
			sb.append("\n感谢您的光临，请在预订时间前到达本店");
			data.setValue(sb.toString());
			map.put("remark", data);

			msg.setData(map);
			TemplateMsgUtil.sendTmplateMsg(token.getToken(), msg);
			return "1";
		} catch (Exception e) {
			Net_Orders orders = new Net_Orders();
			String orderid = request.getParameter("orderid");
			orders.setId(orderid);
			orders.setState("0");
			bookDeskMapper.updateOrdr(orders);
			
			String deskOrderid = request.getParameter("deskOrderid");
			orders.setId(deskOrderid);
			bookDeskMapper.updateOrdr(orders);
			e.printStackTrace();
		}
		
		return "";
	}
	
	/**
	 * 查询预订限制
	 * @param request
	 * @return
	 */
	@RequestMapping(value="queryLimit")
	@ResponseBody
	public String queryLimit(HttpServletRequest request){
		String openid = request.getParameter("openid");
		String dat = request.getParameter("dat");
		String sft = request.getParameter("sft");
		
		String limitType = Commons.getConfig().getProperty("limitType");//限制类型
		if(StringUtils.hasText(limitType)){
			String limitNum = Commons.getConfig().getProperty("limitNum");//限制单数
			
			int limitOrdersNum = 0;
			try{
				limitOrdersNum = Integer.parseInt(StringUtils.hasText(limitNum)?limitNum:"0");
			}catch(Exception e){
				log.error("限制单数格式错误："+limitNum);
			}
			
			if(limitOrdersNum > 0){
				Map<String,String> paramMap = new HashMap<String,String>();
				paramMap.put("openid", openid);
				paramMap.put("dat", dat);
				paramMap.put("state", "2");
				paramMap.put("isfeast","1");//预订单
				
				int bookNum = 0;//已订数量
				String errorMsg = "Y";
				try{
					if("0".equals(limitType)){//限制一天内可预订几单
						bookNum = bookDeskMapper.getCountOrdersWithOpenid(paramMap);
						errorMsg = "N0";
					}else if("1".equals(limitType)){//限制一个班次可预订几单
						paramMap.put("sft", sft);
						bookNum = bookDeskMapper.getCountOrdersWithOpenid(paramMap);
						errorMsg = "N1";
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				
				if(bookNum>=limitOrdersNum){//已订数量大于限制数量
					return errorMsg;
				}
			}
		}
		return "Y";
	}
}
