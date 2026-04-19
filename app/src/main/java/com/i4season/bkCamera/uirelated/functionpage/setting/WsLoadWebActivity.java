package com.i4season.bkCamera.uirelated.functionpage.setting;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.i4season.bkCamera.uirelated.other.Language.Strings;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.Constant;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.SystemUtil;
import com.i4season.i4season_camera.C0413R;

public class WsLoadWebActivity extends AppCompatActivity implements View.OnClickListener {
    protected ImageView mBack;
    protected TextView mTitle;
    protected WebView mWebView;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0413R.layout.activity_load_web);
        SystemUtil.setTransparent(this);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        this.mTitle = (TextView) findViewById(C0413R.id.app_topbar_center_text);
        this.mBack = (ImageView) findViewById(C0413R.id.app_topbar_left_image);
        this.mWebView = (WebView) findViewById(C0413R.id.load_webview);
    }

    private void initData() {
        this.mTitle.setTextColor(getResources().getColor(C0413R.color.appblack));
        this.mBack.setImageResource(C0413R.drawable.ic_app_back_black);
        this.mBack.setVisibility(0);
        this.mTitle.setVisibility(0);
        Bundle extras = getIntent().getExtras();
        String string = extras.getString(Constant.WEB_HTML, "");
        if (Boolean.valueOf(extras.getBoolean(Constant.WEB_TYPE, true)).booleanValue()) {
            this.mTitle.setText(Strings.getString(C0413R.string.App_User_Privacy, this));
        } else {
            this.mTitle.setText(Strings.getString(C0413R.string.App_User_Service, this));
        }
        this.mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        this.mWebView.getSettings().setJavaScriptEnabled(true);
        this.mWebView.setWebChromeClient(new WebChromeClient());
        this.mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.mWebView.loadUrl(string);
        this.mWebView.getSettings().setDomStorageEnabled(true);
    }

    private void initListener() {
        this.mBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() != C0413R.id.app_topbar_left_image) {
            return;
        }
        finish();
    }
}
