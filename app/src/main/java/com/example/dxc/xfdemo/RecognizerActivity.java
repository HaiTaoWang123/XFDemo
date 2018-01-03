package com.example.dxc.xfdemo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dxc.xfdemo.common.BaseActivity;
import com.example.dxc.xfdemo.common.JsonParser;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * 语音识别
 * Created by wahaitao on 12/21/2017.
 */

public class RecognizerActivity extends BaseActivity implements View.OnClickListener {
    public static final String recognizer = "recognizer";
    public static final String FrontTime = "frontTime";
    public static final String EndTime = "endTime";
    public static final String isShowDialog = "showDialog";
    public static final String isTranslation = "translation";
    public static final String isShowSymbole = "symbole";
    private EditText tvResults;
    private SpeechRecognizer mSpeechRecognizer;
    private RecognizerDialog recognizerDialog;
    // 用HashMap存储听写结果
    private HashMap<String, String> mSpeechRecognizerResults = new LinkedHashMap<String, String>();
    private Context context;
    private boolean mTranslateEnable = false;//是否翻译
    private boolean mReognizeDialogEnable = false;//是否显示录音dialog
    private boolean mSymboleEnable = false;//是否显示标点符号
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private int playPercent, bufferPercent;
    private Toast mToast;
    private TextView tvCashPro, tvSpeakPro;
    private ProgressBar progressBar;
    private String recognizerName, frontTime, endTime;
    private SettingDialog settingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentLayout(R.layout.activity_recognizer);
        setTitle("语音识别");
        setSettingVisible(true, "设置");
        context = RecognizerActivity.this;

        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        if (mSpeechRecognizer == null) {
            mSpeechRecognizer = SpeechRecognizer.createRecognizer(context, mInitListener);
        }
        //初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        //使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        recognizerDialog = new RecognizerDialog(context, mInitListener);

        tvResults = (EditText) findViewById(R.id.et_content);
        findViewById(R.id.bt_start_recognize).setOnClickListener(this);
        findViewById(R.id.bt_stop_recognize).setOnClickListener(this);
        findViewById(R.id.bt_cancel_recognize).setOnClickListener(this);

        tvCashPro = (TextView) findViewById(R.id.tv_cash_progress);
        tvSpeakPro = (TextView) findViewById(R.id.tv_speak_progress);
        progressBar = (ProgressBar) findViewById(R.id.pb_synthesizer);
        tvSpeakPro.setText("缓冲进度：" + 0 + "%");
        tvCashPro.setText("缓冲进度：" + 0 + "%");

        mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
    }

    @Override
    public void onSettingClick() {
        if (tvSetting.getText() != null) {
//            Toast.makeText(context,tvSetting.getText(),Toast.LENGTH_SHORT).show();
            settingDialog = new SettingDialog(context, new SettingDialogListener() {
                @Override
                public void getSettingData(Map<String, Object> settingData) {
                    int recognizerId = Integer.parseInt(String.valueOf(settingData.get(recognizer)));
                    recognizerName = getResources().getStringArray(R.array.language_values)[recognizerId];
                    frontTime = (String) settingData.get(FrontTime);
                    endTime = (String) settingData.get(EndTime);
                    mReognizeDialogEnable = (boolean) settingData.get(isShowDialog);
                    mReognizeDialogEnable = (boolean) settingData.get(isShowSymbole);
                    mTranslateEnable = (boolean) settingData.get(isTranslation);
                    showToast("修改参数:" + recognizerName + "/" + frontTime + "/" + endTime + "/" + mReognizeDialogEnable + "/" + mTranslateEnable + "/");
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

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showToast("初始化失败，错误码：" + code);
            }
        }
    };

    /**
     * 参数设置
     *
     * @return
     */
    public void setParam() {
        // 清空参数
        mSpeechRecognizer.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mSpeechRecognizer.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mSpeechRecognizer.setParameter(SpeechConstant.RESULT_TYPE, "json");

        if (mTranslateEnable) {
            Log.i(TAG, "translate enable");
            mSpeechRecognizer.setParameter(SpeechConstant.ASR_SCH, "1");
            mSpeechRecognizer.setParameter(SpeechConstant.ADD_CAP, "translate");
            mSpeechRecognizer.setParameter(SpeechConstant.TRS_SRC, "its");
        }

        if (recognizerName.equals("en_us")) {
            // 设置语言
            mSpeechRecognizer.setParameter(SpeechConstant.LANGUAGE, "en_us");
            mSpeechRecognizer.setParameter(SpeechConstant.ACCENT, null);

            if (mTranslateEnable) {
                mSpeechRecognizer.setParameter(SpeechConstant.ORI_LANG, "en");
                mSpeechRecognizer.setParameter(SpeechConstant.TRANS_LANG, "cn");
            }
        }else {
            // 设置语言
            mSpeechRecognizer.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mSpeechRecognizer.setParameter(SpeechConstant.ACCENT, recognizerName);

            if (mTranslateEnable) {
                mSpeechRecognizer.setParameter(SpeechConstant.ORI_LANG, "cn");
                mSpeechRecognizer.setParameter(SpeechConstant.TRANS_LANG, "en");
            }
        }

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mSpeechRecognizer.setParameter(SpeechConstant.VAD_BOS, frontTime);

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mSpeechRecognizer.setParameter(SpeechConstant.VAD_EOS, endTime);

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        if(mSymboleEnable){
            mSpeechRecognizer.setParameter(SpeechConstant.ASR_PTT,  "1");
        }else {
            mSpeechRecognizer.setParameter(SpeechConstant.ASR_PTT,  "0");
        }

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mSpeechRecognizer.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mSpeechRecognizer.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
    }


    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            if( mTranslateEnable ){
                printTransResult( results );
            }else{
                printResult(results);
            }

        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            if(mTranslateEnable && error.getErrorCode() == 14002) {
                showToast( error.getPlainDescription(true)+"\n请确认是否已开通翻译功能" );
            } else {
                showToast(error.getPlainDescription(true));
            }
        }

    };

    private RecognizerListener recognizerListener = new RecognizerListener() {
        @Override
        public void onVolumeChanged(int volume, byte[] bytes) {
            showToast("当前正在说话，音量大小：" + volume);
        }

        @Override
        public void onBeginOfSpeech() {
            showToast("开始说话");
        }

        @Override
        public void onEndOfSpeech() {
            showToast("结束说话");
        }

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            if( mTranslateEnable ){
                printTransResult(recognizerResult);
            }else{
                printResult(recognizerResult);
            }
        }

        @Override
        public void onError(SpeechError speechError) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            if(mTranslateEnable && speechError.getErrorCode() == 14002) {
                showToast( speechError.getPlainDescription(true)+"\n请确认是否已开通翻译功能" );
            } else {
                showToast(speechError.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    private void printTransResult (RecognizerResult results) {
        String trans  = JsonParser.parseTransResult(results.getResultString(),"dst");
        String oris = JsonParser.parseTransResult(results.getResultString(),"src");

        if( TextUtils.isEmpty(trans)||TextUtils.isEmpty(oris) ){
            showToast( "解析结果失败，请确认是否已开通翻译功能。" );
        }else{
            tvResults.setText( "原始语言:\n"+oris+"\n目标语言:\n"+trans );
        }

    }

    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSpeechRecognizerResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mSpeechRecognizerResults.keySet()) {
            resultBuffer.append(mSpeechRecognizerResults.get(key));
        }

        tvResults.setText(resultBuffer.toString());
        tvResults.setSelection(tvResults.length());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_start_recognize:
                tvResults.setText("");
                mSpeechRecognizerResults.clear();
                setParam();
                // 显示听写对话框
                recognizerDialog.setListener(mRecognizerDialogListener);
                recognizerDialog.show();
                int code = mSpeechRecognizer.startListening(recognizerListener);
                if (code != ErrorCode.SUCCESS) {
                    showToast("语音合成失败,错误码: " + code);
                }
                break;
            case R.id.bt_stop_recognize:
                mSpeechRecognizer.stopListening();
                showToast("停止听写");
                break;
            case R.id.bt_cancel_recognize:
                mSpeechRecognizer.cancel();
                showToast("取消听写");
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
        Spinner recognizerSpinner;
        EditText etFrontTime, etEndTime, tvSpeakerTone;
        CheckBox cbSymbole,cbTranslation,cbDialog;
        SettingDialogListener listener = null;

        public SettingDialog(@NonNull Context context, SettingDialogListener listener) {
            super(context);
            this.dialogContext = context;
            this.listener = listener;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_recognizer_setting);
            Window dialogWindow = getWindow();
//            final WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//            DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
//            lp.width = (int) (d.widthPixels * 0.9); // 高度设置为屏幕的0.6
//            lp.height = (int) (d.heightPixels * 0.8); // 高度设置为屏幕的0.6
            dialogWindow.setGravity(Gravity.CENTER);
//            dialogWindow.setAttributes(lp);

            recognizerSpinner = (Spinner) findViewById(R.id.recognizer_spinner);
            recognizerSpinner.setSelection(0, true);
            etFrontTime = (EditText) findViewById(R.id.front_time);
            etEndTime = (EditText) findViewById(R.id.end_time);
            btnConfirm = (Button) findViewById(R.id.bt_confirm);
            btnCancel = (Button) findViewById(R.id.bt_cancel);
            cbSymbole = (CheckBox) findViewById(R.id.cb_symbole);
            cbTranslation = (CheckBox) findViewById(R.id.cb_translation);
            cbDialog = (CheckBox) findViewById(R.id.cb_dialog);

            String[] speakerData = getResources().getStringArray(R.array.speaker_spinnerarr);
            recognizerSpinner.setAdapter(new ArrayAdapter<String>(context, R.layout.spinner_item_layout, speakerData));
            String[] audioStreamData = getResources().getStringArray(R.array.audio_stream_type);

            recognizerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    recognizerSpinner.setSelection(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, Object> settingData = new HashMap<>();
                    settingData.put(recognizer, recognizerSpinner.getSelectedItemId());
                    settingData.put(FrontTime, etFrontTime.getText().toString().trim());
                    settingData.put(EndTime, etEndTime.getText().toString().trim());
                    settingData.put(isShowDialog, cbSymbole.isChecked());
                    settingData.put(isTranslation, cbTranslation.isChecked());
                    settingData.put(isShowSymbole, cbSymbole.isChecked());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mSpeechRecognizer){
            // 退出时释放连接
            mSpeechRecognizer.cancel();
            mSpeechRecognizer.destroy();
        }
    }
}