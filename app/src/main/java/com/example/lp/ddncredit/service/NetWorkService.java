package com.example.lp.ddncredit.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.lp.ddncredit.Myapplication;
import com.example.lp.ddncredit.datebase.ParentAttendInfoDb;
import com.example.lp.ddncredit.datebase.StaffAttendInfoDb;
import com.example.lp.ddncredit.mainview.networkdetail.NetworkListener;
import com.example.lp.ddncredit.utils.NetUtil;
import com.example.lp.ddncredit.utils.TimeUtil;

import org.litepal.LitePal;

public class NetWorkService extends Service {
    private static final String TAG = "NetWorkService";

    private static boolean isRunning = false;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate...");
        isRunning = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        Log.i(TAG, "开启网络探测服务: ");
        Log.i(TAG, "onStartCommand...");
        if(mNetWorker == null){
            mNetWorker = new Thread(mNetWorkRunnable);
            mNetWorker.start();
        }
        int ret = super.onStartCommand(intent, flags, startId);
        Log.i(TAG, "onStartCommand ret = " + ret);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy...");
        isRunning = false;
        if(mNetWorker != null){
            try{
                mNetWorker.join();
            }catch(InterruptedException e){
                e.printStackTrace();
            }finally {
                mNetWorker = null;
            }
        }
    }

    public static boolean isServiceRunning(){
        return isRunning;
    }

    private Thread mNetWorker = null;
    private Runnable mNetWorkRunnable =  new Runnable() {
        @Override
        public void run() {
            while(isRunning){
                //先检查网络是否连通
                boolean result=checkNetWork();
                //获取还没有上传的数量
                checkUnUploadNumber();

                TimeUtil.delayMs(1000);
            }
        }
    };


    private boolean checkNetWork() {
        boolean result=false;
        result= NetUtil.isNetworkConnected(getApplicationContext());
        Intent intent = new Intent(NetworkListener.NETWORK_ACTION);
        intent.putExtra(NetworkListener.NETSTATUS, result);
        LocalBroadcastManager.getInstance(Myapplication.getInstance()).sendBroadcast(intent);//发送网络状态


        return result;
    }

    private void checkUnUploadNumber() {
        int countsParentAttendInfoDb= LitePal.count(ParentAttendInfoDb.class);
        int countsStaffAttendInfoDb=LitePal.count(StaffAttendInfoDb.class);
        int countsTotal=countsParentAttendInfoDb+countsStaffAttendInfoDb;
        Intent intent = new Intent(NetworkListener.NUMBER_ACTION);
        intent.putExtra(NetworkListener.NUMBER_NAME, countsTotal);
        LocalBroadcastManager.getInstance(Myapplication.getInstance()).sendBroadcast(intent);//发送未上传个数
    }
}
