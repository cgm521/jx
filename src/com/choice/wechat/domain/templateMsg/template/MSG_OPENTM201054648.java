package com.choice.wechat.domain.templateMsg.template;

/**
 * 
 * @author King
 *微信会员消费通知
{{first.DATA}}
消费金额：{{keyword1.DATA}}
获得积分：{{keyword2.DATA}}
消费门店：{{keyword3.DATA}}
当前余额：{{keyword4.DATA}}
当前积分：{{keyword5.DATA}}
{{remark.DATA}}
 */
public class MSG_OPENTM201054648 extends MsgStructure {
	public String[] items = {"first","keyword1","keyword2","keyword3","keyword4","keyword5","remark"};
	
	public String[] getItems(){
		return items;
	}
}
