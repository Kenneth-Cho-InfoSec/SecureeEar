package com.i4season.bkCamera.logicrelated.camera.i4season_new;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.core.view.PointerIconCompat;
import com.i4season.bkCamera.logicrelated.camera.AudioPlayManager;
import com.i4season.bkCamera.logicrelated.camera.CameraConstant;
import com.i4season.bkCamera.logicrelated.camera.CameraEventObserver;
import com.i4season.bkCamera.logicrelated.camera.CameraManager;
import com.i4season.bkCamera.logicrelated.camera.ICameraProgramme;
import com.i4season.bkCamera.logicrelated.camera.bean.CameraFirmInfo;
import com.i4season.bkCamera.logicrelated.camera.reportlic.IReportDelegate;
import com.i4season.bkCamera.logicrelated.camera.reportlic.ReportLicInstance;
import com.i4season.bkCamera.logicrelated.encoder.RecodeInstans;
import com.i4season.bkCamera.logicrelated.encoder.RecordParams;
import com.i4season.bkCamera.logicrelated.recoder.RecoderVideoRunable;
import com.i4season.bkCamera.uirelated.other.AppPathInfo;
import com.i4season.bkCamera.uirelated.other.FunctionSwitch;
import com.i4season.bkCamera.uirelated.other.MainFrameHandleInstance;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.Constant;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.FileUtil;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.ImgUtil;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.UtilTools;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.media.OperateLocalMedia;
import com.i4season.bkCamera.uirelated.other.logmanage.LogWD;
import com.jni.AOADeviceHandle.AOADeviceCameraData;
import com.jni.I4ToolApi;
import com.jni.StorageDeviceCommand;
import com.jni.UStorageDeviceModule;
import com.jni.WifiCameraApi;
import com.jni.WifiCameraInfo.WifiCameraFirmInfo;
import com.jni.WifiCameraInfo.WifiCameraLedStatus;
import com.jni.WifiCameraInfo.WifiCameraLicInfo;
import com.jni.WifiCameraInfo.WifiCameraPic;
import com.jni.WifiCameraInfo.WifiCameraStatusInfo;
import com.jni.WifiCameraInfo.WifiScreenParameters;
import java.io.File;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class I4seasonCameraNew implements ICameraProgramme, RecoderVideoRunable.RecoderDelegate, IReportDelegate {
    private String currentDeviceSsid;
    private WifiCameraStatusInfo lastWifiCameraStatusInfo;
    private Bitmap mBitmap;
    private CameraEventObserver mCameraEventObserver;
    private Context mContext;
    private MediaScannerConnection mMs;
    private Timer timer;
    private WifiCameraFirmInfo wifiCameraFirmInfo;
    private WifiCameraLicInfo wifiCameraLicInfo;
    private WifiCameraPic mAoaDeviceCameraData = new WifiCameraPic();
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
    int lastAudioNum = -1;
    byte[] fillBuffer = new byte[PointerIconCompat.TYPE_GRAB];
    private ExecutorService pool = Executors.newCachedThreadPool();
    private MediaScannerConnection.MediaScannerConnectionClient connectionClient = new MediaScannerConnection.MediaScannerConnectionClient() {
        @Override
        public void onScanCompleted(String str, Uri uri) {
            I4seasonCameraNew.this.mMs.disconnect();
        }

        @Override
        public void onMediaScannerConnected() {
            I4seasonCameraNew.this.mMs.scanFile(I4seasonCameraNew.this.FILE_SAVE_PATH, "");
        }
    };

    @Override
    public void acceptCurrentResolution() {
    }

    @Override
    public void acceptResolutionList() {
    }

    @Override
    public void disconnectDev() {
    }

    @Override
    public void resetConnect2DevChange() {
    }

    @Override
    public void setData(int i, AOADeviceCameraData aOADeviceCameraData) {
    }

    public I4seasonCameraNew(CameraEventObserver cameraEventObserver) {
        this.mCameraEventObserver = cameraEventObserver;
        setBuffer();
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
        } else if (i2 != 4) {
            if (i2 != 5) {
                return;
            }
            LogWD.writeMsg(this, 8, "底层回调状态   底层不能创建Socket的错误");
        } else {
            LogWD.writeMsg(this, 8, "底层回调状态   设备掉线");
            this.SDKCallbackOnlineStatus = 4;
            LogWD.writeMsg(this, 2, "此方案设备掉线");
            cameraOnlineOrOffline(31);
        }
    }

    @Override
    public void setData(int i, WifiCameraPic wifiCameraPic) {
        LogWD.writeMsg(this, 2, "setData mIsOnline: " + this.mOnlineStatus);
        try {
            if (this.mOnlineStatus == 33) {
                acceptImageData(i, wifiCameraPic);
            }
        } catch (Exception e) {
            LogWD.writeMsg(this, 2, e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void setCameraInfo(int i, WifiCameraStatusInfo wifiCameraStatusInfo) {
        LogWD.writeMsg(this, 2, "setCameraInfo mIsOnline: " + this.mOnlineStatus);
        if (this.mOnlineStatus == 33) {
            WifiCameraStatusInfo wifiCameraStatusInfo2 = this.lastWifiCameraStatusInfo;
            if (wifiCameraStatusInfo2 == null || wifiCameraStatusInfo2.isusedbyother != wifiCameraStatusInfo.isusedbyother) {
                sendDeviceSeizeStatusChanged(wifiCameraStatusInfo);
            }
            sendWifiCameraStatusInfo(5, wifiCameraStatusInfo);
            this.lastWifiCameraStatusInfo = wifiCameraStatusInfo;
        }
    }

    @Override
    public void setVoiceData(int i, WifiCameraPic wifiCameraPic) {
        try {
            if (this.mOnlineStatus != 33 || wifiCameraPic.type != 7 || wifiCameraPic.data == null || wifiCameraPic.data.length <= 0) {
                return;
            }
            if (this.IS_RECODEER) {
                RecodeInstans.getInstance().setVoicesBuffer(wifiCameraPic.data);
            } else if (FunctionSwitch.VOICE_SWITCH) {
                AudioPlayManager.getInstance().pushBufData(wifiCameraPic.data);
            }
            this.lastAudioNum = wifiCameraPic.audionum;
        } catch (Exception e) {
            LogWD.writeMsg(this, 2, e.getMessage());
        }
    }

    private void setBuffer() {
        for (int i = 0; i < 1020; i++) {
            this.fillBuffer[i] = 0;
        }
    }

    private void acceptImageData(int i, WifiCameraPic wifiCameraPic) {
        if (wifiCameraPic.data.length == 0) {
            return;
        }
        this.mAoaDeviceCameraData = wifiCameraPic;
        physicalKeysHandler();
        Bitmap bitmapByteToBitmap = ImgUtil.byteToBitmap(this.mAoaDeviceCameraData.data);
        if (bitmapByteToBitmap == null) {
            LogWD.writeMsg(this, 2, "数据可能会不对");
            return;
        }
        Bitmap bitmapCuttingZoom = Constant.CAMERASHOW_IS_DETAIL ? ImgUtil.cuttingZoom(bitmapByteToBitmap, 0.75f) : bitmapByteToBitmap;
        if (this.mAoaDeviceCameraData.hasg == 1) {
            LogWD.writeMsg(this, 2, "陀螺仪角度处理");
            if (CameraConstant.GYROSCOPE_SWITCH) {
                Bitmap bitmapCropBitmap = ImgUtil.cropBitmap(bitmapCuttingZoom);
                if ("BK7231U-XRH-FBPRO".equals(this.wifiCameraFirmInfo.getproduct())) {
                    this.mLastAngle = this.mAoaDeviceCameraData.angle + 180.0f;
                } else if (FunctionSwitch.IS_ROTING_90) {
                    this.mLastAngle = this.mAoaDeviceCameraData.angle + 90.0f;
                } else {
                    this.mLastAngle = this.mAoaDeviceCameraData.angle;
                }
                Bitmap bitmapRotateBitmap2 = ImgUtil.rotateBitmap2(bitmapCropBitmap, this.mLastAngle + FunctionSwitch.angleValue);
                bitmapCuttingZoom = FunctionSwitch.IS_FBPRO ? ImgUtil.toRoundBitmapMask(bitmapRotateBitmap2) : ImgUtil.toRoundBitmap(bitmapRotateBitmap2);
            }
        }
        if (Constant.BITMAP_IS_SCALE) {
            bitmapCuttingZoom = ImgUtil.fzBitmap(bitmapCuttingZoom, false);
        }
        Constant.BITMAP_WIDTH = bitmapByteToBitmap.getWidth();
        Constant.BITMAP_HEIGHT = bitmapByteToBitmap.getHeight();
        showBitmap(bitmapCuttingZoom);
    }

    private void deviceOnline() {
        LogWD.writeMsg(this, 8, "设备上线成功");
        this.wifiCameraFirmInfo = new WifiCameraFirmInfo();
        if (WifiCameraApi.getInstance().gWifiCamera.cameraFirmInfoGet(this.wifiCameraFirmInfo) == 0) {
            String str = this.wifiCameraFirmInfo.getssid();
            LogWD.writeMsg(this, 8, "从设备获取到的设备ssid：  " + str);
            this.SDKCallbackOnlineStatus = 2;
            this.currentDeviceSsid = str;
            if (this.mOnlineStatus != 33) {
                deivceDefaultWith(this.wifiCameraFirmInfo);
                cameraOnlineOrOffline(33);
                return;
            }
            return;
        }
        this.SDKCallbackOnlineStatus = 3;
        this.mOnlineStatus = 2;
        cameraOnlineOrOffline(2);
    }

    private void deivceDefaultWith(WifiCameraFirmInfo wifiCameraFirmInfo) {
        Log.d("liusheng", wifiCameraFirmInfo.getvendor() + "  ----  " + wifiCameraFirmInfo.getproduct());
        FunctionSwitch.offsetValue = 0;
        FunctionSwitch.angleValue = 0;
        FunctionSwitch.IS_VOICE_DEVICE = false;
        FunctionSwitch.IS_FBPRO = false;
        FunctionSwitch.IS_ROTING_90 = true;
        if ("Ocenus".equals(wifiCameraFirmInfo.getvendor())) {
            FunctionSwitch.IS_VOICE_DEVICE = true;
        }
        FunctionSwitch.IS_FBPRO = true;
        if ("TX816_ICM_007".equals(wifiCameraFirmInfo.getproduct())) {
            FunctionSwitch.IS_ROTING_90 = false;
        }
        WifiCameraApi.getInstance().gWifiCamera.setCameraTimeout(3);
        WifiScreenParameters wifiScreenParameters = new WifiScreenParameters();
        int iScreenParametersGet = WifiCameraApi.getInstance().gWifiCamera.screenParametersGet(wifiScreenParameters);
        Log.d("liusheng", "获取偏移量 code:" + iScreenParametersGet);
        LogWD.writeMsg(this, 8, "获取偏移量 code:" + iScreenParametersGet);
        if (iScreenParametersGet == 0) {
            Log.d("liusheng", "offsetValue :" + wifiScreenParameters.GetWide() + "  angleValue: " + wifiScreenParameters.GetAngle());
            LogWD.writeMsg(this, 8, "offsetValue :" + wifiScreenParameters.GetWide() + "  angleValue: " + wifiScreenParameters.GetAngle());
            FunctionSwitch.offsetValue = wifiScreenParameters.GetWide();
            FunctionSwitch.angleValue = wifiScreenParameters.GetAngle();
        }
    }

    private void gyrocopeAngleHandler() {
        float f = this.mAoaDeviceCameraData.angle;
        LogWD.writeMsg(this, 2, "sendAllEventResponse2GyroscopeObserver angle: " + f);
        gyroscopeChangeAngle(f, f - this.mLastAngle);
    }

    private void physicalKeysHandler() {
        if (this.mAoaDeviceCameraData.picbutton == 1) {
            Log.d("liusheng", "物理拍摄按键");
            LogWD.writeMsg(this, 2, "物理拍摄按键");
            acceptKeyPressStatus(20);
        }
        if (this.mAoaDeviceCameraData.videobutton == 1) {
            if (!this.IS_RECODEER) {
                LogWD.writeMsg(this, 2, "物理录制按键 开始");
                acceptKeyPressStatus(21);
            } else {
                LogWD.writeMsg(this, 2, "物理录制按键 保存");
                acceptKeyPressStatus(22);
            }
        }
        if (this.mAoaDeviceCameraData.zoomdown == 1) {
            Log.d("liusheng", "物理 缩小 按键");
            LogWD.writeMsg(this, 2, "物理缩小按键");
            acceptKeyPressStatus(24);
        }
        if (this.mAoaDeviceCameraData.zoomup == 1) {
            Log.d("liusheng", "物理 放大 按键");
            LogWD.writeMsg(this, 2, "物理放大按键");
            acceptKeyPressStatus(23);
        }
    }

    private boolean acceptWifiLicense() {
        WifiCameraLicInfo wifiCameraLicInfo = new WifiCameraLicInfo();
        int iCameraLicInfoGet = WifiCameraApi.getInstance().gWifiCamera.cameraLicInfoGet(wifiCameraLicInfo);
        LogWD.writeMsg(this, 2, "errorCode = " + iCameraLicInfoGet);
        if (iCameraLicInfoGet == 0) {
            return wifiLicenseCheck(wifiCameraLicInfo);
        }
        int iCameraLicInfoGet2 = WifiCameraApi.getInstance().gWifiCamera.cameraLicInfoGet(wifiCameraLicInfo);
        LogWD.writeMsg(this, 2, "errorCode = " + iCameraLicInfoGet2);
        if (iCameraLicInfoGet2 == 0) {
            return wifiLicenseCheck(wifiCameraLicInfo);
        }
        int iCameraLicInfoGet3 = WifiCameraApi.getInstance().gWifiCamera.cameraLicInfoGet(wifiCameraLicInfo);
        LogWD.writeMsg(this, 2, "errorCode = " + iCameraLicInfoGet3);
        if (iCameraLicInfoGet3 == 0) {
            return wifiLicenseCheck(wifiCameraLicInfo);
        }
        FunctionSwitch.isCheckError = true;
        return true;
    }

    private boolean wifiLicenseCheck(WifiCameraLicInfo wifiCameraLicInfo) {
        if (CameraManager.getInstance().isSijiVendor()) {
            return true;
        }
        this.wifiCameraLicInfo = wifiCameraLicInfo;
        FunctionSwitch.isCheckError = true;
        StorageDeviceCommand storageDeviceCommand = UStorageDeviceModule.getInstance().gStorageCommandHandle;
        byte[] license = wifiCameraLicInfo.getLicense();
        int length = wifiCameraLicInfo.getLicense().length;
        WifiCameraFirmInfo wifiCameraFirmInfo = this.wifiCameraFirmInfo;
        int iCheckLic10 = storageDeviceCommand.checkLic10(license, length, wifiCameraFirmInfo == null ? "" : wifiCameraFirmInfo.getproduct());
        Log.d("liusheng", "license校验结果  code : " + iCheckLic10);
        LogWD.writeMsg(this, 2, "license校验结果  code : " + iCheckLic10);
        if (iCheckLic10 == 0) {
            return true;
        }
        String strCheckLic15 = I4ToolApi.getInstance().gI4Tool.checkLic15(wifiCameraLicInfo.getLicense(), wifiCameraLicInfo.getLicense().length);
        LogWD.writeMsg(this, 16777215, "license校验结果  licStr : " + strCheckLic15);
        boolean zLicProcessBegin = ReportLicInstance.getInstance().licProcessBegin(this.mContext, strCheckLic15, wifiCameraLicInfo.getSn(), this.wifiCameraFirmInfo.getmac(), this.wifiCameraFirmInfo.getvendor(), this.wifiCameraFirmInfo.getproduct(), this.wifiCameraFirmInfo.getversion(), this);
        Log.d("liusheng", "license校验结果  enable : " + zLicProcessBegin);
        return zLicProcessBegin;
    }

    @Override
    public void reportReult(boolean z) {
        int i = z ? 33 : 31;
        int iCameraCheckOnline = WifiCameraApi.getInstance().gWifiCamera.cameraCheckOnline();
        if (i == this.mOnlineStatus || iCameraCheckOnline != 0) {
            return;
        }
        cameraOnlineOrOffline(i);
    }

    @Override
    public void cameraCheckOnline() {
        int iCameraCheckOnline = WifiCameraApi.getInstance().gWifiCamera.cameraCheckOnline();
        LogWD.writeMsg(this, 16777215, "cameraCheckOnline  online : " + iCameraCheckOnline);
        ReportLicInstance.getInstance().cameraCheckOnlineBack(iCameraCheckOnline, this.wifiCameraLicInfo.getSn(), this.wifiCameraFirmInfo.getmac());
    }

    @Override
    public void brunLicBegin(String str) {
        String sn = this.wifiCameraLicInfo.getSn();
        byte[] bArr = new byte[30];
        if (I4ToolApi.getInstance().gI4Tool.getLic15(bArr, bArr.length, str) >= 0) {
            int iCameraSetLic = WifiCameraApi.getInstance().gWifiCamera.cameraSetLic(sn, bArr, bArr.length);
            LogWD.writeMsg(this, 16777215, "brunLicBegin  code : " + iCameraSetLic);
            if (iCameraSetLic == 0) {
                acceptWifiLicense();
            }
        }
    }

    @Override
    public void initProgrammeSdk(Context context) {
        LogWD.writeMsg(this, 8, "四季方案初始化");
        Log.d("liusheng", "四季方案初始化");
        this.mContext = context;
        connectDev2IP();
    }

    private void connectDev2IP() {
        WifiCameraApi.getInstance().init(this.mContext);
        WifiCameraApi.getInstance().gWifiCamera.caStart();
        WifiCameraApi.getInstance().audiostart();
    }

    @Override
    public void cameraOnlineOrOffline(int i) {
        if (i == 33) {
            if (!FunctionSwitch.isCanGetSsid) {
                FunctionSwitch.isCanGetSsid = true;
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    WifiCameraLedStatus wifiCameraLedStatus = new WifiCameraLedStatus();
                    wifiCameraLedStatus.ledStatus = 1;
                    wifiCameraLedStatus.lightValue = 100;
                    LogWD.writeMsg(this, 16, "上线打开led灯 : " + WifiCameraApi.getInstance().gWifiCamera.cameraLedStatusSet(wifiCameraLedStatus, 1));
                }
            }).start();
        } else if (i == 31) {
            FunctionSwitch.isCanGetSsid = UtilTools.checkGetSsidConditions(MainFrameHandleInstance.getInstance().getmCurrentContext());
            this.wifiCameraFirmInfo = null;
            this.lastWifiCameraStatusInfo = null;
            sendFinishRecoder();
            FunctionSwitch.IS_VOICE_DEVICE = false;
            FunctionSwitch.IS_FBPRO = false;
        }
        int i2 = this.mOnlineStatus;
        if (i2 != 2) {
            i2 = i;
        }
        this.mOnlineStatus = i2;
        this.mCameraEventObserver.cameraOnlineOrOffline(i, 5);
    }

    @Override
    public void licenseCheck() {
        boolean zAcceptWifiLicense = acceptWifiLicense();
        LogWD.writeMsg(this, 2, "licenseCheckCommand： = " + zAcceptWifiLicense);
        this.mCameraEventObserver.sendAllEventResponse2LicenseCheckObserver(zAcceptWifiLicense, 5);
    }

    @Override
    public void showBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
        videoRecorder(bitmap);
        this.mCameraEventObserver.sendAllEventResponse2ShowBitmapObserver(bitmap, 5);
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
        this.mCameraEventObserver.sendAllEventResponse2GyroscopeObserver(f, f3, 5);
    }

    @Override
    public void acceptKeyPressStatus(int i) {
        LogWD.writeMsg(this, 2, "acceptKeyPressStatus keyType: " + i);
        this.mCameraEventObserver.sendAllEventKeyPressStatus2ResolutionObserver(i, 5);
    }

    @Override
    public void acceptThreshold(int i) {
        LogWD.writeMsg(this, 2, "sendAllEventResponse2ThresoldObserver threshold: " + i);
        if (this.mIsThreshold) {
            this.mCameraEventObserver.sendAllEventResponse2ThresoldObserver(i, 5);
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
        this.mCameraEventObserver.sendAllEventResponse2BatteryAndChargingObserver(i, f, i2);
    }

    @Override
    public void sendLongTimePrompt(boolean z) {
        this.mCameraEventObserver.sendAllEventResponse2LongTimeObserver(z);
    }

    @Override
    public void sendWifiCameraStatusInfo(int i, WifiCameraStatusInfo wifiCameraStatusInfo) {
        this.mCameraEventObserver.sendAllEventResponse2DeviceInfoObserver(wifiCameraStatusInfo);
    }

    private void sendDeviceSeizeStatusChanged(WifiCameraStatusInfo wifiCameraStatusInfo) {
        this.mCameraEventObserver.sendDeviceSeizeStatusChangedObserver(wifiCameraStatusInfo);
    }

    @Override
    public void acceptFwInfo() {
        new Thread() {
            @Override
            public void run() {
                int iCameraFirmInfoGet;
                CameraFirmInfo cameraFirmInfo = new CameraFirmInfo();
                if (I4seasonCameraNew.this.wifiCameraFirmInfo == null) {
                    I4seasonCameraNew.this.wifiCameraFirmInfo = new WifiCameraFirmInfo();
                    iCameraFirmInfoGet = WifiCameraApi.getInstance().gWifiCamera.cameraFirmInfoGet(I4seasonCameraNew.this.wifiCameraFirmInfo);
                } else {
                    iCameraFirmInfoGet = -99;
                }
                LogWD.writeMsg(this, 2, "cameraFirmInfoGet errorCode = " + iCameraFirmInfoGet);
                cameraFirmInfo.setSsid(I4seasonCameraNew.this.wifiCameraFirmInfo.getssid());
                cameraFirmInfo.setVersion(I4seasonCameraNew.this.wifiCameraFirmInfo.getversion());
                cameraFirmInfo.setVendor(I4seasonCameraNew.this.wifiCameraFirmInfo.getvendor());
                cameraFirmInfo.setProduct(I4seasonCameraNew.this.wifiCameraFirmInfo.getproduct());
                WifiCameraLicInfo wifiCameraLicInfo = new WifiCameraLicInfo();
                int iCameraLicInfoGet = WifiCameraApi.getInstance().gWifiCamera.cameraLicInfoGet(wifiCameraLicInfo);
                cameraFirmInfo.setLicense(wifiCameraLicInfo.getLicense());
                cameraFirmInfo.setSn(wifiCameraLicInfo.getSn());
                I4seasonCameraNew.this.mCameraEventObserver.sendAllEventResponse2FWObserver(iCameraLicInfoGet == 0, cameraFirmInfo, 5);
            }
        }.start();
    }

    @Override
    public void takePhoto(Bitmap bitmap) {
        LogWD.writeMsg(this, 16, "take photo");
        UtilTools.playTakePhotoAudio(this.mContext);
        this.pool.execute(new Runnable() {
            /* JADX WARN: Removed duplicated region for block: B:7:0x0017  */
            @Override
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public void run() {
                boolean z;
                if (I4seasonCameraNew.this.mBitmap != null) {
                    I4seasonCameraNew i4seasonCameraNew = I4seasonCameraNew.this;
                    z = i4seasonCameraNew.isSavePhotoSuccful(i4seasonCameraNew.mBitmap);
                }
                I4seasonCameraNew.this.mCameraEventObserver.sendAllTakePhotoRecoder2FWObserver(z, I4seasonCameraNew.this.FILE_SAVE_PATH, 1);
            }
        });
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
        try {
            if (bitmap.getWidth() == bitmap.getHeight()) {
                bitmap = ImgUtil.zoomBitmapNoRecyled(bitmap, 2300, 2300);
            }
            String strReplace = UtilTools.getMsecTimeFromLong(System.currentTimeMillis()).replace(Constant.SMB_COLON, "_");
            String saveCameraDataPath2AndroidO = AppPathInfo.getSaveCameraDataPath2AndroidO();
            String str = "Photo_" + strReplace + Constant.SAVE_PHOTO_SUFFIX;
            if (!new File(saveCameraDataPath2AndroidO).exists()) {
                UtilTools.createFolderInSdcard(saveCameraDataPath2AndroidO);
            }
            this.FILE_SAVE_PATH = saveCameraDataPath2AndroidO + File.separator + str;
            boolean zBytesToImageBitmap = FileUtil.bytesToImageBitmap(bitmap, this.FILE_SAVE_PATH);
            if (zBytesToImageBitmap) {
                if (this.mMs == null) {
                    this.mMs = new MediaScannerConnection(this.mContext, this.connectionClient);
                }
                this.mMs.connect();
            }
            LogWD.writeMsg(this, 16, "take photo save: " + zBytesToImageBitmap);
            return zBytesToImageBitmap;
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
        String saveCameraDataPath2AndroidO = AppPathInfo.getSaveCameraDataPath2AndroidO();
        String str = "Video_" + strReplace;
        if (!new File(saveCameraDataPath2AndroidO).exists()) {
            UtilTools.createFolderInSdcard(saveCameraDataPath2AndroidO);
        }
        this.FILE_SAVE_PATH = saveCameraDataPath2AndroidO + File.separator + str;
        this.mVideoSavePath = this.FILE_SAVE_PATH;
        int width = this.mBitmap.getWidth();
        int height = this.mBitmap.getHeight();
        RecordParams recordParams = new RecordParams();
        recordParams.setRecordPath(this.mVideoSavePath);
        recordParams.setRecordDuration(0);
        recordParams.setVoiceClose(true);
        if (FunctionSwitch.IS_VOICE_DEVICE) {
            recordParams.setVoiceClose(false);
            recordParams.setExternalVoiceData(true);
        }
        RecodeInstans.getInstance().startRecording(recordParams, width, height, 16, new RecodeInstans.RecodeEndInterface() {
            @Override
            public void initMediaCodecFaild(int i, int i2, int i3, int i4) {
            }

            @Override
            public void recoderEnd(String str2) {
                I4seasonCameraNew.this.FILE_SAVE_PATH = str2;
                if (FileUtil.checkVideoFile(str2)) {
                    if (I4seasonCameraNew.this.mMs == null) {
                        I4seasonCameraNew i4seasonCameraNew = I4seasonCameraNew.this;
                        i4seasonCameraNew.mMs = new MediaScannerConnection(i4seasonCameraNew.mContext, I4seasonCameraNew.this.connectionClient);
                    }
                    I4seasonCameraNew.this.mMs.connect();
                    I4seasonCameraNew.this.mCameraEventObserver.sendAllTakePhotoRecoder2FWObserver(true, I4seasonCameraNew.this.FILE_SAVE_PATH, 2);
                    return;
                }
                Log.d("liusheng", "获取不到视频的分辨率  一般是视频出问题了 这里删除文件走录制失败处理");
                new File(str2).delete();
                I4seasonCameraNew.this.mCameraEventObserver.sendAllTakePhotoRecoder2FWObserver(false, "", 2);
            }
        });
        this.IS_RECODEER = true;
        LogWD.writeMsg(this, 16, "startAviVideoRecord: ");
        WifiCameraApi.getInstance().gWifiCamera.cameraRecordStatusSync(1);
    }

    @Override
    public void onRecoderAddFrameFinish(boolean z) throws Throwable {
        LogWD.writeMsg(this, 16, "onRecoderAddFrameFinish: " + z);
        if (!FileUtil.checkVideoFile(this.mVideoSavePath)) {
            new File(this.mVideoSavePath).delete();
            this.mCameraEventObserver.sendAllTakePhotoRecoder2FWObserver(false, "", 2);
            return;
        }
        if (Build.VERSION.SDK_INT >= 29) {
            File file = new File(this.mVideoSavePath);
            FileUtil.copyPrivateToPictures(this.mContext, file.getAbsolutePath(), file.getName());
            file.delete();
            this.FILE_SAVE_PATH = AppPathInfo.getSaveCameraDataPath2AndroidO() + File.separator + file.getName();
        }
        this.mCameraEventObserver.sendAllTakePhotoRecoder2FWObserver(z, this.FILE_SAVE_PATH, 2);
    }

    private void videoRecorder(Bitmap bitmap) {
        if (this.IS_RECODEER) {
            RecodeInstans.getInstance().setFram(bitmap);
        }
    }

    private void sendFinishRecoder() {
        if (this.IS_RECODEER) {
            this.IS_RECODEER = false;
            RecodeInstans.getInstance().stopRecording();
            LogWD.writeMsg(this, 16, "sendFinishRecoder: ");
            WifiCameraApi.getInstance().gWifiCamera.cameraRecordStatusSync(0);
        }
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
        WifiCameraStatusInfo wifiCameraStatusInfo = this.lastWifiCameraStatusInfo;
        if (wifiCameraStatusInfo != null) {
            return wifiCameraStatusInfo;
        }
        WifiCameraStatusInfo wifiCameraStatusInfo2 = new WifiCameraStatusInfo();
        if (WifiCameraApi.getInstance().gWifiCamera.cameraStatusInfoGet(wifiCameraStatusInfo2) == 0) {
            return wifiCameraStatusInfo2;
        }
        return null;
    }

    public Bitmap getmBitmap() {
        return this.mBitmap;
    }

    private Bitmap rotateBitmap(Bitmap bitmap, double d) {
        int i;
        int i2;
        Bitmap bitmap2;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int i3 = width / 2;
        int i4 = height / 2;
        int i5 = i3 < i4 ? i3 : i4;
        double d2 = (d / 360.0d) * 6.283185307179586d;
        double dCos = Math.cos(d2);
        double dSin = Math.sin(d2);
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, new Matrix(), true);
        for (int i6 = 0; i6 < width; i6++) {
            int i7 = 0;
            while (i7 < height) {
                int i8 = i6 - i3;
                int i9 = i7 - i4;
                if ((i8 * i8) + (i9 * i9) > i5 * i5) {
                    bitmapCreateBitmap.setPixel(i6, i7, bitmap.getPixel(i6, i7));
                    bitmap2 = bitmapCreateBitmap;
                    i2 = width;
                    i = height;
                } else {
                    double d3 = i8;
                    int i10 = width;
                    i = height;
                    double d4 = i9;
                    i2 = i10;
                    bitmap2 = bitmapCreateBitmap;
                    bitmap2.setPixel(i6, i7, bitmap.getPixel(((int) ((d3 * dCos) + (d4 * dSin))) + i3, ((int) ((d4 * dCos) - (d3 * dSin))) + i4));
                }
                i7++;
                bitmapCreateBitmap = bitmap2;
                width = i2;
                height = i;
            }
        }
        return bitmapCreateBitmap;
    }

    public String getCurrentDeviceSsid() {
        return this.currentDeviceSsid;
    }

    public WifiCameraFirmInfo getWifiCameraFirmInfo() {
        return this.wifiCameraFirmInfo;
    }

    public boolean hasAngle() {
        WifiCameraPic wifiCameraPic = this.mAoaDeviceCameraData;
        return wifiCameraPic != null && wifiCameraPic.hasg == 1;
    }
}
