package com.fzs.comn.widget.pay.alipay;

/**
 * 支付结果回调
 * */
public interface CallBack{
    void success();
    void waiting(String errorCode, String errorMsg);
    void failure(String errorCode, String errorMsg);
}