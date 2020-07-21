package com.example.lp.ddncredit.rfid;

import android.content.Context;
import android.util.Log;

import com.example.lp.ddncredit.Myapplication;
import com.example.lp.ddncredit.utils.SPUtil;
import com.example.lp.ddncredit.websocket.bean.RunningInfo;

import java.io.IOException;

import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.RFID_MODE;
import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.SP_NAME;

/**
 * 接收RFID数据
 * lp
 * 2019/05/27
 */
public class RFIDCollector {
    private static final String TAG = "RFIDCollector";
    private Context mContext;
    private Reader reader;
    private int currentMode = -1;

    private RfidDataListener mOnDataReceiveListener;

    private RFIDCollector(Context context) {
        mContext = context;
    }

    public static RFIDCollector getInstance() {
        return Instance.instance;
    }

    private static class Instance {
        private static RFIDCollector instance = new RFIDCollector(Myapplication.getInstance());
    }

    public RFIDCollector setOnDataReceiveListener(RfidDataListener listener) {
        mOnDataReceiveListener = listener;
        return this;
    }

    /**
     * 切换模式
     *
     * @param rfidMode 0,1,2
     */
    public void setReader(int rfidMode) {
        /*决定使用哪种刷卡模块*/
        if (currentMode == rfidMode) {
            return;
        }
        currentMode = rfidMode;
        switch (rfidMode) {
            case 0:
                //Reader第一种刷卡器实例的单例
                reader = new ReaderOne();
                Log.i(TAG, "ReaderOne: ");
                break;
            case 1:
                //M2 串口形式
                reader = new ReaderM2();
                Log.i(TAG, "ReaderM2: ");
                break;
            case 2:
                //获取Reader第二种刷卡器实例的单例,智新班牌USB读卡方法
                reader = new ReaderM13Usb();
                Log.i(TAG, "ReaderM13Usb: ");
                break;
        }
        //保存
        SPUtil.writeInt(SP_NAME, RFID_MODE, rfidMode);
    }

    public void execute() {
        int rfidMode = SPUtil.readInt(SP_NAME, RFID_MODE);
        try {
            /*决定使用哪种刷卡模块*/
            setReader(rfidMode);

        } catch (Exception e) {
            e.printStackTrace();
            setCreditInfo(e.getMessage());
            return;
        }
        isRunning = true;
        mRead = new Thread(mRunnable);
        mRead.start();
    }

    public void stop() {
        if (isRunning) {
            try {
                isRunning = false;
                mRead.join();
                mRead = null;
                reader.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    //线程
    private Thread mRead = null;
    public boolean isRunning = false;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            while (isRunning) {
                if (reader == null) {
                    continue;
                }
                synchronized (reader) {
                    //检查是否有数据
                    try {
                        Long rfid = reader.getRfid();
//                    Log.e(TAG, "rfid: "+rfid);
                        if (mOnDataReceiveListener != null && rfid != null) {
                            mOnDataReceiveListener.process(rfid);
                        }
                        delay(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }
                }
            }
        }
    };

    private void delay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将刷卡器异常信息传到诊断平台
     */
    private void setCreditInfo(String info) {
        RunningInfo runningInfo = new RunningInfo();
        runningInfo.setCreditStatus(info);
        runningInfo.upload();
    }
}
