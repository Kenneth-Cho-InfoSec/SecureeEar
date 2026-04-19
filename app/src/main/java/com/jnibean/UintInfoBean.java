package com.jnibean;

import java.util.ArrayList;

public class UintInfoBean {
    private ArrayList<CardInfoBean> mCardInfoArr;
    private int mCount;

    public int getmCount() {
        return this.mCount;
    }

    public void setmCount(int i) {
        this.mCount = i;
    }

    public ArrayList<CardInfoBean> getmCardInfoArr() {
        return this.mCardInfoArr;
    }

    public void setmCardInfoArr(ArrayList<CardInfoBean> arrayList) {
        this.mCardInfoArr = arrayList;
    }
}
