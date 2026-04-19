package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import java.security.MessageDigest;

public class CircleCrop extends BitmapTransformation {

    private static final String f57ID = "com.bumptech.glide.load.resource.bitmap.CircleCrop.1";
    private static final byte[] ID_BYTES = f57ID.getBytes(CHARSET);
    private static final int VERSION = 1;

    @Override
    protected Bitmap transform(BitmapPool bitmapPool, Bitmap bitmap, int i, int i2) {
        return TransformationUtils.circleCrop(bitmapPool, bitmap, i, i2);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CircleCrop;
    }

    @Override
    public int hashCode() {
        return f57ID.hashCode();
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }
}
