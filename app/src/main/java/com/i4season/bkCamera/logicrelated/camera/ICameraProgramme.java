package com.i4season.bkCamera.logicrelated.camera;

import android.content.Context;
import android.graphics.Bitmap;
import com.jni.AOADeviceHandle.AOADeviceCameraData;
import com.jni.WifiCameraInfo.WifiCameraPic;
import com.jni.WifiCameraInfo.WifiCameraStatusInfo;

public interface ICameraProgramme {
    public static final int TAKE_PHOTO = 1;
    public static final int TAKE_RECORD = 2;

    void acceptCurrentResolution();

    void acceptFwInfo();

    void acceptKeyPressStatus(int i);

    void acceptResolutionList();

    void acceptThreshold(int i);

    void cameraOnlineOrOffline(int i);

    void disconnectDev();

    void gyroscopeChangeAngle(float f, float f2);

    void initProgrammeSdk(Context context);

    void licenseCheck();

    void resetConnect2DevChange();

    void sendBatteryAndChargingStatus(int i, float f, int i2);

    void sendLongTimePrompt(boolean z);

    void sendLowerBattery(int i);

    void sendWifiCameraStatusInfo(int i, WifiCameraStatusInfo wifiCameraStatusInfo);

    void setCameraInfo(int i, WifiCameraStatusInfo wifiCameraStatusInfo);

    void setData(int i, AOADeviceCameraData aOADeviceCameraData);

    void setData(int i, WifiCameraPic wifiCameraPic);

    void setStatus(int i, int i2);

    void setVoiceData(int i, WifiCameraPic wifiCameraPic);

    void showBitmap(Bitmap bitmap);

    void startVideoRecoder();

    void stopVideoRecoder();

    void takePhoto(Bitmap bitmap);
}
