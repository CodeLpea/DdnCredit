package com.example.lp.ddncredit.rfid;

import android.content.Context;
import android.util.Log;

import com.example.lp.ddncredit.websocket.bean.RunningInfo;

import java.io.IOException;

/**
 * 接收RFID数据
 * lp
 * 2019/05/27
 */
public class RFIDCollector {
    private static final String TAG = "RFIDCollector";
    private Context mContext;
    private  Reader reader;

    private RfidDataListener mOnDataReceiveListener;
    public RFIDCollector(Context context){
        mContext = context;
    }

    public RFIDCollector setOnDataReceiveListener(RfidDataListener listener){
        mOnDataReceiveListener = listener;
        return this;
    }

    public void execute(){
        try{
            /*决定使用哪种刷卡模块*/
            reader=new ReaderOne();
//            reader=new ReaderM2();
//            reader=new ReaderM13Usb();

        }catch(Exception e){
            e.printStackTrace();
            setCreditInfo(e.getMessage());
            return;
        }
        isRunning = true;
        mRead = new Thread(mRunnable);
        mRead.start();
    }

    public void stop(){
        if(isRunning) {
            try {
                isRunning = false;
                mRead.join();
                mRead=null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    //线程
    private Thread mRead = null;
    private boolean isRunning = false;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            while(isRunning){
                //检查是否有数据
                try{
                    Long rfid = reader.getRfid();
//                    Log.e(TAG, "rfid: "+rfid);
                    if(mOnDataReceiveListener!=null&&rfid!=null){
                        mOnDataReceiveListener.process(rfid);
                    }
                    delay(100);
                }catch(Exception e){
                    e.printStackTrace();
                    continue;
                }

            }
        }
    };

    private void delay(int ms){
        try{
            Thread.sleep(ms);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    /**
     * 将刷卡器异常信息传到诊断平台
     * */
    private void setCreditInfo(String info){
        RunningInfo runningInfo=new RunningInfo();
        runningInfo.setCreditStatus(info);
        runningInfo.upload();
    }
}
