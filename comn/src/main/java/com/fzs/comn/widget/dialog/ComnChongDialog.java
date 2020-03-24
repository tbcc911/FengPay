package com.fzs.comn.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.fzs.comn.R;
import com.fzs.comn.tools.ImageTools;
import com.fzs.comn.tools.UserTools;
import com.fzs.comn.tools.Util;
import com.hzh.frame.util.AndroidUtil;
import com.hzh.frame.util.Code2Util;
import com.hzh.frame.widget.toast.BaseToast;
public class ComnChongDialog extends Dialog {

    public Activity activity;
    ImageView qrCode;
    TextView payCode;
    Bitmap qrCodeBitmap;

    public ComnChongDialog(Activity activity) {
        super(activity, R.style.XSubmitDialog);
        this.activity=activity;
        LayoutInflater in = LayoutInflater.from(activity);
        View viewDialog = in.inflate(R.layout.comn_view_dialog_chong, null);
        viewDialog.findViewById(R.id.close).setOnClickListener(view -> dismiss());
        // 这里可以设置dialog的大小，当然也可以设置dialog title等
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(AndroidUtil.getWindowWith(activity) * 9 / 10, FrameLayout.LayoutParams.WRAP_CONTENT);
        setContentView(viewDialog,layoutParams);
        // 点击对话框外部取消对话框显示
        setCanceledOnTouchOutside(true);
        TextView save = viewDialog.findViewById(R.id.save);
        qrCode = viewDialog.findViewById(R.id.qr);
        payCode = viewDialog.findViewById(R.id.payCode);
        //获取剪贴板管理器
        save.setOnClickListener(v -> {
            if(qrCodeBitmap!=null){
                ImageTools.saveBitmap2Camera(activity,qrCodeBitmap,"UsdtAddress"+ UserTools.getInstance(activity).getUser().getAcount());
                BaseToast.getInstance().setMsg("保存到相册成功").show();
            }else{
                BaseToast.getInstance().setMsg("加载图片失败").show();
            }
        });

        payCode.setOnClickListener(v -> {
            ClipboardManager cmb = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData myClip = ClipData.newPlainText("ReCharge",payCode.getText().toString());
            cmb.setPrimaryClip(myClip);
            BaseToast.getInstance().setMsg("复制成功" + payCode.getText().toString()).show();
        });
    }

    @Override
    public void show() {
        if (!isShowing()) {
            super.show();
        }else{
            super.dismiss();
            super.show();
        }
    }


    public void loadData(CallBack callBack){
        if (UserTools.getInstance(getContext()).getIsLogin()) {
            String address=UserTools.getInstance().getUser().getUsdtAddress();
            int widthAndHeight= Util.dip2px(activity,activity.getResources().getDimension(R.dimen.dp_200));
            qrCodeBitmap= Code2Util.create(address, widthAndHeight,widthAndHeight);
            qrCode.setImageBitmap(qrCodeBitmap);
            payCode.setText(address);
            callBack.onSuccess();
        } else {
            BaseToast.getInstance().setMsg("网络异常，请检查您的网络").show();
        }
    }

    public interface CallBack{
        void onSuccess();
    }

}
