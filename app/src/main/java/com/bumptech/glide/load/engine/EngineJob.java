package com.bumptech.glide.load.engine;

import androidx.core.util.Pools;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.DecodeJob;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.bumptech.glide.request.ResourceCallback;
import com.bumptech.glide.util.Executors;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.pool.FactoryPools;
import com.bumptech.glide.util.pool.StateVerifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

class EngineJob<R> implements DecodeJob.Callback<R>, FactoryPools.Poolable {
    private static final EngineResourceFactory DEFAULT_FACTORY = new EngineResourceFactory();
    private final GlideExecutor animationExecutor;
    final ResourceCallbacksAndExecutors cbs;
    DataSource dataSource;
    private DecodeJob<R> decodeJob;
    private final GlideExecutor diskCacheExecutor;
    EngineResource<?> engineResource;
    private final EngineResourceFactory engineResourceFactory;
    GlideException exception;
    private boolean hasLoadFailed;
    private boolean hasResource;
    private boolean isCacheable;
    private volatile boolean isCancelled;
    private Key key;
    private final EngineJobListener listener;
    private boolean onlyRetrieveFromCache;
    private final AtomicInteger pendingCallbacks;
    private final Pools.Pool<EngineJob<?>> pool;
    private Resource<?> resource;
    private final GlideExecutor sourceExecutor;
    private final GlideExecutor sourceUnlimitedExecutor;
    private final StateVerifier stateVerifier;
    private boolean useAnimationPool;
    private boolean useUnlimitedSourceGeneratorPool;

    EngineJob(GlideExecutor glideExecutor, GlideExecutor glideExecutor2, GlideExecutor glideExecutor3, GlideExecutor glideExecutor4, EngineJobListener engineJobListener, Pools.Pool<EngineJob<?>> pool) {
        this(glideExecutor, glideExecutor2, glideExecutor3, glideExecutor4, engineJobListener, pool, DEFAULT_FACTORY);
    }

    EngineJob(GlideExecutor glideExecutor, GlideExecutor glideExecutor2, GlideExecutor glideExecutor3, GlideExecutor glideExecutor4, EngineJobListener engineJobListener, Pools.Pool<EngineJob<?>> pool, EngineResourceFactory engineResourceFactory) {
        this.cbs = new ResourceCallbacksAndExecutors();
        this.stateVerifier = StateVerifier.newInstance();
        this.pendingCallbacks = new AtomicInteger();
        this.diskCacheExecutor = glideExecutor;
        this.sourceExecutor = glideExecutor2;
        this.sourceUnlimitedExecutor = glideExecutor3;
        this.animationExecutor = glideExecutor4;
        this.listener = engineJobListener;
        this.pool = pool;
        this.engineResourceFactory = engineResourceFactory;
    }

    synchronized EngineJob<R> init(Key key, boolean z, boolean z2, boolean z3, boolean z4) {
        this.key = key;
        this.isCacheable = z;
        this.useUnlimitedSourceGeneratorPool = z2;
        this.useAnimationPool = z3;
        this.onlyRetrieveFromCache = z4;
        return this;
    }

    public synchronized void start(DecodeJob<R> decodeJob) {
        GlideExecutor activeSourceExecutor;
        this.decodeJob = decodeJob;
        if (decodeJob.willDecodeFromCache()) {
            activeSourceExecutor = this.diskCacheExecutor;
        } else {
            activeSourceExecutor = getActiveSourceExecutor();
        }
        activeSourceExecutor.execute(decodeJob);
    }

    synchronized void addCallback(ResourceCallback resourceCallback, Executor executor) {
        this.stateVerifier.throwIfRecycled();
        this.cbs.add(resourceCallback, executor);
        boolean z = true;
        if (this.hasResource) {
            incrementPendingCallbacks(1);
            executor.execute(new CallResourceReady(resourceCallback));
        } else if (this.hasLoadFailed) {
            incrementPendingCallbacks(1);
            executor.execute(new CallLoadFailed(resourceCallback));
        } else {
            if (this.isCancelled) {
                z = false;
            }
            Preconditions.checkArgument(z, "Cannot add callbacks to a cancelled EngineJob");
        }
    }

    synchronized void callCallbackOnResourceReady(ResourceCallback resourceCallback) {
        try {
            resourceCallback.onResourceReady(this.engineResource, this.dataSource);
        } catch (Throwable th) {
            throw new CallbackException(th);
        }
    }

    synchronized void callCallbackOnLoadFailed(ResourceCallback resourceCallback) {
        try {
            resourceCallback.onLoadFailed(this.exception);
        } catch (Throwable th) {
            throw new CallbackException(th);
        }
    }

    synchronized void removeCallback(ResourceCallback resourceCallback) {
        this.stateVerifier.throwIfRecycled();
        this.cbs.remove(resourceCallback);
        if (this.cbs.isEmpty()) {
            cancel();
            if ((this.hasResource || this.hasLoadFailed) && this.pendingCallbacks.get() == 0) {
                release();
            }
        }
    }

    boolean onlyRetrieveFromCache() {
        return this.onlyRetrieveFromCache;
    }

    private GlideExecutor getActiveSourceExecutor() {
        if (this.useUnlimitedSourceGeneratorPool) {
            return this.sourceUnlimitedExecutor;
        }
        return this.useAnimationPool ? this.animationExecutor : this.sourceExecutor;
    }

    void cancel() {
        if (isDone()) {
            return;
        }
        this.isCancelled = true;
        this.decodeJob.cancel();
        this.listener.onEngineJobCancelled(this, this.key);
    }

    synchronized boolean isCancelled() {
        return this.isCancelled;
    }

    private boolean isDone() {
        return this.hasLoadFailed || this.hasResource || this.isCancelled;
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    void notifyCallbacksOfResult() {
        synchronized (this) {
            this.stateVerifier.throwIfRecycled();
            if (this.isCancelled) {
                this.resource.recycle();
                release();
                return;
            }
            if (this.cbs.isEmpty()) {
                throw new IllegalStateException("Received a resource without any callbacks to notify");
            }
            if (this.hasResource) {
                throw new IllegalStateException("Already have resource");
            }
            this.engineResource = this.engineResourceFactory.build(this.resource, this.isCacheable);
            this.hasResource = true;
            ResourceCallbacksAndExecutors resourceCallbacksAndExecutorsCopy = this.cbs.copy();
            incrementPendingCallbacks(resourceCallbacksAndExecutorsCopy.size() + 1);
            this.listener.onEngineJobComplete(this, this.key, this.engineResource);
            for (ResourceCallbackAndExecutor resourceCallbackAndExecutor : resourceCallbacksAndExecutorsCopy) {
                resourceCallbackAndExecutor.executor.execute(new CallResourceReady(resourceCallbackAndExecutor.f52cb));
            }
            decrementPendingCallbacks();
        }
    }

    synchronized void incrementPendingCallbacks(int i) {
        Preconditions.checkArgument(isDone(), "Not yet complete!");
        if (this.pendingCallbacks.getAndAdd(i) == 0 && this.engineResource != null) {
            this.engineResource.acquire();
        }
    }

    synchronized void decrementPendingCallbacks() {
        this.stateVerifier.throwIfRecycled();
        Preconditions.checkArgument(isDone(), "Not yet complete!");
        int iDecrementAndGet = this.pendingCallbacks.decrementAndGet();
        Preconditions.checkArgument(iDecrementAndGet >= 0, "Can't decrement below 0");
        if (iDecrementAndGet == 0) {
            if (this.engineResource != null) {
                this.engineResource.release();
            }
            release();
        }
    }

    private synchronized void release() {
        if (this.key == null) {
            throw new IllegalArgumentException();
        }
        this.cbs.clear();
        this.key = null;
        this.engineResource = null;
        this.resource = null;
        this.hasLoadFailed = false;
        this.isCancelled = false;
        this.hasResource = false;
        this.decodeJob.release(false);
        this.decodeJob = null;
        this.exception = null;
        this.dataSource = null;
        this.pool.release(this);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override
    public void onResourceReady(Resource<R> resource, DataSource dataSource) {
        synchronized (this) {
            this.resource = resource;
            this.dataSource = dataSource;
        }
        notifyCallbacksOfResult();
    }

    @Override
    public void onLoadFailed(GlideException glideException) {
        synchronized (this) {
            this.exception = glideException;
        }
        notifyCallbacksOfException();
    }

    @Override
    public void reschedule(DecodeJob<?> decodeJob) {
        getActiveSourceExecutor().execute(decodeJob);
    }

    void notifyCallbacksOfException() {
        synchronized (this) {
            this.stateVerifier.throwIfRecycled();
            if (this.isCancelled) {
                release();
                return;
            }
            if (this.cbs.isEmpty()) {
                throw new IllegalStateException("Received an exception without any callbacks to notify");
            }
            if (this.hasLoadFailed) {
                throw new IllegalStateException("Already failed once");
            }
            this.hasLoadFailed = true;
            Key key = this.key;
            ResourceCallbacksAndExecutors resourceCallbacksAndExecutorsCopy = this.cbs.copy();
            incrementPendingCallbacks(resourceCallbacksAndExecutorsCopy.size() + 1);
            this.listener.onEngineJobComplete(this, key, null);
            for (ResourceCallbackAndExecutor resourceCallbackAndExecutor : resourceCallbacksAndExecutorsCopy) {
                resourceCallbackAndExecutor.executor.execute(new CallLoadFailed(resourceCallbackAndExecutor.f52cb));
            }
            decrementPendingCallbacks();
        }
    }

    @Override
    public StateVerifier getVerifier() {
        return this.stateVerifier;
    }

    private class CallLoadFailed implements Runnable {

        private final ResourceCallback f50cb;

        CallLoadFailed(ResourceCallback resourceCallback) {
            this.f50cb = resourceCallback;
        }

        @Override
        public void run() {
            synchronized (EngineJob.this) {
                if (EngineJob.this.cbs.contains(this.f50cb)) {
                    EngineJob.this.callCallbackOnLoadFailed(this.f50cb);
                }
                EngineJob.this.decrementPendingCallbacks();
            }
        }
    }

    private class CallResourceReady implements Runnable {

        private final ResourceCallback f51cb;

        CallResourceReady(ResourceCallback resourceCallback) {
            this.f51cb = resourceCallback;
        }

        @Override
        public void run() {
            synchronized (EngineJob.this) {
                if (EngineJob.this.cbs.contains(this.f51cb)) {
                    EngineJob.this.engineResource.acquire();
                    EngineJob.this.callCallbackOnResourceReady(this.f51cb);
                    EngineJob.this.removeCallback(this.f51cb);
                }
                EngineJob.this.decrementPendingCallbacks();
            }
        }
    }

    static final class ResourceCallbacksAndExecutors implements Iterable<ResourceCallbackAndExecutor> {
        private final List<ResourceCallbackAndExecutor> callbacksAndExecutors;

        ResourceCallbacksAndExecutors() {
            this(new ArrayList(2));
        }

        ResourceCallbacksAndExecutors(List<ResourceCallbackAndExecutor> list) {
            this.callbacksAndExecutors = list;
        }

        void add(ResourceCallback resourceCallback, Executor executor) {
            this.callbacksAndExecutors.add(new ResourceCallbackAndExecutor(resourceCallback, executor));
        }

        void remove(ResourceCallback resourceCallback) {
            this.callbacksAndExecutors.remove(defaultCallbackAndExecutor(resourceCallback));
        }

        boolean contains(ResourceCallback resourceCallback) {
            return this.callbacksAndExecutors.contains(defaultCallbackAndExecutor(resourceCallback));
        }

        boolean isEmpty() {
            return this.callbacksAndExecutors.isEmpty();
        }

        int size() {
            return this.callbacksAndExecutors.size();
        }

        void clear() {
            this.callbacksAndExecutors.clear();
        }

        ResourceCallbacksAndExecutors copy() {
            return new ResourceCallbacksAndExecutors(new ArrayList(this.callbacksAndExecutors));
        }

        private static ResourceCallbackAndExecutor defaultCallbackAndExecutor(ResourceCallback resourceCallback) {
            return new ResourceCallbackAndExecutor(resourceCallback, Executors.directExecutor());
        }

        @Override
        public Iterator<ResourceCallbackAndExecutor> iterator() {
            return this.callbacksAndExecutors.iterator();
        }
    }

    static final class ResourceCallbackAndExecutor {

        final ResourceCallback f52cb;
        final Executor executor;

        ResourceCallbackAndExecutor(ResourceCallback resourceCallback, Executor executor) {
            this.f52cb = resourceCallback;
            this.executor = executor;
        }

        public boolean equals(Object obj) {
            if (obj instanceof ResourceCallbackAndExecutor) {
                return this.f52cb.equals(((ResourceCallbackAndExecutor) obj).f52cb);
            }
            return false;
        }

        public int hashCode() {
            return this.f52cb.hashCode();
        }
    }

    static class EngineResourceFactory {
        EngineResourceFactory() {
        }

        public <R> EngineResource<R> build(Resource<R> resource, boolean z) {
            return new EngineResource<>(resource, z, true);
        }
    }
}
