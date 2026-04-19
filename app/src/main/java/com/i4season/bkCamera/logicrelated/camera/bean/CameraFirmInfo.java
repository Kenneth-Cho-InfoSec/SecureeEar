package com.i4season.bkCamera.logicrelated.camera.bean;

public class CameraFirmInfo {
    int Channel;
    private int fwint;
    private String vendor = null;
    private String product = null;
    private String version = null;
    private String ssid = null;
    private String PASSWD = "";

    private String f62sn = null;
    private String hwVersion = null;
    private byte[] license = null;

    public String getvendor() {
        return this.vendor;
    }

    public String getproduct() {
        return this.product;
    }

    public String getversion() {
        return this.version;
    }

    public String getssid() {
        return this.ssid;
    }

    public void Setvendor(String str) {
        this.vendor = str;
    }

    public void Setproduct(String str) {
        this.product = str;
    }

    public void Setversion(String str) {
        this.version = str;
    }

    public void Setssid(String str) {
        this.ssid = str;
    }

    public String getVendor() {
        return this.vendor;
    }

    public void setVendor(String str) {
        this.vendor = str;
    }

    public String getProduct() {
        return this.product;
    }

    public void setProduct(String str) {
        this.product = str;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String str) {
        this.version = str;
    }

    public String getSsid() {
        return this.ssid;
    }

    public void setSsid(String str) {
        this.ssid = str;
    }

    public String getSn() {
        return this.f62sn;
    }

    public void setSn(String str) {
        this.f62sn = str;
    }

    public String getHwVersion() {
        return this.hwVersion;
    }

    public void setHwVersion(String str) {
        this.hwVersion = str;
    }

    public byte[] getLicense() {
        return this.license;
    }

    public void setLicense(byte[] bArr) {
        this.license = bArr;
    }

    public int getFwint() {
        return this.fwint;
    }

    public void setFwint(int i) {
        this.fwint = i;
    }

    public String getPASSWD() {
        return this.PASSWD;
    }

    public void setPASSWD(String str) {
        this.PASSWD = str;
    }

    public int getChannel() {
        return this.Channel;
    }

    public void setChannel(int i) {
        this.Channel = i;
    }
}
