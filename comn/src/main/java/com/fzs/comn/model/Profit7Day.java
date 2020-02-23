package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Profit7Day")
public class Profit7Day extends Model {
	@Column(name = "xtime", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private String xtime;// 日期
	@Column
	private double ynum;// 数量
	public String getXtime() {
		return xtime;
	}
	public void setXtime(String xtime) {
		this.xtime = xtime;
	}
	public double getYnum() {
		return ynum;
	}
	public void setYnum(double ynum) {
		this.ynum = ynum;
	}
	

}
