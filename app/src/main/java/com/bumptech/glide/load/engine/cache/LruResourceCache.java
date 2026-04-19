package com.bumptech.glide.load.engine.cache;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.util.LruCache;

public class LruResourceCache extends LruCache<Key, Resource<?>> implements MemoryCache {
    private MemoryCache.ResourceRemovedListener listener;

    @Override
    public Resource put(Key key, Resource resource) {
        return (Resource) super.put(key, resource);
    }

    @Override
    public Resource remove(Key key) {
        return (Resource) super.remove(key);
    }

    public LruResourceCache(long j) {
        super(j);
    }

    @Override
    public void setResourceRemovedListener(MemoryCache.ResourceRemovedListener resourceRemovedListener) {
        this.listener = resourceRemovedListener;
    }

    @Override
    public void onItemEvicted(Key key, Resource<?> resource) {
        MemoryCache.ResourceRemovedListener resourceRemovedListener = this.listener;
        if (resourceRemovedListener == null || resource == null) {
            return;
        }
        resourceRemovedListener.onResourceRemoved(resource);
    }

    @Override
    public int getSize(Resource<?> resource) {
        if (resource == null) {
            return super.getSize((Object) null);
        }
        return resource.getSize();
    }

    @Override
    public void trimMemory(int i) {
        if (i >= 40) {
            clearMemory();
        } else if (i >= 20 || i == 15) {
            trimToSize(getMaxSize() / 2);
        }
    }
}
