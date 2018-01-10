package com.example.dxc.xfdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.dxc.xfdemo.common.BaseActivity;

/**
 * Created by wahaitao on 12/21/2017.
 */

public class FaceRequestActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentLayout(R.layout.activity_facerequest);
        setTitle("人脸识别");
        setSettingVisible(false,"");
    }

    @Override
    public void onSettingClick() {

    }
}
