package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * 广告商排行
 * */
@Table(name = "RankAder")
public class RankAder extends Model {
	@Column(name = "aderid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private String aderid;//广告商ID
	
	@Column(name="userid")
	private String userid;//用户ID
	
	@Column(name="rank")
	private String rank;//排名
	
	@Column(name="nc")
	private String nc;//昵称
	
	@Column(name="time")
	private String time;//时间
	
	@Column(name="money")
	private String money;//金额
	
	@Column(name="number")
	private int number;//数量
	
	@Column(name="grade")
	private String grade;//等级
	
	@Column(name="head")
	private String head;//头像

	public String getAderid() {
		return aderid;
	}

	public void setAderid(String aderid) {
		this.aderid = aderid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getNc() {
		return nc;
	}

	public void setNc(String nc) {
		this.nc = nc;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}
	
}
