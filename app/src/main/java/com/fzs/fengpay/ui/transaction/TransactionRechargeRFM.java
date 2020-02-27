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
 * 充值
 */
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
//        setLoadPattern(2);
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
        return R.layout.head_rv_transaction_recharge;
    }
    
    @Override
    protected String setHttpPath() {
        return "finance/getRechargeDetail";
    }

    @Override
    protected List<TransactionOrder> handleHttpData(JSONObject response) {
//        try {
//            String json = FileUtil.readTextFromFile(getActivity(), "json/TransactionRecharge.json");
//            response = new JSONObject(json);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
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
//                        model.setNid(obj.optString("id"));
//                        model.setType(obj.optString("type"));
//                        model.setIntegral(obj.optString("integral"));
//                        model.setMoney(obj.optString("money"));
//                        model.setDesc(obj.optString("desc"));
//                        model.setTime(obj.optString("time"));
//                        model.setState(obj.optString("state"));
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
        holder.setText(R.id.time,model.getCreateTime());
        holder.setText(R.id.money,"¥"+model.getMoney());
        holder.getView(R.id.flag).setVisibility(View.GONE);
        holder.getView(R.id.annotation).setVisibility(View.GONE);
        holder.setText(R.id.state,model.getStatusName());
        if("1".equals(model.getState())){
            holder.setTextColor(R.id.state,"#228B22");
        } else
        if("2".equals(model.getState())){
            holder.setTextColor(R.id.state,"#837DF9");
        } else
        if("3".equals(model.getState())){
            holder.setTextColor(R.id.state,"#ff0000");
        }
        if ("0".equals(model.getType())){
            holder.setText(R.id.desc,"银行卡");
            holder.getImageView(R.id.type).setImageResource(R.mipmap.base_image_bank);
        }else if ("1".equals(model.getType())){
            holder.setText(R.id.desc,"支付宝");
            holder.getImageView(R.id.type).setImageResource(R.mipmap.base_image_alipay);
        }else if ("2".equals(model.getType())){
            holder.setText(R.id.desc,"微信");
            holder.getImageView(R.id.type).setImageResource(R.mipmap.base_image_wchat);
        }else if ("3".equals(model.getType())){
            holder.setText(R.id.desc,"USDT");
            holder.getImageView(R.id.type).setImageResource(R.mipmap.default_icon);
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
    }
}
