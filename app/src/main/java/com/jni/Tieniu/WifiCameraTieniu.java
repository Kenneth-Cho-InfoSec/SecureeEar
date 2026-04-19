package com.jni.Tieniu;

import java.util.ArrayList;

public class WifiCameraTieniu {
    public static final int LED_CAMERA = 1;

    public static class conConfigHolder {
        public static WifiCameraTieniu gWifiCameraApi = new WifiCameraTieniu();
    }

    public native int Start();

    public native int callback(TieniuCallBack tieniuCallBack);

    public native int cameraAcceptFileList(ArrayList<tieniuFileInfoBean> arrayList);

    public native int changemode(int i);

    public native int delfile(String str);

    public native int format();

    public native int getBattery();

    public native int getdevinfo(Tieniudevinfo tieniudevinfo);

    public native int getinfo(TieniuInfo tieniuInfo);

    public native int getzoominfo();

    public native int ishavesdcard();

    public native int reset();

    public native int setB(int i);

    public native int setBrightness(int i);

    public native int setContrast(int i);

    public native int setG(int i);

    public native int setR(int i);

    public native int setResolution(int i);

    public native int setSharpness(int i);

    public native int setlic(String str, byte[] bArr, int i);

    public native int setlicmode(int i);

    public native int takePic();

    public native int takeVideo(int i);

    public native int zoomdown();

    public native int zoomup();

    /* JADX WARN: Type inference failed for: r1v0, types: [com.jni.Tieniu.WifiCameraTieniu$1] */
    public int TieniuStart() {
        int iStart = Start();
        new Thread() {
            @Override
            public void run() {
                WifiCameraTieniu.getInstance().callback(TieniuCallBack.getInstance());
            }
        }.start();
        return iStart;
    }

    public static WifiCameraTieniu getInstance() {
        return conConfigHolder.gWifiCameraApi;
    }

    static {
        System.loadLibrary("WifiCamera");
    }
}
