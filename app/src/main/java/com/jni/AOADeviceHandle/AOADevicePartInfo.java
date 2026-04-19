package com.jni.AOADeviceHandle;

public class AOADevicePartInfo {
    public long mAvailSize;
    public int mPartNumb;
    public long mTotalSize;
    public long mUsedSize;
    public String mountDir;
    public int partFormat;
    public int readonly;
    public String volumeName;

    public void SetmPartNumb(int i) {
        this.mPartNumb = i;
    }

    public void SetmTotalSize(long j) {
        this.mTotalSize = j;
    }

    public void SetmUsedSize(long j) {
        this.mUsedSize = j;
    }

    public void SetmAvailSize(long j) {
        this.mAvailSize = j;
    }

    public void SetmpartFormat(int i) {
        this.partFormat = i;
    }

    public void SetmountDir(String str) {
        this.mountDir = str;
    }

    public void SetvolumeName(String str) {
        this.volumeName = str;
    }

    public void SetReadonly(int i) {
        this.readonly = i;
    }

    public String toString() {
        return "AOADevicePartInfo{mountDir='" + this.mountDir + "', volumeName='" + this.volumeName + "', mPartNumb=" + this.mPartNumb + ", mTotalSize=" + this.mTotalSize + ", mUsedSize=" + this.mUsedSize + ", mAvailSize=" + this.mAvailSize + ", partFormat=" + this.partFormat + '}';
    }
}
