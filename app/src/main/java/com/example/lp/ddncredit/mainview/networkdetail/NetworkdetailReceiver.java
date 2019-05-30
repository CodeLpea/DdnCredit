package com.example.lp.ddncredit.mainview.networkdetail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class NetworkdetailReceiver extends BroadcastReceiver {
    private static final String  TAG="NetworkdetailReceiver";
    public static String NUMBER_ACTION = "com.ddncredit.NumImageView";
    public static String NETWORK_ACTION = "com.ddncredit.netWork";
    public static String NUMBER_NAME = "number";
    public static String NETSTATUS = "netStatus";
    private networkLister networkLister;
    public  interface networkLister{
         void onNetChange(boolean netStatus);
         void onUnloadNumChange(int unloadNum);
    }

    public NetworkdetailReceiver() {
    }
    public void setnNtworkLister(networkLister networkLister){
        this.networkLister=networkLister;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(NUMBER_ACTION)){
            int countTotal = intent.getIntExtra(NUMBER_NAME, 0);
            networkLister.onUnloadNumChange(countTotal);
        }
        else if(intent.getAction().equals(NETWORK_ACTION)){
            boolean netstaus=intent.getBooleanExtra(NETSTATUS,true);
            networkLister.onNetChange(netstaus);

        }
    }
}
