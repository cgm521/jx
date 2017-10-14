package com.choice.wechat.util;

import java.util.HashMap;
import java.util.Map;

import com.choice.test.utils.Commons;
import com.choice.wechat.domain.templateMsg.BaseTemplateMsg;
import com.choice.wechat.domain.templateMsg.template.MsgStructure;
import com.wxap.util.MD5Util;

import net.sf.json.JSONObject;

public class TemplateMsgUtil {
	public final static String getTemplateIDUrl = "https://api.weixin.qq.com/cgi-bin/template/api_add_template?access_token=ACCESS_TOKEN";
	/**
	 * 
	 * @param code 模版编号 TM00015
	 * @return
	 */
	public static String getTemplateID(String access_token, String code){
		String requestUrl = getTemplateIDUrl.replace("ACCESS_TOKEN", access_token);
		
		StringBuilder sb = new StringBuilder();
		sb.append("{")
			.append("\"template_id_short\":\"")
			.append(code)
			.append("\"}");
		JSONObject json = WeChatUtil.httpRequest(requestUrl, "POST", sb.toString());
		String template_id = json.getString("template_id");
		
		return template_id;
	}
	
	public final static String sendTmplateMsgUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
	/**
	 * 
	 * @param access_token
	 * @param msg  BaseTemplateMsg
	 * @return
	 */
	public static String sendTmplateMsg(String access_token, BaseTemplateMsg msg){
		String requestUrl = sendTmplateMsgUrl.replace("ACCESS_TOKEN", access_token);
		
		JSONObject json = JSONObject.fromObject(msg);
		JSONObject result = WeChatUtil.httpRequest(requestUrl, "POST", json.toString());
		LogUtil.writeToTxt(LogUtil.BUSINESS, "mq消息：" + json.toString() + ",发送结果：" + result + System.getProperty("line.separator"));
		return result.getString("errcode");
	}
	
	/**
	 * 
	 * @param access_token
	 * @param msg json字符串
	 * @return
	 */
	public static String sendTmplateMsg(String access_token, String msg){
		String requestUrl = sendTmplateMsgUrl.replace("ACCESS_TOKEN", access_token);
		
		JSONObject result = WeChatUtil.httpRequest(requestUrl, "POST", msg);
		LogUtil.writeToTxt(LogUtil.BUSINESS, "mq消息：" + msg + ",发送结果：" + result + System.getProperty("line.separator"));
		return result.getString("errcode");
	}
	
	/**
	 * 
	 * @param access_token
	 * @param param
	 * @return
	 */
	public static String sendTemplateMsg(String access_token, Map<String,String> param){
		if(param!=null){
			BaseTemplateMsg msg = structureMsg(param);
			if(msg!=null){
				if("1".equals(Commons.getConfig().getProperty("thirdPartyPlatformCode"))) {
					msg.setTemplate_id_short(param.get("templateCode"));
					return sendTemplateMsgByAqua(msg);
				}
				return sendTmplateMsg(access_token,msg);
			}
		}
		return "-999999";
	}
	
	/**
	 * 
	 * @param access_token
	 * @param param
	 * @return
	 */
	public static String sendTemplateMsgHttp(String access_token, Map<String,String[]> param){
		if(param!=null){
			BaseTemplateMsg msg = structureMsgHttp(param);
			if(msg!=null){
				return sendTmplateMsg(access_token,msg);
			}
		}
		return "-999999";
	}
	
	private static BaseTemplateMsg structureMsg(Map<String,String> param){
		try {
			String templateCode = param.get("templateCode");
			if(templateCode==null || "".equals(templateCode)){
				return null;
			}
			Class<MsgStructure> clazz = MsgStructure.class;
			StringBuilder sb = new StringBuilder(clazz.getPackage().getName());
			sb.append(".MSG_");
			sb.append(templateCode);
			MsgStructure structure = (MsgStructure) Class.forName(sb.toString()).newInstance();//可优化为单例
			return structure.initMsg(param);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static BaseTemplateMsg structureMsgHttp(Map<String,String[]> param){
		
		try {
			String templateCode = param.get("templateCode")[0];
			if(templateCode==null || "".equals(templateCode)){
				return null;
			}
			Class<MsgStructure> clazz = MsgStructure.class;
			StringBuilder sb = new StringBuilder(clazz.getPackage().getName());
			sb.append(".MSG_");
			sb.append(templateCode);
			MsgStructure structure = (MsgStructure) Class.forName(sb.toString()).newInstance();
			return structure.initMsgHttp(param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 使用绿数接口推送模板消息
	 * @param msg
	 * @return
	 */
	private static String sendTemplateMsgByAqua(BaseTemplateMsg msg) {
		String time = String.valueOf(System.currentTimeMillis()).substring(0, 10);
		msg.setKey(Commons.getConfig().getProperty("aquaKey"));
		msg.setTime(time);
		msg.setToken(getTokenFromAqua(time));
		msg.setPub_id(MessageUtil.getUser(msg.getTouser()).getPubid());
		msg.setTopcolor("#FF0000");
		
		JSONObject json = JSONObject.fromObject(msg);
		LogUtil.writeToTxt("SendTemplateMessage", "推送模板消息：" + json.toString());
		String url = Commons.getConfig().getProperty("thirdPartyPlatformUrl") + "sendTemplateMsg";
		String result = WeChatUtil.httpRequestReturnString(url, "POST", json.toString());
		
		return result;
	}
	
	/**
	 * 获取绿数加密信息
	 * @param time
	 * @return
	 */
	private static String getTokenFromAqua(String time) {
		String key = Commons.getConfig().getProperty("aquaKey");
		String secret = Commons.getConfig().getProperty("aquaSecret");
		
		String src = key + secret + time;
		return MD5Util.MD5Encode(src, "UTF-8");
	}
	
	public static void main(String args[]){
		Map<String,String> map = new HashMap<String,String>();
		map.put("templateCode", "TM00530");//必选
		map.put("openid","oubXxt4PHXaOw_CkiwhzrlYNCdLQ");//必选
		map.put("first","您好，已接收到您的点菜订单");
		map.put("storeName","测试店");
		map.put("orderId","1512171001");
		map.put("orderType","堂食");
		map.put("remark","感谢您的光临，请尽快进店扫码下单");
		
		//String token = WeChatUtil.getAccessToken("wx0bfdd531a019d59d","41c118a6bf0c1db2f1554bc3500fe7c8").getToken();
		//System.out.println(token);
		//sendTemplateMsg("WTBlDtr4vtt_3N46KJVRvIXQx92q0htuRNiCAVp_b188ox0nP8IvYSW9WPoYnJMy3iMAgIJheBEqr0IIDtG7eViqF3v-lUj99cbHTL_CEPo",map);
		BaseTemplateMsg msg = structureMsg(map);
		msg.setTemplate_id_short(map.get("templateCode"));
		sendTemplateMsgByAqua(msg);
	}
}
