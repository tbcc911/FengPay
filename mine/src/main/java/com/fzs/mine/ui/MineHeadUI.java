package com.fzs.mine.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fzs.comn.ComnConfig;
import com.fzs.comn.matisse.GifSizeFilter;
import com.fzs.comn.tools.QiniuTools;
import com.fzs.comn.tools.UriUtil;
import com.fzs.comn.tools.UserTools;
import com.fzs.comn.widget.imageview.ExpandImageView;
import com.fzs.mine.R;
import com.hzh.frame.comn.callback.HttpCallBack;
import com.hzh.frame.core.HttpFrame.BaseHttp;
import com.hzh.frame.ui.activity.BaseUI;
import com.hzh.frame.util.AndroidUtil;
import com.hzh.frame.util.PowerUtil;
import com.hzh.frame.widget.xdialog.XDialogSubmit;
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
import java.util.List;

/**
 * 设置头像
 * */
@Route(path = "/mine/MineHeadUI")
public class MineHeadUI extends BaseUI {
    private static final int REQUEST_IMAGE = 1;// 图片选择
    private static final int REQUEST_CROP = 2;// 图片剪裁
	private static final String IMAGE_FILE_NAME = "/UserHead.jpg";
	private int width=610 ,height=610;
	private ExpandImageView head;//广告图片
	private Bitmap adImageBitmap;//广告图片
	private XDialogSubmit submitDialog;
	private Uri headImageUri_old=Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/"+ ComnConfig.ImageFrameCacheDir+IMAGE_FILE_NAME);
	private Uri headImageUri_new;
	private Uri headUri;
    private Bitmap headBitmap;
    private String headUrl = "";
    private XDialogSubmit loading;
	
	@Override
	protected void onCreateBase() {
		submitDialog=new XDialogSubmit(this);
		setContentView(R.layout.mine_ui_core_head);
		getTitleView().setContent("头像");
		head=(ExpandImageView)findViewById(R.id.head);
		head.setImageURI(UserTools.getInstance().getUser().getHead());
		loading = new XDialogSubmit(MineHeadUI.this);
	}
    
	/**
	 * 相机修改头像
	 * */
    public void openCamera1(View view){
    	File file=new File(Environment.getExternalStorageDirectory(), "/"+ComnConfig.ImageFrameCacheDir+IMAGE_FILE_NAME);
		if (!file.getParentFile().exists())file.getParentFile().mkdirs();
        //com.look.screen.fileProvider:路径必须跟清单文件中声明的android:authorities一致
		headImageUri_new = FileProvider.getUriForFile(this, "com.look.screen.fileProvider", file);//通过FileProvider创建一个content类型的Uri
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
		intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
		intent.putExtra(MediaStore.EXTRA_OUTPUT, headImageUri_new);//将拍取的照片保存到指定URI
		startActivityForResult(intent,1);
    }

    /**
     * 相机
     */
    public void openCamera(View view) {
        if(PowerUtil.selectApply(MineHeadUI.this, Manifest.permission.CAMERA)) {
            Matisse.from(this).choose(MimeType.ofImage()).theme(R.style.Matisse_Dracula)
                    //                .countable(true)//有序选择图片 123456...
                    .maxSelectable(1)//最大选择数量为1
                    .countable(false).capture(true)//拍照
                    .captureStrategy(new CaptureStrategy(true, AndroidUtil.getPackageName() + ".fileProvider"))//拍照
                    .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K)).maxSelectable(9).originalEnable(true).maxOriginalSize(10).imageEngine(new PicassoEngine()).forResult(REQUEST_IMAGE);
        }else {
            String[] permissions=new String[]{Manifest.permission.CAMERA};
            PowerUtil.apply(MineHeadUI.this,100,permissions);
        }
    }

    /**
     * 相册
     */
    public void openPhoto(View view) {
        Matisse.from(this).choose(MimeType.ofImage()).theme(R.style.Matisse_Dracula).maxSelectable(1)//最大选择数量为1
                .countable(false)
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .maxSelectable(9).originalEnable(true).maxOriginalSize(10)
                .imageEngine(new PicassoEngine())
                .forResult(REQUEST_IMAGE);
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
        options.setCircleDimmedLayer(true);//设置是否展示圆形剪裁框
        options.setShowCropFrame(false);//设置是否展示矩形剪裁框
        options.setShowCropGrid(false);//设置是否展示网格剪裁框
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);//设置裁剪图片可操作的手势 缩放、旋转、剪裁,none就是不设置
        //        options.setToolbarColor(ActivityCompat.getColor(activity, R.color.dracula_primary));//设置toolbar颜色
        options.setStatusBarColor(ActivityCompat.getColor(activity, R.color.application_color));//设置状态栏颜色
        //        options.setHideBottomControls(true);//是否隐藏底部容器，默认显示
        options.setFreeStyleCropEnabled(false);//是否能调整裁剪图片的宽高比
        uCrop.withOptions(options);
        uCrop.withAspectRatio(1, 1);//设置裁剪图片的宽高比，比如16：9（设置后就不能选择其他比例了、选择面板就不会出现了）
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
                            startUCrop(this, imageUris.get(0));
                        }
                    }
                    break;
                case REQUEST_CROP:// 裁剪图片
                    selectImg(UCrop.getOutput(data));
                    break;
            }
        }
    }

    public void selectImg(Uri uri) {
        //图片压缩
        headBitmap = BitmapFactory.decodeFile(UriUtil.getRealFilePath(this, uri), getBitmapOption(1)); //将图片的长和宽缩小味原来的1/2
        headBitmap = com.hzh.frame.util.ImageUtil.Bytes2Bimap(com.hzh.frame.util.ImageUtil.zoom(headBitmap, 200));
        headUri = uri;
        //图片显示
//        BaseHttp.getInstance().query("qiniu", null, new HttpCallBack() {
//            @Override
//            public void onSuccess(JSONObject response) {
//                super.onSuccess(response);
//            }
//        });
        loading.show();
        QiniuTools.getInstance().loadToken("qiniu", new QiniuTools.QiniuCallBack() {
            @Override
            public void onSuccess(String sevenToken, String sevenCdnurl) {
                QiniuTools.getInstance().loadImage(MineHeadUI.this, headUri, 1, new QiniuTools.UpLodImageCallBack() {
                    @Override
                    public void onSuccess(String keys, int type) {
                        headUrl = keys;
                        setSubmit(keys);
                    }

                    @Override
                    public void onFail(String msg) {
                        alert(msg);
                    }
                });
            }

            @Override
            public void onFail(String msg) {
                alert(msg);
            }
        });
    }

    private BitmapFactory.Options getBitmapOption(int inSampleSize) {
        System.gc();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return options;
    }
    
    private void setSubmit(String headUrl){
        JSONObject params = new JSONObject();
        try {
            params.put("avatarUrl", headUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        BaseHttp.getInstance().query("member/updateAvatarUrl", params,new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);
                if (response.optInt("code") == 200){
                    loading.dismiss();
                    head.setImageURI(headUrl);
                    UserTools.getInstance().updUser(UserTools.getInstance().getUser().setHead(headUrl));
                }
                alert(response.optString("message"));
            }
        });
    }
}
