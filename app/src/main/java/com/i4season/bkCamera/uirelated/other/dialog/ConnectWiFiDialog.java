package com.i4season.bkCamera.uirelated.other.dialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.i4season.bkCamera.uirelated.other.Language.Strings;
import com.i4season.i4season_camera.C0413R;

public class ConnectWiFiDialog extends BaseDialog implements View.OnClickListener {
    private TextView mConnectWifiBtn;
    private ImageView mConnectWifiCancel;
    private TextView mConnectWifiTitle;

    public ConnectWiFiDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0413R.layout.dialog_connect_wifi);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        this.mConnectWifiCancel = (ImageView) findViewById(C0413R.id.connent_wifi_cancel);
        this.mConnectWifiTitle = (TextView) findViewById(C0413R.id.connent_wifi_title);
        this.mConnectWifiBtn = (TextView) findViewById(C0413R.id.connent_wifi_btn);
    }

    private void initData() {
        this.mConnectWifiTitle.setText(Strings.getString(C0413R.string.App_Connect_Wifi_Dialog, this.mContext));
        this.mConnectWifiBtn.setText(Strings.getString(C0413R.string.App_Goto_WifiSetting_Now, this.mContext));
    }

    private void initListener() {
        this.mConnectWifiCancel.setOnClickListener(this);
        this.mConnectWifiBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case C0413R.id.connent_wifi_btn:
                Intent intent = new Intent();
                intent.setAction("android.net.wifi.PICK_WIFI_NETWORK");
                this.mContext.startActivity(intent);
                dismiss();
                break;
            case C0413R.id.connent_wifi_cancel:
                dismiss();
                break;
        }
    }
}
