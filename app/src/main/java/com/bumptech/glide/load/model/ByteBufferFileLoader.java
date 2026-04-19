package com.bumptech.glide.load.model;

import android.util.Log;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.signature.ObjectKey;
import com.bumptech.glide.util.ByteBufferUtil;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ByteBufferFileLoader implements ModelLoader<File, ByteBuffer> {
    private static final String TAG = "ByteBufferFileLoader";

    @Override
    public boolean handles(File file) {
        return true;
    }

    @Override
    public ModelLoader.LoadData<ByteBuffer> buildLoadData(File file, int i, int i2, Options options) {
        return new ModelLoader.LoadData<>(new ObjectKey(file), new ByteBufferFetcher(file));
    }

    public static class Factory implements ModelLoaderFactory<File, ByteBuffer> {
        @Override
        public void teardown() {
        }

        @Override
        public ModelLoader<File, ByteBuffer> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new ByteBufferFileLoader();
        }
    }

    private static final class ByteBufferFetcher implements DataFetcher<ByteBuffer> {
        private final File file;

        @Override
        public void cancel() {
        }

        @Override
        public void cleanup() {
        }

        ByteBufferFetcher(File file) {
            this.file = file;
        }

        @Override
        public void loadData(Priority priority, DataFetcher.DataCallback<? super ByteBuffer> dataCallback) {
            try {
                dataCallback.onDataReady(ByteBufferUtil.fromFile(this.file));
            } catch (IOException e) {
                if (Log.isLoggable(ByteBufferFileLoader.TAG, 3)) {
                    Log.d(ByteBufferFileLoader.TAG, "Failed to obtain ByteBuffer for file", e);
                }
                dataCallback.onLoadFailed(e);
            }
        }

        @Override
        public Class<ByteBuffer> getDataClass() {
            return ByteBuffer.class;
        }

        @Override
        public DataSource getDataSource() {
            return DataSource.LOCAL;
        }
    }
}
