package com.i4season.bkCamera.uirelated.functionpage.setting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.i4season.bkCamera.WDApplication;
import com.i4season.bkCamera.logicrelated.permissionmanage.PermissionInstans;
import com.i4season.bkCamera.uirelated.other.FunctionSwitch;
import com.i4season.bkCamera.uirelated.other.Language.Strings;
import com.i4season.bkCamera.uirelated.other.MainFrameHandleInstance;
import com.i4season.bkCamera.uirelated.other.dialog.GeneralDialog;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.Constant;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.NotifyCode;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.SpUtils;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.SystemUtil;
import com.i4season.bkCamera.uirelated.other.logmanage.LogWD;
import com.i4season.i4season_camera.C0413R;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout companyRl;
    private TextView companyTv;
    protected RelativeLayout mAlbumRl;
    protected TextView mAlbumTitle;
    protected RelativeLayout mAppVersionRl;
    protected TextView mAppVersionTitle;
    protected TextView mAppVersionValue;
    protected ImageView mBack;
    protected RelativeLayout mCacheRl;
    protected TextView mCacheRlTitle;
    protected TextView mCacheRlValue;
    protected RelativeLayout mLanguageRl;
    protected TextView mLanguageTitle;
    protected View mLine;
    protected RelativeLayout mPrivaceRl;
    protected TextView mPrivaceTitle;
    protected RelativeLayout mProblemRl;
    protected TextView mProblemTitle;
    protected RelativeLayout mServiceRl;
    protected TextView mServiceTitle;
    protected TextView mTitle;
    protected LinearLayout mTopBar;
    private SpUtils spUtils;
    private int logNum = 0;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what != 205) {
                return;
            }
            SettingActivity.this.goSettingPermission();
        }
    };
    private final BroadcastReceiver mBroadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LogWD.writeMsg(this, 2, "WifiChangeReceiver onReceive() action = " + action);
            if (((action.hashCode() == 888870417 && action.equals(NotifyCode.LANGUAGE_CHANGE_NOTIFY)) ? (byte) 0 : (byte) -1) != 0) {
                return;
            }
            SettingActivity.this.languageChangSet();
        }
    };

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0413R.layout.activity_setting);
        SystemUtil.setTransparent(this);
        initView();
        initData();
        initListener();
        registerReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.mBroadReceiver);
    }

    private void initView() {
        this.mTopBar = (LinearLayout) findViewById(C0413R.id.add_user_topbar);
        this.mTitle = (TextView) findViewById(C0413R.id.app_topbar_center_text);
        this.mBack = (ImageView) findViewById(C0413R.id.app_topbar_left_image);
        this.mLine = findViewById(C0413R.id.app_bottom_line);
        this.mAlbumRl = (RelativeLayout) findViewById(C0413R.id.setting_album_rl);
        this.mAlbumTitle = (TextView) findViewById(C0413R.id.setting_album_title);
        this.mProblemRl = (RelativeLayout) findViewById(C0413R.id.setting_problem_rl);
        this.mProblemTitle = (TextView) findViewById(C0413R.id.setting_problem_title);
        this.mLanguageRl = (RelativeLayout) findViewById(C0413R.id.user_language_rl);
        this.mLanguageTitle = (TextView) findViewById(C0413R.id.user_language_title);
        this.mCacheRl = (RelativeLayout) findViewById(C0413R.id.user_cache_rl);
        this.mCacheRlTitle = (TextView) findViewById(C0413R.id.user_cache_title);
        this.mCacheRlValue = (TextView) findViewById(C0413R.id.user_cache_value);
        this.mServiceRl = (RelativeLayout) findViewById(C0413R.id.user_server_rl);
        this.mServiceTitle = (TextView) findViewById(C0413R.id.user_server_title);
        this.mPrivaceRl = (RelativeLayout) findViewById(C0413R.id.user_privace_rl);
        this.mPrivaceTitle = (TextView) findViewById(C0413R.id.user_privace_title);
        this.mAppVersionRl = (RelativeLayout) findViewById(C0413R.id.user_app_version_rl);
        this.mAppVersionTitle = (TextView) findViewById(C0413R.id.user_app_version_title);
        this.mAppVersionValue = (TextView) findViewById(C0413R.id.user_app_version_value);
        this.companyRl = (RelativeLayout) findViewById(C0413R.id.companyweb_rl);
        this.companyTv = (TextView) findViewById(C0413R.id.companyweb_tv);
    }

    private void initData() {
        this.mTopBar.setBackgroundColor(getResources().getColor(C0413R.color.transparent));
        this.mTitle.setVisibility(0);
        this.mTitle.setText(Strings.getString(C0413R.string.App_Setting, this));
        this.mBack.setImageResource(C0413R.drawable.ic_app_back_black);
        this.mBack.setVisibility(0);
        this.mLine.setVisibility(8);
        this.spUtils = new SpUtils(this);
        languageChangSet();
    }

    public void languageChangSet() {
        this.mAlbumTitle.setText(Strings.getString(C0413R.string.App_Setting_Album, this));
        this.mProblemTitle.setText(Strings.getString(C0413R.string.App_Setting_Problem, this));
        this.mLanguageTitle.setText(Strings.getString(C0413R.string.App_User_Language, this));
        this.mCacheRlTitle.setText(Strings.getString(C0413R.string.App_User_Cache, this));
        this.mServiceTitle.setText(Strings.getString(C0413R.string.App_User_Service, this));
        this.mPrivaceTitle.setText(Strings.getString(C0413R.string.App_User_Privacy, this));
        this.mAppVersionTitle.setText(Strings.getString(C0413R.string.App_User_App_Version, this));
        this.companyTv.setText(Strings.getString(C0413R.string.App_User_Websit, this));
    }

    private void initListener() {
        this.mBack.setOnClickListener(this);
        this.mAlbumRl.setOnClickListener(this);
        this.mProblemRl.setOnClickListener(this);
        this.mLanguageRl.setOnClickListener(this);
        this.mCacheRl.setOnClickListener(this);
        this.mServiceRl.setOnClickListener(this);
        this.mPrivaceRl.setOnClickListener(this);
        this.mAppVersionRl.setOnClickListener(this);
        this.companyRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case C0413R.id.app_topbar_left_image:
                finish();
                break;
            case C0413R.id.companyweb_rl:
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://www.i4season.com")));
                break;
            case C0413R.id.setting_album_rl:
                gotoAlbum();
                break;
            case C0413R.id.setting_problem_rl:
                MainFrameHandleInstance.getInstance().showProblemPageActivity(this, false);
                break;
            case C0413R.id.user_app_version_rl:
                MainFrameHandleInstance.getInstance().showWsAboutCVActivity(this, false);
                break;
            case C0413R.id.user_language_rl:
                MainFrameHandleInstance.getInstance().showWSLanguageCVActivity(this, false);
                break;
            case C0413R.id.user_privace_rl:
                openPrivacyPolicy();
                break;
            case C0413R.id.user_server_rl:
                openTermsOfService();
                break;
        }
    }

    private void gotoAlbum() {
        if (PermissionInstans.getInstance().isHavaStoragePermission(this)) {
            MainFrameHandleInstance.getInstance().showAlbumListActivity(this, false);
        } else if (new SpUtils(this).getBoolean(Constant.STORAGE_PERMISSION_TAG, false)) {
            showGoSettingPermissionDialog();
        } else {
            PermissionInstans.getInstance().requestPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i != 124) {
            return;
        }
        if (iArr[0] == 0) {
            WDApplication.getInstance().createTempCacheDir();
            if (PermissionInstans.getInstance().isHavaStoragePermission(this)) {
                MainFrameHandleInstance.getInstance().showAlbumListActivity(this, false);
                return;
            }
            return;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.READ_EXTERNAL_STORAGE")) {
            return;
        }
        new SpUtils(this).putBoolean(Constant.STORAGE_PERMISSION_TAG, true);
    }

    private void showGoSettingPermissionDialog() {
        GeneralDialog generalDialog = new GeneralDialog(this, this.mHandler, C0413R.string.App_Permission_Title);
        generalDialog.show();
        generalDialog.setButtonText(C0413R.string.App_Go_Setting, C0413R.string.App_Button_Cancel);
    }

    public void goSettingPermission() {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", getPackageName(), null));
        startActivityForResult(intent, PermissionInstans.GO_SETTING_REQUEST_CODE);
    }

    private void openPrivacyPolicy() {
        MainFrameHandleInstance.getInstance().showWsLoadWebActivity(this, false, Strings.LANGUAGE_SCHINESE.equals(Strings.getLanguage()) ? FunctionSwitch.privacyContentURL : FunctionSwitch.privacyContentURL_en, true);
    }

    private void openTermsOfService() {
        MainFrameHandleInstance.getInstance().showWsLoadWebActivity(this, false, Strings.LANGUAGE_SCHINESE.equals(Strings.getLanguage()) ? FunctionSwitch.termsOfServiceURL_cn : FunctionSwitch.termsOfServiceURL_en, false);
    }

    public void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NotifyCode.LANGUAGE_CHANGE_NOTIFY);
        registerReceiver(this.mBroadReceiver, intentFilter);
    }
}
