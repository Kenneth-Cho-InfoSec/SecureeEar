package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Util;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

public class Rotate extends BitmapTransformation {

    private static final String f60ID = "com.bumptech.glide.load.resource.bitmap.Rotate";
    private static final byte[] ID_BYTES = f60ID.getBytes(CHARSET);
    private final int degreesToRotate;

    public Rotate(int i) {
        this.degreesToRotate = i;
    }

    @Override
    protected Bitmap transform(BitmapPool bitmapPool, Bitmap bitmap, int i, int i2) {
        return TransformationUtils.rotateImage(bitmap, this.degreesToRotate);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Rotate) && this.degreesToRotate == ((Rotate) obj).degreesToRotate;
    }

    @Override
    public int hashCode() {
        return Util.hashCode(f60ID.hashCode(), Util.hashCode(this.degreesToRotate));
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
        messageDigest.update(ByteBuffer.allocate(4).putInt(this.degreesToRotate).array());
    }
}
