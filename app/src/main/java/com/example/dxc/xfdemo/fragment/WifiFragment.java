package com.example.dxc.xfdemo.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.dxc.xfdemo.R;
import com.google.zxing.client.result.ParsedResultType;
import com.mylhyl.zxing.scanner.encode.QREncode;

/**
 * Created by haitaow on 1/11/2018-10:18 AM.
 * Email: haitaow@hpe.com
 * version 1.0
 */

@SuppressLint("ValidFragment")
public class WifiFragment extends EncodeBaseFragment {
    Context context;
    EditText etName, etPassword;
    Spinner spType;

    public WifiFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentView(R.layout.fragment_encode_wifi);
        initView(view);
//        setEnable();
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        etName = (EditText) view.findViewById(R.id.et_count_name);
        etPassword = (EditText) view.findViewById(R.id.et_password);
        spType = (Spinner) view.findViewById(R.id.sp_type);
        spType.setSelection(0,true);
    }

    private void setEnable(){
        if (etName != null && !etName.getText().toString().equals("") && etPassword != null && !etPassword.getText().toString().equals("")){
            setClearButtonEnable(true);
            setEncodeButtonEnable(true);
        }else {
            setClearButtonEnable(false);
            setEncodeButtonEnable(false);
        }
    }

    @Override
    public void clearClickListener() {
        if (etName != null && etPassword != null){
            etName.setText("");
            etPassword.setText("");
        }
    }

    @Override
    public void encodingClickListener() {
        if (!etName.getText().toString().equals("")
                && !etPassword.getText().toString().equals("")) {

            Bitmap bitmap = new QREncode.Builder(context)
                    .setColor(getResources().getColor(R.color.colorPrimary))//二维码颜色
                    .setColors(0xFF0094FF, 0xFFFED545, 0xFF5ACF00, 0xFFFF4081)//二维码彩色
                    .setMargin(0)//二维码边框
                    //二维码类型
                    .setParsedResultType(ParsedResultType.WIFI)
                    //二维码内容
                    .setContents(etName.getText().toString()+"\r\n"+etPassword.getText().toString()+"\r\n"+getResources().getStringArray(R.array.wifi_type)[(int) spType.getSelectedItemId()] )
                    .setUseVCard(true)
                    .setSize(400)//二维码等比大小
                    .build().encodeAsBitmap();
            setEncodingResult(bitmap);
        }
    }

    @Override
    public boolean isEmpty() {
        if (etName != null && !etName.getText().toString().equals("")
                && etPassword != null && !etPassword.getText().toString().equals("")){
            return false;
        }
        return true;
    }
}
