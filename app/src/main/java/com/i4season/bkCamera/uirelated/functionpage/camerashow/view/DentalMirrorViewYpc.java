package com.i4season.bkCamera.uirelated.functionpage.camerashow.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import com.bumptech.glide.Glide;
import com.i4season.bkCamera.logicrelated.camera.CameraConstant;
import com.i4season.bkCamera.uirelated.other.FunctionSwitch;
import com.i4season.bkCamera.uirelated.other.Language.LanguageInfo;
import com.i4season.bkCamera.uirelated.other.Language.Strings;
import com.i4season.bkCamera.uirelated.other.animation.Rotate3dAnimation;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.Constant;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.SpUtils;
import com.i4season.bkCamera.uirelated.other.view.BottomView;
import com.i4season.bkCamera.uirelated.other.view.TransformativeImageView;
import com.i4season.bkCamera.uirelated.other.view.VerticalTextView;
import com.i4season.i4season_camera.C0413R;
import java.util.Timer;
import java.util.TimerTask;

public class DentalMirrorViewYpc extends BaseCameraView implements View.OnClickListener, TransformativeImageView.onTouchCallBack, BottomView.OnScrollerChangeListener {
    public static final int TOUCH_HIED_VIEW = 200;
    private float SCALE_TIMES;
    private RelativeLayout mBgLl;
    private RelativeLayout mFunctionRl;
    private LinearLayout mGyroscopeLl;
    private RelativeLayout mGyroscopeOffRl;
    private VerticalTextView mGyroscopeOffTv;
    private RelativeLayout mGyroscopeOnRl;
    private VerticalTextView mGyroscopeOnTv;
    private Handler mHandler;
    private boolean mIsLand;
    private boolean mIsRecoder;
    private RelativeLayout.LayoutParams mLayoutParams;
    private RelativeLayout mTopbarRl;
    private int mTouchTime;
    private Timer mTouchTimer;
    private ImageView voiceImgSwitch;

    private void ratioChange(int i) {
    }

    @Override
    public void onClickCallBack() {
    }

    @Override
    public void onDoubleClickCallBack() {
    }

    static int access$108(DentalMirrorViewYpc dentalMirrorViewYpc) {
        int i = dentalMirrorViewYpc.mTouchTime;
        dentalMirrorViewYpc.mTouchTime = i + 1;
        return i;
    }

    public DentalMirrorViewYpc(Context context, boolean z, boolean z2) {
        super(context);
        this.SCALE_TIMES = 1.0f;
        this.mTouchTime = 0;
        this.mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void dispatchMessage(Message message) {
                if (message.what != 200) {
                    return;
                }
                DentalMirrorViewYpc.this.hideOrShowView(true);
            }
        };
        this.mContext = context;
        init(z, z2);
    }

    private void init(boolean z, boolean z2) {
        this.mIsLand = z;
        initView(z);
        initData(z, z2);
        initListener();
    }

    private void initView(boolean z) {
        View viewInflate;
        if (!z) {
            viewInflate = inflate(this.mContext, C0413R.layout.layout_dental_mirror_ypc, null);
        } else {
            viewInflate = inflate(this.mContext, C0413R.layout.layout_dental_mirror_land_ypc, null);
        }
        removeAllViews();
        addView(viewInflate);
        this.mCameraShow = (ImageView) viewInflate.findViewById(C0413R.id.camera_image);
        this.mTakePhotoOrRecorder = (ImageView) viewInflate.findViewById(C0413R.id.camerashow_take_photo);
        this.mTakeVideoTimeRl = (RelativeLayout) viewInflate.findViewById(C0413R.id.camerashow_take_recode_ll);
        this.mTakeVideoTime = (TextView) viewInflate.findViewById(C0413R.id.camera_take_recode_time);
        this.mTakeVideoTimeDot = viewInflate.findViewById(C0413R.id.camera_take_recode_dot);
        this.mBackBtn = (ImageView) viewInflate.findViewById(C0413R.id.camerashow_back);
        this.mElectricity = (ImageView) viewInflate.findViewById(C0413R.id.camerashow_electricity);
        this.mScreeBtn = (ImageView) viewInflate.findViewById(C0413R.id.camerashow_screen_change);
        this.mLockIcon = (ImageView) viewInflate.findViewById(C0413R.id.camerashow_screen_lock);
        this.mLockIconRl = (RelativeLayout) viewInflate.findViewById(C0413R.id.camerashow_screen_lock_rl);
        this.mAlbum = (ImageView) viewInflate.findViewById(C0413R.id.camerashow_album);
        this.mAlbumThumb = (ImageView) viewInflate.findViewById(C0413R.id.camerashow_album_thumb);
        this.mAlbumText = (TextView) viewInflate.findViewById(C0413R.id.camerashow_album_text);
        this.mGyroscopeOnTv = (VerticalTextView) viewInflate.findViewById(C0413R.id.gyroscope_on_tv);
        this.mGyroscopeOffTv = (VerticalTextView) viewInflate.findViewById(C0413R.id.gyroscope_off_tv);
        this.mFunctionRl = (RelativeLayout) viewInflate.findViewById(C0413R.id.camerashow_function_rl_bottom);
        this.mGyroscopeLl = (LinearLayout) viewInflate.findViewById(C0413R.id.camerashow_gyroscope_rl);
        this.mTopbarRl = (RelativeLayout) viewInflate.findViewById(C0413R.id.camerashow_topbar);
        this.mAlbumRl = (RelativeLayout) viewInflate.findViewById(C0413R.id.camerashow_album_rl);
        this.mMirrorRl = (LinearLayout) viewInflate.findViewById(C0413R.id.camerashow_mirror_rl);
        this.mMirrorIcon = (ImageView) viewInflate.findViewById(C0413R.id.camerashow_mirror_change);
        this.mMirrorText = (TextView) viewInflate.findViewById(C0413R.id.camerashow_mirror_text);
        this.mBottomView = (BottomView) findViewById(C0413R.id.photo_video_bv);
        this.mBgLl = (RelativeLayout) findViewById(C0413R.id.detail_mirror_ll);
        this.mTouchView = findViewById(C0413R.id.camera_show_touch_hide);
        this.mGyroscopeOnRl = (RelativeLayout) findViewById(C0413R.id.gyroscope_on_rl);
        this.mGyroscopeOffRl = (RelativeLayout) findViewById(C0413R.id.gyroscope_off_rl);
        this.voiceImgSwitch = (ImageView) findViewById(C0413R.id.voice_switch);
    }

    @Override
    public void licCheckFiald() {
        this.licCheckFail = true;
        this.mTakePhotoOrRecorder.setEnabled(false);
        this.mScreeBtn.setEnabled(false);
        this.mLockIcon.setEnabled(false);
        this.mLockIconRl.setEnabled(false);
        this.mAlbum.setEnabled(false);
        this.mAlbumRl.setEnabled(false);
        this.mGyroscopeOnTv.setEnabled(false);
        this.mGyroscopeOffTv.setEnabled(false);
        this.mMirrorRl.setEnabled(false);
        this.mMirrorIcon.setEnabled(false);
        this.mTouchView.setEnabled(false);
        this.mBgLl.setEnabled(false);
        this.mBottomView.setTouchEnabled(false);
    }

    private void initData(boolean z, boolean z2) {
        Constant.BITMAP_IS_SCALE = false;
        this.mScreeBtn.setEnabled(true);
        this.mBottomView.init(this);
        this.mGyroscopeOnTv.setText(Strings.getString(C0413R.string.App_Gyroscope_Off, this.mContext));
        this.mGyroscopeOffTv.setText(Strings.getString(C0413R.string.App_Gyroscope_On, this.mContext));
        this.mGyroscopeOnTv.setHorizontal(!this.mIsLand);
        this.mGyroscopeOffTv.setHorizontal(true ^ this.mIsLand);
        this.mMirrorText.setText(Strings.getString(C0413R.string.App_Lable_Ear_Right, this.mContext));
        this.mAlbumText.setText(Strings.getString(C0413R.string.App_Lable_Album, this.mContext));
        this.mTakeVideoTimeRl.setVisibility(8);
        String setLanguage = LanguageInfo.getInstance().getSetLanguage(this.mContext);
        if (setLanguage.equals(Strings.LANGUAGE_SPANISH) || setLanguage.equals(Strings.LANGUAGE_FRENCH)) {
            this.mGyroscopeOnTv.setTextSize(10);
            this.mGyroscopeOffTv.setTextSize(10);
        } else if (setLanguage.equals(Strings.LANGUAGE_GERMAN)) {
            this.mGyroscopeOnTv.setTextSize(12);
            this.mGyroscopeOffTv.setTextSize(12);
        } else if (setLanguage.equals(Strings.LANGUAGE_ITALIAN)) {
            this.mGyroscopeOnTv.setTextSize(8);
            this.mGyroscopeOffTv.setTextSize(10);
        }
        String string = new SpUtils(this.mContext).getString(Constant.SAVE_FILE_LAST_PATH, "");
        if (!TextUtils.isEmpty(string)) {
            saveFileFinish(string);
        }
        startTouchTimer();
        gyroscopeLlAnmation();
    }

    private void gyroscopeLlAnmation() {
        if (this.mIsLand) {
            RotateAnimation rotateAnimation = new RotateAnimation(0.0f, 180.0f, 1, 0.5f, 1, 0.5f);
            rotateAnimation.setFillAfter(true);
            rotateAnimation.setInterpolator(new AccelerateInterpolator());
            this.mGyroscopeLl.startAnimation(rotateAnimation);
        }
    }

    private void startTouchTimer() {
        this.mTouchTimer = new Timer();
        this.mTouchTimer.schedule(new TouchTimerTask(), 1000L, 1000L);
    }

    private class TouchTimerTask extends TimerTask {
        private TouchTimerTask() {
        }

        @Override
        public void run() {
            if (DentalMirrorViewYpc.this.mTouchTime >= 20) {
                DentalMirrorViewYpc.this.mHandler.sendEmptyMessage(200);
            }
            DentalMirrorViewYpc.access$108(DentalMirrorViewYpc.this);
        }
    }

    @Override
    public void onScrollerChangeListener(boolean z) {
        this.mTouchTime = 0;
        functionChange(!z);
    }

    private void initListener() {
        this.mGyroscopeOnTv.setOnClickListener(this);
        this.mGyroscopeOffTv.setOnClickListener(this);
        this.mTouchView.setOnClickListener(this);
        this.mGyroscopeOnRl.setOnClickListener(this);
        this.mGyroscopeOffRl.setOnClickListener(this);
        this.voiceImgSwitch.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        this.mTouchTime = 0;
        if (this.licCheckFail) {
        }
        switch (view.getId()) {
            case C0413R.id.camera_show_touch_hide:
            case C0413R.id.detail_mirror_ll:
                if (!this.mIsRecoder) {
                    hideOrShowView(!Constant.CAMERASHOW_IS_HIDE);
                    break;
                }
                break;
            case C0413R.id.camerashow_change_to_video_model:
                functionChange(false);
                break;
            case C0413R.id.gyroscope_off_rl:
            case C0413R.id.gyroscope_off_tv:
                if (!this.mIsRecoder) {
                    if (Constant.BITMAP_IS_SCALE) {
                        mirrorChange();
                    }
                    wideOrFocus(!this.mIsLand);
                    break;
                }
                break;
            case C0413R.id.gyroscope_on_rl:
            case C0413R.id.gyroscope_on_tv:
                if (!this.mIsRecoder) {
                    if (Constant.BITMAP_IS_SCALE) {
                        mirrorChange();
                    }
                    wideOrFocus(this.mIsLand);
                    break;
                }
                break;
            case C0413R.id.voice_switch:
                if (FunctionSwitch.VOICE_SWITCH) {
                    FunctionSwitch.VOICE_SWITCH = false;
                    this.voiceImgSwitch.setImageResource(C0413R.drawable.ic_voice_close);
                } else {
                    FunctionSwitch.VOICE_SWITCH = true;
                    this.voiceImgSwitch.setImageResource(C0413R.drawable.ic_voice_open);
                }
                break;
        }
    }

    private void wideOrFocus(boolean z) {
        if (z) {
            if (this.mIsLand) {
                this.mGyroscopeOffRl.setBackgroundResource(C0413R.drawable.shape_white_bottom_corner30_ypc);
                this.mGyroscopeOnRl.setBackground(null);
            } else {
                this.mGyroscopeOffRl.setBackgroundResource(C0413R.drawable.shape_white_right_corner30_ypc);
                this.mGyroscopeOnRl.setBackground(null);
            }
            this.mGyroscopeOnTv.setTextColor(-1);
            this.mGyroscopeOffTv.setTextColor(ViewCompat.MEASURED_STATE_MASK);
            Constant.CAMERASHOW_IS_DETAIL = true;
            return;
        }
        if (this.mIsLand) {
            this.mGyroscopeOnRl.setBackgroundResource(C0413R.drawable.shape_white_top_corner30_ypc);
            this.mGyroscopeOffRl.setBackground(null);
        } else {
            this.mGyroscopeOnRl.setBackgroundResource(C0413R.drawable.shape_white_left_corner30_ypc);
            this.mGyroscopeOffRl.setBackground(null);
        }
        this.mGyroscopeOffTv.setTextColor(-1);
        this.mGyroscopeOnTv.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        Constant.CAMERASHOW_IS_DETAIL = false;
    }

    @Override
    public void changeLanguage() {
        super.changeLanguage();
        this.mGyroscopeOnTv.setText(Strings.getString(C0413R.string.App_Gyroscope_On, this.mContext));
        this.mGyroscopeOffTv.setText(Strings.getString(C0413R.string.App_Gyroscope_Off, this.mContext));
        this.mBottomView.initData();
    }

    private void functionChange(boolean z) {
        if (!z) {
            this.mTakePhotoOrRecorder.setImageResource(C0413R.drawable.ic_take_video_white);
        } else {
            this.mTakePhotoOrRecorder.setImageResource(C0413R.drawable.ic_take_photo_white);
        }
        this.isTakePhoto = z;
    }

    private void gyrocopeUnable(int i) {
        if (i == 1) {
            this.mGyroscopeOnTv.setEnabled(true);
            this.mGyroscopeOffTv.setEnabled(true);
        } else {
            this.mGyroscopeOnTv.setEnabled(false);
            this.mGyroscopeOffTv.setEnabled(false);
        }
    }

    @Override
    public void mirrorChange() {
        this.mMirrorIcon.clearAnimation();
        if (!Constant.BITMAP_IS_SCALE) {
            Rotate3dAnimation rotate3dAnimation = new Rotate3dAnimation(this.mMirrorIcon.getWidth() / 2, this.mMirrorIcon.getHeight() / 2, 180.0f, false);
            rotate3dAnimation.setFillAfter(true);
            this.mMirrorIcon.startAnimation(rotate3dAnimation);
            this.mMirrorText.setText(Strings.getString(C0413R.string.App_Lable_Ear_Left, this.mContext));
        } else {
            Rotate3dAnimation rotate3dAnimation2 = new Rotate3dAnimation(this.mMirrorIcon.getWidth() / 2, this.mMirrorIcon.getHeight() / 2, 0.0f, false);
            rotate3dAnimation2.setFillAfter(true);
            this.mMirrorIcon.startAnimation(rotate3dAnimation2);
            this.mMirrorText.setText(Strings.getString(C0413R.string.App_Lable_Ear_Right, this.mContext));
        }
        Constant.BITMAP_IS_SCALE = !Constant.BITMAP_IS_SCALE;
    }

    @Override
    public void saveFileFinish(String str) {
        this.mAlbumText.setVisibility(0);
        this.mAlbum.setVisibility(0);
        this.mAlbumThumb.setVisibility(8);
        Glide.with(this.mContext).load(str).into(this.mAlbumThumb);
    }

    @Override
    public void changeLandOrPortrait(boolean z) {
        onDestory();
    }

    @Override
    public void startRecoderUI() {
        if (this.isTakePhoto) {
            this.mBottomView.moveRight();
        }
        this.mIsRecoder = true;
        this.mTakePhotoOrRecorder.setVisibility(0);
        this.mElectricity.setVisibility(8);
        this.mGyroscopeLl.clearAnimation();
        this.mGyroscopeLl.setVisibility(8);
        this.mAlbumRl.setVisibility(8);
        this.mMirrorRl.setVisibility(8);
        this.mScreeBtn.setVisibility(8);
        this.mLockIcon.setVisibility(8);
        this.mBottomView.setVisibility(8);
        this.voiceImgSwitch.setVisibility(8);
    }

    @Override
    public void stopRecoderUI() {
        this.mTakePhotoOrRecorder.setImageResource(C0413R.drawable.ic_take_video_white);
        this.mIsRecoder = false;
        this.mElectricity.setVisibility(0);
        this.mGyroscopeLl.setVisibility(0);
        gyroscopeLlAnmation();
        this.mAlbumRl.setVisibility(0);
        this.mMirrorRl.setVisibility(0);
        this.mScreeBtn.setVisibility(0);
        this.mLockIcon.setVisibility(0);
        this.mBottomView.setVisibility(0);
    }

    @Override
    public void screenLock() {
        if (this.mIsRecoder) {
            return;
        }
        if (!Constant.CAMERASHOW_IS_SCREEN) {
            this.mLockIcon.setImageResource(C0413R.drawable.ic_screen_unlock_white);
            this.mBackBtn.setVisibility(4);
            this.mScreeBtn.setVisibility(4);
            this.mAlbumRl.setVisibility(4);
            this.mTakePhotoOrRecorder.setVisibility(4);
            this.mMirrorIcon.setVisibility(4);
            this.mGyroscopeLl.clearAnimation();
            this.mGyroscopeLl.setVisibility(4);
            this.mMirrorRl.setVisibility(4);
            this.mBottomView.setVisibility(4);
        } else {
            this.mLockIcon.setImageResource(C0413R.drawable.ic_screen_lock_white);
            this.mBackBtn.setVisibility(0);
            this.mScreeBtn.setVisibility(0);
            this.mTakePhotoOrRecorder.setVisibility(0);
            this.mMirrorIcon.setVisibility(0);
            this.mAlbumRl.setVisibility(0);
            this.mGyroscopeLl.setVisibility(0);
            gyroscopeLlAnmation();
            this.mMirrorRl.setVisibility(0);
            this.mBottomView.setVisibility(0);
        }
        Constant.CAMERASHOW_IS_SCREEN = !Constant.CAMERASHOW_IS_SCREEN;
    }

    public void hideOrShowView(boolean z) {
        if (Constant.CAMERASHOW_IS_SCREEN || this.mIsRecoder) {
            reflashTouchTimer();
            return;
        }
        if (z) {
            this.mLockIcon.setVisibility(4);
            this.mBackBtn.setVisibility(4);
            this.mScreeBtn.setVisibility(4);
            this.mAlbumRl.setVisibility(4);
            this.mTakePhotoOrRecorder.setVisibility(4);
            this.mMirrorIcon.setVisibility(4);
            this.mGyroscopeLl.clearAnimation();
            this.mGyroscopeLl.setVisibility(4);
            this.mMirrorRl.setVisibility(4);
            this.mBottomView.setVisibility(4);
        } else {
            this.mLockIcon.setVisibility(0);
            this.mBackBtn.setVisibility(0);
            this.mScreeBtn.setVisibility(0);
            this.mTakePhotoOrRecorder.setVisibility(0);
            this.mMirrorIcon.setVisibility(0);
            this.mAlbumRl.setVisibility(0);
            this.mGyroscopeLl.setVisibility(0);
            gyroscopeLlAnmation();
            this.mMirrorRl.setVisibility(0);
            this.mBottomView.setVisibility(0);
            this.mLockIcon.setImageResource(C0413R.drawable.ic_screen_lock_white);
        }
        Constant.CAMERASHOW_IS_HIDE = z;
    }

    public void reflashTouchTimer() {
        this.mTouchTime = 0;
    }

    @Override
    public void onDestory() {
        super.onDestory();
        Constant.CAMERASHOW_IS_DETAIL = false;
        Constant.CAMERASHOW_IS_SCREEN = false;
        CameraConstant.GYROSCOPE_SWITCH = true;
        this.mTouchTime = 0;
        Timer timer = this.mTouchTimer;
        if (timer != null) {
            timer.cancel();
            this.mTouchTimer = null;
        }
    }
}
