package com.tiancikeji.zaoke.db.base;

import java.io.Serializable;

import android.graphics.drawable.Drawable;

public class Dborder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3679861370100167993L;
	/**
	 * 此处数据订单生成
	 */
	private String orderId;
	private Integer paymode;
	private Long ordertime;
	private Double balance;

	/**
	 * 此处参数为传递过来的
	 */
	private String shiwuName;
	private Integer shiwuId;
	private Double shiwuPrice=(double) 0;
	private String shiwuUrl;
	private String yinliaoName;
	private Integer yinliaoId;
	private Double yinliaoPrice=(double) 0;
	private String yinliaoUrl;
	private Double combPrice;

	/**
	 * 此处为设置完地址后从数据库获得， 网络获得写入数据库的没有endtime参数，所有pick_time数据均存在starttime中
	 */
	private Integer locId;
	private String starttime;
	private String endtime;
	private String local;

	/**
	 * 此处为用户数据，数据库获得
	 */
	private String userid;
	private byte[] qxImg;

	public Integer getPaymode() {
		return paymode;
	}

	public void setPaymode(Integer paymode) {
		this.paymode = paymode;
	}

	public String getShiwuUrl() {
		return shiwuUrl;
	}

	public void setShiwuUrl(String shiwuUrl) {
		this.shiwuUrl = shiwuUrl;
	}

	public String getYinliaoUrl() {
		return yinliaoUrl;
	}

	public void setYinliaoUrl(String yinliaoUrl) {
		this.yinliaoUrl = yinliaoUrl;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getShiwuName() {
		return shiwuName;
	}

	public void setShiwuName(String shiwuName) {
		this.shiwuName = shiwuName;
	}

	public Integer getShiwuId() {
		return shiwuId;
	}

	public void setShiwuId(Integer shiwuId) {
		this.shiwuId = shiwuId;
	}

	public Double getShiwuPrice() {
		return shiwuPrice;
	}

	public void setShiwuPrice(Double shiwuPrice) {
		this.shiwuPrice = shiwuPrice;
	}

	public String getYinliaoName() {
		return yinliaoName;
	}

	public void setYinliaoName(String yinliaoName) {
		this.yinliaoName = yinliaoName;
	}

	public Integer getYinliaoId() {
		return yinliaoId;
	}

	public void setYinliaoId(Integer yinliaoId) {
		this.yinliaoId = yinliaoId;
	}

	public Double getYinliaoPrice() {
		return yinliaoPrice;
	}

	public void setYinliaoPrice(Double yinliaoPrice) {
		this.yinliaoPrice = yinliaoPrice;
	}

	public Double getCombPrice() {
		return combPrice;
	}

	public void setCombPrice(Double combPrice) {
		this.combPrice = combPrice;
	}

	public Long getOrdertime() {
		return ordertime;
	}

	public void setOrdertime(Long ordertime) {
		this.ordertime = ordertime;
	}

	public Integer getLocId() {
		return locId;
	}

	public void setLocId(Integer locId) {
		this.locId = locId;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public byte[] getQxImg() {
		return qxImg;
	}

	public void setQxImg(byte[] qxImg) {
		this.qxImg = qxImg;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public boolean isComplete() {
		Boolean flag1 = shiwuName != null && shiwuId != null && shiwuPrice != null && combPrice != null;
		Boolean flag2 = yinliaoName != null && yinliaoId != null && yinliaoPrice != null && combPrice != null;
		return flag1 || flag2;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("orderId="+orderId+",");
		sb.append("paymode="+paymode+",");
		sb.append("ordertime="+ordertime+",");
		sb.append("balance="+balance+",");
		
		sb.append("shiwuName="+shiwuName+",");
		sb.append("shiwuId="+shiwuId+",");
		sb.append("shiwuPrice="+shiwuPrice+",");
		sb.append("shiwuUrl="+shiwuUrl+",");
		
		sb.append("yinliaoName="+yinliaoName+",");
		sb.append("yinliaoId="+yinliaoId+",");
		sb.append("yinliaoPrice="+yinliaoPrice+",");
		sb.append("yinliaoUrl="+yinliaoUrl+",");
		
		sb.append("combPrice="+combPrice+",");
		
		sb.append("locId="+locId+",");
		sb.append("starttime="+starttime+",");
		sb.append("endtime="+endtime+",");
		sb.append("local="+local+",");
		
		sb.append("userid="+userid+",");
		sb.append("qxImg="+qxImg!=null+",");

		return sb.toString();
	}

}
