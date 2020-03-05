package com.fzs.mine.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fzs.comn.matisse.GifSizeFilter;
import com.fzs.comn.model.UploadImage;
import com.fzs.comn.model.User;
import com.fzs.comn.tools.ImageUtil;
import com.fzs.comn.tools.QiniuTools;
import com.fzs.comn.tools.UriUtil;
import com.fzs.comn.tools.UserTools;
import com.fzs.comn.tools.Util;
import com.fzs.comn.widget.imageview.ExpandImageView;
import com.fzs.mine.R;
import com.hzh.frame.comn.ItemDecoration.BaseGridSpacingItemDecoration;
import com.hzh.frame.comn.annotation.ViewInject;
import com.hzh.frame.comn.callback.HttpCallBack;
import com.hzh.frame.core.HttpFrame.BaseHttp;
import com.hzh.frame.ui.activity.BaseUI;
import com.hzh.frame.util.AndroidUtil;
import com.hzh.frame.widget.xdialog.XDialogSubmit;
import com.hzh.frame.widget.xrecyclerview.BaseRecyclerAdapter;
import com.hzh.frame.widget.xrecyclerview.RecyclerViewHolder;
import com.yalantis.ucrop.UCrop;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 银行卡充值
 * */
@Route(path = "/mine/MinePayBankUI")
public class MinePayBankUI extends BaseUI {
    private RecyclerView imageUploadRecyclerView; //图片
    private TextView sName;
    private TextView sCardName;
    private TextView sCard;
    private EditText fName;
    private EditText fCardName;
    private EditText fCard;
    private EditText fMoney;
    private Button submitButton;
    private static final int REQUEST_IMAGE = 1;// 图片选择
    private static final int REQUEST_CROP = 2;// 图片剪裁
    private String rechargeType = "";
    private String collectionId = "";
    private List<String> issueImageUrl;
    private String imageUrl = "";
    private int zero = 0;
    
	@Override
	protected void onCreateBase() {
		setContentView(R.layout.mine_ui_pay_bank);
		getTitleView().setContent("银行卡充值");
        rechargeType = getIntent().getStringExtra("rechargeType");
        imageUploadRecyclerView = findViewById(R.id.image_upload_recyclerView);
        sName = findViewById(R.id.sName);
        sCardName = findViewById(R.id.sCardName);
        sCard = findViewById(R.id.sCard);
        fName = findViewById(R.id.fName);
        fCardName = findViewById(R.id.fCardName);
        fCard = findViewById(R.id.fCard);
        fMoney = findViewById(R.id.fMoney);
        submitButton = findViewById(R.id.submitButton);
		initImageUploadGridview();
        getCollectionInfo();
        submitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPay();
            }
        });
	}
	
    public void initImageUploadGridview() {
        imageUploadRecyclerView.setLayoutManager(new GridLayoutManager(MinePayBankUI.this,5));
        imageUploadRecyclerView.addItemDecoration(new BaseGridSpacingItemDecoration(MinePayBankUI.this,5,3));
        List<UploadImage> resourceList;
        resourceList = new ArrayList<>();
        resourceList.add(new UploadImage());
        imageUploadRecyclerView.setAdapter(new ItemAdapter(MinePayBankUI.this,resourceList));
    }

    
    private class ItemAdapter extends BaseRecyclerAdapter<UploadImage> {

        public ItemAdapter(Context ctx, List<UploadImage> list) {
            super(ctx, list);
        }

        @Override
        public int getItemLayoutId(int viewType) {
            return R.layout.mine_item_gv_image_upload;
        }

        @Override
        public void bindData(RecyclerViewHolder holder, final int position, UploadImage item) {
            if (!Util.isEmpty(item.getUri())) {
                //正常显示图片
                ((ImageView) holder.getView(R.id.image)).setImageURI(Uri.parse(item.getUri()));
                ((ImageView) holder.getView(R.id.close)).setVisibility(View.VISIBLE);
                holder.getView(R.id.close).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getDatalist().remove(position);
                        notifyDataSetChanged();
                    }
                });
                holder.setOnItemClickListener(null);
            } else {
                //显示添加图片按钮
                ((ImageView) holder.getView(R.id.image)).setImageResource(R.mipmap.mine_image_upload);
                ((ImageView) holder.getView(R.id.close)).setVisibility(View.GONE);
                holder.setOnItemClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openCamera(view);
                    }
                });
            }
        }

        public List<UploadImage> getDatalist() {
            return super.getDatas();
        }
    }

    /**
     * 相机
     */
    public void openCamera(View view) {
        Matisse.from(this).choose(MimeType.ofImage()).theme(R.style.Matisse_Dracula)
                //                .countable(true)//有序选择图片 123456...
                .maxSelectable(9)//最大选择数量为1
                .countable(false).capture(true)//拍照
                .captureStrategy(new CaptureStrategy(true, AndroidUtil.getPackageName() + ".fileProvider"))//拍照
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K)).maxSelectable(9).originalEnable(true).maxOriginalSize(10).imageEngine(new PicassoEngine()).forResult(REQUEST_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //        StatusBarUtil.setWhileIcon(this);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE:// 图片选择
                    if (resultCode == -1) {//-1:确定 0:取消
                        List<Uri> imageUris = Matisse.obtainResult(data);
                        if (imageUris != null && imageUris.size() > 0) {
                            for (int i = 0;i < imageUris.size();i++){
                                setImage(imageUris.get(i));
                            }
                        }
                    }
                    break;
                case REQUEST_CROP:// 裁剪图片
                    setImage(UCrop.getOutput(data));
                    break;
            }
        }
    }

    private Bitmap headBitmap;//头像
    private Uri localHead;

    public void setImage(Uri uri) {
        localHead = uri;
        headBitmap = BitmapFactory.decodeFile(UriUtil.getRealFilePath(MinePayBankUI.this, uri), getBitmapOption(1)); //将图片的长和宽缩小味原来的1/2
        headBitmap = ImageUtil.Bytes2Bimap(ImageUtil.zoom(headBitmap, 200));

        ItemAdapter itemAdapter = (ItemAdapter) imageUploadRecyclerView.getAdapter();
        List<UploadImage> list = itemAdapter.getDatalist();
        UploadImage model = new UploadImage();
        model.setUri(UriUtil.getRealFilePath(MinePayBankUI.this, uri));
        list.add(list.size()-1, model);
        itemAdapter.notifyDataSetChanged();
    }

    private BitmapFactory.Options getBitmapOption(int inSampleSize) {
        System.gc();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return options;
    }
    
    private void getCollectionInfo(){
        JSONObject params=new JSONObject();
        try {
            params.put("rechargeType", rechargeType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        BaseHttp.getInstance().query("finance/getCollectionInfo", params, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);
                if (response.optInt("code") == 200){
                    JSONObject data = response.optJSONObject("data");
                    if (data != null && data.length() > 0){
                        collectionId = data.optString("collectionId");
                        sName.setText(data.optString("bankAccount"));
                        sCardName.setText(data.optString("bankName"));
                        sCard.setText(data.optString("bankCardNo"));
                    }
                }else {
                    alert(response.optString("message"));
                }
            }

            @Override
            public void onFailure() {
                super.onFailure();
                alert("请检查您的网络");
            }
        });
    }

    private void getFileList(fileCallBack fileCallBack){
        List<HashMap<String,Object>> fileList;
        ItemAdapter adapter= (ItemAdapter) imageUploadRecyclerView.getAdapter();
        List<UploadImage> list = adapter.getDatalist();
        issueImageUrl = new ArrayList<>();
        imageUrl = "";
        zero = 0;
        if (list != null && list.size() > 0){
            for(int i=0;i<list.size();i++){
                Log.e("xxxx","222222222222222" + "/" + list.size() + "/" + i);
                fileList=new ArrayList<>();
                UploadImage model=list.get(i);
                if (!Util.isEmpty(model.getUri())) {
                    HashMap<String,Object> map = new HashMap<>();
                    map.put("name","file");
                    map.put("file",new File(model.getUri()));
                    fileList.add(map);
                }
                int finalI = i;
                BaseHttp.getInstance().uploadFile(MinePayBankUI.this,"file/upload", fileList, new HttpCallBack() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        super.onSuccess(response);
                        Log.e("xxxx","55555555555555555");
                        if (response.optInt("code") == 200){
                            JSONObject data = response.optJSONObject("data");
                            if (data != null && data.length() > 0){
                                if (Util.isEmpty(imageUrl)){
                                    imageUrl = data.optString("url");
                                }else {
                                    imageUrl = imageUrl + "," + imageUrl;
                                }
                            }
                        }
                        if (finalI + 1 == list.size()){
                            Log.e("xxxx","3333333333333333");
                            fileCallBack.onSuccess(imageUrl);
                        }
                    }
                });
            }
        }else {
            Log.e("xxxx","4444444444444444");
            fileCallBack.onSuccess("");
        }
    }
    
    private void submitPay(){
        String name = fName.getText().toString().trim();
        String cardName = fCardName.getText().toString().trim();
        String card = fCard.getText().toString().trim();
        String money = fMoney.getText().toString().trim();
        ItemAdapter itemAdapter = (ItemAdapter) imageUploadRecyclerView.getAdapter();
        List<UploadImage> list = itemAdapter.getDatalist();
        if (Util.isEmpty(name)){
            alert("请输入账户名");
            return;
        }
        if (Util.isEmpty(cardName)){
            alert("请输入银行名称");
            return;
        }
        if (Util.isEmpty(card)){
            alert("请输入银行卡号");
            return;
        }
        if (Util.isEmpty(money)){
            alert("请输入充值金额");
            return;
        }
        if (list != null && list.size() > 0){
            
        }else {
            alert("请上传截图");
            return;
        }
        
        getFileList(new fileCallBack() {
            @Override
            public void onSuccess(String imgUrl) {
                Log.e("xxxx","1111111111111");
                JSONObject params=new JSONObject();
                try   {
                    params.put("collectionId", collectionId);
                    params.put("paymentAccountName", name);
                    params.put("paymentBankCardNo", card);
                    params.put("paymentBankName", cardName);
                    params.put("paymentScreenshot", imgUrl);
                    params.put("value", money);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                BaseHttp.getInstance().query("finance/recharge", params, new HttpCallBack() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        super.onSuccess(response);
                        if (response.optInt("code") == 200){
                            fName.getText().clear();
                            fCardName.getText().clear();
                            fCard.getText().clear();
                            fMoney.getText().clear();
                            initImageUploadGridview();
                        }else {

                        }
                        alert(response.optString("message"));
                    }
                });
            }

            @Override
            public void onFail(String msg) {
                
            }
        });
    }
    public interface fileCallBack {
        void onSuccess(String imgUrl);

        void onFail(String msg);
    }
    

}
