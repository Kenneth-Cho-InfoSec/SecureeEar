package com.i4season.bkCamera.uirelated.filenodeopen.videoplay.handler;

public interface IVlcPlayerCompleted {
    void playerOnCompleted();

    void playerOnFailed();

    void playerOnHwDecodeFailed();

    void playerOnPaused();

    void playerOnResumed();

    void playerOnSeekState(int i);

    void playerOnSeeked();

    void playerOnStarted();

    void playerOnStopped();
}
