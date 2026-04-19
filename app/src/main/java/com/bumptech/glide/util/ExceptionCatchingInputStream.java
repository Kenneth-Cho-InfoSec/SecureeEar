package com.bumptech.glide.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;

public class ExceptionCatchingInputStream extends InputStream {
    private static final Queue<ExceptionCatchingInputStream> QUEUE = Util.createQueue(0);
    private IOException exception;
    private InputStream wrapped;

    public static ExceptionCatchingInputStream obtain(InputStream inputStream) {
        ExceptionCatchingInputStream exceptionCatchingInputStreamPoll;
        synchronized (QUEUE) {
            exceptionCatchingInputStreamPoll = QUEUE.poll();
        }
        if (exceptionCatchingInputStreamPoll == null) {
            exceptionCatchingInputStreamPoll = new ExceptionCatchingInputStream();
        }
        exceptionCatchingInputStreamPoll.setInputStream(inputStream);
        return exceptionCatchingInputStreamPoll;
    }

    static void clearQueue() {
        while (!QUEUE.isEmpty()) {
            QUEUE.remove();
        }
    }

    ExceptionCatchingInputStream() {
    }

    void setInputStream(InputStream inputStream) {
        this.wrapped = inputStream;
    }

    @Override
    public int available() throws IOException {
        return this.wrapped.available();
    }

    @Override
    public void close() throws IOException {
        this.wrapped.close();
    }

    @Override
    public void mark(int i) {
        this.wrapped.mark(i);
    }

    @Override
    public boolean markSupported() {
        return this.wrapped.markSupported();
    }

    @Override
    public int read(byte[] bArr) {
        try {
            return this.wrapped.read(bArr);
        } catch (IOException e) {
            this.exception = e;
            return -1;
        }
    }

    @Override
    public int read(byte[] bArr, int i, int i2) {
        try {
            return this.wrapped.read(bArr, i, i2);
        } catch (IOException e) {
            this.exception = e;
            return -1;
        }
    }

    @Override
    public synchronized void reset() throws IOException {
        this.wrapped.reset();
    }

    @Override
    public long skip(long j) {
        try {
            return this.wrapped.skip(j);
        } catch (IOException e) {
            this.exception = e;
            return 0L;
        }
    }

    @Override
    public int read() {
        try {
            return this.wrapped.read();
        } catch (IOException e) {
            this.exception = e;
            return -1;
        }
    }

    public IOException getException() {
        return this.exception;
    }

    public void release() {
        this.exception = null;
        this.wrapped = null;
        synchronized (QUEUE) {
            QUEUE.offer(this);
        }
    }
}
