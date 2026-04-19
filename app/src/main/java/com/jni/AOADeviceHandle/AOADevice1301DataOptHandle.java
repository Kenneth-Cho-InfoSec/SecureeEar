package com.jni.AOADeviceHandle;

import com.jni.logmanage.LogWD;
import com.norelsys.ns108xalib.Model.StorageState;
import com.norelsys.ns108xalib.NS108XAccDevice;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class AOADevice1301DataOptHandle {
    private NS108XAccDevice nsDevice;
    public byte[] buffer = null;
    public boolean breakflag = false;
    String imgFile = null;
    int fwdirset = 0;

    public AOADevice1301DataOptHandle(NS108XAccDevice nS108XAccDevice) {
        this.nsDevice = null;
        this.nsDevice = nS108XAccDevice;
    }

    public int writeDevice(byte[] bArr, long j, int i, int i2) {
        if (this.breakflag) {
            return -1;
        }
        try {
            return this.nsDevice.writeDisk(i2 == 0 ? (byte) 0 : (byte) 1, j, i, bArr);
        } catch (IOException unused) {
            LogWD.writeMsg(this, 2, "write error");
            return -1;
        }
    }

    public void setbuf(byte[] bArr) {
        this.buffer = bArr;
    }

    public int readDevice(long j, int i, int i2) {
        int disk = -1;
        if (this.breakflag) {
            return -1;
        }
        byte b = i2 == 0 ? (byte) 0 : (byte) 1;
        if (this.buffer == null) {
            LogWD.writeMsg(this, 2, "no buf");
            return -1;
        }
        try {
            LogWD.writeMsg(this, 2, "toread lba=" + j + ";lun =" + ((int) b) + ";sectorCount" + i);
            disk = this.nsDevice.readDisk(b, j, i, this.buffer);
            StringBuilder sb = new StringBuilder();
            sb.append("ret = ");
            sb.append(disk);
            LogWD.writeMsg(this, 2, sb.toString());
            return disk;
        } catch (IOException unused) {
            LogWD.writeMsg(this, 2, "read error");
            return disk;
        }
    }

    public int testUnitReady(int i) {
        if (this.breakflag) {
            return -1;
        }
        byte b = i == 0 ? (byte) 0 : (byte) 1;
        LogWD.writeMsg(this, 2, "test lun=" + ((int) b));
        StorageState storageStateTestUnitReady = this.nsDevice.testUnitReady(b);
        LogWD.writeMsg(this, 2, "test state=" + storageStateTestUnitReady);
        return storageStateTestUnitReady.getState();
    }

    public long readCapacity(int i) {
        if (this.breakflag) {
            return -1L;
        }
        byte b = i == 0 ? (byte) 0 : (byte) 1;
        LogWD.writeMsg(this, 2, "readCapacity lun=" + ((int) b));
        long capacity = this.nsDevice.readCapacity(b);
        LogWD.writeMsg(this, 2, "readCapacity ret=" + capacity);
        return capacity;
    }

    public int close(int i) {
        this.nsDevice.closeAccessory();
        return 0;
    }

    public int open(int i) {
        LogWD.writeMsg(this, 2, "open begin" + i);
        if (this.breakflag) {
            return -1;
        }
        if (this.nsDevice.getDeviceFwVersion() > 0) {
            LogWD.writeMsg(this, 2, "open ok" + i);
            return 0;
        }
        LogWD.writeMsg(this, 2, "open bad" + i);
        return -1;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(11:0|2|(2:35|3)|(2:40|4)|(4:5|(1:7)(1:42)|23|24)|8|33|9|23|24|(1:(0))) */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public byte[] readFileByBytes2(String str) throws Throwable {
        FileInputStream fileInputStream;
        byte[] bArr;
        ByteBuffer.allocate((int) new File(str).length());
        LogWD.writeMsg(this, 2, "readFileByBytes2 begin file=" + str);
        FileInputStream fileInputStream2 = null;
        byte[] bArr2 = null;
        fileInputStream2 = null;
        try {
            try {
                fileInputStream = new FileInputStream(str);
            } catch (Exception e) {
                e = e;
                bArr = null;
            }
        } catch (Throwable th) {
            th = th;
            fileInputStream = fileInputStream2;
        }
        try {
            bArr2 = new byte[fileInputStream.available()];
            LogWD.writeMsg(this, 2, "readFileByBytes2 begin2");
        } catch (Exception e2) {
            e = e2;
            bArr = bArr2;
            fileInputStream2 = fileInputStream;
            e.printStackTrace();
            if (fileInputStream2 != null) {
                try {
                    fileInputStream2.close();
                } catch (IOException unused) {
                }
            }
            bArr2 = bArr;
        } catch (Throwable th2) {
            th = th2;
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException unused2) {
                }
            }
            throw th;
        }
        while (true) {
            int i = fileInputStream.read(bArr2);
            if (i == -1) {
                break;
            }
            LogWD.writeMsg(this, 2, "readFileByBytes2 read=" + i);
            LogWD.writeMsg(this, 2, "readFileByBytes2 end ");
            return bArr2;
        }
        LogWD.writeMsg(this, 2, "readFileByBytes2 ok");
        fileInputStream.close();
        LogWD.writeMsg(this, 2, "readFileByBytes2 end ");
        return bArr2;
    }

    public int UpSetFwdir(String str) {
        if (this.fwdirset != 0) {
            return 0;
        }
        this.imgFile = str + "/1301fw.img";
        this.fwdirset = 1;
        return 0;
    }

    public int Updatefw(int i) throws Throwable {
        LogWD.writeMsg(this, 2, "1Updatefw begin");
        String str = this.imgFile;
        if (str == null) {
            LogWD.writeMsg(this, 2, "error");
            return -4;
        }
        if (!new File(str).exists()) {
            LogWD.writeMsg(this, 2, "error  " + this.imgFile);
            return -1;
        }
        LogWD.writeMsg(this, 2, "openimg  " + this.imgFile);
        LogWD.writeMsg(this, 2, "1pdatefw begin2");
        byte[] fileByBytes2 = readFileByBytes2(this.imgFile);
        LogWD.writeMsg(this, 2, "2pdatefw begin2");
        if (!this.nsDevice.checkFwData(fileByBytes2)) {
            LogWD.writeMsg(this, 2, "error");
            return -2;
        }
        LogWD.writeMsg(this, 2, "1Updatefw begin3");
        if (!this.nsDevice.updateFw(fileByBytes2, "123456789")) {
            LogWD.writeMsg(this, 2, "error");
            return -3;
        }
        LogWD.writeMsg(this, 2, "1Updatefw end");
        return 0;
    }

    public int Updatefw2(String str) throws Throwable {
        LogWD.writeMsg(this, 2, "Updatefw2 begin");
        if (str == null) {
            LogWD.writeMsg(this, 2, "Updatefw2 error");
            return -4;
        }
        if (!new File(str).exists()) {
            LogWD.writeMsg(this, 2, "Updatefw2 error  " + str);
            return -1;
        }
        byte[] fileByBytes2 = readFileByBytes2(str);
        if (!this.nsDevice.checkFwData(fileByBytes2)) {
            LogWD.writeMsg(this, 2, "error");
            return -2;
        }
        LogWD.writeMsg(this, 2, "1Updatefw begin3");
        if (!this.nsDevice.updateFw(fileByBytes2, "123456789")) {
            LogWD.writeMsg(this, 2, "error");
            return -3;
        }
        LogWD.writeMsg(this, 2, "1Updatefw end");
        return 0;
    }
}
