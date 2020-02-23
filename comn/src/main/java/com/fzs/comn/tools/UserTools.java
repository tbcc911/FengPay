package com.fzs.comn.tools;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.fzs.comn.ComnConfig;
import com.fzs.comn.model.User;
import com.hzh.frame.comn.callback.HttpCallBack;
import com.hzh.frame.core.BaseSP;
import com.hzh.frame.core.HttpFrame.BaseHttp;
import com.hzh.frame.util.Util;
import com.hzh.frame.widget.toast.BaseToast;
import com.hzh.frame.widget.xdialog.XDialogSubmit;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * @version 1.0
 * @date 2018/4/12
 */

public class UserTools {
    public static final String TAG = "UserTools";

    private static UserTools _instance;
    private static User mUser;
    private static Context context;


    public static UserTools getInstance() {
        synchronized (UserTools.class) {
            if (_instance == null) {
                _instance = new UserTools(null);
                _instance.getUser();
            }
            return _instance;
        }
    }

    public static UserTools getInstance(Context context) {
        synchronized (UserTools.class) {
            if (_instance == null) {
                _instance = new UserTools(context);
                _instance.getUser();
            }
            return _instance;
        }
    }


    public UserTools(Context context){
        if(context!=null){
            this.context=context;
        }else{
            this.context=ComnConfig.applicationContext;
        }
    }

    /**
     * 获取用户是否登录
     */
    public boolean getIsLogin() {
        return BaseSP.getInstance().getBoolean("login", false);
    }


    /**
     * 获取用户信息
     */
    public User getUser() {
        synchronized (User.class) {
            if (getIsLogin()) {
                //已登录
                mUser = new Select().from(User.class).executeSingle();
            }
            return mUser == null ? new User() : mUser;
        }
    }

    /**
     * 获取用户Token
     */
    public String getToken() {
        return getUser().getToken();
    }


    /**
     * 使用json信息创建一个完整的用户信息
     */
    public User createUser(JSONObject userJson,User user){
        clear();//清空用户信息
        mUser=new User()
                .setPhone(user.getPhone())
                .setAcount(user.getAcount())
                .setPassword(user.getPassword())
                .setToken(userJson.optString("tokenHead")+" "+userJson.optString("token"));
        mUser.save();
        BaseSP.getInstance().put("login", true);
        BaseSP.getInstance().put("token", mUser.getToken());
        return mUser;
    }

    /**
     * 刷新用户信息
     */
    public void refresh() {
        this.mUser = mUser = new Select().from(User.class).executeSingle();
    }

    /**
     * 清空用户信息
     */
    public void clear() {
        new Delete().from(User.class).execute();
        BaseSP.getInstance().put("login", false);
        BaseSP.getInstance().put("userId", "");
        BaseSP.getInstance().put("token", "");
        BaseSP.getInstance().put("tokenHead", "");
        mUser = null;
    }

    /**
     * 更新用户信息
     */
    public void updUser(User user) {
        user.save();
        mUser = user;
    }

    /**
     * 登录 | 第一步
     */
    public void login(User user, Activity activity, LoginCallBack callBack) {
        JSONObject params = new JSONObject();
        try {
            params.put("phone", user.getAcount());
            params.put("password", user.getPassword());
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        BaseHttp.getInstance().query("member/login", params, new LoginHttpCallBack(user, callBack).setSubmit(new XDialogSubmit(activity).alert()));
    }

    /**
     * 登录请求回调 | 第二步
     */
    class LoginHttpCallBack extends HttpCallBack {

        User user;
        LoginCallBack mLoginCallBack;

        public LoginHttpCallBack(User user, LoginCallBack callBack) {
            this.user = user;
            this.mLoginCallBack = callBack;
        }

        @Override
        public void onSuccess(JSONObject response) {
            if (200 == response.optInt("code")) {
                JSONObject userJson = response.optJSONObject("data");
                UserTools.getInstance().createUser(userJson, user);

                //获取用户金额资产信息
                UserTools.getInstance().loadUserInfo(mLoginCallBack);
                mLoginCallBack.loginComplete(mUser);//登录完成
            } else {
                BaseToast.getInstance().setMsg(response.optString("message")).show();
            }
        }
    }


    /**
     * 获取用户信息
     */
    public void loadUserInfo() {
        loadUserInfo(null, null);
    }

    /**
     * 获取用户信息
     */
    public void loadUserInfo(LoginCallBack callBack) {
        loadUserInfo(callBack, null);
    }

    /**
     * 获取用户信息
     */
    public void loadUserInfo(AssetsCallBack assetsCallBack) {
        loadUserInfo(null, assetsCallBack);
    }

    /**
     * 获取用户信息
     */
    public void loadUserInfo(final LoginCallBack callBack, final AssetsCallBack assetsCallBack) {
        BaseHttp.getInstance().query("member/getMemberInfo", new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                if (200 == response.optInt("code")) {
                    JSONObject userJson = response.optJSONObject("data");
                    if (mUser == null) {
                        mUser = new User();
                    }
                    mUser
                            .setPhone(userJson.optString("phone"))
                            .setAcount(userJson.optString("phone"))
                            .setHead(userJson.optString("avatarUrl"))
                            .setNickName(Util.isEmpty(userJson.optString("nickname")) ? "暂无" : userJson.optString("nickname"))
                            .setAlipayName(Util.isEmpty(userJson.optString("name")) ? "暂无" : userJson.optString("name"))
                            .setBankAccount(userJson.optString("bankAccount"))
                            .setBankName(userJson.optString("bankName"))
                            .setIdCard(userJson.optString("idCard"))
                            .setIdCardBack(userJson.optString("idCardBack"))
                            .setIdCardFront(userJson.optString("idCardFront"))
                            .setInviteCode(userJson.optString("inviteCode"))
                            .setIsActivation(userJson.optBoolean("isActivation"))
                            .setUsdtAddress(userJson.optString("usdtAddress"))
                            .setInviter(userJson.optString("inviter"));
                    
                    if(!Util.isEmpty(userJson.optString("token"))){
                        mUser.setToken(userJson.optString("token"));
                        BaseSP.getInstance().put("token", mUser.getToken());
                    }
                    mUser.save();

                    if (callBack != null) {
                        callBack.userInfoComplete(mUser);
                    }
                    if (assetsCallBack != null) {
                        assetsCallBack.onSuccess(mUser);
                    }
                } else {
                    if (callBack != null) {
                        callBack.showMsg(response.optString("message"));
                    }
                }
            }
        });
    }


    /**
     * 登录回调接口
     */
    public interface LoginCallBack {
        void showMsg(String msg);//提示信息

        void loginComplete(User user);//登录完成

        void userInfoComplete(User user);//用户信息获取完成
    }

    /**
     * 用户资料回调接口
     */
    public interface AssetsCallBack {
        void onSuccess(User user);//用户信息获取完成
    }

    /**
     * 用户资料回调接口
     */
    public interface PriceCallBack {
        void onSuccess(User user);//用户信息获取完成
    }
}
