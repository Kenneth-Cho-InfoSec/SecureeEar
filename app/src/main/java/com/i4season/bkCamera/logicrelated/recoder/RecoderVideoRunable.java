package com.i4season.bkCamera.logicrelated.recoder;

import android.graphics.Bitmap;
import android.util.Log;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.ImgUtil;
import com.i4season.bkCamera.uirelated.other.logmanage.LogWD;
import com.jni.WifiCameraInfo.WifiCameraPic;
import com.libyuv.util.YuvUtil;
import java.io.File;
import java.util.LinkedList;

public class RecoderVideoRunable implements Runnable {
    private static final String TAG = "RecoderVideoRunable";
    private int frameRate;
    private boolean isOval;
    private AudioEncoder mAudioEncoder;
    private long mFrameTotal;
    private int mHeight;
    private boolean mIsAudioRecoder;
    private boolean mIsCircle;
    private RecoderDelegate mRecoderDelegate;
    private String mVideoFilePath;
    private VideoMediaCoder mVideoMediaCoder;
    private int mWidth;
    private int newHeight;
    private int newWidth;
    private final int bitrate = 350000;
    private boolean mRecoderStop = false;
    private LinkedList<Bitmap> mFrameList = new LinkedList<>();
    private LinkedList<byte[]> mbyteList = new LinkedList<>();

    public interface RecoderDelegate {
        void onRecoderAddFrameFinish(boolean z);
    }

    public RecoderVideoRunable(int i, int i2, int i3, int i4, int i5, boolean z, boolean z2, boolean z3, String str, RecoderDelegate recoderDelegate) {
        this.frameRate = 16;
        this.mIsAudioRecoder = false;
        this.mFrameTotal = 0L;
        if (z) {
            i = Math.min(i, i2);
            i2 = Math.min(i, i2);
        }
        this.mFrameTotal = 0L;
        this.mWidth = i;
        this.mHeight = i2;
        this.newWidth = i3;
        this.newHeight = i4;
        this.mIsCircle = z;
        this.isOval = z2;
        this.mIsAudioRecoder = z3;
        this.mVideoFilePath = str;
        this.mRecoderDelegate = recoderDelegate;
        if (i5 > 0) {
            this.frameRate = i5;
        }
    }

    @Override
    public void run() {
        try {
            this.mVideoMediaCoder = new VideoMediaCoder();
            this.mAudioEncoder = new AudioEncoder();
            initEncoder(this.mWidth, this.mHeight, this.newWidth, this.newHeight, 350000, this.frameRate);
            naStartRecord();
            while (true) {
                if (this.mRecoderStop && this.mFrameList.size() == 0) {
                    closeEncoder();
                    this.mRecoderDelegate.onRecoderAddFrameFinish(true);
                    return;
                }
                if (this.mFrameList.size() > 0) {
                    LogWD.writeMsg(this, 16, "recoder: encodeImage mIsCircle: " + this.mIsCircle);
                    Bitmap first = this.mFrameList.getFirst();
                    if (this.mIsCircle) {
                        offerEncoder(ImgUtil.toRoundBitmapMask(first), this.mWidth, this.mHeight, 0);
                    } else {
                        offerEncoder(first, this.mWidth, this.mHeight, 0);
                    }
                    this.mFrameList.removeFirst();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            closeEncoder();
            this.mRecoderDelegate.onRecoderAddFrameFinish(false);
        }
    }

    private int initEncoder(int i, int i2, int i3, int i4, int i5, int i6) {
        String str = this.mVideoFilePath + "_.tmp";
        if (str != null && str.length() > 10) {
            MyMediaMuxer.start(str);
        }
        if (i != i3 || i2 != i4) {
            YuvUtil.init(i, i2, i3, i4);
        }
        return this.mVideoMediaCoder.initMediaCodec(i3, i4, i5, i6);
    }

    private void offerEncoder(Bitmap bitmap, int i, int i2, int i3) {
        VideoMediaCoder videoMediaCoder = this.mVideoMediaCoder;
        if (videoMediaCoder != null) {
            videoMediaCoder.offerEncoder(bitmap, i, i2, i3);
        }
    }

    private void offerEncoder(byte[] bArr, int i, int i2, int i3) {
        VideoMediaCoder videoMediaCoder = this.mVideoMediaCoder;
        if (videoMediaCoder != null) {
            videoMediaCoder.offerEncoder(bArr, i, i2, i3);
        }
    }

    private void closeEncoder() {
        Log.d("liusheng", "结束录制");
        VideoMediaCoder videoMediaCoder = this.mVideoMediaCoder;
        if (videoMediaCoder != null) {
            videoMediaCoder.F_CloseEncoder();
        }
        MyMediaMuxer.stop();
        G_StartAudio(0);
        String str = this.mVideoFilePath;
        if (str != null && str.length() >= 10) {
            File file = new File(this.mVideoFilePath + "_.tmp");
            File file2 = new File(this.mVideoFilePath);
            if (file2.exists() && file2.isFile()) {
                try {
                    file2.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (file.exists() && file.isFile()) {
                file.renameTo(file2);
            }
            String.format("%02d%s", 1, this.mVideoFilePath);
            this.mVideoFilePath = "";
            LogWD.writeMsg(this, 16, "recoder: end");
        }
    }

    private void naStartRecord() {
        boolean z = this.mIsAudioRecoder;
        MyMediaMuxer.mIsAudioRecoder = z;
        if (z && !G_StartAudio(1)) {
            this.mIsAudioRecoder = false;
        }
        if (this.mIsAudioRecoder) {
            return;
        }
        G_StartAudio(0);
    }

    private boolean G_StartAudio(int i) {
        if (i != 0) {
            return this.mAudioEncoder.start();
        }
        this.mAudioEncoder.stop();
        return true;
    }

    public void addFrame(Bitmap bitmap) {
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_4444);
        }
        this.mFrameList.addLast(bitmap);
    }

    public void addFrame(byte[] bArr) {
        if (bArr == null) {
            return;
        }
        this.mbyteList.add(bArr);
    }

    public void addFrame(WifiCameraPic wifiCameraPic) {
        LogWD.writeMsg(this, 16, "recoder: addFrame");
    }

    public void recoderStop() {
        LogWD.writeMsg(this, 16, "recoder: recoderStop");
        this.mRecoderStop = true;
    }

    public int getDuration() {
        return (int) (this.mFrameTotal / ((long) this.frameRate));
    }
}
