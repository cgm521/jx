package com.choice.wechat.persistence.cookie.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.util.StringUtils;

import com.choice.test.utils.CodeHelper;
import com.choice.test.utils.DateFormat;
import com.choice.test.utils.JdbcConnection;
import com.choice.wechat.domain.cookie.Cookie;
import com.choice.wechat.domain.cookie.WeChatWall;
import com.choice.wechat.persistence.cookie.CookieMapper;

public class CookieMapperImpl implements CookieMapper{

	/**
	 * 查询登录信息
	 */
	@Override
	public List<Cookie> findLoginInfoCookie(Cookie cookie) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		List<Cookie> listCookie = new ArrayList<Cookie>();
		try {
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			StringBuilder sql = new StringBuilder("");
			sql.append("SELECT PK_COOKIE,APPID,OPENID,COOKIE ")
			.append(" FROM WX_COOKIE")
			.append(" WHERE 1=1 ");

			if (StringUtils.hasText(cookie.getAppid())) {
				sql.append(" AND appid= '"+cookie.getAppid()+"' ");
			}
			if (StringUtils.hasText(cookie.getOpenid())) {
				sql.append(" AND openid= '"+cookie.getOpenid()+"' ");
			}
			//查询是否已经登录
			rs = st.executeQuery(sql.toString());
			while(rs.next()){
				Cookie cookie2=new Cookie();
				cookie2.setPk_cookie(rs.getString("PK_COOKIE"));
				cookie2.setAppid(rs.getString("APPID"));
				cookie2.setOpenid(rs.getString("OPENID"));
				cookie2.setCookie(rs.getString("COOKIE"));
				listCookie.add(cookie2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listCookie;
	}
	/**
	 * 新增登录信息
	 */
//	private final static String insertCookie = "INSERT INTO WX_COOKIE(PK_COOKIE,APPID,OPENID,COOKIE,LOGINTS,TS) VALUES(?,?,?,?,?,?)";
	@Override
	public void addLoginInfoCookie(Cookie cookie) {

		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			//查询是否已经登录
			String selectCookieByOpenid = "SELECT PK_COOKIE,APPID,OPENID,COOKIE FROM WX_COOKIE WHERE OPENID = '"+cookie.getOpenid()+"'";
			rs = st.executeQuery(selectCookieByOpenid);
			rs.last();
			//如果已经登录删除登录信息
			if(rs.getRow()>0){
				String deleteCookie = "DELETE FROM WX_COOKIE WHERE OPENID = '"+cookie.getOpenid()+"'";
				st.executeUpdate(deleteCookie);
			}
			//新增登录信息
			String insertCookie = "INSERT INTO WX_COOKIE(PK_COOKIE,APPID,OPENID,COOKIE,LOGINTS,TS) VALUES(";
			StringBuffer sbf = new StringBuffer(insertCookie);
			sbf.append("'"+CodeHelper.createUUID()+"'");
			sbf.append(",'"+cookie.getAppid()+"'");
			sbf.append(",'"+cookie.getOpenid()+"'");
			sbf.append(",'"+cookie.getCookie()+"'");
			sbf.append(",'"+DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss")+"'");
			sbf.append(",'"+DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss")+"'");
			sbf.append(")");
			st.executeUpdate(sbf.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void insertWeChatWall(WeChatWall weChatWall){
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			//新增登录信息
			String insertCookie = "INSERT INTO WX_WECHATWALL(PK_WECHATWALL,OPENID,HEADIMGURL,NICKNAME,PK_STORE,VCONTENT,TS,VISCHECK,DR,MSGTYPE) VALUES(";
			StringBuffer sbf = new StringBuffer(insertCookie);
			sbf.append("'"+CodeHelper.createUUID()+"'");
			sbf.append(",'"+weChatWall.getOpenid()+"'");
			sbf.append(",'"+weChatWall.getHeadimgurl()+"'");
			sbf.append(",'"+weChatWall.getNickname()+"'");
			sbf.append(",'"+weChatWall.getPk_store()+"'");
			sbf.append(",'"+weChatWall.getVcontent()+"'");
			sbf.append(",'"+DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss")+"'");
			sbf.append(",'Y'");
			sbf.append(",'0'");
			sbf.append(",'"+weChatWall.getMsgType()+"'");
			sbf.append(")");
			st.executeUpdate(sbf.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 退出微信墙
	 */
	@Override
	public void deleteLoginInfoCookie(Cookie cookie) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			StringBuilder sql = new StringBuilder("");
			sql.append("delete  FROM WX_COOKIE")
			.append(" WHERE 1=1 ");
	
			if (StringUtils.hasText(cookie.getAppid())) {
				sql.append(" AND appid= '"+cookie.getAppid()+"' ");
			}
			if (StringUtils.hasText(cookie.getOpenid())) {
				sql.append(" AND openid= '"+cookie.getOpenid()+"' ");
			}
			//连接数据库
			conn = new JdbcConnection().getOSPConnection();
			st = conn.createStatement();
			st.executeUpdate(sql.toString());
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
