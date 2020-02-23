package com.fzs.mine.ItemDecoration;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hzh.frame.R;
import com.hzh.frame.util.Util;
import com.hzh.frame.widget.xrecyclerview.BaseRecyclerAdapter;

/**
 * @author hzh
 * @version 1.0
 * @date 2017/8/21 
 */
public class MessageItemDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;
    private Paint mPaint;
    private int lineHeight;

    public MessageItemDecoration(Context context) {
        this(context, R.color.base_bg);
    }
    
    public MessageItemDecoration(Context context, int lineColor) {
        this(context,lineColor,2);
    }

    public MessageItemDecoration(Context context, int lineColor, int lineHeight) {
        mContext=context;
        mPaint=new Paint();
        mPaint.setColor(ContextCompat.getColor(mContext, lineColor));
        this.lineHeight=lineHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int itemPosition = parent.getChildLayoutPosition(view);
        switch (parent.getAdapter().getItemViewType(itemPosition)){
            case BaseRecyclerAdapter.TYPE_HEADER:
                outRect.bottom= lineHeight;
                break;
            case BaseRecyclerAdapter.TYPE_FOOTER:
                break;
            case BaseRecyclerAdapter.TYPE_NORMAL:
                if(itemPosition==parent.getAdapter().getItemCount()-1){
                    outRect.bottom= lineHeight;
                }
                outRect.top= Util.dip2px(mContext,10);
                outRect.left= Util.dip2px(mContext,10);
                outRect.right= Util.dip2px(mContext,10);
                break;
        }
    }
}
