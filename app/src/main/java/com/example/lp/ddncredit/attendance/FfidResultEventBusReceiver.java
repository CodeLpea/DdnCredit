package com.example.lp.ddncredit.attendance;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.example.lp.ddncredit.mainview.expression.ExpressionMessage;
import com.example.lp.ddncredit.rfid.RfidBean;
import com.example.lp.ddncredit.utils.SPUtil;
import com.example.lp.ddncredit.utils.voice.TtsSpeek;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Random;

import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.SP_NAME;
import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.VOICE_LEVEL;
import static com.example.lp.ddncredit.mainview.expression.ExpressionInterface.CreditExpressioneyeLeft;
import static com.example.lp.ddncredit.mainview.expression.ExpressionInterface.CreditExpressioneyeRght;
import static com.example.lp.ddncredit.mainview.expression.ExpressionInterface.SmiletExpression;


/**
 * 1、处理二维码和刷卡操作
 * 2、刷卡或二维码在连续间隔时间内多次刷卡或识别二维码，只处理第一次的数据
 * Created by Long on 2018/8/8.
 */

public class FfidResultEventBusReceiver {
    private static final String TAG = "ResultHandlerEventBusRe";
    private static FfidResultEventBusReceiver mInstance;
    private static final Object mLock = new Object();
    private static long mLastRfid = -1;
    private static long mLastRfidTime = 0;
    private Context mContext;

    private FfidResultEventBusReceiver() {

    }

    public static FfidResultEventBusReceiver getInstance() {
        synchronized (mLock) {
            if (mInstance == null) {
                mInstance = new FfidResultEventBusReceiver();
            }
            return mInstance;
        }
    }

    public void register() {
        Log.i(TAG, "register mInstance" + mInstance.toString());
        if (!EventBus.getDefault().isRegistered(mInstance)) {
            Log.i(TAG, "register mInstance ===> " + mInstance.toString());
            EventBus.getDefault().register(mInstance);
        }
    }

    public FfidResultEventBusReceiver inject(Context context) {
        mContext = context;
        return this;
    }

    public void unregister() {
        Log.i(TAG, "unregister mInstance" + mInstance.toString());
        if (EventBus.getDefault().isRegistered(mInstance)) {
            Log.i(TAG, "unregister mInstance ===> " + mInstance.toString());
            EventBus.getDefault().unregister(mInstance);
        }
    }

    public void post(Object event) {
        EventBus.getDefault().post(event);
    }


    /**
     * ThreadMode.BACKGROUND UI线程中post事件，单独开辟线程，子线程post事件，在至线程中执行
     * 因此post要等Subscribe执行完成之后才会返回
     *
     * @param rfidBean
     */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void rfidHandler(RfidBean rfidBean) {
        Log.i(TAG, "main thread id : " + Looper.getMainLooper().getThread().getId());
        Log.i(TAG, "current thread id : " + Thread.currentThread().getId());
        long rfidNumber = rfidBean.getRfid();
        Log.i(TAG, "rfidBean : " + rfidNumber + " mRfid");
        if (rfidNumber == mLastRfid) {
            //5s时间内连续刷同一张卡，不做处理
            if (Math.abs(System.currentTimeMillis() - mLastRfidTime) < 5 * 1000) {
                TtsSpeek.getInstance().SpeechAdd("请勿频繁刷卡", SPUtil.readInt(SP_NAME, VOICE_LEVEL));
                return;
            }
        }
        //有刷卡动作，让表情变化
        setExpression();


        //拿到卡号，对卡号进行判断，并考勤
        doAttend(rfidNumber);


    }

    private void doAttend(long rfidNumber) {
        if (rfidNumber != 0) {
            boolean isParentResult = false;
            boolean isStaffResult = false;
            //判断是家长打卡
            isParentResult = new ParentAttendManager().execute(rfidNumber);
            if (!isParentResult) {//如果不是家长，就判断是不是老师
                isStaffResult = new StaffAttendManager().execute(rfidNumber);
            }
            if (isParentResult == false && isStaffResult == false) {//既不是家长也不是老师，就播报此卡未绑定
                TtsSpeek.getInstance().SpeechFlush("此卡未绑定", SPUtil.readInt(SP_NAME, VOICE_LEVEL));
                TtsSpeek.getInstance().SpeechAdd(String.valueOf(rfidNumber), SPUtil.readInt(SP_NAME, VOICE_LEVEL));
            }
            mLastRfid = rfidNumber;
            mLastRfidTime = System.currentTimeMillis();
        } else {
            TtsSpeek.getInstance().SpeechFlush("读卡异常", SPUtil.readInt(SP_NAME, VOICE_LEVEL));
        }
    }

    private void setExpression() {
        Random random = new Random();
        int randomExpression = random.nextInt(3);//生成一个0到2之间的随机数用来，随机生成表情
        Log.i(TAG, "randomExpression: " + randomExpression);
        ExpressionMessage expressionMessage = new ExpressionMessage();//新建表情信息，以便Eventbus传递
        switch (randomExpression) {
            case 0:
                expressionMessage.setExpression(CreditExpressioneyeRght);
                Log.i(TAG, "随机表情眨右眼睛: ");
                break;
            case 1:
                expressionMessage.setExpression(CreditExpressioneyeLeft);
                Log.i(TAG, "随机表情眨左眼睛: ");
                break;
            case 2:
                expressionMessage.setExpression(SmiletExpression);
                Log.i(TAG, "随机表情微笑: ");
                break;
            default:
                expressionMessage.setExpression(SmiletExpression);
                Log.i(TAG, "随机表情微笑: ");
                break;
        }
        if (expressionMessage.getExpression() != null) {
            EventBus.getDefault().post(expressionMessage);
        } else {
            Log.i(TAG, "随机表情为空，默认眨右眼睛: ");
            expressionMessage.setExpression(CreditExpressioneyeRght);
            EventBus.getDefault().post(expressionMessage);
        }


    }

}
