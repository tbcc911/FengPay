package com.fzs.fengpay.ui.share;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fzs.comn.tools.UserTools;
import com.fzs.comn.tools.Util;
import com.fzs.fengpay.R;
import com.hzh.frame.comn.callback.HttpCallBack;
import com.hzh.frame.core.BaseSP;
import com.hzh.frame.core.HttpFrame.BaseHttp;
import com.hzh.frame.ui.fragment.BaseFM;
import com.hzh.frame.util.AndroidUtil;
import com.hzh.frame.util.Code2Util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author
 * @version 1.0
 * @date 2020/1/31
 */
public class ShareFM extends BaseFM{
    
    View layout;
    TextView inviteCode;
    private String agentRatio = "";
    private ImageView inviteQr;
    private TextView myRate;
    private Button button;

    @Override
    public boolean setTitleIsShow() {
        return false;
    }
    
    @Override
    protected void onCreateBase() {
        layout=setContentView(R.layout.fm_share);
        inviteCode = layout.findViewById(R.id.inviteCode);
        inviteQr = layout.findViewById(R.id.inviteQr);
        myRate = layout.findViewById(R.id.myRate);
        button = layout.findViewById(R.id.button);
        if (UserTools.getInstance().getIsLogin()){
            inviteCode.setText(UserTools.getInstance().getUser().getInviteCode());
            agentRatio = UserTools.getInstance().getUser().getAgentRatio();
            myRate.setText(agentRatio);
        }else {
            inviteCode.setText("");
            myRate.setText("");
        }
        button.setOnClickListener(v -> {
            setMyRate();
        });
//        layout.findViewById(R.id.statusBar).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, AndroidUtil.getStatusBarHeight()));
    }
    
    private void getShare(String rate){
        JSONObject params=new JSONObject();
        try {
            params.put("agentRatio", rate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        BaseHttp.getInstance().query("member/createSharepageLink", params, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);
                if (response.optInt("code") == 200){
                    JSONObject data = response.optJSONObject("data");
                    if (data != null && data.length() > 0){
                        inviteQr.setImageBitmap(Code2Util.create(data.optString("url"),(int)(getActivity().getResources().getDimension(R.dimen.dp_160)),
                                (int)(getResources().getDimension(R.dimen.dp_80))));
                    }
                }else {
                    alert(response.optString("message"));
                }
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            //界面可见
            
        } else {
            //界面不可见 相当于onpause
        }
    }
    
    private void setMyRate(){
        String rate = myRate.getText().toString().trim();
        if (Util.isEmpty(rate)){
            alert("请输入您的代理比例");
            return;
        }
        if (Float.parseFloat(rate) <= 0){
            alert("请输入大于0的代理比例");
            return;
        }
        if (!Util.isEmpty(rate)){
            if (Float.parseFloat(rate) > Float.parseFloat(agentRatio)){
                alert("代理不得高于自己的代理比例");
                return;
            }
            getShare(rate);
        }else {
            alert("您的代理比例错误");
        }
    }

}
