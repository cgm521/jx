package com.choice.wechat.common;

import java.io.Serializable;

/**
 * 微信通用接口凭证
 * 
 * @date 2015-02-06
 */
public class AccessToken implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3757603353238859712L;
	
	// 获取到的凭证
	private String token;
	// 凭证有效时间，单位：秒
	private int expiresIn;
	//凭证获取时间
	private long time;
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
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
