package com.example.dxc.xfdemo.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.dxc.xfdemo.R;
import com.example.dxc.xfdemo.ScannerResultActivity;
import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.mylhyl.zxing.scanner.OnScannerCompletionListener;
import com.mylhyl.zxing.scanner.decode.QRDecode;

import java.io.ByteArrayOutputStream;

/**
 * Created by haitaow on 1/11/2018-2:25 PM.
 * Email: haitaow@hpe.com
 * version 1.0
 */

public abstract class EncodeBaseFragment extends Fragment {
    private FrameLayout frameLayout;
    private Button btClear, btEncoding;
    private ImageView ivEncodingResult;
    public View view;
    protected Context context;
    private ProgressDialog progressDialog;
    private LinearLayout llEncoding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_encode_base, null);
        context = view.getContext();
        initViews();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        if (view != null) {
//            ViewGroup viewGroup = (ViewGroup) view.getParent();
//            if (viewGroup != null) {
//                viewGroup.removeView(view);
//            }
//        } else {
//            view = inflater.inflate(R.layout.fragment_encode_base, container, false);
//            initView(view);
//        }
//        initView();
        return view;
    }

    private void initViews() {
        frameLayout = (FrameLayout) view.findViewById(R.id.fl_encode_content);
        btClear = (Button) view.findViewById(R.id.bt_clear);
        btEncoding = (Button) view.findViewById(R.id.bt_encoding);
        ivEncodingResult = (ImageView) view.findViewById(R.id.iv_encode_result);
        llEncoding = (LinearLayout) view.findViewById(R.id.ll_enconding);

        btClear.setOnClickListener(clickListener);
        btEncoding.setOnClickListener(clickListener);
        ivEncodingResult.setOnClickListener(clickListener);
    }

    protected void setBaseContentView(int layoutResId) {
        frameLayout.removeAllViews();
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(layoutResId, null);
        frameLayout.addView(v);
    }

    @Override
    public void onDestroyView() {
        try {
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            viewGroup.removeView(viewGroup);
        } catch (Exception e) {

        }
        super.onDestroyView();
    }

    protected void setEncodingResult(Bitmap bitmap) {
        if (bitmap != null) {
            ivEncodingResult.setImageBitmap(bitmap);
        } else {
            ivEncodingResult.setImageResource(R.mipmap.ic_default_encoderesult);
        }
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_clear:
                    if (isEmpty()) {
                        clearClickListener();
                        Toast.makeText(context,"清空内容",Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(context,"内容为空",Toast.LENGTH_LONG).show();
                    }
                    break;
                case R.id.bt_encoding:
                    if (isEmpty()) {
                        encodingClickListener();
                        Toast.makeText(context,"生成二维码",Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(context,"请输入内容",Toast.LENGTH_LONG).show();
                    }
                    break;
                case R.id.iv_encode_result:
                    if (ivEncodingResult.getResources() != null) {
                        encodedImageClickListener();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public abstract void clearClickListener();

    public abstract void encodingClickListener();

    /**
     * 文本框是否为空，为空提醒用户，不为空生成二维码或者清空内容
     * @return
     */
    public abstract boolean isEmpty();

    public void encodedImageClickListener() {
        Bitmap bitmap = null;
        ivEncodingResult.setDrawingCacheEnabled(true);//step 1
        if (ivEncodingResult != null && ivEncodingResult.getDrawingCache() != null) {
            bitmap = ivEncodingResult.getDrawingCache();//step 2
        }
        //step 3 转bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        ivEncodingResult.setDrawingCacheEnabled(false);//step 5
        byte[] bytes = baos.toByteArray();
        if (bytes != null && bytes.length > 0) {
            Bitmap bitmap1 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            if (bitmap != null) {
                QRDecode.decodeQR(bitmap1, new OnScannerCompletionListener() {
                    @Override
                    public void onScannerCompletion(Result result, ParsedResult parsedResult, Bitmap barcode) {
                        if (result != null && result.getText() != null && parsedResult.getType() != null) {
                            ScannerResultActivity.gotoActivity(getActivity(), result.getText(), parsedResult.getType());
                            dismissProgressDialog();
                        }
                    }
                });
            }
        }
    }

    public void initView(View view) {
    }


    public void setClearButtonEnable(boolean isEnable) {
        btClear.setEnabled(isEnable);
        btClear.setClickable(isEnable);
    }

    public void setEncodeButtonEnable(boolean isEnable) {
        btEncoding.setEnabled(isEnable);
        btEncoding.setClickable(isEnable);
    }

    protected void showProgressDialog() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("请稍候...");
        progressDialog.show();
    }

    protected void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    protected void setEncodingGone(){
        llEncoding.setVisibility(View.GONE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
    }
}
