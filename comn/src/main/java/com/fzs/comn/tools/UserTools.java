package com.fzs.comn.tools;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.alibaba.android.arouter.launcher.ARouter;
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
                .setTokenHead(userJson.optString("tokenHead"))
                .setToken(userJson.optString("token"))
                .setIsActivation(userJson.optInt("alipayBindingStatus",0) == 1);
        mUser.save();
        BaseSP.getInstance().put("login", true);
        BaseSP.getInstance().put("token", mUser.getTokenHead()+" "+mUser.getToken());
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
     * 去登录
     */
    public void jumpLoginUI(){
        jumpLoginUI(null);
    }

    /**
     * 去登录
     */
    public void jumpLoginUI(Bundle bundle){
        if(bundle!=null){
            ARouter.getInstance().build("/login/LoginUI").with(bundle).navigation();
        }else{
            ARouter.getInstance().build("/login/LoginUI").navigation();
        }
    }

    /**
     * 登录 | 第一步
     */
    public void login(User user, Activity activity, LoginCallBack callBack) {
        JSONObject params = new JSONObject();
        try {
            params.put("account", user.getAcount());
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
                            .setPhone(userJson.optString("account")) //账号
                            .setAcount(userJson.optString("account")) //账号
                            .setAgentRatio(userJson.optString("agentRatio")) //代理比例
                            .setAlipayUid(userJson.optString("alipayUid")) //支付宝UID
                            .setAvailableIntegration(userJson.optString("availableIntegration")) //可用积分
                            .setHead(userJson.optString("avatarUrl")) //头像
                            .setCreateTime(userJson.optString("createTime")) //注册时间
                            .setEffectiveStatus(userJson.optString("effectiveStatus")) //会员有效状态 0无效 1有效
                            .setFreezeIntegration(userJson.optString("freezeIntegration")) //冻结积分
                            .setIntegration(userJson.optString("integration")) //总积分
                            .setInviteCode(userJson.optString("inviteCode")) //邀请码
                            .setInviter(userJson.optString("inviter")) //邀请人姓名
                            .setMemberId(userJson.optString("memberId")) //会员ID
                            .setUserId(userJson.optString("memberId")) //会员ID
                            .setUserName(Util.isEmpty(userJson.optString("name")) ? "暂无" : userJson.optString("name")) //姓名
                            .setNickName(Util.isEmpty(userJson.optString("nickname")) ? "暂无" : userJson.optString("nickname")) //会员昵称
                            .setStatus(userJson.optString("status")) //账号启用状态 0禁用 1启用
                            .setIsActivation(!Util.isEmpty(mUser.getAlipayUid())) //是否授权
                            .setSuccessLevel(userJson.optString("successLevel")) //成功等级
                            .setSuccessRate(userJson.optString("successRate"));//成功率(直接在后面加上%号)
//                            .setNickName(Util.isEmpty(userJson.optString("nickname")) ? "暂无" : userJson.optString("nickname"))
//                            .setAlipayName(Util.isEmpty(userJson.optString("name")) ? "暂无" : userJson.optString("name"))
//                            .setBankAccount(userJson.optString("bankAccount"))
//                            .setBankName(userJson.optString("bankName"))
//                            .setIdCard(userJson.optString("idCard"))
//                            .setIdCardBack(userJson.optString("idCardBack"))
//                            .setIdCardFront(userJson.optString("idCardFront"))
//                            .setInviteCode(userJson.optString("inviteCode"))
//                            .setUsdtAddress(userJson.optString("usdtAddress"))
//                            .setInviter(userJson.optString("inviter"));
                    
                    if(!Util.isEmpty(userJson.optString("tokenHead")) && !Util.isEmpty(userJson.optString("token"))){
                        mUser.setTokenHead(userJson.optString("tokenHead"));
                        mUser.setToken(userJson.optString("token"));
                        BaseSP.getInstance().put("token", mUser.getTokenHead()+" "+mUser.getToken());
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
