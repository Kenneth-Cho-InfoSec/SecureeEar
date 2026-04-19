package com.i4season.bkCamera.logicrelated.encoder;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import androidx.core.view.MotionEventCompat;
import com.i4season.bkCamera.logicrelated.encoder.biz.AACEncodeConsumer;
import com.i4season.bkCamera.logicrelated.encoder.biz.H264EncodeConsumer;
import com.i4season.bkCamera.logicrelated.encoder.biz.Mp4MediaMuxer;
import com.i4season.bkCamera.uirelated.other.logmanage.LogWD;
import java.util.Timer;
import java.util.TimerTask;

public class RecodeInstans {
    private int fps;
    private Bitmap fram;
    private long lmax;
    private AACEncodeConsumer mAacConsumer;
    private H264EncodeConsumer mH264Consumer;
    private Mp4MediaMuxer mMuxer;
    private MediaVideoBufferEncoder mVideoEncoder;
    private RecordParams paramstmp;
    private RecodeEndInterface recodeEndInterface;
    private int recoderHeight;
    private int recoderWidth;
    private long startRecordTime;
    private Timer timer;
    private String videoPath;
    private long vlen;
    private byte[] voiceBuffer;
    private Timer voiceTimer;
    private byte[] yuv;
    private boolean isRecoder = false;
    byte[] fillBuffer = new byte[1024];
    private int next = 0;

    public interface RecodeEndInterface {
        void initMediaCodecFaild(int i, int i2, int i3, int i4);

        void recoderEnd(String str);
    }

    public static RecodeInstans getInstance() {
        return RecoderInstanseWDHolder.logManager;
    }

    private static class RecoderInstanseWDHolder {
        public static RecodeInstans logManager = new RecodeInstans();

        private RecoderInstanseWDHolder() {
        }
    }

    public RecodeEndInterface getRecodeEndInterface() {
        return this.recodeEndInterface;
    }

    public long getStartRecordTime() {
        return this.startRecordTime;
    }

    public void startRecording(RecordParams recordParams, int i, int i2, int i3, RecodeEndInterface recodeEndInterface) {
        this.recoderWidth = i;
        this.recoderHeight = i2;
        this.fps = i3;
        this.recodeEndInterface = recodeEndInterface;
        Timer timer = this.timer;
        if (timer != null) {
            timer.cancel();
            this.timer = null;
        }
        this.timer = new Timer();
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (RecodeInstans.this.isRecoder && RecodeInstans.this.mH264Consumer != null && RecodeInstans.this.mH264Consumer.isEncoderStart()) {
                    if (RecodeInstans.this.fram != null) {
                        Bitmap bitmapCopy = RecodeInstans.this.fram.copy(RecodeInstans.this.fram.getConfig(), true);
                        RecodeInstans.this.fram = null;
                        int width = bitmapCopy.getWidth();
                        int height = bitmapCopy.getHeight();
                        if (width != RecodeInstans.this.mH264Consumer.getmWidth() || height != RecodeInstans.this.mH264Consumer.getmHeight()) {
                            width = RecodeInstans.this.mH264Consumer.getmWidth();
                            height = RecodeInstans.this.mH264Consumer.getmHeight();
                            bitmapCopy = RecodeInstans.this.zoomBitmapNoRecyled(bitmapCopy, width, height);
                        }
                        int i4 = width * height;
                        int[] iArr = new int[i4];
                        bitmapCopy.getPixels(iArr, 0, width, 0, 0, width, height);
                        RecodeInstans.this.yuv = new byte[(i4 * 3) / 2];
                        if (RecodeInstans.this.mH264Consumer == null) {
                            return;
                        }
                        if (RecodeInstans.this.mH264Consumer.getmColorFormat() == 21) {
                            RecodeInstans recodeInstans = RecodeInstans.this;
                            recodeInstans.encodeYUV420SP(recodeInstans.yuv, iArr, width, height);
                        } else {
                            RecodeInstans recodeInstans2 = RecodeInstans.this;
                            recodeInstans2.encodeYUV420P(recodeInstans2.yuv, iArr, width, height);
                        }
                        bitmapCopy.recycle();
                        if (!RecodeInstans.this.isRecoder || RecodeInstans.this.mH264Consumer == null) {
                            return;
                        }
                        RecodeInstans.this.mH264Consumer.setRawYuv(RecodeInstans.this.yuv, width, height);
                        return;
                    }
                    if (RecodeInstans.this.yuv != null) {
                        RecodeInstans.this.mH264Consumer.setRawYuv(RecodeInstans.this.yuv, RecodeInstans.this.mH264Consumer.getmWidth(), RecodeInstans.this.mH264Consumer.getmHeight());
                    }
                }
            }
        }, 0L, 1000 / i3);
        this.startRecordTime = System.currentTimeMillis();
        this.isRecoder = true;
        handleStartRecording(recordParams);
    }

    private void setBuffer() {
        for (int i = 0; i < 1024; i++) {
            this.fillBuffer[i] = 0;
        }
    }

    public void stopRecording() {
        this.isRecoder = false;
        this.startRecordTime = 0L;
        Timer timer = this.timer;
        if (timer != null) {
            timer.cancel();
            this.timer = null;
        }
        Timer timer2 = this.voiceTimer;
        if (timer2 != null) {
            timer2.cancel();
            this.voiceTimer = null;
        }
        new Thread(new Runnable() {
            @Override
            public final void run() {
                this.f$0.lambda$stopRecording$0$RecodeInstans();
            }
        }).start();
    }

    public void setFram(Bitmap bitmap) {
        this.fram = bitmap;
    }

    public void setVoicesBuffer(byte[] bArr) {
        AACEncodeConsumer aACEncodeConsumer;
        if (!this.isRecoder || (aACEncodeConsumer = this.mAacConsumer) == null) {
            return;
        }
        aACEncodeConsumer.setData(bArr);
    }

    private void handleStartRecording(RecordParams recordParams) {
        LogWD.writeMsg(this, 16777215, "handleStartRecording begin");
        if (this.mMuxer != null) {
            return;
        }
        if (recordParams == null) {
            throw new NullPointerException("RecordParams can not be null!");
        }
        this.lmax = 1604321280L;
        this.videoPath = recordParams.getRecordPath();
        this.vlen = 0L;
        this.paramstmp = recordParams;
        LogWD.writeMsg(this, 16777215, "videoPath =" + this.videoPath);
        if (this.next == 0) {
            this.mMuxer = new Mp4MediaMuxer(recordParams.getRecordPath(), recordParams.getRecordDuration() * 60 * 1000, recordParams.isVoiceClose());
        } else {
            this.mMuxer = new Mp4MediaMuxer(recordParams.getRecordPath() + "-" + this.next, recordParams.getRecordDuration() * 60 * 1000, recordParams.isVoiceClose());
        }
        LogWD.writeMsg(this, 16777215, "startVideoRecord begin" + this.videoPath);
        startVideoRecord();
        LogWD.writeMsg(this, 16777215, "startVideoRecord end" + this.videoPath);
        if (!recordParams.isVoiceClose()) {
            LogWD.writeMsg(this, 16777215, "startAudioRecord begin" + this.videoPath);
            startAudioRecord(recordParams.isExternalVoiceData());
            LogWD.writeMsg(this, 16777215, "startAudioRecord end" + this.videoPath);
        }
        LogWD.writeMsg(this, 16777215, "callOnStartRecording end" + this.videoPath);
    }

    private void reStartRecording(RecordParams recordParams) {
        int i = this.next;
        handleStopRecording1();
        this.next = i + 1;
        handleStartRecording(recordParams);
    }

    private void handleStopRecording1() {
        Mp4MediaMuxer mp4MediaMuxer = this.mMuxer;
        if (mp4MediaMuxer != null) {
            mp4MediaMuxer.release();
            this.mMuxer = null;
        }
        stopAudioRecord();
        stopVideoRecord();
        this.next = 0;
    }

    public void lambda$stopRecording$0$RecodeInstans() {
        Mp4MediaMuxer mp4MediaMuxer = this.mMuxer;
        if (mp4MediaMuxer != null) {
            mp4MediaMuxer.release();
            this.mMuxer = null;
        }
        stopAudioRecord();
        stopVideoRecord();
        this.next = 0;
        RecodeEndInterface recodeEndInterface = this.recodeEndInterface;
        if (recodeEndInterface != null) {
            recodeEndInterface.recoderEnd(this.videoPath + ".mp4");
        }
    }

    private void startVideoRecord() {
        this.mH264Consumer = new H264EncodeConsumer(this.recoderWidth, this.recoderHeight, this.fps);
        this.mH264Consumer.start();
        H264EncodeConsumer h264EncodeConsumer = this.mH264Consumer;
        if (h264EncodeConsumer != null) {
            h264EncodeConsumer.setTmpuMuxer(this.mMuxer);
        }
    }

    private void stopVideoRecord() {
        H264EncodeConsumer h264EncodeConsumer = this.mH264Consumer;
        if (h264EncodeConsumer != null) {
            h264EncodeConsumer.exit();
            this.mH264Consumer.setTmpuMuxer(null);
            try {
                H264EncodeConsumer h264EncodeConsumer2 = this.mH264Consumer;
                this.mH264Consumer = null;
                if (h264EncodeConsumer2 != null) {
                    h264EncodeConsumer2.interrupt();
                    h264EncodeConsumer2.join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void startAudioRecord(boolean z) {
        this.mAacConsumer = new AACEncodeConsumer(z);
        this.mAacConsumer.start();
        AACEncodeConsumer aACEncodeConsumer = this.mAacConsumer;
        if (aACEncodeConsumer != null) {
            aACEncodeConsumer.setTmpuMuxer(this.mMuxer);
        }
    }

    private void stopAudioRecord() {
        AACEncodeConsumer aACEncodeConsumer = this.mAacConsumer;
        if (aACEncodeConsumer != null) {
            aACEncodeConsumer.exit();
            this.mAacConsumer.setTmpuMuxer(null);
            try {
                AACEncodeConsumer aACEncodeConsumer2 = this.mAacConsumer;
                this.mAacConsumer = null;
                if (aACEncodeConsumer2 != null) {
                    aACEncodeConsumer2.interrupt();
                    aACEncodeConsumer2.join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void encodeYUV420SP(byte[] bArr, int[] iArr, int i, int i2) {
        int i3 = i * i2;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        while (i4 < i2) {
            int i7 = i3;
            int i8 = i6;
            int i9 = i5;
            int i10 = 0;
            while (i10 < i) {
                int i11 = iArr[i9];
                int i12 = (iArr[i9] & 16711680) >> 16;
                int i13 = (iArr[i9] & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
                int i14 = 255;
                int i15 = (iArr[i9] & 255) >> 0;
                int i16 = (((((i12 * 66) + (i13 * 129)) + (i15 * 25)) + 128) >> 8) + 16;
                int i17 = (((((i12 * (-38)) - (i13 * 74)) + (i15 * 112)) + 128) >> 8) + 128;
                int i18 = (((((i12 * 112) - (i13 * 94)) - (i15 * 18)) + 128) >> 8) + 128;
                int i19 = i8 + 1;
                if (i16 < 0) {
                    i16 = 0;
                } else if (i16 > 255) {
                    i16 = 255;
                }
                bArr[i8] = (byte) i16;
                if (i4 % 2 == 0 && i9 % 2 == 0) {
                    int i20 = i7 + 1;
                    if (i17 < 0) {
                        i17 = 0;
                    } else if (i17 > 255) {
                        i17 = 255;
                    }
                    bArr[i7] = (byte) i17;
                    i7 = i20 + 1;
                    if (i18 < 0) {
                        i14 = 0;
                    } else if (i18 <= 255) {
                        i14 = i18;
                    }
                    bArr[i20] = (byte) i14;
                }
                i9++;
                i10++;
                i8 = i19;
            }
            i4++;
            i5 = i9;
            i6 = i8;
            i3 = i7;
        }
    }

    public void encodeYUV420P(byte[] bArr, int[] iArr, int i, int i2) {
        int i3 = i * i2;
        int i4 = i3;
        int i5 = (i3 / 4) + i3;
        int i6 = 0;
        int i7 = 0;
        int i8 = 0;
        while (i6 < i2) {
            int i9 = i4;
            int i10 = i5;
            int i11 = i8;
            int i12 = i7;
            int i13 = 0;
            while (i13 < i) {
                int i14 = iArr[i12];
                int i15 = (iArr[i12] & 16711680) >> 16;
                int i16 = (iArr[i12] & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
                int i17 = 255;
                int i18 = (iArr[i12] & 255) >> 0;
                int i19 = (((((i15 * 66) + (i16 * 129)) + (i18 * 25)) + 128) >> 8) + 16;
                int i20 = (((((i15 * (-38)) - (i16 * 74)) + (i18 * 112)) + 128) >> 8) + 128;
                int i21 = (((((i15 * 112) - (i16 * 94)) - (i18 * 18)) + 128) >> 8) + 128;
                int i22 = i11 + 1;
                if (i19 < 0) {
                    i19 = 0;
                } else if (i19 > 255) {
                    i19 = 255;
                }
                bArr[i11] = (byte) i19;
                if (i6 % 2 == 0 && i12 % 2 == 0) {
                    int i23 = i10 + 1;
                    if (i21 < 0) {
                        i21 = 0;
                    } else if (i21 > 255) {
                        i21 = 255;
                    }
                    bArr[i10] = (byte) i21;
                    int i24 = i9 + 1;
                    if (i20 < 0) {
                        i17 = 0;
                    } else if (i20 <= 255) {
                        i17 = i20;
                    }
                    bArr[i9] = (byte) i17;
                    i9 = i24;
                    i10 = i23;
                }
                i12++;
                i13++;
                i11 = i22;
            }
            i6++;
            i7 = i12;
            i8 = i11;
            i5 = i10;
            i4 = i9;
        }
    }

    public Bitmap zoomBitmapNoRecyled(Bitmap bitmap, int i, int i2) {
        if (bitmap == null) {
            return bitmap;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(i / width, i2 / height);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }
}
