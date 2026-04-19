package com.i4season.bkCamera.logicrelated.encoder;

public class RecordParams {
    private boolean isAutoSave;
    private boolean isExternalVoiceData = false;
    private int recordDuration;
    private String recordPath;
    private boolean voiceClose;

    public boolean isVoiceClose() {
        return this.voiceClose;
    }

    public void setVoiceClose(boolean z) {
        this.voiceClose = z;
    }

    public String getRecordPath() {
        return this.recordPath;
    }

    public void setRecordPath(String str) {
        this.recordPath = str;
    }

    public int getRecordDuration() {
        return this.recordDuration;
    }

    public void setRecordDuration(int i) {
        this.recordDuration = i;
    }

    public boolean isAutoSave() {
        return this.isAutoSave;
    }

    public void setAutoSave(boolean z) {
        this.isAutoSave = z;
    }

    public boolean isExternalVoiceData() {
        return this.isExternalVoiceData;
    }

    public void setExternalVoiceData(boolean z) {
        this.isExternalVoiceData = z;
    }
}
