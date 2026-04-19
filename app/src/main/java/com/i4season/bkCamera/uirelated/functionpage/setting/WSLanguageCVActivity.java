package com.i4season.bkCamera.uirelated.functionpage.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.i4season.bkCamera.uirelated.functionpage.setting.adapter.WSChooseAdapter;
import com.i4season.bkCamera.uirelated.functionpage.setting.bean.WSChooseBean;
import com.i4season.bkCamera.uirelated.other.Language.LanguageInfo;
import com.i4season.bkCamera.uirelated.other.Language.Strings;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.NotifyCode;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.SystemUtil;
import com.i4season.i4season_camera.C0413R;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WSLanguageCVActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    protected ImageView mBack;
    protected ListView mSettingListview;
    protected TextView mTitle;
    protected LinearLayout mTopBar;
    private WSChooseAdapter wsChooseAdapter;
    private final String language_english = "English";
    private final String language_schinese = "简体中文";
    private final String language_tchinese = "繁体中文";
    private final String language_french = "Français";
    private final String language_russian = "Pусский";
    private final String language_german = "Deutsch";
    private final String language_japanese = "日本語";
    private final String language_korean = "한국어";
    private final String language_portuguese = "Português";
    private final String language_spanish = "Español";
    private final String language_italian = "Italiano";
    private final String language_dutch = "Nederlands";
    private final String language_Polish = "Polski";
    private List<WSChooseBean> mWsChooseBeans = new ArrayList();
    public String mSelectLanduageCode = "";

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0413R.layout.activity_language);
        SystemUtil.setTransparent(this);
        initData();
        initView();
        initListener();
    }

    private void initData() {
        this.mSelectLanduageCode = LanguageInfo.getInstance().getSetLanguage(this);
        if (this.mSelectLanduageCode.equals(Strings.LANGUAGE_AUTO)) {
            this.mSelectLanduageCode = Strings.getLanguage();
        }
        getLanguageBeans();
    }

    private void initView() {
        this.mTopBar = (LinearLayout) findViewById(C0413R.id.lanauge_topbar);
        this.mTitle = (TextView) findViewById(C0413R.id.app_topbar_center_text);
        this.mBack = (ImageView) findViewById(C0413R.id.app_topbar_left_image);
        this.mTitle.setVisibility(0);
        this.mTitle.setText(Strings.getString(C0413R.string.App_User_Language, this));
        this.mBack.setImageResource(C0413R.drawable.ic_app_back_black);
        this.mBack.setVisibility(0);
        this.mSettingListview = (ListView) findViewById(C0413R.id.setting_listview);
        this.wsChooseAdapter = new WSChooseAdapter(this, this.mWsChooseBeans, null);
        this.mSettingListview.setAdapter((ListAdapter) this.wsChooseAdapter);
    }

    private void initListener() {
        this.mBack.setOnClickListener(this);
        this.mSettingListview.setOnItemClickListener(this);
    }

    public void getLanguageBeans() {
        this.mWsChooseBeans.clear();
        WSChooseBean wSChooseBean = new WSChooseBean();
        wSChooseBean.setWSChTitle("简体中文");
        wSChooseBean.setWSChInfo(Strings.LANGUAGE_SCHINESE);
        if (this.mSelectLanduageCode.equals(wSChooseBean.getWSChInfo())) {
            wSChooseBean.setChSelect(true);
        }
        this.mWsChooseBeans.add(wSChooseBean);
        WSChooseBean wSChooseBean2 = new WSChooseBean();
        wSChooseBean2.setWSChTitle("English");
        wSChooseBean2.setWSChInfo(Strings.LANGUAGE_ENGLISH);
        if (this.mSelectLanduageCode.equals(wSChooseBean2.getWSChInfo())) {
            wSChooseBean2.setChSelect(true);
        }
        this.mWsChooseBeans.add(wSChooseBean2);
        WSChooseBean wSChooseBean3 = new WSChooseBean();
        wSChooseBean3.setWSChTitle("Deutsch");
        wSChooseBean3.setWSChInfo(Strings.LANGUAGE_GERMAN);
        if (this.mSelectLanduageCode.equals(wSChooseBean3.getWSChInfo())) {
            wSChooseBean3.setChSelect(true);
        }
        this.mWsChooseBeans.add(wSChooseBean3);
        WSChooseBean wSChooseBean4 = new WSChooseBean();
        wSChooseBean4.setWSChTitle("日本語");
        wSChooseBean4.setWSChInfo(Strings.LANGUAGE_JANPANESE);
        if (this.mSelectLanduageCode.equals(wSChooseBean4.getWSChInfo())) {
            wSChooseBean4.setChSelect(true);
        }
        this.mWsChooseBeans.add(wSChooseBean4);
        WSChooseBean wSChooseBean5 = new WSChooseBean();
        wSChooseBean5.setWSChTitle("Español");
        wSChooseBean5.setWSChInfo(Strings.LANGUAGE_SPANISH);
        if (this.mSelectLanduageCode.equals(wSChooseBean5.getWSChInfo())) {
            wSChooseBean5.setChSelect(true);
        }
        this.mWsChooseBeans.add(wSChooseBean5);
        WSChooseBean wSChooseBean6 = new WSChooseBean();
        wSChooseBean6.setWSChTitle("Français");
        wSChooseBean6.setWSChInfo(Strings.LANGUAGE_FRENCH);
        if (this.mSelectLanduageCode.equals(wSChooseBean6.getWSChInfo())) {
            wSChooseBean6.setChSelect(true);
        }
        this.mWsChooseBeans.add(wSChooseBean6);
        WSChooseBean wSChooseBean7 = new WSChooseBean();
        wSChooseBean7.setWSChTitle("Italiano");
        wSChooseBean7.setWSChInfo(Strings.LANGUAGE_ITALIAN);
        if (this.mSelectLanduageCode.equals(wSChooseBean7.getWSChInfo())) {
            wSChooseBean7.setChSelect(true);
        }
        this.mWsChooseBeans.add(wSChooseBean7);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        allnotSelect();
        WSChooseBean wSChooseBean = (WSChooseBean) adapterView.getItemAtPosition(i);
        wSChooseBean.setChSelect(true);
        this.mSelectLanduageCode = wSChooseBean.getWSChInfo();
        this.wsChooseAdapter.notifyDataSetChanged();
        saveChooseLanguage();
    }

    private void allnotSelect() {
        Iterator<WSChooseBean> it = this.mWsChooseBeans.iterator();
        while (it.hasNext()) {
            it.next().setChSelect(false);
        }
    }

    private void saveChooseLanguage() {
        updateLanguage(this.mSelectLanduageCode);
        LanguageInfo.getInstance().setLanguage(this.mSelectLanduageCode);
        this.mTitle.setText(Strings.getString(C0413R.string.App_User_Language, this));
        sendDeviceStatusChangeBoradcastNotify();
    }

    private void updateLanguage(String str) {
        Strings.setLanguage(this, str);
        LanguageInfo.isAUTO = str.equals(Strings.LANGUAGE_AUTO);
        getLanguageBeans();
    }

    public void sendDeviceStatusChangeBoradcastNotify() {
        Intent intent = new Intent();
        intent.setAction(NotifyCode.LANGUAGE_CHANGE_NOTIFY);
        sendBroadcast(intent);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() != C0413R.id.app_topbar_left_image) {
            return;
        }
        finish();
    }
}
