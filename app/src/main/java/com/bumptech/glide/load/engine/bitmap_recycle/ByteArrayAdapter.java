package com.bumptech.glide.load.engine.bitmap_recycle;

public final class ByteArrayAdapter implements ArrayAdapterInterface<byte[]> {
    private static final String TAG = "ByteArrayPool";

    @Override
    public int getElementSizeInBytes() {
        return 1;
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public int getArrayLength(byte[] bArr) {
        return bArr.length;
    }

    @Override
    public byte[] newArray(int i) {
        return new byte[i];
    }
}
