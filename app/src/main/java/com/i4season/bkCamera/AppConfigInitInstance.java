package com.i4season.bkCamera;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.StrictMode;
import com.i4season.bkCamera.logicrelated.camera.CameraManager;
import com.i4season.bkCamera.logicrelated.database.WdOpenHelper;
import com.i4season.bkCamera.logicrelated.ipchange.IpChangManager;
import com.i4season.bkCamera.uirelated.other.AppPathInfo;
import com.i4season.bkCamera.uirelated.other.Language.LanguageInfo;
import com.i4season.bkCamera.uirelated.other.Language.Strings;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.Constant;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.FileUtil;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.SpUtils;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.SystemUtil;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.UtilTools;
import com.i4season.bkCamera.uirelated.other.logmanage.CrashHandler;
import com.i4season.bkCamera.uirelated.other.logmanage.LogWD;
import com.jni.CallBack.WifiCallBackFuc;
import com.jni.UStorageDeviceModule;
import com.jni.WifiCameraApi;
import com.jni.logmanageWifi.LogManagerWD;
import java.io.File;

public class AppConfigInitInstance {
    private WdOpenHelper i4seasonOpenHelper = null;
    private SharedPreferences languageRecord;

    public static class AppConfigurationInitializationInstanceWDHolder {
        public static AppConfigInitInstance appConfigurationInitialization = new AppConfigInitInstance();
    }

    public static AppConfigInitInstance getInstance() {
        return AppConfigurationInitializationInstanceWDHolder.appConfigurationInitialization;
    }

    public void initApplicationInformation(Context context) {
        if (Build.VERSION.SDK_INT >= 24) {
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
        }
        setLogcatSwitch(context);
        initAppVendorValue();
        initMulitLanguage(context);
        createTempCacheDir();
    }

    private void setLogcatSwitch(Context context) {
        if (!new SpUtils(context).getBoolean(Constant.LOGCAT_SWITCH, false)) {
            WifiCameraApi.getInstance().setLog_switch(false);
            LogManagerWD.LOG_SWITCH = 0;
            com.i4season.bkCamera.uirelated.other.logmanage.LogManagerWD.LOG_SWITCH = 0;
        } else {
            WifiCameraApi.getInstance().setLog_switch(true);
            LogManagerWD.LOG_SWITCH = 16777215;
            com.i4season.bkCamera.uirelated.other.logmanage.LogManagerWD.LOG_SWITCH = 16777215;
            UtilTools.showToast(context, "当前日志为开启状态");
        }
        CrashHandler.getInstance().init(context);
    }

    private void initAppVendorValue() {
        AppPathInfo.app_package_name = AppPathInfo.app_camera_cache;
    }

    public void initMulitLanguage(Context context) {
        Strings.initLanguage(context);
        String setLanguage = LanguageInfo.getInstance().getSetLanguage(context);
        String language = Strings.getLanguage();
        LanguageInfo.isAUTO = false;
        if (setLanguage.equals(language) || setLanguage.equals("") || setLanguage.equals(Strings.LANGUAGE_AUTO)) {
            return;
        }
        Strings.setLanguage(context, setLanguage);
    }

    public void createTempCacheDir() {
        UtilTools.createFolderInSdcard(AppPathInfo.getLogPath());
        UtilTools.createFolderInSdcard(AppPathInfo.getSaveCameraDataPath2AndroidO());
        UtilTools.createFolderInSdcard(AppPathInfo.getTempTransferDownloadPath());
        WifiCameraApi.APP_SDCARD = AppPathInfo.getLogPath();
        UStorageDeviceModule.APP_SDCARD = AppPathInfo.getLogPath();
    }

    public void checkPdfHeader(Context context) {
        SpUtils spUtils = new SpUtils(context);
        UtilTools.createFolderInSdcard(AppPathInfo.getSavePdfPath() + File.separator + ".header");
        String str = AppPathInfo.getSavePdfPath() + File.separator + ".header" + File.separator + "ic_pdf_header.jpg";
        if (!new File(str).exists()) {
            boolean zWriteAssetsFileToLocal = FileUtil.writeAssetsFileToLocal(context, "ic_pdf_header.jpg", str);
            LogWD.writeMsg(this, 64, "checkPdfHeader    writeSuccess: " + zWriteAssetsFileToLocal);
            if (zWriteAssetsFileToLocal) {
                spUtils.putString(Constant.PDF_HEADER, str);
                return;
            }
            return;
        }
        spUtils.putString(Constant.PDF_HEADER, str);
    }

    public void initDeviceJniLibValue(Context context) {
        LogWD.writeMsg(this, 1, "initDeviceJniLibValue()");
        LogWD.writeMsg(this, 1, "初始化上报信息");
        UStorageDeviceModule.getInstance().gStorageCommandHandle.caReportParamSet(AppPathInfo.getLogPath(), AppPathInfo.app_package_name, SystemUtil.getVerName(context), SystemUtil.getSystemModel(), SystemUtil.getSystemVersion(), "android");
        LogWD.writeMsg(this, 1, "初始化底层 传入支持的设备 根据ssid前缀区分");
        UStorageDeviceModule.getInstance().gStorageCommandHandle.caReportSsidPreSet(Constant.CONNECT_WIFI_KEY1);
        UStorageDeviceModule.getInstance().gStorageCommandHandle.caReportSsidPreSet(Constant.CONNECT_WIFI_KEY2);
        if ("OnePlus".equals(Build.MANUFACTURER)) {
            UtilTools.bindWifi2(context);
            WifiCallBackFuc.getInstance().breakbind();
        }
        LogWD.writeMsg(this, 1, "摄像头模块初始化");
        CameraManager.getInstance().initProgrammeSdk(context);
        IpChangManager.getInstance().init(context);
        IpChangManager.getInstance().addWifiChangeListener(CameraManager.getInstance());
    }

    public WdOpenHelper getWdSQLite(Context context) {
        LogWD.writeMsg(this, 32, "getSQLite()");
        if (this.i4seasonOpenHelper == null) {
            this.i4seasonOpenHelper = new WdOpenHelper(context);
        }
        return this.i4seasonOpenHelper;
    }

    public SharedPreferences getLanguageRecord() {
        return this.languageRecord;
    }

    public void setLanguageRecord(SharedPreferences sharedPreferences) {
        this.languageRecord = sharedPreferences;
    }
}
