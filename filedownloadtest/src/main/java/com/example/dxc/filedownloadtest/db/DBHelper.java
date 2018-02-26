package com.example.dxc.filedownloadtest.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by haitaow on 2/8/2018-9:29 AM.
 * Email: haitaow@hpe.com
 * version 1.0
 */

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "downLoadTest.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table download_info(_id INTEGER PRIMARY KEY AUTOINCREMENT,task_id INTEGER," +
                "download_length INTEGER, url VARCHAR(255), is_success INTEGER)");
        db.execSQL("create table download_file(_id INTEGER PRIMARY KEY AUTOINCREMENT,app_name VARCHAR(255)," +
                "url VARCHAR(255), download_percent INTEGER, status INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
