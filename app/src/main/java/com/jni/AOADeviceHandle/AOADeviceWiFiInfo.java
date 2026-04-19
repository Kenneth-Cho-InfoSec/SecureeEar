package com.jni.AOADeviceHandle;

public class AOADeviceWiFiInfo {
    int Channel;
    private String SSID = "";
    private String PASSWD = "";

    public String getSSID() {
        return this.SSID;
    }

    public String getPASSWD() {
        return this.PASSWD;
    }

    public void SetSSID(String str) {
        this.SSID = str;
    }

    public void SetPASSWD(String str) {
        this.PASSWD = str;
    }

    public void SetChannel(int i) {
        this.Channel = i;
    }

    public int getChannel() {
        return this.Channel;
    }
}
