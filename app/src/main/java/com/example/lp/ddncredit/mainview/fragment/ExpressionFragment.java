package com.example.lp.ddncredit.mainview.fragment;


import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.lp.ddncredit.MainActivity;
import com.example.lp.ddncredit.R;
import com.example.lp.ddncredit.mainview.expression.ExpressionMessage;
import com.example.lp.ddncredit.mainview.expression.ExpressionTimer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import static com.example.lp.ddncredit.mainview.expression.ExpressionInterface.ExpressionList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExpressionFragment extends Fragment {
    private static final String TAG = "ExpressionFragment";
    private GifImageView gifImageView;
    private ExpressionTimer mExpressionTimer;


    public ExpressionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: ");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expression, container, false);
        mExpressionTimer = new ExpressionTimer().schedule();//开启表情定时任务
        initView(view);

        return view;
    }

    /**
     * 初始化控件
     */
    private void initView(View view) {
        gifImageView = view.findViewById(R.id.giv_express);
        setViewSize(gifImageView);
        try {
            gifImageView.setImageDrawable((new GifDrawable(getActivity().getAssets(), ExpressionList[0])));//第一次先放个表情
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 处理考勤模块发来的表情变化信息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ExpressionEvent(ExpressionMessage expressionMessage) {
        Log.i(TAG, "表情界面得到表情变化:");
        if (expressionMessage.getExpression() != null && !expressionMessage.getExpression().isEmpty()) {
            try {
                gifImageView.setImageDrawable((new GifDrawable(getActivity().getAssets(), expressionMessage.getExpression())));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
        EventBus.getDefault().unregister(this);//在pause的时候注销eventbus

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
        EventBus.getDefault().register(this);//onResume中注册evnetbus


    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        super.onHiddenChanged(hidden);
        if (hidden) {//不在最前端界面显示，相当于调用了onPause(),但是不可提到onPause，因为回到系统主页面时，还是会掉onPause而不是onHiddenChanged
            Log.i(TAG, "不在最前端界面显示");

        } else {//重新显示到最前端 ,相当于调用了onResume()
            //进行网络数据刷新  此处执行必须要在 Fragment与Activity绑定了 即需要添加判断是否完成绑定，否则将会报空（即非第一个显示出来的fragment，虽然onCreateView没有被调用,
            //但是onHiddenChanged也会被调用，所以如果你尝试去获取活动的话，注意防止出现空指针）
            Log.i(TAG, "重新显示到最前端 ");

        }

    }

    /**
     * 自定义设置位置及其大小
     * @param
     */
    private void setViewSize(GifImageView view) {
        int NavigationBarHeight=getNavigationBarHeight();//获取底部状态栏高度
        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(view.getLayoutParams());
        int dpTop = dp2px( 140);
        int dpLeft=dp2px( 320);

        margin.setMargins(dpLeft, dpTop, 0, 0);

        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(margin);
        layoutParams.width = 1280;
        layoutParams.height= 800-NavigationBarHeight;
        view.setLayoutParams(layoutParams);
    }
    public  int dp2px(float dpValue){
        final float scale=getActivity().getResources().getDisplayMetrics().density;

        return (int)(dpValue*scale+0.5f);
    }

    private int getNavigationBarHeight() {
        Resources resources = getActivity().getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height","dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        Log.v("dbw", "Navi height:" + height);
        return height;
    }

    @Override
    public void onAttach(Activity activity) {
        Log.i(TAG, "ExpressionFragment onAttach: ");
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "ExpressionFragment onDetach: ");

    }
}
