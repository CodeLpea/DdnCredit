package com.example.lp.ddncredit;

import android.app.Application;

import com.example.lp.ddncredit.utils.SPUtil;
import com.example.lp.ddncredit.utils.voice.TtsSpeek;
import com.example.lp.ddncredit.attendance.FfidResultEventBusReceiver;

import org.litepal.LitePal;

import static com.example.lp.ddncredit.constant.Constants.CONFIG_DIR;


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
        instance=this;
        LitePal.initialize(this);//初始化LitePal数据库
        initSP();
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
        FfidResultEventBusReceiver.getInstance().inject(this).register();
    }

    private void initVoice() {
        TtsSpeek.getInstance();//初始化tts，避免第一句说话慢
    }




}
