package com.yibingding.haolaiwu.view;

import com.yibingding.haolaiwu.adapter.MyAttention_Adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class ScrollListviewDelete extends ListView implements OnScrollListener {

	private float minDis = 10;
	private float mLastMotionX;// 记住上次X触摸屏的位置
	private float mLastMotionY;// 记住上次Y触摸屏的位置
	private boolean isLock = false;

	public interface ItemClickListener {
		void onItemClick(int position);
	}

	private ItemClickListener onItemClickListener;

	public void setOnItemClickListener(ItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public ScrollListviewDelete(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnScrollListener(this);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (!isIntercept(ev)) {
			MyAttention_Adapter.ItemDeleteReset();
			return false;
		}
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		boolean dte = super.dispatchTouchEvent(event);
		if (MotionEvent.ACTION_UP == event.getAction() && !dte) {// onItemClick
			int position = pointToPosition((int) event.getX(),
					(int) event.getY());
			if (onItemClickListener != null) {
				onItemClickListener.onItemClick(position);
			}
		}
		return dte;
	}

	private boolean isIntercept(MotionEvent ev) {
		float x = ev.getX();
		float y = ev.getY();
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mLastMotionX = x;
			mLastMotionY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			if (!isLock) {
				float deltaX = Math.abs(mLastMotionX - x);
				float deltay = Math.abs(mLastMotionY - y);
				mLastMotionX = x;
				mLastMotionY = y;
				if (deltaX > deltay && deltaX > minDis) {
					isLock = true;
					return false;
				}
			} else {
				return false;
			}
			break;
		case MotionEvent.ACTION_UP:
			isLock = false;
			break;
		case MotionEvent.ACTION_CANCEL:
			isLock = false;
			break;
		}
		return true;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState != OnScrollListener.SCROLL_STATE_IDLE) {// 认为是滚动，重置
			MyAttention_Adapter.ItemDeleteReset();
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

	}

}
