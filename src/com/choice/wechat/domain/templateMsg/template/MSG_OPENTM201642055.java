package com.choice.wechat.domain.templateMsg.template;
/**
 * 
 * @author King
 *
 *微信支付成功通知
{{first.DATA}}
订单号：{{keyword1.DATA}}
消费金额：{{keyword2.DATA}}
消费门店：{{keyword3.DATA}}
消费时间：{{keyword4.DATA}}
{{remark.DATA}}
 */
public class MSG_OPENTM201642055 extends MsgStructure{
	public String[] items = {"first","keyword1","keyword2","keyword3","keyword4","remark"};
	public String[] getItems() {
		return items;
	}

}
