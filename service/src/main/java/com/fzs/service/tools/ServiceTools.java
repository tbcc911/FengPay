package com.fzs.service.tools;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.fzs.service.NotificationMonitorService;

/**
 * @author
 * @version 1.0
 * @date 2020/2/21
 */
public class ServiceTools {
    
    //启动支付通知监听服务
    public static boolean startPayMonitor(Context context){
        ComponentName name=context.startService(new Intent(context , NotificationMonitorService.class));
        toggleNotificationListenerService(context);
        if (name == null) {
            return false;
        }else{
            return true;
        }
    }

    /**
     * Service的disable，会有Intent.ACTION_PACKAGE_CHANGED广播，移除服务列表名单。
     * 利用这一特性，把应用的NotificationListenerService实现类disable再enable，即可触发系统rebind操作。
     * */
    private static void toggleNotificationListenerService(Context context) {
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
        packageManager.setComponentEnabledSetting(new ComponentName(context, NotificationMonitorService.class), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        packageManager.setComponentEnabledSetting(new ComponentName(context, NotificationMonitorService.class), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }
}
