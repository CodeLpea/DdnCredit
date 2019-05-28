package com.example.lp.ddncredit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lp.ddncredit.rfid.RFIDCollectService;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ServiceManager.getInstance().startServices();//开启所有服务

    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        ServiceManager.getInstance().stopServices();//关闭所有服务
    }
}
