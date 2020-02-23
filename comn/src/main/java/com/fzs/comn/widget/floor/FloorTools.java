package com.fzs.comn.widget.floor;

import android.app.Activity;
import android.content.Intent;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fzs.comn.tools.Util;
import com.hzh.frame.widget.x5webview.X5WebViewUI;

/**
 * @author
 * @version 1.0
 * @date 2019/12/11
 */
public class FloorTools {

    private static FloorTools _instance;
    private Activity mActivity;
    
    public static FloorTools getInstance(Activity activity){
        if(_instance==null){
            _instance=new FloorTools(activity);
        }
        return _instance;
    }
    
    public FloorTools(Activity activity){
        this.mActivity=activity;
    }

    //绑定View的点击事件的执行内容
    public void onBindClick(String type, String value) {
        if (!Util.isEmpty(type)) {
            switch (Integer.parseInt(type)) {
                case 1://网页
                    Intent in=new Intent(mActivity, X5WebViewUI.class);
                    in.putExtra("url", value);
                    mActivity.startActivity(in);
                    break;
                case 2://分类(商品列表)
                    ARouter.getInstance()
                            .build("/store/StoreListRUI")
                            .withString("httpPath", "member/login")
                            .withString("id", value)
                            .navigation();
                    break;
                case 3://商品(商品详情)
                    ARouter.getInstance().build("/store/StoreGoodsInfoUI").withString("id", value).navigation();
                    break;
                case 4://游戏
                    ARouter.getInstance().build("/game/GameIntroduceUI").withString("id", value).navigation();
                    break;
                case 5://基金
                    ARouter.getInstance().build("/main/MainUI").withInt("switchTab", 3).navigation();
                    break;
                default:
                    break;
            }
        }
    }
}
