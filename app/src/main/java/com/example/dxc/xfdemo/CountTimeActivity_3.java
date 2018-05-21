package com.example.dxc.xfdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.dxc.xfdemo.common.BaseActivity;
import com.example.dxc.xfdemo.model.TimeCountMDL;
import com.example.dxc.xfdemo.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @Class:
 * @Description:
 * @Author: haitaow(haitaow @ hpe.com)
 * @Date: 5/17/2018-3:58 PM.
 * @Version 1.0
 */
public class CountTimeActivity_3 extends BaseActivity implements View.OnClickListener {

    TextView tvTime, tvIntervalTime;
    Button btStart, btCount, btReset;
    RecyclerView rvCountTime;
    int timeCount = 0, intervalTimeCount = 0;
    //    Handler handler;
    List<TimeCountMDL> countedTimesList;
    CountTimeActivity.TimeAdapter timeAdapter;
    boolean isRunning = false;
    //    Timer timer;
//    Thread myThread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentLayout(R.layout.activity_count_time_layout);
        setTitle("秒表（Thread.postDelay）");
        setBackVisible(true, "返回");
        setSettingVisible(true, "");

        initViews();
    }

    private void initViews() {
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvIntervalTime = (TextView) findViewById(R.id.tv_interval_time);
        btStart = (Button) findViewById(R.id.bt_start);
        btCount = (Button) findViewById(R.id.bt_count);
        btReset = (Button) findViewById(R.id.bt_reset);
        rvCountTime = (RecyclerView) findViewById(R.id.rv_times);

        btStart.setOnClickListener(this);
        btCount.setOnClickListener(this);
        btReset.setOnClickListener(this);

        btCount.setEnabled(false);
        btReset.setEnabled(false);

//        timer = new Timer();

        countedTimesList = new ArrayList<>();
        timeAdapter = new CountTimeActivity.TimeAdapter(CountTimeActivity_3.this, countedTimesList);
        rvCountTime.setLayoutManager(new LinearLayoutManager(CountTimeActivity_3.this, LinearLayoutManager.VERTICAL, true));
        ViewGroup.LayoutParams layoutParams = this.rvCountTime.getLayoutParams();
        int showSize = countedTimesList.size();
        layoutParams.height = showSize * ScreenUtil.dp2px(CountTimeActivity_3.this, 60);
        this.rvCountTime.setLayoutParams(layoutParams);

        rvCountTime.setAdapter(timeAdapter);
    }

    @Override
    public void onSettingClick() {

    }



    private Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            if (isRunning) {
                Message message = new Message();
                message.what = 0;
                myHandler.sendMessage(message);
                myHandler.postDelayed(myRunnable, 10);
            }
        }
    };

    private void startTask() {
        if (!isRunning) {
            isRunning = true;
            myHandler.post(myRunnable);
            btStart.setText("计时中");
            btCount.setEnabled(true);
            btReset.setEnabled(true);
        } else {
            isRunning = false;
            stopTask();
            btStart.setText("暂停");
            btCount.setEnabled(false);
        }
    }

    @SuppressWarnings("HandlerLeak")
    public Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    timeCount++;
                    intervalTimeCount++;
                    updateTime(timeCount, intervalTimeCount, false, false);
                    break;
            }
        }
    };

    private void countTask() {
        updateTime(timeCount, intervalTimeCount, true, false);
    }

    private void resetTask() {
        isRunning = false;
        stopTask();
        timeCount = 0;
        intervalTimeCount = 0;
        updateTime(timeCount, intervalTimeCount, false, true);
    }

    private void stopTask() {
        myHandler.removeCallbacks(myRunnable);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_start:
                startTask();
                break;
            case R.id.bt_reset:
                resetTask();
                break;
            case R.id.bt_count:
                countTask();
                intervalTimeCount = 0;
                break;
            default:
                break;
        }
    }

    private void updateTime(int timeCount, int intervalTimeCount, boolean isCount, boolean isReset) {

        if (isReset) {
            countedTimesList.clear();
            timeAdapter.notifyDataSetChanged();
            btStart.setText("开始");
            btCount.setEnabled(false);
            btReset.setEnabled(false);
            isRunning = false;
            tvTime.setText("00:00.00");
            tvIntervalTime.setText("00:00.00");
        } else {
            tvTime.setText(getStringTime(timeCount));
            tvIntervalTime.setText(getStringTime(intervalTimeCount));
            if (isCount) {
                countedTimesList.add(new TimeCountMDL(getStringTime(timeCount), getStringTime(intervalTimeCount)));
                if (countedTimesList.size() > 0) {
                    timeAdapter.notifyItemInserted(countedTimesList.size() - 1);
                    ((LinearLayoutManager) rvCountTime.getLayoutManager()).scrollToPositionWithOffset(countedTimesList.size() - 1, 0);
                }
            }
        }
    }

    private String getStringTime(int timeCount) {
        int mSecond = (timeCount % 60) * 100 / 60;// 计算毫秒
        int second = (timeCount / 60) % 60;//计算秒
        int minute = timeCount / 3600;//计算分钟

        Log.e("时间:timeCount", timeCount + "");
        Log.e("时间:mSecond", mSecond + "");
        Log.e("时间:second", second + "");
        Log.e("时间:minute", minute + "");

        String sMinute = "";
        String sSecond = "";
        String sMsecond = "";
        if (minute < 10) {
            sMinute = "0" + minute;
        } else if (minute == 0) {
            sMinute = "00";
        } else if (minute >= 60) {
            sMinute = minute / 60 + ":" + minute % 60;
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

    @Override
    protected void onDestroy() {
        stopTask();
        myRunnable = null;
        super.onDestroy();
    }

}
