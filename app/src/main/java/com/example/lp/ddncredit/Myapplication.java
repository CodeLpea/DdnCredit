package com.example.lp.ddncredit;

import android.app.Application;
import android.content.Intent;

import com.example.lp.ddncredit.Voice.TtsSpeek;
import com.example.lp.ddncredit.rfid.FirstRFIDCollectService;
public class Myapplication  extends Application{
    public static final String TAG="Myapplication";
    private static  Myapplication instance;

    // 单例模式中获取唯一的MyApplication实例
    public static Myapplication getInstance() {
        if (instance == null) {
            instance = new Myapplication();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initService();

        initVoice();
    }

    private void initVoice() {

        TtsSpeek.getInstance();//初始化tts，避免第一句说话慢
    }

    private void initService() {
        /**
         * 开启串口读取卡号服务
         * */
        Intent firstRfidIntent = new Intent(this, FirstRFIDCollectService.class);
        this.startService(firstRfidIntent);


    }


}
