package com.example.lp.ddncredit.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class ActivityAndServiceUtils {

    public static   boolean isServiceExisted(Context context, String className) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(Integer.MAX_VALUE);

        if (!(serviceList.size() > 0)) {
            return false;
        }

        for (int i = 0; i < serviceList.size(); i++) {
            ActivityManager.RunningServiceInfo serviceInfo = serviceList.get(i);
            ComponentName serviceName = serviceInfo.service;
            Log.e("ActivityAndServiceUtils", "getClassName: "+serviceName.getClassName());

            if (serviceName.getClassName().contains(className)) {
                Log.e("ActivityAndServiceUtils","-------------存在----------: "+className);
                return true;
            }
        }
        return false;
    }
}
