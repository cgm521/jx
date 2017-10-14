package com.choice.wxc.persistence.diningEvaluate;

import java.util.List;

import com.choice.wxc.domain.diningEvaluate.NetOrderDtl;
import com.choice.wxc.domain.diningEvaluate.OlpEvaluate;

public interface DiningEvaluateMapper {
	/**
	 * 获取订单菜品列表
	 * @param dtl
	 * @return
	 */
	public List<NetOrderDtl> getListOrderDtl(NetOrderDtl dtl);
	
	/**
	 * 根据订单ID获取订单评价详情
	 * @param eva
	 * @return
	 */
	public List<OlpEvaluate> getListEvaluate(OlpEvaluate eva);
	
	/**
	 * 保存评价结果
	 * @param evaluateInfo
	 * @return
	 */
	public int saveEvaluateInfo(OlpEvaluate evaluateInfo);
}
