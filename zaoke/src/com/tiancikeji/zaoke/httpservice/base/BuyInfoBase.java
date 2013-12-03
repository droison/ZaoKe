package com.tiancikeji.zaoke.httpservice.base;

public class BuyInfoBase {
	private int status; // 10132一个参数也没加 10120食品不存在 0表示成功
	private String msg;
	private Double combo_price;
	private Double sale_price;

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

	public Double getCombo_price() {
		return combo_price;
	}

	public void setCombo_price(Double combo_price) {
		this.combo_price = combo_price;
	}

	public Double getSale_price() {
		return sale_price;
	}

	public void setSale_price(Double sale_price) {
		this.sale_price = sale_price;
	}

}
