package com.i4season.bkCamera.uirelated.other;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.i4season.bkCamera.WDApplication;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.bean.FileNode;
import com.i4season.bkCamera.uirelated.filenodeopen.photopreview.PhotoPreviewActivity;
import com.i4season.bkCamera.uirelated.filenodeopen.videoplay.VideoPlayerNewActivity;
import com.i4season.bkCamera.uirelated.functionpage.album.AlbumListActivity;
import com.i4season.bkCamera.uirelated.functionpage.camerashow.CameraShowActivity;
import com.i4season.bkCamera.uirelated.functionpage.homepage.HomePageActivity;
import com.i4season.bkCamera.uirelated.functionpage.homepage.UserplayVideoActivity;
import com.i4season.bkCamera.uirelated.functionpage.setting.ProblemPageActivity;
import com.i4season.bkCamera.uirelated.functionpage.setting.SettingActivity;
import com.i4season.bkCamera.uirelated.functionpage.setting.WSLanguageCVActivity;
import com.i4season.bkCamera.uirelated.functionpage.setting.WsAboutCVActivity;
import com.i4season.bkCamera.uirelated.functionpage.setting.WsLoadWebActivity;
import com.i4season.bkCamera.uirelated.other.Language.Strings;
import com.i4season.bkCamera.uirelated.other.dialog.CameraDeviceInitializationErrorDialog;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.Constant;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.NotifyCode;
import com.i4season.bkCamera.uirelated.other.logmanage.LogWD;
import com.i4season.i4season_camera.C0413R;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MainFrameHandleInstance {
    private static MainFrameHandleInstance instance;
    private static Lock reentantLock = new ReentrantLock();
    private CameraDeviceInitializationErrorDialog mCameraDeviceInitializationErrorDialog;
    private Context mCurrentContext;

    public static MainFrameHandleInstance getInstance() {
        if (instance == null) {
            try {
                reentantLock.lock();
                if (instance == null) {
                    instance = new MainFrameHandleInstance();
                }
            } finally {
                reentantLock.unlock();
            }
        }
        return instance;
    }

    public void setCurrentContext(Context context) {
        LogWD.writeMsg(this, 8, "setCurrentContext()");
        this.mCurrentContext = context;
    }

    private void faultTolerantContext(Context context) {
        if (this.mCurrentContext == null) {
            this.mCurrentContext = context;
        }
    }

    public Context getmCurrentContext() {
        return this.mCurrentContext;
    }

    public void sendDeviceOfflineBoradcastNotify() {
        LogWD.writeMsg(this, 64, "掉线通知");
        Log.d("liusheng", "掉线通知");
        Intent intent = new Intent();
        intent.setAction(NotifyCode.DEVICE_OFFLINE_NOTIFY);
        WDApplication.getInstance().sendBroadcast(intent);
    }

    public void sendRegistSuccfulBoradcastNotify() {
        LogWD.writeMsg(this, 64, "登录成功通知");
        Log.d("liusheng", "登录成功通知");
        Intent intent = new Intent();
        intent.setAction(NotifyCode.DEVICE_REGIST_SUCCFUL_NOTIFY);
        WDApplication.getInstance().sendBroadcast(intent);
    }

    public void sendRegistErrorBoradcastNotify(final int i) {
        LogWD.writeMsg(this, 64, "登录失败通知");
        Context context = this.mCurrentContext;
        if (context != null) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MainFrameHandleInstance mainFrameHandleInstance = MainFrameHandleInstance.this;
                    mainFrameHandleInstance.showErrorDialog(mainFrameHandleInstance.mCurrentContext, i);
                }
            });
        }
    }

    public void showErrorDialog(Context context, int i) {
        String str;
        if (i == 0 || i == 1 || i == 2) {
            str = Strings.getString(C0413R.string.License_Device_Illegal, context) + "\r\n\r\nerrCode: " + i;
        } else {
            str = "";
        }
        CameraDeviceInitializationErrorDialog cameraDeviceInitializationErrorDialog = this.mCameraDeviceInitializationErrorDialog;
        if (cameraDeviceInitializationErrorDialog == null) {
            this.mCameraDeviceInitializationErrorDialog = new CameraDeviceInitializationErrorDialog(context, C0413R.style.wdDialog, str, null);
        } else {
            cameraDeviceInitializationErrorDialog.reflashCode(str);
        }
        this.mCameraDeviceInitializationErrorDialog.setCanceledOnTouchOutside(false);
        this.mCameraDeviceInitializationErrorDialog.show();
    }

    public void sendLicenseCheckErrorAndOnlineBoradcastNotify() {
        LogWD.writeMsg(this, 64, "lic校验失败通知");
        Log.d("liusheng", "lic校验失败通知");
        Intent intent = new Intent();
        intent.setAction(NotifyCode.LICENSE_CHECK_ERROR);
        WDApplication.getInstance().sendBroadcast(intent);
    }

    public void sendFindDeviceBoradcastNotify() {
        LogWD.writeMsg(this, 64, "发现设备通知");
        Intent intent = new Intent();
        intent.setAction(NotifyCode.DEVICE_FIND);
        WDApplication.getInstance().sendBroadcast(intent);
    }

    public void sendPhotoDeleteBoradcastNotify(FileNode fileNode) {
        LogWD.writeMsg(this, 64, "图片删除通知");
        Intent intent = new Intent();
        intent.putExtra(NotifyCode.PHOTO_PREVIEW_DELETE_INFO, fileNode);
        intent.setAction(NotifyCode.PHOTO_PREVIEW_DELETE_NOTIFY);
        WDApplication.getInstance().sendBroadcast(intent);
    }

    public void sendLowerBatteryBoradcastNotify() {
        LogWD.writeMsg(this, 64, "发送低电量通知");
        Intent intent = new Intent();
        intent.setAction(NotifyCode.DEVICE_LOWER_BATTERY_NOTIFY);
        WDApplication.getInstance().sendBroadcast(intent);
    }

    public void sendOnlineBurnSussBoradcastNotify() {
        LogWD.writeMsg(this, 64, "在线烧入lic成功通知");
        Intent intent = new Intent();
        intent.setAction(NotifyCode.ONLINE_BURNING_SUSS);
        WDApplication.getInstance().sendBroadcast(intent);
    }

    public void sendBindDeviceEndBoradcastNotify() {
        LogWD.writeMsg(this, 64, "绑定设备完成  发送关闭绑定流程页面的广播");
        Intent intent = new Intent();
        intent.setAction(NotifyCode.BIND_DEVICE_END);
        WDApplication.getInstance().sendBroadcast(intent);
    }

    public void showCenterProgressDialog(boolean z) {
        LogWD.writeMsg(this, 8, "showCenterProgressDialog() isShow = " + z);
    }

    public void showHomepageActivity(Context context, boolean z) {
        LogWD.writeMsg(this, 8, "showHomepageActivity()");
        Intent intent = new Intent(WDApplication.getInstance().getApplicationContext(), (Class<?>) HomePageActivity.class);
        faultTolerantContext(context);
        this.mCurrentContext.startActivity(intent);
        if (z) {
            ((Activity) context).finish();
        }
    }

    public void showCameraShowActivity(Context context, boolean z) {
        LogWD.writeMsg(this, 8, "showHomepageActivity()");
        Intent intent = new Intent(WDApplication.getInstance().getApplicationContext(), (Class<?>) CameraShowActivity.class);
        faultTolerantContext(context);
        this.mCurrentContext.startActivity(intent);
        if (z) {
            ((Activity) context).finish();
        }
    }

    public void showSettingActivity(Context context, boolean z) {
        LogWD.writeMsg(this, 8, "showHomepageActivity()");
        Intent intent = new Intent(WDApplication.getInstance().getApplicationContext(), (Class<?>) SettingActivity.class);
        faultTolerantContext(context);
        this.mCurrentContext.startActivity(intent);
        if (z) {
            ((Activity) context).finish();
        }
    }

    public void showWSLanguageCVActivity(Context context, boolean z) {
        LogWD.writeMsg(this, 8, "showLoginActivity()");
        Intent intent = new Intent(WDApplication.getInstance().getApplicationContext(), (Class<?>) WSLanguageCVActivity.class);
        faultTolerantContext(context);
        this.mCurrentContext.startActivity(intent);
        if (z) {
            ((Activity) context).finish();
        }
    }

    public void showWsAboutCVActivity(Context context, boolean z) {
        LogWD.writeMsg(this, 8, "WsAboutCVActivity()");
        Intent intent = new Intent(WDApplication.getInstance().getApplicationContext(), (Class<?>) WsAboutCVActivity.class);
        Context context2 = this.mCurrentContext;
        if (context2 != null) {
            context2.startActivity(intent);
        } else {
            context.startActivity(intent);
        }
        if (z) {
            ((Activity) context).finish();
        }
    }

    public void showWsLoadWebActivity(Context context, boolean z, String str, boolean z2) {
        LogWD.writeMsg(this, 8, "WsAboutCVActivity()");
        Intent intent = new Intent(WDApplication.getInstance().getApplicationContext(), (Class<?>) WsLoadWebActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.WEB_HTML, str);
        bundle.putBoolean(Constant.WEB_TYPE, z2);
        intent.putExtras(bundle);
        Context context2 = this.mCurrentContext;
        if (context2 != null) {
            context2.startActivity(intent);
        } else {
            context.startActivity(intent);
        }
        if (z) {
            ((Activity) context).finish();
        }
    }

    public void showAlbumListActivity(Context context, boolean z) {
        LogWD.writeMsg(this, 8, "showLoginActivity()");
        Intent intent = new Intent(WDApplication.getInstance().getApplicationContext(), (Class<?>) AlbumListActivity.class);
        faultTolerantContext(context);
        this.mCurrentContext.startActivity(intent);
        if (z) {
            ((Activity) context).finish();
        }
    }

    public void showPhotoPreviewActivity(Context context, Bundle bundle, boolean z) {
        LogWD.writeMsg(this, 8, "WsAboutCVActivity()");
        Intent intent = new Intent(WDApplication.getInstance().getApplicationContext(), (Class<?>) PhotoPreviewActivity.class);
        intent.putExtras(bundle);
        faultTolerantContext(context);
        this.mCurrentContext.startActivity(intent);
        if (z) {
            ((Activity) context).finish();
        }
    }

    public void showVideoPlayerNewActivity(Context context, boolean z) {
        LogWD.writeMsg(this, 8, "showVideoPlayerActivity()");
        Intent intent = new Intent(WDApplication.getInstance().getApplicationContext(), (Class<?>) VideoPlayerNewActivity.class);
        faultTolerantContext(context);
        this.mCurrentContext.startActivity(intent);
        if (z) {
            ((Activity) context).finish();
        }
    }

    public void showProblemPageActivity(Context context, boolean z) {
        LogWD.writeMsg(this, 8, "showVideoPlayerActivity()");
        Intent intent = new Intent(WDApplication.getInstance().getApplicationContext(), (Class<?>) ProblemPageActivity.class);
        faultTolerantContext(context);
        this.mCurrentContext.startActivity(intent);
        if (z) {
            ((Activity) context).finish();
        }
    }

    public void UserplayVideoActivity(Context context, int i, boolean z) {
        LogWD.writeMsg(this, 8, "showCameraShowActivity()");
        Intent intent = new Intent(WDApplication.getInstance().getApplicationContext(), (Class<?>) UserplayVideoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.SECRET_POINT, i);
        intent.putExtras(bundle);
        faultTolerantContext(context);
        this.mCurrentContext.startActivity(intent);
        if (z) {
            ((Activity) context).finish();
        }
    }
}
