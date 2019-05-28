package com.example.lp.ddncredit;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.lp.ddncredit.rfid.RFIDCollectService;
import com.example.lp.ddncredit.versionConTrol.SoftWareUpgradeService;

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

      
    }
}
