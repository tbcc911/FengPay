package com.fzs.comn.widget.pay;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fzs.comn.R;
import com.hzh.frame.ui.activity.BaseUI;

/**
 * 支付 | 支付成功
 * */
@Route(path = "/comn/ComnPaySuccessUI")
public class ComnPaySuccessUI extends BaseUI {

	@Override
	protected void onCreateBase() {
		setContentView(R.layout.comn_ui_pay_success);
		getTitleView().setContent("交易结果");
	}
}
