package com.i4season.bkCamera.logicrelated.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.i4season.bkCamera.logicrelated.camera.CameraEventObserver;
import com.i4season.bkCamera.logicrelated.camera.bean.CameraFirmInfo;
import com.i4season.bkCamera.logicrelated.camera.i4season.I4seasonCamera;
import com.i4season.bkCamera.logicrelated.camera.i4season_new.I4seasonCameraNew;
import com.i4season.bkCamera.logicrelated.ipchange.IpListenerDelagate;
import com.i4season.bkCamera.uirelated.other.FunctionSwitch;
import com.i4season.bkCamera.uirelated.other.logmanage.LogWD;
import com.jni.AOADeviceHandle.AOADeviceCameraData;
import com.jni.CallBack.UCallBack;
import com.jni.CallBack.WifiCallBack;
import com.jni.UStorageDeviceModule;
import com.jni.WifiCameraInfo.WifiCameraFirmInfo;
import com.jni.WifiCameraInfo.WifiCameraPic;
import com.jni.WifiCameraInfo.WifiCameraStatusInfo;

public class CameraManager implements WifiCallBack.DeviceStatusInterface, UCallBack.DeviceStatusInterface, IpListenerDelagate {
    public static int mProgrammeType;
    long lastTime;
    private UsbManager mUsbManager;
    public CameraFirmInfo mWifiCameraFirmInfo;
    private int DEFALT_STATUS = 31;
    long maxTime = 300;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 101:
                    int iIntValue = ((Integer) message.obj).intValue();
                    int i = message.arg1;
                    Log.d("liusheng", "上下线  ismIsOnline   " + iIntValue + "  programmeType = " + i + "    mProgrammeType: " + CameraManager.mProgrammeType);
                    StringBuilder sb = new StringBuilder();
                    sb.append("上下线 programmeType = ");
                    sb.append(i);
                    sb.append("    mProgrammeType: ");
                    sb.append(CameraManager.mProgrammeType);
                    LogWD.writeMsg(this, 2, sb.toString());
                    CameraManager.this.online2LicenseChenck(iIntValue, i);
                    break;
                case 102:
                    boolean zBooleanValue = ((Boolean) message.obj).booleanValue();
                    int i2 = message.arg1;
                    LogWD.writeMsg(this, 8, "license校验结果返回 isSuccessful： " + zBooleanValue);
                    CameraManager.this.licenseCheckHandler(zBooleanValue, i2);
                    break;
                case 103:
                    LogWD.writeMsg(this, 8, "获取固件信息 ");
                    CameraManager.this.mWifiCameraFirmInfo = (CameraFirmInfo) message.obj;
                    break;
                case 104:
                    LogWD.writeMsg(this, 8, "上线错误信息 ");
                    CameraManager.this.DEFALT_STATUS = ((Integer) message.obj).intValue();
                    break;
                case 105:
                    LogWD.writeMsg(this, 8, "当前方案设备掉线 查看是否掉线还是更新上线设备信息 ");
                    if (CameraManager.mProgrammeType == message.arg1) {
                        LogWD.writeMsg(this, 8, "当前方案设备掉线 掉线 ");
                        CameraManager.mProgrammeType = 0;
                        CameraManager.this.DEFALT_STATUS = 31;
                        CameraManager cameraManager = CameraManager.this;
                        cameraManager.mWifiCameraFirmInfo = null;
                        cameraManager.mCameraEventObserver.sendAllEventResponse2OfflineObserver();
                    }
                    break;
            }
        }
    };
    private CameraEventObserver mCameraEventObserver = new CameraEventObserver(this.mHandler);
    private ICameraProgramme mI4seasonCameraNew = new I4seasonCameraNew(this.mCameraEventObserver);
    private ICameraProgramme mI4seasonCamera = new I4seasonCamera(this.mCameraEventObserver);

    public static class CameraManagerWDHolder {
        public static CameraManager logManager = new CameraManager();
    }

    public void destoryCamera() {
    }

    @Override
    public int sendnotifyfile(int i, int i2, int i3, String str) {
        return 0;
    }

    public static CameraManager getInstance() {
        return CameraManagerWDHolder.logManager;
    }

    public CameraManager() {
        WifiCallBack.getInstance().setDeviceStatusInterface(this);
        UCallBack.getInstance().setDeviceStatusInterface(this);
    }

    public void initProgrammeSdk(final Context context) {
        LogWD.writeMsg(this, 8, "initProgrammeSdk ");
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (CameraManager.this.mI4seasonCameraNew != null) {
                    CameraManager.this.mI4seasonCameraNew.initProgrammeSdk(context);
                }
                if (CameraManager.this.mI4seasonCamera != null) {
                    CameraManager.this.mI4seasonCamera.initProgrammeSdk(context);
                }
            }
        }).start();
    }

    public void resetConnect2DevChange() {
        LogWD.writeMsg(this, 8, "resetConnect2DevChange ");
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (CameraManager.this.mI4seasonCameraNew != null) {
                    CameraManager.this.mI4seasonCameraNew.resetConnect2DevChange();
                }
                if (CameraManager.this.mI4seasonCamera != null) {
                    CameraManager.this.mI4seasonCamera.resetConnect2DevChange();
                }
            }
        }).start();
    }

    public void disconnectDev() {
        LogWD.writeMsg(this, 8, "resetConnect2DevChange ");
        this.mI4seasonCameraNew.disconnectDev();
    }

    @Override
    public void getStatus(int i, int i2) {
        ICameraProgramme iCameraProgramme;
        LogWD.writeMsg(this, 8, "底层回调状态   dtype： " + i + "   status:  " + i2);
        Log.d("liusheng", "底层回调状态   dtype： " + i + "   status:  " + i2);
        if (i == 1) {
            ICameraProgramme iCameraProgramme2 = this.mI4seasonCamera;
            if (iCameraProgramme2 != null) {
                iCameraProgramme2.setStatus(i, i2);
                return;
            }
            return;
        }
        if (i == 2 || i != 3 || (iCameraProgramme = this.mI4seasonCameraNew) == null) {
            return;
        }
        iCameraProgramme.setStatus(i, i2);
    }

    @Override
    public void getData(int i, AOADeviceCameraData aOADeviceCameraData) {
        ICameraProgramme iCameraProgramme = this.mI4seasonCamera;
        if (iCameraProgramme != null) {
            iCameraProgramme.setData(i, aOADeviceCameraData);
        }
    }

    @Override
    public void getData(int i, WifiCameraPic wifiCameraPic) {
        ICameraProgramme iCameraProgramme;
        if (i == 1 || i == 2 || i != 3 || (iCameraProgramme = this.mI4seasonCameraNew) == null) {
            return;
        }
        iCameraProgramme.setData(i, wifiCameraPic);
    }

    @Override
    public void getaudioData(int i, WifiCameraPic wifiCameraPic) {
        ICameraProgramme iCameraProgramme;
        if (i != 3 || (iCameraProgramme = this.mI4seasonCameraNew) == null) {
            return;
        }
        iCameraProgramme.setVoiceData(i, wifiCameraPic);
    }

    @Override
    public void getCameraInfo(int i, WifiCameraStatusInfo wifiCameraStatusInfo) {
        ICameraProgramme iCameraProgramme;
        LogWD.writeMsg(this, 8, "getCameraInfo  dtype： " + i);
        if (i == 1 || i == 2 || i != 3 || (iCameraProgramme = this.mI4seasonCameraNew) == null) {
            return;
        }
        iCameraProgramme.setCameraInfo(i, wifiCameraStatusInfo);
    }

    @Override
    public void onIpChangeListener(String str, String str2) {
        LogWD.writeMsg(this, 8, "ip发生变化  重连设备 ");
        LogWD.writeMsg(this, 8, "调用SDK接口通知ip更改 ");
        UStorageDeviceModule.getInstance().gStorageCommandHandle.caChangeWifi();
        LogWD.writeMsg(this, 8, "ip更改 重连");
        getInstance().resetConnect2DevChange();
    }

    @Override
    public void onEmptyIpListener() {
        LogWD.writeMsg(this, 8, "调用SDK接口通知ip为空 ");
        disconnectDev();
        UStorageDeviceModule.getInstance().gStorageCommandHandle.caZeroIp();
    }

    public boolean isSijiVendor() {
        WifiCameraFirmInfo wifiCameraFirmInfo;
        return mProgrammeType == 5 && (wifiCameraFirmInfo = ((I4seasonCameraNew) this.mI4seasonCameraNew).getWifiCameraFirmInfo()) != null && "YIPINCHENG".equals(wifiCameraFirmInfo.getvendor()) && !TextUtils.isEmpty(wifiCameraFirmInfo.getproduct()) && wifiCameraFirmInfo.getproduct().contains("ICB-4U01");
    }

    public WifiCameraFirmInfo getWifiCameraFirmInfo() {
        if (mProgrammeType == 5) {
            return ((I4seasonCameraNew) this.mI4seasonCameraNew).getWifiCameraFirmInfo();
        }
        return null;
    }

    public CameraFirmInfo getmAoaDeviceFirmInfo() {
        return this.mWifiCameraFirmInfo;
    }

    public WifiCameraStatusInfo getWifiCameraStatusInfo() {
        ICameraProgramme iCameraProgramme;
        int i = mProgrammeType;
        if (i == 5) {
            ICameraProgramme iCameraProgramme2 = this.mI4seasonCameraNew;
            if (iCameraProgramme2 != null) {
                return ((I4seasonCameraNew) iCameraProgramme2).getWifiCameraStatusInfo();
            }
        } else if (i != 2 && i == 1 && (iCameraProgramme = this.mI4seasonCamera) != null) {
            return ((I4seasonCamera) iCameraProgramme).getWifiCameraStatusInfo();
        }
        return null;
    }

    public void setOnlineStatus2WifiChange() {
        mProgrammeType = 0;
        this.mWifiCameraFirmInfo = null;
        ICameraProgramme iCameraProgramme = this.mI4seasonCameraNew;
        if (iCameraProgramme != null) {
            ((I4seasonCameraNew) iCameraProgramme).setmOnlineStatus(31);
        }
        ICameraProgramme iCameraProgramme2 = this.mI4seasonCamera;
        if (iCameraProgramme2 != null) {
            ((I4seasonCameraNew) iCameraProgramme2).setmOnlineStatus(31);
        }
    }

    public int getDeviceOnlineStatus() {
        ICameraProgramme iCameraProgramme;
        LogWD.writeMsg(this, 8, "getDeviceOnlineStatus mProgrammeType： " + mProgrammeType);
        int i = mProgrammeType;
        if (i == 5) {
            ICameraProgramme iCameraProgramme2 = this.mI4seasonCameraNew;
            if (iCameraProgramme2 != null) {
                return ((I4seasonCameraNew) iCameraProgramme2).ismOnlineStatus();
            }
        } else if (i != 2 && i == 1 && (iCameraProgramme = this.mI4seasonCamera) != null) {
            return ((I4seasonCamera) iCameraProgramme).ismOnlineStatus();
        }
        return this.DEFALT_STATUS;
    }

    public int getSDKCallbackOnlineStatus() {
        ICameraProgramme iCameraProgramme;
        int i = mProgrammeType;
        if (i == 5) {
            ICameraProgramme iCameraProgramme2 = this.mI4seasonCameraNew;
            if (iCameraProgramme2 != null) {
                return ((I4seasonCameraNew) iCameraProgramme2).getSDKCallbackOnlineStatus();
            }
            return 0;
        }
        if (i != 1 || (iCameraProgramme = this.mI4seasonCamera) == null) {
            return 0;
        }
        return ((I4seasonCamera) iCameraProgramme).getSDKCallbackOnlineStatus();
    }

    public String getCurrentDeviceWifiSsid() {
        ICameraProgramme iCameraProgramme;
        int i = mProgrammeType;
        if (i != 5) {
            return (i != 1 || (iCameraProgramme = this.mI4seasonCamera) == null) ? "" : ((I4seasonCamera) iCameraProgramme).getCurrentDeviceSsid();
        }
        ICameraProgramme iCameraProgramme2 = this.mI4seasonCameraNew;
        return iCameraProgramme2 != null ? ((I4seasonCameraNew) iCameraProgramme2).getCurrentDeviceSsid() : "";
    }

    public Bitmap getCurrentCatchBitmap() {
        ICameraProgramme iCameraProgramme;
        int i = mProgrammeType;
        if (i == 5) {
            ICameraProgramme iCameraProgramme2 = this.mI4seasonCameraNew;
            if (iCameraProgramme2 != null) {
                return ((I4seasonCameraNew) iCameraProgramme2).getmBitmap();
            }
            return null;
        }
        if (i != 1 || (iCameraProgramme = this.mI4seasonCamera) == null) {
            return null;
        }
        return ((I4seasonCamera) iCameraProgramme).getmBitmap();
    }

    public void online2LicenseChenck(int i, int i2) {
        LogWD.writeMsg(this, 2, "首次使用或与当前使用的一致才处理");
        if (i == 33) {
            mProgrammeType = i2;
            LogWD.writeMsg(this, 2, "上线");
            licenseCheckCommand(i2);
        } else if (i == 31) {
            LogWD.writeMsg(this, 2, "下线");
            mProgrammeType = 0;
            this.DEFALT_STATUS = 31;
            this.mWifiCameraFirmInfo = null;
        }
    }

    public void licenseCheckHandler(boolean z, int i) {
        if (z) {
            FunctionSwitch.Lic_Need_Online_Burning = false;
            this.mCameraEventObserver.sendLicenseCheckSuccessfulObserver();
        } else if (FunctionSwitch.isCheckError) {
            FunctionSwitch.isCheckError = false;
            FunctionSwitch.Lic_Need_Online_Burning = true;
            this.mCameraEventObserver.sendLicenseCheckErrorAndOnlineObserver();
        }
    }

    private void licenseCheckCommand(int i) {
        if (CameraConstant.LICENSE_CHECK) {
            LogWD.writeMsg(this, 2, "校验开关 打开");
            mProgrammeType = i;
            FunctionSwitch.Lic_Need_Online_Burning = false;
            FunctionSwitch.isCheckError = false;
            if (i == 5) {
                this.mI4seasonCameraNew.licenseCheck();
                LogWD.writeMsg(this, 2, "licenseCheck： ");
                return;
            } else {
                if (i != 2 && i == 1) {
                    this.mI4seasonCamera.licenseCheck();
                    LogWD.writeMsg(this, 2, "licenseCheck： ");
                    return;
                }
                return;
            }
        }
        LogWD.writeMsg(this, 2, "校验开关 关闭");
        this.mCameraEventObserver.sendLicenseCheckSuccessfulObserver();
    }

    public void gyroscopeSwitch(boolean z) {
        int i = mProgrammeType;
    }

    public void mirrorSwitch(boolean z) {
        int i = mProgrammeType;
    }

    public boolean deviceHasAngle() {
        if (mProgrammeType == 5) {
            return ((I4seasonCameraNew) this.mI4seasonCameraNew).hasAngle();
        }
        return false;
    }

    public void acceptFwInfo(CameraEventObserver.OnAcceptFwInfoListener onAcceptFwInfoListener) {
        addEventObserverListenser(1, onAcceptFwInfoListener);
        int i = mProgrammeType;
        if (i == 5) {
            this.mI4seasonCameraNew.acceptFwInfo();
        } else if (i != 2 && i == 1) {
            this.mI4seasonCamera.acceptFwInfo();
        }
    }

    public void takePhoto(Bitmap bitmap, CameraEventObserver.OnTakePhotoOrRecoderListener onTakePhotoOrRecoderListener) {
        addEventObserverListenser(3, onTakePhotoOrRecoderListener);
        int i = mProgrammeType;
        if (i == 5) {
            this.mI4seasonCameraNew.takePhoto(bitmap);
        } else if (i != 2 && i == 1) {
            this.mI4seasonCamera.takePhoto(bitmap);
        }
    }

    public void addTakePhotoAllListener(CameraEventObserver.OnTakePhotoAllFinishListener onTakePhotoAllFinishListener) {
        addEventObserverListenser(14, onTakePhotoAllFinishListener);
    }

    public void removeTakePhotoAllListener(CameraEventObserver.OnTakePhotoAllFinishListener onTakePhotoAllFinishListener) {
        removeEventObserverListenser(14, onTakePhotoAllFinishListener);
    }

    public boolean isTakePhotoAll() {
        return this.mCameraEventObserver.getPhotoIndex() == 0;
    }

    public void setPhotoIndex(int i) {
        this.mCameraEventObserver.setPhotoIndex(i);
    }

    public void startVideoRecoder() {
        int i = mProgrammeType;
        if (i == 5) {
            this.mI4seasonCameraNew.startVideoRecoder();
        } else if (i != 2 && i == 1) {
            this.mI4seasonCamera.startVideoRecoder();
        }
    }

    public void stopVideoRecoder(CameraEventObserver.OnTakePhotoOrRecoderListener onTakePhotoOrRecoderListener) {
        if (onTakePhotoOrRecoderListener != null) {
            addEventObserverListenser(3, onTakePhotoOrRecoderListener);
        }
        int i = mProgrammeType;
        if (i == 5) {
            this.mI4seasonCameraNew.stopVideoRecoder();
        } else if (i != 2 && i == 1) {
            this.mI4seasonCamera.stopVideoRecoder();
        }
    }

    public void setOnAcceptBitmapListener(CameraEventObserver.OnAcceptBitmapListener onAcceptBitmapListener) {
        addEventObserverListenser(2, onAcceptBitmapListener);
        Bitmap currentCatchBitmap = getCurrentCatchBitmap();
        if (currentCatchBitmap != null) {
            this.mCameraEventObserver.sendAllEventResponse2ShowBitmapObserver(currentCatchBitmap, mProgrammeType);
        }
    }

    public void setOnGyroscopeAngleListener(CameraEventObserver.OnGyroscopeAngleListener onGyroscopeAngleListener) {
        addEventObserverListenser(4, onGyroscopeAngleListener);
    }

    public void setOnOnKeyPhotoOrRecoderListener(CameraEventObserver.OnKeyPhotoOrRecoderListener onKeyPhotoOrRecoderListener) {
        addEventObserverListenser(5, onKeyPhotoOrRecoderListener);
    }

    public void setOnOnKeyZoomListener(CameraEventObserver.OnKeyZoomListener onKeyZoomListener) {
        addEventObserverListenser(6, onKeyZoomListener);
    }

    public void acceptResolutionList(CameraEventObserver.OnResolutionListListener onResolutionListListener) {
        addEventObserverListenser(7, onResolutionListListener);
        int i = mProgrammeType;
        if (i == 5) {
            this.mI4seasonCameraNew.acceptResolutionList();
        } else if (i == 1) {
            this.mI4seasonCamera.acceptResolutionList();
        }
    }

    public void acceptCurrentResolution(CameraEventObserver.OnResolutionListListener onResolutionListListener) {
        addEventObserverListenser(7, onResolutionListListener);
        int i = mProgrammeType;
        if (i == 5) {
            this.mI4seasonCameraNew.acceptCurrentResolution();
        } else if (i == 1) {
            this.mI4seasonCamera.acceptCurrentResolution();
        }
    }

    public void seOnThresholdListener(CameraEventObserver.OnThresholdListener onThresholdListener) {
        addEventObserverListenser(8, onThresholdListener);
    }

    public void setOnBatteryAndChargingListener(CameraEventObserver.OnBatteryAndChargingListener onBatteryAndChargingListener) {
        addEventObserverListenser(9, onBatteryAndChargingListener);
    }

    public void setOnLongTimePromptListener(CameraEventObserver.LongTimePromptListener longTimePromptListener) {
        addEventObserverListenser(11, longTimePromptListener);
    }

    public void setOnOfflineListener(CameraEventObserver.OfflineListener offlineListener) {
        addEventObserverListenser(12, offlineListener);
    }

    public void setOnWifiCameraInfoListener(CameraEventObserver.OnWifiCameraInfoListener onWifiCameraInfoListener) {
        addEventObserverListenser(13, onWifiCameraInfoListener);
    }

    private void addEventObserverListenser(int i, Object obj) {
        this.mCameraEventObserver.addEventListener(i, obj);
    }

    public void removeEventObserverListenser(int i, Object obj) {
        this.mCameraEventObserver.removerEventListener(i, obj);
    }
}
