package com.i4season.bkCamera.uirelated.other.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.i4season.i4season_camera.C0413R;

public class CenterProgressDialog extends Dialog {
    private TextView contentInfoTv;
    private ProgressBar loadingView;
    private Context mContext;

    public CenterProgressDialog(Context context) {
        super(context, C0413R.style.progressDialog);
        initView(context, null);
    }

    public CenterProgressDialog(Context context, String str) {
        super(context, C0413R.style.progressDialog);
        initView(context, str);
    }

    private void initView(Context context, String str) {
        this.mContext = context;
        View viewInflate = LayoutInflater.from(context).inflate(C0413R.layout.dialog_loading_view, (ViewGroup) null);
        setContentView(viewInflate);
        this.loadingView = (ProgressBar) viewInflate.findViewById(C0413R.id.progress_loading);
        this.contentInfoTv = (TextView) viewInflate.findViewById(C0413R.id.progress_txt);
        if (str != null && !str.equals("")) {
            this.contentInfoTv.setVisibility(0);
            this.contentInfoTv.setText(str);
        } else {
            this.contentInfoTv.setVisibility(8);
        }
    }

    public void setTextColor(int i) {
        TextView textView = this.contentInfoTv;
        if (textView != null) {
            textView.setTextColor(this.mContext.getResources().getColor(i));
        }
    }
}
