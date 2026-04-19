package com.jni.AOADeviceHandle;

public class AOADeviceTestError {
    public int begin;
    public int bufsize;
    public int error;
    public int errorbegin;
    public int errorsendsize;
    public int lun;
    public int max;

    public void SetBegin(int i) {
        this.begin = i;
    }

    public void SetBufsize(int i) {
        this.bufsize = i;
    }

    public void SetMax(int i) {
        this.max = i;
    }

    public void SetErrorbegin(int i) {
        this.errorbegin = i;
    }

    public void SetError(int i) {
        this.error = i;
    }

    public void SetErrorsendsize(int i) {
        this.errorsendsize = i;
    }

    public void SetLun(int i) {
        this.lun = i;
    }
}
