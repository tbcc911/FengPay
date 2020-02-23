package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Banner")
public class Banner extends Model{
	@Column(name = "banner_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private String banner_id;
	@Column
	private String name;
	@Column
	private String url;
	@Column
	private String open_way;
	@Column
	private String open_content;
	public String getBanner_id() {
		return banner_id;
	}
	public void setBanner_id(String banner_id) {
		this.banner_id = banner_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getOpen_way() {
		return open_way;
	}
	public void setOpen_way(String open_way) {
		this.open_way = open_way;
	}
	public String getOpen_content() {
		return open_content;
	}
	public void setOpen_content(String open_content) {
		this.open_content = open_content;
	}
	
}
