package com.i4season.bkCamera.logicrelated.fileacceptandoperation;

import com.i4season.bkCamera.logicrelated.fileacceptandoperation.bean.FileNode;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.datasource.FileListDataSourceHandler;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.datasource.IAcceptFileListDataDelegate;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.datasource.dlna.LocalDlnaFileListDataSourceHandler;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.datasource.explorer.DeviceFileListDataSourceHandler;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.datasource.explorer.LocalFileListDataSourceHandler;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.datasource.recent.RecentFileListDataSourceHandler;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.webdavcmd.DeviceFileListWebDavCommandHandle;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.webdavcmd.FileListWebDavCommandHandle;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.webdavcmd.IFileListWebDavCommandDelegate;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.webdavcmd.LocalFileListWebDavCommandHandle;
import com.i4season.bkCamera.uirelated.other.logmanage.LogWD;
import java.util.List;

public class FileDataAcceptAndOperation implements IAcceptFileListDataDelegate, IFileListWebDavCommandDelegate {
    protected IExplorerManagerDelegate iExplorManagerDelegate;
    private String mCurrentPath = "";
    private int mDataSourceType;
    private int mDeviceType;
    private FileListDataSourceHandler mFileListDataSourceHandler;
    private FileListWebDavCommandHandle mFileListWebDavCommandHandle;
    private FileNodeArrayManager mFileNodeArrayManager;

    public FileDataAcceptAndOperation(IExplorerManagerDelegate iExplorerManagerDelegate, int i, int i2) {
        this.iExplorManagerDelegate = iExplorerManagerDelegate;
        this.mDeviceType = i;
        this.mDataSourceType = i2;
        initDataSourceAndWebDavCommand();
    }

    private void initDataSourceAndWebDavCommand() {
        this.mFileNodeArrayManager = new FileNodeArrayManager();
        int i = this.mDataSourceType;
        if (i == 5) {
            initExplorHandler();
        } else if (i == 6) {
            initRecentHandler();
        } else {
            initDlnaHandler();
        }
    }

    private void initExplorHandler() {
        int i = this.mDeviceType;
        if (i == 2) {
            LogWD.writeMsg(this, 2, "storage 文件管理 获取、操作初始化");
            this.mFileListWebDavCommandHandle = new DeviceFileListWebDavCommandHandle(this.mCurrentPath, this.mFileNodeArrayManager, this);
            this.mFileListDataSourceHandler = new DeviceFileListDataSourceHandler(this.mFileNodeArrayManager, this.mFileListWebDavCommandHandle, this);
        } else {
            if (i == 3) {
                LogWD.writeMsg(this, 2, "本地 文件管理 获取、操作初始化");
                this.mFileListWebDavCommandHandle = new LocalFileListWebDavCommandHandle(this.mCurrentPath, this.mFileNodeArrayManager, this);
                this.mFileListDataSourceHandler = new LocalFileListDataSourceHandler(this.mFileNodeArrayManager, this.mFileListWebDavCommandHandle, this);
                return;
            }
            LogWD.writeMsg(this, 2, "wifi 文件管理 获取、操作初始化");
        }
    }

    private void initDlnaHandler() {
        int i = this.mDeviceType;
        if (i == 2) {
            LogWD.writeMsg(this, 2, "storage 文件管理 获取、操作初始化");
        } else {
            if (i == 3) {
                LogWD.writeMsg(this, 2, "本地 文件管理 获取、操作初始化");
                this.mFileListWebDavCommandHandle = new LocalFileListWebDavCommandHandle(this.mCurrentPath, this.mFileNodeArrayManager, this);
                this.mFileListDataSourceHandler = new LocalDlnaFileListDataSourceHandler(this.mDataSourceType, this.mFileNodeArrayManager, this.mFileListWebDavCommandHandle, this);
                return;
            }
            LogWD.writeMsg(this, 2, "wifi 文件管理 获取、操作初始化");
        }
    }

    private void initRecentHandler() {
        int i = this.mDeviceType;
        if (i == 2) {
            LogWD.writeMsg(this, 2, "storage 快捷链接 获取、操作初始化");
        } else if (i == 3) {
            LogWD.writeMsg(this, 2, "本地 快捷链接 获取、操作初始化");
            this.mFileListWebDavCommandHandle = new LocalFileListWebDavCommandHandle(this.mCurrentPath, this.mFileNodeArrayManager, this);
        } else {
            LogWD.writeMsg(this, 2, "wifi 快捷链接 获取、操作初始化");
        }
        this.mFileListDataSourceHandler = new RecentFileListDataSourceHandler(this.mFileNodeArrayManager, this.mFileListWebDavCommandHandle, this);
    }

    @Override
    public void acceptFileListDataSuccful(List<FileNode> list) {
        this.iExplorManagerDelegate.acceptFileListDataSuccful(list);
    }

    @Override
    public void acceptFileListDataError(int i) {
        this.iExplorManagerDelegate.acceptFileListDataError(i);
    }

    @Override
    public void operationSuccful(int i) {
        this.iExplorManagerDelegate.operationSuccful(i);
    }

    @Override
    public void operationError(int i, int i2) {
        this.iExplorManagerDelegate.operationError(i, i2);
    }

    public FileNodeArrayManager getmFileNodeArrayManager() {
        return this.mFileNodeArrayManager;
    }

    public void setmFileNodeArrayManager(FileNodeArrayManager fileNodeArrayManager) {
        this.mFileNodeArrayManager = fileNodeArrayManager;
    }

    public String getmCurrentPath() {
        return this.mCurrentPath;
    }

    public void setmCurrentPath(String str) {
        this.mCurrentPath = str;
        FileListWebDavCommandHandle fileListWebDavCommandHandle = this.mFileListWebDavCommandHandle;
        if (fileListWebDavCommandHandle != null) {
            fileListWebDavCommandHandle.setmCurFolderPath(str);
        }
    }

    public FileListDataSourceHandler getmFileListDataSourceHandler() {
        return this.mFileListDataSourceHandler;
    }

    public void setmFileListDataSourceHandler(FileListDataSourceHandler fileListDataSourceHandler) {
        this.mFileListDataSourceHandler = fileListDataSourceHandler;
    }

    public FileListWebDavCommandHandle getmFileListWebDavCommandHandle() {
        return this.mFileListWebDavCommandHandle;
    }

    public void setmFileListWebDavCommandHandle(FileListWebDavCommandHandle fileListWebDavCommandHandle) {
        this.mFileListWebDavCommandHandle = fileListWebDavCommandHandle;
    }

    public int getDataSourceType() {
        return this.mDataSourceType;
    }

    public void setDataSourceType(int i) {
        this.mDataSourceType = i;
    }
}
