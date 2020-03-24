package com.fzs.comn.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fzs.comn.R;
import com.fzs.comn.model.User;
import com.fzs.comn.tools.InitTools;
import com.hzh.frame.comn.annotation.SelectTable;
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


public class ComnTiDialog extends Dialog {
    /**本地登录用户*/
    @SelectTable(table=User.class) User user;
    public Activity activity;
    public TextView usdt;
    public TextView address;
    public TextView usdtNumber;
    public Button comit;
    public TextView usdt_tb;
    float sxf = Float.parseFloat("0.05");
    public ComnTiStateDialog mMineUSDTWithdrawMoneyStateDialog;

    public ComnTiDialog(Activity activity) {
        
        super(activity, R.style.XSubmitDialog);
        this.activity=activity;
        mMineUSDTWithdrawMoneyStateDialog=new ComnTiStateDialog(activity);
        LayoutInflater in = LayoutInflater.from(activity);
        View viewDialog = in.inflate(R.layout.comn_view_dialog_ti, null);
        usdt=viewDialog.findViewById(R.id.usdt);
        address=viewDialog.findViewById(R.id.address);
        usdtNumber=viewDialog.findViewById(R.id.usdtNumber);
        comit=viewDialog.findViewById(R.id.comit);
        usdt_tb = viewDialog.findViewById(R.id.usdt_tb);
        JSONObject params = new JSONObject();
        if (InitTools.getInstance().getConfig().getWithdrawPoundage() != null && InitTools.getInstance().getConfig().getWithdrawPoundage().length() > 0){
            sxf = Float.parseFloat(InitTools.getInstance().getConfig().getWithdrawPoundage());
            usdt_tb.setText("当前手续费为"+sxf + "%");
        }
        viewDialog.findViewById(R.id.close).setOnClickListener(view -> dismiss());
        // 这里可以设置dialog的大小，当然也可以设置dialog title等
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(AndroidUtil.getWindowWith(activity) * 9 / 10, FrameLayout.LayoutParams.WRAP_CONTENT);
        setContentView(viewDialog,layoutParams);
        // 点击对话框外部取消对话框显示
        setCanceledOnTouchOutside(true);

        RxView.clicks(comit).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(new Consumer<Unit>() {
            @Override
            public void accept(Unit unit) throws Exception {
                new ComnPasswordDialog(activity).setCallBack(password -> submit(password)).show();
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

    //提币
    public void submit(String password){
        JSONObject params = new JSONObject();
        try {
            params.put("usdtAddress", address.getText().toString().trim());
            params.put("number", usdtNumber.getText().toString().trim());
            params.put("payPassword", password);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        BaseHttp.getInstance().write(activity,"finance/withdrawApply", params, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                
                if (200 == response.optInt("code")) {
                    super.onSuccess(response);
                    dismiss();
                    mMineUSDTWithdrawMoneyStateDialog.setUsdt(usdtNumber.getText().toString().trim(),Float.parseFloat(usdtNumber.getText().toString().trim())*sxf/100).show();
                    address.setText("");
                    usdtNumber.setText("");
                }else {
                    BaseToast.getInstance().setMsg(response.optString("message")).show();
                    address.setText("");
                    usdtNumber.setText("");
                }
            }
        });
    }
}
