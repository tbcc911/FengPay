package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Message")
public class Message extends Model{
	@Column(name = "mid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private String mid;// 消息ID
	@Column(name = "title")
	private String title;// 名称
	@Column(name = "addTime")
	private String addTime;// 添加时间
	@Column(name = "showTime")
	private String showTime;// 显示时间
	@Column(name = "content")
	private String content;// 内容
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public String getShowTime() {
		return showTime;
	}
	public void setShowTime(String showTime) {
		this.showTime = showTime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
