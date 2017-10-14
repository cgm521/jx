package com.choice.wechat.service.campaign;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.choice.test.domain.Net_OrderDtl;
import com.choice.test.utils.Commons;
import com.choice.wechat.domain.bookMeal.FdItemSale;
import com.choice.wechat.domain.campaign.ActmItem;
import com.choice.wechat.domain.game.Actm;
import com.choice.wechat.persistence.bookMeal.BookMealMapper;
import com.choice.wechat.persistence.campaign.CampaignMapper;
import com.choice.wechat.persistence.campaign.impl.CampaignMapperImpl;
import com.choice.wechat.util.WeChatUtil;

public class CampaignTools {
	private static CampaignMapper campaignMapper = new CampaignMapperImpl();
	
	/**
	 * 获取外送优惠活动
	 * @param firmid
	 * @return
	 */
	public static List<Actm> getListTakeOutActm(String firmid) {
		return campaignMapper.listTakeOutActm(firmid);
	}
	
	/**
	 * 计算应付金额
	 * @param pkActm
	 * @param billMoney
	 * @param listActm
	 * @param orderDtl
	 * @return
	 */
	public static double calcPayMoney(String pkActm, double billMoney, List<Actm> listActm, List<Net_OrderDtl> orderDtl) {
		double payMoney = billMoney;
		double deliverFee = 0; //外送费
		boolean hasDeliverFee = true; //是否包含外送费
		
		// 外送费不参与折扣，先执行免外送费活动
		for(Actm actm : listActm) {
			if("Y".equals(actm.getBadjust()) && billMoney >= actm.getIdownamount()) {
				// 免外送费
				// 获取活动菜品列表
				List<ActmItem> listActmItem = campaignMapper.listActmItem(actm.getPk_actm(), null);
				for(Net_OrderDtl dtl : orderDtl) {
					for(ActmItem item : listActmItem) {
						if(dtl.getFoodsid().equals(item.getPk_pubitem())) {
							if(billMoney >= actm.getIdownamount() + Double.parseDouble(dtl.getPrice())) {
								// 如果菜品明细中存在启用调价的菜品,菜品总价中减去此菜品价格
								payMoney = payMoney - Double.parseDouble(dtl.getPrice());
								// 外送费已减免
								hasDeliverFee = false;
							} else {
								deliverFee = Double.parseDouble(dtl.getPrice());
							}
						}
					}
				}
			}
		}
		
		//如果外送费没有减免，需要计算出外送费
		if(hasDeliverFee) {
			String itemCode = Commons.getConfig().getProperty("itemCodeOfDeliver");
			List<FdItemSale> listItem = campaignMapper.getItemByCode(itemCode);
			if(null != listItem && !listItem.isEmpty()) {
				FdItemSale item = listItem.get(0);
				for(Net_OrderDtl dtl : orderDtl) {
					if(dtl.getFoodsid().equals(item.getId())) {
						deliverFee = Double.parseDouble(dtl.getPrice());
					}
				}
			}
		}
		
		// 执行满减满折活动
		for(Actm actm : listActm) {
			if("Y".equals(actm.getBremit())) {
				// 账单减免
				if(billMoney >= actm.getIdownamount() && pkActm.equals(actm.getPk_actm())) {
					// 启用金额限制，并且账单金额大于下限金额
					if("1".equals(actm.getJmrdo())) {
						// 折扣比例
						//payMoney = billMoney * actm.getNdiscountrate();
						// 折扣比例，按比例计算折扣金额
						double rate = Double.parseDouble(WeChatUtil.dividedNum(actm.getNderatenum(), 100, 2));
						
						// 如果收取外送费，外送费不参与打折活动
						payMoney = (payMoney - deliverFee) * rate + deliverFee;
					} else {
						payMoney = payMoney - actm.getNderatenum();
					}
				}
			}
		}
		
		return payMoney;
	}
	
	/**
	 * 计算各活动的折扣金额，返回折扣金额最大的活动主键
	 * @param billMoney
	 * @param listActm
	 * @return
	 */
	public static String getMaxDiscountActm(double billMoney, List<Actm> listActm) {
		String pkActm = "";
		double discount = 0;
		for(Actm actm : listActm) {
			if("Y".equals(actm.getBremit())) {
				double tempDiscount = 0;
				// 账单减免
				if(billMoney >= actm.getIdownamount()) {
					// 启用金额限制，并且账单金额大于下限金额
					if("2".equals(actm.getJmrdo())) {
						// 折扣比例，按比例计算折扣金额
						double rate = Double.parseDouble(WeChatUtil.dividedNum(actm.getNderatenum(), 100, 2));
						tempDiscount = billMoney * (1 - rate);
					} else {
						tempDiscount = actm.getNderatenum();
					}
				}
				
				// 如果此活动的折扣金额大于以前的活动，使用此活动
				if(tempDiscount >= discount) {
					pkActm = actm.getPk_actm();
					discount = tempDiscount;
				}
			}
		}
		
		return pkActm;
	}
	
	/**
	 * 过滤账单减免活动，只保留折扣最大的活动
	 * @param pkActm
	 * @param billMoney
	 * @param listActm
	 * @param orderDtl
	 * @return
	 */
	public static List<Actm> filterRemitCampaign(String pkActm,
			double billMoney, List<Actm> listActm, List<Net_OrderDtl> orderDtl) {
		List<Actm> tempList = new ArrayList<Actm>();
		for (Actm actm : listActm) {
			if ("Y".equals(actm.getBremit())) {
				if (pkActm.equals(actm.getPk_actm())) {
					tempList.add(actm);
				}
			} else if("Y".equals(actm.getBadjust()) && billMoney >= actm.getIdownamount()) {
				// 免外送费
				// 获取活动菜品列表
				List<ActmItem> listActmItem = campaignMapper.listActmItem(actm.getPk_actm(), null);
				for(Net_OrderDtl dtl : orderDtl) {
					for(ActmItem item : listActmItem) {
						if(dtl.getFoodsid().equals(item.getPk_pubitem()) 
								&& billMoney >= actm.getIdownamount() + Double.parseDouble(dtl.getPrice())) {
							// 如果菜品明细中存在启用调价的菜品,菜品总价中减去此菜品价格
							tempList.add(actm);
						}
					}
				}
			} else {
				tempList.add(actm);
			}
		}

		return tempList;
	}
	
	/**
	 * 计算折扣金额
	 * @param billMoney
	 * @param actm
	 * @return
	 */
	public static Double getDiscountMoney(double billMoney, Actm actm) {
		double res = 0;
		if("2".equals(actm.getJmrdo())) {
			// 折扣比例，按比例计算折扣金额
			double rate = Double.parseDouble(WeChatUtil.dividedNum(actm.getNderatenum(), 100, 2));
			res = billMoney * (1 - rate);
		} else {
			res = actm.getNderatenum();
		}
		
		return res;
	}
}
