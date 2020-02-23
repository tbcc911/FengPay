package com.fzs.comn.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fzs.comn.R;
import com.fzs.comn.tools.InitTools;
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
 * 兑换
 * */
public class ComnExchangeDialog extends Dialog {

    public Activity activity;
    public LinearLayout transformation;//切换
    public TextView transformationContent;//切换内容
    public TextView exchangeRate;//汇率
    public TextView active;
    public TextView active2;
    public TextView USDT;
    public TextView ejinbi;
    public int type=1;//1:tbcc 2:USDT
    public CallBack callBack;
    public float hl_u_c ;
    public float hl_p_u;

    public float usdtExchangetbccRate;
    public float EVEpayExchangetbccRate;
//    public float goldStandard;
//    public float tbccExchangeUsdtMultiple;
    float value;

    public ComnExchangeDialog(Activity activity) {
        super(activity, R.style.XSubmitDialog);
        this.activity=activity;
        LayoutInflater in = LayoutInflater.from(activity);
        View viewDialog = in.inflate(R.layout.comn_view_dialog_exchange, null);
        USDT = viewDialog.findViewById(R.id.USDT);
        ejinbi = viewDialog.findViewById(R.id.ejinbi);
        transformation=viewDialog.findViewById(R.id.transformation);
        transformationContent=viewDialog.findViewById(R.id.transformationContent);
        exchangeRate=viewDialog.findViewById(R.id.exchangeRate);
        active=viewDialog.findViewById(R.id.active);
        active2=viewDialog.findViewById(R.id.active2);
        TextView submit = viewDialog.findViewById(R.id.submit);
        RxView.clicks(submit).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(new Consumer<Unit>() {
            @Override
            public void accept(Unit unit) throws Exception {
                new ComnPasswordDialog(activity).setCallBack(password -> submit(password)).show();
            }
        });
            usdtExchangetbccRate = Float.parseFloat(InitTools.getInstance().getConfig().getUsdtExchangeTbccRate().toString());
            EVEpayExchangetbccRate = Float.parseFloat(InitTools.getInstance().getConfig().getEVEpayExchangeTbccRate().toString());
//            goldStandard = Float.parseFloat(UserTools.getInstance().getUser().getGoldStandard().toString());
//            tbccExchangeUsdtMultiple = Float.parseFloat(UserTools.getInstance().getUser().gettbccExchangeUsdtMultiple().toString());
            exchangeRate.setText("当前汇率：1 USDT = " + usdtExchangetbccRate + " TBCC");

        transformation.setOnClickListener(new TransformationOnClick());
        viewDialog.findViewById(R.id.close).setOnClickListener(view -> dismiss());
        // 这里可以设置dialog的大小，当然也可以设置dialog title等
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(AndroidUtil.getWindowWith(activity) * 9 / 10, FrameLayout.LayoutParams.WRAP_CONTENT);
        setContentView(viewDialog,layoutParams);
        // 点击对话框外部取消对话框显示
        setCanceledOnTouchOutside(true);
        init();
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

    public void init(){
        
        USDT.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                if (!Util.isEmpty(USDT.getText().toString())) {
                    double eccNumber=Double.parseDouble(USDT.getText().toString().trim());
                    if(type == 2){
                        ejinbi.setText((eccNumber*usdtExchangetbccRate)+"");
                    } else{
                        ejinbi.setText((((float)eccNumber*EVEpayExchangetbccRate))+"");
                    }
                }else{
                    ejinbi.setText("");
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

    class TransformationOnClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(2 == type){
                type=1;
                if (UserTools.getInstance(activity).getIsLogin()) {
                    exchangeRate.setText("当前汇率：1 USDT = " + usdtExchangetbccRate + " TBCC");
                }
                transformationContent.setText("切换EVEpay");
                active.setText("USDT");
//                active2.setText("USDT");
            }else{
                type=2;
                if (UserTools.getInstance(activity).getIsLogin()) {
                    exchangeRate.setText("当前汇率：1 EVEpay = " + EVEpayExchangetbccRate + " TBCC");
                }
                transformationContent.setText("切换USDT");
                active.setText("EVEpay");
//                active2.setText("upay");
            }
        }
    }

    public ComnExchangeDialog alert(){
        show();
        return this;
    }

    //兑换
    public void submit(String password){
        if(Util.isEmpty(USDT.getText().toString().trim())){
            if (type == 1){
                BaseToast.getInstance().setMsg("请输入tbcc").show();
            }else if (type == 2){
                BaseToast.getInstance().setMsg("请输入USDT").show();
            }
            return;
        }
        
        dismiss();
        if (callBack != null){
            callBack.confirm();
        }
//        if(Util.isEmpty(ejinbi.getText().toString().trim())){
//            BaseToast.getInstance().setMsg("请输入E金币").show();
//            return;
//        }
        JSONObject params = new JSONObject();
        try {
            //兑换类型: 0->USDT兑换tbcc; 1->EVEpay兑换tbcc
            if (type == 1){
                params.put("exchangeType", 0);
            }else if (type == 2){
                params.put("exchangeType", 1);
            }
            params.put("number", USDT.getText().toString().trim());
            params.put("payPassword", password);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        BaseHttp.getInstance().write("finance/exchange",params, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                if (200 == response.optInt("code")) {
                    BaseToast.getInstance().setMsg("兑换成功").show();
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

    public ComnExchangeDialog setCallBack(CallBack callBack) {
        this.callBack = callBack;
        return this;
    }
}
