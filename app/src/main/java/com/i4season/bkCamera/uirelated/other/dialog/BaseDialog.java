package com.i4season.bkCamera.uirelated.other.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import com.i4season.i4season_camera.C0413R;

public class BaseDialog extends Dialog {
    protected Context mContext;

    public BaseDialog(Context context) {
        super(context, C0413R.style.wdDialog);
        this.mContext = context;
    }

    @Override
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        Window window = getWindow();
        if (!z || window == null) {
            return;
        }
        View decorView = window.getDecorView();
        if (decorView.getHeight() == 0 || decorView.getWidth() == 0) {
            decorView.requestLayout();
        }
    }
}
