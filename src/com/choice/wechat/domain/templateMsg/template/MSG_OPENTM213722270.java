package com.choice.wechat.domain.templateMsg.template;

/**
 * 优惠券领取成功通知
 * @author King
 *
 *
	{{first.DATA}}
	优惠券：{{keyword1.DATA}}
	来源：{{keyword2.DATA}}
	过期时间：{{keyword3.DATA}}
	使用说明：{{keyword4.DATA}}
	{{remark.DATA}}
 */
public class MSG_OPENTM213722270 extends MsgStructure{
	
	public String[] items = {"first","keyword1","keyword2","keyword3","remark"};
	
	public String[] getItems(){
		return items;
	}
	
}
