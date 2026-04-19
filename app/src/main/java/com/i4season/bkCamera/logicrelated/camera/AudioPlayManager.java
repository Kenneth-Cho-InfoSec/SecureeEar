package com.i4season.bkCamera.logicrelated.camera;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.os.Build;
import java.io.DataInputStream;
import java.io.FileInputStream;

public class AudioPlayManager {
    public AudioTrack audioTrack;
    private DataInputStream dataInputStream;
    private FileInputStream fileInputStream;
    private int mStatus;
    private int minBufferSize;
    private ReadDataThread readDataThread;
    private int STATUS_NO_READY = 0;
    private int STATUS_READY = 1;
    private int STATUS_START = 2;
    private int STATUS_STOP = 3;
    private int STATUS_PAUSE = 4;

    public static class AudioPlayManagerWDHolder {
        public static AudioPlayManager audioPlayManager = new AudioPlayManager();
    }

    public static AudioPlayManager getInstance() {
        return AudioPlayManagerWDHolder.audioPlayManager;
    }

    public void init() {
        this.minBufferSize = AudioTrack.getMinBufferSize(16000, 4, 2);
        if (Build.VERSION.SDK_INT >= 24) {
            this.audioTrack = new AudioTrack(new AudioAttributes.Builder().setContentType(1).setUsage(16).build(), new AudioFormat.Builder().setSampleRate(16000).setChannelMask(4).setEncoding(2).build(), this.minBufferSize, 1, 0);
        } else {
            this.audioTrack = new AudioTrack(3, 16000, 4, 2, this.minBufferSize, 1);
        }
        this.mStatus = this.STATUS_READY;
    }

    public void startPlay() {
        int i;
        int i2;
        AudioTrack audioTrack = this.audioTrack;
        if (audioTrack == null || (i = this.mStatus) == this.STATUS_NO_READY || i == (i2 = this.STATUS_START)) {
            return;
        }
        this.mStatus = i2;
        audioTrack.play();
        this.readDataThread = new ReadDataThread();
        this.readDataThread.start();
    }

    public void pushBufData(byte[] bArr) {
        AudioTrack audioTrack = this.audioTrack;
        if (audioTrack == null || this.mStatus == this.STATUS_NO_READY) {
            return;
        }
        audioTrack.write(bArr, 0, bArr.length);
    }

    public void stopPlay() {
        if (this.mStatus == this.STATUS_NO_READY) {
            return;
        }
        this.mStatus = this.STATUS_STOP;
        this.audioTrack.stop();
        release();
    }

    public void pausePlay() {
        if (this.mStatus == this.STATUS_START) {
            this.mStatus = this.STATUS_PAUSE;
            this.audioTrack.pause();
        }
    }

    public void resumePlay() {
        if (this.mStatus == this.STATUS_PAUSE) {
            this.mStatus = this.STATUS_START;
            this.audioTrack.play();
        }
    }

    public void release() {
        AudioTrack audioTrack = this.audioTrack;
        if (audioTrack != null) {
            audioTrack.release();
            this.audioTrack = null;
        }
        this.mStatus = this.STATUS_NO_READY;
        this.readDataThread = null;
    }

    class ReadDataThread extends Thread {
        private byte[] bytes;

        ReadDataThread() {
        }

        @Override
        public void run() {
            while (true) {
                if (this.bytes != null) {
                    synchronized (AudioPlayManager.this) {
                        AudioPlayManager.this.audioTrack.write(this.bytes, 0, this.bytes.length);
                        this.bytes = null;
                    }
                }
            }
        }

        public void pushBufData(byte[] bArr) {
            this.bytes = bArr;
        }
    }
}
