package com.fzs.comn.widget.floor.adapter;

import android.app.Activity;
import android.graphics.Paint;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fzs.comn.R;
import com.fzs.comn.tools.Util;
import com.fzs.comn.widget.floor.FloorTools;
import com.fzs.comn.widget.floor.bean.FloorBean;
import com.fzs.comn.widget.imageview.ExpandImageView;
import com.hzh.frame.util.AndroidUtil;
import com.hzh.frame.widget.xrecyclerview.BaseRecyclerAdapter;
import com.hzh.frame.widget.xrecyclerview.RecyclerViewHolder;

import java.util.List;

/**
 * @author：admin on 2017/3/30 18:00.
 */

public class FloorSeckillContentAdapter extends BaseRecyclerAdapter<FloorBean.ItemInfoListBean.ItemContentListBean> {

    Activity mActivity;
    
    public FloorSeckillContentAdapter(Activity activity, List<FloorBean.ItemInfoListBean.ItemContentListBean> list) {
        super(activity, list);
        mActivity=activity;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.comn_floor_seckill_content_item;
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, FloorBean.ItemInfoListBean.ItemContentListBean item) {
        int width = (int) ((AndroidUtil.getWindowWith() - mContext.getResources().getDimension(R.dimen.dp_90)) / 3.5);
        ExpandImageView image=holder.getView(R.id.image);
        image.setLayoutParams(new FrameLayout.LayoutParams(width,width));
        image.setAspectRatio(1.0f);
        image.setImageURI(item.imageUrl);
        TextView title=holder.getTextView(R.id.title);
        title.setMaxWidth(width);
        if (!Util.isEmpty(item.title)) {
            title.setVisibility(View.VISIBLE);
            title.setText(item.title);
        }else{
            title.setVisibility(View.GONE);
            title.setText("");
        }
        holder.setText(R.id.price, item.price);
        holder.setText(R.id.oldPrice, item.oldPrice);
        ((TextView) holder.getView(R.id.oldPrice)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        holder.setOnItemClickListener(view->{
            FloorTools.getInstance(mActivity).onBindClick(item.clickType, item.clickValue);
        });

    }
}
