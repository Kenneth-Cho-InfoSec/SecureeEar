package com.i4season.bkCamera.uirelated.filenodeopen.photopreview.view;

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

public class ScrollSpeedLinearLayoutManager extends LinearLayoutManager {
    private float MILLISECONDS_PER_INCH;
    private Context contxt;

    public ScrollSpeedLinearLayoutManager(Context context) {
        super(context);
        this.MILLISECONDS_PER_INCH = 0.03f;
        this.contxt = context;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int i) {
        LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {
            @Override
            public PointF computeScrollVectorForPosition(int i2) {
                return ScrollSpeedLinearLayoutManager.this.computeScrollVectorForPosition(i2);
            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return ScrollSpeedLinearLayoutManager.this.MILLISECONDS_PER_INCH / displayMetrics.density;
            }
        };
        linearSmoothScroller.setTargetPosition(i);
        startSmoothScroll(linearSmoothScroller);
    }

    public void setSpeedSlow() {
        this.MILLISECONDS_PER_INCH = this.contxt.getResources().getDisplayMetrics().density * 0.3f;
    }

    public void setSpeedFast() {
        this.MILLISECONDS_PER_INCH = this.contxt.getResources().getDisplayMetrics().density * 0.03f;
    }
}
