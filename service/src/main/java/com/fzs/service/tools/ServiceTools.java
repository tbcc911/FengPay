package com.fzs.service.tools;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.util.Log;

import com.fzs.comn.tools.Util;
import com.fzs.service.PayNotificationMonitorService;
import com.fzs.service.serviceconnection.ComnServiceConnection;
import com.hzh.frame.comn.callback.CallBack;

import java.util.ArrayList;

public class ServiceTools {


    /**
     * 启动 | 支付通知监听服务
     * */
    public static boolean startPayMonitor(Context context) {
        ComponentName name = context.startService(new Intent(context, PayNotificationMonitorService.class));
        setAutoRestartServiceProtect(context,PayNotificationMonitorService.class);
        if (name == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断服务是否开启
     */
    public static boolean isServiceRunning(Context context, Class<? extends Service> serviceCls) {
        if (Util.isEmpty(serviceCls.getPackage())) {
            return false;
        }
        ActivityManager myManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager.getRunningServices(Integer.MAX_VALUE);
        for (int i = 0; i < runningService.size(); i++) {
            Log.w("ServiceTools",runningService.get(i).service.getClassName());
            if (runningService.get(i).service.getClassName().equals(serviceCls.getName())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 设置服务自动重启保护
     * Service的disable，会有Intent.ACTION_PACKAGE_CHANGED广播，移除服务列表名单。
     * 利用这一特性，把应用的NotificationListenerService实现类disable再enable，即可触发系统rebind操作。
     */
    private static void setAutoRestartServiceProtect(Context context,Class<? extends Service> serviceCls) {
        PackageManager packageManager = context.getPackageManager();
        /**
         * setComponentEnabledSetting
         * @params componentName 组件名称
         * @params newState 组件新的状态，可以设置三个值
         *                  不可用状态：COMPONENT_ENABLED_STATE_DISABLED 
         *                  可用状态：COMPONENT_ENABLED_STATE_ENABLED 
         *                  默认状态：COMPONENT_ENABLED_STATE_DEFAULT
         * @params flags 行为标签，值可以是DONT_KILL_APP或者0。 0说明杀死包含该组件的app
         * */
        packageManager.setComponentEnabledSetting(new ComponentName(context, serviceCls), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        packageManager.setComponentEnabledSetting(new ComponentName(context, serviceCls), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

    }


    //绑定 | 服务 | 测试Demo
    public static <T extends Binder> void bindServiceDemo(Context context, Class<? extends Service> serviceCls, CallBack<T> callBack) {
        ComnServiceConnection connection=new ComnServiceConnection().setCallBack(callBack);
        boolean connetionState=context.bindService(new Intent(context, serviceCls),connection, Context.BIND_AUTO_CREATE);
        if(!connetionState){
            callBack.onFail(null);
        }
    }
}
