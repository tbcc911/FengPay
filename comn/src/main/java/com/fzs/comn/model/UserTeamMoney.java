package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "UserTeamMoney")
public class UserTeamMoney extends Model {
	@Column(name = "lid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private String lid;
	
	@Column(name = "name")//姓名
	private String name;
	
	@Column(name = "headUrl")//头像
	private String headUrl;
	
	@Column(name= "phone")//电话号码
	private String phone;
	
	@Column(name= "sumMoney")//用户累计收益
	private String sumMoney;
	
	@Column(name= "dayMoney")//日新增收益
	private String dayMoney;
	
	@Column(name= "weekMoney")//周新增收益
	private String weekMoney;
	
	@Column(name= "monthMoney")//月新增收益
	private String monthMoney;

	public String getLid() {
		return lid;
	}

	public void setLid(String lid) {
		this.lid = lid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSumMoney() {
		return sumMoney;
	}

	public void setSumMoney(String sumMoney) {
		this.sumMoney = sumMoney;
	}

	public String getDayMoney() {
		return dayMoney;
	}

	public void setDayMoney(String dayMoney) {
		this.dayMoney = dayMoney;
	}

	public String getWeekMoney() {
		return weekMoney;
	}

	public void setWeekMoney(String weekMoney) {
		this.weekMoney = weekMoney;
	}

	public String getMonthMoney() {
		return monthMoney;
	}

	public void setMonthMoney(String monthMoney) {
		this.monthMoney = monthMoney;
	}
	
	

}
