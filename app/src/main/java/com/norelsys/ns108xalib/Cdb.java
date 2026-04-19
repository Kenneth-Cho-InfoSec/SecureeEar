package com.norelsys.ns108xalib;

public class Cdb {
    byte control;
    byte[] lba;
    byte opcode;
    byte reserved1 = 0;
    byte reserved2 = 0;
    byte[] transLength;

    public Cdb(OperEnum operEnum, long j, int i) {
        this.lba = new byte[4];
        this.transLength = new byte[4];
        this.opcode = (byte) operEnum.getValue();
        this.lba = Utils.intToBytesBE((int) j);
        this.transLength = Utils.intToBytesBE(i);
    }

    public byte[] toBytes() {
        byte[] bArr = new byte[16];
        bArr[0] = this.opcode;
        System.arraycopy(this.lba, 0, bArr, 2, 4);
        System.arraycopy(this.transLength, 2, bArr, 7, 2);
        return bArr;
    }
}
