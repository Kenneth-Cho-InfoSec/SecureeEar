package com.jni;

public class I4Tool {
    public native int checkLic10(byte[] bArr, int i, String str);

    public native int checkLic14(String str, byte[] bArr, int i, String str2);

    public native String checkLic15(byte[] bArr, int i);

    public native int getLic15(byte[] bArr, int i, String str);

    public native int vsSetAppinfo(String str, String str2, String str3, int i);

    static {
        System.loadLibrary("I4Tool");
    }
}
