package com.fzs.mine.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.activeandroid.query.From;
import com.fzs.comn.model.MinePayHelp;
import com.fzs.comn.tools.Util;
import com.fzs.mine.R;
import com.hzh.frame.ui.activity.AbsListViewUI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 朋友支付详情
 * */
public class MinePayHelpLUI extends AbsListViewUI<MinePayHelp> {

	TextView payprices;
	
	@Override
	protected int setLayoutView() {
		return R.layout.mine_ui_pay_help_list;
	}
	
	@Override
	protected void bindView() {
		getTitleView().setContent("朋友支付详情");
        payprices=findViewById(R.id.payprices);
	}

	@Override
	public int[] setPageInfo() {
		return new int[]{1,1,999999};
	}
	
	@Override
	protected String setHttpPath() {return "3014";}
	
	@Override
	protected JSONObject setHttpParams() {
		JSONObject params=new JSONObject();
		try {
			params.put("orderId", getIntent().getExtras().getString("orderId"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return params;
	}
	
	@Override
	protected List<MinePayHelp> handleHttpData(JSONObject response) {
		List<MinePayHelp> list=new ArrayList<MinePayHelp>();
		Log.e("response", response.toString());
		if ("1".equals(response.optString("result"))) {
		    JSONObject data=response.optJSONObject("data");
		    if(1==data.optInt("code")){
		    	payprices.setText(""+data.optString("payprices"));
				JSONArray listMode=data.optJSONArray("pay_user");
				if(listMode!=null && listMode.length()>0){
				    	for(int i=0;i<listMode.length();i++){
					    	JSONObject obj=listMode.optJSONObject(i);
					    	MinePayHelp model=new MinePayHelp();
					    	model.setName(obj.optString("PHONE"));
					    	model.setPayHelpMoney(obj.optString("PAY_MONEY"));
					    	model.setDate(obj.optString("PAY_TIME"));
					    	model.setOrderId(getIntent().getExtras().getString("orderId"));
							list.add(model);
					    }
			    }
		    }
		}
		return list;
	}

	@Override
	protected From setSqlParams(From from) {
		from.where("orderId='"+getIntent().getExtras().getString("orderId")+"'");
		return from;
	}
	 
	@Override
	protected View getView(int position, View convertView, ViewGroup parent,LayoutInflater inflater, List<MinePayHelp> list) {
		ViewCache cache=new ViewCache();
		if(convertView==null){
			convertView = inflater.inflate(R.layout.mine_item_lv_pay_help, null);//生成条目界面对象
			cache.name = (TextView) convertView.findViewById(R.id.name);
			cache.payHelpMoney = (TextView) convertView.findViewById(R.id.payHelpMoney);
			cache.date = (TextView) convertView.findViewById(R.id.date);
			convertView.setTag(cache);
		}else{
			cache = (ViewCache) convertView.getTag();
		}
		MinePayHelp model = list.get(position);
		cache.name.setText("帐号:"+model.getName());
		cache.payHelpMoney.setText("-"+model.getPayHelpMoney());
		cache.date.setText("支付时间:"+ Util.long2Date(model.getDate(), "yyyy-MM-dd HH:mm:ss"));
		return convertView;
	}
	
	private final class  ViewCache {
		TextView name;
		TextView payHelpMoney;
		TextView date;
	}

}