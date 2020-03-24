package com.fzs.mine.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fzs.comn.matisse.GifSizeFilter;
import com.fzs.comn.model.UploadImage;
import com.fzs.comn.tools.AndroidTools;
import com.fzs.comn.tools.ImageUtil;
import com.fzs.comn.tools.OtherLoginTools;
import com.fzs.comn.tools.UriUtil;
import com.fzs.comn.tools.UserTools;
import com.fzs.comn.tools.Util;
import com.fzs.mine.R;
import com.fzs.service.PayNotificationMonitorService;
import com.fzs.service.tools.ServicePowerTools;
import com.fzs.service.tools.ServiceTools;
import com.hzh.frame.comn.ItemDecoration.BaseGridSpacingItemDecoration;
import com.hzh.frame.comn.callback.CallBack;
import com.hzh.frame.comn.callback.HttpCallBack;
import com.hzh.frame.core.BaseSP;
import com.hzh.frame.core.HttpFrame.BaseHttp;
import com.hzh.frame.core.WsFrame.WsStatus;
import com.hzh.frame.ui.activity.BaseUI;
import com.hzh.frame.util.AndroidUtil;
import com.hzh.frame.widget.rxbus.MsgEvent;
import com.hzh.frame.widget.rxbus.RxBus;
import com.hzh.frame.widget.xdialog.XDialog2Button;
import com.hzh.frame.widget.xdialog.XDialogSubmit;
import com.hzh.frame.widget.xrecyclerview.BaseRecyclerAdapter;
import com.hzh.frame.widget.xrecyclerview.RecyclerViewHolder;
import com.yalantis.ucrop.UCrop;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.RequiresApi;

/**
 * 
 * */
@Route(path = "/mine/MineIsActivationUI")
public class MineIsActivationUI extends BaseUI {
    private XDialog2Button activation;//授权
    private Switch mSwitch;
    private String type = "";
    private ImageView aliIcon;
    private LinearLayout hidell;
    private LinearLayout servicell;
    private Button activationButton; //重新授权
    
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
	protected void onCreateBase() {
        setContentView(R.layout.mine_ui_isactivation);
        mSwitch = findViewById(R.id.switch_istrue);
        aliIcon = findViewById(R.id.aliIcon);
        hidell = findViewById(R.id.hidell);
        servicell = findViewById(R.id.servicell);
        activationButton = findViewById(R.id.activationButton);
        type = getIntent().getStringExtra("type");
        getTitleView().setContent("支付授权");
        initDialog();
        getType();
        mSwitch.setOnCheckedChangeListener(null);
        mSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            RxBus.getInstance().post(new MsgEvent(PayNotificationMonitorService.TAG, false));
            if(!UserTools.getInstance().getIsLogin()){
                new XDialog2Button(MineIsActivationUI.this)
                        .setMsg("请先登录")
                        .setConfirmName("立即登录","再看看")
                        .setCallback(new CallBack() {
                            @Override
                            public void onSuccess(Object object) {
                                UserTools.getInstance().jumpLoginUI();
                            }
                        }).show();
                return;
            }
            if(!UserTools.getInstance().getUser().getIsActivation()){
                activation
                        .setMsg("先授权,再开工")
                        .setCallback(new CallBack() {
                            @Override
                            public void onSuccess(Object object) {
                                OtherLoginTools.launchAlipayLogin(MineIsActivationUI.this, new OtherLoginTools.CallBack() {
                                    @Override
                                    public void onSuccess(String userId) {
                                        ((TextView)findViewById(R.id.activationCount)).setText(userId);
                                        upLoadUid(userId);
                                    }

                                    @Override
                                    public void onFail(String msg) {
                                        alert(msg);
                                    }
                                });
                            }
                        }).show();
                return;
            }
            if(isChecked){
                mSwitch.setChecked(false);
            }else {
                mSwitch.setChecked(true);
            }
            if (isChecked){
                //接单
                mSwitch.setChecked(false);
                if(!ServicePowerTools.isNotificationPower(MineIsActivationUI.this)){
                    new XDialog2Button(MineIsActivationUI.this)
                            .setMsg("请开启 '"+getResources().getString(R.string.service_label)+"' 通知使用权限")
                            .setConfirmName("立即前往","稍后")
                            .setCallback(new CallBack() {
                                @Override
                                public void onSuccess(Object object) {
                                    ServicePowerTools.openNotificationPower(MineIsActivationUI.this);//已授权,自己去关闭
                                    loadReceiptState();
                                }
                            }).show();
                }else{
                        RxBus.getInstance().post(new MsgEvent(PayNotificationMonitorService.TAG,true));
                    loadReceiptState();
                }
            }else {
                mSwitch.setChecked(true);
                if(ServicePowerTools.isNotificationPower(MineIsActivationUI.this)){
                    RxBus.getInstance().post(new MsgEvent(PayNotificationMonitorService.TAG,false));
                    loadReceiptState();
//                    loadReceiptState();
//                    new XDialog2Button(MineIsActivationUI.this)
//                            .setMsg("请关闭 '"+getResources().getString(R.string.service_label)+"' 通知使用权限")
//                            .setConfirmName("立即前往","稍后")
//                            .setCallback(new CallBack() {
//                                @Override
//                                public void onSuccess(Object object) {
//                                    ServicePowerTools.openNotificationPower(MineIsActivationUI.this);//已授权,自己去关闭
//                                }
//                            }).show();
                }else{
                    RxBus.getInstance().post(new MsgEvent(PayNotificationMonitorService.TAG,false));
                    loadReceiptState();
                }
            }
        });
        activationButton.setOnClickListener(v -> {
            OtherLoginTools.launchAlipayLogin(MineIsActivationUI.this, new OtherLoginTools.CallBack() {
                @Override
                public void onSuccess(String userId) {
//                    hidell.setVisibility(View.VISIBLE);
//                    servicell.setVisibility(View.VISIBLE);
                    ((TextView)findViewById(R.id.activationCount)).setText(userId);
//                    upLoadUid(userId);
                    setUid(userId);
                }

                @Override
                public void onFail(String msg) {
                    alert(msg);
                }
            });
        });
    }

    public void initDialog(){
        activation=new XDialog2Button(MineIsActivationUI.this).setMsg("授权支付宝用户信息").setConfirmName("立即前往","再看看");
    }
    
    private void getActivation(){
        if(!UserTools.getInstance().getUser().getIsActivation()){
            activation
                    .setMsg("先授权,再开工")
                    .setCallback(new CallBack() {
                        @Override
                        public void onSuccess(Object object) {
                            OtherLoginTools.launchAlipayLogin(MineIsActivationUI.this, new OtherLoginTools.CallBack() {
                                @Override
                                public void onSuccess(String userId) {
                                    hidell.setVisibility(View.VISIBLE);
                                    servicell.setVisibility(View.VISIBLE);
                                    ((TextView)findViewById(R.id.activationCount)).setText(userId);
                                    upLoadUid(userId);
                                }

                                @Override
                                public void onFail(String msg) {
                                    alert(msg);
                                }
                            });
                        }
                    }).show();
            return;
        }
	    
        if(UserTools.getInstance().getIsLogin()){
//            if(AndroidTools.isAliPayInstalled(MineIsActivationUI.this)){
//                OtherLoginTools.launchAlipayLogin(MineIsActivationUI.this, new OtherLoginTools.CallBack() {
//                    @Override
//                    public void onSuccess(String userId) {
//                        hidell.setVisibility(View.VISIBLE);
//                        servicell.setVisibility(View.VISIBLE);
//                        ((TextView)findViewById(R.id.activationCount)).setText(userId);
//                        upLoadUid(userId);
//                    }
//
//                    @Override
//                    public void onFail(String msg) {
//                        alert(msg);
//                    }
//                });
            
            
//                if (!UserTools.getInstance().getUser().getIsActivation()) {
//                    activation
//                            .setMsg("支付宝授权")
//                            .setCallback(new CallBack() {
//                                @Override
//                                public void onSuccess(Object object) {
//                                    OtherLoginTools.launchAlipayLogin(MineIsActivationUI.this, new OtherLoginTools.CallBack() {
//                                        @Override
//                                        public void onSuccess(String userId) {
//                                            upLoadUid(userId);
//                                        }
//
//                                        @Override
//                                        public void onFail(String msg) {
//                                            alert(msg);
//                                        }
//                                    });
//                                }
//                            }).show();
//                }else{
//                    activation
//                            .setMsg("重新授权")
//                            .setCallback(new CallBack() {
//                                @Override
//                                public void onSuccess(Object object) {
//                                    OtherLoginTools.launchAlipayLogin(MineIsActivationUI.this, new OtherLoginTools.CallBack() {
//                                        @Override
//                                        public void onSuccess(String userId) {
//                                            upLoadUid(userId);
//                                        }
//
//                                        @Override
//                                        public void onFail(String msg) {
//                                            alert(msg);
//                                        }
//                                    });
//                                }
//                            }).show();
//                }
//            }else{
//                alert("请先安装支付宝");
//            }
        }else{
            ARouter.getInstance().build("/login/LoginUI").navigation();
        }
    }

    //上传用户Uid
    public void upLoadUid(String uid){
        JSONObject params = new JSONObject();
        try {
            params.put("alipayUid", uid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        BaseHttp.getInstance().write(MineIsActivationUI.this,"member/completeInfo", params, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                if (200 == response.optInt("code")) {
                    hidell.setVisibility(View.VISIBLE);
                    servicell.setVisibility(View.VISIBLE);
                    UserTools.getInstance().updUser(UserTools.getInstance().getUser().setIsActivation(true));
                    ((TextView)findViewById(R.id.activationState)).setText("已授权");
                }else{
                    alert(response.optString("message"));
                }
            }
        }.setSubmit(new XDialogSubmit(MineIsActivationUI.this).setMsg("授权成功,正在授权中.")));
    }

    //上传用户Uid
    public void setUid(String uid){
        JSONObject params = new JSONObject();
        try {
            params.put("alipayUid", uid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        BaseHttp.getInstance().write(MineIsActivationUI.this,"member/completeInfo", params, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                if (200 == response.optInt("code")) {
//                    hidell.setVisibility(View.VISIBLE);
//                    servicell.setVisibility(View.VISIBLE);
                    UserTools.getInstance().updUser(UserTools.getInstance().getUser().setIsActivation(true));
                    ((TextView)findViewById(R.id.activationState)).setText("已授权");
                }else{
                    alert(response.optString("message"));
                }
            }
        }.setSubmit(new XDialogSubmit(MineIsActivationUI.this).setMsg("授权成功,正在授权中.")));
    }

    //加载接单状态
    @SuppressLint("NewApi")
    public void loadReceiptState(){
        if(!UserTools.getInstance().getUser().getIsActivation()){//支付宝未授权
            ((TextView) findViewById(R.id.stateName)).setText("休息中");
            ((TextView)findViewById(R.id.stateName)).setTextColor(ContextCompat.getColor(MineIsActivationUI.this, R.color.text_c_666666));
            mSwitch.setChecked(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                RxBus.getInstance().post(new MsgEvent(PayNotificationMonitorService.TAG,false));//开启服务,并关闭的前台通知
            }
        } else
        if(!ServicePowerTools.isNotificationPower(MineIsActivationUI.this)){//手机通知栏权限未授权
            ((TextView) findViewById(R.id.stateName)).setText("休息中");
            ((TextView)findViewById(R.id.stateName)).setTextColor(ContextCompat.getColor(MineIsActivationUI.this, R.color.text_c_666666));
            mSwitch.setChecked(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                RxBus.getInstance().post(new MsgEvent(PayNotificationMonitorService.TAG,false));//开启服务,并关闭的前台通知
            }
        }
        else if (Util.isEmpty(BaseSP.getInstance().getString("isServiceRunning"))){
            ((TextView) findViewById(R.id.stateName)).setText("休息中");
            mSwitch.setChecked(false);
            ((TextView)findViewById(R.id.stateName)).setTextColor(ContextCompat.getColor(MineIsActivationUI.this, R.color.text_c_666666));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                RxBus.getInstance().post(new MsgEvent(PayNotificationMonitorService.TAG,false));//开启服务,并关闭的前台通知
            }
        } else {
            if (BaseSP.getInstance().getString("isServiceRunning").equals("0")){
                ((TextView) findViewById(R.id.stateName)).setText("休息中");
                mSwitch.setChecked(false);
                ((TextView)findViewById(R.id.stateName)).setTextColor(ContextCompat.getColor(MineIsActivationUI.this, R.color.text_c_666666));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    RxBus.getInstance().post(new MsgEvent(PayNotificationMonitorService.TAG,false));//开启服务,并关闭的前台通知
                }
            }else {
                ((TextView) findViewById(R.id.stateName)).setText("接单中");
                mSwitch.setChecked(true);
                ((TextView)findViewById(R.id.stateName)).setTextColor(ContextCompat.getColor(MineIsActivationUI.this, R.color.application_color));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    RxBus.getInstance().post(new MsgEvent(PayNotificationMonitorService.TAG,true));//开启服务,并关闭的前台通知
                }
            }
        }
//        else 
//            if (PayNotificationMonitorService.baseWs != null){
//                String state = String.valueOf(PayNotificationMonitorService.baseWs.getState());
//                if (state.equals(WsStatus.DISCONNECTED_ACTIVE) || state.equals(WsStatus.DISCONNECTED_PASSIVE)){
//                    ((TextView) findViewById(R.id.stateName)).setText("休息中");
//                    mSwitch.setChecked(false);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
//                        RxBus.getInstance().post(new MsgEvent(PayNotificationMonitorService.TAG,false));//开启服务,并关闭的前台通知
//                    }
//                }else {
//                    ((TextView) findViewById(R.id.stateName)).setText("接单中");
//                    mSwitch.setChecked(true);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
//                        RxBus.getInstance().post(new MsgEvent(PayNotificationMonitorService.TAG,true));//开启服务,并关闭的前台通知
//                    }
//                }
//            }else {
//                ((TextView) findViewById(R.id.stateName)).setText("休息中");
//                mSwitch.setChecked(false);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
//                    RxBus.getInstance().post(new MsgEvent(PayNotificationMonitorService.TAG,false));//开启服务,并关闭的前台通知
//                }
//            }
//        else
//        if(!ServiceTools.isServiceRunning(MineIsActivationUI.this,PayNotificationMonitorService.class)){//监听服务未启动
//            ((TextView) findViewById(R.id.stateName)).setText("休息中");
//            mSwitch.setChecked(false);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
//                RxBus.getInstance().post(new MsgEvent(PayNotificationMonitorService.TAG,false));
//            }
//        } else {
//            ((TextView) findViewById(R.id.stateName)).setText("接单中");
//            mSwitch.setChecked(true);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
//                RxBus.getInstance().post(new MsgEvent(PayNotificationMonitorService.TAG,true));//开启服务,并显示的前台通知
//            }
//        }
    }
    
    private void getType(){
	    loadReceiptState();
        if (type.equals("1")){
            hidell.setVisibility(View.VISIBLE);
            servicell.setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.activationState)).setText("已授权");
            ((TextView)findViewById(R.id.activationCount)).setText(UserTools.getInstance().getUser().getAlipayUid());
            ((TextView)findViewById(R.id.activationState)).setTextColor(ContextCompat.getColor(MineIsActivationUI.this, R.color.application_color));
            ((TextView)findViewById(R.id.activationCount)).setTextColor(ContextCompat.getColor(MineIsActivationUI.this, R.color.application_color));
//            getActivation();
        }else {
            if (UserTools.getInstance().getIsLogin()){
                hidell.setVisibility(View.VISIBLE);
                servicell.setVisibility(View.GONE);
                ((TextView)findViewById(R.id.activationCount)).setText(UserTools.getInstance().getUser().getAlipayUid());
                if (UserTools.getInstance().getUser().getIsActivation()){
                    ((TextView)findViewById(R.id.activationState)).setText("已授权");
                    ((TextView)findViewById(R.id.activationState)).setTextColor(ContextCompat.getColor(MineIsActivationUI.this, R.color.application_color));
                    ((TextView)findViewById(R.id.activationCount)).setTextColor(ContextCompat.getColor(MineIsActivationUI.this, R.color.application_color));
                }else {
                    ((TextView)findViewById(R.id.activationState)).setText("未授权");
                    ((TextView)findViewById(R.id.activationState)).setTextColor(ContextCompat.getColor(MineIsActivationUI.this, R.color.text_c_666666));
                    ((TextView)findViewById(R.id.activationCount)).setTextColor(ContextCompat.getColor(MineIsActivationUI.this, R.color.application_color));
                }
            }else {
                ARouter.getInstance().build("/login/LoginUI").navigation();
            }
        }
    }
}
