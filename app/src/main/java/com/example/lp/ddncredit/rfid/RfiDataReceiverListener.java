package com.example.lp.ddncredit.rfid;

import com.example.lp.ddncredit.attendance.FfidResultEventBusReceiver;

public class RfiDataReceiverListener implements RfidDataListener {
    public RfiDataReceiverListener() {
    }

    @Override
    public void process(Long rfid) {
        System.out.println("==============1============rfid=" +rfid);//得到id卡号
        FfidResultEventBusReceiver.getInstance().post(new RfidBean(Long.valueOf(rfid)));
    }
}
