package com.i4season.bkCamera.uirelated.filenodeopen.videoplay.bean;

public class SubtitleOrAudioBean {

    private int f65id;
    private int index;
    private boolean isSelected;
    private String showContent;

    public String getShowContent() {
        return this.showContent;
    }

    public void setShowContent(String str) {
        this.showContent = str;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int i) {
        this.index = i;
    }

    public int getId() {
        return this.f65id;
    }

    public void setId(int i) {
        this.f65id = i;
    }
}
