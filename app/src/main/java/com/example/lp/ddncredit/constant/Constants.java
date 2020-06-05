package com.example.lp.ddncredit.constant;

import android.os.Environment;

import java.io.File;

/**
 * Created by Long on 2018/8/3.
 */

public interface Constants {
    //程序数据存储的主路径
    String ROOT_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "didanuo";
    //照片保存路径
    String PIC_DIR = ROOT_DIR + File.separator + "pictures";
    String EXPRESSION_DIR = ROOT_DIR + File.separator + "expression";
    //存放正式特征值对应的照片
    String PERSONAL_PIC_DIR = PIC_DIR + File.separator + "personal";
    //存放后备特征值对应的照片
    String CACHE_PIC_DIR = PIC_DIR + File.separator + "cache";
    //考勤上传临时缓存照片
    String TMP_CACHE_PIC_DIR = PIC_DIR + File.separator + "tmp";
    //数据库存储路径
    String DB_DIR = ROOT_DIR + File.separator + "databases";
    //设备配置文件路径
    String CONFIG_DIR = ROOT_DIR + File.separator + "config";
    //配置文件名字
    String CONFIG_NAME = "config";
    //程序运行日志存放路径
    String XLOG_DIR = ROOT_DIR + File.separator + "xlog";
    //升级包下载存放路径
    String UPGRADE_PACKAGE_DOWNLOAD_DIR = ROOT_DIR + File.separator + "download";
    //远程定位路径
    String REMOTE_CTROL_DIR = ROOT_DIR + File.separator + "remotectrl";



    //远程控制相关
    int SOFTWARE_SYSTEM_TYTE = 8;


    interface SP_HDetect_NAME {
        String SP_NAME = "SPNUOSHUA";
        /** 用户认证信息 v3*/
        String TOKEN = "token";

        /**学校id和学校名称*/
        String SchoolID="SchoolID";
        String SchoolName="SchoolName";

        /**
         * 语音音量
         * */
        String VOICE_LEVEL="VOICE_LEVEL";
        String VOICE_SPEED_LEVEL="VOICE_SPEED_LEVEL";



        /**
         * 服务器地址
         * */
        String NAME_TUOYUBAO_BASE ="托育宝正式服务器";//key名，用来隐藏和解释地址
        String NAME_XIAONUO_BASE ="小诺正式服务器";//key名，用来隐藏和解释地址
        String API_TUOYUBAO_BASE ="http://api.didano.com/";//默认的托育宝正式服务器地址
        String API_XIAONUO_BASE ="http://120.77.237.242:8081/";//默认的小诺的正式服务器地址
        String APISP_NAME ="APISP_NAME";//服务器地址的key
        String ADRESS_RECODE_NAME ="ADRESS_RECODE_NAME";//所有服务器地址的索引key
    }

}
