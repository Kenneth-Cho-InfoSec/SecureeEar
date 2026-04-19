package com.bumptech.glide.request.target;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.bumptech.glide.request.transition.Transition;

public abstract class ImageViewTarget<Z> extends ViewTarget<ImageView, Z> implements Transition.ViewAdapter {
    private Animatable animatable;

    protected abstract void setResource(Z z);

    public ImageViewTarget(ImageView imageView) {
        super(imageView);
    }

    @Deprecated
    public ImageViewTarget(ImageView imageView, boolean z) {
        super(imageView, z);
    }

    @Override
    public Drawable getCurrentDrawable() {
        return ((ImageView) this.view).getDrawable();
    }

    @Override
    public void setDrawable(Drawable drawable) {
        ((ImageView) this.view).setImageDrawable(drawable);
    }

    @Override
    public void onLoadStarted(Drawable drawable) {
        super.onLoadStarted(drawable);
        setResourceInternal(null);
        setDrawable(drawable);
    }

    @Override
    public void onLoadFailed(Drawable drawable) {
        super.onLoadFailed(drawable);
        setResourceInternal(null);
        setDrawable(drawable);
    }

    @Override
    public void onLoadCleared(Drawable drawable) {
        super.onLoadCleared(drawable);
        Animatable animatable = this.animatable;
        if (animatable != null) {
            animatable.stop();
        }
        setResourceInternal(null);
        setDrawable(drawable);
    }

    @Override
    public void onResourceReady(Z z, Transition<? super Z> transition) {
        if (transition == null || !transition.transition(z, this)) {
            setResourceInternal(z);
        } else {
            maybeUpdateAnimatable(z);
        }
    }

    @Override
    public void onStart() {
        Animatable animatable = this.animatable;
        if (animatable != null) {
            animatable.start();
        }
    }

    @Override
    public void onStop() {
        Animatable animatable = this.animatable;
        if (animatable != null) {
            animatable.stop();
        }
    }

    private void setResourceInternal(Z z) {
        setResource(z);
        maybeUpdateAnimatable(z);
    }

    private void maybeUpdateAnimatable(Z z) {
        if (z instanceof Animatable) {
            this.animatable = (Animatable) z;
            this.animatable.start();
        } else {
            this.animatable = null;
        }
    }
}
