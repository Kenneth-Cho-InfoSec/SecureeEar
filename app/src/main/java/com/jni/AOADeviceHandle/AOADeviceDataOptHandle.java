package com.jni.AOADeviceHandle;

import android.content.Context;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;
import android.os.ParcelFileDescriptor;
import android.os.SystemClock;
import com.jni.logmanage.LogWD;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class AOADeviceDataOptHandle {
    public byte[] buffer = null;

    public FileDescriptor f74fd;
    public FileInputStream mInputStream;
    public FileOutputStream mOutputStream;
    private ParcelFileDescriptor mPd;

    public AOADeviceDataOptHandle(Context context, UsbAccessory usbAccessory) {
        this.mPd = null;
        this.mOutputStream = null;
        this.mInputStream = null;
        this.f74fd = null;
        try {
            LogWD.writeMsg(this, 2, "initTcpClient() 1111");
            UsbManager usbManager = (UsbManager) context.getSystemService("usb");
            LogWD.writeMsg(this, 2, "manager()" + usbManager);
            if (usbManager == null) {
                return;
            } else {
                this.mPd = usbManager.openAccessory(usbAccessory);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogWD.writeMsg(this, 2, "err msg()" + e.getMessage());
        }
        if (this.mPd == null) {
            return;
        }
        LogWD.writeMsg(this, 2, "initTcpClient()");
        this.f74fd = this.mPd.getFileDescriptor();
        FileDescriptor fileDescriptor = this.f74fd;
        if (fileDescriptor == null) {
            return;
        }
        this.mOutputStream = new FileOutputStream(fileDescriptor);
        this.mInputStream = new FileInputStream(this.f74fd);
    }

    public int writeDevice(byte[] bArr, int i) {
        FileOutputStream fileOutputStream = this.mOutputStream;
        if (fileOutputStream == null) {
            return -1;
        }
        try {
            if (i < 512) {
                fileOutputStream.write(bArr, 0, i);
                this.mOutputStream.flush();
            } else {
                int i2 = i - 128;
                fileOutputStream.write(bArr, 0, i2);
                this.mOutputStream.flush();
                this.mOutputStream.write(bArr, i2, 128);
                this.mOutputStream.flush();
            }
            return 0;
        } catch (Exception e) {
            LogWD.writeMsg(e);
            LogWD.writeMsg(this, 2, "writeDevice error");
            return -1;
        }
    }

    public int testAOADevice() {
        return writeDevice(new byte[]{-1, -18, -35, -52, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0}, 24);
    }

    public void setbuf(byte[] bArr) {
        this.buffer = bArr;
    }

    public int readDevice(int i) {
        FileInputStream fileInputStream = this.mInputStream;
        if (fileInputStream == null) {
            return -1;
        }
        try {
            return fileInputStream.read(this.buffer);
        } catch (Exception e) {
            LogWD.writeMsg(e);
            LogWD.writeMsg(this, 2, "readDevice error");
            return -1;
        }
    }

    public int writeDevice2(byte[] bArr, int i) {
        while (true) {
            int iWriteDevice = writeDevice(bArr, i);
            if (iWriteDevice >= 0) {
                return iWriteDevice;
            }
            LogWD.writeMsg(this, 2, "to continue read");
            SystemClock.sleep(2000L);
            LogWD.writeMsg(this, 2, "to continue read2");
            int device = readDevice(16384);
            if (device < 0) {
                return device;
            }
            LogWD.writeMsg(this, 2, "to continue read ok ret = " + device);
        }
    }

    public void clossyAccessory() {
        LogWD.writeMsg(this, 2, "clossyAccessory()");
        FileInputStream fileInputStream = this.mInputStream;
        if (fileInputStream != null) {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                LogWD.writeMsg(e);
            }
        }
        FileOutputStream fileOutputStream = this.mOutputStream;
        if (fileOutputStream != null) {
            try {
                fileOutputStream.close();
            } catch (IOException e2) {
                LogWD.writeMsg(e2);
            }
        }
        if (this.f74fd != null) {
            this.f74fd = null;
        }
        ParcelFileDescriptor parcelFileDescriptor = this.mPd;
        if (parcelFileDescriptor != null) {
            try {
                parcelFileDescriptor.close();
            } catch (IOException e3) {
                LogWD.writeMsg(e3);
            }
        }
        this.mOutputStream = null;
        this.mInputStream = null;
        this.mPd = null;
        if (this.buffer != null) {
            this.buffer = null;
        }
    }
}
