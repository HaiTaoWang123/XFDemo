package com.example.dxc.xfdemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.dxc.xfdemo.common.BaseActivity;
import com.example.dxc.xfdemo.model.TimeCountMDL;
import com.example.dxc.xfdemo.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

import static com.example.dxc.xfdemo.CountTimeActivity.getStringTime;

/**
 * @Class:
 * @Description:
 * @Author: haitaow(haitaow @ hpe.com)
 * @Date: 5/17/2018-3:58 PM.
 * @Version 1.0
 */
public class CountTimeActivity_2 extends BaseActivity implements View.OnClickListener {

    TextView tvTime, tvIntervalTime;
    Button btStart, btCount, btReset;
    RecyclerView rvCountTime;
    int timeCount = 0, intervalTimeCount = 0;
    //    Handler handler;
    List<TimeCountMDL> countedTimesList;
    CountTimeActivity.TimeAdapter timeAdapter;
    boolean isRunning = false;
    //    Timer timer;
    Thread myThread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentLayout(R.layout.activity_count_time_layout);
        setTitle("秒表（Thread+Handler）");
        setBackVisible(true, "返回");
        setSettingVisible(true, "Thread.postDela方式");

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
        timeAdapter = new CountTimeActivity.TimeAdapter(CountTimeActivity_2.this, countedTimesList);
        rvCountTime.setLayoutManager(new LinearLayoutManager(CountTimeActivity_2.this, LinearLayoutManager.VERTICAL, true));
        ViewGroup.LayoutParams layoutParams = this.rvCountTime.getLayoutParams();
        int showSize = countedTimesList.size();
        layoutParams.height = showSize * ScreenUtil.dp2px(CountTimeActivity_2.this, 60);
        this.rvCountTime.setLayoutParams(layoutParams);

        rvCountTime.setAdapter(timeAdapter);
    }

    @Override
    public void onSettingClick() {
        if (myThread != null) {
            myThread.interrupt();//暂停
            myThread = null;
        }
        Intent intent = new Intent(CountTimeActivity_2.this,CountTimeActivity_3.class);
        startActivity(intent);
    }


    private void updateTime(int timeCount, int intervalTimeCount, boolean isCount, boolean isReset) {

        if (isReset) {
            countedTimesList.clear();
            timeAdapter.notifyDataSetChanged();
            btStart.setText("开始");
            btCount.setEnabled(false);
            btReset.setEnabled(false);
//            isRunning = false;
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

    private void startTask() {
        if (!isRunning) {
            isRunning = true;
            myThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (isRunning){
                            Message message = new Message();
                            message.what = 0;
                            myHandler.sendMessage(message);
                            Thread.sleep(10);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            myThread.start();
            btStart.setText("计时中");
            btCount.setEnabled(true);
            btReset.setEnabled(true);
        } else {
            isRunning = false;
            if (myThread != null) {
                myThread.interrupt();
                myThread = null;
            }
            btStart.setText("暂停");
            btCount.setEnabled(false);
        }
    }

    @SuppressWarnings("HandlerLeak")
    public Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    timeCount++;
                    intervalTimeCount++;
                    updateTime(timeCount,intervalTimeCount,false,false);
                    break;
            }
        }
    };

    private void countTask() {
        updateTime(timeCount, intervalTimeCount, true, false);
    }

    private void resetTask() {
        isRunning = false;
        if (myThread != null) {
            myThread.interrupt();//暂停
            myThread = null;
        }

        timeCount = 0;
        intervalTimeCount = 0;
        updateTime(timeCount, intervalTimeCount, false, true);
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

    @Override
    protected void onDestroy() {
        if (myThread != null) {
            myThread.interrupt();//暂停
            myThread = null;
        }
        super.onDestroy();
    }
}
