package com.fzs.mine.ui;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fzs.comn.model.User;
import com.fzs.comn.tools.UserTools;
import com.fzs.mine.R;
import com.hzh.frame.comn.callback.HttpCallBack;
import com.hzh.frame.core.HttpFrame.BaseHttp;
import com.hzh.frame.ui.activity.BaseUI;
import com.hzh.frame.widget.xdialog.XDialogSubmit;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 设置昵称
 * */
@Route(path = "/mine/MineNickUI")
public class MineNickUI extends BaseUI {

	private EditText userName;
	private XDialogSubmit submitDialog;

	@Override
	protected void onCreateBase() {
		submitDialog=new XDialogSubmit(this);
		setContentView(R.layout.mine_ui_core_nick);
		getTitleView().setContent("昵称");
		getTitleView().setRightContent("保存");
		getTitleView().setRightOnclickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				final String nc=userName.getText().toString().trim();
				if(nc.length()<=0 || nc.length()>10){
					alert("昵称长度在1-10个字符之内");
					return;
				}
				JSONObject params=new JSONObject();
				try {
					params.put("nickname", nc);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				submitDialog.show();
				BaseHttp.getInstance().write(MineNickUI.this,"member/updateNickname", params, new HttpCallBack(){
					@Override
					public void onSuccess(JSONObject response) {
						submitDialog.dismiss();
						int code =response.optInt("code");
                        String msg=response.optString("message");
							if("200".equals(code) || code == 200){
                                User user=UserTools.getInstance().getUser();
								user.setNickName(nc);
								UserTools.getInstance().updUser(user);
								userName.setText(nc);
								alert(msg);
								finish();
							}else{
								alert(msg);
							}
					}
					@Override
					public void onFail() {
					    submitDialog.dismiss();
					    alert("修改失败");
					}
				});
			}
		});
		userName=(EditText) findViewById(R.id.userName);
		userName.setText(UserTools.getInstance().getUser().getNickName());
//        userName.setSelection(UserTools.getInstance().getUser().getNickName().length());
	}

}
