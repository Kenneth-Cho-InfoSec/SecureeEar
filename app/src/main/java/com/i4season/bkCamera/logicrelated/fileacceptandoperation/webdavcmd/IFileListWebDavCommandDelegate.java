package com.i4season.bkCamera.logicrelated.fileacceptandoperation.webdavcmd;

public interface IFileListWebDavCommandDelegate {
    public static final int EXCEPTION_ERROR = -1;
    public static final int LOCAL_FILE_OPERATION_ERROR = -2;
    public static final int OPERATION_CREAT_FOLDER = 0;
    public static final int OPERATION_DELETE_FOLDER = 2;
    public static final int OPERATION_EXIST_FOLDER = 3;
    public static final int OPERATION_RENAME_FOLDER = 1;
    public static final int OPERATION_SELECT_OR_UNSELECT_FOLDER = -1;
    public static final int OPERATION_TRANSFER_DATA = 4;

    void operationError(int i, int i2);

    void operationSuccful(int i);
}
