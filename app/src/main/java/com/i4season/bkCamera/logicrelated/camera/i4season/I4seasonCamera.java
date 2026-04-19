package com.i4season.bkCamera.logicrelated.camera.i4season;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import com.i4season.bkCamera.logicrelated.camera.CameraConstant;
import com.i4season.bkCamera.logicrelated.camera.CameraEventObserver;
import com.i4season.bkCamera.logicrelated.camera.ICameraProgramme;
import com.i4season.bkCamera.logicrelated.camera.bean.CameraFirmInfo;
import com.i4season.bkCamera.logicrelated.recoder.I4seasonRecoderInstanse;
import com.i4season.bkCamera.logicrelated.recoder.RecoderVideoRunable;
import com.i4season.bkCamera.uirelated.other.AppPathInfo;
import com.i4season.bkCamera.uirelated.other.FunctionSwitch;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.Constant;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.FileUtil;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.ImgUtil;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.UtilTools;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.media.OperateLocalMedia;
import com.i4season.bkCamera.uirelated.other.logmanage.LogWD;
import com.jni.AOADeviceHandle.AOADeviceCameraConfig;
import com.jni.AOADeviceHandle.AOADeviceCameraData;
import com.jni.AOADeviceHandle.AOADeviceFirmInfo;
import com.jni.AOADeviceHandle.AOADeviceWiFiInfo;
import com.jni.UStorageDeviceModule;
import com.jni.WifiCameraInfo.WifiCameraPic;
import com.jni.WifiCameraInfo.WifiCameraStatusInfo;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class I4seasonCamera implements ICameraProgramme, RecoderVideoRunable.RecoderDelegate {
    private int battery;
    private String currentDeviceSsid;
    private int isCharge;
    private int isLowPowerOff;
    private int isSensorOk;
    private Bitmap mBitmap;
    private CameraEventObserver mCameraEventObserver;
    private Context mContext;
    private Timer timer;
    private AOADeviceCameraData mAoaDeviceCameraData = new AOADeviceCameraData();
    private int mOnlineStatus = 31;
    private boolean SUPPORT_ANGLE = false;
    private float mLastAngle = 0.0f;
    private int ACCEPT_NUM = 0;
    private String mVideoSavePath = "";
    private String FILE_SAVE_PATH = "";
    private boolean IS_RECODEER = false;
    private boolean mDevViceoFirstClick = true;
    private boolean mIsThreshold = true;
    private int SDKCallbackOnlineStatus = 0;

    @Override
    public void sendWifiCameraStatusInfo(int i, WifiCameraStatusInfo wifiCameraStatusInfo) {
    }

    @Override
    public void setCameraInfo(int i, WifiCameraStatusInfo wifiCameraStatusInfo) {
    }

    @Override
    public void setData(int i, WifiCameraPic wifiCameraPic) {
    }

    @Override
    public void setVoiceData(int i, WifiCameraPic wifiCameraPic) {
    }

    public I4seasonCamera(CameraEventObserver cameraEventObserver) {
        this.mCameraEventObserver = cameraEventObserver;
    }

    @Override
    public void setStatus(int i, int i2) {
        if (i2 == 1) {
            LogWD.writeMsg(this, 8, "底层回调状态   发现设备");
            this.SDKCallbackOnlineStatus = 1;
            cameraOnlineOrOffline(32);
            return;
        }
        if (i2 == 2) {
            LogWD.writeMsg(this, 8, "底层回调状态   设备上线成功");
            deviceOnline();
            return;
        }
        if (i2 == 3) {
            LogWD.writeMsg(this, 8, "底层回调状态   设备上线失败");
            this.SDKCallbackOnlineStatus = 3;
            cameraOnlineOrOffline(3);
            return;
        }
        if (i2 != 4) {
            if (i2 != 5) {
                return;
            }
            LogWD.writeMsg(this, 8, "底层回调状态   底层不能创建Socket的错误");
            this.isCharge = 0;
            this.battery = 0;
            this.isLowPowerOff = 0;
            cameraOnlineOrOffline(31);
            return;
        }
        LogWD.writeMsg(this, 8, "底层回调状态   设备掉线");
        this.SDKCallbackOnlineStatus = 4;
        LogWD.writeMsg(this, 2, "此方案设备掉线");
        this.isCharge = 0;
        this.battery = 0;
        this.isLowPowerOff = 0;
        cameraOnlineOrOffline(31);
    }

    @Override
    public void setData(int i, AOADeviceCameraData aOADeviceCameraData) {
        LogWD.writeMsg(this, 2, "setData mIsOnline: " + this.mOnlineStatus);
        try {
            if (this.mOnlineStatus == 33) {
                acceptImageData(i, aOADeviceCameraData);
            }
        } catch (Exception e) {
            LogWD.writeMsg(this, 2, e.getMessage());
            e.printStackTrace();
        }
    }

    private void acceptImageData(int i, AOADeviceCameraData aOADeviceCameraData) {
        if (aOADeviceCameraData == null || aOADeviceCameraData.data == null || aOADeviceCameraData.data.length == 0) {
            return;
        }
        this.mAoaDeviceCameraData = aOADeviceCameraData;
        this.ACCEPT_NUM++;
        LogWD.writeMsg(this, 2, "***************NUM：" + this.ACCEPT_NUM);
        LogWD.writeMsg(this, 2, "电量：" + this.mAoaDeviceCameraData.electricity + " 充電狀態： " + this.mAoaDeviceCameraData.charging);
        if (this.isCharge != this.mAoaDeviceCameraData.charging || this.battery != this.mAoaDeviceCameraData.electricity || this.isLowPowerOff != this.mAoaDeviceCameraData.poweroff) {
            sendBatteryAndChargingStatus(1, this.mAoaDeviceCameraData.electricity, this.mAoaDeviceCameraData.charging);
        }
        this.isCharge = this.mAoaDeviceCameraData.charging;
        this.battery = this.mAoaDeviceCameraData.electricity;
        this.isLowPowerOff = this.mAoaDeviceCameraData.poweroff;
        System.out.println("电量：" + this.mAoaDeviceCameraData.electricity + " 充電狀態： " + this.mAoaDeviceCameraData.charging);
        LogWD.writeMsg(this, 2, "阈值");
        acceptThreshold(this.mAoaDeviceCameraData.data.length);
        LogWD.writeMsg(this, 2, "判断是否有物理按键需要处理");
        physicalKeysHandler();
        LogWD.writeMsg(this, 2, "陀螺仪角度处理");
        sendLongTimePrompt(this.mAoaDeviceCameraData.needmove == 1);
        Bitmap bitmapByteToBitmap = ImgUtil.byteToBitmap(this.mAoaDeviceCameraData.data);
        if (bitmapByteToBitmap == null) {
            LogWD.writeMsg(this, 2, "数据有问题：为空");
            return;
        }
        Constant.BITMAP_WIDTH = bitmapByteToBitmap.getWidth();
        Constant.BITMAP_HEIGHT = bitmapByteToBitmap.getHeight();
        if (this.SUPPORT_ANGLE) {
            Bitmap bitmapCropBitmap = ImgUtil.cropBitmap(bitmapByteToBitmap);
            if (CameraConstant.GYROSCOPE_SWITCH) {
                float f = this.mAoaDeviceCameraData.angle;
                float f2 = f - this.mLastAngle;
                if (Math.abs(10.0f * f2) < 8.0f) {
                    f2 = 0.0f;
                } else {
                    this.mLastAngle = f;
                }
                System.out.println("角度：" + f + "  变化角度： " + f2 + "  mLastAngle： " + this.mLastAngle);
                bitmapCropBitmap = ImgUtil.rotateBitmap2(bitmapCropBitmap, this.mLastAngle);
            }
            showBitmap(bitmapCropBitmap);
            return;
        }
        showBitmap(bitmapByteToBitmap);
    }

    private void deviceOnline() {
        LogWD.writeMsg(this, 8, "设备上线成功");
        AOADeviceWiFiInfo aOADeviceWiFiInfo = new AOADeviceWiFiInfo();
        if (UStorageDeviceModule.getInstance().gStorageCommandHandle.cameraWifiGetWiFiInfo(aOADeviceWiFiInfo) == 0) {
            String ssid = aOADeviceWiFiInfo.getSSID();
            LogWD.writeMsg(this, 8, "从设备获取到的设备ssid：  " + ssid);
            this.SDKCallbackOnlineStatus = 2;
            this.currentDeviceSsid = ssid;
            if (this.mOnlineStatus != 33) {
                cameraOnlineOrOffline(33);
                int iCameraWifiHasAngle = UStorageDeviceModule.getInstance().gStorageCommandHandle.cameraWifiHasAngle();
                this.SUPPORT_ANGLE = iCameraWifiHasAngle != 0;
                LogWD.writeMsg(this, 2, "wifiHasAngle： = " + iCameraWifiHasAngle + "  SUPPORT_ANGLE： = " + this.SUPPORT_ANGLE);
                return;
            }
            return;
        }
        this.SDKCallbackOnlineStatus = 3;
        cameraOnlineOrOffline(2);
    }

    private void gyrocopeAngleHandler() {
        float f = this.mAoaDeviceCameraData.angle;
        LogWD.writeMsg(this, 2, "sendAllEventResponse2GyroscopeObserver angle: " + f);
        gyroscopeChangeAngle(f, f - this.mLastAngle);
    }

    private void physicalKeysHandler() {
        if (this.mAoaDeviceCameraData.take == 1 || this.mAoaDeviceCameraData.take == 3) {
            LogWD.writeMsg(this, 2, "物理拍摄按键");
            acceptKeyPressStatus(20);
        }
        if (this.mAoaDeviceCameraData.take == 2 || this.mAoaDeviceCameraData.take == 3) {
            if (this.mDevViceoFirstClick) {
                LogWD.writeMsg(this, 2, "物理录制按键 开始");
                acceptKeyPressStatus(21);
            }
            this.mDevViceoFirstClick = false;
        }
        if (this.mAoaDeviceCameraData.take == 0) {
            if (!this.mDevViceoFirstClick && this.IS_RECODEER) {
                LogWD.writeMsg(this, 2, "物理录制按键 保存");
                acceptKeyPressStatus(22);
            }
            this.mDevViceoFirstClick = true;
        }
        if (this.mAoaDeviceCameraData.zoom == 1) {
            LogWD.writeMsg(this, 2, "物理 缩小 按键");
            acceptKeyPressStatus(24);
        }
        if (this.mAoaDeviceCameraData.zoom == 2) {
            LogWD.writeMsg(this, 2, "物理 放大 按键");
            acceptKeyPressStatus(23);
        }
    }

    private boolean acceptWifiLicense() {
        AOADeviceFirmInfo aOADeviceFirmInfo = new AOADeviceFirmInfo();
        int iCameraWifiGetFirmInfo = UStorageDeviceModule.getInstance().gStorageCommandHandle.cameraWifiGetFirmInfo(aOADeviceFirmInfo);
        LogWD.writeMsg(this, 2, "errorCode = " + iCameraWifiGetFirmInfo);
        if (iCameraWifiGetFirmInfo == 0) {
            return wifiLicenseCheck(aOADeviceFirmInfo);
        }
        int iCameraWifiGetFirmInfo2 = UStorageDeviceModule.getInstance().gStorageCommandHandle.cameraWifiGetFirmInfo(aOADeviceFirmInfo);
        LogWD.writeMsg(this, 2, "errorCode = " + iCameraWifiGetFirmInfo2);
        if (iCameraWifiGetFirmInfo2 == 0) {
            return wifiLicenseCheck(aOADeviceFirmInfo);
        }
        int iCameraWifiGetFirmInfo3 = UStorageDeviceModule.getInstance().gStorageCommandHandle.cameraWifiGetFirmInfo(aOADeviceFirmInfo);
        LogWD.writeMsg(this, 2, "errorCode = " + iCameraWifiGetFirmInfo3);
        if (iCameraWifiGetFirmInfo3 == 0) {
            return wifiLicenseCheck(aOADeviceFirmInfo);
        }
        FunctionSwitch.isCheckError = false;
        Log.d("liusheng", "请求lic失败 ");
        return false;
    }

    private boolean wifiLicenseCheck(AOADeviceFirmInfo aOADeviceFirmInfo) {
        Log.d("liusheng", "aoaDeviceFirmInfo.toString: " + aOADeviceFirmInfo.toString());
        boolean z = UStorageDeviceModule.getInstance().gStorageCommandHandle.checkLic10(aOADeviceFirmInfo.getLicense(), aOADeviceFirmInfo.getLicense().length, aOADeviceFirmInfo.getModelName()) == 0;
        LogWD.writeMsg(this, 2, "license验证： = " + z);
        FunctionSwitch.isCheckError = true;
        return z;
    }

    @Override
    public void initProgrammeSdk(Context context) {
        LogWD.writeMsg(this, 8, "四季方案初始化");
        Log.d("liusheng", "四季方案初始化");
        this.mContext = context;
        connectDev2IP();
    }

    private void connectDev2IP() {
        String wifiRouteIPAddress = UtilTools.getWifiRouteIPAddress(this.mContext);
        LogWD.writeMsg(this, 8, "ipAddress : " + wifiRouteIPAddress);
        String strAcceptCurrentWifiId = UtilTools.acceptCurrentWifiId(this.mContext);
        if (Constant.CANNOT_GET_SSID.equals(strAcceptCurrentWifiId) || TextUtils.isEmpty(strAcceptCurrentWifiId)) {
            strAcceptCurrentWifiId = "<unknown ssid>";
        }
        UStorageDeviceModule.getInstance().wificallbackstart(this.mContext, strAcceptCurrentWifiId, wifiRouteIPAddress, Constant.WIFI_CAMERA_PORT, UStorageDeviceModule.sdk_switch);
    }

    @Override
    public void resetConnect2DevChange() {
        connectDev2IP();
    }

    @Override
    public void disconnectDev() {
        LogWD.writeMsg(this, 8, "ipAddress : " + UtilTools.getWifiRouteIPAddress(this.mContext));
        String strAcceptCurrentWifiId = UtilTools.acceptCurrentWifiId(this.mContext);
        if (Constant.CANNOT_GET_SSID.equals(strAcceptCurrentWifiId)) {
            strAcceptCurrentWifiId = "";
        }
        UStorageDeviceModule.getInstance().wificallbackstart(this.mContext, strAcceptCurrentWifiId, "0.0.0.0", Constant.WIFI_CAMERA_PORT, UStorageDeviceModule.sdk_switch);
    }

    @Override
    public void cameraOnlineOrOffline(int i) {
        int i2 = this.mOnlineStatus;
        if (i2 != 2) {
            i2 = i;
        }
        this.mOnlineStatus = i2;
        this.mCameraEventObserver.cameraOnlineOrOffline(i, 1);
    }

    @Override
    public void licenseCheck() {
        boolean zAcceptWifiLicense = acceptWifiLicense();
        LogWD.writeMsg(this, 2, "licenseCheckCommand： = " + zAcceptWifiLicense);
        this.mCameraEventObserver.sendAllEventResponse2LicenseCheckObserver(zAcceptWifiLicense, 1);
    }

    @Override
    public void showBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
        videoRecorder(bitmap);
        LogWD.writeMsg(this, 2, "showBitmap mIsOnline: " + this.mOnlineStatus);
        this.mCameraEventObserver.sendAllEventResponse2ShowBitmapObserver(bitmap, 1);
    }

    @Override
    public void acceptResolutionList() {
        LogWD.writeMsg(this, 2, "获取分辨率 errorCode： " + UStorageDeviceModule.getInstance().gStorageCommandHandle.cameraWifiResolutionGet(new ArrayList<>()));
    }

    @Override
    public void acceptCurrentResolution() {
        AOADeviceCameraConfig aOADeviceCameraConfig = new AOADeviceCameraConfig();
        LogWD.writeMsg(this, 2, "获取当前配置 errorCode： " + UStorageDeviceModule.getInstance().gStorageCommandHandle.cameraWifiConfGet(aOADeviceCameraConfig) + "aoaDeviceCameraConfig.toString(): " + aOADeviceCameraConfig.toString());
    }

    @Override
    public void gyroscopeChangeAngle(float f, float f2) {
        float f3;
        LogWD.writeMsg(this, 2, "sendAllEventResponse2GyroscopeObserver angle: " + f + " changAngle: " + f2);
        if (Math.abs(10.0f * f2) < 8.0f) {
            f3 = 0.0f;
        } else {
            this.mLastAngle = f;
            f3 = f2;
        }
        System.out.println("角度：" + f + "  去抖变化角度：" + f3 + "  变化角度： " + f2);
        this.mCameraEventObserver.sendAllEventResponse2GyroscopeObserver(f, f3, 1);
    }

    @Override
    public void acceptKeyPressStatus(int i) {
        LogWD.writeMsg(this, 2, "acceptKeyPressStatus keyType: " + i);
        this.mCameraEventObserver.sendAllEventKeyPressStatus2ResolutionObserver(i, 1);
    }

    @Override
    public void acceptThreshold(int i) {
        LogWD.writeMsg(this, 2, "sendAllEventResponse2ThresoldObserver threshold: " + i);
        if (this.mIsThreshold) {
            this.mCameraEventObserver.sendAllEventResponse2ThresoldObserver(i, 1);
        }
        this.mIsThreshold = !this.mIsThreshold;
    }

    @Override
    public void sendLowerBattery(int i) {
        LogWD.writeMsg(this, 2, "sendAllEventResponse2LowerBatteryObserver");
        this.mCameraEventObserver.sendAllEventResponse2LowerBatteryObserver(i);
    }

    @Override
    public void sendBatteryAndChargingStatus(int i, float f, int i2) {
        LogWD.writeMsg(this, 2, "sendAllEventResponse2BatteryAndChargingObserver 电量：" + this.mAoaDeviceCameraData.battery + " 充電狀態： " + this.mAoaDeviceCameraData.charging);
        this.mCameraEventObserver.sendAllEventResponse2BatteryAndChargingObserver(i, f, i2);
    }

    @Override
    public void sendLongTimePrompt(boolean z) {
        this.mCameraEventObserver.sendAllEventResponse2LongTimeObserver(z);
    }

    @Override
    public void acceptFwInfo() {
        new Thread() {
            @Override
            public void run() {
                CameraFirmInfo cameraFirmInfo = new CameraFirmInfo();
                AOADeviceFirmInfo aOADeviceFirmInfo = new AOADeviceFirmInfo();
                LogWD.writeMsg(this, 2, "cameraWifiGetFirmInfo errorCode = " + UStorageDeviceModule.getInstance().gStorageCommandHandle.cameraWifiGetFirmInfo(aOADeviceFirmInfo));
                cameraFirmInfo.setFwint(aOADeviceFirmInfo.Getfwint());
                cameraFirmInfo.setVersion(aOADeviceFirmInfo.getFwVersion());
                cameraFirmInfo.setHwVersion(aOADeviceFirmInfo.getHwVersion());
                cameraFirmInfo.setLicense(aOADeviceFirmInfo.getLicense());
                cameraFirmInfo.setSn(aOADeviceFirmInfo.getSn());
                cameraFirmInfo.setVendor(aOADeviceFirmInfo.getModelName());
                cameraFirmInfo.setProduct(aOADeviceFirmInfo.getManuFacture());
                AOADeviceWiFiInfo aOADeviceWiFiInfo = new AOADeviceWiFiInfo();
                int iCameraWifiGetWiFiInfo = UStorageDeviceModule.getInstance().gStorageCommandHandle.cameraWifiGetWiFiInfo(aOADeviceWiFiInfo);
                LogWD.writeMsg(this, 2, "cameraWifiSetWiFiInfo errorCode = " + iCameraWifiGetWiFiInfo);
                cameraFirmInfo.setSsid(aOADeviceWiFiInfo.getSSID());
                cameraFirmInfo.setPASSWD(aOADeviceWiFiInfo.getPASSWD());
                cameraFirmInfo.setChannel(aOADeviceWiFiInfo.getChannel());
                I4seasonCamera.this.mCameraEventObserver.sendAllEventResponse2FWObserver(iCameraWifiGetWiFiInfo == 0, cameraFirmInfo, 1);
            }
        }.start();
    }

    @Override
    public void takePhoto(Bitmap bitmap) {
        LogWD.writeMsg(this, 16, "take photo");
        UtilTools.playTakePhotoAudio(this.mContext);
        new Thread() {
            /* JADX WARN: Removed duplicated region for block: B:7:0x0017  */
            @Override
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public void run() {
                boolean z;
                if (I4seasonCamera.this.mBitmap != null) {
                    I4seasonCamera i4seasonCamera = I4seasonCamera.this;
                    z = i4seasonCamera.isSavePhotoSuccful(i4seasonCamera.mBitmap);
                }
                I4seasonCamera.this.mCameraEventObserver.sendAllTakePhotoRecoder2FWObserver(z, I4seasonCamera.this.FILE_SAVE_PATH, 1);
            }
        }.start();
    }

    @Override
    public void startVideoRecoder() {
        startSendRecoder();
    }

    @Override
    public void stopVideoRecoder() {
        sendFinishRecoder();
    }

    public boolean isSavePhotoSuccful(Bitmap bitmap) {
        boolean zTakePhoto2LowerVersion;
        try {
            String strReplace = UtilTools.getMsecTimeFromLong(System.currentTimeMillis()).replace(Constant.SMB_COLON, "_");
            if (!new File(AppPathInfo.getSaveCameraDataPath2AndroidO()).exists()) {
                UtilTools.createFolderInSdcard(AppPathInfo.getSaveCameraDataPath2AndroidO());
            }
            Bitmap bitmapZoomBitmapNoRecyled = ImgUtil.zoomBitmapNoRecyled(ImgUtil.toRoundBitmap(bitmap), 2300, 2300);
            if (Build.VERSION.SDK_INT >= 29) {
                zTakePhoto2LowerVersion = takePhoto2HightVersion(strReplace, bitmapZoomBitmapNoRecyled);
            } else {
                zTakePhoto2LowerVersion = takePhoto2LowerVersion(strReplace, bitmapZoomBitmapNoRecyled);
            }
            LogWD.writeMsg(this, 16, "take photo save: " + zTakePhoto2LowerVersion);
            return zTakePhoto2LowerVersion;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean takePhoto2LowerVersion(String str, Bitmap bitmap) {
        String str2 = AppPathInfo.getSaveCameraDataPath2AndroidO() + File.separator + AppPathInfo.camera_photo + "_" + str + Constant.SAVE_PHOTO_SUFFIX;
        LogWD.writeMsg(this, 16, "take photo savePath: " + str2);
        LogWD.writeMsg(this, 16, "take photo creat savePath: " + UtilTools.createFileInfSdcard(str2));
        boolean zBytesToImageBitmap = FileUtil.bytesToImageBitmap(bitmap, str2);
        if (zBytesToImageBitmap) {
            this.FILE_SAVE_PATH = str2;
            OperateLocalMedia.getInstance().updateMediaSqlite(this.mContext, str2);
        }
        return zBytesToImageBitmap;
    }

    private boolean takePhoto2HightVersion(String str, Bitmap bitmap) {
        boolean zSaveBitmap = FileUtil.saveBitmap(this.mContext, bitmap, "Photo_" + str);
        if (zSaveBitmap) {
            String str2 = AppPathInfo.getSaveCameraDataPath2AndroidO() + File.separator + AppPathInfo.camera_photo + "_" + str + Constant.SAVE_PHOTO_SUFFIX;
            this.FILE_SAVE_PATH = str2;
            OperateLocalMedia.getInstance().updateMediaSqlite(this.mContext, str2);
        }
        return zSaveBitmap;
    }

    private void startSendRecoder() {
        String strReplace = UtilTools.getMsecTimeFromLong(System.currentTimeMillis()).replace(Constant.SMB_COLON, "_");
        if (Build.VERSION.SDK_INT >= 29) {
            String absolutePath = this.mContext.getExternalFilesDir(Environment.DIRECTORY_MOVIES).getAbsolutePath();
            File file = new File(absolutePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            File file2 = new File(absolutePath, "Video_" + strReplace + Constant.SAVE_VIDEO_SUFFIX);
            if (file2.exists()) {
                file2.delete();
            }
            try {
                file2.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.mVideoSavePath = file2.getAbsolutePath();
        } else {
            String str = AppPathInfo.getSaveCameraDataPath2AndroidO() + File.separator + AppPathInfo.camera_video + "_" + strReplace + Constant.SAVE_VIDEO_SUFFIX;
            LogWD.writeMsg(this, 16, "take photo savePath: " + str);
            this.mVideoSavePath = str;
            this.FILE_SAVE_PATH = str;
        }
        I4seasonRecoderInstanse.getInstance().startRecoder(Constant.BITMAP_WIDTH, Constant.BITMAP_HEIGHT, Constant.BITMAP_WIDTH, Constant.BITMAP_HEIGHT, 16, true, false, false, this.mVideoSavePath, this);
        this.IS_RECODEER = true;
        Timer timer = this.timer;
        if (timer != null) {
            timer.cancel();
            this.timer = null;
        }
        this.timer = new Timer();
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                I4seasonCamera i4seasonCamera = I4seasonCamera.this;
                i4seasonCamera.videoRecorder(i4seasonCamera.mBitmap);
            }
        }, 0L, 62);
        LogWD.writeMsg(this, 16, "startAviVideoRecord: ");
    }

    @Override
    public void onRecoderAddFrameFinish(boolean z) throws Throwable {
        LogWD.writeMsg(this, 16, "onRecoderAddFrameFinish: " + z);
        if (Build.VERSION.SDK_INT >= 29) {
            File file = new File(this.mVideoSavePath);
            FileUtil.copyPrivateToPictures(this.mContext, file.getAbsolutePath(), file.getName());
            file.delete();
            this.FILE_SAVE_PATH = AppPathInfo.getSaveCameraDataPath2AndroidO() + File.separator + file.getName();
        }
        this.mCameraEventObserver.sendAllTakePhotoRecoder2FWObserver(z, this.FILE_SAVE_PATH, 2);
    }

    public void videoRecorder(Bitmap bitmap) {
        LogWD.writeMsg(this, 16, "videoRecorder: " + this.IS_RECODEER);
        if (this.IS_RECODEER) {
            I4seasonRecoderInstanse.getInstance().addRecoderFrame(bitmap);
        }
    }

    private void sendFinishRecoder() {
        Timer timer = this.timer;
        if (timer != null) {
            timer.cancel();
            this.timer = null;
        }
        this.IS_RECODEER = false;
        I4seasonRecoderInstanse.getInstance().stopRecoder();
        LogWD.writeMsg(this, 16, "sendFinishRecoder: ");
    }

    public void setmOnlineStatus(int i) {
        this.mOnlineStatus = i;
    }

    public int ismOnlineStatus() {
        LogWD.writeMsg(this, 8, "ismOnlineStatus： " + this.mOnlineStatus);
        return this.mOnlineStatus;
    }

    public int getSDKCallbackOnlineStatus() {
        return this.SDKCallbackOnlineStatus;
    }

    public WifiCameraStatusInfo getWifiCameraStatusInfo() {
        WifiCameraStatusInfo wifiCameraStatusInfo = new WifiCameraStatusInfo();
        wifiCameraStatusInfo.Setbattery(this.battery);
        wifiCameraStatusInfo.SetisCharge(this.isCharge);
        wifiCameraStatusInfo.SetisLowPowerOff(this.isLowPowerOff);
        return wifiCameraStatusInfo;
    }

    public String getCurrentDeviceSsid() {
        return this.currentDeviceSsid;
    }

    public Bitmap getmBitmap() {
        return this.mBitmap;
    }
}
