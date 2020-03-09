package com.happy.godpay.ui.transaction;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fzs.comn.model.TransactionIncome;
import com.happy.godpay.R;
import com.happy.godpay.ui.transaction.ItemDecoration.TransactionItemDecoration;
import com.hzh.frame.ui.fragment.AbsRecyclerViewFM;
import com.hzh.frame.widget.xrecyclerview.RecyclerViewHolder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 收入
 */
public class TransactionIncomeRFM extends AbsRecyclerViewFM<TransactionIncome> {
    private View headView;
    private TextView sumMoney;
    private TextView successMoney;
    private TextView frozenMoney;
    public static TransactionIncomeRFM newInstance(String type) {
        TransactionIncomeRFM myFragment = new TransactionIncomeRFM();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        myFragment.setArguments(bundle);
        return myFragment;
    }
    
    @Override
    protected void bindView(View view) {
        setLoadPattern(2);
        headView = getAdapter().getHeaderView();
        sumMoney = headView.findViewById(R.id.sumMoney);
        successMoney = headView.findViewById(R.id.successMoney);
        frozenMoney = headView.findViewById(R.id.frozenMoney);
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
        return R.layout.head_rv_transaction_income;
    }
    
    @Override
    protected String setHttpPath() {
        return "finance/getIncomeDetail";
    }

    @Override
    protected List<TransactionIncome> handleHttpData(JSONObject response) {
        List<TransactionIncome> listModel = new ArrayList<>();
        if (response.optInt("code") == 200) {
            JSONObject data = response.optJSONObject("data");
            if (data != null && data.length() > 0){
                sumMoney.setText(data.optString("totalIncome"));
                successMoney.setText(data.optString("todayIncome"));
                frozenMoney.setText(data.optString("mothIncome"));
                JSONArray list = data.optJSONArray("list");
                if (list != null && list.length() > 0) {
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject obj = list.optJSONObject(i);
                        TransactionIncome model = new TransactionIncome();
                        model.setAfterValue(obj.optString("afterValue"));
                        model.setBeforeValue(obj.optString("beforeValue"));
                        model.setCreateTime(obj.optString("createTime"));
                        model.setIncometitle(obj.optString("title"));
                        model.setValue(obj.optString("value"));
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
        return R.layout.item_rv_income;
    }

    @Override
    protected void bindItemData(RecyclerViewHolder holder, int position, TransactionIncome model) {
        holder.setText(R.id.incomeTile,model.getIncometitle());
        holder.setText(R.id.time,"创建时间:" + model.getCreateTime());
        holder.setText(R.id.money,model.getBeforeValue());
        holder.setText(R.id.aftermoney,"当前余额:" + model.getAfterValue());
        Float type = Float.parseFloat(model.getAfterValue()) - Float.parseFloat(model.getBeforeValue());
        if (type >= 0){
            holder.setText(R.id.annotation,"+" + model.getValue());
        }else {
            holder.setText(R.id.annotation,"-" + model.getValue());
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
    }
}
