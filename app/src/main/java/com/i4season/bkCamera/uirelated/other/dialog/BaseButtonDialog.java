package com.i4season.bkCamera.uirelated.other.dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.i4season.bkCamera.uirelated.other.Language.Strings;
import com.i4season.i4season_camera.C0413R;

public class BaseButtonDialog extends LinearLayout implements View.OnClickListener {
    private TextView cancelBtn;
    private LayoutInflater inflater;
    private TextView okBtn;

    public int getBaseDialogCancelbtn() {
        return C0413R.id.basedialog_cancelbtn;
    }

    public int getBaseDialogOKbtn() {
        return C0413R.id.basedialog_okbtn;
    }

    public int getCancelButtonId() {
        return C0413R.id.basedialog_cancelbtn;
    }

    public int getOkButtonId() {
        return C0413R.id.basedialog_okbtn;
    }

    @Override
    public void onClick(View view) {
    }

    public BaseButtonDialog(Context context) {
        super(context);
    }

    public BaseButtonDialog(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.inflater.inflate(C0413R.layout.base_dialog, this);
        this.cancelBtn = (TextView) findViewById(C0413R.id.basedialog_cancelbtn);
        this.okBtn = (TextView) findViewById(C0413R.id.basedialog_okbtn);
        initUI();
    }

    public TextView getOkButton() {
        return this.okBtn;
    }

    public void setViewText() {
        TextView textView = this.cancelBtn;
        textView.setText(Strings.getString(C0413R.string.App_Button_Cancel, textView.getContext()));
        TextView textView2 = this.okBtn;
        textView2.setText(Strings.getString(C0413R.string.App_Button_OK, textView2.getContext()));
    }

    private void initUI() {
        setViewText();
        this.okBtn.setOnClickListener(this);
        this.cancelBtn.setOnClickListener(this);
    }

    public void setBtnOnClickListener(View.OnClickListener onClickListener) {
        this.okBtn.setOnClickListener(onClickListener);
        this.cancelBtn.setOnClickListener(onClickListener);
    }

    public void setBtnOkValid(boolean z) {
        this.okBtn.setEnabled(z);
    }

    public TextView getCancelBtn() {
        return this.cancelBtn;
    }

    public void setButtonText(String str, String str2) {
        this.okBtn.setText(str);
        this.cancelBtn.setText(str2);
    }
}
