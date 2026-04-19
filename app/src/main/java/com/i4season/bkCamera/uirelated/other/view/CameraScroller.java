package com.i4season.bkCamera.uirelated.other.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;
import android.widget.TextView;
import com.i4season.bkCamera.uirelated.other.FunctionSwitch;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.UtilTools;
import com.i4season.i4season_camera.C0413R;

public class CameraScroller extends ViewGroup {
    public int duration;
    public int leftIndex;
    public Scroller mScroller;
    public int rightIndex;

    public CameraScroller(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.duration = 300;
        this.mScroller = new Scroller(context);
    }

    public final void scrollToNext(int i, int i2) {
        TextView textView = (TextView) getChildAt(i);
        if (textView != null) {
            if (FunctionSwitch.IS_FBPRO) {
                textView.setTextColor(getResources().getColor(C0413R.color.appwhite_transfer60));
            } else {
                textView.setTextColor(getResources().getColor(C0413R.color.apptextblack3));
            }
        }
        TextView textView2 = (TextView) getChildAt(i2);
        if (textView2 != null) {
            if (FunctionSwitch.IS_FBPRO) {
                textView2.setTextColor(getResources().getColor(C0413R.color.appwhite));
            } else {
                textView2.setTextColor(getResources().getColor(C0413R.color.appblack));
            }
        }
    }

    @Override
    public void computeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            scrollTo(this.mScroller.getCurrX(), this.mScroller.getCurrY());
            invalidate();
        }
        super.computeScroll();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int width;
        int measuredWidth;
        int childCount = getChildCount();
        int currentSelectedIndex = UtilTools.getCurrentSelectedIndex();
        int measuredWidth2 = 0;
        for (int i5 = 0; i5 < childCount; i5++) {
            View childAt = getChildAt(i5);
            if (i5 < currentSelectedIndex) {
                measuredWidth2 += childAt.getMeasuredWidth();
            }
        }
        for (int i6 = 0; i6 < childCount; i6++) {
            View childAt2 = getChildAt(i6);
            if (i6 != 0) {
                width = getChildAt(i6 - 1).getRight();
                measuredWidth = childAt2.getMeasuredWidth();
            } else {
                width = ((getWidth() - getChildAt(currentSelectedIndex).getMeasuredWidth()) / 2) - measuredWidth2;
                measuredWidth = childAt2.getMeasuredWidth();
            }
            childAt2.layout(width, i2, measuredWidth + width, i4);
        }
        TextView textView = (TextView) getChildAt(currentSelectedIndex);
        if (FunctionSwitch.IS_FBPRO) {
            textView.setTextColor(getResources().getColor(C0413R.color.appwhite));
        } else {
            textView.setTextColor(getResources().getColor(C0413R.color.appblack));
        }
    }

    @Override
    protected void onMeasure(int i, int i2) {
        measureChildren(i, i2);
        setMeasuredDimension(View.MeasureSpec.getSize(i), View.MeasureSpec.getSize(i2));
    }
}
