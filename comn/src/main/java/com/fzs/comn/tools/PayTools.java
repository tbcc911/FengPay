package com.fzs.comn.tools;

import android.app.Activity;

import com.fzs.comn.widget.pay.alipay.Alipay;
import com.fzs.comn.widget.pay.alipay.CallBack;
import com.fzs.comn.widget.pay.wx.PayInfoBean;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 支付工具类
 * @date 2019/10/9
 */
public class PayTools {


    /**
     * 发起支付宝
     * */
    public static void launchAlipay(Activity activity, String alipayAppPayRes, CallBack callBack){
        new Alipay(activity).pay(alipayAppPayRes,callBack);
    }

    /**
     * 发起微信支付
     * */
    public static void launchWXpay(Activity activity, PayInfoBean model){
        IWXAPI api= WXAPIFactory.createWXAPI(activity, model.getAppId());
        PayReq req = new PayReq();
        req.appId			= model.getAppId();  // appId
        req.partnerId		= model.getPartnerId();//微信支付分配的商户号
        req.prepayId        = model.getPrepayId();//微信返回的支付交易会话ID
        req.nonceStr        = model.getNonceStr();//随机字符串
        req.timeStamp		= model.getTimestamp();//时间戳
        req.packageValue	= model.getPayPackage();
        req.sign			= model.getSign();
        req.extData			= "app data"; // optional
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        api.sendReq(req);
    }
    
    
}
