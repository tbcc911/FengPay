package com.fzs.fengpay.ui.transaction;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fzs.comn.model.TransactionOrder;
import com.fzs.fengpay.R;
import com.fzs.fengpay.ui.transaction.ItemDecoration.TransactionItemDecoration;
import com.hzh.frame.ui.fragment.AbsRecyclerViewFM;
import com.hzh.frame.util.FileUtil;
import com.hzh.frame.widget.xrecyclerview.RecyclerViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单
 */
public class TransactionOrderRFM extends AbsRecyclerViewFM<TransactionOrder> {
    private View headView;
    private TextView sumMoney;
    private TextView successMoney;
    private TextView frozenMoney;
    public static TransactionOrderRFM newInstance(String type) {
        TransactionOrderRFM myFragment = new TransactionOrderRFM();
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
        return R.layout.head_rv_transaction_order;
    }

    @Override
    protected JSONObject setHttpParams() {
        JSONObject map=new JSONObject();
        try {
            map.put("accountType", getArguments().getString("type"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }
    
    @Override
    protected String setHttpPath() {
        return "finance/getAccountDetail";
    }

    @Override
    protected List<TransactionOrder> handleHttpData(JSONObject response) {
        try {
            String json = FileUtil.readTextFromFile(getActivity(), "json/TransactionOrder.json");
            response = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        List<TransactionOrder> listModel = new ArrayList<>();
        if (response.optInt("code") == 200) {
            JSONObject data = response.optJSONObject("data");
            if (data != null && data.length() > 0){
                sumMoney.setText(data.optString("sumMoney"));
                successMoney.setText(data.optString("successMoney"));
                frozenMoney.setText(data.optString("frozenMoney"));
                JSONArray list = data.optJSONArray("accountDetailList");
                if (list != null && list.length() > 0) {
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject obj = list.optJSONObject(i);
                        TransactionOrder model = new TransactionOrder();
                        model.setNid(obj.optString("id"));
                        model.setType(obj.optString("type"));
                        model.setIntegral(obj.optString("integral"));
                        model.setMoney(obj.optString("money"));
                        model.setDesc(obj.optString("desc"));
                        model.setTime(obj.optString("time"));
                        model.setState(obj.optString("state"));
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
        holder.setText(R.id.time,model.getTime());
        holder.setText(R.id.money,"¥"+model.getMoney());
        holder.setText(R.id.desc,model.getDesc());
        holder.getView(R.id.flag).setVisibility(View.GONE);
        holder.getView(R.id.annotation).setVisibility(View.GONE);
        if("1".equals(model.getState())){
            holder.setText(R.id.state,"已完成");
            holder.setTextColor(R.id.state,"#228B22");
        } else
        if("2".equals(model.getState())){
            holder.setText(R.id.state,"支付中");
            holder.setTextColor(R.id.state,"#837DF9");
        } else
        if("3".equals(model.getState())){
            holder.setText(R.id.state,"支付失败");
            holder.setTextColor(R.id.state,"#e84c3d");
        }
        if ("alipay".equals(model.getType())){
            holder.getImageView(R.id.type).setImageResource(R.mipmap.base_image_alipay);
        } else
        if("wchat".equals(model.getType())){
            holder.getImageView(R.id.type).setImageResource(R.mipmap.base_image_wchat);
        } else{
            holder.getImageView(R.id.type).setImageResource(R.mipmap.base_image_unknown);
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
    }
}
