package com.i4season.bkCamera.logicrelated.recoder;

import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.view.Surface;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.Constant;
import java.io.IOException;
import java.nio.ByteBuffer;

public class AudioEncoder implements AudioCodec {
    private final String TAG = "AudioEncoder";
    private byte[] mFrameByte;
    private Worker mWorker;
    public MediaFormat mediaFormat;

    private static native boolean naSentVoiceData(byte[] bArr, int i);

    public boolean start() {
        if (this.mWorker != null) {
            return false;
        }
        this.mWorker = new Worker();
        boolean zPrepare = this.mWorker.prepare();
        if (zPrepare) {
            this.mWorker.setRunning(true);
            this.mWorker.start();
        }
        return zPrepare;
    }

    public void stop() {
        Worker worker = this.mWorker;
        if (worker != null) {
            worker.setRunning(false);
            this.mWorker = null;
        }
    }

    private class Worker extends Thread {
        boolean bStart;
        private boolean isRunning;
        private byte[] mBuffer;
        MediaCodec.BufferInfo mBufferInfo;
        private MediaCodec mEncoder;
        private int mFrameSize;
        private AudioRecord mRecord;
        long pts;
        private long pts_unit;

        private Worker() {
            this.mFrameSize = 2048;
            this.isRunning = false;
            this.pts_unit = 0L;
            this.bStart = false;
        }

        @Override
        public void run() {
            this.bStart = false;
            this.pts = 0L;
            while (this.isRunning) {
                this.mRecord.read(this.mBuffer, 0, this.mFrameSize);
                encode(this.mBuffer);
            }
            release();
        }

        public void setRunning(boolean z) {
            this.isRunning = z;
        }

        private void release() {
            MediaCodec mediaCodec = this.mEncoder;
            if (mediaCodec != null) {
                mediaCodec.stop();
                this.mEncoder.release();
            }
            AudioRecord audioRecord = this.mRecord;
            if (audioRecord != null) {
                audioRecord.stop();
                this.mRecord.release();
                this.mRecord = null;
            }
        }

        public boolean prepare() {
            try {
                this.mBufferInfo = new MediaCodec.BufferInfo();
                this.mEncoder = MediaCodec.createEncoderByType(AudioCodec.MIME_TYPE);
                AudioEncoder.this.mediaFormat = MediaFormat.createAudioFormat(AudioCodec.MIME_TYPE, 44100, 1);
                AudioEncoder.this.mediaFormat.setInteger("bitrate", AudioCodec.KEY_BIT_RATE);
                AudioEncoder.this.mediaFormat.setInteger("sample-rate", 44100);
                AudioEncoder.this.mediaFormat.setInteger("channel-count", 1);
                AudioEncoder.this.mediaFormat.setInteger("aac-profile", 2);
                this.mEncoder.configure(AudioEncoder.this.mediaFormat, (Surface) null, (MediaCrypto) null, 1);
                this.mEncoder.start();
                try {
                    int minBufferSize = AudioRecord.getMinBufferSize(44100, 16, 2) * 2;
                    this.mRecord = new AudioRecord(1, 44100, 16, 2, minBufferSize);
                    this.mFrameSize = Math.min(2048, minBufferSize);
                    this.mBuffer = new byte[this.mFrameSize];
                    this.pts_unit = (long) ((this.mFrameSize / 88200.0f) * 1000000.0f);
                    this.mRecord.startRecording();
                    return true;
                } catch (Exception unused) {
                    this.mRecord = null;
                    this.mEncoder = null;
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        private void encode(byte[] bArr) {
            long j;
            ByteBuffer[] inputBuffers = this.mEncoder.getInputBuffers();
            ByteBuffer[] outputBuffers = this.mEncoder.getOutputBuffers();
            int iDequeueInputBuffer = this.mEncoder.dequeueInputBuffer(50000L);
            if (iDequeueInputBuffer >= 0) {
                inputBuffers[iDequeueInputBuffer].put(bArr, 0, bArr.length);
                long j2 = this.pts;
                j = j2 * this.pts_unit;
                this.pts = j2 + 1;
                this.mEncoder.queueInputBuffer(iDequeueInputBuffer, 0, bArr.length, j, 0);
            } else {
                j = 0;
            }
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            int iDequeueOutputBuffer = this.mEncoder.dequeueOutputBuffer(bufferInfo, Constant.BACKWARD_DEAFAULT_TIME);
            if (iDequeueOutputBuffer == -2) {
                MyMediaMuxer.AddAudioTrack(this.mEncoder.getOutputFormat());
            }
            if (iDequeueOutputBuffer >= 0) {
                ByteBuffer byteBuffer = outputBuffers[iDequeueOutputBuffer];
                byteBuffer.rewind();
                byte[] bArr2 = new byte[bufferInfo.size];
                byteBuffer.get(bArr2, 0, bArr2.length);
                MyMediaMuxer.WritSample(bArr2, false, false, j);
                this.mEncoder.releaseOutputBuffer(iDequeueOutputBuffer, false);
            }
        }

        private void addADTStoPacket(byte[] bArr, int i) {
            bArr[0] = -1;
            bArr[1] = -7;
            bArr[2] = (byte) 80;
            bArr[3] = (byte) (128 + (i >> 11));
            bArr[4] = (byte) ((i & 2047) >> 3);
            bArr[5] = (byte) (((i & 7) << 5) + 31);
            bArr[6] = -4;
        }
    }
}
