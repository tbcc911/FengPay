package com.fzs.mine.ui;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fzs.mine.R;
import com.hzh.frame.ui.activity.BaseUI;
import com.hzh.frame.util.AndroidUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 兑换专区首页
 * */
public class MineExchangeUI extends BaseUI implements OnClickListener{

	private List<Fragment> mFragmentList = new ArrayList<Fragment>();
	private List<TextView> mBanners=new ArrayList<TextView>();
	public ViewPager mViewPager;
	private TextView mTitleScrollLeft,mTitleScrollRight;
	private String mTitleTextColorUp="#131313";
	private String mTitleTextColorDown="#ff639f";
	
	@Override
	protected void onCreateBase(){
		setContentView(R.layout.mine_ui_exchange);
		getTitleView().setContent("兑换专区");
		mViewPager = (ViewPager)findViewById(R.id.viewPager);
		mBanners.add((TextView)findViewById(R.id.all));
		mBanners.add((TextView)findViewById(R.id.shippedNot));
		mBanners.add((TextView)findViewById(R.id.shipped));
		findViewById(R.id.all).setOnClickListener(this);
		findViewById(R.id.shippedNot).setOnClickListener(this);
		findViewById(R.id.shipped).setOnClickListener(this);
		mFragmentList.add(new MineExchangeAllLFM());
		mFragmentList.add(new MineExchangeShippedNotLFM());
		mFragmentList.add(new MineExchangeShippedLFM());
		mTitleScrollLeft=(TextView) findViewById(R.id.title_scroll_left);
		mTitleScrollRight=(TextView) findViewById(R.id.title_scroll_right);
		mTitleScrollRight.setLayoutParams(new LinearLayout.LayoutParams((AndroidUtil.getWindowWith()/5*3-1)/mFragmentList.size(),LayoutParams.MATCH_PARENT));
		mTitleScrollRight.setBackgroundColor(Color.parseColor(mTitleTextColorDown));
		// 设置填充ViewPager页面的适配器
		mViewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
	    // 设置一个监听器，当ViewPager中的页面改变时调用
		mViewPager.setOnPageChangeListener(new MyPageChangeListener());
	}
	
	
	/**viewPage适配器*/
	private class MyAdapter extends FragmentPagerAdapter {
		public MyAdapter(FragmentManager fm) {
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
	
	/**viewPage滑动监听*/
	private class MyPageChangeListener implements ViewPager.OnPageChangeListener {
		  
	    /**
	     * 此方法是页面跳转完后得到调用
	     * @param position 是你当前选中的页面的Position(位置编号0开始)
	     * **/
	    public void onPageSelected(int position) {
	    	for(int i=0;i<mBanners.size();i++){
	    		if(i==position){
	    			mBanners.get(i).setTextColor(Color.parseColor(mTitleTextColorDown));
	    		}else{
	    			mBanners.get(i).setTextColor(Color.parseColor(mTitleTextColorUp));
	    		}
	    	}
	    }

	    /**
	     * 在状态改变的时候调用
	     * 其中arg0这个参数有三种状态(0，1，2):
	     * arg0 == 0 :的时候表示什么都没做
	     * arg0 == 1 :的时候表示正在滑动
	     * arg0 == 2 :的时候表示滑动完毕了
	     * **/
	    public void onPageScrollStateChanged(int arg0) {}

	    /**
	     * 在滑动的时候会调用此方法,在滑动被停止之前，此方法回一直得到调用 其中三个参数的含义分别为：
	     * @param arg0 当前页面，及你点击滑动的页面
	     * @param arg1 当前页面偏移的百分比
	     * @param arg2 当前页面偏移的像素位置
	     * **/
	    public void onPageScrolled(int arg0, float arg1, int arg2) {
	    	//当前页,滚动栏距最左边的基础距离
	    	int defaultWidth=0;
	    	if(arg0==0){
	    		defaultWidth=0;
	    	}else{
	    		//(views.size()-1):处理除不尽的情况右边有一些小空隙
	    		defaultWidth=((AndroidUtil.getWindowWith()/5*3-1)/mFragmentList.size()*arg0)+(mFragmentList.size()-1);
	    	}
	    	if(arg2!=0){
	    		LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(defaultWidth+(arg2*(AndroidUtil.getWindowWith()/5*3-1)/ AndroidUtil.getWindowWith())/mFragmentList.size(),LayoutParams.MATCH_PARENT);
	    		mTitleScrollLeft.setLayoutParams(lp);
		    }
	    }
	}

	@Override
	public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.all) {
            mViewPager.setCurrentItem(0);
        } else if (id == R.id.shippedNot) {
            mViewPager.setCurrentItem(1);
        } else if (id == R.id.shipped) {
            mViewPager.setCurrentItem(2);
        }
	}
	
}
