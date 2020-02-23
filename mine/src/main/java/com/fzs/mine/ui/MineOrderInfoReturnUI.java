package com.fzs.mine.ui;

import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.fzs.comn.tools.InitTools;
import com.fzs.mine.R;
import com.hzh.frame.ui.activity.BaseUI;
import com.hzh.frame.util.AndroidUtil;

/**
 * 退货须知
 * */
public class MineOrderInfoReturnUI extends BaseUI {
	
	private ImageView iconTop;
	private TextView phone;

	@Override
	protected void onCreateBase() {
		setContentView(R.layout.mine_ui_order_info_return);
        iconTop=findViewById(R.id.iconTop);
        phone = findViewById(R.id.phone);
		getTitleView().setContent("退货");
		LayoutParams lp=new LayoutParams(AndroidUtil.getWindowWith()/12*9, LayoutParams.WRAP_CONTENT);
        phone.setText(InitTools.getInstance().getConfig().getCustomerTelephone());
		lp.gravity=Gravity.CENTER_HORIZONTAL;
		iconTop.setLayoutParams(lp);
		iconTop.setAdjustViewBounds(true);
		
		findViewById(R.id.phoneLin).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent in1 = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+ phone.getText().toString().trim()));
				in1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(in1);
			}
		});
		findViewById(R.id.phoneIcon).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent in1 = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+ phone.getText().toString().trim()));
				in1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(in1);
			}
		});
	}
}
