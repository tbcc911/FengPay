package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "SingninGoods")
public class SingninGoods extends Model {
    
	@Column(name = "nid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private String nid;// 签到订单ID
	@Column(name = "signStatus")
	private String signStatus;// 签到 1已签到，0未签到
	@Column(name = "signDay")
	private String signDay;// 还需要签到天
	@Column(name = "signedDay")
	private String signedDay;// 已经签到天数
	@Column(name = "path")
	private String path;// 图片路径
	@Column(name = "goodsName")
	private String goodsName;// 商品名称
	@Column(name = "goodsAmount")
	private String goodsAmount;// 价格
	@Column(name = "signedMoney")
	private String signedMoney;//签到获得金额
	@Column(name = "share")
	private int share;//是否可分享 1可以，0不可以


    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getSignStatus() {
        return signStatus;
    }

    public void setSignStatus(String signStatus) {
        this.signStatus = signStatus;
    }

    public String getSignDay() {
        return signDay;
    }

    public void setSignDay(String signDay) {
        this.signDay = signDay;
    }

    public String getSignedDay() {
        return signedDay;
    }

    public void setSignedDay(String signedDay) {
        this.signedDay = signedDay;
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

    public String getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(String goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    public String getSignedMoney() {
        return signedMoney;
    }

    public void setSignedMoney(String signedMoney) {
        this.signedMoney = signedMoney;
    }

    public int getShare() {
        return share;
    }

    public void setShare(int share) {
        this.share = share;
    }
}
