package com.jni.Tieniu;

public class Tieniudevinfo {

    private String f79sn = null;
    private byte[] license = null;
    private String ssid = null;

    public String getSn() {
        return this.f79sn;
    }

    public String getssid() {
        return this.ssid;
    }

    public byte[] getLicense() {
        return this.license;
    }

    public void Setsn(String str) {
        this.f79sn = str;
    }

    public void Setssid(String str) {
        this.ssid = str;
    }

    public void Setlicense(byte[] bArr) {
        this.license = bArr;
    }
}
