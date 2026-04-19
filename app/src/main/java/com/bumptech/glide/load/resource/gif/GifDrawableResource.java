package com.bumptech.glide.load.resource.gif;

import com.bumptech.glide.load.engine.Initializable;
import com.bumptech.glide.load.resource.drawable.DrawableResource;

public class GifDrawableResource extends DrawableResource<GifDrawable> implements Initializable {
    public GifDrawableResource(GifDrawable gifDrawable) {
        super(gifDrawable);
    }

    @Override
    public Class<GifDrawable> getResourceClass() {
        return GifDrawable.class;
    }

    @Override
    public int getSize() {
        return ((GifDrawable) this.drawable).getSize();
    }

    @Override
    public void recycle() {
        ((GifDrawable) this.drawable).stop();
        ((GifDrawable) this.drawable).recycle();
    }

    @Override
    public void initialize() {
        ((GifDrawable) this.drawable).getFirstFrame().prepareToDraw();
    }
}
