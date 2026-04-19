package com.norelsys.ns108xalib.Model;

public class ImgProcessUnit {
    public short curValue;
    public short defaultValue;
    public short maxValue;
    public short minValue;

    public ImgProcessUnit(short s, short s2, short s3, short s4) {
        this.defaultValue = s2;
        this.minValue = s3;
        this.maxValue = s4;
        this.curValue = s;
    }

    public short getMaxValue() {
        return this.maxValue;
    }

    public short getMinValue() {
        return this.minValue;
    }

    public short getDefaultValue() {
        return this.defaultValue;
    }

    public short getCurValue() {
        return this.curValue;
    }
}
