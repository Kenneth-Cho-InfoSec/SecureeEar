package com.libyuv.util;

public class YuvUtil {
    public static native void argbToI400(byte[] bArr, int i, int i2, byte[] bArr2, int i3, int i4, int i5);

    public static native void argbToI420(byte[] bArr, int i, int i2, byte[] bArr2, int i3, int i4, int i5);

    public static native void argbToI422(byte[] bArr, int i, int i2, byte[] bArr2, int i3, int i4, int i5);

    public static native void argbToI444(byte[] bArr, int i, int i2, byte[] bArr2, int i3, int i4, int i5);

    public static native void compressYUV(byte[] bArr, int i, int i2, byte[] bArr2, int i3, int i4, int i5, int i6, boolean z);

    public static native void cropYUV(byte[] bArr, int i, int i2, byte[] bArr2, int i3, int i4, int i5, int i6);

    public static native void init(int i, int i2, int i3, int i4);

    public static native void scale1420(byte[] bArr, int i, int i2, byte[] bArr2, int i3, int i4, int i5);

    public static native void scale1422(byte[] bArr, int i, int i2, byte[] bArr2, int i3, int i4, int i5);

    public static native void scaleNV12(byte[] bArr, int i, int i2, byte[] bArr2, int i3, int i4, int i5);

    public static native void yuvI420ToNV21(byte[] bArr, byte[] bArr2, int i, int i2);

    static {
        System.loadLibrary("yuvutil");
    }
}
