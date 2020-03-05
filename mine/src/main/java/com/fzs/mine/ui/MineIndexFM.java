package com.fzs.mine.ui;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fzs.comn.model.User;
import com.fzs.comn.tools.AndroidTools;
import com.fzs.comn.tools.InitTools;
import com.fzs.comn.tools.OtherLoginTools;
import com.fzs.comn.tools.UserTools;
import com.fzs.comn.tools.Util;
import com.fzs.comn.widget.imageview.ExpandImageView;
import com.fzs.mine.R;
import com.fzs.service.PayNotificationMonitorService;
import com.fzs.service.tools.ServicePowerTools;
import com.fzs.service.tools.ServiceTools;
import com.google.gson.Gson;
import com.hzh.frame.BaseInitData;
import com.hzh.frame.comn.callback.CallBack;
import com.hzh.frame.comn.callback.HttpCallBack;
import com.hzh.frame.comn.model.BaseRadio;
import com.hzh.frame.core.BaseSP;
import com.hzh.frame.core.HttpFrame.BaseHttp;
import com.hzh.frame.ui.fragment.BaseFM;
import com.hzh.frame.util.AndroidUtil;
import com.hzh.frame.widget.rxbus.MsgEvent;
import com.hzh.frame.widget.rxbus.RxBus;
import com.hzh.frame.widget.toast.BaseToast;
import com.hzh.frame.widget.xdialog.XDialog2Button;
import com.hzh.frame.widget.xdialog.XDialogRadio;
import com.hzh.frame.widget.xdialog.XDialogSubmit;
import com.hzh.frame.widget.xdialog.XDialogUpdateAPP;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.RequiresApi;
import cn.bertsir.zbar.ScanConfig;
import okhttp3.WebSocket;


/**
 * 个人中心
 */
public class MineIndexFM extends BaseFM implements OnClickListener {
    public static final String TAG="MineIndexFM";
    List<HashMap<String, Object>> list=new ArrayList<>();
    private WebSocket mSocket;
    private XDialog2Button activation;//激活
    
    @Override
    public boolean setTitleIsShow() {
        return false;
    }

    @Override
    protected void onCreateBase() {
        setContentView(R.layout.mine_ui_index);
        findViewById(R.id.statusBar).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,AndroidUtil.getStatusBarHeight()));
        findViewById(R.id.head).setOnClickListener(this);
        findViewById(R.id.usdt).setOnClickListener(this);
        findViewById(R.id.addressManager).setOnClickListener(this);
        findViewById(R.id.systemSetup).setOnClickListener(this);
        findViewById(R.id.message).setOnClickListener(this);
        findViewById(R.id.mineTurn).setOnClickListener(this);
        findViewById(R.id.mine_sao).setOnClickListener(this);
        findViewById(R.id.mine_shou).setOnClickListener(this);
        findViewById(R.id.mineRecharge).setOnClickListener(this);
        findViewById(R.id.status).setOnClickListener(this);
        findViewById(R.id.mine_feedback).setOnClickListener(this);
        findViewById(R.id.mine_team).setOnClickListener(this);
        findViewById(R.id.updata).setOnClickListener(this);
        findViewById(R.id.usdtLL).setOnClickListener(this);
        findViewById(R.id.mine_share).setOnClickListener(this);
        findViewById(R.id.mine_transaction).setOnClickListener(this);
        findViewById(R.id.mine_share).setOnClickListener(this);
        findViewById(R.id.activationState).setOnClickListener(this);
        findViewById(R.id.state).setOnClickListener(this);
        initDialog();
        loadUserInfo();
    }
    
    public void initDialog(){
        activation=new XDialog2Button(getActivity()).setMsg("授权支付宝用户信息").setConfirmName("立即前往","再看看");
    }


    @Override
    public void onClick(View view) {
        Bundle bd = new Bundle();
        int id = view.getId();
        if (id == R.id.head || id == R.id.status) {//用户信息
            ARouter.getInstance().build("/mine/MineUserInfoUI").with(bd).navigation();
        } else
        if (id == R.id.state) { //状态
            if(!UserTools.getInstance().getIsLogin()){
                new XDialog2Button(getActivity())
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
            List<BaseRadio> list = new ArrayList<>();
            list.add(new BaseRadio().setName("接单").setId("1").setChecked(true));
            list.add(new BaseRadio().setName("休息").setId("2").setChecked(false));
            new XDialogRadio<>()
                    .setData(list)
                    .setTitle("切换状态")
                    .setRadioButtonMinWidth(AndroidUtil.getWindowWith() / 10.0 * 5)
                    .setCallBack(new CallBack<BaseRadio>() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
                        @Override
                        public void onSuccess(BaseRadio item) {
                            if("1".equals(item.getId())){//接单
                                if(!ServicePowerTools.isNotificationPower(getActivity())){
                                    new XDialog2Button(getActivity())
                                            .setMsg("请开启 '"+getResources().getString(R.string.service_label)+"' 通知使用权限")
                                            .setConfirmName("立即前往","稍后")
                                            .setCallback(new CallBack() {
                                                @Override
                                                public void onSuccess(Object object) {
                                                    ServicePowerTools.openNotificationPower(getActivity());//已授权,自己去关闭
                                                }
                                            }).show();
                                }else{
                                    RxBus.getInstance().post(new MsgEvent(PayNotificationMonitorService.TAG,true));
                                }
                            } else
                            if("2".equals(item.getId())){//休息
                                if(ServicePowerTools.isNotificationPower(getActivity())){
                                    new XDialog2Button(getActivity())
                                            .setMsg("请关闭 '"+getResources().getString(R.string.service_label)+"' 通知使用权限")
                                            .setConfirmName("立即前往","稍后")
                                            .setCallback(new CallBack() {
                                                @Override
                                                public void onSuccess(Object object) {
                                                    ServicePowerTools.openNotificationPower(getActivity());//已授权,自己去关闭
                                                }
                                            }).show();
                                }else{
                                    RxBus.getInstance().post(new MsgEvent(PayNotificationMonitorService.TAG,false));
                                }
                            }
                        }
                    })
                    .show(getFragmentManager());
        } else
        if (id == R.id.activationState) {// 激活
            if(UserTools.getInstance().getIsLogin()){
                if(AndroidTools.isAliPayInstalled(getActivity())){
                    if (!UserTools.getInstance().getUser().getIsActivation()) {
                        activation
                                .setMsg("支付宝授权激活")
                                .setCallback(new CallBack() {
                                    @Override
                                    public void onSuccess(Object object) {
                                        OtherLoginTools.launchAlipayLogin(getActivity(), new OtherLoginTools.CallBack() {
                                            @Override
                                            public void onSuccess(String userId) {
                                                upLoadUid(userId);
                                            }

                                            @Override
                                            public void onFail(String msg) {}
                                        });
                                    }
                                }).show();
                    }else{
                        activation
                                .setMsg("重新授权激活")
                                .setCallback(new CallBack() {
                                    @Override
                                    public void onSuccess(Object object) {
                                        OtherLoginTools.launchAlipayLogin(getActivity(), new OtherLoginTools.CallBack() {
                                            @Override
                                            public void onSuccess(String userId) {
                                                upLoadUid(userId);
                                            }

                                            @Override
                                            public void onFail(String msg) {}
                                        });
                                    }
                                }).show();
                    }
                }else{
                    alert("请先安装支付宝");
                }
            }else{
                ARouter.getInstance().build("/login/LoginUI").navigation();
            }
        } else 
        if (id == R.id.addressManager) {// 我的地址
            bd.putBoolean("onClickListItemIsReturn", false);
            ARouter.getInstance().build("/store/StoreAddressManageLUI").with(bd).navigation();
        } else 
        if (id == R.id.systemSetup) {// 系统设置
            ARouter.getInstance().build("/mine/MineSetUI").with(bd).navigation();
        } else 
        if (id == R.id.message) {// 消息
            ARouter.getInstance().build("/mine/MineMessageRUI").with(bd).navigation();
        }else 
        if (id == R.id.mineRecharge) {//充值
//            if (isLoginAndActivation()) {
            List<BaseRadio> list = new ArrayList<>();
            list.add(new BaseRadio().setName("银行卡").setId("0").setChecked(true));
            list.add(new BaseRadio().setName("支付宝").setId("1").setChecked(false));
            list.add(new BaseRadio().setName("微信").setId("2").setChecked(false));
            list.add(new BaseRadio().setName("USDT").setId("3").setChecked(false));
                new XDialogRadio<>()
                        .setData(list)
                        .setTitle("选择充值方式")
                        .setOkAndNoName("去支付","再想想")
                        .setRadioButtonMinWidth(AndroidUtil.getWindowWith() / 10.0 * 8)
                        .setCallBack(new CallBack<BaseRadio>() {
                            @Override
                            public void onSuccess(BaseRadio baseRadio) {
//                                alert(baseRadio.getName());
                                if (baseRadio.getId().equals("0")){
                                    ARouter.getInstance().build("/mine/MinePayBankUI")
                                            .withString("rechargeType",baseRadio.getId())
                                            .navigation();
                                }else if (baseRadio.getId().equals("1")){
                                    ARouter.getInstance().build("/mine/MinePayAliOrWxUI")
                                            .withString("rechargeType",baseRadio.getId())
                                            .withString("payTitle","支付宝充值")
                                            .navigation();
                                }else if (baseRadio.getId().equals("2")){
                                    ARouter.getInstance().build("/mine/MinePayAliOrWxUI")
                                            .withString("rechargeType",baseRadio.getId())
                                            .withString("payTitle","微信充值")
                                            .navigation();
                                }
                            }
                        })
                        .show(getFragmentManager());
//            }
        } else 
        if (id == R.id.mineTurn) {//转账
            if (isLoginAndActivation()) {
                ARouter.getInstance().build("/chain/ChainTurnPriceUI").with(bd).navigation();
            }
        } else 
        if (id == R.id.mine_sao) {//扫一扫
            ScanConfig.getInstance().start(getActivity(), result -> {
                ARouter.getInstance().build("/mine/MineTurnMoneyUI").withString("params", result).navigation();
            });
        } else 
        if (id == R.id.mine_shou) {//收款码
            ARouter.getInstance().build("/mine/MineCollectMoneyCodeUI").with(bd).navigation();
        }  else 
        if (id == R.id.usdtLL) {// USDT
//            ARouter.getInstance().build("/chain/ChainPriceDetailsUI").withInt("switchTab", 0).navigation();
            ARouter.getInstance().build("/mine/MineIntegralInfoRUI").withInt("switchTab", 0).navigation();
        } else 
        if (id == R.id.mine_feedback) {
            ARouter.getInstance().build("/mine/MineFeedBackUI").navigation();
        } else 
        if (id == R.id.mine_team) { //我的团队
            RxBus.getInstance().post(new MsgEvent(MineIndexFM.TAG, "3"));
//            ARouter.getInstance().build("/mine/MineTeamRUI").navigation();
//            ARouter.getInstance().build("/main/MainUI").withInt("switchTab",3).navigation();
        } else 
        if (id == R.id.updata) {//版本更新
            getUpgrade();
        } else 
        if (id == R.id.mine_share) { //分享好友
            RxBus.getInstance().post(new MsgEvent(MineIndexFM.TAG, "2"));
//            ARouter.getInstance().build("/chain/ChainShareUI").navigation();
//            ARouter.getInstance().build("/main/MainUI").withInt("switchTab",2).navigation();
        } else 
        if (id == R.id.mine_transaction) { //TBCC交易
//            ARouter.getInstance().build("/c2c/C2CtransactionUI").navigation();
//            ARouter.getInstance().build("/main/MainUI").withInt("switchTab",1).navigation();
            RxBus.getInstance().post(new MsgEvent(MineIndexFM.TAG, "1"));

        }
    }
    
    //判断是否登录和激活
    public boolean isLoginAndActivation(){
        if (UserTools.getInstance().getIsLogin()) {
            if (UserTools.getInstance().getUser().getIsActivation()) {
                return true;
            } else {
                BaseToast.getInstance().setMsg("未激活").show();
                return false;
            }
        } else {
            ARouter.getInstance().build("/login/LoginUI").navigation();
            return false;
        }
    }

    //判断是否激活
    public boolean isActivation(){
        if (UserTools.getInstance().getUser().getIsActivation()) {
            return true;
        } else {
            BaseToast.getInstance().setMsg("未激活").show();
            return false;
        }
    }

    
    @Override
    public void onStart() {
        super.onStart();
        User user = UserTools.getInstance().getUser();
        if (UserTools.getInstance().getIsLogin()) {
            if (rootLayout != null) {
                // 头像
                ((ExpandImageView) findViewById(R.id.userHead)).setImageURI(user.getHead());
                // 昵称
                ((TextView) findViewById(R.id.userNice)).setText(user.getNickName());
            }
        } else {
            // 昵称
            ((TextView) findViewById(R.id.userNice)).setText("点击登录");
            // 用户ID
            ((TextView) findViewById(R.id.phone)).setText("登录查看更多资料");
        }
    }

    public void loadUserInfo() {
        if (UserTools.getInstance(getContext()).getIsLogin()) {
            User user = UserTools.getInstance(getContext()).getUser();
            ((ExpandImageView) findViewById(R.id.userHead)).setImageURI(user.getHead());
            ((TextView) findViewById(R.id.userNice)).setText(user.getNickName());
            ((TextView) findViewById(R.id.phone)).setText(Util.phoneHide(user.getPhone()));
            ((TextView) findViewById(R.id.usdt)).setText(Util.doubleFormat(user.getIntegration(),"#0.00"));
            ((TextView) findViewById(R.id.activationState)).setText(user.getIsActivation() ? "已激活":"未激活");
        } else {
            ((ExpandImageView) findViewById(R.id.userHead)).setImageResource(R.mipmap.base_image_face);
            ((TextView) findViewById(R.id.userNice)).setText("点击登录");
            ((TextView) findViewById(R.id.phone)).setText("登录查看更多资料");
            ((TextView) findViewById(R.id.activationState)).setText("未激活");
        }
        loadReceiptState();
    }
    
    //加载接单状态
    public void loadReceiptState(){
        if(ServicePowerTools.isNotificationPower(getActivity()) && ServiceTools.isServiceRunning(getActivity(),PayNotificationMonitorService.class)){
            ((ImageView)findViewById(R.id.stateIcon)).setImageResource(R.drawable.mine_index_state_ok);
            ((TextView) findViewById(R.id.stateName)).setText("接单中");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                RxBus.getInstance().post(new MsgEvent(PayNotificationMonitorService.TAG,true));//开启服务,并显示的前台通知
            }
        } else
        if(!ServicePowerTools.isNotificationPower(getActivity())){//未授权
            ((ImageView)findViewById(R.id.stateIcon)).setImageResource(R.drawable.mine_index_state_no);
            ((TextView) findViewById(R.id.stateName)).setText("休息中");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                RxBus.getInstance().post(new MsgEvent(PayNotificationMonitorService.TAG,false));//开启服务,并关闭的前台通知
            }
        } else
        if(!ServiceTools.isServiceRunning(getActivity(),PayNotificationMonitorService.class)){//未启动服务
            ((ImageView)findViewById(R.id.stateIcon)).setImageResource(R.drawable.mine_index_state_no);
            ((TextView) findViewById(R.id.stateName)).setText("休息中");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                RxBus.getInstance().post(new MsgEvent(PayNotificationMonitorService.TAG,false));
            }
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
        BaseHttp.getInstance().write(getActivity(),"member/completeInfo", params, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                if (200 == response.optInt("code")) {
                    alert("已激活");
                    UserTools.getInstance().updUser(UserTools.getInstance().getUser().setIsActivation(true));
                    ((TextView)findViewById(R.id.activationState)).setText("已激活");
                }else{
                    alert(response.optString("message"));
                }
            }
        }.setSubmit(new XDialogSubmit(getActivity()).setMsg("授权成功,正在激活中.")));
    }

    public XDialogUpdateAPP updateAPPDialog;//APP升级弹窗

    // 从服务器获取最新版本
    private void getUpgrade() {
        BaseHttp.getInstance().query("other/getAndroidUpgrade", null, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                if (200 == response.optInt("code")) {
                    JSONObject data = response.optJSONObject("data");
                    int versionCode = data.optInt("version");
                    String[] versionNames = (versionCode + "").trim().split("");
                    String versionName = "V ";
                    for (int i = 0; i < versionNames.length; i++) {
                        //第一表 || 最后一个 (注:不加逗号)
                        if (i == 0 || i == (versionNames.length - 1)) {
                            versionName = versionName + versionNames[i];
                        } else {
                            versionName = versionName + versionNames[i] + ".";
                        }
                    }

                    BaseSP.getInstance().put("versionName", versionName);

                    if (versionCode > AndroidUtil.getVersionCode(getActivity())) {
                        updateAPPDialog = new XDialogUpdateAPP(getActivity()).setFileName("TbMall.apk").setMsg(data.optString("msg")).setAppDownUrl(data.optString("url").trim()).setUpdataVersionCode(versionName);
                        updateAPPDialog.show();
                    } else {
                        alert("已是最新版本");
                    }
                }
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            //界面可见
//            onOroffService();
            loadUserInfo();
            InitTools.getInstance().loadConfig();
            if (UserTools.getInstance(getContext()).getIsLogin()) {
                UserTools.getInstance(getContext()).loadUserInfo(user -> loadUserInfo());
            }
        } else {
            //界面不可见 相当于onpause
        }
    }
    
    private void onOroffService(){
        if (!Util.isEmpty(BaseSP.getInstance().getString("switch_ischeck"))){
            if (BaseSP.getInstance().getString("switch_ischeck").equals("1")){
                if(!ServicePowerTools.isNotificationPower(getActivity())){
                    new XDialog2Button(getActivity())
                            .setMsg("请开启 '"+getResources().getString(R.string.service_label)+"' 通知使用权限")
                            .setConfirmName("立即前往","稍后")
                            .setCallback(new CallBack() {
                                @Override
                                public void onSuccess(Object object) {
                                    ServicePowerTools.openNotificationPower(getActivity());//已授权,自己去关闭
                                }
                            }).show();
                }else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        RxBus.getInstance().post(new MsgEvent(PayNotificationMonitorService.TAG,true));
                    }
                }
            }else if (BaseSP.getInstance().getString("switch_ischeck").equals("0")){
                if(ServicePowerTools.isNotificationPower(getActivity())){
                    new XDialog2Button(getActivity())
                            .setMsg("请关闭 '"+getResources().getString(R.string.service_label)+"' 通知使用权限")
                            .setConfirmName("立即前往","稍后")
                            .setCallback(new CallBack() {
                                @Override
                                public void onSuccess(Object object) {
                                    ServicePowerTools.openNotificationPower(getActivity());//已授权,自己去关闭
                                }
                            }).show();
                }else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        RxBus.getInstance().post(new MsgEvent(PayNotificationMonitorService.TAG,false));
                    }
                }
            }
        }
    }

}
