package com.tiancikeji.zaoke.httpservice.base;

public class SendCodeBase {

	private int status; // 0Ϊ������10141Ϊ�����������Ϊ�գ�,10140Ϊδ������� -----�˴����Ϊname
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
