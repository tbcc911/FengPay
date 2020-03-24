package com.fzs.comn.widget.imageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.fzs.comn.R;


public class GifImageView extends ExpandImageView {

    public GifImageView(Context context) {
        this(context,null);
    }

    public GifImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        int mId = 0;
        if (attrs != null) {
            TypedArray gifAttrs = context.obtainStyledAttributes(attrs, R.styleable.GifImageView);
            mId = gifAttrs.getResourceId(R.styleable.GifImageView_resId,0);
            gifAttrs.recycle();
        }

        if(mId > 0){
            setImageUriAndAutoPlay("res://xxx/"+ mId);
        }
        else {
            setImageUriAndAutoPlay(null);
        }
    }


    public void setImageUriAndAutoPlay(String uri){
        PipelineDraweeControllerBuilder builder = Fresco.newDraweeControllerBuilder()
                .setAutoPlayAnimations(true);
        if(!TextUtils.isEmpty(uri)){
            builder.setUri(Uri.parse(uri));
        }
        setController(builder.build());
    }


}
