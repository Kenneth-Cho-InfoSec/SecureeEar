package com.i4season.bkCamera.logicrelated.fileacceptandoperation.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public class FileNode implements Serializable, Comparable<FileNode> {
    public static final int FILE_TYPE_MARKED_APK_NOSUPPORT = 15;
    public static final int FILE_TYPE_MARKED_AUDIO = 3;
    public static final int FILE_TYPE_MARKED_CHM_NOSUPPORT = 16;
    public static final int FILE_TYPE_MARKED_DOC = 4;
    public static final int FILE_TYPE_MARKED_DOCUMENT = 8;
    public static final int FILE_TYPE_MARKED_DOCUMENT_NOSUPPORT = 9;
    public static final int FILE_TYPE_MARKED_EXCEL = 22;
    public static final int FILE_TYPE_MARKED_FOLDER = 5;
    public static final int FILE_TYPE_MARKED_HTML = 14;
    public static final int FILE_TYPE_MARKED_MUSIC_NOSUPPORT = 7;
    public static final int FILE_TYPE_MARKED_NONE = 0;
    public static final int FILE_TYPE_MARKED_PDF = 13;
    public static final int FILE_TYPE_MARKED_PIC = 1;
    public static final int FILE_TYPE_MARKED_PIC_NOSUPPORT = 6;
    public static final int FILE_TYPE_MARKED_PPT = 12;
    public static final int FILE_TYPE_MARKED_RAR = 20;
    public static final int FILE_TYPE_MARKED_SWF_NOSUPPORT = 17;
    public static final int FILE_TYPE_MARKED_TAR = 21;
    public static final int FILE_TYPE_MARKED_TXT = 10;
    public static final int FILE_TYPE_MARKED_VCF_NOSUPPORT = 19;
    public static final int FILE_TYPE_MARKED_VIDEO = 2;
    public static final int FILE_TYPE_MARKED_XLS = 11;
    public static final int FILE_TYPE_MARKED_ZIP = 18;
    public static final String FOLDER_TYPE = "DIR";
    private int headId;
    private int index;
    private boolean isFile;
    private boolean isLocal;
    private FileNode mChildFileNode;
    private long mFileCreateTime;
    private String mFileDevPath;
    private boolean mFileIsSelected;
    private String mFileName;
    private String mFilePath;
    private String mFileSize;
    private String mFileSizeLong;
    private String mFileTime;
    private String mFileType;
    private int mFileTypeMarked;
    private String mLimit;
    private List<FileNode> mNoBackupList;
    private String mParentName;
    private String mParentPath;
    private int mSourceType;
    private String timgLength;
    private String timtTag;

    public boolean isFileNeedMakeThumb() {
        return this.mFileType.toUpperCase(Locale.getDefault()).equals("PNG") || this.mFileType.toUpperCase(Locale.getDefault()).equals("TIFF") || this.mFileType.toUpperCase(Locale.getDefault()).equals("TIF") || this.mFileType.toUpperCase(Locale.getDefault()).equals("JPG") || this.mFileType.toUpperCase(Locale.getDefault()).equals("JPEG") || this.mFileType.toUpperCase(Locale.getDefault()).equals("BMP") || this.mFileType.toUpperCase(Locale.getDefault()).equals("MP3") || this.mFileType.toUpperCase(Locale.getDefault()).equals("WAV") || this.mFileType.toUpperCase(Locale.getDefault()).equals("M4A") || this.mFileType.toUpperCase(Locale.getDefault()).equals("AAC") || this.mFileType.toUpperCase(Locale.getDefault()).equals("AIFF") || this.mFileType.toUpperCase(Locale.getDefault()).equals("AIFC") || this.mFileType.toUpperCase(Locale.getDefault()).equals("CAF") || this.mFileType.toUpperCase(Locale.getDefault()).equals("AIF") || this.mFileType.toUpperCase(Locale.getDefault()).equals("ADTS") || this.mFileType.toUpperCase(Locale.getDefault()).equals("SND") || this.mFileType.toUpperCase(Locale.getDefault()).equals("AU") || this.mFileType.toUpperCase(Locale.getDefault()).equals("SD2") || this.mFileType.toUpperCase(Locale.getDefault()).equals("AC3") || this.mFileType.toUpperCase(Locale.getDefault()).equals("MP4") || this.mFileType.toUpperCase(Locale.getDefault()).equals("3GP") || this.mFileType.toUpperCase(Locale.getDefault()).equals("MOV") || this.mFileType.toUpperCase(Locale.getDefault()).equals("WMV") || this.mFileType.toUpperCase(Locale.getDefault()).equals("3G2") || this.mFileType.toUpperCase(Locale.getDefault()).equals("ASF") || this.mFileType.toUpperCase(Locale.getDefault()).equals("DIVX") || this.mFileType.toUpperCase(Locale.getDefault()).equals("FLV") || this.mFileType.toUpperCase(Locale.getDefault()).equals("M4V") || this.mFileType.toUpperCase(Locale.getDefault()).equals("MKV") || this.mFileType.toUpperCase(Locale.getDefault()).equals("MPG") || this.mFileType.toUpperCase(Locale.getDefault()).equals("RM") || this.mFileType.toUpperCase(Locale.getDefault()).equals("RMVB") || this.mFileType.toUpperCase(Locale.getDefault()).equals("SWF") || this.mFileType.toUpperCase(Locale.getDefault()).equals("TS") || this.mFileType.toUpperCase(Locale.getDefault()).equals("TP") || this.mFileType.toUpperCase(Locale.getDefault()).equals("VOB") || this.mFileType.toUpperCase(Locale.getDefault()).equals("WEBM") || this.mFileType.toUpperCase(Locale.getDefault()).equals("TP") || this.mFileType.toUpperCase(Locale.getDefault()).equals("AVI");
    }

    @Override
    public int compareTo(FileNode fileNode) {
        if (getmFileCreateTime() < fileNode.getmFileCreateTime()) {
            return -1;
        }
        return getmFileCreateTime() > fileNode.getmFileCreateTime() ? 1 : 0;
    }

    public boolean ismFileIsSelected() {
        return this.mFileIsSelected;
    }

    public void setmFileIsSelected(boolean z) {
        this.mFileIsSelected = z;
    }

    public String getmFileDevPath() {
        return this.mFileDevPath;
    }

    public void setmFileDevPath(String str) {
        this.mFileDevPath = str;
    }

    public String getmFilePath() {
        return this.mFilePath;
    }

    public void setmFilePath(String str) {
        this.mFilePath = str;
    }

    public String getmFileName() {
        return this.mFileName;
    }

    public void setmFileName(String str) {
        this.mFileName = str;
    }

    public long getmFileCreateTime() {
        return this.mFileCreateTime;
    }

    public void setmFileCreateTime(long j) {
        this.mFileCreateTime = j;
    }

    public String getmFileTime() {
        return this.mFileTime;
    }

    public void setmFileTime(String str) {
        this.mFileTime = str;
    }

    public String getmFileType() {
        return this.mFileType;
    }

    public void setmFileType(String str) {
        this.mFileType = str;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int i) {
        this.index = i;
    }

    public String getTimtTag() {
        return this.timtTag;
    }

    public void setTimtTag(String str) {
        this.timtTag = str;
    }

    public int getHeadId() {
        return this.headId;
    }

    public void setHeadId(int i) {
        this.headId = i;
    }

    public String getTimgLength() {
        return this.timgLength;
    }

    public void setTimgLength(String str) {
        this.timgLength = str;
    }

    public void setLocal(boolean z) {
        this.isLocal = z;
    }

    public void setFile(boolean z) {
        this.isFile = z;
    }

    public String getmFileSize() {
        return this.mFileSize;
    }

    public void setmFileSize(String str) {
        this.mFileSize = str;
    }

    public String getmFileSizeLong() {
        return this.mFileSizeLong;
    }

    public void setmFileSizeLong(String str) {
        this.mFileSizeLong = str;
    }

    public boolean isLocal() {
        return this.isLocal;
    }

    public boolean isFile() {
        return this.isFile;
    }

    public int getmFileTypeMarked() {
        return this.mFileTypeMarked;
    }

    public void setmFileTypeMarked(int i) {
        this.mFileTypeMarked = i;
    }

    public String getmLimit() {
        return this.mLimit;
    }

    public void setmLimit(String str) {
        this.mLimit = str;
    }

    public String getmParentPath() {
        return this.mParentPath;
    }

    public void setmParentPath(String str) {
        this.mParentPath = str;
    }

    public FileNode getmChildFileNode() {
        return this.mChildFileNode;
    }

    public void setmChildFileNode(FileNode fileNode) {
        this.mChildFileNode = fileNode;
    }

    public List<FileNode> getmNoBackupList() {
        return this.mNoBackupList;
    }

    public void setmNoBackupList(List<FileNode> list) {
        this.mNoBackupList = list;
    }

    public String getmParentName() {
        return this.mParentName;
    }

    public void setmParentName(String str) {
        this.mParentName = str;
    }

    public int getSourceType() {
        return this.mSourceType;
    }

    public void setSourceType(int i) {
        this.mSourceType = i;
    }
}
