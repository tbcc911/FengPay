package com.fzs.mine.ui;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fzs.comn.model.MineIntegration;
import com.fzs.comn.tools.UserTools;
import com.fzs.mine.ItemDecoration.ComnItemDecoration;
import com.fzs.mine.ItemDecoration.MessageItemDecoration;
import com.fzs.mine.ItemDecoration.TeamItemDecoration;
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
@Route(path = "/mine/MineIntegralInfoRUI")
public class MineIntegralInfoRUI extends AbsRecyclerViewUI<MineIntegration> {
    View mView;
    TextView sum_integration;
    TextView integration;
    TextView freezeIntegration;
    TextView income;
    TextView expend;
    
    @Override
    protected int setLayoutId() {
        return R.layout.mine_ui_integral_info;
    }

    @Override
    protected int setHeadLayoutId() {
        return R.layout.mine_head_integral_info;
    }

    @Override
    protected RecyclerView.ItemDecoration setRecyclerViewItemDecoration() {
        return new ComnItemDecoration(getApplicationContext(), R.color.base_bg,Util.dip2px(this,10));
    }

    @Override
    protected void bindView() {
        getTitleView().setContent("积分明细");
        getTitleView().setLineIsShow(false);
        mView = getAdapter().getHeaderView();
        sum_integration = mView.findViewById(R.id.sum_integration);
        integration = mView.findViewById(R.id.integration);
        freezeIntegration = mView.findViewById(R.id.freezeIntegration);
        income = mView.findViewById(R.id.income);
        expend = mView.findViewById(R.id.expend);
        sum_integration.setText(Util.doubleFormat(UserTools.getInstance().getUser().getIntegration(),"#0.00"));
        showLoding();
    }

    @Override
    protected String setHttpPath() {
        return "finance/getIntegrationDetail";
    }
 
    @Override
    protected List<MineIntegration> handleHttpData(JSONObject response) {
        List<MineIntegration> listModel = new ArrayList<>();
        if (response.optInt("code") == 200){
            JSONObject data = response.optJSONObject("data");
            if (data != null && data.length() > 0){
                integration.setText("积分余额:" + data.optString("integration"));
                freezeIntegration.setText("冻结积分:" + data.optString("freezeIntegration"));
                income.setText("收入:" + data.optString("income"));
                expend.setText("支出:" + data.optString("expend"));
                JSONArray list = data.optJSONArray("list");
                if (list != null && list.length() > 0){
                    for (int i = 0;i < list.length();i++){
                        JSONObject object = list.optJSONObject(i);
                        MineIntegration model = new MineIntegration();
                        model.setCreateTime(object.optString("createTime"));
                        model.setValue(object.optString("value"));
                        model.setTradeType(object.optString("tradeType"));
                        model.setTvtitle(object.optString("title"));
                        model.setNote(object.optString("note"));
                        listModel.add(model);
                    }
                }
            }
        }else {
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
        return R.layout.mine_item_rv_integral_info;
    }

    @Override
    protected void bindItemData(RecyclerViewHolder holder, int position, MineIntegration model) {
        TextView price = holder.itemView.findViewById(R.id.price);
        holder.setText(R.id.time,model.getCreateTime());
        String type = model.getTradeType();
        if (!Util.isEmpty(type)){
            if (type.equals("0")){
                price.setText("+" + model.getValue());
                price.setTextColor(ContextCompat.getColor(MineIntegralInfoRUI.this,R.color.green));
            }else if (type.equals("1")){
                price.setText("-" + model.getValue());
                price.setTextColor(ContextCompat.getColor(MineIntegralInfoRUI.this,R.color.red));
            }
        }
        holder.setText(R.id.tvTitle,model.getTvtitle());
        holder.setText(R.id.notes,model.getNote());
    }
}
