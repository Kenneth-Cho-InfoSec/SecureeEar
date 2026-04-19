package com.jni.OTGDeviceHandle;

import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.Constant;
import com.jni.logmanage.LogWD;
import com.norelsys.nsotglib.NSOtgDevice;

public class OTGDeviceDataOptHandle {
    private boolean keep;
    private UsbEndpoint mEndpointIn;
    private UsbEndpoint mEndpointOut;
    private UsbDeviceConnection mUsbDeviceConnection;
    NSOtgDevice nsOtgDevice;
    private boolean status;

    static {
        System.loadLibrary("native-lib");
    }

    private void print(byte[] bArr) {
        String str = new String("print= ");
        for (byte b : bArr) {
            str = str + Integer.toHexString(b & 255) + " ";
        }
    }

    public int OutBulkTransfer(byte[] bArr, int i, int i2) {
        int i3;
        int iBulkTransfer;
        try {
            this.keep = true;
            if (!this.status) {
                this.keep = false;
                LogWD.writeMsg(this, 2, "out error -2 : status is false");
                return -2;
            }
            int i4 = 0;
            int i5 = 0;
            while (true) {
                i3 = -1;
                if (i4 >= i) {
                    i3 = i4;
                    break;
                }
                int i6 = i - i4;
                if (i6 < 16384) {
                    iBulkTransfer = this.mUsbDeviceConnection.bulkTransfer(this.mEndpointOut, bArr, i5, i6, Constant.DISMISS_DELAY);
                    i5 += iBulkTransfer;
                    if (iBulkTransfer <= 0) {
                        LogWD.writeMsg(this, 2, "out error2 -1 : sout <= 0");
                        break;
                    }
                    i4 += iBulkTransfer;
                } else {
                    iBulkTransfer = this.mUsbDeviceConnection.bulkTransfer(this.mEndpointOut, bArr, i5, 16384, Constant.DISMISS_DELAY);
                    i5 += iBulkTransfer;
                    if (iBulkTransfer <= 0) {
                        LogWD.writeMsg(this, 2, "out error3 -1 : out <= 0");
                        break;
                    }
                    i4 += iBulkTransfer;
                }
            }
            if (i3 > 0) {
                i3 = 0;
            }
            this.keep = false;
            return i3;
        } catch (Exception e) {
            e.printStackTrace();
            return -2;
        }
    }

    public int InBulkTransfer(byte[] bArr, int i, int i2) {
        int i3;
        int iBulkTransfer;
        try {
            this.keep = true;
            if (!this.status) {
                this.keep = false;
                LogWD.writeMsg(this, 2, "in error -2 : status == false length=" + i);
                return -2;
            }
            int i4 = 0;
            int i5 = 0;
            while (true) {
                i3 = -1;
                if (i4 >= i) {
                    i3 = i4;
                    break;
                }
                int i6 = i - i4;
                if (i6 < 16384) {
                    iBulkTransfer = this.mUsbDeviceConnection.bulkTransfer(this.mEndpointIn, bArr, i5, i6, Constant.DISMISS_DELAY);
                    i5 += iBulkTransfer;
                    if (iBulkTransfer <= 0) {
                        LogWD.writeMsg(this, 2, "in error1 -1 : in <= 0,length=" + i);
                        break;
                    }
                    if (i < 512 && i6 != iBulkTransfer) {
                        LogWD.writeMsg(this, 2, "length=" + i + " inlen= " + iBulkTransfer);
                        return iBulkTransfer;
                    }
                    i4 += iBulkTransfer;
                } else {
                    iBulkTransfer = this.mUsbDeviceConnection.bulkTransfer(this.mEndpointIn, bArr, i5, 16384, Constant.DISMISS_DELAY);
                    i5 += iBulkTransfer;
                    if (iBulkTransfer <= 0) {
                        LogWD.writeMsg(this, 2, "in error2 -1 : in <= 0,length=" + i);
                        break;
                    }
                    i4 += iBulkTransfer;
                }
            }
            this.keep = false;
            return i3;
        } catch (Exception e) {
            e.printStackTrace();
            return -3;
        }
    }

    public int ControlTransfer(int i, int i2, int i3, int i4, byte[] bArr, int i5, int i6) {
        LogWD.writeMsg(this, 2, "requestType");
        return this.mUsbDeviceConnection.controlTransfer(i, i2, i3, i4, bArr, i5, i6);
    }

    public UsbDeviceConnection getmUsbDeviceConnection() {
        return this.mUsbDeviceConnection;
    }

    public void setmUsbDeviceConnection(UsbDeviceConnection usbDeviceConnection) {
        this.mUsbDeviceConnection = usbDeviceConnection;
    }

    public void setmEndpointOut(UsbEndpoint usbEndpoint) {
        this.mEndpointOut = usbEndpoint;
    }

    public void setmEndpointIn(UsbEndpoint usbEndpoint) {
        this.mEndpointIn = usbEndpoint;
    }

    public void setstatus(boolean z) {
        this.status = z;
    }

    public boolean getstatus() {
        return this.status;
    }

    public void prepareOTG(UsbDeviceConnection usbDeviceConnection, UsbEndpoint usbEndpoint, UsbEndpoint usbEndpoint2) {
        this.nsOtgDevice = NSOtgDevice.getInstance();
        this.nsOtgDevice.setUsbDevice(usbDeviceConnection, usbEndpoint, usbEndpoint2);
    }

    public int supportPassword() {
        return this.nsOtgDevice.supportPassword() ? 1 : 0;
    }

    public int hasPassword() {
        LogWD.writeMsg(this, 2, "hasPassword begin");
        boolean[] zArrHasPassword = this.nsOtgDevice.hasPassword();
        if (zArrHasPassword == null) {
            LogWD.writeMsg(this, 2, "hasPassword error");
            return 0;
        }
        LogWD.writeMsg(this, 2, "hasPassword end");
        if (zArrHasPassword[0]) {
            LogWD.writeMsg(this, 2, "hasPassword 1");
            return 1;
        }
        LogWD.writeMsg(this, 2, "hasPassword 0");
        return 0;
    }

    public int cleanPassword() {
        return this.nsOtgDevice.cleanPassword() ? 0 : -1;
    }

    public int verifyPassword(byte[] bArr) {
        if (this.nsOtgDevice.verifyPassword(bArr)) {
            LogWD.writeMsg(this, 2, "verifyPassword ok 0");
            return 0;
        }
        LogWD.writeMsg(this, 2, "verifyPassword error 0");
        return -1;
    }

    public int setPassword(byte[] bArr) {
        LogWD.writeMsg(this, 2, " setpassword");
        boolean password = this.nsOtgDevice.setPassword(bArr);
        LogWD.writeMsg(this, 2, " 2setpassword");
        return password ? 0 : -1;
    }
}
