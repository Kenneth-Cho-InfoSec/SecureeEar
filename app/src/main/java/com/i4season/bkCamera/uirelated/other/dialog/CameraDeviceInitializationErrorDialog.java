package com.i4season.bkCamera.uirelated.other.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import com.i4season.bkCamera.uirelated.other.Language.Strings;
import com.i4season.i4season_camera.C0413R;

public class CameraDeviceInitializationErrorDialog extends Dialog implements View.OnClickListener {
    public static final int BUTTON_OK = 100;
    private Context mContext;
    private String mErrorCause;
    private TextView mErrorText;
    private TextView mExitSure;
    private Handler mHandler;

    public CameraDeviceInitializationErrorDialog(Context context) {
        super(context);
    }

    public CameraDeviceInitializationErrorDialog(Context context, int i, String str, Handler handler) {
        super(context, i);
        this.mContext = context;
        this.mErrorCause = str;
        if (handler != null) {
            this.mHandler = handler;
        }
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0413R.layout.dialog_initialization_error);
        initView();
        initListener();
    }

    private void initView() {
        this.mErrorText = (TextView) findViewById(C0413R.id.initialization_error_cause);
        this.mExitSure = (TextView) findViewById(C0413R.id.initialization_error_sure);
        this.mErrorText.setText(this.mErrorCause);
        this.mExitSure.setText(Strings.getString(C0413R.string.App_GuideView_Click, this.mContext));
    }

    private void initListener() {
        this.mExitSure.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() != C0413R.id.initialization_error_sure) {
            return;
        }
        dismiss();
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.sendEmptyMessage(100);
        }
    }

    public void reflashCode(String str) {
        this.mErrorCause = str;
        this.mErrorText.setText(this.mErrorCause);
    }
}
