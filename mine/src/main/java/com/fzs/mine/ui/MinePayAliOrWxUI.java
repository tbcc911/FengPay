package com.fzs.mine.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fzs.comn.matisse.GifSizeFilter;
import com.fzs.comn.model.UploadImage;
import com.fzs.comn.tools.ImageTools;
import com.fzs.comn.tools.ImageUtil;
import com.fzs.comn.tools.UriUtil;
import com.fzs.comn.tools.Util;
import com.fzs.comn.widget.imageview.ExpandImageView;
import com.fzs.mine.R;
import com.hzh.frame.comn.ItemDecoration.BaseGridSpacingItemDecoration;
import com.hzh.frame.comn.callback.CallBack;
import com.hzh.frame.comn.callback.HttpCallBack;
import com.hzh.frame.core.HttpFrame.BaseHttp;
import com.hzh.frame.ui.activity.BaseUI;
import com.hzh.frame.util.AndroidUtil;
import com.hzh.frame.widget.xdialog.XDialog1Button;
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

import io.reactivex.internal.schedulers.NewThreadWorker;

/**
 *支付宝或者微信充值
 * */
@Route(path = "/mine/MinePayAliOrWxUI")
public class MinePayAliOrWxUI extends BaseUI {
    private String rechargeType = "";
    private Button submitButton;
    private Button saveImg;
    private RecyclerView imageUploadRecyclerView; //图片
    private ExpandImageView payQr;
    private static final int REQUEST_IMAGE = 1;// 图片选择
    private static final int REQUEST_CROP = 2;// 图片剪裁
    XDialog1Button mXDialog1Button;
    
	@Override
	protected void onCreateBase() {
		setContentView(R.layout.mine_ui_pay_aliorwx);
        showLoding();
		getTitleView().setContent(getIntent().getStringExtra("payTitle"));
        rechargeType = getIntent().getStringExtra("rechargeType");
        submitButton = findViewById(R.id.submitButton);
        saveImg = findViewById(R.id.saveImg);
        imageUploadRecyclerView = findViewById(R.id.image_upload_recyclerView);
        payQr = findViewById(R.id.payQr);
        getCollectionInfo();
        initImageUploadGridview();
        saveImg.setOnClickListener(v -> {
            Bitmap bitmap= ImageTools.getImageViewBitmap(payQr);
            if (submitButton.equals("1")){
                ImageTools.saveBitmap2Camera(this,bitmap,"ALipay");
            }else {
                ImageTools.saveBitmap2Camera(this,bitmap,"Wxpay");
            }
            mXDialog1Button=new XDialog1Button(this, "二维码已保存到相册", new CallBack() {
                @Override
                public void onSuccess(Object o) {
                    
                }
            });
            mXDialog1Button.setButtonName("确定").show();
        });
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
                        payQr.setImageURI(data.optString("qrCode"));
                    }
                }else {
                    alert(response.optString("message"));
                }
                dismissLoding();
            }

            @Override
            public void onFailure() {
                super.onFailure();
                alert("请检查您的网络");
                dismissLoding();
            }
        });
    }

    //保存
    class SaveCameraCallBack extends CallBack {

        @Override
        public void onSuccess(Object o) {
            
//            Intent lan = getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_LAUNCHER);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.setComponent(lan.getComponent());
//            startActivity(intent);
        }
    }

    public void initImageUploadGridview() {
        imageUploadRecyclerView.setLayoutManager(new GridLayoutManager(MinePayAliOrWxUI.this,5));
        imageUploadRecyclerView.addItemDecoration(new BaseGridSpacingItemDecoration(MinePayAliOrWxUI.this,5,3));
        List<UploadImage> resourceList;
        resourceList = new ArrayList<>();
        resourceList.add(new UploadImage());
        imageUploadRecyclerView.setAdapter(new ItemAdapter(MinePayAliOrWxUI.this,resourceList));
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
        headBitmap = BitmapFactory.decodeFile(UriUtil.getRealFilePath(MinePayAliOrWxUI.this, uri), getBitmapOption(1)); //将图片的长和宽缩小味原来的1/2
        headBitmap = ImageUtil.Bytes2Bimap(ImageUtil.zoom(headBitmap, 200));

        ItemAdapter itemAdapter = (ItemAdapter) imageUploadRecyclerView.getAdapter();
        List<UploadImage> list = itemAdapter.getDatalist();
        UploadImage model = new UploadImage();
        model.setUri(UriUtil.getRealFilePath(MinePayAliOrWxUI.this, uri));
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
    
}
