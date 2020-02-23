package com.fzs.mine.ui;

import android.support.v7.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fzs.comn.model.MineMessage;
import com.fzs.mine.ItemDecoration.MessageItemDecoration;
import com.fzs.mine.R;
import com.hzh.frame.ui.activity.AbsRecyclerViewUI;
import com.hzh.frame.util.Util;
import com.hzh.frame.widget.xrecyclerview.RecyclerViewHolder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 */
@Route(path = "/mine/MineMessageRUI")
public class MineMessageRUI extends AbsRecyclerViewUI<MineMessage> {
    
    @Override
    protected int setLayoutId() {
        return R.layout.mine_ui_message;
    }

    @Override
    protected RecyclerView.ItemDecoration setRecyclerViewItemDecoration() {
        return new MessageItemDecoration(getApplicationContext(), R.color.base_bg,Util.dip2px(this,5));
    }

    @Override
    protected void bindView() {
        getTitleView().setContent("消息");
        showLoding();
    }

    @Override
    protected String setHttpPath() {
        return "other/getMessage";
    }
 
    @Override
    protected List<MineMessage> handleHttpData(JSONObject response) {
        List<MineMessage> listModel = new ArrayList<>();
        if (response.optInt("code") == 200) {
            JSONArray data = response.optJSONArray("data");
            if (data != null && data.length() > 0) {
                for (int i = 0; i < data.length(); i++) {
                    JSONObject model = data.optJSONObject(i);
                    MineMessage message = new MineMessage();
                    message.setTitle(model.optString("title"));
                    message.setContent(model.optString("content"));
                    message.setCreateTime(model.optString("createTime"));
                    message.setMessageCategoryName(model.optString("messageCategoryName"));
                    listModel.add(message);
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
        return R.layout.mine_item_message;
    }

    @Override
    protected void bindItemData(RecyclerViewHolder holder, int position, MineMessage model) {
        holder.setText(R.id.title,model.getTitle());
        holder.setText(R.id.system,model.getMessageCategoryName());
        holder.setText(R.id.time,model.getCreateTime());
        holder.setOnItemClickListener(v -> ARouter.getInstance().build("/mine/MineMessageInfoUI")
                .withString("id",model.getNid())
                .withString("title",model.getTitle())
                .withString("content",model.getContent())
                .withString("createTime",model.getCreateTime())
                .navigation());
    }
    
}
