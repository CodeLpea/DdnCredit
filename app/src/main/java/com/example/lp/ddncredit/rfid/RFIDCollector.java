package com.example.lp.ddncredit.rfid;

import android.content.Context;
import android.util.Log;


import com.didanuo.robot.libserialport.SerialPort;
import com.example.lp.ddncredit.Utils.TypesToHexString;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Long on 2018/7/31.
 */

public class RFIDCollector {
    private static final String TAG = "RFIDCollector";
    private Context mContext;
    private SerialPort mSerialPort;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private OnDataReceiveListener mOnDataReceiveListener;
    public RFIDCollector(Context context){
        mContext = context;
    }

    private File mDevice = null;
    private int mBaudRate = 9600;
    private int mFlags = 0;

    public RFIDCollector setDevice(String tty){
        mDevice = new File(tty);
        return this;
    }

    public RFIDCollector setBaudRate(int baudrate){
        mBaudRate = baudrate;
        return this;
    }

    public RFIDCollector setFlags(int flags){
        mFlags = flags;
        return this;
    }

    public RFIDCollector setOnDataReceiveListener(OnDataReceiveListener listener){
        mOnDataReceiveListener = listener;
        return this;
    }

    public void execute(){
        try{
            Log.i(TAG, "mDevice = " + mDevice + " mBaudRate = " + mBaudRate + " mFlags = " + mFlags);
            //mSerialPort = new SerialPort(new File("/dev/ttyS1"), 19200, 0);
            mSerialPort = new SerialPort(mDevice, mBaudRate, mFlags);
            mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();
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
                mOutputStream.close();
                mInputStream.close();
                mSerialPort.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
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
                    if(mInputStream.available() > 0){
                        Log.i(TAG, "data available : " + mInputStream.available());
                        byte[] data = new byte[mInputStream.available()];
                        int size = 0;
                        //读取数据
                        size = mInputStream.read(data);
                        if(size > 0){
                            Log.i(TAG, mDevice.getName() + " read data size : " +  size +  " data : " + TypesToHexString.byte2hex(data));
                            mOnDataReceiveListener.process(data, size);
                        }else{
                            delay(100);
                        }
                    }
                }catch(IOException e){
                    e.printStackTrace();
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
