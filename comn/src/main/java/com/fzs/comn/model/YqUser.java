package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "YqUser")
public class YqUser extends Model {
	@Column(name = "tid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private String tid;
	
	@Column(name = "name")//姓名
	private String name;
	
	@Column(name = "time")//时间
	private long time;
	
	@Column(name = "phone")//电话号码
	private String phone;
	
	@Column(name= "type")//是否是广告商(1:是 , 0:不是)
	private String type;
	
	@Column(name= "newTask")//是否完成新手任务(1:完成 , 0:未完成)
	private int newTask;

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getNewTask() {
		return newTask;
	}

	public void setNewTask(int newTask) {
		this.newTask = newTask;
	}
	
}
