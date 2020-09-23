package com.example.dxc.xfdemo;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.dxc.xfdemo.common.BaseActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author haitaow
 * @version 1.0.0
 * @createTime 2018/9/15-14:06
 * @description
 */
public class SpringBootActivity extends BaseActivity {

    TextView tvResponse;
//    Button btRequest;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentLayout(R.layout.activity_springboot);
        setTitle("二叉树遍历");
        setSettingVisible(false, "");
        tvResponse = (TextView) findViewById(R.id.tv_response);
        requestNetWork();
//        btRequest = (Button) findViewById(R.id.bt_springboot);
//        btRequest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
    }

    private void requestNetWork(){
        try {
            String url = "http://192.168.1.11:8080/user/findAllUser";
            URL url1 = new URL(url);
            HttpURLConnection urlConn = (HttpURLConnection) url1.openConnection();
            urlConn.setRequestProperty("content-type", "application/json");
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setConnectTimeout(5 * 1000);
            urlConn.setRequestMethod("GET");
            urlConn.setRequestProperty("Charset", "UTF-8");
            if (urlConn.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                String str;
                StringBuffer sb = new StringBuffer();
                while ((str = reader.readLine()) != null) {
                    sb.append(str);
                }
                tvResponse.setText(sb.toString());
                urlConn.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSettingClick() {

    }
}
