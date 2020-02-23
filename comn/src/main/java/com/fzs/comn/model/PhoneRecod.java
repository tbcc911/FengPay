package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

@Table(name = "PhoneRecod")
public class PhoneRecod extends Model {
	@Column(name = "phone", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private String phone;
	@Column
	private int num;
	@Column
	private Date lasttime;
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public Date getLasttime() {
		return lasttime;
	}
	public void setLasttime(Date lasttime) {
		this.lasttime = lasttime;
	}

}
