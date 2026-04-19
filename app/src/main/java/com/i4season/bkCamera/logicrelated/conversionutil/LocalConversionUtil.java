package com.i4season.bkCamera.logicrelated.conversionutil;

import com.i4season.bkCamera.logicrelated.fileacceptandoperation.bean.FileNode;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.datasource.explorer.localfilemanage.MountFileBean;
import com.i4season.bkCamera.uirelated.other.function.AdapterType;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.UtilTools;
import java.io.File;

public class LocalConversionUtil {
    public static void rootDir2FileNode(MountFileBean mountFileBean, FileNode fileNode) {
        fileNode.setmFileTime(mountFileBean.getMFBTime());
        fileNode.setmFileName(mountFileBean.getMFBName());
        fileNode.setFile(false);
        fileNode.setmFileDevPath(mountFileBean.getMFBPath());
    }

    public static void file2FileNode(File file, FileNode fileNode) {
        fileNode.setmFilePath(file.getPath());
        fileNode.setmFileDevPath(file.getPath());
        fileNode.setmFileName(file.getName());
        fileNode.setmFileCreateTime(file.lastModified());
        fileNode.setmFileIsSelected(false);
        String timeFromLong = UtilTools.getTimeFromLong(file.lastModified());
        fileNode.setmFileTime(timeFromLong);
        fileNode.setTimtTag(UtilTools.getTimeTag(timeFromLong));
        fileNode.setLocal(true);
        fileNode.setFile(file.isFile());
        fileNode.setmFileSize(UtilTools.FormetFileSize(file.length() + ""));
        fileNode.setmFileSizeLong(file.length() + "");
        if (file.isFile()) {
            String fileTypeFromName = UtilTools.getFileTypeFromName(file.getName());
            fileNode.setmFileType(fileTypeFromName);
            fileNode.setmFileTypeMarked(AdapterType.getFileTypeMarked(fileTypeFromName));
        } else {
            fileNode.setmFileType(FileNode.FOLDER_TYPE);
            fileNode.setmFileTypeMarked(5);
        }
        fileNode.setmParentPath(file.getParentFile().getPath());
        fileNode.setmParentName(file.getParentFile().getName());
    }

    public static void dlanFile2FileNode(String str, String str2, long j, String str3, int i, String str4, FileNode fileNode) {
        fileNode.setmFileDevPath(str);
        fileNode.setmFileName(str2);
        fileNode.setmFileCreateTime(j);
        fileNode.setmFileIsSelected(false);
        fileNode.setmFileType(UtilTools.getFileTypeFromName(str3));
        fileNode.setmFileTypeMarked(i);
        String timeFromLong = UtilTools.getTimeFromLong(j);
        fileNode.setmFileTime(timeFromLong);
        fileNode.setTimtTag(UtilTools.getTimeTag(timeFromLong));
        fileNode.setmFileSize(UtilTools.FormetFileSize(str4));
        fileNode.setmFileSizeLong(str4);
        fileNode.setLocal(true);
        fileNode.setFile(true);
    }
}
