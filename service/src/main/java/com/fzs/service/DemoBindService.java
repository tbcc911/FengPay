
package com.fzs.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
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
public class DemoBindService extends NotificationListenerService{
    private static final String CHANNEL_ID="aa_px_pay";//通知渠道ID (创建通知时需要绑定一个渠道)
    private PayServiceBinder payServiceBinder;
    private Notification notification;
    private boolean isShowNotification;//是否显示通知

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override //当连接成功时调用，一般在开启监听后会回调一次该方法
    public void onListenerConnected() {}

    @Override //当收到一条消息时回调，sbn里面带有这条消息的具体信息
    public void onNotificationPosted(StatusBarNotification statusBarNotification) { }

    @Override //当移除一条消息的时候回调，sbn是被移除的消息
    public void onNotificationRemoved(StatusBarNotification paramStatusBarNotification) {}

    @Override //绑定服务时才会调用 * 必须要实现的方法
    public IBinder onBind(Intent intent) {
        return getPayServiceBinder();
    }

    public PayServiceBinder getPayServiceBinder(){
        if(payServiceBinder==null){
            payServiceBinder = new PayServiceBinder();
        }
        return payServiceBinder;
    }
    
    public class PayServiceBinder extends Binder {

        //查询前台通知是否显示通知
        public boolean isShowNotification(){
            return isShowNotification;
        }

        //显示一个前台通知
        public void showNotification() {
            if(!isShowNotification){
                startForeground(1, createAndGetNotification());
                isShowNotification=true;
            }
        }
        //隐藏一个前台通知
        public void hideNotification(){
            if(isShowNotification){
                stopForeground(true);
                isShowNotification=false;
            }
        }
    }

    //创建一个通知
    public Notification createAndGetNotification(){
        if(notification==null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //当你的 targetSdk >= 26 时，系统是不会给你添加默认Channel,需要手动添加
                NotificationManager notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
                if(notificationManager != null){
                    NotificationChannel channel = notificationManager.getNotificationChannel(CHANNEL_ID);
                    //NotificationChannel是在程序初始化时就已经创建并注册了，千万不要每次发通知的时候都去重新创建一次，没有任何意义
                    if (channel == null) {//创建一次
                        channel = new NotificationChannel(CHANNEL_ID, "pxapy", NotificationManager.IMPORTANCE_DEFAULT);
                        channel.setDescription("支付通知监控"); // 配置通知渠道的属性
                        channel.setSound(null, null);// 设置通知出现时声音，默认通知是有声音的
                        channel.enableLights(true);// 设置通知出现时的闪灯（如果 android 设备支持的话）
                        channel.setLightColor(Color.RED);//闪灯颜色
                        channel.enableVibration(true);// 设置通知出现时的震动（如果 android 设备支持的话）
                        channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                        notificationManager.createNotificationChannel(channel);//最后在 notificationManager 中创建该通知渠道
                    }
                }
            }
            //使用已经构建好的渠道,构建我们的通知并返回
            notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("支付通知监控")
                    .setTicker("支付")
                    .setSmallIcon(R.mipmap.base_app_logo)
                    .setContentText("支付通知监控中.请保持此通知一直运行")
                    .setWhen(System.currentTimeMillis())
                    .build();
        }
        return notification;
    }
}