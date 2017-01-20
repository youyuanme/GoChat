package com.sibozn.gochat.application;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by wjk on 2016/8/13.
 */
public class MyGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder glideBuilder) {
        // Apply options to the builder here.
        // glideBuilder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        // glideBuilder.setDiskCache(new ExternalCacheDiskCacheFactory(context));
        glideBuilder.setMemoryCache(new LruResourceCache(100*1024*1024));

    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        // register ModelLoaders here.
    }
}
