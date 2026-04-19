package com.norelsys.ns108xalib.Model;

public enum StorageState {
    ERR(0),
    READY(1),
    NOTREADY(2);

    int state;

    StorageState(int i) {
        this.state = i;
    }

    public int getState() {
        return this.state;
    }
}
