package com.choice.wechat.common;

import java.io.Serializable;

/**
 * 
 * @author King
 * 调用微信JS接口的临时票据
 */
public class JsapiTicket implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8783193418422405612L;
	
	// 获取到的凭证
	private String ticket;
	// 凭证有效时间，单位：秒
	private int expiresIn;
	//凭证获取时间
	private long time;
	
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public int getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
}
