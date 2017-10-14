package com.choice.wechat.web.controller;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.choice.test.domain.Net_OrderDtl;
import com.choice.test.domain.Net_OrderPackageDetail;
import com.choice.test.domain.Net_Orders;
import com.choice.test.utils.CodeHelper;
import com.choice.test.utils.Commons;
import com.choice.test.utils.DateFormat;
import com.choice.test.utils.GenerateRandom;
import com.choice.test.utils.Utils;
import com.choice.wechat.common.AccessToken;
import com.choice.wechat.domain.ListMapMapper;
import com.choice.wechat.domain.bookDesk.Company;
import com.choice.wechat.domain.bookMeal.NetDishAddItem;
import com.choice.wechat.domain.bookMeal.NetDishProdAdd;
import com.choice.wechat.persistence.bookMeal.BookMealMapper;
import com.choice.wechat.persistence.common.location.LocationMapper;
import com.choice.wechat.util.LogUtil;
import com.choice.wechat.util.MessageUtil;
import com.choice.wechat.util.SignUtil;
import com.choice.wechat.util.TemplateMsgUtil;
import com.choice.wechat.util.ValueCheck;
import com.choice.wechat.util.WeChatUtil;
import com.choice.wxc.util.HttpRequestUtil;
import com.wxap.util.HttpClientUtil;

@Controller
@RequestMapping(value="weixin")
public class ChoiceMessageController{

	private static String token = "mytoken";
	
	@Autowired
	private LocationMapper locationMapper;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private BookMealMapper bookMealMapper;
	
	@RequestMapping(value="handleMessage")
	public void handleMessage(HttpServletRequest req, HttpServletResponse resp){
		// 随机字符串  
		String echostr = req.getParameter("echostr");  
		// 微信加密签名  
		String signature = req.getParameter("signature");  
		// 时间戳  
		String timestamp = req.getParameter("timestamp");  
		// 随机数  
		String nonce = req.getParameter("nonce");  

		try {
			PrintWriter out = resp.getWriter();

			if(echostr==null || "".equals(echostr)){
				//微信服务器在五秒内收不到响应会断掉连接，并且重新发起请求，总共重试三次
				//假如服务器无法保证在五秒内处理并回复，必须做下述回复，微信服务器不会对此作任何处理，并且不会发起重试
				Map<String, String> requestMap;
				try {
					requestMap = MessageUtil.parseXml(req);
					// 消息类型
					String event = requestMap.get("Event");
					if(event!=null && "LOCATION".equals(event)){
						// 发送方帐号（open_id）
						String fromUserName = requestMap.get("FromUserName");
						String createTime = requestMap.get("CreateTime");	 //消息创建时间 （整型）
						String latitude = requestMap.get("Latitude");	 //地理位置纬度
						String longitude = requestMap.get("Longitude");	 //地理位置经度
						locationMapper.saveOrUpdate(fromUserName, latitude, longitude, createTime);
					}
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				out.print(""); 
				out.close();  
				out = null;
				return;
			}
			
			// 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败  
			if (SignUtil.checkSignature(token, signature, timestamp, nonce)) {  
				out.print(echostr);  
			}  
			out.close();  
			out = null;  
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 
	 * @param req
	 * @param resp
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="sendWechatMsg")
	public void sendWechatMsg(HttpServletRequest req, HttpServletResponse resp){
		
		try {
			String pk_group = req.getParameter("pk_group");
			
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

			// 如果是赠送电子券，增加跳转会员页面url参数
			Map<String,String[]> reqParam = req.getParameterMap();
			// 不能直接修改request中获取的Map，新建一个Map存储参数
			Map<String,String[]> param = new HashMap<String,String[]>();
			if(null != reqParam && null != reqParam.get("templateCode") && "OPENTM201048309".equals(reqParam.get("templateCode")[0])) {
				StringBuffer target_url = req.getRequestURL();
				target_url = target_url.delete(target_url.lastIndexOf("/"), target_url.length());
				target_url.append("/cardInfo.do?openid=").append(req.getParameter("openid"))
				.append("&pk_group=").append(pk_group);
				String url = target_url.toString().replace("/weixin/", "/myCard/");
				
				String[] strArray = new String[]{url};
				param.put("url", strArray);
				
				param.put("first", reqParam.get("first"));
				param.put("keyword1", reqParam.get("keyword1"));
				param.put("keyword2", reqParam.get("keyword2"));
				param.put("keyword3", reqParam.get("keyword3"));
				param.put("keyword4", reqParam.get("keyword4"));
				param.put("remark", reqParam.get("remark"));
				param.put("openid", reqParam.get("openid"));
				param.put("templateCode", reqParam.get("templateCode"));
			}
			//添加接口 推送等位信息模板  pk_group  map<String,String[]> reqParam:firmid,openid,templateCode;
			else if(null != reqParam && null != reqParam.get("templateCode") && "OPENTM200568893".equals(reqParam.get("templateCode")[0])){
				StringBuffer sb = new StringBuffer();
				String baseUrl = Commons.getConfig().getProperty("baseurl");//.../waitSeat/myWaitInfo.do
				baseUrl = baseUrl.substring(0,baseUrl.lastIndexOf("/"));
				String firmId = reqParam.get("firmid")[0];//门店id
				//url  点击模板消息跳转
				String openId = reqParam.get("openid")[0];
				sb.append("https://open.weixin.qq.com/connect/oauth2/authorize?appid=").append(appId)
				.append("&redirect_uri=").append(baseUrl)
				.append("/waitSeat/myWaitInfo.do?pk_store=").append(firmId)
				.append("&response_type=code&scope=snsapi_base&state=123#wechat_redirect");
				String[] strArray = new String[]{ sb.toString()};
 				param.put("url",strArray);
 				
 				// 获取用户等位信息
 				Map<String, String> paraMap = new HashMap<String, String>();
 				paraMap.put("wechat", openId);
 				String userObjStr = this.executeHttpMethod("findSeatByUser.do", paraMap);
 				if(null != userObjStr && !userObjStr.isEmpty()) {
 					userObjStr = userObjStr.substring(1, userObjStr.length() - 1).replaceAll("\\\\", "");
 					if(!"null".equals(userObjStr)) {
 						Map<String,String> map = (Map<String,String>)JSONObject.fromObject(userObjStr);
 						param.put("first", new String[]{"您的排队快要到号啦，请及时来店用餐！"});
 						param.put("keyword1",new String[]{map.get("firmname")});//店名
 						param.put("keyword2", new String[]{map.get("rec")});//排队号
 						param.put("keyword3", new String[]{map.get("tblname")});//排队桌型
 						param.put("keyword4", new String[]{map.get("tblnum")});//前面还有
 						param.put("remark", new String[]{"***微信排队，可以随时查询哟！***"});
 						param.put("openid", reqParam.get("openid"));
 						param.put("templateCode", reqParam.get("templateCode"));
 					}
 				}else{
 					param = req.getParameterMap();
 				}
			} else {
				param = req.getParameterMap();
			}
			
			TemplateMsgUtil.sendTemplateMsgHttp(token.getToken(), param);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
			httpUrlConn.setDoOutput(true);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			jsonObject = JSONObject.fromObject(buffer.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	 
	public static void main(String args[]){
		String param0 = "pk_group=&templateCode=OPENTM201048309&openid=o0NbUshwUubW7tIpBdagFTNaClsg&first=领取&keyword1=kkjk&keyword2=欢迎使用&keyword3=哈哈&remark=测试";
		String param1 = "pk_group=&templateCode=OPENTM201054648&openid=o0NbUshwUubW7tIpBdagFTNaClsg&first=领取&keyword1=kkjk&keyword2=欢迎使用&keyword3=哈哈&remark=测试";
		String param2 = "pk_group=&templateCode=OPENTM201061496&openid=o0NbUshwUubW7tIpBdagFTNaClsg&first=领取&keyword1=kkjk&keyword2=欢迎使用&keyword3=哈哈&remark=测试";
		try {
			httpRequest("http://192.168.0.150/ChoiceWeChat/weixin/sendWechatMsg.do","POST",param0);
			//httpRequest("http://192.168.0.150/ChoiceWeChat/weixin/sendWechatMsg.do","POST",param1);
			//httpRequest("http://192.168.0.150/ChoiceWeChat/weixin/sendWechatMsg.do","POST",param2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/
	/**
	 * 获取AccessToken
	 * @param appId
	 * @param secret
	 * @return
	 */
	@RequestMapping(value="getAccessToken")
	@ResponseBody
	public String getAccessToken(String appId,String secret){
		try{
			return WeChatUtil.getAccessToken(appId, secret).getToken();
		}catch(Exception e){
			return "获取AccessToken失败，失败原因："+e.getCause().getMessage();
		}
	}
	/**
	 * 上传订单
	 * @param cardid
	 * @param orderInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="uploadOrderInfo")
	@ResponseBody
	public String uploadOrderInfo(String cardid,String orderInfo){
		try{
			//解密会员卡id
			cardid = Utils.decryptCardId(cardid);
			JSONObject json = JSONObject.fromObject(orderInfo);
			
			StringBuffer errDish = new StringBuffer();
			//查询会员信息
			String sql = "select cardid,cardno,name,wechatid from card where cardid = '"+cardid+"'";
			Map<String, Object> mapCard =  jdbcTemplate.queryForMap(sql);
			if(ValueCheck.IsNotEmpty(mapCard.get("WECHATID"))){

				Double nmoney=0.00;
				String selectOrder = "select id,resv,dat from net_orders where resv = '"+json.get("resv")+"' and firmid = '?'";
				String updateFolioSql = "insert into net_orders(id,resv,tables,pax,bookdeskorderid,dat,nam,contact,ordertime,sft,state,firmid,remark," +
						"bev,isfeast,addr,datmins,openid,rannum,sumprice,paymoney,pk_group,ordfrom,outtradeno) values(?,?,?,?,?,to_date('"+json.get("dat") + "','yyyy-mm-dd'),?,?,to_date('"+json.get("ordertime") + "','yyyy-mm-dd hh24:mi:ss'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				String addFolioPaymentSql = "insert into net_foliopayment(PK_FOLIOPAYMENT,RESV,VSETTLEMENTCODE,VSETTLEMENTNAME,NPAYAMT,NREFUNDAMT,VPAYDATE,NSETTLEMENTCOUNT) values (?,?,?,?,?,?,?,?)";
				List<Object> valuesList = new ArrayList<Object>();
				//根据门店编码查询门店信息
				String selectFirmid = "select pk_store as firmid from cboh_store_3ch where vcode = '"+json.get("store_code").toString()+"'";
				List<Map<String, Object>> listStore = jdbcTemplate.query(selectFirmid,new Object[]{}, new ListMapMapper());
				if(ValueCheck.IsEmpty(listStore) || listStore.isEmpty() || listStore.size()==0){
					return "-1-没有该门店";
				}
				String firmid = listStore.get(0).get("firmid")+"";
				if(ValueCheck.IsEmpty(firmid)){
					return null;
				}
				//根据订单号、门店主键查询订单
				selectOrder = selectOrder.replace("?", firmid);
				List<Map<String, Object>> listOrder = jdbcTemplate.query(selectOrder,new Object[]{}, new ListMapMapper());
				if(ValueCheck.IsEmpty(listOrder) || listOrder.isEmpty() || listOrder.size()==0){//如果总部没有该订单，新生成一个主键
//					json.put("id",CodeHelper.createUUID());
				}else{
//					json.put("id",listOrder.get(0).get("id"));
					//删除订单主表信息
					String deleteOrder = "DELETE FROM net_orders WHERE id=?";
					jdbcTemplate.update(deleteOrder, new Object[] {listOrder.get(0).get("id") });
				}
				 //每次更新前查询一次菜品数据，编码、主键、名称
				 List<Map<String, Object>> listPubitem = queryPubitemFromBOH("",firmid,DateFormat.getStringByDate(new Date(), "yyyy-MM-dd"));
				 System.out.println(JSONArray.fromObject(listPubitem).toString());
				 Map<String,Object> mapPubitem = new HashMap<String,Object>();
				 if(ValueCheck.IsNotEmptyForList(listPubitem)){
					 for(Map<String,Object> map : listPubitem){
						 mapPubitem.put((String) map.get("vcode"), map.get("pk_pubitem")+"'"+map.get("vname"));
					 }
				 }else{
					 //记录日志
					LogUtil.writeToTxt(LogUtil.BUSINESS, "mq流水号："+json.get("id")+",接收pos返回数据。查询菜谱方案菜品明细为空"+System.getProperty("line.separator"));
					 return "";
				 }
				 //每次更新前查询一次附加项数据，编码、主键、名称
				 List<Map<String,Object>> listRedefine = selectRedefine();
				 Map<String,Object> mapRedefine = new HashMap<String,Object>();
				 for(Map<String,Object> map : listRedefine){
					 mapRedefine.put((String) map.get("vcode"), map.get("pk_redefine")+"'"+map.get("vname"));
				 }
				 List<Map<String,Object>> listProdAdd = selectProdAdd();
				 for(Map<String,Object> map : listProdAdd){
					 mapRedefine.put((String) map.get("vcode"), map.get("pk_redefine")+"'"+map.get("vname"));
				 }
				 //更新账单明细数据
				List<Object> orderList = (List<Object>)json.get("menuList");
				if(orderList != null && orderList.size()>0){
					//删除订单明细
					String deleteDtl = "DELETE FROM NET_ORDERDETAIL WHERE ORDERSID=?";
					jdbcTemplate.update(deleteDtl, new Object[] { json.get("id") });
					//删除菜品附加项信息
					String deleteRedefine = "DELETE FROM NET_DISHADDITEM WHERE PK_ORDERSID=?";
					jdbcTemplate.update(deleteRedefine, new Object[] { json.get("id") });
					//删除菜品附加产品信息
					String deleteProd = "DELETE FROM NET_DISHPRODADD WHERE PK_ORDERSID=?";
					jdbcTemplate.update(deleteProd, new Object[] { json.get("id") });
					//删除套餐明细信息
					String deletePackageDtl = "DELETE FROM NET_ORDERPACKAGEDETAIL WHERE PK_ORDERID=?";
					jdbcTemplate.update(deletePackageDtl, new Object[] { json.get("id") });
					//组建订单明细参数list及菜品附加项list
					Net_Orders order = new Net_Orders();
					order.setId( json.get("id")+"");
					List<Net_OrderDtl> listOrderDtl =  new ArrayList<Net_OrderDtl>();
					Net_OrderDtl orderDtl = new Net_OrderDtl();
					NetDishAddItem dishAddItem = new NetDishAddItem();
					Map<String, Object> filterMap = new HashMap<String, Object>();
					List<NetDishAddItem> listNetDishAddItem =  new ArrayList<NetDishAddItem>();
					List<NetDishProdAdd> listNetDishProdAdd = new ArrayList<NetDishProdAdd>();
					//套餐列表
					List<Net_OrderPackageDetail> listOrderPackageDtl = new ArrayList<Net_OrderPackageDetail>();
					Net_OrderPackageDetail orderPackageDetail = new Net_OrderPackageDetail();
					for(Object obj : orderList){
						orderDtl = new Net_OrderDtl();
						Map<String,Object> map = (Map<String,Object>)obj;
						 valuesList = new ArrayList<Object>();
						 String orderDtlId=CodeHelper.createUUID();
						 orderDtl.setId(orderDtlId);
						 if(ValueCheck.IsNotEmpty(mapPubitem.get(map.get("vcode")))){
							 orderDtl.setFoodsid(mapPubitem.get(map.get("vcode")).toString().split("'")[0]+"");//菜品主键
						 }else{
							 errDish.append(","+map.get("vname"));
							 continue;
						 }
						 orderDtl.setOrdersid(json.get("id")+"");
						 orderDtl.setRemark("");//备注
						 orderDtl.setFoodnum(map.get("cnt")+"");//点菜数量
						 orderDtl.setFoodzcnt(map.get("zcnt")+"");//赠送数量
						 orderDtl.setTotalprice(map.get("money")+"");//点菜金额
						 orderDtl.setFoodsname(map.get("vname")+"");//菜品名称
						 orderDtl.setIspackage((map.get("ispackage") == null ? "0" : map.get("ispackage")) + "");//是否套餐
						 List<Object> fujiaList = (List<Object>)map.get("fujiaList");
						 double fujiaPrice = 0.0;
						 if(fujiaList != null && fujiaList.size()>0){
							 for(Object fujia : fujiaList){
								Map<String,Object> mapFujia = (Map<String,Object>)fujia;
								String pk_pubItem = mapPubitem.get(map.get("vcode")).toString().split("'")[0] + "";
								if(!mapRedefine.containsKey(mapFujia.get("vcode"))) {
									orderDtl.setRemark(mapFujia.get("vname") + "");
									continue;
								}
								String pk_redefien = mapRedefine.get(mapFujia.get("vcode")).toString().split("'")[0] + "";
								//键值，用于判断是否已存在
								String key = orderDtl.getOrdersid() + "_" + orderDtl.getFoodsid() + "_" + pk_pubItem + "_" + pk_redefien + "_";
								/*if(filterMap.containsKey(key)) {
									continue;
								}*/
								if(mapFujia.get("visaddprod") != null && "1".equals(mapFujia.get("visaddprod").toString())) {
									NetDishProdAdd data = new NetDishProdAdd();
									data.setPk_group("");
									data.setPk_orderDtlId(orderDtlId);
									data.setPk_pubitem(pk_pubItem);
									data.setPk_prodAdd(pk_redefien);
									data.setPk_prodReqAdd("");
									data.setNcount(mapFujia.get("fcount")+"");
									Double price = Double.parseDouble(mapFujia.get("nprice")+"") / Integer.parseInt(mapFujia.get("fcount")+"");
									data.setNprice(WeChatUtil.formatDoubleLength(Double.toString(price), 2));
									listNetDishProdAdd.add(data);
									filterMap.put(key, data);
									fujiaPrice += Double.parseDouble(mapFujia.get("nprice")+"");
								} else {
									 dishAddItem = new NetDishAddItem();
									 dishAddItem.setPk_group("");
									 dishAddItem.setPk_orderDtlId(orderDtlId);
									 dishAddItem.setPk_pubItem(pk_pubItem);
									 dishAddItem.setPk_redefine(pk_redefien);
									 dishAddItem.setPk_prodcutReqAttAc("");
									 dishAddItem.setNcount(mapFujia.get("fcount")+"");
									 Double price = Double.parseDouble(mapFujia.get("nprice")+"") / Integer.parseInt(mapFujia.get("fcount")+"");
									 dishAddItem.setNprice(WeChatUtil.formatDoubleLength(Double.toString(price), 2));
									 listNetDishAddItem.add(dishAddItem);
									 filterMap.put(key, dishAddItem);
									 fujiaPrice += Double.parseDouble(mapFujia.get("nprice")+"");
								}
							 }
							 orderDtl.setListDishProdAdd(listNetDishProdAdd);
							 orderDtl.setListDishAddItem(listNetDishAddItem);
						 }
						 //如果是套餐
						 if("1".equals(map.get("ispackage"))){
							 listOrderPackageDtl = new ArrayList<Net_OrderPackageDetail>();
							 //套餐明细
							 List<Object> tcMenuList = (List<Object>)map.get("menutclist");
							 if(tcMenuList != null && tcMenuList.size()>0){
								 for(Object tcObj : tcMenuList){
									 Map<String,Object> tcmxMap = (Map<String,Object>)tcObj;
									 orderPackageDetail = new Net_OrderPackageDetail();
									 orderPackageDetail.setPk_pubitem(mapPubitem.get(tcmxMap.get("vcode")).toString().split("'")[0]+"");
									 orderPackageDetail.setNcnt(Double.parseDouble(tcmxMap.get("cnt")+""));
									 orderPackageDetail.setNzcnt(Double.parseDouble(tcmxMap.get("zcnt")+""));
									 orderPackageDetail.setNprice(Double.parseDouble(tcmxMap.get("price")+""));
									 orderPackageDetail.setVremark("");
									 //套餐明细附加项、附加产品
									 List<Object> txmxFujiaList = (List<Object>)tcmxMap.get("fujiaList");
									 if(txmxFujiaList != null && txmxFujiaList.size()>0){
										 listNetDishProdAdd = new ArrayList<NetDishProdAdd>();
										 listNetDishAddItem = new ArrayList<NetDishAddItem>();
										 for(Object fujia : txmxFujiaList){
											Map<String,Object> mapFujia = (Map<String,Object>)fujia;
											String pk_pubItem = mapPubitem.get(map.get("vcode")).toString().split("'")[0] + "";
											if(!mapRedefine.containsKey(mapFujia.get("vcode"))) {
												orderPackageDetail.setVremark(mapFujia.get("vname") + "");
												continue;
											}
											String pk_redefien = mapRedefine.get(mapFujia.get("vcode")).toString().split("'")[0] + "";
											//键值，用于判断是否已存在
											String key = orderDtl.getOrdersid() + "_" + orderDtl.getFoodsid() + "_" + pk_pubItem + "_" + pk_redefien + "_";
											if(mapFujia.get("visaddprod") != null && "1".equals(mapFujia.get("visaddprod").toString())) {
												NetDishProdAdd data = new NetDishProdAdd();
												data.setPk_group("");
												data.setPk_orderDtlId(orderDtlId);
												data.setPk_pubitem(pk_pubItem);
												data.setPk_prodAdd(pk_redefien);
												data.setPk_prodReqAdd("");
												data.setNcount(mapFujia.get("fcount")+"");
												Double price = Double.parseDouble(mapFujia.get("nprice")+"") / Integer.parseInt(mapFujia.get("fcount")+"");
												data.setNprice(WeChatUtil.formatDoubleLength(Double.toString(price), 2));
												listNetDishProdAdd.add(data);
												fujiaPrice += Double.parseDouble(mapFujia.get("nprice")+"");
											} else {
												 dishAddItem = new NetDishAddItem();
												 dishAddItem.setPk_group("");
												 dishAddItem.setPk_orderDtlId(orderDtlId);
												 dishAddItem.setPk_pubItem(pk_pubItem);
												 dishAddItem.setPk_redefine(pk_redefien);
												 dishAddItem.setPk_prodcutReqAttAc("");
												 dishAddItem.setNcount(mapFujia.get("fcount")+"");
												 Double price = Double.parseDouble(mapFujia.get("nprice")+"") / Integer.parseInt(mapFujia.get("fcount")+"");
												 dishAddItem.setNprice(WeChatUtil.formatDoubleLength(Double.toString(price), 2));
												 listNetDishAddItem.add(dishAddItem);
												 fujiaPrice += Double.parseDouble(mapFujia.get("nprice")+"");
											}
										 }
										 orderPackageDetail.setListDishProdAdd(listNetDishProdAdd);
										 orderPackageDetail.setListDishAddItem(listNetDishAddItem);
									 }
									 listOrderPackageDtl.add(orderPackageDetail);
								 }
								 orderDtl.setOrderPackageDetailList(listOrderPackageDtl);
							 }
						 }
						 listOrderDtl.add(orderDtl);
						 //重新计算账单金额
						 nmoney = WeChatUtil.stringPlusDouble(nmoney, map.get("money")) + fujiaPrice;
					}
					if(ValueCheck.IsNotEmpty(errDish.toString())){
						 return "0-菜品【"+errDish.toString().substring(1)+"】在总部菜谱中不存在";
					}
					order.setListNetOrderDtl(listOrderDtl);
					//插入pos上传的订单明细
					bookMealMapper.saveOrderDtl(order);
				}
				//插入退菜数据
				List<Object> delmenuList = (List<Object>)json.get("delmenuList");
				if(delmenuList != null && delmenuList.size()>0){
					//删除订单退菜信息
					String deleteDtl = "DELETE FROM net_backdishes WHERE orderid=?";
					jdbcTemplate.update(deleteDtl, new Object[] { json.get("id") });
					//组建退菜参数list
					String backdishesSql = "insert into net_backdishes(pk_backdishes,vpcode,vpname,nzcnt,nzmoney,pk_pubitem,orderid,vdesc) values (?,?,?,?,?,?,?,?)";
					for(Object obj : delmenuList){
						Map<String,Object> map = (Map<String,Object>)obj;
						 valuesList = new ArrayList<Object>();
						 valuesList.add(CodeHelper.createUUID());
						 valuesList.add(map.get("vcode"));//菜品编码
						 valuesList.add(map.get("vname"));//菜品名称
						 valuesList.add(map.get("ycnt"));//退菜数量
						 valuesList.add(map.get("ymoney"));//退菜金额
						 valuesList.add(mapPubitem.get(map.get("vcode")).toString().split("'")[0]);//菜品主键
						 valuesList.add(json.get("id"));//订单主键
						 valuesList.add(map.get("vdesc"));//退菜原因
						 jdbcTemplate.update(backdishesSql,valuesList.toArray());
					}
				}
				 //插入折扣信息
				List<Object> paymentList = (List<Object>)json.get("paymentList");
				if(paymentList != null && paymentList.size()>0){
					//删除订单折扣信息
					String deleteDtl = "DELETE FROM NET_FOLIOPAYMENT WHERE RESV=?";
					jdbcTemplate.update(deleteDtl, new Object[] { json.get("resv") });
					for(Object obj : paymentList){
						Map<String,Object> map = (Map<String,Object>)obj;
						 valuesList = new ArrayList<Object>();
						 valuesList.add(CodeHelper.createUUID());
						 valuesList.add(json.get("resv"));//账单号
						 valuesList.add(map.get("vsettlementcode"));//支付方式编码
						 valuesList.add(map.get("vsettlementname"));//支付方式名称
						 valuesList.add(0);//支付金额
						 valuesList.add(map.get("nrefundamt"));//优惠金额
						 valuesList.add(DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
						 valuesList.add(map.get("vsettlementcount"));//数量
						 //存储门店返回的账单折扣信息
						 jdbcTemplate.update(addFolioPaymentSql, valuesList.toArray());
					}
				}

				//-------------------------修改账单应付金额start----------------------------
				valuesList = new ArrayList<Object>();
				StringBuffer sb = new StringBuffer(updateFolioSql);
				valuesList.add(json.get("id"));
				valuesList.add(json.get("resv"));
				valuesList.add(json.get("tables"));
				valuesList.add(json.get("pax"));
				valuesList.add("");
				valuesList.add(json.get("nam"));
				valuesList.add(json.get("contact"));
				valuesList.add(json.get("sft"));
				valuesList.add("7");
				valuesList.add(firmid);
				valuesList.add(json.get("remark"));
				valuesList.add("3");
				valuesList.add("0");
				valuesList.add(json.get("addr"));
				valuesList.add("");
				valuesList.add(mapCard.get("WECHATID"));
				valuesList.add(json.get("resv")+GenerateRandom.getRanNum(6));
				valuesList.add(WeChatUtil.formatDoubleLength(String.valueOf(nmoney),2));
				valuesList.add(json.get("paymoney"));
				valuesList.add(json.get("pk_group"));
				valuesList.add("HANDHELD");//手持机上传
				// 修改商户订单号生成规则
				String outTradeNo = json.get("store_code").toString() +json.get("resv").toString() + (new java.util.Random().nextInt(9000) + 1000);
				valuesList.add(outTradeNo);
				if(ValueCheck.IsNotEmpty(json.get("disc"))){//折扣金额
				}
				 jdbcTemplate.update(sb.toString(), valuesList.toArray());
				//-------------------------修改账单应付金额end----------------------------
				 
				//记录日志
				LogUtil.writeToTxt(LogUtil.BUSINESS, "mq流水号："+json.get("id")+",接收pos返回数据。数据内容："+json+System.getProperty("line.separator"));
			
			}else{
				return "0-会员参数不完整，请确认客户已关注公众号并注册会员";
			}
			return "1-上传参数成功，请等待微信后台处理";
		}catch(Exception e){
			return "0-上传订单参数失败："+e.getMessage();
		}
	}
	/**
	 * 查询该门店所属方案的菜品编码
	 */
	private final static String queryPubitemFromBOH = "SELECT M.PK_ITEMPRGM FROM CBOH_ITEMPRGM_3CH M,CBOH_ITEMPRGFIRM_3CH F"+
				" WHERE M.PK_ITEMPRGM = F.PK_ITEMPRGM AND M.ENABLESTATE=2 "+
				" AND M.DEDAT >=? AND M.DBDAT <= ? AND F.PK_STORE= ? ORDER BY M.VLEV DESC ";
	public List<Map<String, Object>> queryPubitemFromBOH(String pk_group,String firmid, String dat) {
		StringBuilder sb = new StringBuilder(queryPubitemFromBOH);
		List<Object> valuesList = new ArrayList<Object>();
		
		valuesList.add(dat);
		valuesList.add(dat);
		valuesList.add(firmid);
		//查询菜谱方案按优先级倒序排序
		List<Map<String, Object>> listRes = jdbcTemplate.query(sb.toString(),valuesList.toArray(), new ListMapMapper());
		if(ValueCheck.IsNotEmptyForList(listRes)){
			//取第一个
			Map<String,Object> resMap = listRes.get(0);
			String sql = " select b.vcode as vcode,a.vname,a.vpubitem,a.pk_pubitem "+
					" from cboh_itemprgd_3ch a "+
					" left join cboh_pubitemcode_3ch b on a.pk_pubitem = b.pk_pubitem "+
					" left join cboh_store_3ch s on  b.pk_brand = s.pk_brandid "+
					" where s.pk_store='"+firmid+"' and a.pk_itemprgm='"+resMap.get("pk_itemprgm")+"' "+
					" union all "+
					" select  b.vcode,a.vname,a.npubid as vpubitem,a.pk_pubpack as pk_pubitem  "+
					" from cboh_itemprgpackage_3ch a "+ 
					" left join CBOH_PUBPACKCODE_3CH b on a.pk_pubpack = b.pk_pubpack "+
					" left join cboh_store_3ch s on  b.pk_brand = s.pk_brandid "+
					" where s.pk_store='"+firmid+"' and a.pk_itemprgm='"+resMap.get("pk_itemprgm")+"'";
			return  jdbcTemplate.query(sql,new Object[]{}, new ListMapMapper());
		}
		return null;
	}
	/**
	 * 查询附加项信息
	 * @return
	 */
	public List<Map<String, Object>> selectRedefine() {
		String sql = " select a.pk_redefine,a.vcode,a.vname from cboh_redefine_3ch a where a.enablestate=2  order by a.vcode,a.vname";
		return jdbcTemplate.query(sql,new Object[]{}, new ListMapMapper());
	}
	/**
	 * 查询附加产品信息
	 * @return
	 */
	public List<Map<String, Object>> selectProdAdd() {
		String sql = " select d.pk_prodadd as pk_redefine, p.vname as vname, p.vcode as vcode from net_dishprodadd d, cboh_pubitem_3ch p, cboh_productadditional_3ch a "
				+ "where d.pk_prodadd = a.pk_prodadd and a.pk_prodreqadd = p.pk_pubitem and p.enablestate = 2 ";
		return jdbcTemplate.query(sql,new Object[]{}, new ListMapMapper());
	}
	//{\"companyId\":\"\",\"delmenuList\":[{\"cnt\":\"1\",\"money\":\"88\",\"price\":\"88\",\"vcode\":\"1001\",\"vname\":\"老坛子酸菜鱼(2人份)\",\"ycnt\":\"1\",\"ymoney\":\"88\",\"zcnt\":\"0\",\"zmoney\":\"0.00\"}],\"id\":\"7b7a0d6f9c2e4e37b8502371a6417071\",\"menuList\":[{\"cnt\":\"1\",\"fujiaList\":[],\"ispackage\":\"0\",\"menutclist\":[],\"money\":\"18\",\"price\":\"18\",\"vcode\":\"202010005\",\"vname\":\"虎皮椒拌松花蛋\",\"ycnt\":\"1\",\"ymoney\":\"18.00\",\"zcnt\":\"0\",\"zmoney\":\"0.00\"},{\"cnt\":\"1\",\"fujiaList\":[],\"ispackage\":\"0\",\"menutclist\":[],\"money\":\"18\",\"price\":\"18\",\"vcode\":\"201030001\",\"vname\":\"上汤娃娃菜\",\"ycnt\":\"1\",\"ymoney\":\"18.00\",\"zcnt\":\"0\",\"zmoney\":\"0.00\"},{\"cnt\":\"1\",\"fujiaList\":[],\"ispackage\":\"0\",\"menutclist\":[],\"money\":\"28\",\"price\":\"28\",\"vcode\":\"201060006\",\"vname\":\"小二哥爱炸鸡翅\",\"ycnt\":\"1\",\"ymoney\":\"28.00\",\"zcnt\":\"0\",\"zmoney\":\"0.00\"},{\"cnt\":\"1\",\"fujiaList\":[{\"fcount\":\"1\",\"nprice\":\"8\",\"vcode\":\"201020001\",\"visaddprod\":\"1\",\"vname\":\"粉丝\"},{\"fcount\":\"1\",\"nprice\":\"8\",\"vcode\":\"201020005\",\"visaddprod\":\"1\",\"vname\":\"金针菇\"}],\"ispackage\":\"0\",\"menutclist\":[],\"money\":\"88\",\"price\":\"88\",\"vcode\":\"1001\",\"vname\":\"老坛子酸菜鱼(2人份)\",\"ycnt\":\"1\",\"ymoney\":\"88.00\",\"zcnt\":\"0\",\"zmoney\":\"0.00\"}],\"paymentList\":[],\"paymoney\":\"168.00\",\"resv\":\"16021811051128\",\"type\":\"5\",\"tables\":\"1\",\"pax\":\"2\",\"dat\":\"2016-03-02\",\"ordertime\":\"2016-03-02 17:35:43\",\"sft\":\"1\",\"store_code\":\"1101\",\"paymoney\":\"100.00\"}
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
	 * 上传多媒体文件接口
	 * 只支持图片，视频，音频。
	 * @param filePath 文件的绝对路径
	 * @param replytype 多媒体消息类型 //0 文本  1图文 2图片 3视频 4音频
	 * @param des 视频消息的描述
	 * @param title 视频消息的标题
	 * @param pk_id 主键 
	 * 新增上传多媒体文件接口 uploadMedia 用于boh设置自定义消息回复的图片   调用此接口上传多媒体文件
	 * */
	@RequestMapping(value="uploadMedia")
	private void uploadMedia(HttpServletRequest request,HttpServletResponse response){
		String pk_id = request.getParameter("pk_id");//wx_keyword表主键  
		
		String filePath = request.getParameter("filePath");//文件的绝对路径*
		String type = request.getParameter("replytype");//上传消息类型*
		String des = request.getParameter("des");//描述 
		String title =  request.getParameter("title");//标题 *
		Map<String,String> map = new HashMap<String,String>();
		map.put("filePath", filePath);
		//0 文本  1图文 2图片 3视频 4音频
		if("2".equals(type)){
			map.put("mg_type", "image");
		}else if("3".equals(type)){
			map.put("mg_type", "mpvideo");
		}else if("4".equals(type)){
			map.put("mg_type", "voice");
		}
		
		map.put("des", des);
		map.put("title",title);
		String media_id = "";
		//获取token
		String appid = Commons.appId;
		String secret = Commons.secret;
		String token = WeChatUtil.getAccessToken(appid, secret).getToken();
		//上传多媒体文件 获得media_id
		media_id = uploadFile(token,map.get("filePath"),map.get("mg_type"),map);
		//保存media_id 到 wx_keyword 表
		String saveMedia_id = "update wx_keyword set media_id=? where pk_id=?";
		int i = jdbcTemplate.update(saveMedia_id, new Object[] {media_id,pk_id });
		if(i>0){
			System.out.println(new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(new Date())+"------上传多媒体文件成功！,pk_id"+pk_id+",type:"+type);
		}
	}
	/**
	 * 上传多媒体文件
	 * @param Access_Token
	 * @param 
	 * @return media_id
	 * 
	 * {
  		"title":VIDEO_TITLE,
  		"introduction":INTRODUCTION
		}
	 * */
	public String uploadFile(String Access_Token,String filePath,String mg_type,Map<String,String> map){
		String json = null;
		if("text".equals(mg_type)){//文本消息不需要上传文件
			return null;
		}else if("mpvideo".equals(mg_type)){
			mg_type = "video";
			json = "{\"title\":\""+map.get("title")+"\",\"introduction\":\""+map.get("des")+"\""
					+ "}";
		}
		
		JSONObject o = WeChatUtil.uploadMedia(new File(filePath), Access_Token, mg_type,json);
		
		return (String) o.get("media_id");
	}
}
