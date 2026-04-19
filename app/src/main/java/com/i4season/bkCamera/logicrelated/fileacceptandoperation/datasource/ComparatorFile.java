package com.i4season.bkCamera.logicrelated.fileacceptandoperation.datasource;

import com.i4season.bkCamera.logicrelated.fileacceptandoperation.bean.FileNode;
import java.util.Comparator;

public class ComparatorFile implements Comparator<FileNode> {
    @Override
    public int compare(FileNode fileNode, FileNode fileNode2) {
        if (fileNode.getmFileCreateTime() < fileNode2.getmFileCreateTime()) {
            return 1;
        }
        return fileNode.getmFileCreateTime() == fileNode2.getmFileCreateTime() ? 0 : -1;
    }
}
