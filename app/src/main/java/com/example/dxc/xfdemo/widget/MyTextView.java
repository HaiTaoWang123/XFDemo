package com.example.dxc.xfdemo.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @Class:
 * @Description:
 * @Author: haitaow(haitaow @ hpe.com)
 * @Date: 6/5/2018-4:05 PM.
 * @Version 1.0
 */
public class MyTextView extends android.support.v7.widget.AppCompatTextView {
    private boolean isTouchable = false;
    private boolean isDispatchable = false;

    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return isTouchable;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return isDispatchable;
    }
}
