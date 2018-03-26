package com.example.dxc.xfdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dxc.xfdemo.common.BaseActivity;

/**
 * @Class:
 * @Description:
 * @Author: haitaow(haitaow@hpe.com)
 * @Date: 3/23/2018-2:39 PM.
 * @Version 1.0
 */

public class HandlerActivity extends BaseActivity {
    Button btHandler1, btHandler2, btHandler3;
    Handler handler1, handler2, handler3;
    int timer1 = 0, timer2 = 0;
    boolean Running1 = false, Running2 = false;
    MyThread myThread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentLayout(R.layout.activity_handler);

        btHandler1 = (Button) findViewById(R.id.bt_timer1);
        btHandler2 = (Button) findViewById(R.id.bt_timer2);
        btHandler3 = (Button) findViewById(R.id.bt_timer3);

        handler1 = new Handler();
        btHandler1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Running1) {
                    handler1.removeCallbacks(runnable);
                    Running1 = false;
                    Toast.makeText(HandlerActivity.this, "总计" + timer1 + "秒", Toast.LENGTH_LONG).show();
                } else {
                    handler1.postDelayed(runnable, 1000);

                }
            }
        });

        btHandler2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Running2) {
                    myThread = new MyThread();
                    myThread.run();
                    Running2 = true;
                }
                else if (myThread != null && !Running2){
                    myThread.notify();
                }
                else {
                    new Thread(new MyThread()).stop(new Throwable());
                    Running2 = false;
                    Message message = new Message();
                    message.what = 2;
                    handler.sendMessage(message);

                    try {
                        myThread.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler1.postDelayed(this, 1000);
            timer1++;
            Running1 = true;
            Toast.makeText(HandlerActivity.this, "第" + timer1 + "秒", Toast.LENGTH_SHORT).show();
        }
    };

    public class MyThread implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    timer2++;
                    Toast.makeText(HandlerActivity.this, "第" + timer2 + "秒", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(HandlerActivity.this, "总共" + timer2 + "秒", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onSettingClick() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Running1) {
            handler1.removeCallbacks(runnable);
        }
    }
}