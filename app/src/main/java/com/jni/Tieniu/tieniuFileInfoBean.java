package com.jni.Tieniu;

import java.io.Serializable;

public class tieniuFileInfoBean implements Serializable {
    private String mFileName;
    private String mFilePath;
    private long mFileSize;
    private int mFileTime;
    private String mFileUrlPath;
    private String mtimestr;

    public String getmFilePath() {
        return this.mFilePath;
    }

    public void setmFilePath(String str) {
        this.mFilePath = str;
    }

    public String getmFileName() {
        return this.mFileName;
    }

    public void setmFileName(String str) {
        this.mFileName = str;
    }

    public long getmFileSize() {
        return this.mFileSize;
    }

    public void setmFileSize(long j) {
        this.mFileSize = j;
    }

    public int getmFileTime() {
        return this.mFileTime;
    }

    public void setmFileTime(int i) {
        this.mFileTime = i;
    }

    public String getmFileUrlPath() {
        return this.mFileUrlPath;
    }

    public void setmFileUrlPath(String str) {
        this.mFileUrlPath = str;
    }

    public String getmtimestr() {
        return this.mtimestr;
    }

    public void settimestr(String str) {
        this.mtimestr = str;
    }
}
