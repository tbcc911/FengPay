package com.fzs.login;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fzs.comn.model.User;
import com.fzs.comn.tools.UserTools;
import com.hzh.frame.ui.activity.BaseUI;
import com.hzh.frame.util.Util;

import java.io.IOException;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * 用户登录
 */
@Route(path = "/login/LoginUI")
public class LoginUI extends BaseUI implements OnClickListener {
    @Autowired
    public String targetPath;

    ImageView usernameRemove;
    ImageView passwordRemove;
    TextView register;
    TextView forgetPassword;
    TextView userAgreement;
    TextView privacyAgreement;
    Button login;
    EditText username;
    EditText password;
    private String loginType = "0";//0为手机号码,1为微信,2为QQ,3为新浪

    @Override
    protected void onCreateBase() {
        setContentView(R.layout.login_ui_user_login);
        ARouter.getInstance().inject(this);
        getTitleView().setContent("登录");
        usernameRemove = findViewById(R.id.usernameRemove);
        passwordRemove = findViewById(R.id.passwordRemove);
        register = findViewById(R.id.register);
        forgetPassword = findViewById(R.id.forgetPassword);
        login = findViewById(R.id.login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        userAgreement = findViewById(R.id.userAgreement);
        privacyAgreement = findViewById(R.id.privacyAgreement);

        usernameRemove.setVisibility(View.GONE);
        passwordRemove.setVisibility(View.GONE);
        usernameRemove.setOnClickListener(this);
        passwordRemove.setOnClickListener(this);
        register.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
        findViewById(R.id.sinaLogin).setOnClickListener(this);
        findViewById(R.id.qqLogin).setOnClickListener(this);
        findViewById(R.id.wecharLogin).setOnClickListener(this);
        login.setOnClickListener(this);
        getTitleView().setRightOnclickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                if (!Util.isEmpty(username.getText().toString())) {
                    usernameRemove.setVisibility(View.VISIBLE);
                } else {
                    usernameRemove.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                if (!Util.isEmpty(password.getText().toString())) {
                    passwordRemove.setVisibility(View.VISIBLE);
                } else {
                    passwordRemove.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
        userAgreement.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/login/SettingProtocolUI")
                        .withString("type","1")
                        .navigation();
            }
        });

        privacyAgreement.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/login/SettingProtocolUI")
                        .withString("type","2")
                        .navigation();
            }
        });
        //兼容401:Toten过期, 后台无法处理部分接口不拦截 | 清除用户信息
        UserTools.getInstance().clear();
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.login) {
            if (username.getText().toString().trim().length() == 0) {
                alert("请输入手机号/邮箱！");
                return;
            } else if (password.getText().toString().trim().length() == 0) {
                alert("请输入密码！");
                return;
            } else if (password.getText().toString().trim().length() < 4) {
                alert("密码少于6位！");
                return;
            }
            login(new User()
                    .setAcount(username.getText().toString().trim())
                    .setPhone(username.getText().toString().trim())
                    .setPassword(password.getText().toString().trim())
                    .setAuthCode("")
                    .setLoginType("0"));
        } else if (i == R.id.usernameRemove) {
            username.setText("");
            usernameRemove.setVisibility(View.GONE);

        } else if (i == R.id.passwordRemove) {
            password.setText("");
            passwordRemove.setVisibility(View.GONE);

        } else if (i == R.id.register) {
            ARouter.getInstance().build("/login/RegisterFirst").navigation();
        } else if (i == R.id.forgetPassword) {
            ARouter.getInstance().build("/login/ResetPassword").withString("title", "忘记密码").navigation();

        }else if (i == R.id.wecharLogin) {
            Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
            wechat.setPlatformActionListener(new OtherLoginCallBack());
            wechat.SSOSetting(false);
            if (!wechat.isClientValid()) {
                alert("微信未安装,请先安装微信");
            }
            loginType = "1";
            authorize(wechat);
        }
    }

    /**
     * 授权
     *
     * @param platform
     */
    private void authorize(Platform platform) {
        if (platform == null) {
            return;
        }
        if (platform.isAuthValid()) {  //如果授权就删除授权资料
            platform.removeAccount(true);
        }
        platform.showUser(null); //授权并获取用户信息
    }

    /**
     * 其他登录回调
     */
    class OtherLoginCallBack implements PlatformActionListener {
        /**
         * 授权成功的回调
         *
         * @param platform
         * @param i
         * @param hashMap
         */
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            Flowable.just(platform).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Platform>() {
                @Override
                public void accept(Platform platform) throws IOException {
                    User user = new User()
                            .setOpenId(platform.getDb().getUserId())//获取用户账号
                            .setNickName(platform.getDb().getUserName())//获取用户名字
                            .setHead(platform.getDb().getUserIcon())//获取用户头像
                            .setLoginType(loginType);
                    String sex = platform.getDb().getUserGender();//获取用户性别，m = 男, f = 女，如果微信没有设置性别,默认返回null
                    if (sex != null) {
                        if ("m".equals(sex)) {
                            user.setSex("0");
                        } else if ("f".equals(sex)) {
                            user.setSex("1");
                        }
                    }
                    login(user);
                }
            });
        }

        /**
         * 授权错误的回调
         *
         * @param platform
         * @param i
         * @param throwable
         */
        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            Flowable.just(platform).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Platform>() {
                @Override
                public void accept(Platform platform) throws IOException {
                    alert("授权登录失败");
                }
            });
        }

        /**
         * 授权取消的回调
         *
         * @param platform
         * @param i
         */
        @Override
        public void onCancel(Platform platform, int i) {
            Flowable.just(platform).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Platform>() {
                @Override
                public void accept(Platform platform) throws IOException {
                    alert("授权登录取消");
                }
            });
        }
    }


    /**
     * 登录
     */
    public void login(User user){
        UserTools.getInstance(this).login(user, this, new UserTools.LoginCallBack() {
            @Override //提示信息
            public void showMsg(String msg) {
                alert(msg);
            }

            @Override //用户信息获取完成
            public void userInfoComplete(User user) {
                if (!Util.isEmpty(targetPath)) {
                    if (!"this".equals(targetPath)) {
                        ARouter.getInstance().build(targetPath).with(getIntent().getExtras()).navigation();
                        finish();
                    }
                } else {
                    if (getIntent().getStringExtra("intent") != null && getIntent().getStringExtra("intent").length() > 0){
                        finish(); 
                    }else {
                        ARouter.getInstance().build("/main/MainUI").with(getIntent().getExtras()).navigation();
                        finish();
                    }
                }
            }

            @Override //登录完成
            public void loginComplete(User user) {}

        });
    }
    
}
