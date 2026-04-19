package com.jnibean;

public class VSFileInfoObject {
    private byte[] mFileObjectArr = null;
    private int mLength;

    public void initVSFileInfoObjectValue(byte[] bArr, int i) {
        this.mFileObjectArr = bArr;
        this.mLength = i;
    }

    public boolean acceptVSFileInfoObjectValue(byte[] bArr) {
        if (this.mFileObjectArr == null) {
            return false;
        }
        for (int i = 0; i < this.mLength; i++) {
            bArr[i] = this.mFileObjectArr[i];
        }
        return true;
    }

    public byte[] getmFileObjectArr() {
        return this.mFileObjectArr;
    }

    public void setmFileObjectArr(byte[] bArr) {
        this.mFileObjectArr = bArr;
    }

    public int getmLength() {
        return this.mLength;
    }

    public void setmLength(int i) {
        this.mLength = i;
    }
}
