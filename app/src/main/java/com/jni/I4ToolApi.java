package com.jni;

import android.content.Context;
import android.os.Environment;

public class I4ToolApi {
    public static String APP_SDCARD = Environment.getExternalStorageDirectory().getPath();
    public I4Tool gI4Tool;
    private int saveappinfo;

    public static class I4ToolApiHolder {
        public static I4ToolApi gI4ToolApi = new I4ToolApi();
    }

    private I4ToolApi() {
        this.saveappinfo = 0;
        this.gI4Tool = null;
        this.gI4Tool = new I4Tool();
    }

    public static I4ToolApi getInstance() {
        return I4ToolApiHolder.gI4ToolApi;
    }

    public synchronized String getAppName(Context context) {
        try {
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return context.getResources().getString(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.labelRes);
    }

    public synchronized String getPackageName(Context context) {
        try {
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).packageName;
    }

    private void vsSetAppinfo(Context context, String str, int i) {
        if (this.saveappinfo != 0) {
            return;
        }
        this.gI4Tool.vsSetAppinfo(getAppName(context), getPackageName(context), str, 1);
        this.saveappinfo = 1;
    }

    public void initApi(Context context, String str, int i) {
        vsSetAppinfo(context, str, i);
    }
}
