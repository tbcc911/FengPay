package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "MineIntegration") //积分明细
public class MineIntegration extends Model {
    
    @Column(name = "nid")
    private String nid;
    @Column(name = "createTime")
    private String createTime;
    @Column(name = "value")
    private String value;
    @Column(name = "tradeType") //交易类型
    private String tradeType;
    @Column(name = "tvtitle")
    private String tvtitle;
    @Column(name = "note")
    private String note;

    public String getNid() {
        return nid;
    }
    public MineIntegration setNid(String nid) {
        this.nid = nid;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }
    public MineIntegration setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getValue() {
        return value;
    }
    public MineIntegration setValue(String value) {
        this.value = value;
        return this;
    }

    public String getTradeType() {
        return tradeType;
    }
    public MineIntegration setTradeType(String tradeType) {
        this.tradeType = tradeType;
        return this;
    }

    public String getTvtitle() {
        return tvtitle;
    }
    public MineIntegration setTvtitle(String tvtitle) {
        this.tvtitle = tvtitle;
        return this;
    }

    public String getNote() {
        return note;
    }
    public MineIntegration setNote(String note) {
        this.note = note;
        return this;
    }
    
}
