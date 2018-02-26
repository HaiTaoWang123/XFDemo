package com.example.dxc.filedownloadtest.model;


import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by haitaow on 2/8/2018-10:13 AM.
 * Email: haitaow@hpe.com
 * version 1.0
 */

@SuppressLint("ParcelCreator")
public class DownLoadFileInfo implements Serializable, Parcelable {
    private String name;
    private String url;
    private int downloadPercent = 0;
    private Status status = Status.PENDING;

    public DownLoadFileInfo(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getDownloadPercent() {
        return downloadPercent;
    }

    public void setDownloadPercent(int downloadPercent) {
        this.downloadPercent = downloadPercent;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
       /*将DownLoadFileInfo的成员写入Parcel，
         * 注：Parcel中的数据是按顺序写入和读取的，即先被写入的就会先被读取出来
         */
        dest.writeString(name);
        dest.writeString(url);
        dest.writeInt(downloadPercent);
        dest.writeValue(status);
    }

    //该静态域是必须要有的，而且名字必须是CREATOR，否则会出错
    public static final Parcelable.Creator<DownLoadFileInfo> CREATOR =
            new Parcelable.Creator<DownLoadFileInfo>() {

                @Override
                public DownLoadFileInfo createFromParcel(Parcel source) {
                    //从Parcel读取通过writeToParcel方法写入的AppContent的相关成员信息
                    String name = source.readString();
                    String url = source.readString();
                    int downLoadPercent = source.readInt();
                    Status status = (Status) source.readValue(new ClassLoader() {
                    });
                    DownLoadFileInfo downLoadFileInfo = new DownLoadFileInfo(name, url);
                    downLoadFileInfo.setDownloadPercent(downLoadPercent);
                    downLoadFileInfo.setStatus(status);
                    //增加更新读取到的信息，创建返回Person对象
                    return downLoadFileInfo;
                }

                @Override
                public DownLoadFileInfo[] newArray(int size) {
                    //返回AppContent对象数组
                    return new DownLoadFileInfo[size];
                }
            };

    public enum Status {
        /**
         * Indicates that the task has not been executed yet.
         */
        PENDING(1),
        /**
         * Indicates that the task is wating.
         */
        WAITING(2),
        /**
         * Indicates that the task is downloading.
         */
        DOWNLOADING(3),

        /**
         * Indicates that the task was paused.
         */
        PAUSED(4),

        /**
         * Indicates that the task has finished.
         */
        FINISHED(5);

        private int value;

        Status(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static Status getByValue(int value) {
            switch (value) {
                case 1:
                    return Status.PENDING;
                case 2:
                    return Status.WAITING;
                case 3:
                    return Status.DOWNLOADING;
                case 4:
                    return Status.PAUSED;
                case 5:
                    return Status.FINISHED;
            }
            return Status.PENDING;
        }
    }
}
