package com.i4season.bkCamera.logicrelated.fileacceptandoperation.webdavcmd;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.documentfile.provider.DocumentFile;
import com.i4season.bkCamera.WDApplication;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.FileNodeArrayManager;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.bean.FileNode;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.webdavcmd.localpermission.ApplySdcardPermission;
import com.i4season.bkCamera.uirelated.other.AppPathInfo;
import com.i4season.bkCamera.uirelated.other.function.AdapterType;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.SystemUtil;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.UtilTools;
import com.i4season.bkCamera.uirelated.other.logmanage.LogWD;
import java.io.File;
import java.util.List;

public class LocalFileListWebDavCommandHandle extends FileListWebDavCommandHandle {
    @Override
    public void pastFileList() {
    }

    public LocalFileListWebDavCommandHandle(String str, FileNodeArrayManager fileNodeArrayManager, IFileListWebDavCommandDelegate iFileListWebDavCommandDelegate) {
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
        String str = fileNode.getmFileDevPath();
        LogWD.writeMsg(this, 2, "isExistFile: " + str);
        if (new File(str).exists()) {
            this.iFileListWebDavCommandDelegate.operationSuccful(3);
        } else {
            this.iFileListWebDavCommandDelegate.operationError(3, -2);
        }
    }

    @Override
    public void copyFileList() {
        LogWD.writeMsg(this, 2, "copyFileList fileNodes.size: " + this.mFileNodeArrayManager.getAllSelectedFileNode().size());
        this.iFileListWebDavCommandDelegate.operationSuccful(4);
    }

    @Override
    public void cutFileList() {
        LogWD.writeMsg(this, 2, "copyFileList fileNodes.size: " + this.mFileNodeArrayManager.getAllSelectedFileNode().size());
    }

    private void createFolderCommand(String str) {
        boolean zMkdir;
        boolean z;
        if (!str.contains(AppPathInfo.app_sdcard) && SystemUtil.isAndroid5()) {
            LogWD.writeMsg(this, 2, "createFolderCommand 外置卡");
            DocumentFile rootDocumentFile = ApplySdcardPermission.getInstance().getRootDocumentFile();
            if (rootDocumentFile != null) {
                DocumentFile documentFile = rootDocumentFile;
                z = false;
                for (String str2 : str.substring(AppPathInfo.EXTENDSD_PATH.length()).split("/")) {
                    DocumentFile documentFileFindFile = documentFile.findFile(str2);
                    if (documentFileFindFile != null) {
                        documentFile = documentFileFindFile;
                    } else if (documentFile.createDirectory(str2) != null) {
                        z = true;
                    }
                }
            } else {
                z = false;
            }
            zMkdir = z;
        } else {
            LogWD.writeMsg(this, 2, "createFolderCommand 内置卡");
            File file = new File(str);
            zMkdir = !file.exists() ? file.mkdir() : false;
        }
        LogWD.writeMsg(this, 2, "createFolderCommand isSuccess：" + zMkdir);
        if (zMkdir) {
            addFileNode2FileArrayManager(str);
            this.iFileListWebDavCommandDelegate.operationSuccful(0);
        } else {
            this.iFileListWebDavCommandDelegate.operationError(0, -2);
        }
    }

    private void addFileNode2FileArrayManager(String str) {
        File file = new File(str);
        FileNode fileNode = new FileNode();
        fileNode.setmFileDevPath(file.getPath());
        fileNode.setmFileName(file.getName());
        fileNode.setmFileCreateTime(file.lastModified());
        fileNode.setmFileIsSelected(false);
        String timeFromLong = UtilTools.getTimeFromLong(file.lastModified());
        fileNode.setmFileTime(timeFromLong);
        fileNode.setTimtTag(UtilTools.getTimeTag(timeFromLong));
        fileNode.setLocal(true);
        fileNode.setFile(file.isFile());
        fileNode.setmFileType(FileNode.FOLDER_TYPE);
        fileNode.setmFileTypeMarked(5);
        this.mFileNodeArrayManager.addFileNode(fileNode, false);
    }

    private void renameFileCommand(FileNode fileNode, String str, String str2) {
        boolean zRenameTo;
        String str3 = fileNode.getmFileDevPath();
        if (!str3.contains(AppPathInfo.app_sdcard) && SystemUtil.isAndroid5()) {
            LogWD.writeMsg(this, 2, "renameFileCommand 外置卡");
            DocumentFile rootDocumentFile = ApplySdcardPermission.getInstance().getRootDocumentFile();
            zRenameTo = false;
            if (rootDocumentFile != null) {
                for (String str4 : str3.substring(AppPathInfo.EXTENDSD_PATH.length()).split("/")) {
                    DocumentFile documentFileFindFile = rootDocumentFile.findFile(str4);
                    if (documentFileFindFile != null) {
                        rootDocumentFile = documentFileFindFile;
                    }
                }
                zRenameTo = rootDocumentFile.renameTo(str2);
            }
        } else {
            LogWD.writeMsg(this, 2, "renameFileCommand 内置卡");
            zRenameTo = new File(str3).renameTo(new File(str));
        }
        LogWD.writeMsg(this, 2, "renameFileCommand isSuccess：" + zRenameTo);
        if (zRenameTo) {
            updateDeleteLocalMediaLib(WDApplication.getInstance().getApplicationContext(), str3);
            updateRenameLocalMediaLib(str);
            fileNode.setmFileDevPath(str);
            fileNode.setmFileName(str2);
            this.iFileListWebDavCommandDelegate.operationSuccful(1);
            return;
        }
        this.iFileListWebDavCommandDelegate.operationError(1, -2);
    }

    private void updateRenameLocalMediaLib(String str) {
        MediaScannerConnection.scanFile(WDApplication.getInstance().getApplicationContext(), new String[]{UtilTools.getUTF8CodeInfoFromURL(str)}, null, null);
    }

    public boolean deleteFileAndFolderCommand(String str) {
        if (!str.contains(AppPathInfo.app_sdcard) && SystemUtil.isAndroid5()) {
            DocumentFile rootDocumentFile = ApplySdcardPermission.getInstance().getRootDocumentFile();
            if (rootDocumentFile == null) {
                return false;
            }
            for (String str2 : str.substring(AppPathInfo.EXTENDSD_PATH.length()).split("/")) {
                DocumentFile documentFileFindFile = rootDocumentFile.findFile(str2);
                if (documentFileFindFile != null) {
                    rootDocumentFile = documentFileFindFile;
                }
            }
            return rootDocumentFile.delete();
        }
        File file = new File(str);
        if (!file.exists()) {
            return false;
        }
        if (file.isDirectory()) {
            deleteFolderAllFilesCommand(str);
        }
        return deleteFileCommand(str);
    }

    private boolean deleteFileCommand(String str) {
        File file = new File(str);
        if (!file.exists()) {
            return false;
        }
        boolean zDelete = file.delete();
        updateFileFromDatabase(WDApplication.getInstance().getApplicationContext(), file);
        return zDelete;
    }

    private void deleteFolderAllFilesCommand(String str) {
        String[] list;
        File file;
        File file2 = new File(str);
        if (file2.exists() && file2.isDirectory() && (list = file2.list()) != null) {
            for (int i = 0; i < list.length; i++) {
                if (str.endsWith(File.separator)) {
                    file = new File(str + list[i]);
                } else {
                    file = new File(str + File.separator + list[i]);
                }
                if (file.isDirectory()) {
                    deleteFolderAllFilesCommand(str + "/" + list[i]);
                    deleteFileCommand(str + "/" + list[i]);
                } else {
                    if (file.exists()) {
                        file.delete();
                    }
                    updateFileFromDatabase(WDApplication.getInstance().getApplicationContext(), file);
                    updateDeleteLocalMediaLib(WDApplication.getInstance().getApplicationContext(), file.toString());
                }
            }
        }
    }

    private void updateDeleteLocalMediaLib(Context context, String str) {
        String str2;
        Uri uri;
        String fileTypeFromName = UtilTools.getFileTypeFromName(str);
        String uTF8CodeInfoFromURL = UtilTools.getUTF8CodeInfoFromURL(str);
        int fileTypeMarked = AdapterType.getFileTypeMarked(fileTypeFromName);
        if (fileTypeMarked == 1) {
            str2 = "_data='" + uTF8CodeInfoFromURL + "'";
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else if (fileTypeMarked == 2) {
            str2 = "_data='" + uTF8CodeInfoFromURL + "'";
            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        } else if (fileTypeMarked != 3) {
            str2 = "";
            uri = null;
        } else {
            str2 = "_data='" + uTF8CodeInfoFromURL + "'";
            uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }
        if (uri != null) {
            context.getContentResolver().delete(uri, str2, null);
        }
    }

    public static void updateFileFromDatabase(Context context, File file) {
        if (Build.VERSION.SDK_INT >= 19) {
            MediaScannerConnection.scanFile(context, new String[]{Environment.getExternalStorageDirectory().toString()}, null, null);
            MediaScannerConnection.scanFile(context, new String[]{file.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public void onScanCompleted(String str, Uri uri) {
                }
            });
        } else {
            context.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }
    }
}
