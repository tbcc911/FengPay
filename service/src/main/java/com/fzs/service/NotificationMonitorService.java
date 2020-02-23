
package com.fzs.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.hzh.frame.widget.toast.BaseToast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.RequiresApi;

/**
 * 通知监听服务
 * 
 * 一些可用的主要方法
 *     cancelAllNotifications(); //移除所有可移除的通知
 *     cancelNotification(String key); //移除指定key的通知，要求api21以上
 *     cancelNotifications(String[] keys); //移除指定数组内的所有key的通知，要求api21以上
 *     getActiveNotifications(); //获取通知栏上的所有通知，返回一个StatusBarNotification[]
 *
 * StatusBarNotification:是通知栏的通知对象，里面包含了一条通知的所有信息。具体的如下
 *     getPackageName(); //获取发送通知的应用程序包名
 *     isClearable(); //通知是否可被清除
 *     getId(); //获取通知id
 *     getKey(); //获取通知的key
 *     getPostTime(); //通知的发送时间
 *     getNotification(); //获取Notification
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2) //在API 18(Android 4.3)Google加入了的NotificationListenerService
public class NotificationMonitorService extends NotificationListenerService{
    private static final int AliPay = 1;
    private static final int WeixinPay = 2;

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationManager mNM = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && mNM != null) {
            NotificationChannel mNotificationChannel = mNM.getNotificationChannel("aa_px_pay");
            if (mNotificationChannel == null) {
                mNotificationChannel = new NotificationChannel("aa_px_pay", "pxapy", NotificationManager.IMPORTANCE_DEFAULT);
                mNotificationChannel.setDescription("支付通知监控");
                mNM.createNotificationChannel(mNotificationChannel);
            }
        }
        NotificationCompat.Builder nb = new NotificationCompat.Builder(this, "aa_px_pay")
                .setContentTitle("支付通知监控")
                .setTicker("支付")
                .setSmallIcon(R.mipmap.base_app_logo)
                .setContentText("支付通知监控中.请保持此通知一直运行")
                .setWhen(System.currentTimeMillis());
        startForeground(1, nb.build());
        BaseToast.getInstance().setMsg("onCreate").show();
    }

    @Override //当连接成功时调用，一般在开启监听后会回调一次该方法
    public void onListenerConnected() {}

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override //当收到一条消息时回调，sbn里面带有这条消息的具体信息
    public void onNotificationPosted(StatusBarNotification statusBarNotification) {
        Bundle bundle = statusBarNotification.getNotification().extras;
        String packageName = statusBarNotification.getPackageName();
        BaseToast.getInstance().setMsg(packageName).show();
        String title = bundle.getString("android.title");
        String text = bundle.getString("android.text");
        if (title==null||text==null) return;
        if (packageName.equals("com.eg.android.AlipayGphone")) {
            Pattern patternOne=Pattern.compile("(\\S*)通过扫码向你付款([\\d\\.]+)元");
            if (patternOne.matcher(text).find()) {
                String uname = patternOne.matcher(text).group(1);
                String money = patternOne.matcher(text).group(2);
                if (!TextUtils.isEmpty(uname) && !TextUtils.isEmpty(money)) {
                    postMethod(AliPay, money, uname);
                }
            }
            Pattern patternTwo=Pattern.compile("成功收款([\\d\\.]+)元。享免费提现等更多专属服务，点击查看");
            if(patternTwo.matcher(text).find()){
                String money = patternTwo.matcher(text).group(1);
                if (!TextUtils.isEmpty(money)) {
                    postMethod(AliPay, money, "支付宝用户类型2");
                }
            }
        } else 
        if (packageName.equals("com.tencent.mm")) {
            Matcher m = Pattern.compile("微信支付收款([\\d\\.]+)元").matcher(text);
            if (m.find()) {
                String money = m.group(1);
                if (!TextUtils.isEmpty(money)) {
                    postMethod(WeixinPay, money, "微信用户");
                }
            }
        }
    }

    @Override //当移除一条消息的时候回调，sbn是被移除的消息
    public void onNotificationRemoved(StatusBarNotification paramStatusBarNotification) {}


    /**
     * 这个是系统自带的，onStartCommand方法必须具有一个整形的返回值，这个整形的返回值用来告诉系统在服务启动完毕后，
     * 如果被Kill，系统将如何操作，这种方案虽然可以，但是在某些情况or某些定制ROM上可能失效，认为可以多做一种保保守方案。
     * START_STICKY:
     *    如果系统在onStartCommand返回后被销毁，系统将会重新创建服务并依次调用onCreate和onStartCommand（注意：根据测试Android2.3.3以下版本只会调用onCreate根本不会调用onStartCommand，Android4.0可以办到），这种相当于服务又重新启动恢复到之前的状态了）。
     * START_NOT_STICKY:
     *    如果系统在onStartCommand返回后被销毁，如果返回该值，则在执行完onStartCommand方法后如果Service被杀掉系统将不会重启该服务。
     * START_REDELIVER_INTENT:
     *    START_STICKY的兼容版本，不同的是其不保证服务被杀后一定能重启。
     */
    @Override
    public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2) {
        return START_STICKY;
    }

    /**
     * 获取道的支付通知发送到服务器
     *
     * @param pay      支付方式(1支付宝，2微信)
     * @param money    支付金额
     * @param username 支付者名字
     */
    public void postMethod(int pay, String money, String username) {}
}