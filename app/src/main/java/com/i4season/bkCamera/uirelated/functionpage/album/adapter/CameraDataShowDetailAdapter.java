package com.i4season.bkCamera.uirelated.functionpage.album.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.bean.FileNode;
import com.i4season.bkCamera.uirelated.other.AppPathInfo;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.FileUtil;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.UtilTools;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.VideoUtils;
import com.i4season.bkCamera.uirelated.other.view.stickygridheaders.StickyGridHeadersSimpleAdapter;
import com.i4season.i4season_camera.C0413R;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class CameraDataShowDetailAdapter extends BaseAdapter implements StickyGridHeadersSimpleAdapter {
    public static final int GET_VIDEO_TIME_LENGTH = 0;
    public static final int REFLASH_ADAPTER = 1;
    private boolean isEditMode;
    private Context mContext;
    private GridView mGridView;
    private Handler mHandler;
    private LayoutInflater mInflater;
    private LinkedHashMap<String, LinkedList<FileNode>> stringLinkedListLinkedHashMap = new LinkedHashMap<>();
    private List<FileNode> sortFileList = new ArrayList();
    private Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                ((TextView) message.obj).setText(((FileNode) CameraDataShowDetailAdapter.this.sortFileList.get(message.arg1)).getTimgLength());
            } else {
                if (i != 1) {
                    return;
                }
                CameraDataShowDetailAdapter.this.notifyDataSetChanged();
            }
        }
    };

    @Override
    public long getItemId(int i) {
        return i;
    }

    public CameraDataShowDetailAdapter(Context context, List<FileNode> list, GridView gridView, Handler handler, LinkedHashMap<String, LinkedList<FileNode>> linkedHashMap, boolean z) {
        this.sortFileList.clear();
        this.sortFileList.addAll(list);
        this.mContext = context;
        this.mHandler = handler;
        this.mInflater = LayoutInflater.from(context);
        this.mGridView = gridView;
        this.isEditMode = z;
        this.stringLinkedListLinkedHashMap.clear();
        this.stringLinkedListLinkedHashMap.putAll(linkedHashMap);
    }

    @Override
    public int getCount() {
        List<FileNode> list = this.sortFileList;
        if (list == null || list.size() == 0) {
            return 0;
        }
        return this.sortFileList.size();
    }

    @Override
    public Object getItem(int i) {
        return this.sortFileList.get(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = View.inflate(this.mContext, C0413R.layout.camera_datafile_chirld_item, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        FileNode fileNode = this.sortFileList.get(i);
        if (fileNode.getmFileTypeMarked() == 2) {
            viewHolder.videoTime.setVisibility(0);
            if (TextUtils.isEmpty(fileNode.getTimgLength())) {
                getVideoTime(viewHolder.videoTime, i, viewHolder.videoIcon);
            } else {
                viewHolder.videoTime.setText(fileNode.getTimgLength());
            }
        } else {
            viewHolder.videoTime.setVisibility(8);
        }
        setThumb(fileNode, viewHolder.thumbImage);
        if (!this.isEditMode) {
            viewHolder.maskImage.setVisibility(8);
            viewHolder.selectImage.setVisibility(8);
        } else if (fileNode.ismFileIsSelected()) {
            viewHolder.selectImage.setImageResource(C0413R.drawable.ic_file_select);
            viewHolder.selectImage.setVisibility(0);
            viewHolder.maskImage.setVisibility(0);
        } else {
            viewHolder.selectImage.setImageResource(C0413R.drawable.ic_file_unselect);
            viewHolder.selectImage.setVisibility(0);
            viewHolder.maskImage.setVisibility(8);
        }
        return view;
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.i4season.bkCamera.uirelated.functionpage.album.adapter.CameraDataShowDetailAdapter$2] */
    private void getVideoTime(final TextView textView, final int i, final ImageView imageView) {
        new Thread() {
            @Override
            public void run() {
                final FileNode fileNode = (FileNode) CameraDataShowDetailAdapter.this.sortFileList.get(i);
                int videoTime = FileUtil.getVideoTime(fileNode.getmFileDevPath());
                if (videoTime <= 0) {
                    fileNode.setTimgLength("00:00:00");
                } else {
                    fileNode.setTimgLength(VideoUtils.timeToStr(videoTime));
                }
                ((Activity) CameraDataShowDetailAdapter.this.mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(fileNode.getTimgLength());
                        imageView.setVisibility(0);
                    }
                });
            }
        }.start();
    }

    private void setThumb(FileNode fileNode, ImageView imageView) {
        boolean zContains = fileNode.getmFileDevPath().contains(".avi");
        String str = fileNode.getmFileDevPath();
        if (zContains) {
            str = AppPathInfo.getSaveCameraDataPath2AndroidO() + "/.Thumbs/" + UtilTools.getPrefixFromName(fileNode.getmFileName()) + ".jpg";
        }
        Glide.with(this.mContext).load(str).into(imageView);
    }

    @Override
    public View getHeaderView(int i, View view, ViewGroup viewGroup) {
        View viewInflate;
        HeaderViewHolder headerViewHolder;
        if (view == null) {
            headerViewHolder = new HeaderViewHolder();
            viewInflate = this.mInflater.inflate(C0413R.layout.camera_datafile_header_item, viewGroup, false);
            headerViewHolder.mTextView = (TextView) viewInflate.findViewById(C0413R.id.tv_header);
            headerViewHolder.mImageView = (ImageView) viewInflate.findViewById(C0413R.id.group_all_select);
            viewInflate.setTag(headerViewHolder);
        } else {
            viewInflate = view;
            headerViewHolder = (HeaderViewHolder) view.getTag();
        }
        if (i > this.sortFileList.size()) {
            return null;
        }
        String timtTag = this.sortFileList.get(i).getTimtTag();
        headerViewHolder.mTextView.setText(timtTag);
        this.stringLinkedListLinkedHashMap.get(timtTag);
        return viewInflate;
    }

    private class ViewHolder {
        ImageView maskImage;
        ImageView selectImage;
        ImageView thumbImage;
        ImageView videoIcon;
        TextView videoTime;

        public ViewHolder(View view) {
            this.thumbImage = (ImageView) view.findViewById(C0413R.id.camera_data_thumb);
            this.maskImage = (ImageView) view.findViewById(C0413R.id.camera_data_mask);
            this.selectImage = (ImageView) view.findViewById(C0413R.id.camera_data_select);
            this.videoTime = (TextView) view.findViewById(C0413R.id.camera_video_data_time);
            this.videoIcon = (ImageView) view.findViewById(C0413R.id.camera_video_icon);
        }
    }

    public class HeaderViewHolder {
        public ImageView mImageView;
        public TextView mTextView;

        public HeaderViewHolder() {
        }
    }

    @Override
    public long getHeaderId(int i) {
        return this.sortFileList.get(i).getHeadId();
    }

    private void selectAll(HeaderViewHolder headerViewHolder, final LinkedList<FileNode> linkedList) {
        if (linkedList == null) {
            return;
        }
        boolean z = true;
        Iterator<FileNode> it = linkedList.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            } else if (!it.next().ismFileIsSelected()) {
                z = false;
                break;
            }
        }
        if (z) {
            headerViewHolder.mImageView.setImageResource(C0413R.drawable.ic_file_select);
        } else {
            headerViewHolder.mImageView.setImageResource(C0413R.drawable.ic_file_unselect);
        }
        headerViewHolder.mImageView.setVisibility(0);
        headerViewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraDataShowDetailAdapter.this.groupSelectAll(linkedList);
            }
        });
    }

    public void groupSelectAll(final LinkedList<FileNode> linkedList) {
        new Thread() {
            @Override
            public void run() {
                boolean z;
                Iterator it = linkedList.iterator();
                while (true) {
                    if (it.hasNext()) {
                        if (!((FileNode) it.next()).ismFileIsSelected()) {
                            z = false;
                            break;
                        }
                    } else {
                        z = true;
                        break;
                    }
                }
                for (FileNode fileNode : linkedList) {
                    if (z) {
                        fileNode.setmFileIsSelected(false);
                    } else {
                        fileNode.setmFileIsSelected(true);
                    }
                }
                CameraDataShowDetailAdapter.this.mHandler.sendEmptyMessage(102);
                CameraDataShowDetailAdapter.this.handler.sendEmptyMessage(1);
            }
        }.start();
    }

    public void setSortFileList(List<FileNode> list) {
        this.sortFileList = list;
    }

    public List<FileNode> getSortFileList() {
        return this.sortFileList;
    }

    public void setStringLinkedListLinkedHashMap(LinkedHashMap<String, LinkedList<FileNode>> linkedHashMap) {
        this.stringLinkedListLinkedHashMap.clear();
        this.stringLinkedListLinkedHashMap.putAll(linkedHashMap);
    }

    public void setIsEditMode(boolean z) {
        this.isEditMode = z;
    }

    public boolean isEditMode() {
        return this.isEditMode;
    }
}
