package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "OrderShop")
public class OrderShop extends Model {
	@Column(name = "orderid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private String orderid;// 兑换ID
	@Column(name = "image")
	private String image;// 图片URL
	@Column(name = "shopid")
	private String shopid;// 商品ID
	@Column(name = "clientid")
	private String clientid;// 门店ID
	@Column(name = "name")
	private String name;// 商品名称
	@Column(name = "state")
	private String state;// 商品状态
	@Column(name = "time")
	private String time;// 兑换时间
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getShopid() {
		return shopid;
	}
	public void setShopid(String shopid) {
		this.shopid = shopid;
	}
	public String getClientid() {
		return clientid;
	}
	public void setClientid(String clientid) {
		this.clientid = clientid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
}
