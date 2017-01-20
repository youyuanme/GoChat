package com.yibingding.haolaiwu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Window;

import com.yibingding.haolaiwu.R;

public class Dialognetwork extends Dialog{

	public Dialognetwork(Context context) {
		super(context);
		setCanceledOnTouchOutside(false);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		setContentView(R.layout.dialog_network);
		setCanceledOnTouchOutside(false);
		getWindow().setBackgroundDrawable(new BitmapDrawable());
	}
}
