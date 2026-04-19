package com.bumptech.glide.load.data;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class ExifOrientationStream extends FilterInputStream {
    private static final int SEGMENT_START_POSITION = 2;
    private final byte orientation;
    private int position;
    private static final byte[] EXIF_SEGMENT = {-1, -31, 0, 28, 69, 120, 105, 102, 0, 0, 77, 77, 0, 0, 0, 0, 0, 8, 0, 1, 1, 18, 0, 2, 0, 0, 0, 1, 0};
    private static final int SEGMENT_LENGTH = EXIF_SEGMENT.length;
    private static final int ORIENTATION_POSITION = SEGMENT_LENGTH + 2;

    @Override
    public boolean markSupported() {
        return false;
    }

    public ExifOrientationStream(InputStream inputStream, int i) {
        super(inputStream);
        if (i < -1 || i > 8) {
            throw new IllegalArgumentException("Cannot add invalid orientation: " + i);
        }
        this.orientation = (byte) i;
    }

    @Override
    public void mark(int i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int read() throws IOException {
        int i;
        int i2;
        int i3 = this.position;
        if (i3 < 2 || i3 > (i2 = ORIENTATION_POSITION)) {
            i = super.read();
        } else if (i3 == i2) {
            i = this.orientation;
        } else {
            i = EXIF_SEGMENT[i3 - 2] & 255;
        }
        if (i != -1) {
            this.position++;
        }
        return i;
    }

    @Override
    public int read(byte[] bArr, int i, int i2) throws IOException {
        int i3;
        int i4 = this.position;
        int i5 = ORIENTATION_POSITION;
        if (i4 > i5) {
            i3 = super.read(bArr, i, i2);
        } else if (i4 == i5) {
            bArr[i] = this.orientation;
            i3 = 1;
        } else if (i4 < 2) {
            i3 = super.read(bArr, i, 2 - i4);
        } else {
            int iMin = Math.min(i5 - i4, i2);
            System.arraycopy(EXIF_SEGMENT, this.position - 2, bArr, i, iMin);
            i3 = iMin;
        }
        if (i3 > 0) {
            this.position += i3;
        }
        return i3;
    }

    @Override
    public long skip(long j) throws IOException {
        long jSkip = super.skip(j);
        if (jSkip > 0) {
            this.position = (int) (((long) this.position) + jSkip);
        }
        return jSkip;
    }

    @Override
    public void reset() throws IOException {
        throw new UnsupportedOperationException();
    }
}
