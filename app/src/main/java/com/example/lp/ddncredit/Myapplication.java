package com.example.lp.ddncredit;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import com.example.lp.ddncredit.utils.AppUtils;
import com.example.lp.ddncredit.utils.SPUtil;
import com.example.lp.ddncredit.utils.TimeUtil;
import com.example.lp.ddncredit.utils.voice.TtsSpeek;
import com.example.lp.ddncredit.attendance.FfidResultEventBusReceiver;
import com.example.lp.ddncredit.websocket.bean.SoftWareVersionsInfo;

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
        initEventBus();
        initVoice();
        SoftWareVersionsInfo();
    }



    private void initEventBus() {
        FfidResultEventBusReceiver.getInstance().inject(this).register();
    }

    private void initVoice() {
        TtsSpeek.getInstance();//初始化tts，避免第一句说话慢
    }

//软件版本信息采集
    private void SoftWareVersionsInfo(){
        SoftWareVersionsInfo info = new SoftWareVersionsInfo();
        //获取app版本号
        info.setSoftware(AppUtils.getAppVersionName(Myapplication.getInstance()));
        //获取android系统版本号
        info.setSystem(Build.VERSION.RELEASE);
        info.setTime(TimeUtil.getNowDate());
        //获取内核版本号
        info.upload();
        Log.i(TAG,
                "---------------------SoftWareVersionsInfo: " +
                        "\n" + info.toString());
    }



}
