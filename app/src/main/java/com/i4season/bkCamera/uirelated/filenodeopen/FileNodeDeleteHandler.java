package com.i4season.bkCamera.uirelated.filenodeopen;

import com.i4season.bkCamera.logicrelated.fileacceptandoperation.bean.FileNode;
import com.i4season.bkCamera.uirelated.other.logmanage.LogWD;
import java.io.File;

public class FileNodeDeleteHandler {
    private IDeleteResultDelegate iDeleteResultDelegate;
    private FileNode mFileNode;
    private final int SUCCESSFUL = 0;
    private final int ERRORCODE = -1;

    public interface IDeleteResultDelegate {
        void deleteFinish(boolean z, FileNode fileNode, int i);
    }

    public FileNodeDeleteHandler(IDeleteResultDelegate iDeleteResultDelegate) {
        this.iDeleteResultDelegate = iDeleteResultDelegate;
    }

    public void startDeleteFile(FileNode fileNode, boolean z) {
        this.mFileNode = fileNode;
        if (z) {
            LogWD.writeMsg(this, 2, "本地 删除");
            deleteFile2Local(fileNode);
        } else {
            LogWD.writeMsg(this, 2, "设备 删除");
        }
    }

    private void deleteFile2Local(FileNode fileNode) {
        if (new File(fileNode.getmFileDevPath()).delete()) {
            this.iDeleteResultDelegate.deleteFinish(true, this.mFileNode, 0);
        } else {
            this.iDeleteResultDelegate.deleteFinish(true, this.mFileNode, -1);
        }
    }
}
