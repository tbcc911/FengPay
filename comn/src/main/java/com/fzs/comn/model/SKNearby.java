package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * 超市 | 首页附近 | 店铺
 * */
@Table(name = "SKNearby", id = "_id")
public class SKNearby extends Model{
	@Column(name = "sid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)// 本地数据ID
	private String sid;
	@Column(name = "storeId")//店铺ID后台数据库
	private String storeId;
	@Column(name = "path")//商品图片
	private String path;
	@Column(name = "storeName")//商品名称
	private String storeName;
	@Column(name = "saleNum")//销售量
	private long saleNum;
	@Column(name = "storeEvaluate")//店铺评分
	private double storeEvaluate;
    @Column(name = "areaName")//店铺所在区域
    private String areaName;
    @Column(name = "distance")//店铺离我的距离
    private long distance;
    @Column(name = "scale")//线下买单送积分比例
    private int scale;
    

    public String getSid() {
        return sid;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getPath() {
        return path;
    }

    public String getStoreName() {
        return storeName;
    }

    public long getSaleNum() {
        return saleNum;
    }

    public double getStoreEvaluate() {
        return storeEvaluate;
    }

    public String getAreaName() {
        return areaName;
    }

    public long getDistance() {
        return distance;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setSaleNum(long saleNum) {
        this.saleNum = saleNum;
    }

    public void setStoreEvaluate(double storeEvaluate) {
        this.storeEvaluate = storeEvaluate;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }
}
