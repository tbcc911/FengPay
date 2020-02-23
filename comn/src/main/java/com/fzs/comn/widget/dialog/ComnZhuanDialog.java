package com.fzs.comn.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fzs.comn.R;
import com.fzs.comn.tools.UserTools;
import com.fzs.comn.tools.Util;
import com.hzh.frame.comn.callback.HttpCallBack;
import com.hzh.frame.core.HttpFrame.BaseHttp;
import com.hzh.frame.util.AndroidUtil;
import com.hzh.frame.widget.toast.BaseToast;
import com.jakewharton.rxbinding3.view.RxView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;
import kotlin.Unit;

/**
 * E矿场存入
 * */
public class ComnZhuanDialog extends Dialog {

    public Activity activity;

    public EditText ecc;//ECC
    public EditText usdt;//UDST
    public ComnPasswordDialog mPasswordDialog;
    public CallBack callBack;
    public TextView tips;
    float value;

    public ComnZhuanDialog(Activity activity) {
        super(activity, R.style.XSubmitDialog);
        this.activity=activity;
        LayoutInflater in = LayoutInflater.from(activity);
        View viewDialog = in.inflate(R.layout.comn_view_dialog_zhuan, null);
        ecc=viewDialog.findViewById(R.id.ecc);
        usdt=viewDialog.findViewById(R.id.usdt);
        tips = viewDialog.findViewById(R.id.tips);
        TextView submit = viewDialog.findViewById(R.id.submit);
        RxView.clicks(submit).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(new Consumer<Unit>() {
            @Override
            public void accept(Unit unit) throws Exception {
                new ComnPasswordDialog(activity).setCallBack(password -> submit(password)).show();
            }
        });
        viewDialog.findViewById(R.id.close).setOnClickListener(view -> dismiss());
        // 这里可以设置dialog的大小，当然也可以设置dialog title等
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(AndroidUtil.getWindowWith(activity) * 9 / 10, FrameLayout.LayoutParams.WRAP_CONTENT);
        setContentView(viewDialog,layoutParams);
        // 点击对话框外部取消对话框显示
        setCanceledOnTouchOutside(true);
        init();
    }

    public void init(){
        ecc.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

                if (!Util.isEmpty(ecc.getText().toString())) {
                    double eccNumber=Double.parseDouble(ecc.getText().toString().trim());
                    usdt.setText((eccNumber*value)+"");
                }else{
                    usdt.setText("");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
    }


    //划转
    public void submit(String password){
        if(Util.isEmpty(ecc.getText().toString().trim())){
            BaseToast.getInstance().setMsg("请输入USDT").show();
            return;
        }
        dismiss();
        if(callBack != null){
            callBack.confirm();
        }
        JSONObject params = new JSONObject();
        try {
            params.put("value", ecc.getText().toString().trim());
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        BaseHttp.getInstance().write(activity,"finance/tbccTransferInto", params, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);
                if (200 == response.optInt("code")) {
                    dismiss();
                    UserTools.getInstance(activity).loadUserInfo(user -> {
                        if (callBack != null){
                            callBack.confirm();
                        }
                    });
                }
                BaseToast.getInstance().setMsg(response.optString("message")).show();
            }
        });
    }

    @Override
    public void show() {
        if (!isShowing()) {
            super.show();
        }else{
            super.dismiss();
            super.show();
        }
    }

    public ComnZhuanDialog alert(){
        show();
        return this;
    }

    /**
     * 选择结果回调
     * */
    public interface CallBack{
        /**
         * 确定
         */
        void confirm();
    }

    public CallBack getCallBack() {
        return callBack;
    }

    public ComnZhuanDialog setCallBack(CallBack callBack) {
        this.callBack = callBack;
        return this;
    }

}
