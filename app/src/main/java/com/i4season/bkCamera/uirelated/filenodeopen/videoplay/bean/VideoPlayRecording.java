package com.i4season.bkCamera.uirelated.filenodeopen.videoplay.bean;

import java.io.Serializable;

public class VideoPlayRecording implements Serializable {
    public static final int devId = 1111;
    private static final long serialVersionUID = 1;
    public static final int userId = 2222;
    private int deviceID;
    private String lastOpenTime;
    private String playPath;
    private String playedTime;
    private int userID;

    public int getDeviceID() {
        return this.deviceID;
    }

    public void setDeviceID(int i) {
        this.deviceID = i;
    }

    public int getUserID() {
        return this.userID;
    }

    public void setUserID(int i) {
        this.userID = i;
    }

    public String getPlayPath() {
        return this.playPath;
    }

    public void setPlayPath(String str) {
        this.playPath = str;
    }

    public String getPlayedTime() {
        return this.playedTime;
    }

    public void setPlayedTime(String str) {
        this.playedTime = str;
    }

    public String getLastOpenTime() {
        return this.lastOpenTime;
    }

    public void setLastOpenTime(String str) {
        this.lastOpenTime = str;
    }

    public String toString() {
        return "VideoPlayRecording [deviceID=" + this.deviceID + ", userID=" + this.userID + ", playPath=" + this.playPath + ", playedTime=" + this.playedTime + ", lastOpenTime=" + this.lastOpenTime + "]";
    }
}
