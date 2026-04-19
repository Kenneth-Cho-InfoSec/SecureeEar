package com.jni.finger;

public class EnrollNewUser {
    public int index = 0;

    public int f81id = 0;
    public byte[] md5 = null;

    public int Getindex() {
        return this.index;
    }

    public void Setindex(int i) {
        this.index = i;
    }

    public int Getid() {
        return this.f81id;
    }

    public void Setid(int i) {
        this.f81id = i;
    }

    public byte[] getmd5() {
        return this.md5;
    }

    public void Setmd5(byte[] bArr) {
        this.md5 = bArr;
    }
}
