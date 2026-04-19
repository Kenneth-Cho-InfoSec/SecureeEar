package com.i4season.bkCamera.uirelated.filenodeopen.videoplay;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.bean.FileNode;
import com.i4season.bkCamera.uirelated.filenodeopen.FileNodeOpenInstance;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.Constant;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.FileUtil;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.SystemUtil;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.UtilTools;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.VideoUtils;
import com.i4season.bkCamera.uirelated.other.logmanage.LogWD;
import com.i4season.i4season_camera.C0413R;
import java.util.List;

public class VideoPlayerNewActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    public static final int HIDE_UI = 3;
    private static final int REFLASH_PLAY_TIME = 1;
    public static final int RESTART_VIDEO = 2;
    public static List<FileNode> fileNodes;
    private static int mCurrPlayIndex;
    private ImageView mBack;
    private int mLastPlayTime;
    private TextView mMaxTime;
    private ImageView mPlayer;
    private TextView mSecretName;
    private SeekBar mSeekBar;
    private TextView mStartTime;
    private VideoView mSurface;
    private RelativeLayout mVideoLl;
    private RelativeLayout mVideoTopLl;
    private View mView;
    private boolean isMove = false;
    private Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                int currentPosition = VideoPlayerNewActivity.this.mSurface.getCurrentPosition();
                VideoPlayerNewActivity videoPlayerNewActivity = VideoPlayerNewActivity.this;
                videoPlayerNewActivity.mLastPlayTime = currentPosition == 0 ? videoPlayerNewActivity.mLastPlayTime : currentPosition;
                VideoPlayerNewActivity.this.mSeekBar.setProgress(currentPosition);
                VideoPlayerNewActivity.this.mStartTime.setText(VideoUtils.timeToStr(currentPosition));
                VideoPlayerNewActivity.this.mHandler.sendEmptyMessageDelayed(1, 1000L);
                return;
            }
            if (i == 2) {
                if (VideoPlayerNewActivity.this.mSurface != null) {
                    VideoPlayerNewActivity.this.videoOnStart();
                }
            } else {
                if (i != 3) {
                    return;
                }
                VideoPlayerNewActivity.this.mVideoLl.setVisibility(8);
                VideoPlayerNewActivity.this.mVideoTopLl.setVisibility(8);
            }
        }
    };

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setFlags(128, 128);
        SystemUtil.setTransparent(this);
        hideBottomUIMenu();
        setContentView(C0413R.layout.activity_video_new);
        receiverIntentDate();
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

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void receiverIntentDate() {
        LogWD.writeMsg(this, 32768, "receiverIntentDate()");
        mCurrPlayIndex = FileNodeOpenInstance.getInstance().getmOpenIndex();
        LogWD.writeMsg(this, 32768, "receiverIntentDate() 接收Intent的数据 mCurrPlayIndex = " + mCurrPlayIndex);
        fileNodes = FileNodeOpenInstance.getInstance().getmVideoFileList();
    }

    private void initView() {
        this.mSurface = (VideoView) findViewById(C0413R.id.video_player_surfaceView);
        this.mVideoLl = (RelativeLayout) findViewById(C0413R.id.video_player_ll);
        this.mVideoTopLl = (RelativeLayout) findViewById(C0413R.id.video_top_ll);
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
        Uri videoUri = getVideoUri();
        int videoTimeToRaw = FileUtil.getVideoTimeToRaw(this, videoUri);
        if (videoTimeToRaw <= 0) {
            strTimeToStr = "00:00:00";
        } else {
            this.mSeekBar.setMax(videoTimeToRaw);
            strTimeToStr = VideoUtils.timeToStr(videoTimeToRaw);
        }
        this.mMaxTime.setText(strTimeToStr);
        this.mStartTime.setText("00:00:00");
        this.mSurface.setVideoURI(videoUri);
        this.mSurface.start();
        this.mHandler.sendEmptyMessageDelayed(3, Constant.RECORD_DEAFAULT_TIME);
    }

    private Uri getVideoUri() {
        String str = fileNodes.get(mCurrPlayIndex).getmFileDevPath();
        this.mSecretName.setText((mCurrPlayIndex + 1) + "/" + fileNodes.size());
        return Uri.parse(UtilTools.getInfoUTF8toStr(str));
    }

    private void initListener() {
        this.mPlayer.setOnClickListener(this);
        this.mBack.setOnClickListener(this);
        this.mView.setOnClickListener(this);
        this.mSeekBar.setOnSeekBarChangeListener(this);
        this.mSurface.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                VideoPlayerNewActivity.this.mSeekBar.setMax(VideoPlayerNewActivity.this.mSurface.getDuration());
                VideoPlayerNewActivity.this.mHandler.sendEmptyMessageDelayed(1, 1000L);
            }
        });
        this.mSurface.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                VideoPlayerNewActivity.this.mPlayer.setImageResource(C0413R.drawable.ic_takevideo_pause);
                VideoPlayerNewActivity.this.mVideoLl.setVisibility(0);
                VideoPlayerNewActivity.this.mVideoTopLl.setVisibility(0);
                VideoPlayerNewActivity.this.videoOnStart();
                VideoPlayerNewActivity.this.videoOnPause();
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
                finish();
                overridePendingTransition(0, 0);
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
        this.mPlayer.setImageResource(C0413R.drawable.ic_takevideo_pause);
        this.mSurface.pause();
    }

    public void videoOnStart() {
        this.mPlayer.setImageResource(C0413R.drawable.ic_videoplay_pausebtn_normal);
        this.mSurface.start();
    }

    protected void hideBottomUIMenu() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            getWindow().getDecorView().setSystemUiVisibility(8);
        } else if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(4102);
        }
    }
}
