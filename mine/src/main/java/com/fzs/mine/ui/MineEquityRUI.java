package com.fzs.mine.ui;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fzs.comn.model.MineEquity;
import com.fzs.mine.ItemDecoration.MessageItemDecoration;
import com.fzs.mine.R;
import com.hzh.frame.ui.activity.AbsRecyclerViewUI;
import com.hzh.frame.util.Util;
import com.hzh.frame.widget.x5webview.X5WebViewUI;
import com.hzh.frame.widget.xrecyclerview.RecyclerViewHolder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 */
@Route(path = "/mine/MineEquityRUI")
public class MineEquityRUI extends AbsRecyclerViewUI<MineEquity> {

    @Override
    protected int setLayoutId() {
        return R.layout.mine_ui_equity;
    }

    @Override
    protected RecyclerView.ItemDecoration setRecyclerViewItemDecoration() {
        return new MessageItemDecoration(getApplicationContext(), R.color.base_bg, Util.dip2px(this, 5));
    }

    @Override
    protected void bindView() {
        getTitleView().setContent("我的权益证书");
        showLoding();
    }

    @Override
    protected String setHttpPath() {
        return "member/getCertificateList";
    }

    @Override
    protected List<MineEquity> handleHttpData(JSONObject response) {
        List<MineEquity> listModel = new ArrayList<>();
        if (response.optInt("code") == 200) {
            JSONArray data = response.optJSONArray("data");
            if (data != null && data.length() > 0) {
                for (int i = 0; i < data.length(); i++) {
                    JSONObject model = data.optJSONObject(i);
                    MineEquity equity = new MineEquity();
                    equity.setAmount(model.optString("amount"));//购买金额
                    equity.setCertificateNum(model.optString("certificateNum"));//证书编号
                    equity.setCertificateUrl(model.optString("certificateUrl"));//权益证书地址
                    equity.setCreateTime(model.optString("createTime"));//生成时间
                    equity.setIncome(model.optString("income"));//收益
                    equity.setIncomeStatus(model.optString("incomeStatus"));//收益状态: 0->收益中; 1->已结束
                    equity.setMonth(model.optString("month"));//认养月份
                    equity.setName(model.optString("name"));//姓名
                    equity.setOverTime(model.optString("overTime"));//结束时间
                    equity.settbccReward(model.optString("tbccReward"));//tbcc奖励
                    equity.setQuantity(model.optString("quantity"));//购买数量
                    listModel.add(equity);
                }
            }
        } else {
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
        return R.layout.mine_item_rv_equity;
    }

    @Override
    protected void bindItemData(RecyclerViewHolder holder, int position, MineEquity model) {
        holder.setText(R.id.amount, "购买金额:"+model.getAmount());
        holder.setText(R.id.certificateNum, "编号:"+model.getCertificateNum());
        holder.setText(R.id.time, model.getCreateTime()+" 至 "+model.getOverTime());
        holder.setText(R.id.income, "预收益:"+model.getIncome());
        holder.setText(R.id.month, "认养月份:"+model.getMonth());

        holder.setText(R.id.name, "姓名:"+model.getName());
        holder.setText(R.id.tbccReward, "已收益:"+model.gettbccReward());
        holder.setText(R.id.quantity, "购买数量:"+model.getQuantity());
        
        if("0".equals(model.getIncomeStatus())){
            holder.setText(R.id.incomeStatus, "收益中");
            holder.setTextColor(R.id.incomeStatus,"#2F80ED");
//            holder.itemView.setBackgroundResource(R.drawable.mine_equity_status_normal);
        } else
        if("1".equals(model.getIncomeStatus())){
            holder.setText(R.id.incomeStatus, "已结束");
            holder.setTextColor(R.id.incomeStatus,"#ff0000");
//            holder.itemView.setBackgroundResource(R.drawable.mine_equity_status_end);
        }
        
        holder.setOnItemClickListener(v -> {
            Intent in = new Intent(MineEquityRUI.this, X5WebViewUI.class);
            in.putExtra("url", model.getCertificateUrl());
            in.putExtra("title", "权益证书");
            in.putExtra("showTitle", true);
            startActivity(in);
        });

    }

}
