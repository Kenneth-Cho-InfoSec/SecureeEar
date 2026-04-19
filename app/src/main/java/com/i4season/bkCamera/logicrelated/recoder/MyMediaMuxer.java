package com.i4season.bkCamera.logicrelated.recoder;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.util.Log;
import java.nio.ByteBuffer;

public class MyMediaMuxer {
    private static int audioInx = -1;
    private static boolean bRecording = false;
    private static boolean bStartWrite = false;
    public static MediaFormat formatA = null;
    public static MediaFormat formatV = null;
    public static boolean mIsAudioRecoder = false;
    private static MediaMuxer mediaMuxer = null;
    private static int videoInx = -1;

    public static int start(String str) {
        boolean z = bRecording;
        bRecording = false;
        try {
            bStartWrite = false;
            formatV = null;
            formatA = null;
            mediaMuxer = new MediaMuxer(str, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    static void WritSample(byte[] bArr, boolean z, boolean z2, long j) {
        if (bRecording) {
            if (!bStartWrite && z2 && z) {
                bStartWrite = true;
                Log.e("media", "firset key Framne");
            }
            if (bStartWrite) {
                if (z2) {
                    if (bArr == null || mediaMuxer == null) {
                        return;
                    }
                    MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
                    bufferInfo.offset = 0;
                    bufferInfo.size = bArr.length;
                    bufferInfo.flags = 0;
                    if (z) {
                        bufferInfo.flags = 1;
                    }
                    bufferInfo.presentationTimeUs = j;
                    try {
                        mediaMuxer.writeSampleData(videoInx, ByteBuffer.wrap(bArr), bufferInfo);
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                }
                if (bArr == null || mediaMuxer == null || audioInx < 0) {
                    return;
                }
                MediaCodec.BufferInfo bufferInfo2 = new MediaCodec.BufferInfo();
                bufferInfo2.offset = 0;
                bufferInfo2.size = bArr.length;
                bufferInfo2.flags = 0;
                bufferInfo2.presentationTimeUs = j;
                try {
                    mediaMuxer.writeSampleData(audioInx, ByteBuffer.wrap(bArr), bufferInfo2);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    static void AddVideoTrack(MediaFormat mediaFormat) {
        MediaMuxer mediaMuxer2;
        if (mediaFormat == null || (mediaMuxer2 = mediaMuxer) == null) {
            return;
        }
        try {
            if (videoInx < 0) {
                videoInx = mediaMuxer2.addTrack(mediaFormat);
            }
        } catch (Exception e) {
            videoInx = -1;
            e.printStackTrace();
        }
        if (videoInx >= 0) {
            if (mIsAudioRecoder) {
                if (audioInx >= 0) {
                    bRecording = true;
                    bStartWrite = false;
                    mediaMuxer.start();
                    Log.e("media", "Start 111");
                    return;
                }
                return;
            }
            bRecording = true;
            bStartWrite = false;
            mediaMuxer.start();
            Log.e("media", "Start 222");
        }
    }

    static void AddAudioTrack(MediaFormat mediaFormat) {
        MediaMuxer mediaMuxer2;
        if (mediaFormat == null || (mediaMuxer2 = mediaMuxer) == null) {
            return;
        }
        try {
            if (audioInx < 0) {
                audioInx = mediaMuxer2.addTrack(mediaFormat);
            }
        } catch (Exception e) {
            audioInx = -1;
            e.printStackTrace();
        }
        if (audioInx >= 0) {
            if (videoInx >= 0) {
                bRecording = true;
                bStartWrite = false;
                mediaMuxer.start();
                Log.e("media", "Start 333");
                return;
            }
            Log.e("media", "Start 444");
        }
    }

    public static void stop() {
        if (bRecording) {
            try {
                mediaMuxer.stop();
                mediaMuxer.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
            formatV = null;
            formatA = null;
            mediaMuxer = null;
            bRecording = false;
            audioInx = -1;
            videoInx = -1;
        }
    }
}
