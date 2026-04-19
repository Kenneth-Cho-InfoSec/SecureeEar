package com.bumptech.glide.load.engine.cache;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.cache.MemoryCache;

public class MemoryCacheAdapter implements MemoryCache {
    private MemoryCache.ResourceRemovedListener listener;

    @Override
    public void clearMemory() {
    }

    @Override
    public long getCurrentSize() {
        return 0L;
    }

    @Override
    public long getMaxSize() {
        return 0L;
    }

    @Override
    public Resource<?> remove(Key key) {
        return null;
    }

    @Override
    public void setSizeMultiplier(float f) {
    }

    @Override
    public void trimMemory(int i) {
    }

    @Override
    public Resource<?> put(Key key, Resource<?> resource) {
        if (resource == null) {
            return null;
        }
        this.listener.onResourceRemoved(resource);
        return null;
    }

    @Override
    public void setResourceRemovedListener(MemoryCache.ResourceRemovedListener resourceRemovedListener) {
        this.listener = resourceRemovedListener;
    }
}
