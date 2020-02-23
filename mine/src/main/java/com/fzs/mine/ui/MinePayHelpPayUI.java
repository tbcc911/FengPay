package com.fzs.mine.ui;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fzs.comn.tools.Util;
import com.fzs.mine.R;
import com.hzh.frame.comn.callback.HttpCallBack;
import com.hzh.frame.core.HttpFrame.BaseHttp;
import com.hzh.frame.ui.activity.BaseUI;
import com.hzh.frame.widget.xdialog.XDialogSubmit;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 代支付
 * */
public class MinePayHelpPayUI extends BaseUI implements OnClickListener{
	TextView money;
	TextView all;
	ImageView close;
	TextView msg;
	TextView orderCode;
	TextView payType;
	TextView moneyX;
	EditText et_money;
	Button button;
	public double payable=0.00,moneyXNum=0.00;
	public XDialogSubmit submitDialog;


    public void findViewById(){
        money=findViewById(R.id.money);
        all=findViewById(R.id.all);
        close=findViewById(R.id.close);
        msg=findViewById(R.id.msg);
        orderCode=findViewById(R.id.orderCode);
        payType=findViewById(R.id.payType);
        moneyX=findViewById(R.id.moneyX);
        et_money=findViewById(R.id.et_money);
        button=findViewById(R.id.button);
    }
	
	@Override
	protected void onCreateBase() {
		submitDialog=new XDialogSubmit(this).setMsg("支付中");
		setContentView(R.layout.mine_ui_pay_help_pay);
        findViewById();
		showLoding();
		getTitleView().setContent("支付");
		all.setOnClickListener(this);
		button.setOnClickListener(this);
		close.setOnClickListener(this);
		et_money.requestFocus();
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		et_money.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {}
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,int arg2, int arg3) {}
			@Override
			public void afterTextChanged(Editable edit) {
				if (!Util.isEmpty(et_money.getText())) {
					close.setVisibility(View.VISIBLE);
				} else {
					close.setVisibility(View.GONE);
				}
				submitIf();
			}
		});	 
		orderCode.setText(getIntent().getStringExtra("orderCode"));
		loadData();
	}
	
	@Override
	public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.all) {
            if (payable > moneyXNum) {
                et_money.setText(Util.doubleFormat(moneyXNum));
                et_money.setSelection(Util.doubleFormat(moneyXNum).length());
            } else {
                et_money.setText(Util.doubleFormat(payable));
                et_money.setSelection(Util.doubleFormat(payable).length());
            }
        } else if (id == R.id.button) {
            submit();
        } else if (id == R.id.close) {
            et_money.setText("");
        }
	}
	
	public void loadData() {
		JSONObject params=new JSONObject();
		try {
			params.put("order_id", getIntent().getStringExtra("orderCode"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		BaseHttp.getInstance().query("3032", params, new HttpCallBack(){
			@Override
			public void onSuccess(JSONObject response) {
				if(1==response.optInt("result")){
					JSONObject data=response.optJSONObject("data");
					if(1==data.optInt("code")){
						moneyXNum=data.optDouble("totalPrice");
						moneyX.setText(Util.doubleFormat(moneyXNum)+"元");
						payType.setText(data.optString("payment_Method"));
						payable=data.optDouble("payable");
						money.setText("可支付金额"+Util.doubleFormat(payable)+"元");
						submitIf();
					}else{
					}
				}else{
				}
				dismissLoding();
			}
			
			@Override
			public void onFail() {
				showLodingFail();
			}
		});
	}
	
	public boolean submitIf(){
		if (payable<=0) {
			msg.setVisibility(View.VISIBLE);
			msg.setText("可支付金额不足");
			button.setClickable(false);
			button.setBackgroundResource(R.drawable.button_disabled);
			return false;
		}
		if (Util.isEmpty(et_money.getText())) {
			msg.setVisibility(View.VISIBLE);
			msg.setText("请输入支付金额");
			button.setClickable(false);
			button.setBackgroundResource(R.drawable.button_disabled);
			return false;
		}
		if (Double.parseDouble(et_money.getText().toString())<=0) {
			msg.setVisibility(View.VISIBLE);
			msg.setText("支付金额不能小于0");
			button.setClickable(false);
			button.setBackgroundResource(R.drawable.button_disabled);
			return false;
		}
		if (Double.parseDouble(et_money.getText().toString())>payable) {
			msg.setVisibility(View.VISIBLE);
			msg.setText("支付金额不能大于可支付金额");
			button.setClickable(false);
			button.setBackgroundResource(R.drawable.button_disabled);
			return false;
		}
		if (Double.parseDouble(et_money.getText().toString())>moneyXNum) {
			msg.setVisibility(View.VISIBLE);
			msg.setText("支付金额不能大于需付款");
			button.setClickable(false);
			button.setBackgroundResource(R.drawable.button_disabled);
			return false;
		}
		msg.setVisibility(View.GONE);
		msg.setText("");
		button.setClickable(true);
		button.setBackgroundResource(R.drawable.mine_button_select);
		return true;
	}

	/**
	 * 确认支付
	 * */
	public void submit() {
		if(submitIf()){
			JSONObject params=new JSONObject();
			try {
				params.put("PAY_MONEY", et_money.getText().toString().trim());
				params.put("ID", getIntent().getStringExtra("orderId"));
				params.put("ORDER_ID", getIntent().getStringExtra("orderCode"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			submitDialog.show();
			BaseHttp.getInstance().write(this,"4015", params, new HttpCallBack(){
				@Override
				public void onSuccess(JSONObject response) {
					submitDialog.dismiss();
					if(1==response.optInt("result")){
						JSONObject data=response.optJSONObject("data");
						if(1==data.optInt("code")){
							msg.setVisibility(View.GONE);
							msg.setText("");
                            ARouter.getInstance().build("/pay/PaymentSuccessUI").navigation();
						}else{
							msg.setVisibility(View.VISIBLE);
							msg.setText(data.optString("msg"));
                            ARouter.getInstance().build("/pay/PaymentFaiUI").navigation();
						}
					}else{
						msg.setVisibility(View.VISIBLE);
						msg.setText("支付失败");
                        ARouter.getInstance().build("/pay/PaymentFaiUI").navigation();
					}
				}
				@Override
				public void onFail() {
					submitDialog.dismiss();
                    ARouter.getInstance().build("/pay/PaymentFaiUI").navigation();
				}
			});
		}
	}


}
