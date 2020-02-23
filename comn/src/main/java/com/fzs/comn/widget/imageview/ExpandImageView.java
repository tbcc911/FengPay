package com.fzs.comn.widget.imageview;

import android.content.Context;
import android.util.AttributeSet;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * 此为在ExpandImageView基础上做一层包装，防止过度依赖
 */

public class ExpandImageView extends SimpleDraweeView {
    public ExpandImageView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public ExpandImageView(Context context) {
        super(context);
    }

    public ExpandImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ExpandImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

}
