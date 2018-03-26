package com.example.dxc.filedownloadtest.downloader;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.dxc.filedownloadtest.db.DownLoadInfoDao;
import com.example.dxc.filedownloadtest.model.DownLoadTaskInfo;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

/**
 * @Class: DownloadTask
 * @Description: 文件下载AsyncTask
 * @Author: haitaow(haitaow@hpe.com)
 * @Date: 2/28/2018-3:37 PM.
 * @Version 1.0
 */

public class DownloadTask extends AsyncTask<String, Integer, Long>{
    private static final String TAG = "DownloadTask";
    private int taskId;
    private long beginPosition;
    private long endPosition;
    private long downloadLength;
    private String url;
    private Downloader downloader;
    private DownLoadInfoDao downloadInfoDAO;


    public DownloadTask(int taskId, long beginPosition, long endPosition, Downloader downloader,
                        Context context) {
        this.taskId = taskId;
        this.beginPosition = beginPosition;
        this.endPosition = endPosition;
        this.downloader = downloader;
        downloadInfoDAO = DownLoadInfoDao.getInstance(context.getApplicationContext());
    }

    @Override
    protected void onPreExecute() {
        Log.e(TAG,"onPreExecute");
    }

    @Override
    protected void onPostExecute(Long aLong) {
        Log.e(TAG,url + "taskId:" + taskId +"executed");
//        downloader.updateDownloadInfo(null);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        //通知downloader增加已下载大小
        //downloader.updateDownloadLength(values[0]);
    }

    @Override
    protected void onCancelled() {
        Log.e(TAG,"onCancelled");
//        downloader.updateDownloadInfo(null);
    }

    @Override
    protected Long doInBackground(String... strings) {
        //这里加判断的作用是：如果还处于等待就暂停了，运行到这里已经cancel了，就直接退出
        if (isCancelled()){
            return null;
        }
        url = strings[0];
        if (url == null){
            return null;
        }

        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        HttpResponse response;
        InputStream is;
        RandomAccessFile fos = null;
        OutputStream output = null;

        DownLoadTaskInfo downLoadTaskInfo = null;

        try {
            //本地文件
            File file = new File(downloader.getDownloadPath() + File.separator + url.substring(url.lastIndexOf("/")+1));
            //获取之前结束的位置继续下载
            downLoadTaskInfo = downloadInfoDAO.getDownloadInfoByTaskIdAndUrl(taskId,url);
            //从之前结束的位置继续下载
            //这里加了判断file.exists(),判断是否被用户删除了，如果文件没有下载完，但是已经被用户删除了，则重新下载
            if (file.exists() && downLoadTaskInfo != null){
                if (downLoadTaskInfo.isDownloadSuccess() == 1){
                    //下载完成直接结束
                    return null;
                }
                beginPosition = beginPosition + downLoadTaskInfo.getDownloadLength();
                downloadLength = downLoadTaskInfo.getDownloadLength();
            }
            if (!file.exists()){
                //如果此task已经下载完，但是文件被用户删除，则需要重新设置已下载长度，重新下载
                downloader.resetDownloadLength();
            }

            //设置下载的数据位置beginPosition字节到endPosition字节
            Header header_size = new BasicHeader("Range","bytes="+beginPosition+"-"+endPosition);
            request.addHeader(header_size);
            //执行请求获取下载输入流
            response = client.execute(request);
            is = response.getEntity().getContent();

            //创建文件输入流
            fos = new RandomAccessFile(file,"rw");
            //从文件的size以后的位置开始写入
            fos.seek(beginPosition);

            byte buffer[] = new byte[1024];
            int inputSize = -1;
            while((inputSize = is.read(buffer)) != -1){
                fos.write(buffer,0,inputSize);
                downloadLength += inputSize;
                downloader.updateDownloadLength(inputSize);

                //如果暂停了，需要将下载信息存入数据库
                if (isCancelled()){
                    if (downLoadTaskInfo == null){
                        downLoadTaskInfo = new DownLoadTaskInfo();
                    }
                    downLoadTaskInfo.setUrl(url);
                    downLoadTaskInfo.setDownloadLength(downloadLength);
                    downLoadTaskInfo.setTaskId(taskId);
                    downLoadTaskInfo.setDownloadSuccess(0);
                    //保存下载信息到数据库
                    downloadInfoDAO.insertDownloadInfo(downLoadTaskInfo);
                    return null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (request != null){
                    request.abort();
                }
                if (output != null){
                    output.close();
                }
                if (fos != null){
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //执行到这里，说明该task已经下载完了
        if (downLoadTaskInfo == null){
            downLoadTaskInfo = new DownLoadTaskInfo();
        }
        downLoadTaskInfo.setUrl(url);
        downLoadTaskInfo.setDownloadLength(downloadLength);
        downLoadTaskInfo.setTaskId(taskId);
        downLoadTaskInfo.setDownloadSuccess(1);
        //保存下载信息到数据库
        downloadInfoDAO.insertDownloadInfo(downLoadTaskInfo);
        return null;
    }
}
