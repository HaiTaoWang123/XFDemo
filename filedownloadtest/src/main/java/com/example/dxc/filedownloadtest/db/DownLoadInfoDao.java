package com.example.dxc.filedownloadtest.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.example.dxc.filedownloadtest.model.DownLoadTaskInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 记录每个单独线程下载信息的数据库操作类
 * Created by haitaow on 2/8/2018-9:40 AM.
 * Email: haitaow@hpe.com
 * version 1.0
 */

public class DownLoadInfoDao {
    private static final String TAG = "DownloadInfoDao";
    private static DownLoadInfoDao downLoadInfoDao = null;
    private Context context;

    private DownLoadInfoDao(Context context) {
        this.context = context;
    }

    synchronized public static DownLoadInfoDao getInstance(Context context) {
        if (downLoadInfoDao == null) {
            downLoadInfoDao = new DownLoadInfoDao(context);
        }
        return downLoadInfoDao;
    }

    /**
     * 获取数据库连接
     *
     * @return
     */
    public SQLiteDatabase getConnection() {
        SQLiteDatabase sqLiteDatabase = null;
        try {
            sqLiteDatabase = new DBHelper(context).getReadableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
        return sqLiteDatabase;
    }

    public void insertDownloadInfo(DownLoadTaskInfo downLoadTaskInfo) {
        if (downLoadTaskInfo == null) {
            return;
        }

        //如果本地已经存在直接修改
        if (getDownloadInfoByTaskIdAndUrl(downLoadTaskInfo.getTaskId(), downLoadTaskInfo.getUrl()) != null) {
            updateDownloadInfo(downLoadTaskInfo);
        }
        SQLiteDatabase database = getConnection();
        try {
            String sql = "insert into download_info(task_id, download_length, url,is_success) values (?,?,?,?)";
            Object[] bindArgs = {downLoadTaskInfo.getTaskId(), downLoadTaskInfo.getDownloadLength()
                    , downLoadTaskInfo.getUrl(), downLoadTaskInfo.isDownloadSuccess()};
            database.execSQL(sql, bindArgs);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        } finally {
            if (null != database) {
                database.close();
            }
        }
    }

    /**
     * 更新下载信息
     *
     * @param downLoadTaskInfo
     */
    private void updateDownloadInfo(DownLoadTaskInfo downLoadTaskInfo) {
        if (downLoadTaskInfo == null) {
            return;
        }
        SQLiteDatabase database = getConnection();
        try {
            String sql = "update download_info set download_length=?, is_success=? where task_id=? and url=?";
            Object[] bindArgs = {downLoadTaskInfo.getDownloadLength(), downLoadTaskInfo.isDownloadSuccess()
                    , downLoadTaskInfo.getTaskId(), downLoadTaskInfo.getUrl()};
            database.execSQL(sql, bindArgs);
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (null != database) {
                database.close();
            }
        }
    }

    /**
     * 根据taskid和url获取下载信息
     *
     * @param taskId
     * @param url
     * @return
     */
    public DownLoadTaskInfo getDownloadInfoByTaskIdAndUrl(int taskId, String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        SQLiteDatabase sqLiteDatabase = getConnection();
        DownLoadTaskInfo taskInfo = null;
        Cursor cursor = null;
        try {
            String sql = "select * from download_info where url=? and task_id=?";
            cursor = sqLiteDatabase.rawQuery(sql, new String[]{url, String.valueOf(taskId)});
            if (cursor.moveToNext()) {
                taskInfo = new DownLoadTaskInfo();
                taskInfo.setTaskId(cursor.getInt(1));
                taskInfo.setDownloadLength(cursor.getLong(2));
                taskInfo.setDownloadSuccess(cursor.getInt(4));
                taskInfo.setUrl(cursor.getString(3));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        return taskInfo;
    }

    /**
     * 通过URL查询某个下载任务的所有线程信息
     *
     * @param url
     * @return
     */
    public List<DownLoadTaskInfo> getDownloadTaskInfoByUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        SQLiteDatabase database = getConnection();
        List<DownLoadTaskInfo> taskInfoList = new ArrayList<>();
        Cursor cursor = null;
        try {
            String sql = "select * from download_info where url=?";
            cursor = database.rawQuery(sql, new String[]{url});
            while (cursor.moveToNext()) {
                DownLoadTaskInfo taskInfo = new DownLoadTaskInfo();
                taskInfo.setTaskId(cursor.getInt(1));
                taskInfo.setDownloadLength(cursor.getLong(2));
                taskInfo.setDownloadSuccess(cursor.getInt(4));
                taskInfo.setUrl(cursor.getString(3));
                taskInfoList.add(taskInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        } finally {
            if (database != null) {
                database.close();
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        return taskInfoList;
    }
}
