package com.example.dxc.xfdemo.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * @Class:
 * @Description:
 * @Author: haitaow(haitaow @ hpe.com)
 * @Date: 6/5/2018-4:04 PM.
 * @Version 1.0
 */
public class MyLinerLayout extends LinearLayout {
    private boolean isTouchable = false;
    private boolean isInterceptable = false;
    private boolean isDispatchTouchable = false;

    public MyLinerLayout(Context context) {
        super(context);
    }

    public MyLinerLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLinerLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return isTouchable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isInterceptable;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return isDispatchTouchable;
    }

    public boolean isTouchable() {
        return isTouchable;
    }

    public void setTouchable(boolean touchable) {
        isTouchable = touchable;
    }

    public boolean isInterceptable() {
        return isInterceptable;
    }

    public void setInterceptable(boolean interceptable) {
        isInterceptable = interceptable;
    }

    public boolean isDispatchTouchable() {
        return isDispatchTouchable;
    }

    public void setDispatchTouchable(boolean dispatchTouchable) {
        isDispatchTouchable = dispatchTouchable;
    }
}
