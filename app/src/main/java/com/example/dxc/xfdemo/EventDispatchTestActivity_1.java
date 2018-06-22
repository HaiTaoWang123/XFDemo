package com.example.dxc.xfdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.dxc.xfdemo.apt.BaseDaoFactory;
import com.example.dxc.xfdemo.apt.IBaseDao;
import com.example.dxc.xfdemo.apt.User;
import com.example.dxc.xfdemo.common.BaseActivity;
import com.example.dxc.xfdemo.widget.MyLinerLayout;
import com.example.dxc.xfdemo.widget.MyTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Class:
 * @Description:
 * @Author: haitaow(haitaow @ hpe.com)
 * @Date: 6/5/2018-3:22 PM.
 * @Version 1.0
 */
public class EventDispatchTestActivity_1 extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.ll_activity)
    MyLinerLayout activity_ll;
    @BindView(R.id.ll_test1)
    MyLinerLayout test1_ll;
    @BindView(R.id.ll_test2)
    MyLinerLayout test2_ll;
    @BindView(R.id.ll_test3)
    MyLinerLayout test3_ll;
    @BindView(R.id.tv_test1)
    MyTextView test1_tv;
    @BindView(R.id.tv_test2)
    MyTextView test2_tv;
    @BindView(R.id.tv_test3)
    MyTextView test3_tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentLayout(R.layout.activity_event_dispatch_1);
        ButterKnife.bind(this);

        activity_ll.setOnClickListener(this);
        test1_ll.setOnClickListener(this);
        test2_ll.setOnClickListener(this);
        test3_ll.setOnClickListener(this);
        test1_tv.setOnClickListener(this);
        test2_tv.setOnClickListener(this);
        test3_tv.setOnClickListener(this);

        activity_ll.setTouchable(true);
        activity_ll.setDispatchTouchable(true);
        activity_ll.setInterceptable(true);

        IBaseDao sqLiteDatabase = BaseDaoFactory.getInstance().getBaseDao(User.class);
        sqLiteDatabase.insert(new User());
    }


    @Override
    public void onSettingClick() {

    }

    private void showToast(String msg) {
        if (msg != null) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "诶呀，出错了！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        showToast("点击了“" + v.getId() + "”");
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_DOWN){
//            showToast("点击了Activity");
            return super.dispatchTouchEvent(ev);
//        }
//        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_UP:
                showToast("点击了Activity");
//                break;
//            default:
//                break;
//        }
        return super.onTouchEvent(event);
    }
}
