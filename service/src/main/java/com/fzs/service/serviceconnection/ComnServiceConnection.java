package com.fzs.service.serviceconnection;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;

import com.hzh.frame.callback.CallBack;


public class ComnServiceConnection<T extends Binder> implements ServiceConnection {
    
    CallBack<T> callBack;

    @Override // 当与service的连接建立后被调用
    public void onServiceConnected(ComponentName name, IBinder service) {
        if(callBack!=null){
            callBack.onSuccess((T)service);
        }
    }

    @Override // 当与service的连接意外断开时被调用
    public void onServiceDisconnected(ComponentName name) {}

    public CallBack<T> getCallBack() {
        return callBack;
    }

    public ComnServiceConnection<T> setCallBack(CallBack<T> callBack) {
        this.callBack = callBack;
        return this;
    }
}
