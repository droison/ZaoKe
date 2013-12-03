package com.tiancikeji.zaoke.httpservice.base;

import java.util.List;

public class LocListBase {

	private int status; // 0ÎªÕý³£
	private String msg;
	private List<LocalBase> pick_locs;

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

	public List<LocalBase> getPick_locs() {
		return pick_locs;
	}

	public void setPick_locs(List<LocalBase> pick_locs) {
		this.pick_locs = pick_locs;
	}

}
