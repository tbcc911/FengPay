package com.fzs.comn.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fzs.comn.R;
import com.fzs.comn.widget.view.ComnPasswordInputView;
import com.hzh.frame.util.AndroidUtil;
import com.jakewharton.rxbinding3.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;
import kotlin.Unit;

/**
 * 密码输入框
 * */
public class ComnPasswordDialog extends Dialog {
    
    public Activity activity;
    public ComnPasswordInputView mPasswordInputView;
    public TextView cancels;
    public TextView confirms;
    public XUserMainSignRewardDialogCallBack callBack;

    public ComnPasswordDialog(Activity activity) {
        super(activity, R.style.XSubmitDialog);
        this.activity=activity;
		LayoutInflater in = LayoutInflater.from(activity);
		View viewDialog = in.inflate(R.layout.comn_view_xdialog_password, null);
        mPasswordInputView = viewDialog.findViewById(R.id.password); //密码输入框
        cancels = viewDialog.findViewById(R.id.cancel);
        confirms = viewDialog.findViewById(R.id.confirm);
		// 这里可以设置dialog的大小，当然也可以设置dialog title等
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(AndroidUtil.getWindowWith(activity) * 9 / 10, FrameLayout.LayoutParams.WRAP_CONTENT);
		setContentView(viewDialog,layoutParams);
		// 点击对话框外部取消对话框显示
		setCanceledOnTouchOutside(true);

        cancels.setOnClickListener(view -> dismiss());
        RxView.clicks(confirms).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(new Consumer<Unit>() {
            @Override
            public void accept(Unit unit) throws Exception {
                if (mPasswordInputView.getText().toString().trim().length() != 6){
                    Toast.makeText(activity,"请输入6位密码",Toast.LENGTH_LONG).show();
                }
                dismiss();
                if(callBack!=null){
                    callBack.confirm(mPasswordInputView.getText().toString());
                }
            }
        });
	}

    public XUserMainSignRewardDialogCallBack getCallBack() {
        return callBack;
    }

    public ComnPasswordDialog setCallBack(XUserMainSignRewardDialogCallBack callBack) {
        this.callBack = callBack;
        return this;
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
    
    /**
     * 选择结果回调
     * */
    public interface XUserMainSignRewardDialogCallBack{
        /**
         * 确定
         */
        void confirm(String password);
    }

    public ComnPasswordDialog setNumber(int ecc,double usdt){
        return this;
    }
    
}
