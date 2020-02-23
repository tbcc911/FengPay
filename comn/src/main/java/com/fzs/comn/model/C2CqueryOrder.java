package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "C2CqueryOrder") //买卖交易记录
public class C2CqueryOrder extends Model {
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
    @Column(name = "createTime")
    private String createTime;
    @Column(name = "poundage")
    private String poundage;
    @Column(name = "tradingStatus")
    private String tradingStatus;

    public String getNid() {
        return nid;
    }
    public C2CqueryOrder setNid(String nid) {
        this.nid = nid;
        return this;
    }

    public String getNickname() {
        return nickname;
    }
    public C2CqueryOrder setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
    public C2CqueryOrder setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    public String getCount() {
        return count;
    }
    public C2CqueryOrder setCount(String count) {
        this.count = count;
        return this;
    }

    public String getPrice() {
        return price;
    }
    public C2CqueryOrder setPrice(String price) {
        this.price = price;
        return this;
    }

    public String getPhone() {
        return phone;
    }
    public C2CqueryOrder setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }
    public C2CqueryOrder setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getPoundage() {
        return poundage;
    }
    public C2CqueryOrder setPoundage(String poundage) {
        this.poundage = poundage;
        return this;
    }

    public String getTradingStatus() {
        return tradingStatus;
    }
    public C2CqueryOrder setTradingStatus(String tradingStatus) {
        this.tradingStatus = tradingStatus;
        return this;
    }
   
}
