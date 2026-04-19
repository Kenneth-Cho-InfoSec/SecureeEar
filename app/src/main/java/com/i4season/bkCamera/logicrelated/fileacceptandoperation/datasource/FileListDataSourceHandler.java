package com.i4season.bkCamera.logicrelated.fileacceptandoperation.datasource;

import com.i4season.bkCamera.logicrelated.fileacceptandoperation.FileNodeArrayManager;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.bean.FileNode;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.webdavcmd.FileListWebDavCommandHandle;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class FileListDataSourceHandler {
    protected IAcceptFileListDataDelegate iAcceptFileListDataDelegate;
    protected boolean isLoadMoreAble;
    protected boolean isRootDir;
    protected FileListWebDavCommandHandle mFileListWebDavCommandHandle;
    protected FileNodeArrayManager mFileNodeArrayManager;
    protected Lock mNodeArrayReadLock = new ReentrantLock();
    protected int ACCEPT_SIZE = 200;
    protected String mLastAcceptPath = "";
    protected List<FileNode> mFileList = new ArrayList();
    protected LinkedHashMap<String, LinkedList<FileNode>> timeTagArrays = new LinkedHashMap<>();

    protected abstract void acceptFileListForFolderPath(String str, int i, boolean z);

    protected abstract void acceptNextPageFileList();

    protected abstract void acceptRootFileList(boolean z);

    protected abstract void acceptSearchFileList(String str, String str2, int i, int i2);

    protected abstract void reflashFileList();

    public FileListDataSourceHandler(FileNodeArrayManager fileNodeArrayManager, FileListWebDavCommandHandle fileListWebDavCommandHandle, IAcceptFileListDataDelegate iAcceptFileListDataDelegate) {
        this.mFileNodeArrayManager = fileNodeArrayManager;
        this.mFileListWebDavCommandHandle = fileListWebDavCommandHandle;
        this.iAcceptFileListDataDelegate = iAcceptFileListDataDelegate;
    }

    public void queryAcceptRootFileList(final boolean z) {
        new Thread() {
            @Override
            public void run() {
                try {
                    FileListDataSourceHandler.this.mNodeArrayReadLock.lock();
                    FileListDataSourceHandler.this.mFileList.clear();
                    FileListDataSourceHandler.this.acceptRootFileList(z);
                } finally {
                    FileListDataSourceHandler.this.mNodeArrayReadLock.unlock();
                }
            }
        }.start();
    }

    public void queryAcceptFileListForFolderPath(final String str, final int i, final boolean z) {
        new Thread() {
            @Override
            public void run() {
                try {
                    FileListDataSourceHandler.this.mNodeArrayReadLock.lock();
                    FileListDataSourceHandler.this.mFileList.clear();
                    FileListDataSourceHandler.this.acceptFileListForFolderPath(str, i, z);
                } finally {
                    FileListDataSourceHandler.this.mNodeArrayReadLock.unlock();
                }
            }
        }.start();
    }

    public void queryAcceptSearchFileList(final String str, final String str2, final int i, final int i2) {
        new Thread() {
            @Override
            public void run() {
                try {
                    FileListDataSourceHandler.this.mNodeArrayReadLock.lock();
                    FileListDataSourceHandler.this.mFileList.clear();
                    FileListDataSourceHandler.this.acceptSearchFileList(str, str2, i, i2);
                } finally {
                    FileListDataSourceHandler.this.mNodeArrayReadLock.unlock();
                }
            }
        }.start();
    }

    public void queryAcceptNextPageFileList() {
        new Thread() {
            @Override
            public void run() {
                try {
                    FileListDataSourceHandler.this.mNodeArrayReadLock.lock();
                    FileListDataSourceHandler.this.mFileList.clear();
                    FileListDataSourceHandler.this.acceptNextPageFileList();
                } finally {
                    FileListDataSourceHandler.this.mNodeArrayReadLock.unlock();
                }
            }
        }.start();
    }

    public void queryReflashFileList() {
        new Thread() {
            @Override
            public void run() {
                try {
                    FileListDataSourceHandler.this.mNodeArrayReadLock.lock();
                    FileListDataSourceHandler.this.mFileList.clear();
                    FileListDataSourceHandler.this.reflashFileList();
                } finally {
                    FileListDataSourceHandler.this.mNodeArrayReadLock.unlock();
                }
            }
        }.start();
    }

    public boolean isLoadMoreAble() {
        return this.isLoadMoreAble;
    }

    public boolean isRootDir() {
        return this.isRootDir;
    }
}
