package com.example.lp.ddncredit.mainview.networkdetail;

import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.example.lp.ddncredit.Myapplication;

import static com.example.lp.ddncredit.mainview.networkdetail.NetworkListener.NETWORK_ACTION;
import static com.example.lp.ddncredit.mainview.networkdetail.NetworkListener.NUMBER_ACTION;

/**
 * 监听网络状况以及未上传数据的管理类
 * 这样统一管理，避免反复创建广播，已经注册等
 * 解耦
 * <p>
 * 使用方法，直接获取NetWorkDetailManager的单例 ：
 * NetWorkDetailManager.getInstance() 即可自动初始化和全局注册广播
 * 可以手动unRegisterReceiver，全局注销广播
 * <p>
 * 如果想监听 监听网络状况以及未上传数据
 * 则需要在监听的类实现networkLister，并实现其方法，用于监听
 * 并添加该监听类到监听集合中，类似于订阅者
 * netWorkDetailManager.addListenr(this);
 * 方便接收订阅
 * <p>
 * <p>
 * lp
 * 2019/05/31
 */
public class NetWorkDetailManager {
    private static final String TAG = "NetWorkDetailManager";
    private NetworkdetailReceiver mNetworkdetailReceiver;
    private IntentFilter mIntentFilter;
    private boolean isRegister = false;

    //单例获取
    public static NetWorkDetailManager instance;

    public static NetWorkDetailManager getInstance() {
        if (instance == null) {
            instance = new NetWorkDetailManager();
        }
        return instance;
    }

    private NetWorkDetailManager() {
        /**
         * 初始化监听广播
         */
        if (mNetworkdetailReceiver == null) {
            Log.i(TAG, "NetWorkDetailManager: 实例化");
            mNetworkdetailReceiver = NetworkdetailReceiver.getInstance();//实例化
        }
        if (mIntentFilter == null) {
            //创建IntentFilter
            mIntentFilter = new IntentFilter();
            mIntentFilter.addAction(NUMBER_ACTION);
            mIntentFilter.addAction(NETWORK_ACTION);
        }
        if (mNetworkdetailReceiver != null && mIntentFilter != null) {
            Log.i(TAG, "NetWorkDetailManager: 默认自动注册广播一次");
            registerReceiver();//默认自动注册一次
        }

    }

    /**
     * 注册广播
     * 提供对外手动注册广播的方法
     * 一般都不会使用
     */
    public NetWorkDetailManager registerReceiver() {
        if (!isRegister) {
            Log.i(TAG, "没有注册过，正在注册: ");
            LocalBroadcastManager.getInstance(Myapplication.getInstance()).registerReceiver(mNetworkdetailReceiver, mIntentFilter);
            isRegister = true;
            return this;
        }

        Log.i(TAG, "已经注册过了: ");
        return this;
    }

    /**
     * 注销广播
     * 提供对外手动注销广播的方法
     * 一般都不会使用
     */
    public NetWorkDetailManager unRegisterReceiver() {
        if (isRegister) {
            Log.i(TAG, "正在注销: ");
            LocalBroadcastManager.getInstance(Myapplication.getInstance()).unregisterReceiver(mNetworkdetailReceiver);
            isRegister = false;
            return this;
        }
        Log.i(TAG, "没有注册，请勿重复注销: ");
        return this;
    }

    /**
     * 添加到监听集合
     */
    public void addListenr(NetworkListener networkLister) {
        Log.i(TAG, "addListenr: 设置监听");
        mNetworkdetailReceiver.addNtworkLister(networkLister);//设置监听
    }

    /**
     * 从监听集合中移除
     */
    public void removeListenr(NetworkListener networkLister) {
        Log.i(TAG, "removeListenr: 移除监听");
        mNetworkdetailReceiver.removeNtworkLister(networkLister);//移除监听
    }

}
