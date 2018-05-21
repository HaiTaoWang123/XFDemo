package com.example.dxc.xfdemo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.dxc.xfdemo.common.BaseActivity;

/**
 * Created by haitaow on 1/23/2018-7:13 PM.
 * Email: haitaow@hpe.com
 * version 1.0
 */

public class SelectLoginStyleActivity extends BaseActivity {
    private int checkedSytle = 0;
    private Context context;
    private final int PERMISSION_REQUEST = 0x00;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentLayout(R.layout.activity_login_style_select);
        setSettingVisible(false, "");
        setBackVisible(false, "");
        setTitle("选择登录方式");
        context = SelectLoginStyleActivity.this;

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rg_login_style);
        RadioButton rbId = (RadioButton) findViewById(R.id.rb_id_login);
        RadioButton rbFace = (RadioButton) findViewById(R.id.rb_face_login);
        RadioButton rbFinger = (RadioButton) findViewById(R.id.rb_finger_login);
        if (Build.VERSION.SDK_INT < 23) {
            rbFinger.setVisibility(View.GONE);
        }
        rbId.setChecked(true);
        checkedSytle = 1;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.USE_FINGERPRINT,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.INTERNET,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.SEND_SMS},PERMISSION_REQUEST);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_id_login) {
                    checkedSytle = 1;
                } else if (checkedId == R.id.rb_face_login) {
                    checkedSytle = 2;
                }else if (checkedId == R.id.rb_finger_login) {
                    checkedSytle = 3;
                }
            }
        });

        findViewById(R.id.bt_next_step).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedSytle == 1) {
                    Intent intent = new Intent(SelectLoginStyleActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else if (checkedSytle == 2) {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        //权限还没有授予，需要在这里写申请权限的代码
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 60);
                    }

                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        //权限还没有授予，需要在这里写申请权限的代码
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 60);
                    }
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        //权限还没有授予，需要在这里写申请权限的代码
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{Manifest.permission.CAMERA}, 60);
                    } else {
                        Intent intent = new Intent(SelectLoginStyleActivity.this, FaceRequestActivity.class);
                        intent.putExtra(FaceRequestActivity.M_Type, 0);
                        startActivity(intent);
                        finish();
                    }
                }else if(checkedSytle == 3){
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                        //权限还没有授予，需要在这里写申请权限的代码
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{Manifest.permission.USE_FINGERPRINT}, 60);
                    } else {
                        Intent intent = new Intent(SelectLoginStyleActivity.this, FingerVerifierActivity.class);
                        intent.putExtra(FingerVerifierActivity.TAG,false);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(SelectLoginStyleActivity.this, "请选择登录认证方式！", Toast.LENGTH_SHORT).show();
                }

                SharedPreferences sp = getSharedPreferences(SplashActivity.sp_Name, Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt(SplashActivity.is_Login_Style_Selected, checkedSytle);
                editor.commit();
            }
        });
    }

    @Override
    public void onSettingClick() {

    }
}
