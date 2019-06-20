package com.example.lp.ddncredit.mainview.view.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import android.widget.TextView;

import com.example.lp.ddncredit.Myapplication;
import com.example.lp.ddncredit.R;
import com.example.lp.ddncredit.mainview.view.AttendGridView;
import com.example.lp.ddncredit.mainview.view.adapter.AttendGridAdapter;
import com.example.lp.ddncredit.mainview.view.adapter.AttendShowBean;

import java.util.Timer;
import java.util.TimerTask;

public class AttendDialog {
    private AlertDialog mAlertDialog;
    private TextView tv_role, tv_babyname, tv_clazzname, tv_icnumber, tv_attendtime;
    private Button backbotton;
    private AttendGridAdapter attendGridAdapter;
    private AttendGridView attendGridView;
    private View dilogview;
    private Context context;
    private Window window;
    private static final int mDialogWith = 1280;
    private static final int mDialogHeight = 800;

    private AttendtListenr attendtListenr;

    private static AttendDialog instance;

    public static AttendDialog getInstance() {
        if(instance==null){
            instance = new AttendDialog();
        }
        return instance;
    }
    public interface AttendtListenr {
        void AttendDialogtListenr(boolean b);
    }

    public void  setAttendtListenr(AttendtListenr attendtListenr) {
        this.attendtListenr = attendtListenr;
    }
    /**
     * 选择对话框
     */
    public AlertDialog showChoiceDialog(AttendShowBean attendBean, Context context) {
        this.context = context.getApplicationContext();
        if (dilogview == null && window == null) {
            //获取到DialogView()，获取到dialogView，并设置一些属性
            dilogview = getDialogView();//
            //初始化Dialog，绑定布局文件
            initDialogView(context, dilogview);

            ShowDilog();//展示出dilog

            //获取到dilog的Window，以便设置视图内容和设置窗口属性
            window = SetDialogWindow(window);
            //初始化和添加内容到视图中
            initLeftDataView(window);//左侧栏
            //设置左侧栏的内容
            setLeftData(attendBean);
            //设置右侧GridVeiw的内容
            initGridView(attendBean, window);
        } else {
            //已经完成了初始化和显示，只需要更新GirdView中的显示内容
            ShowDilog();//展示出dilog
            notifyData(attendBean);
        }
        return mAlertDialog;
    }

    private void setLeftData(AttendShowBean attendBean) {
        tv_role.setText("角色："+attendBean.getRelation());
        tv_babyname.setText("宝宝："+attendBean.getBabyname());
        tv_clazzname.setText("班级："+attendBean.getClazzname());
        tv_icnumber.setText("IC："+attendBean.getIcnumber());
        tv_attendtime.setText("时间："+attendBean.getAttendtime());
    }

    private Window SetDialogWindow(Window window) {
        window = mAlertDialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = mDialogWith;
        params.height = mDialogHeight;
        window.setAttributes(params);
        window.setContentView(R.layout.fragment_attend);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return window;
    }


    private View getDialogView() {
        LayoutInflater factory = LayoutInflater.from(context);
        View view = factory.inflate(R.layout.fragment_attend, null);
        return view;
    }


    private AlertDialog initDialogView(Context context, View view) {
        mAlertDialog = new AlertDialog.Builder(context).create();
        mAlertDialog.setCanceledOnTouchOutside(false);
        mAlertDialog.setCancelable(false);
       /* mAlertDialog.setView(view);*/
        return mAlertDialog;
    }


    private void notifyData(AttendShowBean attendBean) {
        timerCount=5;//延长时间
        attendGridAdapter.notifiData(attendBean);
        tv_role.setText("角色："+attendBean.getRelation());
        tv_babyname.setText("宝宝："+attendBean.getBabyname());
        tv_clazzname.setText("班级："+attendBean.getClazzname());
        tv_icnumber.setText("IC："+attendBean.getIcnumber());
        tv_attendtime.setText("时间："+attendBean.getAttendtime());

    }

    private void initLeftDataView(Window window) {
        tv_role = window.findViewById(R.id.tv_Role);
        tv_babyname = window.findViewById(R.id.tv_babyname);
        tv_clazzname = window.findViewById(R.id.tv_clazzname);
        tv_icnumber = window.findViewById(R.id.tv_icnumber);
        tv_attendtime = window.findViewById(R.id.tv_attendtime);

        backbotton = window.findViewById(R.id.bt_attendback);

        backbotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回ExpressionFragment
                dissmissDilog();

            }
        });
    }

    private void initGridView(AttendShowBean attendBean, Window window) {
        attendGridView = window.findViewById(R.id.grid_photoview);
        attendGridView.setNumColumns(3);
        attendGridAdapter = new AttendGridAdapter(attendBean, window.getContext(), attendGridView);
        attendGridView.setAdapter(attendGridAdapter);
    }

    private void ShowDilog() {
        if (mAlertDialog != null) {
            mAlertDialog.show();
        }
        attendtListenr.AttendDialogtListenr(true);
        StartTimer();//开始计时自动关闭dilog
    }

    private void dissmissDilog() {
        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
        }
        attendtListenr.AttendDialogtListenr(false);
        StopTimer();
    }

    private Timer loginTimer;
    private TimerTask timerTask;
    private int timerCount=5;

    private void StartTimer() {
        if(loginTimer!=null&&timerTask!=null){
            StopTimer();
        }
        loginTimer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if(timerCount>0){
                    backbotton.post(new Runnable() {
                        @Override
                        public void run() {
                            backbotton.setText(timerCount+"s后返回");
                        }
                    });
                    timerCount--;
                }else {
                    dissmissDilog();//退出，并回收timer
                }
            }
        };
        loginTimer.schedule(timerTask, 1000,1000);//每1秒钟执行
    }

    private void StopTimer() {
        loginTimer.cancel();
        timerTask.cancel();
        loginTimer = null;
        timerTask = null;
        timerCount=5;
    }


}