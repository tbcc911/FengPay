package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "ProfitAd")
public class ProfitAd extends Model {
	@Column(name = "adid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private String adid;// 广告ID
	@Column
	private double predictmoney;// 预投放金额
	@Column
	private double aleradynumey;// 已投放金额
	@Column
	private int leftnum;// 左滑总数
	@Column
	private int rightnum;// 右滑总数
	@Column
	private int clicknum;// 点击总数
	public String getAdid() {
		return adid;
	}
	public void setAdid(String adid) {
		this.adid = adid;
	}
	public double getPredictmoney() {
		return predictmoney;
	}
	public void setPredictmoney(double predictmoney) {
		this.predictmoney = predictmoney;
	}
	public double getAleradynumey() {
		return aleradynumey;
	}
	public void setAleradynumey(double aleradynumey) {
		this.aleradynumey = aleradynumey;
	}
	public int getLeftnum() {
		return leftnum;
	}
	public void setLeftnum(int leftnum) {
		this.leftnum = leftnum;
	}
	public int getRightnum() {
		return rightnum;
	}
	public void setRightnum(int rightnum) {
		this.rightnum = rightnum;
	}
	public int getClicknum() {
		return clicknum;
	}
	public void setClicknum(int clicknum) {
		this.clicknum = clicknum;
	}
	
	

}
