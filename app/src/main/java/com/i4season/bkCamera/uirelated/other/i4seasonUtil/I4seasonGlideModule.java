package com.i4season.bkCamera.uirelated.other.i4seasonUtil;

import android.content.Context;
import android.os.Environment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.GlideModule;
import com.i4season.bkCamera.uirelated.other.AppPathInfo;
import java.io.File;

public class I4seasonGlideModule implements GlideModule {
    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
    }

    @Override
    public void applyOptions(Context context, GlideBuilder glideBuilder) {
        long jMaxMemory = ((int) Runtime.getRuntime().maxMemory()) / 8;
        glideBuilder.setMemoryCache(new LruResourceCache(jMaxMemory));
        glideBuilder.setBitmapPool(new LruBitmapPool(jMaxMemory));
        glideBuilder.setDiskCache(new DiskLruCacheFactory(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + AppPathInfo.app_package_name + AppPathInfo.app_thumb_cache, 100000000));
    }
}
