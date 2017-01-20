package com.yibingding.haolaiwu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.jpush.android.api.b;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.ybd.app.BaseActivity;
import com.ybd.app.interf.GetDataSuccessListener;
import com.ybd.app.tools.PreferenceHelper;
import com.ybd.app.viewpager.MyPageChangeListener;
import com.ybd.app.viewpager.ViewPagerAdapter;
import com.yibingding.haolaiwu.internet.Get_splash_pics_Volley;
import com.yibingding.haolaiwu.tools.AESUtils;
import com.yibingding.haolaiwu.tools.BannerEvent;
import com.yibingding.haolaiwu.tools.BannerUtils;
import com.yibingding.haolaiwu.tools.Constants;

public class SplashActivit extends BaseActivity implements OnClickListener,
		GetDataSuccessListener {

	private ViewPager viewPager;
	private LinearLayout indicator;
	private TextView tv_lijitiyan;
	private List<String> pics;
	private List<View> views = new ArrayList<View>();

	@Override
	public void onCreateThisActivity() {
		setContentView(R.layout.activity_splash);
	}

	@Override
	public void initViews() {
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		indicator = (LinearLayout) findViewById(R.id.indicator);
		tv_lijitiyan = (TextView) findViewById(R.id.tv_lijitiyan);
		tv_lijitiyan.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_lijitiyan:
			PreferenceHelper.write(this, "userinfo", "isSplash", true);
			startActivity(new Intent(this, MainActivity.class));
			finish();
			break;
		}
	}

	@Override
	public void initData() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("Token", AESUtils.encode("Token"));
		Get_splash_pics_Volley get_splash_pics_Volley = new Get_splash_pics_Volley(
				this, Constants.GET_Splash_image, map);
		get_splash_pics_Volley.setOnGetDataSuccessListener(this);
	}

	@Override
	public void onGetDataSuccess(String tag, Object obj) {
		if (obj != null) {
			pics = (List<String>) obj;
			final ImageView[] indicatorImgs = initIndicators(indicator, pics);
			if (pics.size() == 1) {
				tv_lijitiyan.setVisibility(View.VISIBLE);
			}
			viewPager.setAdapter(new ViewPagerAdapter(views));
			viewPager.setOnPageChangeListener(new OnPageChangeListener() {
				@Override
				public void onPageSelected(int arg0) {
					if (arg0 == (pics.size() - 1)) {
						tv_lijitiyan.setVisibility(View.VISIBLE);
					} else {
						tv_lijitiyan.setVisibility(View.GONE);
					}
					// 改变�?��导航的背景图片为：未选中
					for (int i = 0; i < pics.size(); i++) {
						indicatorImgs[i]
								.setBackgroundResource(R.drawable.icon_banner_selected_not);
					}
					// 改变当前背景图片为：选中
					indicatorImgs[arg0]
							.setBackgroundResource(R.drawable.icon_banner_selected);
					System.err.println("=============" + indicatorImgs.length);
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					if (!(arg0 == (pics.size() - 1))) {
						tv_lijitiyan.setAlpha(arg1);
					}
				}

				@Override
				public void onPageScrollStateChanged(int arg0) {
					// TODO Auto-generated method stub

				}
			});

		}
	}

	private ImageView[] initIndicators(LinearLayout indicator, List<String> pics) {
		((ViewGroup) indicator).removeAllViews();
		ImageView[] indicator_imgs = new ImageView[pics.size()];// 存放引到图片数组
		ImageView imgView;
		for (int i = 0; i < pics.size(); i++) {
			LinearLayout linearLayout = new LinearLayout(this);
			ImageView imageView = new ImageView(this);
			linearLayout.addView(imageView, new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT));
			imageView.setScaleType(ScaleType.FIT_XY);
			ImageLoader.getInstance().displayImage(
					Constants.IMAGE_URL + pics.get(i), imageView,
					new DisplayImageOptions.Builder().build());
			views.add(linearLayout);
			imgView = new ImageView(this);
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

}
