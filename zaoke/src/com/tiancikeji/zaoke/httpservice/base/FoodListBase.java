package com.tiancikeji.zaoke.httpservice.base;

import java.util.List;

public class FoodListBase {

	private int status;
	private String defaultInfo;
	private String msg;
	private List<FoodInfoBase> shipin;
	private List<FoodInfoBase> yinliao;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDefaultInfo() {
		return defaultInfo;
	}

	public void setDefaultInfo(String defaultInfo) {
		this.defaultInfo = defaultInfo;
	}

	public List<FoodInfoBase> getShipin() {
		return shipin;
	}

	public void setShipin(List<FoodInfoBase> shipin) {
		this.shipin = shipin;
	}

	public List<FoodInfoBase> getYinliao() {
		return yinliao;
	}

	public void setYinliao(List<FoodInfoBase> yinliao) {
		this.yinliao = yinliao;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
