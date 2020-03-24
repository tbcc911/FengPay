package com.fzs.comn.widget.imageview;

import android.content.Context;
import android.util.AttributeSet;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.RoundingParams;

public class CircleImageView extends ExpandImageView {
    public CircleImageView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
        initCircleImage();
    }

    public CircleImageView(Context context) {
        super(context);
        initCircleImage();
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCircleImage();
    }

    /**
     * 使用代码设置为圆圈形状
     */
    private void initCircleImage() {
        GenericDraweeHierarchy hierarchy = getHierarchy();
        if(hierarchy != null){
            hierarchy.setRoundingParams(new RoundingParams().setRoundAsCircle(true));
            setHierarchy(hierarchy);
        }
    }


}
