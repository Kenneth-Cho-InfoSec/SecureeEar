package com.i4season.bkCamera.logicrelated.fileacceptandoperation;

import com.i4season.bkCamera.logicrelated.fileacceptandoperation.bean.FileNode;
import java.util.List;

public interface IExplorerManagerDelegate {
    void acceptFileListDataError(int i);

    void acceptFileListDataSuccful(List<FileNode> list);

    void operationError(int i, int i2);

    void operationSuccful(int i);
}
