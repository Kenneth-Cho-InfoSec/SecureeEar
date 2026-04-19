package com.bumptech.glide.load.resource;

import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.Preconditions;

public class SimpleResource<T> implements Resource<T> {
    protected final T data;

    @Override
    public final int getSize() {
        return 1;
    }

    @Override
    public void recycle() {
    }

    public SimpleResource(T t) {
        this.data = (T) Preconditions.checkNotNull(t);
    }

    @Override
    public Class<T> getResourceClass() {
        return (Class<T>) this.data.getClass();
    }

    @Override
    public final T get() {
        return this.data;
    }
}
