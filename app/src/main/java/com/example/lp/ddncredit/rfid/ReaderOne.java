package com.example.lp.ddncredit.rfid;

import android.util.Log;

import com.example.lp.ddncredit.utils.StringTool;
import com.example.lp.ddncredit.websocket.bean.RunningInfo;


import static com.example.lp.ddncredit.utils.StringTool.byteHexToSting;

/**
 * 获取Reader第一种刷卡器实例的单例
 * lp
 */
public class ReaderOne implements Reader {

    private com.rfid.reader.Reader reader;
    private static ReaderOne instance;

    // 单例模式中获取唯一的ReaderIml实例
    public static ReaderOne getInstance() {
        if (instance == null) {
            instance = new ReaderOne();
        }
        return instance;
    }

    public ReaderOne() {
        try {
            if (reader == null) {
                reader = com.rfid.reader.Reader.getInstance("/dev/ttyS1", 9600);
            }
        } catch (Exception e) {
            e.printStackTrace();
            setCreditInfo(e.getMessage());
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
    @Override
    public Long getRfid() {

        return excuete();
    }

    @Override
    public void close() {
        if (reader != null) {
            reader.close();
            reader = null;
        }
    }

    private Long excuete() {
        try {
            byte[] errCode = new byte[1];
            byte[] uid = new byte[32];
            byte[] uidLen = new byte[1];
            int result;
            result = reader.Iso14443a_GetUid(uid, uidLen, errCode);
            if (result == 0) {
                Long rfid = StringTool.getRfid(byteHexToSting(uid)).longValue();
                Log.i("excuete", "rfid: " + rfid);
                return rfid;
            }
        } catch (Exception e) {
            e.printStackTrace();
            setCreditInfo(e.getMessage());
        }
        return null;
    }
}
