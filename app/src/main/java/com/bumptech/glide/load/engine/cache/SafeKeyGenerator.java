package com.bumptech.glide.load.engine.cache;

import androidx.core.util.Pools;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.util.LruCache;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import com.bumptech.glide.util.pool.FactoryPools;
import com.bumptech.glide.util.pool.StateVerifier;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SafeKeyGenerator {
    private final LruCache<Key, String> loadIdToSafeHash = new LruCache<>(1000);
    private final Pools.Pool<PoolableDigestContainer> digestPool = FactoryPools.threadSafe(10, new FactoryPools.Factory<PoolableDigestContainer>() {
        /* JADX WARN: Can't rename method to resolve collision */
        @Override
        public PoolableDigestContainer create() {
            try {
                return new PoolableDigestContainer(MessageDigest.getInstance("SHA-256"));
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
    });

    public String getSafeKey(Key key) {
        String strCalculateHexStringDigest;
        synchronized (this.loadIdToSafeHash) {
            strCalculateHexStringDigest = this.loadIdToSafeHash.get(key);
        }
        if (strCalculateHexStringDigest == null) {
            strCalculateHexStringDigest = calculateHexStringDigest(key);
        }
        synchronized (this.loadIdToSafeHash) {
            this.loadIdToSafeHash.put(key, strCalculateHexStringDigest);
        }
        return strCalculateHexStringDigest;
    }

    private String calculateHexStringDigest(Key key) {
        PoolableDigestContainer poolableDigestContainer = (PoolableDigestContainer) Preconditions.checkNotNull(this.digestPool.acquire());
        try {
            key.updateDiskCacheKey(poolableDigestContainer.messageDigest);
            return Util.sha256BytesToHex(poolableDigestContainer.messageDigest.digest());
        } finally {
            this.digestPool.release(poolableDigestContainer);
        }
    }

    private static final class PoolableDigestContainer implements FactoryPools.Poolable {
        final MessageDigest messageDigest;
        private final StateVerifier stateVerifier = StateVerifier.newInstance();

        PoolableDigestContainer(MessageDigest messageDigest) {
            this.messageDigest = messageDigest;
        }

        @Override
        public StateVerifier getVerifier() {
            return this.stateVerifier;
        }
    }
}
