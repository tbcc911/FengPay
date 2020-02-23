package com.fzs.mine.ui;

import android.annotation.SuppressLint;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.facebook.drawee.view.SimpleDraweeView;
import com.fzs.comn.widget.stepview.HorizontalStepView;
import com.fzs.comn.widget.stepview.VerticalStepView;
import com.fzs.mine.R;
import com.hzh.frame.core.ImageFrame.BaseImage;
import com.hzh.frame.ui.activity.BaseUI;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Route(path = "/mine/MineOrderInfoLogisticUI")
public class MineOrderInfoLogisticUI extends BaseUI {
    private VerticalStepView mSetpviewContent;
    private HorizontalStepView mSetpviewTitle;
    private TextView tv_state, tv_kdgsmc, tv_kdbh;
    private SimpleDraweeView pic;
    private int state = -1;
    private String kdbh="", kdgsmc="", url="http://www.baidu.com/";// 快递公司，快递编号，快递公司名称,商品图片URL
    private List<String> listDate = new ArrayList<String>();

    @SuppressLint("NewApi")
    @Override
    protected void onCreateBase() {
        setContentView(R.layout.mine_ui_order_info_logistic);
        getTitleView().setContent("物流详情");
        try {
            state=getIntent().getIntExtra("state", -1);
            kdbh = getIntent().getStringExtra("kdbh");
            kdgsmc = getIntent().getStringExtra("kdgsmc");
            url = getIntent().getStringExtra("pic");
            listDate= getIntent().getStringArrayListExtra("dataList");
        } catch (Exception e) {
        }
        tv_state = (TextView) findViewById(R.id.state);
        tv_kdgsmc = (TextView) findViewById(R.id.tv_kdgsmc);
        tv_kdbh = (TextView) findViewById(R.id.tv_kdbh);
        pic = (SimpleDraweeView) findViewById(R.id.pic);
        mSetpviewTitle = (HorizontalStepView) findViewById(R.id.step_view1);
        mSetpviewContent = (VerticalStepView) findViewById(R.id.step_view0);

        ((TextView) findViewById(R.id.goodsNumber)).setText(getIntent().getIntExtra("goodsNumber",1)+"件商品");
        tv_kdgsmc.setText("承运公司:" + kdgsmc);
        tv_kdbh.setText("运单号:" + kdbh);
        // 头像
        BaseImage.getInstance().loadXY(url, pic);
        initData();
    }

    // 设置值
    public void initData() {
        // 以下为快递100API接口提供快递状态
        // 0：在途，即货物处于运输过程中；
        // 1：揽件，货物已由快递公司揽收并且产生了第一条跟踪信息；
        // 2：疑难，货物寄送过程出了问题；
        // 3：签收，收件人已签收；
        // 4：退签，即货物由于用户拒签、超区等原因退回，而且发件人已经签收；
        // 5：派件，即快递正在进行同城派件；
        // 6：退回，货物正处于退回发件人的途中；
        // 7: 转单
        List<String> listTitle = new LinkedList<String>();
        listTitle.add(0, "揽件");
        listTitle.add(1, "在途");
        listTitle.add(2, "派件");
        listTitle.add(3, "签收");

        if (state == -1) {
            tv_state.setText("异常");
        } else {
            tv_state.setText(listTitle.get(state));
        }
        if(listDate!=null && listDate.size()>0){
            mSetpviewTitle
                    .setStepsViewIndicatorComplectingPosition(state)
                    .setStepViewTexts(listTitle)// 设置完成的步数
                    .setStepsViewIndicatorCompletedLineColor(getResources().getColor(R.color.logistic))// 设置StepsViewIndicator完成线的颜色
                    .setStepsViewIndicatorUnCompletedLineColor(getResources().getColor(R.color.logistic))// 设置StepsViewIndicator未完成线的颜色
                    .setStepViewComplectedTextColor(getResources().getColor(R.color.text_hint_color))// 设置StepsView
                    .setStepViewUnComplectedTextColor(getResources().getColor(R.color.logistic))// 设置StepsView
                    .setStepsViewIndicatorCompleteIcon(getResources().getDrawable(R.mipmap.complted))// 设置StepsViewIndicator
                    .setStepsViewIndicatorDefaultIcon(getResources().getDrawable(R.mipmap.default_icon))// 设置StepsViewIndicator
                    .setStepsViewIndicatorAttentionIcon(getResources().getDrawable(R.mipmap.attention))// 设置StepsViewIndicator
                    .execute();
            mSetpviewContent
                    .setStepsViewIndicatorComplectingPosition(listDate.size() - 1)// 设置完成的步数
                    .setStepViewTexts(listDate)// 总步骤
                    .setLinePaddingProportion(0.85f)// 设置indicator线与线间距的比例系数
                    .setStepsViewIndicatorCompletedLineColor(getResources().getColor(R.color.logistic))// 设置StepsViewIndicator完成线的颜色
                    .setStepsViewIndicatorUnCompletedLineColor(getResources().getColor(R.color.logistic))// 设置StepsViewIndicator未完成线的颜色
                    .setStepViewComplectedTextColor(getResources().getColor(R.color.text_hint_color))// 设置StepsView
                    .setStepViewUnComplectedTextColor(getResources().getColor(R.color.green))// 设置StepsView
                    .setStepsViewIndicatorCompleteIcon(getResources().getDrawable(R.mipmap.complted))// 设置StepsViewIndicator
                    .setStepsViewIndicatorDefaultIcon(getResources().getDrawable(R.mipmap.default_icon))// 设置StepsViewIndicator
                    .setStepsViewIndicatorAttentionIcon(getResources().getDrawable(R.mipmap.attention))
                    .execute();
        }
    }

}

