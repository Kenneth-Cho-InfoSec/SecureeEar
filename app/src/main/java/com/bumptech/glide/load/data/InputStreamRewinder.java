package com.bumptech.glide.load.data;

import com.bumptech.glide.load.data.DataRewinder;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.resource.bitmap.RecyclableBufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class InputStreamRewinder implements DataRewinder<InputStream> {
    private static final int MARK_LIMIT = 5242880;
    private final RecyclableBufferedInputStream bufferedStream;

    InputStreamRewinder(InputStream inputStream, ArrayPool arrayPool) {
        this.bufferedStream = new RecyclableBufferedInputStream(inputStream, arrayPool);
        this.bufferedStream.mark(MARK_LIMIT);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override
    public InputStream rewindAndGet() throws IOException {
        this.bufferedStream.reset();
        return this.bufferedStream;
    }

    @Override
    public void cleanup() {
        this.bufferedStream.release();
    }

    public static final class Factory implements DataRewinder.Factory<InputStream> {
        private final ArrayPool byteArrayPool;

        public Factory(ArrayPool arrayPool) {
            this.byteArrayPool = arrayPool;
        }

        @Override
        public DataRewinder<InputStream> build(InputStream inputStream) {
            return new InputStreamRewinder(inputStream, this.byteArrayPool);
        }

        @Override
        public Class<InputStream> getDataClass() {
            return InputStream.class;
        }
    }
}
