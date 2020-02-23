package com.fzs.comn.widget.floor.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.fzs.comn.R;
import com.fzs.comn.tools.Util;
import com.fzs.comn.widget.floor.FloorTools;
import com.fzs.comn.widget.imageview.ExpandImageView;
import com.hzh.frame.util.AndroidUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @version 1.0
 * @date 2019/12/11
 */
public class FloorTypesGridViewAdapter extends SimpleAdapter {
    private List<HashMap<String, String>> list;
    private int lineNumber = 2;//行数
    private int lineHeight = 0;//行高
    private int columnNumber = 5;//列数
    private int columnWidth = 0;//列宽
    private double imageWeight = 0.7;//图片大小权重(找到小的一边,以小边为基准计算宽高)
    private int imageWidth = 0;
    private int imageHeight = 0;
    private Activity mActivity;

    public FloorTypesGridViewAdapter(Activity activity, List<? extends Map<String, ?>> list, int resource, String[] from, int[] to, int gridViewHeight, int lineNumber, int columnNumber) {
        super(activity, list, resource, from, to);
        this.mActivity=activity;
        this.list = (List<HashMap<String, String>>) list;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
        lineHeight = gridViewHeight / lineNumber;
        columnWidth = AndroidUtil.getWindowWith() / columnNumber;
        imageWeight = 0.7;
        if (lineHeight <= columnWidth) {//找到小的一边,以小边为基准
            imageWidth = (int) (lineHeight * imageWeight);
            imageHeight = (int) (lineHeight * imageWeight);
        } else {
            imageWidth = (int) (columnWidth * imageWeight);
            imageHeight = (int) (columnWidth * imageWeight);
        }
    }

    public FloorTypesGridViewAdapter(Activity activity, List<? extends Map<String, ?>> list, int resource, String[] from, int[] to, int gridViewHeight, int lineNumber, int columnNumber, double imageWeight) {
        super(activity, list, resource, from, to);
        this.mActivity=activity;
        this.list = (List<HashMap<String, String>>) list;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
        lineHeight = gridViewHeight / lineNumber;
        columnWidth = AndroidUtil.getWindowWith() / columnNumber;
        if (lineHeight <= columnWidth) {//找到小的一边,以小边为基准
            imageWidth = (int) (lineHeight * imageWeight);
            imageHeight = (int) (lineHeight * imageWeight);
        } else {
            imageWidth = (int) (columnWidth * imageWeight);
            imageHeight = (int) (columnWidth * imageWeight);
        }

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        view.getLayoutParams().width = columnWidth;
        view.getLayoutParams().height = lineHeight;
        if (!Util.isEmpty(list.get(position).get("titleColor"))) {
            ((TextView) view.findViewById(R.id.name)).setTextColor(Color.parseColor(list.get(position).get("titleColor")));
        } else {
            ((TextView) view.findViewById(R.id.name)).setTextColor(Color.parseColor("#333333"));
        }
        //设置图片的宽高
        android.widget.LinearLayout.LayoutParams imageLp = new android.widget.LinearLayout.LayoutParams(imageWidth, imageHeight);
        view.findViewById(R.id.image).setLayoutParams(imageLp);
        ((ExpandImageView) view.findViewById(R.id.image)).setImageURI(list.get(position).get("path"));
        view.setOnClickListener(v -> FloorTools.getInstance(mActivity).onBindClick(list.get(position).get("type"), list.get(position).get("value")));
        return view;
    }
}
