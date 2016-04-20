package com.martin.cloudmarket.demo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
	
	private String ID;
	private String  date;
	private String address;
	private String tel;
	private List<OrderItem> orderList = new ArrayList<OrderItem>();
	private int paycondition = 0;
	private float price;
	public String getID() {
		return ID;
	}
	public void setID(int id){
		ID = id + "";
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public List<OrderItem> getOrderList() {
		return orderList;
	}
	public void setOrderList(List<OrderItem> orderList) {
		this.orderList = orderList;
	}
	public int getPaycondition() {
		return paycondition;
	}
	public void setPaycondition(int paycondition) {
		this.paycondition = paycondition;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		float  b   =  ((float)(Math.round(price*100)))/100;
		this.price = b;
		System.out.println(b);
	}
	@Override
	public String toString() {
		return "Order [ID=" + ID + ", date=" + date + ", address=" + address + ", tel=" + tel + ", orderList="
				+ orderList + ", paycondition=" + paycondition + ", price=" + price + "]";
	}
	
}
