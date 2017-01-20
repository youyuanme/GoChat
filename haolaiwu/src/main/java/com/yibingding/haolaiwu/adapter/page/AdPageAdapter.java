package com.yibingding.haolaiwu.adapter.page;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yibingding.haolaiwu.FangdaijisuanqiActivity;
import com.yibingding.haolaiwu.ViewPagerActivity;
import com.yibingding.haolaiwu.domian.AdInfo;
import com.yibingding.haolaiwu.tools.Constants;

public class AdPageAdapter extends PagerAdapter {

	private List<AdInfo> adinfos;
	private Context ctx;
	private boolean isMainActivity;

	public AdPageAdapter(Context ctx, List<AdInfo> adinfos) {
		this.ctx = ctx;
		this.adinfos = adinfos;
	}

	public AdPageAdapter(Context ctx, List<AdInfo> adinfos,
			boolean isMainActivity) {
		this.ctx = ctx;
		this.adinfos = adinfos;
		this.isMainActivity = isMainActivity;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		ImageView imgView = new ImageView(ctx);
		imgView.setScaleType(ScaleType.FIT_XY);
		ImageLoader.getInstance().displayImage(adinfos.get(position).imgUrl,
				imgView);
		imgView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isMainActivity) {
					ctx.startActivity(new Intent(ctx,
							FangdaijisuanqiActivity.class).putExtra("url",
							Constants.GET_INDEX_image
									+ adinfos.get(position).guid));
					System.out.println("首页轮播图跳转。。。。"
							+ adinfos.get(position).guid);
				} else {
					ctx.startActivity(new Intent(ctx, ViewPagerActivity.class)
							.putParcelableArrayListExtra("adinfos",
									(ArrayList<? extends Parcelable>) adinfos)
							.putExtra("type", "楼盘")
							.putExtra("position", position));
					System.out.println("明细页面轮播图点击链接。。。。"
							+ adinfos.get(position).guid);
				}
			}
		});

		// ImageLoader.getInstance().clearMemoryCache();

		container.addView(imgView);
		return imgView;
	}

	@Override
	public int getCount() {
		if (adinfos == null) {
			return 0;
		}
		return adinfos.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

}
