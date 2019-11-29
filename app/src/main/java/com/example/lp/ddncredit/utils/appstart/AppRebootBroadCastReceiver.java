package com.example.lp.ddncredit.utils.appstart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.lp.ddncredit.MainActivity;
import com.example.lp.ddncredit.utils.FileUtil;
import com.example.lp.ddncredit.versioncontrol.SoftWareUpgradeInstaller;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import static com.example.lp.ddncredit.constant.Constants.UPGRADE_PACKAGE_DOWNLOAD_DIR;

/**
 * Created by Long on 2018/10/11.
 */

public class AppRebootBroadCastReceiver extends BroadcastReceiver {
    private static final String TAG = "RebootBroadCastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, intent.getAction());
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            Toast.makeText(context , "监听到启动APP的广播" , Toast.LENGTH_LONG).show();
            bootApplication(context);
        }else if (Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())) {
           // Toast.makeText(context , "监听到系统广播替换" , Toast.LENGTH_LONG).show();
           // bootApplication(context);
        } else if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
           // Toast.makeText(context , "监听到系统广播添加" , Toast.LENGTH_LONG).show();
        }else if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) {
            Toast.makeText(context , "监听到系统广播移除" , Toast.LENGTH_LONG).show();
            //删除升级包
            deleteUpgradeApk();
        }
    }

    private void bootApplication(Context context){
        //重启APP
        Intent launchIntent = new Intent(context, MainActivity.class);
        launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(launchIntent);
    }
    /**
     * 删除安装后的apk
     * */
    private void deleteUpgradeApk(){
        List<File> packages = new ArrayList<>();
        FileUtil.getFiles(new File(UPGRADE_PACKAGE_DOWNLOAD_DIR), packages, new FileFilter() {
            @Override
            public boolean accept(File file) {
                if(file.getName().contains(".apk")){
                    return true;
                }else {
                    return false;
                }
            }
        });
        //删除升级包
        for(int i=0;i<packages.size();i++){
            if(!packages.isEmpty()){
                File file=packages.get(i);
                if(file != null) {
                    file.delete();
                }
            }
        }
    }
}
