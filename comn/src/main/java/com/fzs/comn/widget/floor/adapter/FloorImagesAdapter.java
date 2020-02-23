package com.fzs.comn.widget.floor.adapter;

import android.app.Activity;

import com.fzs.comn.R;
import com.fzs.comn.widget.floor.FloorTools;
import com.fzs.comn.widget.floor.bean.FloorBean;
import com.hzh.frame.core.ImageFrame.BaseImage;
import com.hzh.frame.util.AndroidUtil;
import com.hzh.frame.widget.xrecyclerview.BaseRecyclerAdapter;
import com.hzh.frame.widget.xrecyclerview.RecyclerViewHolder;

import java.util.List;

public class FloorImagesAdapter extends BaseRecyclerAdapter<FloorBean.ItemInfoListBean.ItemContentListBean> {

    private Activity mActivity;

    public FloorImagesAdapter(Activity activity, List<FloorBean.ItemInfoListBean.ItemContentListBean> list) {
        super(activity, list);
        mActivity=activity;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.comn_floor_image_s_item;
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, FloorBean.ItemInfoListBean.ItemContentListBean item) {
        int width= (int)((AndroidUtil.getWindowWith()- mContext.getResources().getDimension(R.dimen.dp_20))/getDatas().size());
        BaseImage.getInstance().loadAutoHeight(holder.getView(R.id.image),item.imageUrl,width);
        holder.setOnItemClickListener(view ->FloorTools.getInstance(mActivity).onBindClick(item.clickType, item.clickValue));
    }
}
