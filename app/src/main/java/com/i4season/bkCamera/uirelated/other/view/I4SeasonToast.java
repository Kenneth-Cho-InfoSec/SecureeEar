package com.i4season.bkCamera.uirelated.other.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import com.i4season.i4season_camera.C0413R;

public class I4SeasonToast {
    private ImageView icon;
    private Toast mToast;

    private I4SeasonToast(Context context, int i, int i2) {
        View viewInflate = LayoutInflater.from(context).inflate(C0413R.layout.i4season_toast, (ViewGroup) null);
        this.icon = (ImageView) viewInflate.findViewById(C0413R.id.toast_icon);
        this.icon.setImageResource(i);
        this.mToast = new Toast(context);
        this.mToast.setDuration(i2);
        this.mToast.setView(viewInflate);
    }

    public static I4SeasonToast makeText(Context context, int i, int i2) {
        return new I4SeasonToast(context, i, i2);
    }

    public void show() {
        Toast toast = this.mToast;
        if (toast != null) {
            toast.show();
        }
    }

    public void setGravity(int i, int i2, int i3) {
        Toast toast = this.mToast;
        if (toast != null) {
            toast.setGravity(i, i2, i3);
        }
    }

    public void setImagesrc(int i) {
        ImageView imageView = this.icon;
        if (imageView != null) {
            imageView.setImageResource(i);
        }
    }
}
