package com.example.dxc.xfdemo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dxc.xfdemo.common.BaseActivity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by haitaow on 2/1/2018-2:38 PM.
 * Email: haitaow@hpe.com
 * version 1.0
 */

public class FileDownloadTest extends BaseActivity implements View.OnClickListener {
    private TextView tvProgress1, tvProgress2;
    private ProgressBar progress1, progress2;
    private Button btDown1, btDown2;
    private Context context;
    private String[] urls = {"https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1347785317,4046817947&fm=27&gp=0.jpg",
            "https://mirrors.tuna.tsinghua.edu.cn/centos/filelist.gz",
            "https://mirrors.tuna.tsinghua.edu.cn/anaconda/miniconda/Miniconda-3.6.0-Linux-x86.sh"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentLayout(R.layout.activity_file_download_test);
        setTitle("文件下载测试");
        setSettingVisible(false, "");
        context = FileDownloadTest.this;
        checkPermission();
        initView();
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission
                .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //权限还没有授予，需要在这里写申请权限的代码
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 60);
        }
    }

    private void initView() {
        progress1 = (ProgressBar) findViewById(R.id.pb_down1);
        progress2 = (ProgressBar) findViewById(R.id.pb_down2);
        tvProgress1 = (TextView) findViewById(R.id.tv_download_progress1);
        tvProgress2 = (TextView) findViewById(R.id.tv_download_progress2);
        btDown1 = (Button) findViewById(R.id.bt_start_download1);
        btDown2 = (Button) findViewById(R.id.bt_start_download2);

        btDown1.setOnClickListener(this);
        btDown2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_start_download1:
                fileDownload(urls[0], progress1, tvProgress1);
                break;
            case R.id.bt_start_download2:
                fileDownload(urls[1], progress2, tvProgress2);
            default:
                break;
        }
    }

    private void fileDownload(final String url, final ProgressBar progressBar, final TextView tvProgress) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream inputStream;
                File file;
                RandomAccessFile savedFile = null;
                long fileLength;

                try {
                    fileLength = getFileLength(url);
                    final String fileName = getFileName(url);
                    long downLoadLength = 0;

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(url).build();
                    Response response = client.newCall(request).execute();
                    if (request != null && response.isSuccessful()) {
                        file = new File("/sdcard/" + fileName + "123");
                        savedFile = new RandomAccessFile(file, "rw");

                        inputStream = response.body().byteStream();
                        byte[] buffer = new byte[1024];
                        int len;

                        int total = 0;
                        while ((len = inputStream.read(buffer)) != -1) {
                            savedFile.write(buffer, 0, len);
                            total += len;
                            final int progress = (int) ((total + downLoadLength) * 100 / fileLength);
                            progressBar.setProgress(progress);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvProgress.setText(progress + "%");
                                }
                            });
                        }
                        // response.body().string()只能调用一次，再次调用报错。
                        // 写完后可以把body关了
                        response.body().close();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "下载成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (savedFile != null) {
                            savedFile.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    //获取文件长度
    private long getFileLength(String url) throws IOException {
        long contentLength = 0;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        // 有响应且不为空
        if (response != null && response.isSuccessful()) {
            contentLength = response.body().contentLength();
            response.body().close();
        }
        return contentLength;
    }

    // 得到的是 /xxx.xxx,注意有斜杠
    private String getFileName(String url) {
        return url.substring(url.lastIndexOf("/"));
    }


    @Override
    public void onSettingClick() {

    }
}
