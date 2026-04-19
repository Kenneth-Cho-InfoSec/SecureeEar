package com.i4season.bkCamera.uirelated.filenodeopen.photopreview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.viewpager.widget.ViewPager;

public class I4seasonViewPager extends ViewPager {
    public I4seasonViewPager(Context context) {
        super(context);
    }

    public I4seasonViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        try {
            return super.onInterceptTouchEvent(motionEvent);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
            return false;
        }
    }
}
