package com.example.dxc.xfdemo.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.example.dxc.xfdemo.R;
import com.google.zxing.client.result.ParsedResultType;
import com.mylhyl.zxing.scanner.encode.QREncode;

/**
 * Created by haitaow on 1/12/2018-7:21 PM.
 * Email: haitaow@hpe.com
 * version 1.0
 */
@SuppressLint("ValidFragment")
public class IsbnFragment extends EncodeBaseFragment {
    private Context context;
    private EditText etIsbn;

    public IsbnFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentView(R.layout.fragment_encode_isbn);
        etIsbn = (EditText) view.findViewById(R.id.et_isbn);
//        setEnable();
    }

    private void setEnable(){
        if (etIsbn != null && !etIsbn.getText().toString().equals("")) {
            setClearButtonEnable(true);
            setEncodeButtonEnable(true);
        } else {
            setClearButtonEnable(false);
            setEncodeButtonEnable(false);
        }
    }

    @Override
    public void clearClickListener() {
        if (etIsbn != null && !etIsbn.getText().toString().equals("")) {
            etIsbn.setText("");
        }
    }

    @Override
    public void encodingClickListener() {
        if (etIsbn != null && !etIsbn.getText().toString().equals("")) {
            Bitmap bitmap = new QREncode.Builder(context)
                    .setColor(getResources().getColor(R.color.colorPrimary))//二维码颜色
                    .setColors(0xFF0094FF, 0xFFFED545, 0xFF5ACF00, 0xFFFF4081)//二维码彩色
                    .setMargin(0)//二维码边框
                    //二维码类型
                    .setParsedResultType(ParsedResultType.ISBN)
                    //二维码内容
                    .setContents(etIsbn.getText().toString())
                    .setSize(400)//二维码等比大小
                    .build().encodeAsBitmap();
            setEncodingResult(bitmap);
        }
    }

    @Override
    public boolean isEmpty() {
        if (etIsbn != null && !etIsbn.getText().toString().equals("")) {
            return true;
        }
        return false;
    }
}
