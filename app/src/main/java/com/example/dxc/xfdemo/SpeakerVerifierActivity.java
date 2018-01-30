package com.example.dxc.xfdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.dxc.xfdemo.common.BaseActivity;

/**
 * Created by wahaitao on 12/21/2017.
 */

public class SpeakerVerifierActivity extends BaseActivity {
    private EditText etAuthId,etResult;
    private Button btRegist, btSearch, btDelete, btVerify,btGetPwd,btIdentity;
    private RadioGroup radioGroup;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentLayout(R.layout.activity_speakerverifier);
        setTitle("声纹识别");
        setSettingVisible(false,"");

        etAuthId = (EditText) findViewById(R.id.online_authid);
        etResult = (EditText) findViewById(R.id.online_result);
        btRegist = (Button) findViewById(R.id.bt_voiceRegist);
        btSearch = (Button) findViewById(R.id.bt_searchMdl);
        btDelete = (Button) findViewById(R.id.btn_modle_delete);
        btVerify = (Button) findViewById(R.id.online_verify);
        btGetPwd = (Button) findViewById(R.id.bt_getPwd);
        btIdentity = (Button) findViewById(R.id.btn_identity);
        radioGroup = (RadioGroup) findViewById(R.id.voice_radioGroup);

    }

    @Override
    public void onSettingClick() {

    }
}