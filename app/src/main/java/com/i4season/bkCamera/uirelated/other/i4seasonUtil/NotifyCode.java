package com.i4season.bkCamera.uirelated.other.i4seasonUtil;

import android.content.Intent;
import com.i4season.bkCamera.WDApplication;
import java.io.Serializable;

public class NotifyCode {
    public static final String BC_DEFAULT_PARAM1_KEY = "BroadcastDefaultParam1Key";
    public static final int BC_DEFAULT_PARAM1_VALUE = -1;
    public static final String BC_DEFAULT_PARAM2_KEY = "BroadcastDefaultParam2Key";
    public static final int BC_DEFAULT_PARAM2_VALUE = -1;
    public static final String BC_DEFAULT_PARAM3_KEY = "BroadcastDefaultParam3Key";
    public static final int BC_DEFAULT_PARAM3_VALUE = -1;
    public static final String BC_DEFAULT_SERIAL_KEY = "BroadcastDefaultSerialKey";
    public static final Serializable BC_DEFAULT_SERIAL_VALUE = null;
    public static final String BC_DEFAULT_STRING_KEY = "BroadcastDefaultStringKey";
    public static final String BC_DEFAULT_STRING_VALUE = null;
    public static final String BIND_DEVICE_END = "BIND_DEVICE_END";
    public static final String DEVICE_FIND = "DEVICE_FIND";
    public static final String DEVICE_LOWER_BATTERY_NOTIFY = "DEVICE_LOWER_BATTERY_NOTIFY";
    public static final String DEVICE_OFFLINE_NOTIFY = "DEVICE_OFFLINE_NOTIFY";
    public static final String DEVICE_REGIST_ERROR_INFO = "DEVICE_REGIST_ERROR_INFO";
    public static final String DEVICE_REGIST_ERROR_NOTIFY = "DEVICE_REGIST_ERROR_NOTIFY";
    public static final String DEVICE_REGIST_SUCCFUL_NOTIFY = "DEVICE_REGIST_SUCCFUL_NOTIFY";
    public static final String LANGUAGE_CHANGE_NOTIFY = "LANGUAGE_CHANGE_NOTIFY";
    public static final String LICENSE_CHECK_ERROR = "LICENSE_CHECK_ERROR";
    public static final String ONLINE_BURNING_SUSS = "ONLINE_BURNING_SUSS";
    public static final String PHOTO_PREVIEW_DELETE_INFO = "PHOTO_PREVIEW_DELETE_INFO";
    public static final String PHOTO_PREVIEW_DELETE_NOTIFY = "PHOTO_PREVIEW_DELETE_NOTIFY";

    public static void sendBoradcastNotify(String str) {
        Intent intent = new Intent();
        intent.setAction(str);
        WDApplication.getInstance().sendBroadcast(intent);
    }

    public static void sendBoradcastNotify(String str, int i) {
        Intent intent = new Intent();
        intent.setAction(str);
        intent.putExtra(BC_DEFAULT_PARAM1_KEY, i);
        WDApplication.getInstance().sendBroadcast(intent);
    }

    public static void sendBoradcastNotify(String str, int i, String str2) {
        Intent intent = new Intent();
        intent.setAction(str);
        intent.putExtra(BC_DEFAULT_PARAM1_KEY, i);
        intent.putExtra(BC_DEFAULT_STRING_KEY, str2);
        WDApplication.getInstance().sendBroadcast(intent);
    }

    public static void sendBoradcastNotify(String str, int i, int i2, Serializable serializable) {
        Intent intent = new Intent();
        intent.putExtra(BC_DEFAULT_PARAM1_KEY, i);
        intent.putExtra(BC_DEFAULT_PARAM2_KEY, i2);
        intent.putExtra(BC_DEFAULT_SERIAL_KEY, serializable);
        intent.setAction(str);
        WDApplication.getInstance().sendBroadcast(intent);
    }

    public static void sendBoradcastNotify(String str, int i, int i2, int i3) {
        Intent intent = new Intent();
        intent.putExtra(BC_DEFAULT_PARAM1_KEY, i);
        intent.putExtra(BC_DEFAULT_PARAM2_KEY, i2);
        intent.putExtra(BC_DEFAULT_PARAM3_KEY, i3);
        intent.setAction(str);
        WDApplication.getInstance().sendBroadcast(intent);
    }
}
