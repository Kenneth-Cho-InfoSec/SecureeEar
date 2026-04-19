package com.norelsys.nsotglib;

import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.util.Log;

public class NSOtgDevice {
    private static NSOtgDevice instance;
    private UsbDeviceConnection connection = null;
    private UsbEndpoint inEndPoint = null;
    private UsbEndpoint outEndPoint = null;
    final int MAX_PWD_SIZE = 32;
    private String TAG = getClass().getSimpleName();

    private native synchronized boolean CleanPassword(UsbDeviceConnection usbDeviceConnection, UsbEndpoint usbEndpoint, UsbEndpoint usbEndpoint2);

    private native synchronized byte HasPassword(UsbDeviceConnection usbDeviceConnection, UsbEndpoint usbEndpoint, UsbEndpoint usbEndpoint2);

    private native synchronized boolean SetPassword(UsbDeviceConnection usbDeviceConnection, UsbEndpoint usbEndpoint, UsbEndpoint usbEndpoint2, byte[] bArr);

    private native synchronized boolean SupportPassword(UsbDeviceConnection usbDeviceConnection, UsbEndpoint usbEndpoint, UsbEndpoint usbEndpoint2);

    private native synchronized boolean VerifyPassword(UsbDeviceConnection usbDeviceConnection, UsbEndpoint usbEndpoint, UsbEndpoint usbEndpoint2, byte[] bArr);

    private NSOtgDevice() {
    }

    public static NSOtgDevice getInstance() {
        if (instance == null) {
            synchronized (NSOtgDevice.class) {
                if (instance == null) {
                    instance = new NSOtgDevice();
                }
            }
        }
        return instance;
    }

    public void setUsbDevice(UsbDeviceConnection usbDeviceConnection, UsbEndpoint usbEndpoint, UsbEndpoint usbEndpoint2) {
        this.connection = usbDeviceConnection;
        this.inEndPoint = usbEndpoint;
        this.outEndPoint = usbEndpoint2;
    }

    private boolean check() {
        if (this.connection != null && this.inEndPoint != null && this.outEndPoint != null) {
            return true;
        }
        Log.i(this.TAG, "CHECK FAIL");
        return false;
    }

    public boolean setPassword(byte[] bArr) {
        if (check() && bArr.length <= 32) {
            return SetPassword(this.connection, this.inEndPoint, this.outEndPoint, bArr);
        }
        return false;
    }

    public boolean verifyPassword(byte[] bArr) {
        if (check() && bArr.length <= 32 && bArr.length != 0) {
            return VerifyPassword(this.connection, this.inEndPoint, this.outEndPoint, bArr);
        }
        return false;
    }

    public boolean[] hasPassword() {
        boolean[] zArr = new boolean[2];
        if (!check()) {
            return null;
        }
        byte bHasPassword = HasPassword(this.connection, this.inEndPoint, this.outEndPoint);
        Log.i(this.TAG, "res :" + Integer.toHexString(bHasPassword));
        if (bHasPassword == 255) {
            return null;
        }
        if ((bHasPassword & 32) != 0) {
            zArr[0] = true;
        } else {
            zArr[0] = false;
        }
        if ((bHasPassword & 64) != 0) {
            zArr[1] = false;
        } else {
            zArr[1] = true;
        }
        return zArr;
    }

    public boolean cleanPassword() {
        if (!check()) {
            return false;
        }
        if (hasPassword()[0]) {
            return CleanPassword(this.connection, this.inEndPoint, this.outEndPoint);
        }
        return true;
    }

    public boolean supportPassword() {
        if (check()) {
            return SupportPassword(this.connection, this.inEndPoint, this.outEndPoint);
        }
        return false;
    }
}
