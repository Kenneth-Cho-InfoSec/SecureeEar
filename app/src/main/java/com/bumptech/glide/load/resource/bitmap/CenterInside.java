package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import java.security.MessageDigest;

public class CenterInside extends BitmapTransformation {

    private static final String f56ID = "com.bumptech.glide.load.resource.bitmap.CenterInside";
    private static final byte[] ID_BYTES = f56ID.getBytes(CHARSET);

    @Override
    protected Bitmap transform(BitmapPool bitmapPool, Bitmap bitmap, int i, int i2) {
        return TransformationUtils.centerInside(bitmapPool, bitmap, i, i2);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CenterInside;
    }

    @Override
    public int hashCode() {
        return f56ID.hashCode();
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }
}
