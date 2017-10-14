package com.choice.wechat.domain.templateMsg.template;
/**
 * 
 * @author King
{{first.DATA}} 
 
商店名称：{{storeName.DATA}} 
订单编号：{{orderId.DATA}}
订单类型：{{orderType.DATA}}
{{remark.DATA}}
 */
public class MSG_TM00534 extends MsgStructure{
	public String[] items = {"first","storeName","orderId","orderType","remark"};
	
	public String[] getItems() {
		return items;
	}
}
