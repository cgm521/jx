package com.choice.test.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.choice.test.constants.PubitemConstants;
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
import com.choice.test.domain.StoreTable;
import com.choice.test.domain.WebMsg;
import com.choice.test.service.PubitemSearch;
import com.choice.test.utils.CodeHelper;
import com.choice.test.utils.Commons;
import com.choice.test.utils.DateFormat;
import com.choice.test.utils.FirstLetter;
import com.choice.test.utils.GenerateRandom;
import com.wxap.util.PayUtil;

/**
 * 菜品
 * @author 孙胜彬
 */
@Controller
@RequestMapping(value="pubitem")
public class PubitemController {
	/**
	 * 进入在线点菜页面
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value="onlineBook")
	public ModelAndView onlineBook(ModelMap modelMap, String code,String state){
		try {
			String openid = CodeHelper.getOpenID(Commons.appId, Commons.secret, code);
			//城市
			List<City> listCity=PubitemSearch.getCity();
			modelMap.put("listCity", listCity);
			modelMap.put("openid", openid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(PubitemConstants.ONLINEBOOK,modelMap);
	}
	/**
	 * 根据城市选择门店
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value="getStore")
	@ResponseBody
	public Object getStore(ModelMap modelMap, String area){
		try {
			//城市
			List<Firm> listFirm=PubitemSearch.getStore(area);
			return listFirm;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获取菜品类别
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value="listPubitem")
	public ModelAndView listPubitem(ModelMap modelMap, Net_Orders orders){
		try {
			List<ProjectType> listProjectType=PubitemSearch.getPubitemTyp(orders.getFirmid());
			if(orders.getId() !=null && !orders.getId().equals("")){
				orders.setListNetOrderDtl(PubitemSearch.getOrderDtlMenus(orders.getId()));
				List<Net_Orders> listNet_Orders=PubitemSearch.getOrderMenus(orders.getId(),orders.getOpenid(),null,null,null);
				if(listNet_Orders.size()!=0){
					orders.setRemark(listNet_Orders.get(0).getRemark());
				}
			}
			String count = PubitemSearch.flagWebMsg(orders.getFirmid());
			if(count.equals("0")){//判断是否有套餐
				modelMap.put("count", "");
			}else{
				modelMap.put("count", "精美套餐");
			}
			modelMap.put("listProjectType", listProjectType);
			modelMap.put("orders", orders);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(PubitemConstants.MENULIST,modelMap);
	}
	
	/**
	 * 根据门店获取套餐
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value="listPackages")
	@ResponseBody
	public Object listPackages(ModelMap modelMap,String firmId){
		try {
			List<ItemPrgPackage> listPackage=PubitemSearch.findItemPrgPackage(firmId);
			modelMap.put("listPackage", listPackage);
			return  listPackage;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  null;
	}
	
	/**
	 * 根据门店,套餐ID查询套餐明细
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value="findPackageDetail")
	@ResponseBody
	public Object findPackageDetail(ModelMap modelMap,String firmId,String packageId){
		try {
			List<ItemPrgpackAgedtl> packageDetail=PubitemSearch.findItemPrgpackAgedtl(firmId,packageId);
			modelMap.put("packageDetail", packageDetail);
			return packageDetail;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  null;
	}
	/**
	 * 查询所有菜品
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value="getAllPubitem")
	@ResponseBody
	public Object getAllPubitem(ModelMap modelMap,String firmId){
		try {
			List<Project> listProject=PubitemSearch.getAllPubitem(firmId);
			modelMap.put("listProject", listProject);
			return  listProject;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  null;
		//new ModelAndView(PubitemConstants.LISTPUBITEMDTL,modelMap);
	}
	/**
	 * 查询热门菜品
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value="getHotPubitem")
	@ResponseBody
	public Object getHotPubitem(ModelMap modelMap,String firmId){
		try {
			List<Project> listProject=PubitemSearch.getHotPubitem(firmId);
			modelMap.put("listProject", listProject);
			return  listProject;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  null;
		//new ModelAndView(PubitemConstants.LISTPUBITEMDTL,modelMap);
	}
	/**
	 * 按名字查询
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value="getPubitemByName")
	@ResponseBody
	public Object getPubitemByName(ModelMap modelMap,ProjectType project){
		try {
			List<Project> listProject=PubitemSearch.getPubitemByName(project.getDes(), project.getFirmid());
			modelMap.put("listProject", listProject);
			return  listProject;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  null;
		//new ModelAndView(PubitemConstants.LISTPUBITEMDTL,modelMap);
	}
	
	/**
	 * 获取菜品类别
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value="listPubitemDtlHtml")
	public ModelAndView listPubitemDtlHtml(ModelMap modelMap,Net_Orders orders){
		try {
			List<ProjectType> listProjectType=PubitemSearch.getPubitemTyp(orders.getFirmid());
			modelMap.put("listProjectType", listProjectType);
			modelMap.put("id", orders.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(PubitemConstants.LISTPUBITEMDTL,modelMap);
	}
	
	/**
	 * 我的预定
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value="enterMyOrder")
	public ModelAndView enterMyOrder(ModelMap modelMap, String openid){
		try {
			List<Net_Orders> listNet_Orders=PubitemSearch.getOrderCity(openid);
			modelMap.put("listNet_Orders", listNet_Orders);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(PubitemConstants.LISTMYORDERCITY,modelMap);
	}
	/**
	 * 订桌基本信息
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value="listOrderFirst")
	public ModelAndView listOrderFirst(ModelMap modelMap, String code,String state){
		try {
			String openid = CodeHelper.getOpenID(Commons.appId, Commons.secret, code);
			//城市
			List<City> listCity=PubitemSearch.getCity();
			//餐次
//			List<Sft> listSft=PubitemSearch.getSft();
			modelMap.put("listCity", listCity);
//			modelMap.put("listSft", listSft);
			modelMap.put("openid", openid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(PubitemConstants.LISTORDERFIRST,modelMap);
	}
	/**
	 * 选择门店
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value="choiceFirm")
	public ModelAndView choiceFirm(ModelMap modelMap, String city, String dat, String sft, String openid){
		try {
			//门店列表
			List<Firm> listFirm=PubitemSearch.getStore(city);
			for(int i=0;i<listFirm.size();i++){
				Firm frim=listFirm.get(i);
				List<StoreTable> listStoreTable=PubitemSearch.findTable(sft,dat,frim.getFirmid());
				frim.setListStoreTable(listStoreTable);
			}
			//处理用于页面查询
			Map<String,List<Firm>> mapFirm = new HashMap<String,List<Firm>>();
			//Collections.sort(listFirm);
			for(Firm firm:listFirm){
				String firstLetter = FirstLetter.getFirstLetter(firm.getFirmdes());
				if(mapFirm.containsKey(firstLetter)){
					List<Firm> pListFirm = mapFirm.get(firstLetter);
					pListFirm.add(firm);
				}else{
					List<Firm> pListFirm = new ArrayList<Firm>();
					pListFirm.add(firm);
					mapFirm.put(firstLetter, pListFirm);
				}
			}
			//排序
			List<Map.Entry<String, List<Firm>>> infoIds =
				    new ArrayList<Map.Entry<String, List<Firm>>>(mapFirm.entrySet());
			for(int i=0;i<infoIds.size();i++){
				System.out.println(infoIds.get(i).getKey().toString());
			}
			Collections.sort(infoIds, new Comparator<Map.Entry<String,List<Firm>>>() {   
			    public int compare(Map.Entry<String, List<Firm>> o1, Map.Entry<String, List<Firm>> o2) {      
			        //return (o2.getValue() - o1.getValue()); 
			        return (o1.getKey()).toString().compareTo(o2.getKey());
			    }
			});
			/*System.out.println("排序后");
			for(int i=0;i<infoIds.size();i++){
				System.out.println(infoIds.get(i).getKey().toString());
			}*/
			modelMap.put("listFirm", listFirm);
			modelMap.put("mapFirm", infoIds);
			modelMap.put("dat",dat);
			modelMap.put("sft",sft);
			modelMap.put("openid", openid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(PubitemConstants.LISTFIRM,modelMap);
	}
	
	/**
	 * 选择台位
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value="choiceDesk")
	public ModelAndView choiceDesk(ModelMap modelMap, String firmId, String dat, String sft, String openid){
		try {
			//查询分店
			Firm firm = PubitemSearch.getStoreByFirmId(firmId);
			//根据餐次时间分店查询可预订桌台
			List<StoreTable> listStoreTable=PubitemSearch.findTable(sft,dat,firmId);
			firm.setListStoreTable(listStoreTable);
			modelMap.put("firm",firm);
			modelMap.put("dat",dat);
			modelMap.put("sft",sft);
			modelMap.put("openid", openid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(PubitemConstants.CHOICEDESK,modelMap);
	}
	
	/**
	 * 获取可预订包间
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value="listRooms")
	@ResponseBody
	public Object listRooms(HttpServletResponse response, ModelMap modelMap,StoreTable storeTable, String sft, String dat){
		List<StoreTable> listRooms=new ArrayList<StoreTable>();
		try {
			listRooms=PubitemSearch.findResbTbl(storeTable.getRoomtyp(), sft, dat, storeTable.getFirmid(), null);
			return listRooms;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 确定订位
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value="confirmOrder")
	public ModelAndView confirmOrder(ModelMap modelMap, String firmId, String dat, String sft, String pax, String openid, String roomtyp, String roomId, String des){
		try {
			Firm firm = PubitemSearch.getStoreByFirmId(firmId);
			modelMap.put("firm",firm);
			modelMap.put("dat",dat);
			modelMap.put("sft",sft);
			modelMap.put("pax",pax);
			modelMap.put("openid", openid);
			modelMap.put("roomtyp", roomtyp);
			modelMap.put("roomId", roomId);
			modelMap.put("des", des);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(PubitemConstants.CONFIRMORDER,modelMap);
	}
	
	/***
	 * 
	 * @param modelMap
	 * @param orders
	 * @param roomtyp
	 * @param deskTime
	 * @return
	 */
	@RequestMapping(value="isOrder")
	@ResponseBody
	public String isOrder(ModelMap modelMap, Net_Orders orders, String roomId){
		try {
			//大厅自动分配桌台
			if("大厅".equals(orders.getFirmdes())){
				List<StoreTable> listDesks = PubitemSearch.findResbTbl("大厅", orders.getSft(), orders.getDat(), orders.getFirmid(), orders.getPax());
				if(listDesks.size()==0){
					return "0";//大厅的桌台已经被预定光
				}else{
					return "1";
				}
			}else{
				//判断包间是否被抢先预定
				List<DeskTimes> listDeskTime = PubitemSearch.getDeskTimes(roomId,orders.getSft(),orders.getDat());
				if(listDeskTime.size()>0){
					return "0";//包间的桌台被预定光
				}else{
					return "1";
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 提交订单
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value="createOrder")
	public ModelAndView createOrder(ModelMap modelMap, Net_Orders orders, String roomtyp, DeskTimes deskTime,String realPax){
		try {
			String resvtblId = deskTime.getResvtblid();//用于查询桌台是否被预定
			//大厅自动分配桌台
			if("大厅".equals(roomtyp)){
				List<StoreTable> listDesks = PubitemSearch.findResbTbl("大厅", orders.getSft(), orders.getDat(), orders.getFirmid(), realPax);
				if(listDesks.size()==0){
					modelMap.put("msg", "抱歉，您预定的桌台已被抢先预定，请选择其他桌台！");
					modelMap.put("sft", orders.getSft());
					modelMap.put("openid", orders.getOpenid());
					modelMap.put("firmid", orders.getFirmid());
					modelMap.put("dat",orders.getDat());
					return new ModelAndView(PubitemConstants.FAILEDPAGE,modelMap);
				}
				//随机抽取一个桌台
				Integer custIndex = new Random().nextInt(listDesks.size());
				StoreTable table = listDesks.get(custIndex);
				orders.setTables(table.getDes());
				deskTime.setResvtblid(table.getId());
				resvtblId = table.getId();
			}
			//判断桌台是否被抢先预定
			List<DeskTimes> listDeskTime = PubitemSearch.getDeskTimes(resvtblId,orders.getSft(),orders.getDat());
			if(listDeskTime.size()>0){
				modelMap.put("msg", "抱歉，您预定的桌台已被抢先预定，请选择其他桌台！");
				
				modelMap.put("sft", orders.getSft());
				modelMap.put("openid", orders.getOpenid());
				modelMap.put("firmid", orders.getFirmid());
				modelMap.put("dat",orders.getDat());
				return new ModelAndView(PubitemConstants.FAILEDPAGE,modelMap);
			}
			
			//订单id
			String id=CodeHelper.createUUID();
			//保存时间桌台信息
			deskTime.setId(CodeHelper.createUUID());
			deskTime.setOrdersid(id);
			deskTime.setState("1");//有效
			
			//生成订单
			String resv=DateFormat.getStringByDate(new Date()  , "yyMMddHHmmss")+(new java.util.Random().nextInt(90)+10);
			orders.setId(id);
			orders.setResv(resv);
			orders.setIsfeast("1");//订桌订单
			orders.setRannum(resv+GenerateRandom.getRanNum(6));
			//保存时间桌台
			PubitemSearch.saveDeskTimes(deskTime);
			//保存订单
			PubitemSearch.saveOrder(orders);
			
			modelMap.put("orderid", id);
			modelMap.put("openid", orders.getOpenid());
			modelMap.put("firmid", orders.getFirmid());
			modelMap.put("remark", orders.getRemark());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(PubitemConstants.SUCCESSPAGE,modelMap);
	}
	/**
	 * 获取台位
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value="listStoreTable")
	@ResponseBody
	public Object listStoreTable(ModelMap modelMap,StoreTable storeTable){
		List<StoreTable> listStoreTable=new ArrayList<StoreTable>();
		try {
			listStoreTable.clear();
			listStoreTable=PubitemSearch.getTs(storeTable.getArea());
			modelMap.put("listStoreTable", listStoreTable);
			return listStoreTable;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 分享给好友
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value="listShareToFriend")
	public ModelAndView listShareToFriend(ModelMap modelMap){
		return new ModelAndView(PubitemConstants.LISTSHARETOFRIEND,modelMap);
	}
	/**
	 * 菜品详情
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value="listPubitemDtl")
	@ResponseBody
	public Object listPubitemDtl(ModelMap modelMap,String projecttyp,String firmId){
		try {
			List<Project> listProject=PubitemSearch.getPubitem(projecttyp,firmId);
			modelMap.put("listProject", listProject);
			return  listProject;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  null;
		//new ModelAndView(PubitemConstants.LISTPUBITEMDTL,modelMap);
	}
	
	/**
	 * 我的订单所属门店
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value="getOrderFirm")
	public ModelAndView getOrderFirm(ModelMap modelMap, String code,String state){
		try {
			String openid = CodeHelper.getOpenID(Commons.appId, Commons.secret, code);
			List<Net_Orders> listNet_Orders=PubitemSearch.getOrderCity(openid);
			modelMap.put("listNet_Orders", listNet_Orders);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(PubitemConstants.LISTMYORDERCITY,modelMap);
	}
	
	/**
	 * 我的订单
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value="getOrderMenus")
	public ModelAndView getOrderMenus(ModelMap modelMap, String firmid,String openid,String dat,String dateTime){
		try {
			List<Net_Orders> listNet_Orders=PubitemSearch.getOrderMenus(null,openid,firmid,dat,dateTime);
			if(null == listNet_Orders ||listNet_Orders.size() != 0){
				for(int i =0; i < listNet_Orders.size();i++){
//					listNet_Orders
					String orderid=listNet_Orders.get(i).getId();
					List<Net_OrderDtl> listNet_OrderDtl=PubitemSearch.getOrderDtlMenus(orderid);
					listNet_Orders.get(i).setListNetOrderDtl(listNet_OrderDtl);
//					modelMap.put("listNet_OrderDtl"+i, listNet_OrderDtl);
				}
//				String orderid=listNet_Orders.get(0).getId();
//				List<Net_OrderDtl> listNet_OrderDtl=PubitemSearch.getOrderDtlMenus(orderid);
//				modelMap.put("listNet_OrderDtl", listNet_OrderDtl);
			}
			modelMap.put("listNet_Orders", listNet_Orders);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(PubitemConstants.LISTMYORDER,modelMap);
	}
	/**
	 * 我的订单详情
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value="getOrderDtlMenus")
	public ModelAndView getOrderDtlMenus(ModelMap modelMap,String orderid,String resv,String openid,String firmid){
		try {
			List<Net_OrderDtl> listNet_OrderDtl=PubitemSearch.getOrderDtlMenus(orderid);
			modelMap.put("listNet_OrderDtl", listNet_OrderDtl);
			modelMap.put("resv", resv);
			modelMap.put("orderid", orderid);
			modelMap.put("openid", openid);
			modelMap.put("firmid", firmid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(PubitemConstants.LISTORDERDTL,modelMap);
	}
	/**
	 * 预定点菜
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value="saveOrder")
	@ResponseBody
	public String saveOrder(ModelMap modelMap,Net_Orders orders){
		try {
			String resv=DateFormat.getStringByDate(new Date(),"yyMMddHHmmss")+(new java.util.Random().nextInt(90)+10);
			String id=CodeHelper.createUUID();
			orders.setId(id);
			orders.setResv(resv);
			orders.setRannum(resv+GenerateRandom.getRanNum(6));
			PubitemSearch.saveOrder(orders);
			return id;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 我的点菜
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value="sessionOrder")
	public ModelAndView sessionOrder(ModelMap modelMap,Net_Orders orders){
		try {
			List<String> idList=Arrays.asList(orders.getPubitem().split(","));
			List<String> list=new ArrayList<String>();
			List<Project> listProject=new ArrayList<Project>();
			for(int i=0; i<idList.size(); i++){             
				String str = idList.get(i);  //获取传入集合对象的每一个元素             
				if(!list.contains(str)){
					//查看新集合中是否有指定的元素，如果没有则加入                 
					list.add(str);             
				}         
			}
			for(int j=0;j<list.size();j++){
//				Project project=PubitemSearch.getCodePubitem(list.get(j)).get(0);
//				listProject.add(project);
			}
			modelMap.put("listProject", listProject);
			modelMap.put("id", orders.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(PubitemConstants.LISTMYORDERDTL,modelMap);
	}
	/**
	 * 保存菜单详情
	 * @param orders
	 * @return
	 */
	@RequestMapping(value="saveOrderDtl")
	@ResponseBody
	public String saveOrderDtl(Net_Orders orders){
		try{
			PubitemSearch.updateOrders(orders.getId(), orders.getRemark());
			List<Net_OrderDtl> listOrdDtl=orders.getListNetOrderDtl();
			PubitemSearch.deleteOrderDtlByResv(orders.getId());
			for(int i=0;i<listOrdDtl.size();i++){
				Net_OrderDtl ordersDtl=new Net_OrderDtl();
				ordersDtl=orders.getListNetOrderDtl().get(i);
				ordersDtl.setId(CodeHelper.createUUID());
				PubitemSearch.saveOrderDtl(ordersDtl);
			}
			return "1";
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 取消订单
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value="deleteOrder")
	@ResponseBody
	public String deleteOrder(ModelMap modelMap,String resv,String id){
		try {
			String cont=PubitemSearch.deleteOrder(resv,id);
			return cont;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 删除菜品
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value="deleteOrderDtl")
	@ResponseBody
	public String deleteOrderDtl(ModelMap modelMap,String id){
		try {
			List<String> idList=Arrays.asList(id.split(","));
			String cont="";
			for(int i=0;i<idList.size();i++){
				cont=PubitemSearch.deleteOrderDtl(idList.get(i));
			}
			return cont;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 保存菜品
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value="saveOrderOrDtl")
	@ResponseBody
	public String saveOrderOrDtl(ModelMap modelMap,Net_Orders orders){
		try {
			String resv=DateFormat.getStringByDate(new Date(), "yyMMddHHmmss")+(new java.util.Random().nextInt(90)+10);
			String id=CodeHelper.createUUID();
			orders.setId(id);
			orders.setResv(resv);
			orders.setIsfeast("0");
			orders.setRannum(resv+GenerateRandom.getRanNum(6));
			for(int i=0;i<orders.getListNetOrderDtl().size();i++){
				orders.getListNetOrderDtl().get(i).setId(CodeHelper.createUUID());
				orders.getListNetOrderDtl().get(i).setOrdersid(id);
			}
			String cont=PubitemSearch.saveOrdersOrDtl(orders);
			return cont;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/************************************优惠信息**************************************/
	/**
	 * 优惠信息区域
	 * @param modelMap
	 * @param favorArea
	 * @return
	 */
	@RequestMapping(value="findFavorArea")
	public ModelAndView findFavorArea(ModelMap modelMap, String code,String state){
		try {
			List<FavorArea> listFavorArea=PubitemSearch.findFavorArea(null);
			modelMap.put("listFavorArea", listFavorArea);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(PubitemConstants.FAVORAREA,modelMap);
	}
	/**
	 * 优惠信息
	 * @param modelMap
	 * @param favorArea
	 * @return
	 */
	@RequestMapping(value="findWebMsg")
	public ModelAndView findWebMsg(ModelMap modelMap,String favorAreaId,String firmId){
		try {
			List<WebMsg> listWebMsg=PubitemSearch.findWebMsg(null, null, favorAreaId);
			List<Firm> listFirm=PubitemSearch.findStoreByFirmId(firmId);
			modelMap.put("listWebMsg", listWebMsg);
			modelMap.put("listFirm", listFirm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(PubitemConstants.FAVORCONT,modelMap);
	}
	
	/**
	 * 微信结算账单
	 */
	@RequestMapping(value="findConfigPay")
	public ModelAndView findConfigPay(ModelMap modelMap,String tbl,String resv,String pax,String dat){
		modelMap.put("tbl", tbl);
		modelMap.put("resv", resv);
		modelMap.put("pax", pax);
		modelMap.put("dat", dat);
		return new ModelAndView(PubitemConstants.CONFIGPAY,modelMap);
	}
	
	/**
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value="gotoPay")
	public ModelAndView gotoPay(ModelMap modelMap){
		try{
			
			List<Net_OrderDtl> listNetOrderDtl=new ArrayList<Net_OrderDtl>();
			for(int i=0;i<2;i++){
				Net_OrderDtl orderDtl=new Net_OrderDtl();
				orderDtl.setFoodsid("bb40025dabaf4445853a08099efc1b25");
				orderDtl.setFoodnum("2");
				orderDtl.setPrice("50");
				orderDtl.setTotalprice("100");
				orderDtl.setFoodsname("竹荪莼菜汤");
				orderDtl.setIspackage("0");
				listNetOrderDtl.add(orderDtl);
			}
			double outAmt = 0;
			for(Net_OrderDtl orderDtl : listNetOrderDtl){
				outAmt += Double.parseDouble(orderDtl.getTotalprice());
			}
			String resv=DateFormat.getStringByDate(new Date()  , "yyMMddHHmmss")+(new java.util.Random().nextInt(90)+10);
			//账单需要的基本信息
			modelMap.put("listNetOrderDtl", listNetOrderDtl);
			modelMap.put("resv", resv);
			modelMap.put("tables", "102");
			modelMap.put("pax", "4");
			modelMap.put("dat", "2014-03-26");
			modelMap.put("sft", "1");
			modelMap.put("contact", "15888888888");
			modelMap.put("firmid", "32");
			modelMap.put("firmdes", "北京辰森世纪大酒店");
			modelMap.put("payment", "1");
			modelMap.put("addr", "北京辰森世纪大酒店济南店");
			
			modelMap.put("printName","孙胜彬");//印单人员
			modelMap.put("quotaDiscount", 0);//定额折扣
			modelMap.put("delLittleNum", 0);//抹零
			/*最后要传的参数*/
			modelMap.put("cardNo", "");
			modelMap.put("outAmt", outAmt);//扣款金额
			modelMap.put("firmId", "7");
			modelMap.put("date", "2014-03-26");
			modelMap.put("invoiceAmt", 0);//发票额
			modelMap.put("empName", "王吉峰");//开单人员
		}catch(Exception e){
			e.printStackTrace();
		}
		return new ModelAndView(PubitemConstants.PAYMENU,modelMap);
	}
	/***
	 * 
	 */
	@RequestMapping(value="yespay")
	@ResponseBody
	public String yespay(ModelMap modelMap,Net_Orders orders){
		try{
			orders.setState("6");
//			String state=PubitemSearch.updateOrderState(orders);
//			if(state !=null && !state .equals("")){
				String id=CodeHelper.createUUID();
				orders.setId(id);
				for(int i=0;i<orders.getListNetOrderDtl().size();i++){
					orders.getListNetOrderDtl().get(i).setId(CodeHelper.createUUID());
					orders.getListNetOrderDtl().get(i).setOrdersid(id);
				}
				String cont=PubitemSearch.orderOutAmt(orders);
				return cont;
//			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 微信扫描二维码点菜功能
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value="listTakeOrdr")
	public ModelAndView listTakeOrdr(ModelMap modelMap,String scene_id,String openid){
		try {
//			List<StoreTable> storeTable=PubitemSearch.findRestTbl("1");
			List<StoreTable> storeTable=PubitemSearch.findRestTbl(scene_id);
			StoreTable table = null;
			
			if(null!=storeTable && storeTable.size()>0){
				table=storeTable.get(0);
			}
			Net_Orders orders =new Net_Orders();
			String date = DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm");
			String time = date.substring(11);
			date = date.substring(0,10);
			orders.setDat(date);//预订日期
			orders.setDatmins(time);//预订时间
			orders.setTables(table.getTbl());//预订台位
			orders.setFirmid(table.getFirmid());//预订门店
//			orders.setOpenid("12312312312");
			orders.setOpenid(openid);
			List<ProjectType> listProjectType=PubitemSearch.getPubitemTyp(orders.getFirmid());
//			if(orders.getId() !=null && !orders.getId().equals("")){
//				orders.setListNetOrderDtl(PubitemSearch.getOrderDtlMenus(orders.getId()));
//				List<Net_Orders> listNet_Orders=PubitemSearch.getOrderMenus(orders.getId(),orders.getOpenid(),null,null,null);
//				if(listNet_Orders.size()!=0){
//					orders.setRemark(listNet_Orders.get(0).getRemark());
//				}
//			}
			String count = PubitemSearch.flagWebMsg(orders.getFirmid());
			if(count.equals("0")){//判断是否有套餐
				modelMap.put("count", "");
			}else{
				modelMap.put("count", "精美套餐");
			}
			modelMap.put("count1", "优惠活动");
			modelMap.put("listProjectType", listProjectType);
			modelMap.put("orders", orders);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(PubitemConstants.TAKEORDR,modelMap);
	}
	/**
	 * 微信扫码点菜当前门店的优惠信息
	 * @param modelMap
	 * @param firmId
	 * @return
	 */
	@RequestMapping(value="findStoreWebMsg")
	@ResponseBody
	public Object findStoreWebMsg(ModelMap modelMap,String firmId){
		try {
			List<WebMsg> listWebMsg=PubitemSearch.findStoreWebMsg(firmId);
			return listWebMsg;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 组装微信支付需要相关的信息
	 * @Description:
	 * @Title:jspay
	 * @Author:dwh
	 * @Date:2014-12-19 下午8:01:02 	
	 * @param modelMap
	 * @param request 
	 * @param response 
	 * @param paymoney 支付价额
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="jspay")
	@ResponseBody
	public Object jspay(ModelMap modelMap,HttpServletRequest request,  
            HttpServletResponse response,String paymoney)throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		paymoney = Double.valueOf(Double.valueOf(paymoney)*100).intValue()+"";
		TreeMap<String, String> goodsInfo = new TreeMap<String, String>();
		goodsInfo.put("body", "");
		goodsInfo.put("total_fee", paymoney); //商品总金额,以分为单位
		String packegeValue = PayUtil.packageValueInfo(request, response, goodsInfo);
		String sign = PayUtil.getSign();
		String timestamp = PayUtil.getTimestamp();
		String nonce = PayUtil.getNonceStr();
		map.put("APPID", Commons.appId);
		map.put("PACKEGEVALUE", packegeValue);
		map.put("SIGN", sign);
		map.put("TIMESTAMP",timestamp);
		map.put("NONCE",nonce);
		return map;
	}
	
	/**
	 * 微信支付测试
	 * @Description:
	 * @Title:testjsapi
	 * @Author:dwh
	 * @Date:2014-12-16 上午9:41:59 	
	 * @param modelMap
	 * @param request
	 * @param response
	 * @param paymoney
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="testjsapi")
	public ModelAndView testjsapi(ModelMap modelMap,HttpServletRequest request,  
            HttpServletResponse response,String paymoney)throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		System.out.println(paymoney);
		if(null==paymoney||("").equals(paymoney)){
			paymoney="1";
		}
		TreeMap<String, String> goodsInfo = new TreeMap<String, String>();
		goodsInfo.put("body", "assss");
		goodsInfo.put("total_fee", paymoney); //商品总金额,以分为单位
		String packegeValue = PayUtil.packageValueInfo(request, response, goodsInfo);
		String sign = PayUtil.getSign();
		String timestamp = PayUtil.getTimestamp();
		String nonce = PayUtil.getNonceStr();
		map.put("APPID", Commons.appId);
		map.put("PACKEGEVALUE", packegeValue);
		map.put("SIGN", sign);
		map.put("TIMESTAMP",timestamp);
		map.put("NONCE",nonce);
		modelMap.put("APPID", Commons.appId);
		modelMap.put("PACKEGEVALUE", packegeValue);
		modelMap.put("SIGN", sign);
		modelMap.put("TIMESTAMP",timestamp);
		modelMap.put("NONCE",nonce);
		modelMap.put("SIGNTYPE","SHA1");
		return new ModelAndView(PubitemConstants.TESTJSAPI,modelMap);
	}
	
	@RequestMapping(value="payNotify")
	public ModelAndView wechatPaySuccess(ModelMap modelMap,String scene_id,String openid){
		try {
			return new ModelAndView(PubitemConstants.PAYNOTIFYURL,modelMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(PubitemConstants.TAKEORDR,modelMap);
	}
	
	/**
	 * 查询账单信息
	 * @Description:
	 * @Title:queryfolio
	 * @Author:dwh
	 * @Date:2014-12-26 上午9:34:50 	
	 * @param modelMap
	 * @param resvno 账单号
	 * @param state 账单状态
	 * @param firmid 门店号
	 * @param dat 日期
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="queryfolio")
	public ModelAndView queryfolio(ModelMap modelMap,String foliono,String state,String firmid,String dat)throws Exception {
		List<Net_Orders> listordrs=PubitemSearch.queryfolio(foliono,state,firmid,dat);
		Net_Orders netOrder = new Net_Orders();
		for(int i =0; i < listordrs.size();i++){
			
			netOrder = listordrs.get(i);
			if(netOrder.getState().equals("2")){
				netOrder.setState("已支付");
			}else {
				netOrder.setState("未支付");
			}
		}
		modelMap.put("listordrs",listordrs);
		modelMap.put("netOrder",netOrder);
		return new ModelAndView(PubitemConstants.SHOWFOLIOSTATS,modelMap);
		
	}
}
