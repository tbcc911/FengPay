package com.fzs.mine.ui;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fzs.comn.ComnConfig;
import com.fzs.mine.R;
import com.hzh.frame.ui.activity.BaseUI;

/**
 * 划广告
 * */
@Route(path = "/mine/MineSlipUI")
public class MineSlipUI extends BaseUI {
	Button button;
	TextView phone;
    

	@Override
	protected void onCreateBase() {
		setContentView(R.layout.mine_ui_slip);
        button=findViewById(R.id.button);
        phone=findViewById(R.id.phone);
		getTitleView().setContent("划广告");
        phone.setText(ComnConfig.phone);
	}


	/**
	 * 拨打电话
	 * */
	public void pay(View view) {
        //拨打电话
        Intent in1 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+ComnConfig.phone));
        in1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(in1);
	}

	// 发布广告
	public void slip(View view) {
        ARouter.getInstance().build("/ad/ClientIndexUI").navigation();
	}
    
}
