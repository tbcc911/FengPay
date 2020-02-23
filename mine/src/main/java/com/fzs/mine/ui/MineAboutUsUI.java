package com.fzs.mine.ui;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.fzs.comn.tools.InitTools;
import com.fzs.mine.R;
import com.hzh.frame.ui.activity.BaseUI;
import com.hzh.frame.util.AndroidUtil;
import com.hzh.frame.widget.x5webview.X5WebViewUI;

/**
 * 关于我们
 * */
public class MineAboutUsUI extends BaseUI implements OnClickListener{
	TextView version;
	TextView phone;
	TextView website;
    TextView patch;

    public void findViewById(){
        version=findViewById(R.id.version);
        phone=findViewById(R.id.phone);
        website=findViewById(R.id.website);
        patch=findViewById(R.id.patch);
        findViewById(R.id.phoneLin).setOnClickListener(this);
        findViewById(R.id.websiteLin).setOnClickListener(this);
    }
    
	@Override
	protected void onCreateBase() {
		setContentView(R.layout.mine_ui_aboutus);
        findViewById();
		getTitleView().setContent("关于我们");
        phone.setText(InitTools.getInstance().getConfig().getCustomerTelephone());
        website.setText(InitTools.getInstance().getConfig().getCompanyWebSite());
		version.setText("当前版本:V"+ AndroidUtil.getVersionName(MineAboutUsUI.this));
	}


	@Override
	public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.phoneLin) {//拨打电话
//            Intent in1 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + ComnConfig.phone));
//            in1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(in1);

            Intent intent = new Intent(Intent.ACTION_DIAL);
            Uri data = Uri.parse("tel:" + phone.getText().toString().trim());
            intent.setData(data);
            startActivity(intent);
            
        } else 
        if (id == R.id.websiteLin) {
            Intent in=new Intent(this, X5WebViewUI.class);
            in.putExtra("url", website.getText().toString().trim());
            startActivity(in);
        }
	}

}
