package com.i4season.bkCamera.logicrelated.database.bandingdevice;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.i4season.bkCamera.WDApplication;
import com.i4season.bkCamera.uirelated.functionpage.homepage.bean.UserDeviceInfoBean;
import com.i4season.bkCamera.uirelated.other.logmanage.LogWD;
import java.util.ArrayList;
import java.util.List;

public class BindingDeviceOpt {
    public static final String BINDING_DEVICE_DISPLAY = "bindingDisplayName";
    public static final String BINDING_DEVICE_MODEL = "bindingDeviceModelName";
    public static final String BINDING_DEVICE_SN = "bindingDeviceSN";
    public static final String BINDING_DEVICE_SSID = "bindingDeviceSsid";
    public static final String BINDING_DEVICE_TYPE = "bindingDeviceType";
    public static final String BINDING_ID = "bindingId";
    public static final String BINDING_TABLENAME = "device_binding";

    public boolean saveBindingDeviceInfo(UserDeviceInfoBean userDeviceInfoBean) {
        boolean zUpdateBindDeviceInfoRecord;
        UserDeviceInfoBean userDeviceInfoBeanAcceptBindDeviceInfoFromSsid;
        LogWD.writeMsg(this, 32, "saveBindingDeviceInfo()");
        synchronized (WDApplication.getInstance().getWdSQLite()) {
            if (!isExistRecord(userDeviceInfoBean.getmDeviceSsid())) {
                zUpdateBindDeviceInfoRecord = insertBindDeviceInfoRecord(userDeviceInfoBean);
            } else {
                zUpdateBindDeviceInfoRecord = updateBindDeviceInfoRecord(userDeviceInfoBean);
            }
            if (zUpdateBindDeviceInfoRecord && (userDeviceInfoBeanAcceptBindDeviceInfoFromSsid = acceptBindDeviceInfoFromSsid(userDeviceInfoBean.getmDeviceSsid())) != null) {
                userDeviceInfoBean.setmDeviceId(userDeviceInfoBeanAcceptBindDeviceInfoFromSsid.getmDeviceId());
            }
            LogWD.writeMsg(this, 32, "saveUserInfo() isSuccess = " + zUpdateBindDeviceInfoRecord);
        }
        return zUpdateBindDeviceInfoRecord;
    }

    public boolean isExistRecord(String str) {
        boolean z;
        synchronized (WDApplication.getInstance().getWdSQLite()) {
            LogWD.writeMsg(this, 32, "isExistRecord() devID = " + str);
            z = acceptBindDeviceInfoFromSsid(str) != null;
            LogWD.writeMsg(this, 32, "isExistRecord() isExistRecord = " + z);
        }
        return z;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v1 */
    /* JADX WARN: Type inference failed for: r1v11 */
    /* JADX WARN: Type inference failed for: r1v3 */
    /* JADX WARN: Type inference failed for: r1v4, types: [android.database.Cursor] */
    /* JADX WARN: Type inference failed for: r2v1, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r2v2 */
    /* JADX WARN: Type inference failed for: r2v3 */
    /* JADX WARN: Type inference failed for: r2v4, types: [android.database.sqlite.SQLiteDatabase] */
    /* JADX WARN: Type inference failed for: r2v5, types: [android.database.sqlite.SQLiteDatabase] */
    /* JADX WARN: Type inference failed for: r2v8, types: [android.database.sqlite.SQLiteDatabase] */
    public UserDeviceInfoBean acceptBindDeviceInfoFromSsid(String str) {
        Cursor cursorQuery;
        synchronized (WDApplication.getInstance().getWdSQLite()) {
            ?? readableDatabase = "acceptBindDeviceInfoFromSsid() devID = " + str;
            LogWD.writeMsg(this, 32, readableDatabase);
            ?? r1 = 0;
            r1 = 0;
            bindDeviceInfoFromCursor = null;
            UserDeviceInfoBean bindDeviceInfoFromCursor = null;
            try {
                try {
                    readableDatabase = WDApplication.getInstance().getWdSQLite().getReadableDatabase();
                } catch (Throwable th) {
                    r1 = str;
                    th = th;
                }
                try {
                    cursorQuery = readableDatabase.query(BINDING_TABLENAME, null, "bindingDeviceSsid = ?", new String[]{str}, null, null, null);
                    try {
                    } catch (Exception e) {
                        e = e;
                        LogWD.writeMsg(e);
                        if (readableDatabase != 0) {
                            readableDatabase.close();
                        }
                        if (cursorQuery != null) {
                        }
                        return bindDeviceInfoFromCursor;
                    }
                } catch (Exception e2) {
                    e = e2;
                    cursorQuery = null;
                } catch (Throwable th2) {
                    th = th2;
                    if (readableDatabase != 0) {
                        readableDatabase.close();
                    }
                    if (r1 != 0) {
                        r1.close();
                    }
                    throw th;
                }
            } catch (Exception e3) {
                e = e3;
                cursorQuery = null;
                readableDatabase = 0;
            } catch (Throwable th3) {
                th = th3;
                readableDatabase = 0;
            }
            if (cursorQuery == null) {
                readableDatabase.close();
                if (readableDatabase != 0) {
                    readableDatabase.close();
                }
                if (cursorQuery != null) {
                    cursorQuery.close();
                }
                return null;
            }
            cursorQuery.moveToFirst();
            bindDeviceInfoFromCursor = cursorQuery.isAfterLast() ? null : getBindDeviceInfoFromCursor(cursorQuery);
            if (readableDatabase != 0) {
                readableDatabase.close();
            }
            if (cursorQuery != null) {
                cursorQuery.close();
            }
            return bindDeviceInfoFromCursor;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r13v0, types: [int] */
    /* JADX WARN: Type inference failed for: r1v1 */
    /* JADX WARN: Type inference failed for: r1v11 */
    /* JADX WARN: Type inference failed for: r1v3 */
    /* JADX WARN: Type inference failed for: r1v4, types: [android.database.Cursor] */
    /* JADX WARN: Type inference failed for: r2v1, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r2v2 */
    /* JADX WARN: Type inference failed for: r2v3 */
    /* JADX WARN: Type inference failed for: r2v4, types: [android.database.sqlite.SQLiteDatabase] */
    /* JADX WARN: Type inference failed for: r2v5, types: [android.database.sqlite.SQLiteDatabase] */
    /* JADX WARN: Type inference failed for: r2v8, types: [android.database.sqlite.SQLiteDatabase] */
    public UserDeviceInfoBean acceptBindingDeviceInfoFromDevID(int i) {
        Cursor cursorQuery;
        synchronized (WDApplication.getInstance().getWdSQLite()) {
            ?? readableDatabase = "acceptBindingDeviceInfoFromDevID() devID = " + ((int) i);
            LogWD.writeMsg(this, 32, readableDatabase);
            ?? r1 = 0;
            r1 = 0;
            bindDeviceInfoFromCursor = null;
            UserDeviceInfoBean bindDeviceInfoFromCursor = null;
            try {
                try {
                    readableDatabase = WDApplication.getInstance().getWdSQLite().getReadableDatabase();
                } catch (Throwable th) {
                    r1 = i;
                    th = th;
                }
            } catch (Exception e) {
                e = e;
                cursorQuery = null;
                readableDatabase = 0;
            } catch (Throwable th2) {
                th = th2;
                readableDatabase = 0;
            }
            try {
                cursorQuery = readableDatabase.query(BINDING_TABLENAME, null, "bindingId=?", new String[]{((int) i) + ""}, null, null, null);
                try {
                } catch (Exception e2) {
                    e = e2;
                    LogWD.writeMsg(e);
                    if (readableDatabase != 0) {
                        readableDatabase.close();
                    }
                    if (cursorQuery != null) {
                    }
                    return bindDeviceInfoFromCursor;
                }
            } catch (Exception e3) {
                e = e3;
                cursorQuery = null;
            } catch (Throwable th3) {
                th = th3;
                if (readableDatabase != 0) {
                    readableDatabase.close();
                }
                if (r1 != 0) {
                    r1.close();
                }
                throw th;
            }
            if (cursorQuery == null) {
                readableDatabase.close();
                if (readableDatabase != 0) {
                    readableDatabase.close();
                }
                if (cursorQuery != null) {
                    cursorQuery.close();
                }
                return null;
            }
            cursorQuery.moveToFirst();
            bindDeviceInfoFromCursor = cursorQuery.isAfterLast() ? null : getBindDeviceInfoFromCursor(cursorQuery);
            if (readableDatabase != 0) {
                readableDatabase.close();
            }
            if (cursorQuery != null) {
                cursorQuery.close();
            }
            return bindDeviceInfoFromCursor;
        }
    }

    public UserDeviceInfoBean getBindDeviceInfoFromCursor(Cursor cursor) {
        synchronized (WDApplication.getInstance().getWdSQLite()) {
            LogWD.writeMsg(this, 32, "getBindDeviceInfoFromCursor()");
            if (cursor == null) {
                return null;
            }
            UserDeviceInfoBean userDeviceInfoBean = new UserDeviceInfoBean();
            userDeviceInfoBean.setmDeviceId(cursor.getInt(cursor.getColumnIndex(BINDING_ID)));
            userDeviceInfoBean.setmDeviceType(cursor.getInt(cursor.getColumnIndex(BINDING_DEVICE_TYPE)));
            userDeviceInfoBean.setmModleName(cursor.getString(cursor.getColumnIndex(BINDING_DEVICE_MODEL)));
            userDeviceInfoBean.setmDeviceSn(cursor.getString(cursor.getColumnIndex(BINDING_DEVICE_SN)));
            userDeviceInfoBean.setmDisplayName(cursor.getString(cursor.getColumnIndex(BINDING_DEVICE_DISPLAY)));
            userDeviceInfoBean.setmDeviceSsid(cursor.getString(cursor.getColumnIndex(BINDING_DEVICE_SSID)));
            return userDeviceInfoBean;
        }
    }

    public boolean insertBindDeviceInfoRecord(UserDeviceInfoBean userDeviceInfoBean) {
        boolean z;
        SQLiteDatabase writableDatabase;
        synchronized (WDApplication.getInstance().getWdSQLite()) {
            LogWD.writeMsg(this, 32, "insertBindDeviceInfoRecord()");
            SQLiteDatabase sQLiteDatabase = null;
            try {
                try {
                    writableDatabase = WDApplication.getInstance().getWdSQLite().getWritableDatabase();
                } catch (Exception e) {
                    e = e;
                }
            } catch (Throwable th) {
                th = th;
                writableDatabase = sQLiteDatabase;
            }
            try {
                ContentValues contentValues = new ContentValues();
                contentValues.put(BINDING_DEVICE_TYPE, Integer.valueOf(userDeviceInfoBean.getmDeviceType()));
                contentValues.put(BINDING_DEVICE_MODEL, userDeviceInfoBean.getmModleName());
                contentValues.put(BINDING_DEVICE_SN, userDeviceInfoBean.getmDeviceSn());
                contentValues.put(BINDING_DEVICE_DISPLAY, userDeviceInfoBean.getmDisplayName());
                contentValues.put(BINDING_DEVICE_SSID, userDeviceInfoBean.getmDeviceSsid());
                z = writableDatabase.insert(BINDING_TABLENAME, null, contentValues) != -1;
                if (writableDatabase != null) {
                    writableDatabase.close();
                }
            } catch (Exception e2) {
                e = e2;
                sQLiteDatabase = writableDatabase;
                LogWD.writeMsg(e);
                if (sQLiteDatabase != null) {
                    sQLiteDatabase.close();
                }
            } catch (Throwable th2) {
                th = th2;
                if (writableDatabase != null) {
                    writableDatabase.close();
                }
                throw th;
            }
            LogWD.writeMsg(this, 32, "insertBindDeviceInfoRecord() isSuccess = " + z);
        }
        return z;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v1 */
    /* JADX WARN: Type inference failed for: r1v4, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r1v6, types: [android.database.sqlite.SQLiteDatabase] */
    /* JADX WARN: Type inference failed for: r1v7 */
    public boolean updateBindDeviceInfoRecord(UserDeviceInfoBean userDeviceInfoBean) {
        boolean z;
        synchronized (WDApplication.getInstance().getWdSQLite()) {
            LogWD.writeMsg(this, 32, "updateBindDeviceInfoRecord()");
            SQLiteDatabase writableDatabase = 0;
            writableDatabase = 0;
            try {
                try {
                    writableDatabase = WDApplication.getInstance().getWdSQLite().getWritableDatabase();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(BINDING_DEVICE_TYPE, Integer.valueOf(userDeviceInfoBean.getmDeviceType()));
                    contentValues.put(BINDING_DEVICE_MODEL, userDeviceInfoBean.getmModleName());
                    contentValues.put(BINDING_DEVICE_SN, userDeviceInfoBean.getmDeviceSn());
                    contentValues.put(BINDING_DEVICE_DISPLAY, userDeviceInfoBean.getmDisplayName());
                    z = writableDatabase.update(BINDING_TABLENAME, contentValues, "bindingDeviceSsid = ?", new String[]{userDeviceInfoBean.getmDeviceSsid()}) > 0;
                } catch (Exception e) {
                    LogWD.writeMsg(e);
                    if (writableDatabase != 0) {
                    }
                    StringBuilder sb = new StringBuilder();
                    writableDatabase = "updateBindDeviceInfoRecord() isUpdataSuccess = ";
                    sb.append("updateBindDeviceInfoRecord() isUpdataSuccess = ");
                    sb.append(z);
                    LogWD.writeMsg(this, 32, sb.toString());
                    return z;
                }
                StringBuilder sb2 = new StringBuilder();
                writableDatabase = "updateBindDeviceInfoRecord() isUpdataSuccess = ";
                sb2.append("updateBindDeviceInfoRecord() isUpdataSuccess = ");
                sb2.append(z);
                LogWD.writeMsg(this, 32, sb2.toString());
            } finally {
                if (writableDatabase != 0) {
                    writableDatabase.close();
                }
            }
        }
        return z;
    }

    public List<UserDeviceInfoBean> acceptAllBindingDevice() {
        ArrayList arrayList;
        SQLiteDatabase writableDatabase;
        synchronized (WDApplication.getInstance().getWdSQLite()) {
            arrayList = new ArrayList();
            Cursor cursorQuery = null;
            try {
                writableDatabase = WDApplication.getInstance().getWdSQLite().getWritableDatabase();
                try {
                    cursorQuery = writableDatabase.query(BINDING_TABLENAME, new String[]{BINDING_ID, BINDING_DEVICE_TYPE, BINDING_DEVICE_MODEL, BINDING_DEVICE_SN, BINDING_DEVICE_DISPLAY, BINDING_DEVICE_SSID}, null, null, null, null, null);
                    if (cursorQuery != null) {
                        while (cursorQuery.moveToNext()) {
                            arrayList.add(getBindDeviceInfoFromCursor(cursorQuery));
                        }
                    }
                    if (cursorQuery != null) {
                        cursorQuery.close();
                    }
                    if (writableDatabase != null) {
                        writableDatabase.close();
                    }
                } catch (Throwable th) {
                    th = th;
                    if (cursorQuery != null) {
                        cursorQuery.close();
                    }
                    if (writableDatabase != null) {
                        writableDatabase.close();
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                writableDatabase = null;
            }
        }
        return arrayList;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v2 */
    /* JADX WARN: Type inference failed for: r1v5, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r1v7, types: [android.database.sqlite.SQLiteDatabase] */
    /* JADX WARN: Type inference failed for: r1v8 */
    public boolean deleteBindingDeviceInfoRecordFromDevId(String str) {
        boolean z;
        synchronized (WDApplication.getInstance().getWdSQLite()) {
            LogWD.writeMsg(this, 32, "deleteBindingDeviceInfoRecordFromDevId() userId = " + str);
            SQLiteDatabase writableDatabase = 0;
            writableDatabase = 0;
            try {
                try {
                    writableDatabase = WDApplication.getInstance().getWdSQLite().getWritableDatabase();
                    z = writableDatabase.delete(BINDING_TABLENAME, "bindingDeviceSsid = ?", new String[]{str}) != 0;
                } catch (Exception e) {
                    LogWD.writeMsg(e);
                    if (writableDatabase != 0) {
                    }
                    StringBuilder sb = new StringBuilder();
                    writableDatabase = "deleteBindingDeviceInfoRecordFromDevId() isSuccess = ";
                    sb.append("deleteBindingDeviceInfoRecordFromDevId() isSuccess = ");
                    sb.append(z);
                    LogWD.writeMsg(this, 32, sb.toString());
                    return z;
                }
                StringBuilder sb2 = new StringBuilder();
                writableDatabase = "deleteBindingDeviceInfoRecordFromDevId() isSuccess = ";
                sb2.append("deleteBindingDeviceInfoRecordFromDevId() isSuccess = ");
                sb2.append(z);
                LogWD.writeMsg(this, 32, sb2.toString());
            } finally {
                if (writableDatabase != 0) {
                    writableDatabase.close();
                }
            }
        }
        return z;
    }
}
