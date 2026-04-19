package com.i4season.bkCamera.logicrelated.recoder;

import android.graphics.Bitmap;
import com.i4season.bkCamera.logicrelated.recoder.RecoderVideoRunable;
import com.jni.WifiCameraInfo.WifiCameraPic;

public class I4seasonRecoderInstanse {
    private RecoderVideoRunable mRecoderVideoRunable;

    public static class I4seasonRecoderInstanseWDHolder {
        public static I4seasonRecoderInstanse logManager = new I4seasonRecoderInstanse();
    }

    public static I4seasonRecoderInstanse getInstance() {
        return I4seasonRecoderInstanseWDHolder.logManager;
    }

    public void startRecoder(int i, int i2, int i3, int i4, int i5, boolean z, boolean z2, boolean z3, String str, RecoderVideoRunable.RecoderDelegate recoderDelegate) {
        this.mRecoderVideoRunable = new RecoderVideoRunable(i, i2, i3, i4, i5, z, z2, z3, str, recoderDelegate);
        new Thread(this.mRecoderVideoRunable).start();
    }

    public void addRecoderFrame(Bitmap bitmap) {
        RecoderVideoRunable recoderVideoRunable = this.mRecoderVideoRunable;
        if (recoderVideoRunable != null) {
            recoderVideoRunable.addFrame(bitmap);
        }
    }

    public void addRecoderFrame(byte[] bArr) {
        RecoderVideoRunable recoderVideoRunable = this.mRecoderVideoRunable;
        if (recoderVideoRunable != null) {
            recoderVideoRunable.addFrame(bArr);
        }
    }

    public void addRecoderFrame(WifiCameraPic wifiCameraPic) {
        RecoderVideoRunable recoderVideoRunable = this.mRecoderVideoRunable;
        if (recoderVideoRunable != null) {
            recoderVideoRunable.addFrame(wifiCameraPic);
        }
    }

    public void stopRecoder() {
        RecoderVideoRunable recoderVideoRunable = this.mRecoderVideoRunable;
        if (recoderVideoRunable != null) {
            recoderVideoRunable.recoderStop();
        }
    }

    public int getDuration() {
        RecoderVideoRunable recoderVideoRunable = this.mRecoderVideoRunable;
        if (recoderVideoRunable != null) {
            return recoderVideoRunable.getDuration();
        }
        return 0;
    }
}
