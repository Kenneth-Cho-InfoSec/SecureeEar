package com.bumptech.glide.load.resource.bytes;

import com.bumptech.glide.load.data.DataRewinder;
import java.nio.ByteBuffer;

public class ByteBufferRewinder implements DataRewinder<ByteBuffer> {
    private final ByteBuffer buffer;

    @Override
    public void cleanup() {
    }

    public ByteBufferRewinder(ByteBuffer byteBuffer) {
        this.buffer = byteBuffer;
    }

    @Override
    public ByteBuffer rewindAndGet() {
        this.buffer.position(0);
        return this.buffer;
    }

    public static class Factory implements DataRewinder.Factory<ByteBuffer> {
        @Override
        public DataRewinder<ByteBuffer> build(ByteBuffer byteBuffer) {
            return new ByteBufferRewinder(byteBuffer);
        }

        @Override
        public Class<ByteBuffer> getDataClass() {
            return ByteBuffer.class;
        }
    }
}
