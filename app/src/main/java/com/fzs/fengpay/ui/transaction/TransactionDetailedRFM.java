package com.fzs.fengpay.ui.transaction;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fzs.comn.model.MineIntegration;
import com.fzs.comn.model.TransactionOrder;
import com.fzs.fengpay.ItemDecoration.TeamItemDecoration;
import com.fzs.fengpay.R;
import com.fzs.fengpay.ui.transaction.ItemDecoration.TransactionItemDecoration;
import com.hzh.frame.ui.fragment.AbsRecyclerViewFM;
import com.hzh.frame.util.FileUtil;
import com.hzh.frame.util.Util;
import com.hzh.frame.widget.xrecyclerview.RecyclerViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 明细
 */
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
//        try {
//            String json = FileUtil.readTextFromFile(getActivity(), "json/TransactionDetailed.json");
//            response = new JSONObject(json);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
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

    @Override
    public void onRefresh() {
        super.onRefresh();
    }
}
