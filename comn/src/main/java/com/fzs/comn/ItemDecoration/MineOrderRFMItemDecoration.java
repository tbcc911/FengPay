package com.fzs.comn.ItemDecoration;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fzs.comn.tools.Util;
import com.hzh.frame.widget.xrecyclerview.BaseRecyclerAdapter;

/**
 * @author hzh
 * @version 1.0
 * @date 2017/8/21
 */
public class MineOrderRFMItemDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;
    int itemFirstPosition = -1;

    public MineOrderRFMItemDecoration(Context context) {
        mContext = context;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int itemPosition = parent.getChildLayoutPosition(view);
        switch (parent.getAdapter().getItemViewType(itemPosition)) {
            case BaseRecyclerAdapter.TYPE_HEADER:
                break;
            case BaseRecyclerAdapter.TYPE_FOOTER:
                break;
            case BaseRecyclerAdapter.TYPE_NORMAL:
                if (itemFirstPosition == -1) {
                    itemFirstPosition = itemPosition;
                }
                if (itemFirstPosition == itemPosition) {
                    outRect.top = Util.dip2px(mContext, 10);
                    outRect.bottom = Util.dip2px(mContext, 10);
                    outRect.left = Util.dip2px(mContext, 10);
                    outRect.right = Util.dip2px(mContext, 10);
                } else {
                    outRect.bottom = Util.dip2px(mContext, 10);
                    outRect.left = Util.dip2px(mContext, 10);
                    outRect.right = Util.dip2px(mContext, 10);
                }
                break;
        }
    }
}
