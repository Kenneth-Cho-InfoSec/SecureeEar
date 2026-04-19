package com.i4season.bkCamera.uirelated.filenodeopen.videoplay.handler;

import android.content.SharedPreferences;
import com.i4season.bkCamera.WDApplication;
import com.i4season.bkCamera.uirelated.filenodeopen.videoplay.handler.VideoHandler;

public class SaveVideoDecodeValue {
    private static final String DECODE_FILE = "videoDecode";
    private static final String DECODE_MODE_VALUE = "decodeMode";

    public static void setDecodeMode(VideoHandler.DecodeMode decodeMode) {
        String str;
        SharedPreferences.Editor editorEdit = WDApplication.getInstance().getSharedPreferences(DECODE_FILE, 0).edit();
        if (decodeMode == VideoHandler.DecodeMode.MHW) {
            str = "mhw";
        } else {
            str = decodeMode == VideoHandler.DecodeMode.SW ? "sw" : "";
        }
        editorEdit.putString(DECODE_MODE_VALUE, str);
        editorEdit.commit();
    }

    public static VideoHandler.DecodeMode getDecodeMode() {
        String string = WDApplication.getInstance().getSharedPreferences(DECODE_FILE, 0).getString(DECODE_MODE_VALUE, "sw");
        VideoHandler.DecodeMode decodeMode = VideoHandler.DecodeMode.SW;
        if (string.equals("sw")) {
            return VideoHandler.DecodeMode.SW;
        }
        return string.equals("mhw") ? VideoHandler.DecodeMode.MHW : decodeMode;
    }
}
