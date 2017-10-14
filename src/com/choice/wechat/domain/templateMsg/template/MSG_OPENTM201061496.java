package com.choice.wechat.domain.templateMsg.template;

/**
 * 
 * @author King
 * 充值成功通知
{{first.DATA}}
充值金额：{{keyword1.DATA}}
充值门店：{{keyword2.DATA}}
当前余额：{{keyword3.DATA}}
{{remark.DATA}}
 */
public class MSG_OPENTM201061496 extends MsgStructure{
	public String[] items = {"first","keyword1","keyword2","keyword3","remark"};
	
	public String[] getItems(){
		return items;
	}
}
