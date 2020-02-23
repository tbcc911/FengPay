package com.fzs.service.tools;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

/**
 * @author
 * @version 1.0
 * @date 2020/2/22
 */
public class ServicePowerTools {

    //跳转系统设置里的通知使用权页面
    public static boolean openNotificationPower(Context context){
        try {
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return true;
        } catch(ActivityNotFoundException e) {
            try {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ComponentName cn = new ComponentName("com.android.settings","com.android.settings.Settings$NotificationAccessSettingsActivity");
                intent.setComponent(cn);
                intent.putExtra(":settings:show_fragment", "NotificationAccessSettings");
                context.startActivity(intent);
                return true;
            } catch(Exception ex) {
                ex.printStackTrace();
            }
            return false;
        }
    }
    
    //判断是否拥有通知使用权
    public static boolean isNotificationPower(Context context) {
        boolean enable = false;
        String packageName = context.getPackageName();
        String flat= Settings.Secure.getString(context.getContentResolver(),"enabled_notification_listeners");
        if (flat != null) {
            enable= flat.contains(packageName);
        }
        return enable;
    }
}
