package com.fzs.mine.ui;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.fzs.comn.ComnConfig;
import com.fzs.comn.model.User;
import com.fzs.comn.tools.UserTools;
import com.fzs.comn.tools.Util;
import com.fzs.mine.R;
import com.hzh.frame.comn.callback.HttpCallBack;
import com.hzh.frame.core.HttpFrame.BaseHttp;
import com.hzh.frame.ui.activity.BaseUI;
import com.hzh.frame.widget.xdialog.XDialogSubmit;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 设置银行卡信息
 */
public class MinePayBankInfoUI extends BaseUI {

    /**
     * true:添加  false:显示
     */
    private boolean flag = true;
    EditText bankName;
    EditText bankAddress;
    EditText bankMasterName;
    EditText bankAccount;
    TextView phone;
    private XDialogSubmit submitDialog;

    public void findViewById(){
        bankName=findViewById(R.id.bankName);
        bankAddress=findViewById(R.id.bankAddress);
        bankMasterName=findViewById(R.id.bankMasterName);
        bankAccount=findViewById(R.id.bankAccount);
        phone=findViewById(R.id.phone);
    }
    
    @Override
    protected void onCreateBase() {
        submitDialog = new XDialogSubmit(this);
        if (Util.isEmpty(UserTools.getInstance().getUser().getBankName())) {
            //添加
            flag = true;
        } else {
            //显示
            flag = false;
        }
        setContentView(R.layout.mine_ui_pay_bank_info);
        phone.setText(ComnConfig.phone);
        getTitleView().setContent("银行卡信息");
        if (flag) {
            getTitleView().setRightContent("保存");
            getTitleView().setRightOnclickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    submit();
                }
            });
        } else {
            getTitleView().setRightContent("");
        }
        if (flag) {

        } else {
            bankName.setText(UserTools.getInstance().getUser().getAlipayName());
            bankAddress.setText(UserTools.getInstance().getUser().getBankAddress());
            bankAccount.setText(UserTools.getInstance().getUser().getAlipayAcount());
            bankMasterName.setText(UserTools.getInstance().getUser().getBankMasterName());
            bankName.setFocusable(false);
            bankName.setEnabled(false);
            bankAddress.setFocusable(false);
            bankAddress.setEnabled(false);
            bankAccount.setFocusable(false);
            bankAccount.setEnabled(false);
            bankMasterName.setFocusable(false);
            bankMasterName.setEnabled(false);

        }
    }

    /**
     * 提交信息
     */
    public void submit() {
        final String name = bankName.getText().toString().trim();
        final String address = bankAddress.getText().toString().trim();
        final String masterName = bankMasterName.getText().toString().trim();
        final String account = bankAccount.getText().toString().trim();
        if (name.length() <= 0) {
            alert("开户行名称不能为空");
            return;
        }
        if (address.length() <= 0) {
            alert("开户行地址不能为空");
            return;
        }
        if (masterName.length() <= 0) {
            alert("持卡人姓名不能为空");
            return;
        }
        if (account.length() <= 0) {
            alert("银行卡卡号不能为空");
            return;
        }
        JSONObject params = new JSONObject();
        try {
            params.put("opening_bank", name);
            params.put("opening_bank_address", address);
            params.put("back_card_name", masterName);
            params.put("back_card", account);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        submitDialog.show();
        BaseHttp.getInstance().write(this, "4014", params, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                submitDialog.dismiss();
                if ("1".equals(response.optString("result"))) {
                    JSONObject data = response.optJSONObject("data");
                    if ("1".equals(data.optString("code"))) {
                        User user=UserTools.getInstance().getUser();
                        user.setBankName(name);
                        user.setBankAccount(account);
                        user.setBankAddress(address);
                        user.setBankMasterName(masterName);
                        UserTools.getInstance().updUser(user);
                        alert(data.optString("msg"));
                    } else {
                        alert(data.optString("msg"));
                    }
                } else {
                    alert("添加失败");
                }
            }

            @Override
            public void onFail() {
                submitDialog.dismiss();
                alert("添加失败");
            }
        });
    }

    /**
     * 拨打电话
     */
    public void goPhone(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone.getText().toString().trim()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
