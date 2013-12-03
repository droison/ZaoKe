package com.tiancikeji.zaoke.httpservice.base;

public class FinalOrderBase {

	private double combo_price; // ���ֻ��һ����Ʒ����û�д˲���
	private double sale_price;
	private FoodInfoBase shipin;
	private FoodInfoBase yinliao;
	private String orderid;
	private String order_time;
	private String pick_time;
	private int paymode;
	private int status;
	private String pick_end_time;
	private String pick_start_time;

	
	public String getPick_end_time() {
		return pick_end_time;
	}

	public void setPick_end_time(String pick_end_time) {
		this.pick_end_time = pick_end_time;
	}

	public String getPick_start_time() {
		return pick_start_time;
	}

	public void setPick_start_time(String pick_start_time) {
		this.pick_start_time = pick_start_time;
	}

	public double getCombo_price() {
		return combo_price;
	}

	public void setCombo_price(double combo_price) {
		this.combo_price = combo_price;
	}

	public double getSale_price() {
		return sale_price;
	}

	public void setSale_price(double sale_price) {
		this.sale_price = sale_price;
	}

	public FoodInfoBase getShipin() {
		return shipin;
	}

	public void setShipin(FoodInfoBase shipin) {
		this.shipin = shipin;
	}

	public FoodInfoBase getYinliao() {
		return yinliao;
	}

	public void setYinliao(FoodInfoBase yinliao) {
		this.yinliao = yinliao;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getOrder_time() {
		return order_time;
	}

	public void setOrder_time(String order_time) {
		this.order_time = order_time;
	}

	public String getPick_time() {
		return pick_time;
	}

	public void setPick_time(String pick_time) {
		this.pick_time = pick_time;
	}

	public int getPaymode() {
		return paymode;
	}

	public void setPaymode(int paymode) {
		this.paymode = paymode;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
