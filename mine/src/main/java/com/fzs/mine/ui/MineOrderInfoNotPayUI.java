package com.fzs.mine.ui;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fzs.comn.tools.Util;
import com.fzs.comn.widget.imageview.ExpandImageView;
import com.fzs.mine.R;
import com.hzh.frame.comn.callback.CallBack;
import com.hzh.frame.comn.callback.HttpCallBack;
import com.hzh.frame.core.HttpFrame.BaseHttp;
import com.hzh.frame.core.ImageFrame.BaseImage;
import com.hzh.frame.ui.activity.BaseUI;
import com.hzh.frame.widget.xdialog.XDialog2Button;
import com.hzh.frame.widget.xrecyclerview.AbsRecyclerAdapter;
import com.hzh.frame.widget.xrecyclerview.AutoLinearLayoutManager;
import com.hzh.frame.widget.xrecyclerview.RecyclerViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 未支付订单详情
 * */
@Route(path = "/mine/MineOrderInfoNotPayUI")
public class MineOrderInfoNotPayUI extends BaseUI implements OnClickListener{
    TextView payHelpMoney;
//    LinearLayout payHelpInfo;
    TextView fuzhi;
    RecyclerView recyclerview;
    private TextView orderCode,stateName,storeName,transportName,invoiceFlag,goodsMoney,transportMoney,sumMoney,addTime;
    private TextView addresssName,addresssPhone,addresssContent;
    private RelativeLayout rl_order;
    private TextView cancel,confirm;
    private String totalPrice;
    private String orderCodeNumber;
    private int ifCart=-1;

    public void findViewById(){
//        payHelpMoney=findViewById(R.id.payHelpMoney);
        fuzhi=findViewById(R.id.fuzhi);
        recyclerview=findViewById(R.id.recyclerview);
    }
    
    @Override
    protected void onCreateBase() {
        setContentView(R.layout.mine_ui_order_info_not_pay);
        findViewById();
        getTitleView().setContent("订单详情");
        showLoding();
        initView();
    }

    public void initView() {
        cancel = (TextView) findViewById(R.id.cancel);
        confirm = (TextView) findViewById(R.id.confirm);
        orderCode=(TextView) findViewById(R.id.orderCode);
        stateName=(TextView) findViewById(R.id.stateName);
        addresssName=(TextView) findViewById(R.id.addresssName);
        addresssPhone=(TextView) findViewById(R.id.addresssPhone);
        addresssContent=(TextView) findViewById(R.id.addresssContent);
        storeName=(TextView) findViewById(R.id.storeName);
        transportName=(TextView) findViewById(R.id.transportName);
        invoiceFlag=(TextView) findViewById(R.id.invoiceFlag);
        goodsMoney=(TextView) findViewById(R.id.goodsMoney);
        transportMoney=(TextView) findViewById(R.id.transportMoney);
        sumMoney=(TextView) findViewById(R.id.sumMoney);
        addTime=(TextView) findViewById(R.id.addTime);
        rl_order = (RelativeLayout) findViewById(R.id.rl_order);
     fuzhi.setOnClickListener(this);
        //取消订单
        cancel.setOnClickListener(this);
        //确认订单
        confirm.setOnClickListener(this);
        recyclerview.setLayoutManager(new AutoLinearLayoutManager(this));
        loadData();
    }

    public void loadData() {
        JSONObject params = new JSONObject();
        try {
            params.put("orderId", getIntent().getStringExtra("orderId"));
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        BaseHttp.getInstance().query("member/order/getOrderDetail", params, new HttpCallBack(){
            @Override
            public void onSuccess(JSONObject response) {
                    JSONObject data = response.optJSONObject("data");
                    if(200==response.optInt("code")){
                        JSONObject order=response.optJSONObject("data");
                        orderCodeNumber=order.optString("orderSn");
                        
                        orderCode.setText("订单号:"+order.optString("orderSn"));
                        stateName.setText(order.optString("orderStatusName"));
                        addresssName.setText("收货人:"+order.optString("receiverName"));
                        addresssContent.setText("收货地址:"+order.optString("receiverProvince")+order.optString("receiverCity")+order.optString("receiverRegion")+order.optString("receiverDetailAddress"));
                        addresssPhone.setText(order.optString("receiverPhone"));
                        storeName.setText(order.optString("storeName"));
                        goodsMoney.setText(""+ Util.doubleFormat(order.optDouble("payAmount")));
                        transportMoney.setText(""+Util.doubleFormat("10"));
                        sumMoney.setText(""+Util.doubleFormat(order.optDouble("payAmount")));
                        totalPrice=order.optString("payAmount");
                        transportName.setText(order.optString("transport"));
                        /**0:个人(不开发票也是0)  1:公司  */
                        if(1==order.optInt("invoiceType")){
                            invoiceFlag.setText("发票抬头:"+order.optString("invoice"));
                        }else{
                            invoiceFlag.setText("发票抬头:个人");
                        }
                        addTime.setText("下单时间:"+order.optString("createTime"));
                        //商品信息
                        JSONArray arrList=order.optJSONArray("orderProductDetailList");
                        List<JSONObject> jsonObjList=new ArrayList<JSONObject>();
                        if(arrList!=null && arrList.length()>0) {
                            for (int i = 0; i < arrList.length(); i++) {
                                jsonObjList.add(arrList.optJSONObject(i));
                            }
                            recyclerview.setAdapter(new BaseRecyclerAdapter(recyclerview.getContext(),jsonObjList));
                        }
                        dismissLoding();
                    }else{
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
            holder.setOnItemClickListener(new ItemOnclickListener(item.optString("productId")));
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
    
    

    @SuppressLint("NewApi")
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.fuzhi) {
            ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData myClip = ClipData.newPlainText("ORDERID", orderCodeNumber);
            cmb.setPrimaryClip(myClip);
            alert("成功复制订单号:" + orderCodeNumber);
        }
//        
            //         else if (id == R.id.payHelpInfo) {
//            Intent in1 = new Intent(this, MinePayHelpLUI.class);
//            in1.putExtra("orderId", getIntent().getStringExtra("orderId"));
//            startActivity(in1);
//        } else if 
        //        
        if (id == R.id.cancel) {
            new XDialog2Button(MineOrderInfoNotPayUI.this).setMsg("你确定要取消吗？").setCallback(new CallBack() {
                @Override
                public void onSuccess(Object object) {
                    JSONObject params = new JSONObject();
                    try {
                        params.put("orderId", getIntent().getStringExtra("order_id"));
                        params.put("state", 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    BaseHttp.getInstance().write(MineOrderInfoNotPayUI.this, "member/order/cancelOrder", params, new HttpCallBack() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            if (200 == response.optInt("code")) {
                                JSONObject data = response.optJSONObject("data");
                                setResult(1);
                                finish();
                            } else {
                                alert("取消失败");
                            }
                        }

                        @Override
                        public void onFail() {
                            alert("取消失败");
                        }
                    });
                }
            }).show();
        } else if (id == R.id.confirm) {
            if (1 == getIntent().getIntExtra("outline", 0)) {
                ARouter.getInstance().build("/pay/PaymentLineUI").withString("orderId",getIntent().getStringExtra("order_id")).navigation();
            } else {
                if (ifCart == 1) {//购物车订单
                    ARouter.getInstance()
                            .build("/pay/PaymentUI")
                            .withString("orderId",getIntent().getStringExtra("order_id"))
                            .withString("money",totalPrice)
                            .withString("httpPath","4021")
                            .navigation();
                } else if (ifCart == 0) {//不是购物车订单
                    ARouter.getInstance()
                            .build("/pay/PaymentUI")
                            .withString("orderId",getIntent().getStringExtra("order_id"))
                            .withString("money",totalPrice)
                            .withString("httpPath","3015")
                            .navigation();
                } else {
                    JSONObject params=new JSONObject();
                    try{
                        params.put("orderId",getIntent().getStringExtra("orderId"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                BaseHttp.getInstance().write(this,"member/order/getPayTypeList", params, new HttpCallBack() {
                                public void onSuccess(JSONObject response) {
                                    if (response.optInt("code") == 200) {
                                        JSONObject data = response.optJSONObject("data");
                                        ARouter.getInstance()
                                                .build("/pay/PaymentUI")
                                                .withString("orderId",getIntent().getStringExtra("orderId"))
                                                .withString("money",totalPrice)
                                                .withString("httpPath","member/order/payment")
                                                .withString("orderPayTypeList",data.optJSONArray("orderPayTypeList").toString())
                                                .navigation();
                                    }
                                }
                            }
                            );

                }
            }
        }
    }
}
