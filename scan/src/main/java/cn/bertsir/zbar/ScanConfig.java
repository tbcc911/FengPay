package cn.bertsir.zbar;

import android.app.Activity;
import android.support.v4.content.ContextCompat;

import com.fzs.comn.ComnConfig;
import com.hzh.frame.BaseInitData;

public class ScanConfig {

    private static ScanConfig _instance;
    private static QrManager mQrManager;

    public static ScanConfig getInstance(){
        if (null==_instance){
            _instance=new ScanConfig();
            _instance.init();
        }
        return _instance;
    }

    private void init(){
        QrConfig qrConfig = new QrConfig.Builder()
                .setDesText(ComnConfig.applicationContext.getString(R.string.scan_text_13))//扫描框下文字
                .setShowDes(true)//是否显示扫描框下面文字
                .setCornerColor(ContextCompat.getColor(BaseInitData.applicationContext,R.color.white))//设置扫描框颜色
                .setLineColor(ContextCompat.getColor(BaseInitData.applicationContext,R.color.white))//设置扫描线颜色
                .setLineSpeed(QrConfig.LINE_MEDIUM)//设置扫描线速度
                .setScanType(QrConfig.TYPE_QRCODE)//设置扫码类型（二维码，条形码，全部，自定义，默认为二维码）
                .setScanViewType(QrConfig.SCANVIEW_TYPE_QRCODE)//设置扫描框类型（二维码还是条形码，默认为二维码）
                .setCustombarcodeformat(QrConfig.BARCODE_EAN13)//此项只有在扫码类型为TYPE_CUSTOM时才有效
                .setPlaySound(true)//是否扫描成功后bi~的声音
                //                    .setDingPath( R.raw.qrcode)//设置提示音(不设置为默认的Ding~)
                .setIsOnlyCenter(true)//是否只识别框中内容(默认为全屏识别)
                .create();
        mQrManager=QrManager.getInstance().init(qrConfig);
    }
    
    public void start(Activity activity,QrManager.OnScanResultCallback callback){
        mQrManager.startScan(activity, callback);
    }

}
