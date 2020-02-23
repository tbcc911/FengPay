package com.fzs.fengpay.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.activeandroid.query.Delete;
import com.alibaba.android.arouter.launcher.ARouter;
import com.baidu.location.BDLocation;
import com.fzs.comn.tools.BaiduLocation;
import com.fzs.comn.tools.BaiduLocationCallBack;
import com.fzs.comn.tools.InitTools;
import com.fzs.fengpay.R;
import com.hzh.frame.comn.annotation.ContentView;
import com.hzh.frame.comn.annotation.OnClick;
import com.hzh.frame.comn.annotation.ViewInject;
import com.hzh.frame.comn.model.BaseHttpRequest;
import com.hzh.frame.core.BaseSP;
import com.hzh.frame.ui.activity.BaseUI;
import com.hzh.frame.util.AndroidUtil;
import com.hzh.frame.util.PowerUtil;
import com.hzh.frame.util.StatusBarUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

@ContentView(R.layout.ui_welcome)
public class WelcomeUI extends BaseUI {

    Disposable disposable;

    @ViewInject(R.id.time) TextView time;
    @ViewInject(R.id.startApp) LinearLayout startApp;

    @Override
    public boolean setTitleIsShow() {
        return false;
    }

    @Override
    protected void onCreateBase() {
        new Delete().from(BaseHttpRequest.class).execute();
        StatusBarUtil.setTransparentForImageView(this, null);
        StatusBarUtil.setWhileIconToImage(this);
        startApp.setClickable(false);
        startTime();//开启倒计时
        powerApply();//开始权限申请
        InitTools.getInstance().loadConfig();//获取初始化配置信息
    }

    @OnClick({R.id.startApp})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.startApp:
                if(powerApply()){//权限都申请成功了
                    //是否已经进入过APP介绍页面
                    if(BaseSP.getInstance().getBoolean("isIntroduce",false)){
                        ARouter.getInstance().build("/main/MainUI").navigation();
                    }else{
                        ARouter.getInstance().build("/app/IntroduceUI").navigation();
                    }
                    finish();
                }
                break;
        }
    }

    /**
     * 倒计时
     */
    public void startTime() {
        //从0开始发射11个数字为：0-60依次输出，延时0s执行，每1s发射一次。
        disposable = Flowable.intervalRange(0, 5, 0, 1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).doOnNext(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                time.setText((5 - aLong) + "s");
            }
        }).doOnComplete(() -> {
            disposable.dispose();
            time.setText("进入");
            startApp.setClickable(true);
            startApp.performClick();//代码触发进入按钮
        }).subscribe();
    }

    //权限申请
    public boolean powerApply() {
        /**
         * 申请APP所需的权限(申请后系统会回调一个传入Activity的onRequestPermissionsResult)
         * */
        String[] permissions = new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!PowerUtil.selectApply(this, permissions)) {
            PowerUtil.apply(this, 100, permissions);
            return false;
        } else {
            BaseSP.getInstance().put("powerApply", true);
            return true;
        }
    }

    @Override
    //权限申请 | 结果回调
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(100==requestCode && permissions.length == grantResults.length){
            //判断每个权限的申请情况
            for(int i=0;i<grantResults.length;i++){
                if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                    Log.i(AndroidUtil.getPackageName()+"--->权限申请:","授权成功 | " + permissions[i]);
                    BaseSP.getInstance().put(permissions[i],true);//记录申请成功的权限
                    if(Manifest.permission.ACCESS_FINE_LOCATION.equals(permissions[i])){//定位权限申请成功
                        new BaiduLocation(this, new BaiduLocationCallBack() {
                            @Override
                            public void success(BDLocation location) {
                            }
                            @Override
                            public void failure(int code) {}
                        });
                    }
                } else{
                    Log.i(AndroidUtil.getPackageName()+"--->权限申请:","授权失败 | " + permissions[i]);
                    BaseSP.getInstance().put(permissions[i],false);//记录申请失败的权限
                }
            }
        }
    }

}
