package com.jni.AOADeviceHandle;

public class AOADeviceCameraResolution {
    public int bFrameIndex;
    public int dwDefaultFrameInterval;
    public int wHeight;
    public int wWidth;

    public void SetFrameIndex(int i) {
        this.bFrameIndex = i;
    }

    public void SetWidth(int i) {
        this.wWidth = i;
    }

    public void SetHeight(int i) {
        this.wHeight = i;
    }

    public void SetDefaultFrameInterval(int i) {
        this.dwDefaultFrameInterval = i;
    }

    public String toString() {
        return "AOADeviceCameraResolution{bFrameIndex=" + this.bFrameIndex + ", wWidth=" + this.wWidth + ", wHeight=" + this.wHeight + ", dwDefaultFrameInterval=" + this.dwDefaultFrameInterval + '}';
    }
}
