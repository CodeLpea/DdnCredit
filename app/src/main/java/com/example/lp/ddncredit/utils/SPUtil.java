package com.example.lp.ddncredit.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.util.Log;


import com.example.lp.ddncredit.Myapplication;
import com.example.lp.ddncredit.R;

import java.io.File;
import java.lang.reflect.Field;

import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.ADRESS_RECODE_NAME;
import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.APISP_NAME;

import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.NAME_TUOYUBAO_BASE;
import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.NAME_XIAONUO_BASE;


import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.SP_NAME;
import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.SchoolID;
import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.SchoolName;
import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.TOKEN;
import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.VOICE_LEVEL;
import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.VOICE_SPEED_LEVEL;



/**
 * Created by xiaoyuren on 2018/1/27.
 * 项目名称：didano-robot
 * 类描述：SharedPreferences 工具类
 * company：www.didano.cn
 * email：vin.qin@didano.cn
 * 创建时间：2018/1/27 14:16
 */

public class SPUtil {

    private static final String TAG = "SPUtil";

    /**
     * 获取sp对象
     *
     * @param SPName xxx.xml文件名
     * @return
     */
    private static SharedPreferences getSharedPreferences(String SPName) {
        /**
         * MODE_MULTI_PROCESS 修改sp后，可以及时刷新读取值
         */
        return Myapplication.getInstance().getSharedPreferences(SPName, Context.MODE_PRIVATE);
    }

    /**
     * 读取String类型字段
     *
     * @param SPName
     * @param key
     * @return
     */
    public static String readString(String SPName, String key) {
        return getSharedPreferences(SPName).getString(key, "");
    }

    /**
     * 写入String类型字段
     *
     * @param SPName
     * @param key
     * @param value
     * @return
     */
    public static void writeString(String SPName, String key, String value) {
        getSharedPreferences(SPName).edit().putString(key, value).apply();
    }

    /**
     * 读取boolean类型
     *
     * @param SPName
     * @param key
     * @return
     */
    public static boolean readBoolean(String SPName, String key) {
        return getSharedPreferences(SPName).getBoolean(key, false);
    }

    /**
     * 写入boolean类型
     *
     * @param SPName
     * @param key
     * @param value
     */
    public static void writeBoolean(String SPName, String key, boolean value) {
        getSharedPreferences(SPName).edit().putBoolean(key, value).apply();
    }

    /**
     * 读取int类型
     *
     * @param SPName
     * @param key
     * @return
     */
    public static int readInt(String SPName, String key) {
        return getSharedPreferences(SPName).getInt(key, 0);
    }

    /**
     * 写入int类型
     *
     * @param SPName
     * @param key
     * @param value
     */
    public static void writeInt(String SPName, String key, int value) {
        getSharedPreferences(SPName).edit().putInt(key, value).apply();
    }

    /**
     * 读取long类型
     *
     * @param SPName
     * @param key
     * @return
     */
    public static long readLong(String SPName, String key) {
        return getSharedPreferences(SPName).getLong(key, 0);
    }

    /**
     * 写入long类型
     *
     * @param SPName
     * @param key
     * @param value
     */
    public static void writeLong(String SPName, String key, long value) {
        getSharedPreferences(SPName).edit().putLong(key, value).apply();
    }

    public static float readFloat(String SPName, String key) {
        return getSharedPreferences(SPName).getFloat(key, 0.01f);
    }

    public static void writeFloat(String SPName, String key, float value) {
        getSharedPreferences(SPName).edit().putFloat(key, value).apply();
    }

    public static void removeKey(String SPName, String key) {
        getSharedPreferences(SPName).edit().remove(key).apply();
    }

    /**
     * sp文件位置默认在/data/data/package/下，软件下载就会删除，因此需要存放在sd卡里
     *
     * @param dir
     */
    public static void assignDir(String dir) {
        try {
            if (!FileUtil.isFileExist(dir))
                FileUtil.createDirs(dir);
            //利用java反射机制将XML文件自定义存储
            Field field;
            // 获取ContextWrapper对象中的mBase变量。该变量保存了ContextImpl对象
            field = ContextWrapper.class.getDeclaredField("mBase");
            field.setAccessible(true);
            // 获取mBase变量
            Object obj = field.get(Myapplication.getInstance());
            // 获取ContextImpl。mPreferencesDir变量，该变量保存了数据文件的保存路径
            field = obj.getClass().getDeclaredField("mPreferencesDir");
            field.setAccessible(true);
            // 创建自定义路径
            File file = new File(dir);
            // 修改mPreferencesDir变量的值
            field.set(obj, file);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 检查指定路径下是否已经存在配置文件了，如果没有则创建一份并写入默认值
     */
    public static void setDefaultConfig(String dir) {
        try {
            if (SPUtil.readString(SP_NAME, APISP_NAME).equals("")) {
                Log.i("setdefauletect_NAME", "=========================");
                writeInt(SP_NAME,VOICE_LEVEL,15);
                writeFloat(SP_NAME,VOICE_SPEED_LEVEL,1.0f);
                writeString(SP_NAME, APISP_NAME, NAME_TUOYUBAO_BASE);//默认地址为托育宝地址
                writeString(SP_NAME, ADRESS_RECODE_NAME, NAME_TUOYUBAO_BASE + ":-P" + NAME_XIAONUO_BASE + ":-P");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
