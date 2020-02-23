package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * 用户当前收益明细
 * */
@Table(name = "ChangeMoney")
public class ChangeMoney extends Model{
	@Column(name = "userid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private String userid;//用户ID
	@Column
	private String buy_time;//报销时间
	@Column
	private String money;//金额
	@Column
	private String images;//图片
	@Column
	private String prod_name;//名称
	@Column
	private String state;//状态
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getBuy_time() {
		return buy_time;
	}
	public void setBuy_time(String buy_time) {
		this.buy_time = buy_time;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getImages() {
		return images;
	}
	public void setImages(String images) {
		this.images = images;
	}
	public String getProd_name() {
		return prod_name;
	}
	public void setProd_name(String prod_name) {
		this.prod_name = prod_name;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	
}
