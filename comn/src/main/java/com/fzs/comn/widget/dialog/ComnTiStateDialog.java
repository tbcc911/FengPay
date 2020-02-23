package com.fzs.comn.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fzs.comn.R;
import com.hzh.frame.util.AndroidUtil;

/**
 * USDT提币
 * */
public class ComnTiStateDialog extends Dialog {
    
    public Activity activity;
    public TextView usdtIv;
    public TextView commissionIv;

    public ComnTiStateDialog(Activity activity) {
        super(activity, R.style.XSubmitDialog);
        this.activity=activity;
		LayoutInflater in = LayoutInflater.from(activity);
		View viewDialog = in.inflate(R.layout.comn_view_dialog_mine_ti_state, null);
        usdtIv=viewDialog.findViewById(R.id.usdt);
        commissionIv=viewDialog.findViewById(R.id.commission);
        viewDialog.findViewById(R.id.close).setOnClickListener(view -> dismiss());
        viewDialog.findViewById(R.id.tv_close).setOnClickListener(view -> dismiss());
		// 这里可以设置dialog的大小，当然也可以设置dialog title等
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(AndroidUtil.getWindowWith(activity) * 9 / 10, FrameLayout.LayoutParams.WRAP_CONTENT);
		setContentView(viewDialog,layoutParams);
		// 点击对话框外部取消对话框显示
		setCanceledOnTouchOutside(true);
		
	}
	
	public ComnTiStateDialog setUsdt(String usdt,float commission){
        usdtIv.setText(usdt+" USDT");
        commissionIv.setText(commission+" USDT");
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
    
}
