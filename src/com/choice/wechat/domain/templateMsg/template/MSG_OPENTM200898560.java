package com.choice.wechat.domain.templateMsg.template;

public class MSG_OPENTM200898560 extends MsgStructure{
/**
{{first.DATA}}
会员姓名：{{keyword1.DATA}}
会员账号：{{keyword2.DATA}}
积分变更：{{keyword3.DATA}}
剩余积分：{{keyword4.DATA}}
{{remark.DATA}}
 */
	public String[] items = {"first","keyword1","keyword2","keyword3","keyword4","remark"};
	public String[] getItems() {
		return items;
	}

}
