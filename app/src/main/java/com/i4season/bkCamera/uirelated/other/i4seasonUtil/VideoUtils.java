package com.i4season.bkCamera.uirelated.other.i4seasonUtil;

import android.os.Handler;
import com.bumptech.glide.load.Key;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class VideoUtils {
    public static String getUrlExtension(String str) {
        int iLastIndexOf;
        return (isEmpty(str) || (iLastIndexOf = str.lastIndexOf(46)) <= 0 || iLastIndexOf >= str.length() + (-1)) ? "" : str.substring(iLastIndexOf + 1).toLowerCase();
    }

    public static String getVideoNameFromUrl(String str) {
        int iLastIndexOf;
        String uTF8CodeInfoFromURL = getUTF8CodeInfoFromURL(str);
        return (isEmpty(uTF8CodeInfoFromURL) || (iLastIndexOf = uTF8CodeInfoFromURL.lastIndexOf(47)) <= 0 || iLastIndexOf >= uTF8CodeInfoFromURL.length() + (-1)) ? "" : uTF8CodeInfoFromURL.substring(iLastIndexOf + 1);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static String millisToString(long j) {
        boolean z = j < 0;
        long jAbs = Math.abs(j) / 1000;
        int i = (int) (jAbs % 60);
        long j2 = jAbs / 60;
        int i2 = (int) (j2 % 60);
        long j3 = j2 / 60;
        int i3 = (int) j3;
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        decimalFormat.applyPattern("00");
        if (j3 > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(z ? "-" : "");
            sb.append(i3);
            sb.append(Constant.SMB_COLON);
            sb.append(decimalFormat.format(i2));
            sb.append(Constant.SMB_COLON);
            sb.append(decimalFormat.format(i));
            return sb.toString();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(z ? "-" : "");
        sb2.append(i2);
        sb2.append(Constant.SMB_COLON);
        sb2.append(decimalFormat.format(i));
        return sb2.toString();
    }

    public static String timeToStr(long j) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return simpleDateFormat.format(Long.valueOf(j));
    }

    public static String timeToStr2(long j) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return j <= 0 ? "00:00" : simpleDateFormat.format(Long.valueOf(j));
    }

    public static String stringForTime(int i) {
        int i2 = i / 1000;
        int i3 = i2 % 60;
        int i4 = (i2 / 60) % 60;
        int i5 = i2 / 3600;
        return i5 > 0 ? String.format("%02d:%02d:%02d", Integer.valueOf(i5), Integer.valueOf(i4), Integer.valueOf(i3)).toString() : String.format("%02d:%02d:%02d", Integer.valueOf(i5), Integer.valueOf(i4), Integer.valueOf(i3)).toString();
    }

    public static void handlerMsgToUI(Handler handler, int i) {
        handler.sendEmptyMessage(i);
    }

    public static void handlerMsgToUI(Handler handler, int i, long j) {
        handler.sendEmptyMessageDelayed(i, j);
    }

    public static String getUTF8CodeInfoFromURL(String str) {
        try {
            return URLDecoder.decode(str, Key.STRING_CHARSET_NAME);
        } catch (Exception unused) {
            return str;
        }
    }

    public static boolean isExceedTwoGFile(String str) {
        return Long.parseLong(str) / 1048576 > 2000;
    }
}
