package com.example.dxc.xfdemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

/**
 * @Class: MyHzScrollView
 * @Description: 自定义scrollView解决滑动冲突
 * @Author: haitaow(haitaow@hpe.com)
 * @Date: 4/12/2018-3:59 PM.
 * @Version 1.0
 */
public class MyHzScrollView extends HorizontalScrollView {
    private boolean mCanScroll = true;
    private float mDownX;
    private float mParentWidth;

    public MyHzScrollView(Context context) {
        super(context);
    }



    public MyHzScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    public float getmParentWidth() {
        return mParentWidth;
    }

    public void setmParentWidth(float mParentWidth) {
        this.mParentWidth = mParentWidth;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN){
            mDownX = ev.getX();
        }

        if (ev.getAction() == MotionEvent.ACTION_MOVE){
            int scrollX = getScrollX();
            if ((scrollX ==0 && mDownX - ev.getX()<=-10) || (getChildAt(0).getMeasuredWidth() <= (scrollX + mParentWidth) && mDownX -ev.getX()>=10)){
                mCanScroll = false;
            }
        }

        if (ev.getAction() == MotionEvent.ACTION_UP
                || ev.getAction() == MotionEvent.ACTION_CANCEL) {
            mCanScroll = true;
        }

        if (mCanScroll){
            //此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
            getParent().requestDisallowInterceptTouchEvent(true);
            return super.onTouchEvent(ev);
        }else {
            getParent().requestDisallowInterceptTouchEvent(false);
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}