package com.choice.wechat.persistence.campaign;

import java.util.List;

import com.choice.wechat.domain.bookMeal.FdItemSale;
import com.choice.wechat.domain.campaign.ActmItem;
import com.choice.wechat.domain.game.Actm;

public interface CampaignMapper {
	
	/**
	 * 查询外送活动
	 * @param firmid
	 * @return
	 */
	public List<Actm> listTakeOutActm(String firmid);
	
	/**
	 * 获取活动菜品明细 （账单减免和启用调价菜品）
	 * @param pk_actm
	 * @param actmCode
	 * @return
	 */
	public List<ActmItem> listActmItem(String pk_actm, String actmCode);
	
	/**
	 * 根据菜品编码获取菜品
	 * @param code
	 * @return
	 */
	public List<FdItemSale> getItemByCode(String code);
}
