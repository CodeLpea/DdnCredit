package com.example.lp.ddncredit;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.lp.ddncredit.service.ServiceManager;
import com.example.lp.ddncredit.utils.SPUtil;

import java.util.List;

import static com.example.lp.ddncredit.constant.Constants.CONFIG_DIR;
import static com.example.lp.ddncredit.utils.NavigationBarUtil.hideNavigationBar;

public class BaseActivity extends PermissionActivity {
    private static final String TAG = "BaseActivity";
    private PermisonListener permisonListener;
    private String[] Permisons = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};

    public static Activity activity;
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
        activity=this;
        //hideBottomUIMenu();
        hideNavigationBar(this.getWindow());

        permisonListener = new PermisonListener();
        performRequestPermissions("拒绝权限后请到设置中手动开启权限", Permisons, 101, permisonListener);
    }

    private class PermisonListener implements PermissionsResultListener {
        @Override
        public void onPermissionGranted() {
            //开启sp文件夹创建，否则创建失败
            Log.i(TAG, "onPermissionGranted: ");
            initSP();
            ServiceManager.getInstance().startServices();//开启所有服务
        }

        @Override
        public void onPermissionDenied() {

        }
    }
    @Override
    protected void onPause() {
        Log.i(TAG, "onPause: ");
        super.onPause();

    }

    protected void initSP() {
        /**
         * 指定app.xml存储位置
         * 避免更新app后配置文件读取失败
         */


        Log.i(TAG, "initSP: ");
        SPUtil.setDefaultConfig(CONFIG_DIR);
//        SPUtil.assignDir(CONFIG_DIR);

    }
/**
 * 判断当前显示的是哪一个Fragment
 * */
    public Fragment getVisibleFragment(){
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for(Fragment fragment : fragments){
            if(fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }

    /**
     * 用于m2读卡器
     * */
    public static final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
        ServiceManager.getInstance().stopServices();//关闭所有服务
    }
}
