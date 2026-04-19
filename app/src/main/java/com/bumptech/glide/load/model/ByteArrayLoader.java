package com.bumptech.glide.load.model;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.signature.ObjectKey;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ByteArrayLoader<Data> implements ModelLoader<byte[], Data> {
    private final Converter<Data> converter;

    public interface Converter<Data> {
        Data convert(byte[] bArr);

        Class<Data> getDataClass();
    }

    @Override
    public boolean handles(byte[] bArr) {
        return true;
    }

    public ByteArrayLoader(Converter<Data> converter) {
        this.converter = converter;
    }

    @Override
    public ModelLoader.LoadData<Data> buildLoadData(byte[] bArr, int i, int i2, Options options) {
        return new ModelLoader.LoadData<>(new ObjectKey(bArr), new Fetcher(bArr, this.converter));
    }

    private static class Fetcher<Data> implements DataFetcher<Data> {
        private final Converter<Data> converter;
        private final byte[] model;

        @Override
        public void cancel() {
        }

        @Override
        public void cleanup() {
        }

        Fetcher(byte[] bArr, Converter<Data> converter) {
            this.model = bArr;
            this.converter = converter;
        }

        @Override
        public void loadData(Priority priority, DataFetcher.DataCallback<? super Data> dataCallback) {
            dataCallback.onDataReady(this.converter.convert(this.model));
        }

        @Override
        public Class<Data> getDataClass() {
            return this.converter.getDataClass();
        }

        @Override
        public DataSource getDataSource() {
            return DataSource.LOCAL;
        }
    }

    public static class ByteBufferFactory implements ModelLoaderFactory<byte[], ByteBuffer> {
        @Override
        public void teardown() {
        }

        @Override
        public ModelLoader<byte[], ByteBuffer> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new ByteArrayLoader(new Converter<ByteBuffer>() {
                @Override
                public ByteBuffer convert(byte[] bArr) {
                    return ByteBuffer.wrap(bArr);
                }

                @Override
                public Class<ByteBuffer> getDataClass() {
                    return ByteBuffer.class;
                }
            });
        }
    }

    public static class StreamFactory implements ModelLoaderFactory<byte[], InputStream> {
        @Override
        public void teardown() {
        }

        @Override
        public ModelLoader<byte[], InputStream> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new ByteArrayLoader(new Converter<InputStream>() {
                /* JADX WARN: Can't rename method to resolve collision */
                @Override
                public InputStream convert(byte[] bArr) {
                    return new ByteArrayInputStream(bArr);
                }

                @Override
                public Class<InputStream> getDataClass() {
                    return InputStream.class;
                }
            });
        }
    }
}
