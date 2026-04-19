package com.i4season.bkCamera.logicrelated.fileacceptandoperation.datasource.recent;

import android.database.Cursor;
import android.provider.MediaStore;
import com.i4season.bkCamera.WDApplication;
import com.i4season.bkCamera.logicrelated.conversionutil.LocalConversionUtil;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.FileNodeArrayManager;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.bean.FileNode;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.datasource.FileListDataSourceHandler;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.datasource.IAcceptFileListDataDelegate;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.webdavcmd.FileListWebDavCommandHandle;
import com.i4season.bkCamera.uirelated.other.AppPathInfo;
import com.i4season.bkCamera.uirelated.other.logmanage.LogWD;
import java.io.File;

public class RecentFileListDataSourceHandler extends FileListDataSourceHandler {
    @Override
    protected void acceptFileListForFolderPath(String str, int i, boolean z) {
    }

    @Override
    protected void acceptNextPageFileList() {
    }

    @Override
    protected void acceptSearchFileList(String str, String str2, int i, int i2) {
    }

    @Override
    protected void reflashFileList() {
    }

    public RecentFileListDataSourceHandler(FileNodeArrayManager fileNodeArrayManager, FileListWebDavCommandHandle fileListWebDavCommandHandle, IAcceptFileListDataDelegate iAcceptFileListDataDelegate) {
        super(fileNodeArrayManager, fileListWebDavCommandHandle, iAcceptFileListDataDelegate);
    }

    @Override
    protected void acceptRootFileList(boolean z) {
        acceptRecentFileList();
    }

    private void acceptRecentFileList() {
        FileNode recentPhoneOnePicData = getRecentPhoneOnePicData();
        if (recentPhoneOnePicData != null) {
            this.mFileList.add(recentPhoneOnePicData);
        }
        FileNode recentAppOnePicData = getRecentAppOnePicData();
        if (recentAppOnePicData != null) {
            this.mFileList.add(recentAppOnePicData);
        }
        LogWD.writeMsg(this, 2, "acceptRecentFileList() end fileNodes.size(): " + this.mFileList.size());
        this.isLoadMoreAble = false;
        this.mFileNodeArrayManager.addFileNodeList(this.mFileList, false);
        this.mFileNodeArrayManager.addDlnaListAll(this.mFileList);
        this.iAcceptFileListDataDelegate.acceptFileListDataSuccful(this.mFileList);
    }

    private FileNode getRecentAppOnePicData() {
        File[] fileArrListFiles = new File(AppPathInfo.getTransferDownloadPath()).listFiles();
        if (fileArrListFiles == null || fileArrListFiles.length <= 0) {
            return null;
        }
        FileNode fileNode = new FileNode();
        fileNode.setSourceType(1);
        LocalConversionUtil.file2FileNode(fileArrListFiles[0], fileNode);
        return fileNode;
    }

    public FileNode getRecentPhoneOnePicData() {
        Cursor cursorQuery = WDApplication.getInstance().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_id", "_data", "title"}, null, null, "date_modified DESC");
        FileNode fileNode = null;
        if (cursorQuery != null) {
            while (true) {
                if (!cursorQuery.moveToNext()) {
                    break;
                }
                String string = cursorQuery.getString(cursorQuery.getColumnIndexOrThrow("_data"));
                File file = new File(string);
                if (file.exists()) {
                    FileNode fileNode2 = new FileNode();
                    fileNode2.setSourceType(0);
                    LocalConversionUtil.dlanFile2FileNode(string, file.getName(), file.lastModified(), file.getName(), 1, file.length() + "", fileNode2);
                    fileNode = fileNode2;
                    break;
                }
            }
            cursorQuery.close();
        }
        return fileNode;
    }
}
