package com.i4season.bkCamera.logicrelated.recoder;

import android.graphics.Bitmap;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Build;
import android.util.Log;
import android.view.Surface;
import androidx.core.view.MotionEventCompat;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.Constant;
import java.io.IOException;
import java.nio.ByteBuffer;

public class VideoMediaCoder2 {
    private static final float BPP = 0.5f;
    private static final int FRAME_RATE = 15;
    private static final String TAG = "MediaCoder";
    private static final String VCODEC = "video/avc";
    protected static int[] recognizedFormats = {21, 19};
    int fps;
    long pts;
    public long pts_;
    private boolean bGetPPS = false;
    public long nCountFrame = 0;
    public int colorFormat = 0;
    int ddd = 0;
    private MediaCodec mMediaCodec = null;

    private MediaFormat F_GetMediaFormat(int i, int i2, int i3, int i4, int i5) {
        MediaCodec mediaCodec = this.mMediaCodec;
        if (mediaCodec != null) {
            mediaCodec.stop();
            this.mMediaCodec.release();
            this.mMediaCodec = null;
            this.pts = 0L;
            this.pts_ = 0L;
            this.nCountFrame = 0L;
        }
        boolean z = false;
        this.bGetPPS = false;
        this.pts = 0L;
        this.pts_ = 0L;
        this.nCountFrame = 0L;
        this.fps = i4;
        try {
            this.mMediaCodec = MediaCodec.createEncoderByType(VCODEC);
            z = true;
        } catch (IOException | IllegalArgumentException | NullPointerException e) {
            e.printStackTrace();
        }
        if (!z) {
            this.mMediaCodec = null;
            return null;
        }
        this.colorFormat = i5;
        MediaFormat mediaFormatCreateVideoFormat = MediaFormat.createVideoFormat(VCODEC, i, i2);
        mediaFormatCreateVideoFormat.setInteger("width", i);
        mediaFormatCreateVideoFormat.setInteger("height", i2);
        mediaFormatCreateVideoFormat.setInteger("bitrate", calcBitRate(i, i2));
        mediaFormatCreateVideoFormat.setInteger("i-frame-interval", 1);
        mediaFormatCreateVideoFormat.setInteger("frame-rate", i4);
        mediaFormatCreateVideoFormat.setInteger("color-format", i5);
        if (Build.VERSION.SDK_INT >= 21) {
            mediaFormatCreateVideoFormat.setInteger("bitrate-mode", 1);
            mediaFormatCreateVideoFormat.setInteger("profile", 1);
            mediaFormatCreateVideoFormat.setInteger("level", 1);
            mediaFormatCreateVideoFormat.setInteger("capture-rate", i4);
        }
        try {
            this.mMediaCodec.configure(mediaFormatCreateVideoFormat, (Surface) null, (MediaCrypto) null, 1);
            return mediaFormatCreateVideoFormat;
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    private int calcBitRate(int i, int i2) {
        int i3 = (int) (i * 7.5f * i2);
        Log.i(TAG, String.format("bitrate=%5.2f[Mbps]", Float.valueOf((i3 / 1024.0f) / 1024.0f)));
        return i3;
    }

    public int initMediaCodec(int i, int i2, int i3, int i4) {
        int iSelectVideoCodec = selectVideoCodec();
        if (F_GetMediaFormat(i, i2, i3, i4, iSelectVideoCodec) == null) {
            iSelectVideoCodec = 19;
            if (F_GetMediaFormat(i, i2, i3, i4, 19) == null) {
                iSelectVideoCodec = 0;
            }
        }
        if (iSelectVideoCodec != 0) {
            this.mMediaCodec.start();
        } else {
            this.mMediaCodec.release();
            this.mMediaCodec = null;
        }
        return iSelectVideoCodec;
    }

    public void F_CloseEncoder() {
        MediaCodec mediaCodec = this.mMediaCodec;
        if (mediaCodec == null) {
            return;
        }
        try {
            mediaCodec.stop();
            this.mMediaCodec.release();
            this.mMediaCodec = null;
            this.bGetPPS = false;
            this.nCountFrame = 0L;
            Log.e(TAG, "Close MediaCodec!!!---");
        } catch (Exception unused) {
        }
    }

    public long getRecordTime() {
        int i = this.fps;
        if (i <= 0) {
            return 0L;
        }
        return (long) (this.nCountFrame * (1000.0f / i));
    }

    private int getColorFormat() {
        MediaCodecInfo mediaCodecInfoChooseVideoEncoder = chooseVideoEncoder(null);
        if (mediaCodecInfoChooseVideoEncoder == null) {
            return 0;
        }
        MediaCodecInfo.CodecCapabilities capabilitiesForType = mediaCodecInfoChooseVideoEncoder.getCapabilitiesForType(VCODEC);
        for (int i = 0; i < capabilitiesForType.colorFormats.length; i++) {
            int i2 = capabilitiesForType.colorFormats[i];
            if (i2 == 19 || i2 == 21) {
                return i2;
            }
        }
        return 0;
    }

    public void offerEncoder(Bitmap bitmap, int i, int i2, int i3) {
        if (this.mMediaCodec == null) {
            return;
        }
        byte[] nv12 = getNV12(i, i2, bitmap);
        this.mMediaCodec.getInputBuffers();
        int iDequeueInputBuffer = this.mMediaCodec.dequeueInputBuffer(Constant.RECORD_DEAFAULT_TIME);
        if (iDequeueInputBuffer >= 0) {
            long j = this.pts;
            this.pts_ = ((long) (1000000 / this.fps)) * j;
            this.pts = j + 1;
            ByteBuffer inputBuffer = this.mMediaCodec.getInputBuffer(iDequeueInputBuffer);
            inputBuffer.clear();
            inputBuffer.put(nv12);
            this.mMediaCodec.queueInputBuffer(iDequeueInputBuffer, 0, nv12.length, this.pts_, 0);
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            int iDequeueOutputBuffer = this.mMediaCodec.dequeueOutputBuffer(bufferInfo, 2000L);
            if (iDequeueOutputBuffer == -2) {
                MyMediaMuxer.AddVideoTrack(this.mMediaCodec.getOutputFormat());
            }
            if (iDequeueOutputBuffer >= 0) {
                ByteBuffer outputBuffer = this.mMediaCodec.getOutputBuffer(iDequeueOutputBuffer);
                byte[] bArr = new byte[bufferInfo.size];
                outputBuffer.get(bArr);
                if (bufferInfo.flags == 2) {
                    if (!this.bGetPPS) {
                        this.bGetPPS = true;
                    }
                } else {
                    boolean z = bufferInfo.flags == 1;
                    this.nCountFrame++;
                    MyMediaMuxer.WritSample(bArr, z, true, this.pts_);
                }
                this.mMediaCodec.releaseOutputBuffer(iDequeueOutputBuffer, false);
            }
        }
    }

    private MediaCodecInfo chooseVideoEncoder(String str) {
        int codecCount = MediaCodecList.getCodecCount();
        for (int i = 0; i < codecCount; i++) {
            MediaCodecInfo codecInfoAt = MediaCodecList.getCodecInfoAt(i);
            if (codecInfoAt.isEncoder()) {
                for (String str2 : codecInfoAt.getSupportedTypes()) {
                    if (str2.equalsIgnoreCase(VCODEC) && (str == null || codecInfoAt.getName().contains(str))) {
                        return codecInfoAt;
                    }
                }
            }
        }
        return null;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private byte[] getNV12(int i, int i2, Bitmap bitmap) {
        int i3 = i * i2;
        int[] iArr = new int[i3];
        bitmap.getPixels(iArr, 0, i, 0, 0, i, i2);
        byte[] bArr = new byte[(i3 * 3) / 2];
        int i4 = this.colorFormat;
        if (i4 != 39) {
            switch (i4) {
                case 19:
                    encodeYUV420P(bArr, iArr, i, i2);
                    break;
                case 20:
                    encodeYUV420PP(bArr, iArr, i, i2);
                    break;
                case 21:
                    encodeYUV420SP(bArr, iArr, i, i2);
                    break;
            }
        } else {
            encodeYUV420PSP(bArr, iArr, i, i2);
        }
        return bArr;
    }

    private void encodeYUV420SP(byte[] bArr, int[] iArr, int i, int i2) {
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

    private void encodeYUV420P(byte[] bArr, int[] iArr, int i, int i2) {
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

    private void encodeYUV420PSP(byte[] bArr, int[] iArr, int i, int i2) {
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        while (i3 < i2) {
            int i6 = i4;
            int i7 = i5;
            for (int i8 = 0; i8 < i; i8++) {
                int i9 = iArr[i7];
                int i10 = (iArr[i7] & 16711680) >> 16;
                int i11 = (iArr[i7] & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
                int i12 = (iArr[i7] & 255) >> 0;
                int i13 = (((((i10 * 66) + (i11 * 129)) + (i12 * 25)) + 128) >> 8) + 16;
                int i14 = (((((i10 * (-38)) - (i11 * 74)) + (i12 * 112)) + 128) >> 8) + 128;
                int i15 = (((((i10 * 112) - (i11 * 94)) - (i12 * 18)) + 128) >> 8) + 128;
                int i16 = i6 + 1;
                if (i13 < 0) {
                    i13 = 0;
                } else if (i13 > 255) {
                    i13 = 255;
                }
                bArr[i6] = (byte) i13;
                if (i3 % 2 == 0 && i7 % 2 == 0) {
                    int i17 = i16 + 1;
                    if (i14 < 0) {
                        i14 = 0;
                    } else if (i14 > 255) {
                        i14 = 255;
                    }
                    bArr[i17] = (byte) i14;
                    int i18 = i16 + 3;
                    if (i15 < 0) {
                        i15 = 0;
                    } else if (i15 > 255) {
                        i15 = 255;
                    }
                    bArr[i18] = (byte) i15;
                }
                if (i7 % 2 == 0) {
                    i16++;
                }
                i6 = i16;
                i7++;
            }
            i3++;
            i5 = i7;
            i4 = i6;
        }
    }

    private void encodeYUV420PP(byte[] bArr, int[] iArr, int i, int i2) {
        int length = bArr.length / 2;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        while (i3 < i2) {
            int i6 = i4;
            int i7 = length;
            int i8 = i5;
            for (int i9 = 0; i9 < i; i9++) {
                int i10 = iArr[i8];
                int i11 = (iArr[i8] & 16711680) >> 16;
                int i12 = (iArr[i8] & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
                int i13 = 255;
                int i14 = (iArr[i8] & 255) >> 0;
                int i15 = (((((i11 * 66) + (i12 * 129)) + (i14 * 25)) + 128) >> 8) + 16;
                int i16 = (((((i11 * (-38)) - (i12 * 74)) + (i14 * 112)) + 128) >> 8) + 128;
                int i17 = (((((i11 * 112) - (i12 * 94)) - (i14 * 18)) + 128) >> 8) + 128;
                int i18 = i3 % 2;
                if (i18 == 0 && i8 % 2 == 0) {
                    int i19 = i6 + 1;
                    if (i15 < 0) {
                        i15 = 0;
                    } else if (i15 > 255) {
                        i15 = 255;
                    }
                    bArr[i6] = (byte) i15;
                    i6 = i19 + 1;
                    if (i16 < 0) {
                        i16 = 0;
                    } else if (i16 > 255) {
                        i16 = 255;
                    }
                    bArr[i6] = (byte) i16;
                    int i20 = i7 + 1;
                    if (i17 < 0) {
                        i13 = 0;
                    } else if (i17 <= 255) {
                        i13 = i17;
                    }
                    bArr[i20] = (byte) i13;
                } else if (i18 == 0 && i8 % 2 == 1) {
                    int i21 = i6 + 1;
                    if (i15 < 0) {
                        i13 = 0;
                    } else if (i15 <= 255) {
                        i13 = i15;
                    }
                    bArr[i6] = (byte) i13;
                    i6 = i21;
                } else if (i18 == 1 && i8 % 2 == 0) {
                    int i22 = i7 + 1;
                    if (i15 < 0) {
                        i13 = 0;
                    } else if (i15 <= 255) {
                        i13 = i15;
                    }
                    bArr[i7] = (byte) i13;
                    i7 = i22 + 1;
                } else if (i18 == 1 && i8 % 2 == 1) {
                    int i23 = i7 + 1;
                    if (i15 < 0) {
                        i13 = 0;
                    } else if (i15 <= 255) {
                        i13 = i15;
                    }
                    bArr[i7] = (byte) i13;
                    i7 = i23;
                }
                i8++;
            }
            i3++;
            i5 = i8;
            i4 = i6;
            length = i7;
        }
    }

    protected final int selectVideoCodec() {
        int iSelectColorFormat;
        int codecCount = MediaCodecList.getCodecCount();
        for (int i = 0; i < codecCount; i++) {
            MediaCodecInfo codecInfoAt = MediaCodecList.getCodecInfoAt(i);
            if (codecInfoAt.isEncoder()) {
                for (String str : codecInfoAt.getSupportedTypes()) {
                    if (str.equalsIgnoreCase(VCODEC) && (iSelectColorFormat = selectColorFormat(codecInfoAt, VCODEC)) > 0) {
                        return iSelectColorFormat;
                    }
                }
            }
        }
        return 0;
    }

    protected final int selectColorFormat(MediaCodecInfo mediaCodecInfo, String str) {
        try {
            Thread.currentThread().setPriority(10);
            MediaCodecInfo.CodecCapabilities capabilitiesForType = mediaCodecInfo.getCapabilitiesForType(str);
            Thread.currentThread().setPriority(5);
            int i = 0;
            int i2 = 0;
            while (true) {
                if (i2 >= capabilitiesForType.colorFormats.length) {
                    break;
                }
                int i3 = capabilitiesForType.colorFormats[i2];
                if (isRecognizedViewoFormat(i3)) {
                    i = i3;
                    break;
                }
                i2++;
            }
            if (i == 0) {
                Log.e(TAG, "couldn't find a good color format for " + mediaCodecInfo.getName() + " / " + str);
            }
            return i;
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
}
