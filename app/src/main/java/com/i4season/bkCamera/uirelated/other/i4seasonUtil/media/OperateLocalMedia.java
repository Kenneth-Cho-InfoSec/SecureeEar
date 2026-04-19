package com.i4season.bkCamera.uirelated.other.i4seasonUtil.media;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.provider.MediaStore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OperateLocalMedia {
    public static final int AUDIO = 0;
    public static final int IMAGE = 2;
    public static final int VIDEO = 1;
    private static OperateLocalMedia instance;
    private static Lock reentantLock = new ReentrantLock();
    private Context context;
    private String strPath;

    public static OperateLocalMedia getInstance() {
        if (instance == null) {
            try {
                reentantLock.lock();
                if (instance == null) {
                    instance = new OperateLocalMedia();
                }
            } finally {
                reentantLock.unlock();
            }
        }
        return instance;
    }

    /* JADX WARN: Code restructure failed: missing block: B:9:0x0038, code lost:
    
        r9.context.getContentResolver().delete(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "_id=" + r3, null);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void deletePicFromGallery() throws Throwable {
        Cursor cursorQuery;
        try {
            cursorQuery = this.context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_id", "_data"}, null, null, null);
            if (cursorQuery != null) {
                while (true) {
                    try {
                        if (!cursorQuery.moveToNext()) {
                            break;
                        }
                        String string = cursorQuery.getString(cursorQuery.getColumnIndexOrThrow("_id"));
                        if (this.strPath.equals(cursorQuery.getString(cursorQuery.getColumnIndexOrThrow("_data")))) {
                            break;
                        }
                    } catch (Throwable th) {
                        th = th;
                        if (cursorQuery != null) {
                            cursorQuery.close();
                        }
                        throw th;
                    }
                }
            }
            if (cursorQuery != null) {
                cursorQuery.close();
            }
        } catch (Throwable th2) {
            th = th2;
            cursorQuery = null;
        }
    }

    private void deleteVideoFromGallery() {
        Cursor cursorQuery = this.context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[]{"_id", "_data"}, null, null, null);
        while (cursorQuery.moveToNext()) {
            String string = cursorQuery.getString(cursorQuery.getColumnIndexOrThrow("_id"));
            if (this.strPath.equals(cursorQuery.getString(cursorQuery.getColumnIndexOrThrow("_data")))) {
                this.context.getContentResolver().delete(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "_id=" + string, null);
            }
        }
    }

    private void deleteAudioFromGallery() {
        Cursor cursorQuery = this.context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{"_id", "_data"}, null, null, null);
        while (cursorQuery.moveToNext()) {
            String string = cursorQuery.getString(cursorQuery.getColumnIndexOrThrow("_id"));
            if (this.strPath.equals(cursorQuery.getString(cursorQuery.getColumnIndexOrThrow("_data")))) {
                this.context.getContentResolver().delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, "_id=" + string, null);
            }
        }
    }

    private void addPicToGallery() {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("_data", this.strPath);
            contentValues.put("mime_type", "image/*");
            contentValues.put("date_added", Long.valueOf(System.currentTimeMillis()));
            this.context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addVideoToGallery() {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("_data", this.strPath);
            contentValues.put("mime_type", "video/*");
            contentValues.put("date_added", Long.valueOf(System.currentTimeMillis()));
            this.context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addAudioToGallery() {
        try {
            String strSubstring = this.strPath.substring(this.strPath.lastIndexOf("/") + 1, this.strPath.lastIndexOf("."));
            ContentValues contentValues = new ContentValues();
            contentValues.put("_data", this.strPath);
            contentValues.put("title", strSubstring);
            contentValues.put("mime_type", "audio/*");
            contentValues.put("is_music", (Boolean) true);
            contentValues.put("is_podcast", (Boolean) false);
            contentValues.put("is_notification", (Boolean) false);
            contentValues.put("is_alarm", (Boolean) false);
            contentValues.put("is_music", (Boolean) true);
            contentValues.put("is_ringtone", (Boolean) false);
            this.context.getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateMediaSqlite(Context context, String str) {
        this.context = context;
        this.strPath = str;
        if ("Hisense C20".equals(Build.MODEL)) {
            return;
        }
        int allFileType = JudgeMultiMediaType.getAllFileType(str);
        if (allFileType == 0) {
            addAudioToGallery();
        } else if (allFileType == 1) {
            addVideoToGallery();
        } else if (allFileType == 2) {
            addPicToGallery();
        }
    }

    public void updateDeleteMediaSqlite(Context context, String str) throws Throwable {
        this.context = context;
        this.strPath = str;
        int allFileType = JudgeMultiMediaType.getAllFileType(str);
        if (allFileType == 0) {
            deleteAudioFromGallery();
        } else if (allFileType == 1) {
            deleteVideoFromGallery();
        } else if (allFileType == 2) {
            deletePicFromGallery();
        }
    }

    public static String getExtensionName(String str) {
        int iLastIndexOf;
        return (str == null || str.length() <= 0 || (iLastIndexOf = str.lastIndexOf(46)) <= -1 || iLastIndexOf >= str.length() + (-1)) ? str : str.substring(iLastIndexOf + 1);
    }
}
