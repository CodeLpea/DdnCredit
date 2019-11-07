package com.example.lp.ddncredit.websocket;

import android.os.Build;
import android.util.Log;

import com.example.lp.ddncredit.Myapplication;
import com.example.lp.ddncredit.utils.AppUtils;
import com.example.lp.ddncredit.utils.TimeUtil;
import com.example.lp.ddncredit.websocket.bean.RunningInfo;
import com.example.lp.ddncredit.websocket.bean.SoftWareVersionsInfo;

/**
 * 测试使用的信息采集线程
 * 采集本机硬件信息状态
 * 启动记录
 * 摄像头，刷卡器状态
 * 语音模块状态
 */
public class InfoCollector implements Runnable {
    private static final String TAG = "InfoCollector";
    private static boolean status = false;

    @Override
    public void run() {
        while (status) {
            //十秒钟采集一次信息
            Log.i(TAG, "-------------InfoCollector十秒钟采集一次信息----------");
            TimeDelay(10 * 1000);
            hardWareCollect();
            SoftWareVersionsInfo();
        }


    }

    public InfoCollector() {
        startCollect();
    }

    public void startCollect() {
        Log.i(TAG, "-------------startCollect-------------- ");
        status = true;
    }

    public void stopCollect() {
        Log.i(TAG, "-------------startCollstopCollectect-------------- ");
        status = false;
    }

    private void TimeDelay(int times) {
        try {
            Thread.sleep(times);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*硬件信息的采集*/
    private void hardWareCollect() {
        Log.i(TAG, "---------测试信息开始采集-----------: ");
        RunningInfo runningInfo=new RunningInfo();
        runningInfo.setCreditStatus(TimeUtil.getYMDHMSDate001());
        runningInfo.setCameraStatus(TimeUtil.getYMDHMSDate001());
        runningInfo.setVoiceStatus(TimeUtil.getYMDHMSDate001());
        runningInfo.upload();
        Log.i(TAG, "hardWareCollect: "+runningInfo.toString());

    }

    //软件版本信息采集
    private void SoftWareVersionsInfo(){
        SoftWareVersionsInfo info = new SoftWareVersionsInfo();
        //获取app版本号
        info.setSoftware(AppUtils.getAppVersionName(Myapplication.getInstance()));
        //获取android系统版本号
        info.setSystem(Build.VERSION.RELEASE);
        info.setTime(TimeUtil.getYMDHMSDate001());
        //获取内核版本号
        info.upload();
        Log.i(TAG,
                "---------------------SoftWareVersionsInfo: " +
                        "\n" + info.toString());
    }

}
