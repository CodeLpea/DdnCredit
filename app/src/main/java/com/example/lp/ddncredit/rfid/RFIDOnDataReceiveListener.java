package com.example.lp.ddncredit.rfid;

import android.content.Context;
import android.util.Log;


import com.example.lp.ddncredit.Utils.TypesToHexString;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Long on 2018/8/7.
 */

public class RFIDOnDataReceiveListener implements RFIDCollector.OnDataReceiveListener{
    private static final String TAG = "RFIDOnDataReceiveListen";
    private ByteArrayOutputStream mBuffer = null;
    private Context mContext;
    public RFIDOnDataReceiveListener(Context context){
        mContext = context;
        mBuffer = new ByteArrayOutputStream();
    }

    @Override
    public void process(byte[] data, int size) {
        byte[] buffer = null;
        try{
            mBuffer.write(data);
            buffer = mBuffer.toByteArray();
            Log.i(TAG, "byte size : " + size + " ---> " + TypesToHexString.byte2hex(buffer));
        }catch(IOException e){
            e.printStackTrace();
        }

        long rfid = 0;
        //  rfid = Long.parseLong(new String(data).trim());
        if(buffer.length >= 7){
            Log.i(TAG, "============1========" + (int)buffer[0] + " " + (int)buffer[1]);
            if(data[0] == (byte)0xAA && data[1] == (byte)0x55){
                int rfidNumber = 0;
                rfidNumber |= (data[5] << 24 & 0xFF000000);
                rfidNumber |= (data[4] << 16 & 0x00FF0000);
                rfidNumber |= (data[3] << 8  & 0x0000FF00);
                rfidNumber |= (data[2] << 0  & 0x000000FF);
                rfid = Long.parseLong(Integer.toHexString(rfidNumber), 16);
                Log.i(TAG, "==============1============rfid=" + rfidNumber);
            }
            Log.i(TAG, "rfid : " + rfid);
            //清空ByteArrayOutputStream
            mBuffer.reset();
        //    ResultHandlerEventBusReceiver.getInstance().post(Long.valueOf(rfid));
        }

    }
}
