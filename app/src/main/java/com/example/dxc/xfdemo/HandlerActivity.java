package com.example.dxc.xfdemo;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
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
    Button btHandler1, btHandler2, btHandler3,btHandler4;
    Handler handler1, handler4, handler3;
    int timer1 = 0, timer2 = 0;
    boolean Running1 = false, Running2 = false;
    MyThread myThread;
    Intent serviceIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentLayout(R.layout.activity_handler);

        btHandler1 = (Button) findViewById(R.id.bt_timer1);
        btHandler2 = (Button) findViewById(R.id.bt_timer2);
        btHandler3 = (Button) findViewById(R.id.bt_timer3);
        btHandler4 = (Button) findViewById(R.id.btn_modle_delete);

        serviceIntent = new Intent(getApplicationContext(),TimeService.class);
        handler1 = new Handler();
        btHandler1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Running1) {
                    handler1.removeCallbacks(runnable);
                    Running1 = false;
                    Toast.makeText(HandlerActivity.this, "总计" + timer1 + "秒", Toast.LENGTH_LONG).show();
                } else {
                    if (timer1 >0){
                        Toast.makeText(HandlerActivity.this, "此前计时" + timer1 + "秒", Toast.LENGTH_SHORT).show();
                    }
                    handler1.postDelayed(runnable, 1000);
                }
            }
        });

//        btHandler2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!Running2) {
//                    myThread = new MyThread();
//                    myThread.run();
//                    Running2 = true;
//                }
//                else if (myThread != null && !Running2){
//                    myThread.notify();
//                }
//                else {
//                    new Thread(new MyThread()).stop(new Throwable());
//                    Running2 = false;
//                    Message message = new Message();
//                    message.what = 2;
//                    handler.sendMessage(message);
//
//                    try {
//                        myThread.wait();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//
//        btHandler3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startService(serviceIntent);
//            }
//        });
//
//        btHandler4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new Thread(new MyThread2()).run();
//            }
//        });
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler1.postDelayed(this, 1000);
            Running1 = true;
            Message message = new Message();
            message.what = 1;
            runnableHandler.sendMessage(message);
        }
    };

    @SuppressLint("HandlerLeak")
    Handler runnableHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    timer1++;
                    Toast.makeText(HandlerActivity.this, "第" + timer1 + "秒", Toast.LENGTH_SHORT).show();
                    break;
                    default:
                        break;
            }
        }
    };

    static Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            new Handler().postDelayed(this, 1000);
            Message message = new Message();
            message.what = 1;
            handler2.sendMessage(message);
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

    public static class MyThread2 implements Runnable{
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                Message message = new Message();
                message.what = 1;
                handler2.sendMessage(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("HandlerLeak")
    static Handler handler2 = new Handler(){
        int i = 0;
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    i++;
                    System.out.println("第"+i+"秒");
                    break;
                default:
                    break;
            }
        }
    };

    @SuppressLint("HandlerLeak")
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
        Intent intent = new Intent(this,CountTimeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Running1) {
            handler1.removeCallbacks(runnable);
        }
        stopService(serviceIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(serviceIntent);
    }

    public static class TimeService extends Service{

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
//            new Thread(new MyThread2()).run();
            return null;
        }

        @Override
        public void onCreate() {
//            new Thread(new MyThread2()).run();
            Handler handler = new Handler();
            handler.postDelayed(runnable2, 1000);
            super.onCreate();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
//            new Thread(new MyThread2()).run();
            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        public void onStart(Intent intent, int startId) {
            super.onStart(intent, startId);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            handler2.removeCallbacks(runnable2);
        }
    }
}