package com.fzs.login;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * 找回密码
 */
@Route(path = "/login/ResetPassword")
public class ResetPasswordUI extends BaseUI {
    TextView send;
    EditText phone;
    EditText code;
    EditText passwordOne;
    EditText passwordTwo;
    Disposable disposable;

    @Override
    protected void onCreateBase() {
        setContentView(R.layout.login_ui_user_forget_password);
        getTitleView().setContent(getIntent().getStringExtra("title"));
        send = findViewById(R.id.send);
        phone = findViewById(R.id.phone);
        code = findViewById(R.id.code);
        passwordOne = findViewById(R.id.passwordOne);
        passwordTwo = findViewById(R.id.passwordTwo);
    }

    /**
     * 发送验证码
     */
    public void send(View view) {
        if (phone.getText().toString().trim().length() == 0) {
            alert("手机号不能为空");
            return;
        }
        if (phone.getText().toString().trim().length() != 11) {
            alert("手机号不正确");
            return;
        }
        JSONObject params = new JSONObject();
        try {
            params.put("receiver", phone.getText().toString().trim());
            params.put("verifyType", "2");
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        BaseHttp.getInstance().write(ResetPasswordUI.this,"member/getAuthCode", params, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                if (200 == response.optInt("code")) {
                    //开启发送计时
                    send.setEnabled(false);
                    send.setBackgroundResource(R.drawable.login_button_disabled);
                    //从0开始发射11个数字为：0-60依次输出，延时0s执行，每1s发射一次。
                    disposable = Flowable.intervalRange(0, 60, 0, 1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).doOnNext(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            send.setText((60 - aLong) + "s");
                        }
                    }).doOnComplete(new Action() {
                        @Override
                        public void run() throws Exception {
                            disposable.dispose();
                            send.setEnabled(true);
                            send.setText(ResetPasswordUI.this.getResources().getString(R.string.user_reg_codeinput));
                            send.setBackgroundResource(R.drawable.comn_button_circle_select);
                        }
                    }).subscribe();
                } else {
                    alert(response.optString("message"));
                }
            }
        });
    }

    /**
     * 提交
     */
    public void commit(View view) {
        if (phone.getText().toString().trim().length() == 0) {
            alert("请输入手机号");
            return;
        }
        if (phone.getText().toString().trim().length() != 11) {
            alert("请输入正确的手机号");
            return;
        }
        if (code.getText().toString().trim().length() == 0) {
            alert("请输入验证码");
            return;
        }
        if (passwordOne.getText().toString().trim().length() < 6 || passwordOne.getText().toString().trim().length() > 16) {
            alert("请输入6-16位密码");
            return;
        }
        if (!passwordOne.getText().toString().trim().equals(passwordTwo.getText().toString().trim())) {
            alert("输入密码不一致");
            return;
        }
        JSONObject params = new JSONObject();
        try {
            params.put("phone", phone.getText().toString().trim());
            params.put("authCode", code.getText().toString().trim());
            params.put("password", passwordOne.getText().toString().trim());
            params.put("rePassword", passwordTwo.getText().toString().trim());
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        BaseHttp.getInstance().write(ResetPasswordUI.this,"member/forgetPassword", params, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                if (200 == response.optInt("code")) {
                    new XDialog2Button(ResetPasswordUI.this).setMsg("恭喜您找回成功，点击可以直接登录！").setConfirmName("登录", "取消").setCallback(new CallBack() {
                        @Override
                        public void onSuccess(Object object) {
                            login();
                        }
                    }).show();
                } else {
                    alert(response.optString("message"));
                }
            }

        });
    }

    public void login() {
        User user = new User()
                .setAcount(phone.getText().toString().trim())
                .setPassword(passwordOne.getText().toString().trim())
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
