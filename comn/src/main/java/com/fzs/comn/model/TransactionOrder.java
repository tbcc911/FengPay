package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "TransactionOrder") 
public class TransactionOrder extends Model {
    
    
    @Column(name = "nid")
    private String nid;
    @Column(name = "time")
    private String time;
    @Column(name = "money")
    private String money;
    @Column(name = "type")
    private String type;
    @Column(name = "desc")
    private String desc;
    @Column(name = "integral")
    private String integral;
    @Column(name = "state")
    private String state;
    @Column(name = "auditTime") //审核时间
    private String auditTime;
    @Column(name = "createTime") //创建时间
    private String createTime;
    @Column(name = "statusName") //充值状态名称
    private String statusName;

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
