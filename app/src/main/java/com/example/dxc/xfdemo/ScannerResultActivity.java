package com.example.dxc.xfdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.example.dxc.xfdemo.common.BaseActivity;

/**
 * Created by wahaitao on 1/8/2018.
 */

public class ScannerResultActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentLayout(R.layout.activity_barcode_scanner_result);
        setTitle("扫描结果");
        setSettingVisible(false,"");
        TextView tvResult = (TextView) findViewById(R.id.tv_result);
        Bundle bundle = getIntent().getExtras();
        String result = bundle.getString(ScannerTestActivity.SCANNER_RESULT);

        if (result != null){
            tvResult.setText(result);
        }else {
            tvResult.setText("抱歉，扫描结果获取失败");
        }
    }

    @Override
    public void onSettingClick() {
        //TODO
    }
}
