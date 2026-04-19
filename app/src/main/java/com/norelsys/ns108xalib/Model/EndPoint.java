package com.norelsys.ns108xalib.Model;

public class EndPoint {
    int alternateSetting;
    int interfaceNumber;
    int maxPacketSize;

    public EndPoint(int i, int i2, int i3) {
        this.interfaceNumber = i;
        this.alternateSetting = i2;
        this.maxPacketSize = i3;
    }

    public int getInterfaceNumber() {
        return this.interfaceNumber;
    }

    public int getAlternateSetting() {
        return this.alternateSetting;
    }

    public int getMaxPacketSize() {
        return this.maxPacketSize;
    }
}
