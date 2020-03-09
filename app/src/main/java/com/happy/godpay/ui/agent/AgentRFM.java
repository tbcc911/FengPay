package com.happy.godpay.ui.agent;

import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.fzs.comn.model.MineTeamUser;
import com.fzs.comn.tools.Util;
import com.fzs.comn.widget.imageview.ExpandImageView;
import com.happy.godpay.ItemDecoration.TeamItemDecoration;
import com.happy.godpay.R;
import com.hzh.frame.ui.fragment.AbsRecyclerViewFM;
import com.hzh.frame.widget.xrecyclerview.RecyclerViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2019/7/30
 */
public class AgentRFM extends AbsRecyclerViewFM<MineTeamUser> {
    EditText search;
    View mView;
    
    @Override
    protected void bindView(View layout) {
        getTitleView().setLeftIsShow(false);
        getTitleView().setContent("代理");
        findViewById(R.id.search_button).setOnClickListener(view ->{
            if(!search.getText().toString().contains("****")){
                search.setTag(search.getText().toString());
            }
            onRefresh();
        });
//        setLoadPattern(2);
        mView = getAdapter().getHeaderView();
        showLoding();
        search = findViewById(R.id.search);
        search.setTag("");
        search.setOnEditorActionListener((view, actionId, event) -> {
            if (actionId== EditorInfo.IME_ACTION_SEND ||(event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER)) {
                if(!search.getText().toString().contains("****")){
                    search.setTag(search.getText().toString());
                }
                onRefresh();
                return true;
            }
            return false;
        });
    }

    @Override
    protected RecyclerView.ItemDecoration setRecyclerViewItemDecoration() {
        return new TeamItemDecoration(getActivity(), R.color.base_bg, getResources().getDimension(R.dimen.dp_10));
    }
    
    @Override
    protected int setLayoutId() {
        return R.layout.fm_rv_agent;
    }

    @Override
    protected int setHeadLayoutId() {
        return R.layout.head_agent_index;
    }

    @Override
    protected String setHttpPath() {
        return "member/getTeamInfo";
    }

    @Override
    protected JSONObject setHttpParams() {
        JSONObject params=new JSONObject();
        try {
            params.put("phone", search.getTag().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }

    @Override
    protected List<MineTeamUser> handleHttpData(JSONObject response) {
//        try {
//            String json = FileUtil.readTextFromFile(getActivity(), "json/Team.json");
//            response = new JSONObject(json);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        List<MineTeamUser> listModel = new ArrayList<>();
        if (response.optInt("code") == 200) {
            JSONObject data = response.optJSONObject("data");
            if (data != null && data.length() > 0) {
                View headView = getAdapter().getHeaderView();
                ((TextView)headView.findViewById(R.id.sum_person)).setText(data.optString("teamCount"));
                ((TextView)headView.findViewById(R.id.sum_person_effective)).setText(data.optString("teamEffectiveCount"));
                ((TextView)headView.findViewById(R.id.sum_share)).setText(data.optString("shareCount"));
                ((TextView)headView.findViewById(R.id.sum_share_effective)).setText(data.optString("shareEffectiveCount"));
                ((ExpandImageView)headView.findViewById(R.id.user_avatar)).setImageURI(data.optString("avatarUrl"));
                JSONArray teamList =data.optJSONArray("teamList");
                if (teamList != null && teamList.length()>0){
                    for (int i = 0; i < teamList.length(); i++) {
                        JSONObject model = teamList.optJSONObject(i);
                        MineTeamUser mineTeam = new MineTeamUser();
                        mineTeam.setNickname(model.optString("nickname"));
                        mineTeam.setAvatarUrl(model.optString("avatarUrl"));
                        mineTeam.setShareCount(model.optString("shareCount"));
                        mineTeam.setShareEffectiveCount(model.optString("shareEffectiveCount"));
                        mineTeam.setTeamCount(model.optString("teamCount"));
                        mineTeam.setTeamEffectiveCount(model.optString("teamEffectiveCount"));
                        mineTeam.setRegisterTime(model.optString("registerTime"));
                        mineTeam.setPhone(model.optString("phone"));
//                        mineTeam.setIsEffective(model.optBoolean("isEffective"));
//                        mineTeam.setIncomeScale(model.optString("incomeScale"));
//                        mineTeam.setIsEffective(model.optBoolean("effectiveStatus"));
                        mineTeam.setEffectiveStatus(model.optString("effectiveStatus"));
                        mineTeam.setAgentRatio(model.optString("agentRatio"));
                        mineTeam.setTotalIncome(model.optString("totalIncome"));
                        listModel.add(mineTeam);
                    }
                }
            }
        }else{
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
        return R.layout.item_rv_agent_user;
    }

    @Override
    protected void bindItemData(RecyclerViewHolder holder, int position, MineTeamUser model) {
        ((ExpandImageView) holder.getView(R.id.user_avatar)).setImageURI(model.getAvatarUrl());
        holder.setText(R.id.nickName,model.getNickname());
        holder.setText(R.id.phone, "("+Util.phoneHide(model.getPhone())+")");
        holder.setText(R.id.createTime,model.getRegisterTime());
        holder.setText(R.id.teamCount,model.getTeamCount());
        holder.setText(R.id.teamEffectiveCount,model.getTeamEffectiveCount());
        holder.setText(R.id.shareCount,model.getShareCount());
        holder.setText(R.id.incomeScale,(Float.parseFloat(model.getAgentRatio())*100) + "%");
//        holder.setText(R.id.incomeScale,Float.parseFloat(model.getAgentRatio())*100+"%");
        holder.setText(R.id.shareEffectiveCount,model.getShareEffectiveCount());
        holder.setText(R.id.incomeMoney,model.getTotalIncome());
        if(model.getEffectiveStatus().equals("1")){
            holder.getImageView(R.id.isEffective).setImageResource(R.drawable.app_icon_agent_true);
        }else{
            holder.getImageView(R.id.isEffective).setImageResource(R.drawable.app_icon_agent_false);
        }
        holder.setOnItemClickListener(view -> {
            search.setText(Util.phoneHide(model.getPhone()));
            search.setSelection(Util.phoneHide(model.getPhone()).length());
            search.setTag(model.getPhone());
            //初始化下拉刷新动画
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, 120);
            mSwipeRefreshLayout.setRefreshing(true);
            onRefresh();
        });
    }
}
