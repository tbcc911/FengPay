package com.fzs.mine.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fzs.comn.matisse.GifSizeFilter;
import com.fzs.comn.model.UploadImage;
import com.fzs.comn.tools.ImageUtil;
import com.fzs.comn.tools.QiniuTools;
import com.fzs.comn.tools.UriUtil;
import com.fzs.comn.tools.Util;
import com.fzs.mine.R;
import com.hzh.frame.comn.ItemDecoration.BaseGridSpacingItemDecoration;
import com.hzh.frame.comn.callback.CallBack;
import com.hzh.frame.comn.callback.HttpCallBack;
import com.hzh.frame.comn.model.BaseRadio;
import com.hzh.frame.core.HttpFrame.BaseHttp;
import com.hzh.frame.ui.activity.BaseUI;
import com.hzh.frame.util.AndroidUtil;
import com.hzh.frame.widget.xdialog.XDialogRadio;
import com.hzh.frame.widget.xrecyclerview.BaseRecyclerAdapter;
import com.hzh.frame.widget.xrecyclerview.RecyclerViewHolder;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;
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

@Route(path = "/mine/MineFeedBackUI")
public class MineFeedBackUI extends BaseUI {
    private static final int REQUEST_IMAGE = 1;// 图片选择
    private static final int REQUEST_CROP = 2;// 图片剪裁

    EditText feedback; //反馈意见或建议  电话
    EditText tel;
    TextView number; //输入字符串长度
    Button submit;
    RecyclerView imageUploadRecyclerView;
    LinearLayout type;
    TextView typeContent;
    private List<String> issueImageUrl;
    private String imageUrl = "";
    private int zero = 0;
    List<BaseRadio> list=new ArrayList<>();

    @Override
    protected void onCreateBase() {
        setContentView(R.layout.mine_ui_feedback);
        feedback = (EditText) findViewById(R.id.feedback);
        tel = (EditText) findViewById(R.id.tel);
        number = (TextView) findViewById(R.id.number);
        submit = (Button) findViewById(R.id.submit);
        type = (LinearLayout) findViewById(R.id.type);
        typeContent = (TextView) findViewById(R.id.typeContent); 
        imageUploadRecyclerView = (RecyclerView) findViewById(R.id.image_upload_recyclerView);
        getTitleView().setContent("评价反馈");
        typeContent.setTag("1");
        feedback.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                number.setText(s.length() + "/99");
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
        initImageUploadGridview();
        list.add(new BaseRadio().setName("订单").setId("0").setChecked(true));
        list.add(new BaseRadio().setName("其它").setId("1").setChecked(false));
        type.setOnClickListener(v -> {
            setType();
        });
    }

    private void uploadImage() {
        if (TextUtils.isEmpty(feedback.getText()) || TextUtils.isEmpty(tel.getText())) {
            alert("输入意见反馈信息以及联系方式之后才可以提交反馈");
        } else {
            if (tel.getText().length()<11){
                alert("请输入正确的联系方式");
            }else {
                List<HashMap<String,Object>> fileList=new ArrayList<>();
                ItemAdapter adapter= (ItemAdapter) imageUploadRecyclerView.getAdapter();
                List<UploadImage> list = adapter.getDatalist();
                for(int i=0;i<list.size();i++){
                    UploadImage model=list.get(i);
                    if (!Util.isEmpty(model.getUri())) {
                        HashMap<String,Object> map = new HashMap<>();
                        map.put("file",new File(model.getUri()));
                        fileList.add(map);
                    }
                }
                if (fileList.size()>0){
                    issueImageUrl = new ArrayList<>();
                    imageUrl = "";
                    zero = 0;
                    QiniuTools.getInstance().loadToken("qiniu", new QiniuTools.QiniuCallBack() {
                        @Override
                        public void onSuccess(String sevenToken, String sevenCdnurl) {
                            for (int y = 0;y<fileList.size();y++){
                                setissueImageUrl(Uri.fromFile(new File(fileList.get(y).get("file").toString())),zero,fileList.size());
                            }
                        }

                        @Override
                        public void onFail(String msg) {
                            alert(msg);
                        }
                    });
                }else {
                    setSubmit();
                }
            }
        }
    }

    protected void submit(ArrayList<String> array) {
        JSONObject params = new JSONObject();
        try {
            params.put("content", feedback.getText().toString());
            params.put("contactWay", tel.getText().toString());
            if(array!=null){
                params.put("issueImageUrl",array);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        BaseHttp.getInstance().write("other/issueFeedback", params, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                if (response.optInt("code") == 200) {
                    alert(response.optString("message"));
                    feedback.getText().clear();
                    tel.getText().clear();
                    initImageUploadGridview();
                }
            }
        });
    }

    protected void submitNoPic() {
        JSONObject params = new JSONObject();
        try {
            params.put("desc", feedback.getText().toString());
            params.put("tel", tel.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        BaseHttp.getInstance().write("Me/feedBack", params, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                if (response.optInt("code") == 200) {
                    alert(response.optString("msg"));
                    feedback.getText().clear();
                    tel.getText().clear();
                }
            }
        });
    }

    public void initImageUploadGridview() {
        imageUploadRecyclerView.setLayoutManager(new GridLayoutManager(this,5));
        imageUploadRecyclerView.addItemDecoration(new BaseGridSpacingItemDecoration(this,5,0));
        List<UploadImage> resourceList = new ArrayList<>();
        resourceList.add(new UploadImage());
        imageUploadRecyclerView.setAdapter(new ItemAdapter(this,resourceList));
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

    /**
     * 启动裁剪
     *
     * @param activity  上下文
     * @param sourceUir 需要裁剪图片的Uri
     * @return
     */
    public void startUCrop(Activity activity, Uri sourceUir) {
        //裁剪后保存到文件中
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), System.currentTimeMillis() + ".jpg"));
        //初始化，第一个参数：需要裁剪的图片；第二个参数：裁剪后图片
        UCrop uCrop = UCrop.of(sourceUir, destinationUri);
        UCrop.Options options = new UCrop.Options();//初始化UCrop配置
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);//设置剪裁出来的是JPEG格式的图片
        options.setCircleDimmedLayer(false);//设置是否展示圆形剪裁框
        options.setShowCropFrame(true);//设置是否展示矩形剪裁框
        options.setShowCropGrid(true);//设置是否展示网格剪裁框
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);//设置裁剪图片可操作的手势 缩放、旋转、剪裁,none就是不设置
        //        options.setToolbarColor(ActivityCompat.getColor(activity, R.color.dracula_primary));//设置toolbar颜色
        options.setStatusBarColor(ActivityCompat.getColor(activity, R.color.application_color));//设置状态栏颜色
        //        options.setHideBottomControls(true);//是否隐藏底部容器，默认显示
        options.setFreeStyleCropEnabled(true);//是否能调整裁剪图片的宽高比
        uCrop.withOptions(options);
//        uCrop.withAspectRatio(1, 1);//设置裁剪图片的宽高比，比如16：9（设置后就不能选择其他比例了、选择面板就不会出现了）
        uCrop.start(activity, REQUEST_CROP);
//        StatusBarUtil.setBlackIcon(this);
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
        headBitmap = BitmapFactory.decodeFile(UriUtil.getRealFilePath(this, uri), getBitmapOption(1)); //将图片的长和宽缩小味原来的1/2
        headBitmap = ImageUtil.Bytes2Bimap(ImageUtil.zoom(headBitmap, 200));

        ItemAdapter itemAdapter = (ItemAdapter) imageUploadRecyclerView.getAdapter();
        List<UploadImage> list = itemAdapter.getDatalist();
        UploadImage model = new UploadImage();
        model.setUri(UriUtil.getRealFilePath(this, uri));
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
    
    private void setissueImageUrl(Uri uri,int y,int size){
        QiniuTools.getInstance().loadImage(MineFeedBackUI.this, uri, 1, new QiniuTools.UpLodImageCallBack() {
            @Override
            public void onSuccess(String keys, int type) {
                zero++;
//                issueImageUrl.add(keys);
                if (zero == size){
                    imageUrl = imageUrl + keys;
                    setSubmit();
                }else {
                    imageUrl = imageUrl + keys + ",";
                }
            }

            @Override
            public void onFail(String msg) {
                alert(msg);
            }
        });
    }
    
    private void setSubmit(){
        JSONObject params = new JSONObject();
        try {
            params.put("contactWay", tel.getText().toString().trim());
            params.put("content", feedback.getText().toString().trim());
            params.put("issueImageUrl", imageUrl);
            params.put("issueType", typeContent.getTag().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        BaseHttp.getInstance().write(MineFeedBackUI.this,"other/issueFeedback", params, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);
                if (response.optInt("code") == 200){
                    tel.getText().clear();
                    feedback.getText().clear();
                    initImageUploadGridview();
                    typeContent.setTag("0");
                    typeContent.setText("订单");
                    alert(response.optString("message"));
                }else {
                    alert(response.optString("message"));
                }
            }
        });
    }
    
    private void setType(){
        new XDialogRadio<>()
                .setTitle("请选择问题反馈类型")
                .setData(list)
                .setCallBack(new CallBack<BaseRadio>() {
                    @Override
                    public void onSuccess(BaseRadio item) {
                        typeContent.setTag(item.getId());
                        typeContent.setText(item.getName());
                    }
                }).show(getSupportFragmentManager());
    }
    
}
