package com.i4season.bkCamera.uirelated.other.i4seasonUtil;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtils {
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mSp;

    public SpUtils(Context context) {
        this.mSp = context.getSharedPreferences("USEE", 0);
        this.mEditor = this.mSp.edit();
    }

    public String getString(String str, String str2) {
        return this.mSp.getString(str, str2);
    }

    public int getInt(String str, int i) {
        return this.mSp.getInt(str, i);
    }

    public boolean getBoolean(String str, boolean z) {
        return this.mSp.getBoolean(str, z);
    }

    public float getFloat(String str, float f) {
        return this.mSp.getFloat(str, f);
    }

    public void putString(String str, String str2) {
        this.mEditor.putString(str, str2);
        this.mEditor.commit();
    }

    public void putInt(String str, int i) {
        this.mEditor.putInt(str, i);
        this.mEditor.commit();
    }

    public void putBoolean(String str, boolean z) {
        this.mEditor.putBoolean(str, z);
        this.mEditor.commit();
    }

    public void remove(String str) {
        this.mEditor.remove(str);
        this.mEditor.commit();
    }
}
