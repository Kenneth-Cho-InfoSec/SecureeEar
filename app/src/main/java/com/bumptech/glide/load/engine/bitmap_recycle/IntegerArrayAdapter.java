package com.bumptech.glide.load.engine.bitmap_recycle;

public final class IntegerArrayAdapter implements ArrayAdapterInterface<int[]> {
    private static final String TAG = "IntegerArrayPool";

    @Override
    public int getElementSizeInBytes() {
        return 4;
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public int getArrayLength(int[] iArr) {
        return iArr.length;
    }

    @Override
    public int[] newArray(int i) {
        return new int[i];
    }
}
