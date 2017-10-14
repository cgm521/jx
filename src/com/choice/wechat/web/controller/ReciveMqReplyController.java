package com.choice.wechat.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.choice.test.domain.Net_Orders;
import com.choice.test.persistence.WeChatOrderMapper;
import com.choice.test.service.impl.WechatOrderService;
import com.choice.test.utils.Commons;
import com.choice.test.utils.DateFormat;
import com.choice.wechat.persistence.WeChatPay.WxPayMapper;
import com.choice.wechat.persistence.bookDesk.BookDeskMapper;
import com.choice.wechat.service.bookDesk.IBookDeskService;
import com.choice.wechat.util.LogUtil;
import com.choice.wechat.util.TemplateMsgUtil;
import com.choice.wechat.util.ValueCheck;
import com.choice.wechat.util.WeChatUtil;
/**
 * 自动接收pos发送消息并将消息推送给微信用户
 * @author ZGL
 *
 */
public class ReciveMqReplyController extends TimerTask {

	@Autowired
	private BookDeskMapper bookDeskMapper;
	@Autowired
	private WxPayMapper wxPayMapper;
	@Autowired
	private IBookDeskService bookDeskService;
	static WeChatOrderMapper orderMapper = new WechatOrderService();
	
	@Override
	public void run() {
		QueueConnection connection = null;
		QueueSession session = null;
		
		try {
			QueueConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
					ActiveMQConnection.DEFAULT_PASSWORD,Commons.broker_url);
			connection = factory.createQueueConnection();
			connection.start();
			session = connection.createQueueSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue(Commons.reciveMqQueueName);
			QueueReceiver receiver = session.createReceiver(queue);

			receiver.setMessageListener(new MessageListener() {
				
				@Override
				public void onMessage(Message msg) {
					if(msg != null){
						TextMessage mess = (TextMessage)msg;
						try {
							String str = mess.getText();
							LogUtil.writeToTxt(LogUtil.BUSINESS, "自动执行收取门店发送的返回信息信息："+str);
							System.out.println(DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss")+str);
							JSONObject json = JSONObject.fromObject(str);

						
							Map<String,Object> mqMap = new HashMap<String,Object>();
							  Net_Orders orders = new Net_Orders();
							  String state = "";
							//更新账单折扣金额及应付金额
							if("5".equals(json.get("type")) || "6".equals(json.get("type"))){
								LogUtil.writeToTxt(LogUtil.BUSINESS, "自动执行收取门店发送的返回信息mq流水号："+json.get("id")+",返回的错误信息："+json.get("error"));
								//字符串格式
								//{"id":"86859cfa62ba4098ae89cbf30f06535b","resv":"15030616321511","paymoney":"120",
								//	"paymentList":[{vsettlementcode:"1001",vsettlementname:"88折",nrefundamt："30"},
								//		{vsettlementcode:"1002",vsettlementname:"买二送一",nrefundamt："20"}]}
								if("5".equals(json.get("type"))){
									/**
									 * 适用于第一次我要结账，包含团购券信息并有验证失败，取消支付后，第二次我要结账不包含团购券信息的情况
									 */
									wxPayMapper.updateGroupRecordSql(json.getString("id"));
								}
								//修改账单应付金额及存储门店返回的账单折扣信息
								wxPayMapper.updateFolioDisc(json);
							}else if("12".equals(json.get("type")) || "13".equals(json.get("type")) ){
								LogUtil.writeToTxt(LogUtil.BUSINESS, "自动执行收取门店发送的信息订单号："+json.get("resv")+",返回的错误信息："+json.get("error"));
								//查询订单信息及店铺信息
								Map<String,String> paramMap = new HashMap<String,String>();
								paramMap.put("resv", json.getString("resv"));
								List<Net_Orders> orderMapList = bookDeskMapper.getOrderMenus(paramMap);
								if(orderMapList!=null && orderMapList.size()>0){
									state = "12".equals(json.get("type"))?"6":"3";
									//修改订单状态
									orders.setId(orderMapList.get(0).getId());
									orders.setState(state);
									//记录日志
									bookDeskMapper.updateOrdr(orders);
									
									if("3".equals(state)){//如果是取消,执行退款
										String paymoney = orderMapList.get(0).getPaymoney()+"";//有paymoney时认为已付过款
										if(paymoney!=null && paymoney.length()>0 && !paymoney.contains("null")){
											bookDeskService.cancelOrders(orderMapList.get(0).getId(),orderMapList.get(0).getOpenid(), "", orderMapList.get(0).getFirmid(), "6", 
													orderMapList.get(0).getVtransactionid(), orderMapList.get(0).getPaymoney(), "-1");
										}
									}
									
									//构建微信消息提示
									Map<String,String> messageMap = new HashMap<String,String>();
	//								String token = WeChatUtil.getAccessToken("wxd6c7035d76ad8ab0","aab813fdf22f20707e3f4d5cf5be1dbf").getToken();
										String token = WeChatUtil.getAccessToken(Commons.appId,Commons.secret).getToken();
									messageMap.put("templateCode", "OPENTM213722270");//微信的模版编号
									messageMap.put("openid", orderMapList.get(0).getOpenid());
	//								messageMap.put("openid", "ouziAjhsEJ0aFj8AAbFJ1EIFBcSk");
									messageMap.put("first","");
									//*******************将订单主键存下来****************
									  orders.setId(orderMapList.get(0).getId());
									
									if("12".equals(json.get("type"))){
										messageMap.put("keyword1","您在【"+orderMapList.get(0).getFirmdes()+"】"+orderMapList.get(0).getResv()+"门店已开始处理");
									}else if("13".equals(json.get("type"))){
										messageMap.put("keyword1","您在【"+orderMapList.get(0).getFirmdes()+"】"+orderMapList.get(0).getResv()+"门店已取消");
									}
									
									messageMap.put("keyword2",Commons.vtitle);
									messageMap.put("keyword3",DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
									messageMap.put("remark","");
									
									//发送消息到微信
									LogUtil.writeToTxt(LogUtil.BUSINESS, "订单号："+json.get("resv")+",发送微信信息开始。。。"+messageMap.toString()+System.getProperty("line.separator"));
									TemplateMsgUtil.sendTemplateMsg(token,messageMap);
									//记录日志
									LogUtil.writeToTxt(LogUtil.BUSINESS, "订单号："+json.get("resv")+",发送微信信息成功。发送内容："+json+System.getProperty("line.separator"));
								}
							}else if("14".equals(json.get("type")) || "15".equals(json.get("type"))){//微信端取消操作
								LogUtil.writeToTxt(LogUtil.BUSINESS, "自动执行收取门店发送的返回信息mq流水号："+json.get("serialid")+",返回的错误信息："+json.get("error"));
								mqMap.put("pk_mqlogs", json.get("serialid"));
								mqMap.put("errmsg", json.get("error"));
								mqMap.put("state", json.get("state"));
								//更新流水号数据，标志成功、失败
								int res = bookDeskMapper.updateMqlogs(mqMap);
								//记录日志
								LogUtil.writeToTxt(LogUtil.BUSINESS, "mq流水号："+json.get("serialid")+"。更新流水号记录状态返回信息："+res+System.getProperty("line.separator"));
							} else if ("42".equals(json.get("type"))) {
								// 检测是否可以下单
								Map<String, JSONObject> infoMap = WeChatUtil.getPreOrderInfoMap();
								JSONObject info = new JSONObject();
								info.put("state", json.getString("state"));
								info.put("errmsg", json.get("error"));
								
								infoMap.put(json.getString("serialid"), info);
							} else {
								//字符串格式
								//{"serialid":"",
								//"state":"",  0失败 1 成功
								//"type":"",  //1 菜品 2 支付 3 加菜 4 我要结账 5向微信推送结账信息
								//"error":""}";
								mqMap.put("pk_mqlogs", json.get("serialid"));
								mqMap.put("errmsg", json.get("error"));
								mqMap.put("state", json.get("state"));
								//更新流水号数据，标志成功、失败
								int res = bookDeskMapper.updateMqlogs(mqMap);
								//记录日志
								LogUtil.writeToTxt(LogUtil.BUSINESS, "mq流水号："+json.get("serialid")+"。更新流水号记录状态返回信息："+res+System.getProperty("line.separator"));
								if("4".equals(json.get("type")) && ValueCheck.IsNotEmpty(json.get("error")) && ValueCheck.IsNotEmpty(json.get("billsmessage"))){
									if(ValueCheck.IsNotEmpty(json.get("errorCouponId"))){
										wxPayMapper.addCouponErrMsg(json.get("errorCouponId")+"",JSONObject.fromObject(json.get("billsmessage")).get("id")+"");
									}
									//2015-11-19 add 团购验证记录 song
									if(json.containsKey("tgcouponstate")){
										JSONArray tgcouponstate = (JSONArray) json.get("tgcouponstate");
										Iterator<?> i = tgcouponstate.iterator();
										List<Map<String,String>> list = new ArrayList<Map<String,String>>();
										
										while(i.hasNext()){
											JSONObject object = (JSONObject) i.next();
											Map<String,String> map = (Map<String,String>) object.toBean(object, HashMap.class);
											list.add(map);
										}
										wxPayMapper.addGroupRecord(json.getString("serialid"),list);
									}
									wxPayMapper.updateFolioDisc(JSONObject.fromObject(json.get("billsmessage")));
									return;
								}
								//查询订单信息及店铺信息
								List<Map<String, Object>> orderMapList = bookDeskMapper.queryOrderFromMqlogs(mqMap);
								if(ValueCheck.IsNotEmptyForList(orderMapList)){
									//查询门店台位信息
									Map<String, Object> storeSeatMap = new HashMap<String, Object>();
									List<Map<String,Object>> listStoreSeat=orderMapper.getStoreSeatInfo(orderMapList.get(0).get("firmid")+"", orderMapList.get(0).get("tables")+"");
									if(listStoreSeat!=null && listStoreSeat.size()>0){
										storeSeatMap = listStoreSeat.get(0);
									}else{
										storeSeatMap.put("pk_sited","");
										storeSeatMap.put("vcode",orderMapList.get(0).get("tables"));
										storeSeatMap.put("vname",orderMapList.get(0).get("tables"));
									}
									//构建微信消息提示
									Map<String,String> messageMap = new HashMap<String,String>();
	//								String token = WeChatUtil.getAccessToken("wxd6c7035d76ad8ab0","aab813fdf22f20707e3f4d5cf5be1dbf").getToken();
										String token = WeChatUtil.getAccessToken(Commons.appId,Commons.secret).getToken();
									messageMap.put("templateCode", "OPENTM213722270");//微信的模版编号
									messageMap.put("openid", orderMapList.get(0).get("openid")+"");
	//								messageMap.put("openid", "ouziAjhsEJ0aFj8AAbFJ1EIFBcSk");
									messageMap.put("first","");
									//*******************将订单主键存下来****************
									  orders.setId(orderMapList.get(0).get("id")+"");
									String result="";
									switch (Integer.parseInt(json.get("state").toString())) {
									case 1:
										  result="成功";
										  if("1".equals(json.get("type"))){
											  // 如果是先支付后下单，修改状态为6，否则修改为7
											  if("Y".equals(Commons.getConfig().getProperty("mustPayBeforeOrder"))) {
												  state = "6";
											  } else {
												  state = "7";
											  }
										  }else if("2".equals(json.get("type"))){
											  state = "6";
										  }
//										  bookDeskMapper.updateOrdr(orders);
										  break;
									case 0:
										result="失败，失败原因："+json.get("error");
										if("10".equals(json.get("type")) || "11".equals(json.get("type"))){
											  state = "0";
										  }
										break;
									default:
										  result="成功";
										  break;
									}
									if("1".equals(json.get("type")) || "37".equals(json.get("type"))){
										//state = "7";
										String message = "您在【"+orderMapList.get(0).get("vname")+"】台位"+storeSeatMap.get("vname")+"的账单："+orderMapList.get(0).get("resv")+"下单"+result + "。点击查看详情";
										if(json.containsKey("vserialno") && StringUtils.hasText(json.getString("vserialno"))) {
											// 包含取餐号，拼接到返回信息
											message += "\n您的取餐号是：" + json.getString("vserialno");
										}
										//message += "。点击查看详情";
										messageMap.put("keyword1", message);
									}else if("2".equals(json.get("type"))){
										state = "6";
										messageMap.put("keyword1","【"+orderMapList.get(0).get("vname")+"】台位"+storeSeatMap.get("vname")+"的账单："+orderMapList.get(0).get("resv")+"支付"+result+"。点击查看详情");
										if("0".equals(json.get("state"))){//如果pos返回失败，撤销支付
											if(ValueCheck.IsNotEmptyForList(orderMapList)){
												bookDeskService.cancelOrders(orderMapList.get(0).get("id")+"",orderMapList.get(0).get("openid")+"", "", orderMapList.get(0).get("firmid")+"", "6", 
														orderMapList.get(0).get("vtransactionid")+"", orderMapList.get(0).get("paymoney")+"", "-1");
											}
										}
									}else if("3".equals(json.get("type"))){
										state = "7";
										messageMap.put("keyword1","【"+orderMapList.get(0).get("vname")+"】台位"+storeSeatMap.get("vname")+"的账单："+orderMapList.get(0).get("resv")+"加菜"+result+"。点击查看详情");
									}else if("8".equals(json.get("type")) || "9".equals(json.get("type"))){
										if("1".equals(json.get("state"))) {
											state = "7";
										}
										messageMap.put("keyword1","您在【"+orderMapList.get(0).get("vname")+"】"+ orderMapList.get(0).get("resv")+"的外卖订单："+result+"。点击查看详情");
									}else if("10".equals(json.get("type")) || "11".equals(json.get("type"))){
										if("1".equals(json.get("state"))) {
											state = "7";
										}
										messageMap.put("keyword1","您在【"+orderMapList.get(0).get("vname")+"】"+ orderMapList.get(0).get("resv")+"的预订订单："+result+"。点击查看详情");
									}
									messageMap.put("keyword2",Commons.vtitle);
									messageMap.put("keyword3",DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
									messageMap.put("remark","");
									String url = Commons.vtakeOrdrurl.replace("gotoMenu", "orderDetail")
											+ "?orderid=" + orderMapList.get(0).get("id")
											+ "&openid=" + orderMapList.get(0).get("openid")
											+ "&firmid=" + orderMapList.get(0).get("firmid");
									if("8".equals(json.get("type")) || "9".equals(json.get("type"))){
										url = url.replace("bookMeal", "takeout");
									}else if("10".equals(json.get("type")) || "11".equals(json.get("type"))){
										url = url.replace("bookMeal", "bookDesk");
									}
									messageMap.put("url", url);
									//修改订单状态
									if("1".equals(json.get("state"))){
										orders.setId(orderMapList.get(0).get("id")+"");
										orders.setState(state);
										//记录日志
										LogUtil.writeToTxt(LogUtil.BUSINESS, "mq流水号："+json.get("serialid")+",更新订单状态开始，订单状态修改为"+state+"。。。"+System.getProperty("line.separator"));
										bookDeskMapper.updateOrdr(orders);
										LogUtil.writeToTxt(LogUtil.BUSINESS, "mq流水号："+json.get("serialid")+",更新订单状态成功。"+System.getProperty("line.separator"));
									}else if("0".equals(json.get("state"))){//失败时更新回初始状态
										if("10".equals(json.get("type")) || "11".equals(json.get("type"))){
											orders.setId(orderMapList.get(0).get("id")+"");
											orders.setState(state);
											//记录日志
											LogUtil.writeToTxt(LogUtil.BUSINESS, "mq流水号："+json.get("serialid")+",更新订单状态开始，订单状态修改为"+state+"。。。"+System.getProperty("line.separator"));
											bookDeskMapper.updateOrdr(orders);
											LogUtil.writeToTxt(LogUtil.BUSINESS, "mq流水号："+json.get("serialid")+",更新订单状态成功。"+System.getProperty("line.separator"));
										}else if("8".equals(json.get("type"))){
											String paymoney =  orderMapList.get(0).get("paymoney")+"";//有paymoney时认为已付过款
											if(paymoney!=null && paymoney.length()>0 && !paymoney.contains("null")){
												bookDeskService.cancelOrders(orderMapList.get(0).get("id")+"", messageMap.get("openid"), "", orderMapList.get(0).get("firmid")+"", "6", 
													orderMapList.get(0).get("vtransactionid")+"", paymoney, "-1");
											}else{
												//返回下单失败，没有支付时，只取消订单
												bookDeskService.cancelOrders(orderMapList.get(0).get("id")+"", messageMap.get("openid"), "", orderMapList.get(0).get("firmid")+"", "0", 
														orderMapList.get(0).get("vtransactionid")+"", paymoney, "-1");
											}
										}
									}
									//发送消息到微信
									LogUtil.writeToTxt(LogUtil.BUSINESS, "mq流水号："+json.get("serialid")+",发送微信信息开始。。。"+messageMap.toString()+System.getProperty("line.separator"));
									TemplateMsgUtil.sendTemplateMsg(token,messageMap);
									//记录日志
									LogUtil.writeToTxt(LogUtil.BUSINESS, "mq流水号："+json.get("serialid")+",发送微信信息成功。发送内容："+json+System.getProperty("line.separator"));
								}else{
									if("9".equals(json.get("type"))){
										if("0".equals(json.get("state"))){//失败时更新回初始状态
											Map<String,String> params = new HashMap<String,String>();
											params.put("id",json.get("serialid")+"");
											List<Net_Orders> orderslist = bookDeskMapper.getOrderMenus(params);
											if(orderslist!=null && orderslist.size()>0){
												if(!"3".equals(orderslist.get(0).getState())){//如果没取消过
													bookDeskService.cancelOrders(orderslist.get(0).getId(), orderslist.get(0).getOpenid(), "", orderslist.get(0).getFirmid(), "6", 
															orderslist.get(0).getVtransactionid(), orderslist.get(0).getPaymoney(), "-1");
												}
											}
											//构建微信消息提示
											Map<String,String> messageMap = new HashMap<String,String>();
			//								String token = WeChatUtil.getAccessToken("wxd6c7035d76ad8ab0","aab813fdf22f20707e3f4d5cf5be1dbf").getToken();
												String token = WeChatUtil.getAccessToken(Commons.appId,Commons.secret).getToken();
											messageMap.put("templateCode", "OPENTM213722270");//微信的模版编号
											messageMap.put("openid", orderslist.get(0).getOpenid());
			//								messageMap.put("openid", "ouziAjhsEJ0aFj8AAbFJ1EIFBcSk");
											messageMap.put("first","");
											//*******************将订单主键存下来****************
											 orders.setId(orderslist.get(0).getId());
											
											messageMap.put("keyword1","您在【"+orderslist.get(0).getFirmdes()+"】"+orderslist.get(0).getResv()+"的外卖订单，门店收款失败。订单已取消，并退款");
											
											messageMap.put("keyword2",Commons.vtitle);
											messageMap.put("keyword3",DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
											messageMap.put("remark","");
											String url = Commons.vtakeOrdrurl.replace("gotoMenu", "orderDetail")
													+ "?orderid=" + orderslist.get(0).getId()
													+ "&openid=" + orderslist.get(0).getOpenid()
													+ "&firmid=" + orderslist.get(0).getFirmid();
											url = url.replace("bookMeal", "takeout");
											messageMap.put("url", url);
											//发送消息到微信
											LogUtil.writeToTxt(LogUtil.BUSINESS, "mq流水号："+json.get("serialid")+",发送微信信息开始。。。"+messageMap.toString()+System.getProperty("line.separator"));
											TemplateMsgUtil.sendTemplateMsg(token,messageMap);
											//记录日志
											LogUtil.writeToTxt(LogUtil.BUSINESS, "mq流水号："+json.get("serialid")+",发送微信信息成功。发送内容："+json+System.getProperty("line.separator"));
										}
									}
								}
							}
						} catch (JMSException e) {
							e.printStackTrace();
						}
					}
				}
			});
			
			Thread.sleep(1000*5);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(session != null){
				try {
					session.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
			if(connection != null){
				try {
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
