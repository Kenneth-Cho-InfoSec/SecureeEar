package com.i4season.bkCamera.uirelated.functionpage.camerashow.view;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.core.view.InputDeviceCompat;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.Constant;
import com.i4season.bkCamera.uirelated.other.view.BottomView;

public class BaseCameraView extends RelativeLayout {
    public static final int ACCEPT_BATTARY_INFO = 143;
    public static final int ACCEPT_THRESHOLD = 142;
    public static final int GYROSCOPE_CHANGE_ANGLE = 141;
    public static final int GYROSCOPE_INITIAL_ANGLE = 140;
    public static final int LONG_TIME_PROMPT = 144;
    public static final int REFLASH_ALBUM = 146;
    public static final int REFLASH_TIME = 145;
    protected boolean isTakePhoto;
    protected boolean isTakeVideoing;
    protected boolean licCheckFail;
    protected ImageView mAlbum;
    protected RelativeLayout mAlbumRl;
    protected TextView mAlbumText;
    protected ImageView mAlbumThumb;
    protected ImageView mBackBtn;
    protected ImageView mBatteryIcon;
    protected LinearLayout mBottomBar;
    protected BottomView mBottomView;
    protected ImageView mCameraShow;
    protected ImageView mCircleZhezhao;
    protected Context mContext;
    protected ImageView mElectricity;
    protected RelativeLayout mFindNewLic;
    protected TextView mFindNewLicTv;
    protected boolean mGyroscopeSwitch;
    protected ImageView mLockIcon;
    protected RelativeLayout mLockIconRl;
    protected ImageView mMirrorIcon;
    protected LinearLayout mMirrorRl;
    protected TextView mMirrorText;
    protected ImageView mScreeBtn;
    protected ImageView mTakePhotoOrRecorder;
    protected TextView mTakeVideoTime;
    protected View mTakeVideoTimeDot;
    protected RelativeLayout mTakeVideoTimeRl;
    protected LinearLayout mTopBar;
    protected View mTouchView;

    public void albumRefresh() {
    }

    public void changeLandOrPortrait(boolean z) {
    }

    public void changeLanguage() {
    }

    public void licCheckFiald() {
    }

    public void mirrorChange() {
    }

    public void onDestory() {
    }

    public void saveFileFinish(String str) {
    }

    public void screenLock() {
    }

    public void startRecoderUI() {
    }

    public void stopRecoderUI() {
    }

    public void updateUi(boolean z) {
    }

    public BaseCameraView(Context context) {
        super(context);
        this.isTakePhoto = true;
        this.isTakeVideoing = false;
        this.licCheckFail = false;
    }

    protected void hideBottomUIMenu(Context context) {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            ((Activity) context).getWindow().getDecorView().setSystemUiVisibility(8);
        } else if (Build.VERSION.SDK_INT >= 19) {
            ((Activity) context).getWindow().getDecorView().setSystemUiVisibility(InputDeviceCompat.SOURCE_TOUCHSCREEN);
        }
    }

    public boolean ismIsLock() {
        return Constant.CAMERASHOW_IS_SCREEN;
    }

    public ImageView getmCameraShow() {
        return this.mCameraShow;
    }

    public RelativeLayout getmTakeVideoTimeRl() {
        return this.mTakeVideoTimeRl;
    }

    public TextView getmTakeVideoTime() {
        return this.mTakeVideoTime;
    }

    public View getmTakeVideoTimeDot() {
        return this.mTakeVideoTimeDot;
    }

    public ImageView getmTakePhotoOrRecorder() {
        return this.mTakePhotoOrRecorder;
    }

    public ImageView getmBackBtn() {
        return this.mBackBtn;
    }

    public ImageView getmScreeBtn() {
        return this.mScreeBtn;
    }

    public ImageView getmCircleZhezhao() {
        return this.mCircleZhezhao;
    }

    public boolean isTakePhoto() {
        return this.isTakePhoto;
    }

    public ImageView getmElectricity() {
        return this.mElectricity;
    }

    public ImageView getmAlbum() {
        return this.mAlbum;
    }

    public boolean isTakeVideoing() {
        return this.isTakeVideoing;
    }

    public void setTakeVideoing(boolean z) {
        this.isTakeVideoing = z;
    }

    public boolean ismGyroscopeSwitch() {
        return this.mGyroscopeSwitch;
    }

    public ImageView getmBatteryIcon() {
        return this.mBatteryIcon;
    }

    public ImageView getmMirrorIcon() {
        return this.mMirrorIcon;
    }

    public ImageView getmLockIcon() {
        return this.mLockIcon;
    }

    public RelativeLayout getmLockIconRl() {
        return this.mLockIconRl;
    }

    public RelativeLayout getmAlbumRl() {
        return this.mAlbumRl;
    }

    public LinearLayout getmMirrorRl() {
        return this.mMirrorRl;
    }

    public BottomView getmBottomView() {
        return this.mBottomView;
    }
}
