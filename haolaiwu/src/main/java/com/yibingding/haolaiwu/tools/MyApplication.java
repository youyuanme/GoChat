package com.yibingding.haolaiwu.tools;

import android.app.Activity;
import android.os.Handler;
import cn.jpush.android.api.JPushInterface;

import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.ybd.app.MyBaseApplication;
import com.yibingding.haolaiwu.R;

public class MyApplication extends MyBaseApplication {
	public static String pageSize = "10";
	public static String wordCount = "100";
	public static Handler handler;
	public static String city;
	public static MyApplication application;

	@Override
	public void onCreate() {
		super.onCreate();
		handler = new Handler();
		application = this;
		SDKInitializer.initialize(getApplicationContext());
		// File cacheDir = StorageUtils.getOwnCacheDirectory(this,
		// "imageloader/Cache");

		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.default_pic)
				.showImageOnFail(R.drawable.default_pic)
				.showImageOnLoading(R.drawable.default_pic).cacheInMemory(true)
				.cacheOnDisc(true).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.defaultDisplayImageOptions(defaultOptions)
				.discCacheSize(50 * 1024 * 1024).discCacheFileCount(100)
				.writeDebugLogs()
				// default为使用HASHCODE对UIL进行加密命名， 还可以用MD5(new
				// Md5FileNameGenerator())加密
				.discCacheFileNameGenerator(new Md5FileNameGenerator()) // 将保存的时候的URI名称用MD5
																		// 加密
				.build();

		// DisplayImageOptions defaultOptions = new
		// DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.default_pic)
		// .showImageOnFail(R.drawable.default_pic).cacheInMemory(true).cacheOnDisc(true).build();
		// ImageLoaderConfiguration config = new
		// ImageLoaderConfiguration.Builder(getApplicationContext())
		// .defaultDisplayImageOptions(defaultOptions).discCacheSize(50 * 1024 *
		// 1024).discCacheFileCount(100).writeDebugLogs()
		// .discCache(new UnlimitedDiscCache(cacheDir))
		// .build();
		ImageLoader.getInstance().init(config);

		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);

		city = getSharedPreferences(MyConstant.SP_CONFIG_NAME,
				Activity.MODE_PRIVATE).getString(MyConstant.CITY_NAME, "");
	}

}
