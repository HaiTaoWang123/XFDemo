package com.example.dxc.filedownloadtest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dxc.filedownloadtest.R;
import com.example.dxc.filedownloadtest.model.DownLoadFileInfo;
import com.example.dxc.filedownloadtest.view.DownloadPercentView;

import java.util.List;

/**
 * @Class:
 * @Description:
 * @Author: haitaow(haitaow@hpe.com)
 * @Date: 3/7/2018-4:00 PM.
 * @Version 1.0
 */

public class DownloadAdapter extends BaseAdapter{
    private List<DownLoadFileInfo> fileInfoList = null;
    private Context mContext;

    public DownloadAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setDatas(List<DownLoadFileInfo> mDatas) {
        this.fileInfoList = mDatas;
    }

    @Override
    public int getCount() {
        return fileInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return fileInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_download,null);
            holder.statusIcon = (DownloadPercentView) convertView.findViewById(R.id.status_icon);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.downloadPercent = (TextView) convertView.findViewById(R.id.download_percent);
            holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressbar);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        setData(holder, position);
        return convertView;
    }

    /**
     * 设置viewHolder的数据
     * @param holder
     * @param itemIndex
     */
    private void setData(ViewHolder holder, int itemIndex) {
        DownLoadFileInfo appContent = fileInfoList.get(itemIndex);
        holder.name.setText(appContent.getName());
        holder.progressBar.setProgress(appContent.getDownloadPercent());
        setIconByStatus(holder.statusIcon, appContent.getStatus());
        if(appContent.getStatus() == DownLoadFileInfo.Status.PENDING) {
            holder.downloadPercent.setVisibility(View.INVISIBLE);
        } else {
            holder.downloadPercent.setVisibility(View.VISIBLE);
            holder.statusIcon.setProgress(appContent.getDownloadPercent());
            holder.downloadPercent.setText("下载进度：" + appContent.getDownloadPercent() + "%");
        }
    }

    /**
     * 局部刷新
     * @param view
     * @param itemIndex
     */
    public void updateView(View view, int itemIndex) {
        if(view == null) {
            return;
        }
        //从view中取得holder
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.statusIcon = (DownloadPercentView) view.findViewById(R.id.status_icon);
        holder.name = (TextView) view.findViewById(R.id.name);
        holder.downloadPercent = (TextView) view.findViewById(R.id.download_percent);
        holder.progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        setData(holder, itemIndex);
    }

    /**
     * 根据状态设置图标
     * @param downloadPercentView
     * @param status
     */
    private void setIconByStatus(DownloadPercentView downloadPercentView, DownLoadFileInfo.Status status) {
        downloadPercentView.setVisibility(View.VISIBLE);
        if(status == DownLoadFileInfo.Status.PENDING) {
            downloadPercentView.setStatus(DownloadPercentView.STATUS_PEDDING);
        }
        if(status == DownLoadFileInfo.Status.DOWNLOADING) {
            downloadPercentView.setStatus(DownloadPercentView.STATUS_DOWNLOADING);
        }
        if(status == DownLoadFileInfo.Status.WAITING) {
            downloadPercentView.setStatus(DownloadPercentView.STATUS_WAITING);
        }
        if(status == DownLoadFileInfo.Status.PAUSED) {
            downloadPercentView.setStatus(DownloadPercentView.STATUS_PAUSED);
        }
        if(status == DownLoadFileInfo.Status.FINISHED) {
            downloadPercentView.setStatus(DownloadPercentView.STATUS_FINISHED);
        }
    }

    private class ViewHolder {
        private DownloadPercentView statusIcon;
        private TextView name;
        private TextView downloadPercent;
        private ProgressBar progressBar;
    }

}
