package com.example.lp.ddncredit.mainview.fragment;


import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import pl.droidsonroids.gif.GifImageView;

public class BaseFragment extends Fragment {

    /**
     * 自定义GifImageView
     * 设置位置及其大小
     *
     * @param
     */
    protected void setGifViewSize(GifImageView view) {
        int NavigationBarHeight = getNavigationBarHeight();//获取底部状态栏高度
        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(view.getLayoutParams());
        int dpTop = dp2px(140);
        int dpLeft = dp2px(320);

        margin.setMargins(dpLeft, dpTop, 0, 0);

        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(margin);
        layoutParams.width = 1280;
        layoutParams.height = 800;
        view.setLayoutParams(layoutParams);
    }

    protected int dp2px(float dpValue) {
        final float scale = getActivity().getResources().getDisplayMetrics().density;

        return (int) (dpValue * scale + 0.5f);
    }

    protected int getNavigationBarHeight() {
        Resources resources = getActivity().getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        Log.v("dbw", "Navi height:" + height);
        return height;
    }
}
