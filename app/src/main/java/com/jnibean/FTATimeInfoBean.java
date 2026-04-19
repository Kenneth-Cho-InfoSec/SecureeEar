package com.jnibean;

import java.io.Serializable;

public class FTATimeInfoBean implements Serializable {
    private long mCreateTime;
    private long mModifyTime;

    public long getmCreateTime() {
        return this.mCreateTime;
    }

    public void setmCreateTime(long j) {
        this.mCreateTime = j;
    }

    public long getmModifyTime() {
        return this.mModifyTime;
    }

    public void setmModifyTime(long j) {
        this.mModifyTime = j;
    }
}
