package com.i4season.bkCamera.logicrelated.fileacceptandoperation;

import com.i4season.bkCamera.logicrelated.fileacceptandoperation.bean.FileNode;
import com.i4season.bkCamera.uirelated.other.logmanage.LogWD;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FileNodeArrayManager {
    public static final int PHOTO_MODEL = 1;
    public static final int VIDEO_MODEL = 2;
    public static int mCurrentModel;
    private static Lock reentantLock = new ReentrantLock();
    protected List<FileNode> mFileList = new ArrayList();
    protected List<FileNode> mPhotoFileList = new ArrayList();
    protected List<FileNode> mVideoFileList = new ArrayList();
    protected List<FileNode> mAudioFileList = new ArrayList();
    protected LinkedHashMap<String, LinkedList<FileNode>> timeTagArrays = new LinkedHashMap<>();

    public void addFileNode(FileNode fileNode, boolean z) {
        try {
            reentantLock.lock();
            if (z) {
                LogWD.writeMsg(this, 2, "清掉数据");
                this.mFileList.clear();
            }
            LogWD.writeMsg(this, 2, "添加 " + fileNode.getmFileName());
            this.mFileList.add(fileNode);
        } finally {
            reentantLock.unlock();
        }
    }

    public void removeFileNode(FileNode fileNode) {
        try {
            reentantLock.lock();
            LogWD.writeMsg(this, 2, "移除 " + fileNode.getmFileName());
            this.mFileList.remove(fileNode);
            removeFileNode2Dlna(fileNode);
        } finally {
            reentantLock.unlock();
        }
    }

    public void removeFileNode(List<FileNode> list) {
        try {
            reentantLock.lock();
            for (FileNode fileNode : list) {
                LogWD.writeMsg(this, 2, "移除 " + fileNode.getmFileName());
                this.mFileList.remove(fileNode);
                removeFileNode2Dlna(fileNode);
            }
        } finally {
            reentantLock.unlock();
        }
    }

    public void clearFileNodeList() {
        try {
            reentantLock.lock();
            LogWD.writeMsg(this, 2, "移除所有 ");
            this.mFileList.clear();
        } finally {
            reentantLock.unlock();
        }
    }

    public void addFileNodeList(List<FileNode> list, boolean z) {
        try {
            reentantLock.lock();
            if (z) {
                LogWD.writeMsg(this, 2, "清掉数据");
                this.mFileList.clear();
            }
            LogWD.writeMsg(this, 2, "添加 fileNodes.size() " + list.size());
            this.mFileList.addAll(list);
        } finally {
            reentantLock.unlock();
        }
    }

    public void addDlnaListAll(List<FileNode> list) {
        try {
            reentantLock.lock();
            LogWD.writeMsg(this, 2, "添加到dlna all ");
            for (FileNode fileNode : list) {
                int i = fileNode.getmFileTypeMarked();
                if (i == 1 || i == 6) {
                    this.mPhotoFileList.add(fileNode);
                } else if (i == 2) {
                    this.mVideoFileList.add(fileNode);
                } else if (i == 3) {
                    this.mAudioFileList.add(fileNode);
                }
                LogWD.writeMsg(this, 2, "添加到dlna typeMarked： " + i);
            }
        } finally {
            reentantLock.unlock();
        }
    }

    public void addDlnaList(FileNode fileNode) {
        try {
            reentantLock.lock();
            int i = fileNode.getmFileTypeMarked();
            if (i == 1 || i == 6) {
                this.mPhotoFileList.add(fileNode);
            } else if (i == 2) {
                this.mVideoFileList.add(fileNode);
            } else if (i == 3) {
                this.mAudioFileList.add(fileNode);
            }
            LogWD.writeMsg(this, 2, "添加到dlna typeMarked： " + i);
        } finally {
            reentantLock.unlock();
        }
    }

    public void removeFileNode2Dlna(FileNode fileNode) {
        try {
            reentantLock.lock();
            int i = fileNode.getmFileTypeMarked();
            if (i == 1 || i == 6) {
                this.mPhotoFileList.remove(fileNode);
            } else if (i == 2) {
                this.mVideoFileList.remove(fileNode);
            } else if (i == 3) {
                this.mAudioFileList.remove(fileNode);
            }
            LogWD.writeMsg(this, 2, "从dlna列表移除 typeMarked： " + i);
        } finally {
            reentantLock.unlock();
        }
    }

    public void clearDlnaList() {
        this.mPhotoFileList.clear();
        this.mVideoFileList.clear();
        this.mAudioFileList.clear();
        LogWD.writeMsg(this, 2, "清空dlna列表");
    }

    public void selectOrUnselectAll(boolean z) {
        try {
            reentantLock.lock();
            Iterator<FileNode> it = this.mFileList.iterator();
            while (it.hasNext()) {
                it.next().setmFileIsSelected(z);
            }
        } finally {
            reentantLock.unlock();
        }
    }

    public List<FileNode> getAllSelectedFileNode() {
        ArrayList arrayList = new ArrayList();
        try {
            reentantLock.lock();
            if (mCurrentModel == 1) {
                for (FileNode fileNode : this.mPhotoFileList) {
                    if (fileNode.ismFileIsSelected()) {
                        arrayList.add(fileNode);
                    }
                }
            } else if (mCurrentModel == 2) {
                for (FileNode fileNode2 : this.mVideoFileList) {
                    if (fileNode2.ismFileIsSelected()) {
                        arrayList.add(fileNode2);
                    }
                }
            } else {
                for (FileNode fileNode3 : this.mFileList) {
                    if (fileNode3.ismFileIsSelected()) {
                        arrayList.add(fileNode3);
                    }
                }
            }
            return arrayList;
        } finally {
            reentantLock.unlock();
        }
    }

    public void selectHeadIdDay() {
        reentantLock.lock();
        this.timeTagArrays.clear();
        HashMap map = new HashMap();
        int i = 0;
        for (FileNode fileNode : this.mFileList) {
            String timtTag = fileNode.getTimtTag();
            if (!this.timeTagArrays.containsKey(timtTag)) {
                LinkedList<FileNode> linkedList = new LinkedList<>();
                linkedList.add(fileNode);
                this.timeTagArrays.put(timtTag, linkedList);
                fileNode.setHeadId(i);
                map.put(timtTag, Integer.valueOf(i));
                i++;
            } else {
                this.timeTagArrays.get(timtTag).addLast(fileNode);
                fileNode.setHeadId(((Integer) map.get(timtTag)).intValue());
            }
        }
        reentantLock.unlock();
    }

    public void selectHeadIdDayForModel(boolean z) {
        reentantLock.lock();
        this.timeTagArrays.clear();
        HashMap map = new HashMap();
        int i = 0;
        for (FileNode fileNode : z ? this.mPhotoFileList : this.mVideoFileList) {
            String timtTag = fileNode.getTimtTag();
            if (!this.timeTagArrays.containsKey(timtTag)) {
                LinkedList<FileNode> linkedList = new LinkedList<>();
                linkedList.add(fileNode);
                this.timeTagArrays.put(timtTag, linkedList);
                fileNode.setHeadId(i);
                map.put(timtTag, Integer.valueOf(i));
                i++;
            } else {
                this.timeTagArrays.get(timtTag).addLast(fileNode);
                fileNode.setHeadId(((Integer) map.get(timtTag)).intValue());
            }
        }
        reentantLock.unlock();
    }

    public List<FileNode> getmFileList() {
        return this.mFileList;
    }

    public void setmFileList(List<FileNode> list) {
        this.mFileList = list;
    }

    public LinkedHashMap<String, LinkedList<FileNode>> getTimeTagArrays() {
        return this.timeTagArrays;
    }

    public void setTimeTagArrays(LinkedHashMap<String, LinkedList<FileNode>> linkedHashMap) {
        this.timeTagArrays = linkedHashMap;
    }

    public List<FileNode> getmPhotoFileList() {
        return this.mPhotoFileList;
    }

    public List<FileNode> getmVideoFileList() {
        return this.mVideoFileList;
    }

    public static void setmCurrentModel(int i) {
        mCurrentModel = i;
    }
}
