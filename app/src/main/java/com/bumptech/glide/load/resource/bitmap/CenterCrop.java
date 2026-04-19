package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import java.security.MessageDigest;

public class CenterCrop extends BitmapTransformation {

    private static final String f55ID = "com.bumptech.glide.load.resource.bitmap.CenterCrop";
    private static final byte[] ID_BYTES = f55ID.getBytes(CHARSET);

    @Override
    protected Bitmap transform(BitmapPool bitmapPool, Bitmap bitmap, int i, int i2) {
        return TransformationUtils.centerCrop(bitmapPool, bitmap, i, i2);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CenterCrop;
    }

    @Override
    public int hashCode() {
        return f55ID.hashCode();
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }
}
