package com.example.dxc.filedownloadtest.model;

/**
 * @Class:
 * @Description:
 * @Author: haitaow(haitaow@hpe.com)
 * @Date: 2/8/2018-4:42 PM.
 * @Version 1.0
 */

public class DownLoadTaskInfo {

    private String url;  //下载的链接

    //负责下载的AsyncTask的编号，一个文件由3个线程下载，编号分别为0,、1、2
    private int taskId;

    //记录该线程已经下载的长度
    private long downloadLength;

    //标识该线程的下载任务是否完成
    private int downloadSuccess;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public long getDownloadLength() {
        return downloadLength;
    }

    public void setDownloadLength(long downloadLength) {
        this.downloadLength = downloadLength;
    }

    public int isDownloadSuccess() {
        return downloadSuccess;
    }

    public void setDownloadSuccess(int downloadSuccess) {
        this.downloadSuccess = downloadSuccess;
    }
}
