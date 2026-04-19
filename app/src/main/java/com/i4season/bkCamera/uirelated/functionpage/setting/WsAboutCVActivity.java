package com.i4season.bkCamera.uirelated.functionpage.setting;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.i4season.bkCamera.logicrelated.camera.CameraEventObserver;
import com.i4season.bkCamera.logicrelated.camera.CameraManager;
import com.i4season.bkCamera.logicrelated.camera.bean.CameraFirmInfo;
import com.i4season.bkCamera.uirelated.other.Language.Strings;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.Constant;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.SpUtils;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.SystemUtil;
import com.i4season.bkCamera.uirelated.other.logmanage.LogManagerWD;
import com.i4season.bkCamera.uirelated.other.logmanage.LogWD;
import com.i4season.i4season_camera.C0413R;
import com.jni.WifiCameraApi;

public class WsAboutCVActivity extends AppCompatActivity implements View.OnClickListener, CameraEventObserver.OnAcceptFwInfoListener {
    private TextView mAppName;
    private TextView mAppVersion;
    protected ImageView mBack;
    private TextView mFirmwareName;
    private TextView mFirmwareVersion;
    private ImageView mIcon;
    private TextView mProductName;
    private TextView mProductTitle;
    private Switch mSwitch;
    private RelativeLayout mSwitchRl;
    protected TextView mTitle;
    protected LinearLayout mTopBar;
    private int num;
    private SpUtils spUtils;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0413R.layout.activity_about);
        SystemUtil.setTransparent(this);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        this.mTopBar = (LinearLayout) findViewById(C0413R.id.app_about_topbar);
        this.mTitle = (TextView) findViewById(C0413R.id.app_topbar_center_text);
        this.mBack = (ImageView) findViewById(C0413R.id.app_topbar_left_image);
        this.mIcon = (ImageView) findViewById(C0413R.id.app_about_icon);
        this.mProductTitle = (TextView) findViewById(C0413R.id.app_about_product_name);
        this.mProductName = (TextView) findViewById(C0413R.id.app_about_product_content);
        this.mFirmwareName = (TextView) findViewById(C0413R.id.app_about_firmware_name);
        this.mFirmwareVersion = (TextView) findViewById(C0413R.id.app_about_firmware_version);
        this.mAppName = (TextView) findViewById(C0413R.id.app_about_app_name);
        this.mAppVersion = (TextView) findViewById(C0413R.id.app_about_app_version);
        this.mSwitch = (Switch) findViewById(C0413R.id.app_about_logcat_switch);
        this.mSwitchRl = (RelativeLayout) findViewById(C0413R.id.app_about_logcat);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CameraManager.getInstance().removeEventObserverListenser(1, this);
    }

    private void initData() {
        this.mTopBar.setBackgroundColor(getResources().getColor(C0413R.color.apptakeaction));
        this.mTitle.setTextColor(getResources().getColor(C0413R.color.appwhite));
        this.mTitle.setVisibility(0);
        this.mTitle.setText(Strings.getString(C0413R.string.App_User_App_Version, this));
        this.mBack.setImageResource(C0413R.drawable.ic_app_back_black);
        this.mBack.setVisibility(0);
        LogWD.writeMsg(this, 8, "设置产品名称");
        setProductInfo();
        LogWD.writeMsg(this, 8, "固件");
        setFirmwareInfo();
        LogWD.writeMsg(this, 8, "app");
        setAppVersionInfo();
        LogWD.writeMsg(this, 8, "app");
        setLogCat();
    }

    private void setLogCat() {
        this.spUtils = new SpUtils(this);
        this.mSwitch.setChecked(this.spUtils.getBoolean(Constant.LOGCAT_SWITCH, false));
    }

    private void setAppVersionInfo() {
        String str;
        this.mAppName.setText(Strings.getString(C0413R.string.App_User_App_Version, this));
        try {
            str = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            LogWD.writeMsg(e);
            str = "";
        }
        this.mAppVersion.setText(str);
    }

    private void setFirmwareInfo() {
        this.mFirmwareName.setText(Strings.getString(C0413R.string.Settings_Label_About_Firmware, this));
        if (CameraManager.mProgrammeType != 0) {
            CameraFirmInfo cameraFirmInfo = CameraManager.getInstance().getmAoaDeviceFirmInfo();
            if (cameraFirmInfo != null) {
                LogWD.writeMsg(this, 8, "aoaDeviceFirmInfo.getFwVersion(): " + cameraFirmInfo.getversion());
                this.mFirmwareVersion.setText(cameraFirmInfo.getversion());
                return;
            }
            CameraManager.getInstance().acceptFwInfo(this);
            return;
        }
        this.mFirmwareVersion.setText("");
    }

    @Override
    public void onOnAcceptFwInfoListener(final boolean z, final CameraFirmInfo cameraFirmInfo) {
        LogWD.writeMsg(this, 8, "onOnAcceptFwInfoListener .isSuccessful(): " + z);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String str = z ? cameraFirmInfo.getversion() : "";
                LogWD.writeMsg(this, 8, "firmware: " + str);
                WsAboutCVActivity.this.mFirmwareVersion.setText(str);
            }
        });
    }

    private void setProductInfo() {
        this.mProductTitle.setText(Strings.getString(C0413R.string.Settings_Label_About_ProductName, this));
        this.mProductName.setText(Strings.getString(C0413R.string.app_name, this));
    }

    private void initListener() {
        this.mBack.setOnClickListener(this);
        this.mIcon.setOnClickListener(this);
        this.mSwitch.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == C0413R.id.app_about_icon) {
            this.num++;
            if (this.num == 10) {
                this.num = 0;
                if (this.mSwitchRl.getVisibility() == 0) {
                    this.mSwitchRl.setVisibility(8);
                    return;
                } else {
                    this.mSwitchRl.setVisibility(0);
                    return;
                }
            }
            return;
        }
        if (id != C0413R.id.app_about_logcat_switch) {
            if (id != C0413R.id.app_topbar_left_image) {
                return;
            }
            finish();
            return;
        }
        this.spUtils.putBoolean(Constant.LOGCAT_SWITCH, this.mSwitch.isChecked());
        if (!this.mSwitch.isChecked()) {
            WifiCameraApi.getInstance().setLog_switch(false);
            LogManagerWD.LOG_SWITCH = 0;
        } else {
            WifiCameraApi.getInstance().setLog_switch(true);
            LogManagerWD.LOG_SWITCH = 16777215;
        }
    }
}
