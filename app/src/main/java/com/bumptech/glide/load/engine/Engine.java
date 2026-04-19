package com.bumptech.glide.load.engine;

import android.util.Log;
import androidx.core.util.Pools;
import com.bumptech.glide.GlideContext;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DecodeJob;
import com.bumptech.glide.load.engine.EngineResource;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskCacheAdapter;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.bumptech.glide.request.ResourceCallback;
import com.bumptech.glide.util.Executors;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.pool.FactoryPools;
import java.util.Map;
import java.util.concurrent.Executor;

public class Engine implements EngineJobListener, MemoryCache.ResourceRemovedListener, EngineResource.ResourceListener {
    private static final int JOB_POOL_SIZE = 150;
    private static final String TAG = "Engine";
    private static final boolean VERBOSE_IS_LOGGABLE = Log.isLoggable(TAG, 2);
    private final ActiveResources activeResources;
    private final MemoryCache cache;
    private final DecodeJobFactory decodeJobFactory;
    private final LazyDiskCacheProvider diskCacheProvider;
    private final EngineJobFactory engineJobFactory;
    private final Jobs jobs;
    private final EngineKeyFactory keyFactory;
    private final ResourceRecycler resourceRecycler;

    public Engine(MemoryCache memoryCache, DiskCache.Factory factory, GlideExecutor glideExecutor, GlideExecutor glideExecutor2, GlideExecutor glideExecutor3, GlideExecutor glideExecutor4, boolean z) {
        this(memoryCache, factory, glideExecutor, glideExecutor2, glideExecutor3, glideExecutor4, null, null, null, null, null, null, z);
    }

    Engine(MemoryCache memoryCache, DiskCache.Factory factory, GlideExecutor glideExecutor, GlideExecutor glideExecutor2, GlideExecutor glideExecutor3, GlideExecutor glideExecutor4, Jobs jobs, EngineKeyFactory engineKeyFactory, ActiveResources activeResources, EngineJobFactory engineJobFactory, DecodeJobFactory decodeJobFactory, ResourceRecycler resourceRecycler, boolean z) {
        this.cache = memoryCache;
        this.diskCacheProvider = new LazyDiskCacheProvider(factory);
        ActiveResources activeResources2 = activeResources == null ? new ActiveResources(z) : activeResources;
        this.activeResources = activeResources2;
        activeResources2.setListener(this);
        this.keyFactory = engineKeyFactory == null ? new EngineKeyFactory() : engineKeyFactory;
        this.jobs = jobs == null ? new Jobs() : jobs;
        this.engineJobFactory = engineJobFactory == null ? new EngineJobFactory(glideExecutor, glideExecutor2, glideExecutor3, glideExecutor4, this) : engineJobFactory;
        this.decodeJobFactory = decodeJobFactory == null ? new DecodeJobFactory(this.diskCacheProvider) : decodeJobFactory;
        this.resourceRecycler = resourceRecycler == null ? new ResourceRecycler() : resourceRecycler;
        memoryCache.setResourceRemovedListener(this);
    }

    public synchronized <R> LoadStatus load(GlideContext glideContext, Object obj, Key key, int i, int i2, Class<?> cls, Class<R> cls2, Priority priority, DiskCacheStrategy diskCacheStrategy, Map<Class<?>, Transformation<?>> map, boolean z, boolean z2, Options options, boolean z3, boolean z4, boolean z5, boolean z6, ResourceCallback resourceCallback, Executor executor) {
        long logTime = VERBOSE_IS_LOGGABLE ? LogTime.getLogTime() : 0L;
        EngineKey engineKeyBuildKey = this.keyFactory.buildKey(obj, key, i, i2, map, cls, cls2, options);
        EngineResource<?> engineResourceLoadFromActiveResources = loadFromActiveResources(engineKeyBuildKey, z3);
        if (engineResourceLoadFromActiveResources != null) {
            resourceCallback.onResourceReady(engineResourceLoadFromActiveResources, DataSource.MEMORY_CACHE);
            if (VERBOSE_IS_LOGGABLE) {
                logWithTimeAndKey("Loaded resource from active resources", logTime, engineKeyBuildKey);
            }
            return null;
        }
        EngineResource<?> engineResourceLoadFromCache = loadFromCache(engineKeyBuildKey, z3);
        if (engineResourceLoadFromCache != null) {
            resourceCallback.onResourceReady(engineResourceLoadFromCache, DataSource.MEMORY_CACHE);
            if (VERBOSE_IS_LOGGABLE) {
                logWithTimeAndKey("Loaded resource from cache", logTime, engineKeyBuildKey);
            }
            return null;
        }
        EngineJob<?> engineJob = this.jobs.get(engineKeyBuildKey, z6);
        if (engineJob != null) {
            engineJob.addCallback(resourceCallback, executor);
            if (VERBOSE_IS_LOGGABLE) {
                logWithTimeAndKey("Added to existing load", logTime, engineKeyBuildKey);
            }
            return new LoadStatus(resourceCallback, engineJob);
        }
        EngineJob<R> engineJobBuild = this.engineJobFactory.build(engineKeyBuildKey, z3, z4, z5, z6);
        DecodeJob<R> decodeJobBuild = this.decodeJobFactory.build(glideContext, obj, engineKeyBuildKey, key, i, i2, cls, cls2, priority, diskCacheStrategy, map, z, z2, z6, options, engineJobBuild);
        this.jobs.put(engineKeyBuildKey, engineJobBuild);
        engineJobBuild.addCallback(resourceCallback, executor);
        engineJobBuild.start(decodeJobBuild);
        if (VERBOSE_IS_LOGGABLE) {
            logWithTimeAndKey("Started new load", logTime, engineKeyBuildKey);
        }
        return new LoadStatus(resourceCallback, engineJobBuild);
    }

    private static void logWithTimeAndKey(String str, long j, Key key) {
        Log.v(TAG, str + " in " + LogTime.getElapsedMillis(j) + "ms, key: " + key);
    }

    private EngineResource<?> loadFromActiveResources(Key key, boolean z) {
        if (!z) {
            return null;
        }
        EngineResource<?> engineResource = this.activeResources.get(key);
        if (engineResource != null) {
            engineResource.acquire();
        }
        return engineResource;
    }

    private EngineResource<?> loadFromCache(Key key, boolean z) {
        if (!z) {
            return null;
        }
        EngineResource<?> engineResourceFromCache = getEngineResourceFromCache(key);
        if (engineResourceFromCache != null) {
            engineResourceFromCache.acquire();
            this.activeResources.activate(key, engineResourceFromCache);
        }
        return engineResourceFromCache;
    }

    private EngineResource<?> getEngineResourceFromCache(Key key) {
        Resource<?> resourceRemove = this.cache.remove(key);
        if (resourceRemove == null) {
            return null;
        }
        if (resourceRemove instanceof EngineResource) {
            return (EngineResource) resourceRemove;
        }
        return new EngineResource<>(resourceRemove, true, true);
    }

    public void release(Resource<?> resource) {
        if (resource instanceof EngineResource) {
            ((EngineResource) resource).release();
            return;
        }
        throw new IllegalArgumentException("Cannot release anything but an EngineResource");
    }

    @Override
    public synchronized void onEngineJobComplete(EngineJob<?> engineJob, Key key, EngineResource<?> engineResource) {
        if (engineResource != null) {
            engineResource.setResourceListener(key, this);
            if (engineResource.isCacheable()) {
                this.activeResources.activate(key, engineResource);
            }
            this.jobs.removeIfCurrent(key, engineJob);
        } else {
            this.jobs.removeIfCurrent(key, engineJob);
        }
    }

    @Override
    public synchronized void onEngineJobCancelled(EngineJob<?> engineJob, Key key) {
        this.jobs.removeIfCurrent(key, engineJob);
    }

    @Override
    public void onResourceRemoved(Resource<?> resource) {
        this.resourceRecycler.recycle(resource);
    }

    @Override
    public synchronized void onResourceReleased(Key key, EngineResource<?> engineResource) {
        this.activeResources.deactivate(key);
        if (engineResource.isCacheable()) {
            this.cache.put(key, engineResource);
        } else {
            this.resourceRecycler.recycle(engineResource);
        }
    }

    public void clearDiskCache() {
        this.diskCacheProvider.getDiskCache().clear();
    }

    public void shutdown() {
        this.engineJobFactory.shutdown();
        this.diskCacheProvider.clearDiskCacheIfCreated();
        this.activeResources.shutdown();
    }

    public class LoadStatus {

        private final ResourceCallback f49cb;
        private final EngineJob<?> engineJob;

        LoadStatus(ResourceCallback resourceCallback, EngineJob<?> engineJob) {
            this.f49cb = resourceCallback;
            this.engineJob = engineJob;
        }

        public void cancel() {
            synchronized (Engine.this) {
                this.engineJob.removeCallback(this.f49cb);
            }
        }
    }

    private static class LazyDiskCacheProvider implements DecodeJob.DiskCacheProvider {
        private volatile DiskCache diskCache;
        private final DiskCache.Factory factory;

        LazyDiskCacheProvider(DiskCache.Factory factory) {
            this.factory = factory;
        }

        synchronized void clearDiskCacheIfCreated() {
            if (this.diskCache == null) {
                return;
            }
            this.diskCache.clear();
        }

        @Override
        public DiskCache getDiskCache() {
            if (this.diskCache == null) {
                synchronized (this) {
                    if (this.diskCache == null) {
                        this.diskCache = this.factory.build();
                    }
                    if (this.diskCache == null) {
                        this.diskCache = new DiskCacheAdapter();
                    }
                }
            }
            return this.diskCache;
        }
    }

    static class DecodeJobFactory {
        private int creationOrder;
        final DecodeJob.DiskCacheProvider diskCacheProvider;
        final Pools.Pool<DecodeJob<?>> pool = FactoryPools.threadSafe(Engine.JOB_POOL_SIZE, new FactoryPools.Factory<DecodeJob<?>>() {
            /* JADX WARN: Can't rename method to resolve collision */
            @Override
            public DecodeJob<?> create() {
                return new DecodeJob<>(DecodeJobFactory.this.diskCacheProvider, DecodeJobFactory.this.pool);
            }
        });

        DecodeJobFactory(DecodeJob.DiskCacheProvider diskCacheProvider) {
            this.diskCacheProvider = diskCacheProvider;
        }

        <R> DecodeJob<R> build(GlideContext glideContext, Object obj, EngineKey engineKey, Key key, int i, int i2, Class<?> cls, Class<R> cls2, Priority priority, DiskCacheStrategy diskCacheStrategy, Map<Class<?>, Transformation<?>> map, boolean z, boolean z2, boolean z3, Options options, DecodeJob.Callback<R> callback) {
            DecodeJob decodeJob = (DecodeJob) Preconditions.checkNotNull(this.pool.acquire());
            int i3 = this.creationOrder;
            this.creationOrder = i3 + 1;
            return decodeJob.init(glideContext, obj, engineKey, key, i, i2, cls, cls2, priority, diskCacheStrategy, map, z, z2, z3, options, callback, i3);
        }
    }

    static class EngineJobFactory {
        final GlideExecutor animationExecutor;
        final GlideExecutor diskCacheExecutor;
        final EngineJobListener listener;
        final Pools.Pool<EngineJob<?>> pool = FactoryPools.threadSafe(Engine.JOB_POOL_SIZE, new FactoryPools.Factory<EngineJob<?>>() {
            /* JADX WARN: Can't rename method to resolve collision */
            @Override
            public EngineJob<?> create() {
                return new EngineJob<>(EngineJobFactory.this.diskCacheExecutor, EngineJobFactory.this.sourceExecutor, EngineJobFactory.this.sourceUnlimitedExecutor, EngineJobFactory.this.animationExecutor, EngineJobFactory.this.listener, EngineJobFactory.this.pool);
            }
        });
        final GlideExecutor sourceExecutor;
        final GlideExecutor sourceUnlimitedExecutor;

        EngineJobFactory(GlideExecutor glideExecutor, GlideExecutor glideExecutor2, GlideExecutor glideExecutor3, GlideExecutor glideExecutor4, EngineJobListener engineJobListener) {
            this.diskCacheExecutor = glideExecutor;
            this.sourceExecutor = glideExecutor2;
            this.sourceUnlimitedExecutor = glideExecutor3;
            this.animationExecutor = glideExecutor4;
            this.listener = engineJobListener;
        }

        void shutdown() {
            Executors.shutdownAndAwaitTermination(this.diskCacheExecutor);
            Executors.shutdownAndAwaitTermination(this.sourceExecutor);
            Executors.shutdownAndAwaitTermination(this.sourceUnlimitedExecutor);
            Executors.shutdownAndAwaitTermination(this.animationExecutor);
        }

        <R> EngineJob<R> build(Key key, boolean z, boolean z2, boolean z3, boolean z4) {
            return ((EngineJob) Preconditions.checkNotNull(this.pool.acquire())).init(key, z, z2, z3, z4);
        }
    }
}
