package com.fzs.mine.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fzs.comn.ItemDecoration.MineOrderRFMItemDecoration;
import com.fzs.comn.model.MineOrderShop;
import com.fzs.comn.tools.Util;
import com.fzs.comn.widget.imageview.ExpandImageView;
import com.fzs.mine.R;
import com.hzh.frame.comn.callback.HttpCallBack;
import com.hzh.frame.comn.callback.CallBack;
import com.hzh.frame.core.HttpFrame.BaseHttp;
import com.hzh.frame.ui.fragment.AbsRecyclerViewFM;
import com.hzh.frame.widget.toast.BaseToast;
import com.hzh.frame.widget.xdialog.XDialog2Button;
import com.hzh.frame.widget.xrecyclerview.AbsRecyclerAdapter;
import com.hzh.frame.widget.xrecyclerview.RecyclerViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 订单列表
 * */
public class MineOrderRFM extends AbsRecyclerViewFM<MineOrderShop> {


    public static MineOrderRFM newInstance(int orderStatus) {
        MineOrderRFM myFragment = new MineOrderRFM();
        Bundle bundle = new Bundle();
        bundle.putInt("orderStatus",orderStatus);
        myFragment.setArguments(bundle);
        return myFragment;
    }

    @Override
    public boolean setTitleIsShow() {
        return false;
    }


    @Override
    protected int setLayoutId() {
        return R.layout.mine_ui_order_rv;
    }

    @Override
    protected RecyclerView.ItemDecoration setRecyclerViewItemDecoration() {
        return new MineOrderRFMItemDecoration(getActivity());
    }

    @Override
	protected void bindView(View view) {
		showLoding();
        setLoadPattern(2);
	}
    @Override
    protected String setHttpPath() {
        return "member/order/getOrderList";
    }

    @Override
    protected JSONObject setHttpParams() {
        JSONObject params = new JSONObject();
        try {
            params.put("orderStatus", getArguments().getInt("orderStatus"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }
    
	@Override
	protected List<MineOrderShop> handleHttpData(JSONObject response) {
		List<MineOrderShop> list = new ArrayList<>();
			if(200==response.optInt("code")){
				JSONArray listJson = response.optJSONArray("data");
				if(listJson!=null && listJson.length()>0){
					for (int i = 0; i < listJson.length(); i++) {
						try {
							JSONObject obj = (JSONObject) listJson.get(i);
							MineOrderShop model = new MineOrderShop();
							model.setImage(obj.optString("productPic"));// 商品图片
							model.setOrder_id(obj.optString("orderId"));
							model.setName(obj.optString("productName"));// 商品名称
							model.setShopname(obj.optString("storeName"));// 商店名称
							model.setState(obj.optString("orderStatusName"));// 商品状态
							model.setOrder_status(obj.optInt("orderStatus") + "");// 商品状态
							model.setCount(obj.optInt("productsNum") + "");// 数量
							model.setTotalPrice(obj.optDouble("payAmount") + "");// 总价
							model.setCompany_name(obj.optString("deliveryCompany"));// 物流公司名称
							model.setCompany_mark(obj.optString("company_mark").trim());// 物流公司编码
							model.setCourierCode(obj.optString("deliverySn").trim());// 运单号
							model.setOrderid(obj.optString("orderId"));// 订单编号
							model.setGoodsId(obj.optString("orderProductDetailList"));
                            model.setGoodsList(obj.optJSONArray("orderProductDetailList"));

                            ArrayList<String> listIcon=new ArrayList<String>();
                            ArrayList<String> listID=new ArrayList<String>();
                            ArrayList<String> listName=new ArrayList<String>();
                            List<JSONObject> listJsonObj=new ArrayList<JSONObject>();
                            if(model.getGoodsList()!=null && model.getGoodsList().length()>0){
                                for(int j=0;j<model.getGoodsList().length();j++){
                                    JSONObject jsonObject=model.getGoodsList().optJSONObject(j);
                                    listJsonObj.add(jsonObject);
                                    
                                    listIcon.add(jsonObject.optString("productPic"));
                                    listID.add(jsonObject.optString("productId"));
                                    listName.add(jsonObject.optString("productName"));
                                }
                            }
                            model.setGoodsListJsonObjct(listJsonObj);
                            model.setListIcon(listIcon);
                            model.setListID(listID);
                            model.setListName(listName);
							list.add(model);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}
		}
		dismissLoding();
		return list;
	}

	@Override
	protected void handleHttpDataFailure() {
		super.handleHttpDataFailure();
		showLodingFail();
	}

    @Override
    protected int setItemLayoutId(int itemType) {
        return R.layout.mine_item_rv_order;
    }
	
    @Override
    protected void bindItemData(RecyclerViewHolder holder, int position, MineOrderShop model) {
        holder.setText(R.id.shopname,model.getShopname());
        holder.setText(R.id.count,"共" + model.getCount() + "件商品");
        holder.setText(R.id.state,model.getState());
        holder.setText(R.id.price,Util.doubleFormat(model.getTotalPrice()));
//        if(model.getBx()>0){
//            holder.setText(R.id.bx,"(可报销:"+Util.doubleFormat(model.getBx())+")");
//        }else{
//            holder.setText(R.id.bx,"");
//        }
        holder.getView(R.id.buttonLeft).setVisibility(View.GONE);
        holder.getView(R.id.buttonCenter).setVisibility(View.GONE);
        holder.getView(R.id.buttonRight).setVisibility(View.GONE);
        holder.itemView.setTag(model);
        holder.getView(R.id.buttonLeft).setTag(model);
        holder.getView(R.id.buttonCenter).setTag(model);
        holder.getView(R.id.buttonRight).setTag(model);
        if(model.getListIcon().size()>1){
            for(String icon:model.getListIcon()){
                ExpandImageView iv=new ExpandImageView(getActivity());
                LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(Util.dip2px(getActivity(), 60),Util.dip2px(getActivity(), 60));
                iv.setLayoutParams(lp);
                lp.leftMargin=Util.dip2px(getActivity(), 10);
                iv.setImageURI(icon);
            }
        }
        if ("0".equals(model.getOrder_status())) {
            holder.setOnItemClickListener(new SelectLogisticOnClick(SelectLogisticOnClick.GO_PAY,position));
        }else{
            holder.setOnItemClickListener(new SelectLogisticOnClick(SelectLogisticOnClick.ORDER_INFO,position));
        }
        switch (Integer.parseInt(model.getOrder_status())) {
            case 0://未支付订单
                holder.getView(R.id.buttonCenter).setVisibility(View.VISIBLE);
                holder.setText(R.id.buttonCenter,"取消订单");
                holder.getView(R.id.buttonCenter).setOnClickListener(new SelectLogisticOnClick(SelectLogisticOnClick.CANCEL,position));
                holder.getView(R.id.buttonRight).setVisibility(View.VISIBLE);
                holder.setText(R.id.buttonRight,"去支付");
                holder.getView(R.id.buttonRight).setOnClickListener(new SelectLogisticOnClick(SelectLogisticOnClick.GO_PAY,position));
                break;
            case 1://待发货
                holder.getView(R.id.buttonRight).setVisibility(View.VISIBLE);
                holder.setText(R.id.buttonRight,"查看详情");
                holder.getView(R.id.buttonRight).setOnClickListener(new SelectLogisticOnClick(SelectLogisticOnClick.ORDER_INFO,position));
                break;
            case 2://配送中订单
                loadLogisticData(model.getOrderid(), model.getCompany_mark(), model.getCourierCode());
                holder.getView(R.id.buttonCenter).setVisibility(View.VISIBLE);
                holder.setText(R.id.buttonCenter,"查看物流");
                holder.getView(R.id.buttonCenter).setOnClickListener(new SelectLogisticOnClick(SelectLogisticOnClick.SELECT_LOGISTIC,position));
                holder.getView(R.id.buttonRight).setVisibility(View.VISIBLE);
                holder.setText(R.id.buttonRight,"确认收货");
                holder.getView(R.id.buttonRight).setOnClickListener(new SelectLogisticOnClick(SelectLogisticOnClick.CONFIRM,position));
                break;
            case 3://已完成,已评价
                loadLogisticData(model.getOrderid(), model.getCompany_mark(), model.getCourierCode());
                holder.getView(R.id.buttonCenter).setVisibility(View.VISIBLE);
                holder.setText(R.id.buttonCenter,"查看物流");
                holder.getView(R.id.buttonCenter).setOnClickListener(new SelectLogisticOnClick(SelectLogisticOnClick.SELECT_LOGISTIC,position));
                holder.getView(R.id.buttonRight).setVisibility(View.VISIBLE);
                holder.setText(R.id.buttonRight,"查看详情");
                holder.getView(R.id.buttonRight).setOnClickListener(new SelectLogisticOnClick(SelectLogisticOnClick.ORDER_INFO,position));
                break;
            case 4://已关闭
                holder.getView(R.id.buttonRight).setVisibility(View.VISIBLE);
                holder.setText(R.id.buttonRight,"删除订单");
                holder.getView(R.id.buttonRight).setOnClickListener(new SelectLogisticOnClick(SelectLogisticOnClick.CANCEL_ORDER,position));
                break;
            case 5: //已付款待审核
                holder.getView(R.id.buttonCenter).setVisibility(View.GONE);
                holder.getView(R.id.buttonRight).setVisibility(View.GONE);
                break;
        }
        ((RecyclerView)holder.getView(R.id.recyclerviewChild)).setLayoutManager(new LinearLayoutManager(getActivity()));
        ((RecyclerView)holder.getView(R.id.recyclerviewChild)).setAdapter(new BaseRecyclerAdapter(getActivity(),model.getGoodsListJsonObjct(),model.getOrder_status(),position,model));
    }

    class BaseRecyclerAdapter extends AbsRecyclerAdapter<JSONObject> {
        
        String orderState="0";
        int orderPosition=0;
        MineOrderShop parentModel;

        public BaseRecyclerAdapter(Context ctx, List<JSONObject> list,String orderState,int orderPosition,MineOrderShop parentModel) {
            super(ctx, list);
            this.orderState=orderState;
            this.orderPosition=orderPosition;
            this.parentModel=parentModel;
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
            holder.setText(R.id.goodsMinPrice, Util.doubleFormat(item.optDouble("productPrice")));
            holder.setText(R.id.goodsNumber, "x"+item.optString("productQuantity"));
            ((ExpandImageView) holder.getView(R.id.goodsIcon)).setImageURI(item.optString("productPic"));
            holder.itemView.setTag(parentModel);
            if ("0".equals(orderState)) {
                holder.setOnItemClickListener(new SelectLogisticOnClick(SelectLogisticOnClick.GO_PAY,orderPosition));
            }else{
                holder.setOnItemClickListener(new SelectLogisticOnClick(SelectLogisticOnClick.ORDER_INFO,orderPosition));
            }
        }

    }

	//查看物流
	public class SelectLogisticOnClick implements OnClickListener{
        static final int SELECT_LOGISTIC=1;//查看物流
        static final int GO_PAY=2;//去支付
        static final int CANCEL_ORDER=3;//删除订单
        static final int CONFIRM=4;//确认收货
        static final int COMMENT=5;//去评论
        static final int ORDER_INFO=6;//查看详情
        static final int REIMBURSEMENT=7;//去报销
        static final int CANCEL=0;//取消订单
        int theFlag=SELECT_LOGISTIC;
        int position=-1;
        
        public SelectLogisticOnClick(int theFlag,int position){
            this.theFlag=theFlag;
            this.position=position;
        }
        
        @Override
        public void onClick(final View view) {
            final MineOrderShop model= (MineOrderShop) view.getTag();
            switch (theFlag){
                case SELECT_LOGISTIC://查看物流
                    ARouter.getInstance()
                            .build("/mine/MineOrderInfoLogisticUI")
                            .withStringArrayList("dataList", logisticData.get(model.getOrderid()))
                            .withInt("state", logisticState.get(model.getOrderid()))
                            .withString("kdbh", model.getCourierCode())
                            .withString("kdgsmc", model.getCompany_name())
                            .withString("pic", model.getImage())
                            .withInt("goodsNumber", Integer.parseInt(model.getCount()))
                            .navigation();
                    break;
                case GO_PAY://去支付
                    ARouter.getInstance()
                            .build("/mine/MineOrderInfoNotPayUI")
                            .withInt("outline", model.getOutline())
                            .withString("orderId", model.getOrderid())
                            .withString("order_id", model.getOrder_id())
                            .navigation();
                    break;
                case CANCEL_ORDER://删除订单
                    new XDialog2Button(getActivity()).setMsg("你确定要删除么？").setCallback(new CallBack() {
                        @Override
                        public void onSuccess(Object object) {
                            JSONObject params=new JSONObject();
                            try {
                                params.put("orderId", model.getOrder_id());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            BaseHttp.getInstance().write(getActivity(),"member/order/deleteOrder", params, new HttpCallBack(){
                                @Override
                                public void onSuccess(JSONObject response) {
                                    if(200 == response.optInt("code")){
                                        getAdapter().removeItem(position);
                                    }else{
                                        alert(response.optString("message"));
                                    }
                                }
                                @Override
                                public void onFail() {
                                    alert(BaseToast.MSG_DEFAULT);
                                }
                            });
                        }
                    }).show();
                    break;
                case CANCEL://取消订单
                    new XDialog2Button(getActivity()).setMsg("你确定要取消么？").setCallback(new CallBack() {
                        @Override
                        public void onSuccess(Object object) {
                            JSONObject params=new JSONObject();
                            try {
                                params.put("orderId", model.getOrder_id());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            BaseHttp.getInstance().write(getActivity(),"member/order/cancelOrder", params, new HttpCallBack(){
                                @Override
                                public void onSuccess(JSONObject response) {
                                    if(200 == response.optInt("code")){
                                        getAdapter().removeItem(position);
                                    }else{
                                        alert(response.optString("message"));
                                    }
                                }
                                @Override
                                public void onFail() {
                                    alert(BaseToast.MSG_DEFAULT);
                                }
                            });
                        }
                    }).show();
                    break;
                case CONFIRM://确认收货
                    JSONObject params=new JSONObject();
                    try {
                        params.put("orderId", model.getOrder_id());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    BaseHttp.getInstance().write(getActivity(),"member/order/confirmReceiving", params, new HttpCallBack(){
                        @Override
                        public void onSuccess(JSONObject response) {
                            if(200==response.optInt("code")){
                                alert(response.optString("message"));
                                getAdapter().removeItem(position);
                            }else{
                                alert("服务器偷懒了失败了！");
                            }
                        }
                        @Override
                        public void onFail() {
                            alert("服务器偷懒了失败了！");
                        }
                    });
                    break;
                case COMMENT://去评论
                    ARouter.getInstance()
                            .build("/store/StoreCommentUI")
                            .withString("orderId",model.getOrderid())
                            .withString("order_id",model.getOrder_id())
                            .withString("name",model.getName())
                            .withString("icon",model.getImage())
                            .withString("goodsId",model.getGoodsId())
                            .withStringArrayList("goodsIds",model.getListID())
                            .withStringArrayList("goodsNames",model.getListName())
                            .withStringArrayList("goodsIcons",model.getListIcon())
                            .navigation();
                    break;
                case ORDER_INFO://查看详情
                    Intent it4 = new Intent(getActivity(),MineOrderInfoYesPayUI.class);
                    it4.putExtra("orderId", model.getOrderid());
                    it4.putExtra("order_id", model.getOrder_id());
                    it4.putExtra("name", model.getName());
                    it4.putExtra("icon", model.getImage());
                    it4.putExtra("goodsId", model.getGoodsId());
                    it4.putStringArrayListExtra("goodsIds", model.getListID());
                    it4.putStringArrayListExtra("goodsNames", model.getListName());
                    it4.putStringArrayListExtra("goodsIcons", model.getListIcon());
                    if (model.getOrder_status().equals("5")){
                        it4.putExtra("status", "0");
                    }else {
                        it4.putExtra("status", "1");
                    }
                    startActivityForResult(it4,2);
                    break;
                case REIMBURSEMENT://去报销
                    new XDialog2Button(getActivity()).setMsg("你确定要报销吗?报销后将不可退货!").setCallback(new CallBack() {
                        @Override
                        public void onSuccess(Object object) {
                            JSONObject params=new JSONObject();
                            try {
                                params.put("orderId", model.getOrder_id());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            BaseHttp.getInstance().write(getActivity(),"4022", params, new HttpCallBack(){
                                @Override
                                public void onSuccess(JSONObject response) {
                                    if("1".equals(response.optString("result"))){
                                        JSONObject data=response.optJSONObject("data");
                                        if(data.optInt("code")>0){
                                            mSwipeRefreshLayout.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    mSwipeRefreshLayout.setRefreshing(true);
                                                }
                                            });
                                            onRefresh();
                                        }
                                        alert(data.optString("msg"));
                                    }else{
                                        alert("服务器偷懒了失败了！");
                                    }
                                }
                                @Override
                                public void onFail() {
                                    alert("服务器偷懒了失败了！");
                                }
                            });
                        }
                    }).show();
                    break;
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
                            list.add(context+ "\n" + time);
                        }
                        logisticData.put(orderId, list);//写入物流信息
                }else{
                    logisticData.put(orderId, new ArrayList<String>());
                    logisticState.put(orderId, -1);
                }
            }
            public void onFail() {
                logisticData.put(orderId, new ArrayList<String>());
                logisticState.put(orderId, -1);
            }
        });
    }
	
    public class CommentBean{
        String name;//商品名称
        String icon;//上屏图标
        String evaluate_seller_val;//好评,中评,差评
        String evaluate_info;//留言
        String evaluate_goods_id;//商品ID
        String description_evaluate;//描述相符1-5
        String ship_evaluate;//发货速度1-5
        String service_evaluate;//服务态度1-5

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getEvaluate_seller_val() {
            return evaluate_seller_val;
        }

        public void setEvaluate_seller_val(String evaluate_seller_val) {
            this.evaluate_seller_val = evaluate_seller_val;
        }

        public String getEvaluate_info() {
            return evaluate_info;
        }

        public void setEvaluate_info(String evaluate_info) {
            this.evaluate_info = evaluate_info;
        }

        public String getEvaluate_goods_id() {
            return evaluate_goods_id;
        }

        public void setEvaluate_goods_id(String evaluate_goods_id) {
            this.evaluate_goods_id = evaluate_goods_id;
        }

        public String getDescription_evaluate() {
            return description_evaluate;
        }

        public void setDescription_evaluate(String description_evaluate) {
            this.description_evaluate = description_evaluate;
        }

        public String getShip_evaluate() {
            return ship_evaluate;
        }

        public void setShip_evaluate(String ship_evaluate) {
            this.ship_evaluate = ship_evaluate;
        }

        public String getService_evaluate() {
            return service_evaluate;
        }

        public void setService_evaluate(String service_evaluate) {
            this.service_evaluate = service_evaluate;
        }
    }
}
