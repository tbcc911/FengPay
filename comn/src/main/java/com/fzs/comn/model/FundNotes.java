package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "FundNotes") //基金
public class FundNotes extends Model {
    
    
    @Column(name = "amount") //当前金额
    private String amount;
    @Column(name = "totalAmount") //总金额
    private String totalAmount;
    @Column(name = "phone") //手机号
    private String phone;
    @Column(name = "value") //存入金额
    private String value;
    @Column(name = "createTime") //存入时间
    private String createTime;
    

    public String getAmount() {
        return amount;
    }

    public FundNotes setAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public FundNotes setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public FundNotes setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getValue() {
        return value;
    }

    public FundNotes setValue(String value) {
        this.value = value;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public FundNotes setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }
    
}
