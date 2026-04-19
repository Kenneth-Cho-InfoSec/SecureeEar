package com.i4season.bkCamera.logicrelated.encoder.biz;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import com.i4season.bkCamera.logicrelated.encoder.RecodeInstans;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.Constant;
import com.i4season.bkCamera.uirelated.other.logmanage.LogWD;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;

public class H264EncodeConsumer extends Thread {
    private static final float BPP = 0.5f;
    private static final boolean DEBUG = false;
    private static final int FRAME_INTERVAL = 1;
    private static final int FRAME_RATE = 15;
    public static final int I420TYPE = 1;
    private static final String MIME_TYPE = "video/avc";
    public static final int NV12TYPE = 0;
    private static final String TAG = "liusheng";
    private static final int TIMES_OUT = 10000;
    private static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test2.h264";
    protected static int[] recognizedFormats = {21, 19};
    protected static int[] recognizedFormats2 = {20, 19, 39, 21};
    private int fps;
    private ByteBuffer[] inputBuffers;
    private boolean isAddKeyFrame;
    private boolean isEncoderStart;
    private boolean isExit;
    long lastPush;
    private OnH264EncodeResultListener listener;
    private int mColorFormat;
    public int mColorType;
    private MediaFormat mFormat;
    private int mHeight;
    private MediaCodec mMediaCodec;
    private WeakReference<Mp4MediaMuxer> mMuxerRef;
    private int mWidth;
    final int millisPerframe;
    private MediaFormat newFormat;
    private ByteBuffer[] outputBuffers;
    private BufferedOutputStream outputStream;

    public interface OnH264EncodeResultListener {
        void onEncodeResult(byte[] bArr, int i, int i2, long j);
    }

    public void setOnH264EncodeResultListener(OnH264EncodeResultListener onH264EncodeResultListener) {
        this.listener = onH264EncodeResultListener;
    }

    public H264EncodeConsumer(int i, int i2) {
        this.isExit = false;
        this.isEncoderStart = false;
        this.mColorType = 0;
        this.millisPerframe = 50;
        this.lastPush = 0L;
        this.isAddKeyFrame = false;
        this.fps = 30;
        this.mWidth = i;
        this.mHeight = i2;
    }

    public H264EncodeConsumer(int i, int i2, int i3) {
        this.isExit = false;
        this.isEncoderStart = false;
        this.mColorType = 0;
        this.millisPerframe = 50;
        this.lastPush = 0L;
        this.isAddKeyFrame = false;
        this.fps = 30;
        this.mWidth = i;
        this.mHeight = i2;
        this.fps = i3;
    }

    public synchronized void setTmpuMuxer(Mp4MediaMuxer mp4MediaMuxer) {
        this.mMuxerRef = new WeakReference<>(mp4MediaMuxer);
        Mp4MediaMuxer mp4MediaMuxer2 = this.mMuxerRef.get();
        if (mp4MediaMuxer2 != null && this.newFormat != null) {
            mp4MediaMuxer2.addTrack(this.newFormat, true);
        }
    }

    public void setRawYuv(byte[] bArr, int i, int i2) {
        if (this.isEncoderStart && this.mWidth == i && this.mHeight == i2) {
            try {
                if (this.lastPush == 0) {
                    this.lastPush = System.currentTimeMillis();
                }
                long jCurrentTimeMillis = System.currentTimeMillis() - this.lastPush;
                if (jCurrentTimeMillis >= 0) {
                    jCurrentTimeMillis = 50 - jCurrentTimeMillis;
                    if (jCurrentTimeMillis > 0) {
                        Thread.sleep(jCurrentTimeMillis / 2);
                    }
                }
                LogWD.writeMsg(this, 16777215, "feedMediaCodecData begin");
                if (bArr != null) {
                    feedMediaCodecData(bArr);
                }
                LogWD.writeMsg(this, 16777215, "feedMediaCodecData end");
                if (jCurrentTimeMillis > 0) {
                    Thread.sleep(jCurrentTimeMillis / 2);
                }
                this.lastPush = System.currentTimeMillis();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public byte[] nv12ToNV21_public(byte[] bArr, int i, int i2) {
        return nv12ToData(bArr, i, i2);
    }

    public void setRawYuv2(byte[] bArr, int i, int i2, long j) {
        if (this.isEncoderStart) {
            feedMediaCodecData2(bArr, j);
        }
    }

    private void feedMediaCodecData(byte[] bArr) {
        int iDequeueInputBuffer;
        ByteBuffer inputBuffer;
        if (this.isEncoderStart) {
            try {
                iDequeueInputBuffer = this.mMediaCodec.dequeueInputBuffer(0L);
            } catch (Exception e) {
                e.printStackTrace();
                LogWD.writeMsg(this, 16777215, "dequeueInputBuffer  Exception :  " + e.toString());
                iDequeueInputBuffer = -99;
            }
            if (iDequeueInputBuffer < 0) {
                if (iDequeueInputBuffer == -99) {
                    LogWD.writeMsg(this, 16777215, "bufferIndex == -99  编码器重启");
                    if (System.currentTimeMillis() - RecodeInstans.getInstance().getStartRecordTime() >= 2000 || !this.isEncoderStart) {
                        return;
                    }
                    this.isEncoderStart = false;
                    this.mMediaCodec.release();
                    resetParm();
                    reStartMediaCodec();
                    return;
                }
                return;
            }
            if (Build.VERSION.SDK_INT >= 21) {
                inputBuffer = this.mMediaCodec.getInputBuffer(iDequeueInputBuffer);
            } else {
                inputBuffer = this.inputBuffers[iDequeueInputBuffer];
            }
            inputBuffer.clear();
            inputBuffer.put(bArr);
            inputBuffer.clear();
            try {
                this.mMediaCodec.queueInputBuffer(iDequeueInputBuffer, 0, bArr.length, System.nanoTime() / 1000, 1);
            } catch (MediaCodec.CodecException e2) {
                LogWD.writeMsg(this, 16777215, "feedMediaCodecData  mMediaCodec.queueInputBuffer CodecException : " + e2.toString());
                e2.isRecoverable();
                if (e2.isRecoverable() || e2.isTransient() || !this.isEncoderStart) {
                    return;
                }
                this.isEncoderStart = false;
                this.mMediaCodec.release();
                resetParm();
                reStartMediaCodec();
            } catch (IllegalStateException e3) {
                LogWD.writeMsg(this, 16777215, "feedMediaCodecData  mMediaCodec.queueInputBuffer IllegalStateException : " + e3.toString());
            }
        }
    }

    private void feedMediaCodecData2(byte[] bArr, long j) {
        int iDequeueInputBuffer;
        ByteBuffer inputBuffer;
        if (this.isEncoderStart) {
            try {
                iDequeueInputBuffer = this.mMediaCodec.dequeueInputBuffer(0L);
            } catch (IllegalStateException e) {
                e.printStackTrace();
                iDequeueInputBuffer = -1;
            }
            if (iDequeueInputBuffer >= 0) {
                if (Build.VERSION.SDK_INT >= 21) {
                    inputBuffer = this.mMediaCodec.getInputBuffer(iDequeueInputBuffer);
                } else {
                    inputBuffer = this.inputBuffers[iDequeueInputBuffer];
                }
                inputBuffer.clear();
                inputBuffer.put(bArr);
                inputBuffer.clear();
                this.mMediaCodec.queueInputBuffer(iDequeueInputBuffer, 0, bArr.length, j, 1);
            }
        }
    }

    public void exit() {
        this.isExit = true;
    }

    @Override
    public void run() {
        int iDequeueOutputBuffer;
        ByteBuffer outputBuffer;
        byte[] bArr;
        boolean z;
        WeakReference<Mp4MediaMuxer> weakReference;
        Mp4MediaMuxer mp4MediaMuxer;
        Mp4MediaMuxer mp4MediaMuxer2;
        if (!this.isEncoderStart) {
            startMediaCodec();
        }
        try {
            Thread.sleep(200L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (!this.isEncoderStart) {
            try {
                LogWD.writeMsg(this, 16777215, "视频编码器未初始化成功  等待。。");
                Thread.sleep(100L);
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
        }
        while (!this.isExit) {
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            byte[] bArr2 = new byte[0];
            byte[] bArr3 = new byte[this.mWidth * this.mHeight];
            do {
                try {
                    iDequeueOutputBuffer = this.mMediaCodec.dequeueOutputBuffer(bufferInfo, Constant.BACKWARD_DEAFAULT_TIME);
                } catch (IllegalStateException unused) {
                    iDequeueOutputBuffer = -99;
                    try {
                        Thread.sleep(10L);
                    } catch (InterruptedException e3) {
                        e3.printStackTrace();
                    }
                }
                if (iDequeueOutputBuffer != -1) {
                    if (iDequeueOutputBuffer == -3) {
                        this.outputBuffers = this.mMediaCodec.getOutputBuffers();
                    } else if (iDequeueOutputBuffer == -2) {
                        synchronized (this) {
                            this.newFormat = this.mMediaCodec.getOutputFormat();
                            if (this.mMuxerRef != null && (mp4MediaMuxer2 = this.mMuxerRef.get()) != null) {
                                mp4MediaMuxer2.addTrack(this.newFormat, true);
                            }
                        }
                    } else if (iDequeueOutputBuffer >= 0) {
                        if (Build.VERSION.SDK_INT >= 21) {
                            outputBuffer = this.mMediaCodec.getOutputBuffer(iDequeueOutputBuffer);
                        } else {
                            outputBuffer = this.outputBuffers[iDequeueOutputBuffer];
                        }
                        outputBuffer.position(bufferInfo.offset);
                        outputBuffer.limit(bufferInfo.offset + bufferInfo.size);
                        if ((bufferInfo.flags & 2) != 0) {
                            z = (bufferInfo.flags & 1) != 0;
                            if (z) {
                                bArr = new byte[0];
                            } else {
                                bArr2 = new byte[bufferInfo.size];
                                outputBuffer.get(bArr2);
                                this.mMediaCodec.releaseOutputBuffer(iDequeueOutputBuffer, false);
                            }
                        } else {
                            bArr = bArr2;
                            z = false;
                        }
                        boolean z2 = z | ((bufferInfo.flags & 1) != 0);
                        int length = bArr.length + bufferInfo.size;
                        if (length > bArr3.length) {
                            bArr3 = new byte[length];
                        }
                        if (z2) {
                            System.arraycopy(bArr, 0, bArr3, 0, bArr.length);
                            outputBuffer.get(bArr3, bArr.length, bufferInfo.size);
                            OnH264EncodeResultListener onH264EncodeResultListener = this.listener;
                            if (onH264EncodeResultListener != null) {
                                onH264EncodeResultListener.onEncodeResult(bArr3, 0, bArr.length + bufferInfo.size, bufferInfo.presentationTimeUs / 1000);
                            }
                            WeakReference<Mp4MediaMuxer> weakReference2 = this.mMuxerRef;
                            if (weakReference2 != null) {
                                Mp4MediaMuxer mp4MediaMuxer3 = weakReference2.get();
                                if (mp4MediaMuxer3 != null) {
                                    mp4MediaMuxer3.pumpStream(outputBuffer, bufferInfo, true);
                                }
                                this.isAddKeyFrame = true;
                            }
                        } else {
                            outputBuffer.get(bArr3, 0, bufferInfo.size);
                            OnH264EncodeResultListener onH264EncodeResultListener2 = this.listener;
                            if (onH264EncodeResultListener2 != null) {
                                onH264EncodeResultListener2.onEncodeResult(bArr3, 0, bufferInfo.size, bufferInfo.presentationTimeUs / 1000);
                            }
                            if (this.isAddKeyFrame && (weakReference = this.mMuxerRef) != null && (mp4MediaMuxer = weakReference.get()) != null) {
                                mp4MediaMuxer.pumpStream(outputBuffer, bufferInfo, true);
                            }
                        }
                        this.mMediaCodec.releaseOutputBuffer(iDequeueOutputBuffer, false);
                        bArr2 = bArr;
                    }
                }
                if (!this.isExit) {
                }
            } while (this.isEncoderStart);
        }
        stopMediaCodec();
    }

    private void startMediaCodec() {
        LogWD.writeMsg(this, 16777215, "startMediaCodec begin");
        if (selectVideoCodec(MIME_TYPE) == null) {
            LogWD.writeMsg(this, 16777215, "startMediaCodec exit");
            Log.e(TAG, "Unable to find an appropriate codec for video/avc");
            return;
        }
        LogWD.writeMsg(this, 16777215, "startMediaCodec begin mWidth=" + this.mWidth + "mHeight=" + this.mHeight);
        Log.d(TAG, "startMediaCodec begin mWidth=" + this.mWidth + "mHeight=" + this.mHeight);
        MediaFormat mediaFormatCreateVideoFormat = MediaFormat.createVideoFormat(MIME_TYPE, this.mWidth, this.mHeight);
        LogWD.writeMsg(this, 16777215, "setInteger begin");
        mediaFormatCreateVideoFormat.setInteger("color-format", this.mColorFormat);
        mediaFormatCreateVideoFormat.setInteger("bitrate", calcBitRate());
        mediaFormatCreateVideoFormat.setInteger("frame-rate", this.fps);
        mediaFormatCreateVideoFormat.setInteger("i-frame-interval", 1);
        LogWD.writeMsg(this, 16777215, "setInteger end");
        try {
            LogWD.writeMsg(this, 16777215, "createEncoderByType begin");
            this.mMediaCodec = MediaCodec.createEncoderByType(MIME_TYPE);
            LogWD.writeMsg(this, 16777215, "createEncoderByType end");
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogWD.writeMsg(this, 16777215, "configure begin");
        try {
            Log.d(TAG, "mMediaCodec.configure ");
            this.mMediaCodec.configure(mediaFormatCreateVideoFormat, (Surface) null, (MediaCrypto) null, 1);
            LogWD.writeMsg(this, 16777215, "configure end");
            this.mMediaCodec.start();
            LogWD.writeMsg(this, 16777215, "start end");
            this.isEncoderStart = true;
            LogWD.writeMsg(this, 16777215, "VERSION" + Build.VERSION.SDK_INT);
            if (Build.VERSION.SDK_INT >= 22) {
                LogWD.writeMsg(this, 16777215, "to null" + Build.VERSION.SDK_INT);
                this.outputBuffers = null;
                this.inputBuffers = null;
            } else {
                LogWD.writeMsg(this, 16777215, "to get buf" + Build.VERSION.SDK_INT);
                this.inputBuffers = this.mMediaCodec.getInputBuffers();
                this.outputBuffers = this.mMediaCodec.getOutputBuffers();
            }
            LogWD.writeMsg(this, 16777215, "start end");
            Bundle bundle = new Bundle();
            bundle.putInt("request-sync", 0);
            if (Build.VERSION.SDK_INT >= 19) {
                LogWD.writeMsg(this, 16777215, "setParameters begin");
                this.mMediaCodec.setParameters(bundle);
                LogWD.writeMsg(this, 16777215, "setParameters end");
            }
        } catch (Exception unused) {
            Log.d(TAG, "reStartMediaCodec ");
            resetParm();
            reStartMediaCodec();
        }
    }

    private void reStartMediaCodec() {
        LogWD.writeMsg(this, 16777215, "startMediaCodec begin");
        LogWD.writeMsg(this, 16777215, "startMediaCodec begin mWidth=" + this.mWidth + "mHeight=" + this.mHeight);
        Log.d(TAG, "reStartMediaCodec begin mWidth=" + this.mWidth + "mHeight=" + this.mHeight);
        MediaFormat mediaFormatCreateVideoFormat = MediaFormat.createVideoFormat(MIME_TYPE, this.mWidth, this.mHeight);
        LogWD.writeMsg(this, 16777215, "setInteger begin");
        mediaFormatCreateVideoFormat.setInteger("color-format", this.mColorFormat);
        mediaFormatCreateVideoFormat.setInteger("bitrate", calcBitRate());
        mediaFormatCreateVideoFormat.setInteger("frame-rate", this.fps);
        mediaFormatCreateVideoFormat.setInteger("i-frame-interval", 1);
        LogWD.writeMsg(this, 16777215, "setInteger end");
        try {
            LogWD.writeMsg(this, 16777215, "createEncoderByType begin");
            this.mMediaCodec = MediaCodec.createEncoderByType(MIME_TYPE);
            LogWD.writeMsg(this, 16777215, "createEncoderByType end");
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogWD.writeMsg(this, 16777215, "configure begin");
        this.mMediaCodec.configure(mediaFormatCreateVideoFormat, (Surface) null, (MediaCrypto) null, 1);
        LogWD.writeMsg(this, 16777215, "configure end");
        this.mMediaCodec.start();
        LogWD.writeMsg(this, 16777215, "start end");
        this.isEncoderStart = true;
        LogWD.writeMsg(this, 16777215, "VERSION" + Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT >= 22) {
            LogWD.writeMsg(this, 16777215, "to null" + Build.VERSION.SDK_INT);
            this.outputBuffers = null;
            this.inputBuffers = null;
        } else {
            LogWD.writeMsg(this, 16777215, "to get buf" + Build.VERSION.SDK_INT);
            this.inputBuffers = this.mMediaCodec.getInputBuffers();
            this.outputBuffers = this.mMediaCodec.getOutputBuffers();
        }
        LogWD.writeMsg(this, 16777215, "start end");
        Bundle bundle = new Bundle();
        bundle.putInt("request-sync", 0);
        if (Build.VERSION.SDK_INT >= 19) {
            LogWD.writeMsg(this, 16777215, "setParameters begin");
            this.mMediaCodec.setParameters(bundle);
            LogWD.writeMsg(this, 16777215, "setParameters end");
        }
    }

    private void stopMediaCodec() {
        this.isEncoderStart = false;
        MediaCodec mediaCodec = this.mMediaCodec;
        if (mediaCodec != null) {
            mediaCodec.stop();
            this.mMediaCodec.release();
            Log.d(TAG, "关闭视频编码器");
        }
    }

    private int calcBitRate() {
        int i = (int) (this.mWidth * 7.5f * this.mHeight);
        Log.i(TAG, String.format("bitrate=%5.2f[Mbps]", Float.valueOf((i / 1024.0f) / 1024.0f)));
        return i;
    }

    protected final MediaCodecInfo selectVideoCodec(String str) {
        int codecCount = MediaCodecList.getCodecCount();
        LogWD.writeMsg(this, 16777215, "getCodecCount numCodecs=" + codecCount);
        for (int i = 0; i < codecCount; i++) {
            LogWD.writeMsg(this, 16777215, "i=" + i);
            MediaCodecInfo codecInfoAt = MediaCodecList.getCodecInfoAt(i);
            LogWD.writeMsg(this, 16777215, "codecInfo=" + codecInfoAt);
            if (codecInfoAt.isEncoder()) {
                String[] supportedTypes = codecInfoAt.getSupportedTypes();
                LogWD.writeMsg(this, 16777215, "types=" + supportedTypes);
                for (int i2 = 0; i2 < supportedTypes.length; i2++) {
                    LogWD.writeMsg(this, 16777215, "typesJ=" + supportedTypes[i2]);
                    if (supportedTypes[i2].equalsIgnoreCase(str)) {
                        int iSelectColorFormat = selectColorFormat(codecInfoAt, str);
                        LogWD.writeMsg(this, 16777215, "format=" + iSelectColorFormat + "COLOR_FormatYUV420SemiPlanar=21");
                        if (iSelectColorFormat > 0) {
                            this.mColorFormat = iSelectColorFormat;
                            int i3 = this.mColorFormat;
                            if (i3 == 19 || i3 == 20) {
                                this.mColorType = 1;
                            } else {
                                this.mColorType = 0;
                            }
                            return codecInfoAt;
                        }
                    }
                }
            }
        }
        return null;
    }

    protected static final int selectColorFormat(MediaCodecInfo mediaCodecInfo, String str) {
        int i;
        int i2;
        try {
            Thread.currentThread().setPriority(10);
            MediaCodecInfo.CodecCapabilities capabilitiesForType = mediaCodecInfo.getCapabilitiesForType(str);
            Thread.currentThread().setPriority(5);
            int i3 = 0;
            int i4 = 0;
            while (true) {
                if (i4 >= capabilitiesForType.colorFormats.length) {
                    i = 0;
                    break;
                }
                i = capabilitiesForType.colorFormats[i4];
                if (isRecognizedViewoFormat(i)) {
                    break;
                }
                i4++;
            }
            if (i == 0) {
                while (true) {
                    if (i3 >= capabilitiesForType.colorFormats.length) {
                        break;
                    }
                    i2 = capabilitiesForType.colorFormats[i3];
                    if (!isRecognizedViewoFormat2(i2)) {
                        i3++;
                    } else if (i != 0) {
                        break;
                    }
                }
                i2 = i;
            } else {
                i2 = i;
            }
            if (i2 == 0) {
                Log.e(TAG, "couldn't find a good color format for " + mediaCodecInfo.getName() + " / " + str);
            }
            return i2;
        } catch (Throwable th) {
            Thread.currentThread().setPriority(5);
            throw th;
        }
    }

    private static final boolean isRecognizedViewoFormat(int i) {
        int[] iArr = recognizedFormats;
        int length = iArr != null ? iArr.length : 0;
        for (int i2 = 0; i2 < length; i2++) {
            if (recognizedFormats[i2] == i) {
                return true;
            }
        }
        return false;
    }

    private static final boolean isRecognizedViewoFormat2(int i) {
        int[] iArr = recognizedFormats2;
        int length = iArr != null ? iArr.length : 0;
        for (int i2 = 0; i2 < length; i2++) {
            if (recognizedFormats2[i2] == i) {
                return true;
            }
        }
        return false;
    }

    private byte[] nv21ToI420(byte[] bArr, int i, int i2) {
        int i3 = i * i2;
        byte[] bArr2 = new byte[(i3 * 3) / 2];
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr2, 0, i3);
        int i4 = i3 / 4;
        ByteBuffer byteBufferWrap2 = ByteBuffer.wrap(bArr2, i3, i4);
        ByteBuffer byteBufferWrap3 = ByteBuffer.wrap(bArr2, i3 + i4, i4);
        byteBufferWrap.put(bArr, 0, i3);
        while (i3 < bArr.length) {
            byteBufferWrap3.put(bArr[i3]);
            byteBufferWrap2.put(bArr[i3 + 1]);
            i3 += 2;
        }
        return bArr2;
    }

    private byte[] nv12ToI420(byte[] bArr, int i, int i2) {
        int i3 = i * i2;
        byte[] bArr2 = new byte[(i3 * 3) / 2];
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr2, 0, i3);
        int i4 = i3 / 4;
        ByteBuffer byteBufferWrap2 = ByteBuffer.wrap(bArr2, i3, i4);
        ByteBuffer byteBufferWrap3 = ByteBuffer.wrap(bArr2, i3 + i4, i4);
        byteBufferWrap.put(bArr, 0, i3);
        while (i3 < bArr.length) {
            byteBufferWrap2.put(bArr[i3]);
            byteBufferWrap3.put(bArr[i3 + 1]);
            i3 += 2;
        }
        return bArr2;
    }

    private byte[] nv12ToNv21(byte[] bArr, int i, int i2) {
        int i3 = i * i2;
        byte[] bArr2 = new byte[(i3 * 3) / 2];
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr2, 0, i3);
        int i4 = i3 / 4;
        ByteBuffer byteBufferWrap2 = ByteBuffer.wrap(bArr2, i3, i4);
        ByteBuffer byteBufferWrap3 = ByteBuffer.wrap(bArr2, i3 + i4, i4);
        byteBufferWrap.put(bArr, 0, i3);
        while (i3 < bArr.length) {
            byteBufferWrap2.put(bArr[i3]);
            byteBufferWrap3.put(bArr[i3 + 1]);
            i3 += 2;
        }
        return bArr2;
    }

    private byte[] nv21ToNV12(byte[] bArr, int i, int i2) {
        int i3 = i * i2;
        byte[] bArr2 = new byte[(i3 * 3) / 2];
        System.arraycopy(bArr, 0, bArr2, 0, i3);
        while (i3 < bArr.length) {
            int i4 = i3 + 1;
            bArr2[i4] = bArr[i4];
            bArr2[i3] = bArr[i3];
            i3 += 2;
        }
        return bArr2;
    }

    private byte[] nv12ToNV21(byte[] bArr, int i, int i2) {
        int i3 = i * i2;
        byte[] bArr2 = new byte[(i3 * 3) / 2];
        System.arraycopy(bArr, 0, bArr2, 0, i3);
        while (i3 < bArr.length) {
            int i4 = i3 + 1;
            bArr2[i3] = bArr[i4];
            bArr2[i4] = bArr[i3];
            i3 += 2;
        }
        return bArr2;
    }

    public byte[] NV12toI420(byte[] bArr, int i, int i2) {
        int i3 = i * i2;
        int i4 = i3 / 4;
        byte[] bArr2 = new byte[(i4 * 2) + i3];
        System.arraycopy(bArr, 0, bArr2, 0, i3);
        for (int i5 = 0; i5 < i4; i5++) {
            int i6 = (i5 * 2) + i3;
            bArr2[i3 + i5] = bArr[i6 + 1];
            bArr2[i3 + i4 + i5] = bArr[i6];
        }
        return bArr2;
    }

    private byte[] nv12ToData(byte[] bArr, int i, int i2) {
        if (!this.isEncoderStart) {
            LogWD.writeMsg(this, 16777215, "notready");
            return null;
        }
        if (this.mColorType == 0) {
            LogWD.writeMsg(this, 16777215, "NV12TYPE type");
            return nv12ToNV21(bArr, i, i2);
        }
        LogWD.writeMsg(this, 16777215, "not NV12TYPE type");
        return NV12toI420(bArr, i, i2);
    }

    public int getmColorType() {
        return this.mColorType;
    }

    public int getmColorFormat() {
        return this.mColorFormat;
    }

    public int getmWidth() {
        return this.mWidth;
    }

    public int getmHeight() {
        return this.mHeight;
    }

    public boolean isEncoderStart() {
        return this.isEncoderStart;
    }

    private void resetParm() {
        int i = this.mWidth;
        int i2 = this.mHeight;
        if (i == i2) {
            if (i > 1080) {
                this.mWidth = 1080;
                this.mHeight = 1080;
                if (RecodeInstans.getInstance().getRecodeEndInterface() != null) {
                    RecodeInstans.getInstance().getRecodeEndInterface().initMediaCodecFaild(this.mWidth, this.mHeight, 1080, 1080);
                    return;
                }
                return;
            }
            this.mWidth = 720;
            this.mHeight = 720;
            if (RecodeInstans.getInstance().getRecodeEndInterface() != null) {
                RecodeInstans.getInstance().getRecodeEndInterface().initMediaCodecFaild(this.mWidth, this.mHeight, 720, 720);
                return;
            }
            return;
        }
        if (i >= 2500 || i2 == 1440) {
            if (RecodeInstans.getInstance().getRecodeEndInterface() != null) {
                RecodeInstans.getInstance().getRecodeEndInterface().initMediaCodecFaild(this.mWidth, this.mHeight, 1920, 1080);
            }
            this.mWidth = 1920;
            this.mHeight = 1080;
            return;
        }
        if (i2 >= 2500 || i == 1440) {
            if (RecodeInstans.getInstance().getRecodeEndInterface() != null) {
                RecodeInstans.getInstance().getRecodeEndInterface().initMediaCodecFaild(this.mWidth, this.mHeight, 1080, 1920);
            }
            this.mWidth = 1080;
            this.mHeight = 1920;
        }
    }
}
