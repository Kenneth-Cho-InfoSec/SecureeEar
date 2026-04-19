package com.jni.Tieniu;

import com.jni.logmanageWifi.LogWD;

public class TieniuCallBack {
    private TieniuInterface gInterface;

    public interface TieniuInterface {
        void batterychange(int i);

        void filebutton(int i);

        void menubutton(int i);

        void picbutton(int i);

        void sdcardchange(int i);

        void videobuttion(int i);

        void zoomchange(int i);
    }

    public static class conConfigHolder {
        public static TieniuCallBack gWifiCallBack = new TieniuCallBack();
    }

    public TieniuCallBack() {
        LogWD.writeMsg(this, 2, "begin: ");
    }

    public void sdcardchange(int i) {
        LogWD.writeMsg(this, 2, "sdcardchange" + i);
        TieniuInterface tieniuInterface = this.gInterface;
        if (tieniuInterface != null) {
            tieniuInterface.sdcardchange(i);
        }
    }

    public void picbutton(int i) {
        LogWD.writeMsg(this, 2, "picbutton" + i);
        TieniuInterface tieniuInterface = this.gInterface;
        if (tieniuInterface != null) {
            tieniuInterface.picbutton(i);
        }
    }

    public void videobuttion(int i) {
        LogWD.writeMsg(this, 2, "videobuttion" + i);
        TieniuInterface tieniuInterface = this.gInterface;
        if (tieniuInterface != null) {
            tieniuInterface.videobuttion(i);
        }
    }

    public void batterychange(int i) {
        LogWD.writeMsg(this, 2, "batterychange" + i);
        TieniuInterface tieniuInterface = this.gInterface;
        if (tieniuInterface != null) {
            tieniuInterface.batterychange(i);
        }
    }

    public void filebutton(int i) {
        LogWD.writeMsg(this, 2, "filebutton" + i);
        TieniuInterface tieniuInterface = this.gInterface;
        if (tieniuInterface != null) {
            tieniuInterface.filebutton(i);
        }
    }

    public void menubutton(int i) {
        LogWD.writeMsg(this, 2, "menubutton" + i);
        TieniuInterface tieniuInterface = this.gInterface;
        if (tieniuInterface != null) {
            tieniuInterface.menubutton(i);
        }
    }

    public void zoomchange(int i) {
        LogWD.writeMsg(this, 2, "zoomchange" + i);
        TieniuInterface tieniuInterface = this.gInterface;
        if (tieniuInterface != null) {
            tieniuInterface.zoomchange(i);
        }
    }

    public static TieniuCallBack getInstance() {
        return conConfigHolder.gWifiCallBack;
    }

    public void setDeviceStatusInterface(TieniuInterface tieniuInterface) {
        this.gInterface = tieniuInterface;
    }
}
