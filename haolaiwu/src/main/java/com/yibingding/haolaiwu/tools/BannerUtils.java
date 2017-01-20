package com.yibingding.haolaiwu.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.ybd.app.viewpager.MyPageChangeListener;
import com.ybd.app.viewpager.MyPageChangeListener.PageSelectedListener;
import com.ybd.app.viewpager.ViewPagerAdapter;
import com.yibingding.haolaiwu.HouseDetailsActivity;
import com.yibingding.haolaiwu.R;
import com.yibingding.haolaiwu.ViewPagerActivity;

public class BannerUtils<T> {

	private Context context;
	private BannerEvent bannerEvent;
	private List<T> list;
	private ViewPager viewpagerInner;
	private LinearLayout indicator;

	private boolean timerShow;

	public BannerUtils(Context context, BannerEvent bannerEvent, List<T> list,
			ViewPager viewpagerInner, LinearLayout indicator, boolean timerShow) {
		this.context = context;
		this.bannerEvent = bannerEvent;
		this.list = list;
		this.viewpagerInner = viewpagerInner;
		this.indicator = indicator;
		this.timerShow = timerShow;

		initViewPagers(list, viewpagerInner, indicator);
	}

	private <T> void initViewPagers(final List<T> list,
			ViewPager viewpagerInner, LinearLayout indicator) {
		int pic_size = list.size();
		final ImageView[] indicatorImgs = initIndicators(indicator, pic_size);
		if (pic_size > 0) {
			List<View> list_img = new ArrayList<View>();
			for (int i = 0; i < pic_size; i++) {
				final ImageView iv_banner = new ImageView(context);
				iv_banner.setScaleType(ScaleType.FIT_XY);
				bannerEvent.disPlayBannerImages(list, i, iv_banner);
				list_img.add(iv_banner);
				final int pos = i;
				if (timerShow) {
					iv_banner.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							bannerEvent.clickPic(list, pos, iv_banner);

							context.startActivity(new Intent(context,
									ViewPagerActivity.class)
									.putExtra("type", "")
									.putStringArrayListExtra("list",
											(ArrayList<String>) list)
									.putExtra("position", pos));
						}
					});
				}

			}

			if (timerShow) {
				startTimerShow();
			}
			ViewPagerAdapter pageAdapter = new ViewPagerAdapter(list_img);
			viewpagerInner.setAdapter(pageAdapter);
			viewpagerInner.setCurrentItem(0);
			MyPageChangeListener listener = new MyPageChangeListener();
			System.out.println("=========================");
			listener.setOnPageSelectedListener(new PageSelectedListener() {
				@Override
				public void selected(int index) {
					// 改变�?��导航的背景图片为：未选中
					for (int i = 0; i < indicatorImgs.length; i++) {
						indicatorImgs[i]
								.setBackgroundResource(R.drawable.icon_banner_selected_not);
					}
					// 改变当前背景图片为：选中
					indicatorImgs[index]
							.setBackgroundResource(R.drawable.icon_banner_selected);
					System.err.println("=============" + indicatorImgs.length);
				}
			});
			viewpagerInner.setOnPageChangeListener(listener);

		}
	}

	private ImageView[] initIndicators(LinearLayout indicator, int picSize) {
		((ViewGroup) indicator).removeAllViews();

		ImageView[] indicator_imgs = new ImageView[picSize];// 存放引到图片数组
		ImageView imgView;

		for (int i = 0; i < picSize; i++) {
			imgView = new ImageView(context);
			LinearLayout.LayoutParams params_linear = new LinearLayout.LayoutParams(
					10, 10);
			params_linear.setMargins(7, 5, 7, 5);
			imgView.setLayoutParams(params_linear);
			indicator_imgs[i] = imgView;
			if (i == 0) { // 初始化第�?��为�?中状�?
				indicator_imgs[i]
						.setBackgroundResource(R.drawable.icon_banner_selected);
			} else {
				indicator_imgs[i]
						.setBackgroundResource(R.drawable.icon_banner_selected_not);
			}
			((ViewGroup) indicator).addView(indicator_imgs[i]);
		}
		return indicator_imgs;
	}

	// 定时轮播滚动

	private ScheduledExecutorService scheduledExecutorService;
	private int currentItem = 0; // 当前图片的索引号
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			viewpagerInner.setCurrentItem(currentItem);// 切换当前显示的图�?
		};
	};

	private class ScrollTask implements Runnable {
		public void run() {
			synchronized (viewpagerInner) {
				currentItem = (currentItem + 1) % list.size();
				handler.obtainMessage().sendToTarget(); // 通过Handler切换图片
			}
		}

	}

	public void startTimerShow() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 3,
				TimeUnit.SECONDS);
	}

	public void stopTimerShow() {
		if (scheduledExecutorService != null) {
			scheduledExecutorService.shutdown();
		}
	}

}
