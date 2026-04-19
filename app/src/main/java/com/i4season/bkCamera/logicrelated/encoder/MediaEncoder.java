package com.i4season.bkCamera.logicrelated.encoder;

import android.media.MediaCodec;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.Constant;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;

public abstract class MediaEncoder implements Runnable {
    public static final int[] AUDIO_SAMPLING_RATES = {96000, 88200, 64000, 48000, 44100, 32000, 24000, 22050, 16000, 12000, 11025, 8000, 7350, -1, -1, -1};
    private static final boolean DEBUG = true;
    protected static final int MSG_FRAME_AVAILABLE = 1;
    protected static final int MSG_STOP_RECORDING = 9;
    private static final String TAG = "MediaEncoder";
    protected static final int TIMEOUT_USEC = 10000;
    public static final int TYPE_AUDIO = 0;
    public static final int TYPE_VIDEO = 1;
    private boolean isExit;
    private long lastPush;
    private MediaCodec.BufferInfo mBufferInfo;
    protected volatile boolean mIsCapturing;
    protected boolean mIsEOS;
    protected final MediaEncoderListener mListener;
    protected MediaCodec mMediaCodec;
    protected boolean mMuxerStarted;
    private int mRequestDrain;
    protected volatile boolean mRequestStop;
    protected int mTrackIndex;
    protected final WeakReference<MediaMuxerWrapper> mWeakMuxer;
    private long millisPerframe;
    protected final Object mSync = new Object();
    ByteBuffer mBuffer = ByteBuffer.allocate(10240);
    private long prevOutputPTSUs = 0;

    public interface MediaEncoderListener {
        void onEncodeResult(byte[] bArr, int i, int i2, long j, int i3);

        void onPrepared(MediaEncoder mediaEncoder);

        void onStopped(MediaEncoder mediaEncoder);
    }

    abstract void prepare() throws IOException;

    public MediaEncoder(MediaMuxerWrapper mediaMuxerWrapper, MediaEncoderListener mediaEncoderListener) {
        if (mediaEncoderListener == null) {
            throw new NullPointerException("MediaEncoderListener is null");
        }
        if (mediaMuxerWrapper == null) {
            throw new NullPointerException("MediaMuxerWrapper is null");
        }
        this.mWeakMuxer = new WeakReference<>(mediaMuxerWrapper);
        mediaMuxerWrapper.addEncoder(this);
        this.mListener = mediaEncoderListener;
        synchronized (this.mSync) {
            this.mBufferInfo = new MediaCodec.BufferInfo();
            new Thread(this, getClass().getSimpleName()).start();
            try {
                this.mSync.wait();
            } catch (InterruptedException unused) {
            }
        }
    }

    public String getOutputPath() {
        MediaMuxerWrapper mediaMuxerWrapper = this.mWeakMuxer.get();
        if (mediaMuxerWrapper != null) {
            return mediaMuxerWrapper.getOutputPath();
        }
        return null;
    }

    public boolean frameAvailableSoon() {
        synchronized (this.mSync) {
            if (this.mIsCapturing && !this.mRequestStop) {
                this.mRequestDrain++;
                this.mSync.notifyAll();
                return true;
            }
            return false;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:52:0x0064 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @Override
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void run() {
        boolean z;
        boolean z2;
        synchronized (this.mSync) {
            this.mRequestStop = false;
            this.mRequestDrain = 0;
            this.mSync.notify();
        }
        FileUtils.createfile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test222.h264");
        while (true) {
            synchronized (this.mSync) {
                z = this.mRequestStop;
                z2 = this.mRequestDrain > 0;
                if (z2) {
                    this.mRequestDrain--;
                }
            }
            if (z) {
                break;
            }
            if (z2) {
                drain();
            } else {
                synchronized (this.mSync) {
                    try {
                        try {
                            this.mSync.wait();
                        } catch (InterruptedException unused) {
                            synchronized (this.mSync) {
                            }
                        }
                    } finally {
                    }
                }
            }
            synchronized (this.mSync) {
                this.mRequestStop = true;
                this.mIsCapturing = false;
            }
            FileUtils.releaseFile();
            return;
        }
        drain();
        signalEndOfInputStream();
        drain();
        release();
        synchronized (this.mSync) {
        }
    }

    void startRecording() {
        Log.v(TAG, "startRecording");
        synchronized (this.mSync) {
            this.mIsCapturing = true;
            this.mRequestStop = false;
            this.isExit = false;
            this.mSync.notifyAll();
        }
    }

    void stopRecording() {
        Log.v(TAG, "stopRecording");
        synchronized (this.mSync) {
            if (this.mIsCapturing && !this.mRequestStop) {
                this.mRequestStop = true;
                this.isExit = true;
                this.mSync.notifyAll();
            }
        }
    }

    protected void release() {
        MediaMuxerWrapper mediaMuxerWrapper;
        Log.d(TAG, "release:");
        try {
            this.mListener.onStopped(this);
        } catch (Exception e) {
            Log.e(TAG, "failed onStopped", e);
        }
        this.mIsCapturing = false;
        MediaCodec mediaCodec = this.mMediaCodec;
        if (mediaCodec != null) {
            try {
                mediaCodec.stop();
                this.mMediaCodec.release();
                this.mMediaCodec = null;
            } catch (Exception e2) {
                Log.e(TAG, "failed releasing MediaCodec", e2);
            }
        }
        if (this.mMuxerStarted && (mediaMuxerWrapper = this.mWeakMuxer.get()) != null) {
            try {
                mediaMuxerWrapper.stop();
            } catch (Exception e3) {
                Log.e(TAG, "failed stopping muxer", e3);
            }
        }
        this.mBufferInfo = null;
    }

    protected void signalEndOfInputStream() {
        Log.d(TAG, "sending EOS to encoder");
        encode((byte[]) null, 0, getPTSUs());
    }

    protected void encode(byte[] bArr, int i, long j) {
        if (this.mIsCapturing) {
            int i2 = 0;
            ByteBuffer[] inputBuffers = this.mMediaCodec.getInputBuffers();
            while (this.mIsCapturing && i2 < i) {
                int iDequeueInputBuffer = this.mMediaCodec.dequeueInputBuffer(Constant.BACKWARD_DEAFAULT_TIME);
                if (iDequeueInputBuffer >= 0) {
                    ByteBuffer byteBuffer = inputBuffers[iDequeueInputBuffer];
                    byteBuffer.clear();
                    int iRemaining = byteBuffer.remaining();
                    if (i2 + iRemaining >= i) {
                        iRemaining = i - i2;
                    }
                    int i3 = iRemaining;
                    if (i3 > 0 && bArr != null) {
                        byteBuffer.put(bArr, i2, i3);
                    }
                    i2 += i3;
                    if (i <= 0) {
                        this.mIsEOS = true;
                        Log.i(TAG, "send BUFFER_FLAG_END_OF_STREAM");
                        this.mMediaCodec.queueInputBuffer(iDequeueInputBuffer, 0, 0, j, 4);
                        return;
                    }
                    this.mMediaCodec.queueInputBuffer(iDequeueInputBuffer, 0, i3, j, 0);
                }
            }
        }
    }

    protected void encode(ByteBuffer byteBuffer, int i) {
        int iDequeueInputBuffer;
        ByteBuffer inputBuffer;
        if (this.mIsCapturing) {
            try {
                if (this.lastPush == 0) {
                    this.lastPush = System.currentTimeMillis();
                }
                long jCurrentTimeMillis = System.currentTimeMillis() - this.lastPush;
                if (jCurrentTimeMillis >= 0) {
                    long j = this.millisPerframe - jCurrentTimeMillis;
                    if (j > 0) {
                        Thread.sleep(j / 2);
                    }
                }
                ByteBuffer[] inputBuffers = this.mMediaCodec.getInputBuffers();
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
                        inputBuffer = inputBuffers[iDequeueInputBuffer];
                    }
                    byte[] bArr = new byte[byteBuffer.capacity()];
                    byteBuffer.get(bArr);
                    inputBuffer.clear();
                    inputBuffer.put(bArr);
                    inputBuffer.clear();
                    this.mMediaCodec.queueInputBuffer(iDequeueInputBuffer, 0, bArr.length, System.nanoTime() / 1000, 1);
                }
                this.lastPush = System.currentTimeMillis();
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
        }
    }

    protected void encode(ByteBuffer byteBuffer, int i, long j) {
        if (this.mIsCapturing) {
            int i2 = 0;
            ByteBuffer[] inputBuffers = this.mMediaCodec.getInputBuffers();
            while (this.mIsCapturing && i2 < i) {
                int iDequeueInputBuffer = this.mMediaCodec.dequeueInputBuffer(Constant.BACKWARD_DEAFAULT_TIME);
                if (iDequeueInputBuffer >= 0) {
                    ByteBuffer byteBuffer2 = inputBuffers[iDequeueInputBuffer];
                    byteBuffer2.clear();
                    int iRemaining = byteBuffer2.remaining();
                    if (i2 + iRemaining >= i) {
                        iRemaining = i - i2;
                    }
                    int i3 = iRemaining;
                    if (i3 > 0 && byteBuffer != null) {
                        byteBuffer.position(i2 + i3);
                        byteBuffer.flip();
                        byteBuffer2.put(byteBuffer);
                    }
                    i2 += i3;
                    if (i <= 0) {
                        this.mIsEOS = true;
                        Log.i(TAG, "send BUFFER_FLAG_END_OF_STREAM");
                        this.mMediaCodec.queueInputBuffer(iDequeueInputBuffer, 0, 0, j, 4);
                        return;
                    }
                    this.mMediaCodec.queueInputBuffer(iDequeueInputBuffer, 0, i3, j, 0);
                }
            }
        }
    }

    protected void drain() {
        int iDequeueOutputBuffer;
        ByteBuffer outputBuffer;
        boolean z;
        MediaCodec mediaCodec = this.mMediaCodec;
        if (mediaCodec == null) {
            return;
        }
        ByteBuffer[] outputBuffers = mediaCodec.getOutputBuffers();
        MediaMuxerWrapper mediaMuxerWrapper = this.mWeakMuxer.get();
        if (mediaMuxerWrapper == null) {
            Log.w(TAG, "muxer is unexpectedly null");
            return;
        }
        byte[] bArr = new byte[307200];
        byte[] bArr2 = new byte[0];
        ByteBuffer[] outputBuffers2 = outputBuffers;
        while (true) {
            int i = 0;
            while (this.mIsCapturing) {
                iDequeueOutputBuffer = this.mMediaCodec.dequeueOutputBuffer(this.mBufferInfo, Constant.BACKWARD_DEAFAULT_TIME);
                if (iDequeueOutputBuffer == -1) {
                    if (!this.mIsEOS && (i = i + 1) > 5) {
                        return;
                    }
                } else if (iDequeueOutputBuffer == -3) {
                    outputBuffers2 = this.mMediaCodec.getOutputBuffers();
                } else {
                    if (iDequeueOutputBuffer == -2) {
                        if (this.mMuxerStarted) {
                            throw new RuntimeException("format changed twice");
                        }
                        this.mTrackIndex = mediaMuxerWrapper.addTrack(this.mMediaCodec.getOutputFormat());
                        this.mMuxerStarted = true;
                        if (mediaMuxerWrapper.start()) {
                            continue;
                        } else {
                            synchronized (mediaMuxerWrapper) {
                                while (!mediaMuxerWrapper.isStarted()) {
                                    try {
                                        mediaMuxerWrapper.wait(100L);
                                    } catch (InterruptedException unused) {
                                    }
                                }
                            }
                        }
                    } else if (iDequeueOutputBuffer < 0) {
                        Log.w(TAG, "drain:unexpected result from encoder#dequeueOutputBuffer: " + iDequeueOutputBuffer);
                    } else {
                        if (Build.VERSION.SDK_INT >= 21) {
                            outputBuffer = this.mMediaCodec.getOutputBuffer(iDequeueOutputBuffer);
                        } else {
                            outputBuffer = outputBuffers2[iDequeueOutputBuffer];
                        }
                        outputBuffer.position(this.mBufferInfo.offset);
                        outputBuffer.limit(this.mBufferInfo.offset + this.mBufferInfo.size);
                        if (outputBuffer == null) {
                            throw new RuntimeException("encoderOutputBuffer " + iDequeueOutputBuffer + " was null");
                        }
                        if ((this.mBufferInfo.flags & 2) != 0) {
                            Log.d(TAG, "drain:BUFFER_FLAG_CODEC_CONFIG");
                            this.mBufferInfo.size = 0;
                        }
                        if ((this.mBufferInfo.flags & 4) != 0) {
                            this.mIsCapturing = false;
                            this.mMuxerStarted = false;
                            return;
                        }
                        if (this.mBufferInfo.size != 0) {
                            if (!this.mMuxerStarted) {
                                throw new RuntimeException("drain:muxer hasn't started");
                            }
                            this.mBufferInfo.presentationTimeUs = getPTSUs();
                            mediaMuxerWrapper.writeSampleData(this.mTrackIndex, outputBuffer, this.mBufferInfo);
                            this.prevOutputPTSUs = this.mBufferInfo.presentationTimeUs;
                            int i2 = this.mTrackIndex;
                            if (i2 == 0) {
                                outputBuffer.position(this.mBufferInfo.offset);
                                outputBuffer.limit(this.mBufferInfo.offset + this.mBufferInfo.size);
                                if ((this.mBufferInfo.flags & 2) != 0) {
                                    z = (this.mBufferInfo.flags & 1) != 0;
                                    if (!z) {
                                        break;
                                    } else {
                                        bArr2 = new byte[0];
                                    }
                                } else {
                                    z = false;
                                }
                                boolean z2 = z | ((this.mBufferInfo.flags & 1) != 0);
                                int length = bArr2.length + this.mBufferInfo.size;
                                if (length > bArr.length) {
                                    bArr = new byte[length];
                                }
                                if (z2) {
                                    System.arraycopy(bArr2, 0, bArr, 0, bArr2.length);
                                    outputBuffer.get(bArr, bArr2.length, this.mBufferInfo.size);
                                    MediaEncoderListener mediaEncoderListener = this.mListener;
                                    if (mediaEncoderListener != null) {
                                        mediaEncoderListener.onEncodeResult(bArr, 0, bArr2.length + this.mBufferInfo.size, this.mBufferInfo.presentationTimeUs / 1000, 1);
                                    }
                                    FileUtils.putFileStream(bArr, 0, bArr2.length + this.mBufferInfo.size);
                                } else {
                                    outputBuffer.get(bArr, 0, this.mBufferInfo.size);
                                    MediaEncoderListener mediaEncoderListener2 = this.mListener;
                                    if (mediaEncoderListener2 != null) {
                                        mediaEncoderListener2.onEncodeResult(bArr, 0, this.mBufferInfo.size, this.mBufferInfo.presentationTimeUs / 1000, 1);
                                    }
                                    FileUtils.putFileStream(bArr, 0, this.mBufferInfo.size);
                                }
                            } else if (i2 == 1) {
                                this.mBuffer.clear();
                                outputBuffer.get(this.mBuffer.array(), 7, this.mBufferInfo.size);
                                outputBuffer.clear();
                                this.mBuffer.position(this.mBufferInfo.size + 7);
                                addADTStoPacket(this.mBuffer.array(), this.mBufferInfo.size + 7);
                                this.mBuffer.flip();
                                MediaEncoderListener mediaEncoderListener3 = this.mListener;
                                if (mediaEncoderListener3 != null) {
                                    mediaEncoderListener3.onEncodeResult(this.mBuffer.array(), 0, this.mBufferInfo.size + 7, this.mBufferInfo.presentationTimeUs / 1000, 0);
                                }
                            }
                            i = 0;
                        }
                        this.mMediaCodec.releaseOutputBuffer(iDequeueOutputBuffer, false);
                    }
                }
            }
            return;
            bArr2 = new byte[this.mBufferInfo.size];
            outputBuffer.get(bArr2);
            this.mMediaCodec.releaseOutputBuffer(iDequeueOutputBuffer, false);
        }
    }

    private void addADTStoPacket(byte[] bArr, int i) {
        bArr[0] = -1;
        bArr[1] = -15;
        bArr[2] = (byte) ((getSamplingRateIndex() << 2) + 64 + 0);
        bArr[3] = (byte) ((i >> 11) + 64);
        bArr[4] = (byte) ((i & 2047) >> 3);
        bArr[5] = (byte) (((i & 7) << 5) + 31);
        bArr[6] = -4;
    }

    private int getSamplingRateIndex() {
        int i = 0;
        while (true) {
            int[] iArr = AUDIO_SAMPLING_RATES;
            if (i >= iArr.length) {
                return -1;
            }
            if (iArr[i] == 44100) {
                return i;
            }
            i++;
        }
    }

    protected long getPTSUs() {
        long jNanoTime = System.nanoTime() / 1000;
        long j = this.prevOutputPTSUs;
        return jNanoTime < j ? jNanoTime + (j - jNanoTime) : jNanoTime;
    }
}
