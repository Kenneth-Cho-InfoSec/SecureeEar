package com.norelsys.ns108xalib;

import java.util.Random;

public class Cbw {
    byte cbwFlag;
    byte[] cbwSignature;
    byte cbwTag;
    Cdb cdb;
    byte cdbLength;
    byte lun;
    byte[] rand;
    Random random;
    int transferLength;

    public Cbw(OperEnum operEnum, long j, int i, byte b, byte b2) {
        this.cbwSignature = new byte[]{78, 83, 77, 67};
        this.random = new Random();
        this.rand = new byte[2];
        this.random.nextBytes(this.rand);
        this.transferLength = i * 512;
        this.cdb = new Cdb(operEnum, j, i);
        this.cbwTag = this.rand[0];
        this.cbwFlag = b;
        this.cdbLength = b2;
        this.lun = (byte) 0;
    }

    public Cbw(OperEnum operEnum, long j, int i, byte b, byte b2, byte b3) {
        this.cbwSignature = new byte[]{78, 83, 77, 67};
        this.random = new Random();
        this.rand = new byte[2];
        this.transferLength = i * 512;
        this.cdb = new Cdb(operEnum, j, i);
        this.random.nextBytes(this.rand);
        this.cbwTag = this.rand[0];
        this.cbwFlag = b;
        this.cdbLength = b2;
        this.lun = b3;
    }

    public byte[] toBytes() {
        byte[] bArr = new byte[28];
        System.arraycopy(this.cbwSignature, 0, bArr, 0, 4);
        bArr[4] = this.cbwTag;
        System.arraycopy(Utils.intToBytesLE(this.transferLength), 0, bArr, 5, 4);
        bArr[9] = this.cbwFlag;
        bArr[10] = this.lun;
        bArr[11] = this.cdbLength;
        System.arraycopy(this.cdb.toBytes(), 0, bArr, 12, 16);
        return bArr;
    }

    public int getTransferLength() {
        return this.transferLength;
    }
}
