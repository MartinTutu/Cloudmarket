package com.martin.cloudmarket.demo;

import android.widget.Toast;

public class PayMsg {
	private String ordernum;
	private String username;
	private String shopname;
	private String price;
	
	public PayMsg() {
		ordernum = null;
		username = "сн©м";
		shopname = null;
		price = "0";
	}
	
	public PayMsg(String s) {
		String msg = s.substring(1,s.length()-1);
		System.out.println(msg);
		String[] m = msg.split(";");
		ordernum = m[0];
		username = m[1];
		shopname = m[2];
		price = m[3];
	}
	
	public String getOrdernum() {
		return ordernum;
	}
	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getShopname() {
		return shopname;
	}
	public void setShopname(String shopname) {
		this.shopname = shopname;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	@Override
	public String toString() {
		return "{" + ordernum + ";" + username + ";" + shopname + ";" + price + "}";
	}
}
