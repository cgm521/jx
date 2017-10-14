package com.choice.wechat.persistence.bookMeal;

import java.util.List;
import java.util.Map;

import com.choice.test.domain.DeskTimes;
import com.choice.test.domain.Net_OrderDtl;
import com.choice.test.domain.Net_OrderPackageDetail;
import com.choice.test.domain.Net_Orders;
import com.choice.test.domain.Voucher;
import com.choice.wechat.domain.bookDesk.City;
import com.choice.wechat.domain.bookDesk.Firm;
import com.choice.wechat.domain.bookDesk.StoreTable;
import com.choice.wechat.domain.bookMeal.FdItemSale;
import com.choice.wechat.domain.bookMeal.FdItemType;
import com.choice.wechat.domain.bookMeal.GroupActm;
import com.choice.wechat.domain.bookMeal.NetDishAddItem;
import com.choice.wechat.domain.bookMeal.NetDishProdAdd;
import com.choice.wechat.domain.bookMeal.ProdReqAdd;
import com.choice.wechat.domain.bookMeal.ProductAdditional;
import com.choice.wechat.domain.bookMeal.ProductRedfine;
import com.choice.wechat.domain.bookMeal.ProductReqAttAc;
import com.choice.wechat.domain.bookMeal.SellOff;

public interface BookMealMapper {
	/**
	 * 
	 * @param pk_group
	 * @param vname 城市名称
	 * @return
	 * 获取公司城市列表
	 */
	public List<City> getCityList(String pk_group, String vname);
	
	/**
	 * 
	 * @param pk_city firm.FIRMID
	 * @return
	 */
	public List<Firm> getFirmList(String pk_group, String pk_city, String firmid);
	
	/**
	 * 
	 * @param pk_group
	 * @param firmID 门店主键
	 * @param sft 餐次
	 * @param dat 日期
	 * @return
	 */
	public List<StoreTable> getDeskFormFirm(String pk_group, String firmID, String sft, String dat);
	
	/**
	 * 
	 * @param roomTyp 
	 * @param sft
	 * @param dat
	 * @param firmid
	 * @param realPax
	 * @return
	 */
	public List<StoreTable> findResvTbl(String roomTyp, String sft, String dat, String firmid, String realPax);
	
	/**
	 * 
	 * @param id 桌台主键
	 * @param firmid
	 * @return
	 * 获取要预定的桌台，并锁定
	 */
	public String getBookDesk(String id, String firmid);
	
	/**
	 * 
	 * @param resvtblid
	 * @param sft
	 * @param dat
	 * @return
	 * 获取已订桌位数量
	 */
	public int getCountDeskTimes(String resvtblid, String ordersid, String sft, String dat);
	/**
	 * 
	 * @param deskTimes
	 * 保存订位
	 */
	public void saveDeskTimes(DeskTimes deskTimes);
	
	/**
	 * 根据企业编码及门店编码获取菜品类型列表
	 * @param pkGroup 企业编码
	 * @param firmId  门店编码
	 * @param tables  台位编码
	 * @param vareaOrder  是否按区域点餐
	 */
	public List<FdItemType> getFdItemTypeList(String pkGroup, String firmId, String tables, String vareaOrder);
	
	/**
	 * 根据企业编码及门店编码判断是否存在套餐
	 * @param pkGroup 企业编码
	 * @param firmId  门店编码
	 */
	public Integer hasPackage(String pkGroup, String firmId);

	/**
	 * 获取菜品列表
	 * @param pkGroup   企业编码
	 * @param firmId    门店编码
	 * @param itemType  菜品类型
	 * @param name		 菜品名称
	 * @param id		 菜品编码
	 * @param scode
	 * @return
	 */
	public List<FdItemSale> getFdItemSaleList(String pkGroup, String firmId, String itemType, String name, String id, String scode, String type);
	
	/**
	 * 获取订单列表
	 * @param params
	 * @return
	 */
	public List<Net_Orders> getOrderMenus(Map<String,String> params);
	
	/**
	 * 
	 * @param id 订单号
	 * 取消订单
	 */
	public void cancelOrders(String id);
	
	/**
	 * 删除订单数据
	 * @param id
	 */
	public void deleteOrder(String id);
	
	/**
	 * 保存订单
	 * @param order
	 * @return
	 */
	public String saveOrder(Net_Orders order);
	
	/**
	 * 加菜处理，更新订单总金额，插入订单明细
	 * @param order
	 * @return
	 */
	public String addMenu(Net_Orders order);
	
	/**
	 * 获取必选附加项列表
	 * @param pk_group
	 * @return
	 */
	public List<ProductReqAttAc> getProductReqAttAcList(String pk_group);
	
	/**
	 * 获取附加项列表
	 * @param pk_group
	 * @return
	 */
	public List<ProductRedfine> getProductRedfineList(String pk_group);
	
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
	 * 查询沽清菜品列表
	 * @param data
	 * @return
	 */
	public List<SellOff> getSellOffList(SellOff data);
	
	/**
	 * 获取可选附加项列表
	 * @param pk_group
	 * @param pk_store
	 * @return
	 */
	public List<ProductRedfine> getProductRedfineByProgList(String pk_group, String pk_store);
	
	/**
	 * 更新订单状态
	 * @param id
	 * @param state
	 * @return
	 */
	public int updateOrderState(String id, String state);

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
	 * 获取门店等位最大等位人数
	 * @param pk_store
	 * @return
	 */
	public int getMaxPerson(String pk_store);
	
	/**
	 * 根据会员卡id获取电子券列表
	 * @param openId
	 * @param firmId
	 * @return
	 */
	public List<Voucher> getListCouponInfo(String openId);
	
	/**
	 * 根据会员卡id获取电子券列表
	 * @param openId
	 * @param firmId
	 * @return
	 */
	public List<Voucher> getListCouponInfo(String openId, String firmId);
	
	/**
	 * 查询菜品必选附加产品
	 * @param pk_group
	 * @return
	 */
	public List<ProdReqAdd> getListProdReqAdd(String pk_group);
	
	/**
	 * 查询菜品附加产品
	 * @param pk_group
	 * @param firmid
	 * @return
	 */
	public List<ProductAdditional> getListProductAdditional(String pk_group, String firmid);
	/**
	 * 查询套餐列表
	 * @param pk_group
	 * @param firmid
	 * @return
	 */
	public List<Map<String, Object>> selectPackage(String pk_group,String firmid,String type);
	/**
	 * 套餐明细
	 * @param pk_group
	 * @param firmid
	 * @param sellOffMap 
	 * @return
	 */
	public Object selectPackageDtl(String pk_group, String firmid, Map<String, String> sellOffMap);
	/**
	 * 查询订单中的套餐明细
	 * @param dtl
	 * @return
	 */
	public List<Net_OrderPackageDetail> getOrderPackageDtl(Net_OrderDtl dtl);
	
	/**
	 * 获取未完成点菜单数量
	 * @param order
	 * @return
	 */
	public int getOrderCount(Net_Orders order);
	
	/**
	 * 根据商户订单号查询订单
	 * @param outTradeNo
	 * @return
	 */
	public List<Net_Orders> getListNetOrdersByOutTradeNo(String outTradeNo);
	
	/**
	 * 获取团购活动
	 * @param firmid
	 * @param date
	 * @return
	 */

	public List<GroupActm> getGroupActm(String firmid,String date);
	
	/**
	 * 获取门店个性菜品列表
	 * @param pkGroup
	 * @param firmId
	 * @param isTake
	 * @return
	 */
	public List<FdItemSale> getStoreFdItemSaleList(String pkGroup, String firmId, String isTake);
	
	/**
	 * 根据菜品编码获取菜品
	 * @param code
	 * @return
	 */
	public List<FdItemSale> getItemByCode(String code);
}
