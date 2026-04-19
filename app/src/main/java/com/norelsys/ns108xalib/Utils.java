package com.norelsys.ns108xalib;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Utils {
    static final int BRIGHTNESS_BIT = 1;
    static final int CAMERA_BUFFER_SIZE = 1048576;
    static final byte CARD_CLR_PWD = 2;
    static final byte CARD_ERASE = 8;
    static final byte CARD_LOCK = 4;
    static final byte CARD_SET_LOCK = 5;
    static final byte CARD_SET_PWD = 1;
    static final byte CARD_UNLOCK = 0;
    public static final byte CDB_LEN_10 = 10;
    public static final byte EXTERNAL_DISK_LUN = 0;
    static final byte GET_CUR_CMD = -127;
    static final byte GET_DEF_CMD = -121;
    static final byte GET_MAX_CMD = -125;
    static final byte GET_MIN_CMD = -126;
    public static final byte HOST_IN = 0;
    public static final byte HOST_OUT = -128;
    public static final long LBA_16 = 4294967295L;
    public static final int MAX_BUFFER_SIZE_R = 16384;
    public static final int MAX_BUFFER_SIZE_W = 16384;
    public static final int MAX_TRANSFER_SECTOR = 30720;
    public static final int MAX_TRANSFER_SECTOR_R = 30720;
    public static final byte SD_LUN = 1;
    public static final int SECTOR_16 = 65535;
    static final byte SET_CUR_CMD = 1;
    public static final int UVC_SINGLE_RECV_LEN = 65536;
    static final int VC_ENCODING_UNIT = 7;
    static final int VC_INPUT_TERMINAL = 2;
    static final int VC_PROCESSING_UNIT = 5;
    public static final int VS_FORMAT_MJPEG = 6;
    public static final int VS_FORMAT_UNCOMPRESSED = 4;

    public static byte[] intToBytesBE(int i) {
        return new byte[]{(byte) (((-16777216) & i) >> 24), (byte) ((16711680 & i) >> 16), (byte) ((65280 & i) >> 8), (byte) (i & 255)};
    }

    public static byte[] intToBytesLE(int i) {
        return new byte[]{(byte) (i & 255), (byte) ((65280 & i) >> 8), (byte) ((16711680 & i) >> 16), (byte) (((-16777216) & i) >> 24)};
    }

    public static boolean isIntNumberNBitONEInBinary(int i, int i2) {
        return ((i >> i2) & 1) > 0;
    }

    public static short byteToShortLE(byte[] bArr) {
        return ByteBuffer.wrap(bArr, 0, bArr.length).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(0);
    }

    public static int bytesToUnsignedInt(byte[] bArr, int i, int i2) {
        if (i2 == 1) {
            return bArr[i] & 255;
        }
        if (i2 == 2) {
            return (bArr[i + 0] & 255) | ((bArr[i + 1] & 255) << 8);
        }
        return (bArr[i + 0] & 255) | ((bArr[i + 3] & 255) << 24) | ((bArr[i + 2] & 255) << 16) | ((bArr[i + 1] & 255) << 8);
    }

    public static int bytesToUnsignedIntBE(byte[] bArr, int i, int i2) {
        if (i2 == 1) {
            return bArr[i] & 255;
        }
        if (i2 == 2) {
            return (bArr[i + 1] & 255) | ((bArr[i + 0] & 255) << 8);
        }
        return (bArr[i + 3] & 255) | ((bArr[i + 0] & 255) << 24) | ((bArr[i + 1] & 255) << 16) | ((bArr[i + 2] & 255) << 8);
    }
}
