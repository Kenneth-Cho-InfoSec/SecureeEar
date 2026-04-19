package com.i4season.bkCamera.uirelated.filenodeopen.videoplay.handler;

import com.i4season.bkCamera.uirelated.filenodeopen.videoplay.bean.SubtitleOrAudioBean;

public interface VideoHandler {

    public enum DecodeMode {
        SW,
        MHW
    }

    int addSubtitle(String str);

    void breakpointPlay(boolean z);

    boolean canPause();

    void compareResolution();

    void deletePlayRecording();

    void destory();

    int getAudioCount();

    SubtitleOrAudioBean getAudioInfo(int i);

    int getBufferPercentage();

    int getCurAudioIndex();

    int getCurSubtitleIndex();

    String getCurrentNetworkSpeed();

    long getCurrentPosition();

    DecodeMode getDecodeMode();

    long getDuration();

    boolean getPlayStatus();

    int getRecordingTime();

    String getResolution();

    String getSubTitleName();

    int getSubtitleCount();

    SubtitleOrAudioBean getSubtitleInfo(int i);

    int getVideoResolution();

    void getWiFiHotSpot();

    boolean isManulCache();

    boolean isPlaying();

    void isShowSubtitle();

    boolean moveSeekBackward();

    boolean moveSeekForward();

    void next();

    void openVideo(String str);

    void pause();

    void play();

    void previous();

    void ramdom(int i);

    void recordBookmark();

    void saveRecording();

    void seekTo(long j);

    void setAudioIndex(int i);

    void setDecodeMode(DecodeMode decodeMode);

    void setIsShowToast(boolean z);

    void setManulCache(boolean z);

    void setRotationDegree(int i);

    void setSubTitleFilePath(String str);

    void setSubtitleIndex(int i);

    void start();

    void startSubMethod();

    void stop();

    void videoViewMode(int i);
}
