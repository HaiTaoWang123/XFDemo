package com.example.dxc.xfdemo.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.dxc.xfdemo.R;
import com.example.dxc.xfdemo.adapter.ARightAdapter;
import com.example.dxc.xfdemo.model.Stock;
import com.example.dxc.xfdemo.widget.MyHzScrollView;

import java.util.HashMap;

/**
 * @Class:
 * @Description:
 * @Author: haitaow(haitaow @ hpe.com)
 * @Date: 4/12/2018-4:40 PM.
 * @Version 1.0
 */
@SuppressLint("ValidFragment")
public class DispatchTouchEventFragment extends EncodeBaseFragment {
    private Context context;
    private EditText etTel;
    private MyHzScrollView hsTel;
    private ListView rvTel;
    private ARightAdapter rightAdapter;
    private float mParentWidth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentView(R.layout.fragment_encode_tel);
        setEncodingGone();
        initView(view);
//        setEnable();
    }

    public float getmParentWidth() {
        return mParentWidth;
    }

    public void setmParentWidth(float mParentWidth) {
        this.mParentWidth = mParentWidth;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        etTel = (EditText) view.findViewById(R.id.et_tel);
        hsTel = (MyHzScrollView) view.findViewById(R.id.hs_tel);
        rvTel = (ListView) view.findViewById(R.id.rv_tel);

        HashMap<Integer,Object> stocks = new HashMap<>();
        Stock stock = new Stock("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20");
        stocks.put(0,stock);
        stocks.put(1,stock);
        stocks.put(2,stock);
        stocks.put(3,stock);
        stocks.put(4,stock);
        stocks.put(5,stock);
        stocks.put(6,stock);
        stocks.put(7,stock);
        stocks.put(8,stock);
        stocks.put(9,stock);
        stocks.put(10,stock);
        rightAdapter = new ARightAdapter(context,stocks,false);
        hsTel.setmParentWidth(mParentWidth);
        rvTel.setAdapter(rightAdapter);
        hsTel.setSmoothScrollingEnabled(true);
    }

    private void setEnable() {
        if ((etTel != null && !etTel.getText().toString().equals(""))) {
            setClearButtonEnable(true);
            setEncodeButtonEnable(true);
        } else {
            setClearButtonEnable(false);
            setEncodeButtonEnable(false);
        }
    }

    public DispatchTouchEventFragment(Context context) {
        this.context = context;
    }

    @Override
    public void clearClickListener() {
        if ((etTel != null && !etTel.getText().toString().equals(""))) {
            etTel.setText("");
        }
    }

//    @Override
    public void encodingClickListener() {
//        if (!etTel.getText().toString().equals("")) {
//            Bitmap bitmap = new QREncode.Builder(context)
//                    .setColor(getResources().getColor(R.color.colorPrimary))//二维码颜色
//                    .setColors(0xFF0094FF, 0xFFFED545, 0xFF5ACF00, 0xFFFF4081)//二维码彩色
//                    .setMargin(0)//二维码边框
//                    二维码类型
//           //         .setParsedResultType(ParsedResultType.TEL)
//               //     二维码内容
//                    .setContents(etTel.getText().toString())
//                    .setSize(400)//二维码等比大小
//                    .build().encodeAsBitmap();
//            setEncodingResult(bitmap);
//        }
    }

    @Override
    public boolean isEmpty() {
        if ((etTel != null && !etTel.getText().toString().equals(""))) {
            return true;
        }
        return false;
    }
}
