package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Profit7DayAd")
public class Profit7DayAd extends Model {
	@Column(name = "xtime", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private String xtime;// 日期
	@Column
	private int ynum;// 数量
	public String getXtime() {
		return xtime;
	}
	public void setXtime(String xtime) {
		this.xtime = xtime;
	}
	public int getYnum() {
		return ynum;
	}
	public void setYnum(int ynum) {
		this.ynum = ynum;
	}
	

}
