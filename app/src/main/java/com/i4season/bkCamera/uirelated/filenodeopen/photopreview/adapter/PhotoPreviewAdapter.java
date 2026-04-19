package com.i4season.bkCamera.uirelated.filenodeopen.photopreview.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.viewpager.widget.PagerAdapter;
import com.bumptech.glide.Glide;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.bean.FileNode;
import com.i4season.bkCamera.uirelated.other.view.TransformativeImageView2;
import com.i4season.i4season_camera.C0413R;
import java.util.List;

public class PhotoPreviewAdapter extends PagerAdapter {
    private List<FileNode> fileNodeArray;
    private boolean isRotoin;
    private onPhotoViewClickListener listener;
    private Context mContext;

    public interface onPhotoViewClickListener {
        void onPhotoViewClick(int i);
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    public PhotoPreviewAdapter(Context context, List<FileNode> list, boolean z) {
        this.isRotoin = false;
        this.mContext = context;
        this.fileNodeArray = list;
        this.isRotoin = z;
    }

    @Override
    public int getCount() {
        List<FileNode> list = this.fileNodeArray;
        if (list == null || list.size() == 0) {
            return this.isRotoin ? 1 : 0;
        }
        return this.fileNodeArray.size();
    }

    @Override
    public View instantiateItem(ViewGroup viewGroup, final int i) {
        View viewInflate = View.inflate(this.mContext, C0413R.layout.photo_preview_detail_item, null);
        List<FileNode> list = this.fileNodeArray;
        if ((list == null || list.size() == 0) && this.isRotoin) {
            ((ImageView) viewInflate.findViewById(C0413R.id.photo_preview_img)).setImageResource(C0413R.drawable.ic_album_empty);
        } else {
            TransformativeImageView2 transformativeImageView2 = (TransformativeImageView2) viewInflate.findViewById(C0413R.id.photo_preview);
            setThumb(this.fileNodeArray.get(i), transformativeImageView2, (TransformativeImageView2) viewInflate.findViewById(C0413R.id.photo_preview2));
            transformativeImageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (PhotoPreviewAdapter.this.listener != null) {
                        PhotoPreviewAdapter.this.listener.onPhotoViewClick(i);
                    }
                }
            });
        }
        viewGroup.addView(viewInflate);
        return viewInflate;
    }

    private void setThumb(FileNode fileNode, ImageView imageView, TransformativeImageView2 transformativeImageView2) {
        if (this.isRotoin) {
            transformativeImageView2.setVisibility(0);
            imageView.setVisibility(8);
            Glide.with(this.mContext).load(fileNode.getmFilePath()).into(transformativeImageView2);
        } else {
            transformativeImageView2.setVisibility(8);
            imageView.setVisibility(0);
            Glide.with(this.mContext).load(fileNode.getmFilePath()).into(imageView);
        }
    }

    @Override
    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        viewGroup.removeView((View) obj);
    }

    public void setOnPhotoViewClickListener(onPhotoViewClickListener onphotoviewclicklistener) {
        this.listener = onphotoviewclicklistener;
    }

    public List<FileNode> getFileNodeArray() {
        return this.fileNodeArray;
    }

    public void setFileNodeArray(List<FileNode> list) {
        this.fileNodeArray = list;
    }
}
