package com.jni.CallBack;

import com.jnibean.VSFileInfoBean;
import java.io.Serializable;

public class UCallBackInfo implements Serializable {
    private VSFileInfoBean mVSFileInfoBean;
    private int type;

    public VSFileInfoBean getVSFileInfoBean() {
        return this.mVSFileInfoBean;
    }

    public void setVSFileInfoBean(VSFileInfoBean vSFileInfoBean) {
        this.mVSFileInfoBean = vSFileInfoBean;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }
}
