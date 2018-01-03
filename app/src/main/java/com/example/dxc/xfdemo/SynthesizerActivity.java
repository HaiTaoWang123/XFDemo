package com.example.dxc.xfdemo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dxc.xfdemo.common.BaseActivity;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * 语音播报
 * Created by wahaitao on 12/21/2017.
 */

public class SynthesizerActivity  extends BaseActivity implements View.OnClickListener {
    public static final String Speaker = "speaker";
    public static final String AudioStream = "audioStream";
    public static final String SpeakerSpeed = "speakerSpeed";
    public static final String SpeakerVolume = "speakerVolume";
    public static final String SpeakerTone = "speakerTone";
    private EditText etInputContent;
    private SpeechSynthesizer mTts;
    private Context context;
    private int playPercent, bufferPercent;
    private Toast mToast;
    private TextView tvCashPro, tvSpeakPro;
    private ProgressBar progressBar;
    private String speaker, audioStream, speakerSpeed, speakerVolume, speakerTone;
    private SettingDialog settingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentLayout(R.layout.activity_synthesizer);
        setTitle("语音播报");
        setSettingVisible(true, "设置");
        context = SynthesizerActivity.this;

        etInputContent = (EditText) findViewById(R.id.et_content);
        findViewById(R.id.bt_start_speech).setOnClickListener(this);
        findViewById(R.id.bt_stop_speech).setOnClickListener(this);
        findViewById(R.id.bt_continue_speech).setOnClickListener(this);
        findViewById(R.id.bt_cancel_speech).setOnClickListener(this);

        tvCashPro = (TextView) findViewById(R.id.tv_cash_progress);
        tvSpeakPro = (TextView) findViewById(R.id.tv_speak_progress);
        progressBar = (ProgressBar) findViewById(R.id.pb_synthesizer);
        tvSpeakPro.setText("缓冲进度：" + 0 + "%");
        tvCashPro.setText("缓冲进度：" + 0 + "%");

//        if (mTts == null) {
//            mTts = SpeechSynthesizer.createSynthesizer(context, mTtsInitListener);
//            mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
//            mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
//            mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100
//            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
//        }
//        Toast.makeText(context,String.format("缓冲进度为%d%%，播放进度为%d%%"+bufferPercent,playPercent),Toast.LENGTH_SHORT).show();
//        Toast.makeText(context,bufferPercent+"/"+playPercent+"",Toast.LENGTH_SHORT).show();
        mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
    }

    @Override
    public void onSettingClick() {
        if (tvSetting.getText() != null) {
//            Toast.makeText(context,tvSetting.getText(),Toast.LENGTH_SHORT).show();
            settingDialog = new SettingDialog(context, new SettingDialogListener() {
                @Override
                public void getSettingData(Map<String, Object> settingData) {
                    int speakerId = Integer.parseInt(String.valueOf(settingData.get(Speaker)));
                    speaker = getResources().getStringArray(R.array.voicer_cloud_values)[speakerId];
                    audioStream = (String) settingData.get(AudioStream);
                    speakerTone = (String) settingData.get(SpeakerTone);
                    speakerVolume = (String) settingData.get(SpeakerVolume);
                    speakerSpeed = (String) settingData.get(SpeakerSpeed);
                    showToast("修改参数:" + speaker + "/" + speakerTone + "/" + speakerVolume + "/" + speakerSpeed + "/" + audioStream + "/");
                }

                @Override
                public void confirm() {
                    settingDialog.dismiss();
                }

                @Override
                public void cancel() {
                    settingDialog.dismiss();
                }
            });
            settingDialog.show();
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

    private void setParam() {
        if (mTts == null) {
            mTts = SpeechSynthesizer.createSynthesizer(context, mTtsInitListener);
        }
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置在线合成发音人
        if (speaker != null) {
            mTts.setParameter(SpeechConstant.VOICE_NAME, speaker);
        } else {
            mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
        }
        //设置合成语速
        if (speakerSpeed != null) {
            mTts.setParameter(SpeechConstant.SPEED, speakerSpeed);
        } else {
            mTts.setParameter(SpeechConstant.SPEED, "50");
        }
        //设置合成音调
        if (speakerTone != null) {
            mTts.setParameter(SpeechConstant.PITCH, speakerTone);
        } else {
            mTts.setParameter(SpeechConstant.PITCH, "50");
        }
        //设置合成音量
        if (speakerVolume != null) {
            mTts.setParameter(SpeechConstant.VOLUME, speakerVolume);
        } else {
            mTts.setParameter(SpeechConstant.VOLUME, "50");
        }
        //设置音频流类型
        if (audioStream != null) {
            mTts.setParameter(SpeechConstant.STREAM_TYPE, audioStream);
        } else {
            mTts.setParameter(SpeechConstant.STREAM_TYPE, "系统");
        }
    }

    private SynthesizerListener synthesizerListener = new SynthesizerListener() {
        @Override
        public void onSpeakBegin() {
            showToast("开始播报");
//            Toast.makeText(context,"开始播报",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBufferProgress(int i, int i1, int i2, String s) {
            bufferPercent = i;
            tvCashPro.setText("缓冲进度：" + i + "%");
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
            tvSpeakPro.setText("播报进度：" + i + "%");
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
            if (playPercent == 99) {
                tvSpeakPro.setText("缓冲进度：" + 100 + "%");
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
                setParam();
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
                tvSpeakPro.setText("缓冲进度：" + 0 + "%");
                tvCashPro.setText("缓冲进度：" + 0 + "%");
                break;
            default:
        }
    }

    private void showToast(final String str) {
        mToast.setText(str);
        mToast.show();
    }

    private class SettingDialog extends Dialog {
        Context dialogContext;
        Button btnConfirm, btnCancel;
        Spinner speakerSpinner, audioStreamSpinner;
        EditText tvSpeakerSpeed, tvSpeakerVolume, tvSpeakerTone;
        SettingDialogListener listener = null;

        public SettingDialog(@NonNull Context context, SettingDialogListener listener) {
            super(context);
            this.dialogContext = context;
            this.listener = listener;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_synthesizer_setting);
            Window dialogWindow = getWindow();
//            final WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//            DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
//            lp.width = (int) (d.widthPixels * 0.9); // 高度设置为屏幕的0.6
//            lp.height = (int) (d.heightPixels * 0.8); // 高度设置为屏幕的0.6
            dialogWindow.setGravity(Gravity.CENTER);
//            dialogWindow.setAttributes(lp);

            speakerSpinner = (Spinner) findViewById(R.id.speaker_spinner);
            audioStreamSpinner = (Spinner) findViewById(R.id.audio_stream_type);
            speakerSpinner.setSelection(0, true);
            audioStreamSpinner.setSelection(0, true);
            tvSpeakerSpeed = (EditText) findViewById(R.id.speaker_speed);
            tvSpeakerVolume = (EditText) findViewById(R.id.speaker_volume);
            tvSpeakerTone = (EditText) findViewById(R.id.speaker_tone);
            btnConfirm = (Button) findViewById(R.id.bt_confirm);
            btnCancel = (Button) findViewById(R.id.bt_cancel);

            String[] speakerData = getResources().getStringArray(R.array.speaker_spinnerarr);
            speakerSpinner.setAdapter(new ArrayAdapter<String>(context, R.layout.spinner_item_layout, speakerData));
            String[] audioStreamData = getResources().getStringArray(R.array.audio_stream_type);
            audioStreamSpinner.setAdapter(new ArrayAdapter<String>(context, R.layout.spinner_item_layout, audioStreamData));

            speakerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    speakerSpinner.setSelection(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            audioStreamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    audioStreamSpinner.setSelection(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, Object> settingData = new HashMap<>();
                    settingData.put(Speaker, speakerSpinner.getSelectedItemId());
                    settingData.put(AudioStream, audioStreamSpinner.getSelectedItem());
                    settingData.put(SpeakerSpeed, tvSpeakerSpeed.getText().toString().trim());
                    settingData.put(SpeakerVolume, tvSpeakerVolume.getText().toString().trim());
                    settingData.put(SpeakerTone, tvSpeakerTone.getText().toString().trim());
                    listener.getSettingData(settingData);
                    listener.confirm();
                }
            });
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.cancel();
                }
            });
        }
    }

    public interface SettingDialogListener {
        void getSettingData(Map<String, Object> settingData);

        void confirm();

        void cancel();
    }
}
