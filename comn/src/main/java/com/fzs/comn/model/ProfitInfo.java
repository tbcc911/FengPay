package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * 用户当前收益明细
 * */
@Table(name = "ProfitInfo")
public class ProfitInfo extends Model{
	@Column(name = "userid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private String userid;//用户ID
	
	@Column
	private long date;//收益日期
	@Column
	private String money;//收益金额
	@Column
	private String name;//收益明细
	@Column
	private String type;//收益类型
	@Column
	private String icon;//收益类型图标URL
	@Column
	private String moneyType;//收支类型
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getMoneyType() {
		return moneyType;
	}
	public void setMoneyType(String moneyType) {
		this.moneyType = moneyType;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	
	
}
