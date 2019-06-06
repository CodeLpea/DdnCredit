package com.example.lp.ddncredit.mainview.networkdetail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;


public class NetworkdetailReceiver extends BroadcastReceiver {
    private static final String  TAG="NetworkdetailReceiver";

    //单例获取
    public static NetworkdetailReceiver instance;

    public static NetworkdetailReceiver getInstance() {
        if (instance == null) {
            instance = new NetworkdetailReceiver();
        }
        return instance;
    }

    private NetworkdetailReceiver() {
    }

    private List<NetworkListener> networkListerList=new ArrayList<NetworkListener>();//监听类的集合

    public void addNtworkLister(NetworkListener networkLister) {
        this.networkListerList.add(networkLister);
    }

    public void removeNtworkLister(NetworkListener networkLister) {
        this.networkListerList.remove(networkLister);
    }



    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(NetworkListener.NUMBER_ACTION)){
            int countTotal = intent.getIntExtra(NetworkListener.NUMBER_NAME, 0);

            for(NetworkListener networkLister1: networkListerList){
                networkLister1.onUnloadNumChange(countTotal);//循环发送回调给监听接口
            }
        }
        else if(intent.getAction().equals(NetworkListener.NETWORK_ACTION)){
            boolean netstaus=intent.getBooleanExtra(NetworkListener.NETSTATUS,true);

            for(NetworkListener networkLister1: networkListerList){
                networkLister1.onNetChange(netstaus);//循环发送回调给监听接口
            }
        }
    }
}
