/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.yibingding.haolaiwu;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.sample.HackyViewPager;
import android.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.yibingding.haolaiwu.domian.AdInfo;
import com.yibingding.haolaiwu.tools.Constants;

public class ViewPagerActivity extends Activity {

	private ViewPager mViewPager;
	private List<AdInfo> adinfos;
	private ArrayList<String> list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		Intent in = getIntent();
		String string = in.getStringExtra("type");
		if (TextUtils.equals("楼盘", string)) {
			adinfos = in.getParcelableArrayListExtra("adinfos");
			Log.e("=============",
					"===========adinfos.size()===" + adinfos.size());
		} else {
			list = in.getStringArrayListExtra("list");
			Log.e("=============", "========list.size()==" + list.size());
		}
		int position = getIntent().getIntExtra("position", 0);

		mViewPager = new HackyViewPager(this);
		mViewPager.setBackgroundColor(getResources().getColor(R.color.black));
		setContentView(mViewPager);

		mViewPager.setAdapter(new SamplePagerAdapter());
		mViewPager.setCurrentItem(position);
	}

	class SamplePagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			if (adinfos == null) {
				return list.size();
			}
			return adinfos.size();
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			PhotoView photoView = new PhotoView(container.getContext());
			// final ProgressBar progressBar = new ProgressBar(
			// container.getContext());
			// Bitmap bitmap = null;
			if (adinfos != null) {
				// ImageLoader.getInstance().displayImage(
				// adinfos.get(position).imgUrl, photoView,
				// new ImageLoadingListener() {
				//
				// @Override
				// public void onLoadingStarted(String arg0, View arg1) {
				// // TODO Auto-generated method stub
				// progressBar.setVisibility(View.VISIBLE);
				// }
				//
				// @Override
				// public void onLoadingFailed(String arg0, View arg1,
				// FailReason arg2) {
				// // TODO Auto-generated method stub
				// progressBar.setVisibility(View.GONE);
				// }
				//
				// @Override
				// public void onLoadingComplete(String arg0,
				// View arg1, Bitmap arg2) {
				// // TODO Auto-generated method stub
				// progressBar.setVisibility(View.GONE);
				// }
				//
				// @Override
				// public void onLoadingCancelled(String arg0,
				// View arg1) {
				// // TODO Auto-generated method stub
				//
				// }
				// });
				ImageLoader.getInstance().displayImage(
						adinfos.get(position).imgUrl, photoView);
				// bitmap = ImageLoader.getInstance().loadImageSync(
				// adinfos.get(position).imgUrl);
			} else {
				ImageLoader.getInstance().displayImage(
						Constants.IMAGE_URL + list.get(position), photoView);
				// bitmap = ImageLoader.getInstance().loadImageSync(
				// Constants.IMAGE_URL + list.get(position));
			}
			// if (bitmap != null) {
			// photoView.setImageBitmap(bitmap);
			// }

			// Now just add PhotoView to ViewPager and return it
			container.addView(photoView, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			// container.addView(progressBar, LayoutParams.WRAP_CONTENT,
			// LayoutParams.WRAP_CONTENT);
			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

}
