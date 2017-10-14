package com.choice.test.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.choice.test.domain.Arear;
import com.choice.test.domain.CALLBILL_DEAILDE;
import com.choice.test.domain.CALLBILL_Order;
import com.choice.test.domain.CallBillOrder;
import com.choice.test.domain.City;
import com.choice.test.domain.DeskTimes;
import com.choice.test.domain.FavorArea;
import com.choice.test.domain.Firm;
import com.choice.test.domain.ItemPrgPackage;
import com.choice.test.domain.ItemPrgpackAgedtl;
import com.choice.test.domain.Net_OrderDtl;
import com.choice.test.domain.Net_Orders;
import com.choice.test.domain.Project;
import com.choice.test.domain.ProjectType;
import com.choice.test.domain.Sft;
import com.choice.test.domain.StoreTable;
import com.choice.test.domain.WebMsg;
import com.choice.test.persistence.WeChatOrderMapper;
import com.choice.test.service.impl.WechatOrderService;
import com.choice.test.utils.CodeHelper;
import com.choice.test.utils.Commons;
import com.choice.test.utils.DateFormat;
import com.choice.wechat.domain.bookMeal.FdItemSale;
import com.choice.wechat.domain.bookMeal.NetDishAddItem;
import com.choice.wechat.domain.bookMeal.NetDishProdAdd;
import com.choice.wechat.domain.cookie.Cookie;
import com.choice.wechat.persistence.cookie.CookieMapper;
import com.choice.wechat.persistence.cookie.impl.CookieMapperImpl;
import com.choice.wechat.util.LogUtil;
import com.choice.wechat.util.MessageUtil;
import com.choice.wechat.util.WeChatUtil;
import com.choice.wechat.util.mq.MqSender;
import com.choice.wxc.util.JdbcConnection;
import com.choicesoft.webService.server.CallBill_OrderWebService;

/**
 * 菜谱
 * @author 孙胜彬
 */
public class PubitemSearch {
	static Logger logger = Logger.getLogger(PubitemSearch.class.getName());
	static WeChatOrderMapper orderMapper = new WechatOrderService();
	static CookieMapper cookieMapper = new CookieMapperImpl();
	public static void main(String[] args){
		try {
			System.out.println(getCity());
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取城市
	 * @return
	 */
	public static List<City> getCity(){
		try {
			List<City> listCity=orderMapper.getCity(null);
			return listCity;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 获取门店
	 * @return
	 * @throws Exception 
	 */
	public static List<Firm> getStore(String area) throws Exception{
		try {
			List<Firm> listFirm=orderMapper.getTeleStore(null, null,area);
			return listFirm;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获取门店通过firmId
	 * @return
	 * @throws Exception 
	 */
	public static List<Firm> findStoreByFirmId(String firmId) throws Exception{
		try {
			List<Firm> listFirm=orderMapper.getTeleStore(firmId, null,null);
			return listFirm;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获取门店通过firmId
	 * @return
	 * @throws Exception 
	 */
	public static Firm getStoreByFirmId(String firmId){
//		try {
			List<Firm> listFirm=orderMapper.getTeleStore(firmId, null,null);
			return listFirm.get(0);
//		} catch (Exception e) {
//			logger.error(e);
//			e.printStackTrace();
//		}
//		return null;
	}
	/**
	 * 获取区域
	 * @return
	 */
	public static List<Arear> getAres(){
		try {
			List<Arear> listArear=orderMapper.getTeleArea("AR", null, "1102");
			return listArear;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获取台位
	 * @return
	 */
	public static List<StoreTable> getTs(String firmId){
		try {
			List<StoreTable> listStoreTable=orderMapper.getTeleStoreTable(null, null, null,firmId);
			return listStoreTable;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获取菜品类别
	 * @return
	 */
	public static List<ProjectType> getPubitemTyp(String firmId){
		try {
			List<ProjectType> listProjectType=orderMapper.getTeleProjectType(null, null,firmId);
			return listProjectType;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获取菜品
	 * @return
	 */
	public static List<Project> getPubitem(String projecttyp,String firmId){
		try {
			List<Project> listProject=orderMapper.getTeleProject(projecttyp,null, null,firmId,null);
			return listProject;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 按名字查询菜品
	 * @return
	 */
	public static List<Project> getPubitemByName(String name,String firmId){
		try {
			List<Project> listProject=orderMapper.getTeleProject(null,null, name,firmId,null);
			return listProject;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 查询所有菜品
	 * @return
	 */
	public static List<Project> getAllPubitem(String firmId){
		try {
			List<Project> listProject=orderMapper.getTeleProject(null,null, null,firmId,null);
			return listProject;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 查询热门菜品
	 * @return
	 */
	public static List<Project> getHotPubitem(String firmId){
		try {
			List<Project> listProject=orderMapper.findHotItem(firmId);
			return listProject;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 根据编码获取菜品
	 * @return
	 */
	public static List<Project> getCodePubitem(String code,String firmId){
		try {
			List<Project> listProject=orderMapper.getTeleProject(null,code, null,firmId,null);
			return listProject;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获取我的订单所属门店
	 * @return
	 */
	public static List<Net_Orders> getOrderCity(String openid){
		try {
			List<Net_Orders> listNet_Orders=orderMapper.getOrderCity("", openid);
			return listNet_Orders;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获取我的订单
	 * @return
	 */
	public static List<Net_Orders> getOrderMenus(String id,String openid,String firmid,String dat,String dateTime){
		try {
			List<Net_Orders> listNet_Orders=orderMapper.getOrderMenus(id,"", openid,firmid,dateTime,dat);
			return listNet_Orders;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获取我的订单详情
	 * @return
	 */
	public static List<Net_OrderDtl> getOrderDtlMenus(String orderid){
		try {
			List<Net_OrderDtl> listNet_OrderDtl=orderMapper.getOrderDtlMenus(orderid);
			return listNet_OrderDtl;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获取餐次
	 * @return
	 */
	public static List<Sft> getSft(){
		try {
			List<Sft> listSft=orderMapper.getSFT(null);
			return listSft;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获取时间台位，台位状态
	 * @return
	 */
	public static List<DeskTimes> getDeskTimes(String resvtblid, String sft, String dat){
		try {
			List<DeskTimes> listDeskTimes=orderMapper.getDeskTimes(resvtblid, null,sft,dat);
			return listDeskTimes;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获取可预订台位
	 * @return
	 */
	public static List<StoreTable> findTable(String sft,String dat,String firmId){
		try {
			List<StoreTable> listStoreTable=orderMapper.findStoreTable(sft, dat, firmId);
			return listStoreTable;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获取可预订台位或者包间
	 * @return
	 */
	public static List<StoreTable> findResbTbl(String roomTyp, String sft, String dat,
			String firmId,String pax){
		try {
			List<StoreTable> listStoreTable=orderMapper.findResvTbl(roomTyp, sft, dat, firmId,pax);
			return listStoreTable;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 订单保存
	 * @return
	 */
	public static String saveOrder(Net_Orders orders){
		try {
			String cont=orderMapper.saveMenus(orders);
			if(null==cont || cont.equals("")){
				return "-1";
			}
			return cont;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 订单详情保存
	 * @return
	 */
	public static String saveOrderDtl(Net_OrderDtl ordersDtl){
		try {
			String cont=orderMapper.saveOrderDtl(ordersDtl);
			if(null==cont || cont.equals("")){
				return "-1";
			}
			return cont;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 取消订单
	 * @return
	 */
	public static String deleteOrder(String resv,String id){
		try {
			String cont=orderMapper.deleteOrder("3", resv);
//			orderMapper.deleteOrderDtl(id,null);
			if(null==cont || cont.equals("")){
				return "-1";
			}
			return cont;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 取消订单详情
	 * @return
	 */
	public static String deleteOrderDtl(String id){
		try {
			String cont=orderMapper.deleteOrderDtl(null,id);
			if(null==cont || cont.equals("")){
				return "-1";
			}
			return cont;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 删除某个订单的菜品
	 * @return
	 */
	public static String deleteOrderDtlByResv(String resv){
		try {
			String cont=orderMapper.deleteOrderDtl(resv,null);
			if(null==cont || cont.equals("")){
				return "-1";
			}
			return cont;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 自助点餐保存菜品
	 * @return
	 */
	public static String saveOrdersOrDtl(Net_Orders orders){
		try {
			String cont=orderMapper.saveOrdersOrDtl(orders);
			if(null==cont || cont.equals("")){
				return "-1";
			}
			if(orders.getStws().equals("1")){
				pushOrderToCallCenter(orders);
			}
			return cont;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 调用呼叫中心订单推送
	 * @param orders
	 */
	public static void pushOrderToCallCenter(Net_Orders orders) {
		Firm firm=orderMapper.getTeleStore(orders.getFirmid(), null,null).get(0);
		
		List<CallBillOrder> listCallBillOrder=new ArrayList<CallBillOrder>();
		CallBillOrder callbillorder=new CallBillOrder();
		List<CALLBILL_Order> CALLBILL_Orders=new ArrayList<CALLBILL_Order>();
		List<CALLBILL_DEAILDE> CALLBILL_DEAILDEs=new ArrayList<CALLBILL_DEAILDE>();
		CALLBILL_Order order=new CALLBILL_Order();
		order.setScode(firm.getVcode());//店铺编码
		order.setOrdcode(orders.getResv());//订单编码
		order.setCustomcode("");//顾客编码
		order.setCname("");//名称
		order.setAddress(orders.getRemark());//地址
		order.setSex("");//性别
		order.setTel(orders.getContact());//电话
		order.setJcode("");//接线员编码
		order.setJname("");//接线员名称
		order.setState("2");
		order.setTotalmoney(orders.getMoney());//订单金额
		order.setTotalscot("");//合计税金
		order.setFtime("");//开单时间
		order.setEtime("");//订单保存时间
		order.setChktime("");//审核时间
		order.setGettime("");//获取订单时间
		order.setFinshtime("");//送餐完毕时间
		order.setStws(orders.getStws());//是否外送
		order.setAssigntime("");//派送时间
		order.setDistrictname("");//商圈名称
		order.setWaddress("");//外送地址
		order.setDcode("");//商圈编码
		order.setWaddressid("");//外送地址ID
		order.setCheckcode("");//审核人编码
		order.setCheckname("");//审核人名称
		order.setAddrstate("1");//地址状态
		order.setCtitime("");//来点时间
		order.setCmemo("");//备注
		order.setPaytime("");//支付时间
		order.setNeedinvoice("");//是否需要发票
		order.setInvoicetitle("");//发表抬头
		order.setOrdersource("4");//订单来源
		order.setPayState("0");//支付状态
		CALLBILL_Orders.add(order);
		for(int i=0;i<orders.getListNetOrderDtl().size();i++){
			Net_OrderDtl dtl= new Net_OrderDtl();
			dtl=orders.getListNetOrderDtl().get(i);
			List<Project> listProject=orderMapper.getTeleProject(null,null, null,null,dtl.getFoodsid());
			Project pro=listProject.get(0);
			CALLBILL_DEAILDE orderdtl=new CALLBILL_DEAILDE();
			orderdtl.setOrdcode(orders.getResv());//订单编码
			orderdtl.setBdcode(dtl.getId());//订单明细表主键
			orderdtl.setPcode(pro.getPitcode());//菜品编码
			orderdtl.setPname(dtl.getFoodsname());//菜品名称
			orderdtl.setAmount(dtl.getFoodnum());//数量
			orderdtl.setUnit("");//单位
			orderdtl.setAmoney(dtl.getPrice());//金额
			orderdtl.setScot("");//税金
			orderdtl.setDtime(DateFormat.getStringByDate(new Date(), "yyyy-MM-dd"));//创建时间
			orderdtl.setMoney(dtl.getTotalprice());//金额
			orderdtl.setYmoney(dtl.getTotalprice());//有效金额
			orderdtl.setZmoney(dtl.getTotalprice());//折扣金额
			orderdtl.setCount(dtl.getFoodnum());//数量
			orderdtl.setYcount(dtl.getFoodnum());//有效数量
			orderdtl.setZcount(dtl.getFoodnum());//折扣数量
			orderdtl.setDept("");//
			orderdtl.setDprice("");
			orderdtl.setIscoupon("");//是否优惠
			orderdtl.setCmemo("");//备注
			CALLBILL_DEAILDEs.add(orderdtl);
		}
		callbillorder.setCALLBILL_Orders(CALLBILL_Orders);
		callbillorder.setCALLBILL_DEAILDEs(CALLBILL_DEAILDEs);
		//调用呼叫中心订单推送
		JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();       
		factoryBean.setServiceClass(CallBill_OrderWebService.class);       
		factoryBean.setAddress(Commons.CALLwebService); 
		CallBill_OrderWebService callService = (CallBill_OrderWebService)factoryBean.create();
		listCallBillOrder.add(callbillorder);
		callService.pushOrderByWeixin(listCallBillOrder, "");
	}
	/**
	 * 查询优惠信息
	 * @return
	 */
	public static List<WebMsg> findWebMsg(String dat,String firmId,String favorAreaId){
		try {
			List<WebMsg> listWebMsg=orderMapper.findWebMsg(dat, firmId,favorAreaId);
			return listWebMsg;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 查询优惠信息区域
	 * @return
	 */
	public static List<FavorArea> findFavorArea(String id){
		try {
			List<FavorArea> listFavorArea=orderMapper.findFavorArea(id);
			return listFavorArea;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 判断是否有套餐
	 * @return
	 */
	public static String flagWebMsg(String firmId){
		try {
			String cont=orderMapper.flagPkgPackAge(firmId);
			return cont;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 查询套餐
	 * @return
	 */
	public static List<ItemPrgPackage> findItemPrgPackage(String firmId){
		try {
			List<ItemPrgPackage> listItemPrgPackage=orderMapper.findPkgPackAge(null, firmId);
			return listItemPrgPackage;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 查询套餐明细
	 * @return
	 */
	public static List<ItemPrgpackAgedtl> findItemPrgpackAgedtl(String firmId,String packAgeId){
		try {
			List<ItemPrgpackAgedtl> listItemPrgpackAgedtl=orderMapper.findPkgPackAgeDtl(null, firmId, packAgeId);
			return listItemPrgpackAgedtl;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 新增时间桌台
	 * @return
	 */
	public static String saveDeskTimes( DeskTimes deskTimes){
		try {
			String cont=orderMapper.saveDeskTimes(deskTimes);
			return cont;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获取热门菜品
	 * @return
	 */
	public static List<Project> findHotItem(String firmId){
		try {
			List<Project> listProject=orderMapper.findHotItem(firmId);
			return listProject;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 修改账单备注
	 * @return
	 */
	public static String updateOrders(String id,String remark){
		try {
			String cont=orderMapper.updateOrders(id, remark);
			return cont;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 修改账单状态
	 * @return
	 */
	public static String updateOrderState(Net_Orders orders){
		try {
			String cont=orderMapper.updateOrderState(orders);
			return cont;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 生成账单消费记录
	 * @return
	 */
	public static String orderOutAmt(Net_Orders orders){
		try {
			String cont=orderMapper.orderOutAmt(orders);
			return cont;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 通过门店查询优惠信息
	 * @return
	 */
	public static List<WebMsg> findStoreWebMsg(String firmId){
		try {
			List<WebMsg> listWebMsg=orderMapper.findStoreWebMsg(firmId);
			return listWebMsg;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 通过扫码获取台位信息
	 * @return
	 */
	public static List<StoreTable> findRestTbl(String scene_id){
		try {
			List<StoreTable> listStoreTable=orderMapper.findRestTbl(scene_id);
			return listStoreTable;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 通过扫码获取台位信息页面返回
	 * @return
	 */
	public static String getStoreTable(String scene_id){
		try {
			List<StoreTable> listStoreTable=orderMapper.findRestTbl(scene_id);
			StoreTable sto=listStoreTable.get(0);
			return sto.getTbl();
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 查询账单信息
	 * @Description:
	 * @Title:queryfolio
	 * @Author:dwh
	 * @Date:2014-12-26 上午9:29:46 	
	 * @param resvno
	 * @param state
	 * @param firmid
	 * @param dat
	 * @return
	 */
	 public static List<Net_Orders> queryfolio(String resvno,String state,String firmid,String dat){
		try {
			List<Net_Orders> listOrders=orderMapper.queryfolio(resvno, state, firmid, dat);
			return listOrders;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
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
		public static String updateOrdr(String openid, String firmid, String tableid){
			try{
				String orderid = MessageUtil.getScanEventMap().get(openid + "_orderid");
				String firmCode = MessageUtil.getScanEventMap().get(openid + "_firmCode");
				String pax = MessageUtil.getScanEventMap().get(openid + "_pax");
				Net_Orders orders = new Net_Orders();
				orders.setId(orderid);
				orders.setTables(tableid);
				orders.setVcode(firmCode);
				//orders.setState("7");
				orders.setPax(pax);
				orders.setFirmid(firmid);
				orders.setDat(DateFormat.getStringByDate(new Date(), "yyyy-MM-dd"));
				orderMapper.updateOrdr(orders);
				
				String mustPayBeforeOrder = Commons.getConfig().getProperty("mustPayBeforeOrder");
				if("Y".equals(mustPayBeforeOrder)) {
					// 如果是先支付，仅更新订单台位号
					return "1";
				}
				
				// 查询订单信息
				List<Net_Orders> listNet_Orders = orderMapper.getOrderMenus(orders.getId(), null, null, null, null, null);
				if (listNet_Orders != null && !listNet_Orders.isEmpty()) {
					orders = listNet_Orders.get(0);
				}
				//查询账单明细
				List<Net_OrderDtl> orderDtl = PubitemSearch.getOrderDtlMenus(orders.getId());
				
				// 查询菜品门店编码
				List<FdItemSale> listPubItemCode = orderMapper.getListPubItemCode(orders.getFirmid());
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
				List<NetDishAddItem> listNetDishAddItem = orderMapper.getDishAddItemList(orders.getPk_group(), orders.getId());
				// 获取菜品附加产品列表
				List<NetDishProdAdd> listNetDishProdAdd = orderMapper.getDishProdAddList(orders.getPk_group(), orders.getId());
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
						temp.setUnit(item.getUnit());
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
					dtl.setPcode(mapPubItemCode.get(dtl.getFoodsid()));
//					for(FdItemSale item : listPubItemCode) {
//						if(dtl.getFoodsid().equals(item.getId())) {
//							dtl.setPcode(item.getItcode());
//							break;
//						}
//					}
					
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
					orderMapper.addOpenItem(addOrder);
					
					// 判断订单中是否已包含开台菜品
					boolean itemExist = false;
					for(Net_OrderDtl dtl : orderDtl) {
						for(Net_OrderDtl subDtl : addOrder.getListNetOrderDtl()) {
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
						for(Net_OrderDtl subDtl : addOrder.getListNetOrderDtl()) {
							totalPrice += Double.parseDouble(subDtl.getTotalprice());
							
							// 设置菜品的门店编码
							for(FdItemSale item : listPubItemCode) {
								if(subDtl.getFoodsid().equals(item.getId())) {
									subDtl.setPcode(item.getItcode());
									break;
								}
							}
							
						}
						orders.setSumprice(String.valueOf(totalPrice));
						orderMapper.updateOrdr(orders);
						orderMapper.saveOrderDtl(addOrder);
						orders.getListNetOrderDtl().addAll(addOrder.getListNetOrderDtl());
					}
				}

				orders.setVcode(firmCode);
				orders.setSumprice(String.valueOf(totalPrice));
				orders.setSerialid(CodeHelper.createUUID());
				orders.setType("1");
				//记录本次记录
				Map<String,Object> mqMap = new HashMap<String,Object>();
				mqMap.put("pk_mqlogs", orders.getSerialid());
				mqMap.put("orderid", orders.getId());
				mqMap.put("vtype", "1");
				mqMap.put("errmsg", "");
				mqMap.put("state", "");
				orderMapper.addMqLogs(mqMap);
				//发送mq消息
				orders.setSerialid(mqMap.get("pk_mqlogs")+"");//重置mq记录的主键
				JSONObject json = JSONObject.fromObject(orders);
				LogUtil.writeToTxt(LogUtil.MQSENDORDER, "下单到MQ服务器中，队列名："+firmCode+"_orderList，订单对象："+json.toString());
				MqSender.sendOrders(json.toString(),firmCode+"_orderList",Commons.mqEffectiveTime);//设置消息有效期为半小时;
				
				// 如果状态是2：已支付，推送支付消息
				if("2".equals(orders.getState())) {
					Map<String,Object> jsonMap = new HashMap<String,Object>();//mq发送信息map对象
					jsonMap.put("serialid", orders.getId());
					jsonMap.put("resv", orders.getResv());
					jsonMap.put("tables", orders.getTables());
					jsonMap.put("vcode", firmCode);
					jsonMap.put("dat", orders.getDat());
					jsonMap.put("type", "2");
					
					// 判断是否开发票
					if(StringUtils.hasText(orders.getVinvoicetitle())) {
						jsonMap.put("visopeninvoice", "Y");//是否开发票
						jsonMap.put("vinvoicetitle", orders.getVinvoicetitle());//发票抬头
						jsonMap.put("invoicemoney", orders.getPaymoney());//发票金额
					} else {
						jsonMap.put("visopeninvoice", "N");//是否开发票
						jsonMap.put("vinvoicetitle", "");//发票抬头
						jsonMap.put("invoicemoney", "");//发票金额
					}
					
					List<Map<String,String>> list = new ArrayList<Map<String,String>>();//结算方式list对象
					Map<String, String> map = new HashMap<String, String>();
					map.put("couponid", Commons.vwxpaycode);
					map.put("couponNum", "1");
					map.put("couponMoney", orders.getPaymoney());
					map.put("type", "2");
					// 增加交易流水号，用于pos退款  20151221
					map.put("weChatPaySerialid", orders.getVtransactionid());
					map.put("out_trade_no", orders.getOutTradeNo());
					list.add(map);
					
					jsonMap.put("xiaofeiList", list);
					
					// 推送结帐信息
					MqSender.sendOrders(JSONObject.fromObject(jsonMap).toString(), firmCode + "_orderList", Commons.mqEffectiveTime);
					LogUtil.writeToTxt(LogUtil.MQSENDORDER, "发送结算信息："+JSONObject.fromObject(jsonMap).toString());
				}
			
				return "1";
			} catch (Exception e) {
				Net_Orders orders = new Net_Orders();
				String orderid = MessageUtil.getScanEventMap().get(openid + "_orderid");
				orders.setId(orderid);
				orders.setState("1");
				orderMapper.updateOrdr(orders);
				e.printStackTrace();
			}
			return "12";
		}

		public static void addRelationShipFirmOpenid(String appid,String openid,String firmid){
				try {
					Cookie cookie = new Cookie();
					cookie.setAppid(appid);
					cookie.setOpenid(openid);
					cookie.setCookie(firmid);
					cookieMapper.addLoginInfoCookie(cookie);
				} catch (Exception e) {
					logger.error(e);
					e.printStackTrace();
				}
			}

	/**
	 * 获取菜品门店编码
	 * 
	 * @param firmid
	 * @return
	 */
	public static List<FdItemSale> getListPubItemCode(String firmid) {
		try {
			List<FdItemSale> listPubItemCode = orderMapper.getListPubItemCode(firmid);
			return listPubItemCode;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 等位关联手机号与openid
	 * 
	 * @param firmid
	 * @return
	 */
	public static String setTeleWithOpenid(String QrcodeReturnStr,String openid) {
		try {
			String[] strs = QrcodeReturnStr.split("_");
			String seq="";
			if(strs.length==2){
				seq = strs[1];
			}else{
				seq = strs[0];
			}
			
			String res = WeChatUtil.httpRequestReturnString(Commons.BOHUrl+"waitSeat/updateSeat.do", "POST", "seq="+seq+"&wechat="+openid);
			System.out.println("===================="+res);
			return res;
//			cookieMapper.setTeleWithOpenid(telephone,openid);
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 查询订单菜品套餐明细
	 * @param dtl
	 * @return
	 */
	 public static List<Map<String, Object>> listFolioPacageDtl(Net_OrderDtl dtl) throws Exception{
		 try {
				List<Map<String, Object>> listFolioPacageDtl=orderMapper.listFolioPacageDtl(dtl);
				return listFolioPacageDtl;
			} catch (Exception e) {
				logger.error(e);
				e.printStackTrace();
			}
			return null;
		}
		/**
		 * 获取门店通过firmId
		 * @return
		 * @throws Exception 
		 */
		public static Map<String,Object> getStoreSeatInfo(String firmId,String tableid){
			List<Map<String,Object>> listStoreSeat=orderMapper.getStoreSeatInfo(firmId, tableid);
			if(listStoreSeat!=null && listStoreSeat.size()>0){
				return listStoreSeat.get(0);
			}else{
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("pk_sited","");
				map.put("vcode",tableid);
				map.put("vname",tableid);
				return map;	
			}
		}
		/**
		 * 根据firmid查询门店是否可扫码点餐
		 * // CBOH_STORE_3CH	NETSHOW = Y
		 * @throws SQLException 
		 * */
		public static String isWxchatDianCan(String firmid){
			String flag = "Y";
			Connection conn = null;
			Statement st = null;
			ResultSet rs = null;
			String sql = "select netshow from cboh_store_3ch where pk_store='"+firmid+"'";
			try{
				conn = new JdbcConnection().getCRMConnection();
				st = conn.createStatement();
				rs = st.executeQuery(sql);
				while(rs.next()){
					flag = rs.getString("NETSHOW");
				}
				if(st!=null){
					st.close();
				}
				if(conn!=null){
					conn.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
//			System.out.println("是否网站显示：======"+flag);
			
			return flag;
		}
}
