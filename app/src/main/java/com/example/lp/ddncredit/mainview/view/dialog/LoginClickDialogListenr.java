package com.example.lp.ddncredit.mainview.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.lp.ddncredit.R;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.lp.ddncredit.utils.NavigationBarUtil.hideNavigationBar;

/**
 * 登录框
 * lp
 * 2019/06/04
 */

public class LoginClickDialogListenr implements View.OnClickListener {
    private static final String TAG = "LoginClickDialogListenr";
    private AlertDialog mAlertDialog;
    private Context mContext;
    private EditText mEdtPassWd;
    private Button mSureBtn;
    private Button mCancelBtn;
    private Window window;

    private LoginResultListenr mLoginResultListenr;

    public interface LoginResultListenr {
        boolean loginResult(int i);
    }

    public LoginClickDialogListenr(Context mContext, LoginResultListenr loginResultListenr) {
        this.mContext = mContext;
        mLoginResultListenr = loginResultListenr;
    }


    public AlertDialog showAlertDialog() {
        Log.i(TAG, "showAlertDialog: ");
        //创建dialog 这种方式
        mAlertDialog = new AlertDialog.Builder(mContext).create();
        LayoutInflater factory = LayoutInflater.from(mContext);
        View textEntryView = factory.inflate(R.layout.login_dialog, null);
        mAlertDialog.setView(textEntryView);
        mAlertDialog.show();//必须在setConentView之前调用
        mAlertDialog.setCanceledOnTouchOutside(false);
        mAlertDialog.setCancelable(false);
        // 解决EditText无法弹出软键盘
        mAlertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        //加载布局
        window = mAlertDialog.getWindow();
        window.setContentView(R.layout.login_dialog);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mEdtPassWd = (EditText) window.findViewById(R.id.et_passwd_input);
        //隐藏密码
        mEdtPassWd.setTransformationMethod(new PasswordTransformationMethod());
        mEdtPassWd.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mEdtPassWd.setTransformationMethod(PasswordTransformationMethod.getInstance());

        InputMethodManager inputManager = (InputMethodManager) mEdtPassWd.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(mEdtPassWd, 0);

        mSureBtn = (Button) window.findViewById(R.id.btn_sure);
        mCancelBtn = (Button) window.findViewById(R.id.btn_cancel);
        mSureBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);
        return mAlertDialog;
    }

    @Override
    public void onClick(View view) {
        Log.i(TAG, "===onClick==" + view.getId());
        Log.i(TAG, "R.id.ib_set: "+R.id.ib_set);

        switch (view.getId()) {
            case R.id.btn_sure:
                Log.i(TAG, "===onClick=1=");
                if ((mEdtPassWd.getText().toString().equals("8888"))) {
                    mLoginResultListenr.loginResult(1);//正确
                    dissmissDialog();
                } else {
                    mLoginResultListenr.loginResult(0);//错误
                }
                break;
            case R.id.btn_cancel:
                Log.i(TAG, "===onClick=2=");
                dissmissDialog();
                mLoginResultListenr.loginResult(2);//退出，隐藏导航栏
                break;
            case R.id.ib_set:
               if(mLoginResultListenr.loginResult(3)){//判断是否弹出密码框
                   Log.i(TAG, "弹出密码框: ");
                   showAlertDialog();
                   hideNavigationBar(window);
                   StartTimer();
               }
                break;
        }
    }

    private Timer loginTimer;
    private TimerTask timerTask;

    private void StartTimer() {
        loginTimer = null;
        timerTask = null;
        loginTimer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                mAlertDialog.dismiss();
                mLoginResultListenr.loginResult(2);//退出，隐藏导航栏
            }
        };
        loginTimer.schedule(timerTask, 10000);//十秒钟之后执行
    }

    private void StopTimer() {
        loginTimer.cancel();
        timerTask.cancel();
        loginTimer = null;
        timerTask = null;
    }

    private void dissmissDialog() {
        mAlertDialog.dismiss();
        StopTimer();
    }
}
