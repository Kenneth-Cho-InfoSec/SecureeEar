package com.i4season.bkCamera.uirelated.other.i4seasonUtil;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import com.i4season.bkCamera.uirelated.other.logmanage.LogWD;

public class AppSrceenInfo {
    private static AppSrceenInfo mAppScreenInfo = new AppSrceenInfo();
    private float mInchRate = 0.3937f;
    public float PIXEL_DENSITY = 0.0f;
    public float PIXEL_DENSITYDPI = 0.0f;
    public int mScreenWidth = 0;
    public int mScreenHeight = 0;
    private boolean isPhone = true;

    private AppSrceenInfo() {
    }

    public static AppSrceenInfo getInstance() {
        if (mAppScreenInfo == null) {
            mAppScreenInfo = new AppSrceenInfo();
        }
        return mAppScreenInfo;
    }

    public synchronized void setOrientation(Context context) {
        LogWD.writeMsg(this, 8, "__setOrientation__");
        if (!this.isPhone) {
            ((Activity) context).setRequestedOrientation(0);
        } else {
            ((Activity) context).setRequestedOrientation(1);
        }
    }

    public void initScreenPixValue(Context context) {
        if (context == null) {
            return;
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.PIXEL_DENSITY = displayMetrics.density;
        this.PIXEL_DENSITYDPI = displayMetrics.densityDpi;
        this.mScreenWidth = displayMetrics.widthPixels;
        this.mScreenHeight = displayMetrics.heightPixels;
        this.isPhone = (context.getResources().getConfiguration().screenLayout & 15) < 3;
        LogWD.writeMsg(this, 8, "Get the screen is phone or pad. The resoult isphone = " + this.isPhone + "  __initScreenPixValue__");
    }

    public boolean isPhoneVersion() {
        return this.isPhone;
    }

    public int dip2px(float f) {
        return (int) ((f * this.PIXEL_DENSITY) + 0.5f);
    }

    public int dip2px(Context context, float f) {
        return (int) ((f * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public int px2dip(float f) {
        return (int) ((f / this.PIXEL_DENSITY) + 0.5f);
    }

    public int px2dip(Context context, float f) {
        return (int) ((f / context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public int changeCMToPX(Context context, float f) {
        return changeFloatToInteger(f * this.mInchRate * context.getResources().getDisplayMetrics().densityDpi);
    }

    private int changeFloatToInteger(float f) {
        return ((int) (10.0f * f)) % 10 >= 5 ? (int) (f + 1.0f) : (int) f;
    }

    public boolean isPortrait(Context context) {
        return context.getResources().getConfiguration().orientation != 2;
    }
}
