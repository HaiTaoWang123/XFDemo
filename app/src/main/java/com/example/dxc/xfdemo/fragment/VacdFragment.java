package com.example.dxc.xfdemo.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.example.dxc.xfdemo.R;
import com.google.zxing.client.result.ParsedResultType;
import com.mylhyl.zxing.scanner.encode.QREncode;

/**
 * Created by haitaow on 1/11/2018-10:19 AM.
 * Email: haitaow@hpe.com
 * version 1.0
 */
@SuppressLint("ValidFragment")
public class VacdFragment extends EncodeBaseFragment {
    Context context;
    EditText etName, etTel, etAddress,etEmail;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentView(R.layout.fragment_encode_vcd);
        initView(view);
//        setEnable();
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        etName = (EditText) view.findViewById(R.id.et_name);
        etTel = (EditText) view.findViewById(R.id.et_tel);
        etAddress = (EditText) view.findViewById(R.id.et_address);
        etEmail = (EditText) view.findViewById(R.id.et_email);
    }

    private void setEnable(){
        if (etName != null && !etName.getText().toString().equals("")
                && etTel != null && !etTel.getText().toString().equals("")
                && etAddress != null && !etAddress.getText().toString().equals("")
                && etEmail != null && !etEmail.getText().toString().equals("")) {
            setClearButtonEnable(true);
            setEncodeButtonEnable(true);
        } else {
            setClearButtonEnable(false);
            setEncodeButtonEnable(false);
        }
    }

    public VacdFragment(Context context) {
        this.context = context;
    }

    @Override
    public void clearClickListener() {
        if (etName != null ||etTel != null || etAddress != null||etEmail !=null) {
            etTel.setText("");
            etName.setText("");
            etAddress.setText("");
            etEmail.setText("");
        }
    }

    @Override
    public void encodingClickListener() {
        if (!etTel.getText().toString().equals("")
                && !etName.getText().toString().equals("")
                && !etAddress.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")) {
            Bitmap bitmap = new QREncode.Builder(context)
                    .setColor(getResources().getColor(R.color.colorPrimary))//二维码颜色
                    .setColors(0xFF0094FF, 0xFFFED545, 0xFF5ACF00, 0xFFFF4081)//二维码彩色
                    .setMargin(0)//二维码边框
                    //二维码类型
                    .setParsedResultType(ParsedResultType.EMAIL_ADDRESS)
                    //二维码内容
                    .setContents(etName.getText().toString()+"\r\n"+etTel.getText().toString()+"\r\n"+etEmail.getText().toString()+"\r\n"+etAddress.getText().toString())
                    .setUseVCard(true)
                    .setSize(400)//二维码等比大小
                    .build().encodeAsBitmap();
            setEncodingResult(bitmap);
        }
    }

    @Override
    public boolean isEmpty() {
        if (etName != null && !etName.getText().toString().equals("")
                && etTel != null && !etTel.getText().toString().equals("")
                && etAddress != null && !etAddress.getText().toString().equals("")
                && etEmail != null && !etEmail.getText().toString().equals("")) {
            return true;
        }
        return false;
    }
}
