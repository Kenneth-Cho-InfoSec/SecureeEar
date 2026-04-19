package com.i4season.bkCamera;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import com.i4season.bkCamera.logicrelated.database.WdOpenHelper;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.Constant;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.SpUtils;
import com.i4season.bkCamera.uirelated.other.logmanage.LogWD;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WDApplication extends Application {
    private static Lock reentantLock = new ReentrantLock();
    private static WDApplication wdApplication;
    private int activityShowCounts;
    private SpUtils spUtils;
    private boolean currentIsStart = true;
    private List<BackgroundOrForegroundListener> backgroundOrForegroundListener = new ArrayList();

    public interface BackgroundOrForegroundListener {
        void onBackground();

        void onForeground();
    }

    static int access$008(WDApplication wDApplication) {
        int i = wDApplication.activityShowCounts;
        wDApplication.activityShowCounts = i + 1;
        return i;
    }

    static int access$010(WDApplication wDApplication) {
        int i = wDApplication.activityShowCounts;
        wDApplication.activityShowCounts = i - 1;
        return i;
    }

    public static WDApplication getInstance() {
        if (wdApplication == null) {
            try {
                reentantLock.lock();
                if (wdApplication == null) {
                    wdApplication = new WDApplication();
                }
            } finally {
                reentantLock.unlock();
            }
        }
        return wdApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        wdApplication = this;
        this.spUtils = new SpUtils(this);
        if (!this.spUtils.getBoolean(Constant.LAUNCHER_FIRST, true)) {
            init();
        } else {
            AppConfigInitInstance.getInstance().initMulitLanguage(getApplicationContext());
        }
        disableAPIDialog();
        backgroundOrForeground(this);
    }

    public void init() {
        AppConfigInitInstance.getInstance().initApplicationInformation(getApplicationContext());
        AppConfigInitInstance.getInstance().initDeviceJniLibValue(getApplicationContext());
    }

    public WdOpenHelper getWdSQLite() {
        LogWD.writeMsg(this, 32, "getSQLite()");
        return AppConfigInitInstance.getInstance().getWdSQLite(getApplicationContext());
    }

    public void createTempCacheDir() {
        AppConfigInitInstance.getInstance().createTempCacheDir();
    }

    public SharedPreferences getLanguageRecord() {
        return AppConfigInitInstance.getInstance().getLanguageRecord();
    }

    public void setLanguageRecord(SharedPreferences sharedPreferences) {
        AppConfigInitInstance.getInstance().setLanguageRecord(sharedPreferences);
    }

    private void disableAPIDialog() {
        if (Build.VERSION.SDK_INT < 28) {
            return;
        }
        try {
            Class<?> cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread", new Class[0]);
            declaredMethod.setAccessible(true);
            Object objInvoke = declaredMethod.invoke(null, new Object[0]);
            Field declaredField = cls.getDeclaredField("mHiddenApiWarningShown");
            declaredField.setAccessible(true);
            declaredField.setBoolean(objInvoke, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void backgroundOrForeground(Application application) {
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
                WDApplication.access$008(WDApplication.this);
                if (WDApplication.this.activityShowCounts == 1) {
                    LogWD.writeMsg(this, 1, "APP进入了前台");
                    WDApplication.this.currentIsStart = true;
                    if (WDApplication.this.backgroundOrForegroundListener == null || WDApplication.this.backgroundOrForegroundListener.size() <= 0) {
                        return;
                    }
                    Iterator it = WDApplication.this.backgroundOrForegroundListener.iterator();
                    while (it.hasNext()) {
                        ((BackgroundOrForegroundListener) it.next()).onForeground();
                    }
                }
            }

            @Override
            public void onActivityStopped(Activity activity) {
                WDApplication.access$010(WDApplication.this);
                if (WDApplication.this.activityShowCounts == 0) {
                    LogWD.writeMsg(this, 1, "APP进入了后台");
                    WDApplication.this.currentIsStart = false;
                    if (WDApplication.this.backgroundOrForegroundListener == null || WDApplication.this.backgroundOrForegroundListener.size() <= 0) {
                        return;
                    }
                    Iterator it = WDApplication.this.backgroundOrForegroundListener.iterator();
                    while (it.hasNext()) {
                        ((BackgroundOrForegroundListener) it.next()).onBackground();
                    }
                }
            }
        });
    }

    public boolean isCurrentIsStart() {
        return this.currentIsStart;
    }

    public void setBackgroundOrForegroundListener(BackgroundOrForegroundListener backgroundOrForegroundListener) {
        this.backgroundOrForegroundListener.add(backgroundOrForegroundListener);
    }

    public void removeBackgroundOrForegroundListener(BackgroundOrForegroundListener backgroundOrForegroundListener) {
        this.backgroundOrForegroundListener.remove(backgroundOrForegroundListener);
    }
}
