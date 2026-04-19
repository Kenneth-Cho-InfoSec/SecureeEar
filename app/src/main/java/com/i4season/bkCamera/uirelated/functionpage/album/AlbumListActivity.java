package com.i4season.bkCamera.uirelated.functionpage.album;

import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.i4season.bkCamera.logicrelated.camera.CameraEventObserver;
import com.i4season.bkCamera.logicrelated.camera.CameraManager;
import com.i4season.bkCamera.logicrelated.conversionutil.DataContants;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.FileDataAcceptAndOperation;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.FileNodeArrayManager;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.IExplorerManagerDelegate;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.bean.FileNode;
import com.i4season.bkCamera.uirelated.filenodeopen.FileNodeOpenInstance;
import com.i4season.bkCamera.uirelated.functionpage.album.adapter.CameraDataShowDetailAdapter;
import com.i4season.bkCamera.uirelated.other.AppPathInfo;
import com.i4season.bkCamera.uirelated.other.Language.Strings;
import com.i4season.bkCamera.uirelated.other.MainFrameHandleInstance;
import com.i4season.bkCamera.uirelated.other.dialog.CenterProgressDialog;
import com.i4season.bkCamera.uirelated.other.dialog.GeneralDialog;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.AppSrceenInfo;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.FileUtil;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.NotifyCode;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.SystemUtil;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.UtilTools;
import com.i4season.bkCamera.uirelated.other.logmanage.LogWD;
import com.i4season.bkCamera.uirelated.other.view.stickygridheaders.StickyGridHeadersGridView;
import com.i4season.i4season_camera.C0413R;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class AlbumListActivity extends AppCompatActivity implements View.OnClickListener, IExplorerManagerDelegate, AdapterView.OnItemClickListener, CameraEventObserver.OnTakePhotoAllFinishListener {
    public static final int HANDLER_MAX_LOADING = 105;
    public static final int HANDLER_MESSAGE_DOWNLOAD_FINISH = 103;
    public static final int HANDLER_MESSAGE_FILESHOW_REFLASH_FILE_SIZE = 102;
    public static final int HANDLER_MESSAGE_FILE_OPERATION_FAIL = 101;
    public static final int HANDLER_MESSAGE_FILE_OPERATION_SUCCFUL = 100;
    public static final int HANDLER_REFLASH_FILELIST = 104;
    public static final int INIT_CAMERA_REFLASH_BOTTOM_STATUS = 4;
    private static final int MAX_LOADING_TIME = 20000;
    private static final int REQUEST_PERMISSION_CODE = 1001;
    private View lineView;
    private ImageView mBack;
    private LinearLayout mBottomDelete;
    private RelativeLayout mBottomLayout;
    private LinearLayout mBottomShare;
    protected CameraDataShowDetailAdapter mCameraDataShowDetailAdapter;
    private boolean mCurrentModel;
    protected FileDataAcceptAndOperation mFileDataAcceptAndOperation;
    private List<FileNode> mFileList;
    private TextView mLeftSelect;
    protected LinearLayout mNoDataSaved;
    protected TextView mNoDataSavedText;
    private ImageView mPhotoModel;
    private RelativeLayout mPhotoModelRl;
    private TextView mRightCancel;
    protected StickyGridHeadersGridView mTimeGridview;
    private LinkedHashMap<String, LinkedList<FileNode>> mTimeTagArrays;
    protected LinearLayout mTopBar;
    private ImageView mTopEdit;
    private TextView mTopTitle;
    private ImageView mVideoModel;
    private RelativeLayout mVideoModelRl;
    private final int GET_SHOW_DATA_FINISH = 0;
    private final int GET_SHOW_DATA_FINISH_NONE = 1;
    private boolean isEditMode = false;
    private boolean isAddData = false;
    private CenterProgressDialog centerProgressDialog = null;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void dispatchMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                if (CameraManager.getInstance().isTakePhotoAll()) {
                    AlbumListActivity.this.updateListUi();
                    return;
                }
                return;
            }
            if (i == 1) {
                MainFrameHandleInstance.getInstance().showCenterProgressDialog(false);
                AlbumListActivity.this.mNoDataSaved.setVisibility(0);
                AlbumListActivity.this.mTimeGridview.setVisibility(8);
                return;
            }
            if (i == 4) {
                AlbumListActivity.this.reflashBottomBtnStatus();
                return;
            }
            if (i != 205) {
                switch (i) {
                    case 100:
                        AlbumListActivity.this.operationActionSuccfulHandler(message);
                        break;
                    case 101:
                        AlbumListActivity.this.operationActionErrorHandler(message);
                        break;
                    case 102:
                        if (AlbumListActivity.this.mCameraDataShowDetailAdapter != null && AlbumListActivity.this.mCameraDataShowDetailAdapter.isEditMode()) {
                            AlbumListActivity.this.mTopTitle.setText(String.format(Strings.getString(C0413R.string.File_Show_Manager_Title_Select, AlbumListActivity.this), String.valueOf(AlbumListActivity.this.mFileDataAcceptAndOperation.getmFileNodeArrayManager().getAllSelectedFileNode().size())));
                        }
                        AlbumListActivity.this.mHandler.sendEmptyMessage(4);
                        break;
                    case 103:
                        if (AlbumListActivity.this.mCameraDataShowDetailAdapter != null) {
                            AlbumListActivity.this.mCameraDataShowDetailAdapter.notifyDataSetChanged();
                        }
                        break;
                    case 104:
                        MainFrameHandleInstance.getInstance().showCenterProgressDialog(false);
                        if (AlbumListActivity.this.mCameraDataShowDetailAdapter != null) {
                            AlbumListActivity.this.mCameraDataShowDetailAdapter.setSortFileList(AlbumListActivity.this.mFileList);
                            AlbumListActivity.this.mCameraDataShowDetailAdapter.setStringLinkedListLinkedHashMap(AlbumListActivity.this.mTimeTagArrays);
                            AlbumListActivity.this.mHandler.sendEmptyMessage(102);
                            AlbumListActivity.this.mCameraDataShowDetailAdapter.notifyDataSetChanged();
                        }
                        if (AlbumListActivity.this.mFileList == null || AlbumListActivity.this.mFileList.size() == 0) {
                            AlbumListActivity.this.mNoDataSaved.setVisibility(0);
                        } else {
                            AlbumListActivity.this.mNoDataSaved.setVisibility(8);
                        }
                        break;
                    case 105:
                        CameraManager.getInstance().setPhotoIndex(0);
                        AlbumListActivity.this.updateListUi();
                        break;
                }
                return;
            }
            if (AlbumListActivity.this.mFileDataAcceptAndOperation.getmFileNodeArrayManager().getAllSelectedFileNode().size() > 0) {
                AlbumListActivity.this.mFileDataAcceptAndOperation.getmFileListWebDavCommandHandle().tryDeleteFile();
            }
        }
    };
    private List<Uri> ulist = new ArrayList();
    private List<FileNode> needDeleteList = new ArrayList();
    private DeviceInsertRegisterReceiver mDeviceInsertRegisterReceiver = null;

    public void updateListUi() {
        CenterProgressDialog centerProgressDialog = this.centerProgressDialog;
        if (centerProgressDialog != null && centerProgressDialog.isShowing()) {
            this.centerProgressDialog.dismiss();
        }
        this.mHandler.removeMessages(105);
        MainFrameHandleInstance.getInstance().showCenterProgressDialog(false);
        this.mNoDataSaved.setVisibility(8);
        this.mTimeGridview.setVisibility(0);
        if (this.mCameraDataShowDetailAdapter != null) {
            this.mTimeGridview.setAdapter((ListAdapter) null);
            this.mCameraDataShowDetailAdapter = null;
        }
        this.mCameraDataShowDetailAdapter = new CameraDataShowDetailAdapter(this, this.mFileList, this.mTimeGridview, this.mHandler, this.mTimeTagArrays, false);
        this.mTimeGridview.setAdapter((ListAdapter) this.mCameraDataShowDetailAdapter);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0413R.layout.activity_album_list);
        SystemUtil.setTransparent(this);
        initView();
        initData();
        initListener();
        registerBoadcastReceiverHandle();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mHandler.removeMessages(105);
        unregisterReceiver(this.mDeviceInsertRegisterReceiver);
        CameraManager.getInstance().removeTakePhotoAllListener(this);
    }

    @Override
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            existFileManager();
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }

    private void initView() {
        this.mTopBar = (LinearLayout) findViewById(C0413R.id.file_manager_top_bar);
        this.mBack = (ImageView) findViewById(C0413R.id.app_topbar_left_image);
        this.mTopTitle = (TextView) findViewById(C0413R.id.app_topbar_center_text);
        this.mTopEdit = (ImageView) findViewById(C0413R.id.app_topbar_right_image);
        this.mLeftSelect = (TextView) findViewById(C0413R.id.app_topbar_left_text);
        this.mRightCancel = (TextView) findViewById(C0413R.id.app_topbar_right_text);
        this.lineView = findViewById(C0413R.id.file_manager_bottom_lines);
        this.mBottomLayout = (RelativeLayout) findViewById(C0413R.id.file_manager_bottom);
        this.mBottomDelete = (LinearLayout) findViewById(C0413R.id.file_bottom_delete);
        this.mBottomShare = (LinearLayout) findViewById(C0413R.id.file_bottom_share);
        this.mNoDataSaved = (LinearLayout) findViewById(C0413R.id.file_manager_no_data_saved);
        this.mNoDataSavedText = (TextView) findViewById(C0413R.id.file_manager_no_data_saved_text);
        this.mTimeGridview = (StickyGridHeadersGridView) findViewById(C0413R.id.file_manager_pic_timeline);
        AppSrceenInfo.getInstance().initScreenPixValue(this);
        if (AppSrceenInfo.getInstance().isPhoneVersion()) {
            this.mTimeGridview.setNumColumns(4);
        } else {
            this.mTimeGridview.setNumColumns(7);
        }
        this.mPhotoModel = (ImageView) findViewById(C0413R.id.ic_album_photo);
        this.mVideoModel = (ImageView) findViewById(C0413R.id.ic_album_video);
        this.mPhotoModelRl = (RelativeLayout) findViewById(C0413R.id.ic_album_photo_rl);
        this.mVideoModelRl = (RelativeLayout) findViewById(C0413R.id.ic_album_video_rl);
        if (CameraManager.getInstance().isTakePhotoAll()) {
            return;
        }
        CameraManager.getInstance().addTakePhotoAllListener(this);
    }

    private void initData() {
        this.mLeftSelect.setText(Strings.getString(C0413R.string.App_Button_Cancel, this));
        this.mRightCancel.setText(Strings.getString(C0413R.string.File_Manager_Edit_Select_All, this));
        this.mTopTitle.setVisibility(0);
        this.mBack.setVisibility(0);
        this.mTopEdit.setVisibility(0);
        this.mBack.setImageResource(C0413R.drawable.ic_app_back_black);
        this.mTopEdit.setImageResource(C0413R.drawable.ic_edit_join);
        this.mNoDataSavedText.setText(Strings.getString(C0413R.string.File_Label_FileList_Empty, this));
        changeAlbumModel(true);
        if (this.mCurrentModel) {
            FileNodeArrayManager.setmCurrentModel(1);
            this.mTopTitle.setText(Strings.getString(C0413R.string.App_Lable_Album, this));
        } else {
            FileNodeArrayManager.setmCurrentModel(2);
            this.mTopTitle.setText(Strings.getString(C0413R.string.App_Lable_Album, this));
        }
    }

    private void initListener() {
        this.mBack.setOnClickListener(this);
        this.mTopEdit.setOnClickListener(this);
        this.mLeftSelect.setOnClickListener(this);
        this.mRightCancel.setOnClickListener(this);
        this.mBottomDelete.setOnClickListener(this);
        this.mBottomShare.setOnClickListener(this);
        this.mTimeGridview.setOnItemClickListener(this);
        this.mPhotoModelRl.setOnClickListener(this);
        this.mVideoModelRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case C0413R.id.app_topbar_left_image:
                existFileManager();
                break;
            case C0413R.id.app_topbar_left_text:
            case C0413R.id.app_topbar_right_image:
                if (this.isEditMode) {
                    exitEditMode();
                } else {
                    joinEditMode();
                }
                break;
            case C0413R.id.app_topbar_right_text:
                selecOrUnselectAll();
                break;
            case C0413R.id.file_bottom_delete:
                deleteSelectFile();
                break;
            case C0413R.id.file_bottom_share:
                shareSelectFile();
                break;
            case C0413R.id.ic_album_photo_rl:
                if (!this.isEditMode) {
                    changeAlbumModel(true);
                    break;
                }
                break;
            case C0413R.id.ic_album_video_rl:
                if (!this.isEditMode) {
                    changeAlbumModel(false);
                    break;
                }
                break;
        }
    }

    private void changeAlbumModel(boolean z) {
        this.mCurrentModel = z;
        if (z) {
            FileNodeArrayManager.setmCurrentModel(1);
            this.mPhotoModel.setImageResource(C0413R.drawable.ic_album_photo_icon);
            this.mVideoModel.setImageResource(C0413R.drawable.ic_album_video_unselect);
        } else {
            FileNodeArrayManager.setmCurrentModel(2);
            this.mPhotoModel.setImageResource(C0413R.drawable.ic_album_photo_unselect);
            this.mVideoModel.setImageResource(C0413R.drawable.ic_album_video_icon);
        }
        this.mFileDataAcceptAndOperation = new FileDataAcceptAndOperation(this, 3, 5);
        this.mFileDataAcceptAndOperation.getmFileListDataSourceHandler().queryAcceptFileListForFolderPath(AppPathInfo.getSaveCameraDataPath2AndroidO(), DataContants.CURRENT_SORT_TYPE, false);
    }

    public void deleteSelectFile() {
        if (this.mFileDataAcceptAndOperation.getmFileNodeArrayManager().getAllSelectedFileNode().size() > 0) {
            GeneralDialog generalDialog = new GeneralDialog(this, this.mHandler, C0413R.string.App_Action_Delete_Message);
            generalDialog.setCanceledOnTouchOutside(false);
            generalDialog.show();
        }
    }

    public void shareSelectFile() {
        List<FileNode> allSelectedFileNode = this.mFileDataAcceptAndOperation.getmFileNodeArrayManager().getAllSelectedFileNode();
        boolean z = false;
        if (allSelectedFileNode.size() == 1) {
            FileNode fileNode = allSelectedFileNode.get(0);
            if (fileNode.getmFileTypeMarked() == 2) {
                FileUtil.shareVideoFile(allSelectedFileNode, this);
                return;
            } else {
                if (fileNode.getmFileTypeMarked() == 1) {
                    FileUtil.shareFile2LocalPath(fileNode.getmFileDevPath(), this);
                    return;
                }
                return;
            }
        }
        if (allSelectedFileNode.size() > 1 && allSelectedFileNode.size() <= 9) {
            Iterator<FileNode> it = allSelectedFileNode.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                } else if (it.next().getmFileTypeMarked() == 2) {
                    z = true;
                    break;
                }
            }
            if (z) {
                UtilTools.showToast(this, Strings.getString(C0413R.string.App_Action_Share_Type_Message, this));
                return;
            } else {
                FileUtil.shareMultipleFile(allSelectedFileNode, this);
                return;
            }
        }
        UtilTools.showToast(this, Strings.getString(C0413R.string.App_Action_Share_Size_Message, this));
    }

    private void selecOrUnselectAll() {
        boolean z;
        if (this.mRightCancel.getText().toString().equals(Strings.getString(C0413R.string.File_Manager_Edit_Select_All, this))) {
            z = true;
            this.mRightCancel.setText(Strings.getString(C0413R.string.File_Manager_Edit_Unselect_All, this));
        } else {
            z = false;
            this.mRightCancel.setText(Strings.getString(C0413R.string.File_Manager_Edit_Select_All, this));
        }
        this.mFileDataAcceptAndOperation.getmFileListWebDavCommandHandle().trySelectOrUnselectAll(z);
    }

    public void reflashBottomBtnStatus() {
        boolean z;
        List<FileNode> allSelectedFileNode = this.mFileDataAcceptAndOperation.getmFileNodeArrayManager().getAllSelectedFileNode();
        Iterator<FileNode> it = allSelectedFileNode.iterator();
        boolean z2 = false;
        boolean z3 = false;
        while (true) {
            if (!it.hasNext()) {
                z = true;
                break;
            }
            FileNode next = it.next();
            if (next.getmFileTypeMarked() == 2) {
                z2 = true;
            } else if (next.getmFileTypeMarked() == 1) {
                z3 = true;
            }
            if (z2 && z3) {
                z = false;
                break;
            }
        }
        if (allSelectedFileNode.size() == 0) {
            this.mBottomDelete.setSelected(false);
            this.mBottomShare.setSelected(false);
            this.mBottomDelete.setEnabled(false);
            this.mBottomShare.setEnabled(false);
            return;
        }
        if (allSelectedFileNode.size() > 1 && !z) {
            this.mBottomDelete.setSelected(true);
            this.mBottomShare.setSelected(false);
            this.mBottomDelete.setEnabled(true);
            this.mBottomShare.setEnabled(false);
            return;
        }
        if (allSelectedFileNode.size() > 9) {
            this.mBottomDelete.setSelected(true);
            this.mBottomShare.setSelected(false);
            this.mBottomDelete.setEnabled(true);
            this.mBottomShare.setEnabled(false);
            return;
        }
        this.mBottomDelete.setSelected(true);
        this.mBottomShare.setSelected(true);
        this.mBottomDelete.setEnabled(true);
        this.mBottomShare.setEnabled(true);
    }

    private void existFileManager() {
        if (this.isEditMode) {
            exitEditMode();
        } else {
            finish();
        }
    }

    private void exitEditMode() {
        if (this.mCurrentModel) {
            this.mTopTitle.setText(Strings.getString(C0413R.string.App_Lable_Album, this));
        } else {
            this.mTopTitle.setText(Strings.getString(C0413R.string.App_Lable_Album, this));
        }
        this.isEditMode = false;
        this.mBottomLayout.setVisibility(8);
        this.mTopEdit.setVisibility(0);
        this.mBack.setVisibility(0);
        this.mLeftSelect.setVisibility(8);
        this.mRightCancel.setVisibility(8);
        CameraDataShowDetailAdapter cameraDataShowDetailAdapter = this.mCameraDataShowDetailAdapter;
        if (cameraDataShowDetailAdapter != null) {
            cameraDataShowDetailAdapter.setIsEditMode(false);
        }
        this.mFileDataAcceptAndOperation.getmFileListWebDavCommandHandle().trySelectOrUnselectAll(false);
    }

    private void joinEditMode() {
        this.mTopTitle.setText(String.format(Strings.getString(C0413R.string.File_Show_Manager_Title_Select, this), "0"));
        this.isEditMode = true;
        this.mBottomLayout.setVisibility(0);
        this.mTopEdit.setVisibility(8);
        this.mBack.setVisibility(8);
        this.mLeftSelect.setVisibility(0);
        this.mRightCancel.setVisibility(0);
        this.mLeftSelect.setText(Strings.getString(C0413R.string.App_Button_Cancel, this));
        this.mRightCancel.setText(Strings.getString(C0413R.string.File_Manager_Edit_Select_All, this));
        CameraDataShowDetailAdapter cameraDataShowDetailAdapter = this.mCameraDataShowDetailAdapter;
        if (cameraDataShowDetailAdapter != null) {
            cameraDataShowDetailAdapter.setIsEditMode(true);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        if (i < this.mFileList.size()) {
            itemClickHandler(this.mFileList.get(i), i);
        }
    }

    private void itemClickHandler(FileNode fileNode, int i) {
        if (this.mCameraDataShowDetailAdapter.isEditMode()) {
            fileNode.setmFileIsSelected(!fileNode.ismFileIsSelected());
            this.mCameraDataShowDetailAdapter.notifyDataSetChanged();
            this.mHandler.sendEmptyMessage(102);
        } else if (fileNode.isLocal()) {
            FileNodeOpenInstance.getInstance().openFile(this, i, fileNode, 5, this.mFileList);
        }
    }

    @Override
    public void acceptFileListDataSuccful(List<FileNode> list) {
        List<FileNode> list2;
        LogWD.writeMsg(this, 2, "acceptFileListDataSuccful() end fileNodes.size(): " + list.size());
        if ((this.mCurrentModel && this.mFileDataAcceptAndOperation.getmFileNodeArrayManager().getmPhotoFileList().size() > 0) || (!this.mCurrentModel && this.mFileDataAcceptAndOperation.getmFileNodeArrayManager().getmVideoFileList().size() > 0)) {
            new ArrayList();
            if (this.mCurrentModel) {
                list2 = this.mFileDataAcceptAndOperation.getmFileNodeArrayManager().getmPhotoFileList();
            } else {
                list2 = this.mFileDataAcceptAndOperation.getmFileNodeArrayManager().getmVideoFileList();
            }
            Collections.sort(list2);
            Collections.reverse(list2);
            this.mFileList = list2;
            this.mFileDataAcceptAndOperation.getmFileNodeArrayManager().selectHeadIdDayForModel(this.mCurrentModel);
            this.mTimeTagArrays = this.mFileDataAcceptAndOperation.getmFileNodeArrayManager().getTimeTagArrays();
            this.mHandler.sendEmptyMessage(0);
        } else {
            this.mHandler.sendEmptyMessage(1);
        }
        this.isAddData = true;
    }

    @Override
    public void acceptFileListDataError(int i) {
        LogWD.writeMsg(this, 2, "acceptFileListDataSuccful() errcode: " + i);
        this.mHandler.sendEmptyMessage(1);
    }

    @Override
    public void operationSuccful(int i) {
        List<FileNode> list;
        if (i == 2) {
            new ArrayList();
            if (this.mCurrentModel) {
                list = this.mFileDataAcceptAndOperation.getmFileNodeArrayManager().getmPhotoFileList();
            } else {
                list = this.mFileDataAcceptAndOperation.getmFileNodeArrayManager().getmVideoFileList();
            }
            Collections.sort(list);
            Collections.reverse(list);
            this.mFileList = list;
            this.mFileDataAcceptAndOperation.getmFileNodeArrayManager().selectHeadIdDayForModel(this.mCurrentModel);
            this.mTimeTagArrays = this.mFileDataAcceptAndOperation.getmFileNodeArrayManager().getTimeTagArrays();
        }
        Message messageObtain = Message.obtain();
        messageObtain.what = 100;
        messageObtain.obj = Integer.valueOf(i);
        this.mHandler.sendMessage(messageObtain);
    }

    @Override
    public void operationError(int i, int i2) {
        Message messageObtain = Message.obtain();
        messageObtain.what = 101;
        messageObtain.obj = Integer.valueOf(i);
        messageObtain.arg1 = i2;
        this.mHandler.sendMessage(messageObtain);
    }

    public void operationActionSuccfulHandler(Message message) {
        MainFrameHandleInstance.getInstance().showCenterProgressDialog(false);
        int iIntValue = ((Integer) message.obj).intValue();
        if (iIntValue == 2) {
            UtilTools.showResultToast(this, true);
            this.mCameraDataShowDetailAdapter.setSortFileList(this.mFileList);
            this.mCameraDataShowDetailAdapter.setStringLinkedListLinkedHashMap(this.mTimeTagArrays);
            this.mHandler.sendEmptyMessage(102);
            this.mHandler.sendEmptyMessage(104);
            MainFrameHandleInstance.getInstance().sendPhotoDeleteBoradcastNotify(null);
        } else if (iIntValue == -1) {
            this.mHandler.sendEmptyMessage(102);
        }
        CameraDataShowDetailAdapter cameraDataShowDetailAdapter = this.mCameraDataShowDetailAdapter;
        if (cameraDataShowDetailAdapter != null) {
            cameraDataShowDetailAdapter.notifyDataSetChanged();
        }
    }

    public void operationActionErrorHandler(Message message) {
        MainFrameHandleInstance.getInstance().showCenterProgressDialog(false);
        int iIntValue = ((Integer) message.obj).intValue();
        int i = message.arg1;
        if (iIntValue == 2) {
            if (Build.VERSION.SDK_INT >= 30) {
                android11FileDelete();
            } else {
                UtilTools.showResultToast(this, false);
            }
        }
    }

    private void android11FileDelete() {
        Uri uriWithAppendedId;
        try {
            this.ulist.clear();
            this.needDeleteList.clear();
            this.needDeleteList = this.mFileDataAcceptAndOperation.getmFileNodeArrayManager().getAllSelectedFileNode();
            for (FileNode fileNode : this.needDeleteList) {
                long filePathToMediaID = UtilTools.getFilePathToMediaID(new File(fileNode.getmFileDevPath()).getAbsolutePath(), this);
                if (fileNode.getmFileDevPath().contains(".mp4")) {
                    uriWithAppendedId = ContentUris.withAppendedId(MediaStore.Video.Media.getContentUri("external"), filePathToMediaID);
                } else {
                    uriWithAppendedId = ContentUris.withAppendedId(MediaStore.Images.Media.getContentUri("external"), filePathToMediaID);
                }
                this.ulist.add(uriWithAppendedId);
            }
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
                this.mFileDataAcceptAndOperation.getmFileNodeArrayManager().removeFileNode(this.needDeleteList);
                operationSuccful(2);
                return;
            }
            UtilTools.showResultToast(this, false);
        }
    }

    public void registerBoadcastReceiverHandle() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NotifyCode.PHOTO_PREVIEW_DELETE_NOTIFY);
        this.mDeviceInsertRegisterReceiver = new DeviceInsertRegisterReceiver();
        registerReceiver(this.mDeviceInsertRegisterReceiver, intentFilter);
    }

    @Override
    public void onTakePhotoAllFinish() {
        if (this.isAddData) {
            this.isAddData = false;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlbumListActivity.this.updateListUi();
                }
            });
        }
    }

    private class DeviceInsertRegisterReceiver extends BroadcastReceiver {
        private DeviceInsertRegisterReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LogWD.writeMsg(this, 8, "HomePageRegisterReceiver onReceive() action = " + action);
            if (((action.hashCode() == -963677799 && action.equals(NotifyCode.PHOTO_PREVIEW_DELETE_NOTIFY)) ? (byte) 0 : (byte) -1) == 0) {
                if (((FileNode) intent.getSerializableExtra(NotifyCode.PHOTO_PREVIEW_DELETE_INFO)) == null) {
                    Log.d("liusheng", "当前页面  删除 不刷新");
                    return;
                }
                Log.d("liusheng", "其他页面删除  刷新");
                MainFrameHandleInstance.getInstance().showCenterProgressDialog(true);
                String saveCameraDataPath2AndroidO = AppPathInfo.getSaveCameraDataPath2AndroidO();
                AlbumListActivity.this.mFileDataAcceptAndOperation.getmFileNodeArrayManager().clearDlnaList();
                AlbumListActivity.this.mFileDataAcceptAndOperation.getmFileListDataSourceHandler().queryAcceptFileListForFolderPath(saveCameraDataPath2AndroidO, DataContants.CURRENT_SORT_TYPE, false);
            }
            intent.setFlags(1207959552);
        }
    }
}
