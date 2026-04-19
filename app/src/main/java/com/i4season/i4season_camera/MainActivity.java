package com.i4season.i4season_camera;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jni.CallBack.WifiCallBack;
import com.jni.WifiCameraApi;
import com.jni.WifiCameraInfo.WifiCameraFirmInfo;
import com.jni.WifiCameraInfo.WifiCameraPic;
import com.jni.WifiCameraInfo.WifiCameraStatusInfo;
import com.jni.logmanageWifi.LogManagerWD;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends Activity implements WifiCallBack.DeviceStatusInterface {
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final AtomicBoolean started = new AtomicBoolean(false);
    private TextView statusText;
    private TextView wifiText;
    private TextView deviceText;
    private TextView frameText;
    private ImageView preview;
    private long frameCount;
    private long audioCount;
    private long lastPreviewMs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildUi();
        requestRuntimePermissions();
        updateWifiText();
        WifiCallBack.getInstance().setDeviceStatusInterface(this);
    }

    private void buildUi() {
        ScrollView scrollView = new ScrollView(this);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(18), dp(18), dp(18), dp(18));
        scrollView.addView(root);

        TextView title = new TextView(this);
        title.setText("UseeEar reconstructed");
        title.setTextSize(24);
        title.setGravity(Gravity.CENTER_HORIZONTAL);
        title.setTextColor(0xff0f2f35);
        root.addView(title, matchWrap());

        TextView hint = new TextView(this);
        hint.setText("Connect the phone to a UseeEar/i4season Wi-Fi network, then start the native camera stack.");
        hint.setTextSize(14);
        hint.setTextColor(0xff47666b);
        hint.setPadding(0, dp(8), 0, dp(14));
        root.addView(hint, matchWrap());

        wifiText = label(root, "Wi-Fi: checking...");
        statusText = label(root, "Status: idle");
        deviceText = label(root, "Device: none");
        frameText = label(root, "Frames: 0  Audio packets: 0");

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER);
        row.setPadding(0, dp(12), 0, dp(12));
        root.addView(row, matchWrap());

        row.addView(button("Wi-Fi", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        }), buttonParams());

        row.addView(button("Start", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCameraStack();
            }
        }), buttonParams());

        row.addView(button("Stop", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopCameraStack();
            }
        }), buttonParams());

        preview = new ImageView(this);
        preview.setAdjustViewBounds(true);
        preview.setScaleType(ImageView.ScaleType.FIT_CENTER);
        preview.setBackgroundColor(0xffeef4f5);
        root.addView(preview, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(420)));

        setContentView(scrollView);
    }

    private TextView label(LinearLayout root, String value) {
        TextView view = new TextView(this);
        view.setText(value);
        view.setTextSize(15);
        view.setTextColor(0xff20383d);
        view.setPadding(0, dp(5), 0, dp(5));
        root.addView(view, matchWrap());
        return view;
    }

    private Button button(String text, View.OnClickListener listener) {
        Button button = new Button(this);
        button.setText(text);
        button.setAllCaps(false);
        button.setOnClickListener(listener);
        return button;
    }

    private LinearLayout.LayoutParams buttonParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        params.setMargins(dp(4), 0, dp(4), 0);
        return params;
    }

    private LinearLayout.LayoutParams matchWrap() {
        return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    private void requestRuntimePermissions() {
        if (Build.VERSION.SDK_INT < 23) {
            return;
        }
        List<String> needed = new ArrayList<>();
        addIfMissing(needed, Manifest.permission.ACCESS_FINE_LOCATION);
        addIfMissing(needed, Manifest.permission.ACCESS_COARSE_LOCATION);
        addIfMissing(needed, Manifest.permission.RECORD_AUDIO);
        if (Build.VERSION.SDK_INT >= 33) {
            addIfMissing(needed, Manifest.permission.NEARBY_WIFI_DEVICES);
            addIfMissing(needed, Manifest.permission.READ_MEDIA_IMAGES);
            addIfMissing(needed, Manifest.permission.READ_MEDIA_VIDEO);
        } else {
            addIfMissing(needed, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!needed.isEmpty()) {
            requestPermissions(needed.toArray(new String[0]), 100);
        }
    }

    private void addIfMissing(List<String> permissions, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(permission);
        }
    }

    private void startCameraStack() {
        if (!started.compareAndSet(false, true)) {
            setStatus("Status: already started");
            return;
        }
        updateWifiText();
        setStatus("Status: starting native Wi-Fi camera stack...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File logDir = new File(getExternalFilesDir(null), "wifi-camera-log");
                    if (!logDir.exists() && !logDir.mkdirs()) {
                        appendStatus("Log directory could not be created: " + logDir.getAbsolutePath());
                    }
                    WifiCameraApi.APP_SDCARD = logDir.getAbsolutePath();
                    LogManagerWD.APP_SDCARD = logDir.getAbsolutePath();
                    WifiCallBack.getInstance().setDeviceStatusInterface(MainActivity.this);
                    WifiCameraApi api = WifiCameraApi.getInstance();
                    api.setLog_switch(true);
                    api.init(getApplicationContext());
                    int startResult = api.gWifiCamera.caStart();
                    appendStatus("caStart() = " + startResult);
                    queryDeviceSnapshot();
                } catch (Throwable throwable) {
                    started.set(false);
                    appendStatus("Start failed: " + throwable.getClass().getSimpleName() + ": " + throwable.getMessage());
                }
            }
        }, "useeear-camera-start").start();
    }

    private void stopCameraStack() {
        started.set(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int result = WifiCameraApi.getInstance().gWifiCamera.caStop();
                    appendStatus("caStop() = " + result);
                } catch (Throwable throwable) {
                    appendStatus("Stop failed: " + throwable.getMessage());
                }
            }
        }, "useeear-camera-stop").start();
    }

    private void queryDeviceSnapshot() {
        try {
            WifiCameraFirmInfo firmInfo = new WifiCameraFirmInfo();
            int firmResult = WifiCameraApi.getInstance().gWifiCamera.cameraFirmInfoGet(firmInfo);
            WifiCameraStatusInfo statusInfo = new WifiCameraStatusInfo();
            int statusResult = WifiCameraApi.getInstance().gWifiCamera.cameraStatusInfoGet(statusInfo);
            String text = String.format(Locale.US,
                    "Device: firm=%d vendor=%s product=%s version=%s ssid=%s mac=%s | status=%d battery=%d charge=%d usedByOther=%d",
                    firmResult,
                    safe(firmInfo.getvendor()),
                    safe(firmInfo.getproduct()),
                    safe(firmInfo.getversion()),
                    safe(firmInfo.getssid()),
                    safe(firmInfo.getmac()),
                    statusResult,
                    statusInfo.battery,
                    statusInfo.isCharge,
                    statusInfo.isusedbyother);
            setDevice(text);
        } catch (Throwable throwable) {
            setDevice("Device: query failed: " + throwable.getMessage());
        }
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private void updateWifiText() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager == null) {
            wifiText.setText("Wi-Fi: unavailable");
            return;
        }
        WifiInfo info = wifiManager.getConnectionInfo();
        String ssid = info == null ? "" : info.getSSID();
        if (ssid != null && ssid.length() >= 2 && ssid.startsWith("\"") && ssid.endsWith("\"")) {
            ssid = ssid.substring(1, ssid.length() - 1);
        }
        DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
        String gateway = dhcpInfo == null ? "" : Formatter.formatIpAddress(dhcpInfo.gateway);
        wifiText.setText("Wi-Fi: SSID=" + safe(ssid) + " gateway=" + gateway + " expected prefixes: UseeEar / i4season");
    }

    @Override
    public void getStatus(final int dtype, final int status) {
        String label;
        switch (status) {
            case 1:
                label = "device found";
                break;
            case 2:
                label = "online";
                break;
            case 3:
                label = "online failed";
                break;
            case 4:
                label = "offline";
                break;
            case 5:
                label = "socket creation failed";
                break;
            default:
                label = "unknown";
                break;
        }
        setStatus("Status: dtype=" + dtype + " status=" + status + " (" + label + ")");
        if (status == 2) {
            queryDeviceSnapshot();
        }
    }

    @Override
    public void getData(int dtype, WifiCameraPic wifiCameraPic) {
        if (wifiCameraPic == null || wifiCameraPic.data == null || wifiCameraPic.data.length == 0) {
            return;
        }
        frameCount++;
        long now = System.currentTimeMillis();
        if (now - lastPreviewMs > 100) {
            lastPreviewMs = now;
            final Bitmap bitmap = BitmapFactory.decodeByteArray(wifiCameraPic.data, 0, wifiCameraPic.data.length);
            if (bitmap != null) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        preview.setImageBitmap(bitmap);
                    }
                });
            }
        }
        updateFrameText(dtype, wifiCameraPic);
    }

    @Override
    public void getaudioData(int dtype, WifiCameraPic wifiCameraPic) {
        audioCount++;
        updateFrameText(dtype, wifiCameraPic);
    }

    @Override
    public void getCameraInfo(int dtype, final WifiCameraStatusInfo info) {
        if (info == null) {
            return;
        }
        setDevice("Device: dtype=" + dtype + " battery=" + info.battery + " charge=" + info.isCharge + " usedByOther=" + info.isusedbyother);
    }

    @Override
    public int sendnotifyfile(int i, int i2, int i3, String path) {
        appendStatus("File notify: " + i + "/" + i2 + "/" + i3 + " " + safe(path));
        return 0;
    }

    private void updateFrameText(final int dtype, final WifiCameraPic pic) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                String detail = "";
                if (pic != null) {
                    detail = " dtype=" + dtype + " type=" + pic.type + " size=" + pic.width + "x" + pic.height + " angle=" + pic.angle;
                }
                frameText.setText("Frames: " + frameCount + "  Audio packets: " + audioCount + detail);
            }
        });
    }

    private void setStatus(final String text) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                statusText.setText(text);
            }
        });
    }

    private void appendStatus(final String text) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                statusText.setText(statusText.getText() + "\n" + text);
            }
        });
    }

    private void setDevice(final String text) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                deviceText.setText(text);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateWifiText();
    }

    @Override
    protected void onDestroy() {
        if (started.get()) {
            stopCameraStack();
        }
        super.onDestroy();
    }
}
