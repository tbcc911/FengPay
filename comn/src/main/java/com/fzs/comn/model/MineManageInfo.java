package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "MineManageInfo") //钱包明细
public class MineManageInfo extends Model {
    
    
    @Column(name = "nid")
    private String nid;
    @Column(name = "sumPrice")
    private String sumPrice;
    @Column(name = "ysPrice")
    private String ysPrice;
    @Column(name = "addPrice")
    private String addPrice;
    @Column(name = "money")
    private String money;
    @Column(name = "time")
    private String time;
    @Column(name = "lv")
    private String lv;
    @Column(name = "status")
    private String status;

    public String getNid() {
        return nid;
    }
    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getSumPrice() {
        return sumPrice;
    }
    public void setSumPrice(String sumPrice) {
        this.sumPrice = sumPrice;
    }

    public String getYsPrice() {
        return ysPrice;
    }
    public void setYsPrice(String ysPrice) {
        this.ysPrice = ysPrice;
    }

    public String getAddPrice() {
        return addPrice;
    }
    public void setAddPrice(String addPrice) {
        this.addPrice = addPrice;
    }

    public String getMoney() {
        return money;
    }
    public void setMoney(String money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public String getLv() {
        return lv;
    }
    public void setLv(String lv) {
        this.lv = lv;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    
}
