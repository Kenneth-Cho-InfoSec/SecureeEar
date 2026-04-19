package com.i4season.bkCamera.logicrelated.fileacceptandoperation.datasource.explorer;

import com.i4season.bkCamera.logicrelated.conversionutil.DataContants;
import com.i4season.bkCamera.logicrelated.conversionutil.LocalConversionUtil;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.FileNodeArrayManager;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.bean.FileNode;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.datasource.ComparatorFile;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.datasource.FileListDataSourceHandler;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.datasource.IAcceptFileListDataDelegate;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.datasource.explorer.localfilemanage.GetFinalStorageForLocal;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.datasource.explorer.localfilemanage.MountFileBean;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.webdavcmd.FileListWebDavCommandHandle;
import com.i4season.bkCamera.uirelated.other.FunctionSwitch;
import com.i4season.bkCamera.uirelated.other.logmanage.LogWD;
import java.io.File;
import java.util.Collections;
import java.util.LinkedList;

public class LocalFileListDataSourceHandler extends FileListDataSourceHandler {
    @Override
    protected void acceptNextPageFileList() {
    }

    @Override
    public void acceptSearchFileList(String str, String str2, int i, int i2) {
    }

    @Override
    protected void reflashFileList() {
    }

    public LocalFileListDataSourceHandler(FileNodeArrayManager fileNodeArrayManager, FileListWebDavCommandHandle fileListWebDavCommandHandle, IAcceptFileListDataDelegate iAcceptFileListDataDelegate) {
        super(fileNodeArrayManager, fileListWebDavCommandHandle, iAcceptFileListDataDelegate);
    }

    @Override
    public void acceptRootFileList(boolean z) {
        getRootFileListHandler(z);
    }

    @Override
    public void acceptFileListForFolderPath(String str, int i, boolean z) {
        getFileListForFolderPathHandler(str, i, z);
    }

    private void getRootFileListHandler(boolean z) {
        LogWD.writeMsg(this, 2, "acceptRootFileList");
        GetFinalStorageForLocal.getInstance().acceptMountDir();
        for (MountFileBean mountFileBean : GetFinalStorageForLocal.getInstance().getStorageDirList()) {
            FileNode fileNode = new FileNode();
            LocalConversionUtil.rootDir2FileNode(mountFileBean, fileNode);
            this.mFileList.add(fileNode);
        }
        LogWD.writeMsg(this, 2, "acceptRootFileList end");
        if (this.mFileList.size() == 1 && FunctionSwitch.ROOTDIR_ONLYONE_AND_ACCEPT_NEXT) {
            LogWD.writeMsg(this, 2, "根目录只有一个 直接获取子目录");
            String str = this.mFileList.get(0).getmFileDevPath();
            this.mFileListWebDavCommandHandle.setmCurFolderPath(str);
            queryAcceptFileListForFolderPath(str, DataContants.CURRENT_SORT_TYPE, z);
            return;
        }
        this.isRootDir = true;
        this.mFileNodeArrayManager.addFileNodeList(this.mFileList, true);
        this.iAcceptFileListDataDelegate.acceptFileListDataSuccful(this.mFileList);
    }

    private void getFileListForFolderPathHandler(String str, int i, boolean z) {
        LogWD.writeMsg(this, 2, "acceptFileListForFolderPath() path = " + str + "---sortMode: " + i + "---isOnlyGetDir: " + z);
        LinkedList linkedList = new LinkedList();
        File[] fileArrListFiles = new File(str).listFiles();
        if (fileArrListFiles != null && fileArrListFiles.length > 0) {
            for (File file : fileArrListFiles) {
                if (!file.getName().startsWith(".") && (!z || !file.isFile())) {
                    FileNode fileNode = new FileNode();
                    LocalConversionUtil.file2FileNode(file, fileNode);
                    if (file.isFile()) {
                        linkedList.addLast(fileNode);
                        this.mFileNodeArrayManager.addDlnaList(fileNode);
                    } else {
                        linkedList.addFirst(fileNode);
                    }
                }
            }
            this.mFileList.addAll(linkedList);
            Collections.sort(this.mFileList, new ComparatorFile());
        }
        this.isRootDir = false;
        LogWD.writeMsg(this, 2, "acceptFileListForFolderPath end");
        this.mFileNodeArrayManager.addFileNodeList(this.mFileList, true);
        this.iAcceptFileListDataDelegate.acceptFileListDataSuccful(this.mFileList);
    }
}
