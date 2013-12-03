package com.tiancikeji.zaoke.httpservice.base;

import java.io.Serializable;
import java.util.List;

public class LocalBase implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5468448365114067847L;
	private int pick_loc_id;
	private String pick_loc_name;
	private String pick_end_time;
	private String pick_start_time;
	private List<LocalBase> pick_loc_list;

	public int getPick_loc_id() {
		return pick_loc_id;
	}

	public void setPick_loc_id(int pick_loc_id) {
		this.pick_loc_id = pick_loc_id;
	}

	public String getPick_loc_name() {
		return pick_loc_name;
	}

	public void setPick_loc_name(String pick_loc_name) {
		this.pick_loc_name = pick_loc_name;
	}

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

	public List<LocalBase> getPick_loc_list() {
		return pick_loc_list;
	}

	public void setPick_loc_list(List<LocalBase> pick_loc_list) {
		this.pick_loc_list = pick_loc_list;
	}

}
