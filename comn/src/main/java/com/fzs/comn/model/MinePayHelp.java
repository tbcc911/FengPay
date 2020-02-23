package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "MinePayHelp")
public class MinePayHelp extends Model {
	@Column(name = "_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private String _id;
	@Column(name = "orderId")
	private String orderId;
	@Column(name = "name")
	private String name;
	@Column(name = "payHelpMoney")
	private String payHelpMoney;
	@Column(name = "date")
	private String date;
	
	
	public String getName() {
		return name;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPayHelpMoney() {
		return payHelpMoney;
	}
	public void setPayHelpMoney(String payHelpMoney) {
		this.payHelpMoney = payHelpMoney;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	
}
