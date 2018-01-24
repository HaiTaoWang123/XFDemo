package com.example.dxc.xfdemo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Window;

/**
 * Created by haitaow on 1/23/2018-7:52 PM.
 * Email: haitaow@hpe.com
 * version 1.0
 */

public class SplashActivity extends Activity {
    public static final String sp_Name = "UserData";
    public static final String is_Login_Style_Selected = "isLoginStyleSelected";
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        context = SplashActivity.this;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sp = getSharedPreferences(sp_Name, Activity.MODE_PRIVATE);
                if (sp.getInt(is_Login_Style_Selected, 0) == 1) {//用户名密码登录
                    startActivity(new Intent(context, LoginActivity.class));
                } else if (sp.getInt(is_Login_Style_Selected, 0) == 2) {//人脸识别登录
                    if (ContextCompat.checkSelfPermission(SplashActivity.this,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        //权限还没有授予，需要在这里写申请权限的代码
                        ActivityCompat.requestPermissions(SplashActivity.this,
                                new String[]{Manifest.permission.CAMERA}, 60);
                    } else {
                        startActivity(new Intent(context, FaceVerifierTestActivity.class));
                    }
                } else {//否则选择登录验证方式
                    startActivity(new Intent(context, SelectLoginStyleActivity.class));
                }
                finish();
            }
        }, 800);
    }
}