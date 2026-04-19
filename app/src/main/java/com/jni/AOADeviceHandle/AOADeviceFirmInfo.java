package com.jni.AOADeviceHandle;

import java.util.Arrays;

public class AOADeviceFirmInfo {
    private int fwint;
    private String manuFacture = null;
    private String modelName = null;

    private String f75sn = null;
    private String fwVersion = null;
    private String hwVersion = null;
    private byte[] license = null;

    public String getManuFacture() {
        return this.manuFacture;
    }

    public String getModelName() {
        return this.modelName;
    }

    public String getSn() {
        return this.f75sn;
    }

    public String getFwVersion() {
        return this.fwVersion;
    }

    public String getHwVersion() {
        return this.hwVersion;
    }

    public byte[] getLicense() {
        return this.license;
    }

    public void SetmanuFacture(String str) {
        this.manuFacture = str;
    }

    public void SetmodelName(String str) {
        this.modelName = str;
    }

    public void Setsn(String str) {
        this.f75sn = str;
    }

    public void SetfwVersion(String str) {
        this.fwVersion = str;
    }

    public void SethwVersion(String str) {
        this.hwVersion = str;
    }

    public void Setlicense(byte[] bArr) {
        this.license = bArr;
    }

    public int Getfwint() {
        return this.fwint;
    }

    public void Setfwint(int i) {
        this.fwint = i;
    }

    public String toString() {
        return "AOADeviceFirmInfo{manuFacture='" + this.manuFacture + "', modelName='" + this.modelName + "', sn='" + this.f75sn + "', fwVersion='" + this.fwVersion + "', hwVersion='" + this.hwVersion + "', license=" + Arrays.toString(this.license) + '}';
    }
}
