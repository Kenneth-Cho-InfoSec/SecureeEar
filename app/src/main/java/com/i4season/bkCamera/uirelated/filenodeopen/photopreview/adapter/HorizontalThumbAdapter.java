package com.i4season.bkCamera.uirelated.filenodeopen.photopreview.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.bean.FileNode;
import com.i4season.bkCamera.uirelated.filenodeopen.photopreview.view.ThumbViewHolder;
import com.i4season.i4season_camera.C0413R;
import java.util.LinkedList;

public class HorizontalThumbAdapter extends RecyclerView.Adapter<ThumbViewHolder> {
    private LinkedList<FileNode> fileNodeArray;
    private onThumbViewClickListener listener;
    private Context mContext;
    private int mCurrPosition;

    public interface onThumbViewClickListener {
        void onThumbViewClick(int i);
    }

    public HorizontalThumbAdapter(Context context, LinkedList<FileNode> linkedList, int i) {
        this.mContext = context;
        this.fileNodeArray = linkedList;
        this.mCurrPosition = i;
    }

    @Override
    public ThumbViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ThumbViewHolder(View.inflate(this.mContext, C0413R.layout.photo_preview_thumb_item, null));
    }

    @Override
    public void onBindViewHolder(ThumbViewHolder thumbViewHolder, final int i) {
        FileNode fileNode = this.fileNodeArray.get(i);
        if (this.mCurrPosition == i) {
            thumbViewHolder.ivBig.setVisibility(0);
            thumbViewHolder.f64iv.setVisibility(8);
            setThumb(fileNode, thumbViewHolder.ivBig);
            setThumb(fileNode, thumbViewHolder.f64iv);
        } else {
            thumbViewHolder.ivBig.setVisibility(8);
            thumbViewHolder.f64iv.setVisibility(0);
            setThumb(fileNode, thumbViewHolder.f64iv);
            setThumb(fileNode, thumbViewHolder.ivBig);
        }
        thumbViewHolder.f64iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HorizontalThumbAdapter.this.listener.onThumbViewClick(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        LinkedList<FileNode> linkedList = this.fileNodeArray;
        if (linkedList == null || linkedList.size() == 0) {
            return 0;
        }
        return this.fileNodeArray.size();
    }

    private void setThumb(FileNode fileNode, ImageView imageView) {
        Glide.with(this.mContext).load(fileNode.getmFileDevPath()).into(imageView);
    }

    public void setOnThumbViewClickListener(onThumbViewClickListener onthumbviewclicklistener) {
        this.listener = onthumbviewclicklistener;
    }

    public int getmCurrPosition() {
        return this.mCurrPosition;
    }

    public void setmCurrPosition(int i) {
        this.mCurrPosition = i;
    }

    public LinkedList<FileNode> getFileNodeArray() {
        return this.fileNodeArray;
    }

    public void setFileNodeArray(LinkedList<FileNode> linkedList) {
        this.fileNodeArray = linkedList;
    }
}
