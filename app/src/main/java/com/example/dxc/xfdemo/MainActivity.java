package com.example.dxc.xfdemo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

public class MainActivity extends Activity implements View.OnClickListener {
//    private Button btSpeechRecognizer,btSpeechSynthesizer,btFaceRequest,btSpeakerVerifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        btSpeechRecognizer = findViewById(R.id.bt_yysb) ;
//        btSpeechSynthesizer = findViewById(R.id.bt_yybb) ;
//        btFaceRequest = findViewById(R.id.bt_face) ;
//        btSpeakerVerifier = findViewById(R.id.bt_swsb) ;

        findViewById(R.id.bt_yysb).setOnClickListener(this);
        findViewById(R.id.bt_yybb).setOnClickListener(this);
        findViewById(R.id.bt_face).setOnClickListener(this);
        findViewById(R.id.bt_swsb).setOnClickListener(this);
        findViewById(R.id.bt_datastructure).setOnClickListener(this);
        findViewById(R.id.bt_scanner).setOnClickListener(this);
        findViewById(R.id.bt_download).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.bt_yysb:
                intent = new Intent(this, RecognizerActivity.class);
                break;
            case R.id.bt_yybb:
                intent = new Intent(this, SynthesizerActivity.class);
                break;
            case R.id.bt_face:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //权限还没有授予，需要在这里写申请权限的代码
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 60);
                }

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //权限还没有授予，需要在这里写申请权限的代码
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 60);
                }
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //权限还没有授予，需要在这里写申请权限的代码
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA}, 60);
                }
                intent = new Intent(this, FaceRequestActivity.class);
                intent.putExtra(FaceRequestActivity.M_Type,1);
                break;
            case R.id.bt_swsb:
                intent = new Intent(this, SpeakerVerifierActivity.class);
                break;
            case R.id.bt_datastructure:
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    //权限还没有授予，需要在这里写申请权限的代码
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CAMERA}, 60);
                } else {
                    intent = new Intent(this, FaceRequestActivity.class);
                }
                break;
            case R.id.bt_scanner:
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    //权限还没有授予，需要在这里写申请权限的代码
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CAMERA}, 60);
                } else {
                    intent = new Intent(this, ScannerTestActivity.class);
                }
                break;
            case R.id.bt_download:
                intent = new Intent(this,FileDownloadTest.class);
                break;
            default:
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}