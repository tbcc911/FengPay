package com.happy.godpay.ui.transaction.ItemDecoration;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hzh.frame.widget.xrecyclerview.BaseRecyclerAdapter;

/**
 * @author hzh
 * @version 1.0
 * @date 2017/8/21 
 */
public class TransactionItemDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;
    private Paint mPaint;
    private int lineHeight;

    public TransactionItemDecoration(Context context) {
        this(context, com.hzh.frame.R.color.base_bg);
    }
    
    public TransactionItemDecoration(Context context, int lineColor) {
        this(context,lineColor,2);
    }

    public TransactionItemDecoration(Context context, int lineColor, float lineHeight) {
        mContext=context;
        mPaint=new Paint();
        mPaint.setColor(ContextCompat.getColor(mContext, lineColor));
        this.lineHeight= (int) lineHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int itemPosition = parent.getChildLayoutPosition(view);
        switch (parent.getAdapter().getItemViewType(itemPosition)){
            case BaseRecyclerAdapter.TYPE_HEADER:
                outRect.top= lineHeight;
                outRect.left= lineHeight;
                outRect.right= lineHeight;
                break;
            case BaseRecyclerAdapter.TYPE_FOOTER:
                outRect.top= lineHeight;
                outRect.left= lineHeight;
                outRect.right= lineHeight;
                outRect.bottom= lineHeight;
                break;
            case BaseRecyclerAdapter.TYPE_NORMAL:
                if(itemPosition==parent.getAdapter().getItemCount()-1){
                    outRect.bottom= lineHeight;
                }
                outRect.top= lineHeight;
                outRect.left= lineHeight;
                outRect.right= lineHeight;
                break;
        }
    }
}
