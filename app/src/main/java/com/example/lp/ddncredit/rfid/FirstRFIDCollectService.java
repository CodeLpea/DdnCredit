package com.example.lp.ddncredit.rfid;

import android.app.Service;
import android.content.Intent;

import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


import com.example.lp.ddncredit.Utils.SPUtil;
import static com.example.lp.ddncredit.constant.Constants.SERIAL1_BAUDRATE;
import static com.example.lp.ddncredit.constant.Constants.SERIAL1_NAME;
import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.SP_NAME;

/**
 * Created by Long on 2018/9/3.
 */

public class FirstRFIDCollectService extends Service {
    private static final String TAG = "FirstRFIDCollectService";
    private static RFIDCollector mRfidCollector = null;
    private static boolean isRunning = false;

    public FirstRFIDCollectService(){

    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate...");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //启动刷卡
//        RFIDCollector mRfidCollector = new RFIDCollector(getApplicationContext());
//        mRfidCollector.setDevice("/dev/ttyS1")
//                .setBaudRate(19200)
//                .setFlags(0)
//                .setOnDataReceiveListener(new RFIDOnDataReceiveListener(getApplicationContext()))
//                .execute();
        isRunning = true;
        mRfidCollector = new RFIDCollector(getApplicationContext());
        mRfidCollector.setDevice("/dev/" + SPUtil.readString(SP_NAME, SERIAL1_NAME))
                .setBaudRate(Integer.valueOf(SPUtil.readString(SP_NAME, SERIAL1_BAUDRATE)))
                .setFlags(0)
                .setOnDataReceiveListener(new RFIDOnDataReceiveListener(getApplicationContext()))
                .execute();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy...");
        if(mRfidCollector != null){
            mRfidCollector.stop();
            mRfidCollector = null;
        }
    }

    public static boolean isServiceRunning(){
        return (mRfidCollector != null ? true : false);
    }
}
