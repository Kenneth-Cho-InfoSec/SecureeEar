package com.i4season.bkCamera.uirelated.other.dialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import com.i4season.bkCamera.uirelated.other.Language.Strings;
import com.i4season.i4season_camera.C0413R;

public class GeneralDialog extends BaseDialog implements View.OnClickListener {
    public static final int BUTTON_CLICK_CANCEL = 201;
    public static final int BUTTON_CLICK_OK = 205;
    private BaseButtonDialog mBtnOkOrCancel;
    private TextView mDialogTitle;
    private Handler mHandler;
    private int mTitleId;

    public GeneralDialog(Context context) {
        super(context);
    }

    public GeneralDialog(Context context, Handler handler, int i) {
        super(context);
        this.mHandler = handler;
        this.mTitleId = i;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0413R.layout.dialog_universal_layout);
        initView();
        initListener();
    }

    private void initView() {
        this.mDialogTitle = (TextView) findViewById(C0413R.id.dialog_universal_title);
        this.mDialogTitle.setText(Strings.getString(this.mTitleId, this.mContext));
        this.mBtnOkOrCancel = (BaseButtonDialog) findViewById(C0413R.id.dialog_universal_button);
    }

    private void initListener() {
        this.mBtnOkOrCancel.setBtnOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == this.mBtnOkOrCancel.getBaseDialogCancelbtn()) {
            dismiss();
            return;
        }
        if (view.getId() == this.mBtnOkOrCancel.getBaseDialogOKbtn()) {
            dismiss();
            Handler handler = this.mHandler;
            if (handler != null) {
                handler.sendEmptyMessage(205);
            }
        }
    }

    public void setButtonText(int i, int i2) {
        this.mBtnOkOrCancel.setButtonText(Strings.getString(i, this.mContext), Strings.getString(i2, this.mContext));
    }
}
