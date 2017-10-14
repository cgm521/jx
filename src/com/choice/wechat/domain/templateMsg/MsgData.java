package com.choice.wechat.domain.templateMsg;

public class MsgData {
	private String value;
	private String color;//默认颜色  = "#D5D5D5"
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
}
