package com.i4season.bkCamera.uirelated.other.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;
import com.i4season.bkCamera.uirelated.other.FunctionSwitch;
import com.i4season.bkCamera.uirelated.other.Language.Strings;
import com.i4season.bkCamera.uirelated.other.MainFrameHandleInstance;
import com.i4season.i4season_camera.C0413R;

public class UserNoticeDialog extends BaseDialog implements View.OnClickListener {
    public static final int USER_NOTICE_DIALOG_DISMISS = 125;
    ClickableSpan clickableSpan;
    ClickableSpan clickableSpan2;
    private TextView mContent;
    private TextView mExitbt;
    private Handler mHandler;
    private TextView mOkbt;
    private TextView mTitle;
    private TextView mUserNoticeTitle;

    public UserNoticeDialog(Context context, Handler handler) {
        super(context);
        this.clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                UserNoticeDialog.this.openPrivacyPolicy();
            }

            @Override
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setUnderlineText(false);
            }
        };
        this.clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                UserNoticeDialog.this.openTermsOfService();
            }

            @Override
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setUnderlineText(false);
            }
        };
        this.mHandler = handler;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        String str;
        super.onCreate(bundle);
        setContentView(C0413R.layout.dialog_user_notice);
        setCancelable(false);
        this.mTitle = (TextView) findViewById(C0413R.id.user_notice_title);
        this.mTitle.setText(Strings.getString(C0413R.string.App_Representations_And_Terms, this.mContext));
        this.mContent = (TextView) findViewById(C0413R.id.user_notice_content);
        this.mContent.setText(Strings.getString(C0413R.string.App_Using_content, this.mContext));
        this.mOkbt = (TextView) findViewById(C0413R.id.user_notice_bt);
        this.mOkbt.setText(Strings.getString(C0413R.string.App_Agree_And_Continue, this.mContext));
        this.mOkbt.setOnClickListener(this);
        this.mExitbt = (TextView) findViewById(C0413R.id.exit_bt);
        this.mExitbt.setText(Strings.LANGUAGE_SCHINESE.equals(Strings.getLanguage()) ? "退出" : "Exit");
        this.mExitbt.setOnClickListener(this);
        this.mUserNoticeTitle = (TextView) findViewById(C0413R.id.user_notice_prompt);
        if (Strings.LANGUAGE_SCHINESE.equals(Strings.getLanguage())) {
            str = "请阅读我们的<<隐私政策>>。点击“同意并继续”接受<<用户协议>>";
        } else {
            str = Strings.LANGUAGE_ENGLISH.equals(Strings.getLanguage()) ? "Read our <<Privacy Policy>>. Tap \"Agree and continue\" to accept the <<User agreement>>." : "Read our <<Privacy Policy>>. Tap \"Agree and continue\" to accept the <<User agreement>.";
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append((CharSequence) str);
        setPrivacyPolicyText(spannableStringBuilder, str);
        setTermsOfServiceText(spannableStringBuilder, str);
        this.mUserNoticeTitle.setMovementMethod(LinkMovementMethod.getInstance());
        this.mUserNoticeTitle.setText(spannableStringBuilder);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == C0413R.id.user_notice_bt) {
            dismiss();
        } else if (view.getId() == C0413R.id.exit_bt) {
            dismiss();
            ((Activity) this.mContext).finish();
            ((Activity) MainFrameHandleInstance.getInstance().getmCurrentContext()).finish();
            Process.killProcess(Process.myPid());
        }
    }

    private void setPrivacyPolicyText(SpannableStringBuilder spannableStringBuilder, String str) {
        int iIndexOf = str.indexOf("<");
        int iIndexOf2 = str.indexOf(">") + 2;
        spannableStringBuilder.setSpan(this.clickableSpan, iIndexOf, iIndexOf2, 33);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#1C80EB")), iIndexOf, iIndexOf2, 33);
        spannableStringBuilder.setSpan(new BackgroundColorSpan(Color.parseColor("#ffffff")), iIndexOf, iIndexOf2, 33);
    }

    private void setTermsOfServiceText(SpannableStringBuilder spannableStringBuilder, String str) {
        int iLastIndexOf = str.lastIndexOf("<") - 1;
        int iLastIndexOf2 = str.lastIndexOf(">") + 1;
        spannableStringBuilder.setSpan(this.clickableSpan2, iLastIndexOf, iLastIndexOf2, 33);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#1C80EB")), iLastIndexOf, iLastIndexOf2, 33);
        spannableStringBuilder.setSpan(new BackgroundColorSpan(Color.parseColor("#ffffff")), iLastIndexOf, iLastIndexOf2, 33);
    }

    public void openPrivacyPolicy() {
        MainFrameHandleInstance.getInstance().showWsLoadWebActivity(getContext(), false, Strings.LANGUAGE_SCHINESE.equals(Strings.getLanguage()) ? FunctionSwitch.privacyContentURL : FunctionSwitch.privacyContentURL_en, true);
    }

    public void openTermsOfService() {
        MainFrameHandleInstance.getInstance().showWsLoadWebActivity(getContext(), false, Strings.LANGUAGE_SCHINESE.equals(Strings.getLanguage()) ? FunctionSwitch.termsOfServiceURL_cn : FunctionSwitch.termsOfServiceURL_en, false);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        this.mHandler.sendEmptyMessage(125);
    }
}
