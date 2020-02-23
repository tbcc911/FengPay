package com.fzs.login.interceptor;

import android.content.Context;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.alibaba.android.arouter.launcher.ARouter;
import com.hzh.frame.core.BaseSP;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录拦截器
 *
 * @author hzh
 * @version 1.0
 * @date 2018/4/9
 */
@Interceptor(priority = 1, name = "LoginIntercept")
public class LoginInterceptor implements IInterceptor {

    public Map<String, String> interceptorMap;

    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        if (null == (interceptorMap.get(postcard.getPath()))) {
            //不需要登录拦截的路由 
            callback.onContinue(postcard);
        } else {
            if (BaseSP.getInstance().getBoolean("login", false)) {
                //已登录
                callback.onContinue(postcard);
            } else {
                //未登录
                callback.onInterrupt(null);
                Bundle bundle = postcard.getExtras();
                bundle.putString("targetPath", postcard.getPath());
                ARouter.getInstance().build("/login/LoginUI").with(bundle).greenChannel().navigation();
            }
        }
    }

    @Override
    public void init(Context context) {
        //添加需要拦截的路由页面
        interceptorMap = new HashMap<String, String>();
        interceptorMap.put("/user/UserRedAwardRecordUI", "/user/UserRedAwardRecordUI");
        interceptorMap.put("/user/UserAlipayUI", "/user/UserAlipayUI");
        interceptorMap.put("/user/UserShopOrderUI", "/user/UserShopOrderUI");
        interceptorMap.put("/user/UserProfitUI", "/user/UserProfitUI");
        interceptorMap.put("/user/UserYqUI", "/user/UserYqUI");
        interceptorMap.put("/user/UserNewtaskLUI", "/user/UserNewtaskLUI");
        interceptorMap.put("/user/UserStealRedMainUI", "/user/UserStealRedMainUI");
        interceptorMap.put("/user/UserDownloadUrlUI", "/user/UserDownloadUrlUI");
        interceptorMap.put("/user/UserRedAwardUI", "/user/UserRedAwardUI");
        interceptorMap.put("/user/VipIndexLUI", "/user/VipIndexLUI");

        interceptorMap.put("/store/StoreGoodsInfoAttrSelectUI", "/store/StoreGoodsInfoAttrSelectUI");
        interceptorMap.put("/store/StoreExchangeOrderInfoUI", "/store/StoreExchangeOrderInfoUI");
        interceptorMap.put("/store/StoreSaleUI", "/store/StoreSaleUI");
        interceptorMap.put("/store/StoreSaleFailUI", "/store/StoreSaleFailUI");
        interceptorMap.put("/store/StoreSaleSuccessUI", "/store/StoreSaleSuccessUI");
        interceptorMap.put("/store/SinginRUI", "/store/SinginRUI");
        interceptorMap.put("/store/StoreAddressManageLUI", "/store/StoreAddressManageLUI");

        interceptorMap.put("/mine/MineBalanceLUI", "/mine/MineBalanceLUI");
        interceptorMap.put("/mine/MineAchievementUI", "/mine/MineAchievementUI");
        interceptorMap.put("/mine/MineExchangeUI", "/mine/MineExchangeUI");
        interceptorMap.put("/mine/MineMainUI", "/mine/MineMainUI");
        interceptorMap.put("/mine/MineMessageRUI", "/mine/MineMessageRUI");
        interceptorMap.put("/mine/MineTeamRUI", "/mine/MineTeamRUI");
        interceptorMap.put("/mine/MineFeedBackUI", "/mine/MineFeedBackUI");
        interceptorMap.put("/mine/MineCollectionRUI", "/mine/MineCollectionRUI");
        interceptorMap.put("/mine/MineOrderRUI", "/mine/MineOrderRUI");

        interceptorMap.put("/ad/ClientIndexUI", "/ad/ClientIndexUI");
        interceptorMap.put("/ad/ClientRealnameUI", "/ad/ClientRealnameUI");
        interceptorMap.put("/ad/ClientAdProve2UI", "/ad/ClientAdProve2UI");
        interceptorMap.put("/ad/ClientFloorBillsUI", "/ad/ClientFloorBillsUI");

        interceptorMap.put("/sk/SKBuyLUI", "/sk/SKBuyLUI");
        interceptorMap.put("/sk/SKBuyUI", "/sk/SKBuyUI");

        interceptorMap.put("/singin/SigninShareUI", "/singin/SigninShareUI");

        interceptorMap.put("/im/ImSessionUI", "/im/ImSessionUI");

        interceptorMap.put("/chain/ChainTurnPriceUI", "/chain/ChainTurnPriceUI");
        interceptorMap.put("/chain/ChainCollectMoneyCodeUI", "/chain/ChainCollectMoneyCodeUI");
        interceptorMap.put("/chain/ChainPriceDetailsUI", "/chain/ChainPriceDetailsUI");
        interceptorMap.put("/chain/ChainTurnMoneyUI", "/chain/ChainTurnMoneyUI");
        interceptorMap.put("/chain/ChainIdUI", "/chain/ChainIdUI");
        interceptorMap.put("/mine/MineEquityRUI", "/mine/MineEquityRUI");
        interceptorMap.put("/chain/ChainInvitationUI", "/chain/ChainInvitationUI");
        interceptorMap.put("/login/ModifyPayPasswordUI", "/login/ModifyPayPasswordUI");

        interceptorMap.put("/shake/UserRedAwardUI", "/shake/UserRedAwardUI");
        
        interceptorMap.put("/c2c/C2CnotesUI", "/c2c/C2CnotesUI");
        interceptorMap.put("/c2c/C2CtransactionUI", "/c2c/C2CtransactionUI");
        interceptorMap.put("/c2c/C2CsearchUI", "/c2c/C2CsearchUI");
    }
}
