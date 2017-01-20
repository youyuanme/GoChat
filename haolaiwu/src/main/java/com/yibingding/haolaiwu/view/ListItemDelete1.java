package com.yibingding.haolaiwu.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;

public class ListItemDelete1 extends LinearLayout {

	private int deltaX;
	private int back_width;// 滑动显示组件的宽度
	private float downX;
	public int isEdit;

	public ListItemDelete1(Context context) {
		this(context, null);
	}

	public ListItemDelete1(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
			if (i == 1) {
				back_width = getChildAt(i).getMeasuredWidth();
				System.out.println("onMeasure+++++"+back_width);
				finishMeasure = true;
				handler.sendEmptyMessage(0);
			}
		}
	}
	
	
	
	public void reSet() {
		finishScroll = false;
		 scrollTo(0, 0);
//		this.setPadding(	0, 0, 0, 0);
	}
	
	private boolean finishScroll;
	private boolean finishMeasure;
	private Handler handler = new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			if (finishScroll && finishMeasure) {
				scrollTo(back_width, 0);
			}
		};
	};
	
	public void openDel() {
		System.out.println("back_width+++++"+back_width);
//		this.setPadding(-back_width, 0, 0, 0);
//		System.out.println(back_width + "");
		if (back_width != 0) {
			scrollTo(back_width, 0);
		} else {
			finishScroll = true;
			handler .sendEmptyMessage(0);
//			ViewTreeObserver viewTreeObserver = getChildAt(1)
//					.getViewTreeObserver();
//			viewTreeObserver
//					.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
//
//						@Override
//						public void onGlobalLayout() {
//							ListItemDelete1.this.getChildAt(1)
//									.getViewTreeObserver()
//									.removeGlobalOnLayoutListener(this);
//							back_width = ListItemDelete1.this.getChildAt(1)
//									.getMeasuredWidth();
//							System.out.println("back_width:::" + back_width);
//							scrollTo(back_width, 0);
//
//						}
//					});
		}
		// getMeasuredWidth();

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		int margeLeft = 0;
		int size = getChildCount();
		for (int i = 0; i < size; i++) {
			View view = getChildAt(i);
			if (view.getVisibility() != View.GONE) {
				int childWidth = view.getMeasuredWidth();
				// 将内部子孩子横排排列
				view.layout(margeLeft, 0, margeLeft + childWidth,
						view.getMeasuredHeight());
				margeLeft += childWidth;
			}
		}
	}
}
