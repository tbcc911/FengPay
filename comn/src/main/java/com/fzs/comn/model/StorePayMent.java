package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "StorePayMent")
public class StorePayMent extends Model{
	@Column(name = "bankAccount")
	private String bankAccount;// 线下支付-收款银行卡账号
	@Column(name = "bankCardNo")
	private String bankCardNo;// 线下支付-收款银行卡号
	@Column(name = "bankName")
	private String bankName;// 线下支付-收款银行名称
	@Column(name = "icon")
	private String icon;// 支付方式图标
    @Column(name = "name")
    private String name;// 支付名称
    @Column(name = "payType")
    private String payType;// 支付方式: 0->upay; 1->tb支付; 2->线下支付
	public String getBankAccount() {
		return bankAccount;
	}
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
    public String getBankCardNo() {
        return bankCardNo;
    }
    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPayType() {
        return payType;
    }
    public void setPayType(String payType) {
        this.payType = payType;
    }
}
