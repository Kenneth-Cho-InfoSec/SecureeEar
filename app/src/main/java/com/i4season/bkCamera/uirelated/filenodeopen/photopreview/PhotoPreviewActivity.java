package com.i4season.bkCamera.uirelated.filenodeopen.photopreview;

import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.InputDeviceCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.i4season.bkCamera.logicrelated.camera.CameraEventObserver;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.bean.FileNode;
import com.i4season.bkCamera.uirelated.filenodeopen.FileNodeDeleteHandler;
import com.i4season.bkCamera.uirelated.filenodeopen.FileNodeOpenInstance;
import com.i4season.bkCamera.uirelated.filenodeopen.photopreview.adapter.HorizontalThumbAdapter;
import com.i4season.bkCamera.uirelated.filenodeopen.photopreview.adapter.PhotoPreviewAdapter;
import com.i4season.bkCamera.uirelated.filenodeopen.photopreview.view.CustomRecycleView;
import com.i4season.bkCamera.uirelated.filenodeopen.photopreview.view.I4seasonViewPager;
import com.i4season.bkCamera.uirelated.filenodeopen.photopreview.view.ScrollSpeedLinearLayoutManager;
import com.i4season.bkCamera.uirelated.other.MainFrameHandleInstance;
import com.i4season.bkCamera.uirelated.other.dialog.GeneralDialog;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.AppSrceenInfo;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.Constant;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.FileUtil;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.NotifyCode;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.SpUtils;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.SystemUtil;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.UtilTools;
import com.i4season.bkCamera.uirelated.other.logmanage.LogWD;
import com.i4season.bkCamera.uirelated.other.view.TransformativeImageView;
import com.i4season.i4season_camera.C0413R;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class PhotoPreviewActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, PhotoPreviewAdapter.onPhotoViewClickListener, HorizontalThumbAdapter.onThumbViewClickListener, CameraEventObserver.OnTakePhotoOrRecoderListener, FileNodeDeleteHandler.IDeleteResultDelegate {
    public static final int CAMERA_DATA_ACCEPT = 120;
    private static final int DELETE_FILE_FINISH = 1;
    private static final int HIDE_BOTTOM_KEYBAR = 0;
    private static final int REQUEST_PERMISSION_CODE = 1001;
    public static final int TAKE_PHOTO_BACK = 121;
    private HorizontalThumbAdapter horizontalThumbAdapter;
    int lastFirstVisibleItemPosition;
    private ScrollSpeedLinearLayoutManager linearLayoutManager;
    private Bitmap mBitmap;
    private RelativeLayout mBottomBar;
    private LinearLayout mBottomCompare;
    private LinearLayout mBottomDelete;
    private ImageView mBottomDeleteImage;
    private TextView mBottomDeleteText;
    private RelativeLayout mBottomLayout;
    private LinearLayout mBottomShare;
    private ImageView mBottomShareImage;
    private TextView mBottomShareText;
    private TransformativeImageView mCameraShowView;
    private ImageView mCameraTake;
    private ImageView mCampareCancel;
    private RelativeLayout mCompareShowRl;
    private int mCurrPosition;
    private CustomRecycleView mCustomRecycleView;
    private FileNodeDeleteHandler mFileNodeDeleteHandler;
    private ImageView mTopBack;
    private RelativeLayout mTopBar;
    private TextView mTopFileName;
    private I4seasonViewPager mViewPage;
    private FileNode needDeleteFileNode;
    private PhotoPreviewAdapter photoPreviewAdapter;
    private SpUtils spUtils;
    private LinkedList<FileNode> fileArray = new LinkedList<>();
    private boolean mIsCompare = false;
    private boolean isHideBar = false;
    private Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                PhotoPreviewActivity.this.hideBottomUIMenu();
                return;
            }
            if (i == 1) {
                UtilTools.showResultToast(PhotoPreviewActivity.this, ((Boolean) message.obj).booleanValue());
                PhotoPreviewActivity.this.reflashData2Delete();
                return;
            }
            if (i == 120) {
                if (PhotoPreviewActivity.this.getResources().getConfiguration().orientation == 2) {
                    Bitmap bitmap = (Bitmap) message.obj;
                    PhotoPreviewActivity.this.mBitmap = bitmap;
                    LogWD.writeMsg(this, 2, "显示 bitmap：" + bitmap);
                    PhotoPreviewActivity.this.mCameraShowView.setImageBitmap(bitmap);
                    return;
                }
                return;
            }
            if (i != 121) {
                if (i != 205) {
                    return;
                }
                PhotoPreviewActivity.this.startDetleteFile();
            } else {
                boolean zBooleanValue = ((Boolean) message.obj).booleanValue();
                UtilTools.showResultToast(PhotoPreviewActivity.this, zBooleanValue);
                if (zBooleanValue) {
                    PhotoPreviewActivity.this.reflashData2takePhoto();
                }
            }
        }
    };
    private List<Uri> ulist = new ArrayList();
    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            super.onScrollStateChanged(recyclerView, i);
            int iFindFirstVisibleItemPosition = PhotoPreviewActivity.this.linearLayoutManager.findFirstVisibleItemPosition();
            if (PhotoPreviewActivity.this.getResources().getConfiguration().orientation == 1) {
                PhotoPreviewActivity.this.mViewPage.setCurrentItem(iFindFirstVisibleItemPosition + 4);
            } else {
                PhotoPreviewActivity.this.mViewPage.setCurrentItem(iFindFirstVisibleItemPosition + 8);
            }
        }
    };
    private DeviceInsertRegisterReceiver mDeviceInsertRegisterReceiver = null;

    @Override
    public void onPageScrollStateChanged(int i) {
    }

    @Override
    public void onPageScrolled(int i, float f, int i2) {
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0413R.layout.activity_photo_preview);
        getWindow().addFlags(134217728);
        SystemUtil.setTransparent(this);
        registerBoadcastReceiverHandle();
        initView();
        initData();
        initListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.mHandler.sendEmptyMessageDelayed(0, 100L);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.mDeviceInsertRegisterReceiver);
    }

    protected void hideBottomUIMenu() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            getWindow().getDecorView().setSystemUiVisibility(8);
        } else if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(InputDeviceCompat.SOURCE_TOUCHSCREEN);
        }
    }

    private void initView() {
        this.mViewPage = (I4seasonViewPager) findViewById(C0413R.id.photo_preview_viewpage);
        this.mTopBar = (RelativeLayout) findViewById(C0413R.id.photo_preview_topbar);
        this.mTopBack = (ImageView) findViewById(C0413R.id.photo_preview_back);
        this.mTopFileName = (TextView) findViewById(C0413R.id.photo_preview_filename);
        this.mCampareCancel = (ImageView) findViewById(C0413R.id.photo_preview_cancel);
        this.mBottomBar = (RelativeLayout) findViewById(C0413R.id.photo_preview_bottombar);
        this.mBottomLayout = (RelativeLayout) findViewById(C0413R.id.photo_preview_bottom);
        this.mBottomDelete = (LinearLayout) findViewById(C0413R.id.file_bottom_delete);
        this.mBottomDeleteImage = (ImageView) findViewById(C0413R.id.file_bottom_delete_image);
        this.mBottomDeleteText = (TextView) findViewById(C0413R.id.file_bottom_delete_text);
        this.mBottomShare = (LinearLayout) findViewById(C0413R.id.file_bottom_share);
        this.mBottomShareImage = (ImageView) findViewById(C0413R.id.file_bottom_share_image);
        this.mBottomShareText = (TextView) findViewById(C0413R.id.file_bottom_share_text);
        this.mBottomCompare = (LinearLayout) findViewById(C0413R.id.file_bottom_compare);
        this.mBottomCompare.setVisibility(8);
        this.mCustomRecycleView = (CustomRecycleView) findViewById(C0413R.id.photo_preview_horizontal);
        this.mCameraShowView = (TransformativeImageView) findViewById(C0413R.id.photo_camera_show);
        this.mCompareShowRl = (RelativeLayout) findViewById(C0413R.id.photo_camera_show_rl);
        this.mCameraTake = (ImageView) findViewById(C0413R.id.photo_camera_take);
        this.mBottomLayout.setVisibility(0);
        this.mBottomDelete.setEnabled(true);
        this.mBottomShare.setEnabled(true);
        this.mBottomDeleteImage.setImageResource(C0413R.drawable.ic_delete_icon);
        this.mBottomShareImage.setImageResource(C0413R.drawable.ic_share_icon);
        this.mBottomDeleteText.setTextColor(-1);
        this.mBottomShareText.setTextColor(-1);
    }

    private void initData() {
        this.spUtils = new SpUtils(this);
        Bundle extras = getIntent().getExtras();
        this.mIsCompare = extras.getBoolean(Constant.PHOTO_VIEW_COMPARE);
        extras.clear();
        getIntent().setFlags(1207959552);
        this.mCurrPosition = FileNodeOpenInstance.getInstance().getmOpenIndex();
        this.fileArray.addAll(FileNodeOpenInstance.getInstance().getmPicFileList());
        this.photoPreviewAdapter = new PhotoPreviewAdapter(this, this.fileArray, false);
        this.mViewPage.setAdapter(this.photoPreviewAdapter);
        this.mViewPage.setCurrentItem(this.mCurrPosition);
        this.mTopFileName.setText((this.mCurrPosition + 1) + "/" + this.fileArray.size());
        this.linearLayoutManager = new ScrollSpeedLinearLayoutManager(this);
        this.linearLayoutManager.setSpeedSlow();
        this.linearLayoutManager.setOrientation(0);
        this.mCustomRecycleView.setLayoutManager(this.linearLayoutManager);
        this.horizontalThumbAdapter = new HorizontalThumbAdapter(this, this.fileArray, this.mCurrPosition);
        this.mCustomRecycleView.setAdapter(this.horizontalThumbAdapter);
        int i = this.mCurrPosition;
        this.mCustomRecycleView.scrollToPosition(i < 4 ? 0 : i - 4);
        this.lastFirstVisibleItemPosition = this.linearLayoutManager.findFirstVisibleItemPosition();
        if (this.mIsCompare) {
            setRequestedOrientation(0);
            this.mCompareShowRl.setVisibility(0);
            this.mCampareCancel.setVisibility(8);
            this.mBottomLayout.setVisibility(8);
        }
    }

    private void initListener() {
        this.mTopBack.setOnClickListener(this);
        this.mBottomDelete.setOnClickListener(this);
        this.mBottomShare.setOnClickListener(this);
        this.mBottomCompare.setOnClickListener(this);
        this.mCameraShowView.setOnClickListener(this);
        this.mCampareCancel.setOnClickListener(this);
        this.mCameraTake.setOnClickListener(this);
        this.mViewPage.setOnPageChangeListener(this);
        this.photoPreviewAdapter.setOnPhotoViewClickListener(this);
        this.horizontalThumbAdapter.setOnThumbViewClickListener(this);
        this.mCustomRecycleView.setOnScrollListener(this.onScrollListener);
    }

    @Override
    public void onClick(View view) {
        int i;
        switch (view.getId()) {
            case C0413R.id.file_bottom_compare:
            case C0413R.id.photo_preview_cancel:
                if (getResources().getConfiguration().orientation == 1) {
                    setRequestedOrientation(0);
                    this.mCompareShowRl.setVisibility(0);
                    this.mCampareCancel.setVisibility(8);
                    this.mBottomLayout.setVisibility(8);
                } else {
                    setRequestedOrientation(1);
                    this.mCompareShowRl.setVisibility(8);
                    this.mCampareCancel.setVisibility(8);
                    this.mBottomLayout.setVisibility(0);
                }
                break;
            case C0413R.id.file_bottom_delete:
                if (this.mCurrPosition <= this.fileArray.size() - 1 && this.mCurrPosition >= 0) {
                    deleteFile();
                }
                break;
            case C0413R.id.file_bottom_share:
                if (this.mCurrPosition <= this.fileArray.size() - 1 && (i = this.mCurrPosition) >= 0) {
                    shareFile(this.fileArray.get(i));
                }
                break;
            case C0413R.id.photo_camera_show:
                hideBottomBar();
                break;
            case C0413R.id.photo_camera_take:
                if (this.mBitmap != null) {
                    takePhoto();
                }
                break;
            case C0413R.id.photo_preview_back:
                if (this.mIsCompare || getResources().getConfiguration().orientation == 1) {
                    finish();
                } else {
                    setRequestedOrientation(1);
                    this.mCompareShowRl.setVisibility(8);
                    this.mCampareCancel.setVisibility(8);
                    this.mBottomLayout.setVisibility(0);
                }
                break;
        }
        this.mHandler.sendEmptyMessageDelayed(0, 200L);
    }

    private void setBottomBarWidth(boolean z) {
        ViewGroup.LayoutParams layoutParams = this.mBottomBar.getLayoutParams();
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        if (z) {
            layoutParams.width = defaultDisplay.getWidth();
        } else {
            layoutParams.width = (int) (((double) defaultDisplay.getWidth()) * 0.5d);
        }
        this.mBottomBar.setLayoutParams(layoutParams);
    }

    private void deleteFile() {
        GeneralDialog generalDialog = new GeneralDialog(this, this.mHandler, C0413R.string.App_Action_Delete_Message);
        generalDialog.setCanceledOnTouchOutside(false);
        generalDialog.show();
        hideBottomUIMenu();
    }

    public void startDetleteFile() {
        FileNode fileNode = this.fileArray.get(this.mCurrPosition);
        if (this.mFileNodeDeleteHandler == null) {
            this.mFileNodeDeleteHandler = new FileNodeDeleteHandler(this);
        }
        this.mFileNodeDeleteHandler.startDeleteFile(fileNode, fileNode.isLocal());
    }

    @Override
    public void deleteFinish(boolean z, FileNode fileNode, int i) {
        LogWD.writeMsg(this, 1024, "删除 isSuccessful: " + z + "  errcode: " + i);
        if (z) {
            FileNodeOpenInstance.getInstance().getmPicFileList().remove(fileNode);
            MainFrameHandleInstance.getInstance().sendPhotoDeleteBoradcastNotify(fileNode);
            if (FileNodeOpenInstance.getInstance().getmPicFileList().size() <= 0) {
                finish();
                return;
            }
            Message messageObtain = Message.obtain();
            messageObtain.what = 1;
            messageObtain.obj = Boolean.valueOf(z);
            this.mHandler.sendMessage(messageObtain);
            return;
        }
        if (Build.VERSION.SDK_INT >= 30) {
            android11FileDelete();
        }
    }

    private void android11FileDelete() {
        Uri uriWithAppendedId;
        try {
            this.ulist.clear();
            this.needDeleteFileNode = this.fileArray.get(this.mCurrPosition);
            long filePathToMediaID = UtilTools.getFilePathToMediaID(new File(this.needDeleteFileNode.getmFileDevPath()).getAbsolutePath(), this);
            if (this.needDeleteFileNode.getmFileDevPath().contains(".mp4")) {
                uriWithAppendedId = ContentUris.withAppendedId(MediaStore.Video.Media.getContentUri("external"), filePathToMediaID);
            } else {
                uriWithAppendedId = ContentUris.withAppendedId(MediaStore.Images.Media.getContentUri("external"), filePathToMediaID);
            }
            this.ulist.add(uriWithAppendedId);
            if (Build.VERSION.SDK_INT >= 30) {
                startIntentSenderForResult(MediaStore.createWriteRequest(getContentResolver(), this.ulist).getIntentSender(), 1001, null, 0, 0, 0);
            }
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
            UtilTools.showResultToast(this, false);
        }
    }

    @Override
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1001) {
            if (i2 == -1) {
                Iterator<Uri> it = this.ulist.iterator();
                while (it.hasNext()) {
                    getContentResolver().delete(it.next(), null, null);
                }
                UtilTools.showResultToast(this, true);
                deleteFinish(true, this.needDeleteFileNode, 0);
                return;
            }
            UtilTools.showResultToast(this, false);
        }
    }

    private void shareFile(FileNode fileNode) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(fileNode);
        FileUtil.shareMultipleFile(arrayList, this);
        hideBottomUIMenu();
    }

    public void reflashData2Delete() {
        List<FileNode> list = FileNodeOpenInstance.getInstance().getmPicFileList();
        this.fileArray.clear();
        this.fileArray.addAll(list);
        this.photoPreviewAdapter.setFileNodeArray(this.fileArray);
        this.mViewPage.setAdapter(this.photoPreviewAdapter);
        this.horizontalThumbAdapter.setFileNodeArray(this.fileArray);
        this.horizontalThumbAdapter.notifyDataSetChanged();
        if (this.mCurrPosition > this.fileArray.size() - 1) {
            this.mCurrPosition = this.fileArray.size() - 1;
        }
        this.mViewPage.setCurrentItem(this.mCurrPosition);
        if (this.fileArray.size() > 0) {
            this.mTopFileName.setText((this.mCurrPosition + 1) + "/" + this.fileArray.size());
            return;
        }
        this.mTopFileName.setText("");
    }

    public void reflashData2takePhoto() {
        List<FileNode> list = FileNodeOpenInstance.getInstance().getmPicFileList();
        this.fileArray.clear();
        this.fileArray.addAll(list);
        this.photoPreviewAdapter.setFileNodeArray(this.fileArray);
        this.mViewPage.setAdapter(this.photoPreviewAdapter);
        this.horizontalThumbAdapter.setFileNodeArray(this.fileArray);
        this.horizontalThumbAdapter.notifyDataSetChanged();
        if (this.mCurrPosition > this.fileArray.size() - 1) {
            this.mCurrPosition = this.fileArray.size() - 1;
        }
        this.mViewPage.setCurrentItem(this.mCurrPosition);
        if (this.fileArray.size() > 0) {
            this.mTopFileName.setText((this.mCurrPosition + 1) + "/" + this.fileArray.size());
            return;
        }
        this.mTopFileName.setText("");
    }

    @Override
    public void onPageSelected(int i) {
        this.mCurrPosition = i;
        this.horizontalThumbAdapter.setmCurrPosition(this.mCurrPosition);
        this.horizontalThumbAdapter.notifyDataSetChanged();
        this.mCustomRecycleView.smoothToCenter(this.mCurrPosition);
        this.mTopFileName.setText((i + 1) + "/" + this.fileArray.size());
    }

    @Override
    public void onPhotoViewClick(int i) {
        hideBottomBar();
    }

    private void hideBottomBar() {
        if (this.isHideBar) {
            this.mTopBar.setVisibility(0);
            this.mBottomBar.setVisibility(0);
            if (getResources().getConfiguration().orientation == 1) {
                this.mBottomLayout.setVisibility(0);
            } else {
                this.mBottomLayout.setVisibility(8);
            }
        } else {
            this.mTopBar.setVisibility(8);
            this.mBottomBar.setVisibility(8);
        }
        this.isHideBar = !this.isHideBar;
    }

    @Override
    public void onThumbViewClick(int i) {
        I4seasonViewPager i4seasonViewPager = this.mViewPage;
        if (i4seasonViewPager != null) {
            i4seasonViewPager.setCurrentItem(i);
        }
    }

    private void setPhoneOrLand() {
        if (AppSrceenInfo.getInstance().isPhoneVersion()) {
            setRequestedOrientation(1);
        } else {
            setRequestedOrientation(0);
        }
    }

    public void registerBoadcastReceiverHandle() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NotifyCode.DEVICE_OFFLINE_NOTIFY);
        intentFilter.addAction(NotifyCode.PHOTO_PREVIEW_DELETE_NOTIFY);
        this.mDeviceInsertRegisterReceiver = new DeviceInsertRegisterReceiver();
        registerReceiver(this.mDeviceInsertRegisterReceiver, intentFilter);
    }

    private class DeviceInsertRegisterReceiver extends BroadcastReceiver {
        private DeviceInsertRegisterReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LogWD.writeMsg(this, 8, "HomePageRegisterReceiver onReceive() action = " + action);
            int iHashCode = action.hashCode();
            if (iHashCode != -963677799) {
                if (iHashCode == 1737269838 && action.equals(NotifyCode.DEVICE_OFFLINE_NOTIFY)) {
                }
            } else if (action.equals(NotifyCode.PHOTO_PREVIEW_DELETE_NOTIFY)) {
            }
        }
    }

    private void takePhoto() {
        LogWD.writeMsg(this, 16, "take photo");
    }

    @Override
    public void onTakePhotoOrRecoderListener(boolean z, String str, int i) {
        Message messageObtain = Message.obtain();
        messageObtain.what = TAKE_PHOTO_BACK;
        messageObtain.obj = Boolean.valueOf(z);
        this.mHandler.sendMessage(messageObtain);
    }
}
