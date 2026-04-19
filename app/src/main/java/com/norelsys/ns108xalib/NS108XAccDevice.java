package com.norelsys.ns108xalib;

import android.content.Context;
import android.os.Handler;
import com.norelsys.ns108xalib.Model.StorageState;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class NS108XAccDevice {
    final boolean DEBUG;
    CircleBuffer circleBuffer;
    Communicator communicator;
    Controller controller;
    FileInputStream fin;
    FileOutputStream fout;
    Handler handler;
    private byte lun;

    private native boolean CheckCardStatus(FileInputStream fileInputStream, FileOutputStream fileOutputStream, byte b, byte[] bArr);

    private native boolean CheckFw(FileInputStream fileInputStream, FileOutputStream fileOutputStream, byte[] bArr);

    private native boolean ChipReset(FileInputStream fileInputStream, FileOutputStream fileOutputStream);

    private native boolean GetCertSerialNumber(FileInputStream fileInputStream, FileOutputStream fileOutputStream, byte[] bArr);

    private native boolean GetCustomerInfo(FileInputStream fileInputStream, FileOutputStream fileOutputStream, byte[] bArr, int i);

    private native int GetDeviceFwVersion(FileInputStream fileInputStream, FileOutputStream fileOutputStream);

    private native int GetMaxLun(FileInputStream fileInputStream, FileOutputStream fileOutputStream);

    private native boolean GetResponse(FileInputStream fileInputStream, FileOutputStream fileOutputStream, byte[] bArr, byte[] bArr2);

    private native int GetSn(FileInputStream fileInputStream, FileOutputStream fileOutputStream, byte[] bArr);

    private native boolean GetUidOdc(FileInputStream fileInputStream, FileOutputStream fileOutputStream, byte[] bArr, byte[] bArr2);

    private native boolean LockUnLockCard(FileInputStream fileInputStream, FileOutputStream fileOutputStream, byte b, byte b2, byte[] bArr);

    private native long ReadCapacity(byte b, FileInputStream fileInputStream, FileOutputStream fileOutputStream);

    private native int ReadDisk(FileInputStream fileInputStream, FileOutputStream fileOutputStream, byte b, long j, int i, byte[] bArr);

    private native boolean ReceiveCSW(FileInputStream fileInputStream, FileOutputStream fileOutputStream);

    private native boolean SendCBW(FileInputStream fileInputStream, FileOutputStream fileOutputStream, byte b, long j, int i, byte b2);

    private native boolean SendChallenge(FileInputStream fileInputStream, FileOutputStream fileOutputStream, byte[] bArr);

    private native boolean TestUnitReady(FileInputStream fileInputStream, FileOutputStream fileOutputStream, byte b);

    private native boolean UpdateFw(FileInputStream fileInputStream, FileOutputStream fileOutputStream, byte[] bArr, String str);

    public String getVersion() {
        return "1.22.08.16";
    }

    public NS108XAccDevice(Context context, Handler handler) {
        this.fin = null;
        this.fout = null;
        this.DEBUG = false;
        this.lun = (byte) 1;
        this.handler = handler;
        this.communicator = new Communicator(context, handler);
        this.controller = new Controller();
    }

    public NS108XAccDevice(Context context) {
        this.fin = null;
        this.fout = null;
        this.DEBUG = false;
        this.lun = (byte) 1;
        this.handler = null;
        this.communicator = new Communicator(context, null);
        this.controller = new Controller();
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
        this.communicator.setHandler(handler);
    }

    public void onDestroy() {
        this.communicator.onDestroy();
    }

    public void setLun(byte b) {
        this.lun = b;
    }

    public synchronized int readDisk(long j, int i, byte[] bArr) throws IOException {
        if (!checkStream()) {
            return -1;
        }
        long j2 = j;
        int i2 = i;
        int i3 = 0;
        while (i2 > 0) {
            int i4 = i2 < 30720 ? i2 : 30720;
            SendCBW(this.communicator.getFileInputStream(), this.communicator.getFileOutputStream(), this.lun, j2, i4, Utils.HOST_OUT);
            if (!readOnce(bArr, i3, i4)) {
                return -1;
            }
            if (!ReceiveCSW(this.communicator.getFileInputStream(), this.communicator.getFileOutputStream())) {
                return -1;
            }
            j2 += (long) i4;
            i3 += i4 * 512;
            i2 -= i4;
        }
        return i - i2;
    }

    public synchronized int readDisk(byte b, long j, int i, byte[] bArr) throws IOException {
        if (!checkStream()) {
            return -1;
        }
        long j2 = j;
        int i2 = i;
        int i3 = 0;
        while (i2 > 0) {
            int i4 = i2 < 30720 ? i2 : 30720;
            SendCBW(this.communicator.getFileInputStream(), this.communicator.getFileOutputStream(), b, j2, i4, Utils.HOST_OUT);
            if (!readOnce(bArr, i3, i4)) {
                return -1;
            }
            if (!ReceiveCSW(this.communicator.getFileInputStream(), this.communicator.getFileOutputStream())) {
                return -1;
            }
            j2 += (long) i4;
            i3 += i4 * 512;
            i2 -= i4;
        }
        return i - i2;
    }

    private boolean readOnce(byte[] bArr, int i, int i2) throws IOException {
        int i3 = i2 * 512;
        while (i3 > 0) {
            int i4 = 16384;
            if (i3 < 16384) {
                i4 = i3;
            }
            int i5 = this.fin.read(bArr, i, i4);
            i3 -= i5;
            i += i5;
        }
        return i3 <= 0;
    }

    public synchronized long readCapacity() {
        if (!checkStream()) {
            return -1L;
        }
        return ReadCapacity(this.lun, this.communicator.getFileInputStream(), this.communicator.getFileOutputStream());
    }

    public synchronized long readCapacity(byte b) {
        if (!checkStream()) {
            return -1L;
        }
        return ReadCapacity(b, this.communicator.getFileInputStream(), this.communicator.getFileOutputStream());
    }

    public synchronized int writeDisk(long j, int i, byte[] bArr) throws IOException {
        if (!checkStream()) {
            return -1;
        }
        long j2 = j;
        int i2 = i;
        int i3 = 0;
        while (i2 > 0) {
            int i4 = i2 < 30720 ? i2 : 30720;
            SendCBW(this.communicator.getFileInputStream(), this.communicator.getFileOutputStream(), this.lun, j2, i4, (byte) 0);
            if (!writeOnce(bArr, i3, i4)) {
                return -1;
            }
            if (!ReceiveCSW(this.communicator.getFileInputStream(), this.communicator.getFileOutputStream())) {
                return -1;
            }
            j2 += (long) i4;
            i3 += i4 * 512;
            i2 -= i4;
        }
        return i - i2;
    }

    public synchronized int writeDisk(byte b, long j, int i, byte[] bArr) throws IOException {
        if (!checkStream()) {
            return -1;
        }
        long j2 = j;
        int i2 = i;
        int i3 = 0;
        while (i2 > 0) {
            int i4 = i2 < 30720 ? i2 : 30720;
            SendCBW(this.communicator.getFileInputStream(), this.communicator.getFileOutputStream(), b, j2, i4, (byte) 0);
            if (!writeOnce(bArr, i3, i4)) {
                return -1;
            }
            if (!ReceiveCSW(this.communicator.getFileInputStream(), this.communicator.getFileOutputStream())) {
                return -1;
            }
            j2 += (long) i4;
            i3 += i4 * 512;
            i2 -= i4;
        }
        return i - i2;
    }

    private boolean writeOnce(byte[] bArr, int i, int i2) throws IOException {
        int i3 = i2 * 512;
        while (i3 > 0) {
            int i4 = 16384;
            if (i3 < 16384) {
                i4 = i3;
            }
            this.fout.write(bArr, i, i4);
            i3 -= i4;
            i += i4;
        }
        return i3 <= 0;
    }

    public synchronized boolean updateFw(byte[] bArr, String str) {
        if (!checkStream()) {
            return false;
        }
        if (str == null) {
            str = getSN();
        }
        return UpdateFw(this.communicator.getFileInputStream(), this.communicator.getFileOutputStream(), bArr, str);
    }

    public boolean checkFwData(byte[] bArr) {
        if (checkStream()) {
            return CheckFw(this.communicator.getFileInputStream(), this.communicator.getFileOutputStream(), bArr);
        }
        return false;
    }

    public synchronized int getDeviceFwVersion() {
        if (!checkStream()) {
            return -1;
        }
        return GetDeviceFwVersion(this.communicator.getFileInputStream(), this.communicator.getFileOutputStream());
    }

    private int getMaxLun() {
        if (checkStream()) {
            return GetMaxLun(this.fin, this.fout) - 1;
        }
        return -1;
    }

    public boolean checkConnection() {
        return checkStream();
    }

    private boolean checkStream() {
        this.fin = this.communicator.getFileInputStream();
        this.fout = this.communicator.getFileOutputStream();
        return (this.fin == null || this.fout == null) ? false : true;
    }

    public void closeAccessory() {
        this.communicator.closeAccessory_plus();
    }

    public boolean openAccessory() {
        return this.communicator.openAccessory_plus();
    }

    public void printUi(String str) {
        this.communicator.printLineToUI(str);
    }

    public void printUiInt(int i) {
        this.communicator.printLineToUI(i + "");
    }

    public void printUiBytes(byte[] bArr) {
        this.communicator.printLineToUI(bArr);
    }

    static {
        System.loadLibrary("ns108x-lib");
    }

    public synchronized StorageState testUnitReady(byte b) {
        if (!checkStream()) {
            return StorageState.ERR;
        }
        if (!TestUnitReady(this.communicator.getFileInputStream(), this.communicator.getFileOutputStream(), b)) {
            return StorageState.NOTREADY;
        }
        return StorageState.READY;
    }

    public synchronized String getSN() {
        if (!checkStream()) {
            return null;
        }
        byte[] bArr = new byte[68];
        int iGetSn = GetSn(this.communicator.getFileInputStream(), this.communicator.getFileOutputStream(), bArr);
        if (iGetSn < 0) {
            return null;
        }
        return new String(Arrays.copyOf(bArr, iGetSn));
    }

    public synchronized boolean chipReset() {
        if (!checkStream()) {
            return false;
        }
        return ChipReset(this.communicator.getFileInputStream(), this.communicator.getFileOutputStream());
    }

    public synchronized boolean getCustomerInfo(byte[] bArr, int i) {
        if (!checkStream()) {
            return false;
        }
        if (i > 1024) {
            return false;
        }
        return GetCustomerInfo(this.communicator.getFileInputStream(), this.communicator.getFileOutputStream(), bArr, i);
    }
}
