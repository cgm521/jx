package com.choice.wechat.persistence.bookDesk;

import java.util.List;
import java.util.Map;

import com.choice.test.domain.DeskTimes;
import com.choice.test.domain.Net_Orders;
import com.choice.test.exception.CRUDException;
import com.choice.wechat.domain.bookDesk.Brands;
import com.choice.wechat.domain.bookDesk.City;
import com.choice.wechat.domain.bookDesk.Firm;
import com.choice.wechat.domain.bookDesk.MealTime;
import com.choice.wechat.domain.bookDesk.StoreTable;
import com.choice.wechat.domain.bookDesk.Street;

public interface BookDeskMapper {
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
	 * 获取门店下可预定桌台的类型及数量
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
	 * 获取可预定桌台列表
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
	public int getCountDeskTimes(String resvtblid, String ordersid, String sft, String dat, String firmid);
	/**
	 * 
	 * @param deskTimes
	 * 保存订位
	 */
	public void saveDeskTimes(DeskTimes deskTimes);

	/**
	 * 
	 * @param orders
	 * @param orderType 0 订位单 1 点菜单
	 * 保存订单
	 */
	public void saveOrders(Net_Orders orders,String clientID);

	/**
	 * 
	 * @param id 订单号
	 * 取消订单
	 */
	public void cancelOrders(String id);

	/**
	 * 
	 * @param id 订单号
	 * 取消订位
	 */
	public void cancelOrdersDesk(String id);

	/**
	 * @return
	 * 获取我的订单
	 */
	public List<Net_Orders> getOrderMenus(Map<String,String> params);

	/**
	 * 通过扫码表示获取台位信息
	 * @param scene_id 二维码
	 * @return
	 */
	public List<StoreTable> findRestTbl(String scene_id);
	/**
	 * 修改订单
	 * @param orders
	 * @throws CRUDException
	 * @author ZGL
	 * @return 
	 * @Date 2015年4月1日 09:45:58
	 */
	public String updateOrdr(Net_Orders orders);

	public Map<String, Object> getDeskState(String firmid, String dat, String sft);

	/**
	 * 浠嶣OH鍙栭棬搴�
	 * @return
	 */
	public List<Firm> getFirmList(Map<String,String> param);

	/**
	 * 闆嗗洟鍝佺墝
	 * @param pk_group
	 * @param brandFilter
	 * @return
	 */
	public List<Brands> getBrands(String pk_group,String brandFilter);

	/**
	 * 鍙栭棬搴楀綋鍓嶆椿鍔�
	 * @param firmid
	 * @return
	 */
	public List<Map<String,Object>> getFirmActm(String firmid);

	/**
	 * 鑾峰彇鍩庡競鐨勮閬�
	 * @return
	 */
	public List<Street> getStreet(String pk_city);

	/**
	 * 鏀惰棌搴楅摵
	 * @param firmid
	 * @param openid
	 * @param pk_group
	 */
	public void storeUp(String firmid, String openid, String pk_group);

	/**
	 * 鍙栨秷鏀惰棌
	 * @param firmid
	 * @param openid
	 * @param pk_group
	 */
	public void cancelStoreUp(String firmid, String openid, String pk_group);
	
	/**
	 * 获取三餐开始结束时间
	 * @param firmid
	 * @return
	 */
	public Map<String,Object> getMealTime(String firmid);
	
	/**
	 * 从视图view_sft获取三餐开始结束时间
	 * @param firmid
	 * @return
	 */
	public List<MealTime> getMealTimeFromView(String firmid);
	
	/**
	 * 从视图view_sft获取三餐开始结束时间,中餐
	 * @param firmid
	 * @return
	 */
	public List<MealTime> getMealTimeFromViewCn(String firmid);
	
	/**
	 * 获取店铺名称
	 * @param firmid
	 * @return
	 */
	public String getFirmName(String firmid);
	/**
	 * 支付方式反结算
	 * 将支付方式的金额置为负数存到系统中 将菜品均摊的折扣记录删除
	 * @author ZGL
	 * @date 2015-04-15 15:07:45
	 * @param id
	 */
	public void cancelOrderPayment(String id);
	/**
	 * 将本次发送的mq信息流水号存储下来
	 * @param mqMap
	 */
	public void addMqLogs(Map<String, Object> mqMap);
	/**
	 * 修改mq信息
	 * @param mqMap
	 * @return 
	 */
	public int updateMqlogs(Map<String, Object> mqMap);
	/**
	 * 查询订单信息及店铺信息
	 * @param mqMap
	 * @return
	 */
	public List<Map<String, Object>> queryOrderFromMqlogs(Map<String, Object> mqMap);

	/**
	 * 获取等位类型
	 * @param pk_store
	 * @return
	 */
	public List<StoreTable> getLineNoList(String pk_store);

	public void deleteDetails(Net_Orders orders);
	
	/**
	 * 发起我要结帐时更新paymoney为空，否则多次发起时只能取到上一次的数据
	 * @param orders
	 */
	public void resetPayMoeny(Net_Orders orders);
	
	/**
	 * 获取订单数量
	 * @param paramMap
	 * @return
	 */
	public int getCountOrdersWithOpenid(Map<String, String> paramMap);
	
	/**
	 * 获取系统中预制的餐次
	 */
	public Map<String,String> getShiftSft();
	
	/**
	 * 获取门店设置的餐次
	 */
	public Map<String,String> getFirmSft(String firmid);
}
