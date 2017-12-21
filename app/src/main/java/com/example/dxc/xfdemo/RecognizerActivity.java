package com.example.dxc.xfdemo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

/**
 * Created by wahaitao on 12/21/2017.
 */

public class RecognizerActivity extends Activity implements View.OnClickListener {
    private EditText etInputContent;
    private SpeechSynthesizer mTts;
    private Context context;
    private int playPercent,bufferPercent;
    private Toast mToast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivty_synthesizer);
        context = RecognizerActivity.this;

        etInputContent = findViewById(R.id.et_content);
        findViewById(R.id.bt_start_speech).setOnClickListener(this);
        findViewById(R.id.bt_stop_speech).setOnClickListener(this);
        findViewById(R.id.bt_continue_speech).setOnClickListener(this);
        findViewById(R.id.bt_cancel_speech).setOnClickListener(this);

        if (mTts == null) {
            mTts = SpeechSynthesizer.createSynthesizer(context, null);
            mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
            mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
            mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
        }

        mToast = Toast.makeText(context,"",Toast.LENGTH_SHORT);
    }

    private SynthesizerListener synthesizerListener = new SynthesizerListener() {
        @Override
        public void onSpeakBegin() {
            showToast("开始播报");
        }

        @Override
        public void onBufferProgress(int i, int i1, int i2, String s) {
            bufferPercent = i;
            showToast(String.format("缓冲进度为%d%%，播放进度为%d%%"+bufferPercent,playPercent));
        }

        @Override
        public void onSpeakPaused() {
            showToast("暂停播报");
        }

        @Override
        public void onSpeakResumed() {
            showToast("继续播报");
        }

        @Override
        public void onSpeakProgress(int i, int i1, int i2) {
            playPercent = i;
            showToast(String.format("缓冲进度为%d%%，播放进度为%d%%"+bufferPercent,playPercent));
        }

        @Override
        public void onCompleted(SpeechError speechError) {
            showToast("播报完成");
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_start_speech:
                mTts.startSpeaking(etInputContent.getText().toString().trim(),synthesizerListener);
                break;
            case R.id.bt_stop_speech:
                mTts.pauseSpeaking();
                break;
            case R.id.bt_continue_speech:
                mTts.resumeSpeaking();
                break;
            case R.id.bt_cancel_speech:
                mTts.stopSpeaking();
                break;
            default:
        }
    }

    private void showToast(final String str) {
        mToast.setText(str);
        mToast.show();
    }
}
