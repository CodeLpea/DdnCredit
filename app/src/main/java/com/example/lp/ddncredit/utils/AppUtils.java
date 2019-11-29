package com.example.lp.ddncredit.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;

import com.example.lp.ddncredit.Myapplication;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

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

  /*  *//**
     * 根据wifi信息获取本地mac

     * @return
     *//*
    public static String getLocalMacAddressFromWifiInfo() {
          String mac=null;
            String macAddress = NetUtil.getEthMacAddress();
            if(macAddress != null && !macAddress.contains(NetUtil.MAC_ERROR)) {
                mac = macAddress.replace(":", "");
            }
        Log.i("mac", mac);
        return mac;
    }*/
    /**
     * 根据wifi信息获取本地mac
     *
     * @param context
     * @return
     */
    public static String getLocalMacAddressFromWifiInfo(Context context) {
        //大于安卓5.1
        if(Build.VERSION.SDK_INT >= 19){
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

                String mac= String.valueOf(res1);
                mac = mac.replace(":", "");
                Log.i("mac", "mac: "+mac);
                return mac.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        }else {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo winfo = wifi.getConnectionInfo();
            String  mac = winfo.getMacAddress();
            mac = mac.replace(":", "");
            return mac;
        }

        return "02:00:00:00:00:00";
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
