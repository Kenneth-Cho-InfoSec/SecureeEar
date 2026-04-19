package com.i4season.bkCamera.logicrelated.database.videoplay;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.i4season.bkCamera.WDApplication;
import com.i4season.bkCamera.uirelated.filenodeopen.videoplay.bean.VideoPlayRecording;

public class VideoPlayRecordingDataOpt {
    public static final String DEVICE_ID = "vprecordDevID";
    public static final String DEVICE_USER_ID = "vprecordUserID";
    public static final String OPEN_VIDEO_TIME = "vprecordOpenTime";
    public static final String RECORDING_PLAY_PATH = "vprecordPlayPath";
    public static final String RECORDING_PLAY_TIME = "vprecordPlayTime";
    public static final String TABLE_MEDIAVIDEORECORDING = "mediavideorecord";
    public static final String VIDEOPLAY_ID = "vprecordID";

    public int savePlayRecording(VideoPlayRecording videoPlayRecording) {
        int iInsertPlayRecording;
        synchronized (WDApplication.getInstance().getWdSQLite()) {
            if (isHavePlayRecording(videoPlayRecording.getPlayPath(), videoPlayRecording.getDeviceID(), videoPlayRecording.getUserID())) {
                iInsertPlayRecording = updatePlayRecording(videoPlayRecording);
            } else {
                iInsertPlayRecording = insertPlayRecording(videoPlayRecording);
            }
        }
        return iInsertPlayRecording;
    }

    public int updatePlayRecording(VideoPlayRecording videoPlayRecording) {
        int iUpdate;
        synchronized (WDApplication.getInstance().getWdSQLite()) {
            SQLiteDatabase writableDatabase = WDApplication.getInstance().getWdSQLite().getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DEVICE_ID, Integer.valueOf(videoPlayRecording.getDeviceID()));
            contentValues.put(DEVICE_USER_ID, Integer.valueOf(videoPlayRecording.getUserID()));
            contentValues.put(RECORDING_PLAY_PATH, videoPlayRecording.getPlayPath());
            contentValues.put(RECORDING_PLAY_TIME, videoPlayRecording.getPlayedTime());
            contentValues.put(OPEN_VIDEO_TIME, videoPlayRecording.getLastOpenTime());
            iUpdate = writableDatabase.update(TABLE_MEDIAVIDEORECORDING, contentValues, "vprecordPlayPath=\"" + videoPlayRecording.getPlayPath() + "\" and " + DEVICE_ID + "=" + videoPlayRecording.getDeviceID() + " and " + DEVICE_USER_ID + "=" + videoPlayRecording.getUserID(), null);
            writableDatabase.close();
            contentValues.clear();
        }
        return iUpdate;
    }

    public int deletePlayRecording(String str, int i, int i2) {
        int iDelete;
        synchronized (WDApplication.getInstance().getWdSQLite()) {
            SQLiteDatabase readableDatabase = WDApplication.getInstance().getWdSQLite().getReadableDatabase();
            iDelete = readableDatabase.delete(TABLE_MEDIAVIDEORECORDING, "vprecordPlayPath=\"" + str + "\" and " + DEVICE_ID + "=" + i + " and " + DEVICE_USER_ID + "=" + i2, null);
            readableDatabase.close();
        }
        return iDelete;
    }

    public void queryPlayRecording() {
        synchronized (WDApplication.getInstance().getWdSQLite()) {
            SQLiteDatabase readableDatabase = WDApplication.getInstance().getWdSQLite().getReadableDatabase();
            Cursor cursorQuery = readableDatabase.query(TABLE_MEDIAVIDEORECORDING, null, null, null, null, null, null);
            try {
                if (readableDatabase.isOpen() && cursorQuery != null) {
                    while (cursorQuery.moveToNext()) {
                        VideoPlayRecording videoPlayRecording = new VideoPlayRecording();
                        videoPlayRecording.setDeviceID((int) cursorQuery.getLong(cursorQuery.getColumnIndex(DEVICE_ID)));
                        videoPlayRecording.setUserID((int) cursorQuery.getLong(cursorQuery.getColumnIndex(DEVICE_USER_ID)));
                        videoPlayRecording.setPlayPath(cursorQuery.getString(cursorQuery.getColumnIndex(RECORDING_PLAY_PATH)));
                        videoPlayRecording.setPlayedTime(cursorQuery.getString(cursorQuery.getColumnIndex(RECORDING_PLAY_TIME)));
                        videoPlayRecording.setLastOpenTime(cursorQuery.getString(cursorQuery.getColumnIndex(OPEN_VIDEO_TIME)));
                    }
                    return;
                }
                if (cursorQuery != null) {
                    cursorQuery.close();
                }
            } finally {
                if (cursorQuery != null) {
                    cursorQuery.close();
                }
            }
        }
    }

    public long queryPlayRecording(String str, int i, int i2) {
        long j;
        synchronized (WDApplication.getInstance().getWdSQLite()) {
            j = 0;
            SQLiteDatabase readableDatabase = WDApplication.getInstance().getWdSQLite().getReadableDatabase();
            Cursor cursorQuery = readableDatabase.query(TABLE_MEDIAVIDEORECORDING, null, "vprecordPlayPath=\"" + str + "\" and " + DEVICE_ID + "=" + i + " and " + DEVICE_USER_ID + "=" + i2, null, null, null, null);
            if (cursorQuery != null) {
                while (cursorQuery.moveToNext()) {
                    j = Long.parseLong(cursorQuery.getString(cursorQuery.getColumnIndex(RECORDING_PLAY_TIME)));
                }
            }
            cursorQuery.close();
            readableDatabase.close();
        }
        return j;
    }

    public boolean isHavePlayRecording(String str, int i, int i2) {
        boolean zMoveToFirst;
        synchronized (WDApplication.getInstance().getWdSQLite()) {
            SQLiteDatabase readableDatabase = WDApplication.getInstance().getWdSQLite().getReadableDatabase();
            Cursor cursorQuery = readableDatabase.query(TABLE_MEDIAVIDEORECORDING, null, "vprecordPlayPath=\"" + str + "\" and " + DEVICE_ID + "=" + i + " and " + DEVICE_USER_ID + "=" + i2, null, null, null, null);
            zMoveToFirst = cursorQuery.moveToFirst();
            cursorQuery.close();
            readableDatabase.close();
        }
        return zMoveToFirst;
    }

    public int insertPlayRecording(VideoPlayRecording videoPlayRecording) {
        int iInsert;
        synchronized (WDApplication.getInstance().getWdSQLite()) {
            SQLiteDatabase writableDatabase = WDApplication.getInstance().getWdSQLite().getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DEVICE_ID, Integer.valueOf(videoPlayRecording.getDeviceID()));
            contentValues.put(DEVICE_USER_ID, Integer.valueOf(videoPlayRecording.getUserID()));
            contentValues.put(RECORDING_PLAY_PATH, videoPlayRecording.getPlayPath());
            contentValues.put(RECORDING_PLAY_TIME, videoPlayRecording.getPlayedTime());
            contentValues.put(OPEN_VIDEO_TIME, videoPlayRecording.getLastOpenTime());
            iInsert = (int) writableDatabase.insert(TABLE_MEDIAVIDEORECORDING, null, contentValues);
            writableDatabase.close();
            contentValues.clear();
        }
        return iInsert;
    }

    public int deletePlayRecordingTable() {
        int iDelete;
        synchronized (WDApplication.getInstance().getWdSQLite()) {
            SQLiteDatabase readableDatabase = WDApplication.getInstance().getWdSQLite().getReadableDatabase();
            iDelete = readableDatabase.delete(TABLE_MEDIAVIDEORECORDING, null, null);
            readableDatabase.close();
        }
        return iDelete;
    }
}
