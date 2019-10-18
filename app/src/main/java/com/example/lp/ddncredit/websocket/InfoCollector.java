package com.example.lp.ddncredit.websocket;

import android.os.SystemClock;
import android.util.Log;

import com.example.lp.ddncredit.websocket.bean.HardWareInfo;

/**
 * 信息采集线程
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

    private void hardWareCollect() {
        Log.i(TAG, "---------测试信息开始采集-----------: ");
        HardWareInfo hardWareInfo = new HardWareInfo();
        hardWareInfo.setTest1(String.valueOf(SystemClock.currentThreadTimeMillis()));
        hardWareInfo.setTest2(String.valueOf(SystemClock.currentThreadTimeMillis()));
        hardWareInfo.setTest3(String.valueOf(SystemClock.currentThreadTimeMillis()));
        hardWareInfo.setTest4(String.valueOf(SystemClock.currentThreadTimeMillis()));
        Log.i(TAG,
                "---------------------hardWareCollect: " +
                        "\n" + hardWareInfo.toString());

        hardWareInfo.upload();
    }
}
