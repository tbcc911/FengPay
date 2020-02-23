package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "MapUtil")
public class MapUtil extends Model {
	@Column(name = "item", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private String item;
	@Column
	private String val;
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}
	
}
