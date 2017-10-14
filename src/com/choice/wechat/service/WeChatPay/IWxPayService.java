package com.choice.wechat.service.WeChatPay;

import java.util.List;
import java.util.Map;

import com.choice.test.exception.CRUDException;

public interface IWxPayService {
	/**
	 *  查询该门店可用的电子券
	 * @param firmid
	 * @param dat
	 * @throws CRUDException
	 */
	public List<Map<String,Object>> saveOrder(String firmid,String dat) throws CRUDException;
	/**
	 * 取消会员卡支付
	 * @param mapParam
	 * @return
	 * @throws Exception
	 */
	public String cancelPayWithCard(Map<String, Object> mapParam) throws Exception;
	/**
	 * 保存账单支付明细
	 * @param mapParam
	 * @return
	 * @throws Exception
	 */
	public String savePayment(Map<String, Object> mapParam) throws Exception;
	/**
	 * 微信支付退款
	 * @param mapParam
	 * @return
	 */
	public String cancelPayWithTenpay(Map<String, Object> mapParam);
	/**
	 * 撤销电子券消费
	 * @param mapParam
	 * @return
	 * @throws Exception
	 */
	public String cancalCoupon(Map<String, Object> mapParam) throws Exception;
	/**
	 * 微信支付退款
	 * @param mapParam
	 * @return
	 */
	public String cancelPayWithTenpayV3(Map<String, Object> mapParam) throws Exception;
	/**
	 * 财付通对账接口-订单查询
	 * @param mapParam
	 * @return
	 */
	public String normalorderquery(Map<String, Object> mapParam);
	
}
