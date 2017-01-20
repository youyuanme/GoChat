package com.yibingding.haolaiwu.view;

import com.yibingding.haolaiwu.R;
import com.yibingding.haolaiwu.houseparopertynew.HousePropertyNewDetailsActivity;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

public class NewPopuwindow extends PopupWindow {

	private View rootView; // 总的布局
	private TextView tv_shoucang, tv_fenxiang;
	private NewPopuwindowOnClickListener newPopuwindowOnClickListener;
	private Context mContext;

	public NewPopuwindow(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.setWidth(LayoutParams.WRAP_CONTENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// ColorDrawable dw = new ColorDrawable(0xb0000000);
		// this.setBackgroundDrawable(dw);
		this.setBackgroundDrawable(new BitmapDrawable());
		this.setTouchable(true);
		this.setFocusable(true);
		this.setAnimationStyle(R.style.PopupWindowAnimation);
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		rootView = layoutInflater.inflate(R.layout.popu_new_right, null);

		tv_shoucang = (TextView) rootView.findViewById(R.id.tv_shoucang);
		tv_fenxiang = (TextView) rootView.findViewById(R.id.tv_fenxiang);

		tv_shoucang.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				newPopuwindowOnClickListener
						.onPopuwindowOnClick(HousePropertyNewDetailsActivity.popuShoucang);
				dismiss();
			}
		});
		tv_fenxiang.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				newPopuwindowOnClickListener
						.onPopuwindowOnClick(HousePropertyNewDetailsActivity.popuFenxinag);
				dismiss();
			}
		});
		this.setContentView(rootView);
	}

	public void setShoucangImage(String isshoucang) {
		if ("是".equals(isshoucang)) {
			Drawable drawable = mContext.getResources().getDrawable(R.drawable.popu_new_shoucang0);
			// drawable.setBounds(0, 0, 32, 32);
			tv_shoucang.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
			
		} else {
			Drawable drawable = mContext.getResources().getDrawable(R.drawable.popu_new_shoucang);
			// drawable.setBounds(0, 0, 32, 32);
			// tv_shoucang.setCompoundDrawables(drawable, null, null,null);
			tv_shoucang.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
		}
	}

	public interface NewPopuwindowOnClickListener {
		public void onPopuwindowOnClick(int type);
	}

	public void setNewPopuwindowOnClickListener(
			NewPopuwindowOnClickListener setOnPopuwindowListener) {
		this.newPopuwindowOnClickListener = setOnPopuwindowListener;
	}

}
