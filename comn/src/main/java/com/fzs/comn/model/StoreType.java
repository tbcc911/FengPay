package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * 商城商品分类model
 * */
@Table(name = "StoreType")
public class StoreType extends Model{
	@Column(name = "sid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)// 本地数据ID
	private String sid;
	@Column(name = "goodsId")//商品ID后台数据库
	private String goodsId;
	@Column(name = "goodsPrice")//商品价格
	private double goodsPrice;
	@Column(name = "storePrice")//店铺价格
	private double storePrice;
	@Column(name = "path")//商品图片
	private String path;
	@Column(name = "goodsName")//商品名称
	private String goodsName;
	@Column(name = "goodsSalenum")//兑换数量
	private long goodsSalenum;
	
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public double getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public double getStorePrice() {
		return storePrice;
	}
	public void setStorePrice(double storePrice) {
		this.storePrice = storePrice;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public long getGoodsSalenum() {
		return goodsSalenum;
	}
	public void setGoodsSalenum(long goodsSalenum) {
		this.goodsSalenum = goodsSalenum;
	}
	
}
