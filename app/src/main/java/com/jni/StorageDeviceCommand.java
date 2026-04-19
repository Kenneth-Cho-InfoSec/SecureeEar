package com.jni;

import com.jni.AOADeviceHandle.AOADevice1301DataOptHandle;
import com.jni.AOADeviceHandle.AOADeviceCameraConfig;
import com.jni.AOADeviceHandle.AOADeviceCameraData;
import com.jni.AOADeviceHandle.AOADeviceCameraFilParam;
import com.jni.AOADeviceHandle.AOADeviceCameraInterval;
import com.jni.AOADeviceHandle.AOADeviceCameraResolution;
import com.jni.AOADeviceHandle.AOADeviceCameraRunTimes;
import com.jni.AOADeviceHandle.AOADeviceCameraType;
import com.jni.AOADeviceHandle.AOADeviceDataOptHandle;
import com.jni.AOADeviceHandle.AOADeviceFirmInfo;
import com.jni.AOADeviceHandle.AOADevicePartInfo;
import com.jni.AOADeviceHandle.AOADevicePlugHandle;
import com.jni.AOADeviceHandle.AOADeviceTestError;
import com.jni.AOADeviceHandle.AOADeviceWiFiInfo;
import com.jni.CallBack.UCallBack;
import com.jni.CallBack.UCallBackFuc;
import com.jni.OTGDeviceHandle.OTGDeviceDataOptHandle;
import com.jni.UstorageDevice.UstorageSearchInfo;
import com.jni.finger.EnrollNewUser;
import com.jni.finger.fingerFp;
import com.jni.tool.ToolCreatePdf;
import com.jni.tool.ToolCreatePdf2;
import com.jni.tool.ToolCreatePdf3;
import com.jni.tool.vedioChangeInfo;
import com.jnibean.DevTypeInfo;
import com.jnibean.FTATimeInfoBean;
import com.jnibean.UintInfoBean;
import com.jnibean.VSDiskInfoBean;
import com.jnibean.VSFileInfoBean;
import com.jnibean.VSFileInfoObject;
import com.jnibean.fpOldpasswd;
import com.jnibean.fpstatus;
import java.util.ArrayList;

public class StorageDeviceCommand {
    public static final int I4SEASONSupport = 2;
    public static final int NetFrameSupport = 1;

    public native int CallBackFucStart(UCallBackFuc uCallBackFuc);

    public native int CallBackStart(UCallBack uCallBack);

    public native int aoa1301Datatransfer(AOADevice1301DataOptHandle aOADevice1301DataOptHandle, byte[] bArr);

    public native int aoaDatatransfer(AOADeviceDataOptHandle aOADeviceDataOptHandle, byte[] bArr);

    public native int aoaGetFirmInfo(AOADeviceFirmInfo aOADeviceFirmInfo);

    public native int aoaPlugin();

    public native int aoaReciveData(byte[] bArr, int i);

    public native int aoaSet1301FirmInfo(AOADeviceFirmInfo aOADeviceFirmInfo);

    public native int aoaSetLic(String str, byte[] bArr, int i);

    public native int aoaTest(int i, AOADeviceTestError aOADeviceTestError, int i2, long j);

    public native int aoaUpdateData(byte[] bArr, int i);

    public native int aoaUpdateEnd();

    public native int aoaUpdateStart();

    public native int aoarecallCommandInfo(AOADevicePlugHandle aOADevicePlugHandle);

    public native int caAppBackup(int i);

    public native int caChangeWifi();

    public native int caCloseSupportDtype(int i);

    public native String caGetMissLic(String str, String str2, String str3, String str4, String str5, int i);

    public native int caPicFlush(int i);

    public native int caReportParamSet(String str, String str2, String str3, String str4, String str5, String str6);

    public native int caReportSsidPreSet(String str);

    public native int caResetPermit(String str);

    public native int caSsidinfoSet(String str, String str2, String str3);

    public native int caZeroIp();

    public native int cameraChangePictrue(String str, int i, String str2, int i2);

    public native int cameraChangeyuv(AOADeviceCameraData aOADeviceCameraData, byte[] bArr, int i);

    public native int cameraConfigGet(AOADeviceCameraConfig aOADeviceCameraConfig, int i);

    public native void cameraConfigJudgingMoved(int i, int i2, int i3, int i4);

    public native int cameraConfigSet(AOADeviceCameraConfig aOADeviceCameraConfig, int i);

    public native int cameraGet(AOADeviceCameraData aOADeviceCameraData);

    public native int cameraGetPLF(int i);

    public native int cameraGetmotor(int i);

    public native int cameraGetruntimes(AOADeviceCameraRunTimes aOADeviceCameraRunTimes, int i);

    public native int cameraIFapiWifiGet(AOADeviceCameraData aOADeviceCameraData, Object obj);

    public native int cameraIntervalGet(ArrayList<AOADeviceCameraInterval> arrayList, int i, int i2, int i3, int i4);

    public native int cameraResolutionGet(AOADeviceCameraResolution aOADeviceCameraResolution, ArrayList<AOADeviceCameraResolution> arrayList, int i, int i2);

    public native int cameraSetPLF(int i, int i2);

    public native int cameraSetmotor(int i, int i2);

    public native int cameraTakePictrue(String str, String str2, String str3, int i);

    public native int cameraTypeGet(ArrayList<AOADeviceCameraType> arrayList, int i);

    public native int cameraWifiConfGet(AOADeviceCameraConfig aOADeviceCameraConfig);

    public native int cameraWifiConfSet(AOADeviceCameraConfig aOADeviceCameraConfig);

    public native int cameraWifiGet(AOADeviceCameraData aOADeviceCameraData);

    public native int cameraWifiGetFirmInfo(AOADeviceFirmInfo aOADeviceFirmInfo);

    public native int cameraWifiGetWiFiInfo(AOADeviceWiFiInfo aOADeviceWiFiInfo);

    public native int cameraWifiHasAngle();

    public native int cameraWifiHasNewmotor();

    public native int cameraWifiLightGet();

    public native int cameraWifiLightSet(int i);

    public native int cameraWifiResolutionGet(ArrayList<AOADeviceCameraResolution> arrayList);

    public native int cameraWifiSetLic(String str, byte[] bArr, int i);

    public native int cameraWifiSetSleep(int i);

    public native int cameraWifiSetUpdate(String str);

    public native int cameraWifiSetWiFiInfo(AOADeviceWiFiInfo aOADeviceWiFiInfo);

    public native int cameraWifiStart(String str, String str2, String str3, String str4, int i);

    public native void changeToPower();

    public native void changeToPowerSleepTime();

    public native int checkLic10(byte[] bArr, int i, String str);

    public native int checkLic14(String str, byte[] bArr, int i, String str2);

    public native int createHiddenPartition(int i);

    public native void exitDeviceCommandDll(int i);

    public native int fpOldpasswdGet(fpOldpasswd fpoldpasswd);

    public native int fpStatusGet(fpstatus fpstatusVar);

    public native int fpSwitchSet(int i);

    public native int getAviTime(String str);

    public native int initDeviceCommandDll(int i, int i2, String str, String str2, int i3, int i4);

    public native void initDeviceCommandLock(int i);

    public native int localinfoGet(String str, byte[] bArr, int i, String str2);

    public native int openLog(String str, int i);

    public native int opencvParmGet(AOADeviceCameraFilParam aOADeviceCameraFilParam);

    public native int opencvParmSet(AOADeviceCameraFilParam aOADeviceCameraFilParam);

    public native int otgInitNextDisk(int i);

    public native int pdfCreate(ToolCreatePdf toolCreatePdf);

    public native int pdfCreate2(ToolCreatePdf2 toolCreatePdf2);

    public native int pdfCreate3(ToolCreatePdf3 toolCreatePdf3);

    public native int recallCommandInfo(OTGDeviceDataOptHandle oTGDeviceDataOptHandle);

    public native int scsiLunSize(int i);

    public native int setAviPic(byte[] bArr, int i);

    public native int startAviVideoRecord(String str, int i, int i2, int i3);

    public native int startMp4VideoRecord(String str, int i);

    public native int stopAviVideoRecord();

    public native int stopMp4VideoRecord();

    public native int vsAcceptFileInfo(String str, VSFileInfoBean vSFileInfoBean);

    public native int vsAcceptFileList(String str, ArrayList<VSFileInfoBean> arrayList);

    public native int vsAcceptFileListForCamera(ArrayList<VSFileInfoBean> arrayList);

    public native int vsAcceptPartInfoList(int i, ArrayList<AOADevicePartInfo> arrayList, int i2);

    public native int vsAddNewUser(int i, int i2, String str, byte[] bArr);

    public native int vsChangePassword(String str, String str2);

    public native int vsChangePassword2(String str, String str2, String str3);

    public native int vsCheckLicSetParam(int i, int i2, int i3);

    public native boolean vsCheckUnitCardInfo(UintInfoBean uintInfoBean);

    public native int vsCloseFile(VSFileInfoObject vSFileInfoObject);

    public native int vsClosePasswd();

    public native int vsCloseSearch();

    public native int vsCreateFile(String str, FTATimeInfoBean fTATimeInfoBean);

    public native int vsCreatePassword(String str, String str2);

    public native int vsDeleteAllUser();

    public native int vsDeleteDir(String str);

    public native int vsDeleteFile(String str);

    public native int vsDeleteOneUser(int i);

    public native int vsDirIsSamePart(String str, String str2);

    public native int vsDiskInfo(int i, VSDiskInfoBean vSDiskInfoBean);

    public native int vsEditOneUser(int i, int i2, String str);

    public native int vsEnrollNewUser(int i, int i2, EnrollNewUser enrollNewUser);

    public native int vsEnterFPMode();

    public native int vsEraseOneUserFP(int i);

    public native int vsExitFPMoode();

    public native int vsFormatDisk(int i, int i2, String str);

    public native int vsGetChannelNumber(byte[] bArr, int i);

    public native int vsGetCustomData(byte[] bArr, int i);

    public native int vsGetDevTypeInfo(DevTypeInfo devTypeInfo);

    public native int vsGetDiskFormat(int i);

    public native int vsGetFailNumber();

    public native String vsGetId(String str);

    public native int vsGetPartInfo(String str, AOADevicePartInfo aOADevicePartInfo);

    public native String vsGetString();

    public native int vsGetUDiskSerialNumber(String str);

    public native int vsGetVedioChangeInfo(vedioChangeInfo vediochangeinfo);

    public native int vsGetWifiVedioChangeInfo(vedioChangeInfo vediochangeinfo);

    public native int vsGetWkMode(int i);

    public native int vsHasPassword();

    public native int vsInitFileSystem(int i);

    public native int vsInitSleep(int i, int i2);

    public native int vsIsFingerOnSensor();

    public native int vsIsVerifyPassword();

    public native int vsListUsers(int i, fingerFp fingerfp);

    public native int vsMachUserFP();

    public native int vsMkdir(String str, FTATimeInfoBean fTATimeInfoBean);

    public native int vsModifyFailNumber(int i);

    public native int vsMoveFile(String str, String str2);

    public native int vsOpenFile(String str, int i, VSFileInfoObject vSFileInfoObject);

    public native int vsOpenSearch();

    public native int vsReadFile(VSFileInfoObject vSFileInfoObject, byte[] bArr, int i);

    public native int vsRenameFile(String str, String str2);

    public native int vsSearch(String str, int i, String str2, UstorageSearchInfo ustorageSearchInfo);

    public native long vsSeekFile(VSFileInfoObject vSFileInfoObject, long j);

    public native int vsSetAppinfo(String str, String str2, String str3);

    public native int vsSetBaseInfo(String str, int i, int i2, int i3, int i4);

    public native int vsSetChannelNumber(byte[] bArr, int i);

    public native int vsSetCustomData(byte[] bArr, int i);

    public native int vsSetFileAttrib(String str, short s);

    public native int vsSetId(String str, String str2);

    public native int vsSetString(String str);

    public native int vsSetUDiskSerialNumber(String str, int i);

    public native int vsSetVedioChangeInfo(int i, int i2, int i3);

    public native int vsSetWifiVedioChangeInfo(int i, int i2, int i3);

    public native int vsSetWkMode(int i, int i2);

    public native int vsSortFileList(String str, int i, int i2, int i3, ArrayList<VSFileInfoBean> arrayList);

    public native void vsStopFileSystem(int i);

    public native int vsTypeFileList(int i, int i2, int i3, int i4, ArrayList<VSFileInfoBean> arrayList);

    public native int vsVerifyPassword(String str);

    public native int vsWriteFile(VSFileInfoObject vSFileInfoObject, byte[] bArr, int i);

    public native int vsisNeedPartition(int i);

    public native int wifiCameraDelFile(String str);

    public native int wifiCameraGetVedioThumbs(String str, byte[] bArr, int i);

    public native int wifiCameraGetfile(String str, String str2, int i, Object obj);

    public native int wifiCameraRtTalk(int i);

    public native int wifiCameraStatus();

    public native int wifiCameraStreamStart(int i, int i2);

    public native int wifiCameraStreamStop();

    public native int wifiCameraSwitch();

    public native int wifiCameraTfstatus();

    public native int wifiCameraVideoPlayback(String str, int i);

    public native int wifiGetWkMode();

    public native int wifiSetNewMotor(int i);

    public native int wifiSetWkMode(int i);

    static {
        System.loadLibrary("UStorageDeviceFS");
    }
}
