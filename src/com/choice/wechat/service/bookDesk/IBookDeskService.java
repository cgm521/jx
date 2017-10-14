package com.choice.wechat.service.bookDesk;

import com.choice.test.domain.DeskTimes;
import com.choice.test.domain.Net_Orders;

public interface IBookDeskService {
	/**
	 * 
	 * @param orders
	 * @param deskTimes
	 * @param clientID
	 * 保存订位订单
	 */
	public void saveOrder(Net_Orders orders, DeskTimes deskTimes, String clientID);
	
	/**
	 * 
	 * @param id 订单号
	 * 取消订单
	 * @param openid 
	 * @param pk_group 
	 * @param firmid 
	 * @param state 
	 * @param vtransactionid 
	 * @param paymoney 
	 */
	public void cancelOrders(String id, String openid, String pk_group,String firmid, String state, String vtransactionid, String paymoney, String orderType);
}
