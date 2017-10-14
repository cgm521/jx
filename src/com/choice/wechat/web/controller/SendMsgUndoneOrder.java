package com.choice.wechat.web.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

import net.sf.json.JSONObject;

import com.choice.test.utils.Commons;
import com.choice.test.utils.DateFormat;
import com.choice.test.utils.JdbcConnection;
import com.choice.wechat.util.LogUtil;
import com.choice.wechat.util.TemplateMsgUtil;
import com.choice.wechat.util.ValueCheck;
import com.choice.wechat.util.WeChatUtil;
import com.google.gson.JsonObject;

/**
 * 自动处理下单失败的订单，发送消息给用户
 * @author ZGL
 * @date 2016-02-01 09:49:42
 *
 */
public class SendMsgUndoneOrder  extends TimerTask {

	@Override
	public void run() {
		
		Connection conn = null;
		Statement st = null;
		Statement stUpdate = null;
		try {
			// 连接数据库
			conn = new JdbcConnection().getTELEConnection();
			st = conn.createStatement();
			stUpdate = conn.createStatement();

			String dat = DateFormat.getStringByDate(new Date(),"yyyy-MM-dd");
//			String findOrderSql = "SELECT O.ID,O.RESV,O.OPENID,O.TABLES,S.VNAME AS FIRMNAME,ST.VNAME AS STNAME,O.FIRMID,W.VTYPE " +
//					" FROM NET_ORDERS O " +
//					" LEFT JOIN WX_MQLOGS W ON O.ID=W.ORDERID " +
//					" LEFT JOIN CBOH_STORE_3CH S ON O.FIRMID=S.PK_STORE " +
//					" LEFT JOIN CBOH_SITEDEFINE_3CH ST ON O.FIRMID=ST.PK_STOREID AND O.TABLES = ST.VCODE " +
//					" WHERE O.DAT = to_date('" + dat + "','yyyy-mm-dd') AND O.STATE IN ('1','2') AND W.STATE IS NULL  AND O.ORDFROM='WECHAT' AND O.OPENID IS NOT NULL ";
			String findOrderSql = "SELECT O.ID,O.RESV,O.OPENID,O.TABLES,S.VNAME AS FIRMNAME,ST.VNAME AS STNAME,O.FIRMID,W.VTYPE,W.PK_MQLOGS " +
					" FROM NET_ORDERS O ,WX_MQLOGS W , CBOH_STORE_3CH S,CBOH_SITEDEFINE_3CH ST " +
					" WHERE O.ID=W.ORDERID AND O.FIRMID=S.PK_STORE AND O.FIRMID=ST.PK_STOREID AND O.TABLES = ST.VCODE " +
					" AND O.DAT = to_date('" + dat + "','yyyy-mm-dd') AND O.STATE IN ('1','2') AND W.STATE IS NULL  AND  VSENDMSG='N' " +
					" AND O.ORDFROM='WECHAT' AND O.OPENID IS NOT NULL ";
			ResultSet rs = st.executeQuery(findOrderSql);
			
			String token = WeChatUtil.getAccessToken(Commons.appId,Commons.secret).getToken();
			String typShow = "";
			while(rs.next()) {//将查询的结果处理发送消息
				Map<String,String> messageMap = new HashMap<String,String>();
				messageMap.put("templateCode", "OPENTM213722270");//微信的模版编号(待处理通知)
				messageMap.put("openid", rs.getString("OPENID"));
				messageMap.put("first","");
				
				if("1".equals(rs.getString("VTYPE"))){
					typShow = "下单";
				}else if("3".equals(rs.getString("VTYPE"))){
					typShow = "加菜";
				}else{
					typShow = "状态修改";
				}
				messageMap.put("keyword1","您在【"+rs.getString("FIRMNAME")+"】台位"+(ValueCheck.IsEmpty(rs.getString("STNAME"))==true?rs.getString("TABLES"):rs.getString("STNAME"))+"的账单："+rs.getString("RESV")+typShow+"失败，请重新扫码下单。点击查看详情");
				messageMap.put("keyword2",Commons.vtitle);
				messageMap.put("keyword3",DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
				messageMap.put("remark","");
				String url = Commons.vtakeOrdrurl.replace("gotoMenu", "orderDetail")
						+ "?orderid=" + rs.getString("ID")
						+ "&openid=" + rs.getString("OPENID")
						+ "&firmid=" + rs.getString("FIRMID");
				messageMap.put("url", url);
				String resultSend = TemplateMsgUtil.sendTemplateMsg(token,messageMap);
				LogUtil.writeToTxt("autosendMsg", "客户下单pos没有返回消息时，自动发送通知信息内容【"+JSONObject.fromObject(messageMap)+"】结果"+resultSend);
				String updateMqlogs = "UPDATE WX_MQLOGS SET VSENDMSG='Y' WHERE PK_MQLOGS = '"+rs.getString("PK_MQLOGS")+"'";
				int len = stUpdate.executeUpdate(updateMqlogs);
				LogUtil.writeToTxt("autosendMsg", "客户下单pos没有返回消息时，自动发送通知信息更新发送消息标志【"+updateMqlogs+"】更新结果"+len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != stUpdate) {
					stUpdate.close();
				}
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
