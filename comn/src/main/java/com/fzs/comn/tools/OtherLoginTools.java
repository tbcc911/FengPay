package com.fzs.comn.tools;

import android.app.Activity;
import android.provider.Telephony;
import android.text.TextUtils;

import com.alipay.sdk.app.AuthTask;
import com.fzs.comn.widget.pay.alipay.Alipay;
import com.fzs.comn.widget.pay.alipay.AuthResult;
import com.fzs.comn.widget.pay.alipay.OrderInfoUtil2_0;
import com.fzs.comn.widget.pay.wx.PayInfoBean;
import com.hzh.frame.callback.CallBack;
import com.hzh.frame.widget.toast.BaseToast;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.IOException;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 支付工具类
 * @date 2019/10/9
 */
public class OtherLoginTools {

    /**
     * 用于支付宝支付业务的入参 app_id。
     */
    public static final String APPID = "2019092567822201";

    /**
     * 用于支付宝账户登录授权业务的入参 pid。 合作伙伴身份PID
     */
    public static final String PID = "2088631392290234";

    /**
     * 用于支付宝账户登录授权业务的入参 target_id。可自定义，保证唯一性即可
     */
    public static final String TARGET_ID = "12576466884";

    /**
     *  pkcs8 格式的商户私钥。
     *
     * 	如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个，如果两个都设置了，本 Demo 将优先
     * 	使用 RSA2_PRIVATE。RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议商户使用
     * 	RSA2_PRIVATE。
     *
     * 	建议使用支付宝提供的公私钥生成工具生成和获取 RSA2_PRIVATE。
     * 	工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
     */
    public static final String RSA2_PRIVATE = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCcPDPHIIHQy5llJ6BY6JShHObxZQhOFMfyZWHeg3S7/5/ELqnkSP8jUvo17jZ6ZM4+qUm0Mld0iusL4ki+x8eyZXmTKJJThtXuYSJooVCENKkSEL/jntF7mW64qKT2odTNg/5q7CMlNsHP3ZtRfdaGK8vj9j7XT4tczj8zKf8owq2ch5VDyGCkv55TSxT43DDLH2dFUClilQIYcpTQ1mS5KE0ao2f3Or/M6VZgJVNaMdYZyS0fxbI7WfD654VQ4l5c12WuEcgqipml/SNnHTSx4iFXPDTtzzdojUvQ3ZkMBnceVZb67hnXF/WF6lEJ+t9MBPjSua3+WFck9OEEU/CJAgMBAAECggEAB53bvFp2WbKDUO4koBD+pfgdvij5xay3rraIlet8tmmY5VyNBccVQSibcCOj3kWp1F42bKWDo/fdy5+Fgl+E+cp+qdDxiUbqiQuNPOKYZUmX8HyGjldAyTDKuA/osuli0X4c2iOW9wp7WmHoX4mt+q6J6xwnJiIHnkejxx/GA7uecFqEEBM+656mLp37Y6k8Z/X/aGwdjkJTLSPsQfS5X+kFMtx1ZcmGNp8gwnu3tNkOzkgW7YjG1cVTurPBrCoCA7rwFuWWLFOmYhw+1Kg09XfWXq0F3/EzWDXpgelYSxVoebzrA4T1ZrEG77/3rGFvlE1a8n4iRZJ0WyqRpwwJsQKBgQDLq0k96prcn4gRWG3x5+c+0P2dbHJg2vuP4TWVTJ/STUAdSokbtmJ1pVxlVVhKcIROM3U9vSPF1xa/nRQ6I7zJMQKYea33QwHC+GqD6oOTYbhMyzAhRu49pHMxEqvZ+3FIEHdQEZftDAfK2Is8lgByUze8rXxMVubvrQ7ZT48PvQKBgQDEYNyOG0viLaJ0HqqoV9FIvKTDe8yO48DAs4EnG6a4gqGB82KhU5m2O1Bqn44fD+qdL5IYcISRZ9ro7lURAwbM4RQI6UIoaHhbJOiuo2CxH7Q4LoJ1KsJDxM1yfoGx49ESmRgltGBG8SseWxcsdnQPFg15sZzUVjfMxuhyyL66vQKBgEg3dgL0WvD7yD2OQ33RItfNNwBI4c2iGC0OR/emcc0aAy/fq/odk4/vkHGF74aFmwQ3jszTcgnGmJRc2D09SsV0EV2LV7ojEk7V0r7vurPZpIXmTiyef/9vfkDaRAFI/QP2grfrZRlyeJjea5CSB5qCE3NdeIOBFOXLUUaQxUNpAoGAB/xMW7b+cfcbGQ+6LthLWWLabFyXZ+QZDXz/LoDwWaZuPOORyi+lG4Tz8zIDzH2QM84gacABlAweGcS/Ts7JswEgGHCr0QrWghZRI/De2LUKVhKGGY+gkLLrC9HbIkm6UcBJ/BaGp7vc3gIkQhmbBJYH/D/ducC7yib4gtpo7x0CgYEAsQRv7FWUY5a8EJJdOVl0C9vKMwsBpkuP+/ZU2w3g6ESPVCagwJ+DZfhlYv60nYLnMdvRj5Jz4/52MuDtycfHF7ihi2RQ1U0ElR0sZxzTk0YhUfSa02S2L1oecOenW0cIv3On4y+qCHlRB7ZgbaPyDdwZCobqT/95EQwXcdwebbY=";
    public static final String RSA_PRIVATE = "";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    /**
     * 发起支付宝登录
     * */
    public static void launchAlipayLogin(Activity activity, CallBack callBack){
        /*
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * authInfo 的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> authInfoMap = OrderInfoUtil2_0.buildAuthInfoMap(PID, APPID, TARGET_ID, rsa2);
        String info = OrderInfoUtil2_0.buildOrderParam(authInfoMap);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(authInfoMap, privateKey, rsa2);
        String authInfo = info + "&" + sign;

        Flowable.just(authInfo)
                .map(new Function<String, AuthResult>() {
                    @Override
                    public AuthResult apply(String authInfo) throws Exception {
                        // 构造AuthTask 对象
                        AuthTask authTask = new AuthTask(activity);
                        // 调用授权接口，获取授权结果
                        Map<String, String> result = authTask.authV2(authInfo, true);
                        
                        return new AuthResult(result, true);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AuthResult>() {
                    @Override
                    public void accept(AuthResult authInfo) throws IOException {
                        // 判断resultStatus 为“9000”且result_code
                        // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                        if (TextUtils.equals(authInfo.getResultStatus(), "9000") && TextUtils.equals(authInfo.getResultCode(), "200")) {
                            if(callBack!=null){
                                callBack.onSuccess(authInfo.getUserId());
                            }
                        } else {
                            // 其他状态值则为授权失败
                            if(callBack!=null) {
                                callBack.onSuccess("授权失败:" + authInfo.getResultStatus()+" -> "+authInfo.getResultCode());
                            }
                        }
                    }
                });
    }

    /**
     * 回调接口
     */
    public interface CallBack {
        void onSuccess(String content);
        void onFail(String msg);
    }
    
}
