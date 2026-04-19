package com.i4season.bkCamera.uirelated.other.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.i4season.bkCamera.uirelated.other.FunctionSwitch;
import com.i4season.bkCamera.uirelated.other.Language.Strings;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.UtilTools;
import com.i4season.i4season_camera.C0413R;

public class BottomView extends LinearLayout implements View.OnClickListener {
    private Boolean ENABLE;
    private CameraScroller mCameraScroller;
    private Context mContext;
    private OnScrollerChangeListener mOnScrollerChangeListener;
    private int mWidth;
    private TextView photo;
    private TextView video;

    float f68x1;

    float f69x2;

    float f70y1;

    float f71y2;

    public interface OnScrollerChangeListener {
        void onScrollerChangeListener(boolean z);
    }

    public BottomView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f68x1 = 0.0f;
        this.f69x2 = 0.0f;
        this.f70y1 = 0.0f;
        this.f71y2 = 0.0f;
        this.ENABLE = true;
        this.mContext = context;
        LayoutInflater.from(context).inflate(C0413R.layout.camera_scroller_layout, (ViewGroup) this, true);
        this.mWidth = ((WindowManager) this.mContext.getSystemService("window")).getDefaultDisplay().getWidth();
    }

    public void init(OnScrollerChangeListener onScrollerChangeListener) {
        this.mOnScrollerChangeListener = onScrollerChangeListener;
        this.mCameraScroller = (CameraScroller) findViewById(C0413R.id.camera_scroller);
        this.photo = (TextView) this.mCameraScroller.findViewById(C0413R.id.camerashow_take_photo_tv);
        this.video = (TextView) this.mCameraScroller.findViewById(C0413R.id.camerashow_change_to_video_model);
        initData();
        moveLeft();
        if (FunctionSwitch.IS_FBPRO) {
            return;
        }
        this.video.setTextColor(getResources().getColor(C0413R.color.apptextblack3));
    }

    public void initData() {
        this.photo.setText(Strings.getString(C0413R.string.App_Take_Photo, this.mContext));
        this.video.setText(Strings.getString(C0413R.string.App_Take_Video, this.mContext));
    }

    public void moveLeft() {
        Log.d("liusheng", "moveLeft");
        CameraScroller cameraScroller = this.mCameraScroller;
        cameraScroller.leftIndex = UtilTools.getCurrentSelectedIndex() - 1;
        cameraScroller.rightIndex = UtilTools.getCurrentSelectedIndex();
        if (cameraScroller.getChildAt(cameraScroller.leftIndex) == null) {
            return;
        }
        cameraScroller.mScroller.startScroll(cameraScroller.getScrollX(), 0, -Math.round((cameraScroller.getChildAt(cameraScroller.leftIndex).getWidth() + cameraScroller.getChildAt(cameraScroller.rightIndex).getWidth()) / 2.0f), 0, cameraScroller.duration);
        cameraScroller.scrollToNext(cameraScroller.rightIndex, cameraScroller.leftIndex);
        UtilTools.setSelectedIndex(UtilTools.getCurrentSelectedIndex() - 1);
        cameraScroller.invalidate();
        OnScrollerChangeListener onScrollerChangeListener = this.mOnScrollerChangeListener;
        if (onScrollerChangeListener != null) {
            onScrollerChangeListener.onScrollerChangeListener(false);
        }
    }

    public void moveRight() {
        Log.d("liusheng", "moveRight");
        CameraScroller cameraScroller = this.mCameraScroller;
        cameraScroller.leftIndex = UtilTools.getCurrentSelectedIndex();
        cameraScroller.rightIndex = UtilTools.getCurrentSelectedIndex() + 1;
        if (cameraScroller.getChildAt(cameraScroller.rightIndex) == null) {
            return;
        }
        cameraScroller.mScroller.startScroll(cameraScroller.getScrollX(), 0, Math.round((cameraScroller.getChildAt(cameraScroller.leftIndex).getWidth() + cameraScroller.getChildAt(cameraScroller.rightIndex).getWidth()) / 2.0f), 0, cameraScroller.duration);
        cameraScroller.scrollToNext(cameraScroller.leftIndex, cameraScroller.rightIndex);
        UtilTools.setSelectedIndex(UtilTools.getCurrentSelectedIndex() + 1);
        cameraScroller.invalidate();
        OnScrollerChangeListener onScrollerChangeListener = this.mOnScrollerChangeListener;
        if (onScrollerChangeListener != null) {
            onScrollerChangeListener.onScrollerChangeListener(true);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.ENABLE.booleanValue()) {
            return true;
        }
        if (motionEvent.getAction() == 0) {
            this.f68x1 = motionEvent.getX();
            this.f70y1 = motionEvent.getY();
            System.out.println("xxxx1: " + this.f68x1);
        }
        if (motionEvent.getAction() == 1) {
            this.f69x2 = motionEvent.getX();
            this.f71y2 = motionEvent.getY();
            System.out.println("xxxx2: " + this.f69x2);
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            if (layoutParams.width > 0) {
                this.mWidth = layoutParams.width;
            }
            int i = this.photo.getLayoutParams().width + 100;
            float f = this.f68x1;
            float f2 = this.f69x2;
            if (f - f2 > 50.0f) {
                if (UtilTools.getCurrentSelectedIndex() < 2) {
                    moveRight();
                }
            } else if (f2 - f > 50.0f) {
                if (UtilTools.getCurrentSelectedIndex() > 0) {
                    moveLeft();
                }
            } else if (f - f2 < 5.0f || f2 - f < 5.0f) {
                int i2 = this.mWidth / 2;
                float f3 = this.f68x1;
                if (f3 > i2 - i && f3 < i2) {
                    moveLeft();
                } else {
                    float f4 = this.f68x1;
                    if (f4 > i2 && f4 < i2 + i) {
                        moveRight();
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        if (this.ENABLE.booleanValue()) {
            int id = view.getId();
            if (id == C0413R.id.camerashow_change_to_video_model) {
                moveRight();
            } else {
                if (id != C0413R.id.camerashow_take_photo_tv) {
                    return;
                }
                moveLeft();
            }
        }
    }

    public void setTouchEnabled(boolean z) {
        this.ENABLE = Boolean.valueOf(z);
    }
}
