package com.yibingding.haolaiwu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.yibingding.haolaiwu.R;

public class DialogUnLogin extends Dialog {

	View.OnClickListener confirmListener;
	TextView confirm, cancel, title;

	public DialogUnLogin(Context context) {
		super(context);
		setCanceledOnTouchOutside(false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.unlogin_dailog);
		setCanceledOnTouchOutside(false);
		getWindow().setBackgroundDrawable(new BitmapDrawable());
		Window window = this.getWindow();
		window.getDecorView().setPadding(0, 0, 0, 0);
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		window.setAttributes(lp);

		confirm = (TextView) findViewById(R.id.confirm);
		confirm.setOnClickListener(confirmListener);
		cancel = (TextView) findViewById(R.id.cancel);
		title = (TextView) findViewById(R.id.title);
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
	}

	public void setConfirmListener(View.OnClickListener listener) {
		if (confirm != null) {
			confirm.setOnClickListener(listener);
		}
		this.confirmListener = listener;
	}
}
