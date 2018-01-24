package com.example.dxc.xfdemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.example.dxc.xfdemo.util.CameraInterface;
import com.example.dxc.xfdemo.util.EventUtil;
import com.example.dxc.xfdemo.util.GoogleFaceDetect;
import com.example.dxc.xfdemo.util.Util;
import com.example.dxc.xfdemo.widget.CameraSurfaceView;
import com.example.dxc.xfdemo.widget.FaceView;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.IdentityListener;
import com.iflytek.cloud.IdentityResult;
import com.iflytek.cloud.IdentityVerifier;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import static android.content.ContentValues.TAG;

/**
 * Created by haitaow on 1/19/2018-3:23 PM.
 * Email: haitaow@hpe.com
 * version 1.0
 */

public class FaceVerifierTestActivity extends Activity {
    public static final String face_Data = "faceData";
    private Handler mMainHandler = null;
    private GoogleFaceDetect googleFaceDetect = null;
    private FaceView faceView;
    float previewRate = -1f;
    private boolean isStartedFaceDetect = false;
    private Context context;
    // 进度对话框
    private ProgressDialog mProDialog;
    private Toast mToast;

    CameraSurfaceView surfaceView = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_camera);
        context = FaceVerifierTestActivity.this;
        mProDialog = new ProgressDialog(this);
        mToast = Toast.makeText(this,"",Toast.LENGTH_SHORT);

        previewRate = Util.getScreenRate(this); //默认全屏的比例预览
        mMainHandler = new MainHandler();
        mMainHandler.sendEmptyMessageDelayed(EventUtil.CAMERA_HAS_STARTED_PREVIEW, 1500);
        googleFaceDetect = new GoogleFaceDetect(getApplicationContext(), mMainHandler);

        surfaceView = (CameraSurfaceView) findViewById(R.id.camera_surfaceview);
        faceView = (FaceView) findViewById(R.id.face_view);
        findViewById(R.id.btn_switch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchCamera();
            }
        });
    }

    private void takePicture() {
        CameraInterface.getInstance().doTakePicture(mMainHandler);
        mMainHandler.sendEmptyMessageDelayed(EventUtil.CAMERA_HAS_STARTED_PREVIEW, 1500);
    }

    private void switchCamera() {
        stopGoogleFaceDetect();
        int newId = (CameraInterface.getInstance().getCameraId() + 1) % 2;
        CameraInterface.getInstance().doStopCamera();
        CameraInterface.getInstance().doOpenCamera(null, newId);
        CameraInterface.getInstance().doStartPreview(surfaceView.getSurfaceHolder(), previewRate);
        mMainHandler.sendEmptyMessageDelayed(EventUtil.CAMERA_HAS_STARTED_PREVIEW, 1500);
        startGoogleFaceDetect();
    }

    private class MainHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case EventUtil.UPDATE_FACE_RECT:
                    Camera.Face[] faces = (Camera.Face[]) msg.obj;
                    faceView.setFaces(faces);
                    takePicture();
                    break;
                case EventUtil.CAMERA_HAS_STARTED_PREVIEW:
                    startGoogleFaceDetect();
                    break;
                case EventUtil.FACE_IMAGE_DATA:
                    byte[] faceData = (byte[]) msg.obj;
                    Bitmap bitmap = BitmapFactory.decodeByteArray(faceData, 0, faceData.length);
                    Matrix matrix = new Matrix();
                    matrix.setRotate(-90);
                    Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    newBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    verifyFace(byteArrayOutputStream.toByteArray());

//                    Intent intent = new Intent(context,FaceRequestActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putByteArray(face_Data,byteArrayOutputStream.toByteArray());
//                    intent.putExtra("data",faceData);
//                    intent.putExtra("data", byteArrayOutputStream.toByteArray());
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//                    stopGoogleFaceDetect();
            }
            super.handleMessage(msg);
        }

    }

    private void startGoogleFaceDetect() {
        Camera.Parameters params = CameraInterface.getInstance().getCameraParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (params != null && params.getMaxNumDetectedFaces() > 0) {
                if (faceView != null) {
                    faceView.clearFaces();
                    faceView.setVisibility(View.VISIBLE);
                }
                CameraInterface.getInstance().getCameraDevice().setFaceDetectionListener(googleFaceDetect);
                if (!isStartedFaceDetect) {
                    CameraInterface.getInstance().getCameraDevice().startFaceDetection();
                    isStartedFaceDetect = true;
                }
            }
        }
    }

    private void stopGoogleFaceDetect() {
        Camera.Parameters params = CameraInterface.getInstance().getCameraParams();
        if (params != null && params.getMaxNumDetectedFaces() > 0) {
            CameraInterface.getInstance().getCameraDevice().setFaceDetectionListener(null);
            CameraInterface.getInstance().getCameraDevice().stopFaceDetection();
            faceView.clearFaces();
        }
    }

    private void verifyFace(byte[] mImageData){
        if (null != mImageData) {
            mProDialog.setMessage("验证中...");
            mProDialog.show();
            // 设置人脸验证参数
            // 清空参数
            //采用身份识别接口进行在线人脸识别
            //初始化人脸识别引擎
            IdentityVerifier mIdVerifier = IdentityVerifier.createVerifier(this, new InitListener() {
                        @Override
                        public void onInit(int errorCode) {
                            if (ErrorCode.SUCCESS == errorCode) {
//                                ("引擎初始化成功");
                            } else {
                                showTip("引擎初始化失败，错误码：" + errorCode);
                            }
                        }
                    });;
            mIdVerifier.setParameter(SpeechConstant.PARAMS, null);
            // 设置会话场景
            mIdVerifier.setParameter(SpeechConstant.MFV_SCENES, "ifr");
            // 设置会话类型
            mIdVerifier.setParameter(SpeechConstant.MFV_SST, "verify");
            // 设置验证模式，单一验证模式：sin
            mIdVerifier.setParameter(SpeechConstant.MFV_VCM, "sin");
            // 用户id
            SharedPreferences sharedPreferences = getSharedPreferences(SplashActivity.sp_Name,Activity.MODE_PRIVATE);
            mIdVerifier.setParameter(SpeechConstant.AUTH_ID, sharedPreferences.getString(FaceRequestActivity.UserId,""));
            // 设置监听器，开始会话
            mIdVerifier.startWorking(mVerifyListener);

            // 子业务执行参数，若无可以传空字符传
            StringBuffer params = new StringBuffer();
            // 向子业务写入数据，人脸数据可以一次写入
            mIdVerifier.writeData("ifr", params.toString(), mImageData, 0, mImageData.length);
            // 停止写入
            mIdVerifier.stopWrite("ifr");
        }
    }

    /**
     * 人脸验证监听器
     */
    IdentityListener mVerifyListener = new IdentityListener() {
        @Override
        public void onResult(IdentityResult identityResult, boolean b) {
            Log.e(TAG, identityResult.getResultString());

            try {
                JSONObject object = new JSONObject(identityResult.getResultString());
                Log.e(TAG, "object is：" + object.toString());
                String decision = object.getString("decision");

                if ("accepted".equalsIgnoreCase(decision)) {
                    showTip("通过验证");
                    Intent intent = new Intent(context,MainActivity.class);
                    startActivity(intent);
                    stopGoogleFaceDetect();
                    finish();
                } else {
                    showTip("验证失败，请重试");
                    startGoogleFaceDetect();
                }
                if (mProDialog.isShowing()) {
                    mProDialog.dismiss();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(SpeechError speechError) {
            showTip(speechError.getPlainDescription(true));
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };

    private void showTip(final String str){
        mToast.setText(str);
        mToast.show();
    }
}
