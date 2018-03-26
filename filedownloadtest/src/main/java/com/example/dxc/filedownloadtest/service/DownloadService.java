package com.example.dxc.filedownloadtest.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * @Class:
 * @Description:
 * @Author: haitaow(haitaow@hpe.com)
 * @Date: 3/7/2018-2:21 PM.
 * @Version 1.0
 */

public class DownloadService extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
