package com.i4season.bkCamera.uirelated.other.dialog;

import android.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import com.i4season.bkCamera.uirelated.other.Language.Strings;
import com.i4season.i4season_camera.C0413R;

public class LowBatteryDialog extends BaseDialog implements View.OnClickListener {
    private ImageView lowBatteryImg;
    private TextView mCloseBtn;
    private boolean mIsTurnOff;
    private TextView mLowBatteryTurnOffTv;
    private TextView mLowBatteryTv;
    private TextView mLowBatteryWarnningTv;

    public LowBatteryDialog(Context context, boolean z) {
        super(context);
        setCancelable(false);
        this.mIsTurnOff = z;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0413R.layout.dialog_low_battery);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        this.lowBatteryImg = (ImageView) findViewById(C0413R.id.low_battery_img);
        this.mLowBatteryTv = (TextView) findViewById(C0413R.id.low_battery_tv);
        this.mLowBatteryWarnningTv = (TextView) findViewById(C0413R.id.low_battery_warning);
        this.mLowBatteryTurnOffTv = (TextView) findViewById(C0413R.id.low_tutn_off_tv);
        this.mCloseBtn = (TextView) findViewById(C0413R.id.low_battery_close);
        this.mLowBatteryWarnningTv.setVisibility(8);
        this.mLowBatteryTurnOffTv.setVisibility(8);
        updateText();
        this.mCloseBtn.setOnClickListener(this);
    }

    public void updateText() {
        this.mLowBatteryTv.setText(Strings.getString(C0413R.string.Device_Battery_Lower, this.mContext));
        this.mCloseBtn.setText(Strings.getString(C0413R.string.Device_Battery_Close, this.mContext));
    }

    @Override
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        Window window = getWindow();
        if (!z || window == null) {
            return;
        }
        View decorView = window.getDecorView();
        if (decorView.getHeight() == 0 || decorView.getWidth() == 0) {
            decorView.requestLayout();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ((Activity) this.mContext).finish();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() != C0413R.id.low_battery_close) {
            return;
        }
        dismiss();
    }
}
