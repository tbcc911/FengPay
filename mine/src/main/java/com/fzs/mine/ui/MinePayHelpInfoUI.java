package com.fzs.mine.ui;

import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.fzs.comn.tools.Util;
import com.fzs.comn.widget.imageview.ExpandImageView;
import com.fzs.mine.R;
import com.hzh.frame.comn.callback.HttpCallBack;
import com.hzh.frame.core.HttpFrame.BaseHttp;
import com.hzh.frame.core.ImageFrame.BaseImage;
import com.hzh.frame.ui.activity.BaseUI;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 帮朋友付款订单详情
 * */
public class MinePayHelpInfoUI extends BaseUI implements OnClickListener{
	private TextView orderCode,stateName,storeName,goodsMoney,transportMoney,sumMoney,addTime;
	private TextView addresssName,addresssPhone,addresssContent;
	private TextView goodsName,goodsAttrs,goodsMinPrice,goodsNumber,goodsMaxPrice,buyAccounts;
	private ExpandImageView goodsIcon;
	private TextView confirm;
	private String totalPrice,orderId;

	@Override
	protected void onCreateBase() {
		setContentView(R.layout.mine_ui_pay_help_info);
		getTitleView().setContent("订单详情");
		showLoding();
		initView();
	}

	public void initView() {
		confirm = (TextView) findViewById(R.id.confirm);
		orderCode=(TextView) findViewById(R.id.orderCode);
		stateName=(TextView) findViewById(R.id.stateName);
		addresssName=(TextView) findViewById(R.id.addresssName);
		addresssPhone=(TextView) findViewById(R.id.addresssPhone);
		addresssContent=(TextView) findViewById(R.id.addresssContent);
		storeName=(TextView) findViewById(R.id.storeName);
		goodsIcon=(ExpandImageView) findViewById(R.id.goodsIcon);
		goodsName=(TextView) findViewById(R.id.goodsName);
		goodsAttrs=(TextView) findViewById(R.id.goodsAttrs);
		goodsMinPrice=(TextView) findViewById(R.id.goodsMinPrice);
		goodsMaxPrice=(TextView) findViewById(R.id.goodsMaxPrice);
		buyAccounts=(TextView) findViewById(R.id.buyAccounts);
		goodsNumber=(TextView) findViewById(R.id.goodsNumber);
		goodsMoney=(TextView) findViewById(R.id.goodsMoney);
		transportMoney=(TextView) findViewById(R.id.transportMoney);
		sumMoney=(TextView) findViewById(R.id.sumMoney);
		addTime=(TextView) findViewById(R.id.addTime);
		//确认订单
		confirm.setOnClickListener(this);
		loadData();
	}

	public void loadData() {
			JSONObject params = new JSONObject();
			try {
				params.put("order_id", getIntent().getStringExtra("orderCode"));
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			BaseHttp.getInstance().query("3031", params, new HttpCallBack() {
				@Override
				public void onSuccess(JSONObject response) {
					if ("1".equals(response.optString("result"))) {
					   JSONObject data = response.optJSONObject("data");
					   if(1==data.optInt("code")){
						   buyAccounts.setText("购买帐号:"+data.optString("purchase_phone"));
						   JSONObject order=data.optJSONObject("data");
						    orderId=order.optString("id");
							orderCode.setText("订单号:"+order.optString("order_id"));
							stateName.setText(order.optString("state"));
							addresssName.setText("收货人:"+order.optString("trueName"));
							addresssContent.setText("收货地址:"+order.optString("area_info"));
							addresssPhone.setText(order.optString("mobile"));
							storeName.setText(order.optString("store_name"));
							goodsName.setText(order.optString("goods_name"));
							goodsAttrs.setText(order.optString("spec_info"));
							goodsMinPrice.setText(""+ Util.doubleFormat(order.optDouble("store_price")));
							goodsMaxPrice.setText(""+Util.doubleFormat(order.optDouble("goods_price")));
							goodsMaxPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
							goodsNumber.setText("x"+order.optString("count"));
							BaseImage.getInstance().loadXY(order.optString("path"), goodsIcon);
							goodsMoney.setText(""+Util.doubleFormat(order.optDouble("goodsSumPrice")));
							transportMoney.setText(""+Util.doubleFormat(order.optDouble("ship_price")));
							sumMoney.setText(""+Util.doubleFormat(order.optDouble("totalPrice")));
							totalPrice=order.optString("totalPrice");
							addTime.setText("下单时间:"+order.optString("addTime"));
							dismissLoding();
					   }else{
						   alert(data.optString("msg"));
						   finish();
					   }
					} else {
						alert(response.optString("msg"));
						finish();
					}
				}

				@Override
				public void onFail() {
					showLodingFail();
				}
			});
	}

	@Override
	public void onClick(View view) {
        if (view.getId() == R.id.confirm) {
            Intent in = new Intent(MinePayHelpInfoUI.this, MinePayHelpPayUI.class);
            in.putExtra("orderCode", getIntent().getStringExtra("orderCode"));
            in.putExtra("orderId", orderId);
            startActivity(in);
        }
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		loadData();
	}
}
