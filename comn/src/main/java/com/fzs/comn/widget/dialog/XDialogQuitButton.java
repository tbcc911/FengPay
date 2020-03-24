package com.fzs.comn.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
public class XDialogQuitButton extends Dialog {
	
	private TextView content,confirm,line;
	private XDialogQuitButtonCallBack callbackThis;

	public XDialogQuitButton(Context context) {
		super(context, com.hzh.frame.R.style.XSubmitDialog);
		LayoutInflater in = LayoutInflater.from(context);
		View viewDialog = in.inflate(com.hzh.frame.R.layout.base_xdialog_1button, null);
		content=(TextView) viewDialog.findViewById(com.hzh.frame.R.id.content);
		confirm=(TextView) viewDialog.findViewById(com.hzh.frame.R.id.confirm);
		line=(TextView) viewDialog.findViewById(com.hzh.frame.R.id.line);
		confirm.setVisibility(View.GONE);
		line.setVisibility(View.GONE);
		setContentView(viewDialog);
		// 点击对话框外部取消对话框显示
		setCanceledOnTouchOutside(false);
	}

	/**
	 * @param context
	 * @param msg 提示内容
	 * @param callback 选择结果回调
	 * */
	public XDialogQuitButton(Context context, String msg, XDialogQuitButtonCallBack callback) {
		super(context, com.hzh.frame.R.style.XSubmitDialog);
		callbackThis=callback;
		LayoutInflater in = LayoutInflater.from(context);
		View viewDialog = in.inflate(com.hzh.frame.R.layout.base_xdialog_1button, null);
		content=(TextView) viewDialog.findViewById(com.hzh.frame.R.id.content);
		confirm=(TextView) viewDialog.findViewById(com.hzh.frame.R.id.confirm);
		confirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				callbackThis.confirm();
				dismiss();
			}
		});
		content.setText(msg);
		setContentView(viewDialog);
		// 点击对话框外部取消对话框显示
		setCanceledOnTouchOutside(false);
	}
	
	public XDialogQuitButton setButtonName(String name){
        confirm.setText(name);
        return this;
    }
	
	/**
	 * 选择结果回调
	 * */
	public interface XDialogQuitButtonCallBack{
		  /**
		   * 确定
		   */
		  void confirm();
	}
}
