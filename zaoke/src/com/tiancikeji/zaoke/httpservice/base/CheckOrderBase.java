package com.tiancikeji.zaoke.httpservice.base;

import java.util.List;

public class CheckOrderBase {

	private String pick_date;
	private int status; // 0Ϊ��
	private int state;
	private String ticket;
	private int userid;
	private String msg;
	private double balance;
	private String pick_time;
	private int pick_loc_id;
	private String pick_loc_name;
	private int needcheck;
	private List<PaymodeBase> paymode;

	
	
	public String getPick_date() {
		return pick_date;
	}

	public void setPick_date(String pick_date) {
		this.pick_date = pick_date;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getPick_time() {
		return pick_time;
	}

	public void setPick_time(String pick_time) {
		this.pick_time = pick_time;
	}

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

	public int getNeedcheck() {
		return needcheck;
	}

	public void setNeedcheck(int needcheck) {
		this.needcheck = needcheck;
	}

	public List<PaymodeBase> getPaymode() {
		return paymode;
	}

	public void setPaymode(List<PaymodeBase> paymode) {
		this.paymode = paymode;
	}

}
