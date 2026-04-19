package com.i4season.bkCamera.logicrelated.encoder.biz;

import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Build;
import android.os.Process;
import android.os.SystemClock;
import android.util.Log;
import android.view.Surface;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.Constant;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class AACEncodeConsumer extends Thread {
    public static final int[] AUDIO_SAMPLING_RATES = {96000, 88200, 64000, 48000, 44100, 32000, 24000, 22050, 16000, 12000, 11025, 8000, 7350, -1, -1, -1};
    private static int BIT_RATE = 16000;
    private static int BUFFER_SIZE = 1920;
    private static final boolean DEBUG = true;
    private static final String MIME_TYPE = "audio/mp4a-latm";
    private static int SAMPLE_RATE = 8000;
    private static final String TAG = "liusheng";
    private static final long TIMES_OUT = 1000;
    private int bufferSizeInBytes;
    private FileOutputStream fops;
    private boolean isExternalVoiceData;
    private OnAACEncodeResultListener listener;
    private MediaCodec mAudioEncoder;
    private AudioRecord mAudioRecord;
    private WeakReference<Mp4MediaMuxer> mMuxerRef;
    private int mSamplingRateIndex;
    private MediaFormat newFormat;
    private int outChannel = 1;
    private int bitRateForLame = 32;
    private int qaulityDegree = 7;
    private boolean isEncoderStart = false;
    private boolean isRecMp3 = false;
    private boolean isExit = false;
    private long prevPresentationTimes = 0;
    private LinkedList<byte[]> voiceBuffer = new LinkedList<>();

    public interface OnAACEncodeResultListener {
        void onEncodeResult(byte[] bArr, int i, int i2, long j);
    }

    public AACEncodeConsumer(boolean z) {
        int i = 0;
        this.mSamplingRateIndex = 0;
        this.isExternalVoiceData = z;
        if (z) {
            SAMPLE_RATE = 16000;
            BUFFER_SIZE = 960;
        }
        while (true) {
            int[] iArr = AUDIO_SAMPLING_RATES;
            if (i >= iArr.length) {
                return;
            }
            if (iArr[i] == SAMPLE_RATE) {
                this.mSamplingRateIndex = i;
                return;
            }
            i++;
        }
    }

    public void setOnAACEncodeResultListener(OnAACEncodeResultListener onAACEncodeResultListener) {
        this.listener = onAACEncodeResultListener;
    }

    public void exit() {
        this.isExit = true;
    }

    public synchronized void setTmpuMuxer(Mp4MediaMuxer mp4MediaMuxer) {
        this.mMuxerRef = new WeakReference<>(mp4MediaMuxer);
        Mp4MediaMuxer mp4MediaMuxer2 = this.mMuxerRef.get();
        if (mp4MediaMuxer2 != null && this.newFormat != null) {
            mp4MediaMuxer2.addTrack(this.newFormat, false);
        }
    }

    public void setData(byte[] bArr) {
        this.voiceBuffer.addLast(bArr);
    }

    @Override
    public void run() {
        if (!this.isEncoderStart) {
            initAudioRecord();
            initMediaCodec();
        }
        while (!this.isExit) {
            if (this.isExternalVoiceData) {
                LinkedList<byte[]> linkedList = this.voiceBuffer;
                if (linkedList != null && linkedList.size() > 0) {
                    try {
                        byte[] bArrPollFirst = this.voiceBuffer.pollFirst();
                        if (bArrPollFirst != null && bArrPollFirst.length > 0) {
                            byte[] bArr = {0, 0, 0, 0};
                            byte[] bArr2 = new byte[bArrPollFirst.length + bArr.length];
                            System.arraycopy(bArrPollFirst, 0, bArr2, 0, bArrPollFirst.length);
                            System.arraycopy(bArr, 0, bArr2, bArrPollFirst.length, bArr.length);
                            encode(bArr2);
                        }
                    } catch (NoSuchElementException e) {
                        Log.d(TAG, e.toString());
                    }
                }
                SystemClock.sleep(5L);
            } else {
                byte[] bArr3 = new byte[2048];
                int i = this.mAudioRecord.read(bArr3, 0, BUFFER_SIZE);
                if (i > 0) {
                    encodeBytes(bArr3, i);
                }
            }
        }
        stopMediaCodec();
        stopAudioRecord();
    }

    private void encodeBytes(byte[] bArr, int i) {
        int iDequeueOutputBuffer;
        ByteBuffer outputBuffer;
        Mp4MediaMuxer mp4MediaMuxer;
        Mp4MediaMuxer mp4MediaMuxer2;
        ByteBuffer inputBuffer;
        ByteBuffer[] inputBuffers = this.mAudioEncoder.getInputBuffers();
        ByteBuffer[] outputBuffers = this.mAudioEncoder.getOutputBuffers();
        int iDequeueInputBuffer = this.mAudioEncoder.dequeueInputBuffer(TIMES_OUT);
        if (iDequeueInputBuffer >= 0) {
            if (!isLollipop()) {
                inputBuffer = inputBuffers[iDequeueInputBuffer];
            } else {
                inputBuffer = this.mAudioEncoder.getInputBuffer(iDequeueInputBuffer);
            }
            if (bArr == null || i <= 0) {
                this.mAudioEncoder.queueInputBuffer(iDequeueInputBuffer, 0, 0, getPTSUs(), 4);
            } else {
                inputBuffer.clear();
                inputBuffer.put(bArr);
                this.mAudioEncoder.queueInputBuffer(iDequeueInputBuffer, 0, i, getPTSUs(), 0);
            }
        }
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        do {
            iDequeueOutputBuffer = this.mAudioEncoder.dequeueOutputBuffer(bufferInfo, TIMES_OUT);
            if (iDequeueOutputBuffer != -1) {
                if (iDequeueOutputBuffer == -3) {
                    if (!isLollipop()) {
                        outputBuffers = this.mAudioEncoder.getOutputBuffers();
                    }
                } else if (iDequeueOutputBuffer == -2) {
                    Log.i(TAG, "编码器输出缓存区格式改变，添加视频轨道到混合器");
                    synchronized (this) {
                        this.newFormat = this.mAudioEncoder.getOutputFormat();
                        if (this.mMuxerRef != null && (mp4MediaMuxer2 = this.mMuxerRef.get()) != null) {
                            mp4MediaMuxer2.addTrack(this.newFormat, false);
                        }
                    }
                } else {
                    if ((bufferInfo.flags & 2) != 0) {
                        Log.i(TAG, "编码数据被消费，BufferInfo的size属性置0");
                        bufferInfo.size = 0;
                    }
                    if ((bufferInfo.flags & 4) != 0) {
                        Log.i(TAG, "数据流结束，退出循环-------------------------------------");
                        return;
                    }
                    ByteBuffer byteBufferAllocate = ByteBuffer.allocate(10240);
                    if (!isLollipop()) {
                        outputBuffer = outputBuffers[iDequeueOutputBuffer];
                    } else {
                        outputBuffer = this.mAudioEncoder.getOutputBuffer(iDequeueOutputBuffer);
                    }
                    if (bufferInfo.size != 0) {
                        if (outputBuffer == null) {
                            throw new RuntimeException("encodecOutputBuffer" + iDequeueOutputBuffer + "was null");
                        }
                        WeakReference<Mp4MediaMuxer> weakReference = this.mMuxerRef;
                        if (weakReference != null && (mp4MediaMuxer = weakReference.get()) != null) {
                            mp4MediaMuxer.pumpStream(outputBuffer, bufferInfo, false);
                        }
                        byteBufferAllocate.clear();
                        outputBuffer.get(byteBufferAllocate.array(), 7, bufferInfo.size);
                        outputBuffer.clear();
                        byteBufferAllocate.position(bufferInfo.size + 7);
                        addADTStoPacket(byteBufferAllocate.array(), bufferInfo.size + 7);
                        byteBufferAllocate.flip();
                        if (this.listener != null) {
                            Log.i(TAG, "----->得到aac数据流<-----");
                            this.listener.onEncodeResult(byteBufferAllocate.array(), 0, bufferInfo.size + 7, bufferInfo.presentationTimeUs / TIMES_OUT);
                        }
                    }
                    this.mAudioEncoder.releaseOutputBuffer(iDequeueOutputBuffer, false);
                }
            }
        } while (iDequeueOutputBuffer >= 0);
    }

    private void encode(byte[] bArr) {
        Mp4MediaMuxer mp4MediaMuxer;
        Mp4MediaMuxer mp4MediaMuxer2;
        ByteBuffer[] inputBuffers = this.mAudioEncoder.getInputBuffers();
        ByteBuffer[] outputBuffers = this.mAudioEncoder.getOutputBuffers();
        int iDequeueInputBuffer = this.mAudioEncoder.dequeueInputBuffer(Constant.RECORD_DEAFAULT_TIME);
        if (iDequeueInputBuffer >= 0) {
            inputBuffers[iDequeueInputBuffer].put(bArr, 0, bArr.length);
            this.mAudioEncoder.queueInputBuffer(iDequeueInputBuffer, 0, bArr.length, getPTSUs(), 0);
        }
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        int iDequeueOutputBuffer = this.mAudioEncoder.dequeueOutputBuffer(bufferInfo, Constant.RECORD_DEAFAULT_TIME);
        if (iDequeueOutputBuffer == -2) {
            this.newFormat = this.mAudioEncoder.getOutputFormat();
            WeakReference<Mp4MediaMuxer> weakReference = this.mMuxerRef;
            if (weakReference != null && (mp4MediaMuxer2 = weakReference.get()) != null) {
                mp4MediaMuxer2.addTrack(this.newFormat, false);
            }
        }
        if (iDequeueOutputBuffer >= 0) {
            ByteBuffer byteBuffer = outputBuffers[iDequeueOutputBuffer];
            byteBuffer.rewind();
            byte[] bArr2 = new byte[bufferInfo.size];
            byteBuffer.get(bArr2, 0, bArr2.length);
            WeakReference<Mp4MediaMuxer> weakReference2 = this.mMuxerRef;
            if (weakReference2 != null && (mp4MediaMuxer = weakReference2.get()) != null) {
                mp4MediaMuxer.pumpStream(byteBuffer, bufferInfo, false);
            }
            this.mAudioEncoder.releaseOutputBuffer(iDequeueOutputBuffer, false);
        }
    }

    private void initAudioRecord() {
        if (this.isExternalVoiceData) {
            return;
        }
        Log.d(TAG, "AACEncodeConsumer-->开始采集音频");
        Process.setThreadPriority(-16);
        this.mAudioRecord = new AudioRecord(1, SAMPLE_RATE, 16, 2, AudioRecord.getMinBufferSize(SAMPLE_RATE, 16, 2));
        this.mAudioRecord.startRecording();
    }

    private void initMediaCodec() {
        Log.d(TAG, "AACEncodeConsumer-->开始编码音频");
        MediaCodecInfo mediaCodecInfoSelectSupportCodec = selectSupportCodec("audio/mp4a-latm");
        if (mediaCodecInfoSelectSupportCodec == null) {
            Log.e(TAG, "编码器不支持audio/mp4a-latm类型");
            return;
        }
        try {
            this.mAudioEncoder = MediaCodec.createByCodecName(mediaCodecInfoSelectSupportCodec.getName());
        } catch (IOException e) {
            Log.e(TAG, "创建编码器失败" + e.getMessage());
            e.printStackTrace();
        }
        MediaFormat mediaFormat = new MediaFormat();
        mediaFormat.setString("mime", "audio/mp4a-latm");
        mediaFormat.setInteger("bitrate", BIT_RATE);
        mediaFormat.setInteger("channel-count", 1);
        mediaFormat.setInteger("sample-rate", SAMPLE_RATE);
        mediaFormat.setInteger("aac-profile", 2);
        mediaFormat.setInteger("max-input-size", BUFFER_SIZE);
        this.mAudioEncoder.configure(mediaFormat, (Surface) null, (MediaCrypto) null, 1);
        this.mAudioEncoder.start();
        this.isEncoderStart = true;
    }

    private void stopAudioRecord() {
        Log.d(TAG, "AACEncodeConsumer-->停止采集音频");
        AudioRecord audioRecord = this.mAudioRecord;
        if (audioRecord != null) {
            audioRecord.stop();
            this.mAudioRecord.release();
            this.mAudioRecord = null;
        }
    }

    private void stopMediaCodec() {
        Log.d(TAG, "AACEncodeConsumer-->停止编码音频");
        MediaCodec mediaCodec = this.mAudioEncoder;
        if (mediaCodec != null) {
            mediaCodec.stop();
            this.mAudioEncoder.release();
            this.mAudioEncoder = null;
        }
        this.isEncoderStart = false;
    }

    private boolean isLollipop() {
        return Build.VERSION.SDK_INT >= 21;
    }

    private boolean isKITKAT() {
        return Build.VERSION.SDK_INT <= 19;
    }

    private long getPTSUs() {
        long jNanoTime = System.nanoTime() / TIMES_OUT;
        long j = this.prevPresentationTimes;
        return jNanoTime < j ? jNanoTime + (j - jNanoTime) : jNanoTime;
    }

    private MediaCodecInfo selectSupportCodec(String str) {
        int codecCount = MediaCodecList.getCodecCount();
        for (int i = 0; i < codecCount; i++) {
            MediaCodecInfo codecInfoAt = MediaCodecList.getCodecInfoAt(i);
            if (codecInfoAt.isEncoder()) {
                for (String str2 : codecInfoAt.getSupportedTypes()) {
                    if (str2.equalsIgnoreCase(str)) {
                        return codecInfoAt;
                    }
                }
            }
        }
        return null;
    }

    private void addADTStoPacket(byte[] bArr, int i) {
        bArr[0] = -1;
        bArr[1] = -15;
        bArr[2] = (byte) ((this.mSamplingRateIndex << 2) + 64 + 0);
        bArr[3] = (byte) ((i >> 11) + 64);
        bArr[4] = (byte) ((i & 2047) >> 3);
        bArr[5] = (byte) (((i & 7) << 5) + 31);
        bArr[6] = -4;
    }

    private short[] transferByte2Short(byte[] bArr, int i) {
        int i2 = i / 2;
        ShortBuffer shortBufferAsShortBuffer = ByteBuffer.wrap(bArr, 0, i).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
        short[] sArr = new short[i2];
        shortBufferAsShortBuffer.get(sArr, 0, i2);
        return sArr;
    }
}
