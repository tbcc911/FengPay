package com.fzs.comn.widget.floor.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fzs.comn.R;
import com.fzs.comn.tools.Util;
import com.fzs.comn.widget.floor.FloorTools;
import com.fzs.comn.widget.floor.banner.BGABanner;
import com.fzs.comn.widget.floor.banner.listener.BannerOnPageChangeListener;
import com.fzs.comn.widget.floor.banner.view.BannerBGView;
import com.fzs.comn.widget.floor.bean.FloorBean;
import com.fzs.comn.widget.imageview.ExpandImageView;
import com.fzs.comn.widget.switcher.UpDownSwitcher;
import com.hzh.frame.core.ImageFrame.BaseImage;
import com.hzh.frame.util.AndroidUtil;
import com.hzh.frame.widget.xrecyclerview.BaseRecyclerAdapter;
import com.hzh.frame.widget.xrecyclerview.RecyclerViewHolder;
import com.hzh.frame.widget.xviewpager.DecoratorViewPager;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2018/4/21
 */

public class FloorAdapter extends BaseRecyclerAdapter<FloorBean.ItemInfoListBean> {

    public static final int FLOOR_BANNER = 0xff01;
    public static final int FLOOR_TYPES = 0xff02;
    public static final int FLOOR_BULLETIN = 0xff03;
    public static final int FLOOR_SECKILL_HEADER = 0xff04;
    public static final int FLOOR_SECKILL_CONTENT = 0xff05;
    public static final int FLOOR_IMAGE_S = 0xff06;
    public static final int FLOOR_IMAGE_ROLL = 0xff07;
    public static final int FLOOR_SHOP_ITEM = 0xff08;
    public static final int FLOOR_NEWS_HEADER = 0xff09;
    public static final int FLOOR_NEWS_CONTENT_LEFT = 0xff10;
    public static final int FLOOR_NEWS_CONTENT_RIGHT = 0xff11;


    private SparseArray<Integer> layouts;
    private CountDownTimer timer;
    protected Activity mActivity;
    private RecyclerView mRecyclerView;

    /**
     * 加载所需要的Item布局文件及数据
     */
    public FloorAdapter(Activity activity, List<FloorBean.ItemInfoListBean> list, RecyclerView recyclerView) {
        super(activity, list);
        mActivity = activity;
        mRecyclerView = recyclerView;
        if (layouts == null) {
            layouts = new SparseArray<>();
        }
        layouts.put(FLOOR_BANNER, R.layout.comn_floor_banner);//头
        layouts.put(FLOOR_TYPES, R.layout.comn_floor_types);//分类
        layouts.put(FLOOR_BULLETIN, R.layout.comn_floor_bulletin);//资讯
        layouts.put(FLOOR_SECKILL_HEADER, R.layout.comn_floor_seckill_header);//秒杀头
        layouts.put(FLOOR_SECKILL_CONTENT, R.layout.comn_floor_seckill_content);//秒杀体
        layouts.put(FLOOR_NEWS_HEADER, R.layout.comn_floor_news_header);//新闻头
        layouts.put(FLOOR_NEWS_CONTENT_LEFT, R.layout.comn_floor_news_content_left);//新闻体
        layouts.put(FLOOR_NEWS_CONTENT_RIGHT, R.layout.comn_floor_news_content_right);//新闻体
        layouts.put(FLOOR_IMAGE_S, R.layout.comn_floor_image_s);//图片
        layouts.put(FLOOR_IMAGE_ROLL, R.layout.comn_floor_image_roll);//滑动图片
        layouts.put(FLOOR_SHOP_ITEM, R.layout.comn_floor_shop_item);
        layouts.put(TYPE_NORMAL, R.layout.comn_floor_shop_item);//Item
    }

    @Override
    /**
     * 设置Item类型
     * */ public int setItemChildViewType(int position) {
        //将返回的后端Json数据Type转为本地对应的Type
        String itemType = getDatas().get(position).itemType;
        int returnType = TYPE_NORMAL;
        if ("topBanner".equals(itemType)) {
            returnType = FLOOR_BANNER;
        } else if ("category".equals(itemType)) {
            returnType = FLOOR_TYPES;
        } else if ("bulletin".equals(itemType)) {
            returnType = FLOOR_BULLETIN;
        } else if ("seckillHeader".equals(itemType)) {
            returnType = FLOOR_SECKILL_HEADER;
        } else if ("seckillContent".equals(itemType)) {
            returnType = FLOOR_SECKILL_CONTENT;
        } else if ("images".equals(itemType)) {
            returnType = FLOOR_IMAGE_S;
        } else if ("imageRoll".equals(itemType)) {
            returnType = FLOOR_IMAGE_ROLL;
        } else if ("shopItem".equals(itemType)) {
            returnType = FLOOR_SHOP_ITEM;
        } else if ("newsHeader".equals(itemType)) {
            returnType = FLOOR_NEWS_HEADER;
        } else if ("newsContent".equals(itemType)) {
            if(position%3==0){
                returnType = FLOOR_NEWS_CONTENT_LEFT;
            }else{
                returnType = FLOOR_NEWS_CONTENT_RIGHT;
            }
        }
        return returnType;
    }


    @Override
    /**
     * 设置Item布局
     * */
    public int getItemLayoutId(int viewType) {
        return layouts.get(viewType);
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, FloorBean.ItemInfoListBean item) {
        if ("topBanner".equals(item.itemType)) {
            bindTopBannerData(holder, item, position);
        } else if ("category".equals(item.itemType)) {
            bindIconListData(holder, item, position);
        } else if ("bulletin".equals(item.itemType)) {
            bindBulletinData(holder, item, position);
        } else if ("seckillHeader".equals(item.itemType)) {
            bindSpikeHeaderData(holder, item, position);
        } else if ("seckillContent".equals(item.itemType)) {
            bindSpikeContentData(holder, item, position);
        } else if ("images".equals(item.itemType)) {
            bindImagesData(holder, item, position);
        } else if ("imageRoll".equals(item.itemType)) {
            bindImageRollData(holder, item, position);
        } else if ("shopItem".equals(item.itemType)) {
            bindShopItemData(holder, item, position);
        } else if ("newsHeader".equals(item.itemType)) {
            bindNewsHeaderData(holder, item, position);
        } else if ("newsContent".equals(item.itemType)) {
            bindNewsContentData(holder, item, position);
        }
    }

    @Override // 解决StaggeredGridLayoutManager占满一行
    public void onViewAttachedToWindow(RecyclerViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            if (getItemViewType(holder.getLayoutPosition()) != FLOOR_SHOP_ITEM) {
                //楼层占满整行
                ((StaggeredGridLayoutManager.LayoutParams) lp).setFullSpan(true);
            } else {
                //商品按实际设置行数
                ((StaggeredGridLayoutManager.LayoutParams) lp).setFullSpan(false);
            }
        }
    }

    /**
     * 绑定banner数据
     */
    private void bindTopBannerData(RecyclerViewHolder holder, FloorBean.ItemInfoListBean item, int position) {
        BGABanner banner = holder.getView(R.id.banner);
        banner.setDelegate((BGABanner.Delegate<View, FloorBean.ItemInfoListBean.ItemContentListBean>) (banner1, itemView, model, position1) ->
                FloorTools.getInstance(mActivity).onBindClick(item.itemContentList.get(position1).clickType, item.itemContentList.get(position1).clickValue)
        );
        banner.setAdapter((BGABanner.Adapter<View, FloorBean.ItemInfoListBean.ItemContentListBean>) (banner12, itemView, model, position12) -> {
            ExpandImageView ExpandImageView = itemView.findViewById(R.id.sdv_item_fresco_content);
            ExpandImageView.setImageURI(Uri.parse(model.imageUrl));
        });
        banner.setData(R.layout.comn_floor_item_banner, item.itemContentList, null);

        BannerBGView iArcView = holder.getView(R.id.bgView);

        //标题栏高度+状态栏高度
        int titleHeight = AndroidUtil.getStatusBarHeight() + Util.dip2px(mActivity, 48);
        int bgViewHeight = (int) (AndroidUtil.getWindowWith() / banner.getAspectRatio());
        iArcView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, bgViewHeight + titleHeight));

        //滑动监听
        banner.setOnPageChangeListener(new BannerOnPageChangeListener(iArcView, item.itemContentList));
        iArcView.setPaintColor(item.itemContentList.get(0).color);
    }


    /**
     * 绑定分类数据
     */
    private void bindIconListData(RecyclerViewHolder holder, FloorBean.ItemInfoListBean item, int position) {
        if (!TextUtils.isEmpty(item.itemContentList.get(0).bgImageUrl)) {
            ((ExpandImageView) holder.getView(R.id.icon_list_bg)).setImageURI(item.itemContentList.get(0).bgImageUrl);
        } else {
            ((ExpandImageView) holder.getView(R.id.icon_list_bg)).setImageResource(0);
        }
        int viewHeight = (int) ((AndroidUtil.getWindowWith() - (Util.dip2px(mActivity, 20))) / 2.25f);//这个高度与背景图片比例对应,具体可看布局
        ((DecoratorViewPager) holder.getView(R.id.viewPager)).getLayoutParams().height = viewHeight;
        //使用拦截滑动事件,让viewPage的滑动不响应RecyclerView
        //        ((DecoratorViewPager)holder.getView(R.id.viewPager)).setNestedpParent((ViewGroup) ((DecoratorViewPager)holder.getView(R.id.viewPager)).getParent());
        holder.getView(R.id.viewPager).setVisibility(View.VISIBLE);
        ((DecoratorViewPager) holder.getView(R.id.viewPager)).setAdapter(new FloorTypesViewPagerAdapter(mActivity,item.itemContentList, viewHeight, 2, 4));
    }


    /**
     * 绑定资讯信息
     */
    private void bindBulletinData(RecyclerViewHolder holder, FloorBean.ItemInfoListBean item, int position) {
        UpDownSwitcher viewSwitcher = holder.getView(R.id.home_view_switcher);
        viewSwitcher.setSwitcheNextViewListener((nextView, index) -> {
            if (nextView == null)
                return;
            String title = item.itemContentList.get(index % item.itemContentList.size()).title;
            String titleColor = item.itemContentList.get(index % item.itemContentList.size()).color;
            String subTitle = item.itemContentList.get(index % item.itemContentList.size()).subTitle;
            String subTitleColor = item.itemContentList.get(index % item.itemContentList.size()).subColor;
            ((TextView) nextView.findViewById(R.id.textview)).setText(subTitle);
            ((TextView) nextView.findViewById(R.id.textview)).setTextColor(Color.parseColor(Util.isEmpty(subTitleColor) ? "#333333" : subTitleColor));
            ((TextView) nextView.findViewById(R.id.switch_title_text)).setText(title);
            ((TextView) nextView.findViewById(R.id.switch_title_text)).setTextColor(Color.parseColor(titleColor));
            nextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FloorTools.getInstance(mActivity).onBindClick(item.itemContentList.get(index % item.itemContentList.size()).clickType, item.itemContentList.get(index % item.itemContentList.size()).clickValue);
                }
            });
        });
        viewSwitcher.setContentLayout(R.layout.comn_floor_bulletin_switch_view);
        holder.setClickListener(R.id.more, view -> {
            ARouter.getInstance().build("/mine/MineMessageRUI").navigation();
        });
    }


    /**
     * 绑定秒杀头信息
     */
    private void bindSpikeHeaderData(RecyclerViewHolder holder, FloorBean.ItemInfoListBean item, int position) {
        holder.setText(R.id.spike_time_field, item.itemContentList.get(0).title);
        holder.setText(R.id.spike_header_desc, item.itemContentList.get(0).subTitle);
        if(Util.isEmpty(item.itemContentList.get(0).time)){
            holder.getView(R.id.spike_time).setVisibility(View.GONE);
        }else{
            holder.getView(R.id.spike_time).setVisibility(View.VISIBLE);
            String time = item.itemContentList.get(0).time;
            if (TextUtils.isEmpty(time) || !time.matches("^[0-9]*$")) return;
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            timer = new CountDownTimer(Long.parseLong(time), 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    long temp = millisUntilFinished / 1000;
                    long hours = temp / 3600;
                    long minutes = (temp - (3600 * hours)) / 60;
                    long seconds = temp - (3600 * hours) - (60 * minutes);
                    holder.setText(R.id.spike_time_hour, hours > 9 ? "" + hours : "0" + hours);
                    holder.setText(R.id.spike_time_minute, minutes > 9 ? "" + minutes : "0" + minutes);
                    holder.setText(R.id.spike_time_seconds, seconds > 9 ? "" + seconds : "0" + seconds);
                    item.itemContentList.get(0).time=(millisUntilFinished-1000)+"";
                }

                @Override
                public void onFinish() {
                    holder.setText(R.id.spike_time_hour, "00");
                    holder.setText(R.id.spike_time_minute, "00");
                    holder.setText(R.id.spike_time_seconds, "00");
                    item.itemContentList.get(0).time="0";
                }
            }.start();
        }
    }


    /**
     * 绑定秒杀内容信息
     */
    private void bindSpikeContentData(RecyclerViewHolder holder, FloorBean.ItemInfoListBean item, int position) {
        if (item.itemContentList == null || item.itemContentList.size() <= 0) return;
        RecyclerView recyclerView = holder.getView(R.id.spike_content_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        FloorSeckillContentAdapter adapter = new FloorSeckillContentAdapter(mActivity, item.itemContentList);
        recyclerView.setAdapter(adapter);
    }


    /**
     * 绑定图片类型信息
     */
    private void bindImagesData(RecyclerViewHolder holder, FloorBean.ItemInfoListBean item, int position) {
        if (item.itemContentList == null || item.itemContentList.size() <= 0) return;
        RecyclerView recyclerView = holder.getView(R.id.imagesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false){
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        });
        FloorImagesAdapter adapter = new FloorImagesAdapter(mActivity, item.itemContentList);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 绑定滑动图片类型信息
     */
    private void bindImageRollData(RecyclerViewHolder holder, FloorBean.ItemInfoListBean item, int position) {
        BGABanner banner = holder.getView(R.id.banner);
        banner.setDelegate(
                (BGABanner.Delegate<View, FloorBean.ItemInfoListBean.ItemContentListBean>) (banner1, itemView, model, position1) -> {
                    FloorTools.getInstance(mActivity).onBindClick(item.itemContentList.get(position1).clickType, item.itemContentList.get(position1).clickValue);
                }
        );
        banner.setAdapter((BGABanner.Adapter<View, FloorBean.ItemInfoListBean.ItemContentListBean>) (banner12, itemView, model, position12) -> {
            ExpandImageView ExpandImageView = itemView.findViewById(R.id.sdv_item_fresco_content);
            ExpandImageView.setImageURI(Uri.parse(model.imageUrl));
        });
        banner.setData(R.layout.comn_floor_item_image_roll, item.itemContentList, null);
    }

    /**
     * 绑定新闻头
     */
    private void bindNewsHeaderData(RecyclerViewHolder holder, FloorBean.ItemInfoListBean item, int position) {
        FloorBean.ItemInfoListBean.ItemContentListBean model = item.itemContentList.get(0);
        holder.setText(R.id.title, model.title);
        holder.setText(R.id.content, model.subTitle);
        BaseImage.getInstance().loadAutoWidth(holder.getView(R.id.image), model.imageUrl, (int)(mActivity.getResources().getDimension(R.dimen.dp_15)));
        onBindClick(model.clickType, model.clickValue, holder.itemView);
    }

    /**
     * 绑定新闻体
     */
    private void bindNewsContentData(RecyclerViewHolder holder, FloorBean.ItemInfoListBean item, int position) {
        FloorBean.ItemInfoListBean.ItemContentListBean model = item.itemContentList.get(0);
        holder.setText(R.id.title, model.title);
        holder.setText(R.id.time, "日期 : "+model.time);
        if(Util.isEmpty(model.imageUrl)){
            holder.getView(R.id.image).setVisibility(View.GONE);
        }else{
            holder.getView(R.id.image).setVisibility(View.VISIBLE);
            BaseImage.getInstance().loadXY(model.imageUrl,holder.getView(R.id.image));
        }
        onBindClick(model.clickType, model.clickValue, holder.itemView);
    }

    /**
     * 绑定Item类型信息
     */
    private void bindShopItemData(RecyclerViewHolder holder, FloorBean.ItemInfoListBean item, int position) {
        FloorBean.ItemInfoListBean.ItemContentListBean model = item.itemContentList.get(0);
        holder.setText(R.id.name, model.title);
        holder.setText(R.id.priceX, model.price);
        holder.setText(R.id.priceY, model.oldPrice);
        holder.getTextView(R.id.priceY).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.setText(R.id.number, model.buyNumber);
        int imageWidth = (int) ((AndroidUtil.getWindowWith() - mActivity.getResources().getDimension(R.dimen.dp_30)) / 2.0 );
        BaseImage.getInstance().loadAutoHeight(holder.getView(R.id.image), model.imageUrl, imageWidth);
        if (Integer.parseInt(model.stockNumber) <= 10 && Integer.parseInt(model.stockNumber) >= 1) {
            holder.setText(R.id.stock, "仅剩" + Integer.parseInt(model.stockNumber) + "件");
            holder.getView(R.id.stock).setBackgroundDrawable(getBadgeDrawable(mContext));
            holder.getView(R.id.stock).setVisibility(View.VISIBLE);
        } else {
            holder.getView(R.id.stock).setVisibility(View.GONE);
        }
        onBindClick(model.clickType, model.clickValue, holder.itemView);
    }

    //绑定View的点击事件
    public void onBindClick(String type, String value, View view) {
        view.setOnClickListener(view1 -> FloorTools.getInstance(mActivity).onBindClick(type, value));
    }


    //获取徽章Drawable
    public GradientDrawable getBadgeDrawable(Context context) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(context.getResources().getDimensionPixelSize(R.dimen.badge_corner_radius));
        shape.setColor(ContextCompat.getColor(context, R.color.title_c_7f_black));//BackgroundColor
        shape.setStroke(0, ContextCompat.getColor(context, R.color.white));//BorderWidth,BorderColor
        return shape;
    }
}
