package com.fzs.mine.ui;

import android.support.v7.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fzs.comn.model.MineCollection;
import com.fzs.comn.widget.imageview.ExpandImageView;
import com.fzs.mine.ItemDecoration.MessageItemDecoration;
import com.fzs.mine.R;
import com.hzh.frame.ui.activity.AbsRecyclerViewUI;
import com.hzh.frame.util.FileUtil;
import com.hzh.frame.util.Util;
import com.hzh.frame.widget.xrecyclerview.RecyclerViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 */
@Route(path = "/mine/MineFootprintRUI")
public class MineFootprintRUI extends AbsRecyclerViewUI<MineCollection> {
    
    @Override
    protected int setLayoutId() {
        return R.layout.mine_footprint_list;
    }

    @Override
    protected RecyclerView.ItemDecoration setRecyclerViewItemDecoration() {
        return new MessageItemDecoration(getApplicationContext(), R.color.base_bg,Util.dip2px(this,5));
    }

    @Override
    protected void bindView() {
        getTitleView().setContent("我的足迹");
        showLoding();
    }

    @Override
    protected String setHttpPath() {
        return "member/login";
    }
 
    @Override
    protected List<MineCollection> handleHttpData(JSONObject response) {
        //读取本地json文件
        try {
            response=new JSONObject(FileUtil.readTextFromFile(getApplicationContext(),"json/MineCollection.json"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        List<MineCollection> listModel = new ArrayList<>();
        if (response.optInt("code") == 200) {
            JSONArray data = response.optJSONArray("data");
            if (data != null && data.length() > 0) {
                for (int i = 0; i < data.length(); i++) {
                    JSONObject model = data.optJSONObject(i);
                    MineCollection collection = new MineCollection();
                    collection.setMemberId(model.optLong("memberId"));
                    collection.setProductId(model.optString("productId"));
                    collection.setMemberNickname(model.optString("memberNickname"));
                    collection.setProductName(model.optString("productName"));
                    collection.setProductPrice(model.optString("productPrice"));
                    collection.setStoreLogoImage(model.optString("storeLogoImage"));
                    listModel.add(collection);
                }
            }
        }else{
            alert(response.optString("message"));
        }
        dismissLoding();
        return listModel;
    }

    @Override
    protected void handleHttpDataFailure() {
        showLodingFail();
    }

    @Override
    protected int setItemLayoutId(int viewType) {
        return R.layout.mine_footprint_manage;
    }

    @Override
    protected void bindItemData(RecyclerViewHolder holder, int position, MineCollection model) {
        ExpandImageView imageView = holder.getView(R.id.productImage);
        imageView.setImageURI(model.getStoreLogoImage());
        holder.setText(R.id.storeName,model.getProductName());
        holder.setText(R.id.productName,model.getProductName());
        holder.setText(R.id.guige,"规格:"+ model.getProductName());
        holder.setText(R.id.productPrice,"￥" +model.getProductPrice());
        holder.setText(R.id.time,"浏览时间:" +"2019-12-12 11:11:11" );
        holder.setOnItemClickListener(v -> 
                ARouter.getInstance()
                        .build("/store/StoreGoodsInfoUI")
                        .withString("productId",model.getProductId())
                        .navigation()
        );
        
    }
    
}
