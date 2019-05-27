package com.example.lp.ddncredit;

import android.app.Application;
import android.content.Intent;

import com.example.lp.ddncredit.utils.SPUtil;
import com.example.lp.ddncredit.voice.TtsSpeek;
import com.example.lp.ddncredit.rfid.RFIDCollectService;
import com.example.lp.ddncredit.rfid.ResultHandlerEventBusReceiver;
import com.rfid.reader.Reader;

import java.io.IOException;
import java.security.InvalidParameterException;

import static com.example.lp.ddncredit.constant.Constants.CONFIG_DIR;
import static com.example.lp.ddncredit.constant.Constants.SERIAL1_NAME;
import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.SP_NAME;

public class Myapplication  extends Application{
    public static final String TAG="Myapplication";
    private static  Myapplication instance;
    public static Reader reader = null;

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
        instance=this;
        initSP();
        initService();
        initEventBus();
        initVoice();
    }

    private void initSP() {
        /**
         * 指定app.xml存储位置
         * 避免更新app后配置文件读取失败
         */
        SPUtil.assignDir(CONFIG_DIR);
        //生成默认的配置文件
        SPUtil.setDefaultConfig(CONFIG_DIR);

    }

    private void initEventBus() {
        ResultHandlerEventBusReceiver.getInstance().inject(this);
        ResultHandlerEventBusReceiver.getInstance().register();
    }

    private void initVoice() {
        TtsSpeek.getInstance(this);//初始化tts，避免第一句说话慢
    }

    private void initService() {
        /**
         * 开启串口读取卡号服务
         * */
        Intent firstRfidIntent = new Intent(this, RFIDCollectService.class);
        this.startService(firstRfidIntent);


    }
/**
 * 串口Reader
 * */
    public Reader getReader() throws SecurityException, IOException, InvalidParameterException {
        if (reader == null) {
          /*  *//* Read serial port parameters from com.rfid.readerdemo_preferences.xml *//*
            SharedPreferences sp = getSharedPreferences("com.rfid.readerdemo_preferences", MODE_PRIVATE);
            String path = sp.getString("DEVICE", "");
            if (path.length() == 0) {
                return null;
            }

            *//* Open the serial port *//*
            Log.i(this.getPackageName(), "open the port " + path);*/
            reader = Reader.getInstance("/dev/" + SPUtil.readString(SP_NAME, SERIAL1_NAME), 9600);
        }
        return reader;
    }

    public void closeReader() {
        if (reader != null) {
            reader.close();
            reader = null;
        }
    }


}
