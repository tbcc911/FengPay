package com.fzs.mine.ui;

import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fzs.comn.tools.Util;
import com.fzs.comn.widget.dialog.ComnPasswordDialog;
import com.fzs.comn.widget.imageview.ExpandImageView;
import com.fzs.mine.R;
import com.hzh.frame.comn.callback.CallBack;
import com.hzh.frame.comn.callback.HttpCallBack;
import com.hzh.frame.comn.model.BaseRadio;
import com.hzh.frame.core.HttpFrame.BaseHttp;
import com.hzh.frame.ui.activity.BaseUI;
import com.hzh.frame.util.AndroidUtil;
import com.hzh.frame.widget.xdialog.XDialogRadio;
import com.jakewharton.rxbinding3.view.RxView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 转账
 * @date 2019/9/4
 */
@Route(path = "/mine/MineTurnMoneyUI")
public class MineTurnMoneyUI extends BaseUI {
    private TextView typeContent;
    private TextView nickName;
    private TextView personUid;
    private EditText money;
    private Button commit;
    private ExpandImageView head;
    private LinearLayout type;
    JSONObject json;

    @Override
    protected void onCreateBase() {
        setContentView(R.layout.mine_ui_turn_money);
        getTitleView().setContent("转账");
        typeContent = findViewById(R.id.typeContent);
        nickName = findViewById(R.id.nickname);
        personUid = findViewById(R.id.personUid);
        money = findViewById(R.id.money);
        commit = findViewById(R.id.commit);
        head = findViewById(R.id.head);
        type = findViewById(R.id.type);
        try {
            init();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RxView.clicks(commit).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(unit -> new ComnPasswordDialog(MineTurnMoneyUI.this).setCallBack(password -> commit(password)).show());

        type.setOnClickListener(v -> {
            List<BaseRadio> list = new ArrayList<>();
            list.add(new BaseRadio().setName("USDT").setId("0").setChecked(true));
            list.add(new BaseRadio().setName("TBCC").setId("1").setChecked(false));
            new XDialogRadio<>()
                    .setData(list)
                    .setTitle("选择转账方式")
                    .setRadioButtonMinWidth(AndroidUtil.getWindowWith() / 10.0 * 8)
                    .setCallBack(new CallBack<BaseRadio>() {
                        @Override
                        public void onSuccess(BaseRadio baseRadio) {
                            typeContent.setTag(baseRadio.getId());
                            typeContent.setText(baseRadio.getName());
                        }
                    })
                    .show(getSupportFragmentManager());
        });
    }

    public void init() throws JSONException {
        typeContent.setTag("1");
        String params = getIntent().getStringExtra("params");
        try {
            json=new JSONObject(URLDecoder.decode(params,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        head.setImageURI(json.optString("head"));
        if (json != null && json.length() > 0){
            nickName.setText(json.optString("nickName"));
            personUid.setText(hideId(json.optString("acount")));
        }
    }


    //提交
    public void commit(String password){
        String acount = json.optString("acount");
        String value = money.getText().toString().trim();
        String type = typeContent.getTag().toString();
        if(Util.isEmpty(acount)){
            alert("二维码错误，请重新扫描");return;
        }
        if(Util.isEmpty(value)){
            alert("转账金额不能为空");return;
        }

        JSONObject params = new JSONObject();
        try {
            params.put("transferPhone", acount);
            params.put("number", value);
            params.put("transferType", type);
            params.put("payPassword", password);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        BaseHttp.getInstance().write(MineTurnMoneyUI.this,"finance/transfer", params, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);
                if(200 == response.optInt("code")){
                    alert(response.optString("message"));
                    money.getText().clear();
                    typeContent.setTag("0");
                    typeContent.setText("USDT");
                }else{
                    alert(response.optString("message"));
                }
            }
        });
    }

    public static String hideId(String id){
        StringBuilder stringBuilder = new StringBuilder(id);
        stringBuilder.replace(2, stringBuilder.length()-2, "****");
        return stringBuilder.toString();
    }
}
