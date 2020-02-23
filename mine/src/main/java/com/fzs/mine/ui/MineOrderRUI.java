package com.fzs.mine.ui;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fzs.mine.R;
import com.hzh.frame.ui.activity.BaseUI;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单列表
 * */
@Route(path = "/mine/MineOrderRUI")
public class MineOrderRUI extends BaseUI {


    TabLayout mTabLayout;
    ViewPager mViewPager;
    //查询订单状态参数: 0->全部; 1->待付款; 2->待发货; 3->待收货; 4->待评价
    String[] stateName=new String[]{"全部","待付款","待发货","待收货","待评价"};
    int[] stateValue=new int[]{0,1,2,3,4};

    public void findViewById(){
        mTabLayout=findViewById(R.id.tablayout);
        mViewPager=findViewById(R.id.viewPager);
    }

    @Override
    protected void onCreateBase() {
        setContentView(R.layout.mine_ui_order);
        findViewById();
        getTitleView().setContent("我的订单");
        initViewPager(); // 初始化ViewPager
    }


    private void initViewPager() {
        // 创建一个集合,装填Fragment
        ArrayList<Fragment> fragments = new ArrayList<>();
        for(int i=0;i<stateValue.length;i++){
            fragments.add(MineOrderRFM.newInstance(stateValue[i]));
        }
        // 创建ViewPager适配器
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        myPagerAdapter.setFragments(fragments);
        // 给ViewPager设置适配器
        mViewPager.setAdapter(myPagerAdapter);
        // TabLayout 指示器 (记得自己手动创建4个Fragment,注意是 app包下的Fragment 还是 V4包下的 Fragment)
        for(int i=0;i<stateValue.length;i++){
            mTabLayout.addTab(mTabLayout.newTab());
        }
        // 使用 TabLayout 和 ViewPager 相关联
        mTabLayout.setupWithViewPager(mViewPager);
        // TabLayout指示器添加文本
        for(int i=0;i<stateName.length;i++){
            mTabLayout.getTabAt(i).setText(stateName[i]);
        }
        //tab的字体选择器,默认黑色,选择时红色
        mTabLayout.setTabTextColors(ContextCompat.getColor(this,R.color.text_c_666666), ContextCompat.getColor(this,R.color.application_color));
        //tab的下划线颜色,默认是粉红色
        mTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this,R.color.application_color));

        switch (getIntent().getIntExtra("order_status",0)){
            case 0:
                mViewPager.setCurrentItem(0);
                break;
            case 1:
                mViewPager.setCurrentItem(1);
                break;
            case 2:
                mViewPager.setCurrentItem(2);
                break;
            case 3:
                mViewPager.setCurrentItem(3);
                break;
            case 4:
                mViewPager.setCurrentItem(4);
                break;
        }
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> mFragmentList;

        public void setFragments(ArrayList<Fragment> fragments) {
            mFragmentList = fragments;
        }

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = mFragmentList.get(position);

            return fragment;
        }

        @Override
        public int getCount() {

            return mFragmentList.size();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //        getListView().startRefresh();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                //                getListView().startRefresh();
                break;
            case 2:
                //                getListView().startRefresh();
                break;
        }
    }
}
