package com.i4season.bkCamera.logicrelated.camera.reportlic;

public interface IReportDelegate {
    void brunLicBegin(String str);

    void cameraCheckOnline();

    void reportReult(boolean z);
}
