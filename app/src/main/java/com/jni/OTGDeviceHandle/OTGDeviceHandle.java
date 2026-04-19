package com.jni.OTGDeviceHandle;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import com.jni.logmanage.LogWD;
import com.norelsys.ns108xalib.Utils;

public class OTGDeviceHandle {
    private UsbEndpoint mEndpointIn;
    private UsbEndpoint mEndpointOut;
    private UsbEndpoint mEpControl;
    private UsbEndpoint mEpIntEndpointIn;
    private UsbEndpoint mEpIntEndpointOut;
    private OTGDeviceDataOptHandle mOTGDeviceDataOptHandle;
    private UsbDeviceConnection mUsbDeviceConnection;
    private UsbInterface mUsbInterface;
    private UsbManager mUsbManager;

    public int getOTGlun() {
        byte[] bArr = new byte[1];
        UsbDeviceConnection usbDeviceConnection = this.mUsbDeviceConnection;
        if (usbDeviceConnection == null) {
            return -1;
        }
        if (usbDeviceConnection.controlTransfer(161, 254, 0, 0, bArr, 1, 1000) <= 0) {
            return 0;
        }
        return bArr[0];
    }

    public String initOTGDeviceHandle(Context context, UsbDevice usbDevice, int i) {
        this.mUsbManager = (UsbManager) context.getSystemService("usb");
        return getInterfaceAndConnection(usbDevice, i);
    }

    public void destoryOTGDeviceHandle() {
        OTGDeviceDataOptHandle oTGDeviceDataOptHandle = this.mOTGDeviceDataOptHandle;
        if (oTGDeviceDataOptHandle != null) {
            oTGDeviceDataOptHandle.setstatus(false);
        }
        UsbDeviceConnection usbDeviceConnection = this.mUsbDeviceConnection;
        if (usbDeviceConnection == null || this.mUsbInterface == null) {
            return;
        }
        usbDeviceConnection.close();
        this.mUsbDeviceConnection = null;
    }

    private void print(byte[] bArr) {
        String str = new String("print= ");
        for (int i = 0; i < 16; i++) {
            str = str + Integer.toHexString(bArr[i] & 255) + "*";
        }
        LogWD.writeMsg(this, 2, "result3 =" + str);
    }

    private int checkid() {
        byte[] bArr = new byte[512];
        byte[] bArr2 = {85, 83, 66, 67, 40, -24, 62, -2, 16, 0, 0, 0, Utils.HOST_OUT, 0, 11, -32, -12, -25, 16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        LogWD.writeMsg(this, 2, "result1= " + this.mUsbDeviceConnection.bulkTransfer(this.mEndpointOut, bArr2, bArr2.length, 1000));
        int iBulkTransfer = this.mUsbDeviceConnection.bulkTransfer(this.mEndpointIn, bArr, bArr.length, 1000);
        LogWD.writeMsg(this, 2, "result2 =" + iBulkTransfer);
        print(bArr);
        if (iBulkTransfer != 16) {
            return -1;
        }
        if (bArr[8] == 53 && bArr[9] == 55 && bArr[10] == 57) {
            LogWD.writeMsg(this, 2, "result2 =" + this.mUsbDeviceConnection.bulkTransfer(this.mEndpointIn, bArr, bArr.length, 1000));
            return 0;
        }
        if (bArr[8] == 53 && bArr[9] == 55 && bArr[10] == 54) {
            LogWD.writeMsg(this, 2, "result2 =" + this.mUsbDeviceConnection.bulkTransfer(this.mEndpointIn, bArr, bArr.length, 1000));
            return 0;
        }
        if (bArr[8] != 53 || bArr[9] != 56 || bArr[10] != 48) {
            return -1;
        }
        LogWD.writeMsg(this, 2, "result2 =" + this.mUsbDeviceConnection.bulkTransfer(this.mEndpointIn, bArr, bArr.length, 1000));
        return 0;
    }

    private int checkid2() {
        byte[] bArr = new byte[512];
        byte[] bArr2 = {85, 83, 66, 67, 0, 0, 0, 0, 32, 0, 0, 0, Utils.HOST_OUT, 0, 16, -57, 31, 5, -113, -57, -113, 48, 53, 56, 70, 0, 0, 0, 0, 0, 0};
        LogWD.writeMsg(this, 2, "result1= " + this.mUsbDeviceConnection.bulkTransfer(this.mEndpointOut, bArr2, bArr2.length, 1000));
        int iBulkTransfer = this.mUsbDeviceConnection.bulkTransfer(this.mEndpointIn, bArr, bArr.length, 1000);
        LogWD.writeMsg(this, 2, "result2 =" + iBulkTransfer);
        print(bArr);
        if (iBulkTransfer != 16) {
            return -1;
        }
        if (bArr[8] == 53 && bArr[9] == 55 && bArr[10] == 57) {
            LogWD.writeMsg(this, 2, "result2 =" + this.mUsbDeviceConnection.bulkTransfer(this.mEndpointIn, bArr, bArr.length, 1000));
            return 0;
        }
        if (bArr[8] == 53 && bArr[9] == 55 && bArr[10] == 54) {
            LogWD.writeMsg(this, 2, "result2 =" + this.mUsbDeviceConnection.bulkTransfer(this.mEndpointIn, bArr, bArr.length, 1000));
            return 0;
        }
        if (bArr[8] != 53 || bArr[9] != 56 || bArr[10] != 48) {
            return -1;
        }
        LogWD.writeMsg(this, 2, "result2 =" + this.mUsbDeviceConnection.bulkTransfer(this.mEndpointIn, bArr, bArr.length, 1000));
        return 0;
    }

    private int getlic() {
        byte[] bArr = new byte[512];
        byte[] bArr2 = new byte[512];
        byte[] bArr3 = {85, 83, 66, 67, 40, -24, 62, -2, Utils.HOST_OUT, 0, 0, 0, Utils.HOST_OUT, 0, 11, -1, 4, Utils.HOST_OUT, 74, 77, 0, Utils.HOST_OUT, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        LogWD.writeMsg(this, 2, "result1= " + this.mUsbDeviceConnection.bulkTransfer(this.mEndpointOut, bArr3, bArr3.length, 1000));
        int iBulkTransfer = this.mUsbDeviceConnection.bulkTransfer(this.mEndpointIn, bArr, bArr.length, 1000);
        LogWD.writeMsg(this, 2, "result2 =" + iBulkTransfer);
        print(bArr);
        if (iBulkTransfer != 128) {
            return -1;
        }
        this.mUsbDeviceConnection.bulkTransfer(this.mEndpointIn, bArr2, bArr2.length, 1000);
        return 0;
    }

    private String getInterfaceAndConnection(UsbDevice usbDevice, int i) {
        if (usbDevice != null) {
            int interfaceCount = usbDevice.getInterfaceCount();
            LogWD.writeMsg(this, 2, "mUsbInterface总数为: " + interfaceCount);
            for (int i2 = 0; i2 < interfaceCount; i2++) {
                LogWD.writeMsg(this, 2, "get");
                this.mUsbInterface = usbDevice.getInterface(i2);
                LogWD.writeMsg(this, 2, "to open");
                UsbDeviceConnection usbDeviceConnectionOpenDevice = this.mUsbManager.openDevice(usbDevice);
                LogWD.writeMsg(this, 2, "to open end");
                if (usbDeviceConnectionOpenDevice != null && usbDeviceConnectionOpenDevice.claimInterface(this.mUsbInterface, true)) {
                    LogWD.writeMsg(this, 2, "OTG独占成功");
                    getPoint();
                    this.mUsbDeviceConnection = usbDeviceConnectionOpenDevice;
                    this.mOTGDeviceDataOptHandle = new OTGDeviceDataOptHandle();
                    this.mOTGDeviceDataOptHandle.setstatus(false);
                    this.mOTGDeviceDataOptHandle.setmUsbDeviceConnection(this.mUsbDeviceConnection);
                    this.mOTGDeviceDataOptHandle.setmEndpointIn(this.mEndpointIn);
                    this.mOTGDeviceDataOptHandle.setmEndpointOut(this.mEndpointOut);
                    this.mOTGDeviceDataOptHandle.prepareOTG(usbDeviceConnectionOpenDevice, this.mEndpointIn, this.mEndpointOut);
                    String serial = this.mUsbDeviceConnection.getSerial();
                    LogWD.writeMsg(this, 2, "设置的mUsbDeviceConnection = " + this.mUsbDeviceConnection + " mEndpointIn = " + this.mEndpointIn + " mEndpointOut = " + this.mEndpointOut + " 设备serial number：" + serial);
                    this.mOTGDeviceDataOptHandle.setstatus(true);
                    if (serial == null) {
                        this.mUsbDeviceConnection.close();
                        this.mUsbDeviceConnection = null;
                    }
                    return serial;
                }
                LogWD.writeMsg(this, 2, "Interface_" + i2 + "_独占失败");
            }
        }
        LogWD.writeMsg(this, 2, "it is null");
        return null;
    }

    private void getPoint() {
        for (int i = 0; i < this.mUsbInterface.getEndpointCount(); i++) {
            UsbEndpoint endpoint = this.mUsbInterface.getEndpoint(i);
            if (endpoint.getType() == 2) {
                if (endpoint.getDirection() == 0) {
                    this.mEndpointOut = endpoint;
                    LogWD.writeMsg(this, 1, "getPoint() Find the mEndpointOut,index:" + i + ",使用端点号：" + this.mEndpointOut.getEndpointNumber());
                } else {
                    this.mEndpointIn = endpoint;
                    LogWD.writeMsg(this, 1, "getPoint() Find the mEndpointIn:index:" + i + ",使用端点号：" + this.mEndpointIn.getEndpointNumber());
                }
            }
            if (endpoint.getType() == 0) {
                this.mEpControl = endpoint;
                LogWD.writeMsg(this, 1, "getPoint() Find the ControlEndPoint:index:" + i + "," + this.mEpControl.getEndpointNumber());
            }
            if (endpoint.getType() == 3) {
                if (endpoint.getDirection() == 0) {
                    this.mEpIntEndpointOut = endpoint;
                    LogWD.writeMsg(this, 1, "getPoint() Find the InterruptEndpointOut:index:" + i + "," + this.mEpIntEndpointOut.getEndpointNumber());
                }
                if (endpoint.getDirection() == 128) {
                    this.mEpIntEndpointIn = endpoint;
                    LogWD.writeMsg(this, 1, "getPoint() Find the InterruptEndpointIn:index:" + i + "," + this.mEpIntEndpointIn.getEndpointNumber());
                }
            }
        }
        if (this.mEndpointOut == null && this.mEndpointIn == null && this.mEpControl == null && this.mEpIntEndpointOut == null && this.mEpIntEndpointIn == null) {
            LogWD.writeMsg(this, 1, "getPoint() not endpoint is founded!");
            throw new IllegalArgumentException("not endpoint is founded!");
        }
    }

    public OTGDeviceDataOptHandle getmOTGDeviceDataOptHandle() {
        return this.mOTGDeviceDataOptHandle;
    }

    public void setmOTGDeviceDataOptHandle(OTGDeviceDataOptHandle oTGDeviceDataOptHandle) {
        this.mOTGDeviceDataOptHandle = oTGDeviceDataOptHandle;
    }
}
