package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "UserStealRedComn")
public class UserStealRedComn extends Model {
	@Column(name = "lid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private String lid;
	
	@Column(name = "nid")
	private String nid;
	
	@Column(name="money")
	private String money;

	@Column(name="leftNick")
	private String leftNick;
	
	@Column(name="leftHead")
	private String leftHead;
	
	@Column(name="rightNick")
	private String rightNick;
	
	@Column(name="rightHead")
	private String rightHead;
	
	@Column(name="time")
	private String time;
	
	@Column(name="state")
	private int state;
	
	@Column(name="backMoney")
	private String backMoney;

	public String getLid() {
		return lid;
	}

	public void setLid(String lid) {
		this.lid = lid;
	}

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getLeftNick() {
		return leftNick;
	}

	public void setLeftNick(String leftNick) {
		this.leftNick = leftNick;
	}

	public String getLeftHead() {
		return leftHead;
	}

	public void setLeftHead(String leftHead) {
		this.leftHead = leftHead;
	}

	public String getRightNick() {
		return rightNick;
	}

	public void setRightNick(String rightNick) {
		this.rightNick = rightNick;
	}

	public String getRightHead() {
		return rightHead;
	}

	public void setRightHead(String rightHead) {
		this.rightHead = rightHead;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getBackMoney() {
		return backMoney;
	}

	public void setBackMoney(String backMoney) {
		this.backMoney = backMoney;
	}
	
	
}
