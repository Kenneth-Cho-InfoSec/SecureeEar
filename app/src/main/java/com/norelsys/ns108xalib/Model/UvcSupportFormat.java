package com.norelsys.ns108xalib.Model;

public class UvcSupportFormat {
    int compreIndex;
    int dwMaxVideoFrameBufferSize;
    int formatIndex;
    int formatType;
    int frameIndex;
    int frameInterval;
    int height;
    int width;

    public UvcSupportFormat() {
    }

    public UvcSupportFormat(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        this.compreIndex = i;
        this.formatType = i2;
        this.formatIndex = i3;
        this.frameIndex = i4;
        this.width = i5;
        this.height = i6;
        this.frameInterval = i7;
        this.dwMaxVideoFrameBufferSize = i8;
    }

    public int getFormatType() {
        return this.formatType;
    }

    public int getFormatIndex() {
        return this.formatIndex;
    }

    public int getFrameIndex() {
        return this.frameIndex;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getFrameInterval() {
        return this.frameInterval;
    }

    public int getDwMaxVideoFrameBufferSize() {
        return this.dwMaxVideoFrameBufferSize;
    }

    public int getCompreIndex() {
        return this.compreIndex;
    }
}
