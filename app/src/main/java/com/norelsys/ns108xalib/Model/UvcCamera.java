package com.norelsys.ns108xalib.Model;

public interface UvcCamera {
    boolean commitControlSet(byte b, byte[] bArr, int i, byte[] bArr2);

    boolean probeControlGet(byte b, int i, byte[] bArr);

    boolean probeControlSet(byte b, byte[] bArr, int i, byte[] bArr2);

    boolean setInterface(int i, int i2, byte[] bArr);
}
