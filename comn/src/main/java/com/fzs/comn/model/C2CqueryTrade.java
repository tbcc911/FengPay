package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "C2CqueryTrade") //TBCC交易列表
public class C2CqueryTrade extends Model {
    @Column(name = "nid")
    private String nid;
    @Column(name = "nickname")
    private String nickname;
    @Column(name = "avatarUrl")
    private String avatarUrl;
    @Column(name = "count")
    private String count;
    @Column(name = "price")
    private String price;
    @Column(name = "phone")
    private String phone;
    @Column(name = "poundage")
    private String poundage;
    @Column(name = "clinchDeal")
    private String clinchDeal;
    @Column(name = "tradingType")
    private String tradingType;

    public String getNid() {
        return nid;
    }
    public C2CqueryTrade setNid(String nid) {
        this.nid = nid;
        return this;
    }

    public String getNickname() {
        return nickname;
    }
    public C2CqueryTrade setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
    public C2CqueryTrade setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    public String getCount() {
        return count;
    }
    public C2CqueryTrade setCount(String count) {
        this.count = count;
        return this;
    }

    public String getPrice() {
        return price;
    }
    public C2CqueryTrade setPrice(String price) {
        this.price = price;
        return this;
    }

    public String getPhone() {
        return phone;
    }
    public C2CqueryTrade setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getPoundage() {
        return poundage;
    }
    public C2CqueryTrade setPoundage(String poundage) {
        this.poundage = poundage;
        return this;
    }

    public String getClinchDeal() {
        return clinchDeal;
    }
    public C2CqueryTrade setClinchDeal(String clinchDeal) {
        this.clinchDeal = clinchDeal;
        return this;
    }

    public String getTradingType() {
        return tradingType;
    }
    public C2CqueryTrade setTradingType(String tradingType) {
        this.tradingType = tradingType;
        return this;
    }

   
}
