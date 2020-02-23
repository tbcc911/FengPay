package com.fzs.mine.ui;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.fzs.comn.model.MineOrderExchange;
import com.fzs.comn.widget.imageview.ExpandImageView;
import com.fzs.mine.R;
import com.hzh.frame.core.ImageFrame.BaseImage;
import com.hzh.frame.ui.fragment.AbsListViewFM;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 兑换专区待发货列表
 * */
public class MineExchangeShippedNotLFM extends AbsListViewFM<MineOrderExchange> {

    @Override
    public boolean setTitleIsShow() {
    	return false;
    }
	
	@Override
	protected int setLayoutView() {
		return R.layout.mine_lv_fm_exchange_all;
	}

	@Override
	protected String setHttpPath() {
		return "3020";
	}

	@Override
	protected JSONObject setHttpParams() {
		JSONObject params = new JSONObject();
		try {
            params.put("shopId", "");
            params.put("orderState", 20);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return params;
	}

	@Override
	protected void bindView(View view) {
		showLoding();
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				MineOrderExchange model = (MineOrderExchange) getAdapter().getItem(position - 1);
				Intent it = new Intent(getActivity(),MineExchangeDetailUI.class);
				it.putExtra("orderId", model.getOrderid());
				startActivity(it);
			}
		});
	}

	@Override
	protected List<MineOrderExchange> handleHttpData(JSONObject response) {
		List<MineOrderExchange> list = new ArrayList<MineOrderExchange>();
		if ("1".equals(response.optString("result"))) {
			JSONObject jsonObj=response.optJSONObject("data");
			if(1==jsonObj.optInt("code")){
				JSONArray listJson = jsonObj.optJSONArray("integralList");
				if(listJson!=null && listJson.length()>0){
					for (int i = 0; i < listJson.length(); i++) {
						try {
							JSONObject obj = (JSONObject) listJson.get(i);
							MineOrderExchange model = new MineOrderExchange();
							model.setImage(obj.optString("path"));// 商品图片
							model.setOrder_status(obj.optInt("igo_status") + "");// 订单状态
							model.setName(obj.optString("ig_goods_name"));// 商品名称
							model.setCount(obj.optString("count"));// 数量
							model.setOrderid(obj.optString("orderId"));
							model.setTotalPrice(obj.optString("integral"));// 总积分
							model.setCompany_name(obj.optString("ship_name"));// 物流公司名称
							model.setCompany_mark(obj.optString("ship_code"));// 物流公司编码
							model.setCourierCode(obj.optString("igo_ship_code"));// 运单号
							list.add(model);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}
				dismissLoding();
			}else{
				showLodingFail();
			}
		}else{
			showLodingFail();
		}
		return list;
	}

	@Override
	protected void handleHttpDataFailure() {
		super.handleHttpDataFailure();
		showLodingFail();
	}

	@Override
	protected View getView(int position, View convertView, ViewGroup parent,LayoutInflater inflater, List<MineOrderExchange> list) {
		ViewCache cache = new ViewCache();
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.mine_item_lv_exchange, null);// 生成条目界面对象
			cache.iv = (ExpandImageView) convertView.findViewById(R.id.pic);
			cache.tv_prodname = (TextView) convertView.findViewById(R.id.prod_name);
			cache.tv_count = (TextView) convertView.findViewById(R.id.count);
			cache.tv_state = (TextView) convertView.findViewById(R.id.state);
			cache.tv_price = (TextView) convertView.findViewById(R.id.price);
			cache.logistic = (TextView) convertView.findViewById(R.id.logistic);
			cache.info = (TextView) convertView.findViewById(R.id.info);
			convertView.setTag(cache);
		} else {
			cache = (ViewCache) convertView.getTag();
		}
		final MineOrderExchange model = list.get(position);
		BaseImage.getInstance().loadXY(model.getImage(), cache.iv);
		cache.tv_prodname.setText(model.getName());
		cache.tv_count.setText("共" + model.getCount() + "件商品");
		cache.tv_price.setText(model.getTotalPrice());
		cache.info.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent it = new Intent(getActivity(),MineExchangeDetailUI.class);
				it.putExtra("orderId", model.getOrderid());
				startActivity(it);
			}
		});
		cache.tv_state.setText("待发货");
		cache.logistic.setVisibility(View.GONE);
		return convertView;
	}

	private final class ViewCache {
		ExpandImageView iv;// 商品图片
		TextView tv_prodname;// 商品名称
		TextView tv_count;// 总数量
		TextView tv_state;// 状态
		TextView tv_price;// 价格
		TextView logistic; // 物流
		TextView info;
	}
}
