package com.example.dxc.xfdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity implements View.OnClickListener{

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
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()){
            case R.id.bt_yysb:
                intent = new Intent(this,RecognizerActivity.class);
                break;
            case R.id.bt_yybb:
                intent = new Intent(this,SynthesizerActivity.class);
                break;
            case R.id.bt_face:
                intent = new Intent(this,FaceRequestActivity.class);
                break;
            case R.id.bt_swsb:
                intent = new Intent(this,SpeakerVerifierActivity.class);
                break;
                default:
        }
        startActivity(intent);
    }
}
