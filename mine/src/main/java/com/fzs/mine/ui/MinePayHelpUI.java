package com.fzs.mine.ui;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.fzs.mine.R;
import com.hzh.frame.ui.activity.BaseUI;
import com.hzh.frame.util.Util;

/**
 * 帮朋友付款
 * */
public class MinePayHelpUI extends BaseUI implements OnClickListener{

	EditText orderCode;
	Button submit;

	@Override
	protected void onCreateBase() {
		setContentView(R.layout.mine_ui_pay_help);
		orderCode=findViewById(R.id.orderCode);
        submit=findViewById(R.id.submit);
		getTitleView().setContent("帮朋友付款");
		submit.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
        if(Util.isEmpty(orderCode.getText().toString().trim())){
            alert("请输入订单号");
            return;
        } 
        
		if(R.id.submit==view.getId()){
			Intent in=new Intent(this,MinePayHelpInfoUI.class);
			in.putExtra("orderCode", orderCode.getText().toString().trim());
			startActivity(in);
		}
	}

}
