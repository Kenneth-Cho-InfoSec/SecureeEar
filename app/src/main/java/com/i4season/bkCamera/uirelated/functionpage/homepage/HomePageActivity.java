package com.i4season.bkCamera.uirelated.functionpage.homepage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.i4season.bkCamera.WDApplication;
import com.i4season.bkCamera.logicrelated.camera.CameraEventObserver;
import com.i4season.bkCamera.logicrelated.camera.CameraManager;
import com.i4season.bkCamera.logicrelated.camera.bean.CameraFirmInfo;
import com.i4season.bkCamera.uirelated.other.Language.Strings;
import com.i4season.bkCamera.uirelated.other.MainFrameHandleInstance;
import com.i4season.bkCamera.uirelated.other.dialog.AppExitDialog;
import com.i4season.bkCamera.uirelated.other.dialog.ConnectWiFiDialog;
import com.i4season.bkCamera.uirelated.other.dialog.DeviceUsedDialog;
import com.i4season.bkCamera.uirelated.other.dialog.LowBatteryDialog;
import com.i4season.bkCamera.uirelated.other.dialog.UserplayVideoDialog;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.NotifyCode;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.SystemUtil;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.UtilTools;
import com.i4season.bkCamera.uirelated.other.logmanage.LogWD;
import com.i4season.i4season_camera.C0413R;
import com.jni.WifiCameraInfo.WifiCameraStatusInfo;

public class HomePageActivity extends AppCompatActivity implements View.OnClickListener, CameraEventObserver.OfflineListener, CameraEventObserver.OnAcceptFwInfoListener, CameraEventObserver.OnWifiCameraInfoListener, CameraEventObserver.OnBatteryAndChargingListener, WDApplication.BackgroundOrForegroundListener {
    public static final int DEVICE_CONNECTING = 103;
    public static final int DEVICE_CONNECT_ERROR = 104;
    public static final int DEVICE_DEVICE_INFO_ACCEPT = 106;
    public static final int DEVICE_FW_ACCEPT = 105;
    public static final int DEVICE_OFFLINE = 102;
    public static final int DEVICE_ONLINE = 101;
    public static final int DEVICE_SEIZE_STATUS_CHANGE = 108;
    public static final int DEVICE_UPDATA = 107;
    private DeviceUsedDialog dialog;
    private TextView goCompanyweb;
    private ImageView mCameraBtn;
    private ConnectWiFiDialog mConnectWiFiDialog;
    private ImageView mDeviceBattertImg;
    private TextView mDeviceBattertInfoTv;
    private TextView mDeviceBattertPercentTv;
    private ImageView mDeviceElectricity;
    private ImageView mDeviceInfo;
    private TextView mDeviceInfoClick;
    private LinearLayout mDeviceInfoLl;
    private TextView mDeviceInfoStatus;
    private TextView mDeviceInfoTv;
    private ImageView mDeviceStatus;
    private TextView mDeviceStatusTv;
    private TextView mDeviceWifiNameTv;
    private TextView mLogoTv;
    protected LowBatteryDialog mLowBatteryDialog;
    private ImageView mMoreIv;
    private RelativeLayout mMoreRl;
    private TextView mRemainElectricity;
    private TextView mRemainElectricityValue;
    private TextView mRemainElectricityValueUnit;
    private TextView mRemainTime;
    private TextView mRemainTimeValue;
    private TextView mRemainTimeValueUnit;
    private ImageView mStatusIv;
    private TextView mStatusTv;
    private ImageView mUserPlayIv;
    private LinearLayout mUserPlayLl;
    private TextView mUserPlayTv;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void dispatchMessage(Message message) {
            switch (message.what) {
                case 101:
                    HomePageActivity.this.deviceOnline();
                    break;
                case 102:
                    HomePageActivity.this.deviceOffline();
                    break;
                case 103:
                    HomePageActivity.this.deviceConnecting();
                    break;
                case 104:
                    MainFrameHandleInstance.getInstance().showErrorDialog(HomePageActivity.this, ((Integer) message.obj).intValue());
                    break;
                case 105:
                    HomePageActivity.this.mDeviceInfoClick.setText(((CameraFirmInfo) message.obj).getssid());
                    break;
                case 106:
                    HomePageActivity.this.showDeviceInfo((WifiCameraStatusInfo) message.obj);
                    break;
                case 107:
                    HomePageActivity.this.deviceUpdata();
                    break;
                case 108:
                    HomePageActivity.this.checkDeviceSeizeStatusChange((WifiCameraStatusInfo) message.obj);
                    break;
            }
        }
    };
    private final BroadcastReceiver mWifiChangeReceiver = new BroadcastReceiver() {
        /* JADX WARN: Removed duplicated region for block: B:13:0x003d  */
        @Override
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onReceive(Context context, Intent intent) {
            byte b;
            String action = intent.getAction();
            LogWD.writeMsg(this, 2, "WifiChangeReceiver onReceive() action = " + action);
            int iHashCode = action.hashCode();
            if (iHashCode != -1118166861) {
                b = (iHashCode == 888870417 && action.equals(NotifyCode.LANGUAGE_CHANGE_NOTIFY)) ? (byte) 0 : (byte) -1;
            } else if (action.equals(NotifyCode.LICENSE_CHECK_ERROR)) {
                b = 1;
            }
            if (b != 0) {
                return;
            }
            HomePageActivity.this.languageChangSet();
        }
    };
    private boolean needShowCamera = false;

    @Override
    public void onBackground() {
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Display defaultDisplay = ((WindowManager) getSystemService("window")).getDefaultDisplay();
        if (UtilTools.getRoundDecimal(defaultDisplay.getHeight(), defaultDisplay.getWidth(), 4) < 1.85d) {
            setContentView(C0413R.layout.activity_homepage_small);
        } else {
            setContentView(C0413R.layout.activity_homepage);
        }
        SystemUtil.setTransparent(this);
        initView();
        initData();
        initListener();
        registerReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.mWifiChangeReceiver);
        CameraManager.getInstance().removeEventObserverListenser(12, this);
        CameraManager.getInstance().removeEventObserverListenser(13, this);
        CameraManager.getInstance().removeEventObserverListenser(9, this);
        WDApplication.getInstance().removeBackgroundOrForegroundListener(this);
    }

    @Override
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            AppExitDialog appExitDialog = new AppExitDialog(this);
            appExitDialog.setCanceledOnTouchOutside(false);
            appExitDialog.show();
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }

    private void initView() {
        this.mLogoTv = (TextView) findViewById(C0413R.id.tv_homepage_name);
        this.mStatusTv = (TextView) findViewById(C0413R.id.tv_homepage_status);
        this.mStatusIv = (ImageView) findViewById(C0413R.id.iv_homepage_status);
        this.mMoreIv = (ImageView) findViewById(C0413R.id.iv_homepage_more);
        this.mMoreRl = (RelativeLayout) findViewById(C0413R.id.iv_homepage_more_rl);
        this.mDeviceElectricity = (ImageView) findViewById(C0413R.id.iv_device_electricity);
        this.mDeviceStatus = (ImageView) findViewById(C0413R.id.iv_device_status);
        this.mDeviceStatusTv = (TextView) findViewById(C0413R.id.tv_device_status);
        this.mUserPlayLl = (LinearLayout) findViewById(C0413R.id.homepage_user_ll);
        this.mUserPlayIv = (ImageView) findViewById(C0413R.id.iv_user_play);
        this.mUserPlayTv = (TextView) findViewById(C0413R.id.tv_user_play);
        this.mDeviceInfoStatus = (TextView) findViewById(C0413R.id.tv_device_unconnect);
        this.mDeviceInfoClick = (TextView) findViewById(C0413R.id.tv_device_unconnect_click);
        this.mDeviceInfo = (ImageView) findViewById(C0413R.id.iv_device_info);
        this.mRemainElectricity = (TextView) findViewById(C0413R.id.tv_remain_electricity);
        this.mRemainElectricityValue = (TextView) findViewById(C0413R.id.tv_remain_electricity_value);
        this.mRemainElectricityValueUnit = (TextView) findViewById(C0413R.id.tv_remain_electricity_value_unit);
        this.mRemainTime = (TextView) findViewById(C0413R.id.tv_remain_time);
        this.mRemainTimeValue = (TextView) findViewById(C0413R.id.tv_remain_time_value);
        this.mRemainTimeValueUnit = (TextView) findViewById(C0413R.id.tv_remain_time_value_unit);
        this.mDeviceInfoLl = (LinearLayout) findViewById(C0413R.id.homeage_device_info_ll);
        this.mCameraBtn = (ImageView) findViewById(C0413R.id.homepage_camera);
        this.mDeviceInfoTv = (TextView) findViewById(C0413R.id.homepage_device_info_tv);
        this.mDeviceWifiNameTv = (TextView) findViewById(C0413R.id.homepage_wifi_name_tv);
        this.mDeviceBattertInfoTv = (TextView) findViewById(C0413R.id.homepage_device_battert_tv);
        this.mDeviceBattertPercentTv = (TextView) findViewById(C0413R.id.homepage_battert_percent_tv);
        this.mDeviceBattertImg = (ImageView) findViewById(C0413R.id.homepage_battert_icon);
        this.goCompanyweb = (TextView) findViewById(C0413R.id.homepage_show_companyweb);
    }

    private void initData() {
        this.mRemainElectricity.setText(Strings.getString(C0413R.string.Device_Info_Remain_Electricity, this));
        this.mRemainTime.setText(Strings.getString(C0413R.string.Device_Info_Remain_Time, this));
        this.mUserPlayTv.setText(Strings.getString(C0413R.string.App_User_Play, this));
        this.mLogoTv.setText(Strings.getString(C0413R.string.app_name, this));
        this.mDeviceInfoTv.setText(Strings.getString(C0413R.string.App_Device_Info, this));
        this.mDeviceBattertInfoTv.setText(Strings.getString(C0413R.string.App_Device_Battert, this));
        showDeviceStatus();
        if (CameraManager.mProgrammeType == 5) {
            WifiCameraStatusInfo wifiCameraStatusInfo = CameraManager.getInstance().getWifiCameraStatusInfo();
            showDeviceInfo(wifiCameraStatusInfo);
            checkDeviceSeizeStatusChange(wifiCameraStatusInfo);
        }
    }

    private void showDeviceStatus() {
        int deviceOnlineStatus = CameraManager.getInstance().getDeviceOnlineStatus();
        if (deviceOnlineStatus == 33) {
            deviceOnline();
            return;
        }
        if (deviceOnlineStatus == 32) {
            deviceConnecting();
            return;
        }
        if (deviceOnlineStatus == 2 || deviceOnlineStatus == 3 || deviceOnlineStatus == 4) {
            deviceOffline();
            MainFrameHandleInstance.getInstance().showErrorDialog(this, deviceOnlineStatus);
        } else {
            deviceOffline();
        }
    }

    public void languageChangSet() {
        this.mRemainElectricity.setText(Strings.getString(C0413R.string.Device_Info_Remain_Electricity, this));
        this.mRemainTime.setText(Strings.getString(C0413R.string.Device_Info_Remain_Time, this));
        this.mUserPlayTv.setText(Strings.getString(C0413R.string.App_User_Play, this));
        this.mDeviceInfoTv.setText(Strings.getString(C0413R.string.App_Device_Info, this));
        this.mDeviceBattertInfoTv.setText(Strings.getString(C0413R.string.App_Device_Battert, this));
        showDeviceStatus();
        this.dialog = null;
    }

    private void initListener() {
        this.mMoreIv.setOnClickListener(this);
        this.mMoreRl.setOnClickListener(this);
        this.mUserPlayIv.setOnClickListener(this);
        this.mUserPlayTv.setOnClickListener(this);
        this.mCameraBtn.setOnClickListener(this);
        this.mUserPlayLl.setOnClickListener(this);
        this.mDeviceElectricity.setOnClickListener(this);
        CameraManager.getInstance().setOnOfflineListener(this);
        CameraManager.getInstance().setOnWifiCameraInfoListener(this);
        CameraManager.getInstance().setOnBatteryAndChargingListener(this);
        this.goCompanyweb.setOnClickListener(this);
        WDApplication.getInstance().setBackgroundOrForegroundListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case C0413R.id.homeage_device_info_ll:
            case C0413R.id.homepage_camera:
            case C0413R.id.iv_device_electricity:
                int deviceOnlineStatus = CameraManager.getInstance().getDeviceOnlineStatus();
                if (deviceOnlineStatus == 33) {
                    WifiCameraStatusInfo wifiCameraStatusInfo = CameraManager.getInstance().getWifiCameraStatusInfo();
                    if (wifiCameraStatusInfo != null && wifiCameraStatusInfo.isusedbyother == 1) {
                        showDialog(true);
                    } else {
                        MainFrameHandleInstance.getInstance().showCameraShowActivity(this, false);
                    }
                    break;
                } else if (deviceOnlineStatus != 32) {
                    showWifiSettingDialog();
                    break;
                }
                break;
            case C0413R.id.homepage_show_companyweb:
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://www.i4season.com")));
                break;
            case C0413R.id.homepage_user_ll:
            case C0413R.id.iv_user_play:
            case C0413R.id.tv_user_play:
                showUserPlayDialog();
                break;
            case C0413R.id.iv_homepage_more:
            case C0413R.id.iv_homepage_more_rl:
                MainFrameHandleInstance.getInstance().showSettingActivity(this, false);
                break;
        }
    }

    private void showUserPlayDialog() {
        UserplayVideoDialog userplayVideoDialog = new UserplayVideoDialog(this, 0);
        userplayVideoDialog.setCanceledOnTouchOutside(false);
        userplayVideoDialog.show();
    }

    private void showWifiSettingDialog() {
        this.mConnectWiFiDialog = null;
        if (this.mConnectWiFiDialog == null) {
            this.mConnectWiFiDialog = new ConnectWiFiDialog(this);
            this.mConnectWiFiDialog.setCanceledOnTouchOutside(false);
        }
        if (isFinishing() || isDestroyed() || this.mConnectWiFiDialog.isShowing()) {
            return;
        }
        this.mConnectWiFiDialog.show();
    }

    private void showLowerBatteryDialog() {
        if (isDestroyed() || isFinishing()) {
            return;
        }
        if (this.mLowBatteryDialog == null) {
            this.mLowBatteryDialog = new LowBatteryDialog(this, false);
            this.mLowBatteryDialog.setCanceledOnTouchOutside(false);
            this.mLowBatteryDialog.setCancelable(false);
        }
        this.mLowBatteryDialog.show();
        this.mLowBatteryDialog.updateText();
    }

    @Override
    public void onWifiCameraInfoListener(WifiCameraStatusInfo wifiCameraStatusInfo) {
        Message messageObtain = Message.obtain();
        messageObtain.what = 106;
        messageObtain.obj = wifiCameraStatusInfo;
        this.mHandler.sendMessage(messageObtain);
    }

    @Override
    public void onDeviceSeizeStatusChanged(WifiCameraStatusInfo wifiCameraStatusInfo) {
        Message messageObtain = Message.obtain();
        messageObtain.what = 108;
        messageObtain.obj = wifiCameraStatusInfo;
        this.mHandler.sendMessage(messageObtain);
    }

    @Override
    public void onBatteryAndCharging(float f, boolean z) {
        WifiCameraStatusInfo wifiCameraStatusInfo = new WifiCameraStatusInfo();
        wifiCameraStatusInfo.Setbattery((int) f);
        wifiCameraStatusInfo.SetisCharge(z ? 1 : 0);
        wifiCameraStatusInfo.SetisLowPowerOff(f <= 10.0f ? 1 : 0);
        Message messageObtain = Message.obtain();
        messageObtain.obj = wifiCameraStatusInfo;
        messageObtain.what = 106;
        this.mHandler.sendMessage(messageObtain);
    }

    public void checkDeviceSeizeStatusChange(WifiCameraStatusInfo wifiCameraStatusInfo) {
        if (wifiCameraStatusInfo == null) {
            return;
        }
        showDialog(wifiCameraStatusInfo.isusedbyother == 1);
        if (wifiCameraStatusInfo.isusedbyother == 1 || !UtilTools.checkActivityIsOnTop(this, getLocalClassName())) {
            return;
        }
        MainFrameHandleInstance.getInstance().showCameraShowActivity(this, false);
    }

    public void showDeviceInfo(WifiCameraStatusInfo wifiCameraStatusInfo) {
        if (wifiCameraStatusInfo == null) {
            return;
        }
        int i = wifiCameraStatusInfo.battery;
        int i2 = wifiCameraStatusInfo.isCharge;
        int i3 = wifiCameraStatusInfo.isLowPowerOff;
        int i4 = wifiCameraStatusInfo.isSensorOk;
        lowerBatteryHandler(i2, i);
        this.mRemainTimeValueUnit.setVisibility(0);
        if (i == 0) {
            this.mDeviceBattertImg.setImageResource(i2 == 1 ? C0413R.drawable.ic_charing_0 : C0413R.drawable.ic_battery_0);
            this.mDeviceBattertPercentTv.setText("0%");
            this.mRemainElectricityValue.setText("0");
            this.mRemainTimeValue.setText("1");
            return;
        }
        if (i > 0 && i <= 10) {
            this.mDeviceBattertImg.setImageResource(i2 == 1 ? C0413R.drawable.ic_charing_10 : C0413R.drawable.ic_battery_10);
            this.mDeviceBattertPercentTv.setText("10%");
            this.mRemainElectricityValue.setText("10");
            this.mRemainTimeValue.setText("10");
            return;
        }
        if (10 < i && i <= 20) {
            this.mDeviceBattertImg.setImageResource(i2 == 1 ? C0413R.drawable.ic_charing_20 : C0413R.drawable.ic_battery_20);
            this.mDeviceBattertPercentTv.setText("20%");
            this.mRemainElectricityValue.setText("20");
            this.mRemainTimeValue.setText("18");
            return;
        }
        if (20 < i && i <= 30) {
            this.mDeviceBattertImg.setImageResource(i2 == 1 ? C0413R.drawable.ic_charing_30 : C0413R.drawable.ic_battery_30);
            this.mDeviceBattertPercentTv.setText("30%");
            this.mRemainElectricityValue.setText("30");
            this.mRemainTimeValue.setText("27");
            return;
        }
        if (30 < i && i <= 40) {
            this.mDeviceBattertImg.setImageResource(i2 == 1 ? C0413R.drawable.ic_charing_40 : C0413R.drawable.ic_battery_40);
            this.mDeviceBattertPercentTv.setText("40%");
            this.mRemainElectricityValue.setText("40");
            this.mRemainTimeValue.setText("36");
            return;
        }
        if (40 < i && i <= 50) {
            this.mDeviceBattertImg.setImageResource(i2 == 1 ? C0413R.drawable.ic_charing_50 : C0413R.drawable.ic_battery_50);
            this.mDeviceBattertPercentTv.setText("50%");
            this.mRemainElectricityValue.setText("50");
            this.mRemainTimeValue.setText("45");
            return;
        }
        if (50 < i && i <= 60) {
            this.mDeviceBattertImg.setImageResource(i2 == 1 ? C0413R.drawable.ic_charing_60 : C0413R.drawable.ic_battery_60);
            this.mDeviceBattertPercentTv.setText("60%");
            this.mRemainElectricityValue.setText("60");
            this.mRemainTimeValue.setText("54");
            return;
        }
        if (60 < i && i <= 70) {
            this.mDeviceBattertImg.setImageResource(i2 == 1 ? C0413R.drawable.ic_charing_70 : C0413R.drawable.ic_battery_70);
            this.mDeviceBattertPercentTv.setText("70%");
            this.mRemainElectricityValue.setText("70");
            this.mRemainTimeValue.setText("63");
            return;
        }
        if (70 < i && i <= 80) {
            this.mDeviceBattertImg.setImageResource(i2 == 1 ? C0413R.drawable.ic_charing_80 : C0413R.drawable.ic_battery_80);
            this.mDeviceBattertPercentTv.setText("80%");
            this.mRemainElectricityValue.setText("80");
            this.mRemainTimeValue.setText("72");
            return;
        }
        if (80 < i && i <= 90) {
            this.mDeviceBattertImg.setImageResource(i2 == 1 ? C0413R.drawable.ic_charing_90 : C0413R.drawable.ic_battery_90);
            this.mDeviceBattertPercentTv.setText("90%");
            this.mRemainElectricityValue.setText("90");
            this.mRemainTimeValue.setText("81");
            return;
        }
        if (90 >= i || i > 100) {
            return;
        }
        this.mDeviceBattertImg.setImageResource(i2 == 1 ? C0413R.drawable.ic_charing_100 : C0413R.drawable.ic_battery_100);
        this.mDeviceBattertPercentTv.setText("100%");
        this.mRemainElectricityValue.setText("100");
        this.mRemainTimeValue.setText("90");
    }

    private void lowerBatteryHandler(int i, int i2) {
        if (i == 1) {
            LowBatteryDialog lowBatteryDialog = this.mLowBatteryDialog;
            if (lowBatteryDialog == null || !lowBatteryDialog.isShowing()) {
                return;
            }
            this.mLowBatteryDialog.dismiss();
            return;
        }
        if (i2 > 0 && i2 <= 10) {
            showLowerBatteryDialog();
            return;
        }
        LowBatteryDialog lowBatteryDialog2 = this.mLowBatteryDialog;
        if (lowBatteryDialog2 == null || !lowBatteryDialog2.isShowing()) {
            return;
        }
        this.mLowBatteryDialog.dismiss();
    }

    @Override
    public void onOfflineListener() {
        this.mHandler.sendEmptyMessage(102);
    }

    @Override
    public void onConnectingListener() {
        this.mHandler.sendEmptyMessage(103);
    }

    @Override
    public void onConnectErrorListener(int i) {
        this.mHandler.sendEmptyMessage(102);
        Message messageObtain = Message.obtain();
        messageObtain.what = 104;
        messageObtain.obj = Integer.valueOf(i);
        this.mHandler.sendMessage(messageObtain);
    }

    @Override
    public void onOnlineListener() {
        this.mHandler.sendEmptyMessage(101);
    }

    @Override
    public void onUpdataDeviceListener() {
        this.mHandler.sendEmptyMessage(107);
    }

    public void deviceOffline() {
        this.mStatusTv.setText(Strings.getString(C0413R.string.Device_Status_UnConnect, this));
        this.mStatusIv.setImageResource(C0413R.drawable.ic_status_unconnect);
        this.mDeviceStatus.setImageResource(C0413R.drawable.ic_devcie_unconnect);
        this.mCameraBtn.setImageResource(C0413R.drawable.ic_homepage_camera_offline);
        this.mDeviceStatusTv.setText(Strings.getString(C0413R.string.Device_Status_GoConnect, this));
        this.mDeviceInfoStatus.setText(Strings.getString(C0413R.string.Device_Status_UnConnect, this));
        this.mDeviceInfoClick.setText(Strings.getString(C0413R.string.Device_Status_UnConnected_Click, this));
        this.mDeviceElectricity.setImageResource(C0413R.drawable.ic_bg_offline);
        this.mRemainElectricityValueUnit.setVisibility(8);
        this.mRemainTimeValueUnit.setVisibility(8);
        this.mDeviceBattertImg.setVisibility(8);
        this.mRemainElectricityValue.setText("——");
        this.mRemainTimeValue.setText("——");
        this.mDeviceWifiNameTv.setText(" ——");
        this.mDeviceBattertPercentTv.setText(" ——");
        LowBatteryDialog lowBatteryDialog = this.mLowBatteryDialog;
        if (lowBatteryDialog != null && lowBatteryDialog.isShowing()) {
            this.mLowBatteryDialog.dismiss();
        }
        showDialog(false);
        this.needShowCamera = false;
    }

    public void deviceConnecting() {
        this.mStatusTv.setText(Strings.getString(C0413R.string.Device_Status_Connecting, this));
        this.mStatusIv.setImageResource(C0413R.drawable.ic_status_connecting);
        this.mDeviceStatus.setImageResource(C0413R.drawable.ic_devcie_connecting);
        this.mCameraBtn.setImageResource(C0413R.drawable.ic_homepage_camera);
        this.mDeviceStatusTv.setText(Strings.getString(C0413R.string.Device_Status_Connecting, this));
        this.mDeviceInfoStatus.setText(Strings.getString(C0413R.string.Device_Status_Connecting, this));
        this.mDeviceInfoClick.setText(Strings.getString(C0413R.string.Device_Status_UnConnected_Click, this));
    }

    public void deviceOnline() {
        ConnectWiFiDialog connectWiFiDialog = this.mConnectWiFiDialog;
        if (connectWiFiDialog != null && connectWiFiDialog.isShowing()) {
            this.mConnectWiFiDialog.dismiss();
        }
        this.mStatusTv.setText(Strings.getString(C0413R.string.Device_Status_Connected, this));
        this.mStatusIv.setImageResource(C0413R.drawable.ic_status_connected);
        this.mDeviceStatus.setImageResource(C0413R.drawable.ic_devcie_connected);
        this.mCameraBtn.setImageResource(C0413R.drawable.ic_homepage_camera);
        this.mDeviceElectricity.setImageResource(C0413R.drawable.ic_bg_online);
        this.mDeviceStatusTv.setText(Strings.getString(C0413R.string.Device_Status_Connected, this));
        this.mDeviceInfoStatus.setText(Strings.getString(C0413R.string.Device_Status_Connected, this));
        this.mRemainElectricityValueUnit.setVisibility(0);
        this.mDeviceBattertImg.setVisibility(0);
        this.mDeviceWifiNameTv.setText(CameraManager.getInstance().getCurrentDeviceWifiSsid());
        WifiCameraStatusInfo wifiCameraStatusInfo = CameraManager.getInstance().getWifiCameraStatusInfo();
        this.mRemainElectricityValue.setText(String.valueOf(wifiCameraStatusInfo != null ? wifiCameraStatusInfo.battery : 0));
        CameraFirmInfo cameraFirmInfo = CameraManager.getInstance().getmAoaDeviceFirmInfo();
        if (cameraFirmInfo != null) {
            this.mDeviceInfoClick.setText(cameraFirmInfo.getssid());
        } else {
            CameraManager.getInstance().acceptFwInfo(this);
        }
        if (wifiCameraStatusInfo == null || wifiCameraStatusInfo.isusedbyother == 1) {
            return;
        }
        if (WDApplication.getInstance().isCurrentIsStart()) {
            LogWD.writeMsg(this, 8, "当前在前台  直接进入");
            if (UtilTools.checkActivityIsOnTop(this, getLocalClassName())) {
                MainFrameHandleInstance.getInstance().showCameraShowActivity(this, false);
                return;
            }
            return;
        }
        LogWD.writeMsg(this, 8, "当前在后台   等待resume");
        this.needShowCamera = true;
    }

    public void deviceUpdata() {
        showDeviceInfo(CameraManager.getInstance().getWifiCameraStatusInfo());
        CameraManager.getInstance().acceptFwInfo(this);
    }

    private void showDialog(boolean z) {
        if (this.dialog == null) {
            this.dialog = new DeviceUsedDialog(this, this.mHandler);
            this.dialog.setCanceledOnTouchOutside(false);
        }
        if (z) {
            if (this.dialog.isShowing()) {
                return;
            }
            this.dialog.show();
        } else if (this.dialog.isShowing()) {
            this.dialog.dismiss();
        }
    }

    @Override
    public void onOnAcceptFwInfoListener(boolean z, CameraFirmInfo cameraFirmInfo) {
        Message messageObtain = Message.obtain();
        messageObtain.what = 105;
        messageObtain.obj = cameraFirmInfo;
        this.mHandler.sendMessage(messageObtain);
    }

    public void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NotifyCode.LANGUAGE_CHANGE_NOTIFY);
        intentFilter.addAction(NotifyCode.LICENSE_CHECK_ERROR);
        registerReceiver(this.mWifiChangeReceiver, intentFilter);
    }

    @Override
    public void onForeground() {
        if (this.needShowCamera) {
            this.needShowCamera = false;
            MainFrameHandleInstance.getInstance().showCameraShowActivity(this, false);
        }
    }
}
