package com.fzs.comn.widget.floor.banner.listener;

import android.support.v4.view.ViewPager;

import com.fzs.comn.tools.Util;
import com.fzs.comn.widget.floor.banner.view.BannerBGView;
import com.fzs.comn.widget.floor.bean.FloorBean;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2019/12/3
 */
public class BannerOnPageChangeListener implements ViewPager.OnPageChangeListener{

    public BannerBGView iArcView;
    public List<FloorBean.ItemInfoListBean.ItemContentListBean> itemContentList;

    public BannerOnPageChangeListener(BannerBGView iArcView, List<FloorBean.ItemInfoListBean.ItemContentListBean> itemContentList){
        this.iArcView=iArcView;
        this.itemContentList=itemContentList;
    }

    /**
     * 在滑动的时候会调用此方法,在滑动被停止之前，此方法回一直得到调用 其中三个参数的含义分别为：
     * @param startPosition 开始页面
     * @param deviation 当前页面偏移的百分比
     * @param i1 当前页面偏移的像素位置
     * **/
    @Override
    public synchronized void onPageScrolled(int startPosition, float deviation, int i1) {
        if(deviation!=0 && deviation!=1){
            int endPosition=startPosition;
            if(endPosition+1 >= itemContentList.size()){
                endPosition=0;
            }else{
                endPosition=endPosition+1;
            }
            String startColor=itemContentList.get(startPosition).color;//开始颜色
            String endColor=itemContentList.get(endPosition).color;//目标颜色
            String color= Util.getMiddleColor(startColor,endColor,deviation);
            iArcView.setPaintColor(color);
        }
    }
    @Override
    public void onPageSelected(int position) { }

    @Override
    public void onPageScrollStateChanged(int state) {}
}
