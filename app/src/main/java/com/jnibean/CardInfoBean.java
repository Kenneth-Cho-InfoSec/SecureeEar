package com.jnibean;

public class CardInfoBean {
    private long mCapcity;
    private int mCardID;
    private String mPrefixName;

    public int getmCardID() {
        return this.mCardID;
    }

    public void setmCardID(int i) {
        this.mCardID = i;
    }

    public long getmCapcity() {
        return this.mCapcity;
    }

    public void setmCapcity(long j) {
        this.mCapcity = j;
    }

    public String getmPrefixName() {
        return this.mPrefixName;
    }

    public void setmPrefixName(String str) {
        this.mPrefixName = str;
    }
}
