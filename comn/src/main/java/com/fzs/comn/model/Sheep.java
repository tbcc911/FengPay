package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * 商城商品列表model
 * */
@Table(name = "Sheep")
public class Sheep extends Model{
	@Column(name = "lid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)// 本地数据ID
	private String lid;
	@Column(name = "nid")
	private String nid;
	@Column(name = "name")
	private String name;
	@Column(name = "desc")
	private String desc;
	@Column(name = "price1")
	private String price1;
	@Column(name = "price2")
	private String price2;
	@Column(name = "number")
	private String number;
	@Column(name = "path")
	private String path;
	@Column(name = "dBi")//D币
	private double dBi;
    @Column(name = "stock")//库存
    private int stock;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrice1() {
		return price1;
	}
	public void setPrice1(String price1) {
		this.price1 = price1;
	}
	public String getPrice2() {
		return price2;
	}
	public void setPrice2(String price2) {
		this.price2 = price2;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getLid() {
		return lid;
	}
	public void setLid(String lid) {
		this.lid = lid;
	}
	public String getNid() {
		return nid;
	}
	public void setNid(String nid) {
		this.nid = nid;
	}
	public double getdBi() {
		return dBi;
	}
	public void setdBi(double dBi) {
		this.dBi = dBi;
	}

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
