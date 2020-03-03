package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "TransactionIncome") 
public class TransactionIncome extends Model {
    @Column(name = "nid")
    private String nid;
    @Column(name = "afterValue")
    private String afterValue;
    @Column(name = "beforeValue")
    private String beforeValue;
    @Column(name = "createTime")
    private String createTime;
    @Column(name = "incometitle")
    private String incometitle;
    @Column(name = "value")
    private String value;
    
    public String getNid() {
        return nid;
    }
    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getAfterValue() {
        return afterValue;
    }
    public void setAfterValue(String afterValue) {
        this.afterValue = afterValue;
    }

    public String getBeforeValue() {
        return beforeValue;
    }
    public void setBeforeValue(String beforeValue) {
        this.beforeValue = beforeValue;
    }

    public String getCreateTime() {
        return createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getIncometitle() {
        return incometitle;
    }
    public void setIncometitle(String incometitle) {
        this.incometitle = incometitle;
    }

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    
}
