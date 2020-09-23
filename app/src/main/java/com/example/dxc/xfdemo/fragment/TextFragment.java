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
 * Created by haitaow on 1/11/2018-10:17 AM.
 * Email: haitaow@hpe.com
 * version 1.0
 */

@SuppressLint("ValidFragment")
public class TextFragment extends EncodeBaseFragment {

    private EditText edText;
    private Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentView(R.layout.fragment_encode_text);
        initView(view);
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        edText = (EditText) view.findViewById(R.id.et_text);
    }

//    private void setEnable(){
//        if (edText != null && !edText.getText().toString().equals("")) {
//            setClearButtonEnable(true);
//            setEncodeButtonEnable(true);
//        } else {
//            setClearButtonEnable(false);
//            setEncodeButtonEnable(false);
//        }
//    }

    public TextFragment(Context context) {
        this.context = context;
    }

    @Override
    public void clearClickListener() {
        if (edText != null && !edText.getText().toString().equals("")) {
            edText.setText("");
        }
    }

    @Override
    public void encodingClickListener() {
        if (edText != null && !edText.getText().toString().equals("")) {
            Bitmap bitmap = new QREncode.Builder(context)
                    .setColor(context.getResources().getColor(R.color.colorPrimary))//二维码颜色
                    .setColors(0xFF0094FF, 0xFFFED545, 0xFF5ACF00, 0xFFFF4081)//二维码彩色
                    .setMargin(0)//二维码边框
                    //二维码类型
                    .setParsedResultType(ParsedResultType.TEXT)
                    //二维码内容
                    .setContents(edText.getText().toString())
                    .setSize(400)//二维码等比大小
                    .build().encodeAsBitmap();
            setEncodingResult(bitmap);
        }
    }

    @Override
    public boolean isEmpty() {
        if (edText != null && !edText.getText().toString().equals("")) {
            return true;
        }
        return false;
    }

}