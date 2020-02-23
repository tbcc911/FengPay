package com.fzs.mine.ui;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fzs.comn.model.User;
import com.fzs.comn.tools.InitTools;
import com.fzs.comn.tools.OtherLoginTools;
import com.fzs.comn.tools.UserTools;
import com.fzs.comn.tools.Util;
import com.fzs.comn.widget.imageview.ExpandImageView;
import com.fzs.mine.R;
import com.fzs.service.tools.ServicePowerTools;
import com.fzs.service.tools.ServiceTools;
import com.hzh.frame.callback.CallBack;
import com.hzh.frame.comn.callback.HttpCallBack;
import com.hzh.frame.comn.model.BaseRadio;
import com.hzh.frame.core.BaseSP;
import com.hzh.frame.core.HttpFrame.BaseHttp;
import com.hzh.frame.ui.fragment.BaseFM;
import com.hzh.frame.util.AndroidUtil;
import com.hzh.frame.widget.toast.BaseToast;
import com.hzh.frame.widget.xdialog.XDialog2Button;
import com.hzh.frame.widget.xdialog.XDialogRadio;
import com.hzh.frame.widget.xdialog.XDialogUpdateAPP;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bertsir.zbar.ScanConfig;


/**
 * 个人中心
 */
public class MineIndexFM extends BaseFM implements OnClickListener {

    private View mBaseView;
    List<HashMap<String, Object>> list=new ArrayList<>();
    
    //获取徽章Drawable
    public GradientDrawable getBadgeDrawable(Context context) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(context.getResources().getDimensionPixelSize(R.dimen.badge_corner_radius));
        shape.setColor(ContextCompat.getColor(context, R.color.application_color));//BackgroundColor
        shape.setStroke(2, ContextCompat.getColor(context, R.color.white));//BorderWidth,BorderColor
        return shape;
    }

    @Override
    public boolean setTitleIsShow() {
        return false;
    }

    @Override
    protected void onCreateBase() {
        mBaseView = setContentView(R.layout.mine_ui_index);
        mBaseView.findViewById(R.id.statusBar).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,AndroidUtil.getStatusBarHeight()));
        mBaseView.findViewById(R.id.head).setOnClickListener(this);
        mBaseView.findViewById(R.id.usdt).setOnClickListener(this);
        mBaseView.findViewById(R.id.addressManager).setOnClickListener(this);
        mBaseView.findViewById(R.id.systemSetup).setOnClickListener(this);
        mBaseView.findViewById(R.id.message).setOnClickListener(this);
        mBaseView.findViewById(R.id.mineTurn).setOnClickListener(this);
        mBaseView.findViewById(R.id.mine_sao).setOnClickListener(this);
        mBaseView.findViewById(R.id.mine_shou).setOnClickListener(this);
        mBaseView.findViewById(R.id.mineRecharge).setOnClickListener(this);
        mBaseView.findViewById(R.id.status).setOnClickListener(this);
        mBaseView.findViewById(R.id.mine_feedback).setOnClickListener(this);
        mBaseView.findViewById(R.id.mine_team).setOnClickListener(this);
        mBaseView.findViewById(R.id.updata).setOnClickListener(this);
        mBaseView.findViewById(R.id.usdtLL).setOnClickListener(this);
        mBaseView.findViewById(R.id.mine_share).setOnClickListener(this);
        mBaseView.findViewById(R.id.transaction).setOnClickListener(this);
        mBaseView.findViewById(R.id.mine_share).setOnClickListener(this);
        mBaseView.findViewById(R.id.activationState).setOnClickListener(this);
        mBaseView.findViewById(R.id.state).setOnClickListener(this);
        loadUserInfo();
    }


    @Override
    public void onClick(View view) {
        Bundle bd = new Bundle();
        int id = view.getId();
        if (id == R.id.head) {//用户信息
            ARouter.getInstance().build("/mine/MineUserInfoUI").with(bd).navigation();
        } else
        if (id == R.id.state) { //状态
            List<BaseRadio> list = new ArrayList<>();
            list.add(new BaseRadio().setName("上线").setId("1").setChecked(true));
            list.add(new BaseRadio().setName("离线").setId("2").setChecked(false));
            new XDialogRadio<>()
                    .setData(list)
                    .setTitle("切换状态")
                    .setRadioButtonMinWidth(AndroidUtil.getWindowWith() / 10.0 * 6)
                    .setCallBack(baseRadio -> {
                        if(!ServicePowerTools.isNotificationPower(getActivity())){
                            ServicePowerTools.openNotificationPower(getActivity());
                        }
                        ServiceTools.startPayMonitor(getActivity());
                        alert(baseRadio.getName());
                    })
                    .show(getFragmentManager());
        } else
        if (id == R.id.activationState) {// 激活状态
            if(UserTools.getInstance().getIsLogin()){
                if (!UserTools.getInstance().getUser().getIsActivation()) {
                    new XDialog2Button(getActivity())
                            .setMsg("授权支付宝用户信息")
                            .setConfirmName("立即前往","再看看")
                            .setCallback(new CallBack() {
                                @Override
                                public void onSuccess(Object object) {
                                    OtherLoginTools.launchAlipayLogin(getActivity(), new OtherLoginTools.CallBack() {
                                        @Override
                                        public void onSuccess(String userId) {
                                            alert("授权成功,uid:"+userId);
                                        }

                                        @Override
                                        public void onFail(String msg) {}
                                    });
                                }
                            })
                            .show();
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
                list.add(new BaseRadio().setName("USDT支付").setId("1").setChecked(true));
                list.add(new BaseRadio().setName("银行卡转账").setId("2").setChecked(false));
                list.add(new BaseRadio().setName("扫码支付").setId("3").setChecked(false));
                new XDialogRadio<>()
                        .setData(list)
                        .setTitle("选择支付方式")
                        .setOkAndNoName("去支付","再想想")
                        .setRadioButtonMinWidth(AndroidUtil.getWindowWith() / 10.0 * 8)
                        .setCallBack(baseRadio -> alert(baseRadio.getName()))
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
            ARouter.getInstance().build("/chain/ChainPriceDetailsUI").withInt("switchTab", 0).navigation();
        } else 
        if (id == R.id.mine_feedback) {
            ARouter.getInstance().build("/mine/MineFeedBackUI").navigation();
        } else 
        if (id == R.id.mine_team) {
            ARouter.getInstance().build("/mine/MineTeamRUI").navigation();
        } else 
        if (id == R.id.updata) {//版本更新
            getUpgrade();
        } else 
        if (id == R.id.mine_share) { //分享好友
            ARouter.getInstance().build("/chain/ChainShareUI").navigation();
        } else 
        if (id == R.id.transaction) { //TBCC交易
            ARouter.getInstance().build("/c2c/C2CtransactionUI").navigation();
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
            if (mBaseView != null) {
                // 头像
                ((ExpandImageView) mBaseView.findViewById(R.id.userHead)).setImageURI(user.getHead());
                // 昵称
                ((TextView) mBaseView.findViewById(R.id.userNice)).setText(user.getNickName());
            }
        } else {
            // 昵称
            ((TextView) mBaseView.findViewById(R.id.userNice)).setText("点击登录");
            // 用户ID
            ((TextView) mBaseView.findViewById(R.id.phone)).setText("登录查看更多资料");
        }
    }

    public void loadUserInfo() {
        if (UserTools.getInstance(getContext()).getIsLogin()) {
            User user = UserTools.getInstance(getContext()).getUser();
            ((ExpandImageView) mBaseView.findViewById(R.id.userHead)).setImageURI(user.getHead());
            ((TextView) mBaseView.findViewById(R.id.userNice)).setText(user.getNickName());
            ((TextView) mBaseView.findViewById(R.id.phone)).setText(Util.phoneHide(user.getPhone()));
            ((TextView) mBaseView.findViewById(R.id.usdt)).setText(Util.doubleFormat(user.getBalnace(),"#0.00"));
            ((TextView) mBaseView.findViewById(R.id.activationState)).setText(user.getIsActivation() ? "已激活":"未激活");
        } else {
            ((ExpandImageView) mBaseView.findViewById(R.id.userHead)).setImageResource(R.mipmap.base_image_face);
            ((TextView) mBaseView.findViewById(R.id.userNice)).setText("点击登录");
            ((TextView) mBaseView.findViewById(R.id.phone)).setText("登录查看更多资料");
            ((TextView) mBaseView.findViewById(R.id.activationState)).setText("未激活");
        }
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
                        alert("暂无版本更新");
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
            loadUserInfo();
            InitTools.getInstance().loadConfig();
            if (UserTools.getInstance(getContext()).getIsLogin()) {
                UserTools.getInstance(getContext()).loadUserInfo(user -> loadUserInfo());
            }
        } else {
            //界面不可见 相当于onpause
        }
    }

}
