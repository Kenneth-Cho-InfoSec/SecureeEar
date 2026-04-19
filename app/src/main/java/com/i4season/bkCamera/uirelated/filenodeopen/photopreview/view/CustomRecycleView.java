package com.i4season.bkCamera.uirelated.filenodeopen.photopreview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Scroller;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CustomRecycleView extends RecyclerView {
    private static final String TAG = CustomRecycleView.class.getSimpleName();
    private int mLastx;
    private float mPxPerMillsec;
    private Scroller mScroller;
    private int mTargetPos;

    public CustomRecycleView(Context context) {
        super(context);
        this.mLastx = 0;
        this.mPxPerMillsec = 0.0f;
        init(context);
    }

    public CustomRecycleView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mLastx = 0;
        this.mPxPerMillsec = 0.0f;
        init(context);
    }

    public CustomRecycleView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mLastx = 0;
        this.mPxPerMillsec = 0.0f;
        init(context);
    }

    private void init(Context context) {
        this.mScroller = new Scroller(context);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        Scroller scroller = this.mScroller;
        if (scroller == null || !scroller.computeScrollOffset()) {
            return;
        }
        scrollBy(this.mLastx - this.mScroller.getCurrX(), 0);
        this.mLastx = this.mScroller.getCurrX();
        postInvalidate();
    }

    public void smoothScrollTo(int i, int i2, int i3) {
        smoothScrollBy(i != 0 ? i - this.mScroller.getFinalX() : 0, i2 != 0 ? i2 - this.mScroller.getFinalY() : 0, i3);
    }

    public void smoothScrollBy(int i, int i2, int i3) {
        if (i3 > 0) {
            Scroller scroller = this.mScroller;
            scroller.startScroll(scroller.getFinalX(), this.mScroller.getFinalY(), i, i2, i3);
        } else {
            Scroller scroller2 = this.mScroller;
            scroller2.startScroll(scroller2.getFinalX(), this.mScroller.getFinalY(), i, i2);
        }
        invalidate();
    }

    public void checkAutoAdjust(int i) {
        getChildCount();
        int iFindFirstVisibleItemPosition = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        int iFindLastVisibleItemPosition = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
        if (i == iFindFirstVisibleItemPosition + 1 || i == iFindFirstVisibleItemPosition) {
            leftScrollBy(i, iFindFirstVisibleItemPosition);
        } else if (i == iFindLastVisibleItemPosition - 1 || i == iFindLastVisibleItemPosition) {
            rightScrollBy(i, iFindLastVisibleItemPosition);
        }
    }

    private void leftScrollBy(int i, int i2) {
        View childAt = getChildAt(0);
        if (childAt != null) {
            autoAdjustScroll(childAt.getLeft(), i == i2 ? childAt.getWidth() : 0);
        }
    }

    private void rightScrollBy(int i, int i2) {
        View childAt = getChildAt(getChildCount() - 1);
        if (childAt != null) {
            autoAdjustScroll(childAt.getRight() - getWidth(), i == i2 ? childAt.getWidth() * (-1) : 0);
        }
    }

    private void autoAdjustScroll(int i, int i2) {
        int iAbs = this.mPxPerMillsec != 0.0f ? (int) (Math.abs(i2 - i) / this.mPxPerMillsec) : 0;
        this.mLastx = i;
        if (iAbs > 0) {
            this.mScroller.startScroll(i, 0, i2 - i, 0, iAbs);
        } else {
            this.mScroller.startScroll(i, 0, i2 - i, 0);
        }
        postInvalidate();
    }

    public void smoothToCenter(int i) {
        int width = getWidth();
        getChildCount();
        int iFindFirstVisibleItemPosition = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        this.mTargetPos = Math.max(0, Math.min(((LinearLayoutManager) getLayoutManager()).getItemCount() - 1, i));
        View childAt = getChildAt(this.mTargetPos - iFindFirstVisibleItemPosition);
        if (childAt == null) {
            return;
        }
        int left = childAt.getLeft();
        int right = childAt.getRight();
        int i2 = width / 2;
        int width2 = childAt.getWidth() / 2;
        int i3 = i2 - width2;
        int i4 = i2 + width2;
        if (left > i3) {
            this.mLastx = left;
            this.mScroller.startScroll(left, 0, i3 - left, 0, 600);
            postInvalidate();
        } else if (right < i4) {
            this.mLastx = right;
            this.mScroller.startScroll(right, 0, i4 - right, 0, 600);
            postInvalidate();
        }
    }
}
