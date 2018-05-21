package com.example.dxc.xfdemo;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dxc.xfdemo.common.BaseActivity;

/**
 * @Class:
 * @Description:
 * @Author: haitaow(haitaow@hpe.com)
 * @Date: 3/1/2018-9:58 AM.
 * @Version 1.0
 */

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class FingerVerifierActivity extends BaseActivity {
    private ImageView ivFinger;
    private TextView tvFinger;
    FingerprintManager fingerManager;
    KeyguardManager keyguardManager;
    private final static int REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS = 0;
    public final static String TAG = "finger_log";
    public boolean flag = true;
    private Button btFingerVerifier;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentLayout(R.layout.activity_finger_verifier);
        setTitle("指纹识别");
        setSettingVisible(false, "");
        setBackVisible(true, "返回");

        Intent intent = getIntent();
        flag = intent.getBooleanExtra(TAG, true);
        initView();
        startFingerListener(null);
        Log("1","1");
    }

    private void startFingerListener(FingerprintManager.CryptoObject cryptoObject) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //权限还没有授予，需要在这里写申请权限的代码
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.USE_FINGERPRINT}, 60);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fingerManager.authenticate(cryptoObject, cancellationSignal, 0, authenticationCallback, null);
        }
    }

    CancellationSignal cancellationSignal = new CancellationSignal();
    FingerprintManager.AuthenticationCallback authenticationCallback = new FingerprintManager.AuthenticationCallback() {
        @Override
        public void onAuthenticationError(int errorCode, CharSequence errString) {
            Toast.makeText(FingerVerifierActivity.this, "指纹验证失败,请输入锁屏密码启用指纹识别！", Toast.LENGTH_SHORT).show();
            showAuthenticationScreen();
        }

        @Override
        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
            Toast.makeText(FingerVerifierActivity.this, helpString, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
            Toast.makeText(FingerVerifierActivity.this, "指纹识别成功", Toast.LENGTH_SHORT).show();
            if (!flag) {
                Intent intent = new Intent(FingerVerifierActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }

        @Override
        public void onAuthenticationFailed() {
            Toast.makeText(FingerVerifierActivity.this, "指纹识别失败", Toast.LENGTH_SHORT).show();
        }
    };

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void showAuthenticationScreen() {
        Intent intent = keyguardManager.createConfirmDeviceCredentialIntent("finger", "测试指纹识别");
        if (intent != null) {
            startActivityForResult(intent, REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initView() {
        ivFinger = (ImageView) findViewById(R.id.iv_finger);
        tvFinger = (TextView) findViewById(R.id.tv_finger);
        btFingerVerifier = (Button) findViewById(R.id.bt_finger_verifier);
        fingerManager = (FingerprintManager) this.getSystemService(Context.FINGERPRINT_SERVICE);
        keyguardManager = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);

        if (!flag){//如果为登录方式初始化，button设置为影藏
            btFingerVerifier.setVisibility(View.GONE);
            setBackVisible(false,"");
            setTitle("指纹识别登录");
        }
        btFingerVerifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFingerListener(null);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS) {
            if (requestCode == RESULT_OK && !flag) {
                Toast.makeText(this, "识别成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FingerVerifierActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "识别失败", Toast.LENGTH_SHORT).show();
                startFingerListener(null);
            }
        }
    }

    @Override
    public void onSettingClick() {

    }
}