package com.example.lp.ddncredit.mainview.view.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lp.ddncredit.MainActivity;
import com.example.lp.ddncredit.Myapplication;
import com.example.lp.ddncredit.R;
import com.example.lp.ddncredit.mainview.view.AttendGridView;
import com.example.lp.ddncredit.mainview.view.adapter.AttendGridAdapter;
import com.example.lp.ddncredit.mainview.view.adapter.AttendShowBean;

public class AttendDialog {
    private AlertDialog mAlertDialog;
    private TextView tv_role, tv_babyname, tv_clazzname, tv_icnumber, tv_attendtime;
    private Button backbotton;
    private AttendGridAdapter attendGridAdapter;
    private AttendGridView attendGridView;
    private View dilogview;
    private RelativeLayout releteveMain;
    private Context context;
    private  Window window;

    private static AttendDialog instance = new AttendDialog();

    public static AttendDialog getInstance() {
        return instance;
    }

    /**
     * 选择对话框
     */
    public AlertDialog showChoiceDialog(AttendShowBean attendBean, Context context) {
        this.context = context;
        if (mAlertDialog != null) {//正在，着刷新数据
            notifyData(attendBean, true);
        } else {//展示出dilog
            dilogview = initView();//初始化dilog
            mAlertDialog.setView(dilogview);
            mAlertDialog.show();//展示出dilog
            notifyData(attendBean, false);
            attendGridView = (AttendGridView) dilogview.findViewById(R.id.grid_photoview);
            attendGridView.setNumColumns(3);
            attendGridAdapter = new AttendGridAdapter(attendBean, dilogview.getContext(), attendGridView);
            attendGridView.setAdapter(attendGridAdapter);

             window = mAlertDialog.getWindow();
            /* window.getDecorView().setPadding(0, 0, 0, 0);*/
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = 1280;
            params.height = 800;
            window.setAttributes(params);
        }

        hideBottomUIMenu();
        return mAlertDialog;
    }

    private void notifyData(AttendShowBean attendBean, boolean b) {
        if (b) {//已经弹出dlog
            attendGridAdapter.notifiData(attendBean);
            tv_role.setText(attendBean.getRelation());
            tv_babyname.setText(attendBean.getBabyname());
            tv_clazzname.setText(attendBean.getClazzname());
            tv_icnumber.setText(attendBean.getIcnumber());
            tv_attendtime.setText(attendBean.getAttendtime());
        } else {//还没有弹出dilog
            tv_role.setText(attendBean.getRelation());
            tv_babyname.setText(attendBean.getBabyname());
            tv_clazzname.setText(attendBean.getClazzname());
            tv_icnumber.setText(attendBean.getIcnumber());
            tv_attendtime.setText(attendBean.getAttendtime());
        }


    }

    private View initView() {
        mAlertDialog = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View view = factory.inflate(R.layout.fragment_attend, null);
        View viewMain = factory.inflate(R.layout.activity_main, null);
       /* releteveMain =viewMain.findViewById(R.id.rel_main);
        releteveMain.addView(view);
        releteveMain.removeView(view);*/



        mAlertDialog.setCanceledOnTouchOutside(false);
        mAlertDialog.setCancelable(false);

        tv_role = (TextView) view.findViewById(R.id.tv_Role);
        tv_babyname = (TextView) view.findViewById(R.id.tv_babyname);
        tv_clazzname = (TextView) view.findViewById(R.id.tv_clazzname);
        tv_icnumber = (TextView) view.findViewById(R.id.tv_icnumber);
        tv_attendtime = (TextView) view.findViewById(R.id.tv_attendtime);

        backbotton = (Button) view.findViewById(R.id.bt_attendback);

        backbotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回ExpressionFragment
                dissmissDilog();


            }
        });


        return view;
    }

    public void dissmissDilog() {
        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }
        hideBottomUIMenu();
    }
    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        Activity mActivity=(Activity)context;
        Window window= mActivity.getWindow();
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = window.getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = window.getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

}
