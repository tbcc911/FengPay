package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * 钻石会员
 * */
@Table(name = "VipIndex", id = "_id")
public class VipIndex extends Model{
	@Column(name = "head")//头像地址
	private String head;
	@Column(name = "distance")//距离
	private String distance;
    @Column(name = "emapName")//城市
    private String emapName;
    @Column(name = "nickName")//人名
    private String nickName;
    @Column(name = "itemval")//赚的钱
    private String itemval;

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getEmapName() {
        return emapName;
    }

    public void setEmapName(String emapName) {
        this.emapName = emapName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getItemval() {
        return itemval;
    }

    public void setItemval(String itemval) {
        this.itemval = itemval;
    }
}
