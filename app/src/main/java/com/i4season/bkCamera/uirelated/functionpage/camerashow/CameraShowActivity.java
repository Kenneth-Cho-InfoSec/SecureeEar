package com.i4season.bkCamera.uirelated.functionpage.camerashow;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.i4season.bkCamera.WDApplication;
import com.i4season.bkCamera.logicrelated.camera.AudioPlayManager;
import com.i4season.bkCamera.logicrelated.camera.CameraEventObserver;
import com.i4season.bkCamera.logicrelated.camera.CameraManager;
import com.i4season.bkCamera.logicrelated.permissionmanage.PermissionInstans;
import com.i4season.bkCamera.uirelated.functionpage.camerashow.view.BaseCameraView;
import com.i4season.bkCamera.uirelated.functionpage.camerashow.view.DentalMirrorView;
import com.i4season.bkCamera.uirelated.functionpage.camerashow.view.DentalMirrorViewYpc;
import com.i4season.bkCamera.uirelated.other.FunctionSwitch;
import com.i4season.bkCamera.uirelated.other.MainFrameHandleInstance;
import com.i4season.bkCamera.uirelated.other.dialog.GeneralDialog;
import com.i4season.bkCamera.uirelated.other.dialog.LowBatteryDialog;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.Constant;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.NotifyCode;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.SpUtils;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.SystemUtil;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.UtilTools;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.VideoUtils;
import com.i4season.bkCamera.uirelated.other.logmanage.LogWD;
import com.i4season.bkCamera.uirelated.other.view.BottomView;
import com.i4season.i4season_camera.C0413R;
import com.jni.WifiCameraApi;
import com.jni.WifiCameraInfo.WifiCameraLedStatus;
import com.jni.WifiCameraInfo.WifiCameraStatusInfo;
import java.util.Timer;
import java.util.TimerTask;

public class CameraShowActivity extends AppCompatActivity implements View.OnClickListener, CameraEventObserver.OnAcceptBitmapListener, CameraEventObserver.OnTakePhotoOrRecoderListener, CameraEventObserver.OnWifiCameraInfoListener, CameraEventObserver.OfflineListener, CameraEventObserver.OnBatteryAndChargingListener, CameraEventObserver.OnKeyPhotoOrRecoderListener, CameraEventObserver.OnKeyZoomListener, WDApplication.BackgroundOrForegroundListener {
    public static int CURRENT_SHOW_MODEL = 112;
    public static final int KEY_DOWN_TO_PHOTO = 250;
    public static final int KEY_DOWN_TO_VIDEO = 251;
    public static final int KEY_DOWN_TO_ZOOM = 252;
    public static final int OPEN_VOICE_SWITCH = 115;
    public static final int RECODER_AUTO_END = 1002;
    public static final int REFLASH_DEVICE_INFO = 101;
    public static final int REFLASH_IMAGE_SHOW = 100;
    public static final int SHOW_ALBUM_MODEL = 114;
    public static final int TAKE_PHOTO_MODEL = 112;
    public static final int TAKE_PHOTO_VIDEO_END = 2;
    public static final int TAKE_VIDEO_MODEL = 113;
    public static final int TAKE_VIDEO_REFLASH_TIME = 1;
    private Animation alphaAnimation;
    protected ImageView mAlbum;
    private RelativeLayout mAlbumRl;
    protected ImageView mBackBtn;
    private BaseCameraView mBaseCameraView;
    protected ImageView mBatteryIcon;
    private Bitmap mBitmap;
    private BottomView mBottomView;
    private ImageView mCameraShow;
    private RelativeLayout mContainerView;
    protected ImageView mLockIcon;
    protected RelativeLayout mLockIconRl;
    protected LowBatteryDialog mLowBatteryDialog;
    protected ImageView mMirrorIcon;
    private LinearLayout mMirrorRl;
    private int mOrientation;
    private OrientationEventListener mOrientationListener;
    protected ImageView mScreeBtn;
    protected ImageView mTakePhotoOrRecorder;
    private TextView mTakeVideoTime;
    private View mTakeVideoTimeDot;
    private RelativeLayout mTakeVideoTimeRl;
    private SpUtils spUtils;
    private Timer timer;
    private boolean mShowPermissson = false;
    private boolean isAutoStop = false;
    private Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                int iIntValue = ((Integer) message.obj).intValue();
                CameraShowActivity.this.mTakeVideoTime.setText(VideoUtils.timeToStr(iIntValue * 1000));
                if (!FunctionSwitch.IS_FBPRO || iIntValue < 600) {
                    return;
                }
                if (CameraShowActivity.this.timer != null) {
                    CameraShowActivity.this.timer.cancel();
                    CameraShowActivity.this.timer = null;
                }
                CameraShowActivity.this.isAutoStop = true;
                CameraManager.getInstance().stopVideoRecoder(CameraShowActivity.this);
                return;
            }
            if (i == 2) {
                String str = (String) message.obj;
                boolean z = message.arg2 == 0;
                if (message.arg1 == 2 && !CameraShowActivity.this.isFinishing() && !CameraShowActivity.this.isDestroyed()) {
                    UtilTools.showResultToast(CameraShowActivity.this, z);
                }
                if (z) {
                    CameraShowActivity.this.mBaseCameraView.saveFileFinish(str);
                    return;
                }
                return;
            }
            if (i == 100) {
                Bitmap bitmap = (Bitmap) message.obj;
                CameraShowActivity.this.mBitmap = bitmap;
                CameraShowActivity.this.mCameraShow.setImageBitmap(bitmap);
                return;
            }
            if (i == 101) {
                CameraShowActivity.this.reflashDeviceInfo((WifiCameraStatusInfo) message.obj);
                return;
            }
            if (i == 205) {
                CameraShowActivity.this.goSettingPermission();
                return;
            }
            if (i == 1002) {
                if (CameraShowActivity.this.timer != null) {
                    CameraShowActivity.this.timer.cancel();
                    CameraShowActivity.this.timer = null;
                }
                CameraShowActivity.this.mTakeVideoTime.setText("00:00:00");
                CameraShowActivity.this.timer = new Timer();
                CameraShowActivity.this.timer.schedule(new ReflashTimerTask(), 1000L, 1000L);
                CameraManager.getInstance().startVideoRecoder();
                return;
            }
            if (i == 19999) {
                CameraShowActivity.this.startRecoderUI();
                CameraShowActivity.this.mBaseCameraView.setTakeVideoing(true);
                CameraManager.getInstance().startVideoRecoder();
            } else if (i != 250) {
                if (i != 251) {
                    return;
                }
                CameraShowActivity.this.checkPermission(113);
            } else if (CameraShowActivity.this.timer == null) {
                CameraShowActivity.this.checkPermission(112);
            } else {
                CameraShowActivity.this.checkPermission(113);
            }
        }
    };
    private long takePhotoTime = 0;
    private final BroadcastReceiver mWifiChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LogWD.writeMsg(this, 2, "WifiChangeReceiver onReceive() action = " + action);
            if (((action.hashCode() == -1118166861 && action.equals(NotifyCode.LICENSE_CHECK_ERROR)) ? (byte) 0 : (byte) -1) == 0 && CameraShowActivity.this.mBaseCameraView != null) {
                CameraShowActivity.this.mBaseCameraView.licCheckFiald();
            }
        }
    };

    @Override
    public void onConnectErrorListener(int i) {
    }

    @Override
    public void onConnectingListener() {
    }

    @Override
    public void onForeground() {
    }

    @Override
    public void onOnlineListener() {
    }

    @Override
    public void onUpdataDeviceListener() {
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0413R.layout.activity_camerashow);
        if (FunctionSwitch.IS_FBPRO) {
            SystemUtil.setBlackStatusBar(this);
        } else {
            SystemUtil.setWhiteStatusBar(this);
        }
        FunctionSwitch.VOICE_SWITCH = false;
        initView();
        initData();
        initListener();
        registerReceiver();
        checkLic();
        if (FunctionSwitch.IS_VOICE_DEVICE) {
            AudioPlayManager.getInstance().init();
            AudioPlayManager.getInstance().startPlay();
        }
    }

    private void checkLic() {
        BaseCameraView baseCameraView;
        if (!FunctionSwitch.Lic_Need_Online_Burning || (baseCameraView = this.mBaseCameraView) == null) {
            return;
        }
        baseCameraView.licCheckFiald();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (FunctionSwitch.IS_VOICE_DEVICE) {
            AudioPlayManager.getInstance().resumePlay();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (FunctionSwitch.IS_VOICE_DEVICE) {
            AudioPlayManager.getInstance().pausePlay();
        }
    }

    private void closeOrOpenDeviceLed(boolean z) {
        WifiCameraLedStatus wifiCameraLedStatus = new WifiCameraLedStatus();
        if (z) {
            wifiCameraLedStatus.ledStatus = 1;
            wifiCameraLedStatus.lightValue = 100;
        } else {
            if (this.mShowPermissson) {
                return;
            }
            wifiCameraLedStatus.ledStatus = 0;
            wifiCameraLedStatus.lightValue = 0;
        }
        LogWD.writeMsg(this, 16, "closeOrOpenDeviceLed : " + WifiCameraApi.getInstance().gWifiCamera.cameraLedStatusSet(wifiCameraLedStatus, 1) + "   isOpen: " + z);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.mWifiChangeReceiver);
        Timer timer = this.timer;
        if (timer != null) {
            timer.cancel();
            this.timer = null;
            CameraManager.getInstance().stopVideoRecoder(null);
        }
        CameraManager.getInstance().removeEventObserverListenser(3, this);
        CameraManager.getInstance().removeEventObserverListenser(2, this);
        CameraManager.getInstance().removeEventObserverListenser(13, this);
        CameraManager.getInstance().removeEventObserverListenser(12, this);
        CameraManager.getInstance().removeEventObserverListenser(9, this);
        CameraManager.getInstance().removeEventObserverListenser(5, this);
        CameraManager.getInstance().removeEventObserverListenser(6, this);
        WDApplication.getInstance().removeBackgroundOrForegroundListener(this);
        BaseCameraView baseCameraView = this.mBaseCameraView;
        if (baseCameraView != null) {
            baseCameraView.onDestory();
        }
        if (FunctionSwitch.IS_VOICE_DEVICE) {
            AudioPlayManager.getInstance().stopPlay();
        }
    }

    private void initView() {
        this.mContainerView = (RelativeLayout) findViewById(C0413R.id.app_content_view);
    }

    private void initData() {
        this.spUtils = new SpUtils(this);
        setScreenBrightness();
        this.alphaAnimation = AnimationUtils.loadAnimation(this, C0413R.anim.recode_dot);
        if (FunctionSwitch.IS_FBPRO) {
            this.mBaseCameraView = new DentalMirrorViewYpc(this, false, true);
        } else {
            this.mBaseCameraView = new DentalMirrorView(this, false, true);
        }
        this.mContainerView.removeAllViews();
        this.mContainerView.addView(this.mBaseCameraView);
        setViewParameter();
        reflashDeviceInfo(CameraManager.getInstance().getWifiCameraStatusInfo());
    }

    private void setScreenBrightness() {
        getWindow().setFlags(128, 128);
        LogWD.writeMsg(this, 32768, "setScreenBrightness()");
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.screenBrightness = 0.5f;
        getWindow().setAttributes(attributes);
    }

    private void setViewParameter() {
        this.mBackBtn = this.mBaseCameraView.getmBackBtn();
        this.mScreeBtn = this.mBaseCameraView.getmScreeBtn();
        this.mCameraShow = this.mBaseCameraView.getmCameraShow();
        this.mLockIcon = this.mBaseCameraView.getmLockIcon();
        this.mLockIconRl = this.mBaseCameraView.getmLockIconRl();
        this.mBatteryIcon = this.mBaseCameraView.getmElectricity();
        this.mMirrorIcon = this.mBaseCameraView.getmMirrorIcon();
        this.mMirrorRl = this.mBaseCameraView.getmMirrorRl();
        this.mTakePhotoOrRecorder = this.mBaseCameraView.getmTakePhotoOrRecorder();
        this.mAlbum = this.mBaseCameraView.getmAlbum();
        this.mAlbumRl = this.mBaseCameraView.getmAlbumRl();
        this.mTakeVideoTimeRl = this.mBaseCameraView.getmTakeVideoTimeRl();
        this.mTakeVideoTime = this.mBaseCameraView.getmTakeVideoTime();
        this.mTakeVideoTimeDot = this.mBaseCameraView.getmTakeVideoTimeDot();
        this.mBottomView = this.mBaseCameraView.getmBottomView();
    }

    private void initListener() {
        setViewListener();
        CameraManager.getInstance().setOnAcceptBitmapListener(this);
        if (this.mBatteryIcon.getVisibility() == 0) {
            CameraManager.getInstance().setOnWifiCameraInfoListener(this);
            CameraManager.getInstance().setOnBatteryAndChargingListener(this);
        }
        CameraManager.getInstance().setOnOfflineListener(this);
        CameraManager.getInstance().setOnOnKeyPhotoOrRecoderListener(this);
        CameraManager.getInstance().setOnOnKeyZoomListener(this);
        WDApplication.getInstance().setBackgroundOrForegroundListener(this);
    }

    private void setAutoOrentationEvent() {
        if (this.mScreeBtn.getVisibility() == 0) {
            this.mOrientationListener = new OrientationEventListener(this) {
                @Override
                public void onOrientationChanged(int i) {
                    CameraShowActivity.this.phoneAutoRotate(i);
                }
            };
            this.mOrientationListener.enable();
        }
    }

    private void setViewListener() {
        setOnclickListener(this.mBackBtn);
        setOnclickListener(this.mScreeBtn);
        setOnclickListener(this.mLockIcon);
        setOnclickListener(this.mLockIconRl);
        setOnclickListener(this.mMirrorIcon);
        setOnclickListener(this.mTakePhotoOrRecorder);
        setOnclickListener(this.mAlbum);
        setOnclickListener(this.mMirrorRl);
        setOnclickListener(this.mAlbumRl);
    }

    public void setOnclickListener(View view) {
        if (view != null) {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case C0413R.id.camerashow_album:
            case C0413R.id.camerashow_album_rl:
                checkPermission(114);
                break;
            case C0413R.id.camerashow_back:
                exit();
                break;
            case C0413R.id.camerashow_mirror_change:
            case C0413R.id.camerashow_mirror_rl:
                this.mBaseCameraView.mirrorChange();
                break;
            case C0413R.id.camerashow_screen_change:
                changeLandOrPortrait(false);
                break;
            case C0413R.id.camerashow_screen_lock:
            case C0413R.id.camerashow_screen_lock_rl:
                this.mBaseCameraView.screenLock();
                break;
            case C0413R.id.camerashow_take_photo:
                if (!this.mBaseCameraView.isTakePhoto()) {
                    if (System.currentTimeMillis() - this.takePhotoTime > 1000) {
                        this.takePhotoTime = System.currentTimeMillis();
                        checkPermission(113);
                    }
                } else if (System.currentTimeMillis() - this.takePhotoTime > 500) {
                    this.takePhotoTime = System.currentTimeMillis();
                    checkPermission(112);
                }
                break;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x002b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void changeLandOrPortrait(boolean z) {
        if (z) {
            int i = this.mOrientation;
            if (i == 0) {
                setRequestedOrientation(1);
                changeScreen(false);
                sceenOrientation();
            } else if (i == 90) {
                setRequestedOrientation(8);
                changeScreen(true);
                sceenOrientation();
            } else if (i != 180) {
                if (i == 270) {
                    setRequestedOrientation(0);
                    changeScreen(true);
                    sceenOrientation();
                }
            }
        } else {
            sceenOrientation();
        }
        checkLic();
    }

    private void sceenOrientation() {
        BaseCameraView baseCameraView = this.mBaseCameraView;
        if (baseCameraView != null) {
            baseCameraView.onDestory();
        }
        boolean z = false;
        if (getResources().getConfiguration().orientation == 1) {
            setRequestedOrientation(0);
            changeScreen(true);
            z = true;
        } else {
            setRequestedOrientation(1);
            changeScreen(false);
        }
        if (FunctionSwitch.IS_FBPRO) {
            this.mBaseCameraView = new DentalMirrorViewYpc(this, z, true);
        } else {
            this.mBaseCameraView = new DentalMirrorView(this, z, true);
        }
        this.mContainerView.removeAllViews();
        this.mContainerView.addView(this.mBaseCameraView);
        setViewParameter();
        setViewListener();
        WifiCameraStatusInfo wifiCameraStatusInfo = CameraManager.getInstance().getWifiCameraStatusInfo();
        if (wifiCameraStatusInfo != null) {
            reflashBatteryInfo(wifiCameraStatusInfo.battery, wifiCameraStatusInfo.isCharge);
        }
    }

    @Override
    public void onAcceptBitmap(Bitmap bitmap) {
        if (isDestroyed() || isFinishing()) {
            return;
        }
        Message messageObtain = Message.obtain();
        messageObtain.obj = bitmap;
        messageObtain.what = 100;
        this.mHandler.sendMessage(messageObtain);
    }

    @Override
    public void onWifiCameraInfoListener(WifiCameraStatusInfo wifiCameraStatusInfo) {
        Message messageObtain = Message.obtain();
        messageObtain.obj = wifiCameraStatusInfo;
        messageObtain.what = 101;
        this.mHandler.sendMessage(messageObtain);
    }

    @Override
    public void onDeviceSeizeStatusChanged(WifiCameraStatusInfo wifiCameraStatusInfo) {
        if (wifiCameraStatusInfo.isusedbyother == 1) {
            finish();
        }
    }

    @Override
    public void onBatteryAndCharging(float f, boolean z) {
        WifiCameraStatusInfo wifiCameraStatusInfo = new WifiCameraStatusInfo();
        wifiCameraStatusInfo.Setbattery((int) f);
        wifiCameraStatusInfo.SetisCharge(z ? 1 : 0);
        wifiCameraStatusInfo.SetisLowPowerOff(f <= 10.0f ? 1 : 0);
        Message messageObtain = Message.obtain();
        messageObtain.obj = wifiCameraStatusInfo;
        messageObtain.what = 101;
        this.mHandler.sendMessage(messageObtain);
    }

    public void reflashDeviceInfo(WifiCameraStatusInfo wifiCameraStatusInfo) {
        if (wifiCameraStatusInfo == null) {
            return;
        }
        int i = wifiCameraStatusInfo.battery;
        int i2 = wifiCameraStatusInfo.isCharge;
        int i3 = wifiCameraStatusInfo.isLowPowerOff;
        int i4 = wifiCameraStatusInfo.isSensorOk;
        reflashBatteryInfo(i, i2);
        lowerBatteryHandler(i2, i);
    }

    private void reflashBatteryInfo(int i, int i2) {
        if (FunctionSwitch.IS_FBPRO) {
            if (i2 == 1) {
                if (i == 0) {
                    this.mBatteryIcon.setImageResource(C0413R.drawable.ic_charing_0_white);
                    return;
                }
                if (i > 0 && i <= 10) {
                    this.mBatteryIcon.setImageResource(C0413R.drawable.ic_charing_10_white);
                    return;
                }
                if (10 < i && i <= 20) {
                    this.mBatteryIcon.setImageResource(C0413R.drawable.ic_charing_20_white);
                    return;
                }
                if (20 < i && i <= 30) {
                    this.mBatteryIcon.setImageResource(C0413R.drawable.ic_charing_30_white);
                    return;
                }
                if (30 < i && i <= 40) {
                    this.mBatteryIcon.setImageResource(C0413R.drawable.ic_charing_40_white);
                    return;
                }
                if (40 < i && i <= 50) {
                    this.mBatteryIcon.setImageResource(C0413R.drawable.ic_charing_50_white);
                    return;
                }
                if (50 < i && i <= 60) {
                    this.mBatteryIcon.setImageResource(C0413R.drawable.ic_charing_60_white);
                    return;
                }
                if (60 < i && i <= 70) {
                    this.mBatteryIcon.setImageResource(C0413R.drawable.ic_charing_70_white);
                    return;
                }
                if (70 < i && i <= 80) {
                    this.mBatteryIcon.setImageResource(C0413R.drawable.ic_charing_80_white);
                    return;
                }
                if (80 < i && i <= 90) {
                    this.mBatteryIcon.setImageResource(C0413R.drawable.ic_charing_90_white);
                    return;
                } else {
                    if (90 >= i || i > 100) {
                        return;
                    }
                    this.mBatteryIcon.setImageResource(C0413R.drawable.ic_charing_100_white);
                    return;
                }
            }
            if (i == 0) {
                this.mBatteryIcon.setImageResource(C0413R.drawable.ic_battery_0_white);
                return;
            }
            if (i > 0 && i <= 10) {
                this.mBatteryIcon.setImageResource(C0413R.drawable.ic_battery_10_white);
                return;
            }
            if (10 < i && i <= 20) {
                this.mBatteryIcon.setImageResource(C0413R.drawable.ic_battery_20_white);
                return;
            }
            if (20 < i && i <= 30) {
                this.mBatteryIcon.setImageResource(C0413R.drawable.ic_battery_30_white);
                return;
            }
            if (30 < i && i <= 40) {
                this.mBatteryIcon.setImageResource(C0413R.drawable.ic_battery_40_white);
                return;
            }
            if (40 < i && i <= 50) {
                this.mBatteryIcon.setImageResource(C0413R.drawable.ic_battery_50_white);
                return;
            }
            if (50 < i && i <= 60) {
                this.mBatteryIcon.setImageResource(C0413R.drawable.ic_battery_60_white);
                return;
            }
            if (60 < i && i <= 70) {
                this.mBatteryIcon.setImageResource(C0413R.drawable.ic_battery_70_white);
                return;
            }
            if (70 < i && i <= 80) {
                this.mBatteryIcon.setImageResource(C0413R.drawable.ic_battery_80_white);
                return;
            }
            if (80 < i && i <= 90) {
                this.mBatteryIcon.setImageResource(C0413R.drawable.ic_battery_90_white);
                return;
            } else {
                if (90 >= i || i > 100) {
                    return;
                }
                this.mBatteryIcon.setImageResource(C0413R.drawable.ic_battery_100_white);
                return;
            }
        }
        if (i2 == 1) {
            if (i == 0) {
                this.mBatteryIcon.setImageResource(C0413R.drawable.ic_charing_0);
                return;
            }
            if (i > 0 && i <= 10) {
                this.mBatteryIcon.setImageResource(C0413R.drawable.ic_charing_10);
                return;
            }
            if (10 < i && i <= 20) {
                this.mBatteryIcon.setImageResource(C0413R.drawable.ic_charing_20);
                return;
            }
            if (20 < i && i <= 30) {
                this.mBatteryIcon.setImageResource(C0413R.drawable.ic_charing_30);
                return;
            }
            if (30 < i && i <= 40) {
                this.mBatteryIcon.setImageResource(C0413R.drawable.ic_charing_40);
                return;
            }
            if (40 < i && i <= 50) {
                this.mBatteryIcon.setImageResource(C0413R.drawable.ic_charing_50);
                return;
            }
            if (50 < i && i <= 60) {
                this.mBatteryIcon.setImageResource(C0413R.drawable.ic_charing_60);
                return;
            }
            if (60 < i && i <= 70) {
                this.mBatteryIcon.setImageResource(C0413R.drawable.ic_charing_70);
                return;
            }
            if (70 < i && i <= 80) {
                this.mBatteryIcon.setImageResource(C0413R.drawable.ic_charing_80);
                return;
            }
            if (80 < i && i <= 90) {
                this.mBatteryIcon.setImageResource(C0413R.drawable.ic_charing_90);
                return;
            } else {
                if (90 >= i || i > 100) {
                    return;
                }
                this.mBatteryIcon.setImageResource(C0413R.drawable.ic_charing_100);
                return;
            }
        }
        if (i == 0) {
            this.mBatteryIcon.setImageResource(C0413R.drawable.ic_battery_0);
            return;
        }
        if (i > 0 && i <= 10) {
            this.mBatteryIcon.setImageResource(C0413R.drawable.ic_battery_10);
            return;
        }
        if (10 < i && i <= 20) {
            this.mBatteryIcon.setImageResource(C0413R.drawable.ic_battery_20);
            return;
        }
        if (20 < i && i <= 30) {
            this.mBatteryIcon.setImageResource(C0413R.drawable.ic_battery_30);
            return;
        }
        if (30 < i && i <= 40) {
            this.mBatteryIcon.setImageResource(C0413R.drawable.ic_battery_40);
            return;
        }
        if (40 < i && i <= 50) {
            this.mBatteryIcon.setImageResource(C0413R.drawable.ic_battery_50);
            return;
        }
        if (50 < i && i <= 60) {
            this.mBatteryIcon.setImageResource(C0413R.drawable.ic_battery_60);
            return;
        }
        if (60 < i && i <= 70) {
            this.mBatteryIcon.setImageResource(C0413R.drawable.ic_battery_70);
            return;
        }
        if (70 < i && i <= 80) {
            this.mBatteryIcon.setImageResource(C0413R.drawable.ic_battery_80);
            return;
        }
        if (80 < i && i <= 90) {
            this.mBatteryIcon.setImageResource(C0413R.drawable.ic_battery_90);
        } else {
            if (90 >= i || i > 100) {
                return;
            }
            this.mBatteryIcon.setImageResource(C0413R.drawable.ic_battery_100);
        }
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
            showLowerBatteryDialog(false);
            return;
        }
        LowBatteryDialog lowBatteryDialog2 = this.mLowBatteryDialog;
        if (lowBatteryDialog2 == null || !lowBatteryDialog2.isShowing()) {
            return;
        }
        this.mLowBatteryDialog.dismiss();
    }

    private void showLowerBatteryDialog(boolean z) {
        if (isDestroyed() || isFinishing()) {
            return;
        }
        LowBatteryDialog lowBatteryDialog = this.mLowBatteryDialog;
        if (lowBatteryDialog != null && lowBatteryDialog.isShowing()) {
            this.mLowBatteryDialog.dismiss();
        }
        this.mLowBatteryDialog = new LowBatteryDialog(this, z);
        this.mLowBatteryDialog.setCanceledOnTouchOutside(false);
        this.mLowBatteryDialog.setCancelable(false);
        this.mLowBatteryDialog.show();
    }

    public void checkPermission(int i) {
        if (!PermissionInstans.getInstance().isHavaStoragePermission(this)) {
            this.mShowPermissson = true;
            if (new SpUtils(this).getBoolean(Constant.STORAGE_PERMISSION_TAG, false)) {
                showGoSettingPermissionDialog(C0413R.string.App_Permission_Title);
                return;
            } else {
                CURRENT_SHOW_MODEL = i;
                PermissionInstans.getInstance().requestPermissions(this);
                return;
            }
        }
        if (i == 112) {
            takePhoto();
        } else if (i == 113) {
            takeVideo();
        } else if (i == 114) {
            gotoAlbumList();
        }
    }

    private void gotoAlbumList() {
        MainFrameHandleInstance.getInstance().showAlbumListActivity(this, false);
        if (getResources().getConfiguration().orientation == 2) {
            this.mBottomView.moveLeft();
        }
    }

    private void showGoSettingPermissionDialog(int i) {
        GeneralDialog generalDialog = new GeneralDialog(this, this.mHandler, i);
        generalDialog.show();
        generalDialog.setButtonText(C0413R.string.App_Go_Setting, C0413R.string.App_Button_Cancel);
    }

    public void goSettingPermission() {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", getPackageName(), null));
        startActivityForResult(intent, PermissionInstans.GO_SETTING_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        this.mShowPermissson = false;
        if (i != 124) {
            if (i != 125) {
                return;
            }
            if (PermissionInstans.getInstance().isHaveRecordAudioPermission(this)) {
                takeVideo();
                return;
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.RECORD_AUDIO")) {
                    return;
                }
                new SpUtils(this).putBoolean(Constant.RECORD_AUDIO_PERMISSION_TAG, true);
                return;
            }
        }
        if (iArr[0] == 0) {
            WDApplication.getInstance().createTempCacheDir();
            if (PermissionInstans.getInstance().isHavaStoragePermission(this)) {
                int i2 = CURRENT_SHOW_MODEL;
                if (i2 == 112) {
                    takePhoto();
                    return;
                } else if (i2 == 113) {
                    takeVideo();
                    return;
                } else {
                    if (i2 == 114) {
                        gotoAlbumList();
                        return;
                    }
                    return;
                }
            }
            return;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.READ_EXTERNAL_STORAGE")) {
            return;
        }
        new SpUtils(this).putBoolean(Constant.STORAGE_PERMISSION_TAG, true);
    }

    private void takePhoto() {
        UtilTools.showResultToast(this, true);
        CameraManager.getInstance().takePhoto(this.mBitmap, this);
    }

    private void takeVideo() {
        this.isAutoStop = false;
        UtilTools.playTakeVideoAudio(this);
        if (this.timer == null) {
            this.mHandler.removeMessages(19999);
            this.mHandler.sendEmptyMessageDelayed(19999, 0L);
        } else {
            stopRecoderUI();
            CameraManager.getInstance().stopVideoRecoder(this);
            this.mBaseCameraView.setTakeVideoing(false);
        }
    }

    public void startRecoderUI() {
        this.mBackBtn.setVisibility(4);
        this.mTakeVideoTimeRl.setVisibility(0);
        BaseCameraView baseCameraView = this.mBaseCameraView;
        if (baseCameraView != null) {
            baseCameraView.startRecoderUI();
        }
        this.timer = new Timer();
        this.timer.schedule(new ReflashTimerTask(), 1000L, 1000L);
        this.mTakeVideoTimeDot.startAnimation(this.alphaAnimation);
    }

    private void stopRecoderUI() {
        Timer timer = this.timer;
        if (timer != null) {
            timer.cancel();
            this.timer = null;
        }
        this.mBackBtn.setVisibility(0);
        this.mTakeVideoTimeRl.setVisibility(8);
        BaseCameraView baseCameraView = this.mBaseCameraView;
        if (baseCameraView != null) {
            baseCameraView.stopRecoderUI();
        }
        Message messageObtain = Message.obtain();
        messageObtain.obj = 0;
        messageObtain.what = 1;
        this.mHandler.sendMessage(messageObtain);
        this.mTakeVideoTimeDot.clearAnimation();
    }

    @Override
    public void onOfflineListener() {
        exit();
    }

    private void exit() {
        LowBatteryDialog lowBatteryDialog = this.mLowBatteryDialog;
        if (lowBatteryDialog != null && lowBatteryDialog.isShowing()) {
            this.mLowBatteryDialog.dismiss();
        }
        Timer timer = this.timer;
        if (timer != null) {
            timer.cancel();
            this.timer = null;
            CameraManager.getInstance().stopVideoRecoder(null);
        }
        if (isFinishing() || isDestroyed()) {
            return;
        }
        finish();
    }

    @Override
    public void onKeyTakePhoto() {
        this.mHandler.sendEmptyMessage(250);
    }

    @Override
    public void onKeyRecoderBegin() {
        this.mHandler.sendEmptyMessage(KEY_DOWN_TO_VIDEO);
    }

    @Override
    public void onKeyRecoderEnd() {
        this.mHandler.sendEmptyMessage(KEY_DOWN_TO_VIDEO);
    }

    @Override
    public void onKeyZoom(boolean z) {
        this.mHandler.sendEmptyMessage(KEY_DOWN_TO_ZOOM);
    }

    @Override
    public void onBackground() {
        Timer timer = this.timer;
        if (timer != null) {
            timer.cancel();
            this.timer = null;
            CameraManager.getInstance().stopVideoRecoder(null);
            stopRecoderUI();
        }
    }

    private class ReflashTimerTask extends TimerTask {
        private int mRecoderTime;

        private ReflashTimerTask() {
            this.mRecoderTime = 0;
        }

        @Override
        public void run() {
            this.mRecoderTime++;
            Message messageObtain = Message.obtain();
            messageObtain.obj = Integer.valueOf(this.mRecoderTime);
            messageObtain.what = 1;
            CameraShowActivity.this.mHandler.sendMessage(messageObtain);
            if (CameraShowActivity.this.mBaseCameraView instanceof DentalMirrorView) {
                ((DentalMirrorView) CameraShowActivity.this.mBaseCameraView).reflashTouchTimer();
            }
        }
    }

    @Override
    public void onTakePhotoOrRecoderListener(boolean z, String str, int i) {
        if (isDestroyed() || isFinishing()) {
            return;
        }
        if (z) {
            this.spUtils.putString(Constant.SAVE_FILE_LAST_PATH, str);
        }
        if (i == 2 && this.isAutoStop) {
            this.isAutoStop = false;
            this.mHandler.sendEmptyMessage(1002);
            return;
        }
        LogWD.writeMsg(this, 2, "isSuccessful： = " + z + "   savePath： = " + str + "   takeType： = " + i);
        Message messageObtain = Message.obtain();
        messageObtain.what = 2;
        messageObtain.obj = str;
        messageObtain.arg1 = i;
        messageObtain.arg2 = !z ? 1 : 0;
        this.mHandler.sendMessage(messageObtain);
    }

    public void phoneAutoRotate(int i) {
        if (isFinishing() || this.timer != null || Constant.CAMERASHOW_IS_SCREEN) {
            return;
        }
        int i2 = this.mOrientation;
        if (i == -1) {
            this.mOrientation = -1;
            return;
        }
        if (i > 350 || i < 10) {
            this.mOrientation = 0;
        } else if (i > 80 && i < 100) {
            this.mOrientation = 90;
        } else if (i > 170 && i < 190) {
            this.mOrientation = 180;
        } else if (i > 260 && i < 280) {
            this.mOrientation = 270;
        }
        try {
            if (Settings.System.getInt(getContentResolver(), "accelerometer_rotation") == 0) {
                return;
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        if (i2 != this.mOrientation) {
            setRequestedOrientation(-1);
            changeLandOrPortrait(true);
        }
    }

    private void changeScreen(boolean z) {
        if (z) {
            getWindow().addFlags(1024);
            getWindow().getDecorView().setSystemUiVisibility(5894);
            return;
        }
        getWindow().clearFlags(1024);
        getWindow().getDecorView().setSystemUiVisibility(0);
        if (FunctionSwitch.IS_FBPRO) {
            SystemUtil.setBlackStatusBar(this);
        } else {
            SystemUtil.setWhiteStatusBar(this);
        }
    }

    protected void hideBottomUIMenu2() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            getWindow().getDecorView().setSystemUiVisibility(8);
        } else if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(4102);
        }
    }

    public void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NotifyCode.LICENSE_CHECK_ERROR);
        registerReceiver(this.mWifiChangeReceiver, intentFilter);
    }
}
