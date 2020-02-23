package com.fzs.login;

import android.view.View;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fzs.comn.model.User;
import com.fzs.comn.tools.UserTools;
import com.fzs.comn.widget.dialog.XDialogQuitButton;
import com.hzh.frame.comn.callback.HttpCallBack;
import com.hzh.frame.core.BaseSP;
import com.hzh.frame.core.HttpFrame.BaseHttp;
import com.hzh.frame.ui.activity.BaseUI;
import com.hzh.frame.util.CloseAppUtil;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.disposables.Disposable;

/**
 * 找回密码
 */
@Route(path = "/login/ForgetPasswordUI")
public class ForgetPasswordUI extends BaseUI {
    EditText oldPassword;
    EditText passwordOne;
    EditText passwordTwo;
    Disposable disposable;

    @Override
    protected void onCreateBase() {
        setContentView(R.layout.login_ui_user_forget);
        getTitleView().setContent(getIntent().getStringExtra("title"));
        oldPassword = findViewById(R.id.oldPassword);
        passwordOne = findViewById(R.id.passwordOne);
        passwordTwo = findViewById(R.id.passwordTwo);
    }

    /**
     * 提交
     */
    public void commit(View view) {
        if (oldPassword.getText().toString().trim().length() == 0) {
            alert("请输入旧密码");
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
            params.put("oldPassword", oldPassword.getText().toString().trim());
            params.put("password", passwordOne.getText().toString().trim());
            params.put("rePassword", passwordTwo.getText().toString().trim());
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        BaseHttp.getInstance().write(ForgetPasswordUI.this,"member/editPassword", params, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                if (200 == response.optInt("code")) {
                    new XDialogQuitButton(ForgetPasswordUI.this, "登录密码修改成功，请重新登录", new XDialogQuitButton.XDialogQuitButtonCallBack() {
                        @Override
                        public void confirm() {
                            CloseAppUtil.restartLogin(ForgetPasswordUI.this, "/login/LoginUI", User.class);
                            BaseSP.getInstance().put("token","");
                            UserTools.getInstance().clear();
                        }
                    }).show();
                } else {
                    alert(response.optString("message"));
                }
            }

        });
    }
    
}
