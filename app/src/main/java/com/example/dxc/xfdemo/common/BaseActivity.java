package com.example.dxc.xfdemo.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dxc.xfdemo.R;

/**
 * Created by wahaitao on 12/25/2017.
 */

public abstract class BaseActivity extends Activity {

    protected TextView tvBack, tvSetting, tvTitle;
    protected LinearLayout llBack;
    protected FrameLayout base_content;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_base);

        tvBack = (TextView) findViewById(R.id.tv_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvSetting = (TextView) findViewById(R.id.tv_setting);
        base_content = (FrameLayout) findViewById(R.id.base_content);
        llBack = (LinearLayout) findViewById(R.id.ll_back);

        if (tvBack != null) {
            llBack.setOnClickListener(clickListener);
        }
        if (tvSetting != null) {
            tvSetting.setOnClickListener(clickListener);
        }
    }

    protected void setTitle(String title) {
        if (title != null) {
            tvTitle.setText(title);
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_back:
                    onBackPressed();
                    break;
                case R.id.tv_setting:
                    onSettingClick();
                    break;
                default:
            }
        }
    };

    protected void setSettingVisible(boolean isVisible, String settingText) {
        if (isVisible) {
            tvSetting.setVisibility(View.VISIBLE);
        } else {
            tvSetting.setVisibility(View.GONE);
            tvSetting.setText("");
        }

        if (settingText != null) {
            tvSetting.setText(settingText);
        } else {
            tvSetting.setText("设置");
        }
    }

    protected void setBackVisible(boolean isVisible, String backText) {
        if (isVisible) {
            llBack.setVisibility(View.VISIBLE);
        } else {
            llBack.setVisibility(View.GONE);
            tvBack.setText("");
        }

        if (backText != null) {
            tvSetting.setText(backText);
        } else {
            tvSetting.setText("返回");
        }
    }

    protected void setSettingClickable(boolean isClickable) {
        tvSetting.setClickable(isClickable);
    }

    protected void setBaseContentLayout(int layoutResId) {
        base_content.removeAllViews();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(layoutResId, null);
        base_content.addView(v);
    }

    public abstract void onSettingClick();

    protected void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("请稍候...");
        progressDialog.show();
    }

    protected void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    /**
     * 打印日志
     * @param tag 标志
     * @param msg 信息
     */
    protected void Log(String tag, String msg) {
        Log.e(tag, msg);
    }
}
