package com.example.dxc.xfdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dxc.xfdemo.Common.BaseActivity;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

import static android.content.ContentValues.TAG;

/**
 * Created by wahaitao on 12/21/2017.
 */

public class RecognizerActivity extends BaseActivity implements View.OnClickListener {
    private EditText etInputContent;
    private SpeechSynthesizer mTts;
    private Context context;
    private int playPercent, bufferPercent;
    private Toast mToast;
    private TextView tvCashPro, tvSpeakPro;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivty_synthesizer);
        setTitle("语音播报");
        setSettingVisible(true,"");
        context = RecognizerActivity.this;

        etInputContent = (EditText) findViewById(R.id.et_content);
        findViewById(R.id.bt_start_speech).setOnClickListener(this);
        findViewById(R.id.bt_stop_speech).setOnClickListener(this);
        findViewById(R.id.bt_continue_speech).setOnClickListener(this);
        findViewById(R.id.bt_cancel_speech).setOnClickListener(this);

        tvCashPro = (TextView) findViewById(R.id.tv_cash_progress);
        tvSpeakPro = (TextView) findViewById(R.id.tv_speak_progress);
        progressBar = (ProgressBar) findViewById(R.id.pb_synthesizer);
        tvSpeakPro.setText("缓冲进度："+0+"%");
        tvCashPro.setText("缓冲进度："+0+"%");

        if (mTts == null) {
            mTts = SpeechSynthesizer.createSynthesizer(context, mTtsInitListener);
            mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
            mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
            mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
        }
//        Toast.makeText(context,String.format("缓冲进度为%d%%，播放进度为%d%%"+bufferPercent,playPercent),Toast.LENGTH_SHORT).show();
//        Toast.makeText(context,bufferPercent+"/"+playPercent+"",Toast.LENGTH_SHORT).show();
        mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
    }

    @Override
    public void onSettingClick() {
        if (tvSetting.getText() != null){

        }
    }

    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(TAG, "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showToast("初始化失败,错误码：" + code);
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };
    private SynthesizerListener synthesizerListener = new SynthesizerListener() {
        @Override
        public void onSpeakBegin() {
            showToast("开始播报");
//            Toast.makeText(context,"开始播报",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBufferProgress(int i, int i1, int i2, String s) {
            bufferPercent = i;
            tvCashPro.setText("缓冲进度："+i+"%");
            progressBar.setSecondaryProgress(i);
//            showToast("缓冲进度为" + bufferPercent+",播放进度为"+playPercent);
//            Log.e("Toast", "缓冲进度为" + bufferPercent+",播放进度为"+playPercent);
//            Toast.makeText(context,String.format("缓冲进度为%d%%，播放进度为%d%%"+bufferPercent,playPercent),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSpeakPaused() {
            showToast("暂停播报");
            Log.e("Toast", "暂停播报");
//            Toast.makeText(context,"暂停播报",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSpeakResumed() {
            showToast("继续播报");
//            Log.e("Toast", "继续播报");
//            Toast.makeText(context,"继续播报",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSpeakProgress(int i, int i1, int i2) {
            tvSpeakPro.setText("播报进度："+i+"%");
            progressBar.setProgress(i);
            playPercent = i;
//            showToast("缓冲进度为" + bufferPercent+",播放进度为"+playPercent);
//            Log.e("Toast", "缓冲进度为" + bufferPercent+",播放进度为"+playPercent);
            //            Toast.makeText(context,String.format("缓冲进度为%d%%，播放进度为%d%%"+bufferPercent,playPercent),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCompleted(SpeechError speechError) {
//            showToast("播报完成");
//            Toast.makeText(context,"播报完成",Toast.LENGTH_SHORT).show();
            if (speechError == null) {
                showToast("播放完成");
            } else if (speechError != null) {
                showToast(speechError.getPlainDescription(true));
            }
            if (playPercent == 99){
                tvSpeakPro.setText("缓冲进度："+100+"%");
            }
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_start_speech:
                int code = mTts.startSpeaking(etInputContent.getText().toString().trim(), synthesizerListener);
                if (code != ErrorCode.SUCCESS) {
                    showToast("语音合成失败,错误码: " + code);
                }
                break;
            case R.id.bt_stop_speech:
                mTts.pauseSpeaking();
                break;
            case R.id.bt_continue_speech:
                mTts.resumeSpeaking();
                break;
            case R.id.bt_cancel_speech:
                mTts.stopSpeaking();
                progressBar.setProgress(0);
                progressBar.setSecondaryProgress(0);
                tvSpeakPro.setText("缓冲进度："+0+"%");
                tvCashPro.setText("缓冲进度："+0+"%");
                break;
            default:
        }
    }

    private void showToast(final String str) {
        mToast.setText(str);
        mToast.show();
    }


}