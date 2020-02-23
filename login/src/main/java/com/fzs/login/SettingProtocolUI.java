package com.fzs.login;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.hzh.frame.comn.callback.HttpCallBack;
import com.hzh.frame.core.HttpFrame.BaseHttp;
import com.hzh.frame.ui.activity.BaseUI;
import com.hzh.frame.widget.x5webview.X5WebView;

import org.json.JSONObject;

import static android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;

/**
 * @author
 * @version 1.0
 * @date 2019/7/30
 */
@Route(path = "/login/SettingProtocolUI")
public class SettingProtocolUI extends BaseUI {
    X5WebView detail;
    String type = "";
    String userAgreement = "other/getUserAgreement"; //用户协议
    String privacyAgreement = "other/getUserAgreement"; //隐私协议
    String WithdrawAgreement = "other/getAdoptAgreement"; //服务协议
    String shakeRule = "other/getUserAgreement"; //摇一摇游戏规则
    String link = "";
    
    @Override
    protected void onCreateBase() {
        setContentView(R.layout.login_setting_protocol);
        detail = findViewById(R.id.detail);
        type = getIntent().getStringExtra("type");
        if(type != null && type.length()>0){
            if (type.equals("1")){
                getTitleView().setContent("用户协议和隐私协议");
                link = userAgreement;
            }else if(type.equals("2")){
                getTitleView().setContent("用户协议和隐私协议");
                link = privacyAgreement;
            }else if(type.equals("3")){
                getTitleView().setContent("认养协议");
                link = WithdrawAgreement;
            }else if(type.equals("4")){
                getTitleView().setContent("游戏规则");
                link = shakeRule;
            }
        }else {
            getTitleView().setContent("用户协议和隐私协议");
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            detail.getSettings().setMixedContentMode(MIXED_CONTENT_ALWAYS_ALLOW);
        }
        getTitleView().setLeftIsShow(true);
        BaseHttp.getInstance().query(link, new HttpCallBack() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);
                if (response.optInt("code") == 200){
                    if (response.optString("data") != null && response.optString("data").length()>0){
                        StringBuilder sb = new StringBuilder();
                        sb.append(getHtmlData(response.optString("data")));
                        detail.loadDataWithBaseURL(null,sb.toString(),"text/html", "utf-8", null);
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                            detail.setText(String.valueOf(Html.fromHtml(response.optString("data"),Html.FROM_HTML_MODE_COMPACT)));
//                        }else {
//                            detail.setText(String.valueOf(Html.fromHtml(response.optString("data"))));
//                        }
                    }
                }else {
                    alert(response.optString("message"));
                }
            }
        });
    }

    private String getHtmlData(String bodyHTML) {
        String head1 = "<head>"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> "
                + "<style>img{max-width: 100%; width:auto; height:auto;}</style>"
                + "</head>";
        String head = "<head><style>img{width: 100%;}</style></head>";
        return "<html>" + head1 + "<body>" + bodyHTML + "</body></html>";
    }
    
}
