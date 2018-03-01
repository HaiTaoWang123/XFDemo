package com.example.dxc.filedownloadtest.util;

import android.os.Environment;

import java.io.File;

/**
 * @Class:
 * @Description:
 * @Author: haitaow(haitaow@hpe.com)
 * @Date: 2/28/2018-3:52 PM.
 * @Version 1.0
 */

public class DownloadUtils {

    public static String getDownloadPath(){
        String downloadPath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            downloadPath = Environment.getExternalStorageState() + File.separator + "download";
        }
        return downloadPath;
    }
}
