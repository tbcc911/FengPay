package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * 购物车店铺model
 * */
@Table(name = "CartStores", id = "_id")
public class CartStores extends Model{
	@Column(name = "storeID")//店铺ID
	private String storeID;
	@Column(name = "storeType")//店铺类型
	private String storeType;
	@Column(name = "storeName")//店铺名称
	private String storeName;
	@Column(name = "scId")//购物车店铺ID
	private String scId;
	
	@Column(name = "isAll")//店铺商品是否全选 0:未全选  1:已全选
	private int isAll;
	@Column(name = "addTime")//新增时间
	private long addTime;
	@Column(name = "userId")//用户ID
	private String userId;
	
	public String getStoreID() {
		return storeID;
	}
	public void setStoreID(String storeID) {
		this.storeID = storeID;
	}
	public String getStoreType() {
		return storeType;
	}
	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public int getIsAll() {
		return isAll;
	}
	public void setIsAll(int isAll) {
		this.isAll = isAll;
	}
	public long getAddTime() {
		return addTime;
	}
	public void setAddTime(long addTime) {
		this.addTime = addTime;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getScId() {
		return scId;
	}
	public void setScId(String scId) {
		this.scId = scId;
	}
	
}
