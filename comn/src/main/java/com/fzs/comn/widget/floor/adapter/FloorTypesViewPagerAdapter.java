package com.fzs.comn.widget.floor.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.fzs.comn.R;
import com.fzs.comn.widget.floor.bean.FloorBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class FloorTypesViewPagerAdapter extends PagerAdapter {
    List<View> viewList;
    List<FloorBean.ItemInfoListBean.ItemContentListBean> datas;

    /**
     * @param datas      需要显示的数据
     * @param viewHeight 高度
     * @param lines      显示行数
     * @param columns    显示列数
     */
    public FloorTypesViewPagerAdapter(Activity activity, List<FloorBean.ItemInfoListBean.ItemContentListBean> datas, int viewHeight, int lines, int columns) {
        this.datas = datas;
        //每页显示数量
        int theItemNumber = lines * columns;
        if (datas.size() % theItemNumber == 0) {
            //数据能被刚好显示完
            //需要多少页能显示完
            int pages = datas.size() / theItemNumber;
            viewList = new ArrayList<View>(pages);
            for (int i = 0; i < pages; i++) {
                List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                for (int j = i * theItemNumber; j < i * theItemNumber + theItemNumber; j++) {
                    FloorBean.ItemInfoListBean.ItemContentListBean model = datas.get(j);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("value", model.clickValue);
                    map.put("type", model.clickType);
                    map.put("path", model.imageUrl);
                    map.put("name", model.title);
                    map.put("titleColor", model.color);
                    list.add(map);
                }
                View view = activity.getLayoutInflater().inflate(R.layout.comn_floor_item_vp_types, null);
                GridView gridView = view.findViewById(R.id.gridView);
                gridView.setNumColumns(columns);
                gridView.getLayoutParams().height = viewHeight;
                gridView.setAdapter(new FloorTypesGridViewAdapter(activity, list, R.layout.comn_floor_item_gr_vp, new String[]{"name"}, new int[]{R.id.name}, viewHeight, lines, columns, 0.6));
                viewList.add(view);
            }
        } else {
            //数据不能被刚好显示完(这时候总页码需要+1)
            int pages = (datas.size() / theItemNumber) + 1;
            viewList = new ArrayList(pages);
            for (int i = 0; i < pages; i++) {
                List<HashMap<String, String>> resourceList = new ArrayList<HashMap<String, String>>();
                int maxIndex = 0;
                if (datas.size() - i * theItemNumber > theItemNumber) {
                    maxIndex = i * theItemNumber + theItemNumber;
                } else {
                    maxIndex = datas.size();
                }
                for (int j = i * theItemNumber; j < maxIndex; j++) {
                    FloorBean.ItemInfoListBean.ItemContentListBean model = datas.get(j);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("value", model.clickValue);
                    map.put("type", model.clickType);
                    map.put("path", model.imageUrl);
                    map.put("name", model.title);
                    map.put("titleColor", model.color);
                    resourceList.add(map);
                }
                View view = activity.getLayoutInflater().inflate(R.layout.comn_floor_item_vp_types, null);
                GridView gridView = view.findViewById(R.id.gridView);
                gridView.setNumColumns(columns);
                gridView.getLayoutParams().height = viewHeight;
                gridView.setAdapter(new FloorTypesGridViewAdapter(activity, resourceList, R.layout.comn_floor_item_gr_vp, new String[]{"name"}, new int[]{R.id.name}, viewHeight, lines, columns, 0.6));
                viewList.add(view);
            }
        }
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    /**
     * 从当前container中删除指定位置（position）的View
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewList.get(position));
    }

    /**
     * 做了两件事，第一：将当前视图添加到container中，第二：返回当前View
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewList.get(position));
        return viewList.get(position);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

}
