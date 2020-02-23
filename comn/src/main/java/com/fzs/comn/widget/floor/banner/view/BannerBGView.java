package com.fzs.comn.widget.floor.banner.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.fzs.comn.R;
import com.fzs.comn.tools.Util;

/**
 * 圆弧
 */
public class BannerBGView extends View {
    private static String TAG = "IArcView";
    public String paintColor="#2F80ED";
    public int bitmapWidth=Util.dip2px(getContext(),30);

    public BannerBGView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BannerBGView(Context context) {
        super(context);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(!Util.isEmpty(paintColor) && paintColor.matches("^#([0-9a-fA-F]{6}|[0-9a-fA-F]{3})$")){
            float width = getWidth();
            float height = getHeight()/4*3;
            super.onDraw(canvas);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            // 画弧形
            RectF rectArc = new RectF(0, height/2-1, width, height);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.parseColor(paintColor));
            canvas.drawArc(rectArc, 0, 180, false, paint);
            // 圆弧描边
            paint.setColor(Color.parseColor(paintColor));
            paint.setStrokeWidth(1);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawArc(rectArc, 0, 180, false, paint);
            // 画矩形
            RectF rectRect = new RectF(0, 0, width, height/4*3);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.parseColor(paintColor));
            canvas.drawRect(rectRect,paint);
            // 图片
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.base_app_logo, null);
            Rect codeSrc = new Rect();// 图片显示大小(可绘画图片的某一些地方，而不是全部图片)
            codeSrc.left = 0;
            codeSrc.top = 0;
            codeSrc.right = codeSrc.left + bitmap.getWidth();
            codeSrc.bottom = codeSrc.top + bitmap.getHeight();
            Rect codeDst = new Rect();// 图片在canvas上显示的位置及大小
            codeDst.left = (int)(width/2-bitmapWidth);
            codeDst.top = (int)(height/4*3-bitmapWidth);
            codeDst.right = (int)(width/2+bitmapWidth);
            codeDst.bottom = (int)(height/4*3+bitmapWidth);
            canvas.drawBitmap(bitmap, codeSrc, codeDst, null);
        }
    }
    
    public void setPaintColor(String color){
        if(!Util.isEmpty(color)){
            this.paintColor=color;
            invalidate();
        }
    }
    
    public String getPaintColor(){
        return paintColor;
    }

    
}