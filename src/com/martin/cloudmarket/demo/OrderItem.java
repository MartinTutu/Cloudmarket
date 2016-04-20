package com.martin.cloudmarket.demo;


public class OrderItem {
	private String  orderID;
	private String  shopName;
	private String  shopID;
	private String  goodName;
	private String  goodID;
	private int     goodNum;
	private float     goodPrice;
	
	public String getOrderID() {
		return orderID;
	}
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getShopID() {
		return shopID;
	}
	public void setShopID(String shopID) {
		this.shopID = shopID;
	}
	public String getGoodName() {
		return goodName;
	}
	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
	public String getGoodID() {
		return goodID;
	}
	public void setGoodID(String goodID) {
		this.goodID = goodID;
	}
	public int getGoodNum() {
		return goodNum;
	}
	public void setGoodNum(int goodNum) {
		this.goodNum = goodNum;
	}
	public void setGoodNum(String s){
		this.goodNum = Integer.parseInt(s);
	}
	public float getGoodPrice() {
		return goodPrice;
	}
	public void setGoodPrice(float goodPrice) {
		float  b   =  ((float)(Math.round(goodPrice*100)))/100;
		this.goodPrice = b;
	}
	public void setGoodPrice(String s){
		float  b   =  ((float)(Math.round(Float.parseFloat(s)*100)))/100;
		this.goodPrice = b;
	}
	@Override
	public String toString() {
		return "OrderItem [orderID=" + orderID + ", shopName=" + shopName + ", shopID=" + shopID + ", goodName="
				+ goodName + ", goodID=" + goodID + ", goodNum=" + goodNum + ", goodPrice=" + goodPrice + "]";
	}	
}
