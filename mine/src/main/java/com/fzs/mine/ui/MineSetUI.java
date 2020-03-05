package com.fzs.mine.ui;

import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fzs.comn.model.User;
import com.fzs.comn.tools.UserTools;
import com.fzs.comn.tools.Util;
import com.fzs.mine.R;
import com.fzs.service.PayNotificationMonitorService;
import com.fzs.service.tools.ServicePowerTools;
import com.fzs.service.tools.ServiceTools;
import com.hzh.frame.comn.callback.CallBack;
import com.hzh.frame.core.BaseSP;
import com.hzh.frame.ui.activity.BaseUI;
import com.hzh.frame.util.CloseAppUtil;
import com.hzh.frame.widget.rxbus.MsgEvent;
import com.hzh.frame.widget.rxbus.RxBus;
import com.hzh.frame.widget.xdialog.XDialog2Button;

/**
 * 设置
 * */
@Route(path = "/mine/MineSetUI")
public class MineSetUI extends BaseUI {
	Button button;
	TextView patch;
	LinearLayout editPassword;
	Switch switch_istrue;

	@Override
	protected void onCreateBase() {
		setContentView(R.layout.mine_ui_set);
        button=findViewById(R.id.button);
        patch=findViewById(R.id.patch);
        editPassword = findViewById(R.id.editPassword);
        switch_istrue = findViewById(R.id.switch_istrue);
		getTitleView().setContent("设置");
        if(UserTools.getInstance().getIsLogin()){
            button.setVisibility(View.VISIBLE);
        }else{
            button.setVisibility(View.GONE);
        }
        editPassword.setOnClickListener(v -> {
            ARouter.getInstance().build("/login/ForgetPasswordUI").navigation();
        });
//        if (!Util.isEmpty(BaseSP.getInstance().getString("switch_ischeck"))){
//            if (BaseSP.getInstance().getString("switch_ischeck").equals("1")){
//                switch_istrue.setChecked(true);
//            }else {
//                switch_istrue.setChecked(false);
//            }
//        }
        switch_istrue.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                //选中状态 可以做一些操作
                alert("选中");
                if (Util.isEmpty(BaseSP.getInstance().getString("switch_ischeck"))){
                    BaseSP.getInstance().put("switch_ischeck","1");
                }
            }else {
                //未选中状态 可以做一些操作
                alert("未选中");
                if (Util.isEmpty(BaseSP.getInstance().getString("switch_ischeck"))){
                    BaseSP.getInstance().put("switch_ischeck","0");
                }
            }
        });
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

    @Override
    protected void onRestart() {
        super.onRestart();
//        loadReceiptState();
    }

    //加载接单状态
    public void loadReceiptState(){
        if(ServicePowerTools.isNotificationPower(MineSetUI.this) && ServiceTools.isServiceRunning(MineSetUI.this,PayNotificationMonitorService.class)){
            switch_istrue.setChecked(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                RxBus.getInstance().post(new MsgEvent(PayNotificationMonitorService.TAG,true));//开启服务,并显示的前台通知
            }
        } else
        if(!ServicePowerTools.isNotificationPower(MineSetUI.this)){//未授权
            switch_istrue.setChecked(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                RxBus.getInstance().post(new MsgEvent(PayNotificationMonitorService.TAG,false));//开启服务,并关闭的前台通知
            }
        } else
        if(!ServiceTools.isServiceRunning(MineSetUI.this,PayNotificationMonitorService.class)){//未启动服务
            switch_istrue.setChecked(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                RxBus.getInstance().post(new MsgEvent(PayNotificationMonitorService.TAG,false));
            }
        }
    }

}
