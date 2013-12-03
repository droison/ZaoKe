package com.tiancikeji.zaoke.httpservice.base;

import java.io.Serializable;

public class OrderBase implements Serializable {

	private double combo_price;
	private double sale_price;
	private FoodInfoBase shipin;
	private FoodInfoBase yinliao;

	public double getCombinePrice() {
		return combo_price;
	}

	public void setCombinePrice(double combo_price) {
		this.combo_price = combo_price;
	}

	public double getPrice() {
		return sale_price;
	}

	public void setPrice(double sale_price) {
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

}
