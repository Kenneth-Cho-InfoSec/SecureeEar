package com.i4season.bkCamera.uirelated.filenodeopen;

import android.content.Intent;

public class FileOpenSetOpenProperty {
    public static final int DLNA_GET_FILE_LIST_OBJECTID_BT = 5;
    public static final int DLNA_GET_FILE_LIST_OBJECTID_COMMON = 0;
    public static final int DLNA_GET_FILE_LIST_OBJECTID_DOCMENT = 4;
    public static final int DLNA_GET_FILE_LIST_OBJECTID_MOVIE = 2;
    public static final int DLNA_GET_FILE_LIST_OBJECTID_MUSIC = 1;
    public static final int DLNA_GET_FILE_LIST_OBJECTID_PICTURE = 3;
    public static final int DLNA_GET_FILE_LIST_OBJECTID_RAR = 6;
    public static final int DLNA_USER_TYPE_ADMIN = 0;
    public static final int DLNA_USER_TYPE_GEUST = 1;
    public static final String INTERNAL_IMAGE_APP_OPEN = "ImageOpen";
    public static final String INTERNAL_MUSIC_APP_OPEN = "MusicOpen";
    public static final String INTERNAL_VIDEO_APP_OPEN = "VideoOpen";
    private Intent mIntent;
    private String mMimeType = "";
    private int mFileDlnaType = 0;
    private String mPlayType = "MusicOpen";
    private boolean mIsMediaFile = false;
    private boolean mIsInternalAppOpen = false;

    public FileOpenSetOpenProperty() {
        this.mIntent = null;
        this.mIntent = new Intent("android.intent.action.VIEW");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:21:0x012f  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0172  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setOpenProperty(int i) {
        this.mIsInternalAppOpen = false;
        switch (i) {
            case 1:
                this.mIsInternalAppOpen = true;
                this.mPlayType = "ImageOpen";
                if (!this.mIsInternalAppOpen) {
                    this.mIsInternalAppOpen = false;
                }
                this.mMimeType = "image/*";
                this.mFileDlnaType = 3;
                this.mIntent.addCategory("android.intent.category.DEFAULT");
                this.mIntent.addFlags(268435456);
                this.mIntent.addFlags(1);
                break;
            case 2:
                this.mMimeType = "video/*";
                this.mFileDlnaType = 2;
                this.mIntent.addFlags(67108864);
                this.mIntent.putExtra("oneshot", 0);
                this.mIntent.putExtra("configchange", 0);
                this.mIntent.addFlags(1);
                break;
            case 3:
                this.mIsInternalAppOpen = true;
                this.mPlayType = "MusicOpen";
                if (!this.mIsInternalAppOpen) {
                    this.mIsInternalAppOpen = false;
                }
                this.mMimeType = "audio/*";
                this.mFileDlnaType = 1;
                this.mIntent.addFlags(67108864);
                this.mIntent.putExtra("oneshot", 0);
                this.mIntent.putExtra("configchange", 0);
                this.mIntent.addFlags(1);
                break;
            case 6:
                if (!this.mIsInternalAppOpen) {
                }
                this.mMimeType = "image/*";
                this.mFileDlnaType = 3;
                this.mIntent.addCategory("android.intent.category.DEFAULT");
                this.mIntent.addFlags(268435456);
                this.mIntent.addFlags(1);
                break;
            case 7:
                if (!this.mIsInternalAppOpen) {
                }
                this.mMimeType = "audio/*";
                this.mFileDlnaType = 1;
                this.mIntent.addFlags(67108864);
                this.mIntent.putExtra("oneshot", 0);
                this.mIntent.putExtra("configchange", 0);
                this.mIntent.addFlags(1);
                break;
            case 8:
            case 9:
                this.mMimeType = "application/msword";
                this.mFileDlnaType = 4;
                this.mIntent.addCategory("android.intent.category.DEFAULT");
                this.mIntent.addFlags(268435456);
                this.mIntent.addFlags(1);
                break;
            case 10:
                this.mMimeType = "text/plain";
                this.mFileDlnaType = 4;
                this.mIntent.addCategory("android.intent.category.DEFAULT");
                this.mIntent.addFlags(268435456);
                this.mIntent.addFlags(1);
                break;
            case 11:
                this.mMimeType = "application/vnd.ms-excel";
                this.mFileDlnaType = 4;
                this.mIntent.addCategory("android.intent.category.DEFAULT");
                this.mIntent.addFlags(268435456);
                this.mIntent.addFlags(1);
                break;
            case 12:
                this.mMimeType = "application/vnd.ms-powerpoint";
                this.mFileDlnaType = 4;
                this.mIntent.addCategory("android.intent.category.DEFAULT");
                this.mIntent.addFlags(268435456);
                this.mIntent.addFlags(1);
                break;
            case 13:
                this.mMimeType = "application/pdf";
                this.mFileDlnaType = 4;
                this.mIntent.addCategory("android.intent.category.DEFAULT");
                this.mIntent.addFlags(268435456);
                this.mIntent.addFlags(1);
                break;
            case 14:
                this.mMimeType = "text/html";
                this.mFileDlnaType = 4;
                this.mIntent.addFlags(1);
                break;
            case 15:
                this.mMimeType = "application/vnd.android.package-archive";
                this.mFileDlnaType = 4;
                this.mIntent.addFlags(268435456);
                this.mIntent.addFlags(1);
                break;
            case 16:
                this.mMimeType = "application/x-chm";
                this.mFileDlnaType = 4;
                this.mIntent.addCategory("android.intent.category.DEFAULT");
                this.mIntent.addFlags(268435456);
                this.mIntent.addFlags(1);
                break;
            case 17:
                this.mMimeType = "flash/*";
                this.mFileDlnaType = 4;
                this.mIntent.addFlags(1);
                break;
            case 18:
                this.mMimeType = "application/zip";
                this.mFileDlnaType = 4;
                this.mIntent.addCategory("android.intent.category.DEFAULT");
                this.mIntent.addFlags(268435456);
                this.mIntent.addFlags(1);
                break;
            case 19:
                this.mMimeType = "text/x-vcard";
                this.mFileDlnaType = 4;
                this.mIntent.addCategory("android.intent.category.DEFAULT");
                this.mIntent.addFlags(268435456);
                this.mIntent.addFlags(1);
                break;
            case 20:
                this.mMimeType = "application/x-rar-compressed";
                this.mFileDlnaType = 4;
                this.mIntent.addCategory("android.intent.category.DEFAULT");
                this.mIntent.addFlags(268435456);
                this.mIntent.addFlags(1);
                break;
            case 21:
                this.mMimeType = "application/x-tar";
                this.mFileDlnaType = 4;
                this.mIntent.addCategory("android.intent.category.DEFAULT");
                this.mIntent.addFlags(268435456);
                this.mIntent.addFlags(1);
                break;
        }
    }

    public String getMimeType() {
        return this.mMimeType;
    }

    public Intent getIntent() {
        return this.mIntent;
    }

    public void setIntent() {
        this.mIntent = null;
    }

    public int getFileDlnaType() {
        return this.mFileDlnaType;
    }

    public String getPlayType() {
        return this.mPlayType;
    }

    public boolean isMediaFile() {
        return this.mIsMediaFile;
    }

    public boolean isInternalAppOpen() {
        return this.mIsInternalAppOpen;
    }

    public void setInternalAppOpen(boolean z) {
        this.mIsInternalAppOpen = z;
    }
}
