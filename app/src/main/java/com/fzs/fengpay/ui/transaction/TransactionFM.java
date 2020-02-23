package com.fzs.fengpay.ui.transaction;

import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.fzs.fengpay.R;
import com.hzh.frame.ui.fragment.BaseFM;

import java.util.ArrayList;
import java.util.List;

/**
 * 邀请
 * @date 2019/10/4
 */
public class TransactionFM extends BaseFM {
    View layout;
    TabLayout mTabLayout;
    ViewPager mViewPager;
    String[] stateName=new String[] { "订单", "充值", "收入", "积分明细"};
    MyPagerAdapter myPagerAdapter;

    @Override
    protected void onCreateBase() {
        layout=setContentView(R.layout.fm_transation);
        getTitleView().setLeftIsShow(false);
        getTitleView().setContent("交易信息");
        mTabLayout = layout.findViewById(R.id.tabLayout);
        mViewPager = layout.findViewById(R.id.viewPager);
        initViewPager();
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> mFragmentList;

        @Override
        //每次Fragment切换都会调用到FragmentPagerAdapter.setPrimaryItem 方法
        public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            super.setPrimaryItem(container, position, object);
        }

        public void setFragments(ArrayList<Fragment> fragments) {
            mFragmentList = fragments;
        }

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }


    private void initViewPager() {
        // 创建一个集合,装填Fragment
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(TransactionOrderRFM.newInstance("0"));
        fragments.add(TransactionRechargeRFM.newInstance("1"));
        fragments.add(TransactionIncomeRFM.newInstance("2"));
        fragments.add(TransactionDetailedRFM.newInstance("3"));
        // 创建ViewPager适配器
        myPagerAdapter = new MyPagerAdapter(getChildFragmentManager());//!!! 这里要注意
        myPagerAdapter.setFragments(fragments);
        // 给ViewPager设置适配器
        mViewPager.setAdapter(myPagerAdapter);
        mViewPager.setOffscreenPageLimit(stateName.length);//默认全部加载完
        // TabLayout 指示器 (记得自己手动创建4个Fragment,注意是 app包下的Fragment 还是 V4包下的 Fragment)
        for (int i = 0; i < stateName.length; i++) {
            mTabLayout.addTab(mTabLayout.newTab());
        }
        // 使用 TabLayout 和 ViewPager 相关联
        mTabLayout.setupWithViewPager(mViewPager);
        // TabLayout指示器添加文本
        for (int i = 0; i < stateName.length; i++) {
            mTabLayout.getTabAt(i).setText(stateName[i]);
        }
        //tab的字体选择器,默认黑色,选择时红色
        mTabLayout.setTabTextColors(ContextCompat.getColor(getActivity(), R.color.text_c_666666), ContextCompat.getColor(getActivity(), R.color.application_color));
        //tab的下划线颜色,默认是粉红色
        mTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getActivity(), R.color.application_color));

        mViewPager.setCurrentItem(0);
    }

}
