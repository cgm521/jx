package com.choice.wechat.domain.templateMsg.template;

/**
 * 优惠券使用通知
 * @author King
 *
 *
	{{first.DATA}}
	优惠券：{{keyword1.DATA}}
	兑换码：{{keyword2.DATA}}
	消费日：{{keyword3.DATA}}
	{{remark.DATA}}
 */
public class MSG_OPENTM200972357 extends MsgStructure{
	
	public String[] items = {"first","keyword1","keyword2","keyword3","remark"};
	
	public String[] getItems(){
		return items;
	}
	
}
