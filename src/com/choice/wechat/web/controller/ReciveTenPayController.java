package com.choice.wechat.web.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.choice.test.utils.CodeHelper;
import com.choice.test.utils.Commons;
import com.choice.test.utils.DateFormat;
import com.choice.test.utils.JdbcConnection;
import com.choice.wechat.util.CallWebService;
import com.choice.wechat.util.LogUtil;
import com.choice.wechat.util.ValueCheck;
import com.choice.wechat.util.WeChatUtil;
import com.wxap.util.MD5SignUtil;
import com.wxap.util.Sha1Util;

/**
 * 同步财付通对账单数据
 * @author ZGL
 * @date 2015-09-25 10:55:58
 *
 */
public class ReciveTenPayController extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 62300517331675127L;
	
	/**
	 * 执行接收消息方法
	 * 
	 * @throws Exception
	 */
	private final static String insertTenPayOrderSql = "INSERT INTO cboh_tenpayOrders_3ch(pk_enpayOrders,VTRANSACTIONID,pk_orders,vtype,vstatus,vbankorderid,nmoney,vbackorderid,vbackmoney,vbackstatus,nfee,nfeerate,vmemo,ts,partner,dat) values(?)";
	private final static String updateTenPayOrderSql = "INSERT INTO WX_MQLOGS(PK_MQLOGS,ORDERID,VTYPE,ERRMSG,STATE) values(?,?,?,?,?)";
	
	public void init() throws ServletException {
		super.init();
		
		Connection conn = null;
		Statement st = null;
		try {
			// 连接数据库
			conn = new JdbcConnection().getTELEConnection();
			st = conn.createStatement();

			String dat = DateFormat.getStringByDate(
					DateFormat.getDateBefore(new Date(), "day", -1, 1),
					"yyyy-MM-dd");
			String findTenPayOrderSql = "SELECT COUNT(1) AS CNT FROM cboh_tenpayOrders_3ch WHERE DAT = '" + dat + "' AND PARTNER = '" + Commons.partner + "'";
			ResultSet rs = st.executeQuery(findTenPayOrderSql);
			
			int cnt = 0;
			while(rs.next()) {
				cnt = rs.getInt("CNT");
			}
			
			if(cnt <= 0) {
				// 启动时，若没有上一天数据，从财付通取
				tenPayOrderInterface();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != st) {
					st.close();
				}
				if (null != conn) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void tenPayOrderInterface() throws Exception {
		//String urll = "http://mch.tenpay.com/cgi-bin/mchdown_real_new.cgi";
		String urll = "https://api.mch.weixin.qq.com/pay/downloadbill";
		// 连接数据库
		Connection conn = new JdbcConnection().getTELEConnection();
		Statement st = conn.createStatement();
		try {
			String dat = DateFormat.getStringByDate(
					DateFormat.getDateBefore(new Date(), "day", -1, 1),
					"yyyy-MM-dd");

			// 取数据之前先删除已有数据
			String deleteTenPayOrderSql = "DELETE FROM cboh_tenpayOrders_3ch WHERE DAT = '" + dat + "' AND PARTNER = '" + Commons.partner + "'";
			st.executeUpdate(deleteTenPayOrderSql);
			
			//对账单下载服务商模式配置 0-非服务商模式 1-服务商模式
			String tenPaySeriveFlag = Commons.getConfig().getProperty("tenPaySeriveFlag");
			//商户密钥
			String partner_key = Commons.partner_key;
			
			SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
			if("1".equals(tenPaySeriveFlag)) {
				//服务商模式
				parameters.put("appid", "wx35f67221fe52ad3c");//服务商appid
				parameters.put("mch_id", "1271747601");//服务商商户号
				parameters.put("sub_appid", Commons.appId);
				parameters.put("sub_mch_id", Commons.partner);
				partner_key = "2b954a077b3d40e398b93a76597cc9e7";//服务商商户密钥
			} else {
				//非服务商模式
				parameters.put("appid", Commons.appId);
				parameters.put("mch_id", Commons.partner);
			}
			
			parameters.put("nonce_str",  Sha1Util.getNonceStr());
			parameters.put("bill_date", dat.replaceAll("-", ""));
			parameters.put("bill_type", "ALL");

			String orderPar = WeChatUtil.orderParams(parameters);
			String sign = MD5SignUtil.Sign(orderPar, partner_key);
			parameters.put("sign", sign);
			String requestXML = WeChatUtil.getRequestXml(parameters);
			String sss = WeChatUtil.httpRequestNew(urll, "POST", requestXML);
			
			/*String orderPar = "spid=" + Commons.partner + "&trans_time=" + dat + "&stamp=" + Sha1Util.getTimeStamp();
		    String sign = MD5SignUtil.Sign(orderPar, Commons.partner_key).toLowerCase();
		    orderPar += "&sign="+sign;
		    String sss = CallWebService.httpUrlConnection(urll, orderPar);*/
			
			if (ValueCheck.IsNotEmpty(sss)) {
				if (sss.indexOf("<html>") > -1) {
					LogUtil.writeToTxt("tenPayOrder", "同步" + dat
							+ "时的对账单没有返回有效数据，错误信息：" + sss);
				} else {
					sss = sss.replaceAll(dat, "\n" + dat);
					sss = sss.replaceAll("总交易单数", "\n总交易单数");
					
					String[] listS = sss.split("\n");// 拆解成数组
					for (int i = 0; i < listS.length; i++) {
						if (i == 0 || i == listS.length - 1) {// 第一行，最后一行不做处理
							continue;
						}
						String rs = listS[i];
						if (ValueCheck.IsNotEmpty(rs)) {
							//﻿0交易时间,1公众账号ID,2商户号,3子商户号,4设备号,5微信订单号,6商户订单号,7用户标识,8交易类型,9交易状态,10付款银行,
							//11货币种类,12总金额,13企业红包金额,14微信退款单号,15商户退款单号,16退款金额,17企业红包退款金额,18退款类型,19退款状态,20商品名称,21商户数据包,22手续费,23费率
							String[] ls = rs.split(",");
							StringBuffer setSql = new StringBuffer();
							setSql.append("'" + CodeHelper.createUUID() + "'");
							setSql.append(",'" + ls[5].trim().replace("`", "")
									+ "'");
							setSql.append(",'" + ls[6].trim().replace("`", "")
									+ "'");
							setSql.append(",'" + ls[8].trim().replace("`", "")
									+ "'");
							setSql.append(",'" + ls[9].trim().replace("`", "")
									+ "'");
							setSql.append(",'" + ls[14].trim().replace("`", "")
									+ "'");
							setSql.append(",'" + ls[12].trim().replace("`", "")
									+ "'");
							setSql.append(",'" + ls[15].trim().replace("`", "")
									+ "'");
							setSql.append(",'" + ls[16].trim().replace("`", "")
									+ "'");
							setSql.append(",'" + ls[19].trim().replace("`", "")
									+ "'");
							setSql.append(",'" + ls[22].trim().replace("`", "")
									+ "'");
							setSql.append(",'" + ls[23].trim().replace("`", "")
									+ "'");
							setSql.append(",'" + ls[20].trim().replace("`", "")
									+ "'");
							setSql.append(",'" + ls[0].trim().replace("`", "")
									+ "'");
							setSql.append(",'" + Commons.partner
									+ "'");
							setSql.append(",'" + dat
									+ "'");
							
							try {
								int rss = st.executeUpdate(insertTenPayOrderSql
										.replace("?", setSql.toString()));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					LogUtil.writeToTxt("tenPayOrder", "同步" + dat + "时的对账单数据成功，共返回" + (listS.length-3) + "条数据。");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != st) {
					st.close();
				}
				if (null != conn) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
