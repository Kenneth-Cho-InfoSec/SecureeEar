package com.jnibean;

public class VSDiskInfoBean {
    private long mAvailSize;
    private int mPartNumb;
    private long mTotalSize;
    private long mUsedSize;

    public int getmPartNumb() {
        return this.mPartNumb;
    }

    public void setmPartNumb(int i) {
        this.mPartNumb = i;
    }

    public long getmTotalSize() {
        return this.mTotalSize;
    }

    public void setmTotalSize(long j) {
        this.mTotalSize = j;
    }

    public long getmUsedSize() {
        return this.mUsedSize;
    }

    public void setmUsedSize(long j) {
        this.mUsedSize = j;
    }

    public long getmAvailSize() {
        return this.mAvailSize;
    }

    public void setmAvailSize(long j) {
        this.mAvailSize = j;
    }
}
