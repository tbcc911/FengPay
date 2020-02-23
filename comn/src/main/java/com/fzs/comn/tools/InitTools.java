package com.fzs.comn.tools;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.fzs.comn.model.AppConfig;
import com.hzh.frame.comn.callback.HttpCallBack;
import com.hzh.frame.core.HttpFrame.BaseHttp;
import com.hzh.frame.widget.toast.BaseToast;

import org.json.JSONObject;

/**
 * @author
 * @version 1.0
 * @date 2020/1/4
 */
public class InitTools {

    AppConfig mAppConfig;
    private static InitTools _instance;

    public static InitTools getInstance() {
        synchronized (InitTools.class) {
            if (_instance == null) {
                _instance = new InitTools();
                _instance.getConfig();
            }
            return _instance;
        }
    }
    
    /**
     * 获取用户信息
     */
    public AppConfig getConfig() {
        synchronized (AppConfig.class) {
            if (mAppConfig==null) {
                //已登录
                mAppConfig = new Select().from(AppConfig.class).executeSingle();
            }
            return mAppConfig == null ? new AppConfig() : mAppConfig;
        }
    }

    /**
     * 获取配置信息
     */
    public void loadConfig() {
        BaseHttp.getInstance().query("other/getCommonInfo", null, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                if (200 == response.optInt("code")) {
                    JSONObject data = response.optJSONObject("data");
                    if (data != null && data.length()>0){
                        new Delete().from(AppConfig.class).execute();
                        if (mAppConfig == null) {
                            mAppConfig = new AppConfig();
                        }
                        mAppConfig.setCompanyWebSite(Util.isEmpty(data.optString("companyWebSite"))?"0":data.optString("companyWebSite"));
                        mAppConfig.setCustomerTelephone(Util.isEmpty(data.optString("customerTelephone"))?"0":data.optString("customerTelephone"));
                        mAppConfig.setEVEpayExchangeTbccLowerLimit(Util.isEmpty(data.optString("evepayExchangeTbccLowerLimit"))?"0":data.optString("evepayExchangeTbccLowerLimit"));
                        mAppConfig.setEVEpayExchangeTbccMultiple(Util.isEmpty(data.optString("evepayExchangeTbccMultiple"))?"0":data.optString("evepayExchangeTbccMultiple"));
                        mAppConfig.setEVEpayExchangeTbccRate(Util.isEmpty(data.optString("evepayExchangeTbccRate"))?"0":data.optString("evepayExchangeTbccRate"));
                        mAppConfig.setInviteReward(Util.isEmpty(data.optString("inviteReward"))?"0":data.optString("inviteReward"));
                        mAppConfig.setSharePageUrl(Util.isEmpty(data.optString("sharePageUrl"))?"0":data.optString("sharePageUrl"));
                        mAppConfig.setTransferLowerLimit(Util.isEmpty(data.optString("transferLowerLimit"))?"0":data.optString("transferLowerLimit"));
                        mAppConfig.setTransferMultiple(Util.isEmpty(data.optString("transferMultiple"))?"0":data.optString("transferMultiple"));
                        mAppConfig.setUsdtExchangeTbccLowerLimit(Util.isEmpty(data.optString("usdtExchangeTbccLowerLimit"))?"0":data.optString("usdtExchangeTbccLowerLimit"));
                        mAppConfig.setUsdtExchangeTbccMultiple(Util.isEmpty(data.optString("usdtExchangeTbccMultiple"))?"0":data.optString("usdtExchangeTbccMultiple"));
                        mAppConfig.setUsdtExchangeTbccRate(Util.isEmpty(data.optString("usdtExchangeTbccRate"))?"0":data.optString("usdtExchangeTbccRate"));
                        mAppConfig.setWithdrawLowerLimit(Util.isEmpty(data.optString("withdrawLowerLimit"))?"0":data.optString("withdrawLowerLimit"));
                        mAppConfig.setWithdrawMultiple(Util.isEmpty(data.optString("withdrawMultiple"))?"0":data.optString("withdrawMultiple"));
                        mAppConfig.setWithdrawPoundage(Util.isEmpty(data.optString("withdrawPoundage"))?"0":data.optString("withdrawPoundage"));
                        mAppConfig.setDigitalCurrencyTradingPoundage(Util.isEmpty(data.optString("digitalCurrencyTradingPoundage"))?"0":data.optString("digitalCurrencyTradingPoundage"));
                        mAppConfig.setCurrencyTradeMinPrice(Util.isEmpty(data.optString("currencyTradeMinPrice"))?"0":data.optString("currencyTradeMinPrice"));
                        mAppConfig.setCurrencyTradeMaxPrice(Util.isEmpty(data.optString("currencyTradeMaxPrice"))?"0":data.optString("currencyTradeMaxPrice"));
                        mAppConfig.save();
                    }
                } else {
                    BaseToast.getInstance().setMsg("APP初始化失败 : "+response.optString("msg")).show();
                }
            }
        });
    }
}
