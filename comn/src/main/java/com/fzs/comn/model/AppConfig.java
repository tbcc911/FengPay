package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "AppConfig")
public class AppConfig extends Model{

    @Column(name = "sharePageUrl")
    private String sharePageUrl;// 分享页面链接地址
    @Column(name = "goldStandard")
    private String goldStandard;// 金本位的价值(例: 1个tb等于0.1人民币)
    @Column(name = "tbReleasetbcc")
    private String tbReleasetbcc;// tb每日释放tbcc的百分比
    @Column(name = "tbccExchangeUsdtLowerLimit")
    private String tbccExchangeUsdtLowerLimit;// tbcc兑换USDT的最小数量(值为0时，不限制)
    @Column(name = "tbccExchangeUsdtMultiple")
    private String tbccExchangeUsdtMultiple;// tbcc兑换USDT的整数倍数(值为0时，不限制)
    @Column(name = "transferLowerLimit")
    private String transferLowerLimit;// 允许转账的最小数量
    @Column(name = "transferMultiple")
    private String transferMultiple;// 允许转账的整数倍数
    @Column(name = "usdtExchangeupayLowerLimit")
    private String usdtExchangeupayLowerLimit;// USDT兑换upay的最小数量(值为0时，不限制)
    @Column(name = "usdtExchangeupayMultiple")
    private String usdtExchangeupayMultiple;// USDT兑换upay的整数倍数(值为0时，不限制)
    @Column(name = "usdtExchangeupayRate")
    private String usdtExchangeupayRate;// USDT兑换upay的比例
    @Column(name = "withdrawLowerLimit")
    private String withdrawLowerLimit;// 允许提现的最小数量
    @Column(name = "withdrawMultiple")
    private String withdrawMultiple;// 允许提现的整数倍数
    @Column(name = "withdrawPoundage")
    private String withdrawPoundage;// 提币手续费的百分比
    @Column(name = "inviteReward")
    private String inviteReward;// 邀请好友

    @Column(name = "companyWebSite")
    private String companyWebSite;// 公司地址
    @Column(name = "customerTelephone")
    private String customerTelephone;// 客服电话

    @Column(name = "EVEpayExchangeTbccLowerLimit")
    private String EVEpayExchangeTbccLowerLimit;// EVEpay兑换TBCC的最小数量(值为0时，不限制)
    @Column(name = "EVEpayExchangeTbccMultiple")
    private String EVEpayExchangeTbccMultiple;// EVEpay兑换TBCC的整数倍数(值为0时，不限制)
    @Column(name = "EVEpayExchangeTbccRate")
    private String EVEpayExchangeTbccRate;// EVEpay兑换TBCC的比例
    @Column(name = "usdtExchangeTbccLowerLimit")
    private String usdtExchangeTbccLowerLimit;// USDT兑换TBCC的最小数量(值为0时，不限制)
    @Column(name = "usdtExchangeTbccMultiple")
    private String usdtExchangeTbccMultiple;// USDT兑换TBCC的整数倍数(值为0时，不限制)
    @Column(name = "usdtExchangeTbccRate")
    private String usdtExchangeTbccRate;// USDT兑换TBCC的比例
    @Column(name = "digitalCurrencyTradingPoundage")
    private String digitalCurrencyTradingPoundage;// C2C手续费
    @Column(name = "currencyTradeMinPrice")
    private String currencyTradeMinPrice;// 货币交易最高单价
    @Column(name = "currencyTradeMaxPrice")
    private String currencyTradeMaxPrice;// 货币交易最低单价

    public String getSharePageUrl() {
        return sharePageUrl;
    }

    public void setSharePageUrl(String sharePageUrl) {
        this.sharePageUrl = sharePageUrl;
    }

    public String getGoldStandard() {
        return goldStandard;
    }

    public void setGoldStandard(String goldStandard) {
        this.goldStandard = goldStandard;
    }

    public String getTbReleasetbcc() {
        return tbReleasetbcc;
    }

    public void setTbReleasetbcc(String tbReleasetbcc) {
        this.tbReleasetbcc = tbReleasetbcc;
    }

    public String getTbccExchangeUsdtLowerLimit() {
        return tbccExchangeUsdtLowerLimit;
    }

    public void setTbccExchangeUsdtLowerLimit(String tbccExchangeUsdtLowerLimit) {
        this.tbccExchangeUsdtLowerLimit = tbccExchangeUsdtLowerLimit;
    }

    public String getTbccExchangeUsdtMultiple() {
        return tbccExchangeUsdtMultiple;
    }

    public void setTbccExchangeUsdtMultiple(String tbccExchangeUsdtMultiple) {
        this.tbccExchangeUsdtMultiple = tbccExchangeUsdtMultiple;
    }

    public String getTransferLowerLimit() {
        return transferLowerLimit;
    }

    public void setTransferLowerLimit(String transferLowerLimit) {
        this.transferLowerLimit = transferLowerLimit;
    }

    public String getTransferMultiple() {
        return transferMultiple;
    }

    public void setTransferMultiple(String transferMultiple) {
        this.transferMultiple = transferMultiple;
    }

    public String getUsdtExchangeupayLowerLimit() {
        return usdtExchangeupayLowerLimit;
    }

    public void setUsdtExchangeupayLowerLimit(String usdtExchangeupayLowerLimit) {
        this.usdtExchangeupayLowerLimit = usdtExchangeupayLowerLimit;
    }

    public String getUsdtExchangeupayMultiple() {
        return usdtExchangeupayMultiple;
    }

    public void setUsdtExchangeupayMultiple(String usdtExchangeupayMultiple) {
        this.usdtExchangeupayMultiple = usdtExchangeupayMultiple;
    }

    public String getUsdtExchangeupayRate() {
        return usdtExchangeupayRate;
    }

    public void setUsdtExchangeupayRate(String usdtExchangeupayRate) {
        this.usdtExchangeupayRate = usdtExchangeupayRate;
    }

    public String getWithdrawLowerLimit() {
        return withdrawLowerLimit;
    }

    public void setWithdrawLowerLimit(String withdrawLowerLimit) {
        this.withdrawLowerLimit = withdrawLowerLimit;
    }

    public String getWithdrawMultiple() {
        return withdrawMultiple;
    }

    public void setWithdrawMultiple(String withdrawMultiple) {
        this.withdrawMultiple = withdrawMultiple;
    }

    public String getWithdrawPoundage() {
        return withdrawPoundage;
    }

    public void setWithdrawPoundage(String withdrawPoundage) {
        this.withdrawPoundage = withdrawPoundage;
    }

    public String getInviteReward() {
        return inviteReward;
    }

    public void setInviteReward(String inviteReward) {
        this.inviteReward = inviteReward;
    }

    public String getCompanyWebSite() {
        return companyWebSite;
    }

    public void setCompanyWebSite(String companyWebSite) {
        this.companyWebSite = companyWebSite;
    }

    public String getCustomerTelephone() {
        return customerTelephone;
    }

    public void setCustomerTelephone(String customerTelephone) {
        this.customerTelephone = customerTelephone;
    }

    public String getEVEpayExchangeTbccLowerLimit() {
        return EVEpayExchangeTbccLowerLimit;
    }

    public void setEVEpayExchangeTbccLowerLimit(String EVEpayExchangeTbccLowerLimit) {
        this.EVEpayExchangeTbccLowerLimit = EVEpayExchangeTbccLowerLimit;
    }

    public String getEVEpayExchangeTbccMultiple() {
        return EVEpayExchangeTbccMultiple;
    }

    public void setEVEpayExchangeTbccMultiple(String EVEpayExchangeTbccMultiple) {
        this.EVEpayExchangeTbccMultiple = EVEpayExchangeTbccMultiple;
    }

    public String getEVEpayExchangeTbccRate() {
        return EVEpayExchangeTbccRate;
    }

    public void setEVEpayExchangeTbccRate(String EVEpayExchangeTbccRate) {
        this.EVEpayExchangeTbccRate = EVEpayExchangeTbccRate;
    }

    public String getUsdtExchangeTbccLowerLimit() {
        return usdtExchangeTbccLowerLimit;
    }

    public void setUsdtExchangeTbccLowerLimit(String usdtExchangeTbccLowerLimit) {
        this.usdtExchangeTbccLowerLimit = usdtExchangeTbccLowerLimit;
    }

    public String getUsdtExchangeTbccMultiple() {
        return usdtExchangeTbccMultiple;
    }

    public void setUsdtExchangeTbccMultiple(String usdtExchangeTbccMultiple) {
        this.usdtExchangeTbccMultiple = usdtExchangeTbccMultiple;
    }

    public String getUsdtExchangeTbccRate() {
        return usdtExchangeTbccRate;
    }

    public void setUsdtExchangeTbccRate(String usdtExchangeTbccRate) {
        this.usdtExchangeTbccRate = usdtExchangeTbccRate;
    }

    public String getDigitalCurrencyTradingPoundage() {
        return digitalCurrencyTradingPoundage;
    }

    public void setDigitalCurrencyTradingPoundage(String digitalCurrencyTradingPoundage) {
        this.digitalCurrencyTradingPoundage = digitalCurrencyTradingPoundage;
    }

    public String getCurrencyTradeMinPrice() {
        return currencyTradeMinPrice;
    }

    public void setCurrencyTradeMinPrice(String currencyTradeMinPrice) {
        this.currencyTradeMinPrice = currencyTradeMinPrice;
    }

    public String getCurrencyTradeMaxPrice() {
        return currencyTradeMaxPrice;
    }

    public void setCurrencyTradeMaxPrice(String currencyTradeMaxPrice) {
        this.currencyTradeMaxPrice = currencyTradeMaxPrice;
    }
}
