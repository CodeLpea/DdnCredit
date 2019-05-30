package com.example.lp.ddncredit.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.lp.ddncredit.datebase.ParentAttendInfoDb;
import com.example.lp.ddncredit.http.CloudClient;
import com.example.lp.ddncredit.http.UploadOssTask;
import com.example.lp.ddncredit.http.model.ParentAttendancedInfoEntry;
import com.example.lp.ddncredit.utils.NetUtil;
import com.example.lp.ddncredit.utils.TimeUtil;

import org.litepal.LitePal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Long on 2018/8/3.
 */

public class UploadParentAttendService extends Service {
    private static final String TAG = "UploadStuAttendancedInf";
    private ExecutorService mExecutorService = null;
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
        mExecutorService = Executors.newFixedThreadPool(10);
        isRunning = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        Log.i(TAG, "开启家长考勤上传服务: ");
        Log.i(TAG, "onStartCommand...");
        if(mUploadWorker == null){
            mUploadWorker = new Thread(mUploadRunnable);
            mUploadWorker.start();
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
        if(mExecutorService != null) {
            mExecutorService.shutdown();
        }
        if(mUploadWorker != null){
            try{
                mUploadWorker.join();
            }catch(InterruptedException e){
                e.printStackTrace();
            }finally {
                mUploadWorker = null;
            }
        }
    }

    public static boolean isServiceRunning(){
        return isRunning;
    }

    private Thread mUploadWorker = null;
    private Runnable mUploadRunnable =  new Runnable() {
        @Override
        public void run() {
            while(isRunning){
                //先检查网络是否连通
                boolean result=checkNetWork();
                if(!result){
                    TimeUtil.delayMs(500);
                    continue;
                }
                //从缓存数据库中读取数据
                ParentAttendInfoDb parentAttendInfoDb = LitePal.findFirst(ParentAttendInfoDb.class);
                if(parentAttendInfoDb != null){
                    Log.i(TAG, parentAttendInfoDb.toString());
                    String picOSSPath = null;
                    if(parentAttendInfoDb.getPicPath() != null){
                        //上传考勤照片到阿里云服务器
                        picOSSPath = UploadOssTask.getObjectKey(parentAttendInfoDb.getPicPath(), parentAttendInfoDb.getStudentID());
                        Log.i(TAG, "picOSSPath : " + picOSSPath);
                        //开启照片上传oss线程
                        if(picOSSPath != null) {
                            TimeUtil.delayMs(1000);//主要目的是等待照片保存在磁盘中
                            mExecutorService.submit(new UploadOssTask(getApplicationContext(), picOSSPath, parentAttendInfoDb.getPicPath()));
                        }
                    }
                    //上传考勤记录
                    ParentAttendancedInfoEntry parentAttendancedInfoEntry = new ParentAttendancedInfoEntry();
                    parentAttendancedInfoEntry.setStudentId(parentAttendInfoDb.getStudentID());
                    parentAttendancedInfoEntry.setParentId(parentAttendInfoDb.getParentID());
                    parentAttendancedInfoEntry.setImgUrl(picOSSPath);
                    parentAttendancedInfoEntry.setCreateTime(parentAttendInfoDb.getCreateTime());
                    //上传不成功。每间隔10s重传一次
                    boolean isUploadOk = false;
                    while(true){
                        isUploadOk = CloudClient.getInstance().uploadParentAttendancedInfo(parentAttendancedInfoEntry);
                        if(!isUploadOk){
                            int retry = 20;
                            while(isRunning && retry > 0){
                                TimeUtil.delayMs(500);
                                retry--;
                            }
                            continue;
                        }
                        break;
                    }

                    //上传成功,删除记录
                    if(isUploadOk){
                        Log.i(TAG, "上传成功,删除记录");
                        parentAttendInfoDb.delete();
                    }
                }
            }
        }
    };


    private boolean checkNetWork() {
        boolean result=false;
        result=NetUtil.isNetworkConnected(getApplicationContext());
        return result;
    }

}
