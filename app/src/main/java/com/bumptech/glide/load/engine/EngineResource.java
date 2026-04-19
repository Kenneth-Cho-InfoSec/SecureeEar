package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.util.Preconditions;

class EngineResource<Z> implements Resource<Z> {
    private int acquired;
    private final boolean isCacheable;
    private final boolean isRecyclable;
    private boolean isRecycled;
    private Key key;
    private ResourceListener listener;
    private final Resource<Z> resource;

    interface ResourceListener {
        void onResourceReleased(Key key, EngineResource<?> engineResource);
    }

    EngineResource(Resource<Z> resource, boolean z, boolean z2) {
        this.resource = (Resource) Preconditions.checkNotNull(resource);
        this.isCacheable = z;
        this.isRecyclable = z2;
    }

    synchronized void setResourceListener(Key key, ResourceListener resourceListener) {
        this.key = key;
        this.listener = resourceListener;
    }

    Resource<Z> getResource() {
        return this.resource;
    }

    boolean isCacheable() {
        return this.isCacheable;
    }

    @Override
    public Class<Z> getResourceClass() {
        return this.resource.getResourceClass();
    }

    @Override
    public Z get() {
        return this.resource.get();
    }

    @Override
    public int getSize() {
        return this.resource.getSize();
    }

    @Override
    public synchronized void recycle() {
        if (this.acquired > 0) {
            throw new IllegalStateException("Cannot recycle a resource while it is still acquired");
        }
        if (this.isRecycled) {
            throw new IllegalStateException("Cannot recycle a resource that has already been recycled");
        }
        this.isRecycled = true;
        if (this.isRecyclable) {
            this.resource.recycle();
        }
    }

    synchronized void acquire() {
        if (this.isRecycled) {
            throw new IllegalStateException("Cannot acquire a recycled resource");
        }
        this.acquired++;
    }

    void release() {
        synchronized (this.listener) {
            synchronized (this) {
                if (this.acquired <= 0) {
                    throw new IllegalStateException("Cannot release a recycled or not yet acquired resource");
                }
                int i = this.acquired - 1;
                this.acquired = i;
                if (i == 0) {
                    this.listener.onResourceReleased(this.key, this);
                }
            }
        }
    }

    public synchronized String toString() {
        return "EngineResource{isCacheable=" + this.isCacheable + ", listener=" + this.listener + ", key=" + this.key + ", acquired=" + this.acquired + ", isRecycled=" + this.isRecycled + ", resource=" + this.resource + '}';
    }
}
