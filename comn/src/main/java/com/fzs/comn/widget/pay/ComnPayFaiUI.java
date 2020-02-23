package com.fzs.comn.widget.pay;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fzs.comn.R;
import com.hzh.frame.ui.activity.BaseUI;

/**
 * 支付 | 支付失败
 * */
@Route(path = "/comn/ComnPayFaiUI")
public class ComnPayFaiUI extends BaseUI {

	@Override
	protected void onCreateBase() {
		setContentView(R.layout.comn_ui_pay_fail);
		getTitleView().setContent("交易结果");
	}

}
