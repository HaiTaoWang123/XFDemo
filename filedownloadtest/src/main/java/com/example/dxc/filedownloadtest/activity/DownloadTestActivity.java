package com.example.dxc.filedownloadtest.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dxc.filedownloadtest.R;
import com.example.dxc.filedownloadtest.adapter.DownloadAdapter;
import com.example.dxc.filedownloadtest.db.DownLoadFileDao;
import com.example.dxc.filedownloadtest.downloader.Downloader;
import com.example.dxc.filedownloadtest.model.DownLoadFileInfo;
import com.example.dxc.filedownloadtest.util.DownloadUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Class:
 * @Description:
 * @Author: haitaow(haitaow@hpe.com)
 * @Date: 3/7/2018-3:59 PM.
 * @Version 1.0
 */

public class DownloadTestActivity extends Activity {
    public static final String TAG = "MainActivity";
    public static final String BROADCAST_MSG = "downloadFileInfo";
    public static final String DOWNLOAD_MSG = "com.example.dxc.filedownloadtest";
    private DownloadAdapter mDownloadAdapter;
    private ListView mListView;
    private List<DownLoadFileInfo> mFileInfoList;
    private Map<String, Downloader> downloaderMap;
    private int Downloading_Size = 0;
    private int Max_Downloading_Size = 4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.listview);
        mDownloadAdapter = new DownloadAdapter(DownloadTestActivity.this);
        mFileInfoList = DownloadUtils.getTestData();

        initStatus();

        mDownloadAdapter.setDatas(mFileInfoList);
        mListView.setAdapter(mDownloadAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DownLoadFileInfo fileInfo = mFileInfoList.get(position);
                if (downloaderMap != null && downloaderMap.containsKey(fileInfo.getName())) {
                    downloaderMap.get(fileInfo.getName()).puse();
//                    downloaderMap.remove(fileInfo.getName());
                    fileInfo.setStatus(DownLoadFileInfo.Status.PAUSED);
                    mDownloadAdapter.notifyDataSetChanged();
                } else {
                    if (downloaderMap == null) {
                        downloaderMap = new HashMap<>();
                    }
                    switch (fileInfo.getStatus()) {
                        case PENDING://此前还未开始
                            Downloader downloader1 = new Downloader(DownloadTestActivity.this, fileInfo);
                            downloaderMap.put(fileInfo.getName(), downloader1);
                            if (Downloading_Size <= Max_Downloading_Size) {
                                downloader1.download();
                                fileInfo.setStatus(DownLoadFileInfo.Status.DOWNLOADING);
                                Downloading_Size += 1;
                            } else {
                                fileInfo.setStatus(DownLoadFileInfo.Status.WAITING);
                            }
                            break;
                        case PAUSED:
                            Downloader downloader2;
                            if (downloaderMap.get(fileInfo.getName()) != null) {
                                downloader2 = downloaderMap.get(fileInfo.getName());
                            } else {
                                downloader2 = new Downloader(DownloadTestActivity.this, fileInfo);
                                downloaderMap.put(fileInfo.getName(), downloader2);
                            }
                            if (Downloading_Size < Max_Downloading_Size) {
                                downloader2.download();
                                Downloading_Size += 1;
                                fileInfo.setStatus(DownLoadFileInfo.Status.DOWNLOADING);
                            } else {
                                fileInfo.setStatus(DownLoadFileInfo.Status.WAITING);
                                downloader2.puse();
                            }
                            break;
                        case WAITING:
                            Downloader downloader3;
                            if (downloaderMap.get(fileInfo.getName()) != null) {
                                downloader3 = downloaderMap.get(fileInfo.getName());
                            } else {
                                downloader3 = new Downloader(DownloadTestActivity.this, fileInfo);
                                downloaderMap.put(fileInfo.getName(), downloader3);
                            }
                            if (Downloading_Size < Max_Downloading_Size) {
                                downloader3.download();
                                Downloading_Size += 1;
                                fileInfo.setStatus(DownLoadFileInfo.Status.DOWNLOADING);
                            } else {
                                fileInfo.setStatus(DownLoadFileInfo.Status.PAUSED);
                                downloader3.puse();
                            }
                            break;
                        case DOWNLOADING:
                            Downloader downloader4;
                            if (downloaderMap.get(fileInfo.getName()) != null) {
                                downloader4 = downloaderMap.get(fileInfo.getName());
                            } else {
                                downloader4 = new Downloader(DownloadTestActivity.this, fileInfo);
                                downloaderMap.put(fileInfo.getName(), downloader4);
                            }
                            downloader4.puse();
                            Downloading_Size -= 1;
                            fileInfo.setStatus(DownLoadFileInfo.Status.PAUSED);
                            break;
                        case FINISHED:
                            Toast.makeText(DownloadTestActivity.this, fileInfo.getName() + "已经下载完成", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            break;
                    }
//                    if (fileInfo.getStatus().equals(DownLoadFileInfo.Status.FINISHED)) {
//                        Toast.makeText(DownloadTestActivity.this, fileInfo.getName() + "已下载完成", Toast.LENGTH_SHORT).show();
//                    }
//
//                    fileInfo.setStatus(DownLoadFileInfo.Status.WAITING);
                    mDownloadAdapter.notifyDataSetChanged();
                }
            }
        });

        IntentFilter intent = new IntentFilter(DOWNLOAD_MSG);
        registerReceiver(downloadStatusReceiver, intent);
    }

    private void initStatus() {
        List<DownLoadFileInfo> fileInfoList = DownLoadFileDao.getInstance(this).getAll();
        for (DownLoadFileInfo fileInfo : fileInfoList) {
            for (DownLoadFileInfo downLoadFileInfo : mFileInfoList) {
                if (downLoadFileInfo.getUrl().equals(fileInfo.getUrl())) {
                    downLoadFileInfo.setStatus(fileInfo.getStatus());
                    downLoadFileInfo.setDownloadPercent(fileInfo.getDownloadPercent());
                    break;
                }
            }
            if (fileInfo.getStatus().equals(DownLoadFileInfo.Status.DOWNLOADING)) {
                Downloading_Size +=1;
            }
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(downloadStatusReceiver);
        super.onDestroy();
    }

    private BroadcastReceiver downloadStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            DownLoadFileInfo downLoadFileInfo = intent.getParcelableExtra(BROADCAST_MSG);
            if (downLoadFileInfo == null) {
                return;
            }
            int itemIndex = 0;
            for (DownLoadFileInfo fileInfo : mFileInfoList) {
                if (downLoadFileInfo.getUrl().equals(fileInfo.getUrl())) {
                    itemIndex = mFileInfoList.indexOf(fileInfo);
                    fileInfo.setDownloadPercent(downLoadFileInfo.getDownloadPercent());
                    fileInfo.setStatus(downLoadFileInfo.getStatus());
                    break;
                }
            }

            //如果还有靠前的处于等待的任务，就让等待的任务开始下载
            if (downLoadFileInfo.getStatus().equals(DownLoadFileInfo.Status.FINISHED)) {
                Downloading_Size -= 1;
                for (DownLoadFileInfo fileInfo : mFileInfoList) {
                    if (fileInfo.getStatus().equals(DownLoadFileInfo.Status.WAITING)) {
                        downloaderMap.get(fileInfo.getName()).download();
                        Downloading_Size += 1;
                        break;
                    }
                }
            }
            updateView(itemIndex);
        }
    };

    private void updateView(int itemIndex) {
        int visiblePosition = mListView.getFirstVisiblePosition();
        if (itemIndex - visiblePosition >= 0) {
            View view = mListView.getChildAt(itemIndex - visiblePosition);
            mDownloadAdapter.updateView(view, itemIndex);
        }
    }
}
