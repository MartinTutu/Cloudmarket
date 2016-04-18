package com.martin.cloudmarket.demo;

public class Goods {
	private String id; 
	private String title;
	private String type;
	private String xiaoliang;
	private String number;
	private String detail;
	private String jiage;
	private String goodsURL;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
	public String getXiaoliang() {
		return xiaoliang;
	}
	public void setXiaoliang(String xiaoliang) {
		this.xiaoliang = xiaoliang;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getJiage() {
		return jiage;
	}
	public void setJiage(String jiage) {
		this.jiage = jiage;
	}
	public String getGoodsURL() {
		return goodsURL;
	}
	public void setGoodsURL(String goodsURL) {
		this.goodsURL = goodsURL;
	}
	@Override
	public String toString() {
		return "Goods [id=" + id + ", title=" + title + ", type=" + type + ", xiaoliang=" + xiaoliang + ", number="
				+ number + ", detail=" + detail + ", jiage=" + jiage + ", goodsURL=" + goodsURL + "]";
	}
	
}
