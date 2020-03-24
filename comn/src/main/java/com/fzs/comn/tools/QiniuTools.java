package com.fzs.comn.tools;

import android.content.Context;
import android.net.Uri;

import com.fzs.comn.ComnConfig;
import com.fzs.comn.model.User;
import com.hzh.frame.comn.callback.HttpCallBack;
import com.hzh.frame.core.HttpFrame.BaseHttp;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.yalantis.ucrop.util.FileUtils;

import org.json.JSONObject;


public class QiniuTools {
    public static final String TAG = "QiniuTools";

    private static QiniuTools _instance;
    private static User mUser;
    private static Context context;
    private static Configuration config;
    private static String sevenToken;
    private static String sevenHost;
    private static String sevenCdnurl;
    private static String sevenZone;

    public static QiniuTools getInstance() {
        synchronized (QiniuTools.class) {
            if (_instance == null) {
                _instance = new QiniuTools(null);
            }
            return _instance;
        }
    }

    public static QiniuTools getInstance(Context context) {
        synchronized (QiniuTools.class) {
            if (_instance == null) {
                _instance = new QiniuTools(context);
            }
            return _instance;
        }
    }
    
    public QiniuTools(Context context){
        if(context!=null){
            this.context=context;
        }else{
            this.context=ComnConfig.applicationContext;
        }
    }
    
    public void loadToken(){
        loadToken(null);
    }

    public void loadToken(QiniuCallBack qiniuCallBack) {
        loadToken(null, qiniuCallBack);
    }
    
    public void loadToken(final String pathfinal ,final QiniuCallBack qiniuCallBack){
        config = new Configuration.Builder()
                .chunkSize(512 * 1024)        // 分片上传时，每片的大小。 默认256K
                .putThreshhold(1024 * 1024)   // 启用分片上传阀值。默认512K
                .connectTimeout(10)           // 链接超时。默认10秒
                .useHttps(true)               // 是否使用https上传域名
                .responseTimeout(60)          // 服务器响应超时。默认60秒
                .build();
        
        BaseHttp.getInstance().query("qiniu", null,new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);
                JSONObject data = response.optJSONObject("data");
                if (data != null && data.length() > 0){
                    sevenToken = data.optString("token");
                    sevenHost = data.optString("host");
                    sevenCdnurl = data.optString("cdnUrl");
                    sevenZone = data.optString("zone");
                    switch (sevenZone){
                        case "zone0":
                            config = new Configuration.Builder()
                                    .zone(FixedZone.zone0) //设置区域，指定不同区域的上传域名、备用域名、备用IP。
                                    .build();
                            break;
                        case "zone1":
                            config = new Configuration.Builder()
                                    .zone(FixedZone.zone1) //设置区域，指定不同区域的上传域名、备用域名、备用IP。
                                    .build();
                            break;
                        case "zone2":
                            config = new Configuration.Builder()
                                    .zone(FixedZone.zone2) //设置区域，指定不同区域的上传域名、备用域名、备用IP。
                                    .build();
                            break;
                        case "zoneAs0":
                            config = new Configuration.Builder()
                                    .zone(FixedZone.zoneAs0) //设置区域，指定不同区域的上传域名、备用域名、备用IP。
                                    .build();
                            break;
                        case "zoneNa0":
                            config = new Configuration.Builder()
                                    .zone(FixedZone.zoneNa0) //设置区域，指定不同区域的上传域名、备用域名、备用IP。
                                    .build();
                            break;
                    }
                    qiniuCallBack.onSuccess(sevenToken,sevenCdnurl);
                }
            }

            @Override
            public void onFail() {
                super.onFail();
                qiniuCallBack.onFail("请求失败");
            }
        });
    }

    public void loadImage(final Context context,final Uri uri, final int type,final UpLodImageCallBack upLodImageCallBack){
        String fileName = System.currentTimeMillis() + "_tdxz.jpg";
        String keys = sevenCdnurl + "/" + fileName;
        // 重用uploadManager。一般地，只需要创建一个uploadManager对象
        UploadManager uploadManager = new UploadManager(config, 3);//配置3个线程数并发上传；不配置默认为3，只针对file.size>4M生效。线程数建议不超过5，上传速度主要取决于上行带宽，带宽很小的情况单线程和多线程没有区别
        uploadManager.put(FileUtils.getPath(context, uri), fileName, sevenToken,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        //res包含hash、key等信息，具体字段取决于上传策略的设置
                        if(info.isOK()) {
                            upLodImageCallBack.onSuccess(keys,type);
                        } else {
                            upLodImageCallBack.onFail("图片上传失败，请重试");
                            //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                        }

                    }
                }, null);
    }
    
    public interface UpLodImageCallBack {
        void onSuccess(String keys,int type);

        void onFail(String msg);
    }

    public interface QiniuCallBack {
        void onSuccess(String sevenToken,String sevenCdnurl);

        void onFail(String msg);
    }
    
}
