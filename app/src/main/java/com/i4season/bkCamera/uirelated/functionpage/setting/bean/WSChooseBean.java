package com.i4season.bkCamera.uirelated.functionpage.setting.bean;

public class WSChooseBean {
    public static final int IS_CHECK = 1;
    public static final int IS_RADIO = 2;
    private String mWSChTitle = "";
    private String mWSChInfo = "";
    private int mWSChValue = 0;
    private boolean isChSelect = false;
    private int mChooseType = 1;

    public String getWSChTitle() {
        return this.mWSChTitle;
    }

    public void setWSChTitle(String str) {
        this.mWSChTitle = str;
    }

    public String getWSChInfo() {
        return this.mWSChInfo;
    }

    public void setWSChInfo(String str) {
        this.mWSChInfo = str;
    }

    public int getWSChValue() {
        return this.mWSChValue;
    }

    public void setWSChValue(int i) {
        this.mWSChValue = i;
    }

    public boolean isChSelect() {
        return this.isChSelect;
    }

    public void setChSelect(boolean z) {
        this.isChSelect = z;
    }

    public int getChooseType() {
        return this.mChooseType;
    }

    public void setChooseType(int i) {
        this.mChooseType = i;
    }
}
