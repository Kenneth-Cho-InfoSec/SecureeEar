package com.jni;

import com.jni.lic.LicInfo;

public class LicCommand {
    public native int checklic(String str, byte[] bArr);

    public native int cleanlic();

    public native byte[] getlic(String str, String str2);

    public native byte[] localGetlic(String str, String str2);

    public native int localPrepare();

    public native int localinfoGet(LicInfo licInfo);

    public native int localnumMaxset(int i);

    public native int loging(String str, String str2, String str3, String str4);

    public native int start(String str, String str2, String str3, int i);

    static {
        System.loadLibrary("UStorageDeviceFS");
    }
}
