package com.choice.wechat.persistence.alipay;

import java.util.List;
import java.util.Map;

public interface AlipayBillMapper {
	/**
	 * 删除支付宝账单
	 * @param partner 支付宝合作者身份(PID)
	 * @param dat 日期
	 */
	public void deleteBills(String partner, String dat);
	
	/**
	 * 批量插入支付宝账单
	 * @param list
	 */
	public void batchInsertBills(List<Map<String,String>> list);
}
