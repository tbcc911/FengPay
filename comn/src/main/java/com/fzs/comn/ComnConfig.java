package com.fzs.comn;

import com.hzh.frame.BaseInitData;

public class ComnConfig extends BaseInitData {

    public static final String LANG_CN = "zh";
    public static final String LANG_EN = "en";
    public static final String LANG_RU = "ru";

    //公司 | 官方网站
    public static final String www="http://www.baidu.com";
    public static final String phone="66666666";

    //初始化
    public static void init() {
        http_client_url = "http://interface.app.fengyijinfu.com/api/";
        http_client_key = "a$u2Dv3%NU0d4o!goS2maqXo$6bcq^qI";
        http_client_version = 100;
        ImageFrameBgImage = new int[]{R.drawable.base_image_default, R.drawable.base_image_default, R.drawable.base_image_default};
        ImageFrameCacheDir = "Mall";
        SharedPreferencesFileName = "fzs_share_data";
        WX_KEY = "XXXXXXXXXXX";

        base_theme_Tone = true;
        //是否强制登录
        force_login= false;
    }
}
  
