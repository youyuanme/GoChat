package com.yibingding.haolaiwu.dialog;

import com.yibingding.haolaiwu.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class User_SimpleDialog extends Dialog {
	View.OnClickListener confirmListener;
	TextView confirm, cancel, title;

	public User_SimpleDialog(Context context) {
		super(context, R.style.simpledialog);
		Window window = getWindow();
		window.getDecorView().setPadding(0, 0, 0, 0);
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		window.setAttributes(lp);
		setCanceledOnTouchOutside(false);
		setContentView(R.layout.user_simpledailog);
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

	// @Override
	// protected void onCreate(Bundle savedInstanceState) {
	// // TODO Auto-generated method stub
	// super.onCreate(savedInstanceState);
	//
	// }

	public void setConfirmListener(View.OnClickListener listener) {
		if (confirm != null) {
			confirm.setOnClickListener(listener);
		}
		this.confirmListener = listener;
	}

	public void setConfirmText(String text) {
		confirm.setText(text);
	}

	public void setCancelText(String text) {
		cancel.setText(text);
	}

	public void setTitleText(String text) {
		title.setText(text);
	}
}
