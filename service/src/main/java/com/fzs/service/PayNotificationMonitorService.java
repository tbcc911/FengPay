
package com.fzs.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.fzs.comn.model.MinePayHelp;
import com.fzs.comn.model.MineTeam;
import com.fzs.comn.tools.UserTools;
import com.fzs.comn.tools.Util;
import com.fzs.service.observer.PayNotificationMonitorServiceLifecycleObserver;
import com.hzh.frame.comn.callback.HttpCallBack;
import com.hzh.frame.comn.callback.WsCallBack;
import com.hzh.frame.core.BaseSP;
import com.hzh.frame.core.HttpFrame.BaseHttp;
import com.hzh.frame.core.WsFrame.BaseWs;
import com.hzh.frame.core.WsFrame.WsStatus;
import com.hzh.frame.widget.rxbus.MsgEvent;
import com.hzh.frame.widget.rxbus.RxBus;
import com.hzh.frame.widget.toast.BaseToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import androidx.annotation.RequiresApi;
import okhttp3.Response;
import okhttp3.WebSocket;

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
 *     
 *
 *  启动 | 支付通知监听服务 
 *  必要条件 如下:
 *  1.跳转系统设置里的通知使用权页面,让用户同意通知使用
 *  2.设置服务自动重启保护
 *          
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2) //在API 18(Android 4.3)Google加入了的NotificationListenerService
public class PayNotificationMonitorService extends NotificationListenerService implements LifecycleOwner {
    public static final String TAG="PayNotificationMonitorService";
    
    private static final String CHANNEL_ID="aa_px_pay";//通知渠道ID (创建通知时需要绑定一个渠道)
    private static final int AliPay = 1;
    private static final int WeixinPay = 2;
    private Notification notification;
    private boolean isShowNotification;//是否显示通知
    public static BaseWs baseWs;

    //LifecycleRegistry 实现了Lifecycle 
    private LifecycleRegistry mLifecycleRegistry=new LifecycleRegistry(PayNotificationMonitorService.this);
    
    @Override //当连接成功时调用，一般在开启监听后会回调一次该方法
    public void onListenerConnected() {}

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override //当收到一条消息时回调，sbn里面带有这条消息的具体信息
    public void onNotificationPosted(StatusBarNotification statusBarNotification) {
//        BaseToast.getInstance().setMsg("支付通知监控 : 收到一条消息").show();
        Bundle bundle = statusBarNotification.getNotification().extras;
        String packageName = statusBarNotification.getPackageName();
        String title = bundle.getString(Notification.EXTRA_TITLE);
        String content = bundle.getString(Notification.EXTRA_TEXT);
        if (title==null||content==null) return;
        if (packageName.equals("com.eg.android.AlipayGphone")) {
            Pattern patternOne=Pattern.compile("(\\S*)通过扫码向你付款([\\d\\.]+)元");
            if (patternOne.matcher(content).find()) {
                content=content.replaceAll("元","");
                content=content.replaceAll("通过扫码向你付款",",");
                String uname = content.split(",")[0];
                String money = content.split(",")[1];
                if (!Util.isEmpty(uname) && !Util.isEmpty(money)) {
                    postMethod(AliPay, money, uname);
                }
            }
            Pattern patternTwo=Pattern.compile("成功收款([\\d\\.]+)元。享免费提现等更多专属服务，点击查看");
            if(patternTwo.matcher(content).find()){
                content=content.replaceAll("成功收款","");
                String money=content.replaceAll("元。享免费提现等更多专属服务，点击查看","");
                postMethod(AliPay, money, "");
            }
        } else 
        if (packageName.equals("com.tencent.mm")) {
            Pattern patternThree = Pattern.compile("微信支付收款([\\d\\.]+)元");
            if (patternThree.matcher(content).find()) {
                content=content.replaceAll("微信支付收款","");
                String money=content.replaceAll("元","");
                postMethod(WeixinPay, money, "");
            }
        }
    }

    @Override //当移除一条消息的时候回调，sbn是被移除的消息
    public void onNotificationRemoved(StatusBarNotification paramStatusBarNotification) {}

    @Override
    public void onCreate() {
        super.onCreate();
        PayNotificationMonitorServiceLifecycleObserver myObserver = new PayNotificationMonitorServiceLifecycleObserver();
        mLifecycleRegistry.addObserver(myObserver);
        initBus();
    }
    
    private void initBus(){
//        RxBus.getInstance()
//                .toObservable(this, MsgEvent.class)
//                .filter(msgEvent -> msgEvent.getTag().equals(PayNotificationMonitorService.TAG))
//                .subscribe(msgEvent -> {
//                    if((Boolean) msgEvent.getMsg()){//接单中
//                        showNotification();
//                        connectSocket();
//                    }else{//休息中
//                        hideNotification();
//                        disConnectSocket();
//                    }
//                });

        RxBus.getInstance()
                .toObservable(this, MsgEvent.class)
                .filter(msgEvent -> msgEvent.getTag().equals(PayNotificationMonitorService.TAG))
                .subscribe(msgEvent -> {
                    if ((Boolean) msgEvent.getMsg()){
                        showNotification();
                        connectSocket();
                        BaseSP.getInstance().put("isServiceRunning","1");
                    }else {
                        hideNotification();
                        disConnectSocket();
                        BaseSP.getInstance().put("isServiceRunning","0");
                    }
                });
    }

    @Override //这个方法被我当作开关使用了,其实就是更新Service的状态
    public int onStartCommand(Intent intent, int flags, int startId) {
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
        return START_STICKY;
    }

    //显示一个前台通知
    public void showNotification() {
        if(!isShowNotification){
            NotificationManager notificationManager = (NotificationManager)this.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE);
            notificationManager.notify(1,createAndGetNotification());
//            startForeground(1, createAndGetNotification());
            isShowNotification=true;
        }
    }
    //隐藏一个前台通知
    public void hideNotification(){
        if(isShowNotification){
//            stopForeground(true);
            isShowNotification=false;
            NotificationManager notificationManager = (NotificationManager)this.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE);
            notificationManager.cancel(1);
        }
    }
    
    //创建一个前台通知
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
                    .setContentTitle("接单中")
                    .setTicker("支付通知监控")
                    .setSmallIcon(R.mipmap.base_app_logo)
                    .setContentText("支付跑分,全年无休,使命必达")
                    .setWhen(System.currentTimeMillis())
                    .build();
//            notification.flags = Notification.FLAG_ONGOING_EVENT;
        }
        return notification;
    }

    //接单
    public void connectSocket(){
        if (UserTools.getInstance().getIsLogin()){//用户已登录
            //未创建过连接 || 连接已经主动关闭
            if(baseWs==null || WsStatus.DISCONNECTED_ACTIVE.equals(baseWs.getState())){
                //创建订单
//                baseWs=WsSocket.connect("ws://47.104.224.184:8092/websocket/" + UserTools.getInstance().getUser().getUserId(),createWsCallBack());
                baseWs= BaseWs.connect("ws://47.104.224.184:8092/websocket/" +  UserTools.getInstance().getUser().getToken(),createWsCallBack());
            }
        }else{//用户未登录
            disConnectSocket();//未登录接毛个单啊
        }
    }
    //休息
    public void disConnectSocket(){
        if(baseWs!=null){
            baseWs.close();//关闭连接
        }
    }
    
    public WsCallBack createWsCallBack(){
        return new WsCallBack() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) { baseWs.setWebSocket(webSocket); }

            @Override
            public void onMessage(WebSocket webSocket, String message) { baseWs.setWebSocket(webSocket); }

            @Override
            public void onReConnect(WebSocket newWebSocket) { baseWs.setWebSocket(newWebSocket); }
        }.setIsReConnect(true);
    }

    /**
     * 获取到的支付通知发送到服务器
     * @param payType      支付方式(1支付宝，2微信)
     * @param money    支付金额
     * @param nickName 支付者昵称
     */
    public void postMethod(int payType,String money, String nickName) {
        JSONObject params = new JSONObject();
        try {
            params.put("payType", payType);
            params.put("value", money);
            params.put("nickName", nickName);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        BaseHttp.getInstance().write("order/automaticPay", params, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                BaseToast.getInstance().setMsg(response.optString("message")).show();
            }

            @Override
            public void onFail() {
                BaseToast.getInstance().setMsg("网络连接失败,请手动确认订单").show();
            }
        });
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }
    
}