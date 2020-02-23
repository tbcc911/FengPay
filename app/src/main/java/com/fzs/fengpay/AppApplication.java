package com.fzs.fengpay;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.baidu.mapapi.SDKInitializer;
import com.fzs.comn.ComnApplication;
import com.mob.MobSDK;

public class AppApplication extends ComnApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this);
        MobSDK.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //分DEX
        MultiDex.install(base);
    }
}
