package com.i4season.bkCamera.logicrelated.fileacceptandoperation.datasource.dlna;

import android.database.Cursor;
import android.provider.MediaStore;
import com.i4season.bkCamera.WDApplication;
import com.i4season.bkCamera.logicrelated.conversionutil.DataContants;
import com.i4season.bkCamera.logicrelated.conversionutil.LocalConversionUtil;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.FileNodeArrayManager;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.bean.FileNode;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.datasource.ComparatorFile;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.datasource.IAcceptFileListDataDelegate;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.webdavcmd.FileListWebDavCommandHandle;
import com.i4season.bkCamera.uirelated.other.logmanage.LogWD;
import java.io.File;
import java.util.Collections;

public class LocalDlnaFileListDataSourceHandler extends DlnaDataSourceHandler {
    @Override
    protected void acceptNextPageFileList() {
    }

    @Override
    protected void acceptSearchFileList(String str, String str2, int i, int i2) {
    }

    public LocalDlnaFileListDataSourceHandler(int i, FileNodeArrayManager fileNodeArrayManager, FileListWebDavCommandHandle fileListWebDavCommandHandle, IAcceptFileListDataDelegate iAcceptFileListDataDelegate) {
        super(i, fileNodeArrayManager, fileListWebDavCommandHandle, iAcceptFileListDataDelegate);
    }

    @Override
    protected void acceptDlnaFileList(int i) {
        getDlnaFileListHandler(this.mDlanType, i);
    }

    @Override
    protected void reflashFileList() {
        this.mFileNodeArrayManager.clearFileNodeList();
        acceptFileListForFolderPath(this.mLastAcceptPath, DataContants.CURRENT_SORT_TYPE, false);
    }

    private void getDlnaFileListHandler(int i, int i2) {
        LogWD.writeMsg(this, 2, "acceptDlnaFileList() dlanType = " + i + "---sortMode: " + i2);
        if (i == 3) {
            getPhotoData();
        } else if (i == 1) {
            getAudioData();
        } else if (i == 2) {
            getVideoData();
        }
        LogWD.writeMsg(this, 2, "acceptDlnaFileList end");
        this.isLoadMoreAble = false;
        this.mFileNodeArrayManager.addFileNodeList(this.mFileList, true);
        this.iAcceptFileListDataDelegate.acceptFileListDataSuccful(this.mFileList);
    }

    private void getVideoData() {
        Cursor cursorQuery = WDApplication.getInstance().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        if (cursorQuery != null) {
            while (cursorQuery.moveToNext()) {
                cursorQuery.getString(cursorQuery.getColumnIndexOrThrow("_id"));
                String string = cursorQuery.getString(cursorQuery.getColumnIndexOrThrow("_data"));
                String string2 = cursorQuery.getString(cursorQuery.getColumnIndex("_display_name"));
                File file = new File(string);
                if (file.exists()) {
                    FileNode fileNode = new FileNode();
                    LocalConversionUtil.dlanFile2FileNode(string, string2, file.lastModified(), file.getName(), 2, file.length() + "", fileNode);
                    this.mFileList.add(fileNode);
                    this.mFileNodeArrayManager.addDlnaList(fileNode);
                }
            }
            cursorQuery.close();
        }
        Collections.sort(this.mFileList, new ComparatorFile());
    }

    private void getAudioData() {
        Cursor cursorQuery = WDApplication.getInstance().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        if (cursorQuery != null) {
            while (cursorQuery.moveToNext()) {
                cursorQuery.getString(cursorQuery.getColumnIndexOrThrow("_id"));
                String string = cursorQuery.getString(cursorQuery.getColumnIndexOrThrow("_data"));
                String string2 = cursorQuery.getString(cursorQuery.getColumnIndex("_display_name"));
                File file = new File(string);
                if (file.exists()) {
                    FileNode fileNode = new FileNode();
                    LocalConversionUtil.dlanFile2FileNode(string, string2, file.lastModified(), file.getName(), 3, file.length() + "", fileNode);
                    this.mFileList.add(fileNode);
                    this.mFileNodeArrayManager.addDlnaList(fileNode);
                }
            }
            cursorQuery.close();
        }
        Collections.sort(this.mFileList, new ComparatorFile());
    }

    private void getPhotoData() {
        Cursor cursorQuery = WDApplication.getInstance().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        if (cursorQuery != null) {
            while (cursorQuery.moveToNext()) {
                cursorQuery.getString(cursorQuery.getColumnIndexOrThrow("_id"));
                String string = cursorQuery.getString(cursorQuery.getColumnIndexOrThrow("_data"));
                String string2 = cursorQuery.getString(cursorQuery.getColumnIndex("_display_name"));
                File file = new File(string);
                if (file.exists()) {
                    FileNode fileNode = new FileNode();
                    LocalConversionUtil.dlanFile2FileNode(string, string2, file.lastModified(), file.getName(), 1, file.length() + "", fileNode);
                    this.mFileList.add(fileNode);
                    this.mFileNodeArrayManager.addDlnaList(fileNode);
                }
            }
            cursorQuery.close();
        }
        Collections.sort(this.mFileList, new ComparatorFile());
    }
}
