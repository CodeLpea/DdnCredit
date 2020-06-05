package com.example.lp.ddncredit;

import android.Manifest;
import android.annotation.TargetApi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.lp.ddncredit.mainview.camera.MineSessionListener;
import com.example.lp.ddncredit.mainview.camera.MineStateCallback;
import com.example.lp.ddncredit.mainview.camera.MineTextureListener;
import com.example.lp.ddncredit.mainview.networkdetail.NetWorkDetailManager;
import com.example.lp.ddncredit.mainview.networkdetail.NetworkListener;
import com.example.lp.ddncredit.mainview.view.NumImageView;
import com.example.lp.ddncredit.mainview.fragment.ExpressionFragment;
import com.example.lp.ddncredit.mainview.fragment.SetFragment;
import com.example.lp.ddncredit.mainview.view.adapter.AttendShowBean;
import com.example.lp.ddncredit.mainview.view.bgToast;
import com.example.lp.ddncredit.mainview.view.dialog.AttendDialog;
import com.example.lp.ddncredit.mainview.view.dialog.LoginClickDialogListenr;
import com.example.lp.ddncredit.service.ServiceManager;
import com.example.lp.ddncredit.utils.BitmapUtil;
import com.example.lp.ddncredit.utils.Desity;
import com.example.lp.ddncredit.utils.SPUtil;
import com.example.lp.ddncredit.utils.voice.TtsSpeek;
import com.example.lp.ddncredit.websocket.bean.RunningInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.Arrays;

import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.SP_NAME;
import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.VOICE_LEVEL;
import static com.example.lp.ddncredit.utils.NavigationBarUtil.hideNavigationBar;

public class MainActivity extends BaseActivity implements NetworkListener, LoginClickDialogListenr.LoginResultListenr {
    private static final String TAG = "MainActivity";

    private ImageButton imageButton;
    private LoginClickDialogListenr mLoginClickDialogListenr;

    private ExpressionFragment expressionFragment;
    private SetFragment setFragment;
    private NetWorkDetailManager netWorkDetailManager;//全局网络状态管理类，需要实现networkLister配合使用

    private FragmentTransaction fragmentTransaction;
    private ImageView netWorkImageView;
    private NumImageView unUploadNumImageView;

    private TextureView preview;//显示预览图像
    private ImageButton takePicture;//获取照片
    private Size previewSize;//图像尺寸
    private CameraDevice cameraDevice;//相机设备
    private ImageReader imageReader;//用于读取图像数据
    private int CameraPreWith = 480;
    private int CameraPreHight = 640;

    //捕获请求
    private CaptureRequest.Builder requestBuilder;
    //预览的会话
    private CameraCaptureSession captureSession;


    private AttendDialog attendDialog;
    private AlertDialog alertDialog;


    private Fragment currentFragment;

    /**
     * 用于显示图像的显示角度
     */
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private static MainObject MainObjectInstance = null;

    public static MainObject getInstance() {
        return MainObjectInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*屏幕适配*/
        Desity.setAppDesity(Myapplication.getInstance(),this);
        //横屏显示
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        MainObjectInstance = new MainObject(this);//弱引用初始化
      /*  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        //不管是否使用actionbar主题,下面这句代码都有效(api21及以上)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//设置透明导航栏*/
        netWorkDetailManager = NetWorkDetailManager.getInstance();//获取到NetWorkDetailManager实例，用于监听
        initView();
        initFragement();
        preview.setSurfaceTextureListener(textureListener);

    }

    private void initView() {
        attendDialog = AttendDialog.getInstance();//初始化考勤dialog
        
        preview = (TextureView) findViewById(R.id.surface);

        netWorkImageView = findViewById(R.id.iv_netStaus);
        unUploadNumImageView = findViewById(R.id.nv_unUpadateNum);
        netWorkImageView.setVisibility(View.INVISIBLE);//默认是看不见的
        unUploadNumImageView.setVisibility(View.INVISIBLE);//默认是看不见的
        mLoginClickDialogListenr = new LoginClickDialogListenr(MainActivity.this, this);//监听设置点击事件,并初始化弹窗
        imageButton = findViewById(R.id.ib_set);//设置按钮
        imageButton.setOnClickListener(mLoginClickDialogListenr);//设置按钮点击事件监听，自带登录框显示，Timer及时退出。


    }

    /**
     * 密码框状态回调
     * 0错误
     * 1正确
     * 2退出，隐藏导航栏
     * 3点击设置事件
     */
    @Override
    public boolean loginResult(int i) {
        boolean result = false;
        switch (i) {
            case 0:
                Log.e(TAG, "loginResult: 密码错误");
                bgToast.myToast(this, "密码错误", 0, 200);
                TtsSpeek.getInstance().SpeechFlush("密码错误", SPUtil.readInt(SP_NAME, VOICE_LEVEL));
                break;
            case 1:
                Log.e(TAG, "loginResult: switchFragment");
                switchFragment(currentFragment);
                break;
            case 2:
                Log.i(TAG, "遗弃回调类型，隐藏底部栏: ");
                break;
            case 3:
                Log.i(TAG, "loginResult: 3");
                if (currentFragment.equals(expressionFragment)) {
                    Log.i(TAG, "loginResult: 弹出密码框");
                    result = true;
                } else {
                    Log.i(TAG, "loginResult: switchFragment");
                    switchFragment(currentFragment);
                }
                break;
        }
        return result;

    }

    /**
     * 处理考勤模块发来的表情变化信息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void attendEvent(AttendShowBean attendShowBean) {
        Log.i(TAG, "表情界面得到表情变化:");

        alertDialog = attendDialog.showChoiceDialog(attendShowBean,getBitmap(), MainActivity.this);//初始化弹出框



    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
        EventBus.getDefault().register(this);//onResume中注册evnetbus
        netWorkDetailManager.addListenr(this);//监听网络变化已经未上传数量
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause: ");
        super.onPause();
        EventBus.getDefault().unregister(this);//在pause的时候注销eventbus
        netWorkDetailManager.removeListenr(this);//移除监听

    }

    private void initFragement() {
        Log.i(TAG, "initFragment: ");
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        expressionFragment = new ExpressionFragment();
        setFragment = new SetFragment();

        fragmentTransaction.add(R.id.framelayout, expressionFragment);
        fragmentTransaction.add(R.id.framelayout, setFragment);//replace的形式是每次都刷新，add show不会刷新，只是hide隐藏起来
        fragmentTransaction.hide(setFragment);
        fragmentTransaction.show(expressionFragment);//只显示表情界面
        currentFragment=expressionFragment;
        fragmentTransaction.commit();
    }

    private void switchFragment(Fragment checkfragment) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();//必须重新获取
        if (checkfragment.equals(setFragment)) {
            Log.i(TAG, "反转显示expressionFragment: ");
            fragmentTransaction.hide(setFragment);
            fragmentTransaction.show(expressionFragment);//只显示表情界面
            currentFragment=expressionFragment;
            fragmentTransaction.commit();
            imageButton.setBackground(getResources().getDrawable(R.drawable.set));
            bgToast.myToast(this, "考勤模式", 0, 200);
            TtsSpeek.getInstance().SpeechFlush("考勤模式", SPUtil.readInt(SP_NAME, VOICE_LEVEL));
        } else if (checkfragment.equals(expressionFragment)) {
            Log.i(TAG, "反转显示setFragment: ");
            fragmentTransaction.hide(expressionFragment);
            fragmentTransaction.show(setFragment);//只显示设置
            currentFragment=setFragment;
            fragmentTransaction.commit();
            imageButton.setBackground(getResources().getDrawable(R.drawable.mian));
            bgToast.myToast(this, "设置模式", 0, 200);
            TtsSpeek.getInstance().SpeechFlush("设置模式", SPUtil.readInt(SP_NAME, VOICE_LEVEL));
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ServiceManager.getInstance().stopServices();//关闭所有服务
        netWorkDetailManager.unRegisterReceiver();

        if (MainObjectInstance != null) {
            MainObjectInstance = null;
        }
        mLoginClickDialogListenr = null;
    }

    /**
     * 网络发生变化
     */
    @Override
    public void onNetChange(boolean netStatus) {
        if (netStatus) {//如果有网络
            netWorkImageView.setVisibility(View.INVISIBLE);//隐藏无网络提示框
        } else {
            //无网络
            netWorkImageView.setVisibility(View.VISIBLE);//可见
        }

    }

    /**
     * 获取没有上传的数据的个数
     */
    @Override
    public void onUnloadNumChange(int unloadNum) {
        if (unloadNum > 2) {
            unUploadNumImageView.setVisibility(View.VISIBLE);//可以看见
            unUploadNumImageView.setNum(unloadNum);
        } else {
            unUploadNumImageView.setVisibility(View.INVISIBLE);//隐藏
        }

    }

    /**
     * TextureView的监听器-->重点展示图像
     */
    private MineTextureListener textureListener = new MineTextureListener() {
        @Override
        public void onSurfaceCreate(SurfaceTexture surface, int width, int height) {
            Log.i(TAG, "onSurfaceCreate: ");
            openCamera();

        }

        @Override
        public boolean onSurfaceDestroyed(SurfaceTexture surface) {
            Log.i(TAG, "onSurfaceDestroyed: ");
            toRelease();//释放资源
            return super.onSurfaceDestroyed(surface);
        }
    };

    /**
     * 打开相机
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void openCamera() {
        Log.i(TAG, "openCamera: ");
        //通过CameraManager获取相机服务
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraId = manager.getCameraIdList()[0];//返回摄像机列表
            //获取指定相机的特性
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);

            //获取摄像头支持的流配置-->涉及图像的所有分辨率以及每一帧的信息
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            //获取最大的输出尺寸
            previewSize = map.getOutputSizes(SurfaceTexture.class)[0];
            //权限判断
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //打开指定的相机
            manager.openCamera(cameraId, stateCallback, null);
            //创建ImageReader对象
            imageReader = ImageReader.newInstance(CameraPreWith, CameraPreHight, ImageFormat.YUV_420_888, 2);

        } catch (CameraAccessException e) {
            e.printStackTrace();
            setCameraInfo(e.getMessage());
        }
    }
    /**
     * 将摄像头异常信息传到诊断平台
     * */
    private void setCameraInfo(String info){
        RunningInfo runningInfo=new RunningInfo();
        runningInfo.setCameraStatus(info);
        runningInfo.upload();
    }
    /**
     * 获取相机的状态
     */
    private MineStateCallback stateCallback = new MineStateCallback() {
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onOpened(CameraDevice camera) {
            super.onOpened(camera);
            cameraDevice = camera;//获取摄像头
            startPreview();//开启预览
        }
    };

    /**
     * 开启预览
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startPreview() {
        if (cameraDevice == null) {
            Log.e(TAG, "cameraDevice is null");
            return;
        }
        //使用TextureView预览相机就是通过使用SurfaceTexture去实现的
        //与SurfaceView使用SurfaceHolder基本使差不多的
        SurfaceTexture texture = preview.getSurfaceTexture();
        if (texture == null) {
            Log.e(TAG, "surface is null");
            return;
        }

        //设置默认缓冲区的尺寸和相机所支持的最大尺寸一样
        texture.setDefaultBufferSize(CameraPreWith, CameraPreHight);
        //开启预览所需要的输出Surface
        Surface surface = new Surface(texture);
        try {
            //创建预览请求
            requestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            requestBuilder.addTarget(surface);
            //创建预览会话
            cameraDevice.createCaptureSession(Arrays.asList(surface), new MineSessionListener() {
                @Override
                public void onSessionConfigured(CameraCaptureSession session) {
                    //获取预览会话
                    captureSession = session;
                    //配置预览时使用3A模式(自动曝光、自动对焦、自动白平衡)
                    requestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

                    if (null == cameraDevice) {
                        return;
                    }
                    try {
                        //设置重复捕获图像
                        captureSession.setRepeatingRequest(requestBuilder.build(), null, null);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放资源
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void toRelease() {
        try {
            //关闭会话
            if (null != captureSession) {
                captureSession.close();
                captureSession = null;
            }
            //关闭摄像头
            if (null != cameraDevice) {
                cameraDevice.close();
                cameraDevice = null;
            }
            //关闭图像阅读器
            if (null != imageReader) {
                imageReader.close();
                imageReader = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static class MainObject {
        //是用弱引用，防止内部类默认隐式持有外部类对象引用的问题
        private WeakReference<MainActivity> reference;

        public MainObject(MainActivity activity) {
            reference = new WeakReference<MainActivity>(activity);
        }

        /**
         * 拍照，并保存到指定的位置
         *
         * @return
         */
        public Bitmap takePicture() {
            return reference.get().getBitmap();
        }
    }

    private Bitmap getBitmap() {
        Bitmap bitmap = null;
        bitmap = BitmapUtil.scaleBitmap(preview.getBitmap(), CameraPreWith, CameraPreHight);
        return bitmap;
    }

    /**
     * 监听Back键按下事件,方法2:
     * 注意:
     * 返回值表示:是否能完全处理该事件
     * 在此处返回false,所以会继续传播该事件.
     * 在具体项目中此处的返回值视情况而定.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            bgToast.myToast(this,"退出请按“HOME键",0,200);
            return false;//拦截下来
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }
}
