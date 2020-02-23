package com.fzs.login.request;

import com.hzh.frame.core.HttpFrame.api.ApiRequest;

import org.json.JSONObject;

import java.util.HashMap;

import io.reactivex.Flowable;

/**
 * @vesion 1.0
 * @date 2018/5/21
 */
public class LoginRequest extends ApiRequest{

    /**
     * 用户登录(这里只写了一个例子,因为现项目中需要改成这种Retrofit2+Okhttp3+Rxjava2的请求框架需要改动成本太大,调用可参考LoginUI类,写了一个调用例子)
     * @param username 用户名
     * @param password 密码
     */
    public Flowable<JSONObject> login(String username, String password) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("userName", username);
        params.put("password", password);
        params.put("DEVICE", "暂无");
        return queryServer(3028,params);
    }
    
}
