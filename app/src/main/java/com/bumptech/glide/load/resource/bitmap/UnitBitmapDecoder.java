package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.Util;

public final class UnitBitmapDecoder implements ResourceDecoder<Bitmap, Bitmap> {
    @Override
    public boolean handles(Bitmap bitmap, Options options) {
        return true;
    }

    @Override
    public Resource<Bitmap> decode(Bitmap bitmap, int i, int i2, Options options) {
        return new NonOwnedBitmapResource(bitmap);
    }

    private static final class NonOwnedBitmapResource implements Resource<Bitmap> {
        private final Bitmap bitmap;

        @Override
        public void recycle() {
        }

        NonOwnedBitmapResource(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        public Class<Bitmap> getResourceClass() {
            return Bitmap.class;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override
        public Bitmap get() {
            return this.bitmap;
        }

        @Override
        public int getSize() {
            return Util.getBitmapByteSize(this.bitmap);
        }
    }
}
