package com.choice.wechat.domain.templateMsg.template;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.choice.test.utils.Commons;
import com.choice.wechat.domain.templateMsg.BaseTemplateMsg;
import com.choice.wechat.domain.templateMsg.MsgData;

public abstract class MsgStructure {
	
	public abstract String[] getItems();
	/**
	 * 
	 * @param templateCode 模版编码
	 * @param param HttpServletRequest.getParameterMap
	 * @return
	 * 通过http协议调用
	 */
	public BaseTemplateMsg initMsgHttp(Map<String,String[]> param){
		BaseTemplateMsg baseMsg = null;
		
		String templateCode = getValue("templateCode",param);
		if(StringUtils.hasText(templateCode) && param!=null && !param.isEmpty()){
			baseMsg = new BaseTemplateMsg();
			baseMsg.setTemplate_id(Commons.getConfig().getProperty(templateCode));
			
			String openid = getValue("openid",param);
			if(openid==null || "".equals(openid)){
				return null;
			}
			baseMsg.setTouser(openid);
			//baseMsg.setTopcolor("#000000");
			
			baseMsg.setUrl(getValue("url",param));

			Map<String,MsgData> map = new HashMap<String,MsgData>();
			MsgData data = null;
			for(String item : getItems()){
				data = new MsgData();
				data.setValue(getValue(item,param));
				map.put(item, data);
			}
			baseMsg.setData(map);
		}
		
		return baseMsg;
	}
	
	/**
	 * 
	 * @param templateCode 模版编码
	 * @param param
	 * @return
	 * 本地调用
	 */
	public BaseTemplateMsg initMsg(Map<String,String> param){
		BaseTemplateMsg baseMsg = null;
		
		String templateCode = param.get("templateCode");
		if(StringUtils.hasText(templateCode) && param!=null && !param.isEmpty()){
			baseMsg = new BaseTemplateMsg();
			baseMsg.setTemplate_id(Commons.getConfig().getProperty(templateCode));
			
			String openid = param.get("openid");
			if(openid==null || "".equals(openid)){
				return null;
			}
			baseMsg.setTouser(openid);
			//baseMsg.setTopcolor("#000000");
			
			baseMsg.setUrl(param.get("url"));

			Map<String,MsgData> map = new HashMap<String,MsgData>();
			MsgData data = null;
			for(String item : getItems()){
				data = new MsgData();
				data.setValue(param.get(item));
				data.setColor("#000000");
				map.put(item, data);
			}
			baseMsg.setData(map);
		}
		
		return baseMsg;
	}
	
	/**
	 * 
	 * @param key
	 * @param param
	 * @return
	 */
	private String getValue(String key, Map<String,String[]> param){
		if(key == null || "".equals(key)){
			return null;
		}
		
		String[] tempValues = param.get(key);
		if(tempValues == null || tempValues.length<0){
			return "";
		}
		return tempValues[0];
	}
}
