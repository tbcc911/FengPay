package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Product")
public class Product extends Model {
	@Column(name = "pid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private String pid;// 商品ID
	
	@Column(name = "name")
	private String name;// 商品名称
	
	@Column(name = "des")
	private String des;// 商品描述
	
	@Column(name = "price")
	private String price;// 商品价格
	
	@Column(name = "sales")
	private String sales;// 商品销量
	
	@Column(name = "pic")
	private String pic;// 商品图片
	
	@Column(name = "use")
	private String use;//使用说明
	
	@Column(name = "info")
	private String info;//商品介绍
	
	@Column(name = "type")
	private String type;//商品类型
	
	@Column(name = "stock")
	private String stock;//商品库存
	
	@Column(name = "lat")
	private String lat;//纬度
	
	@Column(name = "lng")
	private String lng;//经度
	
	@Column(name = "distance")
	private String distance;//距离
	
	@Column(name = "detailedAddr")
	private String detailedAddr;//商店区域
	
	public String getStock() {
		return stock;//纬度
	}

	public void setStock(String stock) {
		this.stock = stock;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUse() {
		return use;
	}

	public void setUse(String use) {
		this.use = use;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getSales() {
		return sales;
	}

	public void setSales(String sales) {
		this.sales = sales;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getDetailedAddr() {
		return detailedAddr;
	}

	public void setDetailedAddr(String detailedAddr) {
		this.detailedAddr = detailedAddr;
	}
	
}
