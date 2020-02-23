package com.fzs.mine.ui;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fzs.comn.tools.Util;
import com.fzs.comn.widget.imageview.ExpandImageView;
import com.fzs.mine.R;
import com.hzh.frame.comn.callback.HttpCallBack;
import com.hzh.frame.core.HttpFrame.BaseHttp;
import com.hzh.frame.core.ImageFrame.BaseImage;
import com.hzh.frame.ui.activity.BaseUI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MineExchangeDetailUI extends BaseUI {
	private TextView logistic, logistic_time, linke_name, linke_phone,
			linke_address, store_name, prod_name, order_param, total_price,
			count, price, logisctis_price, sum_price, order_number,
			create_time, pay_time, send_time, deliveryGoods;
	private LinearLayout ll_logistic, ll_store, ll_prod;
	private TextView button1, button3;
	private ExpandImageView pic;
	private String kdgs, kdbh;
	private JSONObject order;

	@Override
	protected void onCreateBase() {
		setContentView(R.layout.mine_ui_exchange_detail);
		getTitleView().setContent("订单详情");
		showLoding();
		initView();
	}

	public void initView() {
		logistic = (TextView) findViewById(R.id.tv_1);
		logistic_time = (TextView) findViewById(R.id.tv_2);
		linke_name = (TextView) findViewById(R.id.tv_3);
		linke_phone = (TextView) findViewById(R.id.tv_4);
		linke_address = (TextView) findViewById(R.id.tv_5);
		store_name = (TextView) findViewById(R.id.tv_6);
		prod_name = (TextView) findViewById(R.id.tv_7);
		order_param = (TextView) findViewById(R.id.tv_8);
		price = (TextView) findViewById(R.id.tv_9);
		total_price = (TextView) findViewById(R.id.tv_10);
		count = (TextView) findViewById(R.id.tv_11);
		logisctis_price = (TextView) findViewById(R.id.tv_12);
		sum_price = (TextView) findViewById(R.id.tv_13);
		deliveryGoods = (TextView) findViewById(R.id.tv_14);
		order_number = (TextView) findViewById(R.id.number);
		create_time = (TextView) findViewById(R.id.createtime);
		pay_time = (TextView) findViewById(R.id.paytime);
		send_time = (TextView) findViewById(R.id.sendtime);

		ll_logistic = (LinearLayout) findViewById(R.id.ll_1);
		ll_store = (LinearLayout) findViewById(R.id.ll_2);
		ll_prod = (LinearLayout) findViewById(R.id.ll_3);

		button1 = (TextView) findViewById(R.id.button1);
		button3 = (TextView) findViewById(R.id.button3);

		pic = (ExpandImageView) findViewById(R.id.pic);

		//查看物流
		button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent it = new Intent(MineExchangeDetailUI.this,MineOrderInfoLogisticUI.class);
				it.putStringArrayListExtra("dataList", logisticData.get(order.optString("orderid")));
				it.putExtra("kdbh", kdbh);
				it.putExtra("kdgsmc", order.optString("company_name"));
				it.putExtra("pic", order.optString("path"));
				it.putExtra("state", logisticState.get(order.optString("orderid")));
				startActivity(it);
			}
		});
		//复制
		button3.setOnClickListener(new OnClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(View arg0) {
				ClipboardManager cmb = (ClipboardManager) MineExchangeDetailUI.this.getSystemService(Context.CLIPBOARD_SERVICE);
				ClipData myClip = ClipData.newPlainText("ORDERID",order.optString("orderid"));
				cmb.setPrimaryClip(myClip);
				Toast.makeText(MineExchangeDetailUI.this,"成功复制订单号:" + order.optString("orderid"),Toast.LENGTH_SHORT).show();
			}
		});
		updData();

	}

	// 设置值
	public void initData() {
		kdgs = order.optString("ship_code");
		kdbh = order.optString("igo_ship_code");
		linke_name.setText("收货人：" + order.optString("trueName"));
		linke_phone.setText(order.optString("mobile"));
		linke_address.setText("收货地址：" + order.optString("area_info"));
		store_name.setText("自营店");
		prod_name.setText(order.optString("ig_goods_name"));
		order_param.setText("");
		deliveryGoods.setText("");
		total_price.setText(""+order.optString("ig_goods_price"));
		total_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		count.setText("x"+order.optString("count"));
		price.setText(order.optString("integral")+"分");
		logisctis_price.setText(""+"0.00");
		sum_price.setText((order.optInt("count")*order.optLong("integral"))+"分");
		order_number.setText("订单编号：" + order.optString("orderid"));
		create_time.setText("下单时间：" + Util.long2Date(order.optString("addTime"),"yyyy-MM-dd HH:mm:ss"));
		pay_time.setText("支付时间：" + Util.long2Date(order.optString("igo_pay_time"),"yyyy-MM-dd HH:mm:ss"));
		send_time.setText("发货时间：" + order.optString("igo_ship_time"));
		BaseImage.getInstance().loadXY(order.optString("path"), pic);
		if (order.optInt("igo_status") >= 30) {
			getLogistica();
			loadLogisticData(order.optString("orderid"), kdgs,kdbh);
			// 物理详情
			ll_logistic.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent it = new Intent(MineExchangeDetailUI.this,MineOrderInfoLogisticUI.class);
					it.putStringArrayListExtra("dataList", logisticData.get(order.optString("orderid")));
					it.putExtra("kdbh", kdbh);
					it.putExtra("kdgsmc", order.optString("company_name"));
					it.putExtra("pic", order.optString("path"));
					it.putExtra("state", logisticState.get(order.optString("orderid")));
					startActivity(it);
				}
			});
		} else 
		if (order.optInt("igo_status") == 20) {
			logistic.setText("配货中");
			logistic_time.setText("");
			button1.setVisibility(View.GONE);
			send_time.setVisibility(View.GONE);
		}
	}

	public void updData() {
		JSONObject params = new JSONObject();
		try {
			params.put("orderId", getIntent().getExtras().getString("orderId"));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		BaseHttp.getInstance().query("3021", params, new HttpCallBack() {
			@Override
			public void onSuccess(JSONObject response) {
				try {
					if ("1".equals(response.optString("result"))) {
						JSONObject data=response.getJSONObject("data");
						if(1==data.optInt("code")){
							order = data.getJSONObject("integral");
							initData();
							dismissLoding();
						}else{
							showLodingFail();
						}
					} else {
						showLodingFail();
					}
				} catch (JSONException e) {
					showLodingFail();
				}
			}

			@Override
			public void onFail() {
				showLodingFail();
			}
		});
	}

	public void getLogistica() {
		String url = "http://m.kuaidi100.com/query?type=" + kdgs + "&postid=" + kdbh + "&id=1&valicode=&temp=" + Math.random();
		BaseHttp.getInstance().get(url, new HttpCallBack() {
			public void onSuccess(JSONObject response) {
				try {
					if ("200".equals(response.optString("status"))) {
						// 处理快递状态
						JSONArray array = response.getJSONArray("data");
						logistic.setText(array.getJSONObject(0).getString("context"));
						logistic_time.setText(array.getJSONObject(array.length() - 1).getString("time"));
					} else {
						logistic.setText(response.optString("message"));
						logistic_time.setText("");
					}
				} catch (JSONException e) {
					e.printStackTrace();
					showLodingFail();
				}
			}

			public void onFail() {
				logistic.setText("物理接口异常！");
			}
		});
	}
	
	HashMap<String, ArrayList<String>> logisticData=new HashMap<String, ArrayList<String>>();
	HashMap<String, Integer> logisticState=new HashMap<String, Integer>();
	public void loadLogisticData(final String key,String kdgs,String kdbh) {
		String url = "http://m.kuaidi100.com/query?type=" + kdgs + "&postid=" + kdbh + "&id=1&valicode=&temp=" + Math.random();
		BaseHttp.getInstance().get(url, new HttpCallBack() {
			public void onSuccess(JSONObject response) {
				try {
					ArrayList<String> list= new ArrayList<String>();
					if ("200".equals(response.optString("status"))) {
						// 处理快递状态
						logisticState.put(key, response.optInt("state"));
						switch (logisticState.get(key)) {
						case 5:
							logisticState.put(key, 2);
							break;
						case 1:
							logisticState.put(key, 0);
							break;
						case 0:
							logisticState.put(key, 1);
							break;
						}
						JSONArray array = response.getJSONArray("data");
						for (int i = (array.length() - 1); i >= 0; i--) {
							list.add(array.getJSONObject(i).getString("context")+ "\n" + array.getJSONObject(i).getString("time"));
						}
					} else {
						list.add(response.optString("message"));
						logisticState.put(key, -1);
					}
					logisticData.put(key, list);
				} catch (JSONException e) {
					e.printStackTrace();
					logisticData.put(key, new ArrayList<String>());
					logisticState.put(key, -1);
				}
			}
			public void onFail() {
				logisticData.put(key, new ArrayList<String>());
				logisticState.put(key, -1);
			}
		});
	}
}
