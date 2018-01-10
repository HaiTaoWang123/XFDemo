package com.example.dxc.xfdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.example.dxc.xfdemo.common.BaseActivity;
import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.mylhyl.zxing.scanner.OnScannerCompletionListener;
import com.mylhyl.zxing.scanner.ScannerView;
import com.mylhyl.zxing.scanner.camera.open.CameraFacing;

/**
 * Created by wahaitao on 1/8/2018.
 */

public class ScannerTestActivity extends BaseActivity {
    public static final String SCANNER_RESULT = "scannerResult";
    public static final String RESULT_TYPE = "resultType";
    private ScannerView scannerView;
    private Result scannerResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentLayout(R.layout.activity_barcode_scanner);
        setTitle("扫描测试");
        setSettingVisible(true, "二维码生成");

        scannerView = (ScannerView) findViewById(R.id.scannerView);
        ScannerSetting();

        scannerView.setOnScannerCompletionListener(new OnScannerCompletionListener() {
            @Override
            public void onScannerCompletion(Result result, ParsedResult parsedResult, Bitmap bitmap) {
                showProgressDialog();
                final Intent intent = new Intent(ScannerTestActivity.this, ScannerResultActivity.class);
                if (result != null && result.getText() != null) {
                    intent.putExtra(SCANNER_RESULT, result.getText());
                    intent.putExtra(RESULT_TYPE, parsedResult.getType());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(intent);
                            dismissProgressDialog();
                        }
                    },500);
                }
            }
        });

    }

    /**
     * 扫描设置
     */
    private void ScannerSetting(){
        //扫描设置
        scannerView.isShowResThumbnail(true);//是否显示扫描成功后的缩略图
        scannerView.setMediaResId(R.raw.beep);//设置扫描成功的声音
        scannerView.setDrawText("将二维码放入框内", true);
        scannerView.setDrawTextColor(Color.RED);//文字颜色
        scannerView.isScanFullScreen(false);///是否全屏扫描识别
        scannerView.isHideLaserFrame(false);//是否影藏扫描框
        scannerView.setLaserLineResId(R.mipmap.wx_scan_line);//设置扫描线
//        scannerView.isScanInvert(true);//扫描反色二维码
        scannerView.setCameraFacing(CameraFacing.BACK);
        scannerView.setLaserMoveSpeed(10);//速度
        scannerView.setLaserFrameTopMargin(50);//扫描框与屏幕上方距离
        scannerView.setLaserFrameSize(300, 400);//扫描框大小
        scannerView.setLaserFrameCornerLength(25);//设置4角长度
        scannerView.setLaserLineHeight(5);//设置扫描线高度
        scannerView.setLaserFrameCornerWidth(5);//设置4角宽度
    }

    @Override
    public void onSettingClick() {
        Intent intent = new Intent(this,EncodeActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.onPause();
    }
}