package com.fzs.fengpay.ui.share;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fzs.comn.tools.UserTools;
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

    @Override
    public boolean setTitleIsShow() {
        return false;
    }
    
    @Override
    protected void onCreateBase() {
        layout=setContentView(R.layout.fm_share);
        inviteCode = layout.findViewById(R.id.inviteCode);
        inviteQr = layout.findViewById(R.id.inviteQr);
        if (UserTools.getInstance().getIsLogin()){
            inviteCode.setText(UserTools.getInstance().getUser().getInviteCode());
//            agentRatio = UserTools.getInstance().getUser().getAgentRatio();
            agentRatio = "0.5";
        }else {
            inviteCode.setText("");
        }
        getShare();
//        layout.findViewById(R.id.statusBar).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, AndroidUtil.getStatusBarHeight()));
    }
    
    private void getShare(){
        JSONObject params=new JSONObject();
        try {
            params.put("agentRatio", agentRatio);
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
            getShare();
        } else {
            //界面不可见 相当于onpause
        }
    }

}
