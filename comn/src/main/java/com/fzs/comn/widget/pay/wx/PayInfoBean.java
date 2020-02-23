package com.fzs.comn.widget.pay.wx;

/***
 * 支付相关信息
 */
public class PayInfoBean {
    private String alipayResponse;
    private String appId;
    private String nonceStr;
    private String partnerId;
    private String payAmount;
    private String payPackage;
    private String payType;
    private String prepayId;
    private String sign;
    private String timestamp;

    public String getAlipayResponse() {
        return alipayResponse;
    }

    public void setAlipayResponse(String alipayResponse) {
        this.alipayResponse = alipayResponse;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getPayPackage() {
        return payPackage;
    }

    public void setPayPackage(String payPackage) {
        this.payPackage = payPackage;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
