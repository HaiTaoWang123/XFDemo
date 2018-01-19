package com.example.dxc.xfdemo;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.dxc.xfdemo.common.BaseActivity;
import com.example.dxc.xfdemo.util.CameraInterface;
import com.example.dxc.xfdemo.util.EventUtil;
import com.example.dxc.xfdemo.util.GoogleFaceDetect;
import com.example.dxc.xfdemo.util.Util;
import com.example.dxc.xfdemo.widget.CameraSurfaceView;
import com.example.dxc.xfdemo.widget.FaceView;

/**
 * Created by haitaow on 1/19/2018-3:23 PM.
 * Email: haitaow@hpe.com
 * version 1.0
 */

public class FaceVerifierTestActivity extends BaseActivity {
    private Handler mMainHandler = null;
    private GoogleFaceDetect googleFaceDetect = null;
    private FaceView faceView;
    float previewRate = -1f;
    private boolean isStartedFaceDetect = false;

    CameraSurfaceView surfaceView = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentLayout(R.layout.activity_camera);


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

    @Override
    public void onSettingClick() {

    }

    private void takePicture() {
        CameraInterface.getInstance().doTakePicture(mMainHandler);
//        mMainHandler.sendEmptyMessageDelayed(EventUtil.CAMERA_HAS_STARTED_PREVIEW, 1500);
    }

    private void switchCamera() {
        stopGoogleFaceDetect();
        int newId = (CameraInterface.getInstance().getCameraId() + 1) % 2;
        CameraInterface.getInstance().doStopCamera();
        CameraInterface.getInstance().doOpenCamera(null, newId);
        CameraInterface.getInstance().doStartPreview(surfaceView.getSurfaceHolder(), previewRate);
        mMainHandler.sendEmptyMessageDelayed(EventUtil.CAMERA_HAS_STARTED_PREVIEW, 1500);
//		startGoogleFaceDetect();

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
                    Intent intent = new Intent(FaceVerifierTestActivity.this,FaceRequestActivity.class);
                    intent.putExtra("data",faceData);
                    startActivity(intent);
                    stopGoogleFaceDetect();
            }
            super.handleMessage(msg);
        }

    }

    private void startGoogleFaceDetect() {
        Camera.Parameters params = CameraInterface.getInstance().getCameraParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (params.getMaxNumDetectedFaces() > 0) {
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
        if (params.getMaxNumDetectedFaces() > 0) {
            CameraInterface.getInstance().getCameraDevice().setFaceDetectionListener(null);
            CameraInterface.getInstance().getCameraDevice().stopFaceDetection();
            faceView.clearFaces();
        }
    }
}
