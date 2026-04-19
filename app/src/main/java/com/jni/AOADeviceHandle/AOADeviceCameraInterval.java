package com.jni.AOADeviceHandle;

public class AOADeviceCameraInterval {
    public int Interval = 0;

    public void SetInterval(int i) {
        this.Interval = i;
    }

    public int GetInterval() {
        return this.Interval;
    }

    public String toString() {
        return "AOADeviceCameraInterval{Interval=" + this.Interval + '}';
    }
}
