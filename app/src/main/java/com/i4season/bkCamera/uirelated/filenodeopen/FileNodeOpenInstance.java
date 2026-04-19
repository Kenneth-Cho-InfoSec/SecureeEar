package com.i4season.bkCamera.uirelated.filenodeopen;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.bean.FileNode;
import com.i4season.bkCamera.uirelated.other.MainFrameHandleInstance;
import com.i4season.bkCamera.uirelated.other.function.AdapterType;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.Constant;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.UtilTools;
import com.i4season.bkCamera.uirelated.other.logmanage.LogWD;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FileNodeOpenInstance implements ISetOpenListFinishDelegate {
    private static FileNodeOpenInstance instance;
    private static Lock reentantLock = new ReentrantLock();
    private Context mContext;
    protected boolean mIslocal;
    protected int mOpenIndex;
    protected List<FileNode> mPicFileList = new ArrayList();
    protected List<FileNode> mVideoFileList = new ArrayList();
    protected List<FileNode> mAudioFileList = new ArrayList();

    public static FileNodeOpenInstance getInstance() {
        if (instance == null) {
            try {
                reentantLock.lock();
                if (instance == null) {
                    instance = new FileNodeOpenInstance();
                }
            } finally {
                reentantLock.unlock();
            }
        }
        return instance;
    }

    public void openFile(Context context, int i, FileNode fileNode, int i2, List<FileNode> list) {
        this.mContext = context;
        int i3 = fileNode.getmFileTypeMarked();
        if (i3 == 8 || i3 == 10 || i3 == 11 || i3 == 12 || i3 == 13 || i3 == 14 || i3 == 22 || i3 == 19) {
            openDocument(this.mContext, fileNode);
            return;
        }
        MainFrameHandleInstance.getInstance().showCenterProgressDialog(true);
        if (i2 == 5 || i2 == 6) {
            setOpenList2Explor(this.mContext, list, fileNode.getmFileTypeMarked(), i, fileNode.isLocal(), this);
        } else {
            setOpenList2Dlna(this.mContext, list, i2, i, fileNode.isLocal(), this);
        }
    }

    @Override
    public void setOpenListFinish(int i) {
        LogWD.writeMsg(this, 2, "setOpenListFinish fileType: " + i);
        MainFrameHandleInstance.getInstance().showCenterProgressDialog(false);
        if (i == 1 || i == 6) {
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constant.PHOTO_VIEW_COMPARE, false);
            MainFrameHandleInstance.getInstance().showPhotoPreviewActivity(this.mContext, bundle, false);
        } else if (i != 3 && i == 2) {
            MainFrameHandleInstance.getInstance().showVideoPlayerNewActivity(this.mContext, false);
        }
    }

    public void setOpenList2Explor(final Context context, final List<FileNode> list, final int i, final int i2, final boolean z, final ISetOpenListFinishDelegate iSetOpenListFinishDelegate) {
        new Thread() {
            @Override
            public void run() {
                FileNodeOpenInstance.this.mContext = context;
                FileNodeOpenInstance fileNodeOpenInstance = FileNodeOpenInstance.this;
                fileNodeOpenInstance.mIslocal = z;
                fileNodeOpenInstance.mPicFileList.clear();
                FileNodeOpenInstance.this.mAudioFileList.clear();
                FileNodeOpenInstance.this.mVideoFileList.clear();
                LogWD.writeMsg(this, 2, "文件打开 fileType：" + i + " position：" + i2 + " isLocal：" + z);
                for (int i3 = 0; i3 < list.size(); i3++) {
                    FileNode fileNode = (FileNode) list.get(i3);
                    int i4 = fileNode.getmFileTypeMarked();
                    if (i4 == i) {
                        LogWD.writeMsg(this, 2, "fileTypeMarked: " + i4);
                        if (i4 == 1) {
                            FileNodeOpenInstance.this.mPicFileList.add(fileNode);
                            if (i2 == i3) {
                                FileNodeOpenInstance fileNodeOpenInstance2 = FileNodeOpenInstance.this;
                                fileNodeOpenInstance2.mOpenIndex = fileNodeOpenInstance2.mPicFileList.size() - 1;
                            }
                        } else if (i4 == 3) {
                            FileNodeOpenInstance.this.mAudioFileList.add(fileNode);
                            if (i2 == i3) {
                                FileNodeOpenInstance fileNodeOpenInstance3 = FileNodeOpenInstance.this;
                                fileNodeOpenInstance3.mOpenIndex = fileNodeOpenInstance3.mAudioFileList.size() - 1;
                            }
                        } else if (i4 == 2) {
                            FileNodeOpenInstance.this.mVideoFileList.add(fileNode);
                            if (i2 == i3) {
                                FileNodeOpenInstance fileNodeOpenInstance4 = FileNodeOpenInstance.this;
                                fileNodeOpenInstance4.mOpenIndex = fileNodeOpenInstance4.mVideoFileList.size() - 1;
                            }
                        }
                    }
                }
                iSetOpenListFinishDelegate.setOpenListFinish(i);
            }
        }.start();
    }

    public void setOpenList2Dlna(final Context context, final List<FileNode> list, final int i, final int i2, final boolean z, final ISetOpenListFinishDelegate iSetOpenListFinishDelegate) {
        new Thread() {
            @Override
            public void run() {
                FileNodeOpenInstance.this.mContext = context;
                FileNodeOpenInstance fileNodeOpenInstance = FileNodeOpenInstance.this;
                fileNodeOpenInstance.mOpenIndex = i2;
                fileNodeOpenInstance.mIslocal = z;
                int i3 = 2;
                LogWD.writeMsg(this, 2, "文件打开 dlnaType：" + i + " position：" + i2 + " isLocal：" + z);
                int i4 = i;
                if (i4 == 3) {
                    FileNodeOpenInstance.this.mPicFileList.clear();
                    FileNodeOpenInstance.this.mPicFileList.addAll(list);
                    i3 = 1;
                } else if (i4 == 1) {
                    FileNodeOpenInstance.this.mAudioFileList.clear();
                    FileNodeOpenInstance.this.mAudioFileList.addAll(list);
                    i3 = 3;
                } else if (i4 == 2) {
                    FileNodeOpenInstance.this.mVideoFileList.clear();
                    FileNodeOpenInstance.this.mVideoFileList.addAll(list);
                } else {
                    i3 = 0;
                }
                iSetOpenListFinishDelegate.setOpenListFinish(i3);
            }
        }.start();
    }

    public void setOpenData(int i, List<FileNode> list, int i2) {
        this.mOpenIndex = i2;
        if (i == 1 || i == 6) {
            this.mPicFileList.clear();
            this.mPicFileList.addAll(list);
        } else if (i == 3) {
            this.mAudioFileList.clear();
            this.mAudioFileList.addAll(list);
        } else if (i == 2) {
            this.mVideoFileList.clear();
            this.mVideoFileList.addAll(list);
        }
    }

    public void openDocument(Context context, FileNode fileNode) {
        this.mContext = context;
        if (fileNode.isLocal()) {
            openFile2Intent(new File(fileNode.getmFileDevPath()));
        }
    }

    private void openFile2Intent(File file) {
        try {
            FileOpenSetOpenProperty fileOpenSetOpenProperty = new FileOpenSetOpenProperty();
            fileOpenSetOpenProperty.setOpenProperty(AdapterType.getFileTypeMarked(UtilTools.getFileTypeFromName(file.getName())));
            Intent intent = fileOpenSetOpenProperty.getIntent();
            intent.setDataAndType(Uri.fromFile(file), fileOpenSetOpenProperty.getMimeType());
            this.mContext.startActivity(intent);
        } catch (Exception e) {
            UtilTools.showToast(this.mContext, e.toString());
            e.printStackTrace();
        }
    }

    public List<FileNode> getmPicFileList() {
        return this.mPicFileList;
    }

    public List<FileNode> getmVideoFileList() {
        return this.mVideoFileList;
    }

    public List<FileNode> getmAudioFileList() {
        return this.mAudioFileList;
    }

    public int getmOpenIndex() {
        return this.mOpenIndex;
    }

    public boolean ismIslocal() {
        return this.mIslocal;
    }
}
