package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "FloorBills")
public class FloorBills extends Model {
	@Column(name = "fid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private String fid;// 账单ID
	@Column(name = "date")
	private Long date;// 账单日期
	@Column(name = "money")
	private String money;// 账单金额
	@Column(name = "title")
	private String title;// 账单标题
	@Column(name = "pic")
	private String pic;// 商品图片
	@Column(name = "sqlwhere")
	private String sqlwhere;// 条件
	
	
	public String getFid() {
		return fid;
	}
	public void setFid(String fid) {
		this.fid = fid;
	}
	public Long getDate() {
		return date;
	}
	public void setDate(Long date) {
		this.date = date;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getSqlwhere() {
		return sqlwhere;
	}
	public void setSqlwhere(String sqlwhere) {
		this.sqlwhere = sqlwhere;
	}
    
}
