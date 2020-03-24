package com.happy.godpay.ui;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.happy.godpay.R;
import com.happy.godpay.ui.widget.adapter.GuidePageAdapter;
import com.hzh.frame.comn.annotation.ContentView;
import com.hzh.frame.core.BaseSP;
import com.hzh.frame.ui.activity.BaseUI;
import com.hzh.frame.util.StatusBarUtil;
import com.hzh.frame.util.Util;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/app/IntroduceUI")
@ContentView(R.layout.ui_introduce)
public class IntroduceUI extends BaseUI implements ViewPager.OnPageChangeListener{

    private ViewPager vp;
    private int []imageIdArray;//图片资源的数组
    private List<View> viewList;//图片资源的集合
    private ViewGroup vg;//放置圆点

    //实例化原点View
    private ImageView iv_point;
    private ImageView[] ivPointArray;

    //最后一页的按钮
    private Button ib_start;
    private ImageView ib_logo;
    

    @Override
    public boolean setTitleIsShow() {
        return false;
    }

    @Override
    protected void onCreateBase() {
        StatusBarUtil.setTransparentForImageView(this, null);
        StatusBarUtil.setWhileIconToImage(this);
        
        ib_start = findViewById(R.id.guide_ib_start);
        ib_logo = findViewById(R.id.guide_ib_logo);
        ib_start.setOnClickListener(v -> {
            ARouter.getInstance().build("/main/MainUI").navigation();
            BaseSP.getInstance().put("isIntroduce",true);
            finish();
        });

        //加载ViewPager
        initViewPager();

        //加载底部圆点
        initPoint();
    }

    /**
     * 加载底部圆点
     */
    private void initPoint() {
        //这里实例化LinearLayout
        vg = (ViewGroup) findViewById(R.id.guide_ll_point);
        //根据ViewPager的item数量实例化数组
        ivPointArray = new ImageView[viewList.size()];
        //循环新建底部圆点ImageView，将生成的ImageView保存到数组中
        int size = viewList.size();
        for (int i = 0;i<size;i++){
            iv_point = new ImageView(this);
            LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.leftMargin= Util.dip2px(this,10);
            lp.rightMargin= Util.dip2px(this,10);
            if (i == 0){
                lp.width= Util.dip2px(this,10);
                lp.height= Util.dip2px(this,10);
                iv_point.setBackgroundResource(R.drawable.bg_button_green_r30);
            }else{
                lp.width= Util.dip2px(this,7);
                lp.height= Util.dip2px(this,7);
                iv_point.setBackgroundResource(R.drawable.bg_button_999999);
            }
            iv_point.setLayoutParams(lp);
            ivPointArray[i] = iv_point;
            //将数组中的ImageView加入到ViewGroup
            vg.addView(ivPointArray[i]);
        }
    }

    /**
     * 加载图片ViewPager
     */
    private void initViewPager() {
        vp = (ViewPager) findViewById(R.id.guide_vp);
        //实例化图片资源
        imageIdArray = new int[]{R.drawable.app_introduce_step_1,R.drawable.app_introduce_step_2,R.drawable.app_introduce_step_3};
        viewList = new ArrayList<>();
        //获取一个Layout参数，设置为全屏
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

        //循环创建View并加入到集合中
        int len = imageIdArray.length;
        for (int i = 0;i<len;i++){
            //new ImageView并设置全屏和图片资源
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(params);
            imageView.setBackgroundResource(imageIdArray[i]);

            //将ImageView加入到集合中
            viewList.add(imageView);
        }

        //View集合初始化好后，设置Adapter
        vp.setAdapter(new GuidePageAdapter(viewList));
        //设置滑动监听
        vp.setOnPageChangeListener(this);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * 滑动后的监听
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        //循环设置当前页的标记图
        int length = imageIdArray.length;
        for (int i = 0;i<length;i++){
            ImageView imageView = ivPointArray[i];
            LinearLayout.LayoutParams lp= (LinearLayout.LayoutParams) imageView.getLayoutParams();
            if (position == i){
                lp.width= Util.dip2px(this,10);
                lp.height= Util.dip2px(this,10);
                imageView.setBackgroundResource(R.drawable.bg_button_green_r30);
            }else{
                lp.width= Util.dip2px(this,7);
                lp.height= Util.dip2px(this,7);
                imageView.setBackgroundResource(R.drawable.bg_button_999999);
            }
            imageView.setLayoutParams(lp);
        }

        //判断是否是最后一页，若是则显示按钮
        if (position == imageIdArray.length - 1){
            ib_start.setVisibility(View.VISIBLE);
            ib_logo.setVisibility(View.GONE);
        }else {
            ib_start.setVisibility(View.GONE);
            ib_logo.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
