package com.fzs.mine.ui;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fzs.comn.model.User;
import com.fzs.comn.tools.UserTools;
import com.fzs.mine.R;
import com.hzh.frame.callback.CallBack;
import com.hzh.frame.ui.activity.BaseUI;
import com.hzh.frame.util.CloseAppUtil;
import com.hzh.frame.widget.xdialog.XDialog2Button;

/**
 * 设置
 * */
@Route(path = "/mine/MineSetUI")
public class MineSetUI extends BaseUI {
	Button button;
	TextView patch;

	@Override
	protected void onCreateBase() {
		setContentView(R.layout.mine_ui_set);
        button=findViewById(R.id.button);
        patch=findViewById(R.id.patch);
		getTitleView().setContent("设置");
//        patch.setText("补丁版本:V 0");
        if(UserTools.getInstance().getIsLogin()){
            button.setVisibility(View.VISIBLE);
        }else{
            button.setVisibility(View.GONE);
        }
	}


	/**
	 * 修改登录密码
	 * */
	public void forgetPassword(View view) {
        ARouter.getInstance().build("/login/ForgetPasswordUI").withString("title","修改登录密码").navigation();
	}

    /**
     * 修改支付密码
     * */
    public void payPassword(View view) {
        ARouter.getInstance().build("/login/ModifyPayPasswordUI").withString("title","修改支付密码").navigation();
    }

    /**
     * 个人资料设置
     * */
    public void userInfo(View view) {
        ARouter.getInstance().build("/mine/MineUserInfoUI").navigation();
    }
	

	/**
	 * 关于我们
	 * */
	public void pay(View view) {
		Intent in = new Intent(MineSetUI.this, MineAboutUsUI.class);
		startActivity(in);
	}

	// 退出
	public void exit(View view) {
        new XDialog2Button(MineSetUI.this).setMsg("你确定要退出登录吗?").setCallback(new CallBack() {
					@Override
					public void onSuccess(Object object) {
                        CloseAppUtil.restartLogin(MineSetUI.this, "/login/LoginUI", User.class, () -> UserTools.getInstance().clear());
                    }
				}).show();
	}

}
