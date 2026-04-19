package com.i4season.bkCamera.uirelated.other.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.widget.AppCompatImageView;
import com.i4season.bkCamera.uirelated.other.FunctionSwitch;
import com.i4season.i4season_camera.C0413R;

public class TransformativeImageView extends AppCompatImageView {
    private static final int DEFAULT_REVERT_DURATION = 300;
    private static final int HORIZONTAL = 0;
    private static final float MAX_SCALE_FACTOR = 2.0f;
    private static final float MIN_SCALE_FACTOR = 1.0f;
    private static final int SCALE_BY_FINGER_MID_POINT = 1;
    private static final int SCALE_BY_IMAGE_CENTER = 0;
    private static final float UNSPECIFIED_SCALE_FACTOR = -1.0f;
    private static final int VERTICAL = 1;
    private boolean canTranslate;
    private float centerX;
    private float centerY;
    private long clickIntervalTime;
    private long currentClick;
    private boolean isCanNarrow;
    private boolean isCanRotate;
    private boolean isJz;
    private boolean isRation;
    protected boolean isTransforming;
    private long lastClick;
    protected boolean mCanDrag;
    private boolean mCanRotate;
    private boolean mCanScale;
    private PointF mCurrentMidPoint;
    private PointF mCurrentPoint1;
    private PointF mCurrentPoint2;
    private PointF mCurrentVector;
    private PaintFlagsDrawFilter mDrawFilter;
    private float[] mFromMatrixValue;
    private float mHorizontalMinScaleFactor;
    protected RectF mImageRect;
    protected PointF mLastMidPoint;
    private PointF mLastPoint1;
    private PointF mLastPoint2;
    private int mLastScaleValue;
    private PointF mLastVector;
    protected Matrix mMatrix;
    private float mMaxScaleFactor;
    private float mMinScaleFactor;
    private boolean mOpenAnimator;
    private boolean mOpenRotateRevert;
    private boolean mOpenScaleRevert;
    private boolean mOpenTranslateRevert;
    private MatrixRevertAnimator mRevertAnimator;
    private int mRevertDuration;
    private int mScaleBy;
    private float mScaleFactor;
    private float mScaleFactorValue;
    private float[] mToMatrixValue;
    private float mVerticalMinScaleFactor;
    private onTouchCallBack onTouchCallBack;
    private PointF scaleCenter;
    private float[] xAxis;
    private static final String TAG = TransformativeImageView.class.getSimpleName();
    public static float INIT_SCALE_FACTOR = 0.98f;

    public interface onTouchCallBack {
        void onClickCallBack();

        void onDoubleClickCallBack();
    }

    public TransformativeImageView(Context context) {
        this(context, null);
    }

    public TransformativeImageView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public TransformativeImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mRevertDuration = DEFAULT_REVERT_DURATION;
        this.mMaxScaleFactor = MAX_SCALE_FACTOR;
        this.mMinScaleFactor = UNSPECIFIED_SCALE_FACTOR;
        this.mVerticalMinScaleFactor = MIN_SCALE_FACTOR;
        this.mHorizontalMinScaleFactor = MIN_SCALE_FACTOR;
        this.mMatrix = new Matrix();
        this.mImageRect = new RectF();
        this.mOpenScaleRevert = false;
        this.mOpenRotateRevert = false;
        this.mOpenTranslateRevert = false;
        this.mOpenAnimator = false;
        this.isRation = false;
        this.isCanRotate = true;
        this.isCanNarrow = false;
        this.mDrawFilter = new PaintFlagsDrawFilter(0, 3);
        this.mLastPoint1 = new PointF();
        this.mLastPoint2 = new PointF();
        this.mCurrentPoint1 = new PointF();
        this.mCurrentPoint2 = new PointF();
        this.mScaleFactor = MIN_SCALE_FACTOR;
        this.mScaleFactorValue = MIN_SCALE_FACTOR;
        this.mCanScale = false;
        this.mLastMidPoint = new PointF();
        this.mCurrentMidPoint = new PointF();
        this.mCanDrag = false;
        this.mLastVector = new PointF();
        this.mCurrentVector = new PointF();
        this.mCanRotate = false;
        this.mRevertAnimator = new MatrixRevertAnimator();
        this.mFromMatrixValue = new float[9];
        this.mToMatrixValue = new float[9];
        this.isTransforming = false;
        this.lastClick = 0L;
        this.currentClick = 0L;
        this.clickIntervalTime = 200L;
        this.mScaleBy = 0;
        this.scaleCenter = new PointF();
        this.xAxis = new float[]{MIN_SCALE_FACTOR, 0.0f};
        this.canTranslate = false;
        this.isJz = false;
        this.mLastScaleValue = 1;
        obtainAttrs(attributeSet);
        init();
    }

    private void obtainAttrs(AttributeSet attributeSet) {
        if (attributeSet == null) {
            return;
        }
        TypedArray typedArrayObtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, C0413R.styleable.TransformativeImageView);
        this.mMaxScaleFactor = typedArrayObtainStyledAttributes.getFloat(1, MAX_SCALE_FACTOR);
        this.mMinScaleFactor = typedArrayObtainStyledAttributes.getFloat(2, UNSPECIFIED_SCALE_FACTOR);
        this.mRevertDuration = typedArrayObtainStyledAttributes.getInteger(8, DEFAULT_REVERT_DURATION);
        this.mOpenScaleRevert = typedArrayObtainStyledAttributes.getBoolean(5, false);
        this.mOpenRotateRevert = typedArrayObtainStyledAttributes.getBoolean(4, false);
        this.mOpenTranslateRevert = typedArrayObtainStyledAttributes.getBoolean(6, false);
        this.mOpenAnimator = typedArrayObtainStyledAttributes.getBoolean(3, true);
        this.mScaleBy = typedArrayObtainStyledAttributes.getInt(9, 0);
        this.isRation = typedArrayObtainStyledAttributes.getBoolean(7, false);
        this.isCanRotate = typedArrayObtainStyledAttributes.getBoolean(0, true);
        typedArrayObtainStyledAttributes.recycle();
    }

    private void init() {
        setScaleType(ImageView.ScaleType.MATRIX);
        this.mRevertAnimator.setDuration(this.mRevertDuration);
    }

    @Override
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        initImgPositionAndSize();
    }

    public void initImgPositionAndSize() {
        this.mMatrix.reset();
        refreshImageRect();
        if (this.isRation) {
            this.mHorizontalMinScaleFactor = Math.min(getWidth() / this.mImageRect.height(), getHeight() / this.mImageRect.width());
            this.mVerticalMinScaleFactor = Math.min(getHeight() / this.mImageRect.height(), getWidth() / this.mImageRect.width());
        } else {
            this.mHorizontalMinScaleFactor = Math.min(getWidth() / this.mImageRect.width(), getHeight() / this.mImageRect.height());
            this.mVerticalMinScaleFactor = Math.min(getHeight() / this.mImageRect.width(), getWidth() / this.mImageRect.height());
        }
        float f = this.mHorizontalMinScaleFactor * INIT_SCALE_FACTOR;
        this.mScaleFactor = f;
        this.mMatrix.postScale(f, f, this.mImageRect.centerX(), this.mImageRect.centerY());
        refreshImageRect();
        if (this.isRation) {
            this.mMatrix.postRotate(90 - FunctionSwitch.Angle, this.mImageRect.centerX(), this.mImageRect.centerY());
        } else {
            this.mMatrix.postRotate(0.0f, this.mImageRect.centerX(), this.mImageRect.centerY());
        }
        this.mMatrix.postTranslate((((getRight() - getLeft()) / 2) - this.mImageRect.centerX()) - FunctionSwitch.row, (((getBottom() - getTop()) / 2) - this.mImageRect.centerY()) + FunctionSwitch.col);
        applyMatrix();
        float f2 = this.mMinScaleFactor;
        if (f2 != UNSPECIFIED_SCALE_FACTOR) {
            if (this.isCanNarrow) {
                this.mHorizontalMinScaleFactor = f2;
                this.mVerticalMinScaleFactor = f2;
            } else {
                float f3 = this.mScaleFactor;
                this.mHorizontalMinScaleFactor = f3;
                this.mVerticalMinScaleFactor = f3;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.setDrawFilter(this.mDrawFilter);
        super.onDraw(canvas);
    }

    /* JADX WARN: Removed duplicated region for block: B:32:0x0050  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x00a5  */
    @Override
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        PointF midPointOfFinger = getMidPointOfFinger(motionEvent);
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.mLastMidPoint.set(midPointOfFinger);
            this.isTransforming = false;
            this.mRevertAnimator.cancel();
            this.mCanRotate = false;
            this.mCanScale = false;
            this.mCanDrag = false;
            if (motionEvent.getPointerCount() == 2) {
                this.mCanScale = true;
                this.mLastPoint1.set(motionEvent.getX(0), motionEvent.getY(0));
                this.mLastPoint2.set(motionEvent.getX(1), motionEvent.getY(1));
                if (this.isCanRotate) {
                    this.mCanRotate = true;
                }
                this.mLastVector.set(motionEvent.getX(1) - motionEvent.getX(0), motionEvent.getY(1) - motionEvent.getY(0));
                this.lastClick = 0L;
            } else if (motionEvent.getPointerCount() == 1) {
                this.mCanDrag = true;
                onDoubleClick();
            }
        } else if (actionMasked == 1) {
            if (this.mOpenRotateRevert || this.mOpenScaleRevert || this.mOpenTranslateRevert) {
                this.mMatrix.getValues(this.mFromMatrixValue);
                if (this.mOpenRotateRevert) {
                    checkRotation();
                }
                if (this.mOpenScaleRevert) {
                    checkScale();
                }
                if (this.mOpenTranslateRevert && this.canTranslate) {
                    checkBorder();
                }
                this.mMatrix.getValues(this.mToMatrixValue);
                if (this.mOpenAnimator) {
                    this.mRevertAnimator.setMatrixValue(this.mFromMatrixValue, this.mToMatrixValue);
                    this.mRevertAnimator.cancel();
                    this.mRevertAnimator.start();
                } else {
                    applyMatrix();
                }
            }
            this.mCanScale = false;
            this.mCanDrag = false;
            this.mCanRotate = false;
        } else if (actionMasked == 2) {
            if (this.mCanDrag) {
                translate(midPointOfFinger);
            }
            if (this.mCanScale) {
                scale(motionEvent);
            }
            if (this.mCanRotate) {
                rotate(motionEvent);
            }
            if (!getImageMatrix().equals(this.mMatrix)) {
                this.isTransforming = true;
            }
            if (this.mCanDrag || this.mCanScale || this.mCanRotate) {
                applyMatrix();
            }
        } else {
            if (actionMasked != 3) {
                if (actionMasked != 5) {
                    if (actionMasked == 6) {
                    }
                }
            }
            this.mCanScale = false;
            this.mCanDrag = false;
            this.mCanRotate = false;
        }
        super.onTouchEvent(motionEvent);
        return true;
    }

    private void rotate(MotionEvent motionEvent) {
        this.mCurrentVector.set(motionEvent.getX(1) - motionEvent.getX(0), motionEvent.getY(1) - motionEvent.getY(0));
        this.mMatrix.postRotate(getRotateDegree(this.mLastVector, this.mCurrentVector), this.mImageRect.centerX(), this.mImageRect.centerY());
        this.mLastVector.set(this.mCurrentVector);
    }

    private float getRotateDegree(PointF pointF, PointF pointF2) {
        return (float) Math.toDegrees(Math.atan2(pointF2.y, pointF2.x) - Math.atan2(pointF.y, pointF.x));
    }

    protected void translate(PointF pointF) {
        if (this.canTranslate) {
            this.mMatrix.postTranslate(pointF.x - this.mLastMidPoint.x, pointF.y - this.mLastMidPoint.y);
            this.mLastMidPoint.set(pointF);
        }
    }

    private PointF getMidPointOfFinger(MotionEvent motionEvent) {
        this.mCurrentMidPoint.set(0.0f, 0.0f);
        int pointerCount = motionEvent.getPointerCount();
        for (int i = 0; i < pointerCount; i++) {
            this.mCurrentMidPoint.x += motionEvent.getX(i);
            this.mCurrentMidPoint.y += motionEvent.getY(i);
        }
        float f = pointerCount;
        this.mCurrentMidPoint.x /= f;
        this.mCurrentMidPoint.y /= f;
        return this.mCurrentMidPoint;
    }

    private PointF getScaleCenter() {
        int i = this.mScaleBy;
        if (i == 0) {
            this.scaleCenter.set(this.mImageRect.centerX(), this.mImageRect.centerY());
        } else if (i == 1) {
            this.scaleCenter.set(this.mLastMidPoint.x, this.mLastMidPoint.y);
        }
        return this.scaleCenter;
    }

    private void scale(MotionEvent motionEvent) {
        this.canTranslate = true;
        PointF scaleCenter = getScaleCenter();
        this.mCurrentPoint1.set(motionEvent.getX(0), motionEvent.getY(0));
        this.mCurrentPoint2.set(motionEvent.getX(1), motionEvent.getY(1));
        float fDistance = distance(this.mCurrentPoint1, this.mCurrentPoint2) / distance(this.mLastPoint1, this.mLastPoint2);
        this.mScaleFactor *= fDistance;
        this.mMatrix.postScale(fDistance, fDistance, scaleCenter.x, scaleCenter.y);
        this.mLastPoint1.set(this.mCurrentPoint1);
        this.mLastPoint2.set(this.mCurrentPoint2);
        checkScale();
    }

    private float distance(PointF pointF, PointF pointF2) {
        float f = pointF2.x - pointF.x;
        float f2 = pointF2.y - pointF.y;
        return (float) Math.sqrt((f * f) + (f2 * f2));
    }

    private void checkRotation() {
        float currentRotateDegree = getCurrentRotateDegree();
        float fAbs = Math.abs(currentRotateDegree);
        float f = (fAbs <= 45.0f || fAbs > 135.0f) ? (fAbs <= 135.0f || fAbs > 225.0f) ? (fAbs <= 225.0f || fAbs > 315.0f) ? 0.0f : 270.0f : 180.0f : 90.0f;
        if (currentRotateDegree < 0.0f) {
            f = -f;
        }
        this.mMatrix.postRotate(f - currentRotateDegree, this.mImageRect.centerX(), this.mImageRect.centerY());
    }

    private float getCurrentRotateDegree() {
        float[] fArr = this.xAxis;
        fArr[0] = 1.0f;
        fArr[1] = 0.0f;
        this.mMatrix.mapVectors(fArr);
        float[] fArr2 = this.xAxis;
        return (float) Math.toDegrees(Math.atan2(fArr2[1], fArr2[0]));
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0027  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0016  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void checkScale() {
        float f;
        PointF scaleCenter = getScaleCenter();
        int iImgOrientation = imgOrientation();
        if (iImgOrientation == 0) {
            float f2 = this.mScaleFactor;
            float f3 = this.mHorizontalMinScaleFactor;
            if (f2 < f3) {
                f = f3 / f2;
            } else if (iImgOrientation == 1) {
                float f4 = this.mScaleFactor;
                float f5 = this.mVerticalMinScaleFactor;
                if (f4 < f5) {
                    f = f5 / f4;
                    this.canTranslate = false;
                    this.isJz = true;
                } else {
                    float f6 = this.mScaleFactor;
                    float f7 = this.mMaxScaleFactor;
                    f = f6 > f7 ? f7 / f6 : MIN_SCALE_FACTOR;
                }
            }
        }
        this.mMatrix.postScale(f, f, scaleCenter.x, scaleCenter.y);
        this.mScaleFactor *= f;
        if (this.isJz) {
            this.isJz = false;
            this.mMatrix.postTranslate((((getRight() - getLeft()) / 2) - this.mImageRect.centerX()) - FunctionSwitch.row, (((getBottom() - getTop()) / 2) - this.mImageRect.centerY()) + FunctionSwitch.col);
            onTouchCallBack ontouchcallback = this.onTouchCallBack;
            if (ontouchcallback != null) {
                ontouchcallback.onDoubleClickCallBack();
            }
            this.mLastScaleValue = 1;
        }
    }

    private int imgOrientation() {
        float fAbs = Math.abs(getCurrentRotateDegree());
        return (fAbs <= 45.0f || fAbs > 135.0f) ? 0 : 1;
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0057  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x007c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void checkBorder() {
        float width;
        float fCenterX;
        float f;
        float height;
        float fCenterY;
        refreshImageRect();
        float f2 = 0.0f;
        if (this.mImageRect.width() <= getWidth()) {
            width = getWidth() / 2;
            fCenterX = this.mImageRect.centerX();
        } else {
            if (this.mImageRect.left > 0.0f) {
                f = -this.mImageRect.left;
            } else if (this.mImageRect.right < getWidth()) {
                width = getWidth();
                fCenterX = this.mImageRect.right;
            } else {
                f = 0.0f;
            }
            if (this.mImageRect.height() > getHeight()) {
                height = getHeight() / 2;
                fCenterY = this.mImageRect.centerY();
            } else {
                if (this.mImageRect.top > 0.0f) {
                    f2 = -this.mImageRect.top;
                } else if (this.mImageRect.bottom < getHeight()) {
                    height = getHeight();
                    fCenterY = this.mImageRect.bottom;
                }
                this.mMatrix.postTranslate(f, f2);
            }
            f2 = height - fCenterY;
            this.mMatrix.postTranslate(f, f2);
        }
        f = width - fCenterX;
        if (this.mImageRect.height() > getHeight()) {
        }
        f2 = height - fCenterY;
        this.mMatrix.postTranslate(f, f2);
    }

    public void applyMatrix() {
        refreshImageRect();
        setImageMatrix(this.mMatrix);
    }

    private void refreshImageRect() {
        if (getDrawable() != null) {
            this.mImageRect.set(getDrawable().getBounds());
            Matrix matrix = this.mMatrix;
            RectF rectF = this.mImageRect;
            matrix.mapRect(rectF, rectF);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mRevertAnimator.cancel();
    }

    private class MatrixRevertAnimator extends ValueAnimator implements ValueAnimator.AnimatorUpdateListener {
        private float[] mFromMatrixValue;
        private float[] mInterpolateMatrixValue = new float[9];
        private float[] mToMatrixValue;

        MatrixRevertAnimator() {
            setFloatValues(0.0f, TransformativeImageView.MIN_SCALE_FACTOR);
            addUpdateListener(this);
        }

        void setMatrixValue(float[] fArr, final float[] fArr2) {
            this.mFromMatrixValue = fArr;
            this.mToMatrixValue = fArr2;
            addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    TransformativeImageView.this.mMatrix.setValues(fArr2);
                    TransformativeImageView.this.applyMatrix();
                }
            });
        }

        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            if (this.mFromMatrixValue == null || this.mToMatrixValue == null || this.mInterpolateMatrixValue == null) {
                return;
            }
            for (int i = 0; i < 9; i++) {
                float fFloatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                float[] fArr = this.mInterpolateMatrixValue;
                float[] fArr2 = this.mFromMatrixValue;
                fArr[i] = fArr2[i] + ((this.mToMatrixValue[i] - fArr2[i]) * fFloatValue);
            }
            TransformativeImageView.this.mMatrix.setValues(this.mInterpolateMatrixValue);
            TransformativeImageView.this.applyMatrix();
        }
    }

    @Override
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int size = View.MeasureSpec.getSize(i);
        int size2 = View.MeasureSpec.getSize(i2);
        this.centerX = size / 2;
        this.centerY = size2 / 2;
    }

    public void changeSmallOrBig(boolean z) {
        float f;
        getScaleCenter();
        if (z) {
            f = 0.8f;
            this.mScaleFactorValue -= 0.2f;
        } else {
            f = 1.2f;
            this.mScaleFactorValue += 0.2f;
        }
        if (this.mScaleFactorValue == MIN_SCALE_FACTOR) {
            initImgPositionAndSize();
            return;
        }
        this.mScaleFactor *= f;
        this.mMatrix.postScale(f, f, this.centerX, this.centerY);
        if (!getImageMatrix().equals(this.mMatrix)) {
            this.isTransforming = true;
        }
        if (this.mCanDrag || this.mCanScale || this.mCanRotate) {
            applyMatrix();
        }
        if (this.mOpenRotateRevert || this.mOpenScaleRevert || this.mOpenTranslateRevert) {
            this.mMatrix.getValues(this.mFromMatrixValue);
            if (this.mOpenRotateRevert) {
                checkRotation();
            }
            if (this.mOpenScaleRevert) {
                checkScale();
            }
            if (this.mOpenTranslateRevert) {
                checkBorder();
            }
            this.mMatrix.getValues(this.mToMatrixValue);
            if (this.mOpenAnimator) {
                this.mRevertAnimator.setMatrixValue(this.mFromMatrixValue, this.mToMatrixValue);
                this.mRevertAnimator.cancel();
                this.mRevertAnimator.start();
                return;
            }
            applyMatrix();
        }
    }

    public void scaleSmale(int i) {
        float f;
        if (this.mLastScaleValue == i) {
            return;
        }
        this.mLastScaleValue = i;
        if (i == 1) {
            this.canTranslate = false;
            INIT_SCALE_FACTOR = 0.98f;
            initImgPositionAndSize();
            return;
        }
        if (i == 2) {
            this.canTranslate = true;
            INIT_SCALE_FACTOR = 0.98f;
            initImgPositionAndSize();
            f = 1.38f;
            this.mScaleFactorValue += 0.4f;
            initScals();
        } else if (i == 3) {
            this.canTranslate = true;
            INIT_SCALE_FACTOR = 0.98f;
            initImgPositionAndSize();
            f = 1.78f;
            this.mScaleFactorValue += 0.8f;
            initScals();
        } else {
            initImgPositionAndSize();
            return;
        }
        if (this.mScaleFactorValue == 0.98f) {
            initImgPositionAndSize();
            return;
        }
        this.mScaleFactor *= f;
        this.mMatrix.postScale(f, f, this.centerX, this.centerY);
        if (!getImageMatrix().equals(this.mMatrix)) {
            this.isTransforming = true;
        }
        if (this.mCanDrag || this.mCanScale || this.mCanRotate) {
            applyMatrix();
        }
        if (this.mOpenRotateRevert || this.mOpenScaleRevert || this.mOpenTranslateRevert) {
            this.mMatrix.getValues(this.mFromMatrixValue);
            if (this.mOpenRotateRevert) {
                checkRotation();
            }
            if (this.mOpenScaleRevert) {
                checkScale();
            }
            if (this.mOpenTranslateRevert) {
                checkBorder();
            }
            this.mMatrix.getValues(this.mToMatrixValue);
            if (this.mOpenAnimator) {
                this.mRevertAnimator.setMatrixValue(this.mFromMatrixValue, this.mToMatrixValue);
                this.mRevertAnimator.cancel();
                this.mRevertAnimator.start();
                return;
            }
            applyMatrix();
        }
    }

    public void setmMaxScaleFactor(float f) {
        this.mMaxScaleFactor = f;
    }

    public RectF getmImageRect() {
        return this.mImageRect;
    }

    public Matrix getmMatrix() {
        return this.mMatrix;
    }

    public void setmCanScale(boolean z) {
        this.mCanScale = z;
    }

    public void setmCanDrag(boolean z) {
        this.mCanDrag = z;
    }

    public void setmCanRotate(boolean z) {
        this.mCanRotate = z;
    }

    public float getmScaleFactor() {
        return this.mScaleFactor;
    }

    public float getmScaleFactorValue() {
        return this.mScaleFactorValue;
    }

    private boolean onDoubleClick() {
        onTouchCallBack ontouchcallback = this.onTouchCallBack;
        if (ontouchcallback != null) {
            ontouchcallback.onClickCallBack();
        }
        this.currentClick = System.currentTimeMillis();
        long j = this.currentClick;
        if (j - this.lastClick < this.clickIntervalTime) {
            this.canTranslate = false;
            this.lastClick = j;
            onTouchCallBack ontouchcallback2 = this.onTouchCallBack;
            if (ontouchcallback2 != null) {
                ontouchcallback2.onDoubleClickCallBack();
            }
            initImgPositionAndSize();
            this.mLastScaleValue = 1;
            return true;
        }
        this.lastClick = j;
        return false;
    }

    public void setOnTouchCallBack(onTouchCallBack ontouchcallback) {
        this.onTouchCallBack = ontouchcallback;
    }

    public void setMixScale(float f) {
        this.mMinScaleFactor = f;
    }

    public void setIsCanNarrow(boolean z) {
        this.isCanNarrow = z;
    }

    public void initLastScaleValue() {
        this.mLastScaleValue = 1;
    }

    public void setRation(boolean z) {
        this.isRation = z;
    }

    private void initScals() {
        INIT_SCALE_FACTOR = 1.2f;
        initImgPositionAndSize();
    }
}
