package com.fzs.comn.widget.dialog;

import android.os.Build;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fzs.comn.R;
import com.hzh.frame.util.AndroidUtil;
import com.hzh.frame.widget.xdialog.XDialogFragment;

public class AgreementDialog extends XDialogFragment {

    public static final String TAG="AgreementDialog";
    
    int layoutResour;
    Callback callback;
    String msg;


    public int getLayoutResour() {
        return layoutResour;
    }

    public AgreementDialog setLayoutResour(int layoutResour) {
        this.layoutResour = layoutResour;
        return this;
    }

    public AgreementDialog setMessage(String msg) {
        this.msg = msg;
        return this;
    }

    @Override
    protected void bindView(View layout) {
        super.bindView(layout);
        layout.findViewById(R.id.cancle).setOnClickListener(view ->{
            if(callback!=null){
                dismiss();
                callback.cancle();
            }
        });
        layout.findViewById(R.id.confirm).setOnClickListener(view ->{
            if(callback!=null){
                dismiss();
                callback.confirm();
            }
        });
        TextView details = layout.findViewById(R.id.details);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            details.setText(String.valueOf(Html.fromHtml(msg,Html.FROM_HTML_MODE_COMPACT)));
        }else {
            details.setText(String.valueOf(Html.fromHtml(msg)));
        }
        
        LinearLayout.LayoutParams lp= (LinearLayout.LayoutParams) layout.findViewById(R.id.dialogLayout).getLayoutParams();
        lp.height= (int)(AndroidUtil.getWindowHeight()/3.0*2);
    }
    
    public interface Callback{
        void cancle();
        
        void confirm();
    }
    
    public AgreementDialog setCallback(Callback callback){
        this.callback = callback;
        return this;
    }
}
