package com.fzs.comn.widget.floor.itemDecoration;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.fzs.comn.R;
import com.fzs.comn.widget.floor.adapter.FloorAdapter;
import com.hzh.frame.widget.xrecyclerview.BaseRecyclerAdapter;

/**
 * @author：admin on 2017/4/7 10:43.
 */

public class FloorShopItemDecoration extends RecyclerView.ItemDecoration {
    private float space;
    private int itemFirstPosition = -1;

    public FloorShopItemDecoration(float space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            getGridLayoutItemOffsets(outRect, view, parent, state);
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            getStaggeredGridLayoutItemOffsets(outRect, view, parent, state);
        }
    }


    /**
     * GridLayoutManager
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    private void getGridLayoutItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int itemPosition = parent.getChildLayoutPosition(view);
        switch (parent.getAdapter().getItemViewType(itemPosition)) {
            case BaseRecyclerAdapter.TYPE_HEADER:
                break;
            case BaseRecyclerAdapter.TYPE_FOOTER:
                break;
            case FloorAdapter.FLOOR_SHOP_ITEM:
            case BaseRecyclerAdapter.TYPE_NORMAL://这里BaseRecyclerAdapter的setItemChildViewType被ShopMainAdapter重写了,故BaseRecyclerAdapter.TYPE_NORMAL被细分成了很多个类型
                if (itemFirstPosition == -1) {
                    itemFirstPosition = itemPosition;
                }
                switch (itemPosition - itemFirstPosition) {
                    case 0://item第一排 | 左边Item
                        break;
                    case 1://item第一排 | 右边Item
                        break;
                    default://item第二排开始到最后
                        if ((itemPosition - itemFirstPosition) % 2 == 0) {
                            //左边Item
                            outRect.right = (int) (space / 2);
                            outRect.bottom = (int) space;
                            outRect.left = (int) space;
                        } else {
                            //右边Item
                            outRect.left = (int) (space / 2);
                            outRect.bottom = (int) space;
                            outRect.right = (int) space;
                        }
                        break;
                }
                break;
            case FloorAdapter.FLOOR_NEWS_CONTENT_LEFT:
            case FloorAdapter.FLOOR_NEWS_CONTENT_RIGHT:
                if (itemFirstPosition == -1) {
                    itemFirstPosition = itemPosition;
                }
                if (itemFirstPosition == itemPosition) {
                    //第一个Item
                    outRect.right = (int) space;
                    outRect.top = 2;
                    outRect.left = (int) space;
                    outRect.bottom = (int) (space/2);
                    view.setBackgroundResource(R.drawable.comn_bg_white_fillet_bottom);
                } else
                if(itemPosition+1 == parent.getAdapter().getItemCount()){
                    //最后一项Item
                    outRect.right = (int) space;
                    outRect.left = (int) space;
                    outRect.bottom = (int) (space/2);
                    view.setBackgroundResource(R.drawable.comn_bg_white_fillet);
                }else{
                    //中间项Item
                    outRect.right = (int) space;
                    outRect.left = (int) space;
                    outRect.bottom = (int) (space/2);
                    view.setBackgroundResource(R.drawable.comn_bg_white_fillet);
                }
                break;
        }
    }


    /**
     * StaggeredGridLayoutManager
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    private void getStaggeredGridLayoutItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int itemPosition = parent.getChildLayoutPosition(view);
        switch (parent.getAdapter().getItemViewType(itemPosition)) {
            case BaseRecyclerAdapter.TYPE_HEADER:
                break;
            case BaseRecyclerAdapter.TYPE_FOOTER:
                break;
            case FloorAdapter.FLOOR_SHOP_ITEM:
            case BaseRecyclerAdapter.TYPE_NORMAL://这里BaseRecyclerAdapter的setItemChildViewType被ShopMainAdapter重写了,故BaseRecyclerAdapter.TYPE_NORMAL被细分成了很多个类型
                if (itemFirstPosition == -1) {
                    itemFirstPosition = itemPosition;
                }
                switch (itemPosition - itemFirstPosition) {
                    case 0://item第一排 | 左边Item
                    case 1://item第一排 | 右边Item
                    default://item第二排开始到最后
                        StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
                        //参考 https://blog.csdn.net/tobevan/article/details/78924338
                        //如果是从左到右布局，最左边的第一项的getSpanIndex是0，如果从右到左布局，右边第一项的getSpanIndex是0
                        if (lp.getSpanIndex() % 2 == 0) {
                            //左边Item
                            outRect.right = (int) (space / 2);
                            outRect.bottom = (int) space;
                            outRect.left = (int) space;
                        } else {
                            //右边Item
                            outRect.left = (int) (space / 2);
                            outRect.bottom = (int) space;
                            outRect.right = (int) space;
                        }
                        break;
                }
                break;
            case FloorAdapter.FLOOR_NEWS_CONTENT_LEFT:
            case FloorAdapter.FLOOR_NEWS_CONTENT_RIGHT:
                if (itemFirstPosition == -1) {
                    itemFirstPosition = itemPosition;
                }
                if (itemFirstPosition == itemPosition) {
                    //第一个Item
                    outRect.right = (int) space;
                    outRect.top = 2;
                    outRect.left = (int) space;
                    outRect.bottom = (int) (space/2);
                    view.setBackgroundResource(R.drawable.comn_bg_white_fillet_bottom);
                } else
                if(itemPosition+1 == parent.getAdapter().getItemCount()){
                    //最后一项Item
                    outRect.right = (int) space;
                    outRect.left = (int) space;
                    outRect.bottom = (int) (space/2);
                    view.setBackgroundResource(R.drawable.comn_bg_white_fillet);
                }else{
                    //中间项Item
                    outRect.right = (int) space;
                    outRect.left = (int) space;
                    outRect.bottom = (int) (space/2);
                    view.setBackgroundResource(R.drawable.comn_bg_white_fillet);
                }
                break;
        }
    }
}
