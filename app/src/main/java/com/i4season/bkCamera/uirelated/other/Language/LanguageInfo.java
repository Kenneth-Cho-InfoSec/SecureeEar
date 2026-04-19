package com.i4season.bkCamera.uirelated.other.Language;

import android.content.Context;
import com.i4season.bkCamera.WDApplication;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LanguageInfo {
    public static boolean isAUTO = true;
    private static LanguageInfo languageInfo;
    private static Lock reentantLock = new ReentrantLock();
    private final String DEFAULT_AUTO = Strings.LANGUAGE_AUTO;
    private final String SET_LANGUAGE = "set_language";
    private final String LANGUAGE_CODE = "language";

    private LanguageInfo() {
    }

    public static LanguageInfo getInstance() {
        if (languageInfo == null) {
            try {
                reentantLock.lock();
                if (languageInfo == null) {
                    languageInfo = new LanguageInfo();
                }
            } finally {
                reentantLock.unlock();
            }
        }
        return languageInfo;
    }

    public String getSetLanguage(Context context) {
        getLanguageRecord(context);
        String string = WDApplication.getInstance().getLanguageRecord().getString("language", "");
        return (string.equals("") || string.equals(Strings.LANGUAGE_AUTO)) ? Strings.LANGUAGE_AUTO : string;
    }

    public void setLanguage(String str) {
        if (WDApplication.getInstance().getLanguageRecord() != null) {
            WDApplication.getInstance().getLanguageRecord().edit().putString("language", str).commit();
        }
    }

    public void getLanguageRecord(Context context) {
        if (WDApplication.getInstance().getLanguageRecord() == null) {
            WDApplication.getInstance().setLanguageRecord(context.getSharedPreferences("set_language", 0));
        }
    }
}
