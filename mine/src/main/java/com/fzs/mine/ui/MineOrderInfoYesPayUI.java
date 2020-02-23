package com.fzs.mine.ui;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fzs.comn.tools.Util;
import com.fzs.comn.widget.imageview.ExpandImageView;
import com.fzs.mine.R;
import com.hzh.frame.callback.CallBack;
import com.hzh.frame.comn.callback.HttpCallBack;
import com.hzh.frame.core.HttpFrame.BaseHttp;
import com.hzh.frame.core.ImageFrame.BaseImage;
import com.hzh.frame.ui.activity.BaseUI;
import com.hzh.frame.widget.xdialog.XDialog2Button;
import com.hzh.frame.widget.xdialog.XDialogSubmit;
import com.hzh.frame.widget.xrecyclerview.AbsRecyclerAdapter;
import com.hzh.frame.widget.xrecyclerview.AutoLinearLayoutManager;
import com.hzh.frame.widget.xrecyclerview.RecyclerViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 已支付订单详情
 * */
public class MineOrderInfoYesPayUI extends BaseUI {

    private LinearLayout logistic;
    private TextView logisticTitle;
    private TextView logisticTime;
    private TextView addressName;
    private TextView addressPhone;
    private TextView addressInfo;
    private TextView storeName;
    private TextView logisticMoney;
    private TextView totalMoney;
    private TextView showLogistic;
    private TextView confirm;
    private TextView copy;
    private TextView create_time;
    private TextView order_number;
    private TextView pay_time;
    private TextView send_time;
    private TextView orderCode;
    private TextView stateName;
    private TextView button4;
    private LinearLayout buttonArea;
    private LinearLayout codeAndName;
    private LinearLayout payHelpInfo;
    private RelativeLayout rl_order;
    private RecyclerView recyclerview;
    private TextView payHelpMoney;
    private String kdgs, kdbh,ofid,orderId,companyName;
    private int orderStatus=0;
    private XDialogSubmit subDialog;
    private JSONObject order;
    private int goodsNumber=1;

    @Override
    protected void showLodingFailCallMethod() {
        showLoding();
        updData();
    }

    public void findViewById(){
        logistic=findViewById(R.id.logistic);
        logisticTitle=findViewById(R.id.logisticTitle);
        logisticTime=findViewById(R.id.logisticTime);
        addressName=findViewById(R.id.addressName);
        addressPhone=findViewById(R.id.addressPhone);
        addressInfo=findViewById(R.id.addressInfo);
        storeName=findViewById(R.id.storeName);
        logisticMoney=findViewById(R.id.logisticMoney);
        totalMoney=findViewById(R.id.totalMoney);
        showLogistic=findViewById(R.id.showLogistic);
        confirm=findViewById(R.id.confirm);
        copy=findViewById(R.id.copy);
        order_number=findViewById(R.id.number);
        create_time = findViewById(R.id.createtime);
        pay_time=findViewById(R.id.paytime);
        send_time=findViewById(R.id.sendtime);
        orderCode=findViewById(R.id.orderCode);
        stateName=findViewById(R.id.stateName);
        button4=findViewById(R.id.button4);
        buttonArea=findViewById(R.id.buttonArea);
        codeAndName=findViewById(R.id.codeAndName);
        payHelpInfo=findViewById(R.id.payHelpInfo);
        recyclerview=findViewById(R.id.recyclerview);
        payHelpMoney=findViewById(R.id.payHelpMoney);
        rl_order = findViewById(R.id.rl_order);
    }
    
    @Override
    protected void onCreateBase() {
        setContentView(R.layout.mine_ui_order_info_yes_pay);
        findViewById();
        getTitleView().setContent("订单详情");
        showLoding();
        initView();
    }

    public void initView() {
        recyclerview.setLayoutManager(new AutoLinearLayoutManager(this));
        payHelpInfo.setOnClickListener(v -> {
            Intent in=new Intent(MineOrderInfoYesPayUI.this,MinePayHelpLUI.class);
            in.putExtra("orderId", getIntent().getStringExtra("orderId"));
            startActivity(in);
        });
        showLogistic.setOnClickListener(view -> {
            Intent intent = new Intent(MineOrderInfoYesPayUI.this,MineOrderInfoLogisticUI.class);
            intent.putStringArrayListExtra("dataList", logisticData.get(order.optString("order_id")));
            intent.putExtra("kdbh", kdbh);
            intent.putExtra("kdgsmc", order.optString("deliveryCompany"));
            intent.putExtra("pic", order.optString("path"));
            intent.putExtra("state", logisticState.get(order.optString("order_id")));
            intent.putExtra("goodsNumber", goodsNumber);
            startActivity(intent);
        });
        confirm.setOnClickListener(view -> new XDialog2Button(MineOrderInfoYesPayUI.this).setMsg("你确定吗？").setCallback(new CallBack() {
                    @Override
                    public void onSuccess(Object object) {
                        JSONObject params = new JSONObject();
                        try {
                            params.put("orderId",order.optString("order_id"));
                            params.put("state", 2);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        BaseHttp.getInstance().write(MineOrderInfoYesPayUI.this,"4003", params,new HttpCallBack() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                if (1==(response.optInt("result"))) {
                                    JSONObject data=response.optJSONObject("data");
                                    if(1==data.optInt("code")){
                                        setResult(2);
                                        finish();
                                    }else{
                                        alert(data.optString("msg"));
                                    }
                                } else {
                                    alert(response.optString("msg"));
                                }
                            }

                            @Override
                            public void onFail() {
                                alert("服务器偷懒了失败了！");
                            }
                        });
                    }
                }).show());

        copy.setOnClickListener(new OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View arg0) {
                ClipboardManager cmb = (ClipboardManager) MineOrderInfoYesPayUI.this.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData myClip = ClipData.newPlainText("ORDERID",order.optString("orderId"));
                cmb.setPrimaryClip(myClip);
                alert("成功复制订单号:" + order.optString("order_id"));
            }
        });
        button4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MineOrderInfoYesPayUI.this,MineOrderInfoReturnUI.class));
            }
        });
        updData();
    }

    // 设置值
    public void initData() {
        orderCode.setText("订单号:"+order.optString("orderId"));
        stateName.setText(order.optString("orderStatusName"));
        kdgs = order.optString("deliveryCompany");
        kdbh = order.optString("deliverySn");
        if (getIntent().getStringExtra("status").equals("1")){
            addressName.setText("收货人：" + order.optString("receiverName"));
            addressInfo.setText("收货地址：" +order.optString("receiverProvince")+order.optString("receiverCity")+order.optString("receiverRegion")+ order.optString(
                    "receiverDetailAddress"));
        }else {
            rl_order.setVisibility(View.GONE);
        }
        addressPhone.setText(order.optString("receiverPhone"));
        storeName.setText(order.optString("storeName"));
        logisticMoney.setText(""+order.optString("payAmount"));
        totalMoney.setText(""+order.optString("payAmount"));
        order_number.setText("订单编号：" + order.optString("orderSn"));
        create_time.setText("下单时间：" + order.optString("createTime"));
        pay_time.setText("支付时间：" + order.optString("paymentTime"));
        send_time.setText("发货时间：" + order.optString("deliveryTime"));
        if (order.optInt("orderStatus") >= 2) {
            logistic.setVisibility(View.VISIBLE);
            loadLogisticData(order.optString("orderId"), kdgs,kdbh);
            // 物理详情
            logistic.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent it = new Intent(MineOrderInfoYesPayUI.this,MineOrderInfoLogisticUI.class);
                    it.putStringArrayListExtra("dataList", logisticData.get(order.optString("orderId")));
                    it.putExtra("kdbh", kdbh);
                    it.putExtra("kdgsmc", order.optString("deliveryCompany"));
                    it.putExtra("pic", order.optString("productPic"));
                    it.putExtra("state", logisticState.get(order.optString("orderId")));
                    it.putExtra("goodsNumber", goodsNumber);
                    startActivity(it);
                }
            });
        }
        //are.put("0","已取消");
        //are.put("10","待付款");
        //are.put("15","线下支付待审核");
        //are.put("16","货到付款待发货");
        //are.put("20","已付款即线上支付待发货");
        //are.put("30","已发货");
        //are.put("40","已收货");
        //are.put("50","已完成,已评价");
        //are.put("55","已完成,已报销");
        //are.put("60","已结束");
        if(Util.isEmpty(kdgs) && Util.isEmpty(kdbh)){
            //下载支付订单
            buttonArea.setVisibility(View.GONE);
            logistic.setVisibility(View.GONE);
            codeAndName.setVisibility(View.VISIBLE);
        }
        switch (order.optInt("orderStatus")) {
            case 15:
                codeAndName.setVisibility(View.VISIBLE);
                logistic.setVisibility(View.GONE);
                buttonArea.setVisibility(View.GONE);
                break;
            case 2:
                logisticTitle.setText("配货中");
                logisticTime.setText("");
                logisticTime.setVisibility(View.GONE);
                logisticTitle.setVisibility(View.GONE);
                buttonArea.setVisibility(View.GONE);
                send_time.setVisibility(View.GONE);
                break;
            case 3://配送中订单
                break;
            case 4:
                confirm.setVisibility(View.VISIBLE);
                confirm.setText("去评论");
                confirm.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ARouter.getInstance()
                                .build("/store/StoreCommentUI")
                                .withString("orderId",getIntent().getStringExtra("orderId"))
                                .withString("order_id",getIntent().getStringExtra("order_id"))
                                .withString("name",getIntent().getStringExtra("name"))
                                .withString("icon",getIntent().getStringExtra("icon"))
                                .withString("goodsId",getIntent().getStringExtra("orderId"))
                                .withStringArrayList("goodsIds",getIntent().getStringArrayListExtra("goodsIds"))
                                .withStringArrayList("goodsNames",getIntent().getStringArrayListExtra("goodsNames"))
                                .withStringArrayList("goodsIcons",getIntent().getStringArrayListExtra("goodsIcons"))
                                .navigation();
                    }
                });
                break;
            case 0:
                confirm.setVisibility(View.GONE);
                break;
            case 55:
                confirm.setVisibility(View.GONE);
                break;
            case 60:
                confirm.setVisibility(View.GONE);
                break;
        }
        //商品信息
        JSONArray arrList=order.optJSONArray("orderProductDetailList");
        List<JSONObject> jsonObjList=new ArrayList<JSONObject>();
        if(arrList!=null && arrList.length()>0){
            for(int i=0;i<arrList.length();i++){
                jsonObjList.add(arrList.optJSONObject(i));
            }
            goodsNumber=jsonObjList.size();
        }
        recyclerview.setAdapter(new BaseRecyclerAdapter(recyclerview.getContext(),jsonObjList));
    }

    public void updData() {
        JSONObject params = new JSONObject();
        try {
            params.put("orderId", getIntent().getExtras().getString("orderId"));
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        BaseHttp.getInstance().query("member/order/getOrderDetail", params, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                    if(200==response.optInt("code")){
                        order = response.optJSONObject("data");
                        initData();
                        dismissLoding();
                    }else{
                        alert(response.optString("message"));
                        showLodingFail();
                    }
            }

            @Override
            public void onFail() {
                showLodingFail();
            }
        });
    }

    class BaseRecyclerAdapter extends AbsRecyclerAdapter<JSONObject> {

        public BaseRecyclerAdapter(Context ctx, List<JSONObject> list) {
            super(ctx, list);
        }

        @Override
        public int getItemLayoutId(int viewType) {
            return R.layout.mine_item_rv_mine_order_detail;
        }

        @Override
        public void bindData(RecyclerViewHolder holder, int position, JSONObject item) {
            //调用holder.getView(),getXXX()方法根据id得到控件实例，进行数据绑定即可
            holder.setText(R.id.goodsName, item.optString("productName"));
            holder.setText(R.id.goodsAttrs, item.optString("sp1")+item.optString("sp2")+item.optString("sp3"));
            holder.setText(R.id.goodsMinPrice, ""+ Util.doubleFormat(item.optDouble("productPrice")));
            holder.setText(R.id.goodsNumber, "x"+item.optString("productQuantity"));
            BaseImage.getInstance().loadXY(item.optString("productPic"), (ExpandImageView) holder.getView(R.id.goodsIcon));
            holder.setOnItemClickListener(new ItemOnclickListener(item.optString("goodsId")));
        }
        public class ItemOnclickListener implements OnClickListener {

            String goodsId="";

            public ItemOnclickListener(String goodsId){
                this.goodsId=goodsId;
            }

            @Override
            public void onClick(View view) {
                ARouter.getInstance().build("home/getProductInfo").withString("productId",goodsId).navigation();
            }
        }
    }

    HashMap<String, ArrayList<String>> logisticData=new HashMap<String, ArrayList<String>>();
    HashMap<String, Integer> logisticState=new HashMap<String, Integer>();
    public void loadLogisticData(String orderId,String kdgs,String kdbh) {
        JSONObject params = new JSONObject();
        try {
            params.put("orderId", orderId);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        BaseHttp.getInstance().query("member/order/getLogistics",params, new HttpCallBack() {
            public void onSuccess(JSONObject response) {
                ArrayList<String> list= new ArrayList<String>();
                    if (200 == response.optInt("code")) {
                        JSONObject data=response.optJSONObject("data");
                        //快递100状态:0在途中、1已揽收、2疑难、3已签收、4退签、5同城派送中、6退回、7转单等7个状态
                        switch (data.optInt("state")) {//转换为本地设置的快递状态
                            case 1: logisticState.put(orderId, 0);break;//对应物流详情页面'揽件'状态 
                            case 0: logisticState.put(orderId, 1);break;//对应物流详情页面'在途'状态 
                            case 5: logisticState.put(orderId, 2);break;//对应物流详情页面'派件'状态 
                            case 3: logisticState.put(orderId, 3);break;//对应物流详情页面'签收'状态 
                        }
                        JSONArray infoArray = data.optJSONArray("data");
                        for (int i = (infoArray.length() - 1); i >= 0; i--) {//倒序显示
                            String time=infoArray.optJSONObject(i).optString("time");
                            String context=infoArray.optJSONObject(i).optString("context");
                            if(i == 0){//最后一个状态
                                logisticTime.setText(time);
                                logisticTime.setVisibility(View.VISIBLE);
                                logisticTitle.setText(context);
                                logisticTitle.setVisibility(View.VISIBLE);
                            }
                            list.add(context+ "\n" + time);
                        }
                        logisticData.put(orderId, list);//写入物流信息
                    }else{
                        logisticData.put(orderId, new ArrayList<String>());
                        logisticState.put(orderId, -1);
                        logisticTitle.setText(response.optString("msg"));
                        logisticTitle.setVisibility(View.VISIBLE);
                        logisticTime.setVisibility(View.GONE);
                    }
            }
            public void onFail() {
                logisticData.put(orderId, new ArrayList<String>());
                logisticState.put(orderId, -1);
                logisticTitle.setText("物流查询异常 !");
                logisticTitle.setVisibility(View.VISIBLE);
                logisticTime.setVisibility(View.GONE);
            }
        });
    }
}
