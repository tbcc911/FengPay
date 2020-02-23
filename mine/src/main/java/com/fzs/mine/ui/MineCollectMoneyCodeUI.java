package com.fzs.mine.ui;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fzs.comn.model.User;
import com.fzs.comn.tools.ImageTools;
import com.fzs.comn.tools.UserTools;
import com.fzs.mine.R;
import com.google.gson.Gson;
import com.hzh.frame.ui.activity.BaseUI;
import com.hzh.frame.util.Code2Util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * 收款二维码
 * @date 2019/9/4
 */
@Route(path = "/mine/MineCollectMoneyCodeUI")
public class MineCollectMoneyCodeUI extends BaseUI {
    
    public Bitmap codeBitmap;
    User user;
    private ImageView code;
    private Button saveCode;
    private Button collectionRecord;

    @Override
    protected void onCreateBase() {
        setContentView(R.layout.mine_ui_collect_money_code);
        getTitleView().setContent("收款码");
        code = findViewById(R.id.code);
        saveCode = findViewById(R.id.saveCode);
        collectionRecord = findViewById(R.id.collectionRecord);
        user= UserTools.getInstance(this).getUser();
        HashMap<String, Object> params=new HashMap<>();
        params.put("acount",user.getAcount());//帐号
        params.put("head",user.getHead());//头像
        params.put("nickName",user.getNickName());//昵称

        try {
            codeBitmap= Code2Util.create(URLEncoder.encode(new Gson().toJson(params), "UTF-8"), (int)getResources().getDimension(R.dimen.dp_200),(int)getResources().getDimension(R.dimen.dp_200));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        code.setImageBitmap(codeBitmap);
        
        saveCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageTools.saveBitmap2Camera(MineCollectMoneyCodeUI.this,codeBitmap,user.getAcount());
                alert("保存成功");
            }
        });
        collectionRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
    }

    
    
}
