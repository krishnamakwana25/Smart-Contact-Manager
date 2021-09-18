package com.smartcontactmanager.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Entity
@Table(name="orders")
public class MyOrder 
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long myorderid;
	
	private String orderid;
	
	private String amount;
	
	private String receipt;
	
	private String status;
	
	@ManyToOne
	private User user;
	
	private String paymentid;

	public Long getMyorderid() {
		return myorderid;
	}

	public void setMyorderid(Long myorderid) {
		this.myorderid = myorderid;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getReceipt() {
		return receipt;
	}

	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getPaymentid() {
		return paymentid;
	}

	public void setPaymentid(String paymentid) {
		this.paymentid = paymentid;
	}

	
}