package com.fzs.comn;

import android.content.Context;

import com.hzh.frame.BaseApplication;
import com.hzh.frame.core.BaseSP;
import com.hzh.frame.tools.LanguageTools;
import com.hzh.frame.util.Util;

public class ComnApplication extends BaseApplication{

    @Override
    public void onCreate() {
        ComnConfig.init();
        super.onCreate();
        initLanguage();
    }

    public void initLanguage(){
        String language= BaseSP.getInstance().getString("language");
        if(Util.isEmpty(language)){
            BaseSP.getInstance().put("language", ComnConfig.LANG_EN);
            //            language=ComnConfig.LANG_EN;
            language=ComnConfig.LANG_CN;
        }
        LanguageTools.setAppLanguage(this,language);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
