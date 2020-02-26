package com.fzs.login;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fzs.comn.ComnConfig;
import com.fzs.comn.model.User;
import com.hzh.frame.comn.callback.CallBack;
import com.hzh.frame.comn.callback.HttpCallBack;
import com.hzh.frame.core.HttpFrame.BaseHttp;
import com.hzh.frame.ui.activity.BaseUI;
import com.hzh.frame.widget.x5webview.X5WebViewUI;
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
 * 关联手机号
 */
@Route(path = "/login/RelationPhoneUI")
public class RelationPhoneUI extends BaseUI {

    ImageView agree;
    TextView agreeText;
    Button next;
    TextView send;
    EditText phone;
    EditText code;
    private boolean agreecheck = true;
    Disposable disposable;
    String targetPath;
    User user;

    @Override
    protected void onCreateBase() {
        user=new User()
                .setOpenId(getIntent().getStringExtra("openId"))
                .setNickName(getIntent().getStringExtra("nickName"))
                .setLoginType(getIntent().getStringExtra("loginType"))
                .setHead(getIntent().getStringExtra("userIcon"))
                .setSex(getIntent().getStringExtra("userGender"));
        setContentView(R.layout.login_ui_relation_phone);
        getTitleView().setContent("关联");
        agree = findViewById(R.id.agree);
        agreeText = findViewById(R.id.agreeText);
        next = findViewById(R.id.next);
        send = findViewById(R.id.send);
        phone = findViewById(R.id.phone);
        code = findViewById(R.id.code);
        targetPath=getIntent().getStringExtra("targetPath");
        initView();
    }

    public void initView() {
        // 判断是否勾选了同意
        agree.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (agreecheck) {
                    agree.setImageResource(R.mipmap.base_radio_up);
                    agreecheck = false;
                    next.setBackgroundResource(R.drawable.base_button_disabled);
                    next.setClickable(false);
                } else {
                    agree.setImageResource(R.mipmap.base_radio_down);
                    agreecheck = true;
                    next.setBackgroundResource(R.drawable.comn_button_circle_select);
                    next.setClickable(true);
                }
            }
        });
        agreeText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(RelationPhoneUI.this, X5WebViewUI.class);
                in.putExtra("url", ComnConfig.www);
                startActivity(in);
            }
        });
        next.setOnClickListener(new RelationListener());
        send.setOnClickListener(new SendSmsCodeListener());
    }

    //发送验证码
    class SendSmsCodeListener implements OnClickListener {
        @Override
        public void onClick(View view) {
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
                BaseHttp.getInstance().query("login/getSmsCode", params, new HttpCallBack() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        if (200 == response.optInt("code")) {
                            //testCode | 测试验证码
                            alert(response.optJSONObject("data").optString("code"));
                            //开启发送计时
                            send.setEnabled(false);
                            send.setBackgroundResource(R.drawable.base_button_disabled);
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
                                    send.setText(RelationPhoneUI.this.getResources().getString(R.string.user_reg_codeinput));
                                    send.setBackgroundResource(R.drawable.comn_button_circle_select);
                                }
                            }).subscribe();
                        } else {
                            alert(response.optString("message"));
                        }
                    }
                });
            }
        }
    }

    //关联
    class RelationListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            if (!agreecheck) {
                alert("注册会员时必须同意条款");
                return;
            }
            if (phone.getText().length() == 0) {
                alert("请输入手机号");
                return;
            }
            if (phone.getText().length() != 11) {
                alert("请输入正确的手机号!");
                return;
            }
            if (code.getText().length() == 0) {
                alert("请输入验证码");
                return;
            }
            JSONObject params = new JSONObject();
            try {
                params.put("openId", user.getOpenId());
                params.put("nickName", user.getNickName());
                params.put("source", user.getLoginType());//0为手机号码,1为微信,2为QQ,3为新浪
                params.put("userIcon", user.getHead());
                params.put("phone", phone.getText().toString().trim());
                params.put("code", code.getText().toString().trim());
                if(user.getSex() != null){//获取到性别信息
                    params.put("userGender", user.getSex());
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            BaseHttp.getInstance().query("login/bindingPhone", params, new HttpCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    if (200 == response.optInt("code")) {
                        new XDialog2Button(RelationPhoneUI.this).setMsg("恭喜您关联成功，点击可以直接登录！").setConfirmName("登录", "取消").setCallback(new CallBack() {
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
    }
    
    public void login() {
//        UserTools.getInstance().login(user, this, new UserTools.LoginCallBack() {
//            @Override
//            public void showMsg(String msg) {
//                alert(msg);
//            }
//
//            @Override
//            public void loginComplete() {
//                finish();
//            }
//
//            @Override
//            public void toRelationPhone(User user) {
//                alert("关联失败,请稍后再试");
//            }
//
//            @Override
//            public void userInfoComplete() {
//                ARouter.getInstance().build("/main/MainUI").with(getIntent().getExtras()).navigation();
//            }
//        });
    }

}
