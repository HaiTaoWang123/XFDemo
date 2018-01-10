package com.example.dxc.xfdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.dxc.xfdemo.common.BaseActivity;
import com.google.zxing.client.result.ParsedResultType;

/**
 * Created by wahaitao on 1/8/2018.
 */

public class ScannerResultActivity extends BaseActivity {
    private TextView tvResult;
    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentLayout(R.layout.activity_barcode_scanner_result);
        setTitle("扫描结果");
        setSettingVisible(false, "");
        tvResult = (TextView) findViewById(R.id.tv_result);
        webView = (WebView) findViewById(R.id.webView);

        Intent intent = getIntent();
        String result = intent.getStringExtra(ScannerTestActivity.SCANNER_RESULT);
        ParsedResultType type = (ParsedResultType) intent.getSerializableExtra(ScannerTestActivity.RESULT_TYPE);
        if (result != null && result != null) {
            handleResult(type, result);
//            tvResult.setText(result);
        } else {
            tvResult.setText("抱歉，扫描结果获取失败");
        }
    }

    private void handleResult(ParsedResultType type, String result) {
        switch (type) {
            case URI:
                webView.loadUrl(result);
                tvResult.setVisibility(View.GONE);
                WebSettings wSet = webView.getSettings();
                wSet.setJavaScriptEnabled(true);
                webView.setWebViewClient(new WebViewClient() {
                    public boolean shouldOverrideUrlLoading(WebView view, String url) { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                        if (url.startsWith("http:") || url.startsWith("https:")) {
                            view.loadUrl(url);
                            return false;
                        } else {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(intent);
                            return true;
                        }
                    }
                });
                break;
            case TEL:
                result = result.replace("TEL:", "电话号码：");
                tvResult.setText(result);
                tvResult.setClickable(true);
                final String tel = result.substring(result.lastIndexOf("：") + 1);
                webView.setVisibility(View.GONE);

                tvResult.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ContextCompat.checkSelfPermission(ScannerResultActivity.this,
                                Manifest.permission.CALL_PHONE)
                                != PackageManager.PERMISSION_GRANTED) {
                            //权限还没有授予，需要在这里写申请权限的代码
                            ActivityCompat.requestPermissions(ScannerResultActivity.this,
                                    new String[]{Manifest.permission.CALL_PHONE}, 60);
                        } else {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
                break;
            case SMS:
                result = result.replace("SMSTO:", "电话号码：");
                result = result.replace(":", "\n短信内容：");
                tvResult.setText(result);
                webView.setVisibility(View.GONE);

                final String phoneNum = result.substring(result.indexOf("：") + 1,result.indexOf("\n"));
                final String message = result.substring(result.lastIndexOf("：")+1);
                tvResult.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ContextCompat.checkSelfPermission(ScannerResultActivity.this,
                                Manifest.permission.SEND_SMS)
                                != PackageManager.PERMISSION_GRANTED) {
                            //权限还没有授予，需要在这里写申请权限的代码
                            ActivityCompat.requestPermissions(ScannerResultActivity.this,
                                    new String[]{Manifest.permission.SEND_SMS}, 60);
                        } else {
                            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNum));
                            intent.putExtra("sms_body",message);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//添加此举
                            startActivity(intent);
                        }
                    }
                });

                break;
           /* case WIFI:
                result = "名称："+result;
                result = result.replace("\n","\n密码：");
                int i = result.lastIndexOf("\n密码：");
                String typeStr = result.substring(i+5);
                result = result.substring(0,i)+typeStr;
                tvResult.setText(result);
                webView.setVisibility(View.GONE);
                break;*/
            /*case TEXT:
                tvResult.setText("类型："+type+"\n内容："+result);
                webView.setVisibility(View.GONE);
                break;*/
            default:
                tvResult.setText(result);
                webView.setVisibility(View.GONE);
                break;
        }
//        if (type.equals(URI.toString())) {
//            webView.loadUrl(result);
//            tvResult.setVisibility(View.GONE);
//            WebSettings wSet = webView.getSettings();
//            wSet.setJavaScriptEnabled(true);
//            webView.setWebViewClient(new WebViewClient() {
//                public boolean shouldOverrideUrlLoading(WebView view, String url) { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
//                    if (url.startsWith("http:") || url.startsWith("https:")) {
//                        view.loadUrl(url);
//                        return false;
//                    } else {
//                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                        startActivity(intent);
//                        return true;
//                    }
//                }
//            });
//        } else {
//            tvResult.setText(result);
//            webView.setVisibility(View.GONE);
//        }
    }

    @Override
    public void onSettingClick() {
        //TODO
    }
}
