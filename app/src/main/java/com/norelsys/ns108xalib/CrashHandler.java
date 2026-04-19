package com.norelsys.ns108xalib;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    public static final String TAG = "CrashHandler";
    private static CrashHandler myCrashHandler;
    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private String nameString;
    private Map<String, String> infos = new HashMap();
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

    private CrashHandler() {
    }

    public static synchronized CrashHandler getInstance() {
        if (myCrashHandler != null) {
            return myCrashHandler;
        }
        myCrashHandler = new CrashHandler();
        return myCrashHandler;
    }

    public void init(Context context) {
        this.mContext = context;
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable th) {
        Thread.UncaughtExceptionHandler uncaughtExceptionHandler;
        this.mContext.getFilesDir().getAbsolutePath();
        this.mContext.getPackageResourcePath();
        this.mContext.getFilesDir().toString();
        Log.e("CrashHandler", "uncaughtException");
        if (!handleException(th) && (uncaughtExceptionHandler = this.mDefaultHandler) != null) {
            uncaughtExceptionHandler.uncaughtException(thread, th);
            return;
        }
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            Log.e("CrashHandler", "error : ", e);
        }
        Process.killProcess(Process.myPid());
        System.exit(1);
    }

    /* JADX WARN: Type inference failed for: r3v1, types: [com.norelsys.ns108xalib.CrashHandler$1] */
    private boolean handleException(Throwable th) {
        Log.e("CrashHandler", "HANDLEEXCEPTION");
        if (th == null) {
            return false;
        }
        collectDeviceInfo(this.mContext);
        saveCrashInfo2File(th);
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(CrashHandler.this.mContext, "很抱歉,程序出现异常，即将退出!", 1).show();
                Looper.loop();
            }
        }.start();
        return true;
    }

    public void collectDeviceInfo(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 1);
            if (packageInfo != null) {
                this.infos.put("versionName：", packageInfo.versionName);
                this.infos.put("versionCode", String.valueOf(packageInfo.versionCode));
                this.infos.put("RELEASE", Build.VERSION.RELEASE);
                this.infos.put("API", String.valueOf(Build.VERSION.SDK_INT));
                this.infos.put("MANUFACTURER", Build.MANUFACTURER);
                this.infos.put("MANUFACTURER", Build.MODEL);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("CrashHandler", "an error occured when collect package info", e);
        }
    }

    private String saveCrashInfo2File(Throwable th) {
        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry<String, String> entry : this.infos.entrySet()) {
            stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "\n");
        }
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        th.printStackTrace(printWriter);
        for (Throwable cause = th.getCause(); cause != null; cause = cause.getCause()) {
            cause.printStackTrace(printWriter);
        }
        printWriter.close();
        stringBuffer.append(stringWriter.toString());
        Log.e("CrashHandler", stringBuffer.toString());
        try {
            long jCurrentTimeMillis = System.currentTimeMillis();
            String str = this.formatter.format(new Date()) + "-" + jCurrentTimeMillis + ".txt";
            if (Environment.getExternalStorageState().equals("mounted")) {
                String str2 = Environment.getExternalStorageDirectory().getPath() + "/crash_log/";
                File file = new File(str2);
                if (!file.exists()) {
                    file.mkdirs();
                }
                File file2 = new File(str2 + str);
                file2.createNewFile();
                file2.setWritable(true);
                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                fileOutputStream.write(stringBuffer.toString().getBytes());
                fileOutputStream.close();
            }
            return str;
        } catch (Exception e) {
            Log.e("CrashHandler", "an error occured while writing file...", e);
            return null;
        }
    }
}
