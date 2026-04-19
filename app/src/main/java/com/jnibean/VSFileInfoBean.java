package com.jnibean;

import java.io.Serializable;

public class VSFileInfoBean implements Serializable {
    private int hasThumb;
    private int mCardID;
    private long mChildCount;
    private short mFileAttr;
    private String mFileName;
    private String mFilePath;
    private long mFileSize;
    private FTATimeInfoBean mFileTime;
    private boolean mIsFile;
    private boolean mOpenFlag;

    public boolean ismOpenFlag() {
        return this.mOpenFlag;
    }

    public void setmOpenFlag(boolean z) {
        this.mOpenFlag = z;
    }

    public boolean ismIsFile() {
        return this.mIsFile;
    }

    public void setmIsFile(boolean z) {
        this.mIsFile = z;
    }

    public String getmFileName() {
        return this.mFileName;
    }

    public void setmFileName(String str) {
        this.mFileName = str;
    }

    public String getmFilePath() {
        return this.mFilePath;
    }

    public void setmFilePath(String str) {
        this.mFilePath = str;
    }

    public long getmFileSize() {
        return this.mFileSize;
    }

    public void setmFileSize(long j) {
        this.mFileSize = j;
    }

    public short getmFileAttr() {
        return this.mFileAttr;
    }

    public void setmFileAttr(short s) {
        this.mFileAttr = s;
    }

    public FTATimeInfoBean getmFileTime() {
        return this.mFileTime;
    }

    public void setmFileTime(FTATimeInfoBean fTATimeInfoBean) {
        this.mFileTime = fTATimeInfoBean;
    }

    public int getmCardID() {
        return this.mCardID;
    }

    public void setmCardID(int i) {
        this.mCardID = i;
    }

    public long getmChildCount() {
        return this.mChildCount;
    }

    public void setmChildCount(long j) {
        this.mChildCount = j;
    }

    public int getmFileThumb() {
        return this.hasThumb;
    }

    public void setmFileThumb(int i) {
        this.hasThumb = i;
    }
}
