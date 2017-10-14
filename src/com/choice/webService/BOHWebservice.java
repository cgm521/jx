package com.choice.webService;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;

import com.choice.test.domain.DeskTimes;
import com.choice.test.domain.Net_OrderDtl;
import com.choice.test.domain.Net_Orders;


@WebService(name="BOHWebService")
public interface BOHWebservice {

	/**
	 * 获取加盟商可用金额
	 * @param scode  店铺编码
  	 * @return 
	 * @throws Exception
	 */
   public String showJoiningTraderMoney(@WebParam(name = "showJoiningTraderMoney",targetNamespace = "http://webService.choice.com/")String scode)throws Exception;
	
   /**
    * 验货修改加盟商金额
    * @param scode 店铺编码
    * @param list 验货金额 单据号
    * @return
    * @throws Exception
    */
   public String editJoiningTraderState(@WebParam(name = "editJoiningTraderState",targetNamespace = "http://webService.choice.com/")String scode,List<String[]> list)throws Exception;

   /**
    * 申购上传提交加盟商消费记录，并增加冻结金额
    * @param scode 店铺编码
    * @param list 申购金额 单据号
    * @return
    * @throws Exception
    */
   public String addJoiningTraderState(@WebParam(name = "editJoiningTraderState",targetNamespace = "http://webService.choice.com/")String scode,List<String[]> list)throws Exception;

   /**
    * 门店
    * @param code 门店主键
    * @param name 门店名称
    * @param area 隶属城市
    * @return
    * @throws Exception
    */
   public String getTeleStore(@WebParam(name = "getTeleStore",targetNamespace = "http://webService.choice.com/")String code,String name,String area)throws Exception;
   
   /**
    * 区域
    * @param code
    * @param name
    * @return
    * @throws Exception
    */
   public String getTeleArea(@WebParam(name = "getTeleArea",targetNamespace = "http://webService.choice.com/")String code,String name,String firmid)throws Exception;

   /**
    * 台位
    * @param areaid
    * @param code
    * @param name
    * @return
    * @throws Exception
    */
   public String getTeleStoreTable(@WebParam(name = "getTeleStoreTable",targetNamespace = "http://webService.choice.com/")String areaid,String code,String name,String firmId)throws Exception;

   /**
    * 菜品类别
    * @param code
    * @param name
    * @return
    * @throws Exception
    */
   public String getTeleProjectType(@WebParam(name = "getTeleProjectType",targetNamespace = "http://webService.choice.com/")String code,String name,String firmId)throws Exception;

   /**
    * 菜品
    * @param protypeid
    * @param scode
    * @param name
    * @return
    * @throws Exception
    */
   public String getTeleProject(@WebParam(name = "getTeleProject",targetNamespace = "http://webService.choice.com/")String protypeid,String scode,String name,String firmId)throws Exception;
   
   /**
    * 查询我的订单
    * @param state
    * @param openid
    * @return
    * @throws Exception
    */
   public String getOrderMenus(@WebParam(name = "getOrderMenus",targetNamespace = "http://webService.choice.com/")String id,
		   @WebParam(name = "state",targetNamespace = "http://webService.choice.com/")String state,
		   @WebParam(name = "openid",targetNamespace = "http://webService.choice.com/")String openid,
		   @WebParam(name = "firmid",targetNamespace = "http://webService.choice.com/")String firmid,
		   @WebParam(name = "datetime",targetNamespace = "http://webService.choice.com/")String datetime,
		   @WebParam(name = "dat",targetNamespace = "http://webService.choice.com/")String dat,
		   @WebParam(name = "resv",targetNamespace = "http://webService.choice.com/")String resv)throws Exception;

   /**
    * 查询我的订单隶属门店
    * @param state
    * @param openid
    * @return
    * @throws Exception
    */
   public String getOrderCity(@WebParam(name = "getOrderCity",targetNamespace = "http://webService.choice.com/")String state,String openid)throws Exception;

   /**
    * 查询我的订单详情
    * @param orderid
    * @return
    * @throws Exception
    */
   public String getOrderDtlMenus(@WebParam(name = "getOrderDtlMenus",targetNamespace = "http://webService.choice.com/")String orderid)throws Exception;

   /**
    * 预定点菜
    * @param orders
    * @return
    * @throws Exception
    */
   public String saveMenus(@WebParam(name = "saveMenus",targetNamespace = "http://webService.choice.com/")Net_Orders orders)throws Exception;

   /**
    * 预定点菜详情
    * @param ordersDtl
    * @return
    * @throws Exception
    */
   public String saveOrderDtl(@WebParam(name = "saveOrderDtl",targetNamespace = "http://webService.choice.com/")Net_OrderDtl ordersDtl)throws Exception;

   /**
    * 取消订单
    * @param state
    * @param resv
    * @return
    * @throws Exception
    */
   public String deleteOrder(@WebParam(name = "deleteOrder",targetNamespace = "http://webService.choice.com/")String state,String resv)throws Exception;
  
   /**
    * 删除菜品
    * @param resv
    * @param id
    * @return
    * @throws Exception
    */
   public String deleteOrderDtl(@WebParam(name = "deleteOrderDtl",targetNamespace = "http://webService.choice.com/")String resv,String id)throws Exception;
  
   /**
    * 城市
    * @param des
    * @return
    * @throws Exception
    */
   public String getCity(@WebParam(name = "getCity",targetNamespace = "http://webService.choice.com/")String des)throws Exception;
   /**
    * 餐次
    * @param name
    * @return
    * @throws Exception
    */
   public String getSFT(@WebParam(name = "getSFT",targetNamespace = "http://webService.choice.com/")String name)throws Exception;
   /**
    * 时间桌台，台位状态
    * @param resvtblid
    * @param ordersid
    * @return
    * @throws Exception
    */
   public String getDeskTimes(@WebParam(name = "getDeskTimes",targetNamespace = "http://webService.choice.com/")String resvtblid,String ordersid,String sft,String dat)throws Exception;

   /**
    * 查询可预订台位（人数对应台位）
    * @param ordersDtl
    * @return
    * @throws Exception
    */
   public String findStoreTable(@WebParam(name = "findStoreTable",targetNamespace = "http://webService.choice.com/")String sft,String dat,String firmId)throws Exception;
   
   /**
    * 自助点菜保存菜品
    * @param orders
    * @return
    * @throws Exception
    */
   public String saveOrdersOrDtl(@WebParam(name = "saveOrdersOrDtl",targetNamespace = "http://webService.choice.com/")Net_Orders orders)throws Exception;

   /**
    * 查询可预订的包间或者大厅
    * @param roomTyp
    * @param sft
    * @param dat
    * @param firmId
    * @return
    * @throws Exception
    */
   public String findResvTbl(@WebParam(name = "findResvTbl", targetNamespace = "http://webService.choice.com/") String roomTyp,String sft,String dat,String firmId,String pax)throws Exception;
   
   /**
    * 查询优惠信息
    * @param dat
    * @param firmId
    * @param FavorAreaId
    * @return
    * @throws Exception
    */
   public String findWebMsg(@WebParam(name = "findWebMsg", targetNamespace = "http://webService.choice.com/") String dat,String firmId,String FavorAreaId)throws Exception;
   
   /**
    * 查询优惠信息区域
    * @param id
    * @return
    * @throws Exception
    */
   public String findFavorArea(@WebParam(name = "findFavorArea", targetNamespace = "http://webService.choice.com/") String id)throws Exception;
   
   /**
    * 判断当前门店是否有套餐
    * @param firmId
    * @return
    * @throws Exception
    */
   public String flagPkgPackAge(@WebParam(name = "flagPkgPackAge", targetNamespace = "http://webService.choice.com/") String firmId)throws Exception;
   
   /**
    * 查询所有套餐
    * @param id
    * @param firmId
    * @return
    * @throws Exception
    */
   public String findPkgPackAge(@WebParam(name = "findPkgPackAge", targetNamespace = "http://webService.choice.com/") String id,String firmId)throws Exception;
   
   /**
    * 查询套餐明细
    * @param idCode
    * @param firmId
    * @param packAgeId
    * @return
    * @throws Exception
    */
   public String findPkgPackAgeDtl(@WebParam(name = "findPkgPackAgeDtl", targetNamespace = "http://webService.choice.com/") String idCode,String firmId,String packAgeId)throws Exception;

   /**
    * 新增时间桌台
    * @param deskTimes
    * @return
    * @throws Exception
    */
   public String saveDeskTimes(@WebParam(name = "saveDeskTimes", targetNamespace = "http://webService.choice.com/") DeskTimes deskTimes)throws Exception;
  
   /**
    * 热门菜品
    * @param firmId
    * @return
    * @throws Exception
    */
   public String findHotItem(@WebParam(name = "findHotItem", targetNamespace = "http://webService.choice.com/") String firmId)throws Exception;
  
   /**
    * 修改账单备注
    * @param id
    * @param remark
    * @return
    * @throws Exception
    */
   public String updateOrders(@WebParam(name = "updateOrders", targetNamespace = "http://webService.choice.com/") String id,String remark)throws Exception;
   /**
    * 修改账单状态
    * @param id
    * @param remark
    * @return
    * @throws Exception
    */
   public String updateOrderState(@WebParam(name = "updateOrderState", targetNamespace = "http://webService.choice.com/") Net_Orders orders)throws Exception;

   /**
    * 生成账单消费记录
    * @param id
    * @param remark
    * @return
    * @throws Exception
    */
   public String orderOutAmt(@WebParam(name = "orderOutAmt", targetNamespace = "http://webService.choice.com/") Net_Orders orders)throws Exception;

}
