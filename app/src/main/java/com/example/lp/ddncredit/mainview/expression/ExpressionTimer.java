package com.example.lp.ddncredit.mainview.expression;
import android.util.Log;
import org.greenrobot.eventbus.EventBus;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifDrawable;

import static com.example.lp.ddncredit.mainview.expression.ExpressionInterface.CreditExpressioneyeRght;
import static com.example.lp.ddncredit.mainview.expression.ExpressionInterface.ExpressionList;

public class ExpressionTimer extends Timer {
    private static final String TAG = "ExpressionTimer";
    private ExpressionTimerTask mExpressionTimerTask;


    public ExpressionTimer() {
        mExpressionTimerTask = new ExpressionTimerTask();
    }

    public ExpressionTimer schedule() {
        this.schedule(mExpressionTimerTask, 10000, 30000);//每个20秒钟执行一次
        return this;
    }

    @Override
    public void schedule(TimerTask task, Date firstTime, long period) {
        Log.i(TAG, "ExpressionTimer schedule: ");
    }

    @Override
    public void cancel() {
        Log.i(TAG, "ExpressionTimer cancel: ");
    }

    private class ExpressionTimerTask extends TimerTask {//定时任务

        @Override
        public void run() {
            setExpression();
        }
    }

    private void setExpression() {
        Random random = new Random();
        int randomExpression = random.nextInt(ExpressionList.length - 1);//生成一个0到ExpressionList.length - 1之间的随机数用来，随机生成表情
        Log.i(TAG, "闲时随机表情: " + randomExpression);
        ExpressionMessage expressionMessage = new ExpressionMessage();//新建表情信息，以便Eventbus传递

        expressionMessage.setGifDrawable( ExpressionManager.getInstance().getGifDrawable(randomExpression));
        if (expressionMessage.getGifDrawable() != null) {
            EventBus.getDefault().post(expressionMessage);
        } else {
            Log.i(TAG, "随机表情为空，默认眨右眼睛: ");
        }

    }
}
