package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * 超市 | 商品
 * */
@Table(name = "SKGoods", id = "_id")
public class SKGoods extends Model{
	@Column(name = "sid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)// 本地数据ID
	private String sid;
    @Column(name = "storesId")//店铺ID
    private String storesId;
	@Column(name = "goodsId")//商品ID
	private String goodsId;
	@Column(name = "name")//商品名称
	private String name;
	@Column(name = "price")//现价格
	private double price;
    @Column(name = "storePrice")//门市价格
    private double storePrice;
	@Column(name = "number")
	private long number;
    @Column(name = "song")
    private double song;
    @Column(name = "path")
    private String path;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getStorePrice() {
        return storePrice;
    }

    public void setStorePrice(double storePrice) {
        this.storePrice = storePrice;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public double getSong() {
        return song;
    }

    public void setSong(double song) {
        this.song = song;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getStoresId() {
        return storesId;
    }

    public void setStoresId(String storesId) {
        this.storesId = storesId;
    }
}
