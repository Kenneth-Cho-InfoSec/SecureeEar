package com.bumptech.glide.load.engine.bitmap_recycle;

import android.graphics.Bitmap;

public class BitmapPoolAdapter implements BitmapPool {
    @Override
    public void clearMemory() {
    }

    @Override
    public long getMaxSize() {
        return 0L;
    }

    @Override
    public void setSizeMultiplier(float f) {
    }

    @Override
    public void trimMemory(int i) {
    }

    @Override
    public void put(Bitmap bitmap) {
        bitmap.recycle();
    }

    @Override
    public Bitmap get(int i, int i2, Bitmap.Config config) {
        return Bitmap.createBitmap(i, i2, config);
    }

    @Override
    public Bitmap getDirty(int i, int i2, Bitmap.Config config) {
        return get(i, i2, config);
    }
}
