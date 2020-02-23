package com.fzs.comn.callback;

/**
 * 回调接口
 */
public class CallBack<T> implements com.hzh.frame.callback.CallBack<T> {
    @Override
    public void onSuccess(T obj) {}

    public void onFail(T obj) {}
}
