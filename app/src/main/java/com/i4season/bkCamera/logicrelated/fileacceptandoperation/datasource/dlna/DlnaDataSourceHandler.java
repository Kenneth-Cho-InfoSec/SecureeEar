package com.i4season.bkCamera.logicrelated.fileacceptandoperation.datasource.dlna;

import com.i4season.bkCamera.logicrelated.fileacceptandoperation.FileNodeArrayManager;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.datasource.FileListDataSourceHandler;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.datasource.IAcceptFileListDataDelegate;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.webdavcmd.FileListWebDavCommandHandle;

public abstract class DlnaDataSourceHandler extends FileListDataSourceHandler {
    protected int mDlanType;

    protected abstract void acceptDlnaFileList(int i);

    @Override
    protected void acceptRootFileList(boolean z) {
    }

    public DlnaDataSourceHandler(int i, FileNodeArrayManager fileNodeArrayManager, FileListWebDavCommandHandle fileListWebDavCommandHandle, IAcceptFileListDataDelegate iAcceptFileListDataDelegate) {
        super(fileNodeArrayManager, fileListWebDavCommandHandle, iAcceptFileListDataDelegate);
        this.mDlanType = i;
    }

    @Override
    protected void acceptFileListForFolderPath(String str, int i, boolean z) {
        acceptDlnaFileList(i);
    }
}
