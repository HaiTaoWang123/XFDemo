package com.example.dxc.filedownloadtest.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.example.dxc.filedownloadtest.model.DownLoadFileInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 每个文件下载状态记录的数据库操作类
 * Created by haitaow on 2/8/2018-9:40 AM.
 * Email: haitaow@hpe.com
 * version 1.0
 */

public class DownLoadFileDao {
    private static final String TAG = "DownLoadFileDao";
    private static DownLoadFileDao downLoadFileDao = null;
    private Context context;

    public DownLoadFileDao(Context context) {
        this.context = context;
    }

    /**
     * 线程安全的懒汉式单例模式
     *
     * @param context
     * @return
     */
    synchronized public static DownLoadFileDao getInstance(Context context) {
        if (downLoadFileDao == null) {
            downLoadFileDao = new DownLoadFileDao(context);
        }
        return downLoadFileDao;
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

    /**
     * 插入数据
     *
     * @param fileInfo
     */
    public void insertDownloadFile(DownLoadFileInfo fileInfo) {
        if (fileInfo == null) {
            return;
        }
        //如果是本地已经存在，直接修改
        if (getDownLoadFileInfo(fileInfo.getUrl()) != null) {
            updateDownLoadFile(fileInfo);
            return;
        }

        SQLiteDatabase database = getConnection();
        try {
            String sql = "insert into download_file(app_name, url, download_percent, status) values(?,?,?,?)";
            Object[] bindArgs = {fileInfo.getName(), fileInfo.getUrl(), fileInfo.getDownloadPercent(), fileInfo.getStatus().getValue()};
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
     * @param fileInfo
     */
    public void updateDownLoadFile(DownLoadFileInfo fileInfo) {
        if (fileInfo == null) {
            return;
        }
        SQLiteDatabase database = getConnection();
        try {
            String sql = "update download_file set app_name=?, url=?, download_percent=?, status=? where url=?";
            Object[] bindArgs = {fileInfo.getName(), fileInfo.getUrl(), fileInfo.getDownloadPercent(), fileInfo.getStatus().getValue(), fileInfo.getUrl()};
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
     * 根据url获取下载文件信息
     *
     * @param url
     * @return
     */
    public DownLoadFileInfo getDownLoadFileInfo(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        SQLiteDatabase database = getConnection();
        DownLoadFileInfo fileInfo = null;
        Cursor cursor = null;
        try {
            String sql = "select * from download_file where url=?";
            cursor = database.rawQuery(sql, new String[]{url});
            if (cursor.moveToNext()) {
                fileInfo = new DownLoadFileInfo(cursor.getString(1), cursor.getString(2));
                fileInfo.setDownloadPercent(cursor.getInt(3));
                fileInfo.setStatus(DownLoadFileInfo.Status.getByValue(cursor.getInt(4)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        } finally {
            if (null != database) {
                database.close();
            }
            if (null != cursor) {
                cursor.close();
            }
        }
        return fileInfo;
    }

    public List<DownLoadFileInfo> getAll() {
        List<DownLoadFileInfo> fileInfoList = new ArrayList<>();
        SQLiteDatabase database = getConnection();
        Cursor cursor = null;
        try {
            String sql = "select * from download_file";
            cursor = database.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                DownLoadFileInfo fileInfo = new DownLoadFileInfo(cursor.getString(1), cursor.getString(2));
                fileInfo.setDownloadPercent(cursor.getInt(3));
                fileInfo.setStatus(DownLoadFileInfo.Status.getByValue(cursor.getInt(4)));
                fileInfoList.add(fileInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        } finally {
            if (null != database) {
                database.close();
            }
            if (null != cursor) {
                cursor.close();
            }
        }
        return fileInfoList;
    }

    /**
     * 根据url删除记录
     *
     * @param url
     */
    public void delByUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        SQLiteDatabase database = getConnection();
        try {
            String sql = "delete from download_file where url=?";
            Object[] bindArgs = {url};
            database.execSQL(sql, bindArgs);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        } finally {
            if (null != database) {
                database.close();
            }
        }
    }
}