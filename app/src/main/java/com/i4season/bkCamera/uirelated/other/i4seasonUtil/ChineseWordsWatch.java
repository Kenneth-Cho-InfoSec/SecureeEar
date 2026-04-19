package com.i4season.bkCamera.uirelated.other.i4seasonUtil;

public class ChineseWordsWatch {
    private char[] mCharArray;
    private boolean mIsChinese;
    private int mStartX;
    private int mStopX;

    public static boolean isChinese(char c) {
        return c >= 19968 && c <= 40869;
    }

    public void setString(String str) {
        this.mCharArray = str.toCharArray();
        this.mStartX = 0;
        this.mStopX = 0;
    }

    public String nextString() {
        this.mStopX = this.mStartX;
        this.mIsChinese = false;
        boolean z = false;
        while (true) {
            int i = this.mStopX;
            char[] cArr = this.mCharArray;
            if (i >= cArr.length) {
                break;
            }
            if (!isChinese(cArr[i])) {
                this.mStopX++;
                z = true;
            } else if (z) {
                this.mIsChinese = false;
            } else {
                this.mStopX++;
                this.mIsChinese = true;
            }
        }
        int i2 = this.mStartX;
        int i3 = this.mStopX;
        String str = i2 == i3 ? null : new String(this.mCharArray, i2, i3 - i2);
        this.mStartX = this.mStopX;
        return str;
    }

    public boolean isChineseWord() {
        return this.mIsChinese;
    }
}
