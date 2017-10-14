package com.choice.wechat.web.controller.bookMeal;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

import com.choice.test.domain.Card;
import com.choice.test.domain.ItemPrgPackage;
import com.choice.test.domain.ItemPrgpackAgedtl;
import com.choice.test.domain.Net_OrderDtl;
import com.choice.test.domain.Net_OrderPackageDetail;
import com.choice.test.domain.Net_Orders;
import com.choice.test.domain.Voucher;
import com.choice.test.persistence.WeChatOrderMapper;
import com.choice.test.service.CardQrCode;
import com.choice.test.service.CardSearch;
import com.choice.test.service.PubitemSearch;
import com.choice.test.service.impl.WechatOrderService;
import com.choice.test.utils.CodeHelper;
import com.choice.test.utils.Commons;
import com.choice.test.utils.DateFormat;
import com.choice.test.utils.GenerateRandom;
import com.choice.webService.CRMWebservice;
import com.choice.wechat.common.AccessToken;
import com.choice.wechat.common.WeChatUser;
import com.choice.wechat.constants.bookDesk.BookDeskConstants;
import com.choice.wechat.constants.bookDesk.FindDiningRoomConstants;
import com.choice.wechat.constants.bookMeal.BookMealConstants;
import com.choice.wechat.domain.bookDesk.City;
import com.choice.wechat.domain.bookDesk.Company;
import com.choice.wechat.domain.bookDesk.Firm;
import com.choice.wechat.domain.bookMeal.FdItemSale;
import com.choice.wechat.domain.bookMeal.FdItemType;
import com.choice.wechat.domain.bookMeal.NetDishAddItem;
import com.choice.wechat.domain.bookMeal.NetDishProdAdd;
import com.choice.wechat.domain.bookMeal.ProdReqAdd;
import com.choice.wechat.domain.bookMeal.ProductAdditional;
import com.choice.wechat.domain.bookMeal.ProductRedfine;
import com.choice.wechat.domain.bookMeal.ProductReqAttAc;
import com.choice.wechat.domain.bookMeal.SellOff;
import com.choice.wechat.domain.game.Actm;
import com.choice.wechat.domain.takeout.StoreRange;
import com.choice.wechat.persistence.bookDesk.BookDeskMapper;
import com.choice.wechat.persistence.bookMeal.BookMealMapper;
import com.choice.wechat.persistence.myCard.MyCardMapper;
import com.choice.wechat.persistence.takeout.TakeOutMapper;
import com.choice.wechat.service.campaign.CampaignTools;
import com.choice.wechat.util.LogUtil;
import com.choice.wechat.util.MapBeanConvertUtil;
import com.choice.wechat.util.MessageUtil;
import com.choice.wechat.util.Sign;
import com.choice.wechat.util.TemplateMsgUtil;
import com.choice.wechat.util.ValueCheck;
import com.choice.wechat.util.WeChatUtil;
import com.choice.wechat.util.mq.MqSender;
import com.wxap.util.PayUtil;

/**
 * 点餐
 */
@Controller
@RequestMapping(value = "bookMeal")
public class BookMealController {

	@Autowired
	private BookMealMapper bookMealMapper;
	
	@Autowired
	private TakeOutMapper takeOutMapper;
	
	@Autowired
	private MyCardMapper myCardMapper;
	
	@Autowired
	private BookDeskMapper bookDeskMapper;
	static WeChatOrderMapper orderMapper = new WechatOrderService();
	private static CRMWebservice crmService;
		static{	//初始化接口调用
			JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();       
		    factoryBean.setServiceClass(CRMWebservice.class);  
		  //获取配置文件中的CTF访问路径
		    factoryBean.setAddress(Commons.CRMwebService); 
			crmService = (CRMWebservice)factoryBean.create();
		}
	/**
	 * 点餐基本信息
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "listFirm")
	public ModelAndView listFirm(ModelMap modelMap, String code, String state,
			String pk_group) {
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

			// 通过网页授权方式获取openID
			String oauth2[] = WeChatUtil.getOAuth2(appId, secret, code);

			// 城市
			List<City> listCity = bookMealMapper.getCityList(pk_group, null);

			City userCity = (City) listCity.get(0);// 默认为第一个

			// 获取用户基本信息
			AccessToken token = WeChatUtil.getAccessToken(appId, secret);
			WeChatUser user = WeChatUtil.getWeChatUser(oauth2[0],
					token.getToken());

			for (City city : listCity) {// 找到用户基本信息匹配的城市
				if (city.getVname().equals(user.getCity())) {
					userCity = city;
				}
			}

			// 根据城市查询店铺
			List<Firm> listFirm = choiceFirm(pk_group, userCity.getPk_city());

			modelMap.put("listCity", listCity);// 城市列表
			modelMap.put("userCity", userCity);// 用户城市
			modelMap.put("listFirm", listFirm);// 门店列表
			modelMap.put("openid", user.getOpenid());
			modelMap.put("pk_group", pk_group);
			modelMap.put("code", code);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(BookMealConstants.LISTFIRM, modelMap);
	}

	/**
	 * 切换城市选择门店
	 * @param modelMap
	 * @param pk_group
	 * @param pk_city
	 * @param openid
	 * @return
	 */
	@RequestMapping(value = "choiceFirm")
	@ResponseBody
	public List<Firm> choiceFirm(ModelMap modelMap, String pk_group, String pk_city, String openid) {
		List<Firm> listFirm = null;
		try {
			listFirm = choiceFirm(pk_group, pk_city);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listFirm;
	}

	/**
	 * 根据企业编号、城市编号获取门店列表
	 * @param pk_group
	 * @param pk_city
	 * @param openid
	 * @return
	 */
	public List<Firm> choiceFirm(String pk_group, String pk_city) {
		try {
			// 门店列表
			List<Firm> listFirm = bookMealMapper.getFirmList(pk_group, pk_city,	null);

			return listFirm;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 进入菜单页面
	 * 
	 * @param modelMap
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "gotoMenu")
	public ModelAndView gotoMenu(ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response, String code, String state, String openid, String firmid, String tables) {
		try {
			List<FdItemSale> listItemSale = new ArrayList<FdItemSale>();
			Map<String,Object> mapItemSale = new HashMap<String,Object>();
			int tcseq = 0,tcPubitemSeq=0,tcDtlSeq=1;
			Map<String,String> orderPackageIdTemp= new HashMap<String,String>();//套餐主键
			Map<String,String> orderPackageDtlIdTemp=new HashMap<String,String>();//套餐菜品明细主键
			Map<String,List<NetDishAddItem>> listProductRedfineMust = new HashMap<String,List<NetDishAddItem>>();//必选附加项
			List<NetDishAddItem> listProductRedfineCanselect = new ArrayList<NetDishAddItem>();//可选附加项
			Map<String,List<NetDishProdAdd>> listNetDishProdAddMust = new HashMap<String,List<NetDishProdAdd>>();//必选附加产品
			List<NetDishProdAdd> listNetDishProdAddCanselect = new ArrayList<NetDishProdAdd>();//可选附加产品
			// 如果通过扫码点餐，判断链接的时效性，超过配置时间后，需重新扫码
			String createTime = request.getParameter("createTime");
			if(null != createTime && !createTime.isEmpty()) {
				long create = Long.parseLong(createTime);
				long now = System.currentTimeMillis();
				String configTime = Commons.getConfig().getProperty("scanExpireTime");
				if(null == configTime || configTime.isEmpty()) {
					configTime = "5";
				}
				// 如果扫码已超过配置时间，返回错误信息
				if(now - create > Long.parseLong(configTime) * 60 * 1000) {
					return new ModelAndView(BookMealConstants.SCANEXPIREPAGE);
				}
			}
			String pk_group = request.getParameter("pk_group");
			if(null == firmid || "".equals(firmid)) {
				firmid = request.getParameter("firmid");
			}
			if(null == openid || "".equals(openid)) {
				openid = request.getParameter("openid");
			}
			
			if(null == openid || "".equals(openid)) {
				String appId = Commons.appId;
				String secret = Commons.secret;
				if(null == code || "".equals(code)) {
					code = request.getParameter("code");
				}
				// 通过网页授权方式获取openID
				String oauth2[] = WeChatUtil.getOAuth2(appId, secret, code);

				// 获取用户基本信息
				AccessToken token = WeChatUtil.getAccessToken(appId, secret);
				WeChatUser user = WeChatUtil.getWeChatUser(oauth2[0],
						token.getToken());
				openid = user.getOpenid();
			}
			//判断是否是会员
			String isVip = myCardMapper.isVipofOpenid(openid);
			modelMap.put("isVip", isVip);
			//判断是否已限制每天订单数，如果已限制并超过限制数量，跳转到我的点菜单
			String limitType = Commons.getConfig().getProperty("limitType");
			String limitNum = Commons.getConfig().getProperty("limitNum");
			if(StringUtils.hasText(limitType)) {
				//查询已点点菜订单数
				Net_Orders order = new Net_Orders();
				order.setFirmid(firmid);
				order.setOpenid(openid);
				order.setDat(DateFormat.getStringByDate(new Date(), "yyyy-MM-dd"));
				order.setIsfeast("0");
				order.setState("2");
				
				int cnt = bookMealMapper.getOrderCount(order);
				if(cnt >= Integer.parseInt(StringUtils.hasText(limitNum) ? limitNum : "0")) {
					String errorMsg = "您还有未完成的点餐订单，请先完成或取消订单后再进行点餐";
					modelMap.put("msg", errorMsg);
					modelMap.put("openid", order.getOpenid());
					modelMap.put("firmid", order.getFirmid());
					modelMap.put("dat",order.getDat());
					return new ModelAndView(FindDiningRoomConstants.FAILEDPAGE,modelMap);
				}
			}
			//订单类型 takeout 外卖
			String type = request.getParameter("type");
			String pageFrom = request.getParameter("pageFrom");
			
			String isTake = type;//是否是外卖
			if(pageFrom!=null && "takeout".equals(pageFrom)){
				isTake = pageFrom;//外卖单详情里修改菜品、加菜时，type!=takeout;但是pageFrom=takeout
			}
			if("takeout".equals(isTake)) {
				// 读取优惠活动
				List<Actm> listActm = CampaignTools.getListTakeOutActm(firmid);
				modelMap.put("listActm", listActm);
				
				// 获取起送价
				List<StoreRange> listStoreRange = takeOutMapper.getListStoreRange(pk_group, firmid);
				if(null != listStoreRange && !listStoreRange.isEmpty()) {
					double startPrice = listStoreRange.get(0).getNstartprice();
					modelMap.put("startPrice", startPrice);
				}
			}

//			int count = bookMealMapper.hasPackage(pk_group, firmid);
			
			// 沽清列表
			List<SellOff> sellOffList = new ArrayList<SellOff>();

			// 门店列表
			List<Firm> listFirm = bookMealMapper.getFirmList(pk_group, null,
					firmid);
			if (listFirm != null && !listFirm.isEmpty()) {
				Firm firm = listFirm.get(0);
				modelMap.put("firm", firm);
				
				// 查询菜品展示类别
				List<FdItemType> listItemType = bookMealMapper.getFdItemTypeList(
						pk_group, firmid, tables, firm.getVareaOrder());
				
				// 查询沽清菜品
				SellOff data = new SellOff();
				data.setPk_group(pk_group);
				data.setPk_store(firm.getFirmCode());
				sellOffList = bookMealMapper.getSellOffList(data);
				//用于判断套餐明细的沽清状态
				Map<String,String> sellOffMap = new HashMap<String, String>();
				for(SellOff info : sellOffList) {
					sellOffMap.put(info.getPk_pubItem(),"Y");
				}
				// 获取所有菜品列表
				List<FdItemSale> tempListItemSale = bookMealMapper.getFdItemSaleList(
						pk_group, firmid, null, null, null, null, isTake);
				//查询套餐
				List<Map<String,Object>> listPackage = bookMealMapper.selectPackage(pk_group, firmid, isTake);
				if (listPackage != null && listPackage.size() > 0) {// 判断是否有套餐
//					modelMap.put("count", "套餐");
					listPackage = WeChatUtil.formatNumForListResult(listPackage,"price",2);
					// 判断是否有套餐,将套餐加到菜品列表中
					for(Map<String,Object> packageMap : listPackage){
						FdItemSale fdItemSale  = (FdItemSale) MapBeanConvertUtil.convertMap(FdItemSale.class,packageMap);
						fdItemSale.setPkgtag("1");
						fdItemSale.setHasSellOff(sellOffMap.get(fdItemSale.getItcode()));//设置沽清状态
						tempListItemSale.add(fdItemSale);
					}
//					modelMap.put("listPackage", JSONArray.fromObject(WeChatUtil.formatNumForListResult(listPackage,"price",2)));//套餐列表
					modelMap.put("packageDtl", bookMealMapper.selectPackageDtl(pk_group, firmid,sellOffMap));//套餐明细
				} else {
					modelMap.put("count", "");
				}
				
				// 获取门店个性菜品列表
				List<FdItemSale> storeListItemSale = bookMealMapper.getStoreFdItemSaleList(pk_group, firmid, isTake);
				
				List<FdItemSale> usedListItemSale = new ArrayList<FdItemSale>();
				for(FdItemSale temp : tempListItemSale) {
					// 集团菜谱的菜品在门店个性菜谱中是否存在
					boolean existInStore = false;
					for(FdItemSale store : storeListItemSale) {
						if(store.getId().equals(temp.getId())) {
							existInStore = true;
						}
					}
					
					// 如果集团菜谱的菜品在门店个性菜谱中存在，不使用集团菜谱
					if(!existInStore) {
						usedListItemSale.add(temp);
					}
				}
				// 加入所有可用门店个性菜谱
				for(FdItemSale item : storeListItemSale) {
					if("2".equals(item.getEnablestate())) {
						usedListItemSale.add(item);
					}
				}
				
				// 根据优先级过滤全新方案和附加方案中重复的菜品
				listItemSale = clearDuplicateItem(usedListItemSale);
				
				// 过滤沽清菜品
				filterSellOffDish(modelMap, pk_group, firm.getFirmCode(), listItemSale);
				
				// 按照类别整理菜品列表
				clearItemSaleByType(modelMap, listItemSale, listItemType);
				//将list转为map，便于匹配不用每次有循环list
				for(FdItemSale fdItemSale : listItemSale){
					mapItemSale.put(fdItemSale.getId(), fdItemSale);
				}
			}

			modelMap.put("openid", openid);
			modelMap.put("pk_group", pk_group);
			
			// 获取必选附加项明细列表
			List<ProductRedfine> productRedfineList = bookMealMapper.getProductRedfineList(pk_group);
			
			// 按照必选附加项主键整理数据，一个必选附加项对应一个列表
			Map<String, List<ProductRedfine>> productRedfineMap = new HashMap<String, List<ProductRedfine>>();
			for(ProductRedfine data : productRedfineList) {
				// 菜品主键和必选附加项主键作为key
				String key = data.getPk_PubItem() + "_" +data.getPk_ProdcutReqAttAc();
				if(productRedfineMap.containsKey(key)) {
					// map中已包含key，直接增加数据
					productRedfineMap.get(key).add(data);
				} else {
					// map中未包含key，生成list加入map
					List<ProductRedfine> list = new ArrayList<ProductRedfine>();
					list.add(data);
					productRedfineMap.put(key, list);
				}
			}
			
			// 获取必选附加项列表
			List<ProductReqAttAc> productReqAttAcList = bookMealMapper.getProductReqAttAcList(pk_group);
			
			// 按照菜品ID整理必选附加项
			Map<String, List<ProductReqAttAc>> menuReqAttAcMap = new HashMap<String, List<ProductReqAttAc>>();
			for(ProductReqAttAc data : productReqAttAcList) {
				String key = data.getPk_PubItem();
				String subkey = data.getPk_PubItem() + "_" + data.getPk_ProdcutReqAttAc();
				data.setProductRedfineList(productRedfineMap.get(subkey));
				if(menuReqAttAcMap.containsKey(key)) {
					menuReqAttAcMap.get(key).add(data);
				} else {
					List<ProductReqAttAc> list = new ArrayList<ProductReqAttAc>();
					list.add(data);
					menuReqAttAcMap.put(key, list);
				}
			}
			
			modelMap.put("menuReqAttAcMap", menuReqAttAcMap);
			
			// 整理附加产品
			List<ProdReqAdd> listProdReqAdd = bookMealMapper.getListProdReqAdd(pk_group);
			List<ProductAdditional> tempListProductAdditional = bookMealMapper.getListProductAdditional(pk_group, firmid);
			
			// 根据优先级过滤全新方案和附加方案中重复的附加产品
			List<ProductAdditional> listProductAdditional = this.clearDuplicateProdItem(tempListProductAdditional);
			
			// 过滤掉沽清的附加产品
			if(null != listProductAdditional && !listProductAdditional.isEmpty() && null != sellOffList && !sellOffList.isEmpty()) {
				for(int i = listProductAdditional.size() - 1; i >= 0; i--) {
					ProductAdditional temp = listProductAdditional.get(i);
					for(SellOff info : sellOffList) {
						if(temp.getVcode().equals(info.getPk_pubItem())) {
							listProductAdditional.remove(i);
							break;
						}
					}
				}
			}
			
			// 按照必选附加产品表主键整理数据，每个主键对应一个Map
			Map<String, List<ProductAdditional>> prodAdditionalMap = new HashMap<String, List<ProductAdditional>>();
			for(ProductAdditional data : listProductAdditional) {
				String key = data.getPk_pubitem() + "_" + data.getPk_addPubitem();
				if(prodAdditionalMap.containsKey(key)) {
					prodAdditionalMap.get(key).add(data);
				} else {
					List<ProductAdditional> list = new ArrayList<ProductAdditional>();
					list.add(data);
					prodAdditionalMap.put(key, list);
				}
			}
			// 根据菜品整理菜品必选附加产品
			Map<String, List<ProdReqAdd>> prodReqAddMap = new HashMap<String, List<ProdReqAdd>>();
			for(ProdReqAdd data : listProdReqAdd) {
				String key = data.getPk_pubitem();
				String subKey = data.getPk_pubitem() + "_" + data.getPk_prodReqAdd();
				data.setListProductAdditional(prodAdditionalMap.get(subKey));
				if(prodReqAddMap.containsKey(key)) {
					prodReqAddMap.get(key).add(data);
				} else {
					List<ProdReqAdd> list = new ArrayList<ProdReqAdd>();
					list.add(data);
					prodReqAddMap.put(key, list);
				}
			}
			modelMap.put("prodReqAddMap", prodReqAddMap);
			
			// 获取可选附加项列表
			List<ProductRedfine> redefineList = bookMealMapper.getProductRedfineByProgList(pk_group, firmid);
			modelMap.put("redefineList", redefineList);
			modelMap.put("redefineListSize", redefineList.size());

			// 微信支付
			TreeMap<String, String> goodsInfo = new TreeMap<String, String>();
			goodsInfo.put("body", "assss");
			String packegeValue = PayUtil.packageValueInfo(request, response,
					goodsInfo);
			String sign = PayUtil.getSign();
			String timestamp = PayUtil.getTimestamp();
			String nonce = PayUtil.getNonceStr();
			modelMap.put("APPID", Commons.appId);
			modelMap.put("PACKEGEVALUE", packegeValue);
			modelMap.put("SIGN", sign);
			modelMap.put("TIMESTAMP", timestamp);
			modelMap.put("NONCE", nonce);
			modelMap.put("SIGNTYPE", "SHA1");
			
			modelMap.put("code", request.getParameter("code"));
			
			// 通过订单详情修改菜品进入时，查询订单信息；加菜时不设置订单明细
			String orderid = request.getParameter("orderid");
			
			if(!StringUtils.hasText(orderid) && StringUtils.hasText(tables)) {
				// 如果通过点击图文消息进入菜单列表，根据台位、日期、openid查询是否有未结帐单，如果有，按加菜处理
				String dat = DateFormat.getStringByDate(new Date(), "yyyy-MM-dd");
				Map<String, String> tempMap = new HashMap<String, String>();
				tempMap.put("state1", "6");
				tempMap.put("state2", "7");
				tempMap.put("dat", dat);
				tempMap.put("openid", openid);
				tempMap.put("tables", tables);
				List<Net_Orders> tempOrderList = bookMealMapper.getOrderMenus(tempMap);
				if(null != tempOrderList && !tempOrderList.isEmpty()) {
					orderid = tempOrderList.get(0).getId();
					type = "add";
				}
			}
			
			if(null != orderid && !"".equals(orderid)) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", orderid);
				List<Net_Orders> listNet_Orders = bookMealMapper.getOrderMenus(map);
				if (listNet_Orders != null && !listNet_Orders.isEmpty()) {
					Net_Orders order = listNet_Orders.get(0);
					if("modify".equals(type)) {
						List<Net_OrderDtl> orderDtl = PubitemSearch.getOrderDtlMenus(orderid);
						
						// 获取开台菜品列表
						Net_Orders tempOrder = new Net_Orders();
						List<Net_OrderDtl> tempListDtl = new ArrayList<Net_OrderDtl>();
						tempOrder.setListNetOrderDtl(tempListDtl);
						tempOrder.setFirmid(order.getFirmid());
						tempOrder.setTables(order.getTables());
						tempOrder.setId(order.getId());
						tempOrder.setPax(order.getPax());
						bookMealMapper.addOpenItem(tempOrder);
						for(int i = orderDtl.size() - 1; i >= 0; i--) {
							Net_OrderDtl tempDtl = orderDtl.get(i);
							for(Net_OrderDtl dtl : tempOrder.getListNetOrderDtl()) {
								if(tempDtl.getFoodsid().equals(dtl.getFoodsid())) {
									orderDtl.remove(i);
									break;
								}
							}
						}
						
						// 获取菜品附加项列表
						List<NetDishAddItem> listNetDishAddItem = bookMealMapper.getDishAddItemList(pk_group, orderid);
						// 获取附加产品列表
						List<NetDishProdAdd> listNetDishProdAdd = bookMealMapper.getDishProdAddList(pk_group, orderid);
						int dishSeq = 1;

						// 整理附加项分组，如果包含必选附加项，则key值为【additem_菜品主键_必选附加项组主键】,否则为【additem_菜品主键_canSelect】
						Map<String, List<NetDishAddItem>> netDishAddItemMap = new HashMap<String, List<NetDishAddItem>>();
						// 整理附加产品分组，如果包含必选附加产品，则key值为【additem_菜品主键_必选附加产品主键】
						Map<String, List<NetDishProdAdd>> netDishProdAddMap = new HashMap<String, List<NetDishProdAdd>>();
						
						for (Net_OrderDtl dtl : orderDtl) {
							// 如果包含必选附加产品或必选附加项，设置序列号
							if("Y".equals(dtl.getReqredefine()) || "Y".equals(dtl.getProdReqAddFlag())) {
								dtl.setSeq(dishSeq);
								dishSeq++;
							}
							
							// 将附加项添加到菜品下
							List<NetDishAddItem> tempList = new ArrayList<NetDishAddItem>();
							for (NetDishAddItem item : listNetDishAddItem) {
								if (dtl.getId().equals(item.getPk_orderDtlId()) && dtl.getFoodsid().equals(item.getPk_pubItem())) {
									tempList.add(item);
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
								}
							}
							if (!addList.isEmpty()) {
								dtl.setListDishProdAdd(addList);
							}
							//如果是套餐查询套餐明细及其附加项、附加产品
							if("1".equals(dtl.getIspackage())){
								dtl.setGrptyp("packageCode");
								String title = "";
								List<NetDishAddItem> listNetDishAddItemDel = new ArrayList<NetDishAddItem>();
								List<NetDishProdAdd> listNetDishProdAddDel = new ArrayList<NetDishProdAdd>();
								//如果一个账单有一个套餐点了多份，设置套餐序列号
								if(ValueCheck.IsEmpty(orderPackageIdTemp.get(dtl.getFoodsid()))){
									orderPackageIdTemp.put(dtl.getFoodsid(), "true");
								}else{
									dtl.setTcseq(tcseq);
									tcseq++;
								}
								List<Net_OrderPackageDetail> listPackageDtl = bookMealMapper.getOrderPackageDtl(dtl);
								if(null != listPackageDtl && listPackageDtl.size()>0){
									orderPackageDtlIdTemp=new HashMap<String,String>();
									for(Net_OrderPackageDetail orderPackageDtl : listPackageDtl){
										//清空临时变量容器
										tempList = new ArrayList<NetDishAddItem>();
										addList = new ArrayList<NetDishProdAdd>();
										listProductRedfineMust = new HashMap<String,List<NetDishAddItem>>();//必选附加项
										listProductRedfineCanselect = new ArrayList<NetDishAddItem>();//可选附加项
										listNetDishProdAddMust = new HashMap<String,List<NetDishProdAdd>>();//必选附加产品
										listNetDishProdAddCanselect = new ArrayList<NetDishProdAdd>();//可选附加产品
										//如果一个账单有一个套餐一个菜点了多份，设置菜品序列号
										if(ValueCheck.IsEmpty(orderPackageDtlIdTemp.get(orderPackageDtl.getPk_pubitem()))){
											orderPackageDtlIdTemp.put(orderPackageDtl.getPk_pubitem(), "true");
											orderPackageDtl.setTcPubitemSeq(tcDtlSeq+"");
										}else{
											orderPackageDtl.setTcPubitemSeq(tcDtlSeq+"_"+tcPubitemSeq);
											tcPubitemSeq++;
										}
										//将其他信息放到套餐明细中
										FdItemSale fdItemSale = (FdItemSale)mapItemSale.get(orderPackageDtl.getPk_pubitem());
										if(ValueCheck.IsNotEmpty(fdItemSale)){
											if(fdItemSale.getId().equals(orderPackageDtl.getPk_pubitem())){
												orderPackageDtl.setVpcode(fdItemSale.getItcode());
												orderPackageDtl.setVname(fdItemSale.getDes());
												orderPackageDtl.setNprice1(fdItemSale.getPrice());
												orderPackageDtl.setUnit(fdItemSale.getUnit());
												orderPackageDtl.setReqredefine(fdItemSale.getReqredefine());
												orderPackageDtl.setProdReqAddFlag(fdItemSale.getProdReqAddFlag());
											}
										}
										
										//附加项
										for (NetDishAddItem item : listNetDishAddItem) {
											if (orderPackageDtl.getPk_orderpackagedetail().equals(item.getPk_orderDtlId()) && orderPackageDtl.getPk_pubitem().equals(item.getPk_pubItem())) {
												tempList.add(item);
												listNetDishAddItemDel.add(item);//添加需要移除的套餐附加项
											}
										}
										if (!tempList.isEmpty()) {
											listNetDishAddItem.removeAll(listNetDishAddItemDel);//移除套餐附加项
											for(NetDishAddItem dishAddItem : tempList){
												//必选附加项
												if(ValueCheck.IsNotEmpty(dishAddItem.getPk_prodcutReqAttAc())){
													//找到对应的必选附加项组
													if(listProductRedfineMust.get(dishAddItem.getPk_prodcutReqAttAc())!=null 
															&& listProductRedfineMust.get(dishAddItem.getPk_prodcutReqAttAc()).size()>0){
														listProductRedfineMust.get(dishAddItem.getPk_prodcutReqAttAc()).add(dishAddItem);
													}else{//找不到就新增一个
														List<NetDishAddItem> list = new ArrayList<NetDishAddItem>();
														list.add(dishAddItem);
														listProductRedfineMust.put(dishAddItem.getPk_prodcutReqAttAc(), list);
													}
												}else{//可选附加项
													listProductRedfineCanselect.add(dishAddItem);
												}
												title = ValueCheck.IsEmpty(orderPackageDtl.getNetDishAddItemtitle())==true?"":orderPackageDtl.getNetDishAddItemtitle();
												title += "  "+dishAddItem.getRedefineName();
												if(Double.parseDouble(dishAddItem.getNprice())>0){
													title += " <span style='color:red; font-size:90%'>￥"+dishAddItem.getNprice()+"</span>";
												}
												orderPackageDtl.setNetDishAddItemtitle(title);
											}
											orderPackageDtl.setListDishAddItem(tempList);
											//可选附加项
											orderPackageDtl.setListDishAddItemCanselect(listProductRedfineCanselect);
											//必选附加项
											orderPackageDtl.setMapDishAddItemMust(listProductRedfineMust);
										}
										//附加产品
										for(NetDishProdAdd item : listNetDishProdAdd) {
											if (orderPackageDtl.getPk_orderpackagedetail().equals(item.getPk_orderDtlId()) && orderPackageDtl.getPk_pubitem().equals(item.getPk_pubitem())) {
												addList.add(item);
												listNetDishProdAddDel.add(item);//添加需要移除的套餐附加产品
											}
										}
										if (!addList.isEmpty()) {
											listNetDishProdAdd.removeAll(listNetDishProdAddDel);//移除套餐附加产品
											for(NetDishProdAdd netDishProdAdd : addList){
												//必选附加项
												if(ValueCheck.IsNotEmpty(netDishProdAdd.getPk_prodReqAdd())){
													//找到对应的必选附加项组
													if(listNetDishProdAddMust.get(netDishProdAdd.getPk_prodReqAdd())!=null 
															&& listNetDishProdAddMust.get(netDishProdAdd.getPk_prodReqAdd()).size()>0){
														listNetDishProdAddMust.get(netDishProdAdd.getPk_prodReqAdd()).add(netDishProdAdd);
													}else{//找不到就新增一个
														List<NetDishProdAdd> list = new ArrayList<NetDishProdAdd>();
														list.add(netDishProdAdd);
														listNetDishProdAddMust.put(netDishProdAdd.getPk_prodReqAdd(), list);
													}
												}else{//可选附加项
													listNetDishProdAddCanselect.add(netDishProdAdd);
												}
												title = ValueCheck.IsEmpty(orderPackageDtl.getNetDishProdAddtitle())==true?"":orderPackageDtl.getNetDishProdAddtitle();
												title += "  "+netDishProdAdd.getProdAddName();
												if(Double.parseDouble(netDishProdAdd.getNprice())>0){
													title += " <span style='color:red; font-size:90%'>￥"+netDishProdAdd.getNprice()+"</span>";
												}
												orderPackageDtl.setNetDishProdAddtitle(title);
											}
											orderPackageDtl.setListDishProdAdd(addList);
											//可选附加产品
											orderPackageDtl.setListDishProdAddCanselect(listNetDishProdAddCanselect);
											//必选附加项
											orderPackageDtl.setMapDishProdAddMust(listNetDishProdAddMust);
										}
									}
									dtl.setOrderPackageDetailList(listPackageDtl);
									title = "";
								}
							}
							tcDtlSeq++;//套餐明细内的套餐顺序号修改
							
							for (NetDishAddItem item : listNetDishAddItem) {
								if(item.getPk_orderDtlId().equals(dtl.getId())) {
									item.setSeq(dtl.getSeq());
									String itemKey = "additem_" + item.getPk_pubItem() + dtl.getSeq() + "_canSelect";
									String prodcutReqAttAc = item.getPk_prodcutReqAttAc();
									if(null != prodcutReqAttAc && !prodcutReqAttAc.isEmpty() && !"undefined".equals(prodcutReqAttAc)) {
										itemKey = "additem_" + item.getPk_pubItem() + dtl.getSeq() + "_" + item.getPk_prodcutReqAttAc();
									}
									if(netDishAddItemMap.containsKey(itemKey)) {
										netDishAddItemMap.get(itemKey).add(item);
									} else {
										List<NetDishAddItem> listItem = new ArrayList<NetDishAddItem>();
										listItem.add(item);
										netDishAddItemMap.put(itemKey, listItem);
									}
								}
							}
							
							for (NetDishProdAdd item : listNetDishProdAdd) {
								if(item.getPk_orderDtlId().equals(dtl.getId())) {
									item.setSeq(dtl.getSeq());
									String itemKey = "prodadd_" + item.getPk_pubitem() + dtl.getSeq() + "_" + item.getPk_prodReqAdd();
									if(netDishProdAddMap.containsKey(itemKey)) {
										netDishProdAddMap.get(itemKey).add(item);
									} else {
										List<NetDishProdAdd> listItem = new ArrayList<NetDishProdAdd>();
										listItem.add(item);
										netDishProdAddMap.put(itemKey, listItem);
									}
								}
							}
						}
						modelMap.put("netDishAddItemMap", netDishAddItemMap);
						modelMap.put("netDishProdAddMap", netDishProdAddMap);
						order.setListNetOrderDtl(orderDtl);
						order.setListDishAddItem(listNetDishAddItem);
						modelMap.put("dishSeq", dishSeq);
						
						/*// 整理附加项分组，如果包含必选附加项，则key值为【additem_菜品主键_必选附加项组主键】,否则为【additem_菜品主键_canSelect】
						Map<String, List<NetDishAddItem>> netDishAddItemMap = new HashMap<String, List<NetDishAddItem>>();
						for (NetDishAddItem item : listNetDishAddItem) {
							String itemKey = "additem_" + item.getPk_pubItem() + "_canSelect";
							String prodcutReqAttAc = item.getPk_prodcutReqAttAc();
							if(null != prodcutReqAttAc && !prodcutReqAttAc.isEmpty() && !"undefined".equals(prodcutReqAttAc)) {
								itemKey = "additem_" + item.getPk_pubItem() + "_" + item.getPk_prodcutReqAttAc();
							}
							if(netDishAddItemMap.containsKey(itemKey)) {
								netDishAddItemMap.get(itemKey).add(item);
							} else {
								List<NetDishAddItem> listItem = new ArrayList<NetDishAddItem>();
								listItem.add(item);
								netDishAddItemMap.put(itemKey, listItem);
							}
						}
						modelMap.put("netDishAddItemMap", netDishAddItemMap);
						
						// 整理附加产品分组，如果包含必选附加产品，则key值为【additem_菜品主键_必选附加产品主键】
						Map<String, List<NetDishProdAdd>> netDishProdAddMap = new HashMap<String, List<NetDishProdAdd>>();
						for (NetDishProdAdd item : listNetDishProdAdd) {
							String itemKey = "prodadd_" + item.getPk_pubitem() + "_" + item.getPk_prodReqAdd();
							if(netDishProdAddMap.containsKey(itemKey)) {
								netDishProdAddMap.get(itemKey).add(item);
							} else {
								List<NetDishProdAdd> listItem = new ArrayList<NetDishProdAdd>();
								listItem.add(item);
								netDishProdAddMap.put(itemKey, listItem);
							}
						}
						modelMap.put("netDishProdAddMap", netDishProdAddMap);*/
					}
					if(null != order.getRemark() && !"".equals(order.getRemark())) {
						order.setRemark(order.getRemark().replaceAll("[\\t\\n\\r]"," "));
					}
					modelMap.put("orders", order);
				}
			} else {
				if(null == tables || "".equals(tables)) {
					tables = request.getParameter("tables");
				}
				Net_Orders orders = new Net_Orders();
				orders.setPk_group(pk_group);
				orders.setFirmid(firmid);
				orders.setOpenid(openid);
				orders.setTables(tables);
				orders.setResv("");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				orders.setDat(sdf.format(Calendar.getInstance().getTime()));

				modelMap.put("orders", orders);
			}
			modelMap.put("type", type);
			modelMap.put("tcPubitemSeq", tcPubitemSeq);
			
			// 订位单单号
			String bookDeskOrderID = request.getParameter("bookDeskOrderID");
			modelMap.put("bookDeskOrderID", bookDeskOrderID == null ? "" : bookDeskOrderID);

			/*************订位单关联的点菜单修改菜品时************/
			modelMap.put("pageFrom", pageFrom==null?"":pageFrom);
			
			modelMap.put("mustPayBeforeOrder", Commons.getConfig().getProperty("mustPayBeforeOrder"));
			
			return new ModelAndView(BookMealConstants.LISTMENU, modelMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(BookMealConstants.FAILEDPAGE, modelMap);
	}
	
	/**
	 * 过滤全新方案和附加方案中重复的菜品，删除优先级低的
	 * @param listItem
	 * @return
	 */
	private List<FdItemSale> clearDuplicateItem(List<FdItemSale> listItem) {
		List<FdItemSale> listItemSale = new ArrayList<FdItemSale>();
		LinkedHashMap<String, FdItemSale> MapItemSale = new LinkedHashMap<String, FdItemSale>();
		for(FdItemSale item : listItem) {
			String key = item.getId();
			if(MapItemSale.containsKey(key) && Integer.parseInt(item.getVlev()) > Integer.parseInt(MapItemSale.get(key).getVlev())) {
				// 如果此菜品在Map中已存在，判断优先级，保存优先级高的
				MapItemSale.put(key, item);
			} else {
				// Map中不存在，直接保存
				MapItemSale.put(key, item);
			}
		}
		
		// 循环map，将菜品放入返回列表中
		Iterator<Map.Entry<String, FdItemSale>> it = MapItemSale.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, FdItemSale> entry = it.next();
			listItemSale.add(entry.getValue());
		}
		
		return listItemSale;
	}
	
	/**
	 * 过滤全新方案和附加方案中重复的附加产品，删除优先级低的
	 * @param listItem
	 * @return
	 */
	private List<ProductAdditional> clearDuplicateProdItem(List<ProductAdditional> listProdItem) {
		List<ProductAdditional> listProdAdd = new ArrayList<ProductAdditional>();
		Map<String, ProductAdditional> MapItemSale = new HashMap<String, ProductAdditional>();
		for(ProductAdditional item : listProdItem) {
			String key = item.getPk_prodAdd();
			if(MapItemSale.containsKey(key) && Integer.parseInt(item.getVlev()) > Integer.parseInt(MapItemSale.get(key).getVlev())) {
				// 如果此菜品在Map中已存在，判断优先级，保存优先级高的
				MapItemSale.put(key, item);
			} else {
				// Map中不存在，直接保存
				MapItemSale.put(key, item);
			}
		}
		
		// 循环map，将菜品放入返回列表中
		Iterator<Map.Entry<String, ProductAdditional>> it = MapItemSale.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, ProductAdditional> entry = it.next();
			listProdAdd.add(entry.getValue());
		}
		
		return listProdAdd;
	}
	
	/**
	 * 根据菜品过滤类别，没有菜品的类别不显示
	 * @param modelMap
	 * @param listItemSale
	 * @param listItemType
	 */
	private void clearItemSaleByType(ModelMap modelMap, List<FdItemSale> listItemSale, List<FdItemType> listItemType) {
		//按顺序号排序
		try{
			Collections.sort(listItemSale, new Comparator<FdItemSale>() {   
				public int compare(FdItemSale f1, FdItemSale f2) {
					if(!StringUtils.hasText(f1.getSortnum())){
						return 1;
					}else if(!StringUtils.hasText(f2.getSortnum())){
						return -1;
					}else{
						Integer num1 = Integer.parseInt(f1.getSortnum());
						Integer num2 = Integer.parseInt(f2.getSortnum());
						return num1.compareTo(num2);
					}
				}
			});
		}catch(Exception e){
		}
		
		Map<String, JSONArray> map = new HashMap<String, JSONArray>();
		for(FdItemSale item : listItemSale) {
			String key = item.getGrptyp();
			// 如果已包含此类别，将数据加入列表
			if(map.containsKey(key)) {
				map.get(key).add(JSONObject.fromObject(item));
			} else {
				JSONArray tempArray = new JSONArray();
				tempArray.add(JSONObject.fromObject(item));
				map.put(key, tempArray);
			}
		}
		
		// 过滤类别，不包含菜品的类别不显示
		int cnt = listItemType.size() - 1;
		for(int i = cnt; i >= 0; i--) {
			FdItemType type = listItemType.get(i);
			if(!map.containsKey(type.getCode().toString())) {
				listItemType.remove(i);
			}
		}
		
		modelMap.put("allItemListMap", map);
		modelMap.put("listItemType", listItemType);
	}

	/**
	 * 菜品详情
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "listMenuDtl")
	@ResponseBody
	public Object listMenuDtl(ModelMap modelMap, HttpServletRequest request) {
		try {
			String pkGroup = request.getParameter("pk_group");
			String firmId = request.getParameter("firmid");
			String itemType = request.getParameter("itemtype");
			String firmCode = request.getParameter("firmCode");

			String type = request.getParameter("type");

			List<FdItemSale> listItemSale = bookMealMapper.getFdItemSaleList(
					pkGroup, firmId, itemType, null, null, null, type);
			
			// 过滤沽清菜品
			filterSellOffDish(modelMap, pkGroup, firmCode, listItemSale);
			
			return listItemSale;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 过滤沽清菜品
	 * @param modelMap
	 * @param pkGroup
	 * @param firmId
	 * @param listItemSale
	 */
	private void filterSellOffDish(ModelMap modelMap, String pkGroup,
			String firmId, List<FdItemSale> listItemSale) {
		// 查询沽清菜品
		SellOff data = new SellOff();
		data.setPk_group(pkGroup);
		data.setPk_store(firmId);
		List<SellOff> sellOffList = bookMealMapper.getSellOffList(data);
		for(FdItemSale item : listItemSale) {
			for(SellOff info : sellOffList) {
				// 门店不能传菜品主键，只能使用菜品编码
				if(item.getItcode().equals(info.getPk_pubItem())) {
					item.setHasSellOff("Y");
				}
			}
		}
		
		modelMap.put("listItemSale", listItemSale);
	}

	/**
	 * 根据门店获取套餐
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "listPackages")
	@ResponseBody
	public Object listPackages(ModelMap modelMap, HttpServletRequest request) {
		try {
			// String pkGroup = request.getParameter("pk_group");
			String firmId = request.getParameter("firmid");
			// String itemType = request.getParameter("itemtype");

			List<ItemPrgPackage> listPackage = PubitemSearch
					.findItemPrgPackage(firmId);
			for (ItemPrgPackage item : listPackage) {
				item.setWxbigpic(item.getPicsrc());
				item.setWxsmallpic(item.getPicsrc());
			}

			modelMap.put("listPackage", listPackage);
			return listPackage;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据门店,套餐ID查询套餐明细
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "findPackageDetail")
	@ResponseBody
	public Object findPackageDetail(ModelMap modelMap,
			HttpServletRequest request) {
		try {
			String firmId = request.getParameter("firmId");
			String packageId = request.getParameter("packageId");

			List<ItemPrgpackAgedtl> packageDetail = PubitemSearch
					.findItemPrgpackAgedtl(firmId, packageId);
			modelMap.put("packageDetail", packageDetail);
			return packageDetail;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 查询订单详情
	 * 
	 * @param modelMap
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "orderDetail")
	public ModelAndView orderDetail(ModelMap modelMap,
			HttpServletRequest request) {
		try {
			String firmid = request.getParameter("firmid");
			String orderid = request.getParameter("orderid");
			String pk_group = request.getParameter("pk_group");
			String code = request.getParameter("code");
			String openid = request.getParameter("openid");

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
			List<Net_Orders> listNet_Orders = bookMealMapper.getOrderMenus(map);

			List<Firm> listFirm = new ArrayList<Firm>();
			List<Net_OrderDtl> orderDtl = PubitemSearch
					.getOrderDtlMenus(orderid);

			if (listNet_Orders != null && !listNet_Orders.isEmpty()) {
				modelMap.put("orderHead", listNet_Orders.get(0));
				firmid = listNet_Orders.get(0).getFirmid();
				map.put("firmid", firmid);
				listFirm = bookMealMapper.getFirmList(null, null, firmid);
				
				Map<String,Object> storeSeatMap = PubitemSearch.getStoreSeatInfo(firmid, listNet_Orders.get(0).getTables());
			}
			if (listFirm != null && !listFirm.isEmpty()) {
				modelMap.put("firm", listFirm.get(0));
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
			if (null != listNet_Orders && !listNet_Orders.isEmpty()) {
				DecimalFormat df = new DecimalFormat("####0.00");
				modelMap.put("totalPrice", df.format(totalPrice));
				modelMap.put("orderDetail", listNet_Orders.get(0));
			}
			modelMap.put("pk_group", pk_group);
			modelMap.put("pageFrom", request.getParameter("pageFrom"));

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
			if(user == null){//返回按钮，已取得用户授权的情况
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
			modelMap.put("openScan", request.getParameter("openScan"));
			
			// 是否已到店
			modelMap.put("inStoreType", request.getParameter("inStoreType"));
			// 是否扫码点餐
			modelMap.put("scanType", request.getParameter("scanType"));
			
			request.setAttribute("openid", openid);
			modelMap.put("openid", openid);
			
			// 获取电子券
			processCoupon(modelMap, openid, firmid);
			
			// 页面来源
			modelMap.put("pageFrom", request.getParameter("pageFrom"));
			
			// 页面来源
			modelMap.put("deskOrderState", request.getParameter("deskOrderState"));
			
			//团购券 20151026 song
			modelMap.put("groupactms", bookMealMapper.getGroupActm(firmid, null));
			
			if("Y".equals(Commons.getConfig().getProperty("generateQrCode"))) {
				return new ModelAndView(BookMealConstants.ORDER_DETAIL_QR, modelMap);
			} else {
				return new ModelAndView(BookMealConstants.ORDERDETAIL, modelMap);
			}
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

	/**
	 * 设置标志位：扫码下单
	 * @param modelMap
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "setScanEventValue")
	@ResponseBody
	public Object setScanEventValue(ModelMap modelMap, HttpServletRequest request) {
		String openid = request.getParameter("openid");
		String pk_group = request.getParameter("pk_group");
		String orderid = request.getParameter("orderid");
		String firmid = request.getParameter("firmid");
		String firmCode = request.getParameter("firmCode");
		String pax = request.getParameter("pax");
		Map<String, String> scanEventMap = MessageUtil.getScanEventMap();
		if(null == scanEventMap) {
			scanEventMap = new HashMap<String, String>();
		}
		scanEventMap.put(openid, "1");
		scanEventMap.put(openid + "_orderid", orderid);
		scanEventMap.put(openid + "_firmid", firmid);
		scanEventMap.put(openid + "_firmCode", firmCode);
		scanEventMap.put(openid + "_pax", pax);
		System.out.println("==========================================="+JSONObject.fromObject(scanEventMap).toString());
		MessageUtil.setScanEventMap(scanEventMap);
		
		return null;
	}
	
	/**
	 * 获取扫描结果
	 * @param modelMap
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getTableId")
	@ResponseBody
	public Object getTableId(ModelMap modelMap, HttpServletRequest request) {
		String openid = request.getParameter("openid");
		Map scanEventMap = MessageUtil.getScanEventMap();
		String scanEventKey = "";
		int cnt = 0;
		
		try {
			while(true) {
				if(null != scanEventMap && scanEventMap.containsKey(openid + "_event_key")) {
					scanEventKey = scanEventMap.get(openid + "_event_key").toString();
					break;
				} else {
					System.out.println("sleep 500ms...........................");
					if(cnt > 10) {
						scanEventMap.put(openid, "0");
						MessageUtil.setScanEventMap(scanEventMap);
						break;
					}
					cnt ++;
					Thread.sleep(500);
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return scanEventKey;
	}

	/**
	 * 保存菜品
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "saveOrderOrDtl")
	@ResponseBody
	public String saveOrderOrDtl(ModelMap modelMap, HttpServletRequest request, Net_Orders orders) {
		try {
			String resv = DateFormat
					.getStringByDate(new Date(), "yyMMddHHmmssSS")
					+ (new java.util.Random().nextInt(90) + 10);
			String id = CodeHelper.createUUID();
			if(null != orders.getId() && !orders.getId().isEmpty()) {
				id = orders.getId();
				if(null != orders.getResv() && !orders.getResv().isEmpty()) {
					resv = orders.getResv();
				}
				// 通过修改菜品进入，先删除以前点的菜品
				bookMealMapper.deleteOrder(id);
			}
			orders.setId(id);
			orders.setResv(resv);
			orders.setIsfeast("0");
			
			String type = request.getParameter("type");
			if("takeout".equals(type)){
				orders.setIsfeast("2");//用2代表外卖订单
				orders.setState("0");//保存，未下单状态
			}else{
				//20151112 订餐订单保存姓名、电话
				WeChatUser user = (WeChatUser) request.getSession().getAttribute("WeChatUser");
				
				/***20150625 如果Unionid不为空，则用他取值***/
				List<Card> listCard = null;
				if(StringUtils.hasText(user==null?"":user.getUnionid())){
					listCard = CardSearch.listCard(user.getUnionid());
				}else{
					listCard = CardSearch.listCard(orders.getOpenid());
				}
				
				if (listCard != null && listCard.size() > 0) {
					orders.setName(listCard.get(0).getName());
					orders.setContact(listCard.get(0).getTele());
				} else {
					if(null != user) {
						orders.setName(user.getNickname());
					}
					
					/*// 插入会员记录
					Card card = new Card();
					card.setName(orders.getName());
					card.setOpenid(orders.getOpenid());
					card.setChlb("会员");
					card.setFirmId(orders.getVcode());
					card.setTyp(Commons.getConfig().getProperty("registerCardTyp"));
					CardSearch.addCardRed(card);*/
				}
			}
			
			orders.setRannum(resv + GenerateRandom.getRanNum(6));
			for (int i = 0; i < orders.getListNetOrderDtl().size(); i++) {
				orders.getListNetOrderDtl().get(i)
						.setId(CodeHelper.createUUID());
				orders.getListNetOrderDtl().get(i).setOrdersid(id);
			}
			String cont = bookMealMapper.saveOrder(orders);

			if (null == cont || cont.equals("")) {
				return "-1";
			}
			// 调用呼叫中心订单推送
			if (orders.getStws().equals("1")) {
				PubitemSearch.pushOrderToCallCenter(orders);						
			}
			
			// 判断是否需要生成二维码
			if(!"modify".equals(type) && "Y".equals(Commons.getConfig().getProperty("generateQrCode"))) {
				String path = CodeHelper.QrcodePic + resv + ".png";
				CardQrCode handler = new CardQrCode();
				String dat = DateFormat.getStringByDate(new Date(), "yyyyMMdd");
		        handler.encoderQRCode(orders.getVcode() + "_" + resv + "_" + dat, path, "png");
			}
			
			// 如果是扫码点餐，不推送订单消息
			if((null != orders.getTables() && !orders.getTables().isEmpty()) 
					|| (null != orders.getBookDeskOrderID() && !orders.getBookDeskOrderID().isEmpty())) {
				return id;
			}
			
			// 如果是修改菜品，不推送订单消息
			if("modify".equals(type) || "takeout".equals(type)) {
				return id;
			}
			
			/**START**/
			/**订单完成后推送模版消息 song 20150506**/ 
			Map<String,String> msgMap = new HashMap<String,String>();
			msgMap.put("templateCode", "TM00530");
			msgMap.put("openid", orders.getOpenid());
			
			StringBuffer sb = request.getRequestURL();
			sb = sb.delete(sb.lastIndexOf("/"), sb.length());
			sb.append("/orderDetail.do?orderid=").append(orders.getId())
				.append("&openid=").append(orders.getOpenid())
				.append("&firmid=").append(orders.getFirmid())
				.append("&pk_group=").append(orders.getPk_group())
				.append("&orderTyp=0");
			msgMap.put("url", sb.toString());
			
			msgMap.put("first", "您好，已接收到您的点菜订单");
			
			List<Firm> list = bookMealMapper.getFirmList("","",orders.getFirmid());
			String storeName = "";
			if(list!=null && list.size()>0){
				storeName = list.get(0).getFirmdes();
			}
			msgMap.put("storeName", storeName);
			
			msgMap.put("orderId", orders.getResv());
			msgMap.put("orderType", "堂食");
			
			msgMap.put("remark", "感谢您的光临，请尽快进店扫码下单");
			
			String pk_group = orders.getPk_group();
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
			/**END**/
			return id;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 保存菜品
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "addMenu")
	@ResponseBody
	public String addMenu(ModelMap modelMap, HttpServletRequest request, Net_Orders orders) {
		String id = orders.getId();
		try {
			for (int i = 0; i < orders.getListNetOrderDtl().size(); i++) {
				orders.getListNetOrderDtl().get(i)
						.setId(CodeHelper.createUUID());
				orders.getListNetOrderDtl().get(i).setOrdersid(id);
			}
			List<Net_OrderDtl> listDtl = orders.getListNetOrderDtl();
			String cont = bookMealMapper.addMenu(orders);

			if (null == cont || cont.equals("")) {
				return "-1";
			}
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", orders.getId());
			//查询账单主表
			List<Net_Orders> listNet_Orders = bookMealMapper.getOrderMenus(map);
			if (listNet_Orders != null && !listNet_Orders.isEmpty()) {
				orders = listNet_Orders.get(0);
			}
			//查询门店
			List<Firm> listFirm = bookMealMapper.getFirmList(null, null, orders.getFirmid());

			if (listFirm != null && !listFirm.isEmpty()) {
				Firm firm =  listFirm.get(0);
				orders.setVcode(firm.getFirmCode());
			}else{
				return "门店不存在";
			}
			
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
			// 订单总金额
			double totalPrice = 0.0;
			//循环账单明细，将附加项与菜品关联起来
			for (Net_OrderDtl dtl : listDtl) {
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
				if(null != listNetDishAddItem && !listNetDishAddItem.isEmpty()) {
					List<NetDishAddItem> tempList = new ArrayList<NetDishAddItem>();
					for (NetDishAddItem item : listNetDishAddItem) {
						if (dtl.getId().equals(item.getPk_orderDtlId()) && dtl.getFoodsid().equals(item.getPk_pubItem())) {
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
				//如果菜品是套餐
				if("1".equals(dtl.getIspackage())){
					//查询套餐明细
					List<Map<String,Object>> listPacageDtl = PubitemSearch.listFolioPacageDtl(dtl);
					// 将附加项添加到菜品下
					List<NetDishAddItem> tempList = new ArrayList<NetDishAddItem>();
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
					dtl.setOrderPackageDetailList(null);//清空保存时的内容，pos不用
				}
			}
			orders.setListNetOrderDtl(listDtl);
			orders.setSumprice(String.valueOf(totalPrice));
			orders.setSerialid(CodeHelper.createUUID());
			orders.setType("3");
			//记录本次记录
			Map<String,Object> mqMap = new HashMap<String,Object>();
			mqMap.put("pk_mqlogs", orders.getSerialid());
			mqMap.put("orderid", orders.getId());
			mqMap.put("vtype", "3");
			mqMap.put("errmsg", "");
			mqMap.put("state", "");
			bookDeskMapper.addMqLogs(mqMap);
			//发送mq消息
			orders.setSerialid(mqMap.get("pk_mqlogs")+"");//重置mq记录的主键
			// 通过MQ推送加菜消息
			JSONObject json = JSONObject.fromObject(orders);
			LogUtil.writeToTxt(LogUtil.MQSENDORDER, "加菜下单到MQ服务器中，队列名：" + orders.getVcode() + "_orderList，订单对象：" + json.toString());
			MqSender.sendOrders(json.toString(), orders.getVcode() + "_orderList",Commons.mqEffectiveTime);//设置消息有效期为半小时;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	/**
	 * 按名字查询菜品
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "getPubitemByName")
	@ResponseBody
	public Object getPubitemByName(ModelMap modelMap, HttpServletRequest request, FdItemSale fdItemSale) {
		try {
			String type = request.getParameter("type");
			String pageFrom = request.getParameter("pageFrom");
			if(pageFrom!=null && "takeout".equals(pageFrom)){
				type = pageFrom;
			}
			
			List<FdItemSale> listItemSale = bookMealMapper.getFdItemSaleList(
					fdItemSale.getPk_group(), fdItemSale.getFirmid(), null,
					fdItemSale.getDes(), null, null, type);

			// 过滤沽清菜品
			String firmCode = request.getParameter("firmCode");
			filterSellOffDish(modelMap, fdItemSale.getPk_group(), firmCode, listItemSale);
			
			return listItemSale;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 跳转到成功页面
	 * 
	 * @param modelMap
	 * @param HttpServletRequest
	 * @return
	 */
	@RequestMapping(value = "successpage")
	public ModelAndView toSuccessPage(ModelMap modelMap,
			HttpServletRequest request) {
		modelMap.put("orderid", request.getParameter("orderid"));
		modelMap.put("firmid", request.getParameter("firmid"));
		modelMap.put("pk_group", request.getParameter("pk_group"));
		modelMap.put("openid", request.getParameter("openid"));

		return new ModelAndView(BookMealConstants.SUCCESSPAGE, modelMap);
	}

	/**
	 * 
	 * @param modelMap
	 * @param orderid
	 * @param pk_group
	 * @return 取消订单
	 */
	@RequestMapping(value = "cancelOrder")
	public ModelAndView cancelOrder(ModelMap modelMap, String orderid,
			String pk_group) {
		try {
			bookMealMapper.cancelOrders(orderid);
			return new ModelAndView("redirect:/bookMeal/listFirm.do?pk_group="
					+ pk_group);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(BookDeskConstants.FAILEDPAGE, modelMap);
	}
	
	
	@RequestMapping(value = "updateOrderState")
	@ResponseBody
	public Object updateOrderState(ModelMap modelMap, String pk_group, String orderid, String state) {
		return bookMealMapper.updateOrderState(orderid, state);
	}
}