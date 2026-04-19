package com.i4season.bkCamera.uirelated.other.i4seasonUtil;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.location.LocationManager;
import android.media.AudioAttributes;
import android.media.MediaMetadataRetriever;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.StatFs;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;
import com.bumptech.glide.load.Key;
import com.i4season.bkCamera.WDApplication;
import com.i4season.bkCamera.logicrelated.permissionmanage.PermissionInstans;
import com.i4season.bkCamera.uirelated.functionpage.camerashow.CameraShowActivity;
import com.i4season.bkCamera.uirelated.functionpage.camerashow.view.BaseCameraView;
import com.i4season.bkCamera.uirelated.other.AppPathInfo;
import com.i4season.bkCamera.uirelated.other.Language.Strings;
import com.i4season.bkCamera.uirelated.other.logmanage.LogWD;
import com.i4season.bkCamera.uirelated.other.view.I4SeasonToast;
import com.i4season.i4season_camera.C0413R;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.NetworkInterface;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class UtilTools {
    static final boolean $assertionsDisabled = false;
    public static final int MAX_INDEX = 2;
    public static final int MIN_INDEX = 0;
    private static ConnectivityManager connMgr = null;
    private static String hexString = "0123456789ABCDEF";
    private static I4SeasonToast i4SeasonToast;
    private static MessageDigest mMessageDigest;
    private static Toast mToast;
    public static final String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    public static final AtomicInteger sNextGeneratedId = new AtomicInteger(PathInterpolatorCompat.MAX_NUM_POINTS);
    private static int selected_index = 1;
    public static SoundPool soundPool;
    public static SoundPool videoSoundPool;
    private static WifiManager wifiManager;

    public static boolean isEmojiCharacter(char c) {
        return (c == 0 || c == '\t' || c == '\n' || c == '\r' || (c >= ' ' && c <= 55295) || ((c >= 57344 && c <= 65533) || (c >= 0 && c <= 65535))) ? false : true;
    }

    public static boolean isHaveWifiNet(Context context) {
        NetworkInfo activeNetworkInfo;
        int type;
        return (context == null || (activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo()) == null || ((type = activeNetworkInfo.getType()) != 1 && type != 0)) ? false : true;
    }

    public static int getNetworkConnectType(Context context) {
        NetworkInfo activeNetworkInfo;
        if (context == null || (activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo()) == null) {
            return -1;
        }
        return activeNetworkInfo.getType();
    }

    public static String getDevicesID(Context context) {
        return ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
    }

    public static String getPhoneModel() {
        return Build.MODEL;
    }

    public static String getAndroidID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), "android_id");
    }

    public static boolean isAirplaneModeOn(Context context) {
        return Settings.System.getInt(context.getContentResolver(), "airplane_mode_on", 0) != 0;
    }

    public static String getFileTypeFromName(String str) {
        return str.substring(str.lastIndexOf(".") + 1);
    }

    public static String getFilePrefixLength(String str) {
        return (str == null || !str.contains(".")) ? str : str.substring(0, str.lastIndexOf("."));
    }

    public static String getFileSuffix(String str) {
        return (str == null || !str.contains(".")) ? "" : str.substring(str.lastIndexOf("."));
    }

    public static String createFolderPath(String str) {
        File file = new File(str);
        if (!file.exists()) {
            file.mkdirs();
        }
        return str;
    }

    public static boolean createFileInfSdcard(String str) {
        if (!Environment.getExternalStorageState().equals("mounted")) {
            return false;
        }
        File file = new File(str);
        if (file.exists()) {
            return false;
        }
        try {
            return file.createNewFile();
        } catch (Exception e) {
            LogWD.writeMsg(e);
            return false;
        }
    }

    public static boolean createFolderInSdcard(String str) {
        if (!Environment.getExternalStorageState().equals("mounted")) {
            return false;
        }
        File file = new File(str);
        if (file.exists()) {
            return false;
        }
        return file.mkdirs();
    }

    public static String getFileName(String str) {
        return !TextUtils.isEmpty(str) ? str.substring(str.lastIndexOf("/") + 1, str.length()) : "";
    }

    public static String getFilePath(String str) {
        int iLastIndexOf;
        if (str == null || (iLastIndexOf = str.lastIndexOf("/")) == -1) {
            return null;
        }
        return str.substring(0, iLastIndexOf);
    }

    public static String changeDefSDCardToMnt(String str) {
        String string = Environment.getExternalStorageDirectory().toString();
        return str.startsWith(string) ? str.replace(string, AppPathInfo.app_sdcard) : str;
    }

    public static void do_sort(List<File> list) {
        if (list.isEmpty()) {
            return;
        }
        Collections.sort(list, new Comparator<File>() {
            @Override
            public int compare(File file, File file2) {
                return file.getPath().substring(file.getPath().lastIndexOf("/")).toLowerCase(Locale.getDefault()).compareTo(file2.getPath().substring(file.getPath().lastIndexOf("/")).toLowerCase(Locale.getDefault()));
            }
        });
    }

    public static String deleteFileNameFromPath(String str) {
        int iLastIndexOf = str.lastIndexOf("/");
        if (iLastIndexOf == -1) {
            return null;
        }
        return str.substring(0, iLastIndexOf);
    }

    public static String getUTF8CodeInfoFromURL(String str) {
        int i;
        boolean z;
        try {
            StringBuffer stringBuffer = new StringBuffer();
            int length = str.length();
            int i2 = 0;
            int i3 = 0;
            int i4 = -1;
            while (i2 < length) {
                int iCharAt = str.charAt(i2);
                if (iCharAt == 37) {
                    if (i2 >= length - 2) {
                        return str;
                    }
                    int i5 = i2 + 1;
                    char cCharAt = str.charAt(i5);
                    if ((cCharAt >= 'a' && cCharAt <= 'f') || ((cCharAt >= '0' && cCharAt <= '9') || (cCharAt >= 'A' && cCharAt <= 'F'))) {
                        char c = cCharAt;
                        int lowerCase = (Character.isDigit(c) ? cCharAt - '0' : (Character.toLowerCase(c) + '\n') - 97) & 15;
                        int i6 = i5 + 1;
                        char cCharAt2 = str.charAt(i6);
                        if ((cCharAt2 >= 'a' && cCharAt2 <= 'f') || ((cCharAt2 >= '0' && cCharAt2 <= '9') || (cCharAt2 >= 'A' && cCharAt2 <= 'F'))) {
                            iCharAt = (lowerCase << 4) | ((Character.isDigit(cCharAt2) ? cCharAt2 - '0' : (Character.toLowerCase(r8) + '\n') - 97) & 15);
                            i = i6;
                            z = true;
                        }
                    }
                    return str;
                }
                i = i2;
                z = false;
                if (!z) {
                    stringBuffer.append((char) iCharAt);
                } else if ((iCharAt & 192) == 128) {
                    int i7 = (i3 << 6) | (iCharAt & 63);
                    i4--;
                    if (i4 == 0) {
                        stringBuffer.append((char) i7);
                    }
                    i3 = i7;
                } else if ((iCharAt & 128) == 0) {
                    stringBuffer.append((char) iCharAt);
                } else if ((iCharAt & 224) == 192) {
                    i3 = iCharAt & 31;
                    i4 = 1;
                } else if ((iCharAt & 240) == 224) {
                    i3 = iCharAt & 15;
                    i4 = 2;
                } else if ((iCharAt & 248) == 240) {
                    i3 = iCharAt & 7;
                    i4 = 3;
                } else if ((iCharAt & CameraShowActivity.KEY_DOWN_TO_ZOOM) == 248) {
                    i3 = iCharAt & 3;
                    i4 = 4;
                } else {
                    i3 = iCharAt & 1;
                    i4 = 5;
                }
                i2 = i + 1;
            }
            return stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return str;
        }
    }

    public static String getURLCodeInfoFromUTF8(String str) {
        byte[] bytes;
        try {
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < str.length(); i++) {
                char cCharAt = str.charAt(i);
                if (cCharAt < 0 || cCharAt > 127) {
                    try {
                        bytes = String.valueOf(cCharAt).getBytes("utf-8");
                    } catch (Exception e) {
                        System.out.println(e);
                        bytes = new byte[0];
                    }
                    for (int i2 : bytes) {
                        if (i2 < 0) {
                            i2 += 256;
                        }
                        stringBuffer.append("%" + Integer.toHexString(i2).toUpperCase());
                    }
                } else if ((cCharAt >= '0' && cCharAt <= '9') || ((cCharAt >= 'a' && cCharAt <= 'z') || ((cCharAt >= 'A' && cCharAt <= 'Z') || cCharAt == '/' || cCharAt == '.' || cCharAt == '_' || cCharAt == '-'))) {
                    stringBuffer.append(cCharAt);
                } else {
                    stringBuffer.append("%" + Integer.toHexString(cCharAt & 255).toUpperCase());
                }
            }
            return stringBuffer.toString();
        } catch (Exception e2) {
            e2.printStackTrace();
            return str;
        }
    }

    public static String replaceSpecialStingFormString(String str, String str2, String str3) {
        if (str == null || str2 == null || str3 == null) {
            return null;
        }
        int length = str2.length();
        int iIndexOf = str.indexOf(str2);
        while (iIndexOf != -1) {
            str = str.substring(0, iIndexOf) + str3 + str.substring(iIndexOf + length);
            iIndexOf = str.indexOf(str2);
        }
        return str;
    }

    public static String getURLCodeInfoForSpecialChar(String str) {
        return replaceSpecialStingFormString(str, "%2F", "/");
    }

    public static String getInfoUTF8toStr(String str) {
        try {
            return URLDecoder.decode(str, Key.STRING_CHARSET_NAME);
        } catch (Exception unused) {
            return str;
        }
    }

    public static String strSpaceConversionTo20(String str) {
        return !TextUtils.isEmpty(str) ? str.replace(" ", "%20") : "";
    }

    public static String encode(String str) {
        byte[] bytes = str.getBytes();
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            sb.append(hexString.charAt((bytes[i] & 240) >> 4));
            sb.append(hexString.charAt((bytes[i] & 15) >> 0));
        }
        return sb.toString();
    }

    public static String decode(String str) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(str.length() / 2);
        for (int i = 0; i < str.length(); i += 2) {
            byteArrayOutputStream.write((hexString.indexOf(str.charAt(i)) << 4) | hexString.indexOf(str.charAt(i + 1)));
        }
        return new String(byteArrayOutputStream.toByteArray());
    }

    public static String getFileLastModifiedTime(File file) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date(file.lastModified()));
    }

    public static String getTimeFromLong(long j) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date(j));
    }

    public static String getMsecTimeFromLong(long j) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.getDefault()).format(new Date(j));
    }

    public static String getDeteilTimeFromLong(long j) {
        return new SimpleDateFormat("HH:mm MM/dd/yyyy", Locale.getDefault()).format(new Date(j));
    }

    public static String getTimeTag(String str) {
        if (str.contains("T")) {
            return str.substring(0, str.indexOf("T"));
        }
        return str.contains(" ") ? str.substring(0, str.indexOf(" ")) : str;
    }

    public static String modifyTimeShowStyle(String str) {
        String string;
        String[] strArrSplit = str.split(" ");
        if (strArrSplit.length == 2) {
            return str.replace("-", "/");
        }
        if (strArrSplit.length != 6) {
            return str;
        }
        String str2 = strArrSplit[1];
        String str3 = strArrSplit[2];
        String str4 = strArrSplit[3];
        String str5 = strArrSplit[4];
        int i = 0;
        while (true) {
            String[] strArr = months;
            if (i >= strArr.length || str3.equals(strArr[i])) {
                break;
            }
            i++;
        }
        if (i < 10) {
            string = "0" + (i + 1);
        } else {
            string = Integer.toString(i);
        }
        if (str2.length() == 1) {
            str2 = str2 + "0";
        }
        return str4 + "/" + string + "/" + str2 + " " + str5;
    }

    public static String formatGMTDate(String str) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            return simpleDateFormat.format(new Date(str));
        } catch (Throwable unused) {
            return str;
        }
    }

    public static long getPaserTime(String str) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    public static String cachePathMD5ValueForKey(String str) {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("String to encript cannot be null or zero length");
        }
        StringBuffer stringBuffer = new StringBuffer();
        try {
            if (mMessageDigest == null) {
                mMessageDigest = MessageDigest.getInstance("MD5");
            }
            mMessageDigest.update(str.getBytes());
            byte[] bArrDigest = mMessageDigest.digest();
            for (int i = 0; i < bArrDigest.length; i++) {
                if ((bArrDigest[i] & 255) < 16) {
                    stringBuffer.append("0" + Integer.toHexString(bArrDigest[i] & 255));
                } else {
                    stringBuffer.append(Integer.toHexString(bArrDigest[i] & 255));
                }
            }
        } catch (NoSuchAlgorithmException unused) {
        }
        return stringBuffer.toString();
    }

    public static long getCardMemorySize(String str) {
        if (!new File(str).exists()) {
            createFolderInSdcard(str);
        }
        StatFs statFs = new StatFs(str);
        return ((long) statFs.getAvailableBlocks()) * ((long) statFs.getBlockSize());
    }

    public static String FormetFileSize(String str) {
        String str2;
        try {
            DecimalFormat decimalFormat = new DecimalFormat("#.0");
            long j = str.equals("") ? 0L : Long.parseLong(str);
            if (j < 1024) {
                str2 = decimalFormat.format(j) + "B";
            } else if (j < 1048576) {
                str2 = decimalFormat.format(j / 1024.0d) + "KB";
            } else if (j < 1073741824) {
                str2 = decimalFormat.format(j / 1048576.0d) + "MB";
            } else {
                str2 = decimalFormat.format(j / 1.073741824E9d) + "GB";
            }
            String str3 = str2;
            if (!str3.startsWith(".")) {
                return str3;
            }
            return "0" + str3;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getFileType(String str) {
        int iLastIndexOf = str.lastIndexOf(".");
        if (iLastIndexOf < 0) {
            return null;
        }
        return str.substring(iLastIndexOf, str.length());
    }

    public static String getExternalStoragePath() {
        if ("mounted".equals(Environment.getExternalStorageState()) && Environment.getExternalStorageDirectory().canWrite()) {
            return Environment.getExternalStorageDirectory().getPath();
        }
        return null;
    }

    public static long getAvailableStore(String str) {
        StatFs statFs = new StatFs(str);
        return ((long) statFs.getAvailableBlocks()) * ((long) statFs.getBlockSize());
    }

    public static String getFileTypeWithNoNod(String str) {
        int iLastIndexOf = str.lastIndexOf(".");
        if (iLastIndexOf < 0) {
            return null;
        }
        return str.substring(iLastIndexOf + 1, str.length());
    }

    public static void hideSoftKeyboard(Context context, IBinder iBinder) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService("input_method");
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(iBinder, 0);
        }
    }

    public static void showKeyboard(final EditText editText) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ((InputMethodManager) editText.getContext().getSystemService("input_method")).showSoftInput(editText, 0);
            }
        }, 1000L);
    }

    public static String getUTF8Info2(String str) {
        StringBuilder sb = new StringBuilder();
        while (true) {
            int iIndexOf = str.indexOf(32);
            if (iIndexOf != -1) {
                try {
                    sb.append(URLEncoder.encode(str.substring(0, iIndexOf), Key.STRING_CHARSET_NAME));
                } catch (UnsupportedEncodingException unused) {
                }
                sb.append("%20");
                str = str.substring(iIndexOf + 1);
            } else {
                try {
                    break;
                } catch (UnsupportedEncodingException unused2) {
                }
            }
        }
        sb.append(URLEncoder.encode(str, Key.STRING_CHARSET_NAME));
        return sb.toString();
    }

    public static Bitmap getLocalMusicBitmap(String str) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(str);
        return Bytes2Bimap(mediaMetadataRetriever.getEmbeddedPicture());
    }

    public static Bitmap getLocalVideoBitmap(String str) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(str);
        return mediaMetadataRetriever.getFrameAtTime();
    }

    public static Bitmap Bytes2Bimap(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        return BitmapFactory.decodeByteArray(bArr, 0, bArr.length);
    }

    public static String FormetkbTo(String str) {
        DecimalFormat decimalFormat = new DecimalFormat("#.0");
        long j = str.equals("") ? 0L : Long.parseLong(str);
        if (j == 0) {
            return "0MB";
        }
        if (j < 1024) {
            return decimalFormat.format(j) + "MB";
        }
        if (j < 1048576) {
            return decimalFormat.format(j / 1024.0d) + "GB";
        }
        if (j >= 1073741824) {
            return "";
        }
        return decimalFormat.format(j / 1048576.0d) + "TB";
    }

    public static boolean isMatchValidator(String str) {
        return str.matches("^[\\sA-Za-z0-9-]+$");
    }

    public static boolean isMatchValidator3(String str) {
        return str.matches("^[A-Za-z0-9]+$");
    }

    public static boolean isMatchValidator2(String str) {
        boolean zMatches = str.matches("^[A-Za-z0-9-]+$");
        return zMatches ? str.substring(0, 1).matches("^[A-Za-z]+$") : zMatches;
    }

    public static String changeSecurityPwd(String str) {
        return (str == null || str.equals("")) ? str : (str.contains("CDATA") || str.contains("[[")) ? str.substring(8, str.indexOf("]]")) : str;
    }

    private static boolean copyFile(String str, String str2) {
        try {
            if (!new File(str).exists()) {
                return true;
            }
            FileInputStream fileInputStream = new FileInputStream(str);
            FileOutputStream fileOutputStream = new FileOutputStream(str2);
            byte[] bArr = new byte[1444];
            while (true) {
                int i = fileInputStream.read(bArr);
                if (i != -1) {
                    fileOutputStream.write(bArr, 0, i);
                } else {
                    fileInputStream.close();
                    fileOutputStream.close();
                    return true;
                }
            }
        } catch (Exception unused) {
            return false;
        }
    }

    public static float pixelsToDp(float f, Context context) {
        return (int) ((f / context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int getStatusBarHeight(Context context) {
        try {
            Class<?> cls = Class.forName("com.android.internal.R$dimen");
            return context.getResources().getDimensionPixelSize(Integer.parseInt(cls.getField("status_bar_height").get(cls.newInstance()).toString()));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        return resources.getDimensionPixelSize(resources.getIdentifier("navigation_bar_height", "dimen", "android"));
    }

    public static boolean checkDeviceHasNavigationBar(Context context) {
        Resources resources = context.getResources();
        int identifier = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        boolean z = identifier > 0 ? resources.getBoolean(identifier) : false;
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            String str = (String) cls.getMethod("get", String.class).invoke(cls, "qemu.hw.mainkeys");
            if ("1".equals(str)) {
                return false;
            }
            if ("0".equals(str)) {
                return true;
            }
            return z;
        } catch (Exception unused) {
            return z;
        }
    }

    public static String getPrefixFromName(String str) {
        return str.substring(0, str.lastIndexOf("."));
    }

    public static String getExtensionFromSuffdix(String str) {
        return str.substring(str.lastIndexOf("."));
    }

    public static void setCursorLocation(CharSequence charSequence) {
        if (charSequence instanceof Spannable) {
            Selection.setSelection((Spannable) charSequence, charSequence.length());
        }
    }

    public static void showEditTextPwd(EditText editText) {
        editText.setInputType(BaseCameraView.REFLASH_TIME);
    }

    public static void hideEditTextPwd(EditText editText) {
        editText.setInputType(BaseCameraView.REFLASH_TIME);
    }

    public static String getSpecialFromName(String str) {
        return str.substring(str.lastIndexOf("/") + 1);
    }

    public static void bindWifi(Context context) {
        if (Build.VERSION.SDK_INT < 23 || context == null) {
            return;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        for (Network network : connectivityManager.getAllNetworks()) {
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
            if (networkCapabilities != null) {
                if (networkCapabilities == null || !networkCapabilities.hasTransport(1)) {
                    return;
                }
                connectivityManager.bindProcessToNetwork(network);
                return;
            }
        }
    }

    public static void bindWifi2(Context context) {
        if (Build.VERSION.SDK_INT >= 21) {
            final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            builder.addTransportType(1);
            connectivityManager.requestNetwork(builder.build(), new ConnectivityManager.NetworkCallback() {
                static final boolean $assertionsDisabled = false;

                @Override
                public void onAvailable(Network network) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        connectivityManager.bindProcessToNetwork(network);
                    } else {
                        ConnectivityManager.setProcessDefaultNetwork(network);
                    }
                }
            });
        }
    }

    public static void unBindWifi(Context context) {
        if (Build.VERSION.SDK_INT < 23 || context == null) {
            return;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        for (Network network : connectivityManager.getAllNetworks()) {
            if (connectivityManager.getNetworkCapabilities(network) != null) {
                connectivityManager.bindProcessToNetwork(network);
                return;
            }
        }
    }

    public static final void startDefineUrl(Context context, String str) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setData(Uri.parse(str));
        context.startActivity(intent);
    }

    public static String getExtensionFromName(String str) {
        return str.substring(str.lastIndexOf(".") + 1);
    }

    public static void setTranslucentStatus(Activity activity, boolean z) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        if (z) {
            attributes.flags |= 67108864;
        } else {
            attributes.flags &= -67108865;
        }
        window.setAttributes(attributes);
    }

    public static int generateViewId() {
        int i;
        int i2;
        do {
            i = sNextGeneratedId.get();
            i2 = i + 1;
            if (i2 > 16777215) {
                i2 = 1;
            }
        } while (!sNextGeneratedId.compareAndSet(i, i2));
        return i;
    }

    public static Camera getDefaultCameraInstance() {
        return Camera.open();
    }

    public static Camera.Size getOptimalVideoSize(List<Camera.Size> list, List<Camera.Size> list2, int i, int i2) {
        double d = ((double) i) / ((double) i2);
        List<Camera.Size> list3 = list != null ? list : list2;
        Camera.Size size = null;
        double dAbs = Double.MAX_VALUE;
        double dAbs2 = Double.MAX_VALUE;
        for (Camera.Size size2 : list3) {
            if (Math.abs((((double) size2.width) / ((double) size2.height)) - d) <= 0.1d && Math.abs(size2.height - i2) < dAbs2 && list2.contains(size2)) {
                dAbs2 = Math.abs(size2.height - i2);
                size = size2;
            }
        }
        if (size == null) {
            for (Camera.Size size3 : list3) {
                if (Math.abs(size3.height - i2) < dAbs && list2.contains(size3)) {
                    dAbs = Math.abs(size3.height - i2);
                    size = size3;
                }
            }
        }
        return size;
    }

    public static boolean FlymeSetStatusBarLightMode(Window window, boolean z) {
        if (window != null) {
            try {
                WindowManager.LayoutParams attributes = window.getAttributes();
                Field declaredField = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field declaredField2 = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                declaredField.setAccessible(true);
                declaredField2.setAccessible(true);
                int i = declaredField.getInt(null);
                int i2 = declaredField2.getInt(attributes);
                declaredField2.setInt(attributes, z ? i2 | i : (~i) & i2);
                window.setAttributes(attributes);
                return true;
            } catch (Exception unused) {
            }
        }
        return false;
    }

    public static boolean MIUISetStatusBarLightMode(Window window, boolean z) {
        if (window != null) {
            Class<?> cls = window.getClass();
            try {
                Class<?> cls2 = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                int i = cls2.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE").getInt(cls2);
                Method method = cls.getMethod("setExtraFlags", Integer.TYPE, Integer.TYPE);
                if (z) {
                    method.invoke(window, Integer.valueOf(i), Integer.valueOf(i));
                } else {
                    method.invoke(window, 0, Integer.valueOf(i));
                }
                return true;
            } catch (Exception unused) {
            }
        }
        return false;
    }

    public static void showToast(Context context, String str) {
        try {
            if (mToast == null) {
                mToast = Toast.makeText(context.getApplicationContext(), str, 0);
            } else {
                mToast.setText(str);
            }
            mToast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isAppOnForeground(Context context, Class cls) {
        ComponentName componentNameResolveActivity = new Intent(context, (Class<?>) cls).resolveActivity(context.getPackageManager());
        if (componentNameResolveActivity == null) {
            return false;
        }
        Iterator<ActivityManager.RunningTaskInfo> it = ((ActivityManager) context.getSystemService("activity")).getRunningTasks(10).iterator();
        while (it.hasNext()) {
            if (it.next().baseActivity.equals(componentNameResolveActivity)) {
                return true;
            }
        }
        return false;
    }

    public static String getFileSize(long j) {
        if (j < 1024) {
            return String.valueOf(j) + "B";
        }
        long j2 = j / 1024;
        if (j2 < 1024) {
            return String.valueOf(j2) + "KB";
        }
        long j3 = j2 / 1024;
        if (j3 < 1024) {
            long j4 = j3 * 100;
            return String.valueOf(j4 / 100) + "." + String.valueOf(j4 % 100) + "MB";
        }
        long j5 = (j3 * 100) / 1024;
        return String.valueOf(j5 / 100) + "." + String.valueOf(j5 % 100) + "GB";
    }

    public static String encrypt(String str, String str2, String str3) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            int blockSize = cipher.getBlockSize();
            byte[] bytes = str2.getBytes();
            int length = bytes.length;
            if (length % blockSize != 0) {
                length += blockSize - (length % blockSize);
            }
            byte[] bArr = new byte[length];
            System.arraycopy(bytes, 0, bArr, 0, bytes.length);
            cipher.init(1, new SecretKeySpec(str.getBytes(), "AES"), new IvParameterSpec(str3.getBytes()));
            return bytes2hex02(cipher.doFinal(bArr));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String bytes2hex02(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bArr) {
            String hexString2 = Integer.toHexString(b & 255);
            if (hexString2.length() == 1) {
                hexString2 = "0" + hexString2;
            }
            sb.append(hexString2);
        }
        return sb.toString();
    }

    public static boolean isMeizuFlymeOS() {
        String systemProperty = getSystemProperty("ro.build.display.id", "");
        if (TextUtils.isEmpty(systemProperty)) {
            return false;
        }
        return systemProperty.contains("flyme") || systemProperty.toLowerCase().contains("flyme");
    }

    private static String getSystemProperty(String str, String str2) {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            return (String) cls.getMethod("get", String.class, String.class).invoke(cls, str, str2);
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException unused) {
            return null;
        }
    }

    public static boolean isZFBInstall(Context context) {
        List<PackageInfo> installedPackages = context.getPackageManager().getInstalledPackages(0);
        if (installedPackages == null) {
            return false;
        }
        boolean z = false;
        for (int i = 0; i < installedPackages.size(); i++) {
            if (installedPackages.get(i).packageName.equals("com.eg.android.AlipayGphone")) {
                z = true;
            }
        }
        return z;
    }

    public static boolean isScreenOFF() {
        return !((PowerManager) WDApplication.getInstance().getSystemService("power")).isScreenOn();
    }

    public static void sendNotification(Context context, Class cls, String str, String str2) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        Intent intent = new Intent(context, (Class<?>) cls);
        intent.setFlags(337641472);
        PendingIntent activity = PendingIntent.getActivity(context, 0, intent, 67108864);
        long[] jArr = {0, 500, 1000, 1500};
        if (Build.VERSION.SDK_INT >= 26) {
            Notification.Builder sound = new Notification.Builder(context).setContentTitle(str).setContentText(str2).setWhen(System.currentTimeMillis()).setSmallIcon(C0413R.mipmap.ic_launcher).setLargeIcon(BitmapFactory.decodeResource(context.getResources(), C0413R.mipmap.ic_launcher)).setVibrate(jArr).setContentIntent(activity).setPriority(1).setDefaults(-1).setChannelId(context.getPackageName()).setSound(MediaStore.Audio.Media.INTERNAL_CONTENT_URI);
            notificationManager.createNotificationChannel(new NotificationChannel(context.getPackageName(), Strings.getString(C0413R.string.app_name, context), 3));
            notificationManager.notify(0, sound.build());
            return;
        }
        notificationManager.notify(0, new Notification.Builder(context).setContentTitle(str).setContentText(str2).setWhen(System.currentTimeMillis()).setSmallIcon(C0413R.mipmap.ic_launcher).setLargeIcon(BitmapFactory.decodeResource(context.getResources(), C0413R.mipmap.ic_launcher)).setVibrate(jArr).setContentIntent(activity).setWhen(System.currentTimeMillis()).setPriority(1).setDefaults(-1).setSound(MediaStore.Audio.Media.INTERNAL_CONTENT_URI).build());
    }

    public static double getRoundDecimal(long j, long j2, int i) {
        try {
            return new BigDecimal(Double.toString(j)).divide(new BigDecimal(Double.toString(j2)), i, 4).doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0d;
        }
    }

    public static int px2dp(Context context, float f) {
        return (int) ((f / context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int dp2px(Context context, float f) {
        return (int) ((f * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int getUpCurrentWiFiHotSpotStrengthCount() {
        WifiManager wifiManager2 = (WifiManager) WDApplication.getInstance().getApplicationContext().getSystemService("wifi");
        WifiInfo connectionInfo = wifiManager2.getConnectionInfo();
        List<ScanResult> scanResults = wifiManager2.getScanResults();
        String ssid = connectionInfo.getSSID();
        String strSubstring = ssid.substring(1, ssid.length() - 1);
        int iAbs = 0;
        for (ScanResult scanResult : scanResults) {
            if (strSubstring.equals(scanResult.SSID)) {
                iAbs = Math.abs(scanResult.level);
            }
        }
        int i = 0;
        for (int i2 = 0; i2 < scanResults.size(); i2++) {
            if (iAbs >= Math.abs(scanResults.get(i2).level)) {
                i++;
            }
        }
        return i;
    }

    public static boolean getAppIsRunning(Context context, Class cls) {
        ComponentName componentNameResolveActivity = new Intent(context, (Class<?>) cls).resolveActivity(context.getPackageManager());
        if (componentNameResolveActivity == null) {
            return false;
        }
        Iterator<ActivityManager.RunningTaskInfo> it = ((ActivityManager) context.getSystemService("activity")).getRunningTasks(10).iterator();
        while (it.hasNext()) {
            if (it.next().baseActivity.equals(componentNameResolveActivity)) {
                return true;
            }
        }
        return false;
    }

    public static void showResultToast(Context context, boolean z) {
        int i = z ? C0413R.drawable.ic_toast_ok : C0413R.drawable.ic_toast_error;
        I4SeasonToast i4SeasonToast2 = i4SeasonToast;
        if (i4SeasonToast2 == null) {
            i4SeasonToast = I4SeasonToast.makeText(context, i, 0);
            i4SeasonToast.setGravity(17, 0, 0);
        } else {
            i4SeasonToast2.setImagesrc(i);
        }
        i4SeasonToast.show();
    }

    public static boolean isAppForeground(Context context) {
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses();
        if (runningAppProcesses == null) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
            if (runningAppProcessInfo.processName.equals(context.getPackageName()) && runningAppProcessInfo.importance == 100) {
                return true;
            }
        }
        return false;
    }

    public static int getWordCount(String str) {
        return str.replaceAll("[^\\x00-\\xff]", "**").length();
    }

    public static boolean containsEmoji(String str) {
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (isEmojiCharacter(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static String acceptCurrentWifiId(Context context) {
        WifiManager wifiManager2;
        String strReplace;
        if (Build.VERSION.SDK_INT >= 23 && !PermissionInstans.getInstance().isHavaLocalPermission(context)) {
            return Constant.CANNOT_GET_SSID;
        }
        if ((Build.VERSION.SDK_INT >= 26 && !isLocationEnabled(context)) || (wifiManager2 = (WifiManager) context.getSystemService("wifi")) == null) {
            return Constant.CANNOT_GET_SSID;
        }
        if (!wifiManager2.isWifiEnabled()) {
            return "";
        }
        if (wifiManager2.isWifiEnabled()) {
            wifiManager2.getWifiState();
            WifiInfo connectionInfo = wifiManager2.getConnectionInfo();
            strReplace = connectionInfo != null ? connectionInfo.getSSID().replace("\"", "") : null;
        } else {
            strReplace = "";
        }
        return (TextUtils.isEmpty(strReplace) || !strReplace.contains("unknown ssid")) ? strReplace : "";
    }

    public static boolean checkGetSsidConditions(Context context) {
        if (Build.VERSION.SDK_INT < 23 || PermissionInstans.getInstance().isHavaLocalPermission(context)) {
            return Build.VERSION.SDK_INT < 26 || isLocationEnabled(context);
        }
        return false;
    }

    public static void openGPS(boolean z, Context context) {
        Intent intent = new Intent();
        intent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
        intent.addCategory("android.intent.category.ALTERNATIVE");
        intent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, intent, 67108864).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    public static boolean isGpsOpen(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService("location");
        return locationManager.isProviderEnabled("gps") || locationManager.isProviderEnabled("network");
    }

    public static boolean isLocationEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= 19) {
            try {
                return Settings.Secure.getInt(context.getContentResolver(), "location_mode") != 0;
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        }
        return !TextUtils.isEmpty(Settings.Secure.getString(context.getContentResolver(), "location_providers_allowed"));
    }

    public static String getWifiRouteIPAddress(Context context) {
        String ipAddress = Formatter.formatIpAddress(((WifiManager) context.getSystemService("wifi")).getDhcpInfo().gateway);
        return TextUtils.isEmpty(ipAddress) ? "0.0.0.0" : ipAddress;
    }

    public static String getWifiLocalIPAddress(Context context) {
        return Formatter.formatIpAddress(((WifiManager) context.getSystemService("wifi")).getDhcpInfo().ipAddress);
    }

    public static void playTakePhotoAudio(Context context) {
        SoundPool soundPool2 = soundPool;
        if (soundPool2 == null) {
            if (Build.VERSION.SDK_INT >= 21) {
                SoundPool.Builder builder = new SoundPool.Builder();
                builder.setMaxStreams(1);
                AudioAttributes.Builder builder2 = new AudioAttributes.Builder();
                builder2.setLegacyStreamType(3);
                builder.setAudioAttributes(builder2.build());
                soundPool = builder.build();
            } else {
                soundPool = new SoundPool(1, 1, 5);
            }
            soundPool.load(context, C0413R.raw.take_photo, 1);
            soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool3, int i, int i2) {
                    soundPool3.play(1, 1.0f, 1.0f, 0, 0, 1.0f);
                }
            });
            return;
        }
        soundPool2.play(1, 1.0f, 1.0f, 0, 0, 1.0f);
    }

    public static void playTakeVideoAudio(Context context) {
        SoundPool soundPool2 = videoSoundPool;
        if (soundPool2 == null) {
            if (Build.VERSION.SDK_INT >= 21) {
                SoundPool.Builder builder = new SoundPool.Builder();
                builder.setMaxStreams(1);
                AudioAttributes.Builder builder2 = new AudioAttributes.Builder();
                builder2.setLegacyStreamType(3);
                builder.setAudioAttributes(builder2.build());
                videoSoundPool = builder.build();
            } else {
                videoSoundPool = new SoundPool(1, 1, 5);
            }
            videoSoundPool.load(context, C0413R.raw.take_video, 1);
            videoSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool3, int i, int i2) {
                    soundPool3.play(1, 1.0f, 1.0f, 0, 0, 1.0f);
                }
            });
            return;
        }
        soundPool2.play(1, 1.0f, 1.0f, 0, 0, 1.0f);
    }

    public static byte[] hexString2Bytes(String str) {
        if (str == null || str.equals("") || str.length() % 2 != 0) {
            return null;
        }
        String upperCase = str.toUpperCase();
        int length = upperCase.length() / 2;
        byte[] bArr = new byte[length];
        char[] charArray = upperCase.toCharArray();
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            bArr[i] = (byte) (charToByte(charArray[i2 + 1]) | (charToByte(charArray[i2]) << 4));
        }
        return bArr;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static String encoderGbk(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        try {
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : str.getBytes("gbk")) {
                stringBuffer.append(Integer.toHexString(b & 255));
            }
            return stringBuffer.toString().toUpperCase();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return str;
        }
    }

    public static boolean checkActivityIsOnTop(Context context, String str) {
        return ((ActivityManager) context.getSystemService("activity")).getRunningTasks(1).get(0).topActivity.getClassName().equals(str);
    }

    public static String getConnectedWifiId(Context context) {
        String str;
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                if (connMgr == null) {
                    connMgr = (ConnectivityManager) context.getSystemService("connectivity");
                }
                str = connMgr.getActiveNetwork().hashCode() + "";
            } else {
                if (connMgr == null) {
                    wifiManager = (WifiManager) context.getSystemService("wifi");
                }
                str = wifiManager.getConnectionInfo().getNetworkId() + "";
            }
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getWifiModuleMac(Context context) {
        WifiManager wifiManager2 = (WifiManager) context.getSystemService("wifi");
        if (wifiManager2 == null) {
            return null;
        }
        WifiInfo connectionInfo = wifiManager2.getConnectionInfo();
        if (connectionInfo != null && "02:00:00:00:00:00".equals(connectionInfo.getMacAddress())) {
            try {
                String wifiModuleMacByInterface = getWifiModuleMacByInterface();
                return !TextUtils.isEmpty(wifiModuleMacByInterface) ? wifiModuleMacByInterface : getWifiModuleMacByFile(wifiManager2);
            } catch (Exception unused) {
                return "02:00:00:00:00:00";
            }
        }
        if (connectionInfo == null || connectionInfo.getMacAddress() == null) {
            return null;
        }
        return connectionInfo.getMacAddress();
    }

    private static String getWifiModuleMacByInterface() {
        try {
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (networkInterface.getName().equalsIgnoreCase("wlan0")) {
                    byte[] hardwareAddress = networkInterface.getHardwareAddress();
                    if (hardwareAddress == null) {
                        return null;
                    }
                    StringBuilder sb = new StringBuilder();
                    for (byte b : hardwareAddress) {
                        sb.append(String.format("%02X:", Byte.valueOf(b)));
                    }
                    if (sb.length() > 0) {
                        sb.deleteCharAt(sb.length() - 1);
                    }
                    return sb.toString();
                }
            }
            return null;
        } catch (Exception unused) {
            return null;
        }
    }

    private static String getWifiModuleMacByFile(WifiManager wifiManager2) throws Exception {
        int wifiState = wifiManager2.getWifiState();
        wifiManager2.setWifiEnabled(true);
        FileInputStream fileInputStream = new FileInputStream(new File("/sys/class/net/wlan0/address"));
        String stringFromStream = getStringFromStream(fileInputStream);
        fileInputStream.close();
        wifiManager2.setWifiEnabled(3 == wifiState);
        return stringFromStream;
    }

    private static String getStringFromStream(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return null;
        }
        StringWriter stringWriter = new StringWriter();
        char[] cArr = new char[2048];
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            while (true) {
                int i = bufferedReader.read(cArr);
                if (i != -1) {
                    stringWriter.write(cArr, 0, i);
                } else {
                    inputStream.close();
                    return stringWriter.toString();
                }
            }
        } catch (Throwable th) {
            inputStream.close();
            throw th;
        }
    }

    public static int getCurrentSelectedIndex() {
        return selected_index;
    }

    public static void setSelectedIndex(int i) {
        selected_index = i;
    }

    public static long getFilePathToMediaID(String str, Context context) {
        Cursor cursorQuery = context.getContentResolver().query(MediaStore.Files.getContentUri("external"), new String[]{"_id"}, "_data=?", new String[]{str}, null);
        long j = 0;
        if (cursorQuery != null) {
            while (cursorQuery.moveToNext()) {
                j = Long.parseLong(cursorQuery.getString(cursorQuery.getColumnIndex("_id")));
            }
        }
        return j;
    }
}
