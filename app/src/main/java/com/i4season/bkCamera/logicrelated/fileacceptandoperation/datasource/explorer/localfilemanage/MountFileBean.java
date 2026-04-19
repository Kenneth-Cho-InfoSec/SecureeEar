package com.i4season.bkCamera.logicrelated.fileacceptandoperation.datasource.explorer.localfilemanage;

public class MountFileBean {
    public static final int MSB_EXTENT_SDCARD = 101;
    public static final int MSB_SDCARD = 100;
    public static final int MSB_USB = 102;
    private String mMFBId = "";
    private String mMFBPath = "";
    private String mMFBName = "";
    private String mMFBSize = "";
    private String mMFBTime = "";
    private String mMFBLimit = "";
    private String mMFBType = "";
    private int mMStorageType = 0;

    public String getMFBId() {
        return this.mMFBId;
    }

    public void setMFBId(String str) {
        this.mMFBId = str;
    }

    public String getMFBPath() {
        return this.mMFBPath;
    }

    public void setMFBPath(String str) {
        this.mMFBPath = str;
    }

    public String getMFBName() {
        return this.mMFBName;
    }

    public void setMFBName(String str) {
        this.mMFBName = str;
    }

    public String getMFBSize() {
        return this.mMFBSize;
    }

    public void setMFBSize(String str) {
        this.mMFBSize = str;
    }

    public String getMFBTime() {
        return this.mMFBTime;
    }

    public void setMFBTime(String str) {
        this.mMFBTime = str;
    }

    public String getMFBLimit() {
        return this.mMFBLimit;
    }

    public void setMFBLimit(String str) {
        this.mMFBLimit = str;
    }

    public String getMFBType() {
        return this.mMFBType;
    }

    public void setMFBType(String str) {
        this.mMFBType = str;
    }

    public int getMStorageType() {
        return this.mMStorageType;
    }

    public void setMStorageType(int i) {
        this.mMStorageType = i;
    }
}
