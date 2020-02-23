package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * 系统缓存字典表
 * */
@Table(name = "SysZd")
public class SysZd extends Model {
	@Column
	private String name;// 字典名称
	@Column
	private String value;// 字典值
	@Column
	private String parentID;// 字典父节点ID
	@Column
	private boolean isSelected;// 是否默认选中
	
	
	
	public SysZd() {
		super();
	}

	public SysZd(String name, String value, String parentID, boolean isSelected) {
		super();
		this.name = name;
		this.value = value;
		this.parentID = parentID;
		this.isSelected = isSelected;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getParentID() {
		return parentID;
	}

	public void setParentID(String parentID) {
		this.parentID = parentID;
	}

	public boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

}
