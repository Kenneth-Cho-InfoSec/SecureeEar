package com.i4season.bkCamera.logicrelated.fileacceptandoperation.webdavcmd;

import com.i4season.bkCamera.logicrelated.fileacceptandoperation.FileNodeArrayManager;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.bean.FileNode;
import com.i4season.bkCamera.uirelated.other.logmanage.LogWD;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FileListWebDavCommandHandle {
    protected IFileListWebDavCommandDelegate iFileListWebDavCommandDelegate;
    protected String mCurFolderPath;
    protected FileNodeArrayManager mFileNodeArrayManager;
    protected Lock mNodeArrayReadLock = new ReentrantLock();

    protected void copyFileList() {
    }

    protected void creatFolder(String str) {
    }

    protected void cutFileList() {
    }

    protected void deleteFile() {
    }

    protected void isExistFile(FileNode fileNode) {
    }

    protected void pastFileList() {
    }

    protected void renameFileName(FileNode fileNode, String str) {
    }

    public FileListWebDavCommandHandle(String str, FileNodeArrayManager fileNodeArrayManager, IFileListWebDavCommandDelegate iFileListWebDavCommandDelegate) {
        this.mCurFolderPath = str;
        this.mFileNodeArrayManager = fileNodeArrayManager;
        this.iFileListWebDavCommandDelegate = iFileListWebDavCommandDelegate;
    }

    public void trySelectOrUnselectAll(final boolean z) {
        new Thread() {
            @Override
            public void run() {
                try {
                    try {
                        FileListWebDavCommandHandle.this.mNodeArrayReadLock.lock();
                        FileListWebDavCommandHandle.this.selectOrUnselectAll(z);
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogWD.writeMsg(e);
                        FileListWebDavCommandHandle.this.iFileListWebDavCommandDelegate.operationError(-1, -1);
                    }
                } finally {
                    FileListWebDavCommandHandle.this.mNodeArrayReadLock.unlock();
                }
            }
        }.start();
    }

    public void tryCreatFolder(final String str) {
        new Thread() {
            @Override
            public void run() {
                try {
                    try {
                        FileListWebDavCommandHandle.this.mNodeArrayReadLock.lock();
                        FileListWebDavCommandHandle.this.creatFolder(str);
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogWD.writeMsg(e);
                        FileListWebDavCommandHandle.this.iFileListWebDavCommandDelegate.operationError(0, -1);
                    }
                } finally {
                    FileListWebDavCommandHandle.this.mNodeArrayReadLock.unlock();
                }
            }
        }.start();
    }

    public void tryRenameFileName(final FileNode fileNode, final String str) {
        new Thread() {
            @Override
            public void run() {
                try {
                    try {
                        FileListWebDavCommandHandle.this.mNodeArrayReadLock.lock();
                        FileListWebDavCommandHandle.this.renameFileName(fileNode, str);
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogWD.writeMsg(e);
                        FileListWebDavCommandHandle.this.iFileListWebDavCommandDelegate.operationError(1, -1);
                    }
                } finally {
                    FileListWebDavCommandHandle.this.mNodeArrayReadLock.unlock();
                }
            }
        }.start();
    }

    public void tryDeleteFile() {
        new Thread() {
            @Override
            public void run() {
                try {
                    try {
                        FileListWebDavCommandHandle.this.mNodeArrayReadLock.lock();
                        FileListWebDavCommandHandle.this.deleteFile();
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogWD.writeMsg(e);
                        FileListWebDavCommandHandle.this.iFileListWebDavCommandDelegate.operationError(2, -1);
                    }
                } finally {
                    FileListWebDavCommandHandle.this.mNodeArrayReadLock.unlock();
                }
            }
        }.start();
    }

    public void tryExistFile(final FileNode fileNode) {
        new Thread() {
            @Override
            public void run() {
                try {
                    try {
                        FileListWebDavCommandHandle.this.mNodeArrayReadLock.lock();
                        FileListWebDavCommandHandle.this.isExistFile(fileNode);
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogWD.writeMsg(e);
                        FileListWebDavCommandHandle.this.iFileListWebDavCommandDelegate.operationError(2, -1);
                    }
                } finally {
                    FileListWebDavCommandHandle.this.mNodeArrayReadLock.unlock();
                }
            }
        }.start();
    }

    public void tryCopyFileList() {
        new Thread() {
            @Override
            public void run() {
                try {
                    try {
                        FileListWebDavCommandHandle.this.mNodeArrayReadLock.lock();
                        FileListWebDavCommandHandle.this.copyFileList();
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogWD.writeMsg(e);
                    }
                } finally {
                    FileListWebDavCommandHandle.this.mNodeArrayReadLock.unlock();
                }
            }
        }.start();
    }

    public void tryCutFileList() {
        new Thread() {
            @Override
            public void run() {
                try {
                    try {
                        FileListWebDavCommandHandle.this.mNodeArrayReadLock.lock();
                        FileListWebDavCommandHandle.this.cutFileList();
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogWD.writeMsg(e);
                    }
                } finally {
                    FileListWebDavCommandHandle.this.mNodeArrayReadLock.unlock();
                }
            }
        }.start();
    }

    public void tryPastFileList() {
        new Thread() {
            @Override
            public void run() {
                try {
                    try {
                        FileListWebDavCommandHandle.this.mNodeArrayReadLock.lock();
                        FileListWebDavCommandHandle.this.pastFileList();
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogWD.writeMsg(e);
                    }
                } finally {
                    FileListWebDavCommandHandle.this.mNodeArrayReadLock.unlock();
                }
            }
        }.start();
    }

    protected void selectOrUnselectAll(boolean z) {
        LogWD.writeMsg(this, 2, "selectOrUnselectAll isSelectAll: " + z);
        FileNodeArrayManager fileNodeArrayManager = this.mFileNodeArrayManager;
        if (fileNodeArrayManager != null) {
            fileNodeArrayManager.selectOrUnselectAll(z);
        }
        LogWD.writeMsg(this, 2, "selectOrUnselectAll end");
        this.iFileListWebDavCommandDelegate.operationSuccful(-1);
    }

    public String getmCurFolderPath() {
        return this.mCurFolderPath;
    }

    public void setmCurFolderPath(String str) {
        this.mCurFolderPath = str;
    }
}
