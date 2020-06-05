package com.example.lp.ddncredit.mainview.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.TextView;

import com.example.lp.ddncredit.R;
import com.example.lp.ddncredit.mainview.view.AttendGridView;
import com.example.lp.ddncredit.mainview.view.adapter.AttendGridAdapter;
import com.example.lp.ddncredit.mainview.view.adapter.AttendShowBean;
import com.example.lp.ddncredit.utils.ScreeUtils;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.lp.ddncredit.utils.NavigationBarUtil.hideNavigationBar;
import static com.example.lp.ddncredit.utils.ScreeUtils.getDisplayMetrics;

public class AttendDialog {
    private AlertDialog mAlertDialog;
    private TextView tv_role, tv_babyname, tv_clazzname, tv_icnumber, tv_attendtime, tv_attendpoint;
    private ImageView iv_now_picture;
    private Button backbotton;
    private AttendGridAdapter attendGridAdapter;
    private AttendGridView attendGridView;
    private View dilogview;
    private Context context;
    private Window window;

    private Bitmap nowBitmap=null;
    private static AttendDialog instance;

    public static AttendDialog getInstance() {
        if (instance == null) {
            instance = new AttendDialog();
        }
        return instance;
    }

    /**
     * 选择对话框
     */
    public AlertDialog showChoiceDialog(AttendShowBean attendBean, Bitmap bitmap, Context context) {
        this.context = context.getApplicationContext();
        nowBitmap=bitmap;
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
        if(nowBitmap!=null){
            iv_now_picture.setImageBitmap(nowBitmap);
        }
        if (attendBean.getRelation().equals("老师")) {
            tv_role.setText("角色：" + attendBean.getRelation());
            tv_babyname.setText("姓名：" + attendBean.getBabyname());
            tv_clazzname.setText("IC：" + attendBean.getIcnumber());
            tv_icnumber.setText("时间：" + attendBean.getAttendtime());
            tv_attendpoint.setText(R.string.attendteacherpointphoto);
            tv_attendtime.setVisibility(View.INVISIBLE);
        } else {
            tv_attendtime.setVisibility(View.VISIBLE);
            tv_role.setText("角色：" + attendBean.getRelation());
            tv_babyname.setText("宝宝：" + attendBean.getBabyname());
            tv_clazzname.setText("班级：" + attendBean.getClazzname());
            tv_icnumber.setText("IC：" + attendBean.getIcnumber());
            tv_attendtime.setText("时间：" + attendBean.getAttendtime());
            tv_attendpoint.setText(R.string.attendpointphoto);
        }

    }

    private Window SetDialogWindow(Window window) {
        window = mAlertDialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
//        params.width = (int) (getDisplayMetrics().x* ScreeUtils.Xscale);
//        params.height = (int) (getDisplayMetrics().y*ScreeUtils.Yscale);
        params.width = 1280;
        params.height = 800;
        window.setAttributes(params);
        window.setContentView(R.layout.attend_dialog);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        hideNavigationBar(window);//隐藏底部状态栏
        return window;
    }


    private View getDialogView() {
        LayoutInflater factory = LayoutInflater.from(context);
        View view = factory.inflate(R.layout.attend_dialog, null);
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
        timerCount = 5;//延长时间
        attendGridAdapter.notifiData(attendBean);
        setLeftData(attendBean);
    }

    private void initLeftDataView(Window window) {
        iv_now_picture = window.findViewById(R.id.iv_now_picture);
        tv_role = window.findViewById(R.id.tv_Role);
        tv_babyname = window.findViewById(R.id.tv_babyname);
        tv_clazzname = window.findViewById(R.id.tv_clazzname);
        tv_icnumber = window.findViewById(R.id.tv_icnumber);
        tv_attendtime = window.findViewById(R.id.tv_attendtime);
        tv_attendpoint = window.findViewById(R.id.tv_attendpoint);

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

        StartTimer();//开始计时自动关闭dilog
    }

    private void dissmissDilog() {
        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
        }
        StopTimer();
    }

    private Timer loginTimer;
    private TimerTask timerTask;
    private int timerCount = 5;

    private void StartTimer() {
        if (loginTimer != null && timerTask != null) {
            StopTimer();
        }
        loginTimer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (timerCount > 0) {
                    backbotton.post(new Runnable() {
                        @Override
                        public void run() {
                            backbotton.setText(timerCount + "s后返回");
                        }
                    });
                    timerCount--;
                } else {
                    dissmissDilog();//退出，并回收timer
                }
            }
        };
        loginTimer.schedule(timerTask, 1000, 1000);//每1秒钟执行
    }

    private void StopTimer() {
        loginTimer.cancel();
        timerTask.cancel();
        loginTimer = null;
        timerTask = null;
        timerCount = 5;
    }


}
