package com.jni.finger;

public class fingerFp {

    public int f82id;
    public int type = 0;
    public String name = null;

    public int GetType() {
        return this.type;
    }

    public int SetType(int i) {
        this.type = i;
        return 0;
    }

    public int Getid() {
        return this.f82id;
    }

    public int Setid(int i) {
        this.f82id = i;
        return 0;
    }

    public String getname() {
        return this.name;
    }

    public void Setname(String str) {
        this.name = str;
    }
}
