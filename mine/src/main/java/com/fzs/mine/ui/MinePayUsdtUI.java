package com.fzs.mine.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
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
import com.hzh.frame.util.Code2Util;
import com.hzh.frame.widget.toast.BaseToast;
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

/**
 *支付宝或者微信充值
 * */
@Route(path = "/mine/MinePayUsdtUI")
public class MinePayUsdtUI extends BaseUI {
    ImageView usdtQr;
    TextView usdtAddress;
    RecyclerView imageUploadRecyclerView;
    Button copyAddress;
    Button saveImg;
    Button submitButton;
    EditText fMoney;
    private String rechargeType = "";
    private static final int REQUEST_IMAGE = 1;// 图片选择
    private static final int REQUEST_CROP = 2;// 图片剪裁
    XDialog1Button mXDialog1Button;
    private String imageUrl = "";
    private int zero = 0;
    private List<String> issueImageUrl;
    private String collectionId = "";
    
	@Override
	protected void onCreateBase() {
		setContentView(R.layout.mine_ui_pay_usdt);
		getTitleView().setContent("USDT充值");
        showLoding();
        rechargeType = getIntent().getStringExtra("rechargeType");
        usdtQr = findViewById(R.id.usdtQr);
        usdtAddress = findViewById(R.id.usdtAddress);
        imageUploadRecyclerView = findViewById(R.id.image_upload_recyclerView);
        copyAddress = findViewById(R.id.copyAddress);
        saveImg = findViewById(R.id.saveImg);
        submitButton = findViewById(R.id.submitButton);
        fMoney = findViewById(R.id.fMoney);
		getCollectionInfo();
        initImageUploadGridview();
        saveImg.setOnClickListener(v -> {
            Bitmap bitmap= ImageTools.getImageViewBitmap(usdtQr);
            ImageTools.saveBitmap2Camera(this,bitmap,"Usdtpay");
            mXDialog1Button=new XDialog1Button(this, "二维码已保存到相册", new CallBack() {
                @Override
                public void onSuccess(Object o) {

                }
            });
            mXDialog1Button.setButtonName("确定").show();
        });
        submitButton.setOnClickListener(v -> {
            submitPay();
        });
        usdtAddress.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!Util.isEmpty(usdtAddress.getText().toString().trim())){
                    ClipboardManager cmb = (ClipboardManager) MinePayUsdtUI.this.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData myClip = ClipData.newPlainText("UsdtAddress",usdtAddress.getText().toString().trim());
                    cmb.setPrimaryClip(myClip);
                    BaseToast.getInstance().setMsg("成功复制充值地址:" + usdtAddress.getText().toString().trim()).show();
                }else {
                    alert("暂无充值地址");
                }
                return false;
            }
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
                        collectionId = data.optString("collectionId");
                        usdtAddress.setText(data.optString("usdtAddress"));
                        usdtQr.setImageBitmap(Code2Util.create(data.optString("usdtAddress"),(int)(MinePayUsdtUI.this.getResources().getDimension(R.dimen.dp_180)),
                                (int)(getResources().getDimension(R.dimen.dp_80))));
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

    public void initImageUploadGridview() {
        imageUploadRecyclerView.setLayoutManager(new GridLayoutManager(MinePayUsdtUI.this,5));
        imageUploadRecyclerView.addItemDecoration(new BaseGridSpacingItemDecoration(MinePayUsdtUI.this,5,3));
        List<UploadImage> resourceList;
        resourceList = new ArrayList<>();
        resourceList.add(new UploadImage());
        imageUploadRecyclerView.setAdapter(new ItemAdapter(MinePayUsdtUI.this,resourceList));
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
        headBitmap = BitmapFactory.decodeFile(UriUtil.getRealFilePath(MinePayUsdtUI.this, uri), getBitmapOption(1)); //将图片的长和宽缩小味原来的1/2
        headBitmap = ImageUtil.Bytes2Bimap(ImageUtil.zoom(headBitmap, 200));

        ItemAdapter itemAdapter = (ItemAdapter) imageUploadRecyclerView.getAdapter();
        List<UploadImage> list = itemAdapter.getDatalist();
        UploadImage model = new UploadImage();
        model.setUri(UriUtil.getRealFilePath(MinePayUsdtUI.this, uri));
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

    private void submitPay(){
        String money = fMoney.getText().toString().trim();
        ItemAdapter itemAdapter = (ItemAdapter) imageUploadRecyclerView.getAdapter();
        List<UploadImage> list = itemAdapter.getDatalist();
        if (Util.isEmpty(money)){
            alert("请输入充值金额");
            return;
        }
        
        if (list != null && list.size() > 1){

        }else {
            alert("请上传截图");
            return;
        }

        getFileList(new fileCallBack() {
            @Override
            public void onSuccess(String imgUrl) {
                JSONObject params=new JSONObject();
                try   {
                    params.put("collectionId", collectionId);
                    params.put("paymentScreenshot", imgUrl);
                    params.put("value", money);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                BaseHttp.getInstance().write("finance/recharge", params, new HttpCallBack() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        super.onSuccess(response);
                        if (response.optInt("code") == 200){
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

    private void getFileList(fileCallBack fileCallBack){
        List<HashMap<String,Object>> fileList;
        ItemAdapter adapter= (ItemAdapter) imageUploadRecyclerView.getAdapter();
        List<UploadImage> list = adapter.getDatalist();
        issueImageUrl = new ArrayList<>();
        imageUrl = "";
        zero = 0;
        if (list != null && list.size() > 1){
            for(int i=0;i <= list.size() - 2;i++){
                fileList=new ArrayList<>();
                UploadImage model=list.get(i);
                if (!Util.isEmpty(model.getUri())) {
                    HashMap<String,Object> map = new HashMap<>();
                    map.put("name","file");
                    map.put("file",new File(model.getUri()));
                    fileList.add(map);
                }
                int finalI = i;
                BaseHttp.getInstance().uploadFile(MinePayUsdtUI.this,"file/upload", fileList, new HttpCallBack() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        super.onSuccess(response);
                        if (response.optInt("code") == 200){
                            if (Util.isEmpty(imageUrl)){
                                imageUrl = response.optString("data");
                            }else {
                                imageUrl = imageUrl + "," + response.optString("data");
                            }
                        }
                        if (finalI + 2 == list.size()){
                            fileCallBack.onSuccess(imageUrl);
                        }
                    }
                });
            }
        }else {
            fileCallBack.onSuccess("");
        }
    }
    
}
