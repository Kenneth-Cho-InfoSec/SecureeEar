package com.i4season.bkCamera.logicrelated.camera;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.i4season.bkCamera.logicrelated.camera.bean.CameraFirmInfo;
import com.i4season.bkCamera.uirelated.other.MainFrameHandleInstance;
import com.i4season.bkCamera.uirelated.other.logmanage.LogWD;
import com.jni.WifiCameraInfo.WifiCameraStatusInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CameraEventObserver {
    public static final int SEND_ACCEPT_FW = 103;
    public static final int SEND_CAMERA_OFFLINE_COMMAND = 105;
    public static final int SEND_CAMERA_ONLINE_LICENSE_CHECK_COMMAND = 101;
    public static final int SEND_CONNECT_ERROR_RESULT = 104;
    public static final int SEND_LICENS_CHECK_RESULT = 102;
    private Handler mHandler;
    private List<OnAcceptFwInfoListener> onAcceptFwInfoListenerList = new ArrayList();
    private List<OnAcceptBitmapListener> onAcceptBitmapListenerList = new ArrayList();
    private List<OnTakePhotoOrRecoderListener> onTakePhotoOrRecoderListenerList = new ArrayList();
    private List<OnGyroscopeAngleListener> onGyroscopeAngleListenerList = new ArrayList();
    private List<OnKeyPhotoOrRecoderListener> onKeyPhotoOrRecoderListenerList = new ArrayList();
    private List<OnKeyZoomListener> onKeyZoomListenerList = new ArrayList();
    private List<OnResolutionListListener> onResolutionListListenerList = new ArrayList();
    private List<OnThresholdListener> onThresholdListenerList = new ArrayList();
    private List<OnLowerBatteryListener> onLowerBatteryListenerList = new ArrayList();
    private List<OnBatteryAndChargingListener> onBatteryAndChargingListenerList = new ArrayList();
    private List<LongTimePromptListener> longTimePromptListenerList = new ArrayList();
    private List<OfflineListener> mOfflineListenerList = new ArrayList();
    private List<OnWifiCameraInfoListener> mOnWifiCameraInfoListenerList = new ArrayList();
    private List<OnTakePhotoAllFinishListener> takePhotoAllFinishListeners = new ArrayList();
    private AtomicInteger lastPhotoClick = new AtomicInteger(0);
    private int photoCurrentIndex = 0;

    public interface LongTimePromptListener {
        void onShowLongTimePrompt(boolean z);
    }

    public interface OfflineListener {
        void onConnectErrorListener(int i);

        void onConnectingListener();

        void onOfflineListener();

        void onOnlineListener();

        void onUpdataDeviceListener();
    }

    public interface OnAcceptBitmapListener {
        void onAcceptBitmap(Bitmap bitmap);
    }

    public interface OnAcceptFwInfoListener {
        void onOnAcceptFwInfoListener(boolean z, CameraFirmInfo cameraFirmInfo);
    }

    public interface OnBatteryAndChargingListener {
        void onBatteryAndCharging(float f, boolean z);
    }

    public interface OnGyroscopeAngleListener {
        void onGyroscopeAngle(float f, float f2);
    }

    public interface OnKeyPhotoOrRecoderListener {
        void onKeyRecoderBegin();

        void onKeyRecoderEnd();

        void onKeyTakePhoto();
    }

    public interface OnKeyZoomListener {
        void onKeyZoom(boolean z);
    }

    public interface OnLowerBatteryListener {
        void onLowerBattery();
    }

    public interface OnResolutionListListener {
        void resolutionSet(boolean z);
    }

    public interface OnTakePhotoAllFinishListener {
        void onTakePhotoAllFinish();
    }

    public interface OnTakePhotoOrRecoderListener {
        void onTakePhotoOrRecoderListener(boolean z, String str, int i);
    }

    public interface OnThresholdListener {
        void onThreshold(int i);
    }

    public interface OnWifiCameraInfoListener {
        void onDeviceSeizeStatusChanged(WifiCameraStatusInfo wifiCameraStatusInfo);

        void onWifiCameraInfoListener(WifiCameraStatusInfo wifiCameraStatusInfo);
    }

    public CameraEventObserver(Handler handler) {
        this.mHandler = handler;
    }

    public int addAndGetPhotoIndex() {
        return this.lastPhotoClick.addAndGet(1);
    }

    public int getPhotoIndex() {
        return this.lastPhotoClick.get();
    }

    public void setPhotoIndex(int i) {
        this.lastPhotoClick.set(i);
    }

    public void addEventListener(int i, Object obj) {
        switch (i) {
            case 1:
                if (!this.onAcceptFwInfoListenerList.contains(obj)) {
                    this.onAcceptFwInfoListenerList.add((OnAcceptFwInfoListener) obj);
                }
                break;
            case 2:
                if (!this.onAcceptBitmapListenerList.contains(obj)) {
                    this.onAcceptBitmapListenerList.add((OnAcceptBitmapListener) obj);
                }
                break;
            case 3:
                if (!this.onTakePhotoOrRecoderListenerList.contains(obj)) {
                    this.onTakePhotoOrRecoderListenerList.add((OnTakePhotoOrRecoderListener) obj);
                }
                break;
            case 4:
                if (!this.onGyroscopeAngleListenerList.contains(obj)) {
                    this.onGyroscopeAngleListenerList.add((OnGyroscopeAngleListener) obj);
                }
                break;
            case 5:
                if (!this.onKeyPhotoOrRecoderListenerList.contains(obj)) {
                    this.onKeyPhotoOrRecoderListenerList.add((OnKeyPhotoOrRecoderListener) obj);
                }
                break;
            case 6:
                if (!this.onKeyZoomListenerList.contains(obj)) {
                    this.onKeyZoomListenerList.add((OnKeyZoomListener) obj);
                }
                break;
            case 7:
                if (!this.onResolutionListListenerList.contains(obj)) {
                    this.onResolutionListListenerList.add((OnResolutionListListener) obj);
                }
                break;
            case 8:
                if (!this.onThresholdListenerList.contains(obj)) {
                    this.onThresholdListenerList.add((OnThresholdListener) obj);
                }
                break;
            case 9:
                if (!this.onBatteryAndChargingListenerList.contains(obj)) {
                    this.onBatteryAndChargingListenerList.add((OnBatteryAndChargingListener) obj);
                }
                break;
            case 11:
                if (!this.longTimePromptListenerList.contains(obj)) {
                    this.longTimePromptListenerList.add((LongTimePromptListener) obj);
                }
                break;
            case 12:
                if (!this.mOfflineListenerList.contains(obj)) {
                    this.mOfflineListenerList.add((OfflineListener) obj);
                }
                break;
            case 13:
                if (!this.mOnWifiCameraInfoListenerList.contains(obj)) {
                    this.mOnWifiCameraInfoListenerList.add((OnWifiCameraInfoListener) obj);
                }
                break;
            case 14:
                if (!this.takePhotoAllFinishListeners.contains(obj)) {
                    this.takePhotoAllFinishListeners.add((OnTakePhotoAllFinishListener) obj);
                }
                break;
        }
    }

    public void removerEventListener(int i, Object obj) {
        switch (i) {
            case 1:
                this.onAcceptFwInfoListenerList.remove(obj);
                break;
            case 2:
                this.onAcceptBitmapListenerList.remove((OnAcceptBitmapListener) obj);
                break;
            case 3:
                this.onTakePhotoOrRecoderListenerList.remove((OnTakePhotoOrRecoderListener) obj);
                break;
            case 4:
                this.onGyroscopeAngleListenerList.remove((OnGyroscopeAngleListener) obj);
                break;
            case 5:
                this.onKeyPhotoOrRecoderListenerList.remove((OnKeyPhotoOrRecoderListener) obj);
                break;
            case 6:
                this.onKeyZoomListenerList.remove((OnKeyZoomListener) obj);
                break;
            case 7:
                this.onResolutionListListenerList.remove((OnResolutionListListener) obj);
                break;
            case 8:
                this.onThresholdListenerList.remove((OnThresholdListener) obj);
                break;
            case 9:
                this.onBatteryAndChargingListenerList.remove((OnBatteryAndChargingListener) obj);
                break;
            case 11:
                this.longTimePromptListenerList.remove((LongTimePromptListener) obj);
                break;
            case 12:
                this.mOfflineListenerList.remove((OfflineListener) obj);
                break;
            case 13:
                this.mOnWifiCameraInfoListenerList.remove((OnWifiCameraInfoListener) obj);
                break;
            case 14:
                if (this.takePhotoAllFinishListeners.contains(obj)) {
                    this.takePhotoAllFinishListeners.remove((OnTakePhotoAllFinishListener) obj);
                }
                break;
        }
    }

    public void cameraOnlineOrOffline(int i, int i2) {
        if (i == 33) {
            Message messageObtain = Message.obtain();
            messageObtain.what = 101;
            messageObtain.obj = Integer.valueOf(i);
            messageObtain.arg1 = i2;
            this.mHandler.sendMessage(messageObtain);
            sendLicenseCheckSuccessfulObserver();
            return;
        }
        if (i == 32) {
            sendLicenseConnenctingObserver();
            return;
        }
        if (i == 2 || i == 3 || i == 4) {
            Message messageObtain2 = Message.obtain();
            messageObtain2.what = 104;
            messageObtain2.obj = Integer.valueOf(i);
            messageObtain2.arg1 = i2;
            this.mHandler.sendMessage(messageObtain2);
            sendConnenctErrorObserver(i);
            return;
        }
        Message messageObtain3 = Message.obtain();
        messageObtain3.what = 105;
        messageObtain3.obj = Integer.valueOf(i);
        messageObtain3.arg1 = i2;
        this.mHandler.sendMessage(messageObtain3);
    }

    public void sendAllEventResponse2LicenseCheckObserver(boolean z, int i) {
        Log.d("liusheng", "licenseCheck校验： " + z + "  当前方案类型：" + i);
        StringBuilder sb = new StringBuilder();
        sb.append("licenseCheck校验： ");
        sb.append(z);
        LogWD.writeMsg(this, 2, sb.toString());
        Message messageObtain = Message.obtain();
        messageObtain.what = 102;
        messageObtain.obj = Boolean.valueOf(z);
        messageObtain.arg1 = i;
        this.mHandler.sendMessage(messageObtain);
    }

    public void sendLicenseCheckSuccessfulObserver() {
        MainFrameHandleInstance.getInstance().sendRegistSuccfulBoradcastNotify();
        for (int i = 0; i < this.mOfflineListenerList.size(); i++) {
            this.mOfflineListenerList.get(i).onOnlineListener();
        }
    }

    public void sendLicenseConnenctingObserver() {
        MainFrameHandleInstance.getInstance().sendRegistSuccfulBoradcastNotify();
        for (int i = 0; i < this.mOfflineListenerList.size(); i++) {
            this.mOfflineListenerList.get(i).onConnectingListener();
        }
    }

    public void sendConnenctErrorObserver(int i) {
        for (int i2 = 0; i2 < this.mOfflineListenerList.size(); i2++) {
            this.mOfflineListenerList.get(i2).onConnectErrorListener(i);
        }
    }

    public void sendLicenseCheckErrorObserver() {
        for (int i = 0; i < this.mOfflineListenerList.size(); i++) {
            this.mOfflineListenerList.get(i).onConnectErrorListener(0);
        }
    }

    public void sendLicenseCheckErrorAndOnlineObserver() {
        MainFrameHandleInstance.getInstance().sendLicenseCheckErrorAndOnlineBoradcastNotify();
    }

    public void sendAllEventResponse2OfflineObserver() {
        MainFrameHandleInstance.getInstance().sendDeviceOfflineBoradcastNotify();
        for (int i = 0; i < this.mOfflineListenerList.size(); i++) {
            this.mOfflineListenerList.get(i).onOfflineListener();
        }
    }

    public void sendAllEventResponse2UpdeviceObserver() {
        for (int i = 0; i < this.mOfflineListenerList.size(); i++) {
            this.mOfflineListenerList.get(i).onUpdataDeviceListener();
        }
    }

    public void sendAllEventResponse2ShowBitmapObserver(Bitmap bitmap, int i) {
        LogWD.writeMsg(this, 2, "收到帧数据 programmeType = " + i);
        if (this.onAcceptBitmapListenerList.size() > 0) {
            for (int i2 = 0; i2 < this.onAcceptBitmapListenerList.size(); i2++) {
                this.onAcceptBitmapListenerList.get(i2).onAcceptBitmap(bitmap);
            }
        }
    }

    public void sendAllEventResponse2FWObserver(boolean z, CameraFirmInfo cameraFirmInfo, int i) {
        LogWD.writeMsg(this, 2, "mModelName: " + cameraFirmInfo.getproduct());
        Message messageObtain = Message.obtain();
        messageObtain.what = 103;
        messageObtain.obj = cameraFirmInfo;
        this.mHandler.sendMessage(messageObtain);
        if (this.onAcceptFwInfoListenerList.size() > 0) {
            for (int i2 = 0; i2 < this.onAcceptFwInfoListenerList.size(); i2++) {
                this.onAcceptFwInfoListenerList.get(i2).onOnAcceptFwInfoListener(z, cameraFirmInfo);
            }
        }
    }

    public void sendAllEventResponse2GyroscopeObserver(float f, float f2, int i) {
        LogWD.writeMsg(this, 2, "收到陀螺仪 changAngle = " + f2);
        if (this.onGyroscopeAngleListenerList.size() > 0) {
            for (int i2 = 0; i2 < this.onGyroscopeAngleListenerList.size(); i2++) {
                this.onGyroscopeAngleListenerList.get(i2).onGyroscopeAngle(f, f2);
            }
        }
    }

    public void sendAllEventKeyPressStatus2ResolutionObserver(int i, int i2) {
        LogWD.writeMsg(this, 2, "keyType: " + i);
        int i3 = 0;
        if (i == 20 && this.onKeyPhotoOrRecoderListenerList.size() > 0) {
            LogWD.writeMsg(this, 2, "按键拍照: ");
            while (i3 < this.onKeyPhotoOrRecoderListenerList.size()) {
                this.onKeyPhotoOrRecoderListenerList.get(i3).onKeyTakePhoto();
                i3++;
            }
            return;
        }
        if (i == 21 && this.onKeyPhotoOrRecoderListenerList.size() > 0) {
            LogWD.writeMsg(this, 2, "按键录制开始: ");
            while (i3 < this.onKeyPhotoOrRecoderListenerList.size()) {
                this.onKeyPhotoOrRecoderListenerList.get(i3).onKeyRecoderBegin();
                i3++;
            }
            return;
        }
        if (i == 22 && this.onKeyPhotoOrRecoderListenerList.size() > 0) {
            LogWD.writeMsg(this, 2, "按键录制结束: ");
            while (i3 < this.onKeyPhotoOrRecoderListenerList.size()) {
                this.onKeyPhotoOrRecoderListenerList.get(i3).onKeyRecoderEnd();
                i3++;
            }
            return;
        }
        if (i == 23 && this.onKeyZoomListenerList.size() > 0) {
            LogWD.writeMsg(this, 2, "按键放大: ");
            while (i3 < this.onKeyZoomListenerList.size()) {
                this.onKeyZoomListenerList.get(i3).onKeyZoom(true);
                i3++;
            }
            return;
        }
        if (i != 24 || this.onKeyZoomListenerList == null) {
            return;
        }
        LogWD.writeMsg(this, 2, "按键缩小: ");
        for (int i4 = 0; i4 < this.onKeyZoomListenerList.size(); i4++) {
            this.onKeyZoomListenerList.get(i4).onKeyZoom(false);
        }
    }

    public void sendAllTakePhotoRecoder2FWObserver(boolean z, String str, int i) {
        LogWD.writeMsg(this, 2, "sendAllTakePhotoRecoder2FWObserver: " + z);
        if (this.onTakePhotoOrRecoderListenerList.size() > 0) {
            for (int i2 = 0; i2 < this.onTakePhotoOrRecoderListenerList.size(); i2++) {
                this.onTakePhotoOrRecoderListenerList.get(i2).onTakePhotoOrRecoderListener(z, str, i);
            }
        }
        if (i != 1 || this.lastPhotoClick.get() <= 0) {
            return;
        }
        this.lastPhotoClick.decrementAndGet();
        if (this.takePhotoAllFinishListeners.size() > 0 && this.lastPhotoClick.get() == 0) {
            for (int i3 = 0; i3 < this.takePhotoAllFinishListeners.size(); i3++) {
                this.takePhotoAllFinishListeners.get(i3).onTakePhotoAllFinish();
            }
        }
        this.photoCurrentIndex = this.lastPhotoClick.get();
    }

    public void sendAllEventResponse2SetResolutionObserver(boolean z) {
        LogWD.writeMsg(this, 2, "sendAllEventResponse2SetResolutionObserver: " + z);
        if (this.onResolutionListListenerList.size() > 0) {
            for (int i = 0; i < this.onResolutionListListenerList.size(); i++) {
                this.onResolutionListListenerList.get(i).resolutionSet(z);
            }
        }
    }

    public void sendAllEventResponse2ThresoldObserver(int i, int i2) {
        LogWD.writeMsg(this, 2, "sendAllEventResponse2ThresoldObserver: " + i);
        if (this.onThresholdListenerList.size() > 0) {
            for (int i3 = 0; i3 < this.onThresholdListenerList.size(); i3++) {
                this.onThresholdListenerList.get(i3).onThreshold(i);
            }
        }
    }

    public void sendAllEventResponse2LowerBatteryObserver(int i) {
        LogWD.writeMsg(this, 2, "sendAllEventResponse2LowerBatteryObserver： " + i);
        if (this.onLowerBatteryListenerList.size() > 0) {
            for (int i2 = 0; i2 < this.onLowerBatteryListenerList.size(); i2++) {
                this.onLowerBatteryListenerList.get(i2).onLowerBattery();
            }
        }
    }

    public void sendAllEventResponse2BatteryAndChargingObserver(int i, float f, int i2) {
        LogWD.writeMsg(this, 2, "sendAllEventResponse2BatteryAndChargingObserver： " + i);
        if (this.onBatteryAndChargingListenerList != null) {
            for (int i3 = 0; i3 < this.onBatteryAndChargingListenerList.size(); i3++) {
                OnBatteryAndChargingListener onBatteryAndChargingListener = this.onBatteryAndChargingListenerList.get(i3);
                boolean z = true;
                if (i2 != 1) {
                    z = false;
                }
                onBatteryAndChargingListener.onBatteryAndCharging(f, z);
            }
        }
    }

    public void sendAllEventResponse2LongTimeObserver(boolean z) {
        LogWD.writeMsg(this, 2, "sendAllEventResponse2LongTimeObserver： " + z);
        if (this.longTimePromptListenerList != null) {
            for (int i = 0; i < this.longTimePromptListenerList.size(); i++) {
                this.longTimePromptListenerList.get(i).onShowLongTimePrompt(z);
            }
        }
    }

    public void sendAllEventResponse2DeviceInfoObserver(WifiCameraStatusInfo wifiCameraStatusInfo) {
        LogWD.writeMsg(this, 2, "sendAllEventResponse2LongTimeObserver： " + wifiCameraStatusInfo.toString());
        if (this.mOnWifiCameraInfoListenerList != null) {
            for (int i = 0; i < this.mOnWifiCameraInfoListenerList.size(); i++) {
                this.mOnWifiCameraInfoListenerList.get(i).onWifiCameraInfoListener(wifiCameraStatusInfo);
            }
        }
    }

    public void sendDeviceSeizeStatusChangedObserver(WifiCameraStatusInfo wifiCameraStatusInfo) {
        if (this.mOnWifiCameraInfoListenerList != null) {
            for (int i = 0; i < this.mOnWifiCameraInfoListenerList.size(); i++) {
                this.mOnWifiCameraInfoListenerList.get(i).onDeviceSeizeStatusChanged(wifiCameraStatusInfo);
            }
        }
    }
}
