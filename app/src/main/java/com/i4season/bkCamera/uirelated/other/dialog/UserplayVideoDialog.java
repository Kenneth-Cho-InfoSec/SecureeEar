package com.i4season.bkCamera.uirelated.other.dialog;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.Constant;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.FileUtil;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.VideoUtils;
import com.i4season.i4season_camera.C0413R;

public class UserplayVideoDialog extends Dialog implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    public static final int DIALOG_DISMISS = 4;
    public static final int HIDE_UI = 3;
    private static final int REFLASH_PLAY_TIME = 1;
    public static final int RESTART_VIDEO = 2;
    private boolean isMove;
    private ImageView mBack;
    private Context mContext;
    private Handler mHandler;
    private int mLastPlayTime;
    private TextView mMaxTime;
    private ImageView mPlayer;
    private int mPoint;
    private TextView mSecretName;
    private SeekBar mSeekBar;
    private TextView mStartTime;
    private VideoView mSurface;
    private RelativeLayout mVideoLl;
    private LinearLayout mVideoTopLl;
    private View mView;
    private FrameLayout mZhezhao;

    private Uri getSecretUri() {
        return null;
    }

    public UserplayVideoDialog(Context context, int i) {
        super(context, C0413R.style.wdDialog);
        this.isMove = false;
        this.mHandler = new Handler() {
            @Override
            public void dispatchMessage(Message message) {
                int i2 = message.what;
                if (i2 == 1) {
                    int currentPosition = UserplayVideoDialog.this.mSurface.getCurrentPosition();
                    UserplayVideoDialog userplayVideoDialog = UserplayVideoDialog.this;
                    userplayVideoDialog.mLastPlayTime = currentPosition == 0 ? userplayVideoDialog.mLastPlayTime : currentPosition;
                    UserplayVideoDialog.this.mSeekBar.setProgress(currentPosition);
                    UserplayVideoDialog.this.mStartTime.setText(VideoUtils.timeToStr(currentPosition));
                    UserplayVideoDialog.this.mHandler.sendEmptyMessageDelayed(1, 1000L);
                    return;
                }
                if (i2 == 2) {
                    if (UserplayVideoDialog.this.mSurface != null) {
                        UserplayVideoDialog.this.videoOnStart();
                    }
                } else if (i2 == 3) {
                    UserplayVideoDialog.this.mVideoLl.setVisibility(8);
                    UserplayVideoDialog.this.mVideoTopLl.setVisibility(8);
                } else {
                    if (i2 != 4) {
                        return;
                    }
                    UserplayVideoDialog.this.dismiss();
                }
            }
        };
        this.mContext = context;
        this.mPoint = i;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0413R.layout.dialog_video_user);
        getWindow().setFormat(-3);
        initView();
        initData();
        initListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        VideoView videoView = this.mSurface;
        if (videoView == null || videoView.isPlaying()) {
            return;
        }
        this.mSurface.seekTo(this.mLastPlayTime);
        videoOnStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (this.mSurface != null) {
            videoOnPause();
        }
    }

    private void initView() {
        this.mZhezhao = (FrameLayout) findViewById(C0413R.id.video_player_zhezhao);
        this.mSurface = (VideoView) findViewById(C0413R.id.video_player_surfaceView);
        this.mVideoLl = (RelativeLayout) findViewById(C0413R.id.video_player_ll);
        this.mVideoTopLl = (LinearLayout) findViewById(C0413R.id.video_top_ll);
        this.mSecretName = (TextView) findViewById(C0413R.id.secret_video_plar_name);
        this.mPlayer = (ImageView) findViewById(C0413R.id.secert_video_player);
        this.mSeekBar = (SeekBar) findViewById(C0413R.id.secert_video_seekbar);
        this.mStartTime = (TextView) findViewById(C0413R.id.secert_video_start_time);
        this.mMaxTime = (TextView) findViewById(C0413R.id.secert_video_max_time);
        this.mBack = (ImageView) findViewById(C0413R.id.secret_video_back);
        this.mView = findViewById(C0413R.id.video_player_view);
    }

    private void initData() {
        String strTimeToStr;
        this.mSurface.getHolder().setFormat(-2);
        Uri secretUri = getSecretUri();
        int videoTimeToRaw = FileUtil.getVideoTimeToRaw(this.mContext, secretUri);
        if (videoTimeToRaw <= 0) {
            strTimeToStr = "00:00";
        } else {
            this.mSeekBar.setMax(videoTimeToRaw);
            strTimeToStr = VideoUtils.timeToStr(videoTimeToRaw);
        }
        this.mMaxTime.setText(strTimeToStr);
        this.mStartTime.setText("00:00");
        this.mSurface.setVideoURI(secretUri);
        this.mSurface.start();
        this.mHandler.sendEmptyMessageDelayed(3, Constant.RECORD_DEAFAULT_TIME);
    }

    private void initListener() {
        this.mPlayer.setOnClickListener(this);
        this.mBack.setOnClickListener(this);
        this.mView.setOnClickListener(this);
        this.mSeekBar.setOnSeekBarChangeListener(this);
        this.mSurface.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                UserplayVideoDialog.this.mSurface.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mediaPlayer2, int i, int i2) {
                        if (i != 3) {
                            return true;
                        }
                        UserplayVideoDialog.this.mSurface.setBackgroundColor(0);
                        return true;
                    }
                });
                UserplayVideoDialog.this.mSeekBar.setMax(UserplayVideoDialog.this.mSurface.getDuration());
                UserplayVideoDialog.this.mHandler.sendEmptyMessageDelayed(1, 1000L);
            }
        });
        this.mSurface.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (UserplayVideoDialog.this.mPoint == 0) {
                    UserplayVideoDialog.this.mPlayer.setImageResource(C0413R.drawable.ic_takevideo_pause_secret);
                } else {
                    UserplayVideoDialog.this.mPlayer.setImageResource(C0413R.drawable.ic_takevideo_pause);
                }
                UserplayVideoDialog.this.mVideoLl.setVisibility(0);
                UserplayVideoDialog.this.mVideoTopLl.setVisibility(0);
                UserplayVideoDialog.this.videoOnStart();
                UserplayVideoDialog.this.videoOnPause();
            }
        });
        this.mSurface.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case C0413R.id.secert_video_player:
                if (this.mSurface.isPlaying()) {
                    videoOnPause();
                } else {
                    videoOnStart();
                }
                break;
            case C0413R.id.secret_video_back:
                dismissHandler();
                break;
            case C0413R.id.video_player_surfaceView:
            case C0413R.id.video_player_view:
                this.mVideoLl.setVisibility(0);
                this.mVideoTopLl.setVisibility(0);
                this.mHandler.removeMessages(3);
                this.mHandler.sendEmptyMessageDelayed(3, Constant.RECORD_DEAFAULT_TIME);
                break;
        }
    }

    public void dismissHandler() {
        this.mSurface.setBackgroundColor(-1);
        if (this.mSurface.isPlaying()) {
            videoOnPause();
        }
        this.mHandler.sendEmptyMessageDelayed(4, 10L);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        if (this.isMove) {
            this.mHandler.removeMessages(1);
            this.mSurface.seekTo(i);
            this.mStartTime.setText(VideoUtils.timeToStr(this.mSurface.getCurrentPosition()));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        this.isMove = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        this.isMove = false;
        this.mHandler.removeMessages(1);
        int currentPosition = this.mSurface.getCurrentPosition();
        this.mSurface.seekTo(currentPosition);
        this.mStartTime.setText(VideoUtils.timeToStr(currentPosition));
        this.mHandler.sendEmptyMessageDelayed(1, 1000L);
    }

    public void videoOnPause() {
        if (this.mPoint == 0) {
            this.mPlayer.setImageResource(C0413R.drawable.ic_takevideo_pause_secret);
        } else {
            this.mPlayer.setImageResource(C0413R.drawable.ic_takevideo_pause);
        }
        this.mSurface.pause();
    }

    public void videoOnStart() {
        if (this.mPoint == 0) {
            this.mPlayer.setImageResource(C0413R.drawable.ic_videoplay_pausebtn_secret);
        } else {
            this.mPlayer.setImageResource(C0413R.drawable.ic_videoplay_pausebtn_normal);
        }
        this.mSurface.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (isOutOfBounds(getContext(), motionEvent)) {
            dismissHandler();
        }
        return super.onTouchEvent(motionEvent);
    }

    private boolean isOutOfBounds(Context context, MotionEvent motionEvent) {
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        int scaledWindowTouchSlop = ViewConfiguration.get(context).getScaledWindowTouchSlop();
        View decorView = getWindow().getDecorView();
        int i = -scaledWindowTouchSlop;
        return x < i || y < i || x > decorView.getWidth() + scaledWindowTouchSlop || y > decorView.getHeight() + scaledWindowTouchSlop;
    }
}
