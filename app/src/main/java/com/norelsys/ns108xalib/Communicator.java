package com.norelsys.ns108xalib;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Communicator {
    private static final String ACTION_USB_PERMISSION = "com.google.android.DemoKit.action.USB_PERMISSION";
    Context context;
    Handler handler;
    UsbAccessory mAccessory;
    ParcelFileDescriptor mFileDescriptor;
    private PendingIntent mPermissionIntent;
    private boolean mPermissionRequestPending;
    private UsbManager mUsbManager;
    private final String TAG = "Helper";
    FileOutputStream mOutputStream = null;
    FileInputStream mInputStream = null;
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Communicator.ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbAccessory usbAccessory = (UsbAccessory) intent.getParcelableExtra("accessory");
                    if (intent.getBooleanExtra("permission", false)) {
                        Communicator.this.openAccessoryInternal(usbAccessory);
                    } else {
                        Communicator.this.printLineToUI("permission denied for accessory ");
                        Communicator.this.mAccessory = null;
                        Communicator.this.sendConnStatus(3);
                    }
                    Communicator.this.mPermissionRequestPending = false;
                }
                return;
            }
            if ("android.hardware.usb.action.USB_ACCESSORY_DETACHED".equals(action)) {
                Communicator.this.closeAccessory_plus();
                return;
            }
            if ("android.intent.action.ACTION_POWER_DISCONNECTED".equals(action)) {
                Communicator.this.closeAccessory_plus();
                return;
            }
            if (UsbManagerCustomize.ACTION_USB_STATE.equals(action)) {
                boolean booleanExtra = intent.getBooleanExtra(UsbManagerCustomize.USB_CONNECTED, false);
                boolean booleanExtra2 = intent.getBooleanExtra(UsbManagerCustomize.USB_CONFIGURED, false);
                intent.getBooleanExtra(UsbManagerCustomize.USB_FUNCTION_ADB, false);
                boolean booleanExtra3 = intent.getBooleanExtra(UsbManagerCustomize.USB_FUNCTION_RNDIS, false);
                boolean booleanExtra4 = intent.getBooleanExtra(UsbManagerCustomize.USB_FUNCTION_MTP, false);
                boolean booleanExtra5 = intent.getBooleanExtra(UsbManagerCustomize.USB_FUNCTION_PTP, false);
                boolean booleanExtra6 = intent.getBooleanExtra(UsbManagerCustomize.USB_FUNCTION_AUDIO_SOURCE, false);
                boolean booleanExtra7 = intent.getBooleanExtra(UsbManagerCustomize.USB_FUNCTION_MIDI, false);
                if (booleanExtra && booleanExtra2) {
                    if (booleanExtra3 || booleanExtra4 || booleanExtra5 || booleanExtra6 || booleanExtra7) {
                        Communicator.this.closeAccessory_plus(false);
                    }
                }
            }
        }
    };
    private AccessoryState acState = AccessoryState.Closed;

    public void printLineToUI(String str) {
    }

    public Communicator(Context context, Handler handler) {
        this.handler = null;
        this.context = context;
        this.mUsbManager = (UsbManager) context.getSystemService("usb");
        this.mPermissionIntent = PendingIntent.getBroadcast(context, 1, new Intent(ACTION_USB_PERMISSION), 33554432);
        IntentFilter intentFilter = new IntentFilter(ACTION_USB_PERMISSION);
        intentFilter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
        intentFilter.addAction("android.hardware.usb.action.USB_ACCESSORY_DETACHED");
        intentFilter.addAction(UsbManagerCustomize.ACTION_USB_STATE);
        this.handler = handler;
        context.registerReceiver(this.mUsbReceiver, intentFilter);
    }

    protected void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void onDestroy() {
        this.context.unregisterReceiver(this.mUsbReceiver);
        closeAccessory_plus();
    }

    public AccessoryState getState() {
        return this.acState;
    }

    public synchronized boolean openAccessory_plus() {
        if (this.mOutputStream != null && this.mInputStream != null && this.mFileDescriptor != null && this.mAccessory != null) {
            sendConnStatus(1);
            return true;
        }
        return openAccessory();
    }

    public synchronized void closeAccessory_plus() {
        closeAccessory_plus(true);
    }

    public synchronized void closeAccessory_plus(boolean z) {
        if (this.mAccessory != null) {
            closeAccessory();
        } else if (z) {
            sendConnStatus(2);
        }
    }

    public boolean openAccessory() {
        UsbAccessory[] accessoryList = this.mUsbManager.getAccessoryList();
        UsbAccessory usbAccessory = accessoryList == null ? null : accessoryList[0];
        if (usbAccessory != null) {
            if (this.mUsbManager.hasPermission(usbAccessory)) {
                openAccessoryInternal(usbAccessory);
            } else {
                synchronized (this.mUsbReceiver) {
                    if (!this.mPermissionRequestPending) {
                        this.mUsbManager.requestPermission(usbAccessory, this.mPermissionIntent);
                        this.mPermissionRequestPending = true;
                    }
                }
            }
            return true;
        }
        sendConnStatus(2);
        printLineToUI("accessory is closed!");
        this.acState = AccessoryState.Closed;
        return false;
    }

    public void openAccessoryInternal(UsbAccessory usbAccessory) {
        this.mFileDescriptor = this.mUsbManager.openAccessory(usbAccessory);
        ParcelFileDescriptor parcelFileDescriptor = this.mFileDescriptor;
        if (parcelFileDescriptor != null) {
            this.mAccessory = usbAccessory;
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            this.mOutputStream = new FileOutputStream(fileDescriptor);
            this.mInputStream = new FileInputStream(fileDescriptor);
            printLineToUI("accessory opened");
            this.acState = AccessoryState.Opened;
            sendConnStatus(1);
            return;
        }
        printLineToUI("accessory open fail");
        this.acState = AccessoryState.Closed;
        sendConnStatus(2);
    }

    private void closeAccessory() {
        printLineToUI("accessory closed!");
        try {
            if (this.mFileDescriptor != null) {
                this.mFileDescriptor.close();
            }
            if (this.mOutputStream != null) {
                this.mOutputStream.close();
            }
            if (this.mInputStream != null) {
                this.mInputStream.close();
            }
        } catch (IOException unused) {
        } catch (Throwable th) {
            this.mFileDescriptor = null;
            this.mAccessory = null;
            this.mOutputStream = null;
            this.mInputStream = null;
            this.mPermissionRequestPending = false;
            sendConnStatus(2);
            throw th;
        }
        this.mFileDescriptor = null;
        this.mAccessory = null;
        this.mOutputStream = null;
        this.mInputStream = null;
        this.mPermissionRequestPending = false;
        sendConnStatus(2);
        this.acState = AccessoryState.Closed;
        sendClosedBroadcast();
    }

    public void sendClosedBroadcast() {
        Intent intent = new Intent();
        intent.setAction(UsbManagerCustomize.USB_ACCESSORY_CLOSED);
        this.context.sendBroadcast(intent);
    }

    public void printLineToUI(byte[] bArr) {
        StringBuilder sb = new StringBuilder("");
        if (bArr == null || bArr.length <= 0) {
            return;
        }
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & 255);
            if (hexString.length() < 2) {
                sb.append(0);
            }
            sb.append(hexString + " ");
        }
        printLineToUI(sb.toString());
    }

    public FileInputStream getFileInputStream() {
        FileInputStream fileInputStream = this.mInputStream;
        if (fileInputStream == null) {
            return null;
        }
        return fileInputStream;
    }

    public FileOutputStream getFileOutputStream() {
        FileOutputStream fileOutputStream = this.mOutputStream;
        if (fileOutputStream == null) {
            return null;
        }
        return fileOutputStream;
    }

    private String bytesToHexString(byte[] bArr) {
        StringBuilder sb = new StringBuilder("");
        if (bArr == null || bArr.length <= 0) {
            return null;
        }
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & 255);
            if (hexString.length() < 2) {
                sb.append(0);
            }
            sb.append(hexString + " ");
        }
        return sb.toString();
    }

    private boolean checkUsbConn() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
        return this.context.registerReceiver(null, intentFilter).getIntExtra("plugged", -1) == 2;
    }

    protected void sendConnStatus(int i) {
        Handler handler = this.handler;
        if (handler != null) {
            Message messageObtainMessage = handler.obtainMessage();
            messageObtainMessage.what = 99;
            messageObtainMessage.obj = Integer.valueOf(i);
            this.handler.sendMessage(messageObtainMessage);
        }
    }
}
