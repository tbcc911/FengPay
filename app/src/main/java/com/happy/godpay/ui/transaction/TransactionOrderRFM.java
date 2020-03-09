package com.happy.godpay.ui.transaction;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fzs.comn.model.TransactionOrder;
import com.happy.godpay.R;
import com.happy.godpay.ui.transaction.ItemDecoration.TransactionItemDecoration;
import com.hzh.frame.comn.callback.HttpCallBack;
import com.hzh.frame.core.HttpFrame.BaseHttp;
import com.hzh.frame.ui.fragment.AbsRecyclerViewFM;
import com.hzh.frame.util.Util;
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
        return R.layout.head_rv_transaction_order;
    }
    
    @Override
    protected String setHttpPath() {
        return "finance/getOrderDetail";
    }

    @Override
    protected List<TransactionOrder> handleHttpData(JSONObject response) {
//        try {
//            String json = FileUtil.readTextFromFile(getActivity(), "json/TransactionOrder.json");
//            response = new JSONObject(json);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        List<TransactionOrder> listModel = new ArrayList<>();
        if (response.optInt("code") == 200) {
            JSONObject data = response.optJSONObject("data");
            if (data != null && data.length() > 0){
                sumMoney.setText(data.optString("totalAmount"));
                successMoney.setText(data.optString("successAmount"));
                frozenMoney.setText(data.optString("freezeAmount"));
                JSONArray list = data.optJSONArray("list");
                if (list != null && list.length() > 0) {
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject obj = list.optJSONObject(i);
                        TransactionOrder model = new TransactionOrder();
                        model.setNid(obj.optString("orderId")); //订单ID
                        model.setMoney(obj.optString("amount")); //订单金额
                        model.setCreateTime(obj.optString("createTime")); //创建时间
                        model.setOrderNo(obj.optString("orderNo")); //订单编号
                        model.setState(obj.optString("orderStatus")); //订单状态: 订单状态: 0->未支付; 1->已支付; 2->订单超时
                        model.setStatusName(obj.optString("orderStatusName")); //订单状态名称: 支付中; 已完成; 支付失败
                        model.setTime(obj.optString("payTime")); //支付时间
                        model.setType(obj.optString("payType")); //支付方式: 0->支付宝
                        model.setOrderNo(obj.optString("orderNo"));
                        model.setPayTime(obj.optString("payTime"));
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
        return R.layout.item_rv_order;
    }

    @Override
    protected void bindItemData(RecyclerViewHolder holder, int position, TransactionOrder model) {
        holder.setText(R.id.time,"创建时间:" + model.getCreateTime());
        holder.setText(R.id.money,"¥"+model.getMoney());
        holder.setText(R.id.state,model.getStatusName());
        holder.setText(R.id.desc,"订单编号:" + model.getOrderNo());
        TextView manualPay = holder.itemView.findViewById(R.id.manualPay);
        if (Util.isEmpty(model.getPayTime())){
            holder.setText(R.id.paytime,"未支付");
        }else {
            holder.setText(R.id.paytime,"支付时间:" + model.getPayTime());
        }
        if("1".equals(model.getState())){
            holder.setTextColor(R.id.state,"#228B22");
            manualPay.setVisibility(View.GONE);
        } else
        if("2".equals(model.getState())){
            holder.setTextColor(R.id.state,"#e84c3d");
            manualPay.setVisibility(View.VISIBLE);
        } else
        if("0".equals(model.getState())){
            holder.setTextColor(R.id.state,"#837DF9");
            manualPay.setVisibility(View.VISIBLE);
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

        manualPay.setOnClickListener(v -> {
            JSONObject params = new JSONObject();
            try {
                params.put("orderId", model.getNid());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            BaseHttp.getInstance().write("order/manualPay", params, new HttpCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    super.onSuccess(response);
                    if (response.optInt("code") == 200){
                        onRefresh();
                    }
                    alert(response.optString("message"));
                }
            });
        });
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
    }
}
