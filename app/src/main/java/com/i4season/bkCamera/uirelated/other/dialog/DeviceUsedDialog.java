package com.i4season.bkCamera.uirelated.other.dialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import com.i4season.bkCamera.uirelated.other.Language.Strings;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.SpUtils;
import com.i4season.i4season_camera.C0413R;
import com.jni.WifiCameraApi;

public class DeviceUsedDialog extends BaseDialog implements View.OnClickListener {
    public static final int GO_CAMERASHOW = 10077;
    private TextView cancel;
    private Handler handler;
    private SpUtils mspUtils;

    private TextView f67ok;
    private TextView prompt;

    public DeviceUsedDialog(Context context, Handler handler) {
        super(context);
        this.handler = handler;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0413R.layout.dialog_device_used);
        this.prompt = (TextView) findViewById(C0413R.id.device_used_prompt);
        this.cancel = (TextView) findViewById(C0413R.id.device_used_cancel);
        this.f67ok = (TextView) findViewById(C0413R.id.device_used_ok);
        this.prompt.setText(Strings.getString(C0413R.string.Dialog_Preemptive_Port, getContext()));
        this.cancel.setText(Strings.getString(C0413R.string.App_Button_Cancel, getContext()));
        this.f67ok.setText(Strings.getString(C0413R.string.App_Button_OK, getContext()));
        this.cancel.setOnClickListener(this);
        this.f67ok.setOnClickListener(this);
        this.mspUtils = new SpUtils(this.mContext);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case C0413R.id.device_used_cancel:
                dismiss();
                break;
            case C0413R.id.device_used_ok:
                new Thread(new Runnable() {
                    @Override
                    public final void run() {
                        WifiCameraApi.getInstance().gWifiCamera.openVideoForceApi();
                    }
                }).start();
                dismiss();
                break;
        }
    }
}
