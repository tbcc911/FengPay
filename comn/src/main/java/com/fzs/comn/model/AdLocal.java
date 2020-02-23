package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * 收分记录
 * */
@Table(name = "AdLocal")
public class AdLocal extends Model {
	@Column
	private String userid;// 用户ID
	@Column(name = "adid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private String adid;// 广告ID
	@Column
	private String operation;// 操作
	@Column
	private String addtime;// 添加时间
	@Column
	private boolean done;// 是否已收分


	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getAdid() {
		return adid;
	}

	public void setAdid(String adid) {
		this.adid = adid;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	


}
