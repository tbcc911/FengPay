package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Yq")
public class Yq extends Model {
	@Column(name = "userid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private int userid;//用户ID
	
	@Column(name = "today")
	private double today;// 今日邀请
	
	@Column(name = "num")
	private int num;// 邀请总人数
	
	@Column(name = "suc")
	private int suc;// 成功邀请总人数
	
	@Column(name = "sum")
	private double sum;//邀请总收益
	
	@Column(name = "sucsum")
	private double sucsum;//成功邀请总收益
	
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public double getToday() {
		return today;
	}
	public void setToday(double today) {
		this.today = today;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getSuc() {
		return suc;
	}
	public void setSuc(int suc) {
		this.suc = suc;
	}
	public double getSum() {
		return sum;
	}
	public void setSum(double sum) {
		this.sum = sum;
	}
	public double getSucsum() {
		return sucsum;
	}
	public void setSucsum(double sucsum) {
		this.sucsum = sucsum;
	}
	
}
