package com.jni.CallBack;

import android.util.Log;
import com.jni.AOADeviceHandle.AOADeviceCameraData;
import com.jni.UStorageDeviceModule;
import com.jni.logmanage.LogWD;
import com.jnibean.VSFileInfoBean;

public class UCallBack {
    public static final int CAMERAERRINEND = 3;
    public static final int CAMERAIN = 1;
    public static final int CAMERAINEND = 2;
    public static final int CAMERAOUTEND = 4;
    public static final int CAMERA_ADD_FILE = 1;
    public static final int CAMERA_HAVEPIC = 2;
    public static final int CAMERA_HAVEPICEND = 3;
    public static final int CAMERA_NETWORKBAD = 5;
    public static final int DTYPE_I4SEASONPROGROM = 1;
    public static final int DTYPE_LEXINPROGROM = 2;
    public static final int OFFLINE = 0;
    private DeviceStatusInterface deviceStatusInterface;
    private ICameraEventDelegate mICameraEventDelegate;
    private ICameraEventDelegate2 mICameraEventDelegate2;
    public int loopflag = 0;
    public boolean online = false;

    public interface DeviceStatusInterface {
        void getData(int i, AOADeviceCameraData aOADeviceCameraData);

        void getStatus(int i, int i2);
    }

    public interface ICameraEventDelegate {
        void receiveEvent(UCallBackInfo uCallBackInfo);
    }

    public interface ICameraEventDelegate2 {
        void receiveEvent(Object obj, int i);
    }

    public static class conConfigHolder {
        public static UCallBack gUCallBack = new UCallBack();
    }

    public int setSchedule(int i) {
        return 0;
    }

    public UCallBack(ICameraEventDelegate iCameraEventDelegate) {
        this.mICameraEventDelegate = iCameraEventDelegate;
    }

    public UCallBack(ICameraEventDelegate2 iCameraEventDelegate2) {
        this.mICameraEventDelegate2 = iCameraEventDelegate2;
    }

    public UCallBack(ICameraEventDelegate iCameraEventDelegate, ICameraEventDelegate2 iCameraEventDelegate2) {
        this.mICameraEventDelegate2 = iCameraEventDelegate2;
    }

    public UCallBack() {
        LogWD.writeMsg(this, 2, "begin: ");
    }

    public int sendmsg(Object obj, int i) {
        ICameraEventDelegate2 iCameraEventDelegate2;
        LogWD.writeMsg(this, 2, "type: " + i);
        if (i == 1) {
            if (this.mICameraEventDelegate == null) {
                return 0;
            }
            UCallBackInfo uCallBackInfo = new UCallBackInfo();
            uCallBackInfo.setType(i);
            if (i == 1) {
                uCallBackInfo.setVSFileInfoBean((VSFileInfoBean) obj);
            }
            this.mICameraEventDelegate.receiveEvent(uCallBackInfo);
            return 0;
        }
        if (i == 2) {
            ICameraEventDelegate2 iCameraEventDelegate22 = this.mICameraEventDelegate2;
            if (iCameraEventDelegate22 == null) {
                return 0;
            }
            iCameraEventDelegate22.receiveEvent(obj, i);
            return 0;
        }
        if (i != 3 || (iCameraEventDelegate2 = this.mICameraEventDelegate2) == null) {
            return 0;
        }
        iCameraEventDelegate2.receiveEvent(obj, i);
        return 0;
    }

    public int sendpic(Object obj, int i) {
        AOADeviceCameraData aOADeviceCameraData = (AOADeviceCameraData) obj;
        LogWD.writeMsg(this, 2, "sendpic: type= " + i);
        DeviceStatusInterface deviceStatusInterface = this.deviceStatusInterface;
        if (deviceStatusInterface == null) {
            return 0;
        }
        deviceStatusInterface.getData(i, aOADeviceCameraData);
        return 0;
    }

    public int sendsig(int i, int i2) {
        Log.d("liusheng", "sendpic: dtype= " + i + " status=" + i2);
        LogWD.writeMsg(this, 2, "sendpic: dtype= " + i + " status=" + i2);
        if (i2 == 2) {
            this.online = true;
        }
        if (i2 == 4 || i2 == 3 || i2 == 1) {
            this.online = false;
        }
        DeviceStatusInterface deviceStatusInterface = this.deviceStatusInterface;
        if (deviceStatusInterface != null) {
            deviceStatusInterface.getStatus(i, i2);
        }
        return 0;
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [com.jni.CallBack.UCallBack$1] */
    public int loop() {
        if (this.loopflag != 0) {
            return 0;
        }
        this.loopflag = 1;
        new Thread() {
            @Override
            public void run() {
                UStorageDeviceModule.getInstance().gStorageCommandHandle.CallBackStart(UCallBack.getInstance());
            }
        }.start();
        return 0;
    }

    public static UCallBack getInstance() {
        return conConfigHolder.gUCallBack;
    }

    public void setDeviceStatusInterface(DeviceStatusInterface deviceStatusInterface) {
        this.deviceStatusInterface = deviceStatusInterface;
    }
}
