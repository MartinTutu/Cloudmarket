package com.martin.cloudmarket.demo;

public class Shop {
	private String title;
	private String type;
	private String mark;
	private String tradingarea;
	private String qisong;
	private String distance;
	private String logoURL;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getTradingarea() {
		return tradingarea;
	}
	public void setTradingarea(String tradingarea) {
		this.tradingarea = tradingarea;
	}
	public String getQisong() {
		return qisong;
	}
	public void setQisong(String qisong) {
		this.qisong = qisong;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getLogoURL() {
		return logoURL;
	}
	public void setLogoURL(String logoURL) {
		this.logoURL = logoURL;
	}
	@Override
	public String toString() {
		return "Shop [title=" + title + ", type=" + type + ", mark=" + mark + ", tradingarea=" + tradingarea
				+ ", qisong=" + qisong + ", distance=" + distance + ", logoURL=" + logoURL + "]";
	}
	
}
