package com.happy.godpay.ui.transaction;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fzs.comn.model.TransactionOrder;
import com.happy.godpay.R;
import com.happy.godpay.ui.transaction.ItemDecoration.TransactionItemDecoration;
import com.hzh.frame.ui.fragment.AbsRecyclerViewFM;
import com.hzh.frame.util.Util;
import com.hzh.frame.widget.rxbus.MsgEvent;
import com.hzh.frame.widget.rxbus.RxBus;
import com.hzh.frame.widget.xrecyclerview.RecyclerViewHolder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TransactionRechargeRFM extends AbsRecyclerViewFM<TransactionOrder> {
    private View headView;
    private TextView sumMoney;
    private TextView successMoney;
    private TextView frozenMoney;
    public static TransactionRechargeRFM newInstance(String type) {
        TransactionRechargeRFM myFragment = new TransactionRechargeRFM();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        myFragment.setArguments(bundle);
        return myFragment;
    }
    
    @Override
    protected void bindView(View view) {
        headView = getAdapter().getHeaderView();
        sumMoney = headView.findViewById(R.id.sumMoney);
        successMoney = headView.findViewById(R.id.successMoney);
        frozenMoney = headView.findViewById(R.id.frozenMoney);
        getEvent();
    }
    
    @Override
    public boolean setTitleIsShow() {
        return false;
    }

    @Override
    protected RecyclerView.ItemDecoration setRecyclerViewItemDecoration() {
        return new TransactionItemDecoration(getActivity(), R.color.base_bg, getResources().getDimension(R.dimen.dp_10));
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fm_rv_transaction_order;
    }

    @Override
    protected int setHeadLayoutId() {
        return R.layout.head_rv_transaction_recharge;
    }
    
    @Override
    protected String setHttpPath() {
        return "finance/getRechargeDetail";
    }

    @Override
    protected List<TransactionOrder> handleHttpData(JSONObject response) {
        List<TransactionOrder> listModel = new ArrayList<>();
        if (response.optInt("code") == 200) {
            JSONObject data = response.optJSONObject("data");
            if (data != null && data.length() > 0){
                sumMoney.setText(data.optString("total"));
                successMoney.setText(data.optString("succeed"));
                frozenMoney.setText(data.optString("audit"));
                JSONArray list = data.optJSONArray("list");
                if (list != null && list.length() > 0) {
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject obj = list.optJSONObject(i);
                        TransactionOrder model = new TransactionOrder();
                        model.setAuditTime(obj.optString("auditTime"));
                        model.setCreateTime(obj.optString("createTime"));
                        model.setState(obj.optString("rechargeStatus"));
                        model.setStatusName(obj.optString("rechargeStatusName"));
                        model.setType(obj.optString("rechargeType"));
                        model.setMoney(obj.optString("value"));
                        listModel.add(model);
                    }
                }
            }
        }
        dismissLoding();
        return listModel;
    }
    
    @Override
    protected int setItemLayoutId(int viewType) {
        return R.layout.item_rv_transaction;
    }

    @Override
    protected void bindItemData(RecyclerViewHolder holder, int position, TransactionOrder model) {
        holder.setText(R.id.time,"创建时间:" + model.getCreateTime());
        holder.setText(R.id.money,"¥"+model.getMoney());
        holder.setText(R.id.state,model.getStatusName());
        if (!Util.isEmpty(model.getAuditTime())){
            holder.getView(R.id.paytime).setVisibility(View.VISIBLE);
            holder.setText(R.id.paytime,"审核时间:" + model.getAuditTime());
        }else {
            holder.getView(R.id.paytime).setVisibility(View.GONE);
        }
        if (!Util.isEmpty(model.getState())){
            if("1".equals(model.getState())){
                holder.setTextColor(R.id.state,"#228B22");
            } else
            if("2".equals(model.getState())){
                holder.setTextColor(R.id.state,"#837DF9");
            } else
            if("3".equals(model.getState())){
                holder.setTextColor(R.id.state,"#ff0000");
            }
        }else {
            holder.setTextColor(R.id.state,"#ff0000");
        }
        if ("0".equals(model.getType())){
            holder.getImageView(R.id.type).setImageResource(R.mipmap.base_image_bank);
        }else if ("1".equals(model.getType())){
            holder.getImageView(R.id.type).setImageResource(R.mipmap.base_image_alipay);
        }else if ("2".equals(model.getType())){
            holder.getImageView(R.id.type).setImageResource(R.mipmap.base_image_wchat);
        }else if ("3".equals(model.getType())){
            holder.getImageView(R.id.type).setImageResource(R.mipmap.base_image_usdt);
        }
    }

    private void getEvent(){
        RxBus.getInstance()
                .toObservable(this, MsgEvent.class)
                .filter(msgEvent -> msgEvent.getTag().equals(TransactionFM.TAG))
                .subscribe(msgEvent -> {
                    if (!com.fzs.comn.tools.Util.isEmpty(msgEvent.getMsg().toString())){
                        if (msgEvent.getMsg().toString().equals("1")){
                            onRefresh();
                        }
                    }
                });

    }

    @Override
    public void onRefresh() {
        super.onRefresh();
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            onRefresh();
        } else {
        }
    }
    
}
