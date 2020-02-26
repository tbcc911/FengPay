package com.fzs.login;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fzs.comn.model.User;
import com.fzs.comn.tools.UserTools;
import com.hzh.frame.comn.callback.CallBack;
import com.hzh.frame.comn.callback.HttpCallBack;
import com.hzh.frame.core.HttpFrame.BaseHttp;
import com.hzh.frame.ui.activity.BaseUI;
import com.hzh.frame.util.CloseAppUtil;
import com.hzh.frame.widget.xdialog.XDialog2Button;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 注册第二步
 */
@Route(path = "/login/RegisterSecond")
public class RegisterSecondUI extends BaseUI {
    EditText inviteCode;
    EditText password;
    EditText repeatPassword;
    Button button;

    @Override
    protected void onCreateBase() {
        setContentView(R.layout.login_ui_user_reg_step2);
        getTitleView().setContent("注册");
        inviteCode = findViewById(R.id.inviteCode);
        password = findViewById(R.id.password);
        repeatPassword = findViewById(R.id.repeatPassword);
        button = findViewById(R.id.button);
        initView();
    }

    public void initView() {
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                if (password.getText().length() == 0) {
                    alert("请输入密码！");
                    return;
                }
                if (repeatPassword.getText().length() == 0) {
                    alert("请再次输入密码！");
                    return;
                }
                if (inviteCode.getText().length() == 0) {
                    alert("请输入邀请码！");
                    return;
                }
                JSONObject params = new JSONObject();
                try {
                    params.put("inviteCode", inviteCode.getText().toString().trim()); //邀请码
                    params.put("authCode", getIntent().getStringExtra("code"));
                    params.put("phone", getIntent().getStringExtra("phone"));
//                    params.put("source", 0);//0为手机号码,1为微信,2为QQ,3为新浪
                    params.put("password", password.getText().toString().trim());
                    params.put("rePassword", repeatPassword.getText().toString().trim());
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                BaseHttp.getInstance().write(RegisterSecondUI.this, "member/register", params, new HttpCallBack() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        if (200 == response.optInt("code")) {
                            new XDialog2Button(RegisterSecondUI.this).setMsg("恭喜您注册成功，点击可以直接登录！").setConfirmName("登录", "取消").setCallback(new CallBack() {
                                @Override
                                public void onSuccess(Object object) { login(); }
                            }).show();
                        } else {
                            alert(response.optString("message"));
                        }
                    }
                });
            }
        });
    }

    public void login() {
        User user = new User()
                .setAcount(getIntent().getStringExtra("phone"))
                .setPhone(getIntent().getStringExtra("phone"))
                .setPassword(password.getText().toString().trim())
                .setAuthCode(getIntent().getStringExtra("code"))
                .setLoginType("0");
        UserTools.getInstance(this).login(user, this, new UserTools.LoginCallBack() {
            @Override
            public void showMsg(String msg) {
                alert(msg);
            }

            @Override
            public void userInfoComplete(User user) {
                // 关闭非当前的所有Activity
                for (int i = 0; i < CloseAppUtil.activityList.size(); i++) {
                    Activity activity=CloseAppUtil.activityList.get(i);
                    if (null != activity && !activity.getClass().equals(RegisterSecondUI.class)) {
                        CloseAppUtil.activityList.get(i).finish();
                    }
                }
                ARouter.getInstance().build("/main/MainUI").with(getIntent().getExtras()).navigation();
                finish();
            }

            @Override
            public void loginComplete(User user) {}

        });
    }
}
