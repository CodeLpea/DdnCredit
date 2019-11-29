package com.example.lp.ddncredit.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.lp.ddncredit.Myapplication;

import pl.droidsonroids.gif.GifImageView;

public class ScreeUtils {
    public static float leftXscale = (float) (320.0 / 1920.0);
    public static float leftYscale = (float) (140.0 / 1080.0);
    public static float Yscale = (float) (800.0 / 1080.0);
    public static float Xscale = (float) (1280.0 / 1920.0);

    /**
     * 获取屏幕的尺寸信息
     * */
    public static Point getDisplayMetrics() {
        DisplayMetrics dm = new DisplayMetrics();
        dm = Myapplication.getInstance().getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        return new Point(screenWidth, screenHeight);
    }

    /**
     * setScroolViewSize
     * 设置位置及其大小
     * 采用了Desity屏幕适配方法之后，就不比使用缩放比例的形式来适配大小和位置了
     * @param
     */
    public static void setLayout(ViewGroup view, Activity activity) {
        int NavigationBarHeight = getNavigationBarHeight();//获取底部状态栏高度
        Log.i("setLayout  ", NavigationBarHeight+"");
        Point displayMetrics = getDisplayMetrics();
        int height = displayMetrics.y;
        int width = displayMetrics.x;
        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(view.getLayoutParams());

//        int dpTop = dp2px((int) (leftYscale * height));
//        int dpLeft = dp2px((int) (leftXscale * width));
        int dpTop = (int) (leftYscale * height);
        int dpLeft = (int) (leftXscale * width);
        Log.e("setLayout", "dpTop :" + dpTop);
        Log.e("setLayout", "dpLeft :" + dpLeft);
        Log.e("setLayout", "(int) (leftYscale * height) :" + (int) (leftYscale * height));
        Log.e("setLayout", "(int) (leftXscale * width) :" + (int) (leftXscale * width));
//        margin.setMargins(dpLeft, dpTop, 0, 0);
        margin.setMargins(320, 140, 0, 0);

        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(margin);
//        layoutParams.width = (int) (Xscale * width);
//        layoutParams.height = (int) (Yscale * height);
        layoutParams.width = 1280;
        layoutParams.height = 800;
        Log.e("setLayout", " layoutParams.width :" + layoutParams.width);
        Log.e("setLayout", "layoutParams.height :" + layoutParams.height);
        view.setLayoutParams(layoutParams);
    }

    public static int dp2px(float dpValue) {
        final float scale = Myapplication.getInstance().getResources().getDisplayMetrics().density;

        return (int) (dpValue * scale + 0.5f);
    }

    public static int getNavigationBarHeight() {
        Resources resources = Myapplication.getInstance().getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        Log.v("dbw", "Navi height:" + height);
        return height;
    }
}
