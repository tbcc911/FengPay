package com.happy.godpay.ui.main;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fzs.comn.widget.floor.adapter.FloorAdapter;
import com.fzs.comn.widget.floor.bean.FloorBean;
import com.fzs.comn.widget.floor.itemDecoration.FloorShopItemDecoration;
import com.happy.godpay.R;
import com.google.gson.Gson;
import com.hzh.frame.comn.callback.HttpCallBack;
import com.hzh.frame.core.HttpFrame.BaseHttp;
import com.hzh.frame.ui.fragment.BaseFM;
import com.hzh.frame.util.FileUtil;
import com.hzh.frame.util.StatusBarUtil;
import com.hzh.frame.widget.xlistview.XListViewFooter;
import com.hzh.frame.widget.xrefresh.XSwipeRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class MainFM extends BaseFM implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    public static final String TAG="MainFM";
    View baseView;
    XSwipeRefreshLayout swipeRefresh;
    RecyclerView recyclerView;
    private ImageView goTop;
    private LinearLayout title;
    private int[] pageInfo = new int[]{1, 1, 20};//int[0]:起始页码;int[1]:当前页码 ;int[2]:每页显示数
    private FloorAdapter adapter;
    private int itemSize = 0;

    @Override
    public boolean setTitleIsShow() {
        return false;
    }

    @Override
    protected void onCreateBase() {
        baseView = setContentView(R.layout.fm_rv_main);
        title = baseView.findViewById(R.id.title);
        title.addView(StatusBarUtil.createStatusBarView(getActivity(),0),0);
        baseView.findViewById(R.id.titleText).setOnClickListener(this);
        baseView.findViewById(R.id.title_left).setOnClickListener(this);
        baseView.findViewById(R.id.title_right).setOnClickListener(this);
        goTop = baseView.findViewById(R.id.goTop);
        goTop.setOnClickListener(this);
        swipeRefresh = baseView.findViewById(R.id.swipeRefresh);
        swipeRefresh.setColorSchemeResources(R.color.application_color);
        swipeRefresh.setOnRefreshListener(this);
        recyclerView = baseView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new FloorShopItemDecoration(getResources().getDimension(R.dimen.dp_10)));
        recyclerView.addOnScrollListener(new MyOnScrollListener());
        onRefresh();
        //初始化下拉刷新动画
        swipeRefresh.setProgressViewOffset(false, 0, 120);
        swipeRefresh.setRefreshing(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left:
                break;
            case R.id.titleText:
                break;
            case R.id.title_right:
                ARouter.getInstance().build("/mine/MineMessageRUI").navigation();
                break;
            case R.id.goTop:
                recyclerView.scrollToPosition(0);
                break;
        }
    }

    @Override
    public void onRefresh() {
        pageInfo[1] = pageInfo[0];
        loadNetWorkData();
    }

    public void loadNetWorkData() {
        JSONObject params=new JSONObject();
        try {
            params.put("page", pageInfo[1]);
            params.put("limit", pageInfo[2]);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        BaseHttp.getInstance().query("home/getFloor",params, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                FloorBean jsonBean = new Gson().fromJson(response.toString(), FloorBean.class);
                itemSize = jsonBean.count;
                loadView(jsonBean.data);
                swipeRefresh.setRefreshing(false);
                Flowable.just("")
                        .onBackpressureBuffer()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response1 -> swipeRefresh.setRefreshing(false));
            }
        }.cache());
    }

    public void loadView(List<FloorBean.ItemInfoListBean> listBean) {
        if (listBean != null && listBean.size() > 0) {
            if (pageInfo[0] == pageInfo[1]) {
                //下拉刷新 | 这里的listBean是下拉刷新出来的数据 | 包括loadHead数据
                if (adapter == null) {
                    adapter = new FloorAdapter(getActivity(), listBean,recyclerView);
                    //                    adapter = new MainInfoAdapter(getActivity(), listBean);
                    if (itemSize > 0 && itemSize >= pageInfo[2]) {
                        adapter.setFooterView(createFooterView());
                    } else {
                        adapter.removeFooterView();
                    }
                    recyclerView.setAdapter(adapter);
                } else {
                    if (itemSize > 0 && itemSize >= pageInfo[2]) {
                        adapter.setFooterView(createFooterView());
                    } else {
                        adapter.removeFooterView();
                    }
                    adapter.setDatas(listBean);
                }
                //关闭下拉刷新
                Flowable.just("")
                        .onBackpressureBuffer()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String response) throws IOException {
                                swipeRefresh.setRefreshing(false);
                            }
                        });
            } else {
                //加载更多 | 这里的listBean是加载更多出来的数据 | 不包括loadHead数据
                adapter.getDatas().addAll(listBean);
                if (adapter.getFooterView() != null) {
                    adapter.getFooterView().setClickable(true);
                    ((XListViewFooter) adapter.getFooterView()).setState(XListViewFooter.STATE_NORMAL);
                }
                if (listBean.size() < pageInfo[2]) {
                    adapter.removeFooterView();
                    //这里隐藏了footerView,会导致adapter.getItemCount()查询出来的总数少1,为保证数据一致所以这里+1
                    adapter.notifyItemRangeInserted(adapter.getItemCount() - listBean.size() + 1, listBean.size());
                } else {
                    //批量添加刷新从positionStart开始itemCount数量的item了（这里的刷新指回调onBindViewHolder()方法）
                    adapter.notifyItemRangeInserted(adapter.getItemCount() - listBean.size(), listBean.size());
                }
            }
        } else {
            closeLoadingAll();
        }
    }

    //关闭所有加载中状态View
    public void closeLoadingAll() {
        //关闭下拉刷新
        Flowable.just("")
                .onBackpressureBuffer()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws IOException {
                        swipeRefresh.setRefreshing(false);
                    }
                });
        //关闭加载更多
        if (adapter != null && adapter.getFooterView() != null) {
            adapter.removeFooterView();
        }
    }


    /**
     * 创建脚布局
     */
    public XListViewFooter createFooterView() {
        XListViewFooter mFooterView = new XListViewFooter(getActivity());
        mFooterView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        mFooterView.setState(XListViewFooter.STATE_NORMAL);//设置加载更多状态
        mFooterView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
        mFooterView.setClickable(true);
        mFooterView.setOnClickListener(new View.OnClickListener() {//绑定脚监听
            @Override
            public void onClick(View view) {
                //保证多次响应只响应一次
                if (adapter.getFooterView().isClickable()) {
                    view.setClickable(false);
                    ((XListViewFooter) view).setState(XListViewFooter.STATE_LOADING);
                    pageInfo[1]++;
                }
            }
        });
        return mFooterView;
    }

    // RecyclerView的滑动监听事件
    private class MyOnScrollListener extends RecyclerView.OnScrollListener {
        private int alpha = 0;
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView,dx,dy);
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
            int[] position = null;
            position=layoutManager.findFirstVisibleItemPositions(position);
            if (position[0] > 0) {
                title.setBackgroundColor(Color.parseColor("#" + Integer.toHexString(250) + "2F80ED"));
            } else {
                View firstView = layoutManager.findViewByPosition(position[0]);
                // 获取列表第一个Item的高度作为最大透明区间
                int maxHeight = firstView.getHeight();
                int headTop = -firstView.getTop();
                if (headTop >= 0) {
                    if(headTop == 0){
                        goTop.setVisibility(View.GONE);
                    }else{
                        goTop.setVisibility(View.VISIBLE);
                    }
                    if (headTop <= maxHeight) {
                        alpha = headTop * 255 / maxHeight;
                    } else {
                        alpha = 255;
                    }
                    // 10进制转16进制数
                    if (alpha < 16) {
                        title.setBackgroundColor(Color.parseColor("#" + "0" + Integer.toHexString(alpha) + "2F80ED"));
                    } else
                    if(alpha<250){
                        title.setBackgroundColor(Color.parseColor("#" + Integer.toHexString(alpha) + "2F80ED"));
                    } else{
                        title.setBackgroundColor(Color.parseColor("#" + Integer.toHexString(250) + "2F80ED"));
                    }
                }
            }
        }

        /**
         * SCROLL_STATE_IDLE表示当前并不处于滑动状态 
         * SCROLL_STATE_DRAGGING表示当前RecyclerView处于滑动状态（手指在屏幕上） 
         * SCROLL_STATE_SETTLING表示当前RecyclerView处于滑动状态，（手已经离开屏幕）
         * */
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState==RecyclerView.SCROLL_STATE_IDLE && isVisBottom(recyclerView)){
                if(adapter.getFooterView()!=null && adapter.getFooterView().getVisibility()==View.VISIBLE){
                    adapter.getFooterView().performClick();
                }
            }
        }
    }

    //判断是否到达底部
    public static boolean isVisBottom(RecyclerView recyclerView) {
        StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
        //屏幕中最后一个可见子项的position
        int[] lastVisibleItemPosition = null;
        lastVisibleItemPosition=layoutManager.findFirstVisibleItemPositions(lastVisibleItemPosition);
        //当前屏幕所看到的子项个数
        int visibleItemCount = layoutManager.getChildCount();
        //当前RecyclerView的所有子项个数
        int totalItemCount = layoutManager.getItemCount();
        //RecyclerView的滑动状态
        int state = recyclerView.getScrollState();
        if (visibleItemCount > 0 && lastVisibleItemPosition[0] == totalItemCount - 1 && state == recyclerView.SCROLL_STATE_IDLE) {
            return true;
        } else {
            return false;
        }
    }
}