package com.fzs.login;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fzs.comn.widget.dialog.AgreementDialog;
import com.hzh.frame.comn.callback.HttpCallBack;
import com.hzh.frame.core.HttpFrame.BaseHttp;
import com.hzh.frame.ui.activity.BaseUI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 注册第一步
 */
@Route(path = "/login/RegisterFirst")
public class RegisterFirstUI extends BaseUI {

    ImageView agree;
    LinearLayout agreeText;
    Button next;
    TextView send;
    EditText phone;
    EditText code;
    private boolean agreecheck = false;
    Disposable disposable;
    TextView userAgreement;
    TextView privacyAgreement;

    @Override
    protected void onCreateBase() {
        setContentView(R.layout.login_ui_user_reg_step1);
        getTitleView().setContent("注册");
        agree = findViewById(R.id.agree);
        agreeText = findViewById(R.id.agreeText);
        next = findViewById(R.id.next);
        send = findViewById(R.id.send);
        phone = findViewById(R.id.phone);
        code = findViewById(R.id.code);
        userAgreement = findViewById(R.id.userAgreement);
        privacyAgreement = findViewById(R.id.privacyAgreement);
        initView();
        chooseProtocol();
    }

    public void initView() {
        userAgreement.setOnClickListener(v -> ARouter.getInstance().build("/login/SettingProtocolUI")
                .withString("type","1")
                .navigation());

        privacyAgreement.setOnClickListener(v -> ARouter.getInstance().build("/login/SettingProtocolUI")
                .withString("type","2")
                .navigation());
        
        // 判断是否勾选了同意
        agree.setOnClickListener(v -> {
            if (agreecheck) {
                agree.setImageResource(R.mipmap.base_radio_up);
                agreecheck = false;
                next.setBackgroundResource(R.drawable.login_button_up_hui);
                next.setClickable(false);
            } else {
                agree.setImageResource(R.mipmap.base_radio_down);
                agreecheck = true;
                next.setBackgroundResource(R.drawable.login_button_select);
                next.setClickable(true);
            }
        });
        next.setOnClickListener(view -> {
            if (!agreecheck) {
                alert("注册会员时必须同意条款");
            } else if (phone.getText().length() == 0) {
                alert("请输入手机号");
            } else if (phone.getText().length() != 11) {
                alert("请输入正确的手机号!");
            } else if (code.getText().length() == 0) {
                alert("请输入验证码");
            } else {
                Intent intent = new Intent(RegisterFirstUI.this, RegisterSecondUI.class);
                intent.putExtra("code", code.getText().toString().trim());
                intent.putExtra("phone", phone.getText().toString().trim());
                startActivity(intent);
            }
        });
        send.setOnClickListener(view -> {
            if (phone.getText().length() == 0) {
                alert("手机号不能为空");
            } else if (phone.getText().length() != 11) {
                alert("手机号不正确");
            } else {
                JSONObject params = new JSONObject();
                try {
                    params.put("phone", phone.getText().toString().trim());
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                BaseHttp.getInstance().write(RegisterFirstUI.this,"member/getAuthCode", params, new HttpCallBack() {
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
                            }).doOnComplete(() -> {
                                disposable.dispose();
                                send.setEnabled(true);
                                send.setText(RegisterFirstUI.this.getResources().getString(R.string.user_reg_codeinput));
                                send.setBackgroundResource(R.drawable.comn_button_circle_select);
                            }).subscribe();
                        } else {
                            alert(response.optString("message"));
                        }
                    }
                });
            }
        });
    }

    private void chooseProtocol(){
        BaseHttp.getInstance().query("other/getUserAgreement", null, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);
                if (response.optInt("code") == 200){
                    if (response.optString("data") != null && response.optString("data").length()>0){
                        new AgreementDialog()
                                .setLayoutResour(R.layout.login_view_xdialog_protocols)
                                .setMessage(response.optString("data"))
                                .setCallback(new AgreementDialog.Callback() {
                                    @Override
                                    public void cancle() {}

                                    @Override
                                    public void confirm() {
                                        agree.setImageResource(R.mipmap.base_radio_down);
                                        agreecheck = true;
                                        next.setBackgroundResource(R.drawable.login_button_select);
                                        next.setClickable(true);
                                    }
                                })
                                .show(getSupportFragmentManager(),AgreementDialog.TAG);
                    }
                }else {
                    alert(response.optString("message"));
                }
            }
        });
    }
}
