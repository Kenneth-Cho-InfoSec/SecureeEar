package com.i4season.bkCamera.logicrelated.fileacceptandoperation.webdavcmd;

import com.i4season.bkCamera.logicrelated.fileacceptandoperation.FileNodeArrayManager;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.bean.FileNode;
import com.i4season.bkCamera.uirelated.other.logmanage.LogWD;
import java.io.File;
import java.util.List;

public class DeviceFileListWebDavCommandHandle extends FileListWebDavCommandHandle {
    private void createFolderCommand(String str) {
    }

    private void renameFileCommand(FileNode fileNode, String str, String str2) {
    }

    public boolean deleteFileAndFolderCommand(String str) {
        return false;
    }

    @Override
    public void pastFileList() {
    }

    public DeviceFileListWebDavCommandHandle(String str, FileNodeArrayManager fileNodeArrayManager, IFileListWebDavCommandDelegate iFileListWebDavCommandDelegate) {
        super(str, fileNodeArrayManager, iFileListWebDavCommandDelegate);
    }

    @Override
    public void creatFolder(String str) {
        LogWD.writeMsg(this, 2, "creatFolder folderName: " + str);
        String str2 = this.mCurFolderPath + File.separator + str;
        LogWD.writeMsg(this, 2, "creatFolder folderPath: " + str2);
        createFolderCommand(str2);
    }

    @Override
    public void renameFileName(FileNode fileNode, String str) {
        LogWD.writeMsg(this, 2, "renameFileName newName: " + str);
        String str2 = fileNode.getmFileDevPath();
        LogWD.writeMsg(this, 2, "renameFileCommand fileDevPath: " + str2);
        String strSubstring = str2.substring(0, str2.lastIndexOf(47));
        LogWD.writeMsg(this, 2, "renameFileCommand currentFolder: " + strSubstring);
        String str3 = strSubstring + File.separator + str;
        LogWD.writeMsg(this, 2, "renameFileCommand newPath: " + str3);
        renameFileCommand(fileNode, str3, str);
    }

    @Override
    public void deleteFile() {
        List<FileNode> allSelectedFileNode = this.mFileNodeArrayManager.getAllSelectedFileNode();
        LogWD.writeMsg(this, 2, "deleteFile fileNodes.size: " + allSelectedFileNode.size());
        for (FileNode fileNode : allSelectedFileNode) {
            String str = fileNode.getmFileDevPath();
            LogWD.writeMsg(this, 2, "deleteFile fileDevPath: " + str);
            if (!deleteFileAndFolderCommand(str)) {
                this.iFileListWebDavCommandDelegate.operationError(2, -2);
                return;
            }
            this.mFileNodeArrayManager.removeFileNode(fileNode);
        }
        this.iFileListWebDavCommandDelegate.operationSuccful(2);
    }

    @Override
    public void isExistFile(FileNode fileNode) {
        LogWD.writeMsg(this, 2, "isExistFile: " + fileNode.getmFileDevPath());
    }

    @Override
    public void copyFileList() {
        LogWD.writeMsg(this, 2, "copyFileList fileNodes.size: " + this.mFileNodeArrayManager.getAllSelectedFileNode().size());
    }

    @Override
    public void cutFileList() {
        LogWD.writeMsg(this, 2, "copyFileList fileNodes.size: " + this.mFileNodeArrayManager.getAllSelectedFileNode().size());
    }
}
