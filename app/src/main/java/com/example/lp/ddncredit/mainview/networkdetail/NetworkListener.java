package com.example.lp.ddncredit.mainview.networkdetail;
/**
 * NetworkListener
 * lp
 * 2019/05/31
 * */
public  interface NetworkListener {
    String NUMBER_ACTION = "com.ddncredit.NumImageView";//广播ACTION
    String NETWORK_ACTION = "com.ddncredit.netWork";//广播ACTION
    String NUMBER_NAME = "number";//intent.putExtra中的键
    String NETSTATUS = "netStatus";//intent.putExtra中的键

    void onNetChange(boolean netStatus);
    void onUnloadNumChange(int unloadNum);
}