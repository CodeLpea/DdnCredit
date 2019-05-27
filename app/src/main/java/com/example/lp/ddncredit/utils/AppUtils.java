package com.example.lp.ddncredit.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.example.lp.ddncredit.Myapplication;

/**
 * AppUtils
 * lp
 * 2019/5/27
 * */
public class AppUtils {

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            //versioncode = pi.versionCode;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
        }
        return versionName;
    }

    /**
     * 获取apk文件的版本号
     *
     * @param filePath apk路径
     * @return
     */
    public static String getAPKVersionName(String filePath) {
        if (filePath == null)
            return null;
        if (!FileUtil.isFileExist(filePath))
            return null;

        PackageManager packageManager = Myapplication.getInstance().getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
        if (packageInfo == null)
            return null;
        return packageInfo.versionName;
    }

    /**
     * 根据wifi信息获取本地mac

     * @return
     */
    public static String getLocalMacAddressFromWifiInfo() {
        WifiManager wifi = (WifiManager) Myapplication.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo winfo = wifi.getConnectionInfo();
        String mac = winfo.getMacAddress();
        return mac;
    }
    /**
     * 根据wifi信息获取本地mac
     *7.0权限
     * @param
     * @return
     *//*
    public static String getMacFromHardware() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }*/

}
