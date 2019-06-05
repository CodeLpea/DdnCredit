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




    //串口设置参数
    String SERIAL1_NAME = "serial1_name";
    String SERIAL1_BAUDRATE = "serial1_baudrate";
    String SERIAL1_DATA_BIT = "serial1_databit";
    String SERIAL1_PARITY_BIT = "serial1_paritybit";
    String SERIAL1_STOP_BIT = "serial1_stopbit";
    String SERIAL1_FLOW_CONTROL = "serial1_flow_control";

    String SERIAL2_NAME = "serial2_name";
    String SERIAL2_BAUDRATE = "serial2_baudrate";
    String SERIAL2_DATA_BIT = "serial2_databit";
    String SERIAL2_PARITY_BIT = "serial2_paritybit";
    String SERIAL2_STOP_BIT = "serial2_stopbit";
    String SERIAL2_FLOW_CONTROL = "serial2_flow_control";

    //当前的刷卡场景
    String CARD_OCCASION = "card_occasion";

    //身份认证
    String IDENTI_AUTH_MODE = "identi_auth_mode";

    //语音播放速度
    String VOICE_SPEED = "voice_speed";
    int DEFAULT_VOICE_SPEED = 60;

    //识别阈值相关
    String FACE_RECOGNITION_THRESHOLD = "face_recognition_threshold";
    float DEFAULT_FACE_RECOGNITION_THRESHOLD = 0.6f;
    String FORMAL_FEATURE_UPDATE_THRESHOLD = "formal_feature_update_threshold";
    float DEFAULT_FORMAL_FEATURE_UPDATE_THRESHOLD = 0.8f;
    String CACHE_FEATURE_THRESHOLD = "cache_feature_threshold";
    float DEFAULT_CACHE_FEATURE_THRESHOLD = 0.4f;

    //远程控制相关
    int SOFTWARE_SYSTEM_TYTE = 6;

    //使用模式(仅电子班牌模式:该模式下只获取Web网页信息和操作设置界面)
    String ONLY_ELECTRONIC_CLASS_CARD_MODE = "only_electronic_class_card_mode";



    interface SP_HDetect_NAME {
        String SP_NAME = "SPNUOSHUA";
        /** 用户认证信息 v3*/
        String TOKEN = "token";
        String AUTHORIZATION_INFO = "Authorization";
        /** 蓝牙地址*/
        String BLUETOOTH_ADDRESS = "bluetoothAddress";
        /** 曝光*/
        String EXPLORE = "explore";
        /** 颜色选择记录*/
        String COLORSPINEER= "colorSpiner";
        /** 阈值选择记录*/
        String HoldValuePostion = "HoldValuePostions";
        String HoldValues = "HoldValues";
        /** 天数 */
        String CURRENT_DAY = "currentDay";
        /**学校id和学校名称*/
        String SchoolID="SchoolID";
        String SchoolName="SchoolName";
        String StuNum="StuNum";
        /** 手动设置曝光 */
        String isExplore="isExplore";
        String ProductName="ProductName";//产品名
        String ProductId="ProductId";//产品ID
        String VendorId="VendorId";//供应商ID
        String BcdDevice="BcdDevice";//FW版本
        /** app版本号*/
        String APPVERSION="APPVERSION";//FW版本
        /**
         * 网络摄像头登录信息
         *
         */
        String NET_IP = "NET_IP";
        String NET_PORT = "NET_PORT";
        String NET_USER = "NET_USER";
        String NET_OPSE = "NET_OPSE";

        /**
         * 语音音量
         * */
        String VOICE_LEVEL="VOICE_LEVEL";
    }

}
