package com.sibozn.gochat.application;

import android.app.Activity;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;

import org.java_websocket.WebSocketImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/1.
 */
public class MyApplication extends MultiDexApplication {
    public static boolean isLoginWebSocket = false;
    public static List<Activity> activiys;
    public static String mEmail, mToken, fd;
    private static MyApplication instance;

    {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Intent intent = new Intent(this, WebSocketClientService.class);
        //intent.setAction("com.sibozn.gochat.WEB_SOCKTE_MESSAGE");
        //startService(intent);
        // File cacheDir = StorageUtils.getOwnCacheDirectory(this,
        // "imageloader/Cache");
        activiys = new ArrayList<>();

//        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
//                .showImageForEmptyUri(R.mipmap.default_pic1)
//                .showImageOnFail(R.mipmap.default_pic1)
//                .showImageOnLoading(R.mipmap.default_pic1).cacheInMemory(true)
//                .cacheOnDisc(true).build();
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
//                .defaultDisplayImageOptions(defaultOptions)
//                .discCacheSize(50 * 1024 * 1024).discCacheFileCount(100)
//                .writeDebugLogs()
//                // default为使用HASHCODE对UIL进行加密命名， 还可以用MD5(new
//                // Md5FileNameGenerator())加密
//                .discCacheFileNameGenerator(new Md5FileNameGenerator()) // 将保存的时候的URI名称用MD5
//                // 加密
//                .build();
        // DisplayImageOptions defaultOptions = new
        // DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.default_pic)
        // .showImageOnFail(R.drawable.default_pic).cacheInMemory(true).cacheOnDisc(true).build();
        // ImageLoaderConfiguration config = new
        // ImageLoaderConfiguration.Builder(getApplicationContext())
        // .defaultDisplayImageOptions(defaultOptions).discCacheSize(50 * 1024 *
        // 1024).discCacheFileCount(100).writeDebugLogs()
        // .discCache(new UnlimitedDiscCache(cacheDir))
        // .build();
//        ImageLoader.getInstance().init(config);

        if (instance == null) {
            instance = this;
        }
        // Facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        NoHttp.initialize(this);
        NoHttp.setDefaultConnectTimeout(10 * 1000);
        NoHttp.setDefaultReadTimeout(10 * 1000);
        Logger.setTag("NoHttp============");
        Logger.setDebug(true);// 开始NoHttp的调试模式, 这样就能看到请求过程和日志
        WebSocketImpl.DEBUG = true;
    }

    public static MyApplication getInstance() {
        return instance;
    }
}
