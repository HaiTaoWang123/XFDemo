package com.example.dxc.xfdemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.dxc.xfdemo.common.BaseActivity;
import com.example.dxc.xfdemo.util.ScreenUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Class: CountTimeActivity
 * @Description: 模仿秒表计次功能；
 * @Author: haitaow(haitaow@hpe.com)
 * @Date: 3/28/2018-5:12 PM.
 * @Version 1.0
 */

public class CountTimeActivity extends BaseActivity implements View.OnClickListener {
    TextView tvTime;
    Button btStart, btCount, btReset;
    RecyclerView rvCountTime;
    int minute = 0, second = 0, mSecond = 0, timeCount = 0;
    Handler handler;
    List<String> countTimes;
    TimeAdapter timeAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentLayout(R.layout.activity_count_time_layout);
        setTitle("秒表");
        setBackVisible(true, "返回");
        setSettingVisible(false, "");

        initViews();
    }

    private void initViews() {
        tvTime = (TextView) findViewById(R.id.tv_time);
        btStart = (Button) findViewById(R.id.bt_start);
        btCount = (Button) findViewById(R.id.bt_count);
        btReset = (Button) findViewById(R.id.bt_reset);
        rvCountTime = (RecyclerView) findViewById(R.id.rv_times);

        btStart.setOnClickListener(this);
        btCount.setOnClickListener(this);
        btReset.setOnClickListener(this);

        handler = new Handler();

        countTimes = new ArrayList<>();
        Collections.reverse(countTimes);
        timeAdapter = new TimeAdapter(CountTimeActivity.this, countTimes);
        rvCountTime.setLayoutManager(new LinearLayoutManager(CountTimeActivity.this, LinearLayoutManager.VERTICAL, false));
        ViewGroup.LayoutParams layoutParams = this.rvCountTime.getLayoutParams();
        int showSize = countTimes.size();
        layoutParams.height = showSize * ScreenUtil.dp2px(CountTimeActivity.this, 60);
        this.rvCountTime.setLayoutParams(layoutParams);

        rvCountTime.setAdapter(timeAdapter);
    }

    @Override
    public void onSettingClick() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_start:
                handler.postDelayed(runnable, 100);
                break;
            case R.id.bt_reset:
                //TODO
                handler.removeCallbacks(runnable);
                timeCount = 0;
                tvTime.setText("00:00.00");
                countTimes.clear();
                timeAdapter.notifyDataSetChanged();
                break;
            case R.id.bt_count:
                //TODO
                Message message = new Message();
                message.what = 2;
                timeHandler.sendMessage(message);
                break;
            default:
                break;
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(runnable, 100);
            Message message = new Message();
            message.what = 1;
            timeHandler.sendMessage(message);
        }
    };


    @SuppressLint("HandlerLeak")
    Handler timeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    timeCount++;
                    mSecond = timeCount % 9;
                    second = (timeCount / 10) % 60;
                    minute = timeCount / 600;
                    Log.e("时间:timeCount", timeCount + "");
                    Log.e("时间:mSecond", mSecond + "");
                    Log.e("时间:second", second + "");
                    Log.e("时间:minute", minute + "");
                    tvTime.setText(setTime(minute, second, mSecond));
                    break;
                case 2:
                    countTimes.add(setTime(minute, second, mSecond));
//                    timeAdapter.notifyDataSetChanged();
                    if (countTimes.size() > 0) {
                        timeAdapter.notifyItemInserted(countTimes.size()-1);
                        ((LinearLayoutManager) rvCountTime.getLayoutManager()).scrollToPositionWithOffset(countTimes.size() - 1, 0);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private String setTime(int minute, int second, int mSecond) {
        String sMinute = "";
        String sSecond = "";
        String sMsecond = "";
        if (minute < 10) {
            sMinute = "0" + minute;
        } else if (minute == 0) {
            sMinute = "00";
        } else {
            sMinute = minute + "";
        }

        if (second < 10) {
            sSecond = "0" + second;
        } else if (second == 0) {
            sSecond = "00";
        } else {
            sSecond = second + "";
        }

        if (mSecond < 10) {
            sMsecond = "0" + mSecond;
        } else if (mSecond == 0) {
            sMsecond = "00";
        } else {
            sMsecond = mSecond + "";
        }
        return sMinute + ":" + sSecond + "." + sMsecond;
    }

    private class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.ViewHolder> {
        private Context context;
        private List<String> times;

        public TimeAdapter(Context context, List<String> timeList) {
            this.context = context;
            this.times = timeList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_time_count, parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.tvCount.setText("计次" + (position + 1));
            holder.tvTime.setText(times.get(position));
        }

        @Override
        public int getItemCount() {
            return times.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView tvCount, tvTime;

            public ViewHolder(View itemView) {
                super(itemView);
                tvCount = (TextView) itemView.findViewById(R.id.tv_count);
                tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }
}
