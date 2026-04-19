package com.bumptech.glide.request.target;

import android.graphics.drawable.Drawable;
import com.bumptech.glide.request.Request;

@Deprecated
public abstract class BaseTarget<Z> implements Target<Z> {
    private Request request;

    @Override
    public void onDestroy() {
    }

    @Override
    public void onLoadCleared(Drawable drawable) {
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
    public void setRequest(Request request) {
        this.request = request;
    }

    @Override
    public Request getRequest() {
        return this.request;
    }
}
