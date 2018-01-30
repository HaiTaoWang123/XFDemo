package com.example.dxc.xfdemo.util;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class ScreenUtil {
    public static void prepareMatrix(Matrix matrix, boolean mirror, int displayOrientation,
            int viewWidth, int viewHeight) {
        // Need mirror for front camera.
        matrix.setScale(mirror ? -1 : 1, 1);
        // This is the value for android.hardware.Camera.setDisplayOrientation.
        matrix.postRotate(displayOrientation);
        // Camera driver coordinates range from (-1000, -1000) to (1000, 1000).
        // UI coordinates range from (0, 0) to (width, height).
        matrix.postScale(viewWidth / 2000f, viewHeight / 2000f);
        matrix.postTranslate(viewWidth / 2f, viewHeight / 2f);
    }

    public static float getScreenRate(Context context){
        Point P = getScreenMetrics(context);
        float H = P.y;
        float W = P.x;
        return (H/W);
    }

    /**
     * 获取屏幕宽度和高度，单位为px
     * @param context
     * @return
     */
    public static Point getScreenMetrics(Context context){
        DisplayMetrics dm =context.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        Log.i(TAG, "Screen---Width = " + w_screen + " Height = " + h_screen + " densityDpi = " + dm.densityDpi);
        return new Point(w_screen, h_screen);

    }
}
