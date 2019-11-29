package com.example.lp.ddncredit.rfid;

import android.util.Log;

import com.example.lp.ddncredit.Myapplication;
import com.example.lp.ddncredit.utils.StringTool;
import com.example.lp.ddncredit.websocket.bean.RunningInfo;

import java.math.BigInteger;

import Lib.FWReader.S8.function_S8;

import static com.example.lp.ddncredit.utils.StringTool.byteHexToSting;
import static com.example.lp.ddncredit.utils.StringTool.hdHex;
import static com.example.lp.ddncredit.utils.StringTool.isHexStrValid;

/**
 * 获取Reader第一种刷卡器实例的单例
 * lp
 */
public class ReaderTwo implements Reader {
    private static final String TAG = "ReaderTwo";

    private function_S8 call_contactLess;

    int result = 0, hdev = 1;


    private static ReaderTwo instance;

    // 单例模式中获取唯一的ReaderIml实例
    public static ReaderTwo getInstance() {
        if (instance == null) {
            instance = new ReaderTwo();
        }
        return instance;
    }

    public ReaderTwo() {
        try {
            if (call_contactLess == null) {
                call_contactLess = new function_S8(Myapplication.getInstance());
                call_contactLess.SetTransPara(0x20, 1137, 41234);

                Log.i(TAG, "ReaderTwo: 开启");

            }
        } catch (Exception e) {
            e.printStackTrace();
            setCreditInfo(e.getMessage());
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

    @Override
    public Long getRfid() {
        return excuete();
    }

    @Override
    public void close() {
        if (call_contactLess != null) {
            call_contactLess.fw_exit(hdev);
            call_contactLess = null;
        }
    }

    private Long excuete() {
        try {
            Long rfid = startRead();
//            Log.i(TAG, "rfid: " + rfid);
            return rfid;
        } catch (Exception e) {
            e.printStackTrace();
            setCreditInfo(e.getMessage());
        }
        return null;
    }

    public Long startRead() {
        char[] pModVer = new char[512];
        char[] pSnrM1 = new char[255];
        Long rfid=null;
        // TODO Auto-generated method stub
        do {
            hdev = call_contactLess.fw_init_ex(2, null, 0);
        } while (hdev == -1);
//        Log.e(TAG, "hdev: " + hdev);
        if (hdev != -1) {
            //获取一次硬件版本号
            result = call_contactLess.fw_getver(hdev, pModVer);
            if (0 == result) {
                //获取卡号 UID
//                Log.e(TAG, "pSnrM1: " + String.valueOf(pSnrM1));
                result = call_contactLess.fw_card_str(hdev, (short) 1, pSnrM1);
                //4A0FFBE0
//                Log.e(TAG, "pSnrM1: " + String.valueOf(pSnrM1));

                //只截取前面8位
                String substring = String.valueOf(pSnrM1).substring(0, 8);
                //判断是否符合十六进制
                boolean hexStrValid = isHexStrValid(substring);
//                Log.e(TAG, "hexStrValid: " + hexStrValid);
                if (!hexStrValid) {
                    return null;
                }
                //高低位算法
                String strUid = hdHex(substring, substring.length());
                //十六进制转换为十进制
                rfid=StringTool.getRfid(strUid).longValue();
//                Log.e(TAG, "UID: " + rfid);

            }
        } else {
            Log.e(TAG, "_Link reader failed");
        }
        return rfid;
    }
}
