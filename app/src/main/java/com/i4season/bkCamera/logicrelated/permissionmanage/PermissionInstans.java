package com.i4season.bkCamera.logicrelated.permissionmanage;

import android.app.Activity;
import android.content.Context;
import android.os.Build;

public class PermissionInstans {
    public static final int GO_SETTING_REQUEST_CODE = 12345;
    public static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    public static final int REQUEST_RECORD_AUDIO_PERMISSIONS_CODE = 125;

    public static class InitInstans {
        public static PermissionInstans instans = new PermissionInstans();
    }

    public static PermissionInstans getInstance() {
        return InitInstans.instans;
    }

    private PermissionInstans() {
    }

    public boolean isHavaLocalPermission(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            return context.checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") == 0 && context.checkSelfPermission("android.permission.ACCESS_FINE_LOCATION") == 0;
        }
        return true;
    }

    public boolean isHavaStoragePermission(Context context) {
        if (Build.VERSION.SDK_INT >= 33) {
            return context.checkSelfPermission("android.permission.READ_MEDIA_IMAGES") == 0 && context.checkSelfPermission("android.permission.READ_MEDIA_VIDEO") == 0;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            return context.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") == 0 && context.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0;
        }
        return true;
    }

    public void requestPermissions(Context context) {
        if (Build.VERSION.SDK_INT >= 33) {
            ((Activity) context).requestPermissions(new String[]{"android.permission.READ_MEDIA_IMAGES", "android.permission.READ_MEDIA_VIDEO"}, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        } else if (Build.VERSION.SDK_INT >= 23) {
            ((Activity) context).requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        }
    }

    public boolean isHaveRecordAudioPermission(Context context) {
        return Build.VERSION.SDK_INT < 23 || context.checkSelfPermission("android.permission.RECORD_AUDIO") == 0;
    }

    public void requestRecordAudioPermission(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            ((Activity) context).requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, 125);
        }
    }
}
