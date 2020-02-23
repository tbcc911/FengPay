package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "MineEquity")
public class MineEquity extends Model{
	@Column(name = "lid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private String lid;
    @Column(name = "nid")
    private String nid;

    @Column(name = "amount")
    private String amount;//购买金额
    
    @Column(name = "certificateNum")
    private String certificateNum;//证书编号

    @Column(name = "certificateUrl")
    private String certificateUrl;//权益证书地址

    @Column(name = "createTime")
    private String createTime;//生成时间

    @Column(name = "income")
    private String income;//收益

    @Column(name = "incomeStatus")
    private String incomeStatus;//收益状态: 0->收益中; 1->已结束

    @Column(name = "month")
    private String month;//认养月份

    @Column(name = "name")
    private String name;//姓名

    @Column(name = "overTime")
    private String overTime;//结束时间

    @Column(name = "tbccReward")
    private String tbccReward;//tbcc奖励

    @Column(name = "quantity")
    private String quantity;//购买数量


    public String getLid() {
        return lid;
    }

    public void setLid(String lid) {
        this.lid = lid;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCertificateNum() {
        return certificateNum;
    }

    public void setCertificateNum(String certificateNum) {
        this.certificateNum = certificateNum;
    }

    public String getCertificateUrl() {
        return certificateUrl;
    }

    public void setCertificateUrl(String certificateUrl) {
        this.certificateUrl = certificateUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getIncomeStatus() {
        return incomeStatus;
    }

    public void setIncomeStatus(String incomeStatus) {
        this.incomeStatus = incomeStatus;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOverTime() {
        return overTime;
    }

    public void setOverTime(String overTime) {
        this.overTime = overTime;
    }

    public String gettbccReward() {
        return tbccReward;
    }

    public void settbccReward(String tbccReward) {
        this.tbccReward = tbccReward;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
