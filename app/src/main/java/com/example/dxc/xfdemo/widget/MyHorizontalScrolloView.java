package com.example.dxc.xfdemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * @Class: MyHorizontalScrollow
 * @Description: 自定义HorizontalScrollView
 * @Author: haitaow(haitaow@hpe.com)
 * @Date: 3/30/2018-9:56 AM.
 * @Version 1.0
 */

public class MyHorizontalScrolloView extends HorizontalScrollView {

    private View mView;

    public MyHorizontalScrolloView(Context context) {
        super(context);
    }

    public MyHorizontalScrolloView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mView != null){
            mView.scrollTo(l,t);
        }
    }

    public void setScrollView(View mView) {
        this.mView = mView;
    }
}
