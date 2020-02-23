package com.fzs.mine.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fzs.comn.model.MineBalance;
import com.fzs.mine.R;
import com.hzh.frame.ui.activity.AbsListViewUI;
import com.hzh.frame.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 当前余额
 * */
@Route(path = "/mine/MineBalanceLUI")
public class MineBalanceLUI extends AbsListViewUI<MineBalance> {

	private TextView money;
    private TextView button;
	
	@Override
	protected int setLayoutView() {
		return R.layout.mine_ui_balance_list;
	}

    public void findViewById(){
        money=findViewById(R.id.money);
        button=findViewById(R.id.button);
    }
	
	@Override
	protected void bindView() {
		getTitleView().setContent("报销明细");
		money.setText(getIntent().getStringExtra("money"));
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //报销
//                Intent intent = new Intent(MineBalanceLUI.this,UserChangeMoneyUI.class);
//                startActivity(intent);
            }
        });
	}
	
	@Override
	protected String setHttpPath() {
		return "3048";
	}
	
	@Override
	protected List<MineBalance> handleHttpData(JSONObject response) {
		List<MineBalance> listModel=new ArrayList<MineBalance>();
		if(response.optInt("result")>0){
            JSONArray list=response.optJSONArray("data");
				if(list!=null){
					for(int i=0;i<list.length();i++){
						JSONObject object=(JSONObject) list.optJSONObject(i);
						MineBalance model=new MineBalance();
                        if(object.optInt("STATE")==0){
                            model.setMoney(-object.optDouble("BALANCE"));
                        }else{
                            model.setMoney(object.optDouble("BALANCE"));
                        }
						model.setDate(object.optLong("OPERATIONTIME"));
						model.setName(object.optString("DESCRIBES"));
						listModel.add(model);
					}
			    }
	    }
		return listModel;
	}
	
	@Override
	protected View getView(int position, View convertView, ViewGroup parent, LayoutInflater inflater, List<MineBalance> list) {
		ViewCache cache=new ViewCache();
		if(convertView==null){
			convertView = inflater.inflate(R.layout.mine_item_lv_balance, null);//生成条目界面对象
			cache.week = (TextView) convertView.findViewById(R.id.week);
			cache.time = (TextView) convertView.findViewById(R.id.time);
			cache.money = (TextView) convertView.findViewById(R.id.money);
			cache.name = (TextView) convertView.findViewById(R.id.name);
			cache.icon = (ImageView) convertView.findViewById(R.id.icon);
			convertView.setTag(cache);
		}else{
			cache = (ViewCache) convertView.getTag();
		}
		MineBalance model = list.get(position);
		cache.week.setText(Util.getWeek(new Date(model.getDate())));
		cache.time.setText(Util.long2DateString(model.getDate()+"", "HH:mm"));
		cache.name.setText(Util.long2DateString(model.getDate()+"", "yyyy.MM.dd HH:mm:ss")+" "+model.getName());
		cache.icon.setImageResource(R.mipmap.mine_balance_list_icon);
		if(model.getMoney()>0){
			cache.money.setText("+"+ Util.doubleFormat(model.getMoney()));
		}else{
			cache.money.setText(""+ Util.doubleFormat(model.getMoney()));
		}
		return convertView;
	}
	
	private final class  ViewCache {
		TextView week;//星期
		TextView time;//时间
		TextView money;//金额
		TextView name;//详情
		ImageView icon;//图标
	}

}