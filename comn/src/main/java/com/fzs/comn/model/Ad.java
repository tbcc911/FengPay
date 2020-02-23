package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;
import java.util.Date;

@Table(name = "Ad")
public class Ad extends Model implements Serializable{
	@Column(name = "adid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private String adid;// 广告ID
	@Column
	private String url;// 图片URL
	@Column
	private String leftType;//左滑类型
	@Column
	private String leftMoney;//左滑金额
	@Column
	private String leftVal;//左滑值
	@Column
	private String sdUrl;// SD卡本地图片路径
    @Column
    private String sdUrlBG;// SD卡本地图片路径
	@Column
	private String isDown;// 是否下载完成
	@Column
	private Date lastShowTime;// 最后一次显示时间
	@Column
	private boolean vip;//是否VIP广告
	@Column
	private String des;//广告描述

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public boolean isVip() {
		return vip;
	}

	public void setVip(boolean vip) {
		this.vip = vip;
	}

	public String getAdid() {
		return adid;
	}

	public String getLeftMoney() {
		return leftMoney;
	}

	public void setLeftMoney(String leftMoney) {
		this.leftMoney = leftMoney;
	}

	public void setAdid(String adid) {
		this.adid = adid;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLeftType() {
		return leftType;
	}

	public void setLeftType(String leftType) {
		this.leftType = leftType;
	}

	public String getLeftVal() {
		return leftVal;
	}

	public void setLeftVal(String leftVal) {
		this.leftVal = leftVal;
	}

	public String getSdUrl() {
		return sdUrl;
	}

	public void setSdUrl(String sdUrl) {
		this.sdUrl = sdUrl;
	}

	public String getIsDown() {
		return isDown;
	}

	public void setIsDown(String isDown) {
		this.isDown = isDown;
	}

	public Date getLastShowTime() {
		return lastShowTime;
	}

	public void setLastShowTime(Date lastShowTime) {
		this.lastShowTime = lastShowTime;
	}

    public String getSdUrlBG() {
        return sdUrlBG;
    }

    public void setSdUrlBG(String sdUrlBG) {
        this.sdUrlBG = sdUrlBG;
    }
}
