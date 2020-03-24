package com.happy.godpay.ui.transaction;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fzs.comn.model.MineIntegration;
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

public class TransactionDetailedRFM extends AbsRecyclerViewFM<MineIntegration> {
    private View headView;
    private TextView sumMoney;
    private TextView successMoney;
    private TextView frozenMoney;
    private TextView freezeIntegration;
    public static TransactionDetailedRFM newInstance(String type) {
        TransactionDetailedRFM myFragment = new TransactionDetailedRFM();
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
        freezeIntegration = headView.findViewById(R.id.freezeIntegration);
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
        return R.layout.head_rv_transaction_detailed;
    }
    
    @Override
    protected String setHttpPath() {
        return "finance/getIntegrationDetail";
    }

    @Override
    protected List<MineIntegration> handleHttpData(JSONObject response) {
        List<MineIntegration> listModel = new ArrayList<>();
        if (response.optInt("code") == 200) {
            JSONObject data = response.optJSONObject("data");
            if (data != null && data.length() > 0){
                sumMoney.setText(data.optString("integration"));
                freezeIntegration.setText(data.optString("freezeIntegration"));
                successMoney.setText(data.optString("income"));
                frozenMoney.setText(data.optString("expend"));
                JSONArray list = data.optJSONArray("list");
                if (list != null && list.length() > 0) {
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject obj = list.optJSONObject(i);
                        MineIntegration model = new MineIntegration();
                        model.setCreateTime(obj.optString("createTime"));
                        model.setValue(obj.optString("value"));
                        model.setTradeType(obj.optString("tradeType"));
                        model.setTvtitle(obj.optString("title"));
                        model.setNote(obj.optString("note"));
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
        return R.layout.item_rv_integral;
    }

    @Override
    protected void bindItemData(RecyclerViewHolder holder, int position, MineIntegration model) {
        holder.setText(R.id.tvTitle,model.getTvtitle());
        holder.setText(R.id.money,model.getValue() + "积分");
        holder.setText(R.id.time,model.getCreateTime());
        holder.setText(R.id.desc,model.getNote());
        if (Util.isEmpty(model.getTradeType())){
            if (model.getTradeType().equals("0")){
                holder.setText(R.id.flag,"-");
                holder.setText(R.id.annotation,"收入");
            }else if (model.getTradeType().equals("1")){
                holder.setText(R.id.flag,"+");
                holder.setText(R.id.annotation,"支出");
            }
        }
    }

    private void getEvent(){
        RxBus.getInstance()
                .toObservable(this, MsgEvent.class)
                .filter(msgEvent -> msgEvent.getTag().equals(TransactionFM.TAG))
                .subscribe(msgEvent -> {
                    if (!com.fzs.comn.tools.Util.isEmpty(msgEvent.getMsg().toString())){
                        if (msgEvent.getMsg().toString().equals("3")){
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
