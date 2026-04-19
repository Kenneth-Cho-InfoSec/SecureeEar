package com.i4season.bkCamera.uirelated.other.i4seasonUtil;

import android.R;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import com.i4season.bkCamera.WDApplication;
import com.i4season.i4season_camera.C0413R;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;

public final class SystemUtil {
    private SystemUtil() {
        throw new UnsupportedOperationException();
    }

    public static boolean isKitkat() {
        return Build.VERSION.SDK_INT == 19;
    }

    public static boolean isAndroid5() {
        return isAtLeastVersion(21);
    }

    public static boolean isAtLeastVersion(int i) {
        return Build.VERSION.SDK_INT >= i;
    }

    public static boolean hasCameraActivity() {
        return new Intent("android.media.action.IMAGE_CAPTURE").resolveActivity(WDApplication.getInstance().getPackageManager()) != null;
    }

    public static boolean hasCamera() {
        return WDApplication.getInstance().getPackageManager().hasSystemFeature("android.hardware.camera");
    }

    public static boolean hasFlashlight() {
        return WDApplication.getInstance().getPackageManager().hasSystemFeature("android.hardware.camera.flash");
    }

    public static boolean hasManualSensor() {
        return isAndroid5() && WDApplication.getInstance().getPackageManager().hasSystemFeature("android.hardware.camera.capability.manual_sensor");
    }

    public static boolean isAppInstalled(String str) {
        return WDApplication.getInstance().getPackageManager().getLaunchIntentForPackage(str) != null;
    }

    public static boolean isLandscape() {
        Display defaultDisplay = ((WindowManager) WDApplication.getInstance().getSystemService("window")).getDefaultDisplay();
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= 13) {
            defaultDisplay.getSize(point);
        }
        return point.x > point.y;
    }

    public static boolean isTablet() {
        return (WDApplication.getInstance().getResources().getConfiguration().screenLayout & 15) >= 3;
    }

    private static Display getDefaultDisplay() {
        return ((WindowManager) WDApplication.getInstance().getSystemService("window")).getDefaultDisplay();
    }

    public static int getDisplaySize() {
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= 13) {
            getDefaultDisplay().getSize(point);
        }
        return Math.max(point.x, point.y);
    }

    public static double getPhysicalDisplaySize() {
        getDefaultDisplay().getMetrics(new DisplayMetrics());
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= 13) {
            getDefaultDisplay().getSize(point);
        }
        return Math.max(point.x / r0.xdpi, point.y / r0.ydpi);
    }

    private static String getDeviceId() {
        return ((TelephonyManager) WDApplication.getInstance().getSystemService("phone")).getDeviceId();
    }

    public static String getUserCountry() {
        String lowerCase;
        String networkCountryIso;
        WDApplication wDApplication = WDApplication.getInstance();
        TelephonyManager telephonyManager = (TelephonyManager) wDApplication.getSystemService("phone");
        String simCountryIso = telephonyManager.getSimCountryIso();
        if (simCountryIso != null && simCountryIso.length() == 2) {
            lowerCase = simCountryIso.toUpperCase(Locale.getDefault());
        } else {
            lowerCase = (telephonyManager.getPhoneType() == 2 || (networkCountryIso = telephonyManager.getNetworkCountryIso()) == null || networkCountryIso.length() != 2) ? null : networkCountryIso.toLowerCase(Locale.getDefault());
        }
        return (lowerCase == null || lowerCase.length() != 2) ? wDApplication.getResources().getConfiguration().locale.getCountry() : lowerCase;
    }

    public static int getLargeMemoryClass() {
        return ((ActivityManager) WDApplication.getInstance().getSystemService("activity")).getLargeMemoryClass();
    }

    public static void setBlackStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = activity.getWindow();
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(activity.getResources().getColor(C0413R.color.appblack));
        }
    }

    public static void setWhiteStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = activity.getWindow();
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(activity.getResources().getColor(C0413R.color.appwhite));
            window.getDecorView().setSystemUiVisibility(8192);
        }
    }

    public static void setTextDark(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            activity.getWindow().getDecorView().setSystemUiVisibility(8192);
        }
    }

    public static boolean FlymeSetStatusBarLightMode(Window window, boolean z) {
        if (window != null) {
            try {
                WindowManager.LayoutParams attributes = window.getAttributes();
                Field declaredField = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field declaredField2 = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                declaredField.setAccessible(true);
                declaredField2.setAccessible(true);
                int i = declaredField.getInt(null);
                int i2 = declaredField2.getInt(attributes);
                declaredField2.setInt(attributes, z ? i2 | i : (~i) & i2);
                window.setAttributes(attributes);
                return true;
            } catch (Exception unused) {
            }
        }
        return false;
    }

    public static boolean MIUISetStatusBarLightMode(Window window, boolean z) {
        if (window != null) {
            Class<?> cls = window.getClass();
            try {
                Class<?> cls2 = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                int i = cls2.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE").getInt(cls2);
                Method method = cls.getMethod("setExtraFlags", Integer.TYPE, Integer.TYPE);
                if (z) {
                    method.invoke(window, Integer.valueOf(i), Integer.valueOf(i));
                } else {
                    method.invoke(window, 0, Integer.valueOf(i));
                }
                return true;
            } catch (Exception unused) {
            }
        }
        return false;
    }

    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    public static Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    public static String getSystemModel() {
        return Build.MANUFACTURER + "_" + Build.MODEL;
    }

    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        if (telephonyManager != null) {
            return telephonyManager.getDeviceId();
        }
        return null;
    }

    public static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getVerName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void setTransparent(Activity activity) {
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        transparentStatusBar(activity);
        setRootView(activity);
    }

    private static void transparentStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            activity.getWindow().addFlags(Integer.MIN_VALUE);
            activity.getWindow().clearFlags(67108864);
            activity.getWindow().addFlags(134217728);
            activity.getWindow().setStatusBarColor(0);
            activity.getWindow().getDecorView().setSystemUiVisibility(8192);
            return;
        }
        activity.getWindow().addFlags(67108864);
    }

    private static void setRootView(Activity activity) {
        ViewGroup viewGroup = (ViewGroup) activity.findViewById(R.id.content);
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = viewGroup.getChildAt(i);
            if (childAt instanceof ViewGroup) {
                childAt.setFitsSystemWindows(true);
                ((ViewGroup) childAt).setClipToPadding(true);
            }
        }
    }

    public static String getVersion(Context context) {
        try {
            return "V" + context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "version is null";
        }
    }
}
