package cn.bertsir.zbar;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.hzh.frame.ui.activity.BaseUI;
import com.hzh.frame.widget.toast.BaseToast;

import cn.bertsir.zbar.Qr.Symbol;
import cn.bertsir.zbar.utils.GetPathFromUri;
import cn.bertsir.zbar.utils.QRUtils;
import cn.bertsir.zbar.view.ScanView;

@Route(path = "/scan/QRUI")
public class QRUI extends BaseUI implements View.OnClickListener {

    private CameraPreview cp;
    private SoundPool soundPool;
    private ScanView sv;
    private ImageView iv_flash;
    private ImageView iv_album;
    private TextView textDialog;
    private TextView tv_des;
    private QrConfig options;
    static final int REQUEST_IMAGE_GET = 1;

    @Override
    protected void onCreateBase() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        options = (QrConfig) getIntent().getExtras().get(QrConfig.EXTRA_THIS_CONFIG);

        Symbol.scanType = options.getScan_type();
        Symbol.scanFormat = options.getCustombarcodeformat();
        Symbol.is_only_scan_center = options.isOnly_center();

        setContentView(R.layout.activity_qr);

        getTitleView().setContent("扫一扫");
        initView();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (cp != null) {
            cp.setScanCallback(resultCallback);
            cp.start();
        }
        sv.onResume();
    }

    private void initView() {
        cp = findViewById(R.id.cp);
        soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
        soundPool.load(this, options.getDing_path(), 1);

        sv = findViewById(R.id.sv);
        sv.setType(options.getScan_view_type());
        sv.startScan();


        iv_flash = findViewById(R.id.iv_flash);
        iv_flash.setOnClickListener(this);

        iv_album = findViewById(R.id.iv_album);
        iv_album.setOnClickListener(this);

        tv_des = findViewById(R.id.tv_des);

        tv_des.setVisibility(options.isShow_des() ? View.VISIBLE :View.GONE);

        tv_des.setText(options.getDes_text());

        sv.setCornerColor(options.getCORNER_COLOR());
        sv.setLineSpeed(options.getLine_speed());
        sv.setLineColor(options.getLINE_COLOR());

    }

    private ScanCallback resultCallback = new ScanCallback() {
        @Override
        public void onScanResult(String result) {
            if(options.isPlay_sound()){
                soundPool.play(1, 1, 1, 0, 0, 1);
            }
            if (cp != null) {
                cp.setFlash(false);
            }
            QrManager.getInstance().getResultCallback().onScanSuccess(result);
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cp != null) {
            cp.setFlash(false);
            cp.stop();
        }
        soundPool.release();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (cp != null) {
            cp.stop();
        }
        sv.onPause();
    }


    /**
     * 从相册选择
     */
    private void fromAlbum() {
        if (QRUtils.getInstance().isMIUI()) {//是否是小米设备,是的话用到弹窗选取入口的方法去选取视频
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(Intent.createChooser(intent, getString(R.string.scan_text_6)), REQUEST_IMAGE_GET);
        } else {//直接跳到系统相册去选取视频
            Intent intent = new Intent();
            if (Build.VERSION.SDK_INT < 19) {
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
            } else {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
            }
            startActivityForResult(Intent.createChooser(intent, getString(R.string.scan_text_7)), REQUEST_IMAGE_GET);
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_album) {
            fromAlbum();
        } else if (v.getId() == R.id.iv_flash) {
            if (cp != null) {
                cp.setFlash();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {//从相册选取视频
            final String imagePath = GetPathFromUri.getPath(this, data.getData());
            textDialog = showProgressDialog();
            textDialog.setText(getString(R.string.scan_text_8));
            new Thread(() -> {
                try {
                    if(TextUtils.isEmpty(imagePath)){
                        Toast.makeText(getApplicationContext(), getString(R.string.scan_text_9), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //优先使用zbar识别
                    final String qrcontent = QRUtils.getInstance().decodeQRcode(imagePath);
                    runOnUiThread(() -> {
                        if(!TextUtils.isEmpty(qrcontent)){
                            closeProgressDialog();
                            QrManager.getInstance().getResultCallback().onScanSuccess(qrcontent);
                            finish();
                        }else {
                            //尝试用zxing再试一次
                            final String qrcontent1 = QRUtils.getInstance().decodeQRcodeByZxing(imagePath);
                            if(!TextUtils.isEmpty(qrcontent1)){
                                closeProgressDialog();
                                QrManager.getInstance().getResultCallback().onScanSuccess(qrcontent1);
                                finish();
                            }else {
                                Toast.makeText(getApplicationContext(), getString(R.string.scan_text_11), Toast.LENGTH_SHORT).show();
                                closeProgressDialog();
                            }

                        }
                    });
                } catch (Exception e) {
                    BaseToast.getInstance().setMsg(getString(R.string.scan_text_12)).show();
                    closeProgressDialog();
                }
            }).start();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    private AlertDialog progressDialog;

    public TextView showProgressDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        builder.setCancelable(false);
        View view = View.inflate(this, R.layout.dialog_loading, null);
        builder.setView(view);
        ProgressBar pb_loading = view.findViewById(R.id.pb_loading);
        TextView tv_hint = view.findViewById(R.id.tv_hint);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pb_loading.setIndeterminateTintList(ContextCompat.getColorStateList(this, R.color.dialog_pro_color));
        }
        progressDialog = builder.create();
        progressDialog.show();

        return tv_hint;
    }

    public void closeProgressDialog() {
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
