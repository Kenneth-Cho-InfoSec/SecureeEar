package com.i4season.bkCamera.uirelated.other;

import android.os.Build;
import android.os.Environment;
import com.i4season.bkCamera.WDApplication;
import com.jni.logmanageWifi.LogManagerWD;

public class AppPathInfo {
    public static final String AUDIO_BACKUP_DIR = "/Audio Backup";
    public static final String BACKUP_ROOT_DIR = "/Backup";
    public static final String CAMERA = "/Camera";
    public static final String CONTACTS_BACKUP_DIR = "/Contacts Backup";
    public static final int CURPAGE_SHOW_COUNT = 200;
    public static final String DOC_BACKUP_DIR = "/Doc Backup";
    public static final String FIND_DEVICE_PAHT = "/data/";
    public static final String FIND_DEVICE_STORAGE = "/card";
    public static final String FIND_OTG_DEVICE_PAHT = "/card0/";
    public static final String FIND_OTG_DEVICE_PAHT_CARD1 = "/card1/";
    public static final String HTTP_HANDER = "http://";
    public static String HTTP_IP = "";
    public static final String IMG = "/IMG_";
    public static final String IMG_SUFFDIX = ".JPG";
    public static final String PACKAGE_NAME_WEIBO = "com.sina.weibo";
    public static final String PACKAGE_NAME_WEIXIN = "com.tencent.mm";
    public static final String PHONE_BACKUP_DIR = "/Phone Backup";
    public static final String STORAGE_HTTP = "http://127.0.0.1:";
    public static final String VIDEO = "/VIDEO_";
    public static final String VIDEO_BACKUP_DIR = "/Video Backup";
    public static final String VIDEO_SUFFDIX = ".MP4";
    public static final String V_CARD_FILE_NAME = ".vcf";
    public static final String WiFi_HTTP = "http://10.10.10.254:";
    public static final String app_camera_cache = "/UseeEar";
    public static final String app_database_save = "/Database/";
    public static final String app_log_save = "/Log";
    public static final String app_original_cache = "/OriginalFileCache/";
    public static String app_package_name = "";
    public static final String app_pdf = "/PdfSave";
    public static final String app_restore = "/Restore";
    public static final String app_thumb_cache = "/ThumbCache/";
    public static final String app_transfer_download = "/Download/";
    public static final String app_transfer_temp_download = "/TempSave/";
    public static final String camera_photo = "Photo";
    public static final String camera_video = "Video";
    public static final String app_sdcard = Environment.getExternalStorageDirectory().getPath();
    public static String EXTENDSD_PATH = "";
    public static final String app_camera_data = "/" + Environment.DIRECTORY_DCIM;

    public static String getLogPath() {
        if (Build.VERSION.SDK_INT >= 29) {
            String absolutePath = WDApplication.getInstance().getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
            LogManagerWD.APP_SDCARD = absolutePath;
            return absolutePath + app_log_save;
        }
        return app_sdcard + app_package_name + app_log_save;
    }

    public static String getTransferDownloadPath() {
        return app_sdcard + app_package_name + app_transfer_download;
    }

    public static String getTempTransferDownloadPath() {
        return app_sdcard + app_package_name + app_transfer_temp_download;
    }

    public static String getThumbCachePath() {
        return app_sdcard + app_package_name + app_thumb_cache;
    }

    public static String getOriginalCachePath() {
        return app_sdcard + app_package_name + app_original_cache;
    }

    public static String getSaveCameraDataPath() {
        return app_sdcard + app_package_name + app_camera_cache;
    }

    public static String getSaveCameraDataPath2AndroidO() {
        return app_sdcard + app_camera_data + app_package_name;
    }

    public static String getRestoreDataPath() {
        return app_sdcard + app_package_name + app_restore;
    }

    public static String getDatabaseSavePath() {
        return app_sdcard + app_package_name + app_database_save;
    }

    public static String getSavePdfPath() {
        return app_sdcard + app_package_name + app_pdf;
    }
}
