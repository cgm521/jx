package com.choice.wechat.service.bookDesk.impl;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.choice.test.domain.Card;
import com.choice.test.domain.DeskTimes;
import com.choice.test.domain.Net_Orders;
import com.choice.test.service.CardSearch;
import com.choice.test.utils.CodeHelper;
import com.choice.test.utils.Commons;
import com.choice.test.utils.DateFormat;
import com.choice.wechat.domain.bookDesk.Firm;
import com.choice.wechat.persistence.WeChatPay.WxPayMapper;
import com.choice.wechat.persistence.bookDesk.BookDeskMapper;
import com.choice.wechat.persistence.bookMeal.BookMealMapper;
import com.choice.wechat.persistence.takeout.TakeOutMapper;
import com.choice.wechat.service.WeChatPay.IWxPayService;
import com.choice.wechat.service.bookDesk.IBookDeskService;
import com.choice.wechat.util.BuilderOrderMessage;
import com.choice.wechat.util.LogUtil;
import com.choice.wechat.util.ValueCheck;
import com.choice.wechat.util.mq.MqSender;

@Service
public class BookDeskServiceImpl implements IBookDeskService{
	@Autowired
	private BookDeskMapper bookDeskMapper;
	@Autowired
	private BookMealMapper bookMealMapper;
	@Autowired
	private IWxPayService wxPayService;
	@Autowired
	private WxPayMapper wxPayMapper;
	@Autowired
	private TakeOutMapper takeOutMapper;
	
	public void saveOrder(Net_Orders orders, DeskTimes deskTimes, String clientID){
		
		bookDeskMapper.getBookDesk(deskTimes.getResvtblid(), orders.getFirmid());
		int count = bookDeskMapper.getCountDeskTimes(deskTimes.getVcode(),null,deskTimes.getSft(),deskTimes.getDat(),orders.getFirmid());
		if(count>0){
			throw new RuntimeException("已订");
		}
		bookDeskMapper.saveDeskTimes(deskTimes);
		bookDeskMapper.saveOrders(orders,clientID);
	}
	/**
	 * 取消订单 
	 */
	public void cancelOrders(String id,String openid,String pk_group,String firmid, String state, String vtransactionid, String paymoney, String orderType){
		try{
			//支付后才会走取消支付的方法
			if("6".equals(state) || (orderType != null && "2".equals(orderType) && "2".equals(state))){
				LogUtil.writeToTxt(LogUtil.TENPAYREFUND, "==========退款开始");
				Map<String,Object> mapParam = new HashMap<String,Object>();
				//查询微信绑定的会员实体卡
				List<Card> listCardInfo = CardSearch.listCard(openid);
				if(!listCardInfo.isEmpty() && listCardInfo.size()>0){
					Card card= listCardInfo.get(0);
					mapParam.put("cardNo", card.getCardNo());
				}
				Map<String,Object> firmMap = wxPayMapper.getFirmInfo(firmid);
				mapParam.put("firmid", firmMap.get("VCODE"));
				mapParam.put("poserial", id);
				mapParam.put("paymoney", paymoney);
				mapParam.put("pk_store", firmid);
				if(ValueCheck.IsEmpty(vtransactionid)){
					//取消会员卡支付
					wxPayService.cancelPayWithCard(mapParam);
				}else{
					//取消微信支付
					mapParam.put("id", id);
					mapParam.put("vtransactionid", vtransactionid);
					String result = wxPayService.cancelPayWithTenpayV3(mapParam);
					if(!"1".equals(result)){
						LogUtil.writeToTxt(LogUtil.TENPAYREFUND, "微信支付退款失败，退款财付通单号："+vtransactionid+"，失败原因："+result);
						return;
					}
				}
			}
			bookDeskMapper.cancelOrders(id);
			bookDeskMapper.cancelOrdersDesk(id);
			bookDeskMapper.cancelOrderPayment(id);
			
			if(!"0".equals(state)){//外卖、预订，0是未发送消息 1 2 是已发
				if(orderType == null || "1".equals(orderType)){//订位单
					// 推送订位单
					Net_Orders deskOrder = new Net_Orders();
					String deskOrderid = id;
					Map<String, String> queryDeskMap = new HashMap<String, String>();
					queryDeskMap.put("id", deskOrderid);
					List<Net_Orders> deskOrders = bookMealMapper.getOrderMenus(queryDeskMap);
					if (deskOrders != null && !deskOrders.isEmpty()) {
						deskOrder = deskOrders.get(0);
					}

					//查询门店
					Firm firm = null;
					List<Firm> listFirm = bookMealMapper.getFirmList(null, null, deskOrder.getFirmid());

					if (listFirm != null && !listFirm.isEmpty()) {
						firm =  listFirm.get(0);
						deskOrder.setVcode(firm.getFirmCode());
					}else{
						throw new RuntimeException("门店不存在");
					}
					/*
					//pos和预制数据，和net_orders定义不同，加一符合
					try{
						String sft = String.valueOf(Integer.parseInt(deskOrder.getSft())+1);
						deskOrder.setSft(sft);
					}catch(Exception e){}
					*/
					deskOrder.setSerialid(CodeHelper.createUUID());
					deskOrder.setType("15");

					//记录本次记录
					Map<String,Object> mqMap = new HashMap<String,Object>();
					mqMap.put("pk_mqlogs", deskOrder.getSerialid());
					mqMap.put("orderid", deskOrder.getId());
					mqMap.put("vtype", "1");
					mqMap.put("errmsg", "");
					mqMap.put("state", "");
					bookDeskMapper.addMqLogs(mqMap);
					//发送mq消息
					deskOrder.setSerialid(mqMap.get("pk_mqlogs")+"");//重置mq记录的主键
					JSONObject json = JSONObject.fromObject(deskOrder);
					LogUtil.writeToTxt(LogUtil.MQSENDORDER, "推送订位单到MQ服务器中，队列名：" + firm.getFirmCode() + "_orderList，订单对象：" + json.toString());
					MqSender.sendOrders(json.toString(), firm.getFirmCode() + "_orderList",0);
					
				}else if("2".equals(orderType)){
					Net_Orders orders = BuilderOrderMessage.builderOrderMessage(takeOutMapper, bookMealMapper, id, "14");
					orders.setDat(DateFormat.getStringByDate(new Date(), "yyyy-MM-dd"));
					if(orders != null){
						//查询门店
						Firm firm = null;
						List<Firm> listFirm = bookMealMapper.getFirmList(null, null, orders.getFirmid());

						if (listFirm != null && !listFirm.isEmpty()) {
							firm =  listFirm.get(0);
							orders.setVcode(firm.getFirmCode());
						}else{
							throw new RuntimeException("门店不存在");
						}


						//记录本次记录
						Map<String,Object> mqMap = new HashMap<String,Object>();
						mqMap.put("pk_mqlogs", orders.getSerialid());
						mqMap.put("orderid", orders.getId());
						mqMap.put("vtype", "1");
						mqMap.put("errmsg", "");
						mqMap.put("state", "");
						bookDeskMapper.addMqLogs(mqMap);
						//发送mq消息
						orders.setSerialid(mqMap.get("pk_mqlogs")+"");//重置mq记录的主键
						JSONObject json = JSONObject.fromObject(orders);
						LogUtil.writeToTxt(LogUtil.MQSENDORDER, "下单到MQ服务器中，队列名："+firm.getFirmCode()+"_orderList，订单对象："+json.toString());
						MqSender.sendOrders(json.toString(),firm.getFirmCode()+"_orderList",Commons.mqEffectiveTime);//设置消息有效期为半小时
					}
				}

			}
			
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException("取消订单失败");
		}
	}

}
