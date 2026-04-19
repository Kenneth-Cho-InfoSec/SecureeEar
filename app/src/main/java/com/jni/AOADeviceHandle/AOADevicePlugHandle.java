package com.jni.AOADeviceHandle;

import com.jni.UStorageDeviceModule;
import com.jni.logmanage.LogWD;

public class AOADevicePlugHandle {
    public IFileSystemInitSucc iFileSystemInitSucc;

    public interface IFileSystemInitSucc {
        void fileSystemInitType(int i, int i2, int i3);
    }

    public AOADevicePlugHandle(IFileSystemInitSucc iFileSystemInitSucc) {
        this.iFileSystemInitSucc = iFileSystemInitSucc;
    }

    public void HandleAOADevice(int i, int i2) {
        LogWD.writeMsg(this, 2, "aoatype =" + i + "rec =" + i2);
        IFileSystemInitSucc iFileSystemInitSucc = this.iFileSystemInitSucc;
        if (iFileSystemInitSucc != null) {
            iFileSystemInitSucc.fileSystemInitType(i, i2, 1);
        } else if (i == 3) {
            LogWD.writeMsg(this, 2, "destroy");
            UStorageDeviceModule.getInstance().destoryUStorageDeviceModule();
        }
    }

    public void HandleOTGDevice(int i, int i2) {
        LogWD.writeMsg(this, 2, "type =" + i + "rec =" + i2);
        IFileSystemInitSucc iFileSystemInitSucc = this.iFileSystemInitSucc;
        if (iFileSystemInitSucc != null) {
            iFileSystemInitSucc.fileSystemInitType(i, i2, 0);
        } else if (i == 3) {
            LogWD.writeMsg(this, 2, "destroy");
            UStorageDeviceModule.getInstance().destoryUStorageDeviceModule();
        }
    }
}
