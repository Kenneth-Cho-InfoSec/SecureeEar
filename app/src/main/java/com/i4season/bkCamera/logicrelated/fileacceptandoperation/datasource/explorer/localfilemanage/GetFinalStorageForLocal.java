package com.i4season.bkCamera.logicrelated.fileacceptandoperation.datasource.explorer.localfilemanage;

import android.content.Context;
import android.os.storage.StorageManager;
import com.i4season.bkCamera.WDApplication;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.bean.FileNode;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.UtilTools;
import com.i4season.bkCamera.uirelated.other.logmanage.LogWD;
import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GetFinalStorageForLocal {
    private static GetFinalStorageForLocal instance;
    private static Lock mLock = new ReentrantLock();
    private ArrayList<MountFileBean> storageDirList = new ArrayList<>();

    private GetFinalStorageForLocal() {
    }

    public static GetFinalStorageForLocal getInstance() {
        if (instance == null) {
            mLock.lock();
            if (instance == null) {
                instance = new GetFinalStorageForLocal();
            }
            mLock.unlock();
        }
        return instance;
    }

    public void acceptMountDir() {
        LogWD.writeMsg(this, 2, "acceptMountDir()");
        ArrayList<MountFileBean> arrayList = this.storageDirList;
        if (arrayList != null) {
            arrayList.clear();
        }
        ArrayList<MountDirNew> allStoragePath = getAllStoragePath(WDApplication.getInstance().getApplicationContext());
        for (int i = 0; i < allStoragePath.size(); i++) {
            String path = allStoragePath.get(i).getPath();
            if (new File(path).listFiles() != null) {
                LogWD.writeMsg(this, 2, "获取到的SD卡不为空的路径有: _" + i + "_ " + path + " 是否是外置SD卡: " + allStoragePath.get(i).isRemovale());
                setMFBValueByFile(new File(path), allStoragePath.get(i).isRemovale());
            }
        }
    }

    private void setMFBValueByFile(File file, boolean z) {
        LogWD.writeMsg(this, 2, "setMFBValueByFile() isExtand = " + z);
        MountFileBean mountFileBean = new MountFileBean();
        mountFileBean.setMFBPath(file.getPath());
        if (!z) {
            if (WDApplication.getInstance() != null) {
                mountFileBean.setMFBName("内置");
                mountFileBean.setMStorageType(100);
            }
        } else {
            mountFileBean.setMFBName("SD 卡");
            mountFileBean.setMStorageType(101);
        }
        mountFileBean.setMFBSize(file.length() + "");
        mountFileBean.setMFBTime(UtilTools.getFileLastModifiedTime(file));
        if (file.canWrite()) {
            mountFileBean.setMFBTime("rw");
        } else {
            mountFileBean.setMFBTime("r");
        }
        mountFileBean.setMFBType(FileNode.FOLDER_TYPE);
        this.storageDirList.add(mountFileBean);
    }

    private ArrayList<MountDirNew> getAllStoragePath(Context context) {
        LogWD.writeMsg(this, 2, "getAllStoragePath()");
        ArrayList<MountDirNew> arrayList = new ArrayList<>();
        StorageManager storageManager = (StorageManager) context.getSystemService("storage");
        try {
            Class<?> cls = Class.forName("android.os.storage.StorageVolume");
            Method method = storageManager.getClass().getMethod("getVolumeList", new Class[0]);
            Method method2 = cls.getMethod("getPath", new Class[0]);
            Method method3 = cls.getMethod("isRemovable", new Class[0]);
            Object objInvoke = method.invoke(storageManager, new Object[0]);
            Method method4 = null;
            try {
                method4 = cls.getMethod("getState", new Class[0]);
            } catch (Exception e) {
                LogWD.writeMsg(e);
            }
            Method method5 = method4;
            int length = Array.getLength(objInvoke);
            for (int i = 0; i < length; i++) {
                Object obj = Array.get(objInvoke, i);
                if (method5 != null) {
                    String str = (String) method5.invoke(obj, new Object[0]);
                    LogWD.writeMsg(this, 2, "getAllStoragePath() 是否是挂载状态: state = " + str);
                    if (str.equals("mounted")) {
                        addMountBean(arrayList, method2, method3, i, obj);
                    }
                } else {
                    addMountBean(arrayList, method2, method3, i, obj);
                }
            }
        } catch (Exception e2) {
            LogWD.writeMsg(e2);
        }
        return arrayList;
    }

    private void addMountBean(ArrayList<MountDirNew> arrayList, Method method, Method method2, int i, Object obj) throws IllegalAccessException, InvocationTargetException {
        LogWD.writeMsg(this, 2, "addMountBean()");
        MountDirNew mountDirNew = new MountDirNew();
        String str = (String) method.invoke(obj, new Object[0]);
        boolean zBooleanValue = ((Boolean) method2.invoke(obj, new Object[0])).booleanValue();
        LogWD.writeMsg(this, 2, "addMountBean() 获取到的SD卡的路径有: " + i + " " + str + " 是否是外置SD卡: " + zBooleanValue);
        mountDirNew.setPath(str);
        mountDirNew.setIsRemovale(zBooleanValue);
        arrayList.add(mountDirNew);
    }

    public MountFileBean getExtandSdcardtorage(int i) {
        for (MountFileBean mountFileBean : this.storageDirList) {
            if (mountFileBean.getMStorageType() == i) {
                return mountFileBean;
            }
        }
        return null;
    }

    public ArrayList<MountFileBean> getStorageDirList() {
        return this.storageDirList;
    }
}
