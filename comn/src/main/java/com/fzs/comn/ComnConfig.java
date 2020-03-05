package com.fzs.comn;

import com.hzh.frame.BaseInitData;

/**
 * 配置文件
 * @author hzh 2015-06-08 10:39
 */
public class ComnConfig extends BaseInitData {

    public static final String LANG_CN = "zh";
    public static final String LANG_EN = "en";
    public static final String LANG_RU = "ru";

    //公司 | 官方网站
    public static final String www="http://www.baidu.com";
    public static final String phone="66666666";

    //初始化
    public static void init() {
//        http_client_url = "https://interface.app.xdxjzw.com/api/"; //服务器(香港)
        //        http_client_url = "http://47.244.219.14:8092/api/"; //服务器
                http_client_url = "http://47.104.224.184:8092/api/"; //开发服务器
//        http_client_url="http://192.168.199.222:8092/api/";
        http_client_key = "a$u2Dv3%NU0d4o!goS2maqXo$6bcq^qI";
        http_client_version = 100;
        ImageFrameBgImage = new int[]{R.drawable.base_image_default, R.drawable.base_image_default, R.drawable.base_image_default};
        ImageFrameCacheDir = "Mall";
        SharedPreferencesFileName = "fzs_share_data";
        WX_KEY = "wx7d314006a5998a80";

        base_theme_Tone = true;
        //是否强制登录
        force_login= false;
    }

}
  
