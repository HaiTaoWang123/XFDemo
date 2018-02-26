package com.example.dxc.filedownloadtest.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.example.dxc.filedownloadtest.R;

/**
 * Created by haitaow on 2/7/2018-3:22 PM.
 * Email: haitaow@hpe.com
 * version 1.0
 */

public class DownloadPercentView extends View {

    public final static int STATUS_PEDDING = 1;
    public final static int STATUS_WAITING = 2;
    public final static int STATUS_DOWNLOADING = 3;
    public final static int STATUS_PAUSED = 4;
    public final static int STATUS_FINISHED = 5;

    // 画实心圆的画笔
    private Paint mCirclePaint;
    // 画圆环的画笔
    private Paint mRingPaint;
    // 绘制进度文字的画笔
    private Paint mTxtPaint;
    // 圆形颜色
    private int mCircleColor;
    // 圆环颜色
    private int mRingColor;
    // 半径
    private int mRadius;
    // 圆环宽度
    private int mStrokeWidth = 2;
    // 圆心x坐标
    private int mXCenter;
    // 圆心y坐标
    private int mYCenter;
    // 总进度
    private int mTotalProgress = 100;
    // 当前进度
    private int mProgress;
    //下载状态
    private int mStatus = 1;

    //默认显示的图片
    private Bitmap mNotBeginImg;
    //暂停时中间显示的图片
    private Bitmap mPausedImg;
    //等待时显示的图片
    private Bitmap mWaitImg;
    //下载完成时显示的图片
    private Bitmap finishedImg;

    public DownloadPercentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获取自定义属性
        initAttrs(context, attrs);
        initVariable();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DownloadPercentView, 0, 0);
        mRadius = (int) typedArray.getDimension(R.styleable.DownloadPercentView_radius,100);
        mNotBeginImg = ((BitmapDrawable)typedArray.getDrawable(R.styleable.DownloadPercentView_notBeginImg)).getBitmap();
        mPausedImg = ((BitmapDrawable)typedArray.getDrawable(R.styleable.DownloadPercentView_pausedImg)).getBitmap();
        mWaitImg = ((BitmapDrawable)typedArray.getDrawable(R.styleable.DownloadPercentView_waitImg)).getBitmap();
        finishedImg = ((BitmapDrawable)typedArray.getDrawable(R.styleable.DownloadPercentView_finishedImg)).getBitmap();

        mNotBeginImg = big(mNotBeginImg,mRadius*2,mRadius*2);
        mPausedImg = big(mPausedImg,mRadius*2,mRadius*2);
        mWaitImg = big(mWaitImg,mRadius*2,mRadius*2);
        finishedImg = big(finishedImg,mRadius*2,mRadius*2);

        mStrokeWidth = (int)typedArray.getDimension(R.styleable.DownloadPercentView_strokeWidth, 2);
        mCircleColor = typedArray.getColor(R.styleable.DownloadPercentView_circleColor, 0xFFFFFFFF);
        mRingColor = typedArray.getColor(R.styleable.DownloadPercentView_ringColor, 0xFFFFFFFF);
    }

    private void initVariable() {
        //初始化绘制灰色圆的画笔
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(mStrokeWidth);

        //初始化绘制圆弧的画笔
        mRingPaint = new Paint();
        mRingPaint.setAntiAlias(true);
        mRingPaint.setColor(mRingColor);
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setStrokeWidth(mStrokeWidth);

        //初始化绘制文字的画笔
        mTxtPaint = new Paint();
        mTxtPaint.setAntiAlias(true);
        mTxtPaint.setColor(Color.parseColor("#52ce90"));
        mTxtPaint.setTextAlign(Paint.Align.CENTER);
        mTxtPaint.setTextSize(24);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = (int) (Math.ceil(mRadius)*2);
        setMeasuredDimension(width,width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mXCenter = getWidth()/2;
        mYCenter = getHeight()/2;
        switch (mStatus){
            case STATUS_PEDDING:
                canvas.drawBitmap(mNotBeginImg,0,0,null);
                break;
            case STATUS_WAITING:
                canvas.drawBitmap(mWaitImg,0,0,null);
                break;
            case STATUS_DOWNLOADING:
                drawDownloadingView(canvas);
                break;
            case STATUS_PAUSED:
                drawPausedView(canvas);
                break;
            case STATUS_FINISHED:
                canvas.drawBitmap(finishedImg,0,0,null);
                break;
        }
    }

    private void drawPausedView(Canvas canvas) {
        //绘制灰色圆环
        canvas.drawCircle(mXCenter,mYCenter,mRadius-mStrokeWidth/2,mCirclePaint);

        //绘制进度扇形圆环
        RectF oval = new RectF();
        //设置椭圆上下左右的坐标
        oval.left = mXCenter - mRadius + mStrokeWidth/2;
        oval.top = mYCenter - mRadius + mStrokeWidth/2;
        oval.right = mXCenter + mRadius - mStrokeWidth/2;
        oval.bottom = mYCenter + mRadius - mStrokeWidth/2;
        canvas.drawArc(oval,-90,((float)mProgress/mTotalProgress)*360,false,mRingPaint);

        //绘制中间暂停图标
        canvas.drawBitmap(mPausedImg,0,0,null);
    }

    private void drawDownloadingView(Canvas canvas) {
        //绘制灰色圆环
        canvas.drawCircle(mXCenter,mYCenter,mRadius-mStrokeWidth/2,mCirclePaint);

        //绘制进度扇形圆环
        RectF oval = new RectF();
        //设置椭圆上下左右的坐标
        oval.left = mXCenter - mRadius +mStrokeWidth/2;
        oval.top = mYCenter - mRadius + mStrokeWidth/2;
        oval.right = mXCenter + mRadius - mStrokeWidth/2;
        oval.bottom = mYCenter + mRadius - mStrokeWidth/2;
        canvas.drawArc(oval,-90,((float)mProgress/mTotalProgress)*360,false,mRingPaint);

        //绘制中间暂停图标
        canvas.drawBitmap(mPausedImg,0,0,null);
    }

    /**
     * 更新进度
     * @param progress
     */
    public void setProgress(int progress) {
        mProgress = progress;
        postInvalidate();
    }

    /**
     * 设置下载状态
     * @param status
     */
    public void setStatus(int status) {
        this.mStatus = status;
        postInvalidate();
    }

    public static Bitmap big(Bitmap b, float x, float y) {
        int w=b.getWidth();
        int h=b.getHeight();
        float sx=(float)x/w;
        float sy=(float)y/h;
        Matrix matrix = new Matrix();
        matrix.postScale(sx, sy); // 长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(b, 0, 0, w,
                h, matrix, true);
        return resizeBmp;
    }
}