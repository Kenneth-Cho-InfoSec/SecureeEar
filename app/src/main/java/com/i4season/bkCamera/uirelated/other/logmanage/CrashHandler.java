package com.i4season.bkCamera.uirelated.other.logmanage;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import com.i4season.bkCamera.uirelated.other.AppPathInfo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    public static final String TAG = "CrashHandler";
    private static CrashHandler instance;
    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Map<String, String> infos = new HashMap();
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        if (instance == null) {
            instance = new CrashHandler();
        }
        return instance;
    }

    public void init(Context context) {
        this.mContext = context;
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable th) {
        Thread.UncaughtExceptionHandler uncaughtExceptionHandler;
        if (!handleException(th) && (uncaughtExceptionHandler = this.mDefaultHandler) != null) {
            uncaughtExceptionHandler.uncaughtException(thread, th);
        } else {
            Process.killProcess(Process.myPid());
            System.exit(1);
        }
    }

    private boolean handleException(Throwable th) {
        if (th == null) {
            return false;
        }
        collectDeviceInfo(this.mContext);
        saveCatchInfo2File(th);
        return true;
    }

    public void collectDeviceInfo(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 1);
            if (packageInfo != null) {
                String str = packageInfo.versionName == null ? "null" : packageInfo.versionName;
                String str2 = packageInfo.versionCode + "";
                this.infos.put("versionName", str);
                this.infos.put("versionCode", str2);
            }
        } catch (PackageManager.NameNotFoundException unused) {
        }
        for (Field field : Build.class.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                this.infos.put(field.getName(), field.get(null).toString());
            } catch (Exception unused2) {
            }
        }
    }

    private String saveCatchInfo2File(Throwable th) {
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
        try {
            long jCurrentTimeMillis = System.currentTimeMillis();
            String str = "crash-" + this.formatter.format(new Date()) + "-" + jCurrentTimeMillis + ".txt";
            if (Environment.getExternalStorageState().equals("mounted")) {
                String logPath = AppPathInfo.getLogPath();
                File file = new File(logPath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(logPath + File.separator + str);
                fileOutputStream.write(stringBuffer.toString().getBytes());
                fileOutputStream.close();
            }
            return str;
        } catch (Exception unused) {
            return null;
        }
    }

    private void sendCrashLog2PM(String str) throws Throwable {
        FileInputStream fileInputStream;
        String line;
        if (!new File(str).exists()) {
            return;
        }
        BufferedReader bufferedReader = null;
        bufferedReader = null;
        bufferedReader = null;
        BufferedReader bufferedReader2 = null;
        BufferedReader bufferedReader3 = null;
        bufferedReader = null;
        bufferedReader = null;
        try {
            try {
                try {
                    fileInputStream = new FileInputStream(str);
                    try {
                        BufferedReader bufferedReader4 = new BufferedReader(new InputStreamReader(fileInputStream, "GBK"));
                        do {
                            try {
                                line = bufferedReader4.readLine();
                            } catch (FileNotFoundException e) {
                                bufferedReader2 = bufferedReader4;
                                e = e;
                                e.printStackTrace();
                                bufferedReader2.close();
                                fileInputStream.close();
                                bufferedReader = bufferedReader2;
                            } catch (IOException e2) {
                                bufferedReader3 = bufferedReader4;
                                e = e2;
                                e.printStackTrace();
                                bufferedReader3.close();
                                fileInputStream.close();
                                bufferedReader = bufferedReader3;
                            } catch (Throwable th) {
                                bufferedReader = bufferedReader4;
                                th = th;
                                try {
                                    bufferedReader.close();
                                    fileInputStream.close();
                                } catch (IOException e3) {
                                    e3.printStackTrace();
                                }
                                throw th;
                            }
                        } while (line != null);
                        bufferedReader4.close();
                        fileInputStream.close();
                        bufferedReader = line;
                    } catch (FileNotFoundException e4) {
                        e = e4;
                    } catch (IOException e5) {
                        e = e5;
                    }
                } catch (IOException e6) {
                    e6.printStackTrace();
                }
            } catch (FileNotFoundException e7) {
                e = e7;
                fileInputStream = null;
            } catch (IOException e8) {
                e = e8;
                fileInputStream = null;
            } catch (Throwable th2) {
                th = th2;
                fileInputStream = null;
            }
        } catch (Throwable th3) {
            th = th3;
        }
    }
}
