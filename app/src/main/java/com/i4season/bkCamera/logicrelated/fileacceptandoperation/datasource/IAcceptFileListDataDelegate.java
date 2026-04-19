package com.i4season.bkCamera.logicrelated.fileacceptandoperation.datasource;

import com.i4season.bkCamera.logicrelated.fileacceptandoperation.bean.FileNode;
import java.util.List;

public interface IAcceptFileListDataDelegate {
    void acceptFileListDataError(int i);

    void acceptFileListDataSuccful(List<FileNode> list);
}
