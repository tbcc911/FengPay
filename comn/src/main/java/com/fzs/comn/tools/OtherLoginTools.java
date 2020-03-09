package com.fzs.comn.tools;

import android.app.Activity;
import android.provider.Telephony;
import android.text.TextUtils;

import com.alipay.sdk.app.AuthTask;
import com.fzs.comn.widget.pay.alipay.Alipay;
import com.fzs.comn.widget.pay.alipay.AuthResult;
import com.fzs.comn.widget.pay.alipay.OrderInfoUtil2_0;
import com.fzs.comn.widget.pay.wx.PayInfoBean;
import com.hzh.frame.comn.callback.CallBack;
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
    public static final String APPID = "2021001140639648";

    /**
     * 用于支付宝账户登录授权业务的入参 pid。 合作伙伴身份PID
     */
    public static final String PID = "2088732129395795";

    /**
     * 用于支付宝账户登录授权业务的入参 target_id。可自定义，保证唯一性即可
     */
    public static final String TARGET_ID = "89878564348686";

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
    public static final String RSA2_PRIVATE = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQChAYcdXpGcw5l/HNhVMNEGEyR1FAmbw5iiIYixVQzboukTUm1C8O99dUmdnkOsUDMXHsQj5fpP8b3CHK1hiDuEi3e1Ri2htwzMu3WIG+6otrLkFMypTNx56eWx+WQJQuKu72VHx4QLnHrmILrSfB4ysEZeGxwDsTV8qp03UmQoKo7XDrc2eJDt2beVyOM+BhU1dvNNDXZBkdQORjC/fCUv5dk3l4wkjnf8HjfqR1BFWEgUeipGXNYszk8P2O0ZqXMd2BRQm//of5onrqvi/XYo1dd6OabUiegHjfHt+Rj8w3WU4HcKBqAt1xD7/xqFJBcMfNMVqRoblZ6NZt1rOJfpAgMBAAECggEBAJIPzsGID1VF5TR5uI/gT1/Rcdt8qBLU6Nz8ywMFAzslmYhb2J9H8BvCkVwaPY5O22p4ELv4YJKx3dIBLhEzA7uEwr6CwdbZNxVsYn1tyZ7oz3U2oUxdXA6FcEl605PLR9Yne5wYlePqu1uWCaX1z3dwjwXEVi/LTZihNyr+5bnBGjSTs/C8Gq1If6xVUKVf38RRVlPWg71uM8SeNoNyxBvUKFBTRqGrLjA4cPndb+vHanjNN715GGb4M67zgmTM7hFy2NzRptsEGDsKYRSydSQhw4fk1DiibbXgNM8uWSEce95Gh7NjINsFbacjhXDgERb8RRYPxbqnyqmFr2MQ+wECgYEA+B6YjF4JLHhJXAr7Qk7pt4dlddkhhKGPTT8s69oM6uWPRdDcme04DxTjkHw/nueH9fOFfcVUL8IrB/tQL6Kf/pTbreZVD6kCa3Uc4if0inUxaE3gGFT86ZMKquKCHJr/wJ9/Mx/mtuVLXN/7oY/em8XH+vlV6UbRb3jGOGyJCzECgYEAph6hldgMIvGt5D/VieCmRwNcybi5uxiS7COqahmor9ZoBcEEX76Kwst0I/z05yz8/0q3GQ0xuESxKl9Y8q8X6RfDlCdAAehgp7RKCOj2iRHDqWgzGKZB7tLWTMqa52f3cRYg5svtr9EeEKpgi5VfpxRSdryaK1XDV8hM71PsOjkCgYEArDBHe3oy8ZqUhnzUe+LZzVT4M325Bhs8oMYL/a14M3eWI0RcI7J9qfG8L2QansNdFnGmnW7ZCr+8LzDYKAa68+kIPznOpCyNElfW8Jf73C6SWClbMGeikNJLn+XLl+B1Ro8prigga6La1xxVu3xlZ0Mtkl2AooBUDgEJxtSDkrECgYEAi7R/GFHWAx1VrPuyTn5L2LUPN+bO7HZCOa3VB94Ea7zZRt2JHCxW7nPEfO+j1zeSnfDsA3mBMwMyK/x8Bb0TUsjVhqdrwS74+pE5Ij0p+SSjdLlZH4XTNVLDP1m05KAfDVAa437lqlMoAcQH5dv90bnZkFoVHhTGb/DYIv5lUKECgYByzE5h/Zt7opwiEGjAou1/Qfl8l5iRkmeWVjwhigt/A+9SpOfQT70w64KZlPysmMkH81p5cmQbxjyLy7BvMdJ31bfy7dhcJA1zFfWwBY+XYozlaeTWdXlI6mn0I1JKiDDUEX2x6IgDZoSh1zNIs/YJIXYau+lwxXTXe38ivzNmyA==";
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
                                callBack.onFail("授权失败:" + authInfo.getResultStatus()+" -> "+authInfo.getMemo());
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
