package com.choicesoft.webService.server;


import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;

import com.choice.test.domain.CallBillOrder;
import com.choice.test.domain.PayMent;


@WebService(name="CallBill_OrderWebService")
public interface CallBill_OrderWebService {
	/**
	 * 修改订单状态为已付款
	 * @param ordcode 订单号
	 * @return
	 */
	public String orderComplete(@WebParam(name = "orderComplete",targetNamespace = "http://server.webService.choicesoft.com/")List<PayMent> listPayMent,String sign);
	
	/**
	 * 查询订单
	 * @param ordcode 订单号
	 * @return
	 */
	public String verificationOrder(@WebParam(name = "verificationOrder",targetNamespace = "http://server.webService.choicesoft.com/")String ordcode);

	/**
	 * 微信推送账单到呼叫中心
	 * 参数：
	 * List<CallBillOrder>listOrder订单信息；
	 * String sign   md5校验码（预留，默认为空）
	 * 输出参数：
	 * 返回结果：String result  String 格式字符;
	 * 返回“-1”表示订单保存失败
	 * 返回“1”表示订单保存成功
	 */
	public String pushOrderByWeixin(@WebParam(name = "pushOrderByWeixin",targetNamespace = "http://server.webService.choicesoft.com/")List<CallBillOrder>listOrder,String sign);
	
}
