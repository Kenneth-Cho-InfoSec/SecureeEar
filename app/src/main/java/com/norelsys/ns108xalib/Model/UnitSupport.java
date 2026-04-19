package com.norelsys.ns108xalib.Model;

public class UnitSupport {
    int bmControls;
    int unitId;
    int unitType;
    int uvcInterface;

    public UnitSupport() {
    }

    public UnitSupport(int i, int i2, int i3, int i4) {
        this.unitType = i;
        this.uvcInterface = i2;
        this.unitId = i3;
        this.bmControls = i4;
    }

    public int getUnitType() {
        return this.unitType;
    }

    public int getUvcInterface() {
        return this.uvcInterface;
    }

    public int getUnitId() {
        return this.unitId;
    }

    public int getBmControls() {
        return this.bmControls;
    }
}
