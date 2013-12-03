package com.tiancikeji.zaoke.httpservice.base;

public class SendCodeBase {

	private int status; // 0为正常，10141为参数错误（入参为空）,10140为未设置入参 -----此处入参为name
	private String msg;
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
