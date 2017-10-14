package com.choice.test.persistence;

import java.util.List;
import java.util.Map;

import com.choice.test.domain.Arear;
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
import com.choice.test.exception.CRUDException;
import com.choice.wechat.domain.bookMeal.FdItemSale;
import com.choice.wechat.domain.bookMeal.NetDishAddItem;
import com.choice.wechat.domain.bookMeal.NetDishProdAdd;

/***
 * 
 * @author 王吉峰
 *
 */
public interface WeChatOrderMapper {
	
	/**
    * 城市
    * @param des
    * @return
    * @throws Exception
    */
   public List<City> getCity(String des);

   /**
    * 门店
    * @param code 门店主键
    * @param name 门店名称
    * @param area 隶属城市
    * @return
    * @throws Exception
    */
   public List<Firm> getTeleStore(String code,String name,String area);
   
   /**
   * 菜品类别
   * @param code
   * @param name
   * @param firmId
   * @return
   * @throws Exception
   */
   public List<ProjectType> getTeleProjectType(String code,String name,String firmId);
   
   /**
   * 查询我的订单
   * @param state
   * @param openid
   * @return
   * @throws Exception
   */
  public List<Net_Orders> getOrderMenus(String id,String state,String openid,String firmid,String datetime,String dat);
  
  
  
  /**
  * 查询我的订单详情
  * @param orderid
  * @return
  * @throws Exception
  */
  public List<Net_OrderDtl> getOrderDtlMenus(String orderid);
  
  /**
  * 判断当前门店是否有套餐
  * @param firmId
  * @param dat
  * @return
  * @throws Exception
  */
  public String flagPkgPackAge(String firmId);
  
  /**
   * 查询所有套餐
   * @param firmId
   * @param dat
   * @return
   * @throws Exception
   */
   public List<ItemPrgPackage> findPkgPackAge(String id,String firmId);
   
   /**
    * 查询套餐明细
    * @param firmId
    * @param dat
    * @return
    * @throws Exception
    */
    public List<ItemPrgpackAgedtl> findPkgPackAgeDtl(String idCode,String firmId,String packAgeId);
    
    /**
     * 菜品
     * @param protypeid
     * @param scode
     * @param name
     * @return
     * @throws Exception
     */
    public List<Project> getTeleProject(String protypeid,String scode,String name,String firmId,String id);
  
    /**
     * 热门菜品
     * @param firmId
     * @return
     * @throws Exception
     */
     public List<Project> findHotItem(String firmId);
     
     /**
      * 查询我的订单隶属门店
      * @param state
      * @param openid
      * @return
      * @throws Exception
      */
     public List<Net_Orders> getOrderCity(String state,String openid);
     
     /**
      * 餐次
      * @param ordersDtl
      * @return
      * @throws Exception
      */
     public List<Sft> getSFT(String name);
     
     /**
      * 查询可预订台位（人数对应台位）
      * @param ordersDtl
      * @return
      * @throws Exception
      */
     public List<StoreTable> findStoreTable(String sft,String dat,String firmId);
     
     /**
      * 查询可预订的包间或者大厅
      * @param ordersDtl
      * @return
      * @throws Exception
      */
     public List<StoreTable> findResvTbl(String roomTyp,String sft,String dat,String firmId,String pax);
     
     /**
      * 时间桌台，台位状态
      * @param ordersDtl
      * @return
      * @throws Exception
      */
     public List<DeskTimes> getDeskTimes(String resvtblid,String ordersid,String sft,String dat);
     
     /**
      * 新增时间桌台
      * @param deskTimes
      * @return
      * @throws Exception
      */
     public String saveDeskTimes(DeskTimes deskTimes);
     
     /**
      * 预定点菜
      * @param orders
      * @return
      * @throws Exception
      */
     public String saveMenus(Net_Orders orders);
     
     /**
      * 台位
      * @param areaid
      * @param code
      * @param name
      * @return
      * @throws Exception
      */
     public List<StoreTable> getTeleStoreTable(String areaid,String code,String name,String firmId);
     
     /**
      * 修改账单备注
      * @param id
      * @param remark
      * @return
      * @throws Exception
      */
     public String updateOrders(String id,String remark)throws Exception;

   /**
    * 区域
    * @param code
    * @param name
    * @return
    * @throws Exception
    */
   public List<Arear> getTeleArea(String code,String name,String firmid);

   /**
    * 预定点菜详情
    * @param ordersDtl
    * @return
    * @throws Exception
    */
   public String saveOrderDtl(Net_OrderDtl ordersDtl);

   /**
    * 取消订单
    * @param ordersDtl
    * @return
    * @throws Exception
    */
   public String deleteOrder(String state,String resv);
  
   /**
    * 删除菜品
    * @param ordersDtl
    * @return
    * @throws Exception
    */
   public String deleteOrderDtl(String resv,String id);
  
   /**
    * 自助点菜保存菜品
    * @param ordersDtl
    * @return
    * @throws Exception
    */
   public String saveOrdersOrDtl(Net_Orders orders);
   
   /**
    * 查询优惠信息区域
    * @param firmId
    * @param dat
    * @return
    * @throws Exception
    */
   public List<FavorArea> findFavorArea(String id);

   /**
    * 查询优惠信息
    * @param firmId
    * @param dat
    * @return
    * @throws Exception
    */
   public List<WebMsg> findWebMsg(String dat,String firmId,String FavorAreaId);

   /**
    * 修改账单状态
    * @param id
    * @param remark
    * @return
    * @throws Exception
    */
   public String updateOrderState(Net_Orders orders);

   /**
    * 生成账单消费记录
    * @param id
    * @param remark
    * @return
    * @throws Exception
    */
   public String orderOutAmt(Net_Orders orders);

   /**
    * 通过门店查询优惠信息
    * @param firmId
    * @param dat
    * @return
    * @throws Exception
    */
   public List<WebMsg> findStoreWebMsg(String firmId);
   
   /**
    * 通过扫码标识获取台位信息
    * @param scene_id
    * @return
    * @throws Exception
    */
   public List<StoreTable> findRestTbl(String scene_id);
   
   /**
    * 查询订单信息
    * @Description:
    * @Title:queryfolio
    * @Author:dwh
    * @Date:2014-12-26 上午9:21:01 	
    * @param resvno 预订单号
    * @param state 账单状态
    * @param firmid 门店号
    * @param dat 日期
    * @return
    */
   public List<Net_Orders> queryfolio(String resvno,String state,String firmid,String dat);

	/**
	 * 修改订单
	 * @param orders
	 * @throws CRUDException
	 * @author ZGL
	 * @return 
	 * @Date 2015年4月1日 09:45:58
	 */
	public String updateOrdr(Net_Orders orders);
	
	/**
	 * 获取菜品必选附加项列表
	 * @param pk_group
	 * @param orderId
	 * @return
	 */
	public List<NetDishAddItem> getDishAddItemList(String pk_group, String orderId);
	
	/**
	 * 获取菜品附加产品列表
	 * @param pk_group
	 * @param orderId
	 * @return
	 */
	public List<NetDishProdAdd> getDishProdAddList(String pk_group, String orderId);

	/**
	 * 将本次发送的mq信息流水号存储下来
	 * @author ZGL
	 * @date 2015-04-24 10:27:09
	 */
	public void addMqLogs(Map<String, Object> mqMap);

	/**
	 * 增加开台菜品
	 * @param order
	 */
	public void addOpenItem(Net_Orders order);

	/**
	 * 保存订单明细
	 * @param order
	 * @param cnt
	 * @return
	 */
	public int saveOrderDtl(Net_Orders order);
	
	/**
	 * 获取菜品门店编码
	 * @param firmid
	 * @return
	 */
	public List<FdItemSale> getListPubItemCode(String firmid);
	/**
	 * 查询订单菜品套餐明细
	 * @param dtl
	 * @return
	 */
	public List<Map<String, Object>> listFolioPacageDtl(Net_OrderDtl dtl);
	/**
	 * 获取套餐门店编码
	 * @param firmid
	 * @return
	 */
	public List<FdItemSale> getListPackageCode(String firmid);
	/**
	 * 查询门店台位信息
	 * @param firmId
	 * @param tableid
	 * @return
	 */
	public List<Map<String, Object>> getStoreSeatInfo(String firmId,String tableid);
}
