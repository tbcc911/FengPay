package com.fzs.comn.widget.pay.alipay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;

import java.util.Map;

public class Alipay{

    public static final int SDK_PAY_FLAG = 1;
    public Activity activity;
    public CallBack callBack;// 支付结果回调
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            Map<String, String> resultMap = (Map<String, String>) msg.obj;
            String  resultStatus=resultMap.get("resultStatus");
            String  resultMsg=resultMap.get("memo");
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 9000:支付成功
                        callBack.success();
                    } else
                    if (TextUtils.equals(resultStatus, "8000")) {
                        // 8000:代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        callBack.waiting(resultStatus,resultMsg);
                    } else {
                        // 其它:可能支付失败
                        callBack.failure(resultStatus,resultMsg);
                    }
                    break;
                }
                default:
                    callBack.failure(resultStatus,resultMap.get("memo"));
                    break;
            }
        }
    };
    
    public Alipay(Activity activity){
        this.activity=activity;
    }

    public void pay(String orderInfo, CallBack callBack) {
        this.callBack=callBack;
        Runnable payRunnable = () -> {
            // 构造PayTask 对象
            PayTask alipay = new PayTask(activity);
            // 调用支付接口
            Map<String, String> result = alipay.payV2(orderInfo,true);
            Message msg = new Message();
            msg.what = SDK_PAY_FLAG;
            msg.obj = result;
            mHandler.sendMessage(msg);
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
}
