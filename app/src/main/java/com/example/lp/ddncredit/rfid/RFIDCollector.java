package com.example.lp.ddncredit.rfid;

import android.content.Context;

import com.rfid.reader.Reader;

import java.io.IOException;

/**
 * 接收RFID数据
 * lp
 * 2019/05/27
 */
public class RFIDCollector {
    private static final String TAG = "RFIDCollector";
    private Context mContext;
    private Reader reader;

    private OnDataReceiveListener mOnDataReceiveListener;
    public RFIDCollector(Context context){
        mContext = context;
    }

    public RFIDCollector setOnDataReceiveListener(OnDataReceiveListener listener){
        mOnDataReceiveListener = listener;
        return this;
    }

    public void execute(){
        try{
            reader= ReaderIml.getInstance().getReader();
        }catch(IOException e){
            e.printStackTrace();
            return;
        }catch (SecurityException e){
            e.printStackTrace();
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

    public static interface OnDataReceiveListener{
        void process(byte[] data, int size);
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
                    byte[] errCode = new byte[1];
                    byte[] uid = new byte[32];
                    byte[] uidLen = new byte[1];
                    int result;
                    //long startTime = System.currentTimeMillis();
                    result = reader.Iso14443a_GetUid(uid, uidLen, errCode);
                    if (result == 0) {
                        mOnDataReceiveListener.process(uid, uidLen[0]);
                    }
                /*    long endTime = System.currentTimeMillis();    //获取结束时间
                    Log.i(TAG, "run: "+ "总程序运行时间：" + (endTime - startTime) + "ms\n\n" );  //输出程序运行时间*/
                    delay(400);
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
}
