package com.i4season.bkCamera.uirelated.other.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Process;
import android.view.View;
import android.widget.TextView;
import com.i4season.bkCamera.uirelated.other.Language.Strings;
import com.i4season.bkCamera.uirelated.other.MainFrameHandleInstance;
import com.i4season.i4season_camera.C0413R;

public class AppExitDialog extends BaseDialog implements View.OnClickListener {
    private TextView mAppExitCancel;
    private TextView mAppExitContent;
    private TextView mAppExitOk;
    private TextView mAppExitTitle;

    public AppExitDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0413R.layout.dialog_app_exit_file);
        initView();
        initListener();
    }

    private void initView() {
        this.mAppExitTitle = (TextView) findViewById(C0413R.id.dialog_app_exit_title);
        this.mAppExitContent = (TextView) findViewById(C0413R.id.dialog_app_exit_content);
        this.mAppExitCancel = (TextView) findViewById(C0413R.id.basedialog_cancelbtn);
        this.mAppExitOk = (TextView) findViewById(C0413R.id.basedialog_okbtn);
        this.mAppExitTitle.setText(Strings.getString(C0413R.string.App_Exit_Title, this.mContext));
        this.mAppExitContent.setText(Strings.getString(C0413R.string.App_Exit_Title_Content, this.mContext));
        this.mAppExitCancel.setText(Strings.getString(C0413R.string.App_Button_Cancel, this.mContext));
        this.mAppExitOk.setText(Strings.getString(C0413R.string.App_Button_OK, this.mContext));
    }

    private void initListener() {
        this.mAppExitCancel.setOnClickListener(this);
        this.mAppExitOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == C0413R.id.basedialog_cancelbtn) {
            dismiss();
            return;
        }
        if (id != C0413R.id.basedialog_okbtn) {
            return;
        }
        dismiss();
        try {
            Context context = MainFrameHandleInstance.getInstance().getmCurrentContext();
            if (context != null) {
                ((Activity) context).finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Process.killProcess(Process.myPid());
    }
}
