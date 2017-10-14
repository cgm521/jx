package com.choice.wechat.persistence.cookie;

import java.util.List;

import com.choice.wechat.domain.cookie.Cookie;
import com.choice.wechat.domain.cookie.WeChatWall;

public interface CookieMapper {

	/**
	 * 通过客户主键查询客户登录数据
	 * @param openid
	 * @return
	 */
	public List<Cookie> findLoginInfoCookie(Cookie cookie);
	/**
	 * 新增客户登录数据
	 * @param openid
	 * @return
	 */
	public void addLoginInfoCookie(Cookie cookie);
	public void insertWeChatWall(WeChatWall weChatWall);
	/**
	 * 退出微信墙
	 * @param openid
	 */
	public void deleteLoginInfoCookie(Cookie cookie);
}
