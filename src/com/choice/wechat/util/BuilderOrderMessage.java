package com.choice.wechat.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.choice.test.domain.Net_OrderDtl;
import com.choice.test.domain.Net_Orders;
import com.choice.test.persistence.WeChatOrderMapper;
import com.choice.test.service.PubitemSearch;
import com.choice.test.service.impl.WechatOrderService;
import com.choice.test.utils.CodeHelper;
import com.choice.test.utils.Commons;
import com.choice.wechat.domain.bookMeal.FdItemSale;
import com.choice.wechat.domain.bookMeal.NetDishAddItem;
import com.choice.wechat.domain.bookMeal.NetDishProdAdd;
import com.choice.wechat.persistence.bookMeal.BookMealMapper;
import com.choice.wechat.persistence.takeout.TakeOutMapper;

public class BuilderOrderMessage {
	
	private static WeChatOrderMapper orderMapper = new WechatOrderService();
	
	public static Net_Orders builderOrderMessage(TakeOutMapper takeOutMapper,BookMealMapper bookMealMapper,String orderid,String type){
		// 推送点菜单
		if(orderid==null || "".equals(orderid)){
			return null;
		}
		
		Net_Orders orders = new Net_Orders();
		// 查询订单信息
		Map<String, String> queryMap = new HashMap<String, String>();
		queryMap.put("id", orderid);
		List<Net_Orders> listNet_Orders = takeOutMapper.getOrderMenus(queryMap);
		if (listNet_Orders != null && !listNet_Orders.isEmpty()) {
			orders = listNet_Orders.get(0);
			//查询账单明细
			List<Net_OrderDtl> orderDtl = PubitemSearch.getOrderDtlMenus(orders.getId());

			// 查询菜品门店编码
			List<FdItemSale> listPubItemCode = PubitemSearch.getListPubItemCode(orders.getFirmid());
			//获取套餐门店编码
			List<FdItemSale> listPackageCode = orderMapper.getListPackageCode(orders.getFirmid());
			listPubItemCode.addAll(listPackageCode);
			Map<String,String> mapPubItemCode = new HashMap<String,String>();
			Map<String,String> mapPubItemName = new HashMap<String,String>();
			// 将菜品门店编码容器改为map 提高查找效率
			for(FdItemSale item : listPubItemCode) {
				mapPubItemCode.put(item.getId(), item.getItcode());
				mapPubItemName.put(item.getId(), item.getDes());
			}
			
			// 获取菜品附加项列表
			List<NetDishAddItem> listNetDishAddItem = orderMapper.getDishAddItemList(orders.getPk_group(), orders.getId());
			// 获取附加产品列表
			List<NetDishProdAdd> listNetDishProdAdd = orderMapper.getDishProdAddList(orders.getPk_group(), orders.getId());
			// 将附加产品作为附加项传给POS
			if(null != listNetDishProdAdd && !listNetDishProdAdd.isEmpty()) {
				for(NetDishProdAdd item : listNetDishProdAdd) {
					NetDishAddItem temp = new NetDishAddItem();
					temp.setFcode(item.getFcode());
					temp.setNcount(item.getNcount());
					temp.setNprice(item.getNprice());
					temp.setPk_dishAddItem(item.getPk_dishProdAdd());
					temp.setPk_group(item.getPk_group());
					temp.setPk_orderDtlId(item.getPk_orderDtlId());
					temp.setPk_ordersId(item.getPk_ordersId());
					temp.setPk_prodcutReqAttAc(item.getPk_prodReqAdd());
					temp.setPk_redefine(item.getPk_prodAdd());
					temp.setPk_pubItem(item.getPk_pubitem());
					temp.setRedefineName(item.getProdAddName());
					temp.setUnit(item.getUnit());
					listNetDishAddItem.add(temp);
				}
			}
			double totalPrice = 0.0;
			// 是否删除自动添加的菜品，如果所有菜品的类别和配置文件中的相同，则删除
			boolean delAutoItem = true;
			String delAutoPubitemType = Commons.getConfig().getProperty("delAutoPubitemType");
			
			//先循环列表，找出套餐的附加项和附加产品
			for (Net_OrderDtl dtl : orderDtl) {
				List<NetDishAddItem> tempList = null;
				//如果菜品是套餐
				if("1".equals(dtl.getIspackage())){
					List<Map<String, Object>> listPacageDtl  = null;
					try {
						listPacageDtl = PubitemSearch.listFolioPacageDtl(dtl);
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}
					if(listPacageDtl != null && listPacageDtl.size()>0){
						for (Map<String,Object> mapDtl : listPacageDtl){
							mapDtl.put("tcpcode", mapPubItemCode.get(mapDtl.get("pk_pubitem")));//获取菜品编码
							mapDtl.put("tcpname", mapPubItemName.get(mapDtl.get("pk_pubitem")));//获取菜品名称
							mapDtl.put("tctotalprice", mapDtl.get("tcprice"));//获取菜品总金额
							//处理套餐菜品的附加项及附加产品
							tempList = new ArrayList<NetDishAddItem>();
							for (NetDishAddItem item : listNetDishAddItem) {
								if (mapDtl.get("pk_orderpackagedetail").equals(item.getPk_orderDtlId()) && mapDtl.get("pk_pubitem").equals(item.getPk_pubItem())) {
									tempList.add(item);
									if(null != item.getNprice() && !"".equals(item.getNprice())) {
										totalPrice += Double.parseDouble(item.getNprice()) * Integer.parseInt(dtl.getFoodnum());
									}
								}
							}
							if (!tempList.isEmpty()) {
								mapDtl.put("tclistDishAddItem", tempList);
								listNetDishAddItem.removeAll(tempList);
							}
						}
						dtl.setListDishTcItem(listPacageDtl);
					}
				}
			}
			
			//循环账单明细，将附加项与菜品关联起来
			for (Net_OrderDtl dtl : orderDtl) {
				if (null != dtl.getPrice() && !"".equals(dtl.getPrice())) {
					totalPrice += Double.parseDouble(dtl.getPrice()) * Integer.parseInt(dtl.getFoodnum());
				}

				// 设置菜品的门店编码
				for(FdItemSale item : listPubItemCode) {
					if(dtl.getFoodsid().equals(item.getId())) {
						dtl.setPcode(item.getItcode());
						break;
					}
				}

				
				List<NetDishAddItem> tempList = null;
				// 将附加项添加到菜品下
				tempList = new ArrayList<NetDishAddItem>();
				for (NetDishAddItem item : listNetDishAddItem) {
					if (dtl.getFoodsid().equals(item.getPk_pubItem())) {
						tempList.add(item);
						if(null != item.getNprice() && !"".equals(item.getNprice())) {
							totalPrice += Double.parseDouble(item.getNprice()) * Integer.parseInt(dtl.getFoodnum());
						}
					}
				}
				if (!tempList.isEmpty()) {
					dtl.setListDishAddItem(tempList);
				}

				if(null != dtl.getGrptyp() && !dtl.getGrptyp().equals(delAutoPubitemType)) {
					// 有不同的类别，不删除
					delAutoItem = false;
				}
				
			}
			orders.setListNetOrderDtl(orderDtl);

			orders.setSumprice(String.valueOf(totalPrice));
			orders.setSerialid(CodeHelper.createUUID());
			orders.setType(type);

			return orders;
		}
		return null;
	}
	
}
