package com.example.lp.ddncredit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lp.ddncredit.rfid.RFIDCollectService;

public class MainActivity extends AppCompatActivity {
    private Intent firstRfidIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initService();
    }





    /**
     * 开启串口读取卡号服务
     * */
    private void initService() {
        firstRfidIntent= new Intent(this, RFIDCollectService.class);
        this.startService(firstRfidIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.stopService(firstRfidIntent);
    }
}
