package com.i4season.bkCamera.logicrelated.camera.reportlic;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import com.i4season.bkCamera.logicrelated.camera.CameraConstant;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.Constant;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.SpUtils;
import com.i4season.bkCamera.uirelated.other.logmanage.LogWD;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;

public class ReportLicInstance {
    private boolean IS_REQREST_OK = false;
    private IReportDelegate iReportDelegate;
    private ConnectivityManager mConnectivityManager;
    private I4seasonNetworkCallback mI4seasonNetworkCallback;
    private SpUtils mSpUtils;

    public static class ReportLicInstanceWDHolder {
        public static ReportLicInstance logManager = new ReportLicInstance();
    }

    public static ReportLicInstance getInstance() {
        return ReportLicInstanceWDHolder.logManager;
    }

    public boolean licProcessBegin(Context context, String str, String str2, String str3, String str4, String str5, String str6, IReportDelegate iReportDelegate) {
        if (this.mSpUtils == null) {
            this.mSpUtils = new SpUtils(context);
        }
        this.iReportDelegate = iReportDelegate;
        this.IS_REQREST_OK = false;
        if (!TextUtils.isEmpty(str)) {
            LogWD.writeMsg(this, 16777215, "有lic 本地lic禁用标志");
            boolean z = this.mSpUtils.getBoolean(str, true);
            LogWD.writeMsg(this, 16777215, "带lic上报 更新禁用标志");
            chenckLicReport(context, str, str2, str3, str4, str5, str6);
            return z;
        }
        LogWD.writeMsg(this, 16777215, "无lic 本地缓存lic");
        if (TextUtils.isEmpty(this.mSpUtils.getString(CameraConstant.LOCAL_LIC + str3 + str2, ""))) {
            LogWD.writeMsg(this, 16777215, "本地禁用标志");
            boolean z2 = this.mSpUtils.getBoolean(str4 + str5 + str3 + str2, true);
            LogWD.writeMsg(this, 16777215, "不带lic上报 更新禁用标志本地禁用标志");
            chenckLicReport(context, "", str2, str3, str4, str5, str6);
            return z2;
        }
        cameraCheckOnline();
        return true;
    }

    private void cameraCheckOnline() {
        this.iReportDelegate.cameraCheckOnline();
    }

    public void cameraCheckOnlineBack(int i, String str, String str2) {
        LogWD.writeMsg(this, 16777215, "cameraCheckOnlineBack: " + i);
        if (i == 0) {
            this.iReportDelegate.brunLicBegin(this.mSpUtils.getString(CameraConstant.LOCAL_LIC + str2 + str, ""));
            LogWD.writeMsg(this, 16777215, "烧录不清除lic");
        }
    }

    public void chenckLicReport(final Context context, final String str, final String str2, final String str3, final String str4, final String str5, final String str6) {
        new Thread() {
            @Override
            public void run() {
                ReportLicInstance.this.mConnectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
                NetworkRequest.Builder builder = new NetworkRequest.Builder();
                builder.addCapability(12);
                builder.addTransportType(0);
                builder.removeTransportType(1);
                NetworkRequest networkRequestBuild = builder.build();
                if (ReportLicInstance.this.mI4seasonNetworkCallback == null) {
                    ReportLicInstance reportLicInstance = ReportLicInstance.this;
                    reportLicInstance.mI4seasonNetworkCallback = reportLicInstance.new I4seasonNetworkCallback(context, str, str2, str3, str4, str5, str6);
                }
                ReportLicInstance.this.mConnectivityManager.requestNetwork(networkRequestBuild, ReportLicInstance.this.mI4seasonNetworkCallback);
                SystemClock.sleep(Constant.RECORD_DEAFAULT_TIME);
                if (ReportLicInstance.this.IS_REQREST_OK) {
                    return;
                }
                ReportLicInstance.this.requestNetwork2Wifi(context, str, str2, str3, str4, str5, str6);
            }
        }.start();
    }

    private class I4seasonNetworkCallback extends ConnectivityManager.NetworkCallback {
        private Context context;
        private String lic;
        private String mac;
        private String model;

        private String f63sn;
        private String vender;
        private String version;

        public I4seasonNetworkCallback(Context context, String str, String str2, String str3, String str4, String str5, String str6) {
            this.context = context;
            this.lic = str;
            this.f63sn = str2;
            this.mac = str3;
            this.vender = str4;
            this.model = str5;
            this.version = str6;
        }

        @Override
        public void onAvailable(Network network) throws Throwable {
            super.onAvailable(network);
            LogWD.writeMsg(this, 16777215, "4G上报条件允许: onAvailable");
            if (ReportLicInstance.this.IS_REQREST_OK) {
                return;
            }
            ReportLicInstance.this.IS_REQREST_OK = true;
            ReportLicInstance.this.requestLicCheck(network, this.context, this.lic, this.f63sn, this.mac, this.vender, this.model, this.version);
        }
    }

    public void requestNetwork2Wifi(Context context, String str, String str2, String str3, String str4, String str5, String str6) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL("https://yun.simicloud.com/licplatform/user/v2/report").openConnection();
            httpURLConnection.setConnectTimeout(Constant.DISMISS_DELAY);
            httpURLConnection.setReadTimeout(60000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("content-type", "application/json");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(acceptJsonStr(context, str, str2, str3, str4, str5, str6).getBytes());
            InputStream inputStream = httpURLConnection.getInputStream();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer = new StringBuffer();
                while (true) {
                    String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    } else {
                        stringBuffer.append(line);
                    }
                }
                this.IS_REQREST_OK = true;
                if (this.mConnectivityManager != null && this.mI4seasonNetworkCallback != null) {
                    this.mConnectivityManager.unregisterNetworkCallback(this.mI4seasonNetworkCallback);
                    this.mI4seasonNetworkCallback = null;
                }
                JSONObject jSONObject = new JSONObject(stringBuffer.toString()).getJSONObject("data");
                boolean z = jSONObject.getBoolean("enable");
                int i = jSONObject.getInt("reportType");
                String string = jSONObject.getString("lic");
                if (i == 1) {
                    LogWD.writeMsg(this, 16777215, "不带lic上报");
                    if (TextUtils.isEmpty(string) || string.equals("null")) {
                        LogWD.writeMsg(this, 16777215, "为空不烧录 直接更新本地禁用标志");
                        this.mSpUtils.putBoolean(str4 + str5 + str3 + str2, z);
                        this.iReportDelegate.reportReult(z);
                    } else {
                        LogWD.writeMsg(this, 16777215, "不为空需要烧录 保存lic 更新本地lic禁用标志");
                        this.mSpUtils.putString(CameraConstant.LOCAL_LIC + str3 + str2, string);
                        this.mSpUtils.putBoolean(string, z);
                        LogWD.writeMsg(this, 16777215, "设备是否联通");
                        cameraCheckOnline();
                    }
                } else {
                    LogWD.writeMsg(this, 16777215, "带lic上报 更新本地lic禁用标志");
                    this.mSpUtils.putBoolean(string, z);
                    this.iReportDelegate.reportReult(z);
                }
                closeSilently(outputStream);
                closeSilently(inputStream);
            } catch (Throwable th) {
                closeSilently(outputStream);
                closeSilently(inputStream);
                throw th;
            }
        } catch (Exception e) {
            e.printStackTrace();
            SystemClock.sleep(Constant.RECORD_DEAFAULT_TIME);
            if (this.IS_REQREST_OK) {
                return;
            }
            requestNetwork2Wifi(context, str, str2, str3, str4, str5, str6);
        }
    }

    public void requestLicCheck(Network network, Context context, String str, String str2, String str3, String str4, String str5, String str6) throws Throwable {
        Throwable th;
        HttpURLConnection httpURLConnection;
        OutputStream outputStream;
        InputStream inputStream;
        BufferedReader bufferedReader;
        BufferedReader bufferedReader2 = null;
        try {
            try {
                httpURLConnection = (HttpURLConnection) network.openConnection(new URL("https://yun.simicloud.com/licplatform/user/v2/report"));
                try {
                    httpURLConnection.setConnectTimeout(Constant.DISMISS_DELAY);
                    httpURLConnection.setReadTimeout(60000);
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
                    httpURLConnection.setRequestProperty("content-type", "application/json");
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    outputStream = httpURLConnection.getOutputStream();
                    outputStream.write(acceptJsonStr(context, str, str2, str3, str4, str5, str6).getBytes());
                    inputStream = httpURLConnection.getInputStream();
                    try {
                        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        try {
                            StringBuffer stringBuffer = new StringBuffer();
                            while (true) {
                                String line = bufferedReader.readLine();
                                if (line == null) {
                                    break;
                                } else {
                                    stringBuffer.append(line);
                                }
                            }
                            this.IS_REQREST_OK = true;
                            if (this.mConnectivityManager != null && this.mI4seasonNetworkCallback != null) {
                                this.mConnectivityManager.unregisterNetworkCallback(this.mI4seasonNetworkCallback);
                                this.mI4seasonNetworkCallback = null;
                            }
                            JSONObject jSONObject = new JSONObject(stringBuffer.toString()).getJSONObject("data");
                            boolean z = jSONObject.getBoolean("enable");
                            int i = jSONObject.getInt("reportType");
                            String string = jSONObject.getString("lic");
                            if (i == 1) {
                                LogWD.writeMsg(this, 16777215, "不带lic上报");
                                if (TextUtils.isEmpty(string)) {
                                    LogWD.writeMsg(this, 16777215, "为空不烧录 直接更新本地禁用标志");
                                    this.mSpUtils.putBoolean(str4 + str5 + str3 + str2, z);
                                    this.iReportDelegate.reportReult(z);
                                } else {
                                    LogWD.writeMsg(this, 16777215, "不为空需要烧录 保存lic 更新本地lic禁用标志");
                                    this.mSpUtils.putString(CameraConstant.LOCAL_LIC + str3 + str2, string);
                                    this.mSpUtils.putBoolean(string, z);
                                    LogWD.writeMsg(this, 16777215, "设备是否联通");
                                    cameraCheckOnline();
                                }
                            } else {
                                LogWD.writeMsg(this, 16777215, "带lic上报 更新本地lic禁用标志");
                                this.mSpUtils.putBoolean(string, z);
                                this.iReportDelegate.reportReult(z);
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            bufferedReader2 = bufferedReader;
                            closeSilently(outputStream);
                            closeSilently(inputStream);
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                    }
                } catch (Exception e) {
                    e = e;
                }
            } catch (Throwable th4) {
                th = th4;
            }
            try {
                closeSilently(outputStream);
                closeSilently(inputStream);
                try {
                    bufferedReader.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                if (httpURLConnection == null) {
                    return;
                }
            } catch (Exception e3) {
                e = e3;
                bufferedReader2 = bufferedReader;
                e.printStackTrace();
                if (bufferedReader2 != null) {
                    try {
                        bufferedReader2.close();
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                }
                if (httpURLConnection == null) {
                    return;
                }
            } catch (Throwable th5) {
                th = th5;
                bufferedReader2 = bufferedReader;
                if (bufferedReader2 != null) {
                    try {
                        bufferedReader2.close();
                    } catch (IOException e5) {
                        e5.printStackTrace();
                    }
                }
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                    throw th;
                }
                throw th;
            }
        } catch (Exception e6) {
            e = e6;
            httpURLConnection = null;
        } catch (Throwable th6) {
            th = th6;
            httpURLConnection = null;
        }
        httpURLConnection.disconnect();
    }

    public final void closeSilently(Object obj) {
        if (obj != null) {
            try {
                if (obj instanceof Closeable) {
                    ((Closeable) obj).close();
                } else if (obj instanceof Socket) {
                    ((Socket) obj).close();
                } else {
                    if (obj instanceof ServerSocket) {
                        ((ServerSocket) obj).close();
                        return;
                    }
                    throw new IllegalArgumentException("Unknown object to close");
                }
            } catch (IOException unused) {
            }
        }
    }

    public String acceptJsonStr(Context context, String str, String str2, String str3, String str4, String str5, String str6) {
        String string = "";
        JSONObject jSONObject = new JSONObject();
        try {
            String string2 = Settings.System.getString(context.getContentResolver(), "android_id");
            if (TextUtils.isEmpty(string2)) {
                SpUtils spUtils = new SpUtils(context);
                String string3 = spUtils.getString(CameraConstant.RAMDOM_UUID, "");
                if (TextUtils.isEmpty(string3)) {
                    string3 = getRandomStraing();
                    spUtils.putString(CameraConstant.RAMDOM_UUID, string3);
                }
                string2 = "" + string3;
            }
            jSONObject.put("lic", str);
            jSONObject.put("uuid", string2);
            jSONObject.put("sn", str2);
            jSONObject.put("mac", str3);
            jSONObject.put("vendor", str4);
            jSONObject.put("product", str5);
            jSONObject.put("firmwareVersion", str6);
            jSONObject.put("appName", getAppName(context));
            jSONObject.put("appVersion", getVersionName(context));
            jSONObject.put("appPacketName", context.getPackageName());
            jSONObject.put("phoneProducer", Build.BRAND);
            jSONObject.put("phoneModel", Build.MODEL);
            jSONObject.put("phoneVersion", Build.VERSION.RELEASE);
            string = jSONObject.toString();
            LogWD.writeMsg(this, 16777215, "jsonStr: " + string);
            return string;
        } catch (JSONException e) {
            e.printStackTrace();
            return string;
        }
    }

    private String getRandomStraing() {
        String str = "";
        for (int i = 0; i < 20; i++) {
            str = str + String.valueOf((int) (Math.random() * 10.0d));
        }
        return str;
    }

    private String getAppName(Context context) {
        try {
            return context.getResources().getString(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.labelRes);
        } catch (Throwable th) {
            th.printStackTrace();
            return "";
        }
    }

    private String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
