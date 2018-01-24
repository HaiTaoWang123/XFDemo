package com.example.dxc.xfdemo;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.dxc.xfdemo.common.BaseActivity;
import com.example.dxc.xfdemo.util.FaceUtil;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.FaceRequest;
import com.iflytek.cloud.IdentityListener;
import com.iflytek.cloud.IdentityResult;
import com.iflytek.cloud.IdentityVerifier;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by wahaitao on 12/21/2017.
 */

public class FaceRequestActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = "OnlineFaceDemo";
    private final int REQUEST_PICTURE_CHOOSE = 1;
    private final int REQUEST_CAMERA_IMAGE = 2;
    public static final String UserId = "userId";
    public static final String M_Type = "mType";
    private int mType = 1;//0-登录方式中的人脸注册；1-人脸识别功能

    private Bitmap mImage = null;
    private byte[] mImageData = null;
    // authid为6-18个字符长度，用于唯一标识用户
    private String mAuthid = "";
    private Toast mToast;
    // 进度对话框
    private ProgressDialog mProDialog;
    // 拍照得到的照片文件
    private File mPictureFile;
    // FaceRequest对象，集成了人脸识别的各种功能
    private FaceRequest mFaceRequest;

    //采用身份识别接口进行在线人脸识别
    private IdentityVerifier mIdVerifier;

    // 模型操作
    private int mModelCmd;
    // 删除模型
    private final static int MODEL_DEL = 1;
//    private byte[] faceData;
    private EditText etUserId;
    private LinearLayout llOtherFunction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentLayout(R.layout.activity_facerequest);
        setTitle("人脸识别");
        setSettingVisible(false, "");
        setBackVisible(false,"");
        Intent intent = getIntent();
        mType = intent.getIntExtra(M_Type,1);

        findViewById(R.id.online_pick).setOnClickListener(this);
        findViewById(R.id.online_reg).setOnClickListener(this);
        findViewById(R.id.online_verify).setOnClickListener(this);
        findViewById(R.id.online_camera).setOnClickListener(this);
        findViewById(R.id.btn_modle_delete).setOnClickListener(this);
        findViewById(R.id.btn_identity).setOnClickListener(this);
        etUserId = (EditText) findViewById(R.id.online_authid);
        llOtherFunction = (LinearLayout) findViewById(R.id.ll_other_function);
        if (mType == 0){
            llOtherFunction.setVisibility(View.GONE);
        }else {
            llOtherFunction.setVisibility(View.VISIBLE);
        }

        //由设备型号+6位随机码组成
//        mAuthid = Build.BRAND + "" + (int) ((Math.random() * 9 + 1) * 100000) + "";
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        mProDialog = new ProgressDialog(this);
        mProDialog.setCancelable(true);
        mProDialog.setTitle("请稍后");

        mProDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                // cancel进度框时,取消正在进行的操作
                if (null != mIdVerifier) {
                    mIdVerifier.cancel();
                }
            }
        });
        //初始化人脸识别引擎
        mIdVerifier = IdentityVerifier.createVerifier(FaceRequestActivity.this, new InitListener() {
            @Override
            public void onInit(int errorCode) {
                if (ErrorCode.SUCCESS == errorCode) {
                    showTip("引擎初始化成功");
                } else {
                    showTip("引擎初始化失败，错误码：" + errorCode);
                }
            }
        });

        // 设置会话场景
//        mIdVerifier.setParameter(SpeechConstant.MFV_SCENES, "ifr");
        // 设置会话类型
//        mIdVerifier.setParameter(SpeechConstant.MFV_SST, "enroll");
        // 设置用户id
//        mIdVerifier.setParameter(SpeechConstant.AUTH_ID, mAuthid);
//         设置监听器，开始会话
//        mIdVerifier.startWorking(mEnrollListener);
//        while (!isDataFinished) {
//             写入数据，data为图片的二进制数据
//            mIdVerifier.writeData("ifr", params, data, offset, length);
//        }
//        mIdVerifier.stopWrite("ifr");
    }


    /**
     * 人脸注册监听器
     */
    private IdentityListener mEnrollListener = new IdentityListener() {
        @Override
        public void onResult(IdentityResult identityResult, boolean b) {
            Log.e(TAG, identityResult.getResultString());
            try {
                JSONObject object = new JSONObject(identityResult.getResultString());
                int ret = object.getInt("ret");

                if (ErrorCode.SUCCESS == ret) {
                    showTip("注册成功");
                    if (mProDialog.isShowing()) {
                        mProDialog.dismiss();
                    }

                    if (mType == 0){//若是初次注册人脸，注册成功后保存用户Id并进行人脸验证
                        SharedPreferences sharedPreferences = getSharedPreferences(SplashActivity.sp_Name, Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(UserId,mAuthid);
                        editor.commit();
                        Intent intent = new Intent(FaceRequestActivity.this,FaceVerifierTestActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } else {
                    showTip(new SpeechError(ret).getPlainDescription(true));
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

    /**
     * 人脸验证监听器
     */
    private IdentityListener mVerifyListener = new IdentityListener() {
        @Override
        public void onResult(IdentityResult identityResult, boolean b) {
            Log.e(TAG, identityResult.getResultString());

            try {
                JSONObject object = new JSONObject(identityResult.getResultString());
                Log.e(TAG, "object is：" + object.toString());
                String decision = object.getString("decision");

                if ("accepted".equalsIgnoreCase(decision)) {
                    showTip("通过验证");
                } else {
                    showTip("验证失败");
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

    /**
     * 人脸模型操作监听器
     */
    private IdentityListener mModelListener = new IdentityListener() {

        @Override
        public void onResult(IdentityResult result, boolean islast) {
            Log.d(TAG, result.getResultString());

            JSONObject jsonResult = null;
            int ret = ErrorCode.SUCCESS;
            try {
                jsonResult = new JSONObject(result.getResultString());
                ret = jsonResult.getInt("ret");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // 根据操作类型判断结果类型
            switch (mModelCmd) {
                case MODEL_DEL:
                    if (ErrorCode.SUCCESS == ret) {
                        showTip("删除成功");
                    } else {
                        showTip("删除失败");
                    }
                    if (mProDialog.isShowing()) {
                        mProDialog.dismiss();
                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }

        @Override
        public void onError(SpeechError error) {
            // 弹出错误信息
            showTip(error.getPlainDescription(true));
        }

    };

    private void executeModelCommand(String cmd) {
        // 设置人脸模型操作参数
        // 清空参数
        mIdVerifier.setParameter(SpeechConstant.PARAMS, null);
        // 设置会话场景
        mIdVerifier.setParameter(SpeechConstant.MFV_SCENES, "ifr");
        // 用户id
        mIdVerifier.setParameter(SpeechConstant.AUTH_ID, mAuthid);

        // 设置模型参数，若无可以传空字符传
        StringBuffer params = new StringBuffer();
        // 执行模型操作
        mIdVerifier.execute("ifr", cmd, params.toString(), mModelListener);
    }

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }

    @Override
    public void onSettingClick() {

    }

    @Override
    public void onClick(View v) {
        int ret = ErrorCode.SUCCESS;
        mAuthid = etUserId.getText().toString().trim();
        switch (v.getId()) {
            case R.id.online_pick:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //权限还没有授予，需要在这里写申请权限的代码
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 60);
                }else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(intent, REQUEST_PICTURE_CHOOSE);
                }
                break;
            case R.id.online_reg:
                if (TextUtils.isEmpty(mAuthid)) {
                    showTip("Authid不能为空");
                    return;
                }

                if (null != mImageData) {
                    mProDialog.setMessage("注册中...");
                    mProDialog.show();

                    // 设置人脸验证参数
                    // 清空参数
                    mIdVerifier.setParameter(SpeechConstant.PARAMS, null);
                    // 设置会话场景
                    mIdVerifier.setParameter(SpeechConstant.MFV_SCENES, "ifr");
                    // 设置会话类型
                    mIdVerifier.setParameter(SpeechConstant.MFV_SST, "enroll");
                    // 设置验证模式，单一验证模式：sin
                    mIdVerifier.setParameter(SpeechConstant.MFV_VCM, "sin");
                    // 用户id
                    mIdVerifier.setParameter(SpeechConstant.AUTH_ID, mAuthid);
                    // 设置监听器，开始会话
                    mIdVerifier.startWorking(mEnrollListener);

                    // 子业务执行参数，若无可以传空字符传
                    StringBuffer params = new StringBuffer();
                    // 向子业务写入数据，人脸数据可以一次写入
                    mIdVerifier.writeData("ifr", params.toString(), mImageData, 0, mImageData.length);
                    // 停止写入
                    mIdVerifier.stopWrite("ifr");
                } else {
                    showTip("请选择图片后再验证");
                }
                break;
            case R.id.online_camera:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //权限还没有授予，需要在这里写申请权限的代码
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 60);
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA}, 60);
                }
                // 设置相机拍照后照片保存路径
                mPictureFile = new File(Environment.getExternalStorageDirectory(),
                        "picture" + System.currentTimeMillis() / 1000 + ".jpg");
                // 启动拍照,并保存到临时文件
                Intent mIntent = new Intent();
                mIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                mIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPictureFile));
                mIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                startActivityForResult(mIntent, REQUEST_CAMERA_IMAGE);
                break;
            case R.id.online_verify:
                if (TextUtils.isEmpty(mAuthid)) {
                    showTip("Authid不能为空");
                    return;
                }

                if (null != mImageData) {
                    mProDialog.setMessage("验证中...");
                    mProDialog.show();
                    // 设置人脸验证参数
                    // 清空参数
                    mIdVerifier.setParameter(SpeechConstant.PARAMS, null);
                    // 设置会话场景
                    mIdVerifier.setParameter(SpeechConstant.MFV_SCENES, "ifr");
                    // 设置会话类型
                    mIdVerifier.setParameter(SpeechConstant.MFV_SST, "verify");
                    // 设置验证模式，单一验证模式：sin
                    mIdVerifier.setParameter(SpeechConstant.MFV_VCM, "sin");
                    // 用户id
                    mIdVerifier.setParameter(SpeechConstant.AUTH_ID, mAuthid);
                    // 设置监听器，开始会话
                    mIdVerifier.startWorking(mVerifyListener);

                    // 子业务执行参数，若无可以传空字符传
                    StringBuffer params = new StringBuffer();
                    // 向子业务写入数据，人脸数据可以一次写入
                    mIdVerifier.writeData("ifr", params.toString(), mImageData, 0, mImageData.length);
                    // 停止写入
                    mIdVerifier.stopWrite("ifr");
                } else {
                    showTip("请选择图片后再验证");
                }
                break;
            case R.id.btn_modle_delete:
                // 人脸模型删除
                mModelCmd = MODEL_DEL;
                executeModelCommand("delete");
                break;
            case R.id.btn_identity:
                if (mImageData != null) {
                    ImageView ivImg = (ImageView) findViewById(R.id.online_img);
                    if (mImageData != null) {
                        ivImg.setImageBitmap(BitmapFactory.decodeByteArray(mImageData, 0, mImageData.length));
                    }
                }
                break;
            default:
                break;
        }
        if (ErrorCode.SUCCESS != ret) {
            mProDialog.dismiss();
            showTip("出现错误：" + ret);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        String fileSrc = null;
        if (requestCode == REQUEST_PICTURE_CHOOSE) {
            if ("file".equals(data.getData().getScheme())) {
                // 有些低版本机型返回的Uri模式为file
                fileSrc = data.getData().getPath();
            } else {
                // Uri模型为content
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(data.getData(), proj,
                        null, null, null);
                cursor.moveToFirst();
                int idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                fileSrc = cursor.getString(idx);
                cursor.close();
            }
            // 跳转到图片裁剪页面
            FaceUtil.cropPicture(this, Uri.fromFile(new File(fileSrc)));
        } else if (requestCode == REQUEST_CAMERA_IMAGE) {
            if (null == mPictureFile) {
                showTip("拍照失败，请重试");
                return;
            }

            fileSrc = mPictureFile.getAbsolutePath();
            updateGallery(fileSrc);

            // 跳转到图片裁剪页面
            FaceUtil.cropPicture(this, Uri.fromFile(new File(fileSrc)));
        } else if (requestCode == FaceUtil.REQUEST_CROP_IMAGE) {
            //获取返回数据
            Bitmap bitmap = data.getParcelableExtra("data");
            //若返回数据不为null，保存至本地，防止裁剪时未能正常保存
            if (null != bitmap) {
                FaceUtil.saveBitmapToFile(FaceRequestActivity.this, bitmap);
            }
            //获取图片保存路径
            fileSrc = FaceUtil.getImagePath(FaceRequestActivity.this);
            //获取图片的宽和高
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            mImage = BitmapFactory.decodeFile(fileSrc, options);

            // 压缩图片
            options.inSampleSize = Math.max(1, (int) Math.ceil(Math.max(
                    (double) options.outWidth / 1024f,
                    (double) options.outHeight / 1024f)));
            options.inJustDecodeBounds = false;
            mImage = BitmapFactory.decodeFile(fileSrc, options);

            //若mImageBitmap为空则图片信息不能正常获取
            int degree = FaceUtil.readPictureDegree(fileSrc);
            if (degree != 0) {
                //把图片旋转为正的方向
                mImage = FaceUtil.rotateImage(degree, mImage);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //可根据流量及网络状况对图片进行压缩
            mImage.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            mImageData = baos.toByteArray();

            ((ImageView) findViewById(R.id.online_img)).setImageBitmap(mImage);
        }
    }

    @Override
    public void finish() {
        if (null != mProDialog) {
            mProDialog.dismiss();
        }
        super.finish();
    }

    private void updateGallery(String filename) {
        MediaScannerConnection.scanFile(this, new String[]{filename}, null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    @Override
                    public void onScanCompleted(String path, Uri uri) {

                    }
                });
    }
}
