package com.jni.CallBack;

import android.net.Network;
import android.os.SystemClock;
import com.jni.AOADeviceHandle.AOADeviceCameraData;
import com.jni.UStorageDeviceModule;

public class CameraCallBackFuc {
    public static final int CAMERA_CLOSE = 0;
    public static final int CAMERA_OPEN = 1;
    public static final int CAMERA_OPENING = 2;
    public Network gnetwork;
    private ICameraData mICameraEventDelegate;
    int start;
    public int status;

    public interface ICameraData {
        void receiveEvent(int i, AOADeviceCameraData aOADeviceCameraData);
    }

    public static class conConfigHolder {
        public static CameraCallBackFuc gCameraCallBackFuc = new CameraCallBackFuc();
    }

    private CameraCallBackFuc() {
        this.start = 0;
        this.status = 0;
    }

    public CameraCallBackFuc(ICameraData iCameraData) {
        this.start = 0;
        this.status = 0;
        this.mICameraEventDelegate = iCameraData;
    }

    public void changeStatus(int i, AOADeviceCameraData aOADeviceCameraData) {
        if (i != this.status) {
            this.status = i;
            this.mICameraEventDelegate.receiveEvent(i, aOADeviceCameraData);
        } else if (i == 1) {
            this.mICameraEventDelegate.receiveEvent(2, aOADeviceCameraData);
        }
    }

    public void acceptImageData(AOADeviceCameraData aOADeviceCameraData) {
        while (true) {
            int iCameraWifiGet = UStorageDeviceModule.getInstance().gStorageCommandHandle.cameraWifiGet(aOADeviceCameraData);
            if (iCameraWifiGet > 0) {
                changeStatus(1, aOADeviceCameraData);
                this.mICameraEventDelegate.receiveEvent(0, aOADeviceCameraData);
            } else if (iCameraWifiGet == 0) {
                SystemClock.sleep(20L);
            } else {
                changeStatus(0, null);
                SystemClock.sleep(500L);
            }
        }
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [com.jni.CallBack.CameraCallBackFuc$1] */
    public void loopstart() {
        if (this.start == 0) {
            this.start = 1;
            new Thread() {
                @Override
                public void run() {
                    CameraCallBackFuc.this.acceptImageData(new AOADeviceCameraData());
                }
            }.start();
        }
    }

    public static CameraCallBackFuc getInstance() {
        return conConfigHolder.gCameraCallBackFuc;
    }
}
