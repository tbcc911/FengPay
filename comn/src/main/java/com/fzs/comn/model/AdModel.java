package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "AdModel")
public class AdModel extends Model {
	@Column(name = "adid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private String adid;// 广告ID
	@Column
	private String picture;// 广告展示图片
	@Column
	private String money;// 金额
	@Column
	private String staticName;// 状态名称
	@Column
	private String leftAction;// 左滑动作
	@Column
	private String startTime;// 开始时间
	@Column
	private String endTime;// 结束时间
	@Column
	private String state;// 状态
	@Column
	private String createTime;// 创建时间
	@Column
	private String leftValue;// 左滑值
	@Column
	private String descrite;// 广告描述
	@Column(name = "leftName")
	private String leftName;// 左滑名称
	

	public String getAdid() {
		return adid;
	}

	public void setAdid(String adid) {
		this.adid = adid;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getStaticName() {
		return staticName;
	}

	public void setStaticName(String staticName) {
		this.staticName = staticName;
	}

	public String getLeftAction() {
		return leftAction;
	}

	public void setLeftAction(String leftAction) {
		this.leftAction = leftAction;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getLeftValue() {
		return leftValue;
	}

	public void setLeftValue(String leftValue) {
		this.leftValue = leftValue;
	}

	public String getDescrite() {
		return descrite;
	}

	public void setDescrite(String descrite) {
		this.descrite = descrite;
	}

	public String getLeftName() {
		return leftName;
	}

	public void setLeftName(String leftName) {
		this.leftName = leftName;
	}

}
