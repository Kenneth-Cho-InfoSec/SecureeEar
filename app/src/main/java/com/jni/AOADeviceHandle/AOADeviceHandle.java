package com.jni.AOADeviceHandle;

import android.content.Context;
import android.hardware.usb.UsbAccessory;

public class AOADeviceHandle {
    private AOADeviceDataOptHandle mAOADeviceDataOptHandle;

    public void initAOADeviceHandle(Context context, UsbAccessory usbAccessory) {
    }

    public void destoryAOADeviceHandle() {
        this.mAOADeviceDataOptHandle.clossyAccessory();
    }
}
