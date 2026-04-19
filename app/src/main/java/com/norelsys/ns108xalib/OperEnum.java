package com.norelsys.ns108xalib;

public enum OperEnum {
    READ10(40),
    READ16(136),
    WRITE10(42),
    WRITE16(138);

    int opcode;

    OperEnum(int i) {
        this.opcode = i;
    }

    public int getValue() {
        return this.opcode;
    }
}
