package com.fzs.mine.ui;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fzs.comn.tools.UserTools;
import com.fzs.comn.tools.Util;
import com.fzs.comn.widget.imageview.ExpandImageView;
import com.fzs.mine.R;
import com.hzh.frame.ui.activity.BaseUI;

/**
 * 个人中心
 * */
@Route(path = "/mine/MineUserInfoUI")
public class MineUserInfoUI extends BaseUI {
	
	@Override
	protected void onCreateBase() {
		setContentView(R.layout.mine_ui_user_info);
		getTitleView().setContent("个人资料");
		((TextView)findViewById(R.id.phone)).setText(Util.phoneHide(UserTools.getInstance().getUser().getPhone()));
		((TextView)findViewById(R.id.nc)).setText(UserTools.getInstance().getUser().getNickName());
		((TextView)findViewById(R.id.inviter)).setText(UserTools.getInstance().getUser().getInviter());
		((TextView)findViewById(R.id.bankName)).setText(UserTools.getInstance().getUser().getBalnace());
        ((ExpandImageView)findViewById(R.id.head)).setImageURI(UserTools.getInstance().getUser().getHead());
	}
	
	/**
	 * 修改头像
	 * */
    public void head(View view){
        ARouter.getInstance().build("/mine/MineHeadUI").navigation();
    }
    
	/**
	 * 修改昵称
	 * */
    public void nick(View view){
        ARouter.getInstance().build("/mine/MineNickUI").navigation();
    }
    
	/**
	 * 修改登录密码/api/member/resetPassword
	 * */
    public void forgetPassword(View view){
        ARouter.getInstance().build("/login/ForgetPasswordUI").withString("title","修改登录密码").navigation();
    }

    /**
     * 修改支付密码/api/member/resetPassword
     * */
    public void payPassword(View view){
        ARouter.getInstance().build("/login/ModifyPayPasswordUI").withString("title","修改支付密码").navigation();
    }
    
	/**
	 * 修改支付宝信息
	 * */
    public void pay(View view){
    	Intent in=new Intent(MineUserInfoUI.this,MinePayUI.class);
		startActivity(in);
    }
    
	/**
	 * 修改开户行信息
	 * */
    public void bank(View view){
    	Intent in=new Intent(MineUserInfoUI.this,MinePayBankInfoUI.class);
		startActivity(in);
    }
    
    @Override
    protected void onRestart() {
    	super.onRestart();
		((TextView)findViewById(R.id.nc)).setText(UserTools.getInstance().getUser().getNickName());
		((TextView)findViewById(R.id.bankName)).setText(UserTools.getInstance().getUser().getBankName());
        ((ExpandImageView)findViewById(R.id.head)).setImageURI(UserTools.getInstance().getUser().getHead());
    }
	
}
