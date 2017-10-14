package com.choice.wechat.persistence.takeout;

import java.util.List;
import java.util.Map;

import com.choice.test.domain.Net_Orders;
import com.choice.wechat.domain.takeout.Address;
import com.choice.wechat.domain.takeout.RangeCoordi;
import com.choice.wechat.domain.takeout.StoreRange;

public interface TakeOutMapper {
	/**
	 * 
	 * @param openid
	 * @param pk_id
	 * @return
	 * 获取地址列表
	 */
	public List<Address> getAddress(String openid,String pk_id);
	
	/**
	 * 
	 * @param address
	 * 新增地址
	 */
	public void addAddress(Address address);
	
	/**
	 * 
	 * @param address
	 * 更新地址
	 */
	public void updateAddress(Address address);
	
	/**
	 * 
	 * @param map
	 * 更新外卖订单
	 */
	public int updateTakeOutOrders(Map<String,String> map);
	
	/**
	 * 
	 * @param params
	 * @return
	 * 获取订单，地址为net_orders中地址
	 */
	public List<Net_Orders> getOrderMenus(Map<String, String> params);
	
	/**
	 * 获取门店配送范围列表
	 * @param pkGroup
	 * @param pkStore
	 * @return
	 */
	public List<StoreRange> getListStoreRange(String pkGroup, String pkStore);
	
	/**
	 * 获取门店配送范围坐标点列表
	 * @param pkGroup
	 * @param pkStore
	 * @return
	 */
	public List<RangeCoordi> getListRangeCoordi(String pkGroup, String pkStore);
}
