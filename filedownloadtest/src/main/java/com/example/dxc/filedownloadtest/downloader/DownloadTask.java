package com.example.dxc.filedownloadtest.downloader;

import android.content.Context;
import android.os.AsyncTask;

import com.example.dxc.filedownloadtest.db.DownLoadInfoDao;

/**
 * @Class: DownloadTask
 * @Description: 文件下载AsyncTask
 * @Author: haitaow(haitaow@hpe.com)
 * @Date: 2/28/2018-3:37 PM.
 * @Version 1.0
 */

public class DownloadTask extends AsyncTask<String, Integer, Long>{

    private int taskId;
    private long beginPosition;
    private long endPosition;
    private long downloadLength;
    private String url;
    private Downloader downloador;
    private DownLoadInfoDao downloadInfoDAO;


    public DownloadTask(int taskId, long beginPosition, long endPosition, Downloader downloador,
                        Context context) {
        this.taskId = taskId;
        this.beginPosition = beginPosition;
        this.endPosition = endPosition;
        this.downloador = downloador;
        downloadInfoDAO = DownLoadInfoDao.getInstance(context.getApplicationContext());
    }

    @Override
    protected Long doInBackground(String... strings) {
        return null;
    }
}
