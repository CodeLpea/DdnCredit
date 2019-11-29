package com.example.lp.ddncredit.rfid;

/**
 * 刷卡器接口
 * */
public interface Reader {

     Long getRfid();

     void close();
}
