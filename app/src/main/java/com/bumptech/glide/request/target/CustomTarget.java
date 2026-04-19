package com.bumptech.glide.request.target;

import android.graphics.drawable.Drawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.util.Util;

public abstract class CustomTarget<T> implements Target<T> {
    private final int height;
    private Request request;
    private final int width;

    @Override
    public void onDestroy() {
    }

    @Override
    public void onLoadFailed(Drawable drawable) {
    }

    @Override
    public void onLoadStarted(Drawable drawable) {
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public final void removeCallback(SizeReadyCallback sizeReadyCallback) {
    }

    public CustomTarget() {
        this(Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    public CustomTarget(int i, int i2) {
        if (!Util.isValidDimensions(i, i2)) {
            throw new IllegalArgumentException("Width and height must both be > 0 or Target#SIZE_ORIGINAL, but given width: " + i + " and height: " + i2);
        }
        this.width = i;
        this.height = i2;
    }

    @Override
    public final void getSize(SizeReadyCallback sizeReadyCallback) {
        sizeReadyCallback.onSizeReady(this.width, this.height);
    }

    @Override
    public final void setRequest(Request request) {
        this.request = request;
    }

    @Override
    public final Request getRequest() {
        return this.request;
    }
}
