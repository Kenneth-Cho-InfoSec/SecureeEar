package com.i4season.bkCamera.logicrelated.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.i4season.bkCamera.uirelated.other.logmanage.LogWD;

public class WdOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "universalstorage.db";
    private static final int VERSION = 2;

    public WdOpenHelper(Context context) {
        this(context, DATABASE_NAME, 2);
    }

    public WdOpenHelper(Context context, String str) {
        this(context, str, 2);
    }

    public WdOpenHelper(Context context, String str, int i) {
        this(context, str, null, i);
    }

    public WdOpenHelper(Context context, String str, SQLiteDatabase.CursorFactory cursorFactory, int i) {
        super(context, str, cursorFactory, i);
    }

    @Override
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        LogWD.writeMsg(this, 32, "数据库creat");
        databaseCreateTable(sQLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        LogWD.writeMsg(this, 32, "数据库升级 onUpgrade()");
        databaseDeleteTable(sQLiteDatabase);
        onCreate(sQLiteDatabase);
    }

    private void databaseCreateTable(SQLiteDatabase sQLiteDatabase) {
        LogWD.writeMsg(this, 32, "databaseCreateTable()");
        try {
            LogWD.writeMsg(this, 32, "建用户信息表");
            creatBindingDeviceInfoTable(sQLiteDatabase);
            LogWD.writeMsg(this, 32, "建视频播放保存信息表");
            creatVideoPlayReplayTable(sQLiteDatabase);
        } catch (SQLException e) {
            LogWD.writeMsg(this, 32, "databaseCreateTable() Execption");
            LogWD.writeMsg(e);
        }
    }

    private void creatBindingDeviceInfoTable(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("create table IF NOT EXISTS device_binding(bindingId integer primary key autoincrement, bindingDeviceType integer,bindingDeviceModelName text, bindingDeviceSN text, bindingDisplayName text, bindingDeviceSsid text)");
    }

    private void creatVideoPlayReplayTable(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("create table IF NOT EXISTS mediavideorecord(vprecordID integer primary key autoincrement,vprecordDevID integer,vprecordUserID integer,vprecordPlayPath text,vprecordPlayTime text,vprecordOpenTime datetime)");
    }

    public void databaseDeleteTable(SQLiteDatabase sQLiteDatabase) {
        LogWD.writeMsg(this, 32, "databaseDeleteTable()");
        sQLiteDatabase.beginTransaction();
        try {
            try {
                sQLiteDatabase.execSQL("DROP TABLE IF EXISTS device_binding");
                sQLiteDatabase.execSQL("DROP TABLE IF EXISTS mediavideorecord");
                sQLiteDatabase.setTransactionSuccessful();
            } catch (SQLException e) {
                LogWD.writeMsg(this, 32, "databaseDeleteTable() Exception");
                LogWD.writeMsg(e);
            }
        } finally {
            sQLiteDatabase.endTransaction();
        }
    }
}
