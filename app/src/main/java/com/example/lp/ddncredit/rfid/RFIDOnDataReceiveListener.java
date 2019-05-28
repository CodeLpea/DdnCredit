package com.example.lp.ddncredit.rfid;

import android.content.Context;

import com.example.lp.ddncredit.attendance.FfidResultEventBusReceiver;

import static com.example.lp.ddncredit.utils.StringTool.byteHexToSting;
import static com.example.lp.ddncredit.utils.StringTool.getRfid;

/**
 * 接收RFID结果，处理为rfid
 * 并通知总线
 * lp
 * 2019/05/27
 */
public class RFIDOnDataReceiveListener implements RFIDCollector.OnDataReceiveListener{
    private static final String TAG = "RFIDOnDataReceiveListen";

    public RFIDOnDataReceiveListener(Context context){

    }

    @Override
    public void process(byte[] data, int size) {
            long rfid = 0;
            System.out.println("UID->" + byteHexToSting(data) );
            rfid=getRfid(byteHexToSting(data)).longValue();
            System.out.println("==============1============rfid=" +rfid);//得到id卡号
            FfidResultEventBusReceiver.getInstance().post(Long.valueOf(rfid));


    }
}
