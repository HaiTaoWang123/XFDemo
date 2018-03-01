package com.example.dxc.filedownloadtest.downloader;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.dxc.filedownloadtest.db.DownLoadFileDao;
import com.example.dxc.filedownloadtest.db.DownLoadInfoDao;
import com.example.dxc.filedownloadtest.model.DownLoadFileInfo;
import com.example.dxc.filedownloadtest.model.DownLoadTaskInfo;
import com.example.dxc.filedownloadtest.util.DownloadUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @Class: Downloader
 * @Description: 任务下载器
 * @Author: haitaow(haitaow@hpe.com)
 * @Date: 2/28/2018-11:03 AM.
 * @Version 1.0
 */

public class Downloader {
    public static final String TAG = "Downloader";
    private static final int THREAD_POOL_SIZE = 9;//线程池大小
    private static final int THREAD_NUM = 3;//每个文件3个线程下载
    private static final int GET_LENGTH_SUCCESS = 1;
    public static final Executor THREAD_POOL_EXECUTOR = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    private List<DownloadTask> downloadTasks;
    private InnerHandler handler = new InnerHandler();

    private DownLoadFileInfo downLoadFileInfo;//待下载文件
    private long downloadLength;//下载过程中记录已下载大小
    private long fileLength;
    private Context context;
    private String downloadPath;

    public Downloader(Context context, DownLoadFileInfo downLoadFileInfo) {
        this.context = context;
        this.downLoadFileInfo = downLoadFileInfo;
        this.downloadPath = DownloadUtils.getDownloadPath();
    }

    /**
     * 开始下载
     */
    public void download() {
        if (TextUtils.isEmpty(downloadPath)) {
            Toast.makeText(context, "未找到SD卡", Toast.LENGTH_SHORT).show();
            return;
        }
        if (downLoadFileInfo == null) {
            throw new IllegalArgumentException("download content can't be null");
        }
        new Thread() {
            @Override
            public void run() {
                //获取文件大小
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet request = new HttpGet(downLoadFileInfo.getUrl());
                HttpResponse response = null;
                try {
                    response = httpClient.execute(request);
                    fileLength = response.getEntity().getContentLength();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                } finally {
                    if (request != null) {
                        request.abort();
                    }
                }

                List<DownLoadTaskInfo> lists = DownLoadInfoDao.getInstance(context.getApplicationContext())
                        .getDownloadTaskInfoByUrl(downLoadFileInfo.getUrl());
                for (DownLoadTaskInfo info : lists) {
                    downloadLength += info.getDownloadLength();
                }

                //插入文件下载记录到数据库
                DownLoadFileDao.getInstance(context.getApplicationContext())
                        .insertDownloadFile(downLoadFileInfo);
                Message.obtain(handler, GET_LENGTH_SUCCESS).sendToTarget();
            }
        }.start();
    }


    private class InnerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_LENGTH_SUCCESS:
                    beginDownload();
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private void beginDownload() {
        Log.e(TAG, "beginDownload" + downLoadFileInfo.getUrl());
        downLoadFileInfo.setStatus(DownLoadFileInfo.Status.WAITING);
        long blockLength = fileLength / THREAD_NUM;
        for (int i = 0; i < THREAD_NUM; i++) {
            long beginPosition = i * blockLength;//每条线程下载的开始位置
            long endPosition = (i + 1) * blockLength;//每条线程下载的结束位置
            if (i == (THREAD_NUM - 1)) {
                endPosition = fileLength;//如果整个文件的大小不为线程个数的整数倍，则最后一个线程的结束位置即为文件的总长度
            }
            DownloadTask task = new DownloadTask(i, beginPosition, endPosition, this, context);
            task.executeOnExecutor(THREAD_POOL_EXECUTOR, downLoadFileInfo.getUrl());
            if (downloadTasks == null) {
                downloadTasks = new ArrayList<>();
            }
            downloadTasks.add(task);
        }
    }

    /**
     * 暂停下载
     */
    public void puse() {
        if (downloadTasks != null) {
            for (DownloadTask task : downloadTasks) {
                if (task != null && (task.getStatus() == AsyncTask.Status.RUNNING || !task.isCancelled())) {
                    task.cancel(true);
                }
            }
            downloadTasks.clear();
            downLoadFileInfo.setStatus(DownLoadFileInfo.Status.PAUSED);
            DownLoadFileDao.getInstance(context.getApplicationContext()).updateDownLoadFile(downLoadFileInfo);
        }
    }

    /**
     * 将已下载大小归零
     */
    protected synchronized void resetDownloadLength() {
        this.downloadLength = 0;
    }

    /**
     * 将已下载大小归零
     */
    protected synchronized void updateDownloadLength(long size) {
        this.downloadLength += size;
        //通知更新页面
        int percent = (int) ((float) downloadLength * 100 / (float) fileLength);
        downLoadFileInfo.setDownloadPercent(percent);
        if (percent == 100 || downloadLength == fileLength) {
            downLoadFileInfo.setDownloadPercent(100);//
            downLoadFileInfo.setStatus(DownLoadFileInfo.Status.FINISHED);
            DownLoadFileDao.getInstance(context.getApplicationContext()).updateDownLoadFile(downLoadFileInfo);
        }
        Intent intent = new Intent(Constants.DOWNLOAD_MSG);
        if (downLoadFileInfo.getStatus() == DownLoadFileInfo.Status.WAITING) {
            downLoadFileInfo.setStatus(DownLoadFileInfo.Status.DOWNLOADING);
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable("downloadFileInfo", downLoadFileInfo);
        intent.putExtras(bundle);
        context.sendBroadcast(intent);
    }

    protected String getDownloadPath() {
        return downloadPath;
    }
}
