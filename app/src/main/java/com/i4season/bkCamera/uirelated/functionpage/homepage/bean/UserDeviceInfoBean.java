package com.i4season.bkCamera.uirelated.functionpage.homepage.bean;

public class UserDeviceInfoBean {
    public static final int TYPE_CAVITY_MIRROR = 4;
    public static final int TYPE_GYROSCOPE = 2;
    public static final int TYPE_INSTRUMENT = 3;
    public static final int TYPE_TEETH = 1;
    public static final int TYPE_UNKNOWN = 0;
    private int mDeviceId;
    private String mDeviceSn;
    private String mDeviceSsid;
    private Boolean mDeviceStatus = false;
    private int mDeviceType;
    private String mDisplayName;
    private String mModleName;

    public int getmDeviceId() {
        return this.mDeviceId;
    }

    public void setmDeviceId(int i) {
        this.mDeviceId = i;
    }

    public int getmDeviceType() {
        return this.mDeviceType;
    }

    public void setmDeviceType(int i) {
        this.mDeviceType = i;
    }

    public String getmModleName() {
        return this.mModleName;
    }

    public void setmModleName(String str) {
        this.mModleName = str;
    }

    public String getmDeviceSn() {
        return this.mDeviceSn;
    }

    public void setmDeviceSn(String str) {
        this.mDeviceSn = str;
    }

    public String getmDisplayName() {
        return this.mDisplayName;
    }

    public void setmDisplayName(String str) {
        this.mDisplayName = str;
    }

    public String getmDeviceSsid() {
        return this.mDeviceSsid;
    }

    public void setmDeviceSsid(String str) {
        this.mDeviceSsid = str;
    }

    public Boolean getmDeviceStatus() {
        return this.mDeviceStatus;
    }

    public void setmDeviceStatus(Boolean bool) {
        this.mDeviceStatus = bool;
    }

    public String toString() {
        return "UserDeviceInfoBean{mDeviceId=" + this.mDeviceId + ", mDeviceType=" + this.mDeviceType + ", mModleName='" + this.mModleName + "', mDeviceSn='" + this.mDeviceSn + "', mDisplayName='" + this.mDisplayName + "', mDeviceSsid='" + this.mDeviceSsid + "', mDeviceStatus=" + this.mDeviceStatus + '}';
    }
}
