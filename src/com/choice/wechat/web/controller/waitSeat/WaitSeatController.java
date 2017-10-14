package com.choice.wechat.web.controller.waitSeat;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.choice.test.utils.Commons;
import com.choice.test.utils.DateFormat;
import com.choice.test.utils.SendSMSByWxt.UTF8PostMethod;
import com.choice.wechat.common.AccessToken;
import com.choice.wechat.common.WeChatUser;
import com.choice.wechat.constants.bookMeal.BookMealConstants;
import com.choice.wechat.constants.waitSeat.WaitSeatConstants;
import com.choice.wechat.domain.bookDesk.Company;
import com.choice.wechat.domain.bookDesk.Firm;
import com.choice.wechat.domain.bookDesk.Sft;
import com.choice.wechat.domain.bookDesk.StoreTable;
import com.choice.wechat.domain.templateMsg.BaseTemplateMsg;
import com.choice.wechat.domain.templateMsg.MsgData;
import com.choice.wechat.persistence.bookDesk.BookDeskMapper;
import com.choice.wechat.persistence.bookMeal.BookMealMapper;
import com.choice.wechat.util.TemplateMsgUtil;
import com.choice.wechat.util.WeChatUtil;
import com.wxap.util.HttpClientUtil;

/**
 * 等位
 */
@Controller
@RequestMapping(value = "waitSeat")
public class WaitSeatController {

	@Autowired
	private BookMealMapper bookMealMapper;
	@Autowired
	private BookDeskMapper bookDeskMapper;

	@RequestMapping(value = "firmWaitInfo")
	public ModelAndView firmWaitInfo(ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response, String code, String state) {
		String pk_group = request.getParameter("pk_group");
		String pk_store = request.getParameter("pk_store");
		
		String openId = getOpenId(modelMap, request, pk_group, code, state);
		
		// 获取用户等位信息
		Map<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("wechat", openId);
		String userObjStr = executeHttpMethod("findSeatByUser.do", paraMap);
		if (null != userObjStr && !userObjStr.isEmpty()) {
			userObjStr = userObjStr.substring(1, userObjStr.length() - 1).replaceAll("\\\\", "");
			JSONObject userObj = JSONObject.fromObject(userObjStr);
			modelMap.put("userObj", userObj);
			if (null != userObj && userObj.containsKey("TBLNAME")) {
				return this.myWaitInfo(modelMap, request, code, state);
			}
		}
		
		// 查询最大人数
		int maxPerson = bookMealMapper.getMaxPerson(pk_store);
		modelMap.put("maxPerson", maxPerson);
		
		String firmCode = "";
		List<Firm> firmList = bookMealMapper.getFirmList(pk_group, null, pk_store);
		if(null != firmList && !firmList.isEmpty()) {
			Firm firm = firmList.get(0);
			firmCode = firm.getFirmCode();
			modelMap.put("firm", firm);
			
			String sft = "1"; //门店列表选择的订餐餐次,默认午市
			//根据时间确定餐次，判断前面选择的是否在具体门店的截止时间前

			Date nowDate = new Date();
			Calendar c = Calendar.getInstance(); 
			c.setTime(nowDate);
			int currHour = c.get(Calendar.HOUR_OF_DAY);
			if(null != firm.getLunchendtime() && !firm.getLunchendtime().isEmpty() 
					&& null != firm.getDinnerendtime() && !firm.getDinnerendtime().isEmpty()) {
				int startTime = Integer.parseInt(firm.getLunchendtime().split(":")[0]);
				int endTime = Integer.parseInt(firm.getDinnerendtime().split(":")[0]);
				if(currHour >= startTime && currHour <= endTime) {
					sft = "2"; //16点之后，晚市
				}
			} else {
				if(currHour >= 16) {
					sft = "2";
				}
			}
			
			modelMap.put("sft", sft);
		}
		
		getQueueInfo(modelMap, pk_group, firmCode, openId, pk_store, "firm");
		modelMap.put("code", code);
		
		//获取等位台位类型
		if(firmCode!=null && !"".equals(firmCode)){
			paraMap.clear();
			paraMap.put("pk_store",firmCode);
			String seatType = executeHttpMethod("queryTyp.do", paraMap);
			
			if(seatType!=null && !"".equals(seatType)){
				try{
					JSONArray array = JSONArray.fromObject(seatType.substring(1, seatType.length() - 1).replaceAll("\\\\", ""));
					Iterator<?> i = array.iterator();
					
					List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
					String defaultLineno = "";
					String defaultSeatType = "";
					boolean isHave = true;
					while(i.hasNext()){
						JSONObject object = (JSONObject) i.next();
						Map<String,Object> map = (Map<String,Object>) object.toBean(object, HashMap.class);
						list.add(map);
						if(isHave){
							if((Integer)map.get("minpax") <= 2 && 2 <= (Integer)map.get("maxpax")){
								defaultLineno = (String)map.get("vcode");
								defaultSeatType = (String)map.get("vname");
								isHave = false;
							}
						}
					}
					modelMap.put("defaultLineno", defaultLineno);
					modelMap.put("defaultSeatType", "".equals(defaultSeatType)?"系统自动分配":defaultSeatType);
					modelMap.put("seatTypeList", list);
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}
		}
		
		return new ModelAndView(WaitSeatConstants.FIRM_WAIT_INFO, modelMap);
	}

	/**
	 * 获取openid
	 * @param modelMap
	 * @param request
	 * @param pk_group
	 * @return
	 */
	private String getOpenId(ModelMap modelMap, HttpServletRequest request,
			String pk_group, String code, String state) {
		// 获取用户信息
		// 根据pk_group，获取配置信息
		Company company = WeChatUtil.getCompanyInfo(pk_group);

		String appId = Commons.appId;
		String secret = Commons.secret;
		String openId = request.getParameter("openId");
		if(null != company) {
			if (null != company.getAppId() && !"".equals(company.getAppId())) {
				appId = company.getAppId();
			}
			if (null != company.getSecret() && !"".equals(company.getSecret())) {
				secret = company.getSecret();
			}
		}
		
		if(null == openId || "".equals(openId)) {
			// 通过网页授权方式获取openID
			String oauth2[];
			try {
				oauth2 = WeChatUtil.getOAuth2(appId, secret, code);
				openId = oauth2[0];
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// 获取用户基本信息
		AccessToken token = WeChatUtil.getAccessToken(appId, secret);
		try {
			WeChatUser user = WeChatUtil.getWeChatUser(openId, token.getToken());
			modelMap.put("user", user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		modelMap.put("openId", openId);
		return openId;
	}
	
	/**
	 * 获取我的排号信息
	 * @param modelMap
	 * @param pk_group
	 * @param firmCode
	 * @param openId
	 * @param pk_store
	 * @return
	 */
	@RequestMapping(value = "getQueueInfo")
	@ResponseBody
	public Object getQueueInfo(ModelMap modelMap, String pk_group, String firmCode, String openId, String pk_store, String type) {
		try {
			// 获取用户等位信息
			Map<String, String> paraMap = new HashMap<String, String>();
			paraMap.put("wechat", openId);
			String userObjStr = this.executeHttpMethod("findSeatByUser.do", paraMap);
			userObjStr = userObjStr.substring(1, userObjStr.length() - 1).replaceAll("\\\\", "");
			JSONObject userObj = JSONObject.fromObject(userObjStr);
			if(null != userObj && userObj.containsKey("PK_STORE")) {
				modelMap.put("haveNumber", "Y");
				modelMap.put("userObj", userObj);
			}
			
			// 获取门店等位信息
			paraMap = new HashMap<String, String>();
			paraMap.put("pk_store", firmCode);
			String firmObjStr = this.executeHttpMethod("findSeatByFirm.do", paraMap);
			firmObjStr = firmObjStr.substring(1, firmObjStr.length() - 1).replaceAll("\\\\", "");
			JSONArray firmObj = JSONArray.fromObject(firmObjStr);
			
			// 设定台位信息
			/*List<StoreTable> tableList = getTableInfo(modelMap, pk_group, pk_store);
			for(StoreTable table : tableList) {
				// 设置当前叫号和当前取号
				for(Object obj : firmObj) {
					JSONObject json = (JSONObject)obj;
					if(null != table.getRoomtyp() && table.getRoomtyp().equals(json.get("TBLDES"))) {
						table.setCalldeNumber(json.getString("REC"));
						table.setTakedNumber(json.getString("CALLINGNO"));
						break;
					}
				}
				// 判断当前桌型是否已预订
				if(null != table.getRoomtyp() && userObj.containsKey("TBLNAME") && table.getRoomtyp().equals(userObj.get("TBLNAME"))
						&& pk_store.equals(userObj.get("PK_STORE"))) {
					table.setMyNumber(userObj.getString("REC"));
					table.setCalldeNumber(userObj.getString("SEATREC"));
					modelMap.put("isInQueue", "Y");
					continue;
				}
				table.setCalldeNumber("0");
				table.setTakedNumber("0");
			}*/
			
			List<StoreTable> lineNoList = bookDeskMapper.getLineNoList(pk_store);
			
			List<StoreTable> tableList = new ArrayList<StoreTable>();
			for(Object obj : firmObj) {
				JSONObject dataObj = (JSONObject)obj;
				StoreTable table = new StoreTable();
				table.setDes(dataObj.getString("TBLDES"));
				table.setCalldeNumber(dataObj.getString("CALLINGNO"));
				table.setTakedNumber(dataObj.getString("REC"));
				table.setWaitTblNum(dataObj.getString("TBLNUM"));
				if(dataObj.containsKey("WTIME")) {
					table.setWaitTime(dataObj.getString("WTIME"));
				} else {
					table.setWaitTime(String.valueOf(Integer.parseInt(dataObj.getString("TBLNUM")) * Integer.parseInt(Commons.getConfig().getProperty("avgWaitMinutes"))));
				}
				
				// 设置标准人数，最大最小人数
				for(StoreTable info : lineNoList) {
					if(dataObj.getString("TBLDES").equals(info.getDes())) {
						table.setPax(info.getPax());
						table.setMinpax(info.getMinpax());
						table.setMaxpax(info.getMaxpax());
					}
				}
				
				// 如果当前门店为用户等位门店
				if(null != userObj && userObj.containsKey("PK_STORE") && pk_store.equals(userObj.get("PK_STORE")) 
						&& dataObj.getString("TBLDES").equals(userObj.get("TBLNAME"))) {
					table.setMyNumber(userObj.getString("REC"));
					table.setCalldeNumber(userObj.getString("CALLINGNO"));
					table.setWaitTblNum(userObj.getString("TBLNUM"));
					if(dataObj.containsKey("WTIME")) {
						table.setWaitTime(dataObj.getString("WTIME"));
					} else {
						table.setWaitTime(String.valueOf(Integer.parseInt(dataObj.getString("TBLNUM")) * Integer.parseInt(Commons.getConfig().getProperty("avgWaitMinutes"))));
					}
					modelMap.put("isInQueue", "Y");
				}
				
				tableList.add(table);
			}
			modelMap.put("tableList", tableList);
			
			if("firm".equals(type)) {
				return tableList;
			}
			
			return userObj;
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 取号
	 * @param modelMap
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "takeNumber")
	@ResponseBody
	public Object takeNumber(ModelMap modelMap, HttpServletRequest request) {
		try {
			String firmId = request.getParameter("firmId");
			String personNum = request.getParameter("personNum");
			String tele = request.getParameter("tele");
			String openId = request.getParameter("openId");
			String lineno = request.getParameter("lineno");
			String from = "wechat";
			
			Map<String, String> paraMap = new HashMap<String, String>();
			paraMap.put("pk_store", firmId);
			paraMap.put("pnum", personNum);
			paraMap.put("tele", tele);
			paraMap.put("wechat", openId);
			paraMap.put("lineno", lineno);
			
			// 调用总部BOH接口获取排队号，包括当前叫到几号，排在第几号
			// take number from BOH
			String numStr = this.executeHttpMethod("takeNO.do", paraMap);
			//String[] splitStr = numStr.split("-");
			
			//obj.put("myNum", "10");
			//obj.put("nowNum", "3");
			
			JSONObject obj = new JSONObject();
			
			if(numStr.indexOf("ERROR") >= 0) {
				obj.put("myNum", numStr);
				obj.put("ERRORCODE", numStr.substring(numStr.indexOf("-"), numStr.length()));
			} else {
				//obj.put("myNum", splitStr[splitStr.length - 1].replaceAll("\"", ""));
				obj.put("myNum", numStr);
				
				String pk_group = request.getParameter("pk_group");
				String pk_firm = request.getParameter("pk_firm");
				//根据pk_group，获取配置信息
				Company company = WeChatUtil.getCompanyInfo(pk_group);
				String appId = Commons.appId;
				String secret = Commons.secret;
				if(null != company) {
					if (null != company.getAppId() && !"".equals(company.getAppId())) {
						appId = company.getAppId();
					}
					if (null != company.getSecret() && !"".equals(company.getSecret())) {
						secret = company.getSecret();
					}
				}
				
				AccessToken token = WeChatUtil.getAccessToken(appId, secret);

				String tempid = Commons.getConfig().getProperty("TM00530");
				BaseTemplateMsg msg = new BaseTemplateMsg();
				msg.setTouser(openId);
				msg.setTemplate_id(tempid);
				//msg.setTopcolor("#000000");

				StringBuffer sb = request.getRequestURL();
				String baseUrl = sb.substring(0, sb.lastIndexOf("/"));
				sb.delete(0, sb.length());
				sb.append("https://open.weixin.qq.com/connect/oauth2/authorize?appid=").append(appId)
					.append("&redirect_uri=").append(baseUrl)
					.append("/myWaitInfo.do?pk_store=").append(firmId)
					.append("&response_type=code&scope=snsapi_base&state=123#wechat_redirect");
				msg.setUrl(sb.toString());
				
				Map<String,MsgData> map = new HashMap<String,MsgData>();
				MsgData data = new MsgData();
				data.setValue("您的等位订单已受理成功");
				map.put("first", data);
				String firmName = bookDeskMapper.getFirmName(pk_firm);
				data = new MsgData();
				data.setValue(firmName);
				map.put("storeName", data);

				data = new MsgData();
				data.setValue(numStr);
				map.put("orderId", data);

				data = new MsgData();
				data.setValue("等位");
				map.put("orderType", data);

				data = new MsgData();
				data.setValue("感谢您的光临，希望您下次再来");
				map.put("remark", data);

				msg.setData(map);
				TemplateMsgUtil.sendTmplateMsg(token.getToken(), msg);
			}
			obj.put("beforeMe", "");
			
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 取消排队
	 * @param modelMap
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "cancelQueue")
	@ResponseBody
	public Object cancelQueue(ModelMap modelMap, HttpServletRequest request) {
		try {
			String pk_store = request.getParameter("pk_store");
			String openId = request.getParameter("openId");
			String lineno = request.getParameter("lineno");
			String from = "wechat";
			
			// 调用总部BOH接口取消排队
			Map<String, String> paraMap = new HashMap<String, String>();
			paraMap.put("pk_store", pk_store);
			paraMap.put("wechat", openId);
			paraMap.put("sta", "C");
			paraMap.put("rec", "0");
			paraMap.put("lineno", lineno);
			
			String objStr = this.executeHttpMethod("cancelSeat.do", paraMap);
			JSONObject obj = new JSONObject();
			if(null != objStr && "\"0\"".equals(objStr)) {
				String myNum = request.getParameter("myNum");
				String pk_group = request.getParameter("pk_group");
				String firmid = request.getParameter("firmid");
				
				//根据pk_group，获取配置信息
				Company company = WeChatUtil.getCompanyInfo(pk_group);
				String appId = Commons.appId;
				String secret = Commons.secret;
				if(null != company) {
					if (null != company.getAppId() && !"".equals(company.getAppId())) {
						appId = company.getAppId();
					}
					if (null != company.getSecret() && !"".equals(company.getSecret())) {
						secret = company.getSecret();
					}
				}
				
				AccessToken token = WeChatUtil.getAccessToken(appId, secret);

				String tempid = Commons.getConfig().getProperty("TM00534");
				BaseTemplateMsg msg = new BaseTemplateMsg();
				msg.setTouser(openId);
				msg.setTemplate_id(tempid);
				//msg.setTopcolor("#000000");

				StringBuffer sb = request.getRequestURL();
				String baseUrl = sb.substring(0, sb.lastIndexOf("/"));
				sb.delete(0, sb.length());
				sb.append("https://open.weixin.qq.com/connect/oauth2/authorize?appid=").append(appId)
					.append("&redirect_uri=").append(baseUrl)
					.append("/myWaitInfo.do?pk_store=").append(pk_store)
					.append("&response_type=code&scope=snsapi_base&state=123#wechat_redirect");
				msg.setUrl(sb.toString());
				
				Map<String,MsgData> map = new HashMap<String,MsgData>();
				MsgData data = new MsgData();
				data.setValue("您的等位订单已取消成功");
				map.put("first", data);
				String firmName = bookDeskMapper.getFirmName(firmid);
				data = new MsgData();
				data.setValue(firmName);
				map.put("storeName", data);

				data = new MsgData();
				data.setValue(myNum);
				map.put("orderId", data);

				data = new MsgData();
				data.setValue("等位");
				map.put("orderType", data);

				data = new MsgData();
				data.setValue("感谢您的光临，希望您下次再来");
				map.put("remark", data);

				msg.setData(map);
				TemplateMsgUtil.sendTmplateMsg(token.getToken(), msg);
			}
			
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JSONObject obj = new JSONObject();
		obj.put("result", "NG");
		
		return obj;
	}
	
	/**
	 * 我的等位
	 * @param modelMap
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "myWaitInfo")
	public ModelAndView myWaitInfo(ModelMap modelMap, HttpServletRequest request, String code, String state) {
		String pk_group = request.getParameter("pk_group");
		modelMap.put("pk_group", pk_group);
		modelMap.put("code", code);
		String openId = getOpenId(modelMap, request, pk_group, code, state);
		// 获取用户等位信息
		Map<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("wechat", openId);
		String userObjStr = this.executeHttpMethod("findSeatByUser.do", paraMap);
		if(null != userObjStr && !userObjStr.isEmpty()) {
			userObjStr = userObjStr.substring(1, userObjStr.length() - 1).replaceAll("\\\\", "");
			if(!"null".equals(userObjStr)) {
				JSONObject userObj = JSONObject.fromObject(userObjStr);
				/*if(null != userObj && !userObj.containsKey("WTIME")) {
					userObj.put("WTIME", Integer.parseInt(userObj.getString("TBLNUM")) * Integer.parseInt(Commons.getConfig().getProperty("avgWaitMinutes")));
				}*/
				modelMap.put("userObj", userObj);
				if(null != userObj && userObj.containsKey("TBLNAME")) {
					modelMap.put("isInQueue", "Y");
					String pk_store = userObj.getString("PK_STORE");
					List<Firm> firmList = bookMealMapper.getFirmList(pk_group, null, pk_store);
					if(null != firmList && !firmList.isEmpty()) {
						Firm firm = firmList.get(0);
						modelMap.put("firm", firm);

						String sft = "1"; //门店列表选择的订餐餐次,默认午市
						//根据时间确定餐次，判断前面选择的是否在具体门店的截止时间前

						Date nowDate = new Date();
						Calendar c = Calendar.getInstance(); 
						c.setTime(nowDate);
						int currHour = c.get(Calendar.HOUR_OF_DAY);
						if(null != firm.getLunchendtime() && !firm.getLunchendtime().isEmpty() 
								&& null != firm.getDinnerendtime() && !firm.getDinnerendtime().isEmpty()) {
							int startTime = Integer.parseInt(firm.getLunchendtime().split(":")[0]);
							int endTime = Integer.parseInt(firm.getDinnerendtime().split(":")[0]);
							if(currHour >= startTime && currHour <= endTime) {
								sft = "2"; //16点之后，晚市
							}
						} else {
							if(currHour >= 16) {
								sft = "2";
							}
						}
						
						modelMap.put("sft", sft);
					}
				}
			}
		}
		// 平均每桌等待分钟数
		modelMap.put("avgWaitMinutes", Commons.avgWaitMinutes);
		modelMap.put("waitDate", DateFormat.getStringByDate(new Date(), "yyyy-MM-dd"));
		
		return new ModelAndView(WaitSeatConstants.MY_WAIT_INFO, modelMap);
	}
	
	/**
	 * 获取门店台位信息
	 * @param modelMap
	 * @param pk_group
	 * @param pk_store
	 * @return
	 */
	/*public List<StoreTable> getTableInfo(ModelMap modelMap, String pk_group, String pk_store) {

		List<StoreTable> listStoreTable = bookDeskMapper.getDeskFormFirm(pk_group, pk_store, sft, dat);
		
		return listStoreTable;
	}*/
	
	/*private String executeHttpMethod() {
		HttpClient httpClient = new HttpClient(); // 创建请求对象
		httpClient.getHostConfiguration().setHost("61.156.157.248", 8889, "http"); // 设置请求地址、端口及协议
		
		try {
			PostMethod method = new UTF8PostMethod("/api.php"); // 新建一个UTF-8编码的POST请求
			method.addParameter("username", "yourUsername"); // 发送参数：用户名
			method.addParameter("password", "yourPassword"); // 发送参数：密码
			method.addParameter("action", "surplus"); // 发送参数：动作：查询余额
			httpClient.executeMethod(method); // 发送请求
			String result = request(method); // 获取返回内容
			method.releaseConnection(); // 释放链接，防止请求过多堵塞网络
			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";
	}*/

	/**
	 * 从请求中读取字符串
	 * 
	 * @param method
	 * @return
	 */
	private static String request(HttpMethod method) {
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), "UTF-8"));
			String resultStr = "";
			String line = null;
			while ((line = br.readLine()) != null) {
				resultStr += line + "\r\n";
			}
			method.releaseConnection();
			resultStr = resultStr.replaceAll("\\s", "");
			return resultStr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	private String executeHttpMethod(String methodName, Map<String, String> paraMap) {
		StringBuilder sbUrl = new StringBuilder(Commons.BOHUrl);
		sbUrl.append("waitSeat/").append(methodName).append("?");
		for(Map.Entry<String, String> entry: paraMap.entrySet()) {
			sbUrl.append(entry.getKey())
			.append("=")
			.append(entry.getValue())
			.append("&");
		}
		// 删除最后一个&
		String reuqestUrl = sbUrl.substring(0, sbUrl.lastIndexOf("&"));
		try {
			HttpURLConnection conn = HttpClientUtil.getHttpURLConnection(reuqestUrl);
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(0);
			conn.setInstanceFollowRedirects(true);			
			conn.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
			conn.setDefaultUseCaches(false);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.connect();

			OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8"); // utf-8编码
			//out.write(json.toString());//传递参数
			out.flush();
			out.close();
			DataInputStream dis = new DataInputStream(conn.getInputStream());
			byte []buf = new byte[1024 * 1024 * 2];
			int len=0;
			StringBuffer sb = new StringBuffer("");
			while((len = dis.read(buf)) != -1) {
				sb.append(new String(buf,0,len,"UTF-8"));
			}
			conn.disconnect();
			String resultStr = sb.toString();
			/*if(!"".equals(resultStr) && !"\"null\"".equals(resultStr)) {
				JSONObject jsonStr = JSONObject.fromObject(sb.toString());
				return jsonStr;
			}*/
			return resultStr;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 根据城市获取门店列表
	 * @param modelMap
	 * @param pk_city
	 * @return
	 */
	@RequestMapping(value="getFirmByCity")
	@ResponseBody
	public List<Firm> getFirmByCity(ModelMap modelMap, String pk_city){
		List<Firm> listFirm = null;
		try{
			Map<String, String> param = new HashMap<String, String>();
			param.put("pk_city", pk_city);
			//根据配置文件过滤品牌
			String brandFilter = Commons.getConfig().getProperty("brandFilter");
			if(brandFilter!=null && !"".equals(brandFilter)){
				StringBuilder brand_sb = new StringBuilder();
				if(brandFilter.indexOf(",")>-1){
					String brand_keys[] = brandFilter.split(",");
					for(int i=0;i<brand_keys.length;i++){
						brand_sb.append("'").append(brand_keys[i]).append("',");
					}
					brand_sb.deleteCharAt(brand_sb.length()-1);
				}else{
					brand_sb.append("'").append(brandFilter).append("'");
				}
				brandFilter = brand_sb.toString();
			}
			param.put("brandFilter", brandFilter);
			
			listFirm = bookDeskMapper.getFirmList(param);
		}catch(Exception e){
			e.printStackTrace();
		}
		return listFirm;
	}
}