package com.fzs.comn.widget.view.webview;

import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fzs.comn.R;
import com.hzh.frame.ui.activity.BaseUI;
import com.hzh.frame.util.Util;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import wendu.dsbridge.DWebView;

@Route(path = "/comn/DWebViewUI")
public class DWebViewUI extends BaseUI {
    private DWebView webview;
    private ProgressBar mProgressBar;
    private String titleName = "", url = "http://www.baidu.com";

    @Override
    public boolean setTitleIsShow() {
        return getIntent().getBooleanExtra("showTitle",true);
    }

    @Override
	protected void onCreateBase() {
        setContentView(R.layout.comn_ui_dwebview);
        if (getIntent().getExtras() != null) {
            if (null != getIntent().getStringExtra("title")) {
                titleName = getIntent().getStringExtra("title");
            }
            if (null != getIntent().getStringExtra("url")) {
                url = getIntent().getStringExtra("url");
            }
            if(getIntent().getBooleanExtra("showTitle",true)){
                getTitleView().setContent(titleName);
            }
            initWebView();
        }
//        getTitleView().setRightContent("调用js");
//        getTitleView().setRightOnclickListener(v -> {
//            String user = null;
//            if(UserTools.getInstance().getIsLogin()){
//                user = UserTools.getInstance().getUser().toJson();
//            }else{
//                user = "用户暂未登录";
//            }
//            webview.callHandler("NativeTojs", new Object[]{user}, new OnReturnValue<String>() {
//                @Override
//                public void onValue(String o) {
//                    alert("收到网页的回调,内容 : "+o);
//                }
//            });
//        });
    }

    public void initWebView(){
        mProgressBar = findViewById(R.id.load_progress);
        webview = findViewById(R.id.dWebView);
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                //获取网页标题
                if (!Util.isEmpty(title) && "".equals(titleName) && getIntent().getBooleanExtra("showTitle",true)) {
                    getTitleView().setContent(title);
                }
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                //加载进度改变的时候回调
                if (newProgress == 100) {
                    //隐藏掉进度条
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    //显示进度条并加载进度
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                }
            }
        });
        //禁止调用第三方浏览器,在调用网页之前加上下面这段代码
        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl(url);

        webview.addJavascriptObject(new JsApi(), null);
    }

    public class JsApi {
    }

    @Override
    protected void onDestroy() {
        if (webview != null) {
            //避免内存泄漏
            webview.removeAllViews();
            webview.destroy();
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (webview.canGoBack()) {
                webview.goBack();
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
