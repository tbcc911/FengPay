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
import com.fzs.mine.ItemDecoration.TransactionItemDecoration;
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
    private View headView;
    private TextView sumMoney;
    private TextView successMoney;
    private TextView frozenMoney;
    private TextView freezeIntegration;
    
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
        return new TransactionItemDecoration(getApplicationContext(), R.color.base_bg,getResources().getDimension(R.dimen.dp_10));
    }

    @Override
    protected void bindView() {
        getTitleView().setContent("积分明细");
        getTitleView().setLineIsShow(false);
        headView = getAdapter().getHeaderView();
        sumMoney = headView.findViewById(R.id.sumMoney);
        successMoney = headView.findViewById(R.id.successMoney);
        frozenMoney = headView.findViewById(R.id.frozenMoney);
        freezeIntegration = headView.findViewById(R.id.freezeIntegration);
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
                sumMoney.setText(data.optString("integration"));
                freezeIntegration.setText(data.optString("freezeIntegration"));
                successMoney.setText(data.optString("income"));
                frozenMoney.setText(data.optString("expend"));
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
        holder.setText(R.id.tvTitle,model.getTvtitle());
        holder.setText(R.id.money,model.getValue());
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
}
