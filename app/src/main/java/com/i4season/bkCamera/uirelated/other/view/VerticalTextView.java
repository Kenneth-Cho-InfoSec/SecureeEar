package com.i4season.bkCamera.uirelated.other.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import androidx.core.view.GravityCompat;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.ChineseWordsWatch;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.LinearGradientTool;
import com.i4season.i4season_camera.C0413R;
import java.util.HashMap;

public class VerticalTextView extends View {
    private static final boolean DEBUG = true;
    private int mAngle;
    private float mChineseWordWdth;
    private Paint mDebugPaint;
    private String[] mDrawText;
    private Paint.FontMetrics mFontMetrics;
    private int mGravity;
    private boolean mIsHorizontal;
    private HashMap<Integer, Integer> mLengthRecord;
    private int[] mLinearGradientColor;
    private Paint mPaint;
    private float mSpacing;
    private String mText;
    private int mTextColor;
    private int mTextSize;
    private float mVerticalwordHeight;
    private int mViewHeight;
    private int mViewWidth;
    private float mWordHeight;

    private void drawDebugLine(Canvas canvas, float f, float f2, float f3) {
    }

    public VerticalTextView(Context context) {
        super(context);
        this.mIsHorizontal = false;
        this.mTextColor = -1;
        this.mGravity = GravityCompat.START;
        this.mChineseWordWdth = -1.0f;
        init();
    }

    public VerticalTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mIsHorizontal = false;
        this.mTextColor = -1;
        this.mGravity = GravityCompat.START;
        this.mChineseWordWdth = -1.0f;
        obtainAttrs(attributeSet, context);
        init();
    }

    public VerticalTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mIsHorizontal = false;
        this.mTextColor = -1;
        this.mGravity = GravityCompat.START;
        this.mChineseWordWdth = -1.0f;
        obtainAttrs(attributeSet, context);
        init();
    }

    public VerticalTextView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet);
        this.mIsHorizontal = false;
        this.mTextColor = -1;
        this.mGravity = GravityCompat.START;
        this.mChineseWordWdth = -1.0f;
        init();
    }

    private void obtainAttrs(AttributeSet attributeSet, Context context) {
        if (attributeSet == null) {
            return;
        }
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C0413R.styleable.VerticalTextView);
        this.mTextColor = typedArrayObtainStyledAttributes.getColor(0, -1);
        this.mTextSize = typedArrayObtainStyledAttributes.getDimensionPixelSize(1, 16);
        typedArrayObtainStyledAttributes.recycle();
    }

    public void setLinearGradientColor(int[] iArr, int i) {
        this.mLinearGradientColor = iArr;
        this.mAngle = i;
        update();
    }

    public void setText(String str) {
        this.mText = str;
        update();
    }

    public void setTextSize(int i) {
        this.mTextSize = (int) TypedValue.applyDimension(2, i, getResources().getDisplayMetrics());
        this.mPaint.setTextSize(this.mTextSize);
        update();
    }

    public void setGravity(int i) {
        this.mGravity = i;
        update();
    }

    public void setHorizontal(boolean z) {
        this.mIsHorizontal = z;
        update();
    }

    public boolean isHorizontal() {
        return this.mIsHorizontal;
    }

    public void setTextColor(int i) {
        this.mTextColor = i;
        init();
        update();
    }

    public void update() {
        requestLayout();
        invalidate();
    }

    private void init() {
        this.mPaint = new Paint();
        this.mPaint.setTextSize(this.mTextSize);
        this.mPaint.setColor(this.mTextColor);
    }

    @Override
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (!TextUtils.isEmpty(this.mText)) {
            this.mDrawText = this.mText.split("\n");
        }
        this.mFontMetrics = this.mPaint.getFontMetrics();
        this.mWordHeight = this.mFontMetrics.descent - this.mFontMetrics.ascent;
        this.mVerticalwordHeight = this.mFontMetrics.descent - this.mFontMetrics.ascent;
        this.mSpacing = 0.0f;
        if (this.mIsHorizontal) {
            measureHorizontal(i, i2);
        } else {
            measureVertical(i, i2);
        }
        setMeasuredDimension(this.mViewWidth, this.mViewHeight);
    }

    public void measureHorizontal(int i, int i2) {
        int mode = View.MeasureSpec.getMode(i);
        int size = View.MeasureSpec.getSize(i);
        int mode2 = View.MeasureSpec.getMode(i2);
        int size2 = View.MeasureSpec.getSize(i2);
        this.mViewHeight = size2;
        this.mViewWidth = size;
        String[] strArr = this.mDrawText;
        int iMax = strArr != null ? Math.max(1, strArr.length) : 1;
        if (mode == Integer.MIN_VALUE) {
            this.mViewWidth = 0;
            float f = 0.0f;
            if (!TextUtils.isEmpty(this.mText)) {
                for (String str : this.mDrawText) {
                    float fMeasureText = this.mPaint.measureText(str);
                    if (f < fMeasureText) {
                        f = fMeasureText;
                    }
                }
                f += this.mSpacing * 2.0f;
            }
            this.mViewWidth = (int) (this.mViewWidth + f);
        }
        if (mode2 == Integer.MIN_VALUE) {
            float f2 = this.mWordHeight;
            float f3 = this.mSpacing;
            this.mViewHeight = (int) (((f2 + f3) * iMax) + f3);
        }
        if (size < this.mViewWidth) {
            this.mViewWidth = size;
        }
        if (size2 < this.mViewHeight) {
            this.mViewHeight = size2;
        }
    }

    public void measureVertical(int i, int i2) {
        int i3;
        float f;
        float fMeasureText;
        int mode = View.MeasureSpec.getMode(i);
        int size = View.MeasureSpec.getSize(i);
        int mode2 = View.MeasureSpec.getMode(i2);
        int size2 = View.MeasureSpec.getSize(i2);
        this.mViewHeight = size2;
        this.mViewWidth = size;
        if (this.mLengthRecord == null) {
            this.mLengthRecord = new HashMap<>();
        }
        if (mode == Integer.MIN_VALUE) {
            this.mViewWidth = 0;
            String[] strArr = this.mDrawText;
            this.mViewWidth = (int) ((this.mWordHeight * ((strArr == null || strArr.length <= 0) ? 1 : strArr.length)) + this.mSpacing);
        }
        this.mChineseWordWdth = -1.0f;
        this.mViewHeight = 0;
        String[] strArr2 = this.mDrawText;
        if (strArr2 == null || strArr2.length <= 0) {
            i3 = 0;
        } else {
            ChineseWordsWatch chineseWordsWatch = new ChineseWordsWatch();
            i3 = 0;
            for (int length = this.mDrawText.length - 1; length >= 0; length--) {
                chineseWordsWatch.setString(this.mDrawText[length]);
                int i4 = 0;
                while (true) {
                    String strNextString = chineseWordsWatch.nextString();
                    if (strNextString == null) {
                        break;
                    }
                    if (chineseWordsWatch.isChineseWord()) {
                        if (this.mChineseWordWdth == -1.0f) {
                            this.mChineseWordWdth = this.mPaint.measureText(strNextString);
                        }
                        f = i4;
                        fMeasureText = this.mVerticalwordHeight;
                    } else {
                        f = i4;
                        fMeasureText = this.mPaint.measureText(strNextString);
                    }
                    i4 = (int) (f + fMeasureText);
                }
                this.mLengthRecord.put(Integer.valueOf(length), Integer.valueOf(i4));
                if (i3 < i4) {
                    i3 = i4;
                }
            }
        }
        float f2 = this.mChineseWordWdth;
        if (f2 != -1.0f) {
            this.mViewWidth = (int) (this.mViewWidth - (this.mWordHeight - f2));
        }
        if (mode2 == Integer.MIN_VALUE) {
            this.mViewHeight = (int) (i3 + (this.mSpacing * 2.0f));
        }
        if (size < this.mViewWidth) {
            this.mViewWidth = size;
        }
        if (size2 < this.mViewHeight) {
            this.mViewHeight = size2;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (TextUtils.isEmpty(this.mText)) {
            return;
        }
        if (this.mIsHorizontal) {
            drawHorizontal(canvas);
        } else {
            drawVertical(canvas);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0058  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void drawVertical(Canvas canvas) {
        int iMeasureText;
        float f;
        float f2 = this.mSpacing;
        int i = (int) f2;
        int i2 = (int) f2;
        ChineseWordsWatch chineseWordsWatch = new ChineseWordsWatch();
        for (int length = this.mDrawText.length - 1; length >= 0; length--) {
            chineseWordsWatch.setString(this.mDrawText[length]);
            HashMap<Integer, Integer> map = this.mLengthRecord;
            if (map == null || !map.containsKey(Integer.valueOf(length))) {
                iMeasureText = i2;
            } else {
                int iIntValue = this.mLengthRecord.get(Integer.valueOf(length)).intValue();
                int i3 = this.mGravity;
                if (i3 == 17) {
                    iMeasureText = (this.mViewHeight - iIntValue) >> 1;
                } else if (i3 == 8388613) {
                    iMeasureText = (int) ((this.mViewHeight - (this.mSpacing * 2.0f)) - iIntValue);
                }
            }
            int[] iArr = this.mLinearGradientColor;
            if (iArr != null) {
                LinearGradientTool.getLinearGradient(0.0f, 0.0f, this.mViewWidth, this.mViewHeight, iArr, this.mAngle);
            }
            while (true) {
                String strNextString = chineseWordsWatch.nextString();
                if (strNextString != null) {
                    float f3 = this.mChineseWordWdth;
                    float f4 = f3 != -1.0f ? (float) (((double) (this.mVerticalwordHeight - f3)) * 0.5d) : 0.0f;
                    canvas.save();
                    float f5 = i;
                    float f6 = iMeasureText;
                    canvas.rotate(90.0f, f5, f6);
                    drawDebugLine(canvas, f5, (f6 - this.mFontMetrics.descent) + f4, (int) this.mPaint.measureText(strNextString));
                    int[] iArr2 = this.mLinearGradientColor;
                    if (iArr2 != null) {
                        f = f5;
                        this.mPaint.setShader(LinearGradientTool.getRotate90LinearGradient(f5, f6, 0.0f, 0.0f, this.mViewWidth, this.mViewHeight, iArr2, this.mAngle));
                    } else {
                        f = f5;
                    }
                    canvas.drawText(strNextString, f, (f6 - this.mFontMetrics.descent) + f4, this.mPaint);
                    canvas.restore();
                    iMeasureText = (int) (f6 + this.mPaint.measureText(strNextString));
                }
            }
            i = (int) (i + this.mWordHeight);
        }
    }

    private void drawHorizontal(Canvas canvas) {
        int i;
        int i2 = (int) (this.mSpacing - this.mFontMetrics.ascent);
        int[] iArr = this.mLinearGradientColor;
        if (iArr != null) {
            this.mPaint.setShader(LinearGradientTool.getLinearGradient(0.0f, 0.0f, this.mViewWidth, this.mViewHeight, iArr, this.mAngle));
        }
        for (String str : this.mDrawText) {
            float fMeasureText = this.mPaint.measureText(str);
            int i3 = this.mGravity;
            if (i3 == 8388611) {
                i = (int) this.mSpacing;
            } else if (i3 == 8388613) {
                i = (int) ((this.mViewWidth - this.mSpacing) - fMeasureText);
            } else {
                i = ((int) (this.mViewWidth - fMeasureText)) >> 1;
            }
            float f = i;
            float f2 = i2;
            canvas.drawText(str, f, f2, this.mPaint);
            drawDebugLine(canvas, f, f2, (int) this.mPaint.measureText(str));
            i2 = (int) (f2 + this.mWordHeight + this.mSpacing);
        }
    }
}
