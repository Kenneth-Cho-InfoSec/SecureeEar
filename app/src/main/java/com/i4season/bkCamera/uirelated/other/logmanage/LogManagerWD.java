package com.i4season.bkCamera.uirelated.other.logmanage;

import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.format.DateFormat;
import android.util.Log;
import com.bumptech.glide.load.Key;
import com.i4season.bkCamera.uirelated.other.AppPathInfo;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LogManagerWD {
    public static int LOG_SWITCH = 0;
    public static final int MODE_ALL = 16777215;
    public static final int MODE_BACKUP = 512;
    public static final int MODE_CLOSE = 0;
    public static final int MODE_CONTACTS = 2048;
    public static final int MODE_DATA = 2;
    public static final int MODE_DATABASE = 32;
    public static final int MODE_FILEOPEN = 1024;
    public static final int MODE_INIT = 1;
    public static final int MODE_LOGIN = 64;
    public static final int MODE_PAGE = 8;
    public static final int MODE_PDF = 65536;
    public static final int MODE_PLUG = 4;
    public static final int MODE_REMOTE = 128;
    public static final int MODE_RESTORE = 16384;
    public static final int MODE_SEARCH = 4096;
    public static final int MODE_TAKE = 16;
    public static final int MODE_THUMB = 8192;
    public static final int MODE_TRANSFER = 256;
    public static final int MODE_VIDEO = 32768;
    private static String logname = "";
    private Handler handler;
    private HandlerThread handlerThread;

    public static class LogManagerWDHolder {
        public static LogManagerWD logManager = new LogManagerWD();
    }

    private LogManagerWD() {
        this.handlerThread = new HandlerThread("writelog");
        this.handlerThread.start();
        this.handler = new Handler(this.handlerThread.getLooper());
        logname = new SimpleDateFormat("HH_mm_ss").format(Calendar.getInstance().getTime());
    }

    public static LogManagerWD getInstance() {
        return LogManagerWDHolder.logManager;
    }

    public void Log(Object obj, int i, String str) {
        String className;
        if (checkPermit(i)) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            int length = stackTrace.length;
            int lineNumber = 0;
            int i2 = 0;
            while (true) {
                if (i2 >= length) {
                    className = "";
                    break;
                }
                StackTraceElement stackTraceElement = stackTrace[i2];
                if (stackTraceElement.getClassName().equals(obj.getClass().getName())) {
                    className = stackTraceElement.getClassName();
                    lineNumber = stackTraceElement.getLineNumber();
                    break;
                }
                i2++;
            }
            String str2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(Calendar.getInstance().getTime());
            Log.d("Ypc", "[" + str2 + "]*[ " + className + " + line " + lineNumber + "]\n*[" + str + "]");
            writeMsg("[" + str2 + "]*[ " + className + " + line " + lineNumber + "]\n*[" + str + "]");
        }
    }

    public void Log(Object obj, String str) {
        String className;
        if (checkPermit(16777215)) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            int length = stackTrace.length;
            int lineNumber = 0;
            int i = 0;
            while (true) {
                if (i >= length) {
                    className = "";
                    break;
                }
                StackTraceElement stackTraceElement = stackTrace[i];
                if (stackTraceElement.getClassName().equals(obj.getClass().getName())) {
                    className = stackTraceElement.getClassName();
                    lineNumber = stackTraceElement.getLineNumber();
                    break;
                }
                i++;
            }
            String str2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(Calendar.getInstance().getTime());
            Log.d("Ypc", "[" + str2 + "]*[ " + className + " + line " + lineNumber + "]\n*[" + str + "]");
            writeMsg("[" + str2 + "]*[ " + className + " + line " + lineNumber + "]\n*[" + str + "]");
        }
    }

    public void Log(String str) {
        Log.d("Ypc", str);
        writeMsg(str);
    }

    private boolean checkPermit(int i) {
        return (i & LOG_SWITCH) != 0;
    }

    private void writeMsg(String str) {
        this.handler.post(new WriteRunnable(str));
    }

    private void writeError(String str) {
        RandomAccessFile randomAccessFileMakeErrorFile = makeErrorFile();
        if (randomAccessFileMakeErrorFile != null) {
            try {
                randomAccessFileMakeErrorFile.seek(randomAccessFileMakeErrorFile.length());
                randomAccessFileMakeErrorFile.write((str + "\n\n").getBytes("gbk"));
                randomAccessFileMakeErrorFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class WriteRunnable implements Runnable {
        private String msg;

        public WriteRunnable(String str) {
            this.msg = str;
        }

        @Override
        public void run() {
            RandomAccessFile randomAccessFileMakeRandomFile = LogManagerWD.this.makeRandomFile();
            if (randomAccessFileMakeRandomFile != null) {
                try {
                    randomAccessFileMakeRandomFile.seek(randomAccessFileMakeRandomFile.length());
                    randomAccessFileMakeRandomFile.write((this.msg + "\n\n").getBytes(Key.STRING_CHARSET_NAME));
                    randomAccessFileMakeRandomFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public RandomAccessFile makeRandomFile() {
        if (Environment.getExternalStorageState().equals("mounted")) {
            File file = new File(getLogPath());
            if (!file.exists()) {
                file.mkdir();
            }
            Date time = Calendar.getInstance().getTime();
            new DateFormat();
            File file2 = new File(file.getPath() + File.separator + DateFormat.format("yyyy-MM-dd", time).toString());
            if (!file2.exists()) {
                file2.mkdir();
            }
            try {
                return new RandomAccessFile(new File(file2.getPath() + File.separator + logname + ".txt"), "rw");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private RandomAccessFile makeErrorFile() {
        if (Environment.getExternalStorageState().equals("mounted")) {
            File file = new File(getLogPath());
            if (!file.exists()) {
                file.mkdir();
            }
            File file2 = new File(file.getPath() + File.separator + DateFormat.format("yyyy-MM-dd", Calendar.getInstance().getTime()).toString());
            if (!file2.exists()) {
                file2.mkdir();
            }
            try {
                return new RandomAccessFile(new File(file2.getPath() + File.separator + logname + "Error.txt"), "rw");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String getLogPath() {
        return AppPathInfo.getLogPath();
    }
}
