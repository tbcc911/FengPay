package com.fzs.mine.ui;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.fzs.comn.model.User;
import com.fzs.comn.tools.UserTools;
import com.fzs.comn.tools.Util;
import com.fzs.mine.R;
import com.hzh.frame.comn.callback.HttpCallBack;
import com.hzh.frame.core.HttpFrame.BaseHttp;
import com.hzh.frame.ui.activity.BaseUI;
import com.hzh.frame.widget.xdialog.XDialogSubmit;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 设置支付宝信息
 * */
public class MinePayUI extends BaseUI {
	
	/**true:添加  false:显示*/
	private boolean flag=true;
	private EditText alipayName;
	private EditText alipayAccount;
	private TextView msg;
	private TextView phone;
	private XDialogSubmit submitDialog;
	
	@Override
	protected void onCreateBase() {
		submitDialog=new XDialogSubmit(this);
		if(Util.isEmpty(UserTools.getInstance().getUser().getAlipayName()) || Util.isEmpty(UserTools.getInstance().getUser().getAlipayAcount())){
			//添加
			flag=true;
		}else{
			//显示
			flag=false;
		}
		setContentView(R.layout.mine_ui_core_pay);
		getTitleView().setContent("支付宝信息");
		if(flag){
			getTitleView().setRightContent("保存");
		}else{
			getTitleView().setRightContent("");
		}
		getTitleView().setRightOnclickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				final String name=alipayName.getText().toString().trim();
				final String account=alipayAccount.getText().toString().trim();
				if(name.length()<=0){
					alert("姓名不能为空");
					return;
				}
				if(account.length()<=0){
					alert("账号不能为空");
					return;
				}
				JSONObject params=new JSONObject();
				try {
					params.put("ALIPAY_ACCOUNT", account);
					params.put("ALIPAY_NAME", name);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				submitDialog.show();
				BaseHttp.getInstance().write(MinePayUI.this,"2012", params, new HttpCallBack(){
					@Override
					public void onSuccess(JSONObject response) {
						submitDialog.dismiss();
						System.out.println(response.toString());
						if("1".equals(response.optString("result"))){
							JSONObject data=response.optJSONObject("data");
							if("1".equals(data.optString("code"))){
							    User user = UserTools.getInstance().getUser();
								user.setAlipayName(name);
								user.setAlipayAcount(account);
								user.save();
								alert(data.optString("msg"));
							}else{
								alert(data.optString("msg"));
							}
						}else{
							alert("添加失败");
						}
					}
					@Override
					public void onFail() {
					    submitDialog.dismiss();
					    alert("添加失败");
					}
				});
			}
		});
		msg=(TextView) findViewById(R.id.msg);
		phone=(TextView) findViewById(R.id.phone);
		alipayName=(EditText) findViewById(R.id.alipayName);
		alipayAccount=(EditText) findViewById(R.id.alipayAccount);
		if(flag){
			msg.setText("你设置的支付宝账号和姓名不匹配将不能报销");
			phone.setVisibility(View.GONE);
		}else{
			alipayName.setText(UserTools.getInstance().getUser().getAlipayName());
			alipayAccount.setText(UserTools.getInstance().getUser().getAlipayAcount());
			alipayName.setFocusable(false);
			alipayName.setEnabled(false);
			alipayAccount.setFocusable(false);
			alipayAccount.setEnabled(false);
			msg.setText("若支付宝账号填写错误请联系客服:");
			phone.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 拨打电话
	 * */
	public void goPhone(View view){
		Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + phone.getText().toString().trim()));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

}
