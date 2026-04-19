package com.i4season.bkCamera.uirelated.functionpage.initpage;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import com.i4season.bkCamera.WDApplication;
import com.i4season.bkCamera.uirelated.other.MainFrameHandleInstance;
import com.i4season.bkCamera.uirelated.other.dialog.UserNoticeDialog;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.Constant;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.SpUtils;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.SystemUtil;
import com.i4season.i4season_camera.C0413R;

public class InitPageActivity extends AppCompatActivity {
    public static final int GO_HOMEPAGE = 1;
    public static final int GO_HOMEPAGE_DELAY = 1000;
    private Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                InitPageActivity.this.gotoHomepage();
            } else {
                if (i != 125) {
                    return;
                }
                InitPageActivity.this.spUtils.putBoolean(Constant.LAUNCHER_FIRST, false);
                WDApplication.getInstance().init();
                InitPageActivity.this.gotoHomepage();
            }
        }
    };
    private SpUtils spUtils;
    private UserNoticeDialog userNoticeDialog;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0413R.layout.activity_initpage);
        SystemUtil.setTransparent(this);
        if (!isTaskRoot()) {
            finish();
            return;
        }
        this.spUtils = new SpUtils(this);
        if (this.spUtils.getBoolean(Constant.LAUNCHER_FIRST, true)) {
            this.userNoticeDialog = new UserNoticeDialog(this, this.mHandler);
            this.userNoticeDialog.show();
        } else {
            this.mHandler.sendEmptyMessageDelayed(1, 1000L);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void gotoHomepage() {
        MainFrameHandleInstance.getInstance().showHomepageActivity(this, true);
    }
}
