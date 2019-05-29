package com.example.lp.ddncredit.attendance;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.example.lp.ddncredit.utils.SPUtil;
import com.example.lp.ddncredit.voice.TtsSpeek;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.SP_NAME;
import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.VOICE_LEVEL;


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

    private FfidResultEventBusReceiver(){

    }

    public static FfidResultEventBusReceiver getInstance(){
        synchronized (mLock){
            if(mInstance == null){
                mInstance = new FfidResultEventBusReceiver();
            }
            return mInstance;
        }
    }

    public void register(){
        Log.i(TAG, "register mInstance" + mInstance.toString());
        if(!EventBus.getDefault().isRegistered(mInstance)) {
            Log.i(TAG, "register mInstance ===> " + mInstance.toString());
            EventBus.getDefault().register(mInstance);
        }
    }
    public void inject(Context context){
        mContext = context;
    }

    public void unregister(){
        Log.i(TAG, "unregister mInstance" + mInstance.toString());
        if(EventBus.getDefault().isRegistered(mInstance)) {
            Log.i(TAG, "unregister mInstance ===> " + mInstance.toString());
            EventBus.getDefault().unregister(mInstance);
        }
    }

    public void post(Object event){
        EventBus.getDefault().post(event);
    }


    /**
     * ThreadMode.BACKGROUND UI线程中post事件，单独开辟线程，子线程post事件，在至线程中执行
     * 因此post要等Subscribe执行完成之后才会返回
     * @param rfid
     */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void rfidHandler(Long rfid){
        Log.i(TAG, "main thread id : " + Looper.getMainLooper().getThread().getId());
        Log.i(TAG, "current thread id : " + Thread.currentThread().getId());
        long rfidNumber = rfid.longValue();
        Log.i(TAG, "rfid : " + rfidNumber + " mRfid");
            if (rfidNumber == mLastRfid) {
                //5s时间内连续刷同一张卡，不做处理
                if (Math.abs(System.currentTimeMillis() - mLastRfidTime) < 5 * 1000) {
                    return;
                }
            }
            if (rfidNumber != 0) {
                boolean isParentResult = false;
                boolean isStaffResult = false;
               //判断是家长打卡
                isParentResult = new ParentAttendManager().execute(rfid);
                if(!isParentResult){//如果不是家长，就判断是不是老师
                    isStaffResult=new StaffAttendManager().execute(rfid);
                }
                if (isParentResult == false&&isStaffResult==false) {//既不是家长也不是老师，就播报此卡未绑定
                    TtsSpeek.getInstance().SpeechAdd("此卡未绑定", SPUtil.readInt(SP_NAME,VOICE_LEVEL));
                    TtsSpeek.getInstance().SpeechAdd(String.valueOf(rfidNumber),SPUtil.readInt(SP_NAME,VOICE_LEVEL));
                }
                mLastRfid = rfidNumber;
                mLastRfidTime = System.currentTimeMillis();
            } else {
                TtsSpeek.getInstance().SpeechAdd("读卡异常", SPUtil.readInt(SP_NAME,VOICE_LEVEL));
            }

    }

}
