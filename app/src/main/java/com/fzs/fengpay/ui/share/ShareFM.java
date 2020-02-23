package com.fzs.fengpay.ui.share;

import android.view.View;
import android.widget.LinearLayout;

import com.fzs.fengpay.R;
import com.hzh.frame.ui.fragment.BaseFM;
import com.hzh.frame.util.AndroidUtil;

/**
 * @author
 * @version 1.0
 * @date 2020/1/31
 */
public class ShareFM extends BaseFM{
    
    View layout;

    @Override
    public boolean setTitleIsShow() {
        return false;
    }
    
    @Override
    protected void onCreateBase() {
        layout=setContentView(R.layout.fm_share);
        layout.findViewById(R.id.statusBar).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, AndroidUtil.getStatusBarHeight()));
    }

}
