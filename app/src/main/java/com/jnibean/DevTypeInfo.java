package com.jnibean;

public class DevTypeInfo {
    public static final int SCSI_1081 = 2;
    public static final int SCSI_1083 = 7;
    public static final int SCSI_ANGO = 5;
    public static final int SCSI_FingerCommer = 4;
    public static final int SCSI_JMIC = 1;
    public static final int SCSI_JMIC2 = 10;
    public static final int SCSI_MUSIC = 3;
    public static final int SCSI_NORMOL = 0;
    public static final int SCSI_RFK = 6;
    public static final int SCSI_WANJIU = 8;
    public static final int SCSI_WANJIU2 = 9;
    public int ftype = 0;
    public int maxlun = 0;
    public int publiclun = 0;
    public int secretlun = 0;
    public boolean havefinger = true;
    public boolean supportpasswd = true;

    public void setftype(int i) {
        this.ftype = i;
    }

    public void setmaxlun(int i) {
        this.maxlun = i;
    }

    public void setpubliclun(int i) {
        this.publiclun = i;
    }

    public void setsecretlun(int i) {
        this.secretlun = i;
    }

    public void sethavefinger(boolean z) {
        this.havefinger = z;
    }

    public void setsupportpasswd(boolean z) {
        this.supportpasswd = z;
    }
}
