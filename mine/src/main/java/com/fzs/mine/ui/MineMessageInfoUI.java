package com.fzs.mine.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fzs.mine.R;
import com.hzh.frame.ui.activity.BaseUI;
import com.hzh.frame.widget.xwebview.XEmbedWebView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author
 * @version 1.0
 * @date 2019/7/30
 */
@Route(path = "/mine/MineMessageInfoUI")
public class MineMessageInfoUI extends BaseUI {
    TextView title;
    TextView message_content;
    TextView time;
    XEmbedWebView content;
    
    @Override
    protected void onCreateBase() {
        setContentView(R.layout.mine_ui_message_info);
        getTitleView().setContent("我的消息");
        
        title=findViewById(R.id.message_title);
        message_content=findViewById(R.id.message_content);
        time=findViewById(R.id.message_time);
        content=findViewById(R.id.content);
        getMessageInfo();
    }

    
    private void getMessageInfo(){

        if (getIntent().getStringExtra("title") != null && getIntent().getStringExtra("content") != null && getIntent().getStringExtra("createTime") != null){
            title.setText(getIntent().getStringExtra("title"));
            time.setText(getIntent().getStringExtra("createTime"));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                setActivityContent(getIntent().getStringExtra("content"));
            }else {
                message_content.setText(String.valueOf(Html.fromHtml(getIntent().getStringExtra("content"))));
            }
        }
    }



    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    CharSequence charSequence = (CharSequence) msg.obj;
                    if (charSequence != null) {
                        message_content.setText(charSequence);
                        message_content.setMovementMethod(LinkMovementMethod.getInstance());
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private void setActivityContent(final String activityContent) {
        new Thread(() -> {
            Html.ImageGetter imageGetter = source -> {
                Drawable drawable;
                drawable = getImageNetwork(source);
                if (drawable != null) {
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                } else if (drawable == null) {
                    return null;
                }
                return drawable;
            };
            CharSequence charSequence = Html.fromHtml(activityContent.trim(), imageGetter, null);
            Message ms = Message.obtain();
            ms.what = 1;
            ms.obj = charSequence;
            mHandler.sendMessage(ms);
        }).start();
    }

    /**
     * 连接网络获得相对应的图片
     * @param imageUrl
     * @return
     */
    public Drawable getImageNetwork(String imageUrl) {
        URL myFileUrl = null;
        Drawable drawable = null;
        try {
            myFileUrl = new URL(imageUrl);
            HttpURLConnection conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            drawable = new BitmapDrawable(bitmap);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return drawable;
    }
}
