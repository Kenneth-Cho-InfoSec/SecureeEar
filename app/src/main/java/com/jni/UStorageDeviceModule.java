package com.jni;

import android.content.Context;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbDevice;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.os.SystemClock;
import android.text.TextUtils;
import androidx.core.view.PointerIconCompat;
import com.jni.AOADeviceHandle.AOADevice1301DataOptHandle;
import com.jni.AOADeviceHandle.AOADeviceDataOptHandle;
import com.jni.AOADeviceHandle.AOADeviceFirmInfo;
import com.jni.AOADeviceHandle.AOADevicePlugHandle;
import com.jni.CallBack.UCallBack;
import com.jni.CallBack.UCallBackFuc;
import com.jni.OTGDeviceHandle.OTGDeviceDataOptHandle;
import com.jni.OTGDeviceHandle.OTGDeviceHandle;
import com.jni.lic.LicInfo;
import com.jni.logmanage.LogWD;
import com.jnibean.DevTypeInfo;
import com.norelsys.ns108xalib.NS108XAccDevice;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UStorageDeviceModule {
    public static final int ERROR_NO_SN = -1001;
    public static final int ERROR_USB_DEVICE_OTG_TOOMUTCH = -1000;
    public static final int FS_I4SEASON = 0;
    public static final int FS_I4SEASON_ADD_NTFS = 5;
    public static final int FS_NOSYSTEM = 3;
    public static final int FS_PARAGON = 1;
    public static final int FS_PARAGON_BUT_FAT32_I4SEASON = 4;
    public static final int SDK_CLOSELOG = 0;
    public static final int SDK_OPENLOG = 1;
    public static final int USB_DEVICE_TYPE_AOA = 2;
    public static final int USB_DEVICE_TYPE_AOA2 = 5;
    public static final int USB_DEVICE_TYPE_OTG = 1;
    public static final int USB_DEVICE_TYPE_VIRTUAL = 3;
    public static int fstype_switch = 4;
    private static final int lic = 1;
    public static int sdk_switch;
    private String DATABASE_PATH;
    private int DLNA_EXPLOR_SORTTYPE;
    private int DLNA_MUSIC_SORTTYPE;
    private int DLNA_PHOTO_SORTTYPE;
    private int DLNA_VIDEO_SORTTYPE;
    AOADevicePlugHandle aoaOptHandle;
    public LicCommand gLicCommand;
    public StorageDeviceCommand gStorageCommandHandle;
    private int licbegin;
    private AOADevice1301DataOptHandle mAOADevice1301DataOptHandle;
    private AOADeviceDataOptHandle mAOADeviceDataOptHandle;
    private int mDeviceType;
    private String mDiskPath;
    private Boolean mISAOARun;
    private Boolean mISOTGRun;
    private OTGDeviceHandle mOTGDeviceHandle;
    public NS108XAccDevice nsDevice;
    private int otgtime;
    private int saveappinfo;
    int tmpflag;
    public static String APP_SDCARD = Environment.getExternalStorageDirectory().getPath();
    private static Lock mLock = new ReentrantLock();
    private static Lock otgmLock = new ReentrantLock();

    public static class UStorageDeviceModuleHolder {
        public static UStorageDeviceModule gUstorageDeviceModule = new UStorageDeviceModule();
    }

    public static boolean getLic() {
        return true;
    }

    private UStorageDeviceModule() {
        this.gStorageCommandHandle = null;
        this.gLicCommand = null;
        this.mOTGDeviceHandle = null;
        this.mAOADeviceDataOptHandle = null;
        this.mAOADevice1301DataOptHandle = null;
        this.aoaOptHandle = null;
        this.nsDevice = null;
        this.mISOTGRun = false;
        this.mISAOARun = false;
        this.DATABASE_PATH = APP_SDCARD;
        this.DLNA_PHOTO_SORTTYPE = 5;
        this.DLNA_MUSIC_SORTTYPE = 4;
        this.DLNA_VIDEO_SORTTYPE = 4;
        this.DLNA_EXPLOR_SORTTYPE = 4;
        this.otgtime = 0;
        this.saveappinfo = 0;
        this.mDiskPath = APP_SDCARD + "/i4SeasonVD.vhd";
        this.licbegin = 0;
        this.tmpflag = 0;
        this.gStorageCommandHandle = new StorageDeviceCommand();
        this.gLicCommand = new LicCommand();
    }

    public static UStorageDeviceModule getInstance() {
        return UStorageDeviceModuleHolder.gUstorageDeviceModule;
    }

    public synchronized String getAppName(Context context) {
        try {
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return context.getResources().getString(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.labelRes);
    }

    public synchronized String getPackageName(Context context) {
        try {
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).packageName;
    }

    public void setDefaultParameters(String str, int i, int i2, int i3, int i4) {
        this.DATABASE_PATH = str;
        this.DLNA_PHOTO_SORTTYPE = i;
        this.DLNA_MUSIC_SORTTYPE = i2;
        this.DLNA_VIDEO_SORTTYPE = i3;
        this.DLNA_EXPLOR_SORTTYPE = i4;
        StorageDeviceCommand storageDeviceCommand = this.gStorageCommandHandle;
        if (storageDeviceCommand != null) {
            storageDeviceCommand.vsSetBaseInfo(str, i, i2, i3, i4);
        }
    }

    public void vsSetAppinfo(Context context) {
        if (this.saveappinfo != 0) {
            return;
        }
        this.gStorageCommandHandle.vsSetAppinfo(getAppName(context), getPackageName(context), APP_SDCARD);
        this.saveappinfo = 1;
    }

    public int lictest() {
        Boolean bool = true;
        LicCommand licCommand = this.gLicCommand;
        String str = APP_SDCARD;
        licCommand.start(str, "aaa", str, 1);
        this.gStorageCommandHandle.vsCheckLicSetParam(1, 1, 1);
        this.gLicCommand.localnumMaxset(10);
        int iLoging = this.gLicCommand.loging("20201024plsa", "test", "testbid", "testpid");
        if (iLoging < 0) {
            return iLoging;
        }
        while (bool.booleanValue() && this.gLicCommand.localPrepare() >= 0) {
            byte[] bArrLocalGetlic = this.gLicCommand.localGetlic(null, "testpid");
            if (bArrLocalGetlic != null) {
                this.gLicCommand.checklic(null, bArrLocalGetlic);
            }
        }
        this.gLicCommand.localinfoGet(new LicInfo());
        return 0;
    }

    public int wifistart(Context context, String str, String str2, String str3, int i) {
        vsSetAppinfo(context);
        UCallBackFuc.getInstance().setContext(context);
        this.gStorageCommandHandle.cameraWifiStart(str, str2, str3, APP_SDCARD, i);
        return 0;
    }

    public boolean isMobileEnabled(Context context) {
        try {
            Method declaredMethod = ConnectivityManager.class.getDeclaredMethod("getMobileDataEnabled", new Class[0]);
            declaredMethod.setAccessible(true);
            return ((Boolean) declaredMethod.invoke(context.getSystemService("connectivity"), new Object[0])).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String caGetMissLic(Context context, String str, String str2, String str3, String str4, String str5, int i) {
        if (i != 0) {
            if (isMobileEnabled(context)) {
                return getInstance().gStorageCommandHandle.caGetMissLic(str, str2, str3, str4, str5, i);
            }
            return null;
        }
        return getInstance().gStorageCommandHandle.caGetMissLic(str, str2, str3, str4, str5, i);
    }

    public int wificallbackstart(Context context, String str, String str2, String str3, int i) {
        vsSetAppinfo(context);
        UCallBackFuc.getInstance().setContext(context);
        if (i != 0) {
            this.gStorageCommandHandle.openLog(APP_SDCARD, 1);
        }
        UCallBack.getInstance().loop();
        this.gStorageCommandHandle.caSsidinfoSet(str, str2, str3);
        return 0;
    }

    public int nitwifistart(Context context, String str, int i) {
        return wifistart(context, "192.168.1.1", "192.168.1.1", "10004", i);
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [com.jni.UStorageDeviceModule$1] */
    public void callback() {
        if (this.tmpflag != 0) {
            return;
        }
        this.tmpflag = 1;
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    UStorageDeviceModule.getInstance().gStorageCommandHandle.CallBackStart(new UCallBack());
                }
            }
        }.start();
    }

    public int wifigetfile(Context context, String str, String str2, int i) {
        UCallBackFuc.getInstance().setContext(context);
        this.gStorageCommandHandle.cameraTakePictrue(str, str2, APP_SDCARD, i);
        return 0;
    }

    /* JADX WARN: Type inference failed for: r0v6, types: [com.jni.UStorageDeviceModule$2] */
    /* JADX WARN: Type inference failed for: r13v2, types: [com.jni.UStorageDeviceModule$3] */
    public int initUStorageDeviceModuleWithOTG(Context context, UsbDevice usbDevice, AOADevicePlugHandle.IFileSystemInitSucc iFileSystemInitSucc) {
        this.mDeviceType = 1;
        LogWD.writeMsg(this, 2, "begin");
        otgmLock.lock();
        if (this.otgtime > 0) {
            otgmLock.unlock();
            LogWD.writeMsg(this, 2, "error  is using");
            return -1000;
        }
        LogWD.writeMsg(this, 2, "begin using");
        this.otgtime++;
        Boolean.valueOf(true);
        vsSetAppinfo(context);
        this.mOTGDeviceHandle = new OTGDeviceHandle();
        String strInitOTGDeviceHandle = this.mOTGDeviceHandle.initOTGDeviceHandle(context, usbDevice, 1);
        int oTGlun = this.mOTGDeviceHandle.getOTGlun();
        if (strInitOTGDeviceHandle != null) {
            this.gStorageCommandHandle.vsSetUDiskSerialNumber(strInitOTGDeviceHandle, oTGlun);
            if (oTGlun < 0) {
                this.otgtime--;
                otgmLock.unlock();
                LogWD.writeMsg(this, 2, "error   get lun error");
                return -1;
            }
            LogWD.writeMsg(this, 2, "lun=" + oTGlun);
            startOTGDevieHandle();
            SystemClock.sleep(1000L);
            LogWD.writeMsg(this, 2, "initUStorageDeviceModuleWithOTG=" + strInitOTGDeviceHandle);
            int iInitDeviceCommandDll = this.gStorageCommandHandle.initDeviceCommandDll(1, fstype_switch, this.mDiskPath, APP_SDCARD, sdk_switch, 1);
            if (iInitDeviceCommandDll != 0) {
                LogWD.writeMsg(this, 2, "init error ret=" + iInitDeviceCommandDll);
                otgmLock.unlock();
                destoryUStorageDeviceModule();
                LogWD.writeMsg(this, 2, "init error");
                return iInitDeviceCommandDll;
            }
            if (oTGlun >= 0) {
                this.aoaOptHandle = new AOADevicePlugHandle(iFileSystemInitSucc);
                new Thread() {
                    @Override
                    public void run() {
                        DevTypeInfo devTypeInfo = new DevTypeInfo();
                        LogWD.writeMsg(this, 2, "lu 0 begin");
                        int iOtgInitNextDisk = UStorageDeviceModule.this.gStorageCommandHandle.otgInitNextDisk(0);
                        LogWD.writeMsg(this, 2, "lu 0 end");
                        if (iOtgInitNextDisk != -50001) {
                            if (iOtgInitNextDisk > 0) {
                                iOtgInitNextDisk = 0;
                            }
                            if (UStorageDeviceModule.this.gStorageCommandHandle.vsGetDevTypeInfo(devTypeInfo) == 0) {
                                LogWD.writeMsg(this, 2, "lu 0 send" + iOtgInitNextDisk);
                                UStorageDeviceModule.mLock.lock();
                                UStorageDeviceModule.this.aoaOptHandle.HandleOTGDevice(4, devTypeInfo.publiclun);
                                UStorageDeviceModule.this.aoaOptHandle.HandleOTGDevice(5, iOtgInitNextDisk);
                                UStorageDeviceModule.mLock.unlock();
                                LogWD.writeMsg(this, 2, "lu 0 send end" + iOtgInitNextDisk);
                            }
                        }
                    }
                }.start();
            }
            if (oTGlun >= 0) {
                this.aoaOptHandle = new AOADevicePlugHandle(iFileSystemInitSucc);
                new Thread() {
                    @Override
                    public void run() {
                        DevTypeInfo devTypeInfo = new DevTypeInfo();
                        LogWD.writeMsg(this, 2, "lu 1 begin");
                        int iOtgInitNextDisk = UStorageDeviceModule.this.gStorageCommandHandle.otgInitNextDisk(1);
                        LogWD.writeMsg(this, 2, "lu 1 end");
                        if (iOtgInitNextDisk != -50001) {
                            if (iOtgInitNextDisk > 0) {
                                iOtgInitNextDisk = 0;
                            }
                            if (UStorageDeviceModule.this.gStorageCommandHandle.vsGetDevTypeInfo(devTypeInfo) == 0) {
                                LogWD.writeMsg(this, 2, "lu 1 send" + iOtgInitNextDisk);
                                UStorageDeviceModule.mLock.lock();
                                UStorageDeviceModule.this.aoaOptHandle.HandleOTGDevice(4, devTypeInfo.secretlun);
                                UStorageDeviceModule.this.aoaOptHandle.HandleOTGDevice(5, iOtgInitNextDisk);
                                UStorageDeviceModule.mLock.unlock();
                                LogWD.writeMsg(this, 2, "lu 1 send end" + iOtgInitNextDisk);
                            }
                        }
                    }
                }.start();
            }
            LogWD.writeMsg(this, 2, "lun=" + oTGlun);
            LogWD.writeMsg(this, 2, "init end");
            otgmLock.unlock();
            return iInitDeviceCommandDll;
        }
        this.otgtime--;
        otgmLock.unlock();
        LogWD.writeMsg(this, 2, "mySerial error");
        return PointerIconCompat.TYPE_CONTEXT_MENU;
    }

    /* JADX WARN: Type inference failed for: r12v1, types: [com.jni.UStorageDeviceModule$4] */
    /* JADX WARN: Type inference failed for: r12v2, types: [com.jni.UStorageDeviceModule$5] */
    public void initUStorageDeviceModuleWithAOA(Context context, UsbAccessory usbAccessory, AOADevicePlugHandle.IFileSystemInitSucc iFileSystemInitSucc) {
        String str = new String("Norelsys");
        LogWD.writeMsg(this, 2, "initUStorageDeviceModuleWithAOA begin" + usbAccessory.getManufacturer());
        if (str.equals(usbAccessory.getManufacturer()) || "JD".equals(usbAccessory.getManufacturer()) || "Shenzhen Jiangda Technology".equals(usbAccessory.getManufacturer())) {
            initUStorageDeviceModuleWithAOA2(context, usbAccessory, iFileSystemInitSucc);
            return;
        }
        this.mDeviceType = 2;
        LogWD.writeMsg(this, 2, "initUStorageDeviceModuleWithAOA begin");
        LogWD.writeMsg(this, 2, "initUStorageDeviceModuleWithAOA begin1");
        this.gStorageCommandHandle.initDeviceCommandLock(this.mDeviceType);
        vsSetAppinfo(context);
        this.gStorageCommandHandle.initDeviceCommandDll(2, fstype_switch, this.mDiskPath, APP_SDCARD, sdk_switch, 1);
        SystemClock.sleep(1000L);
        LogWD.writeMsg(this, 2, "initUStorageDeviceModuleWithAOA begin2");
        this.aoaOptHandle = new AOADevicePlugHandle(iFileSystemInitSucc);
        this.mAOADeviceDataOptHandle = new AOADeviceDataOptHandle(context, usbAccessory);
        new Thread() {
            @Override
            public void run() {
                UStorageDeviceModule.this.gStorageCommandHandle.aoarecallCommandInfo(UStorageDeviceModule.this.aoaOptHandle);
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                byte[] bArr = new byte[17408];
                if (UStorageDeviceModule.this.mAOADeviceDataOptHandle != null) {
                    UStorageDeviceModule.this.mAOADeviceDataOptHandle.setbuf(bArr);
                }
                LogWD.writeMsg(this, 2, "aoaDatatransfer begin1");
                UStorageDeviceModule.this.gStorageCommandHandle.aoaDatatransfer(UStorageDeviceModule.this.mAOADeviceDataOptHandle, bArr);
            }
        }.start();
        SystemClock.sleep(100L);
        this.gStorageCommandHandle.aoaPlugin();
        LogWD.writeMsg(this, 2, "initUStorageDeviceModuleWithAOA end");
    }

    /* JADX WARN: Code restructure failed: missing block: B:42:?, code lost:
    
        return r0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private byte[] readFileByBytes(String str) throws Throwable {
        byte[] bArr;
        FileInputStream fileInputStream;
        ByteBuffer.allocate((int) new File(str).length());
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
        }
        try {
            bArr2 = new byte[fileInputStream.available()];
            do {
            } while (fileInputStream.read(bArr2) > 0);
            fileInputStream.close();
            return bArr2;
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
            return bArr;
        } catch (Throwable th2) {
            th = th2;
            fileInputStream2 = fileInputStream;
            if (fileInputStream2 != null) {
                try {
                    fileInputStream2.close();
                } catch (IOException unused2) {
                }
            }
            throw th;
        }
    }

    public int update1301(String str) throws Throwable {
        LogWD.writeMsg(this, 2, "Updatefw begin");
        if (str == null) {
            LogWD.writeMsg(this, 2, "error");
            return -4;
        }
        if (!new File(str).exists()) {
            LogWD.writeMsg(this, 2, "error  " + str);
            return -1;
        }
        LogWD.writeMsg(this, 2, "Updatefw begin2");
        byte[] fileByBytes = readFileByBytes(str);
        this.gStorageCommandHandle.aoaUpdateStart();
        this.gStorageCommandHandle.aoaUpdateData(fileByBytes, fileByBytes.length);
        return this.gStorageCommandHandle.aoaUpdateEnd();
    }

    /* JADX WARN: Type inference failed for: r12v14, types: [com.jni.UStorageDeviceModule$6] */
    /* JADX WARN: Type inference failed for: r12v15, types: [com.jni.UStorageDeviceModule$7] */
    public void initUStorageDeviceModuleWithAOA2(Context context, UsbAccessory usbAccessory, AOADevicePlugHandle.IFileSystemInitSucc iFileSystemInitSucc) {
        this.mDeviceType = 5;
        LogWD.writeMsg(this, 2, "initUStorageDeviceModuleWithAOA2 begin1231");
        NS108XAccDevice nS108XAccDevice = this.nsDevice;
        if (nS108XAccDevice == null) {
            return;
        }
        if (nS108XAccDevice.openAccessory()) {
            LogWD.writeMsg(this, 2, "openok");
        } else {
            LogWD.writeMsg(this, 2, "openbad");
        }
        LogWD.writeMsg(this, 2, "initUStorageDeviceModuleWithAOA begin1");
        String sn = this.nsDevice.getSN();
        if (TextUtils.isEmpty(sn)) {
            return;
        }
        this.gStorageCommandHandle.initDeviceCommandLock(this.mDeviceType);
        vsSetAppinfo(context);
        this.gStorageCommandHandle.initDeviceCommandDll(5, fstype_switch, this.mDiskPath, APP_SDCARD, sdk_switch, 1);
        LogWD.writeMsg(this, 2, "initUStorageDeviceModuleWithAOA begin2");
        this.aoaOptHandle = new AOADevicePlugHandle(iFileSystemInitSucc);
        this.mAOADevice1301DataOptHandle = new AOADevice1301DataOptHandle(this.nsDevice);
        LogWD.writeMsg(this, 2, "getManufacturer= " + usbAccessory.getManufacturer());
        LogWD.writeMsg(this, 2, "getModel= " + usbAccessory.getModel());
        LogWD.writeMsg(this, 2, "getVersion= " + this.nsDevice.getDeviceFwVersion());
        byte[] bArr = new byte[2048];
        this.nsDevice.getCustomerInfo(bArr, 1024);
        AOADeviceFirmInfo aOADeviceFirmInfo = new AOADeviceFirmInfo();
        aOADeviceFirmInfo.SetmanuFacture(usbAccessory.getManufacturer());
        aOADeviceFirmInfo.SetmodelName(usbAccessory.getModel());
        aOADeviceFirmInfo.Setfwint(this.nsDevice.getDeviceFwVersion());
        aOADeviceFirmInfo.Setsn(sn);
        aOADeviceFirmInfo.Setlicense(bArr);
        this.gStorageCommandHandle.aoaSet1301FirmInfo(aOADeviceFirmInfo);
        this.mAOADevice1301DataOptHandle.UpSetFwdir(APP_SDCARD);
        new Thread() {
            @Override
            public void run() {
                UStorageDeviceModule.this.gStorageCommandHandle.aoarecallCommandInfo(UStorageDeviceModule.this.aoaOptHandle);
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                byte[] bArr2 = new byte[262144];
                if (UStorageDeviceModule.this.mAOADevice1301DataOptHandle != null) {
                    UStorageDeviceModule.this.mAOADevice1301DataOptHandle.setbuf(bArr2);
                }
                LogWD.writeMsg(this, 2, "aoaDatatransfer begin1");
                UStorageDeviceModule.this.gStorageCommandHandle.aoa1301Datatransfer(UStorageDeviceModule.this.mAOADevice1301DataOptHandle, bArr2);
            }
        }.start();
        SystemClock.sleep(500L);
        iFileSystemInitSucc.fileSystemInitType(0, 0, 1);
        this.gStorageCommandHandle.aoaPlugin();
        LogWD.writeMsg(this, 2, "initUStorageDeviceModuleWithAOA end");
    }

    public void initUStorageDeviceModuleWithVirtualDisk() {
        this.mDeviceType = 3;
        this.gStorageCommandHandle.initDeviceCommandDll(3, fstype_switch, this.mDiskPath, APP_SDCARD, sdk_switch, 1);
    }

    public void destoryUStorageDeviceModule() {
        LogWD.writeMsg(this, 2, "destoryUStorageDeviceModule begin");
        int i = this.mDeviceType;
        if (i == 1) {
            otgmLock.lock();
            StorageDeviceCommand storageDeviceCommand = this.gStorageCommandHandle;
            if (storageDeviceCommand == null) {
                otgmLock.unlock();
                return;
            }
            int i2 = this.otgtime;
            if (i2 <= 0) {
                otgmLock.unlock();
                return;
            }
            this.otgtime = i2 - 1;
            storageDeviceCommand.exitDeviceCommandDll(1);
            OTGDeviceHandle oTGDeviceHandle = this.mOTGDeviceHandle;
            if (oTGDeviceHandle != null) {
                oTGDeviceHandle.destoryOTGDeviceHandle();
                this.mOTGDeviceHandle = null;
            }
            otgmLock.unlock();
            return;
        }
        if (i == 2) {
            LogWD.writeMsg(this, 2, "initUStorageDeviceModuleWithAOA end1");
            if (this.gStorageCommandHandle != null) {
                LogWD.writeMsg(this, 2, "initUStorageDeviceModuleWithAOA exitDeviceCommandDll");
                this.gStorageCommandHandle.exitDeviceCommandDll(2);
            }
            if (this.mAOADeviceDataOptHandle != null) {
                LogWD.writeMsg(this, 2, "initUStorageDeviceModuleWithAOA clossyAccessory");
                this.mAOADeviceDataOptHandle.clossyAccessory();
                this.mAOADeviceDataOptHandle = null;
            }
            LogWD.writeMsg(this, 2, "initUStorageDeviceModuleWithAOA end");
            return;
        }
        if (i == 5) {
            if (this.gStorageCommandHandle != null) {
                LogWD.writeMsg(this, 2, "initUStorageDeviceModuleWithAOA2 exitDeviceCommandDll");
                this.gStorageCommandHandle.exitDeviceCommandDll(5);
            }
            if (this.mAOADevice1301DataOptHandle != null) {
                this.mAOADevice1301DataOptHandle = null;
                LogWD.writeMsg(this, 2, "initUStorageDeviceModuleWithAOA clossyAccessory");
            }
        }
    }

    public void startOTGDevieHandle() {
        this.mISOTGRun = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                OTGDeviceDataOptHandle oTGDeviceDataOptHandle = UStorageDeviceModule.this.mOTGDeviceHandle.getmOTGDeviceDataOptHandle();
                LogWD.writeMsg(this, 1, "OTGCmdHandle start");
                while (UStorageDeviceModule.this.mISOTGRun.booleanValue() && UStorageDeviceModule.this.gStorageCommandHandle.recallCommandInfo(oTGDeviceDataOptHandle) == 0) {
                }
                LogWD.writeMsg(this, 1, "OTGCmdHandle stop");
            }
        }).start();
    }
}
