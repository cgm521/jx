package com.choice.test.domain;

import java.util.List;

public class CallBillOrder {
	private List<CALLBILL_Order> CALLBILL_Orders;
	private List<CALLBILL_DEAILDE> CALLBILL_DEAILDEs;
	public List<CALLBILL_Order> getCALLBILL_Orders() {
		return CALLBILL_Orders;
	}
	public void setCALLBILL_Orders(List<CALLBILL_Order> orders) {
		CALLBILL_Orders = orders;
	}
	public List<CALLBILL_DEAILDE> getCALLBILL_DEAILDEs() {
		return CALLBILL_DEAILDEs;
	}
	public void setCALLBILL_DEAILDEs(List<CALLBILL_DEAILDE> es) {
		CALLBILL_DEAILDEs = es;
	}
	
}
