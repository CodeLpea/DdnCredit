package com.example.lp.ddncredit;

import android.app.FragmentTransaction;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.lp.ddncredit.mainview.view.NumImageView;
import com.example.lp.ddncredit.mainview.networkdetail.NetworkdetailReceiver;
import com.example.lp.ddncredit.mainview.fragment.CameraFragment;
import com.example.lp.ddncredit.mainview.fragment.ExpressionFragment;
import com.example.lp.ddncredit.mainview.fragment.SetFragment;
import com.example.lp.ddncredit.service.ServiceManager;
import com.example.lp.ddncredit.mainview.networkdetail.NetworkdetailReceiver.networkLister;

import static com.example.lp.ddncredit.mainview.networkdetail.NetworkdetailReceiver.NETWORK_ACTION;
import static com.example.lp.ddncredit.mainview.networkdetail.NetworkdetailReceiver.NUMBER_ACTION;

public class MainActivity extends AppCompatActivity implements networkLister {
    private static final String TAG = "MainActivity";

    private FragmentTransaction fragmentTransaction;
    private CameraFragment cameraFragment;
    private ExpressionFragment expressionFragment;
    private SetFragment setFragment;
    private NetworkdetailReceiver unUploadNumberReceiver;
    private  IntentFilter filter;

    private ImageView netWorkImageView;
    private NumImageView unUploadNumImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ServiceManager.getInstance().startServices();//开启所有服务
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        initView();
        initFragement();

    }

    private void initView() {
        netWorkImageView=findViewById(R.id.iv_netStaus);
        unUploadNumImageView=findViewById(R.id.nv_unUpadateNum);

        netWorkImageView.setVisibility(View.INVISIBLE);//默认是看不见的
        unUploadNumImageView.setVisibility(View.INVISIBLE);//默认是看不见的


        unUploadNumberReceiver = new NetworkdetailReceiver();//实例化
        unUploadNumberReceiver.setnNtworkLister(this);//设置监听
        //创建IntentFilter
        filter = new IntentFilter();
        filter.addAction(NUMBER_ACTION);
        filter.addAction(NETWORK_ACTION);

    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume: ");
        registerBroadcastReceiver();//注册广播
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        UnregisterBroadcastReceiver();//注销广播
    }

    /**
     * 注册网络状态广播
     */
    private void registerBroadcastReceiver() {
        LocalBroadcastManager.getInstance(Myapplication.getInstance()).registerReceiver(unUploadNumberReceiver, filter);
    }
    /**
     * 注销网络状态广播
     */
    private void UnregisterBroadcastReceiver() {
        LocalBroadcastManager.getInstance(Myapplication.getInstance()).unregisterReceiver(unUploadNumberReceiver);
    }

    private void initFragement() {
        Log.i(TAG, "initFragment: ");
        fragmentTransaction = getFragmentManager().beginTransaction();
        //fragmentTransaction=getSupportFragmentManager().beginTransaction();
        cameraFragment = new CameraFragment();
        expressionFragment = new ExpressionFragment();
        setFragment = new SetFragment();
        fragmentTransaction.add(R.id.framelayout, cameraFragment);
        fragmentTransaction.add(R.id.framelayout, expressionFragment);
        fragmentTransaction.add(R.id.framelayout, setFragment);//replace的形式是每次都刷新，add show不会刷新，只是hide隐藏起来
        fragmentTransaction.hide(cameraFragment);
        fragmentTransaction.hide(setFragment);
        fragmentTransaction.show(expressionFragment);//只显示表情界面
        fragmentTransaction.commit();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ServiceManager.getInstance().stopServices();//关闭所有服务
    }

    /**
     * 网络发生变化
     */
    @Override
    public void onNetChange(boolean netStatus) {
       if(netStatus){//如果有网络
           netWorkImageView.setVisibility(View.INVISIBLE);//隐藏无网络提示框
       }else {
           //无网络
           netWorkImageView.setVisibility(View.VISIBLE);//可见
       }

    }

    /**
     * 获取没有上传的数据的个数
     */
    @Override
    public void onUnloadNumChange(int unloadNum) {
        if(unloadNum>2){
            unUploadNumImageView.setVisibility(View.VISIBLE);//可以看见
            unUploadNumImageView.setNum(unloadNum);
        }else {
            unUploadNumImageView.setVisibility(View.INVISIBLE);//隐藏
        }

    }
}
