package com.example.lp.ddncredit.rfid;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


/**
 * RFID开启接收服务
 * lp
 * 2019/05/27
 */
public class RFIDCollectService extends Service {
    private static final String TAG = "RFIDCollectService";
    private static RFIDCollector mRfidCollector = null;
    private static boolean isRunning = false;

    public RFIDCollectService(){

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
        isRunning = true;
        mRfidCollector = new RFIDCollector(getApplicationContext());
        mRfidCollector.setOnDataReceiveListener(new RfiDataReceiverListener()).execute();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy...");
        if(mRfidCollector != null){
            mRfidCollector.stop();
            mRfidCollector = null;
            ReaderOne.getInstance().close();
            ReaderM13Usb.getInstance().close();
        }
    }

    public static boolean isServiceRunning(){
        return (mRfidCollector != null ? true : false);
    }
}
