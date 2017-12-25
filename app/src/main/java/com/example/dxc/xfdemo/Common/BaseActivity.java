package com.example.dxc.xfdemo.Common;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.dxc.xfdemo.R;

/**
 * Created by wahaitao on 12/25/2017.
 */

public abstract class BaseActivity extends Activity{

    protected TextView tvBack,tvSetting,tvTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏

        tvBack = (TextView) findViewById(R.id.tv_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvSetting = (TextView) findViewById(R.id.tv_setting);

        if (tvBack != null){
            tvBack.setOnClickListener(clickListener);
        }
        if (tvSetting != null){
            tvSetting.setOnClickListener(clickListener);
        }
    }

    protected void setTitle(String title){
        if (title != null){
        tvTitle.setText(title);
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_back:
                    onBackPressed();
                    break;
                case R.id.tv_setting:
                    onSettingClick();
                    break;
                default:
            }
        }
    };

    protected void setSettingVisible(boolean isVisible,String settingText){
        if (isVisible){
            tvSetting.setVisibility(View.VISIBLE);
        }else {
            tvSetting.setVisibility(View.GONE);
            tvSetting.setText("");
        }

        if (settingText != null){
            tvSetting.setText(settingText);
        }else {
            tvSetting.setText("设置");
        }
    }

    public abstract void onSettingClick();
}
