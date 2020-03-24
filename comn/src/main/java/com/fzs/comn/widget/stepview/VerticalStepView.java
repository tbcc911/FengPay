package com.fzs.comn.widget.stepview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fzs.comn.R;

import java.util.List;

public class VerticalStepView extends LinearLayout implements VerticalStepViewIndicator.OnDrawIndicatorListener {
	private RelativeLayout mTextContainer;
	private VerticalStepViewIndicator mStepsViewIndicator;
	private List<String> mTexts;
	private int mComplectingPosition;
	private int mUnComplectedTextColor = getResources().getColor(R.color.green);// 定义默认未完成文字的颜色;
	private int mComplectedTextColor = getResources().getColor(R.color.text_hint_color);// 定义默认完成文字的颜色;
	private int ingTextColor = getResources().getColor(R.color.green);// 定义默认进行中文字的颜色;

	public VerticalStepView(Context context) {
		this(context, null);
	}

	public VerticalStepView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	@SuppressLint("NewApi")
	public VerticalStepView(Context context, AttributeSet attrs,int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		View rootView = LayoutInflater.from(getContext()).inflate(R.layout.widget_vertical_stepsview, this);
		mStepsViewIndicator = (VerticalStepViewIndicator) rootView.findViewById(R.id.steps_indicator);
		mStepsViewIndicator.setOnDrawListener(this);
		mTextContainer = (RelativeLayout) rootView.findViewById(R.id.rl_text_container);
		mTextContainer.removeAllViews();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 * 设置显示的文字
	 * 
	 * @param texts
	 * @return
	 */
	public VerticalStepView setStepViewTexts(List<String> texts) {
		mTexts = texts;
		mStepsViewIndicator.setStepNum(mTexts.size());
		return this;
	}

	/**
	 * 设置正在进行的position
	 * 
	 * @param complectingPosition
	 * @return
	 */
	public VerticalStepView setStepsViewIndicatorComplectingPosition(int complectingPosition) {
		mComplectingPosition = complectingPosition;
		mStepsViewIndicator.setComplectingPosition(complectingPosition);
		return this;
	}

	/**
	 * 设置未完成文字的颜色
	 * 
	 * @param unComplectedTextColor
	 * @return
	 */
	public VerticalStepView setStepViewUnComplectedTextColor(int unComplectedTextColor) {
		mUnComplectedTextColor = unComplectedTextColor;
		return this;
	}

	/**
	 * 设置完成文字的颜色
	 * 
	 * @param complectedTextColor
	 * @return
	 */
	public VerticalStepView setStepViewComplectedTextColor(int complectedTextColor) {
		this.mComplectedTextColor = complectedTextColor;
		return this;
	}

	/**
	 * 设置StepsViewIndicator未完成线的颜色
	 * 
	 * @param unCompletedLineColor
	 * @return
	 */
	public VerticalStepView setStepsViewIndicatorUnCompletedLineColor(int unCompletedLineColor) {
		mStepsViewIndicator.setUnCompletedLineColor(unCompletedLineColor);
		return this;
	}

	/**
	 * 设置StepsViewIndicator完成线的颜色
	 * 
	 * @param completedLineColor
	 * @return
	 */
	public VerticalStepView setStepsViewIndicatorCompletedLineColor(int completedLineColor) {
		mStepsViewIndicator.setCompletedLineColor(completedLineColor);
		return this;
	}

	/**
	 * 设置StepsViewIndicator默认图片
	 * 
	 * @param defaultIcon
	 */
	public VerticalStepView setStepsViewIndicatorDefaultIcon(Drawable defaultIcon) {
		mStepsViewIndicator.setDefaultIcon(defaultIcon);
		return this;
	}

	/**
	 * 设置StepsViewIndicator已完成图片
	 * 
	 * @param completeIcon
	 */
	public VerticalStepView setStepsViewIndicatorCompleteIcon(Drawable completeIcon) {
		mStepsViewIndicator.setCompleteIcon(completeIcon);
		return this;
	}

	/**
	 * 设置StepsViewIndicator正在进行中的图片
	 * 
	 * @param attentionIcon
	 */
	public VerticalStepView setStepsViewIndicatorAttentionIcon(Drawable attentionIcon) {
		mStepsViewIndicator.setAttentionIcon(attentionIcon);
		return this;
	}

	/**
	 * is reverse draw 是否倒序画
	 * 
	 * @param isReverSe
	 *            default is true
	 * @return
	 */
	public VerticalStepView reverseDraw(boolean isReverSe) {
		this.mStepsViewIndicator.reverseDraw(isReverSe);
		return this;
	}

	/**
	 * set linePadding proportion 设置线间距的比例系数
	 * 
	 * @param linePaddingProportion
	 * @return
	 */
	public VerticalStepView setLinePaddingProportion(float linePaddingProportion) {
		this.mStepsViewIndicator.setIndicatorLinePaddingProportion(linePaddingProportion);
		return this;
	}
	
    /**
     * 执行、开始创建
     */
    public void execute(){
    	mStepsViewIndicator.invalidate();
    }

	@SuppressLint("NewApi")
	@Override
	public void ondrawIndicator() {
		List<Float> complectedXPosition = mStepsViewIndicator.getCircleCenterPointPositionList();
		if(mTexts != null && mTexts.size()==complectedXPosition.size()){
			for (int i = 0; i < mTexts.size(); i++) {
				TextView textView = new TextView(getContext());
				textView.setText(mTexts.get(i));
				textView.setY(complectedXPosition.get(i)- mStepsViewIndicator.getCircleRadius() / 2);
				textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
				textView.setTextSize(12);
				if (i == mComplectingPosition) {
					textView.setTextColor(ingTextColor);
				} else 
				if (i <= mComplectingPosition) {
					// textView.setTypeface(null, Typeface.BOLD);
					textView.setTextColor(mComplectedTextColor);
				} else {
					textView.setTextColor(mUnComplectedTextColor);
				}
				mTextContainer.addView(textView);
			}
		}
	}
}
