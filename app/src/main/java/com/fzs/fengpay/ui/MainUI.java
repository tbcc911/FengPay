package com.fzs.fengpay.ui;

import android.Manifest;
import android.content.Intent;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.baidu.location.BDLocation;
import com.fzs.comn.tools.BaiduLocation;
import com.fzs.comn.tools.BaiduLocationCallBack;
import com.fzs.comn.tools.UserTools;
import com.fzs.comn.tools.Util;
import com.fzs.fengpay.R;
import com.fzs.fengpay.ui.agent.AgentRFM;
import com.fzs.fengpay.ui.main.MainFM;
import com.fzs.fengpay.ui.share.ShareFM;
import com.fzs.fengpay.ui.transaction.TransactionFM;
import com.fzs.mine.ui.MineIndexFM;
import com.hzh.frame.BaseInitData;
import com.hzh.frame.comn.annotation.ContentView;
import com.hzh.frame.comn.annotation.ViewInject;
import com.hzh.frame.comn.callback.HttpCallBack;
import com.hzh.frame.core.BaseSP;
import com.hzh.frame.core.HttpFrame.BaseHttp;
import com.hzh.frame.ui.activity.BaseUI;
import com.hzh.frame.util.AndroidUtil;
import com.hzh.frame.util.PowerUtil;
import com.hzh.frame.widget.rxbus.MsgEvent;
import com.hzh.frame.widget.rxbus.RxBus;
import com.hzh.frame.widget.xdialog.XDialogUpdateAPP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import q.rorbin.badgeview.QBadgeView;

@Route(path = "/main/MainUI")
@ContentView(R.layout.ui_main)
public class MainUI extends BaseUI{
    
    @ViewInject(R.id.bottomNavigationView) BottomNavigationView mBottomNavigationView;
    @ViewInject(R.id.viewPager) ViewPager mViewPager;
	
	private MainFM mMainFM=new MainFM();
    private TransactionFM mTransactionFM=new TransactionFM(); 
    private ShareFM mShareFM=new ShareFM();
    private AgentRFM mAgentRFM=new AgentRFM();
	private MineIndexFM mMineFM=new MineIndexFM();
    
    private QBadgeView mQBadgeView=new QBadgeView(BaseInitData.applicationContext);
	
	@Override
	public boolean setTitleIsShow() {
		return false;
	}
	
	@Override
	protected void onCreateBase() {
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
        mViewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        mBottomNavigationView.setOnNavigationItemSelectedListener(new MyOnNavigationItemSelectedListener());
		
		init();
		getUpgrade();
		getEvent();
		//用户收益-->提升收益-->回到首页我是客户页面
		if( BaseSP.getInstance().getBoolean("UserProfitAddMoney", false)){
			BaseSP.getInstance().put("UserProfitAddMoney", false);
		}
	}

    class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:return mMainFM;
                case 1:return mTransactionFM;
                case 2:return mShareFM;
                case 3:return mAgentRFM;
                case 4:return mMineFM;
                default: return null;
            }
        }
        @Override
        public int getCount() {
            return 5;
        }
    }


    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageScrolled(int i, float v, int i1) {}
        @Override
        public void onPageScrollStateChanged(int i) {}
        @Override
        public void onPageSelected(int position) {
            mBottomNavigationView.getMenu().getItem(position).setChecked(true);
        }
    }

    //这里可true是一个消费过程，同样可以使用break，外部返回true也可以
    class MyOnNavigationItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.main:
                    mViewPager.setCurrentItem(0);
                    return true;
                case R.id.type:
                    mViewPager.setCurrentItem(1);
                    return true;
                case R.id.share:
                    mViewPager.setCurrentItem(2);
                    return true;
                case R.id.agent:
                    mViewPager.setCurrentItem(3);
                    return true;
                case R.id.mine:
                    mViewPager.setCurrentItem(4);
                    return true;
            }
            return true;
        }
    }

	private void init() {
        //判断定位权限是否申请成功
        if(PowerUtil.selectApply(this,Manifest.permission.ACCESS_FINE_LOCATION)){
            new BaiduLocation(this, new BaiduLocationCallBack() {
                @Override
                public void success(BDLocation location) {
                }
                @Override
                public void failure(int code) {}
            });
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

                    BaseSP.getInstance().put("versionName",versionName);

                    if (versionCode > AndroidUtil.getVersionCode(MainUI.this)) {
                        updateAPPDialog = new XDialogUpdateAPP(MainUI.this)
                                .setFileName("TBMall.apk")
                                .setMsg(data.optString("msg"))
                                .setAppDownUrl(data.optString("url").trim())
                                .setUpdataVersionCode(versionName);
                        updateAPPDialog.show();
                    }
                }
            }
        });
    }
	

	private long exitTime;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) // System.currentTimeMillis()无论何时调用，肯定大于2000
			{
				alert("再按一次退出程序");
				exitTime = System.currentTimeMillis();
			} else {
//                伪 退出
                moveTaskToBack(true);
//                真 退出
//                finish();
//                System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == XDialogUpdateAPP.INSTALL_PERMISS_CODE) {
            //安装未知应用来源的权限通过,安装APK
            if(updateAPPDialog!=null) updateAPPDialog.openApk();
        }
    }
    
    public void updBadges(){
        //更新购物车数量
        if(UserTools.getInstance().getIsLogin()) {
            JSONObject params = new JSONObject();
            try {
                params.put("page", 1);
                params.put("limit", 9999);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            BaseHttp.getInstance().query("3030",params,new HttpCallBack(){
                @Override
                public void onSuccess(JSONObject response) {
                    int cartSumGoods=0;
                    if(1==response.optInt("result")){
                        JSONObject data=response.optJSONObject("data");
                        if(1==data.optInt("code")){
                            JSONArray storelist=data.optJSONArray("goodsCart");
                            if(storelist!=null && storelist.length()>0){
                                for (int i = 0; i < storelist.length(); i++) {
                                    //更新店铺信息
                                    JSONObject storeModel=storelist.optJSONObject(i);
                                    if(storeModel.optJSONArray("goods").length()>0){
                                        for (int k = 0; k < storeModel.optJSONArray("goods").length(); k++) {
                                            JSONObject goodsModel=storeModel.optJSONArray("goods").optJSONObject(k);
                                            cartSumGoods=cartSumGoods+goodsModel.optInt("count");
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if(cartSumGoods>0){
                        showBadgeView(3,cartSumGoods);
                    }else{
                        hideBadgeView(3);
                    }
                }
            });
        }
    }

    /**
     * BottomNavigationView隐藏角标
     * @param viewIndex tab索引
     */
    private void hideBadgeView(int viewIndex) {
        // 具体child的查找和view的嵌套结构请在源码中查看
        // 从bottomNavigationView中获得BottomNavigationMenuView
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) mBottomNavigationView.getChildAt(0);
        // 从BottomNavigationMenuView中获得childview, BottomNavigationItemView
        if (viewIndex < menuView.getChildCount()) {
            // 获得viewIndex对应子tab
            View view = menuView.getChildAt(viewIndex);
            // 显示badegeview
            mQBadgeView.bindTarget(view).hide(false);
        }
    }
    

    /**
     * BottomNavigationView显示角标
     * @param viewIndex tab索引
     * @param showNumber 显示的数字，小于等于0是将不显示
     */
    private void showBadgeView(int viewIndex, int showNumber) {
        // 具体child的查找和view的嵌套结构请在源码中查看
        // 从bottomNavigationView中获得BottomNavigationMenuView
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) mBottomNavigationView.getChildAt(0);
        // 从BottomNavigationMenuView中获得childview, BottomNavigationItemView
        if (viewIndex < menuView.getChildCount()) {
            // 获得viewIndex对应子tab
            View view = menuView.getChildAt(viewIndex);
            // 显示badegeview
            mQBadgeView.bindTarget(view).setGravityOffset(10, 3, false).setBadgeNumber(showNumber);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mMainFM.setUserVisibleHint(mMainFM.getUserVisibleHint());
        mTransactionFM.setUserVisibleHint(mTransactionFM.getUserVisibleHint());
        mAgentRFM.setUserVisibleHint(mAgentRFM.getUserVisibleHint());
        mMineFM.setUserVisibleHint(mMineFM.getUserVisibleHint());
        updBadges();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && intent.getExtras() != null) {
            int switchTab = intent.getExtras().getInt("switchTab", 0);
            mViewPager.setCurrentItem(switchTab);
        }
    }
    
    private void getEvent(){
        RxBus.getInstance()
                .toObservable(this, MsgEvent.class)
                .filter(msgEvent -> msgEvent.getTag().equals(MineIndexFM.TAG))
                .subscribe(msgEvent -> {
                    if (!Util.isEmpty(msgEvent.getMsg().toString())){
                        int switchTab = Integer.parseInt(msgEvent.getMsg().toString());
                        mViewPager.setCurrentItem(switchTab);
                    }
                });
                
    }
}
