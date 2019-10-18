package com.example.lp.ddncredit.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.lp.ddncredit.Myapplication;
import com.example.lp.ddncredit.rfid.RFIDCollectService;
import com.example.lp.ddncredit.versioncontrol.SoftWareUpgradeService;
import com.example.lp.ddncredit.websocket.service.WebSocketService;

/**
 * 服务管理
 * lp
 * 2019/05/28
 */

public class ServiceManager {
    private static final String TAG = "ServiceManager";
    private Context mContext = null;
    
    private static ServiceManager instance;
    // 单例模式中获取唯一的ServiceManager实例
    public static ServiceManager getInstance() {
        if (instance == null) {
            instance = new ServiceManager(Myapplication.getInstance());
        }
        return instance;
    }
    public ServiceManager(Context context) {
        mContext = context;
    }
    public void startServices(){
        if(mContext == null){
            Log.i(TAG, "=======startServices============>mConetxt = " + mContext);
            return;
        }
        Log.i(TAG, "startServices: ");
       //刷卡服务
        if(!RFIDCollectService.isServiceRunning()) {
            Intent firstRfidIntent = new Intent(mContext, RFIDCollectService.class);
            mContext.startService(firstRfidIntent);
        }
        //版本控制服务
        if(!SoftWareUpgradeService.isServiceRunning()) {
            Intent SoftWareIntent = new Intent(mContext, SoftWareUpgradeService.class);
            mContext.startService(SoftWareIntent);
        }
        //下载信息服务
        if(!UpdateSchoolInfoService.isServiceRunning()) {
            Intent UpdateInfoIntent = new Intent(mContext, UpdateSchoolInfoService.class);
            mContext.startService(UpdateInfoIntent);
        }

        //上传考勤服务
        if(!UploadAttendService.isServiceRunning()) {
            Intent UpdateParentInfoIntent = new Intent(mContext, UploadAttendService.class);
            mContext.startService(UpdateParentInfoIntent);
        }

        //开启网络探测，未上传信息数量报告服务
        if(!NetWorkService.isServiceRunning()) {
            Intent netWorkIntent = new Intent(mContext, NetWorkService.class);
            mContext.startService(netWorkIntent);
        }
        //开启WebSorket服务
        if(!WebSocketService.isServiceRunning()) {
            Intent websorketIntent = new Intent(mContext, WebSocketService.class);
            mContext.startService(websorketIntent);
        }

    }

    public void stopServices(){
        if(mContext == null){
            Log.i(TAG, "=======stopServices============>mConetxt = " + mContext);
            return;
        }
        Log.i(TAG, "stopServices: ");
        // 停止刷卡服务
        if(RFIDCollectService.isServiceRunning()) {
            Intent firstRfidIntent = new Intent(mContext, RFIDCollectService.class);
            mContext.stopService(firstRfidIntent);
        }
        // 停止版本控制服务
        if(SoftWareUpgradeService.isServiceRunning()) {
            Intent SoftWareIntent = new Intent(mContext, SoftWareUpgradeService.class);
            mContext.stopService(SoftWareIntent);
        }
        //停止下载信息服务
        if(UpdateSchoolInfoService.isServiceRunning()) {
            Intent UpdateInfoIntent = new Intent(mContext, UpdateSchoolInfoService.class);
            mContext.stopService(UpdateInfoIntent);
        }

        //停止考勤信息服务
        if(UploadAttendService.isServiceRunning()) {
            Intent UpdateParentInfoIntent = new Intent(mContext, UploadAttendService.class);
            mContext.stopService(UpdateParentInfoIntent);
        }

        //关闭网络探测，未上传信息数量报告服务
        if(NetWorkService.isServiceRunning()) {
            Intent netWorkIntent = new Intent(mContext, NetWorkService.class);
            mContext.stopService(netWorkIntent);
        }
        //关闭WebSorket服务
        if(WebSocketService.isServiceRunning()) {
            Intent websorketIntent = new Intent(mContext, WebSocketService.class);
            mContext.stopService(websorketIntent);
        }


    }
}
