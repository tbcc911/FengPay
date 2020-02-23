package com.fzs.comn.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * 圆弧
 */
public class IntroduceView extends View {
    private static String TAG = "IntroduceView";

    public IntroduceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IntroduceView(Context context) {
        super(context);
    }

    public String paintColor = "#ffffff";

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float width = getWidth();
        float height = getHeight();
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        // 画弧形
        RectF rectArc = new RectF(0, 0, width, (int)(height / 2.0) - 1);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor(paintColor));
        canvas.drawArc(rectArc, 180, 360, false, paint);
        // 画矩形
        RectF rectRect = new RectF(0, (int)(height / 4 * 1), width, height);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor(paintColor));
        canvas.drawRect(rectRect, paint);
    }

}