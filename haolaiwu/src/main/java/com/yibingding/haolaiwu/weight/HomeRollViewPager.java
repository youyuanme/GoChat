package com.yibingding.haolaiwu.weight;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yibingding.haolaiwu.R;
import com.yibingding.haolaiwu.tools.ThreadUtils;

public class HomeRollViewPager extends ViewPager {

	private List<ImageView> pointList;

	public HomeRollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.setOnPageChangeListener(pageChageListener);
	}

	public HomeRollViewPager(Context context) {
		super(context);

		this.setOnPageChangeListener(pageChageListener);

	}

	private int lastPosition = 0;

	private OnPageChangeListener pageChageListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int position) {
			if (null != pointList && pointList.size() > 0) {
				pointList.get(lastPosition).setImageResource(
						R.drawable.red_stroke);
				pointList.get(position).setImageResource(R.drawable.red_full);
				lastPosition = position;
			}
			currentPosition = position;
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
		}

		@Override
		public void onPageScrollStateChanged(int state) {

		}
	};

	/**
	 * 显示 数据 必须调用。。
	 * 
	 * @param adapter
	 *            传入 adapter
	 */
	public void setMyAdapter(PagerAdapter adapter) {
		HomeRollViewPager.this.setAdapter(adapter);
		createHandler();
	}

	private int size;

	/**
	 * 创建该方法必须调用
	 * 
	 * @param totalSize
	 *            数据长度
	 * @param points
	 *            指示点的linearlayout
	 */
	public void initPointList(int totalSize, LinearLayout points) {
		this.size = totalSize;

		float scale = getContext().getResources().getDisplayMetrics().density;
		int marginLeft = (int) (6 * scale + 0.5f);

		pointList = new ArrayList<ImageView>();
		for (int i = 0; i < totalSize; i++) {
			ImageView point = new ImageView(getContext());
			if (i == 0) {
				point.setImageResource(R.drawable.red_full);
			} else {

				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				layoutParams.setMargins(marginLeft, 0, 0, 0);
				point.setLayoutParams(layoutParams);
				point.setImageResource(R.drawable.red_stroke);
			}
			pointList.add(point);

		}

		points.removeAllViews();
		for (int i = 0; i < pointList.size(); i++) {
			points.addView(pointList.get(i));
		}

	}

	private int lastX;
	private int lastY;
	private boolean isFirst = true;
	private boolean isMine  ;
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			lastX = (int) ev.getX();
			lastY = (int) ev.getY();
		}
		int moveX = (int) ev.getX();
		int moveY = (int) ev.getY();
		if (ev.getAction() == MotionEvent.ACTION_MOVE) {
			int disX = Math.abs(moveX - lastX);
			int disY = Math.abs(moveY - lastY);
			if (isFirst) {
				if (disX > disY-2) {
					isMine = true;
					getParent().requestDisallowInterceptTouchEvent(true);
				}else{
					isMine = false;
					getParent().requestDisallowInterceptTouchEvent(false);
				}
				isFirst = false;
			}
		}

		if (ev.getAction() == MotionEvent.ACTION_UP
				|| ev.getAction() == MotionEvent.ACTION_CANCEL) {
			isFirst = true;
			isMine = false;
			go();
			lastX = 0;
			lastY = 0;
		}
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			cancel();
		}

		return super.dispatchTouchEvent(ev);
	}

	private Handler handler;
	private final int WHAT_GO = 1000;
	private int currentPosition = 0;

	private synchronized void createHandler() {
		if (handler == null) {
			ThreadUtils.runUIThread(new Runnable() {
				@Override
				public void run() {
					handler = new Handler() {
						@Override
						public void handleMessage(Message msg) {
							switch (msg.what) {
							case WHAT_GO:
								if (HomeRollViewPager.this != null) {
									
									currentPosition++;
									HomeRollViewPager.this
									.setCurrentItem(currentPosition % size);
								}
								sendEmptyMessageDelayed(WHAT_GO, 5000);
								break;
							}
						}
					};
					go();
				}
			});
		}
	}

	public boolean isGoing;

	public void go() {
		isGoing = true;
		if (handler == null) {
			createHandler();
		} else {
			handler.removeMessages(WHAT_GO);
			handler.sendEmptyMessageDelayed(WHAT_GO, 5000);
		}
	}

	public void cancel() {
		isGoing = false;
		if (handler != null) {
			
			handler.removeMessages(WHAT_GO);
		}
	}

}
