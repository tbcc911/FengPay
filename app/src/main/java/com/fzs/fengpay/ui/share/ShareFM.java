package com.fzs.fengpay.ui.share;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fzs.comn.tools.ImageTools;
import com.fzs.comn.tools.UserTools;
import com.fzs.comn.tools.Util;
import com.fzs.fengpay.R;
import com.hzh.frame.comn.callback.CallBack;
import com.hzh.frame.comn.callback.HttpCallBack;
import com.hzh.frame.core.BaseSP;
import com.hzh.frame.core.HttpFrame.BaseHttp;
import com.hzh.frame.ui.fragment.BaseFM;
import com.hzh.frame.util.AndroidUtil;
import com.hzh.frame.util.Code2Util;
import com.hzh.frame.widget.xdialog.XDialog1Button;

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
    private Button saveQr;
    private String qrUrl = "";
    XDialog1Button mXDialog1Button;

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
        saveQr = layout.findViewById(R.id.saveQr);
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
        saveQr.setOnClickListener(v -> {
            if (!Util.isEmpty(qrUrl)){
                Bitmap bitmap= ImageTools.getImageViewBitmap(inviteQr);
                ImageTools.saveBitmap2Camera(getActivity(),bitmap,"share");
                mXDialog1Button=new XDialog1Button(getActivity(), "二维码已保存到相册", new CallBack() {
                    @Override
                    public void onSuccess(Object o) {

                    }
                });
                mXDialog1Button.setButtonName("确定").show();
            }else {
                alert("暂无二维码可保存");
            }
        });
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
                        qrUrl = data.optString("url");
                        inviteQr.setImageBitmap(Code2Util.create(qrUrl,(int)(getActivity().getResources().getDimension(R.dimen.dp_160)),
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
