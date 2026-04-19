package com.jni.AOADeviceHandle;

public class AOADeviceCameraConfig {
    public int bFormatType;
    public int dwFrameInterval;
    public int wHeight;
    public int wWidth;

    public void SetFormatType(int i) {
        this.bFormatType = i;
    }

    public void SetWidth(int i) {
        this.wWidth = i;
    }

    public void SetHeight(int i) {
        this.wHeight = i;
    }

    public void SetFrameInterval(int i) {
        this.dwFrameInterval = i;
    }

    public int GetbFormatType() {
        return this.bFormatType;
    }

    public int GetwWidth() {
        return this.wWidth;
    }

    public int GetwHeight() {
        return this.wHeight;
    }

    public int GetdwFrameInterval() {
        return this.dwFrameInterval;
    }

    public String toString() {
        return "AOADeviceCameraConfig{bFormatType=" + this.bFormatType + ", wWidth=" + this.wWidth + ", wHeight=" + this.wHeight + ", dwFrameInterval=" + this.dwFrameInterval + '}';
    }
}
