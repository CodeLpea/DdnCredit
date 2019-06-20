package com.example.lp.ddncredit.mainview.fragment;


import android.os.Bundle;
import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.lp.ddncredit.R;
import com.example.lp.ddncredit.mainview.expression.ExpressionManager;
import com.example.lp.ddncredit.mainview.expression.ExpressionMessage;
import com.example.lp.ddncredit.mainview.expression.ExpressionTimer;
import com.example.lp.ddncredit.mainview.view.adapter.AttendShowBean;
import com.example.lp.ddncredit.mainview.view.bgToast;
import com.example.lp.ddncredit.mainview.view.dialog.AttendDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import pl.droidsonroids.gif.GifImageView;
/**
 * 表情碎片
 * lp
 * 2019/5/30
 */
public class ExpressionFragment extends BaseFragment {
    private static final String TAG = "ExpressionFragment";
    private GifImageView gifImageView;
    private ExpressionTimer mExpressionTimer;

    private static long mLastRfidTime = 0;

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
        setGifViewSize(gifImageView);
        gifImageView.setImageDrawable((ExpressionManager.getInstance().getGifDrawable(0)));//第一次先放个表情

    }

    /**
     * 处理考勤模块发来的表情变化信息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ExpressionEvent(ExpressionMessage expressionMessage) {
        Log.i(TAG, "表情界面得到表情变化:");
        if (Math.abs(System.currentTimeMillis() - mLastRfidTime) < 5 * 1000) {
          //防止刷卡过快，频繁变化表情
            return;
        }
        if (expressionMessage.getGifDrawable() != null) {
            gifImageView.setImageDrawable(expressionMessage.getGifDrawable());
            mLastRfidTime=System.currentTimeMillis();
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

}
