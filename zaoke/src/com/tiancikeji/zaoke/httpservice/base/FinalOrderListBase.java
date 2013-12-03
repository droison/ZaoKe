package com.tiancikeji.zaoke.httpservice.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FinalOrderListBase {

	private int status; // 0Ϊ��10141Ϊ����������Ϊ�գ�,10140Ϊδ������� -----�˴����Ϊname
	private String msg;
	private Map<String, FinalOrderBase> orders;
	private List<String> orderid;
	

	public List<String> getOrderid() {
		return orderid;
	}

	public void setOrderid(List<String> orderid) {
		this.orderid = orderid;
	}

	public Map<String, FinalOrderBase> getOrders() {
		return orders;
	}

	public void setOrders(Map<String, FinalOrderBase> orders) {
		this.orders = orders;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
