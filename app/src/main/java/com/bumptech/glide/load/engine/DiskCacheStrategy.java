package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.EncodeStrategy;

public abstract class DiskCacheStrategy {
    public static final DiskCacheStrategy ALL = new DiskCacheStrategy() {
        @Override
        public boolean decodeCachedData() {
            return true;
        }

        @Override
        public boolean decodeCachedResource() {
            return true;
        }

        @Override
        public boolean isDataCacheable(DataSource dataSource) {
            return dataSource == DataSource.REMOTE;
        }

        @Override
        public boolean isResourceCacheable(boolean z, DataSource dataSource, EncodeStrategy encodeStrategy) {
            return (dataSource == DataSource.RESOURCE_DISK_CACHE || dataSource == DataSource.MEMORY_CACHE) ? false : true;
        }
    };
    public static final DiskCacheStrategy NONE = new DiskCacheStrategy() {
        @Override
        public boolean decodeCachedData() {
            return false;
        }

        @Override
        public boolean decodeCachedResource() {
            return false;
        }

        @Override
        public boolean isDataCacheable(DataSource dataSource) {
            return false;
        }

        @Override
        public boolean isResourceCacheable(boolean z, DataSource dataSource, EncodeStrategy encodeStrategy) {
            return false;
        }
    };
    public static final DiskCacheStrategy DATA = new DiskCacheStrategy() {
        @Override
        public boolean decodeCachedData() {
            return true;
        }

        @Override
        public boolean decodeCachedResource() {
            return false;
        }

        @Override
        public boolean isResourceCacheable(boolean z, DataSource dataSource, EncodeStrategy encodeStrategy) {
            return false;
        }

        @Override
        public boolean isDataCacheable(DataSource dataSource) {
            return (dataSource == DataSource.DATA_DISK_CACHE || dataSource == DataSource.MEMORY_CACHE) ? false : true;
        }
    };
    public static final DiskCacheStrategy RESOURCE = new DiskCacheStrategy() {
        @Override
        public boolean decodeCachedData() {
            return false;
        }

        @Override
        public boolean decodeCachedResource() {
            return true;
        }

        @Override
        public boolean isDataCacheable(DataSource dataSource) {
            return false;
        }

        @Override
        public boolean isResourceCacheable(boolean z, DataSource dataSource, EncodeStrategy encodeStrategy) {
            return (dataSource == DataSource.RESOURCE_DISK_CACHE || dataSource == DataSource.MEMORY_CACHE) ? false : true;
        }
    };
    public static final DiskCacheStrategy AUTOMATIC = new DiskCacheStrategy() {
        @Override
        public boolean decodeCachedData() {
            return true;
        }

        @Override
        public boolean decodeCachedResource() {
            return true;
        }

        @Override
        public boolean isDataCacheable(DataSource dataSource) {
            return dataSource == DataSource.REMOTE;
        }

        @Override
        public boolean isResourceCacheable(boolean z, DataSource dataSource, EncodeStrategy encodeStrategy) {
            return ((z && dataSource == DataSource.DATA_DISK_CACHE) || dataSource == DataSource.LOCAL) && encodeStrategy == EncodeStrategy.TRANSFORMED;
        }
    };

    public abstract boolean decodeCachedData();

    public abstract boolean decodeCachedResource();

    public abstract boolean isDataCacheable(DataSource dataSource);

    public abstract boolean isResourceCacheable(boolean z, DataSource dataSource, EncodeStrategy encodeStrategy);
}
