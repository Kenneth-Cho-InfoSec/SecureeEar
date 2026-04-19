package com.i4season.bkCamera.uirelated.filenodeopen.photopreview.view;

import android.view.View;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.i4season.i4season_camera.C0413R;

public class ThumbViewHolder extends RecyclerView.ViewHolder {

    public ImageView f64iv;
    public ImageView ivBig;

    public ThumbViewHolder(View view) {
        super(view);
        this.f64iv = (ImageView) view.findViewById(C0413R.id.photo_preview_thumb);
        this.ivBig = (ImageView) view.findViewById(C0413R.id.photo_preview_thumb_big);
    }
}
