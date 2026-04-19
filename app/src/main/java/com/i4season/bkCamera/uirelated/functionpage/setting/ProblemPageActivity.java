package com.i4season.bkCamera.uirelated.functionpage.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.i4season.bkCamera.uirelated.other.Language.Strings;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.SystemUtil;
import com.i4season.i4season_camera.C0413R;

public class ProblemPageActivity extends AppCompatActivity implements View.OnClickListener {
    protected TextView mA1;
    protected TextView mA2;
    protected TextView mA3;
    protected TextView mA4;
    protected TextView mA5;
    protected TextView mA6;
    protected LinearLayout mAccessoriesContent;
    protected ImageView mAccessoriesIv;
    protected LinearLayout mAccessoriesLl;
    protected TextView mAccessoriesTv;
    protected ImageView mBack;
    protected LinearLayout mOtherContent;
    protected ImageView mOtherIv;
    protected LinearLayout mOtherLl;
    protected TextView mOtherTv;
    protected LinearLayout mPickearsstickContent;
    protected ImageView mPickearsstickIv;
    protected LinearLayout mPickearsstickLl;
    protected TextView mPickearsstickTv;
    protected TextView mQ1;
    protected TextView mQ2;
    protected TextView mQ3;
    protected TextView mQ4;
    protected TextView mQ5;
    protected TextView mQ6;
    protected TextView mTitle;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0413R.layout.activity_problem_page);
        SystemUtil.setTransparent(this);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        this.mTitle = (TextView) findViewById(C0413R.id.app_topbar_center_text);
        this.mBack = (ImageView) findViewById(C0413R.id.app_topbar_left_image);
        this.mPickearsstickLl = (LinearLayout) findViewById(C0413R.id.problem_pickearsstick_ll);
        this.mPickearsstickTv = (TextView) findViewById(C0413R.id.problem_pickearsstick_tv);
        this.mPickearsstickIv = (ImageView) findViewById(C0413R.id.problem_pickearsstick_iv);
        this.mPickearsstickContent = (LinearLayout) findViewById(C0413R.id.problem_pickearsstick_content);
        this.mAccessoriesLl = (LinearLayout) findViewById(C0413R.id.problem_accessories_ll);
        this.mAccessoriesTv = (TextView) findViewById(C0413R.id.problem_accessories_tv);
        this.mAccessoriesIv = (ImageView) findViewById(C0413R.id.problem_accessories_iv);
        this.mAccessoriesContent = (LinearLayout) findViewById(C0413R.id.problem_accessories_content);
        this.mOtherLl = (LinearLayout) findViewById(C0413R.id.problem_other_ll);
        this.mOtherTv = (TextView) findViewById(C0413R.id.problem_other_tv);
        this.mOtherIv = (ImageView) findViewById(C0413R.id.problem_other_iv);
        this.mOtherContent = (LinearLayout) findViewById(C0413R.id.problem_other_content);
        this.mQ1 = (TextView) findViewById(C0413R.id.problem_q1);
        this.mA1 = (TextView) findViewById(C0413R.id.problem_a1);
        this.mQ2 = (TextView) findViewById(C0413R.id.problem_q2);
        this.mA2 = (TextView) findViewById(C0413R.id.problem_a2);
        this.mQ3 = (TextView) findViewById(C0413R.id.problem_q3);
        this.mA3 = (TextView) findViewById(C0413R.id.problem_a3);
        this.mQ4 = (TextView) findViewById(C0413R.id.problem_q4);
        this.mA4 = (TextView) findViewById(C0413R.id.problem_a4);
        this.mQ5 = (TextView) findViewById(C0413R.id.problem_q5);
        this.mA5 = (TextView) findViewById(C0413R.id.problem_a5);
        this.mQ6 = (TextView) findViewById(C0413R.id.problem_q6);
        this.mA6 = (TextView) findViewById(C0413R.id.problem_a6);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initData() {
        this.mTitle.setTextColor(getResources().getColor(C0413R.color.appwhite));
        this.mTitle.setVisibility(0);
        this.mTitle.setText(Strings.getString(C0413R.string.App_Setting_Problem, this));
        this.mBack.setImageResource(C0413R.drawable.ic_app_back_white);
        this.mBack.setVisibility(0);
        this.mQ1.setText(Strings.getString(C0413R.string.App_Problem_Question1, this));
        this.mA1.setText(Strings.getString(C0413R.string.App_Problem_Answer1, this));
        this.mQ2.setText(Strings.getString(C0413R.string.App_Problem_Question2, this));
        this.mA2.setText(Strings.getString(C0413R.string.App_Problem_Answer2, this));
        this.mQ3.setText(Strings.getString(C0413R.string.App_Problem_Question3, this));
        this.mA3.setText(Strings.getString(C0413R.string.App_Problem_Answer3, this));
        this.mQ4.setText(Strings.getString(C0413R.string.App_Problem_Question4, this));
        this.mA4.setText(Strings.getString(C0413R.string.App_Problem_Answer4, this));
        this.mQ5.setText(Strings.getString(C0413R.string.App_Problem_Question5, this));
        this.mA5.setText(Strings.getString(C0413R.string.App_Problem_Answer5, this));
        this.mQ6.setText(Strings.getString(C0413R.string.App_Problem_Question6, this));
        this.mA6.setText(Strings.getString(C0413R.string.App_Problem_Answer6, this));
    }

    private void initListener() {
        this.mBack.setOnClickListener(this);
        this.mPickearsstickLl.setOnClickListener(this);
        this.mAccessoriesLl.setOnClickListener(this);
        this.mOtherLl.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case C0413R.id.app_topbar_left_image:
                finish();
                break;
            case C0413R.id.problem_accessories_ll:
                this.mPickearsstickContent.setVisibility(8);
                this.mAccessoriesContent.setVisibility(0);
                this.mOtherContent.setVisibility(8);
                break;
            case C0413R.id.problem_other_ll:
                this.mPickearsstickContent.setVisibility(8);
                this.mAccessoriesContent.setVisibility(8);
                this.mOtherContent.setVisibility(0);
                break;
            case C0413R.id.problem_pickearsstick_ll:
                this.mPickearsstickContent.setVisibility(0);
                this.mAccessoriesContent.setVisibility(8);
                this.mOtherContent.setVisibility(8);
                break;
        }
    }
}
